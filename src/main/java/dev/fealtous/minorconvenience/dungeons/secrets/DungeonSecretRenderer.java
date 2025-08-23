package dev.fealtous.minorconvenience.dungeons.secrets;

import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import oshi.util.tuples.Pair;

import java.util.ArrayList;
import java.util.List;

public class DungeonSecretRenderer {
    private static Minecraft mc = Minecraft.getInstance();
    static List<Pair<BlockPos, Integer>> points = new ArrayList<>();

    public static void updatePoints(List<Pair<BlockPos, Integer>> points) {
        DungeonSecretRenderer.points = points;
    }
// Skull emoji
//    @SubscribeEvent
//    public static void renderSecrets(RenderLevelStageEvent e) {
//        if (e.getStage() == RenderLevelStageEvent.Stage.AFTER_WEATHER) {
//            var pose = e.getPoseStack();
//            var matrix = e.getProjectionMatrix();
//            VertexBuffer vBuff = new VertexBuffer(VertexBuffer.Usage.STATIC);
//            Tesselator tesselator = Tesselator.getInstance();
//            BufferBuilder buffer = tesselator.getBuilder();
//            buffer.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
//            // Render code here
//            for (Pair<BlockPos, Integer> point : points) {
//                RenderUtils.setRenderColor(point.getB());
//                RenderUtils.renderBox(buffer, point.getA());
//                RenderUtils.resetRenderColor();
//            }
//            vBuff.bind();
//            vBuff.upload(buffer.end());
//            VertexBuffer.unbind();
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
