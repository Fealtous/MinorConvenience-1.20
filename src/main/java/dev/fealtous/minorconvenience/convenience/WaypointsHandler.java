package dev.fealtous.minorconvenience.convenience;

import dev.fealtous.minorconvenience.utils.Location;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.core.BlockPos;

import java.util.ArrayList;

public class WaypointsHandler {
    private static ArrayList<Waypoint> waypoints = new ArrayList<>();
    private static ArrayList<Boolean> enabledWPs = new ArrayList<>();
    private static final Font font = Minecraft.getInstance().font;
    public WaypointsHandler() {
    }
//    @SubscribeEvent
//    public static void renderWaypoints(RenderLevelStageEvent e) {
//        if (e.getStage() != RenderLevelStageEvent.Stage.AFTER_LEVEL) return;
//        for (Waypoint waypoint : waypoints) {
////            var ps = e.getPoseStack();
////            ps.pushPose();
////            ps.translate(0,5,0);
////            //RenderUtils.renderRelativeToPlayer(ps, Minecraft.getInstance().player);
////            ps.mulPose(Minecraft.getInstance().getEntityRenderDispatcher().cameraOrientation());
////            ps.scale(10f,10f,10f);
////            var m4f = ps.last().pose();
////            font.drawInBatch(waypoint.label, (float) (-font.width(waypoint.label)/2), 0, -1,
////                    false, m4f, Minecraft.getInstance().renderBuffers().bufferSource(),
////                    Font.DisplayMode.NORMAL, 1, 0xffffff);
////            ps.popPose();
//        }
//
//    }

    public static void addWP(BlockPos pos, String label) {
        waypoints.add(new Waypoint(pos, label, Location.HUB));
        //enabledWPs.add(false);
    }
    public static void removeWP(String label) {
        var wp = new Waypoint(label);
        int index = waypoints.indexOf(wp);
        waypoints.remove(index);
        enabledWPs.remove(index);
    }
}
class Waypoint {
    BlockPos pos;
    //todo Color
    String label;
    Location area;

    public Waypoint(BlockPos pos, String label, Location loc) {
        this.pos = pos;
        this.label = label;
        area = loc;
    }
    public Waypoint(String label) {
        this.label = label;
    }
    @Override
    public boolean equals(Object other) {
        if (other instanceof Waypoint wp) {
            return wp.label.equals(this.label);
        }
        return false;
    }
}
