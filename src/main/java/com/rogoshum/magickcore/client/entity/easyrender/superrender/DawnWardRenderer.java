package com.rogoshum.magickcore.client.entity.easyrender.superrender;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.rogoshum.magickcore.MagickCore;
import com.rogoshum.magickcore.client.BufferContext;
import com.rogoshum.magickcore.client.RenderHelper;
import com.rogoshum.magickcore.client.VectorHitReaction;
import com.rogoshum.magickcore.client.entity.easyrender.EasyRenderer;
import com.rogoshum.magickcore.entity.superentity.DawnWardEntity;
import com.rogoshum.magickcore.lib.LibShaders;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Matrix4f;

public class DawnWardRenderer extends EasyRenderer<DawnWardEntity> {
    private static ResourceLocation blankTex = new ResourceLocation(MagickCore.MOD_ID + ":textures/blank.png");
    private float scale_ = 1;
    private boolean direction = false;

    @Override
    public void render(DawnWardEntity entityIn, MatrixStack matrixStackIn, BufferBuilder bufferIn, float partialTicks) {
        //int packedLightIn = Minecraft.getInstance().getRenderManager().getPackedLight(entityIn, partialTicks);
        int packedLightIn = RenderHelper.renderLight;
        Matrix4f positionMatrix = matrixStackIn.getLast().getMatrix();
        if(entityIn.spellContext().element != null && entityIn.spellContext().element.getRenderer() != null) {
            //EasyRenderer.renderRift(matrixStackIn, bufferIn.getBuffer(RenderHelper.ORB), entityIn, 6.0f, entityIn.getElement().getRenderer().getColor()
                    //, 2.0f, partialTicks, entityIn.world);
            if(entityIn.initial) {
                float scale = Math.min(1f, (float) (entityIn.ticksExisted - 25) / 5f) * entityIn.getWidth();
                //scale *= scale_;
                matrixStackIn.scale(scale, scale, scale);
                VectorHitReaction[] test = {};
                //MagickCore.LOGGER.debug(!direction);
                RenderHelper.renderSphere(BufferContext.create(matrixStackIn, bufferIn,
                        RenderHelper.getTexedSphereGlow(RenderHelper.ripple_5, 3f, 0f)).useShader(LibShaders.slime),
                        16, 0.6f, entityIn.getHitReactions(), entityIn.spellContext().element.color(), packedLightIn, 0.3f);
                /*
                RenderHelper.renderSphere(BufferPackage.create(matrixStackIn, bufferIn,
                        RenderHelper.getTexedSphereGlow(RenderHelper.ripple_5, 3f, 0f))
                        , 16, 1.0f, entityIn.getHitReactions(), entityIn.spellContext().element.color(), packedLightIn, 0.3f);
                 */
                matrixStackIn.scale(0.99f, 0.99f, 0.99f);
                RenderHelper.renderSphere(BufferContext.create(matrixStackIn, bufferIn,
                        RenderHelper.getTexedSphereGlow(blank, 1f, 0f)),
                        16, 0.3f, entityIn.getHitReactions(), entityIn.spellContext().element.color(), packedLightIn, 0.5f);

                if(!direction && scale_ <= 1.25) {
                    scale_ += 0.01;
                    if(scale_ >= 1.25)
                        direction = true;
                }
                if(direction && scale_ >= 1) {
                    scale_ -= 0.02;
                    if(scale_ <= 1)
                        direction = false;
                }
            }
        }
    }
}
