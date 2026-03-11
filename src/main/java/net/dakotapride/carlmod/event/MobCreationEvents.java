package net.dakotapride.carlmod.event;

import net.dakotapride.carlmod.CarlDuckEntity;
import net.dakotapride.carlmod.CarlMod;
import net.dakotapride.carlmod.quackiness.BiblicallyAccurateCarlBoss;
import net.dakotapride.carlmod.quackiness.EggCarl;
import net.dakotapride.carlmod.quackiness.EggCarlDuckEntityModel;
import net.dakotapride.carlmod.quackiness.UntitledCarl;
import net.dakotapride.carlmod.register.ModEntities;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.SpawnPlacementType;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;

@EventBusSubscriber(modid = CarlMod.MOD_ID, bus = EventBusSubscriber.Bus.MOD)
public class MobCreationEvents {

    @SubscribeEvent
    public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
        event.put(ModEntities.CARL_ENTITY.get(), CarlDuckEntity.setAttributes());
        event.put(ModEntities.BIBLICALLY_ACCURATE_CARL_ENTITY.get(), BiblicallyAccurateCarlBoss.setAttributes());
        event.put(ModEntities.UNTITLED_CARL_ENTITY.get(), UntitledCarl.setAttributes());
        event.put(ModEntities.EGG_CARL_ENTITY.get(), EggCarl.setAttributes());
    }

    @SubscribeEvent
    public static void registerSpawnPlacements(RegisterSpawnPlacementsEvent event) {
        event.register(ModEntities.CARL_ENTITY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                CarlDuckEntity::spawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.UNTITLED_CARL_ENTITY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                UntitledCarl::spawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
        event.register(ModEntities.EGG_CARL_ENTITY.get(), SpawnPlacementTypes.ON_GROUND, Heightmap.Types.MOTION_BLOCKING_NO_LEAVES,
                EggCarl::spawnRules, RegisterSpawnPlacementsEvent.Operation.REPLACE);
    }

}