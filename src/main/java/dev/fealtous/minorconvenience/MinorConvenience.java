package dev.fealtous.minorconvenience;

import com.mojang.blaze3d.shaders.FogShape;
import com.mojang.logging.LogUtils;
import dev.fealtous.minorconvenience.commands.ClientCommands;
import dev.fealtous.minorconvenience.convenience.*;
import dev.fealtous.minorconvenience.convenience.experiments.ExperimentsHandler;
import dev.fealtous.minorconvenience.convenience.hoppity.CFOptimizer;
import dev.fealtous.minorconvenience.dungeons.DungeonMapRenderer;
import dev.fealtous.minorconvenience.dungeons.secrets.DungeonSecretRenderer;
import dev.fealtous.minorconvenience.dungeons.DungeonsHandler;
import dev.fealtous.minorconvenience.mining.DivanSolver;
import dev.fealtous.minorconvenience.mining.MiningHandler;
import dev.fealtous.minorconvenience.utils.network.InboundListener;
import net.minecraft.client.renderer.FogRenderer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.ViewportEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLEnvironment;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;

import java.nio.file.Path;
import java.util.Random;
import java.util.function.Consumer;

@Mod(MinorConvenience.MODID)
public class MinorConvenience
{
    public static final String MODID = "minorconvenience";
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
            MinecraftForge.EVENT_BUS.register(new InboundListener());
            MinecraftForge.EVENT_BUS.register(MiningHandler.class);
            MinecraftForge.EVENT_BUS.register(DivanSolver.class);
            MinecraftForge.EVENT_BUS.register(ChatHandler.class);
            MinecraftForge.EVENT_BUS.addListener(DungeonMapRenderer::dungeonChat);
            MinecraftForge.EVENT_BUS.register(DungeonSecretRenderer.class);
            MinecraftForge.EVENT_BUS.register(CFOptimizer.class);
            MinecraftForge.EVENT_BUS.register(Alerts.class);
            MinecraftForge.EVENT_BUS.addListener((Consumer<ViewportEvent.RenderFog>) renderFog -> {
                renderFog.setCanceled(true);
                renderFog.setFogShape(FogShape.CYLINDER);
                renderFog.setNearPlaneDistance(-4f);
                renderFog.setFarPlaneDistance(100000f);
            });
            IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
            modEventBus.addListener(KeyBindingHandlers::registerClientShit);
            modEventBus.addListener(BasicInfoOverlay::basicInfoOverlay);
            MinecraftForge.EVENT_BUS.addListener(BasicInfoOverlay::cancelPotionOverlay);
            modEventBus.addListener(MiningHandler::registerChestLootOverlay);
            modEventBus.addListener(DungeonMapRenderer::mapRender);
            ModLoadingContext.get().registerConfig(ModConfig.Type.CLIENT, Config.SPEC);
        }
    }
}

