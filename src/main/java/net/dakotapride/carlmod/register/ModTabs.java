package net.dakotapride.carlmod.register;

import net.dakotapride.carlmod.CarlMod;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, CarlMod.MOD_ID);
    public static final DeferredHolder<CreativeModeTab, CreativeModeTab> CARL_TAB = CREATIVE_MODE_TABS.register("carl",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModBlocks.CARL_ITEM.get()))
                    .title(Component.translatable("itemGroup.carlmod.carl"))
                    .displayItems(new DisplayItems())
                    .build());

    public static class DisplayItems implements CreativeModeTab.DisplayItemsGenerator {

        @Override
        public void accept(CreativeModeTab.ItemDisplayParameters parameters, CreativeModeTab.Output output) {
            // Quackiness
            output.accept(ModItems.BIBLICALLY_ACCURATE_CARL_SPAWN_EGG.get().getDefaultInstance());
            output.accept(ModItems.UNTITLED_CARL_SPAWN_EGG.get().getDefaultInstance());
            output.accept(ModItems.EGG_CARL_SPAWN_EGG.get().getDefaultInstance());
            output.accept(ModItems.CARL_EGG.get().getDefaultInstance());
            output.accept(ModBlocks.CARL_FEATHERS_BLOCK.toStack());
            output.accept(ModBlocks.UNTITLED_CARL_FEATHERS_BLOCK.toStack());
            output.accept(ModBlocks.BIBLICALLY_ACCURATE_CARL_FEATHERS_BLOCK.toStack());
            output.accept(ModBlocks.BEAK_BLOCK.toStack());
            output.accept(ModBlocks.UNTITLED_BEAK_BLOCK.toStack());
            output.accept(ModBlocks.HELMET_BLOCK.toStack());
            output.accept(ModBlocks.OUTER_HELMET_BLOCK.toStack());

            // Normal
            output.accept(ModItems.DUCK_BUCKET.get().getDefaultInstance());
            output.accept(ModBlocks.CARL_ITEM.get().getDefaultInstance());
            output.accept(ModItems.CARL_SPAWN_EGG.get().getDefaultInstance());
            output.accept(ModItems.CARL_TEMPLATE.get().getDefaultInstance());

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

    static ItemStack getNameTag(ItemStack stack, String nameTagName) {
        stack.set(DataComponents.CUSTOM_NAME, Component.literal(nameTagName));
        stack.set(DataComponents.REPAIR_COST, 0);

        return stack;
    }
}
