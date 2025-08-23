package dev.fealtous.minorconvenience;

import com.mojang.logging.LogUtils;
import dev.fealtous.minorconvenience.commands.ClientCommands;
import dev.fealtous.minorconvenience.convenience.*;
import dev.fealtous.minorconvenience.convenience.chat.ChatHandler;
import dev.fealtous.minorconvenience.convenience.experiments.ExperimentsHandler;
import dev.fealtous.minorconvenience.convenience.hoppity.CFOptimizer;
import dev.fealtous.minorconvenience.dungeons.DungeonMapRenderer;
import dev.fealtous.minorconvenience.dungeons.secrets.DungeonSecretRenderer;
import dev.fealtous.minorconvenience.dungeons.DungeonsHandler;
import dev.fealtous.minorconvenience.mining.DivanSolver;
import dev.fealtous.minorconvenience.mining.MiningHandler;
import dev.fealtous.minorconvenience.utils.network.InboundListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.function.Consumer;

@Mod(MinorConvenience.MODID)
public class MinorConvenience
{
    public static final String MODID = "minorconvenience";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Path GAMEDIR = FMLPaths.GAMEDIR.get().normalize().toAbsolutePath();
    public static final Path DIR = GAMEDIR.resolve("minorconvenience");
    public static final Path DUNGEONS = DIR.resolve("dungeons");
    public MinorConvenience(FMLJavaModLoadingContext fmlctx)
    {
        if (FMLEnvironment.dist.equals(Dist.CLIENT)) {
            DungeonsHandler.init(); // todo requires full rewrite
            ExperimentsHandler.init();
            ClientCommands.init();
            //MinecraftForge.EVENT_BUS.register(WaypointsHandler.class); // todo framepass or FLD
            //MinecraftForge.EVENT_BUS.register(new InboundListener()); // todo determine wtf i'll do with this
            //MinecraftForge.EVENT_BUS.register(MiningHandler.class); // todo FLD rework
            //MinecraftForge.EVENT_BUS.register(DivanSolver.class); // todo FLD rework & Framepass
            ChatHandler.init();
            DungeonMapRenderer.init(fmlctx.getModBusGroup());
            //MinecraftForge.EVENT_BUS.register(DungeonSecretRenderer.class); // todo framepass
            //MinecraftForge.EVENT_BUS.register(CFOptimizer.class);
            Alerts.init();
            ViewportEvent.RenderFog.BUS.addListener(evt -> {

                evt.setNearPlaneDistance(-4f);
                evt.setFarPlaneDistance(100000f);
            });
            var modEventBus = fmlctx.getModBusGroup();
            RegisterKeyMappingsEvent.getBus(modEventBus).addListener(KeyBindingHandlers::registerClientShit);
            fmlctx.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
        }
    }
}

