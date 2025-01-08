package dev.fealtous.minorconvenience.dungeons.secrets;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import dev.fealtous.minorconvenience.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.client.gui.overlay.GuiOverlayManager;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;
import oshi.util.tuples.Pair;

import java.awt.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DungeonSecretRenderer {
    private static Minecraft mc = Minecraft.getInstance();
    static List<Pair<BlockPos, Integer>> points = new ArrayList<>();

    public static void updatePoints(List<Pair<BlockPos, Integer>> points) {
        DungeonSecretRenderer.points = points;
    }

    @SubscribeEvent
    public static void renderSecrets(RenderLevelStageEvent e) {
        if (e.getStage() == RenderLevelStageEvent.Stage.AFTER_WEATHER) {
            var pose = e.getPoseStack();
            var matrix = e.getProjectionMatrix();
            VertexBuffer vBuff = new VertexBuffer(VertexBuffer.Usage.STATIC);
            Tesselator tesselator = Tesselator.getInstance();
            BufferBuilder buffer = tesselator.getBuilder();
            buffer.begin(VertexFormat.Mode.DEBUG_LINES, DefaultVertexFormat.POSITION_COLOR);
            // Render code here
            for (Pair<BlockPos, Integer> point : points) {
                RenderUtils.setRenderColor(point.getB());
                RenderUtils.renderBox(buffer, point.getA());
                RenderUtils.resetRenderColor();
            }
            vBuff.bind();
            vBuff.upload(buffer.end());
            VertexBuffer.unbind();
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            GL11.glEnable(GL11.GL_LINE_SMOOTH);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            RenderSystem.setShader(GameRenderer::getPositionColorShader);
            pose.pushPose();
            RenderUtils.renderRelativeToPlayer(pose, mc.cameraEntity);
            vBuff.bind();
            vBuff.drawWithShader(pose.last().pose(), new Matrix4f(matrix), RenderSystem.getShader());
            VertexBuffer.unbind();
            pose.popPose();
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_LINE_SMOOTH);
        }
    }
}
