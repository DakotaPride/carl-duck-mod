package net.dakotapride.carlmod.config;

import net.dakotapride.carlmod.CarlMod;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.event.config.ModConfigEvent;
import net.neoforged.neoforge.common.ModConfigSpec;

@EventBusSubscriber(modid = CarlMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class CarlConfig {

    CarlConfig(ModConfigSpec.Builder builder) {}

    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    public static ModConfigSpec.DoubleValue QUACK_RIGHT_CLICK_VOLUME;
    public static ModConfigSpec.DoubleValue QUACK_RIGHT_CLICK_PITCH;

    public static double volume;
    public static double pitch;

    static {
        BUILDER.push("configs");

        QUACK_RIGHT_CLICK_VOLUME = BUILDER
                .comment("Default value: 0.6")
                .defineInRange("volume", 0.6D, 0.0D, 10.0D);
        QUACK_RIGHT_CLICK_PITCH = BUILDER
                .comment("Default value: 1.0")
                .defineInRange("pitch", 1.0D, 0.0D, 10.0D);

        BUILDER.pop();
        SPEC = BUILDER.build();
    }

    @SubscribeEvent
    public static void onConfigLoad(ModConfigEvent.Loading event) {
        volume = QUACK_RIGHT_CLICK_VOLUME.get();
        pitch = QUACK_RIGHT_CLICK_PITCH.get();
    }

    @SubscribeEvent
    public static void onConfigReload(ModConfigEvent.Reloading event) {}
}
