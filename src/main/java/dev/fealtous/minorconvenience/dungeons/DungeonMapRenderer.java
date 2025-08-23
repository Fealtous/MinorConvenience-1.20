package dev.fealtous.minorconvenience.dungeons;

import dev.fealtous.minorconvenience.Config;
import dev.fealtous.minorconvenience.MinorConvenience;
import dev.fealtous.minorconvenience.dungeons.secrets.RoomScanner;
import dev.fealtous.minorconvenience.utils.RegexUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.state.MapRenderState;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.MapItem;
import net.minecraftforge.client.event.AddGuiOverlayLayersEvent;
import net.minecraftforge.client.event.SystemMessageReceivedEvent;
import net.minecraftforge.client.gui.overlay.ForgeLayeredDraw;
import net.minecraftforge.client.gui.overlay.ForgeLayer;
import net.minecraftforge.eventbus.api.bus.BusGroup;
import org.joml.Matrix4f;

import static dev.fealtous.minorconvenience.utils.LocatorUtil.isDungeons;
import static dev.fealtous.minorconvenience.Config.getXOff;
import static dev.fealtous.minorconvenience.Config.getYOff;
public class DungeonMapRenderer {
    private static final Minecraft mc = Minecraft.getInstance();
    static final int HOTBAR_LAST = 8;

    private static final ResourceLocation mapRl = ResourceLocation.fromNamespaceAndPath(MinorConvenience.MODID, "dungeon_map");

    public static void init(BusGroup busGroup) {
        SystemMessageReceivedEvent.BUS.addListener(DungeonMapRenderer::dungeonChat);
        AddGuiOverlayLayersEvent.getBus(busGroup).addListener(DungeonMapRenderer::overlay);
    }

    public static void dungeonChat(SystemMessageReceivedEvent e) {
        if (isDungeons()) {
            var msg = e.getMessage().getString();
            var match = RegexUtils.cataPlayerPattern.matcher(msg);
            if (match.find()) {
                RoomScanner.init();
            }
        }
    }

    public static void overlay(AddGuiOverlayLayersEvent evt) {
        evt.getLayeredDraw().addAbove(mapRl, ForgeLayeredDraw.SLEEP_OVERLAY, mapOverlay);
    }

    public static final ForgeLayer mapOverlay = (guiGraphics, partialTick) -> {
        var mapItemstack = mc.player.getInventory().getNonEquipmentItems().get(8);
        if (!mapItemstack.is(Items.FILLED_MAP)) return;
        var pose = guiGraphics.pose();
        var mapid = mapItemstack.get(DataComponents.MAP_ID);
        var mapdata = MapItem.getSavedData(mapItemstack, mc.level);
        if (mapdata == null) return;
        float scale = (float) Config.mapScale;

//            pose.pushMatrix();
//
//            pose.translate(getXOff(5, Config.leftMapOffset), getYOff(5, Config.topMapOffset));
//            pose.scale(scale,scale);
        var state = new MapRenderState();
        mc.getMapRenderer().extractRenderState(mapid, mapdata, state);
        state.decorations.forEach((decoration) -> decoration.renderOnFrame = true);
        guiGraphics.submitMapRenderState(state);
    };
}
