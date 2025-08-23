package dev.fealtous.minorconvenience.dungeons;

import dev.fealtous.minorconvenience.utils.InventoryHelper;
import dev.fealtous.minorconvenience.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import oshi.util.tuples.Pair;

import java.util.*;

import static dev.fealtous.minorconvenience.utils.LocatorUtil.isDungeons;
import static dev.fealtous.minorconvenience.utils.RegexUtils.blazeHealthPattern;

public class DungeonsHandler {
    private static final Map<String, Double> scalarMap = new HashMap<>();
    private static final Minecraft mc = Minecraft.getInstance();
    private static LinkedList<Entity> blazes = new LinkedList<>();

    public static void init() {
        LivingDeathEvent.BUS.addListener(DungeonsHandler::onBlazeDeath);
        ScreenEvent.Render.Post.BUS.addListener(DungeonsHandler::terminalSolver);
        ClientChatReceivedEvent.BUS.addListener(DungeonsHandler::threeWeirdos);

    }

    public static void alert() {
        if (isDungeons()) {
            var b = getBlazes();
            for (Blaze blaze : b) {
                if (blaze.position().distanceTo(mc.player.position()) < 30) {
                    blazes.clear();
                    registerBlazes(b);
                    break;
                }
            }
            //RoomScanner.identifyRoom();
        }
    }
// Needs frame pass event
//    @SubscribeEvent
//    public static void renderBlazeBounding(RenderLevelStageEvent e) {
//        if (!blazes.isEmpty() && isDungeons()) {
//            if (e.getStage().equals(RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS)) {
//                var ps = e.getPoseStack();
//                Player p = mc.player;
//                ps.pushPose();
//                RenderUtils.renderRelativeToPlayer(ps, p);
//                MultiBufferSource.BufferSource rb = mc.renderBuffers().bufferSource();
//                VertexConsumer vc = rb.getBuffer(RenderType.LINES);
//                try {
//                    if (blazes.size() <= 0) return;
//                    if (blazes.size() != 1) {
//                        LevelRenderer.renderLineBox(ps, vc, blazes.getFirst().getBoundingBox(), 1, 0, 0, 1);
//                        LevelRenderer.renderLineBox(ps, vc, blazes.getLast().getBoundingBox(), 0, 1, 0, 1);
//                    } else {
//                        LevelRenderer.renderLineBox(ps, vc, blazes.get(0).getBoundingBox(), 1, 1, 1, 1);
//                    }
//                } catch (Exception e1) {
//                    blazes.clear();
//                }
//                ps.popPose();
//                rb.endBatch(RenderType.LINES);
//
//            }
//        }
//    }

    @SubscribeEvent
    public static void onBlazeDeath(LivingDeathEvent e) {
        blazes.remove(e.getEntity());
    }


    public static void registerBlazes(List<Blaze> availableBlazes) {
        if (!isDungeons()) return;
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
            var blaze1 = blazeHealthPattern.matcher(b1.getB().getDisplayName().getString());
            var blaze2 = blazeHealthPattern.matcher(b2.getB().getDisplayName().getString());
            if (blaze1.find() && blaze2.find()) {
                String s1 = blaze1.group().replaceAll("[^0-9]", "");
                String s2 = blaze2.group().replaceAll("[^0-9]", "");
                hp1 = Integer.parseInt(s1);
                hp2 = Integer.parseInt(s2);
                if (hp1 > hp2) {
                    return 1;
                } else {
                    return -1;
                }
            } return 0;
        });
        tmpBlazes.forEach((x) -> blazes.add(x.getA()));
    }

    final static String [] weirdoYes = {"The reward is not in my chest!", "At least one of them is lying, and the reward is not in",
            "My chest doesn't have the reward. We are all telling the truth", "My chest has the reward and I'm telling the truth",
            "The reward isn't in any of our chests", "Both of them are telling the truth."};
    @SubscribeEvent
    public static void threeWeirdos(ClientChatReceivedEvent e) {
        if (isDungeons()) {
            String message = e.getMessage().getString().replaceAll("[^a-zA-Z,.!'\\s]","");
            if (message.contains("NPC")) {
                for (String s : weirdoYes) {
                    if (message.contains(s)) {
                        MutableComponent component = (MutableComponent) e.getMessage();
                        component.setStyle(e.getMessage().getStyle().withColor(0x00ff00));
                        component.getSiblings().forEach((i) -> {
                            ((MutableComponent) i).setStyle(e.getMessage().getStyle().withColor(0x00ff00));
                        });
                        e.setMessage(component);
                        break;
                    }
                }
            }
        }
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

    private static List<Blaze> getBlazes() {
        var availableBlazes = new LinkedList<Blaze>();
        mc.level.entitiesForRendering().forEach((x) -> {
            if (x instanceof Blaze && !blazes.contains(x)) availableBlazes.add((Blaze) x);
        });
        return availableBlazes;
    }
    public static void resetBlazes() {
        blazes.clear();
    }
}
