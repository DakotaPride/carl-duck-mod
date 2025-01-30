package net.dakotapride.carlmod.neoforge;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.neoforged.neoforge.common.loot.IGlobalLootModifier;
import net.neoforged.neoforge.common.loot.LootModifier;

public class AddItemLootModifier extends LootModifier {
    // See below for how the codec works.
    public static final MapCodec<AddItemLootModifier> CODEC = RecordCodecBuilder.mapCodec(inst ->
            // LootModifier#codecStart adds the conditions field.
            LootModifier.codecStart(inst).and(inst.group(
                    Codec.INT.fieldOf("chance").forGetter(e -> e.chance),
                    BuiltInRegistries.ITEM.byNameCodec().fieldOf("item").forGetter(e -> e.item)
            )).apply(inst, AddItemLootModifier::new)
    );
    // Our extra properties.
    private final int chance;
    private final Item item;

    // First constructor parameter is the list of conditions. The rest is our extra properties.
    public AddItemLootModifier(LootItemCondition[] conditions, int chance, Item item) {
        super(conditions);
        this.chance = chance;
        this.item = item;
    }

    // Return our codec here.
    @Override
    public MapCodec<? extends IGlobalLootModifier> codec() {
        return CODEC;
    }

    // This is where the magic happens. Use your extra properties here if needed.
    // Parameters are the existing loot, and the loot context.
    @Override
    protected ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        // Add your items to generatedLoot here.

        // default 0.25
        if (context.getRandom().nextFloat() >= chance) {
            generatedLoot.add(new ItemStack(item));
        }

        return generatedLoot;
    }
}