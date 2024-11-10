package net.dakotapride.carlmod.client;

import net.dakotapride.carlmod.CarlDuckEntity;
import net.dakotapride.carlmod.CarlMod;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class CarlDuckEntityRenderer extends GeoEntityRenderer<CarlDuckEntity> {
    public CarlDuckEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new CarlDuckEntityModel());
        this.shadowRadius = 0.3f;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull CarlDuckEntity instance) {
        return instance.hasCustomName() &&

                ("awsome".equalsIgnoreCase(instance.getName().getString()) || "carltheawsome".equalsIgnoreCase(instance.getName().getString())) ?
                new ResourceLocation(CarlMod.MODID, "textures/entity/carltheawsome_1.png")

                : ("dejojo".equalsIgnoreCase(instance.getName().getString()) || "dejojotheawsome".equalsIgnoreCase(instance.getName().getString())) ?
                new ResourceLocation(CarlMod.MODID, "textures/entity/carltheawsome.png")

                : ("garnished".equalsIgnoreCase(instance.getName().getString())) ?
                new ResourceLocation(CarlMod.MODID, "textures/entity/garnished.png")

                : ("bare_bones".equalsIgnoreCase(instance.getName().getString()) || "barebones".equalsIgnoreCase(instance.getName().getString())) ?
                new ResourceLocation(CarlMod.MODID, "textures/entity/barebones.png")

                : ("mekanism".equalsIgnoreCase(instance.getName().getString()) || "mekanized".equalsIgnoreCase(instance.getName().getString())) ?
                new ResourceLocation(CarlMod.MODID, "textures/entity/mekanism.png")

                : ("create".equalsIgnoreCase(instance.getName().getString())) ?
                new ResourceLocation(CarlMod.MODID, "textures/entity/create.png")

                : ("adorable".equalsIgnoreCase(instance.getName().getString()) || "deaudie".equalsIgnoreCase(instance.getName().getString()) || "audrey".equalsIgnoreCase(instance.getName().getString())) ?
                new ResourceLocation(CarlMod.MODID, "textures/entity/pink.png")

                : ("dragon".equalsIgnoreCase(instance.getName().getString()) || "ender_dragon".equalsIgnoreCase(instance.getName().getString()) || "jean".equalsIgnoreCase(instance.getName().getString())) ?
                new ResourceLocation(CarlMod.MODID, "textures/entity/dragon.png")

                : ("god".equalsIgnoreCase(instance.getName().getString()) || "angel".equalsIgnoreCase(instance.getName().getString())
                || "biblically_accurate".equalsIgnoreCase(instance.getName().getString()) || "biblically_accurate_carl".equalsIgnoreCase(instance.getName().getString())) ?
                new ResourceLocation(CarlMod.MODID, "textures/entity/biblically_accurate_carl.png")

                : ("lamb".equalsIgnoreCase(instance.getName().getString()) || "cult_leader".equalsIgnoreCase(instance.getName().getString())) ?
                new ResourceLocation(CarlMod.MODID, "textures/entity/lamb.png")

                : ("steel_spacesuit".equalsIgnoreCase(instance.getName().getString()) || "steel_space_suit".equalsIgnoreCase(instance.getName().getString())) ?
                new ResourceLocation(CarlMod.MODID, "textures/entity/steel_spacesuit.png")

                : ("calorite_spacesuit".equalsIgnoreCase(instance.getName().getString()) || "calorite_space_suit".equalsIgnoreCase(instance.getName().getString())) ?
                new ResourceLocation(CarlMod.MODID, "textures/entity/calorite_spacesuit.png")

                : ("netherite_spacesuit".equalsIgnoreCase(instance.getName().getString()) || "netherite_space_suit".equalsIgnoreCase(instance.getName().getString())) ?
                new ResourceLocation(CarlMod.MODID, "textures/entity/netherite_spacesuit.png")

                : ("braincell".equalsIgnoreCase(instance.getName().getString()) || "braincells".equalsIgnoreCase(instance.getName().getString())) ?
                new ResourceLocation(CarlMod.MODID, "textures/entity/braincells.png")

                : ("copycat".equalsIgnoreCase(instance.getName().getString()) || "copycats".equalsIgnoreCase(instance.getName().getString())) ?
                new ResourceLocation(CarlMod.MODID, "textures/entity/copycats.png")

//                : ("trans_rights".equalsIgnoreCase(instance.getName().getString()) || "transgender".equalsIgnoreCase(instance.getName().getString())) ?
//                new ResourceLocation(CarlMod.MODID, "textures/entity/trans_rights.png")

                : ("helmetless".equalsIgnoreCase(instance.getName().getString())) ?
                new ResourceLocation(CarlMod.MODID, "textures/entity/helmetless_carl.png")

                : ("hooty".equalsIgnoreCase(instance.getName().getString()) || "hootsifer".equalsIgnoreCase(instance.getName().getString())) ?
                new ResourceLocation(CarlMod.MODID, "textures/entity/hoot.png")

                : new ResourceLocation(CarlMod.MODID, "textures/entity/carl.png");
    }

    @Override
    public RenderType getRenderType(CarlDuckEntity animatable, ResourceLocation texture, @Nullable MultiBufferSource bufferSource, float partialTick) {
        if ("steel_spacesuit".equalsIgnoreCase(animatable.getName().getString()) || "calorite_spacesuit".equalsIgnoreCase(animatable.getName().getString()) || "netherite_spacesuit".equalsIgnoreCase(animatable.getName().getString())) {
            return RenderType.entityTranslucent(texture);
        } else return super.getRenderType(animatable, texture, bufferSource, partialTick);
    }
}