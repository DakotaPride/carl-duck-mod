package net.dakotapride.carlmod;

import com.mojang.logging.LogUtils;
import net.dakotapride.carlmod.client.CarlDuckEntityRenderer;
import net.dakotapride.carlmod.forge.LootModifiers;
import net.minecraft.client.renderer.ItemBlockRenderTypes;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Registry;
import net.minecraft.nbt.CompoundTag;
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
import net.minecraft.world.level.material.Material;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.util.ForgeSoundType;
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
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import software.bernie.geckolib3.GeckoLib;

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

    public static final DeferredRegister<PaintingVariant> PAINTING_VARIANTS =
            DeferredRegister.create(ForgeRegistries.PAINTING_VARIANTS, MODID);

    public static class ModCreativeModeTab {
        public static final CreativeModeTab CARL_MOD = new CreativeModeTab("carlmod.carl") {
            @Override
            public @NotNull ItemStack makeIcon() {
                return new ItemStack(CarlMod.CARL_ITEM.get());
            }

            static ItemStack getDejojoTag(ItemStack stack, String nameTagName) {

                stack.getOrCreateTagElement("display").putString("Name", Component.Serializer.toJson(Component.literal(nameTagName)));
                stack.getOrCreateTag().putInt("RepairCost", 0);

                return stack;
            }

            @Override
            public void fillItemList(NonNullList<ItemStack> stacks) {
                // stacks.add(0, CarlMod.CARL_MOLD_ITEM.get().getDefaultInstance());
                // stacks.add(0, CarlMod.ANDESITE_HELMET.get().getDefaultInstance());
                // Name Tags
                stacks.add(0, getDejojoTag(Items.NAME_TAG.getDefaultInstance(), "jean"));
                stacks.add(0, getDejojoTag(Items.NAME_TAG.getDefaultInstance(), "ender_dragon"));
                stacks.add(0, getDejojoTag(Items.NAME_TAG.getDefaultInstance(), "dragon"));
                stacks.add(0, getDejojoTag(Items.NAME_TAG.getDefaultInstance(), "audrey"));
                stacks.add(0, getDejojoTag(Items.NAME_TAG.getDefaultInstance(), "deaudie"));
                stacks.add(0, getDejojoTag(Items.NAME_TAG.getDefaultInstance(), "adorable"));
                stacks.add(0, getDejojoTag(Items.NAME_TAG.getDefaultInstance(), "create"));
                stacks.add(0, getDejojoTag(Items.NAME_TAG.getDefaultInstance(), "mekanized"));
                stacks.add(0, getDejojoTag(Items.NAME_TAG.getDefaultInstance(), "mekanism"));
                stacks.add(0, getDejojoTag(Items.NAME_TAG.getDefaultInstance(), "bare_bones"));
                stacks.add(0, getDejojoTag(Items.NAME_TAG.getDefaultInstance(), "barebones"));
                stacks.add(0, getDejojoTag(Items.NAME_TAG.getDefaultInstance(), "garnished"));
                stacks.add(0, getDejojoTag(Items.NAME_TAG.getDefaultInstance(), "dejojotheawsome"));
                stacks.add(0, getDejojoTag(Items.NAME_TAG.getDefaultInstance(), "dejojo"));
                stacks.add(0, getDejojoTag(Items.NAME_TAG.getDefaultInstance(), "carltheawsome"));
                stacks.add(0, getDejojoTag(Items.NAME_TAG.getDefaultInstance(), "awsome"));

                stacks.add(0, CarlMod.CARL_SPAWN_EGG.get().getDefaultInstance());
                stacks.add(0, CarlMod.DUCK_BUCKET.get().getDefaultInstance());
                stacks.add(0, CarlMod.CARL_ITEM.get().getDefaultInstance());
            }
        };
    }

    // Creates a new Block with the id "examplemod:example_block", combining the namespace and path
    // public static final RegistryObject<Block> EXAMPLE_BLOCK = BLOCKS.register("example_block", () -> new Block(BlockBehaviour.Properties.of(Material.STONE)));
    // Creates a new BlockItem with the id "examplemod:example_block", combining the namespace and path
    // public static final RegistryObject<Item> EXAMPLE_BLOCK_ITEM = ITEMS.register("example_block", () -> new BlockItem(EXAMPLE_BLOCK.get(), new Item.Properties().tab(CreativeModeTab.TAB_BUILDING_BLOCKS)));


    public static final RegistryObject<Block> CARL_BLOCK = BLOCKS.register("carl", () -> new CarlBlock(BlockBehaviour.Properties.of(Material.WOOL).noOcclusion().instabreak().sound(CarlMod.CARL_GENERIC_SOUNDS)));
    public static final RegistryObject<Item> CARL_ITEM = ITEMS.register("carl", () -> new CarlBlockItem(CARL_BLOCK.get(), new Item.Properties().fireResistant().tab(ModCreativeModeTab.CARL_MOD)));
    public static final RegistryObject<Item> DUCK_BUCKET = ITEMS.register("duck_bucket", () -> new MobBucketItem(CarlMod.CARL_ENTITY, () -> Fluids.WATER, () -> SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1).tab(ModCreativeModeTab.CARL_MOD)));

    public static final RegistryObject<Item> CARL_SPAWN_EGG = ITEMS.register("carl_spawn_egg", () -> new ForgeSpawnEggItem(CarlMod.CARL_ENTITY, 0xFFDC41, 0xFFC844, new Item.Properties().tab(ModCreativeModeTab.CARL_MOD)));

    // Create Compat/Integration
    // public static final RegistryObject<Item> CARL_MOLD_ITEM = ITEMS.register("carl_mold", () -> new CompatItem(CompatItem.ModIds.CREATE.id, new Item.Properties().tab(ModCreativeModeTab.CARL_MOD)));
    // public static final RegistryObject<Item> INCOMPLETE_CARL_ITEM = ITEMS.register("incomplete_carl", () -> new CompatItem(CompatItem.ModIds.CREATE.id, new Item.Properties()));
    // public static final RegistryObject<Item> ANDESITE_HELMET = ITEMS.register("andesite_helmet", () -> new CompatItem(CompatItem.ModIds.CREATE.id, new Item.Properties().tab(ModCreativeModeTab.CARL_MOD)));

    public static final RegistryObject<SoundEvent> CARL_QUACK = SOUNDS.register("quack", () -> new SoundEvent(new ResourceLocation(MODID, "quack")));
    public static final RegistryObject<SoundEvent> CARL_WAMP = SOUNDS.register("wamp", () -> new SoundEvent(new ResourceLocation(MODID, "wamp")));

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

        GeckoLib.initialize();

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);

        // Register our mod's ForgeConfigSpec so that Forge can create and load the config file for us
        // ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.SPEC);
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
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
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
