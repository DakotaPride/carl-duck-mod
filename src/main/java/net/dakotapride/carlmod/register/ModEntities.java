package net.dakotapride.carlmod.register;

import net.dakotapride.carlmod.CarlDuckEntity;
import net.dakotapride.carlmod.CarlMod;
import net.dakotapride.carlmod.quackiness.BiblicallyAccurateCarlBoss;
import net.dakotapride.carlmod.quackiness.EggCarl;
import net.dakotapride.carlmod.quackiness.ThrownCarlEgg;
import net.dakotapride.carlmod.quackiness.UntitledCarl;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, CarlMod.MOD_ID);

    public static final DeferredHolder<EntityType<?>, EntityType<CarlDuckEntity>> CARL_ENTITY =
            ENTITY_TYPES.register("carl",
                    () -> EntityType.Builder.of(CarlDuckEntity::new, MobCategory.CREATURE)
                            .sized(0.6f, 0.6f)
                            .build(ResourceLocation.fromNamespaceAndPath(CarlMod.MOD_ID, "carl").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<BiblicallyAccurateCarlBoss>> BIBLICALLY_ACCURATE_CARL_ENTITY =
            ENTITY_TYPES.register("biblically_accurate_carl",
                    () -> EntityType.Builder.of(BiblicallyAccurateCarlBoss::new, MobCategory.MONSTER)
                            .sized(0.6f, 0.6f)
                            .build(ResourceLocation.fromNamespaceAndPath(CarlMod.MOD_ID, "biblically_accurate_carl").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<UntitledCarl>> UNTITLED_CARL_ENTITY =
            ENTITY_TYPES.register("untitled_carl",
                    () -> EntityType.Builder.of(UntitledCarl::new, MobCategory.MONSTER)
                            .sized(0.6f, 0.6f)
                            .build(ResourceLocation.fromNamespaceAndPath(CarlMod.MOD_ID, "untitled_carl").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<EggCarl>> EGG_CARL_ENTITY =
            ENTITY_TYPES.register("egg_carl",
                    () -> EntityType.Builder.of(EggCarl::new, MobCategory.CREATURE)
                            .sized(0.6f, 0.6f)
                            .build(ResourceLocation.fromNamespaceAndPath(CarlMod.MOD_ID, "egg_carl").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<ThrownCarlEgg>> THROWN_CARL_EGG_ENTITY =
            ENTITY_TYPES.register("thrown_carl_egg",
                    () -> EntityType.Builder.<ThrownCarlEgg>of(ThrownCarlEgg::new, MobCategory.MISC)
                            .sized(0.1f, 0.1f)
                            .build(ResourceLocation.fromNamespaceAndPath(CarlMod.MOD_ID, "thrown_carl_egg").toString()));
}
