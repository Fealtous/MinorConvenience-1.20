package dev.fealtous.minorconvenience.dungeons.secrets;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec2;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RoomScanner {
    private static Minecraft mc = Minecraft.getInstance();
    private static int x = 0, z = 0; // Define x, y as > 0, indicating room index from left/top
    private static final Map<Pair<Integer, Integer>, RoomDefinition> roomCache = new HashMap<>();
    private static RoomDefinition currentRoom = RoomDefinition.EMPTY;
    private static int xStart = 0, zStart = 0;
    private static int centering = 5;
    private static final int ROOM_SIZE = 32;
    private static final int HEIGHT = 68;
    public static void init() {
        roomCache.clear();
    }
    public static void update() {
        identifyRoom();
    }
    public static Vec2 getIndices() {
        return new Vec2(x,z);
    }
    /**
     * Get x,y indices of room given current dungeon layout.
     * @return false if in the same room as last check.
     */
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
        if (!transformToIndices()) return; // Check if we're in the same room we were previously
        var room = roomCache.get(new Pair<>(x, z)); // Check if this is a room we've visited in the dungeon
        if (room == null) {
            var currentIdentifier = genIdentifier(scan());
            var opt = RoomDefinition.getIfKnown(currentIdentifier);
            if (opt != null) {
                // Room is known, check alignment and see if we need to add parent rooms.
                currentRoom = opt;
                roomCache.put(new Pair<>(x,z), opt);
                // todo alignment shit
            } else {
                Minecraft.getInstance().gui.getChat().addMessage(Component.literal("New Room: " + currentIdentifier));
                var iden = genIdentifier(scanNorth());
            }
        } else {
            currentRoom = room; // Previously visited, don't do any scanning.
        }
    }
    public static Pair<Long, Long> getIdentifiers() {
        return new Pair<>(genIdentifier(scan()), genIdentifier(scanNorth()));
    }
    private static long genIdentifier(Map<String, Integer> map) {
        List<String> list = new ArrayList<>(map.keySet());
        list.sort(String::compareTo);
        list.forEach((x) -> mc.gui.getChat().addMessage(Component.literal(x + ": " + (map.get(x) & 0xff))));
        long res = 0;
        for (String s: list) {
            res ^= map.get(s) & 0xff;
            res = res << 8;
        }
        return res;
    }
    private static Map<String, Integer> scan() {
        var lvl = mc.level;
        Map<String, Integer> blockmap = new HashMap<>();
        for (int xit = xStart + centering; xit < xStart + ROOM_SIZE - centering; xit++) {
            for (int zit = zStart + centering; zit < zStart + ROOM_SIZE - centering; zit++) {
                var pos = new BlockPos(xit, HEIGHT, zit);
                var state = lvl.getBlockState(pos).getBlock().getName().getString();
                blockmap.merge(state, 1, Integer::sum);
            }
        }
        return blockmap;
    }
    private static Map<String, Integer> scanNorth() {
        int baseZ = zStart + (ROOM_SIZE / 2) - centering + 2;
        int baseX = xStart + (ROOM_SIZE / 2) - centering + 2;
        int scanLength = 8;
        int offset = 4;
        var lvl = mc.level;
        Map<String, Integer> blockmap = new HashMap<>();
        for (int xit = baseX; xit < baseX + 6; xit++) {
            for (int zit = baseZ; zit > baseZ - scanLength - offset; zit--) {
                var pos = new BlockPos(xit, HEIGHT, zit);
                var state = lvl.getBlockState(pos).getBlock().getName().getString();
                blockmap.merge(state, 1, Integer::sum);
            }
        }
        return blockmap;
    }
    private static Alignment needsRotationFrom() {
        int baseZ = zStart + (ROOM_SIZE / 2) - centering + 2;
        int baseX = xStart + (ROOM_SIZE / 2) - centering + 2;
        int scanLength = 8;
        int offset = 4;
        var lvl = mc.level;
        Map<String, Integer> northScan = new HashMap<>();
        Map<String, Integer> westScan = new HashMap<>();
        Map<String, Integer> eastScan = new HashMap<>();
        // west
        for (int zit = baseZ; zit < baseZ + 6; zit++) {
            for (int xit = baseX; xit > baseX - scanLength - offset; xit--) {
                var pos = new BlockPos(xit, HEIGHT, zit);
                var state = lvl.getBlockState(pos).getBlock().getName().getString();
                westScan.merge(state, 1, Integer::sum);
            }
        }
        // north
        for (int xit = baseX; xit < baseX + 6; xit++) {
            for (int zit = baseZ; zit > baseZ - scanLength - offset; zit--) {
                var pos = new BlockPos(xit, HEIGHT, zit);
                var state = lvl.getBlockState(pos).getBlock().getName().getString();
                northScan.merge(state, 1, Integer::sum);
            }
        }
        // east
        for (int zit = baseZ + 5; zit >= baseZ; zit--) {
            for (int xit = baseX + centering; xit < baseX + centering + scanLength + offset; xit++) {
                var pos = new BlockPos(xit, HEIGHT, zit);
                var state = lvl.getBlockState(pos).getBlock().getName().getString();
                eastScan.merge(state, 1, Integer::sum);
            }
        }
        if (currentRoom.matchesAlignment(genIdentifier(northScan))) {
            return Alignment.NORTH;
        } else if (currentRoom.matchesAlignment(genIdentifier(westScan))) {
            return Alignment.WEST;
        } else if (currentRoom.matchesAlignment(genIdentifier(eastScan))) {
            return Alignment.EAST;
        } else return Alignment.SOUTH;
    }
    enum Alignment {
        NORTH,
        SOUTH,
        EAST,
        WEST
    }
    //skull emoji
