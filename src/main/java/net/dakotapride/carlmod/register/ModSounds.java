package net.dakotapride.carlmod.register;

import net.dakotapride.carlmod.CarlMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.neoforged.neoforge.common.util.DeferredSoundType;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModSounds {
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, CarlMod.MOD_ID);

    public static final DeferredHolder<SoundEvent, SoundEvent> CARL_QUACK = SOUNDS.register("quack", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(CarlMod.MOD_ID, "quack")));
    public static final DeferredHolder<SoundEvent, SoundEvent> CARL_QUACK_PLANT = SOUNDS.register("quack_plant", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(CarlMod.MOD_ID, "quack_plant")));
    public static final DeferredHolder<SoundEvent, SoundEvent> CARL_WAMP = SOUNDS.register("wamp", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(CarlMod.MOD_ID, "wamp")));

    public static final DeferredSoundType CARL_GENERIC_SOUNDS = new DeferredSoundType(1f, 1f,
            CARL_QUACK, () -> SoundEvents.CALCITE_STEP, CARL_QUACK,
            CARL_QUACK, () -> SoundEvents.CALCITE_FALL);
}
