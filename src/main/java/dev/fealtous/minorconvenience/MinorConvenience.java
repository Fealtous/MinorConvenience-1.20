package dev.fealtous.minorconvenience;

import com.mojang.logging.LogUtils;
import dev.fealtous.minorconvenience.commands.ClientCommands;
import dev.fealtous.minorconvenience.convenience.*;
import dev.fealtous.minorconvenience.convenience.hoppity.CFOptimizer;
import dev.fealtous.minorconvenience.dungeons.DungeonMapRenderer;
import dev.fealtous.minorconvenience.dungeons.DungeonRoomIdentifier;
import dev.fealtous.minorconvenience.dungeons.DungeonsHandler;
import dev.fealtous.minorconvenience.mining.MiningHandler;
import dev.fealtous.minorconvenience.utils.network.CustomReader;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;

import java.nio.file.Path;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MinorConvenience.MODID)
public class MinorConvenience
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "minorconvenience";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final Path GAMEDIR = FMLPaths.GAMEDIR.get().normalize().toAbsolutePath();
    public static final Path DIR = GAMEDIR.resolve("minorconvenience");
    public static final Path DUNGEONS = DIR.resolve("dungeons");
    public MinorConvenience()
    {
        if (FMLEnvironment.dist.equals(Dist.CLIENT)) {
            MinecraftForge.EVENT_BUS.register(this);
            MinecraftForge.EVENT_BUS.register(DungeonsHandler.class);
            MinecraftForge.EVENT_BUS.register(ExperimentsHandler.class);
            MinecraftForge.EVENT_BUS.register(ClientCommands.class);
            MinecraftForge.EVENT_BUS.register(WaypointsHandler.class);
            MinecraftForge.EVENT_BUS.register(new CustomReader());
            MinecraftForge.EVENT_BUS.register(MiningHandler.class);
            MinecraftForge.EVENT_BUS.register(ChatHandler.class);
            MinecraftForge.EVENT_BUS.register(DungeonMapRenderer.class);
            MinecraftForge.EVENT_BUS.register(DungeonRoomIdentifier.class);
            MinecraftForge.EVENT_BUS.register(CFOptimizer.class);
            MinecraftForge.EVENT_BUS.register(Alerts.class);
            IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
            modEventBus.addListener(this::registerClientShit);
        }

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        //ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public void registerClientShit(RegisterKeyMappingsEvent e) {
        e.register(KeyBindingHandlers.COPY_TOGGLE);
    }


}

