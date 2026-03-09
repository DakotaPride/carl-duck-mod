package net.dakotapride.carlmod;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import net.dakotapride.carlmod.client.CarlDuckEntityRenderer;
import net.dakotapride.carlmod.config.CarlConfig;
import net.dakotapride.carlmod.neoforge.AddItemLootModifier;
import net.dakotapride.carlmod.quackiness.*;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.util.DeferredSoundType;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.*;
import org.slf4j.Logger;

import java.util.function.Supplier;

@Mod(CarlMod.MOD_ID)
@SuppressWarnings("unused")
public class CarlMod {
    public static final String MOD_ID = "carlmod";
    private static final Logger LOGGER = LogUtils.getLogger();
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MOD_ID);
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MOD_ID);
    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, MOD_ID);
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, MOD_ID);
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MOD_ID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CARL_TAB = CREATIVE_MODE_TABS.register("carl",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(CarlMod.CARL_ITEM.get()))
                    .title(Component.translatable("itemGroup.carlmod.carl"))
                    .displayItems(new DisplayItems())
                    .build());

    public static class DisplayItems implements CreativeModeTab.DisplayItemsGenerator {

        @Override
        public void accept(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
            // Quackiness
            output.accept(CarlMod.BIBLICALLY_ACCURATE_CARL_SPAWN_EGG.get().getDefaultInstance());
            output.accept(CarlMod.UNTITLED_CARL_SPAWN_EGG.get().getDefaultInstance());
            output.accept(CarlMod.EGG_CARL_SPAWN_EGG.get().getDefaultInstance());
            output.accept(CarlMod.CARL_EGG.get().getDefaultInstance());
            output.accept(CarlMod.CARL_FEATHERS_BLOCK.toStack());
            output.accept(CarlMod.UNTITLED_CARL_FEATHERS_BLOCK.toStack());
            output.accept(CarlMod.BIBLICALLY_ACCURATE_CARL_FEATHERS_BLOCK.toStack());
            output.accept(CarlMod.BEAK_BLOCK.toStack());
            output.accept(CarlMod.UNTITLED_BEAK_BLOCK.toStack());
            output.accept(CarlMod.HELMET_BLOCK.toStack());
            output.accept(CarlMod.OUTER_HELMET_BLOCK.toStack());

            // Normal
            output.accept(CarlMod.DUCK_BUCKET.get().getDefaultInstance());
            output.accept(CarlMod.CARL_ITEM.get().getDefaultInstance());
            output.accept(CarlMod.CARL_SPAWN_EGG.get().getDefaultInstance());
            output.accept(CarlMod.CARL_TEMPLATE.get().getDefaultInstance());

            output.accept(getNameTag(Items.NAME_TAG.getDefaultInstance(), "awsome"));
            output.accept(getNameTag(Items.NAME_TAG.getDefaultInstance(), "carltheawsome"));
            output.accept(getNameTag(Items.NAME_TAG.getDefaultInstance(), "dejojo"));
            output.accept(getNameTag(Items.NAME_TAG.getDefaultInstance(), "dejojotheawsome"));
            output.accept(getNameTag(Items.NAME_TAG.getDefaultInstance(), "garnished"));
            output.accept(getNameTag(Items.NAME_TAG.getDefaultInstance(), "barebones"));
            output.accept(getNameTag(Items.NAME_TAG.getDefaultInstance(), "bare_bones"));
            output.accept(getNameTag(Items.NAME_TAG.getDefaultInstance(), "mekanism"));
            output.accept(getNameTag(Items.NAME_TAG.getDefaultInstance(), "mekanized"));
            output.accept(getNameTag(Items.NAME_TAG.getDefaultInstance(), "create"));
            output.accept(getNameTag(Items.NAME_TAG.getDefaultInstance(), "cardboard"));
            output.accept(getNameTag(Items.NAME_TAG.getDefaultInstance(), "adorable"));
            output.accept(getNameTag(Items.NAME_TAG.getDefaultInstance(), "deaudie"));
            output.accept(getNameTag(Items.NAME_TAG.getDefaultInstance(), "audrey"));
            output.accept(getNameTag(Items.NAME_TAG.getDefaultInstance(), "dragon"));
            output.accept(getNameTag(Items.NAME_TAG.getDefaultInstance(), "ender_dragon"));
            output.accept(getNameTag(Items.NAME_TAG.getDefaultInstance(), "jean"));
            output.accept(getNameTag(Items.NAME_TAG.getDefaultInstance(), "breeze"));
        }
    }

    // Creates a new Block with the id "examplemod:example_block", combining the namespace and path
    // public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block", () -> new Block(BlockBehaviour.Properties.of(Material.STONE)));
    // Creates a new BlockItem with the id "examplemod:example_block", combining the namespace and path
    // public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM = ITEMS.register("example_block", () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }
    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }

    public static final DeferredBlock<Block> CARL_FEATHERS_BLOCK = registerBlock("carl_feathers_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.YELLOW_WOOL)));
    public static final DeferredBlock<Block> UNTITLED_CARL_FEATHERS_BLOCK = registerBlock("untitled_carl_feathers_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.WHITE_WOOL)));
    public static final DeferredBlock<Block> BIBLICALLY_ACCURATE_CARL_FEATHERS_BLOCK = registerBlock("biblically_accurate_carl_feathers_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.LIGHT_BLUE_WOOL)));
    public static final DeferredBlock<Block> HELMET_BLOCK = registerBlock("helmet_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.STONE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> OUTER_HELMET_BLOCK = registerBlock("outer_helmet_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.DEEPSLATE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> BEAK_BLOCK = registerBlock("beak_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.ORANGE_TERRACOTTA).mapColor(DyeColor.ORANGE).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> UNTITLED_BEAK_BLOCK = registerBlock("untitled_beak_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.YELLOW_TERRACOTTA).mapColor(DyeColor.YELLOW).requiresCorrectToolForDrops()));
    public static final DeferredBlock<Block> ANGELIC_RING_BLOCK = registerBlock("angelic_ring_block",
            () -> new Block(BlockBehaviour.Properties.ofFullCopy(Blocks.GOLD_BLOCK).mapColor(DyeColor.ORANGE).requiresCorrectToolForDrops()));

    public static final DeferredHolder<Block, CarlBlock> CARL_BLOCK = BLOCKS.register("carl", () -> new CarlBlock(BlockBehaviour.Properties.of().noOcclusion().instabreak().sound(CarlMod.CARL_GENERIC_SOUNDS)));
    public static final DeferredHolder<Item, CarlBlockItem> CARL_ITEM = ITEMS.register("carl", () -> new CarlBlockItem(CARL_BLOCK.get(), new Item.Properties().fireResistant()));
    // Stupid shit - smithing template for a bucket of duck... worth it
    public static final DeferredHolder<Item, SmithingTemplateItem> CARL_TEMPLATE = ITEMS.register("carl_template", CarlTemplateUtils::createCarlUpgradeTemplate);
    public static final DeferredHolder<Item, MobBucketItem> DUCK_BUCKET = ITEMS.register("duck_bucket", () -> new MobBucketItem(CarlMod.CARL_ENTITY.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));

    public static final DeferredHolder<Item, CarlEggItem> CARL_EGG = ITEMS.register("carl_egg", () -> new CarlEggItem(new Item.Properties().stacksTo(16)));

    public static final DeferredHolder<Item, DeferredSpawnEggItem> CARL_SPAWN_EGG = ITEMS.register("carl_spawn_egg", () -> new DeferredSpawnEggItem(CarlMod.CARL_ENTITY, 0xFFDC41, 0xFFC844, new Item.Properties()));
    public static final DeferredHolder<Item, DeferredSpawnEggItem> BIBLICALLY_ACCURATE_CARL_SPAWN_EGG = ITEMS.register("biblically_accurate_carl_spawn_egg", () -> new DeferredSpawnEggItem(CarlMod.BIBLICALLY_ACCURATE_CARL_ENTITY, 0xE5FFFF, 0xCAE7F5, new Item.Properties()));
    public static final DeferredHolder<Item, DeferredSpawnEggItem> UNTITLED_CARL_SPAWN_EGG = ITEMS.register("untitled_carl_spawn_egg", () -> new DeferredSpawnEggItem(CarlMod.UNTITLED_CARL_ENTITY, 0xEFF2F4, 0xD7DEE5, new Item.Properties()));
    public static final DeferredHolder<Item, DeferredSpawnEggItem> EGG_CARL_SPAWN_EGG = ITEMS.register("egg_carl_spawn_egg", () -> new DeferredSpawnEggItem(CarlMod.EGG_CARL_ENTITY, 0xDFCE9B, 0xB3A57D, new Item.Properties()));

    // Create Compat/Integration
    // public static final RegistryObject<Item> CARL_MOLD_ITEM = ITEMS.register("carl_mold", () -> new CompatItem(CompatItem.ModIds.CREATE.id, new Item.Properties().tab(ModCreativeModeTab.CARL_MOD)));
    // public static final RegistryObject<Item> INCOMPLETE_CARL_ITEM = ITEMS.register("incomplete_carl", () -> new CompatItem(CompatItem.ModIds.CREATE.id, new Item.Properties()));
    // public static final RegistryObject<Item> ANDESITE_HELMET = ITEMS.register("andesite_helmet", () -> new CompatItem(CompatItem.ModIds.CREATE.id, new Item.Properties().tab(ModCreativeModeTab.CARL_MOD)));

    public static final DeferredHolder<SoundEvent, SoundEvent> CARL_QUACK = SOUNDS.register("quack", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "quack")));
    public static final DeferredHolder<SoundEvent, SoundEvent> CARL_QUACK_PLANT = SOUNDS.register("quack_plant", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "quack_plant")));
    public static final DeferredHolder<SoundEvent, SoundEvent> CARL_WAMP = SOUNDS.register("wamp", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MOD_ID, "wamp")));

    public static final DeferredSoundType CARL_GENERIC_SOUNDS = new DeferredSoundType(1f, 1f,
            CARL_QUACK, () -> SoundEvents.CALCITE_STEP, CARL_QUACK,
            CARL_QUACK, () -> SoundEvents.CALCITE_FALL);

    public static final DeferredHolder<EntityType<?>, EntityType<CarlDuckEntity>> CARL_ENTITY =
            ENTITY_TYPES.register("carl",
                    () -> EntityType.Builder.of(CarlDuckEntity::new, MobCategory.MISC)
                            .sized(0.6f, 0.6f)
                            .build(ResourceLocation.fromNamespaceAndPath(MOD_ID, "carl").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<BiblicallyAccurateCarlBoss>> BIBLICALLY_ACCURATE_CARL_ENTITY =
            ENTITY_TYPES.register("biblically_accurate_carl",
                    () -> EntityType.Builder.of(BiblicallyAccurateCarlBoss::new, MobCategory.MISC)
                            .sized(0.6f, 0.6f)
                            .build(ResourceLocation.fromNamespaceAndPath(MOD_ID, "biblically_accurate_carl").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<UntitledCarl>> UNTITLED_CARL_ENTITY =
            ENTITY_TYPES.register("untitled_carl",
                    () -> EntityType.Builder.of(UntitledCarl::new, MobCategory.MISC)
                            .sized(0.6f, 0.6f)
                            .build(ResourceLocation.fromNamespaceAndPath(MOD_ID, "untitled_carl").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<EggCarl>> EGG_CARL_ENTITY =
            ENTITY_TYPES.register("egg_carl",
                    () -> EntityType.Builder.of(EggCarl::new, MobCategory.MISC)
                            .sized(0.6f, 0.6f)
                            .build(ResourceLocation.fromNamespaceAndPath(MOD_ID, "egg_carl").toString()));
    public static final DeferredHolder<EntityType<?>, EntityType<ThrownCarlEgg>> THROWN_CARL_EGG_ENTITY =
            ENTITY_TYPES.register("thrown_carl_egg",
                    () -> EntityType.Builder.<ThrownCarlEgg>of(ThrownCarlEgg::new, MobCategory.MISC)
                            .sized(0.1f, 0.1f)
                            .build(ResourceLocation.fromNamespaceAndPath(MOD_ID, "thrown_carl_egg").toString()));

    // Easter Egg

//    public static final DeferredHolder<PaintingVariant, PaintingVariant> DEJOJO_THE_PIXELATED = PAINTING_VARIANTS.register("dejojo_the_pixelated",
//            () -> new PaintingVariant(16, 16, ResourceLocation.fromNamespaceAndPath(MODID, "dejojo_the_pixelated")));
//    public static final DeferredHolder<PaintingVariant, PaintingVariant> DEJOJO_THE_PIXELATED_64x = PAINTING_VARIANTS.register("dejojo_the_pixelated_64px",
//            () -> new PaintingVariant(64, 64, ResourceLocation.fromNamespaceAndPath(MODID, "dejojo_the_pixelated_64x")));



    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MOD_ID);

    public static final Supplier<MapCodec<AddItemLootModifier>> ADD_ITEM_MODIFIER =
            GLOBAL_LOOT_MODIFIER_SERIALIZERS.register("add_item", () -> AddItemLootModifier.CODEC);

    public CarlMod(IEventBus modEventBus, ModContainer modContainer) {
        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        //BLOCK_ENTITIES.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);

        SOUNDS.register(modEventBus);

        ENTITY_TYPES.register(modEventBus);

        //PAINTING_VARIANTS.register(modEventBus);

        //LootModifiers.register(modEventBus);

        GLOBAL_LOOT_MODIFIER_SERIALIZERS.register(modEventBus);

        // ModCreativeModeTab.load();
        // Register the item to a creative tab
        CREATIVE_MODE_TABS.register(modEventBus);

        modContainer.registerConfig(ModConfig.Type.SERVER, CarlConfig.SPEC, "carlmod-server.toml");

        //GeckoLib.initialize();

        // Register ourselves for server and other game events we are interested in
        //NeoForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        // ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    static ItemStack getNameTag(ItemStack stack, String nameTagName) {

        //stack.getOrCreateTagElement("display").putString("Name", Component.Serializer.toJson(Component.literal(nameTagName)));
        //stack.getOrCreateTag().putInt("RepairCost", 0);
        stack.set(DataComponents.CUSTOM_NAME, Component.literal(nameTagName));
        stack.set(DataComponents.REPAIR_COST, 0);

        return stack;
    }

    private void commonSetup(final FMLCommonSetupEvent event) {
        // Some common setup code
        // LOGGER.info("HELLO FROM COMMON SETUP");

        // if (Config.logDirtBlock)
        //     LOGGER.info("DIRT BLOCK >> {}", ForgeRegistries.BLOCKS.getKey(Blocks.DIRT));

        // LOGGER.info(Config.magicNumberIntroduction + Config.magicNumber);

        // Config.items.forEach((item) -> LOGGER.info("ITEM >> {}", item.toString()));
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event) {
        // Do something when the server starts
        // LOGGER.info("HELLO from server starting");
    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @EventBusSubscriber(modid = MOD_ID, bus = EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            // LOGGER.info("HELLO FROM CLIENT SETUP");
            // LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

            ItemBlockRenderTypes.setRenderLayer(CARL_BLOCK.get(), RenderType.cutoutMipped());

            EntityRenderers.register(CarlMod.CARL_ENTITY.get(), CarlDuckEntityRenderer::new);
            EntityRenderers.register(CarlMod.BIBLICALLY_ACCURATE_CARL_ENTITY.get(), BiblicallyAccurateCarlDuckEntityRenderer::new);
            EntityRenderers.register(CarlMod.UNTITLED_CARL_ENTITY.get(), UntitledCarlDuckEntityRenderer::new);
            EntityRenderers.register(CarlMod.EGG_CARL_ENTITY.get(), EggCarlDuckEntityRenderer::new);
            EntityRenderers.register(CarlMod.THROWN_CARL_EGG_ENTITY.get(), ThrownItemRenderer::new);

            LOGGER.info("[Carl Mod] Quack Quack");
        }

        @SubscribeEvent
        public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
            event.put(CarlMod.CARL_ENTITY.get(), CarlDuckEntity.setAttributes());
            event.put(CarlMod.BIBLICALLY_ACCURATE_CARL_ENTITY.get(), BiblicallyAccurateCarlBoss.setAttributes());
            event.put(CarlMod.UNTITLED_CARL_ENTITY.get(), UntitledCarl.setAttributes());
            event.put(CarlMod.EGG_CARL_ENTITY.get(), EggCarl.setAttributes());
        }
    }
}
