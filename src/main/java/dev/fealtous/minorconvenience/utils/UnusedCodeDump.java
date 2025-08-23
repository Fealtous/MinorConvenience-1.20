package dev.fealtous.minorconvenience.utils;

import net.minecraft.client.renderer.RenderType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.phys.Vec3;

public class UnusedCodeDump {
    /*public static void push(float radius) {
        // skip push if we haven't moved.
        int pos = Math.floorMod(iterator -1, 3);
        var dist = srb[pos].origin.distanceTo(mc.player.position().multiply(-1,0,-1));
        //mc.gui.getChat().addMessage(Component.literal(String.valueOf(dist)));
        //if (dist < 10) return;
        srb[iterator] = new Intersector(mc.player.position(), radius);

        iterator++;
        iterator = Math.floorMod(iterator, 3);
        intersectionResult = calculateIntersection(mc.player.position().y);
        mc.gui.getChat().addMessage(Component.literal(intersectionResult.toString()));
    }

    @SubscribeEvent
    public static void renderPoint(RenderLevelStageEvent e) {
        if (!mc.player.getMainHandItem().getDisplayName().getString().contains("Metal Detector")) return;
        if (e.getStage().equals(RenderLevelStageEvent.Stage.AFTER_CUTOUT_BLOCKS)) {
            var pose = e.getPoseStack();
            var camera = e.getCamera().getPosition();
            var buffers = mc.renderBuffers();
            var bufSource = buffers.bufferSource();

            pose.pushPose();
            pose.translate(-camera.x, -camera.y, -camera.z);
            var matrix = pose.last().pose();
            var consumer = bufSource.getBuffer(RenderType.lines());
            RenderUtils.definePoint(consumer, matrix, mc.player.position().add(0, .25, 0));
            intersectionResult = intersectionResult.subtract(0,intersectionResult.y - mc.player.position().y,0);
            //System.out.printf("%.1f %.1f %.1f | %.1f %.1f %.1f\n", intersectionResult.x, intersectionResult.y, intersectionResult.z,
            //mc.player.position().x, mc.player.position().y, mc.player.position().z);
            RenderUtils.definePoint(consumer, matrix, intersectionResult);
            //definePoint(consumer, matrix, new Vec3(681, 66, 463));
            bufSource.endBatch();
            pose.popPose();
        }
    }


    private static Vec3 calculateIntersection(double pY) {
        double x1 = srb[0].origin.x, x2 = srb[1].origin.x, x3 = srb[2].origin.x;
        double z1 = srb[0].origin.z, z2 = srb[1].origin.z, z3 = srb[2].origin.z;
        double r1 = srb[0].radius, r2 = srb[1].radius, r3 = srb[2].radius;
        double x12 = Math.pow(x1,2), x22 = Math.pow(x2,2), x32 = Math.pow(x3,2);
        double z12 = Math.pow(z1,2), z22 = Math.pow(z2,2), z32 = Math.pow(z3,2);
        double r12 = Math.pow(r1,2), r22 = Math.pow(r2,2), r32 = Math.pow(r3,2);
        double commonFirst = (x22 - x12) + (z22 - z12) + (r12 - r22);
        double commonSecond = (x32 - x22) + (z32 - z22) + (r22 - r32);

        double yCoord = ((x2-x3) * (commonFirst)) - ((x1 - x2) * (commonSecond));
        yCoord = yCoord / (2 * (((z1 - z2) * (x2 - x3)) - ((z2 - z3) * (x1 - x2))));
        double xCoord = ((z2-z3) * (commonFirst)) - ((x1 - x2) * (commonSecond));
        xCoord = xCoord / (2 * (((x1 - x2) * (z2 - z3)) - ((x2 - x3) * (z1 - z2))));
        return new Vec3(-xCoord,pY,-yCoord);
    }




    static class Intersector {
        private Vec3 origin = Vec3.ZERO;
        private float radius = 0;

        public Intersector(Vec3 origin, float radius) {
            this.origin = origin;
            this.radius = radius;
        }

        public Intersector() {}

        static Intersector[] initBuffer() {
            return new Intersector[]{new Intersector(), new Intersector(), new Intersector()};
        }

        @Override
        public String toString() {
            return String.format("x:%f y:%f z:%f | radius: %f", origin.x, origin.y, origin.z, radius);
        }
    }*/
}
