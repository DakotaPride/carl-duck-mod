package net.dakotapride.carlmod;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import net.dakotapride.carlmod.client.CarlDuckEntityRenderer;
import net.dakotapride.carlmod.config.CarlConfig;
import net.dakotapride.carlmod.neoforge.AddItemLootModifier;
import net.dakotapride.carlmod.quackiness.*;
import net.dakotapride.carlmod.register.*;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.entity.SpawnPlacementTypes;
import net.minecraft.world.level.levelgen.Heightmap;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.entity.RegisterSpawnPlacementsEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.slf4j.Logger;

import java.util.function.Supplier;

@Mod(CarlMod.MOD_ID)
@SuppressWarnings("unused")
public class CarlMod {
    public static final String MOD_ID = "carlmod";
    private static final Logger LOGGER = LogUtils.getLogger();

    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MOD_ID);

    public static final Supplier<MapCodec<AddItemLootModifier>> ADD_ITEM_MODIFIER =
            GLOBAL_LOOT_MODIFIER_SERIALIZERS.register("add_item", () -> AddItemLootModifier.CODEC);

    public CarlMod(IEventBus modEventBus, ModContainer modContainer) {
        ModBlocks.BLOCKS.register(modEventBus);
        ModItems.ITEMS.register(modEventBus);
        ModSounds.SOUNDS.register(modEventBus);
        ModEntities.ENTITY_TYPES.register(modEventBus);

        GLOBAL_LOOT_MODIFIER_SERIALIZERS.register(modEventBus);
        ModTabs.CREATIVE_MODE_TABS.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.SERVER, CarlConfig.SPEC, "carlmod-server.toml");
    }

    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    public static class ModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            ItemBlockRenderTypes.setRenderLayer(ModBlocks.CARL_BLOCK.get(), RenderType.cutoutMipped());

            EntityRenderers.register(ModEntities.CARL_ENTITY.get(), CarlDuckEntityRenderer::new);
            EntityRenderers.register(ModEntities.BIBLICALLY_ACCURATE_CARL_ENTITY.get(), BiblicallyAccurateCarlDuckEntityRenderer::new);
            EntityRenderers.register(ModEntities.UNTITLED_CARL_ENTITY.get(), UntitledCarlDuckEntityRenderer::new);
            EntityRenderers.register(ModEntities.EGG_CARL_ENTITY.get(), EggCarlDuckEntityRenderer::new);
            EntityRenderers.register(ModEntities.THROWN_CARL_EGG_ENTITY.get(), ThrownItemRenderer::new);

            LOGGER.info("[Carl Mod] Quack Quack");
        }
    }
}
