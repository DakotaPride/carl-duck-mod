package net.dakotapride.carlmod.client;


import net.dakotapride.carlmod.CarlDuckEntity;
import net.dakotapride.carlmod.CarlMod;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib3.core.event.predicate.AnimationEvent;
import software.bernie.geckolib3.core.processor.IBone;
import software.bernie.geckolib3.model.AnimatedGeoModel;
import software.bernie.geckolib3.model.provider.data.EntityModelData;

public class CarlDuckEntityModel extends AnimatedGeoModel<CarlDuckEntity> {
    @Override
    public ResourceLocation getModelResource(CarlDuckEntity object) {
        return new ResourceLocation(CarlMod.MODID, "geo/carl.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CarlDuckEntity object) {
        return new ResourceLocation(CarlMod.MODID, "textures/entity/carl.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CarlDuckEntity animatable) {
        return new ResourceLocation(CarlMod.MODID, "animations/carl.animation.json");
    }

    @Override
    public void setLivingAnimations(CarlDuckEntity entity, Integer uniqueID, AnimationEvent customPredicate) {
        super.setLivingAnimations(entity, uniqueID, customPredicate);
        IBone head = this.getAnimationProcessor().getBone("head");
        EntityModelData extraData = (EntityModelData) customPredicate.getExtraDataOfType(EntityModelData.class).get(0);
        if (!customPredicate.isMoving()) {
            head.setRotationX(extraData.headPitch * ((float) Math.PI / 180F));
            head.setRotationY(extraData.netHeadYaw * ((float) Math.PI / 180F));
        }
    }

}
