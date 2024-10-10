package net.dakotapride.carlmod;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class CarlBlockItem extends BlockItem {
    public CarlBlockItem(Block p_40565_, Properties p_40566_) {
        super(p_40565_, p_40566_);
    }

    @Override
    public boolean canBeHurtBy(DamageSource source) {
        return !isCactus(source) && super.canBeHurtBy(source);
    }

    private boolean isCactus(DamageSource source) {
        return source == DamageSource.CACTUS;
    }
}
