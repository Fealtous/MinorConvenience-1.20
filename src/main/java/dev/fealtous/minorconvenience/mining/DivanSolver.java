package dev.fealtous.minorconvenience.mining;

import com.mojang.blaze3d.framegraph.FramePass;
import com.mojang.blaze3d.vertex.PoseStack;
import dev.fealtous.minorconvenience.Config;
import dev.fealtous.minorconvenience.convenience.chat.ChatParser;
import dev.fealtous.minorconvenience.utils.Location;
import dev.fealtous.minorconvenience.utils.LocatorUtil;
import dev.fealtous.minorconvenience.utils.render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelTargetBundle;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.decoration.ArmorStand;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.FramePassManager;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.stream.Collectors;

import static dev.fealtous.minorconvenience.utils.RegexUtils.keeperPattern;
import static dev.fealtous.minorconvenience.utils.RegexUtils.metalDetectorPattern;

public class DivanSolver {
    private static final String DIAMOND = "diamond";
    private static final String EMERALD = "emerald";
    private static final String LAPIS = "lapis";
    private static final String GOLD = "gold";
    private static Minecraft mc = Minecraft.getInstance();
    private static boolean initFlag = false;
    private static Vec3 center = Vec3.ZERO;
    private static Keeper npcOffset = Keeper.NONE;
    private static final ArrayList<Vec3> offsets = new ArrayList<>();

    public static void init() {
        initFlag = true;
        for (Entity entity : mc.level.entitiesForRendering()) {
            if (entity instanceof ArmorStand stand) {
                String name;
                if (stand.getDisplayName() == null) continue; // Shouldn't happen but w/e
                name = stand.getDisplayName().getString().replaceAll("[^a-zA-Z]","").toLowerCase();
                Matcher matcher = keeperPattern.matcher(name);
                if (matcher.find()) {
                    switch (matcher.group()) {
                        case DIAMOND -> npcOffset = Keeper.DIAMOND;
                        case GOLD -> npcOffset = Keeper.GOLD;
                        case LAPIS -> npcOffset = Keeper.LAPIS;
                        case EMERALD -> npcOffset = Keeper.EMERALD;
                    }
                    // Center is calculated as (x,z) + (xOffset, zOffset)
                    center = stand.position().add(new Vec3(npcOffset.x(), 0 , npcOffset.z()));
                    return; // Found a Keepers, no need to continue iterating
                }
            }
        }
        initFlag = false; // Didn't find a Keepers, try again later.
    }
    public static void deInit() {
        initFlag = false;
        npcOffset = Keeper.NONE;
        center = Vec3.ZERO;
    }

    public static void refresh() {
        lastRadius = Float.MAX_VALUE;
        solutionSet = new ArrayList<>();
    }

    private static List<Vec3> solutionSet = new ArrayList<>();
    private static float lastRadius = Float.MAX_VALUE;

    public static void push(float radius) {
        if (!initFlag || radius + .05f > lastRadius) return;
        Vec3 relativePlayerPosition = (mc.player.position()).subtract(center); // Get player position
        List<Vec3> oplist;
        if (solutionSet.isEmpty()) {
            oplist = ((List<Vec3>) offsets.clone());
        } else {
            oplist = solutionSet;
        }
        solutionSet = oplist.stream().filter((x) -> {
            double dist = Math.abs(Math.sqrt(x.distanceToSqr(relativePlayerPosition)) - radius);
            return (dist < Config.miningSens);
        }).collect(Collectors.toList());
    }


    public static final FramePassManager.PassDefinition divanSolver = new FramePassManager.PassDefinition() {
        @Override
        public void targets(LevelTargetBundle bundle, FramePass pass) {
            bundle.main = pass.readsAndWrites(bundle.main);
        }

        @Override
        public void executes() {
            if (!LocatorUtil.isIn(Location.DIVAN) || !initFlag) return;
            var item = mc.player.getMainHandItem().getDisplayName().getString();
            if (!item.contains("Detector") || solutionSet.isEmpty()) return;
            var pose = new PoseStack();
            var camera = mc.gameRenderer.getMainCamera();
            var buffers = mc.renderBuffers();
            var bufSource = buffers.bufferSource();

            pose.pushPose();
            RenderUtils.renderRelativeToPlayer(pose, camera);
            var matrix = pose.last().pose();
            for (Vec3 sol : solutionSet) {
                var consumer = bufSource.getBuffer(RenderType.lines());
                RenderUtils.definePoint(consumer, matrix, mc.player.position().add(0, .25, 0));
                RenderUtils.definePoint(consumer, matrix, center.add(sol).add(0,2,0));
                bufSource.endBatch();
            }
            pose.popPose();
        }
    };

    public static void alert() {
        if (LocatorUtil.isIn(Location.DIVAN)) {
            if (!initFlag) init();
        }
        if (!LocatorUtil.isIn(Location.DIVAN)) {
            if (initFlag) deInit();
        }
    }
    public static final ChatParser parser = new ChatParser() {

        @Override
        public boolean handleMessage(Component msg) {
            Matcher matcher = metalDetectorPattern.matcher(msg.getString());
            if (matcher.find()) {
                String match = matcher.group(0);
                match = match.substring(0, match.length()-1);
                push(Float.parseFloat(match));
            }
            if (msg.getString().matches(".*You found.*Metal.*")) refresh();
            return false;
        }
    };
    static {
        // Locations are relative to the center of the divan mines, y value relative to the Keeperss.
        offsets.addAll(Arrays.asList(
                new Vec3(-38,-22, 26),
                new Vec3(38,-22, 26),
                new Vec3(-40,-22, 18),
                new Vec3(-41,-20, 22),
                new Vec3(-5,-21, 16),
                new Vec3(40, -22, -30),
                new Vec3(-42,-19, -28),
                new Vec3(-43,-22, -40),
                new Vec3(42,-19, -41),
                new Vec3(43,-21, -16),
                new Vec3(-1,-22, -20),
                new Vec3(6,-21, 28),
                new Vec3(7,-21, 11),
                new Vec3(7,-21, 22),
                new Vec3(-12,-21, -44),
                new Vec3(12,-22, 31),
                new Vec3(12,-22, -22),
                new Vec3(12,-21, 7),
                new Vec3(12,-21, -43),
                new Vec3(-14,-21,43),
                new Vec3(-14,-21, 22),
                new Vec3(-17,-21, 20),
                new Vec3(-20,-22, 0),
                new Vec3(1,-21, 20),
                new Vec3(19,-21, 29),
                new Vec3(20,-22, 0),
                new Vec3(20,-22, 0),
                new Vec3(20,-21, -26),
                new Vec3(-23,-22, 40),
                new Vec3(22,-21, -14),
                new Vec3(-24,-22, 12),
                new Vec3(23,-22, 26),
                new Vec3(23,-22, -39),
                new Vec3(24,-22, 27),
                new Vec3(25,-22, 17),
                new Vec3(29,-21, -44),
                new Vec3(-31,-21, -12),
                new Vec3(-31,-21, -40),
                new Vec3(30,-21, -25),
                new Vec3(-32,-21, -40),
                new Vec3(-36,-20, 42),
                new Vec3(-37,-21, -14),
                new Vec3(-37,-21, -22)
        ));
    }
}
