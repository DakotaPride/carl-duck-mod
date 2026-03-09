package net.dakotapride.carlmod.quackiness;

import net.dakotapride.carlmod.CarlMod;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class EggCarlDuckEntityRenderer extends GeoEntityRenderer<EggCarl> {
    public EggCarlDuckEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new EggCarlDuckEntityModel());
        this.shadowRadius = 0.3f;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull EggCarl instance) {
        return ResourceLocation.fromNamespaceAndPath(CarlMod.MOD_ID, "textures/entity/egg.png");
    }
}