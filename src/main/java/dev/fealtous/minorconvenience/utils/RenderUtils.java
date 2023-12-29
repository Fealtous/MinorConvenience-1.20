package dev.fealtous.minorconvenience.utils;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;

public class RenderUtils {
    public static final int COLOR_STANDARD = 0xe0555555;
    public static void renderRelativeToPlayer(PoseStack ps, Entity p) {
        ps.translate(-p.getX(), -p.getY()-1.5, -p.getZ());
    }
    public static void renderBoxAround(PoseStack ps, Entity e, VertexConsumer vc, int pRed, int pGreen, int pBlue, int pAlpha) {

        LevelRenderer.renderLineBox(ps, vc, e.getBoundingBox(), pRed, pGreen, pBlue, pAlpha);

    }

    public static final int HIGHLIGHT = 0x99000099;
    public static void slotHighlight(GuiGraphics gg, ContainerScreen gui, int xi, int yi, int color) {
        try {

            int x = gui.getGuiLeft()+xi;
            int y = gui.getGuiTop()+yi;
            gg.fill(RenderType.gui(), x-1, y-1, x + 17, y + 17, 0xe0099099);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
