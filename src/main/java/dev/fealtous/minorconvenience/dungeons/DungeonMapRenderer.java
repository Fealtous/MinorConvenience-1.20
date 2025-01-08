package dev.fealtous.minorconvenience.dungeons;

import dev.fealtous.minorconvenience.Config;
import dev.fealtous.minorconvenience.dungeons.secrets.RoomScanner;
import dev.fealtous.minorconvenience.utils.RegexUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.MapItem;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RegisterGuiOverlaysEvent;
import net.minecraftforge.client.gui.overlay.ForgeGui;
import net.minecraftforge.client.gui.overlay.IGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;

import static dev.fealtous.minorconvenience.utils.LocatorUtil.isDungeons;
import static dev.fealtous.minorconvenience.Config.getXOff;
import static dev.fealtous.minorconvenience.Config.getYOff;
public class DungeonMapRenderer {
    static final int HOTBAR_LAST = 8;
    static final MapGui guiMap = new MapGui();
    public static void dungeonChat(ClientChatReceivedEvent.System e) {
        if (e.isOverlay()) return;
        if (isDungeons()) {
            var msg = e.getMessage().getString();
            var match = RegexUtils.cataPlayerPattern.matcher(msg);
            if (match.find()) {
                RoomScanner.init();
            }
        }
    }
    public static void mapRender(RegisterGuiOverlaysEvent e) {
        e.registerAbove(VanillaGuiOverlay.VIGNETTE.id(), "mcvc_dungeon_map", guiMap);
    }

    static class MapGui implements IGuiOverlay {
        private static final Minecraft mc = Minecraft.getInstance();
        @Override
        public void render(ForgeGui gui, GuiGraphics guiGraphics, float partialTick, int screenWidth, int screenHeight) {
            if (!isDungeons()) return;
            var mapItemstack = ((Inventory) mc.player.inventoryMenu.slots.get(45).container).items.get(HOTBAR_LAST);
            var mapItem = mapItemstack.getItem();
            if (mapItem instanceof MapItem) {
                gui.setupOverlayRenderState(true, true);
                var pose = guiGraphics.pose();
                Integer mapId = MapItem.getMapId(mapItemstack);
                if (mapId == null) return;
                var mapdata = MapItem.getSavedData(mapId, mc.level);
                if (mapdata == null) return;
                float scale = (float) Config.mapScale;
                pose.pushPose();
                pose.translate(getXOff(5, Config.leftMapOffset), getYOff(5, Config.topMapOffset), 1);
                pose.scale(scale,scale,-10f);
                var buffer = guiGraphics.bufferSource();
                mc.gameRenderer.getMapRenderer().render(pose,
                        buffer,
                        mapId,
                        mapdata,
                        false,
                        mc.getEntityRenderDispatcher().getPackedLightCoords(mc.player, partialTick));
                pose.popPose();
            }
        }
    }
}
