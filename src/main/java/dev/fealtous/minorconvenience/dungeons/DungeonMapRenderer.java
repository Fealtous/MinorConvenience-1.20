package dev.fealtous.minorconvenience.dungeons;

import com.mojang.math.Axis;
import dev.fealtous.minorconvenience.utils.RegexUtils;
import dev.fealtous.minorconvenience.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.PlayerFaceRenderer;
import net.minecraft.client.player.AbstractClientPlayer;
import net.minecraft.client.resources.PlayerSkin;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.MapItem;
import net.minecraft.world.level.material.MapColor;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.NamedGuiOverlay;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static net.minecraftforge.client.gui.overlay.VanillaGuiOverlay.VIGNETTE;
import static dev.fealtous.minorconvenience.utils.LocatorUtil.isDungeons;

public class DungeonMapRenderer {
    static Minecraft mc = Minecraft.getInstance();
    static Room size = Room.EMPTY;
    static final int HOTBAR_LAST = 8;

    @SubscribeEvent
    public static void dungeonChat(ClientChatReceivedEvent.System e) {
        if (e.isOverlay()) return;
        if (isDungeons()) {
            var msg = e.getMessage().getString();
            var match = RegexUtils.cataPlayerPattern.matcher(msg);
            if (match.find()) {
                size = Room.EMPTY;
                DungeonRoomIdentifier.init();
                return;
            }

        }
    }

    @SubscribeEvent
    public static void mapRender(RenderGuiOverlayEvent.Post e) {
        if (!isDungeons()) return;
        var mapItemstack = ((Inventory) Minecraft.getInstance().player.inventoryMenu.slots.get(45).container).items.get(HOTBAR_LAST);
        var mapItem = mapItemstack.getItem();
        if (mapItem instanceof MapItem) {

            var mc = Minecraft.getInstance();
            var buffer = mc.renderBuffers().bufferSource();
            var pose = e.getGuiGraphics().pose();
            Integer mapId = MapItem.getMapId(mapItemstack);
            if (mapId == null) return;
            var mapdata = MapItem.getSavedData(mapId, mc.level);
            if (mapdata == null) return;

            double screenRelationX = 960.0 / 1920.0;
            double translationX = screenRelationX * mc.getWindow().getWidth();
            double offsetLeft = 5; // todo all the configs
            double offsetTop = 5 + 200;
            double screenRelationY = 0.5f;
            float scale = 1.5f; // todo add config scaling

            if (isGuiPhase(e.getOverlay(), VIGNETTE)) {
                pose.pushPose();
                pose.translate(offsetLeft, offsetTop, 0);
                //
                mc.gameRenderer.getMapRenderer().render(pose,
                        buffer,
                        mapId,
                        mapdata,
                        true,
                        mc.getEntityRenderDispatcher().getPackedLightCoords(mc.player, e.getPartialTick()));
                // Normalize world-space
                pose.pushPose();
                var x = (mc.player.getX() + size.x) / (size.x);
                var z = (mc.player.getZ() + size.z) / (size.z);
                // Then apply map-space conversion, offset to the center of the screen-map.
                pose.translate(64 + x* size.xm, 64 + z*size.zm, 0);
                pose.mulPose(Axis.ZP.rotationDegrees(mc.player.getYRot() + 180));
                PlayerFaceRenderer.draw(e.getGuiGraphics(), mc.player.getSkin(), -4, -4, 8);
                pose.popPose();
                pose.popPose();
            }
        }
    }
    private static final int SAFE_ROOM = 0xff007c00;
    private static Supplier<TimerTask> findmap = () -> new TimerTask() {
        @Override
        public void run() {
            var mapItemstack = ((Inventory) mc.player.inventoryMenu.slots.get(45).container).items.get(HOTBAR_LAST);
            var mapItem = mapItemstack.getItem();
            if (mapItem instanceof MapItem) {
                Integer mapId = MapItem.getMapId(mapItemstack);
                if (mapId == null) return;
                var map = MapItem.getSavedData(mapId, mc.level).colors;
                int safeX = 0, safeY = 0;
                for (int i = 0; i < map.length; i++) {
                    // Locate top left corner of safe room.
                    if (MapColor.getColorFromPackedId(map[i]) == SAFE_ROOM) {
                        safeX = i % 128;
                        safeY = i / 128;
                        break;
                    }
                }
                int width = alignmentPoint(safeX);
                int height = alignmentPoint(safeY);

                switch (width) {
                    case 4 -> {
                        switch (height) {
                            case 4 -> size = Room.SIZE_4x4;
                            case 5 -> size = Room.SIZE_4x5;
                            default -> size = Room.EMPTY;
                        }
                    }
                    case 5 -> {
                        switch (height) {
                            case 4 -> size = Room.SIZE_5x4;
                            case 5 -> size = Room.SIZE_5x5;
                            case 6 -> size = Room.SIZE_5x6;
                            default -> size = Room.EMPTY;
                        }
                    }
                    case 6 -> {
                        switch (height) {
                            case 5 -> size = Room.SIZE_6x5;
                            case 6 -> size = Room.SIZE_6x6;
                            default -> size = Room.EMPTY;
                        }
                    }
                    default -> size = Room.EMPTY;
                }
            }
        }};
    public static void setMapSize() {
        new Timer().schedule(findmap.get(), 75);
    }

    private static int alignmentPoint(int point) {
        if (point % 22 == 0) {
            return 4; // Hits on 4x5 width
        } else if (point % 11 == 0 && point % 2 == 1) {
            return 5; // Hits on 4x5 and 5x5, but not 6x5
        } else if (point % 10 == 6) {
            return 5; // Hits on 6x5
        } else if ((point - 15) % 10 == 0) {
            return 6;
        } else {
            return 0;
        }
    }

    public static void alert() {
        if (!isDungeons()) return;
        if (size == Room.EMPTY) {
            setMapSize();
        }
    }
    private static boolean isGuiPhase(NamedGuiOverlay evt, VanillaGuiOverlay stage) {
        return evt.id().equals(stage.id());
    }

    enum Room {
        // These represent the center of the room...
        SIZE_4x4(136,136, 92., 92.), // 140, 133
        SIZE_4x5(140,118, 92., 84.), // 140, 116
        SIZE_5x4(118,140, 84., 92.), // 116, 140
        SIZE_5x5(120.5,120.5, 84., 84.), // 123, 116
        SIZE_5x6(122,102, 76., 64.), // 122, 102
        SIZE_6x5(102,122, 76., 64.), // 102 122
        SIZE_6x6(105,105, 64., 64.), // 105, 105
        EMPTY(1,1, 1, 1);
        double x;
        double z;
        double xm;
        double zm;
        Room(double offsetx, double offsetz, double xmult, double zmult) {
            x = offsetx;
            z = offsetz;
            xm = xmult;
            zm = zmult;
        }
    }
}
