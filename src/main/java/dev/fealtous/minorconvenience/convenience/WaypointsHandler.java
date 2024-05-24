package dev.fealtous.minorconvenience.convenience;

import dev.fealtous.minorconvenience.utils.LocatorUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.BreakingItemParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ItemParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.particles.VibrationParticleOption;
import net.minecraftforge.client.event.RenderLevelStageEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import oshi.util.tuples.Pair;

import java.util.ArrayList;

public class WaypointsHandler {
    private static ArrayList<Waypoint> waypoints = new ArrayList<>();
    private static ArrayList<Boolean> enabledWPs = new ArrayList<>();
    public WaypointsHandler() {
    }
    @SubscribeEvent
    public static void renderWaypoints(RenderLevelStageEvent e) {
//        for (int i = 0; i < waypoints.size(); i++) {
//            if (!enabledWPs.get(i)) {
//                var pos = waypoints.get(i).pos;
//                Minecraft.getInstance().level.addParticle(
//                        ParticleTypes.SMOKE,
//                        pos.getX(),pos.getY(),pos.getZ(),0,0, 0);
//            }
//        }

    }

    public static void addWP(BlockPos pos, String label) {
        //waypoints.add(new Waypoint(pos, label, LocatorUtil.LOCATIONS.HUB));
        Minecraft.getInstance().level.addParticle(
                ParticleTypes.SMOKE,
                pos.getX(),pos.getY(),pos.getZ(),0,0, 0);
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
    LocatorUtil.LOCATIONS area;

    public Waypoint(BlockPos pos, String label, LocatorUtil.LOCATIONS loc) {
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
