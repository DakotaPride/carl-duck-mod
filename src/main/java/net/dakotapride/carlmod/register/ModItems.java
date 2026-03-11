package net.dakotapride.carlmod.register;

import net.dakotapride.carlmod.CarlMod;
import net.dakotapride.carlmod.CarlTemplateUtils;
import net.dakotapride.carlmod.quackiness.CarlEggItem;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.MobBucketItem;
import net.minecraft.world.item.SmithingTemplateItem;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.common.DeferredSpawnEggItem;
import net.neoforged.neoforge.registries.DeferredHolder;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(CarlMod.MOD_ID);

    // Stupid shit - smithing template for a bucket of duck... worth it
    public static final DeferredHolder<Item, SmithingTemplateItem> CARL_TEMPLATE = ITEMS.register("carl_template", CarlTemplateUtils::createCarlUpgradeTemplate);
    public static final DeferredHolder<Item, MobBucketItem> DUCK_BUCKET = ITEMS.register("duck_bucket", () -> new MobBucketItem(ModEntities.CARL_ENTITY.get(), Fluids.WATER, SoundEvents.BUCKET_EMPTY_FISH, new Item.Properties().stacksTo(1)));

    public static final DeferredHolder<Item, CarlEggItem> CARL_EGG = ITEMS.register("carl_egg", () -> new CarlEggItem(new Item.Properties().stacksTo(16)));

    public static final DeferredHolder<Item, DeferredSpawnEggItem> CARL_SPAWN_EGG = ITEMS.register("carl_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.CARL_ENTITY, 0xFFDC41, 0xFFC844, new Item.Properties()));
    public static final DeferredHolder<Item, DeferredSpawnEggItem> BIBLICALLY_ACCURATE_CARL_SPAWN_EGG = ITEMS.register("biblically_accurate_carl_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.BIBLICALLY_ACCURATE_CARL_ENTITY, 0xE5FFFF, 0xCAE7F5, new Item.Properties()));
    public static final DeferredHolder<Item, DeferredSpawnEggItem> UNTITLED_CARL_SPAWN_EGG = ITEMS.register("untitled_carl_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.UNTITLED_CARL_ENTITY, 0xEFF2F4, 0xD7DEE5, new Item.Properties()));
    public static final DeferredHolder<Item, DeferredSpawnEggItem> EGG_CARL_SPAWN_EGG = ITEMS.register("egg_carl_spawn_egg", () -> new DeferredSpawnEggItem(ModEntities.EGG_CARL_ENTITY, 0xDFCE9B, 0xB3A57D, new Item.Properties()));
}
