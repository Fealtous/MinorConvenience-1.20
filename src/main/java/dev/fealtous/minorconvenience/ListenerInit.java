package dev.fealtous.minorconvenience;

import dev.fealtous.minorconvenience.commands.ClientCommands;
import dev.fealtous.minorconvenience.convenience.Alerts;
import dev.fealtous.minorconvenience.convenience.KeyBindingHandlers;
import dev.fealtous.minorconvenience.convenience.chat.ChatHandler;
import dev.fealtous.minorconvenience.convenience.experiments.ExperimentsHandler;
import dev.fealtous.minorconvenience.dungeons.DungeonMapRenderer;
import dev.fealtous.minorconvenience.dungeons.DungeonsHandler;
import dev.fealtous.minorconvenience.mining.MiningHandler;
import dev.fealtous.minorconvenience.utils.Location;
import dev.fealtous.minorconvenience.utils.LocatorUtil;
import dev.fealtous.minorconvenience.utils.network.InboundListener;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.client.event.AddGuiOverlayLayersEvent;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.client.gui.overlay.ForgeLayeredDraw;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.network.ConnectionStartEvent;
import net.minecraftforge.eventbus.api.bus.BusGroup;

public class ListenerInit {
    public static void init(BusGroup group) {
        AddGuiOverlayLayersEvent.getBus(group).addListener(ListenerInit::initOverlays);
        DungeonsHandler.init();
        ExperimentsHandler.init();
        ClientCommands.init();
        //MinecraftForge.EVENT_BUS.register(WaypointsHandler.class); // todo framepass or FLD
        ConnectionStartEvent.BUS.addListener(InboundListener::onConnect); // todo determine wtf i'll do with this
        //MinecraftForge.EVENT_BUS.register(DivanSolver.class); // todo FLD rework & Framepass
        ChatHandler.init();
        DungeonMapRenderer.init();
        //MinecraftForge.EVENT_BUS.register(DungeonSecretRenderer.class); // todo framepass
        //MinecraftForge.EVENT_BUS.register(CFOptimizer.class);
        Alerts.init();
        ViewportEvent.RenderFog.BUS.addListener(evt -> {
            evt.setNearPlaneDistance(-4f);
            evt.setFarPlaneDistance(100000f);
        });
        RegisterKeyMappingsEvent.getBus(group).addListener(KeyBindingHandlers::registerClientShit);
    }



    public static void initOverlays(AddGuiOverlayLayersEvent event) {
        ForgeLayeredDraw minorDrawStack = new ForgeLayeredDraw(ResourceLocation.fromNamespaceAndPath(MinorConvenience.MODID, "minorconvenience_root"));
        minorDrawStack
                .addWithCondition(DungeonMapRenderer.mapRl, DungeonMapRenderer.mapOverlay, LocatorUtil::isDungeons)
                .addWithCondition(MiningHandler.miningRl, MiningHandler.miningOverlay, () -> LocatorUtil.isIn(Location.HOLLOWS_GENERIC))
                /*.addWithCondition(TestRender.rl, TestRender.overlay, () -> Minecraft.getInstance().player != null)*/
        ;


        event.getLayeredDraw().add(minorDrawStack.getName(), minorDrawStack, () -> true);
        event.getLayeredDraw().move(minorDrawStack.getName(), ForgeLayeredDraw.SLEEP_OVERLAY, ForgeLayeredDraw.LayerOffset.ABOVE);

    }
}
