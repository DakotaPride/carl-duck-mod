package net.dakotapride.carlmod;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.MapCodec;
import net.dakotapride.carlmod.client.CarlDuckEntityRenderer;
import net.dakotapride.carlmod.neoforge.AddItemLootModifier;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.item.*;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.equipment.Equippable;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLClientSetupEvent;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.util.DeferredSoundType;
import net.neoforged.neoforge.event.entity.EntityAttributeCreationEvent;
import net.neoforged.neoforge.event.server.ServerStartingEvent;
import net.neoforged.neoforge.registries.*;
import org.slf4j.Logger;

import java.util.function.Function;
import java.util.function.Supplier;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(CarlMod.MODID)
@SuppressWarnings("unused")
public class CarlMod {
    // Define mod id in a common place for everything to reference
    // change to "carlmod"
    public static final String MODID = "carlmod";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();
    // Create a Deferred Register to hold Blocks which will all be registered under the "examplemod" namespace
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(MODID);
    //public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(Registries.BLOCK_ENTITY_TYPE, MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "examplemod" namespace
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(MODID);

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(Registries.SOUND_EVENT, MODID);

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(Registries.ENTITY_TYPE, MODID);

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

//    public static final DeferredRegister<PaintingVariant> PAINTING_VARIANTS =
//            DeferredRegister.create(Registries.PAINTING_VARIANT, MODID);

    // public static final RegistryObject<CreativeModeTab> CARL_TAB = CREATIVE_MODE_TABS.register("carl", ModCreativeModeTab::new);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CARL_TAB = CREATIVE_MODE_TABS.register("carl",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(CarlMod.CARL_BLOCK.get().asItem()))
                    .title(Component.translatable("itemGroup.carlmod.carl"))
                    .displayItems(new DisplayItems())
                    .build());

    public static class DisplayItems implements CreativeModeTab.DisplayItemsGenerator {

        @Override
        public void accept(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
            output.accept(CarlMod.DUCK_BUCKET.get().getDefaultInstance());
            output.accept(CarlMod.CARL_BLOCK.get().asItem().getDefaultInstance());
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

    public static <T extends Item> DeferredItem<T> registerItem(String name, Function<Item.Properties, T> item, Supplier<Item.Properties> properties) {
        return ITEMS.register(name, () -> item.apply(properties.get().setId(ResourceKey.create(Registries.ITEM, ResourceLocation.fromNamespaceAndPath("carlmod", name)))));
    }

    public static <T extends Block> DeferredBlock<T> registerBlockWithItem(String name,
                                                                           Function<BlockBehaviour.Properties, T> block,
                                                                           Supplier<BlockBehaviour.Properties> properties) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, () -> block.apply(properties.get().setId(ResourceKey.create(Registries.BLOCK,
                ResourceLocation.fromNamespaceAndPath("carlmod", name)))));
        registerItem(
                name,
                itemProps -> new BlockItem(toReturn.get(), itemProps),
                () -> new Item.Properties().useBlockDescriptionPrefix());
        return toReturn;
    }

    public static <T extends Block> DeferredBlock<T> registerCarlHeadItem(String name,
                                                                           Function<BlockBehaviour.Properties, T> block,
                                                                           Supplier<BlockBehaviour.Properties> properties) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, () -> block.apply(properties.get().setId(ResourceKey.create(Registries.BLOCK,
                ResourceLocation.fromNamespaceAndPath("carlmod", name)))));
        registerItem(
                name,
                itemProps -> new BlockItem(toReturn.get(), itemProps),
                () -> new Item.Properties().fireResistant().component(
                        DataComponents.EQUIPPABLE,
                        Equippable.builder(EquipmentSlot.HEAD).setSwappable(false).build()
                ).useBlockDescriptionPrefix());
        return toReturn;
    }

    public static final DeferredBlock<CarlBlock> CARL_BLOCK = registerCarlHeadItem("carl",
            CarlBlock::new, () -> BlockBehaviour.Properties.of().noOcclusion().instabreak().sound(CarlMod.CARL_GENERIC_SOUNDS));
    // Stupid shit - smithing template for a bucket of duck... worth it
    public static final DeferredItem<SmithingTemplateItem> CARL_TEMPLATE = registerItem("carl_template",
            CarlTemplateUtils::createCarlUpgradeTemplate, Item.Properties::new);
    public static final DeferredItem<MobBucketItem> DUCK_BUCKET = registerItem("duck_bucket",
            settings -> new MobBucketItem(CarlMod.CARL_ENTITY.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, settings),
            () -> new Item.Properties().stacksTo(1).component(DataComponents.BUCKET_ENTITY_DATA, CustomData.EMPTY));

    public static final DeferredItem<SpawnEggItem> CARL_SPAWN_EGG = registerItem("carl_spawn_egg",
            settings -> new SpawnEggItem(CarlMod.CARL_ENTITY.get(), settings), Item.Properties::new);

    // Create Compat/Integration
    // public static final RegistryObject<Item> CARL_MOLD_ITEM = ITEMS.register("carl_mold", () -> new CompatItem(CompatItem.ModIds.CREATE.id, new Item.Properties().tab(ModCreativeModeTab.CARL_MOD)));
    // public static final RegistryObject<Item> INCOMPLETE_CARL_ITEM = ITEMS.register("incomplete_carl", () -> new CompatItem(CompatItem.ModIds.CREATE.id, new Item.Properties()));
    // public static final RegistryObject<Item> ANDESITE_HELMET = ITEMS.register("andesite_helmet", () -> new CompatItem(CompatItem.ModIds.CREATE.id, new Item.Properties().tab(ModCreativeModeTab.CARL_MOD)));

    public static final DeferredHolder<SoundEvent, SoundEvent> CARL_QUACK = SOUNDS.register("quack", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MODID, "quack")));
    public static final DeferredHolder<SoundEvent, SoundEvent> CARL_QUACK_PLANT = SOUNDS.register("quack_plant", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MODID, "quack_plant")));
    public static final DeferredHolder<SoundEvent, SoundEvent> CARL_WAMP = SOUNDS.register("wamp", () -> SoundEvent.createVariableRangeEvent(ResourceLocation.fromNamespaceAndPath(MODID, "wamp")));

    public static final DeferredSoundType CARL_GENERIC_SOUNDS = new DeferredSoundType(1f, 1f,
            CARL_QUACK, () -> SoundEvents.CALCITE_STEP, CARL_QUACK,
            CARL_QUACK, () -> SoundEvents.CALCITE_FALL);

