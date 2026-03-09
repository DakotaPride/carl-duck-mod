package net.dakotapride.carlmod.quackiness;


import net.dakotapride.carlmod.CarlMod;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class EggCarlDuckEntityModel extends GeoModel<EggCarl> {
    @Override
    public ResourceLocation getModelResource(EggCarl object) {
        return ResourceLocation.fromNamespaceAndPath(CarlMod.MOD_ID, "geo/carl.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(EggCarl object) {
        return ResourceLocation.fromNamespaceAndPath(CarlMod.MOD_ID, "textures/entity/egg.png");
    }

    @Override
    public ResourceLocation getAnimationResource(EggCarl animatable) {
        return ResourceLocation.fromNamespaceAndPath(CarlMod.MOD_ID, "animations/carl.animation.json");
    }

    @Override
    public void setCustomAnimations(EggCarl entity, long instanceId, AnimationState<EggCarl> customPredicate) {
        super.setCustomAnimations(entity, instanceId, customPredicate);
        GeoBone head = this.getAnimationProcessor().getBone("head");
        EntityModelData extraData = customPredicate.getData(DataTickets.ENTITY_MODEL_DATA);
        if (!customPredicate.isMoving()) {
            head.setRotX(extraData.headPitch() * ((float) Math.PI / 180F));
            head.setRotY(extraData.netHeadYaw() * ((float) Math.PI / 180F));
        }
    }

}
