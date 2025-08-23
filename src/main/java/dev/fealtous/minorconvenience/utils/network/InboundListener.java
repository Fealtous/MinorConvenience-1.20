package dev.fealtous.minorconvenience.utils.network;

import dev.fealtous.minorconvenience.convenience.experiments.ExperimentsHandler;
import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.*;
import net.minecraftforge.event.network.ConnectionStartEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;

@ChannelHandler.Sharable
public class InboundListener extends SimpleChannelInboundHandler<Packet> {
    public static Minecraft mc = Minecraft.getInstance();
    private static InboundListener instance = null;
    public InboundListener() {
        super(false);
        instance = this;
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception {
        // Reduce log spam, shouldn't cause any issues?
        if (msg instanceof ClientboundPlayerInfoUpdatePacket pkt) {
            if (pkt.actions().contains(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LATENCY)) {
                return;
            }
        } else if (msg instanceof ClientboundSetPassengersPacket pkt) {
            if (mc.level != null) {
                if (mc.level.getEntity(pkt.getVehicle()) == null) return;
            }
        } else if (ExperimentsHandler.inExperiments && msg instanceof ClientboundContainerSetSlotPacket pkt) {
            ExperimentsHandler.handleChronoUpdate(pkt.getItem(), pkt.getSlot());
        }
        ctx.fireChannelRead(msg);
    }

    @SubscribeEvent
    public static void onConnect(ConnectionStartEvent e) {
        new InboundListener();
        e.getConnection().channel().pipeline().addBefore("packet_handler", InboundListener.class.getName(), instance);
        e.getConnection().channel().pipeline().addBefore("packet_handler", OutboundListener.class.getName(), new OutboundListener());
    }
}
