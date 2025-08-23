package dev.fealtous.minorconvenience.utils;

import dev.fealtous.minorconvenience.MinorConvenience;
import net.minecraft.resources.ResourceLocation;

public class ResourceHelper {
    public static ResourceLocation rl(String path) {
        return ResourceLocation.fromNamespaceAndPath(MinorConvenience.MODID, path);
    }
}
