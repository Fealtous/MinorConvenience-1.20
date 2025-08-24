package dev.fealtous.minorconvenience;

import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.gui.overlay.ForgeLayer;

public class TestRender {

    private static final Minecraft mc = Minecraft.getInstance();
    public static final ResourceLocation rl = ResourceLocation.fromNamespaceAndPath(MinorConvenience.MODID, "test");
    public static final ForgeLayer overlay = (guiGraphics, deltaTracker) -> {

        var val = mc.player.getViewVector(0f);
        Vec3 vecA = new Vec3(0,0,0);
        Vec3 vecB = mc.player.position().subtract(vecA); // When |vecA| = 0, vecB is only matter
        val = vecB.cross(val);
        int height = guiGraphics.guiHeight() / 2;
        int width = guiGraphics.guiWidth() / 2;
        guiGraphics.fill(width - 5,height - 5, width + 5, height + 5, 0x50f0f0f0);



        String output = String.format("%.2f %.2f %.2f", val.x, val.y, val.z);
        guiGraphics.drawString(mc.font, output, 30, 30, 0xffffffff);

    };
}
