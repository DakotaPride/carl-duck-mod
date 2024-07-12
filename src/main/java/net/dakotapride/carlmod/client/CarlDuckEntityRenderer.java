package net.dakotapride.carlmod.client;

import net.dakotapride.carlmod.CarlDuckEntity;
import net.dakotapride.carlmod.CarlMod;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib3.renderers.geo.GeoEntityRenderer;

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

                : new ResourceLocation(CarlMod.MODID, "textures/entity/carl.png");
    }
}