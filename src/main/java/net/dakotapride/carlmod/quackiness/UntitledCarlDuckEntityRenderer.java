package net.dakotapride.carlmod.quackiness;

import net.dakotapride.carlmod.CarlMod;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class UntitledCarlDuckEntityRenderer extends GeoEntityRenderer<UntitledCarl> {
    public UntitledCarlDuckEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new UntitledCarlDuckEntityModel());
        this.shadowRadius = 0.3f;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull UntitledCarl instance) {
        return ResourceLocation.fromNamespaceAndPath(CarlMod.MOD_ID, "textures/entity/untitled_duck.png");
    }
}