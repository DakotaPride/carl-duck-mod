package net.dakotapride.carlmod.client;


import net.dakotapride.carlmod.CarlDuckEntity;
import net.dakotapride.carlmod.CarlMod;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class CarlDuckEntityModel extends GeoModel<CarlDuckEntity> {
    @Override
    public ResourceLocation getModelResource(CarlDuckEntity object) {
        if ("god".equalsIgnoreCase(object.getName().getString()) || "angel".equalsIgnoreCase(object.getName().getString())
                || "biblically_accurate".equalsIgnoreCase(object.getName().toString()) || "biblically_accurate_carl".equalsIgnoreCase(object.getName().getString())) {
            return new ResourceLocation(CarlMod.MODID, "geo/biblically_accurate_carl.geo.json");
        }
        else return new ResourceLocation(CarlMod.MODID, "geo/carl.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(CarlDuckEntity object) {
        return new ResourceLocation(CarlMod.MODID, "textures/entity/carl.png");
    }

    @Override
    public ResourceLocation getAnimationResource(CarlDuckEntity animatable) {
        if ("god".equalsIgnoreCase(animatable.getName().getString()) || "angel".equalsIgnoreCase(animatable.getName().getString())
                || "biblically_accurate".equalsIgnoreCase(animatable.getName().getString()) || "biblically_accurate_carl".equalsIgnoreCase(animatable.getName().getString())) {
            return new ResourceLocation(CarlMod.MODID, "animations/biblically_accurate_carl.animation.json");
        }
        else return new ResourceLocation(CarlMod.MODID, "animations/carl.animation.json");
    }

    @Override
    public void setCustomAnimations(CarlDuckEntity entity, long instanceId, AnimationState<CarlDuckEntity> customPredicate) {
        super.setCustomAnimations(entity, instanceId, customPredicate);
        CoreGeoBone head = this.getAnimationProcessor().getBone("head");
        EntityModelData extraData = customPredicate.getData(DataTickets.ENTITY_MODEL_DATA);
        if (!customPredicate.isMoving()) {
            head.setRotX(extraData.headPitch() * ((float) Math.PI / 180F));
            head.setRotY(extraData.netHeadYaw() * ((float) Math.PI / 180F));
        }
    }

}