//    public static final DeferredHolder<EntityType<?>, EntityType<CarlDuckEntity>> CARL_ENTITY =
//            ENTITY_TYPES.register("carl",
//                    () -> EntityType.Builder.of(CarlDuckEntity::new, MobCategory.MISC)
//                            .sized(0.4f, 0.4f)
//                            .build(ResourceLocation.fromNamespaceAndPath(MODID, "carl").toString()));

    public static DeferredHolder<EntityType<?>, EntityType<CarlDuckEntity>> CARL_ENTITY = ENTITY_TYPES.register(
            "carl", () -> EntityType.Builder.of(CarlDuckEntity::new, MobCategory.MISC)
                    .sized(0.4F, 0.4F).build(keyOf("carl")));

    private static ResourceKey<EntityType<?>> keyOf(String id) {
        return ResourceKey.create(Registries.ENTITY_TYPE, ResourceLocation.fromNamespaceAndPath("carlmod", id));
    }

    // Easter Egg

//    public static final DeferredHolder<PaintingVariant, PaintingVariant> DEJOJO_THE_PIXELATED = PAINTING_VARIANTS.register("dejojo_the_pixelated",
//            () -> new PaintingVariant(16, 16, ResourceLocation.fromNamespaceAndPath(MODID, "dejojo_the_pixelated")));
//    public static final DeferredHolder<PaintingVariant, PaintingVariant> DEJOJO_THE_PIXELATED_64x = PAINTING_VARIANTS.register("dejojo_the_pixelated_64px",
//            () -> new PaintingVariant(64, 64, ResourceLocation.fromNamespaceAndPath(MODID, "dejojo_the_pixelated_64x")));



    public static final DeferredRegister<MapCodec<? extends IGlobalLootModifier>> GLOBAL_LOOT_MODIFIER_SERIALIZERS =
            DeferredRegister.create(NeoForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, MODID);

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
    @EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)
    public static class ClientModEvents {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event) {
            // Some client setup code
            // LOGGER.info("HELLO FROM CLIENT SETUP");
            // LOGGER.info("MINECRAFT NAME >> {}", Minecraft.getInstance().getUser().getName());

            ItemBlockRenderTypes.setRenderLayer(CARL_BLOCK.get(), RenderType.cutoutMipped());

            EntityRenderers.register(CarlMod.CARL_ENTITY.get(), CarlDuckEntityRenderer::new);

            LOGGER.info("[Carl Mod] Quack Quack");
        }

        @SubscribeEvent
        public static void entityAttributeEvent(EntityAttributeCreationEvent event) {
            event.put(CarlMod.CARL_ENTITY.get(), CarlDuckEntity.setAttributes());
        }
    }
}
