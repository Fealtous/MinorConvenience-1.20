package dev.fealtous.minorconvenience.dungeons;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.logging.LogUtils;
import dev.fealtous.minorconvenience.utils.InventoryHelper;
import dev.fealtous.minorconvenience.utils.LocatorUtil;
import dev.fealtous.minorconvenience.utils.RenderUtils;
import dev.fealtous.minorconvenience.utils.TextUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.monster.Blaze;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.Slot;
import net.minecraftforge.client.event.RenderGuiEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.ArrowNockEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import oshi.util.tuples.Pair;

import java.util.LinkedList;
import java.util.List;

public class DungeonsHandler {
    private static int ticks = 0;
    private static final Minecraft mc = Minecraft.getInstance();
    private static Entity correct;
    private static boolean solved = false;
    private static LinkedList<Entity> blazes = new LinkedList<>();
    private static List<Entity> availableBlazes = new LinkedList<>();


    @SubscribeEvent
    public static void passiveChecker(TickEvent.ClientTickEvent e) {
        if (mc.level == null || ticks++ % 10 != 0) return;
        for (String name : TextUtils.getPlayerNameList()) {
            if (name.matches("Higher Or Lower \\[.]")) {

                LogUtils.getLogger().info(name);
            }
        }
        if (ticks % 20 == 0 && isDungeons()) {
            availableBlazes = new LinkedList<>();
            Minecraft.getInstance().level.entitiesForRendering().forEach((x) -> {
                if (x instanceof Blaze) availableBlazes.add(x);
            });
            registerBlazes();
        }
        if (solved) correct = null;

    }
    @SubscribeEvent
    public static void renderBlazeBounding(RenderLevelStageEvent e) {
        if (!blazes.isEmpty() && isDungeons()) {
            if (e.getStage().equals(RenderLevelStageEvent.Stage.AFTER_SOLID_BLOCKS)) {
                PoseStack ps = e.getPoseStack();
                Player p = mc.player;
                if (p != null) {
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
    }
    private static boolean isDungeons() {
        return LocatorUtil.whereAmI() == LocatorUtil.LOCATIONS.CATACOMBS;
    }

    @SubscribeEvent
    public static void chestRender(RenderGuiEvent.Post e) {
        var currentScreen = Minecraft.getInstance().screen;
        if (!InventoryHelper.isChestScreen(currentScreen)) return;
        List<Slot> slots = InventoryHelper.chestInventory(currentScreen);
        slots.forEach((x) -> {
            RenderUtils.slotHighlight(e.getGuiGraphics(), (ContainerScreen) currentScreen,
                    x.x, x.y, RenderUtils.COLOR_STANDARD);
        });
    }

    public static void registerBlazes() {
        if (!isDungeons()) return;
        Minecraft mc = Minecraft.getInstance();
        var tmpBlazes = new LinkedList<Pair<Entity, Entity>>();
        //10 blazes per puzzle
        for (Entity entity : availableBlazes) {
            if (entity instanceof Blaze) {
                List<Entity> nearby = mc.level.getEntities(entity, entity.getBoundingBox().inflate(1.25));
                if (!nearby.isEmpty()) {
                    tmpBlazes.add(new Pair<>(entity, nearby.get(0)));
                }
            }
        }
        tmpBlazes.sort((b1, b2) -> {
            int hp1, hp2;
            String[] s1 = b1.getB().getDisplayName().getString().split(" ");
            String[] s2 = b2.getB().getDisplayName().getString().split(" ");
            hp1 = Integer.parseInt(s1[s1.length-1].split("/")[0]);
            hp2 = Integer.parseInt(s2[s2.length-1].split("/")[0]);
            if (hp1 > hp2) {
                return 1;
            }
            return hp1 == hp2 ? 0 : -1;
        });
        tmpBlazes.forEach((x) -> blazes.add(x.getA())
        );
    }
}
