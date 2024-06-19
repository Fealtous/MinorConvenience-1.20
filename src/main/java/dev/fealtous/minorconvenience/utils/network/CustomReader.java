package dev.fealtous.minorconvenience.utils.network;

import io.netty.channel.ChannelHandler;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientboundPlayerInfoUpdatePacket;
import net.minecraftforge.event.network.ConnectionStartEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.Collection;
import java.util.HashSet;

@ChannelHandler.Sharable
public class CustomReader extends SimpleChannelInboundHandler<Packet> {
    public static HashSet<String> exclusions = new HashSet<>();
    public static Collection<String> rcvd = null;
    public static Minecraft mc = Minecraft.getInstance();
    public CustomReader() {
        super(false);
    }
    @Override
    protected void channelRead0(ChannelHandlerContext ctx, Packet msg) throws Exception {
        var name = msg.getClass().getSimpleName();
        if (msg instanceof ClientboundPlayerInfoUpdatePacket pkt) {
            if (pkt.actions().contains(ClientboundPlayerInfoUpdatePacket.Action.UPDATE_LATENCY)) {
                return;
            }
        }
        ctx.fireChannelRead(msg);
    }

    @SubscribeEvent
    public void connect(ConnectionStartEvent e) {
        e.getConnection().channel().pipeline().addBefore("packet_handler", this.getClass().getName(), this);
    }
}
