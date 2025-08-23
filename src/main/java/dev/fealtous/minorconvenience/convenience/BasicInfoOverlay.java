package dev.fealtous.minorconvenience.convenience;

import dev.fealtous.minorconvenience.Config;
import dev.fealtous.minorconvenience.utils.LocatorUtil;
import dev.fealtous.minorconvenience.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;

import java.awt.*;

import static dev.fealtous.minorconvenience.Config.getXOff;
import static dev.fealtous.minorconvenience.Config.getYOff;

public class BasicInfoOverlay {
    static Minecraft mc = Minecraft.getInstance();
    public static String petInfo = "<Not Detected Yet>";
//    public static final LayeredDraw.Layer overlay = (guiGraphics, delta) -> {
//        RenderUtils.renderText(guiGraphics, String.format("%.2f, %.2f", mc.player.getX(), mc.player.getZ()), 5, 5);
//        RenderUtils.renderText(guiGraphics, petInfo, getXOff(5, Config.petLeft), getYOff(5, Config.petTop), Color.ORANGE);
//        RenderUtils.renderText(guiGraphics, LocatorUtil.whereAmI().getName(), 5, 25);
//    };
    // Leaving here so I can remember to cancel the potion effects overlay.
//    public static void cancelPotionOverlay(RenderGuiOverlayEvent e) {
//        if (e.getOverlay().id().equals(VanillaGuiOverlay.POTION_ICONS.id())) {
//            e.setCanceled(true);
//        }
//    }
}
