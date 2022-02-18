package com.rogoshum.magickcore.client.item;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.rogoshum.magickcore.MagickCore;
import com.rogoshum.magickcore.api.IManaElement;
import com.rogoshum.magickcore.client.BufferPackage;
import com.rogoshum.magickcore.client.RenderHelper;
import com.rogoshum.magickcore.init.ModElements;
import com.rogoshum.magickcore.lib.LibElements;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.client.renderer.tileentity.ItemStackTileEntityRenderer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector3f;

public class ElementWoolTileEntityItemStackRenderer extends ItemStackTileEntityRenderer {
    protected final ResourceLocation blank = new ResourceLocation(MagickCore.MOD_ID + ":textures/blank.png");
    protected final ResourceLocation cylinder_rotate = new ResourceLocation(MagickCore.MOD_ID + ":textures/element/base/cylinder_rotate.png");
    protected final ResourceLocation wool = new ResourceLocation("textures/block/white_wool.png");
    @Override
    public void func_239207_a_(ItemStack stack, ItemCameraTransforms.TransformType transformType, MatrixStack matrixStackIn, IRenderTypeBuffer typeBufferIn, int combinedLight, int combinedOverlay) {
        matrixStackIn.push();
        matrixStackIn.translate(0.5F, 0.5F, 0.5F);
        matrixStackIn.scale(0.31f, 0.31f, 0.31f);
        if(transformType == ItemCameraTransforms.TransformType.GUI) {
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(47.5f));
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(20));
            matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(20));
        }
        else if(transformType == ItemCameraTransforms.TransformType.GROUND)
            matrixStackIn.scale(0.4f, 0.4f, 0.4f);
        else
            matrixStackIn.scale(0.6f, 0.6f, 0.6f);
        float[] color = RenderHelper.ORIGIN;

        if(stack.hasTag())
        {
            CompoundNBT blockTag = stack.getTag();

            String type = blockTag.getString("ELEMENT") == "" ? LibElements.ORIGIN : blockTag.getString("ELEMENT");
            color = MagickCore.proxy.getElementRender(type).getColor();
        }
        BufferBuilder bufferIn = Tessellator.getInstance().getBuffer();
        float alpha = 1.0f;
        if(transformType != ItemCameraTransforms.TransformType.GUI) {
            matrixStackIn.push();
            matrixStackIn.translate(0.0, 0.0, -1.0);
            RenderHelper.renderStaticParticle(BufferPackage.create(matrixStackIn, bufferIn, RenderHelper.getTexedOrbSolid(wool)), alpha, color);
            matrixStackIn.pop();

            matrixStackIn.push();
            matrixStackIn.translate(1.0, 0.0, 0.0);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
            RenderHelper.renderStaticParticle(BufferPackage.create(matrixStackIn, bufferIn, RenderHelper.getTexedOrbSolid(wool)), alpha, color);
            matrixStackIn.pop();

            matrixStackIn.push();
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(270));
            matrixStackIn.translate(0.0, 0.0, -1.0);
            RenderHelper.renderStaticParticle(BufferPackage.create(matrixStackIn, bufferIn, RenderHelper.getTexedOrbSolid(wool)), alpha, color);
            matrixStackIn.pop();
        }
        matrixStackIn.push();
        matrixStackIn.translate(0.0, 0.0, 1.0);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        RenderHelper.renderStaticParticle(BufferPackage.create(matrixStackIn, bufferIn, RenderHelper.getTexedOrbSolid(wool)), alpha, color);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(0.0, 0.0, -1.0);
        RenderHelper.renderStaticParticle(BufferPackage.create(matrixStackIn, bufferIn, RenderHelper.getTexedOrbSolid(wool)), alpha, color);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90));
        matrixStackIn.translate(0.0, 0.0, -1.0);
        RenderHelper.renderStaticParticle(BufferPackage.create(matrixStackIn, bufferIn, RenderHelper.getTexedOrbSolid(wool)), alpha, color);
        matrixStackIn.pop();
        ////////////////////////////////////////////////////////////////////////////////

        alpha = 1.0f;
        matrixStackIn.push();
        matrixStackIn.translate(0.0, 0.0, 1.0);
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(180));
        RenderHelper.renderStaticParticle(BufferPackage.create(matrixStackIn, bufferIn, RenderHelper.getTexedOrbGlint(cylinder_rotate, 1f, 0f)), alpha, color);
        matrixStackIn.pop();

        if(transformType != ItemCameraTransforms.TransformType.GUI) {
            matrixStackIn.push();
            matrixStackIn.translate(0.0, 0.0, -1.0);
            RenderHelper.renderStaticParticle(BufferPackage.create(matrixStackIn, bufferIn, RenderHelper.getTexedOrbGlint(cylinder_rotate, 1f, 0f)), alpha, color);
            matrixStackIn.pop();

            matrixStackIn.push();
            matrixStackIn.translate(1.0, 0.0, 0.0);
            matrixStackIn.rotate(Vector3f.YP.rotationDegrees(270));
            RenderHelper.renderStaticParticle(BufferPackage.create(matrixStackIn, bufferIn, RenderHelper.getTexedOrbGlint(cylinder_rotate, 1f, 0f)), alpha, color);
            matrixStackIn.pop();

            matrixStackIn.push();
            matrixStackIn.rotate(Vector3f.XP.rotationDegrees(270));
            matrixStackIn.translate(0.0, 0.0, -1.0);
            RenderHelper.renderStaticParticle(BufferPackage.create(matrixStackIn, bufferIn, RenderHelper.getTexedOrbGlint(cylinder_rotate, 1f, 0f)), alpha, color);
            matrixStackIn.pop();
        }
        matrixStackIn.push();
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(90));
        matrixStackIn.translate(0.0, 0.0, -1.0);
        RenderHelper.renderStaticParticle(BufferPackage.create(matrixStackIn, bufferIn, RenderHelper.getTexedOrbGlint(cylinder_rotate, 1f, 0f)), alpha, color);
        matrixStackIn.pop();

        matrixStackIn.push();
        matrixStackIn.rotate(Vector3f.XP.rotationDegrees(90));
        matrixStackIn.translate(0.0, 0.0, -1.0);
        RenderHelper.renderStaticParticle(BufferPackage.create(matrixStackIn, bufferIn, RenderHelper.getTexedOrbGlint(cylinder_rotate, 1f, 0f)), alpha, color);
        matrixStackIn.pop();

        matrixStackIn.pop();
    }

    public static CompoundNBT getBlockTag(CompoundNBT tag)
    {
        CompoundNBT blockTag = tag.getCompound("BlockEntityTag");
        return blockTag;
    }
}
