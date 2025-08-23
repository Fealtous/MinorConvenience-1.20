package dev.fealtous.minorconvenience.utils.network;

import dev.fealtous.minorconvenience.utils.Location;
import dev.fealtous.minorconvenience.utils.LocatorUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelOutboundHandlerAdapter;
import io.netty.channel.ChannelPromise;
import net.minecraft.client.Minecraft;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.protocol.game.ServerboundUseItemOnPacket;
import net.minecraft.world.phys.BlockHitResult;

public class OutboundListener extends ChannelOutboundHandlerAdapter {
    static Minecraft mc = Minecraft.getInstance();
    /* Fixes blaze slayer daggers not properly swapping state when right clicking a block
    * Caused by ServerboundUseItemOnPacket and ServerboundUseItemPacket being sent
    * at the same time when right clicking a block. This causes sword to swap state twice.
    * Cancelling one of these packets solves this issue.
    * */
    @Override
    public void write(ChannelHandlerContext ctx, Object msg, ChannelPromise promise) throws Exception {
        if (msg instanceof ServerboundUseItemOnPacket && mc.hitResult instanceof BlockHitResult) {
            if (mc.player != null && mc.player.getMainHandItem().getComponents().has(DataComponents.WEAPON) && LocatorUtil.isIn(Location.NETHER)) {
                return;
            }
        }
        super.write(ctx, msg, promise);
    }
}
