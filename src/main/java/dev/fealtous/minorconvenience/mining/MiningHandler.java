package dev.fealtous.minorconvenience.mining;


import dev.fealtous.minorconvenience.utils.Location;
import dev.fealtous.minorconvenience.utils.LocatorUtil;
import dev.fealtous.minorconvenience.utils.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.Vec2;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.gui.overlay.GuiOverlayManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import static dev.fealtous.minorconvenience.utils.RegexUtils.keeperPattern;

import java.awt.*;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;

public class MiningHandler {
    private static final String DIAMOND = "diamond";
    private static final String EMERALD = "emerald";
    private static final String LAPIS = "lapis";
    private static final String GOLD = "gold";
    private static Minecraft mc = Minecraft.getInstance();
    private static boolean initFlag = false;
    private static Vec2 center = Vec2.ZERO;
    private static Vec2 solution = Vec2.ZERO;
    private static KEEPER npcOffset = KEEPER.NONE;
    private static float lastRadius = 0;
    private static List<Vec2> offsets = Arrays.asList(
            new Vec2(-38, 26),
            new Vec2(38, 26),
            new Vec2(-40, 18),
            new Vec2(-41, 22),
            new Vec2(-5, 16),
            new Vec2(40, -30),
            new Vec2(-42, -28),
            new Vec2(-43, -40),
            new Vec2(42, -41),
            new Vec2(43, -16),
            new Vec2(-1, -20),
            new Vec2(6, 28),
            new Vec2(7, 11),
            new Vec2(7, 22),
            new Vec2(-12, -44),
            new Vec2(12, 31),
            new Vec2(12, -22),
            new Vec2(12, 7),
            new Vec2(12, -43),
            new Vec2(-14,43),
            new Vec2(-14, 22),
            new Vec2(-17, 20),
            new Vec2(-20, 0),
            new Vec2(1, 20),
            new Vec2(19, 29),
            new Vec2(20, 0),
            new Vec2(20, 0),
            new Vec2(20, -26),
            new Vec2(-23, 40),
            new Vec2(22, -14),
            new Vec2(-24, 12),
            new Vec2(23, 26),
            new Vec2(23, -39),
            new Vec2(24, 27),
            new Vec2(25, 17),
            new Vec2(29, -44),
            new Vec2(-31, -12),
            new Vec2(-31, -40),
            new Vec2(30, -25),
            new Vec2(-32, -40),
            new Vec2(-36, 42),
            new Vec2(-37, -14),
            new Vec2(-37, -22)
    );
    private static int[] filter = new int[offsets.size()];

    public static void init() {
        initFlag = true;
        for (Entity entity : mc.level.entitiesForRendering()) {
            if (entity instanceof ArmorStand stand) {
                String name;
                if (stand.getDisplayName() == null) continue; // Shouldn't happen but w/e
                name = stand.getDisplayName().getString().replaceAll("[^a-zA-Z]","").toLowerCase();
                Matcher matcher = keeperPattern.matcher(name);
                if (matcher.find()) {
                    switch (matcher.group()) {
                        case DIAMOND -> npcOffset = KEEPER.DIAMOND;
                        case GOLD -> npcOffset = KEEPER.GOLD;
                        case LAPIS -> npcOffset = KEEPER.LAPIS;
                        case EMERALD -> npcOffset = KEEPER.EMERALD;
                    }
                    // Center is calculated as (x,z) + (xOffset, zOffset)
                    center = toVec2(stand.position().add(new Vec3(npcOffset.x(), 0, npcOffset.z())));
                    break; // Found a keeper, no need to continue iterating
                }
            }
        }
    }
    public static void deInit() {
        initFlag = false;
        npcOffset = KEEPER.NONE;
        center = Vec2.ZERO;
        solution = Vec2.ZERO;
        filter = new int[offsets.size()];
    }

    public static void refresh() {
        filter = new int[offsets.size()];
        solution = offsets.get(0);
    }

    public static void push(float radius) {
        if (!initFlag || radius > lastRadius) return;
        if (radius > 5) lastRadius = radius;
        var pos = toVec2(mc.player.position()); // Get player position
        var posRelative = pos.add(center.negated());
        var best = offsets.get(0);
        var radOfBest = Math.sqrt(best.distanceToSqr(posRelative));
        for (Vec2 offset : offsets) {
            var dist = Math.sqrt(offset.distanceToSqr(posRelative)); // Distance from me to this offset
            if (Math.abs(dist - radius) < Math.abs(radOfBest - radius)) {
                best = offset;
                radOfBest = dist;
            }
        }
        mc.gui.getChat().addMessage(Component.literal(String.valueOf(radOfBest - radius)));
        if (radOfBest - radius > Math.log(radius)) return;

        solution = best;

    }
    private static Vec2 toVec2(Vec3 i) {
        return new Vec2(((float) i.x), ((float) i.z));
    }

    private static Vec3 toVec3(Vec2 i) {
        return new Vec3(i.x, mc.player.position().y, i.y);
    }

    @SubscribeEvent
    public static void renderPoint(RenderLevelStageEvent e) {
        if (!isDivan() || initFlag) return;
        if (!mc.player.getMainHandItem().getDisplayName().getString().contains("Metal Detector")) return;
        if (e.getStage().equals(RenderLevelStageEvent.Stage.AFTER_CUTOUT_BLOCKS)) {
            var pose = e.getPoseStack();
            var camera = e.getCamera().getPosition();
            var buffers = mc.renderBuffers();
            var bufSource = buffers.bufferSource();

            pose.pushPose();
            pose.translate(-camera.x, -camera.y, -camera.z);
            var matrix = pose.last().pose();
            var consumer = bufSource.getBuffer(RenderType.lines());
            RenderUtils.definePoint(consumer, matrix, mc.player.position().add(0, .25, 0));
            RenderUtils.definePoint(consumer, matrix, toVec3(center.add(solution)));
            bufSource.endBatch();
            pose.popPose();
        }
    }

    @SubscribeEvent
    public static void renderTextOverlay(RenderGuiOverlayEvent.Post e) {

        //if (!isDivan()) return;
        if (!e.getOverlay().equals(GuiOverlayManager.getOverlays().get(0))) return;
        var buffer = mc.renderBuffers().bufferSource();
        var pose = e.getGuiGraphics().pose();
        pose.pushPose();
        var m4f = pose.last().pose();
        var consumer = buffer.getBuffer(RenderType.textBackground());
        e.getGuiGraphics().drawString(mc.font, LocatorUtil.whereAmI().name(), 5, 5, Color.BLACK.getRGB(), false);
        //e.getGuiGraphics().drawString(mc.font, String.format("x:%f  z:%f", solution.x, solution.y), 5, 5, Color.BLACK.getRGB(), false);
        //e.getGuiGraphics().drawString(mc.font, String.format("x:%f  z:%f", mc.player.position().x, mc.player.position().z), 5, 15, Color.BLACK.getRGB(), false);

        buffer.endBatch();
        pose.popPose();
    }

    private static boolean isDivan() {
        return LocatorUtil.whereAmI().equals(Location.DIVAN);
    }
}
enum KEEPER {

    GOLD(3, -33),
    LAPIS(-33, -3),
    EMERALD(-3, 33),
    DIAMOND(33, 3),
    NONE(0,0);
    private final int x, z;
    KEEPER(int x, int z) {
        this.x = x;
        this.z = z;
    }
    public int x() {return x;}
    public int z() {return z;}
}
