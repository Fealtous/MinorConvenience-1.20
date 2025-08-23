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
            ListenerInit.init(fmlctx.getModBusGroup());
            fmlctx.registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
        }
    }
}

