package net.dakotapride.carlmod;

import com.mojang.logging.LogUtils;
import net.dakotapride.carlmod.client.CarlDuckEntityRenderer;
import net.dakotapride.carlmod.forge.LootModifiers;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.decoration.PaintingVariant;
import net.minecraft.world.item.*;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeSoundType;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import org.slf4j.Logger;
import software.bernie.geckolib.GeckoLib;

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
    public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, MODID);
    public static final DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES = DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, MODID);
    // Create a Deferred Register to hold Items which will all be registered under the "examplemod" namespace
    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, MODID);

    public static final DeferredRegister<SoundEvent> SOUNDS = DeferredRegister.create(ForgeRegistries.SOUND_EVENTS, MODID);

    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);

    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, MODID);

    public static final DeferredRegister<PaintingVariant> PAINTING_VARIANTS =
            DeferredRegister.create(ForgeRegistries.PAINTING_VARIANTS, MODID);

    // public static final RegistryObject<CreativeModeTab> CARL_TAB = CREATIVE_MODE_TABS.register("carl", ModCreativeModeTab::new);
    public static final RegistryObject<CreativeModeTab> CARL_TAB = CREATIVE_MODE_TABS.register("carl",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(CarlMod.CARL_ITEM.get()))
                    .title(Component.translatable("itemGroup.carlmod.carl"))
                    .displayItems(new DisplayItems())
                    .build());

    public static class DisplayItems implements CreativeModeTab.DisplayItemsGenerator {

        @Override
        public void accept(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
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
            output.accept(getNameTag(Items.NAME_TAG.getDefaultInstance(), "adorable"));
            output.accept(getNameTag(Items.NAME_TAG.getDefaultInstance(), "deaudie"));
            output.accept(getNameTag(Items.NAME_TAG.getDefaultInstance(), "audrey"));
            output.accept(getNameTag(Items.NAME_TAG.getDefaultInstance(), "dragon"));
            output.accept(getNameTag(Items.NAME_TAG.getDefaultInstance(), "ender_dragon"));
            output.accept(getNameTag(Items.NAME_TAG.getDefaultInstance(), "jean"));
        }
    }

    // Creates a new Block with the id "examplemod:example_block", combining the namespace and path
    // public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block", () -> new Block(BlockBehaviour.Properties.of(Material.STONE)));
    // Creates a new BlockItem with the id "examplemod:example_block", combining the namespace and path
    // public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM = ITEMS.register("example_block", () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));


    public static final RegistryObject<Block> CARL_BLOCK = BLOCKS.register("carl", () -> new CarlBlock(BlockBehaviour.Properties.of().noOcclusion().instabreak().sound(CarlMod.CARL_GENERIC_SOUNDS)));
    public static final RegistryObject<Item> CARL_ITEM = ITEMS.register("carl", () -> new CarlBlockItem(CARL_BLOCK.get(), new Item.Properties().fireResistant()));
    // Stupid shit - smithing template for a bucket of duck... worth it
    public static final RegistryObject<Item> CARL_TEMPLATE = ITEMS.register("carl_template", CarlTemplateUtils::createCarlUpgradeTemplate);
    public static final RegistryObject<Item> DUCK_BUCKET = ITEMS.register("duck_bucket", () -> new MobBucketItem(CarlMod.CARL_ENTITY, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));

    public static final RegistryObject<Item> CARL_SPAWN_EGG = ITEMS.register("carl_spawn_egg", () -> new ForgeSpawnEggItem(CarlMod.CARL_ENTITY, 0xFFDC41, 0xFFC844, new Item.Properties()));

    // Create Compat/Integration
    // public static final RegistryObject<Item> CARL_MOLD_ITEM = ITEMS.register("carl_mold", () -> new CompatItem(CompatItem.ModIds.CREATE.id, new Item.Properties().tab(ModCreativeModeTab.CARL_MOD)));
    // public static final RegistryObject<Item> INCOMPLETE_CARL_ITEM = ITEMS.register("incomplete_carl", () -> new CompatItem(CompatItem.ModIds.CREATE.id, new Item.Properties()));
    // public static final RegistryObject<Item> ANDESITE_HELMET = ITEMS.register("andesite_helmet", () -> new CompatItem(CompatItem.ModIds.CREATE.id, new Item.Properties().tab(ModCreativeModeTab.CARL_MOD)));

    public static final RegistryObject<SoundEvent> CARL_QUACK = SOUNDS.register("quack", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, "quack")));
    public static final RegistryObject<SoundEvent> CARL_WAMP = SOUNDS.register("wamp", () -> SoundEvent.createVariableRangeEvent(new ResourceLocation(MODID, "wamp")));

    public static final ForgeSoundType CARL_GENERIC_SOUNDS = new ForgeSoundType(1f, 1f,
            CARL_QUACK, () -> SoundEvents.CALCITE_STEP, CARL_QUACK,
            CARL_QUACK, () -> SoundEvents.CALCITE_FALL);

    public static final RegistryObject<EntityType<CarlDuckEntity>> CARL_ENTITY =
            ENTITY_TYPES.register("carl",
                    () -> EntityType.Builder.of(CarlDuckEntity::new, MobCategory.MISC)
                            .sized(0.4f, 0.4f)
                            .build(new ResourceLocation(MODID, "carl").toString()));

    // Easter Egg

    public static final RegistryObject<PaintingVariant> DEJOJO_THE_PIXELATED = PAINTING_VARIANTS.register("dejojo_the_pixelated",
            () -> new PaintingVariant(16, 16));
    public static final RegistryObject<PaintingVariant> DEJOJO_THE_PIXELATED_64x = PAINTING_VARIANTS.register("dejojo_the_pixelated_64px",
            () -> new PaintingVariant(64, 64));

    public CarlMod() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register the Deferred Register to the mod event bus so blocks get registered
        BLOCKS.register(modEventBus);
        BLOCK_ENTITIES.register(modEventBus);
        // Register the Deferred Register to the mod event bus so items get registered
        ITEMS.register(modEventBus);

        SOUNDS.register(modEventBus);

        ENTITY_TYPES.register(modEventBus);

        PAINTING_VARIANTS.register(modEventBus);

        LootModifiers.register(modEventBus);

        // ModCreativeModeTab.load();
        // Register the item to a creative tab
        CREATIVE_MODE_TABS.register(modEventBus);

        GeckoLib.initialize();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        // ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
    }

    static ItemStack getNameTag(ItemStack stack, String nameTagName) {

        stack.getOrCreateTagElement("display").putString("Name", Component.Serializer.toJson(Component.literal(nameTagName)));
        stack.getOrCreateTag().putInt("RepairCost", 0);

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
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
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
