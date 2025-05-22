package dev.fealtous.minorconvenience.dungeons;

import dev.fealtous.minorconvenience.Config;
import dev.fealtous.minorconvenience.dungeons.secrets.RoomScanner;
import dev.fealtous.minorconvenience.utils.RegexUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.LayeredDraw;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraftforge.client.event.SystemMessageReceivedEvent;

import static dev.fealtous.minorconvenience.utils.LocatorUtil.isDungeons;
import static dev.fealtous.minorconvenience.Config.getXOff;
import static dev.fealtous.minorconvenience.Config.getYOff;
public class DungeonMapRenderer {
    private static final Minecraft mc = Minecraft.getInstance();
    static final int HOTBAR_LAST = 8;
    public static void dungeonChat(SystemMessageReceivedEvent e) {
        if (isDungeons()) {
            var msg = e.getMessage().getString();
            var match = RegexUtils.cataPlayerPattern.matcher(msg);
            if (match.find()) {
                RoomScanner.init();
            }
        }
    }

    public static final LayeredDraw.Layer mapOverlay = (guiGraphics, partialTick) -> {
        var mapItemstack = mc.player.inventoryMenu.slots.get(45).container.getItem(HOTBAR_LAST);
        if (!mapItemstack.is(Items.MAP)) return;
        var item = mapItemstack.getItem();
        if (item instanceof MapItem) {
            var pose = guiGraphics.pose();
            var mapid = mapItemstack.get(DataComponents.MAP_ID);
            var mapdata = MapItem.getSavedData(mapItemstack, mc.level);
            if (mapdata == null) return;
            float scale = (float) Config.mapScale;
            pose.pushPose();
            pose.translate(getXOff(5, Config.leftMapOffset), getYOff(5, Config.topMapOffset), 1);
            pose.scale(scale,scale,-10f);
            var buffer = guiGraphics.getBufferSource();
            var state = new MapRenderState();
            mc.getMapRenderer().extractRenderState(mapid, mapdata, state);
            mc.getMapRenderer().render(
                    state,
                    pose,
                    buffer,
                    false,
                    mc.getEntityRenderDispatcher().getPackedLightCoords(mc.player, partialTick.getRealtimeDeltaTicks()));
            pose.popPose();
        }
    };
}
