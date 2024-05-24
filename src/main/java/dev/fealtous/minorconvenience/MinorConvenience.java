package dev.fealtous.minorconvenience;

import com.mojang.logging.LogUtils;
import dev.fealtous.minorconvenience.commands.ClientCommands;
import dev.fealtous.minorconvenience.convenience.ChatHandler;
import dev.fealtous.minorconvenience.convenience.ExperimentsHandler;
import dev.fealtous.minorconvenience.convenience.KeyBindingHandlers;
import dev.fealtous.minorconvenience.convenience.WaypointsHandler;
import dev.fealtous.minorconvenience.dungeons.DungeonsHandler;
import net.minecraftforge.client.event.RegisterKeyMappingsEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(MinorConvenience.MODID)
public class MinorConvenience
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "minorconvenience";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    public MinorConvenience()
    {

        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(DungeonsHandler.class);
        MinecraftForge.EVENT_BUS.register(ExperimentsHandler.class);
        MinecraftForge.EVENT_BUS.register(ClientCommands.class);
        MinecraftForge.EVENT_BUS.register(WaypointsHandler.class);
        MinecraftForge.EVENT_BUS.register(ChatHandler.class);

        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        modEventBus.addListener(this::registerClientShit);
        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        //ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    public void registerClientShit(RegisterKeyMappingsEvent e) {
        e.register(KeyBindingHandlers.COPY_TOGGLE);
    }


}

