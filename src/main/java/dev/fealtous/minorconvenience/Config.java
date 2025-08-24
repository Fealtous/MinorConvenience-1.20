package dev.fealtous.minorconvenience;

import dev.fealtous.minorconvenience.mining.MiningHandler;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.config.ModConfigEvent;

import java.util.List;


@Mod.EventBusSubscriber(modid = MinorConvenience.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class Config {

    private static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    private static final ForgeConfigSpec.ConfigValue<Double> mapLeftOffset = BUILDER.push("Dungeon Map Settings")
            .comment("Offset of map from left edge")
            .defineInRange("left", .25, 0., 1.);
    private static final ForgeConfigSpec.ConfigValue<Double> mapTopOffset = BUILDER
            .comment("Offset of map from top edge")
            .defineInRange("top", .25, 0., 1.);
    private static final ForgeConfigSpec.ConfigValue<Double> mapScaleConf = BUILDER
            .comment("Map Scaling")
            .define("scale", 1.0);
    private static final ForgeConfigSpec.ConfigValue<Boolean> enableMini = BUILDER
            .comment("Enable the minimap")
            .define("enabled", true);
    private static final ForgeConfigSpec.ConfigValue<Double> divanToolSensitivity = BUILDER.pop().push("Mining")
            .comment("Radius to use when attempting to locate divan tools. (larger = less precision)")
            .define("sensitivity",3.);
    private static final ForgeConfigSpec.ConfigValue<List<? extends String>> powderChestLoot = BUILDER
            .comment("Render order for powder chest loot. Also defines what will be tracked. " +
                    "This is not sanitized, so keep it to alphanumeric characters. (numbers and letters only)")
            .defineList("items", List.of("Gemstone Powder", "Blue Goblin Egg", "Red Goblin Egg",
                    "Yellow Goblin Egg", "Green Goblin Egg", "Goblin Egg",
                    "Sludge Juice", "Gold Essence", "Diamond Essence",
                    "Robotron Reflector","FTX 3070","Control Switch",
                    "Synthetic Heart","Superlite Motor","Electron Transmitter"), (x) -> true);
    private static final ForgeConfigSpec.ConfigValue<Double> powderChestOverlayY = BUILDER
            .comment("Offset of mining overlay from top edge")
            .defineInRange("topMining", 0., 0., 1.);
    private static final ForgeConfigSpec.ConfigValue<Double> powderChestOverlayX = BUILDER
            .comment("Offset of mining overlay from left edge")
            .defineInRange("leftMining", 0., 0., 1.);
    private static final ForgeConfigSpec.ConfigValue<Double> petOverlayX = BUILDER
            .comment("you get it by now")
            .defineInRange("leftPet", 0., 0., 1.);
    private static final ForgeConfigSpec.ConfigValue<Double> petOverlayY = BUILDER
            .defineInRange("topPet", 0., 0., 1.);

    static final ForgeConfigSpec SPEC = BUILDER.build();
    public static double leftMapOffset;
    public static double topMapOffset;
    public static double mapScale;
    public static double miningSens;
    public static boolean enableDungeonMinimap;
    public static double powderLeft;
    public static double powderTop;
    public static double petLeft;
    public static double petTop;

    @SubscribeEvent
    static void onLoad(final ModConfigEvent event)
    {
        mapScale = mapScaleConf.get();
        leftMapOffset = mapLeftOffset.get() / 2.;
        topMapOffset = mapTopOffset.get() / 2.;
        miningSens = divanToolSensitivity.get();
        enableDungeonMinimap = enableMini.get();
        MiningHandler.updateRenderList((List<String>) powderChestLoot.get());
        powderLeft = powderChestOverlayX.get();
        powderTop = powderChestOverlayY.get();
        petLeft = petOverlayX.get();
        petTop = petOverlayY.get();
    }
    public static int getXOff(int flat, double modifier) {
        return (int) (flat + (Minecraft.getInstance().getWindow().getGuiScaledWidth()* modifier));
    }
    public static int getYOff(int flat, double modifier) {
        return (int) (flat + (Minecraft.getInstance().getWindow().getGuiScaledHeight() * modifier));
    }
}
