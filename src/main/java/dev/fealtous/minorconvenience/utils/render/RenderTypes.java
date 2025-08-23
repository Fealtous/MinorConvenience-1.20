package dev.fealtous.minorconvenience.utils.render;

import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;

import java.util.OptionalDouble;

public class RenderTypes extends RenderStateShard {
//    public static final RenderType VIEW_THROUGH = RenderType.create("mcc_see_behind",
//            DefaultVertexFormat.POSITION_COLOR,
//            VertexFormat.Mode.QUADS, 1536,
//            false, false, RenderType.CompositeState.builder()
//                    .setTransparencyState(RenderStateShard.TRANSLUCENT_TRANSPARENCY)
//                    .setCullState(NO_CULL)
//                    .setDepthTestState(RenderStateShard.NO_DEPTH_TEST)
//                    .setShaderState(RenderStateShard.POSITION_COLOR_SHADER)
//                    .setWriteMaskState(COLOR_WRITE)
//            .createCompositeState(false));
    public RenderTypes(String s, Runnable r0, Runnable r1) {
        super(s, r0, r1);
    }
}
