package dev.fealtous.minorconvenience.utils.render;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import org.joml.Matrix4f;

import java.awt.*;
import java.util.Collection;

public class RenderUtils {
    private static final Minecraft mc = Minecraft.getInstance();
    public static final int COLOR_STANDARD = 0xe0555555;
    public static void renderRelativeToPlayer(PoseStack ps, Entity p) {
        ps.translate(-p.getX(), -p.getY()-1.5, -p.getZ());
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
}
