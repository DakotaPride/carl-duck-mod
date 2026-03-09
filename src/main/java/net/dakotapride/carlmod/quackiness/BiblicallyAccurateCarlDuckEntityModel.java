package net.dakotapride.carlmod.quackiness;


import net.dakotapride.carlmod.CarlMod;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.animation.AnimationState;
import software.bernie.geckolib.cache.object.GeoBone;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class BiblicallyAccurateCarlDuckEntityModel extends GeoModel<BiblicallyAccurateCarlBoss> {
    @Override
    public ResourceLocation getModelResource(BiblicallyAccurateCarlBoss object) {
        return ResourceLocation.fromNamespaceAndPath(CarlMod.MOD_ID, "geo/biblically_accurate_carl.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(BiblicallyAccurateCarlBoss object) {
        return ResourceLocation.fromNamespaceAndPath(CarlMod.MOD_ID, "textures/entity/biblically_accurate_carl.png");
    }

    @Override
    public ResourceLocation getAnimationResource(BiblicallyAccurateCarlBoss animatable) {
        return ResourceLocation.fromNamespaceAndPath(CarlMod.MOD_ID, "animations/biblically_accurate_carl.animation.json");
    }

    @Override
    public void setCustomAnimations(BiblicallyAccurateCarlBoss entity, long instanceId, AnimationState<BiblicallyAccurateCarlBoss> customPredicate) {
        super.setCustomAnimations(entity, instanceId, customPredicate);
        GeoBone head = this.getAnimationProcessor().getBone("head");
        EntityModelData extraData = customPredicate.getData(DataTickets.ENTITY_MODEL_DATA);
        if (!customPredicate.isMoving()) {
            head.setRotX(extraData.headPitch() * ((float) Math.PI / 180F));
            head.setRotY(extraData.netHeadYaw() * ((float) Math.PI / 180F));
        }
    }

}
