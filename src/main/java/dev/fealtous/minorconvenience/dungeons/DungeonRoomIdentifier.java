package dev.fealtous.minorconvenience.dungeons;

import com.mojang.blaze3d.vertex.VertexConsumer;
import dev.fealtous.minorconvenience.utils.Location;
import dev.fealtous.minorconvenience.utils.LocatorUtil;
import dev.fealtous.minorconvenience.utils.render.RenderTypes;
import dev.fealtous.minorconvenience.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.gui.overlay.GuiOverlayManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import oshi.util.tuples.Pair;

import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class DungeonRoomIdentifier {
    private static Minecraft mc = Minecraft.getInstance();
    private static int x = 0, z = 0; // Define x, y as >0, indicating room index from left/top
    private static int xStart = 0, zStart = 0;
    private static int centering = 5;
    private static final int ROOM_SIZE = 32;
    public static int currentRoomHash = 0;
    // Map x,y -> room?
    private static final Map<Pair<Integer, Integer>, RoomDefiner> roomCache = new HashMap<>();
    private static RoomDefiner currentRoom = RoomDefiner.EMPTY;
    public static void init() {
        roomCache.clear();
        x = 0; z = 0;
    }
    private static boolean transformToIndices() {
        var pos = mc.player.blockPosition();
        int tx = (pos.getX() + 200) / ROOM_SIZE;
        int tz = (pos.getZ() + 200) / ROOM_SIZE;
        if (tx == x && tz == z) {
            return false;
        }
        x = tx;
        z = tz;
        xStart = x * ROOM_SIZE - 200;
        zStart = z * ROOM_SIZE - 200;
        return true;
    }
    public static void identifyRoom() {
        // Get floor beneath player
        if (!transformToIndices()) return; // Check if we're in the same room we were previously
        var room = roomCache.get(new Pair<>(x, z)); // Check if this is a room we've visited in the dungeon
        if (room == null) {
            var lvl = mc.level;
            int scuffedHash = 0;
            for (int xit = xStart + centering; xit < xStart + ROOM_SIZE - centering; xit++) {
                for (int zit = zStart + centering; zit < zStart + ROOM_SIZE - centering; zit++) {
                    var pos = new BlockPos(xit, 68, zit);
                    var state = lvl.getBlockState(pos);
                    scuffedHash ^= state.getBlock().getName().getString().hashCode();
                }
            }
            currentRoomHash = scuffedHash;
            var opt = RoomDefiner.tryGet(currentRoomHash);
            if (opt != null) {
                currentRoom = opt;
                roomCache.put(new Pair<>(x,z), opt);
            } else {
                Minecraft.getInstance().gui.getChat().addMessage(Component.literal("New Room: " + scuffedHash));
            }

        } else {
            currentRoom = room;
        }

    }

    @SubscribeEvent
    public static void renderSecrets(RenderLevelStageEvent e) {
        if (!e.getStage().equals(RenderLevelStageEvent.Stage.AFTER_WEATHER)) return;
        //if (!LocatorUtil.isIn(Location.CATACOMBS)) return;
        MultiBufferSource.BufferSource rb = mc.renderBuffers().bufferSource();
        VertexConsumer vc = rb.getBuffer(RenderType.lines());
        var pose = e.getPoseStack();

        // Render Secrets

        if (currentRoom != null) {
            pose.pushPose();
            RenderUtils.renderRelativeToPlayer(pose, mc.cameraEntity);
            for (POI poi : currentRoom.pois) {
                var pos = poi.getOffset();
                var xpos = pos.getX();
                var ypos = pos.getY();
                var zpos = pos.getZ();
                AABB aabb = new AABB(xpos, ypos, zpos, xpos+1, ypos+1, zpos+1);
                LevelRenderer.renderLineBox(pose, vc, aabb, 1,0, 0,1);
            }
            pose.popPose();
        }

        // Room scanning indication

        pose.pushPose();
        RenderUtils.renderRelativeToPlayer(pose, mc.player);
        for (int xit = xStart + centering; xit < xStart + ROOM_SIZE - centering; xit++) {
            for (int zit = zStart + centering; zit < zStart + ROOM_SIZE - centering; zit++) {
                AABB aabb = new AABB(xit,69 ,zit,xit+1,68,zit+1);
                LevelRenderer.renderLineBox(pose, vc, aabb, 1, 1,1, 1);
            }
        }
        pose.popPose();
    }
    public static BlockPos getRelativeCoordinates() {
        return new BlockPos(x * ROOM_SIZE - 200, (int) mc.player.getY(), z * ROOM_SIZE - 200);
    }
    @SubscribeEvent
    public static void renderTextOverlay(RenderGuiOverlayEvent.Post e) {
        if (!e.getOverlay().equals(GuiOverlayManager.getOverlays().get(0))) return;
        var buffer = mc.renderBuffers().bufferSource();
        var pose = e.getGuiGraphics().pose();
        pose.pushPose();
        var m4f = pose.last().pose();
        var consumer = buffer.getBuffer(RenderType.textBackground());
        e.getGuiGraphics().drawString(mc.font,
                String.format("DC: %d, %d", xStart,
                        zStart), 5, 25, Color.WHITE.getRGB(), true);
        buffer.endBatch();
        pose.popPose();
    }
}
