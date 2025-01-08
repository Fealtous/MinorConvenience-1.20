package dev.fealtous.minorconvenience.convenience;

import dev.fealtous.minorconvenience.Config;
import dev.fealtous.minorconvenience.utils.Location;
import dev.fealtous.minorconvenience.utils.LocatorUtil;
import dev.fealtous.minorconvenience.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;

import java.awt.Color;

import static dev.fealtous.minorconvenience.Config.getXOff;
import static dev.fealtous.minorconvenience.Config.getYOff;
public class BasicInfoOverlay {
    static Minecraft mc = Minecraft.getInstance();
    public static String petInfo = "<Not Detected Yet>";
    public static void basicInfoOverlay(RegisterGuiOverlaysEvent e) {
        IGuiOverlay overlay = (gui, guiGraphics, partialTick, screenWidth, screenHeight) -> {
            RenderUtils.renderText(guiGraphics, String.format("%.2f, %.2f", mc.player.getX(), mc.player.getZ()), 5, 5);
            RenderUtils.renderText(guiGraphics, petInfo, getXOff(5, Config.petLeft), getYOff(5, Config.petTop), Color.ORANGE);
        };
        e.registerAbove(VanillaGuiOverlay.VIGNETTE.id(), "mcvc_basic_info", overlay);
    }
    public static void cancelPotionOverlay(RenderGuiOverlayEvent e) {
        if (e.getOverlay().id().equals(VanillaGuiOverlay.POTION_ICONS.id())) {
            e.setCanceled(true);
        }
    }
}