//    public static void debugRenderer(RenderLevelStageEvent e) {
//        if (e.getStage() == RenderLevelStageEvent.Stage.AFTER_WEATHER) {
//            var pose = e.getPoseStack();
//            var matrix = e.getProjectionMatrix();
//            // Room scanning indication
//            VertexBuffer vBuff = new VertexBuffer(VertexBuffer.Usage.STATIC);
//            Tesselator tesselator = Tesselator.getInstance();
//            BufferBuilder buffer = tesselator.getBuilder();
//            buffer.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
//            for (int xit = xStart + centering; xit < xStart + ROOM_SIZE - centering; xit++) {
//                for (int zit = zStart + centering; zit < zStart + ROOM_SIZE - centering; zit++) {
//                    RenderUtils.renderBox(buffer, xit, HEIGHT, zit);
//                }
//            }
//
//            int baseZ = zStart + (ROOM_SIZE / 2) - centering + 2;
//            int baseX = xStart + (ROOM_SIZE / 2) - centering + 2;
//            int scanLength = 8;
//            int offset = 4;
//            // west
//            RenderUtils.setRenderColor(0xff00ff);
//            for (int zit = baseZ; zit < baseZ + 6; zit++) {
//                for (int xit = baseX; xit > baseX - scanLength - offset; xit--) {
//                    RenderUtils.renderBox(buffer, xit, HEIGHT, zit);
//                }
//            }
//            RenderUtils.setRenderColor(0x00ffff);
//            // north
//            for (int xit = baseX; xit < baseX + 6; xit++) {
//                for (int zit = baseZ; zit > baseZ - scanLength - offset; zit--) {
//                    RenderUtils.renderBox(buffer, xit, HEIGHT, zit);
//                }
//            }
//            RenderUtils.setRenderColor(0x00ff00);
//            // east
//            for (int zit = baseZ + 5; zit >= baseZ; zit--) {
//                for (int xit = baseX + centering; xit < baseX + centering + scanLength + offset; xit++) {
//                    RenderUtils.renderBox(buffer, xit, HEIGHT, zit);
//                }
//            }
//            RenderUtils.resetRenderColor();
//
//            vBuff.bind();
//            vBuff.upload(buffer.end());
//            VertexBuffer.unbind();
//
//            GL11.glEnable(GL11.GL_BLEND);
//            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
//            GL11.glEnable(GL11.GL_LINE_SMOOTH);
//            GL11.glDisable(GL11.GL_DEPTH_TEST);
//            RenderSystem.setShader(GameRenderer::getPositionColorShader);
//            pose.pushPose();
//            RenderUtils.renderRelativeToPlayer(pose, mc.cameraEntity);
//            vBuff.bind();
//            vBuff.drawWithShader(pose.last().pose(), new Matrix4f(matrix), RenderSystem.getShader());
//            VertexBuffer.unbind();
//            pose.popPose();
//            GL11.glEnable(GL11.GL_DEPTH_TEST);
//            GL11.glDisable(GL11.GL_BLEND);
//            GL11.glDisable(GL11.GL_LINE_SMOOTH);
//        }
//    }
}
