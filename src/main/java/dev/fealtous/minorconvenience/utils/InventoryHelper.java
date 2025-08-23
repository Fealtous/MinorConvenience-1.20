package dev.fealtous.minorconvenience.utils;

import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.ContainerScreen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.Slot;

import java.util.List;
import java.util.stream.Collectors;

public class InventoryHelper {
    public static boolean isChestScreen(Screen s) {
        return s instanceof ContainerScreen;
    }
    public static List<Slot> chestInventory(Screen screen) {
        return (((ContainerScreen) screen).getMenu().slots.stream()
                .filter((i) -> !(i.container instanceof Inventory))).collect(Collectors.toList());
    }
}