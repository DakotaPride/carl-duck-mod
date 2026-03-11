package net.dakotapride.carlmod.register;

import net.dakotapride.carlmod.CarlBlock;
import net.dakotapride.carlmod.CarlBlockItem;
import net.dakotapride.carlmod.CarlMod;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.neoforged.neoforge.registries.DeferredBlock;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

import java.util.function.Supplier;

public class ModBlocks {
    public static final DeferredRegister.Blocks BLOCKS = DeferredRegister.createBlocks(CarlMod.MOD_ID);

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

    public static final DeferredHolder<Block, CarlBlock> CARL_BLOCK = BLOCKS.register("carl", () -> new CarlBlock(BlockBehaviour.Properties.of().noOcclusion().instabreak().sound(ModSounds.CARL_GENERIC_SOUNDS)));
    public static final DeferredHolder<Item, CarlBlockItem> CARL_ITEM = ModItems.ITEMS.register("carl", () -> new CarlBlockItem(CARL_BLOCK.get(), new Item.Properties().fireResistant()));

    private static <T extends Block> DeferredBlock<T> registerBlock(String name, Supplier<T> block) {
        DeferredBlock<T> toReturn = BLOCKS.register(name, block);
        registerBlockItem(name, toReturn);
        return toReturn;
    }
    private static <T extends Block> void registerBlockItem(String name, DeferredBlock<T> block) {
        ModItems.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
    }
}
