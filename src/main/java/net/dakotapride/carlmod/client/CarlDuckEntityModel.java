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
        } else if ("lamb".equalsIgnoreCase(object.getName().getString()) || "cult_leader".equalsIgnoreCase(object.getName().getString())) {
            return new ResourceLocation(CarlMod.MODID, "geo/carl_cult_leader.geo.json");
        } else if ("braincell".equalsIgnoreCase(object.getName().getString()) || "braincells".equalsIgnoreCase(object.getName().getString())) {
            return new ResourceLocation(CarlMod.MODID, "geo/braincells.geo.json");
        } else if ("hooty".equalsIgnoreCase(object.getName().getString()) || "hootsifer".equalsIgnoreCase(object.getName().getString())) {
            return new ResourceLocation(CarlMod.MODID, "geo/hoot.geo.json");
        } else if ("bok_choy".equalsIgnoreCase(object.getName().getString()) || "bok_choyo".equalsIgnoreCase(object.getName().getString())) {
            return new ResourceLocation(CarlMod.MODID, "geo/bok_choy.geo.json");
        } else if ("egg".equalsIgnoreCase(object.getName().getString())) {
            return new ResourceLocation(CarlMod.MODID, "geo/egg.geo.json");
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
        } else if ("lamb".equalsIgnoreCase(animatable.getName().getString()) || "cult_leader".equalsIgnoreCase(animatable.getName().getString())) {
            return new ResourceLocation(CarlMod.MODID, "animations/carl_cult_leader.animation.json");
        } else if ("hooty".equalsIgnoreCase(animatable.getName().getString()) || "hootsifer".equalsIgnoreCase(animatable.getName().getString())) {
            return new ResourceLocation(CarlMod.MODID, "animations/hoot.animation.json");
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
