package net.dakotapride.carlmod.quackiness;

import com.mojang.blaze3d.vertex.PoseStack;
import net.dakotapride.carlmod.CarlDuckEntity;
import net.dakotapride.carlmod.CarlMod;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.renderer.GeoEntityRenderer;

public class BiblicallyAccurateCarlDuckEntityRenderer extends GeoEntityRenderer<BiblicallyAccurateCarlBoss> {
    public BiblicallyAccurateCarlDuckEntityRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager, new BiblicallyAccurateCarlDuckEntityModel());
        this.shadowRadius = 0.3f;
    }

    @Override
    public @NotNull ResourceLocation getTextureLocation(@NotNull BiblicallyAccurateCarlBoss instance) {
        return ResourceLocation.fromNamespaceAndPath(CarlMod.MOD_ID, "textures/entity/biblically_accurate_carl.png");
    }

    @Override
    public void render(BiblicallyAccurateCarlBoss entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {

        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }
}