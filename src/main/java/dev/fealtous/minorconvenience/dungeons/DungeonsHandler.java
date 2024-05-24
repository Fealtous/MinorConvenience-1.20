package dev.fealtous.minorconvenience.dungeons;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.fealtous.minorconvenience.utils.InventoryHelper;
import dev.fealtous.minorconvenience.utils.LocatorUtil;
import dev.fealtous.minorconvenience.utils.RenderUtils;
import dev.fealtous.minorconvenience.utils.TextUtils;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.multiplayer.PlayerInfo;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.saveddata.maps.MapDecoration;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.gui.overlay.GuiOverlayManager;
import net.minecraftforge.client.gui.overlay.NamedGuiOverlay;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import oshi.util.tuples.Pair;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class DungeonsHandler {
    private static int ticks = 0;
    private static final Minecraft mc = Minecraft.getInstance();
    private static LinkedList<Entity> blazes = new LinkedList<>();
    private static boolean blazeSolverActive = false;
    private static boolean inDungeons = false;
    private static int blazeLoc = -1;
    private static String component = null;


    @SubscribeEvent
    public static void passiveChecker(TickEvent.ClientTickEvent e) {
        if (!e.phase.equals(TickEvent.Phase.START)) return;
        if (mc.level == null || ++ticks % 10 != 0) return;
        inDungeons = isDungeons();
        if (!blazeSolverActive) {
            var list = TextUtils.getPlayerNameList();
            for (int i = 0; i < list.size(); i++) {
                if (isDungeons() && list.get(i).matches(".*Higher.*")) {
                    blazeSolverActive = true;
                    blazeLoc = i;
                    component = list.get(i);
                    break;
                } else {
                    if (!blazes.isEmpty()) blazes.clear();
                }
            }
        }
        if (blazeSolverActive && ticks % 30 == 0) {
            if (blazes.isEmpty() && TextUtils.getPlayerNameList().get(blazeLoc).equals(component)) registerBlazes(getBlazes());
            else blazeSolverActive = false;
        }

    }

    @SubscribeEvent
    public static void renderBlazeBounding(RenderLevelStageEvent e) {
        if (!blazes.isEmpty() && inDungeons) {
            if (e.getStage().equals(RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS)) {
                PoseStack ps = e.getPoseStack();
                Player p = mc.player;
                ps.pushPose();
                RenderUtils.renderRelativeToPlayer(ps, p);
                MultiBufferSource.BufferSource rb = mc.renderBuffers().bufferSource();
                VertexConsumer vc = rb.getBuffer(RenderType.LINES);
                if (blazes.size() != 1) {
                    LevelRenderer.renderLineBox(ps, vc, blazes.getFirst().getBoundingBox(), 1, 0, 0, 1);
                    LevelRenderer.renderLineBox(ps, vc, blazes.getLast().getBoundingBox(), 0, 1, 0, 1);
                } else {
                    LevelRenderer.renderLineBox(ps, vc, blazes.get(0).getBoundingBox(), 1, 1, 1, 1);
                }
                ps.popPose();
                rb.endBatch(RenderType.LINES);

            }
        }
    }

    @SubscribeEvent
    public static void onBlazeDeath(LivingDeathEvent e) {
        blazes.remove(e.getEntity());
    }
    private static boolean isDungeons() {
        return LocatorUtil.whereAmI() == LocatorUtil.LOCATIONS.CATACOMBS;
    }



    public static void registerBlazes(List<Blaze> availableBlazes) {
        if (inDungeons) return;
        Minecraft mc = Minecraft.getInstance();
        var tmpBlazes = new LinkedList<Pair<Entity, Entity>>();
        //10 blazes per puzzle
        for (Entity entity : availableBlazes) {
            List<Entity> nearby = mc.level.getEntities(entity, entity.getBoundingBox().inflate(1.25));
            if (!nearby.isEmpty()) {
                tmpBlazes.add(new Pair<>(entity, nearby.get(0)));
            }
        }
        tmpBlazes.sort((b1, b2) -> {
            int hp1, hp2;
            String[] s1 = b1.getB().getDisplayName().getString().split(" ");
            String[] s2 = b2.getB().getDisplayName().getString().split(" ");
            hp1 = Integer.parseInt(s1[s1.length-1].split("/")[0].replaceAll("[^0-9]",""));
            hp2 = Integer.parseInt(s2[s2.length-1].split("/")[0].replaceAll("[^0-9]",""));
            if (hp1 > hp2) {
                return 1;
            }
            return hp1 == hp2 ? 0 : -1;
        });
        tmpBlazes.forEach((x) -> blazes.add(x.getA()));
    }

    final static String [] weirdoYes = {"The reward is not in my chest!", "At least one of them is lying, and the reward is not in",
            "My chest doesn't have the reward. We are all telling the truth", "My chest has the reward and I'm telling the truth",
            "The reward isn't in any of our chests", "Both of them are telling the truth."};
    @SubscribeEvent
    public static void threeWierdosSolver(ClientChatReceivedEvent e) {
        if (inDungeons) return;
        String message = e.getMessage().getString().replaceAll("[^a-zA-Z,.!'\\s]","");
        if (!message.contains("NPC")) return;
        for (String s : weirdoYes) {
            if (message.contains(s)) {
                MutableComponent iTextComponent = (MutableComponent) e.getMessage();
                iTextComponent.setStyle(e.getMessage().getStyle().applyFormat(ChatFormatting.GREEN));
                iTextComponent.getSiblings().forEach((i) -> {
                    ((MutableComponent) i).setStyle(e.getMessage().getStyle().applyFormat(ChatFormatting.GREEN));
                });
                e.setMessage(iTextComponent);
                break;
            }
        }
    }
    private static List<Blaze> getBlazes() {
        var availableBlazes = new LinkedList<Blaze>();
        Minecraft.getInstance().level.entitiesForRendering().forEach((x) -> {
            if (x instanceof Blaze && !blazes.contains(x)) availableBlazes.add((Blaze) x);
        });
        return availableBlazes;
    }
    public static void resetBlazes() {
        blazes.clear();
        blazeSolverActive = false;

    }

    @SubscribeEvent
    public static void terminalSolver(ScreenEvent.Render.Post e) {
        var currentScreen = e.getScreen();
        if (!InventoryHelper.isChestScreen(currentScreen)) return;
        var title = currentScreen.getTitle().getString().trim();
        if (title.contains("starts")) {
            var split = title.split("'");
            InventoryHelper.chestInventory(currentScreen).forEach((x) -> {
                if (x.hasItem() && x.getItem().getDisplayName().getString().startsWith(split[1])) {
                    RenderUtils.slotHighlight(e.getGuiGraphics(), ((ContainerScreen) currentScreen), x.x, x.y, RenderUtils.HIGHLIGHT);
                }
            });
        }
    }
    static HashMap<MapDecoration, PlayerInfo> playerHeadsToRender = new HashMap<>();
    static int tickTimer = 300;
    static final NamedGuiOverlay overlayNo = GuiOverlayManager.getOverlays().get(25);
    static final NamedGuiOverlay overlayYes = GuiOverlayManager.getOverlays().get(0);
    @SubscribeEvent
    public static void mapRender(RenderGuiOverlayEvent.Post e) {
        if (!inDungeons) {
            if (!playerHeadsToRender.isEmpty()) playerHeadsToRender.clear();
            tickTimer = 300;
            return;
        }
        if (!(e.getOverlay().equals(overlayNo) || e.getOverlay().equals(overlayYes))) return;

        var mapItemstack = ((Inventory) Minecraft.getInstance().player.inventoryMenu.slots.get(45).container).items.get(8);
        var mapItem = mapItemstack.getItem();

        if (mapItem instanceof MapItem) {
            var mc = Minecraft.getInstance();
            var buffer = mc.renderBuffers().bufferSource();
            var pose = e.getGuiGraphics().pose();
            int mapId = MapItem.getMapId(mapItemstack);
            var mapdata = MapItem.getSavedData(mapId, mc.level);

            if (tickTimer-- == 0) {
                var playerInfos = Minecraft.getInstance().player.connection.getListedOnlinePlayers();
                var players = Minecraft.getInstance().level.players();
                playerInfos.forEach((playerInfo -> {
                    for (AbstractClientPlayer player : players) {

                    }
                }));
                TextUtils.sendIngameMessage(players.get(0).getName().getString());
                tickTimer = 300;
            }

            double screenRelationX = 960.0 / 1920.0;
            double translationX = screenRelationX * mc.getWindow().getWidth();
            double screenRelationY = 0.5f;
            int offset = 5;
            float scale = 1.5f; // todo add config scaling
            if (e.getOverlay().equals(GuiOverlayManager.getOverlays().get(0))) {
                pose.pushPose();
                pose.translate(translationX - MapItem.IMAGE_WIDTH - offset, offset, 0);
                mc.gameRenderer.getMapRenderer().render(pose,
                        buffer,
                        mapId,
                        mapdata,
                        true,
                        mc.getEntityRenderDispatcher().getPackedLightCoords(mc.player, e.getPartialTick()));
                pose.popPose();
            }

            if (e.getOverlay().equals(GuiOverlayManager.getOverlays().get(25))) {
                pose.pushPose();
                playerHeadsToRender.forEach((deco, player) -> {
                    int xpos = (deco.getX() + 128) / 2;
                    int ypos = (deco.getY() + 128) / 2;
                    PlayerFaceRenderer.draw(e.getGuiGraphics(), player.getSkinLocation(), (int) (translationX - MapItem.IMAGE_WIDTH + xpos - 10), ypos - 4, 8);
                });
                pose.popPose();
            }
        }

    }

    public static void chestRender(ScreenEvent.Render.Post e) {
        var currentScreen = e.getScreen();
        if (!InventoryHelper.isChestScreen(currentScreen)) return;
        List<Slot> slots = InventoryHelper.chestInventory(currentScreen);
        slots.forEach((x) -> {
            if (x.hasItem()) RenderUtils.slotHighlight(e.getGuiGraphics(), (ContainerScreen) currentScreen,
                    x.x, x.y, RenderUtils.COLOR_STANDARD);
        });
    }
}
