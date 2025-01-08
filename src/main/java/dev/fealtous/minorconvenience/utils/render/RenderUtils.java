package dev.fealtous.minorconvenience.utils.render;

import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.Collection;

public class RenderUtils {
    private static final Minecraft mc = Minecraft.getInstance();
    public static int RENDER_RED = 0xff; // Leave public if you want to do it manaully.
    public static int RENDER_GREEN = 0xff;
    public static int RENDER_BLUE = 0xff;
    public static void setRenderColor(int color) {
        RENDER_BLUE = color & 0xff;
        RENDER_GREEN = (color >> 8) & 0xff;
        RENDER_RED = (color >> 16) & 0xff;
    }
    public static void resetRenderColor() {
        RENDER_RED = 0xff; RENDER_GREEN = 0xff; RENDER_BLUE = 0xff;
    }
    public static void renderRelativeToPlayer(PoseStack ps, Entity p) {
        ps.translate(-p.getX(), -p.getY()-mc.player.getEyeHeight(), -p.getZ());
    }

    public static final int HIGHLIGHT = 0x99007733;
    public static void slotHighlight(GuiGraphics gg, ContainerScreen gui, int xi, int yi, int color) {
        try {
            int x = gui.getGuiLeft()+xi;
            int y = gui.getGuiTop()+yi;
            gg.fill(RenderType.guiOverlay(), x-1, y-1, x + 17, y + 17, color);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void definePoint(VertexConsumer consumer, Matrix4f matrix, Vec3 p) {
        consumer.vertex(matrix, ((float) p.x), ((float) p.y), ((float) p.z)).color(0f,1f,0f,1f).normal(0,1,0).endVertex();
    }

    public static void renderList(PoseStack p, GuiGraphics gg, Collection<String> strings) {
        int y = 5;
        p.pushPose();
        for (String string : strings) {
            gg.drawString(mc.font, string, 5, y, Color.WHITE.getRGB(), true);
            y+=15;

        }
        p.popPose();
    }
    public static void renderBox(BufferBuilder buffer, BlockPos pos) {
        renderBox(buffer, pos.getX(), pos.getY(), pos.getZ());
    }
    public static void renderBox(BufferBuilder buffer, int x, int y, int z) {
        // Bottom
        buffer.vertex(x, y, z).color(RENDER_RED, RENDER_GREEN, RENDER_BLUE, 0xff).endVertex();
        buffer.vertex(x+1, y, z).color(RENDER_RED, RENDER_GREEN, RENDER_BLUE, 0xff).endVertex();

        buffer.vertex(x, y, z+1).color(RENDER_RED, RENDER_GREEN, RENDER_BLUE, 0xff).endVertex();
        buffer.vertex(x+1, y, z+1).color(RENDER_RED, RENDER_GREEN, RENDER_BLUE, 0xff).endVertex();

        buffer.vertex(x+1, y, z).color(RENDER_RED, RENDER_GREEN, RENDER_BLUE, 0xff).endVertex();
        buffer.vertex(x+1, y, z+1).color(RENDER_RED, RENDER_GREEN, RENDER_BLUE, 0xff).endVertex();

        buffer.vertex(x, y, z+1).color(RENDER_RED, RENDER_GREEN, RENDER_BLUE, 0xff).endVertex();
        buffer.vertex(x, y, z).color(RENDER_RED, RENDER_GREEN, RENDER_BLUE, 0xff).endVertex();
        // Top
        buffer.vertex(x, y+1, z).color(RENDER_RED, RENDER_GREEN, RENDER_BLUE, 0xff).endVertex();
        buffer.vertex(x+1, y+1, z).color(RENDER_RED, RENDER_GREEN, RENDER_BLUE, 0xff).endVertex();

        buffer.vertex(x, y+1, z+1).color(RENDER_RED, RENDER_GREEN, RENDER_BLUE, 0xff).endVertex();
        buffer.vertex(x+1, y+1, z+1).color(RENDER_RED, RENDER_GREEN, RENDER_BLUE, 0xff).endVertex();

        buffer.vertex(x+1, y+1, z).color(RENDER_RED, RENDER_GREEN, RENDER_BLUE, 0xff).endVertex();
        buffer.vertex(x+1, y+1, z+1).color(RENDER_RED, RENDER_GREEN, RENDER_BLUE, 0xff).endVertex();

        buffer.vertex(x, y+1, z+1).color(RENDER_RED, RENDER_GREEN, RENDER_BLUE, 0xff).endVertex();
        buffer.vertex(x, y+1, z).color(RENDER_RED, RENDER_GREEN, RENDER_BLUE, 0xff).endVertex();
        // Pillars
        buffer.vertex(x, y+1, z).color(RENDER_RED, RENDER_GREEN, RENDER_BLUE, 0xff).endVertex();
        buffer.vertex(x, y, z).color(RENDER_RED, RENDER_GREEN, RENDER_BLUE, 0xff).endVertex();

        buffer.vertex(x+1, y+1, z+1).color(RENDER_RED, RENDER_GREEN, RENDER_BLUE, 0xff).endVertex();
        buffer.vertex(x+1, y, z+1).color(RENDER_RED, RENDER_GREEN, RENDER_BLUE, 0xff).endVertex();

        buffer.vertex(x+1, y+1, z).color(RENDER_RED, RENDER_GREEN, RENDER_BLUE, 0xff).endVertex();
        buffer.vertex(x+1, y, z).color(RENDER_RED, RENDER_GREEN, RENDER_BLUE, 0xff).endVertex();

        buffer.vertex(x, y+1, z+1).color(RENDER_RED, RENDER_GREEN, RENDER_BLUE, 0xff).endVertex();
        buffer.vertex(x, y, z+1).color(RENDER_RED, RENDER_GREEN, RENDER_BLUE, 0xff).endVertex();
    }

    public static void renderText(GuiGraphics gg, String text, int x, int y, Color color) {
        gg.drawString(mc.font, text, x, y, color.getRGB(), true);
    }
    public static void renderText(GuiGraphics gg, String text, int x, int y) {
        renderText(gg, text, x, y, Color.WHITE);
    }
}
