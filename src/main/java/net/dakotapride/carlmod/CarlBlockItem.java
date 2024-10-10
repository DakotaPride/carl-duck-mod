package net.dakotapride.carlmod;

import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.level.block.Block;

public class CarlBlockItem extends BlockItem {
    public CarlBlockItem(Block pBlock, Properties pProperties) {
        super(pBlock, pProperties);
    }

    @Override
    public boolean canBeHurtBy(DamageSource pDamageSource) {
        return !pDamageSource.is(DamageTypes.CACTUS) && super.canBeHurtBy(pDamageSource);
    }
}
