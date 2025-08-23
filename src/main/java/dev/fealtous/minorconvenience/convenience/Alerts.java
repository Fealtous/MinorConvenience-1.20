package dev.fealtous.minorconvenience.convenience;

import dev.fealtous.minorconvenience.dungeons.DungeonMapRenderer;
import dev.fealtous.minorconvenience.dungeons.DungeonsHandler;
import dev.fealtous.minorconvenience.mining.DivanSolver;
import dev.fealtous.minorconvenience.mining.MiningHandler;
import dev.fealtous.minorconvenience.utils.LocatorUtil;
import net.minecraft.client.Minecraft;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.listener.SubscribeEvent;

public class Alerts {
    static int timer = 0;
    public static void init() {
        TickEvent.ClientTickEvent.Pre.BUS.addListener(Alerts::clientTickManager);
    }

    @SubscribeEvent
    public static void clientTickManager(TickEvent.ClientTickEvent.Pre e) {
        if (Minecraft.getInstance().level == null) return;
        timer++;
        if (timer % 15 == 0) {
            LocatorUtil.alert();
            DungeonsHandler.alert();
            DivanSolver.alert();
        }
    }
}
