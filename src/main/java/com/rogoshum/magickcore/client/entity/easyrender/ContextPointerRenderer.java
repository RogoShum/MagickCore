package com.rogoshum.magickcore.client.entity.easyrender;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.rogoshum.magickcore.client.entity.easyrender.base.EasyRenderer;
import com.rogoshum.magickcore.client.render.BufferContext;
import com.rogoshum.magickcore.client.RenderHelper;
import com.rogoshum.magickcore.client.render.RenderMode;
import com.rogoshum.magickcore.client.render.RenderParams;
import com.rogoshum.magickcore.common.entity.pointed.ContextPointerEntity;
import com.rogoshum.magickcore.common.lib.LibShaders;
import com.rogoshum.magickcore.common.magick.context.SpellContext;
import com.rogoshum.magickcore.common.extradata.ExtraDataUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.util.math.vector.Quaternion;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

import java.util.HashMap;
import java.util.List;
import java.util.function.Consumer;

public class ContextPointerRenderer extends EasyRenderer<ContextPointerEntity> {
    float alpha;
    Quaternion rotate;
    RenderType TYPE = RenderHelper.getTexedCylinderGlint(wind, entity.getHeight(), 0f);

    public ContextPointerRenderer(ContextPointerEntity entity) {
        super(entity);
    }

    @Override
    public void update() {
        super.update();
        alpha = 0.5f - (float)entity.ticksExisted % 100 / 100f;
        alpha *= alpha * 4;
        if(alpha < 0.8f)
            alpha = 0.8f;
        float c = entity.ticksExisted % 30;
        rotate = Vector3f.YP.rotationDegrees(360f * (c / 29));
        TYPE = RenderHelper.getTexedCylinderGlint(wind, entity.getHeight(), 0f);
    }

    public void renderItems(RenderParams params) {
        MatrixStack matrixStackIn = params.matrixStack;
        baseOffset(matrixStackIn);
        float partialTicks = Minecraft.getInstance().getRenderPartialTicks();
        List<ContextPointerEntity.PosItem> stacks = entity.getStacks();
        for(int i = 0; i < stacks.size(); i++) {
            ContextPointerEntity.PosItem item = stacks.get(i);
            matrixStackIn.push();
            double x = item.prePos.x + (item.pos.x - item.prePos.x) * (double) partialTicks;
            double y = item.prePos.y + (item.pos.y - item.prePos.y) * (double) partialTicks;
            double z = item.prePos.z + (item.pos.z - item.prePos.z) * (double) partialTicks;
            matrixStackIn.translate(x, y - entity.getHeight() / 2, z);
            float f3 = ((float)item.age + partialTicks) / 20.0F + item.hoverStart;
            matrixStackIn.rotate(Vector3f.YP.rotation(f3));
            IBakedModel ibakedmodel_ = Minecraft.getInstance().getItemRenderer().getItemModelWithOverrides(item.getItemStack(), null, null);
            IRenderTypeBuffer.Impl renderTypeBuffer = IRenderTypeBuffer.getImpl(params.buffer);
            Minecraft.getInstance().getItemRenderer().renderItem(item.getItemStack(), ItemCameraTransforms.TransformType.GROUND, false, matrixStackIn, renderTypeBuffer, RenderHelper.renderLight, OverlayTexture.NO_OVERLAY, ibakedmodel_);
            renderTypeBuffer.finish();
            matrixStackIn.pop();
        }
    }

    public void renderCyclone(RenderParams params) {
        MatrixStack matrixStackIn = params.matrixStack;
        BufferBuilder bufferIn = params.buffer;
        baseOffset(matrixStackIn);
        matrixStackIn.rotate(rotate);
        RenderHelper.CylinderContext context =
                new RenderHelper.CylinderContext(0.25f, 0.25f, 1
                        , 0.2f + entity.getHeight(), 16
                        , 0.5f * alpha, alpha, 0.3f, entity.spellContext().element.color());
        RenderHelper.renderCylinder(BufferContext.create(matrixStackIn, bufferIn, TYPE), context);
    }

    public void renderCylinder(RenderParams params) {
        MatrixStack matrixStackIn = params.matrixStack;
        BufferBuilder bufferIn = params.buffer;
        baseOffset(matrixStackIn);
        matrixStackIn.rotate(rotate);

        float height = entity.getHeight() - 0.2f;
        alpha = 1.0f;
        matrixStackIn.translate(0, 0.2, 0);
        RenderHelper.CylinderContext context = new RenderHelper.CylinderContext(0.5f, 0.3f, 1.5f
                , height, 16
                , 0.3f * alpha, alpha, 0.3f, entity.spellContext().element.color());
        RenderHelper.renderCylinder(BufferContext.create(matrixStackIn, bufferIn, TYPE)
                , context);
        matrixStackIn.translate(0, -0.2, 0);
        context = new RenderHelper.CylinderContext(0.4f, 0.25f, 1.5f
                , height, 16
                , 0.2f * alpha, alpha, 0.3f, entity.spellContext().element.color());
        RenderHelper.renderCylinder(BufferContext.create(matrixStackIn, bufferIn, TYPE)
                , context);
    }

    @Override
    protected void updateSpellContext() {
        Vector3d cam = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
        double camX = cam.x, camY = cam.y, camZ = cam.z;
        Vector3d offset = cam.subtract(x, y, z).normalize().scale(entity.getWidth() * 0.5);
        debugX = x - camX + offset.x;
        debugY = y - camY + entity.getHeight() * 0.5 + offset.y;
        debugZ = z - camZ + offset.z;

        SpellContext context = SpellContext.create();
        if(entity.getStacks().size() < 1) {
            debugSpellContext = new String[]{};
            return;
        }
        if(entity.getStacks().size() < 2) {
            context.copy(ExtraDataUtil.itemManaData(entity.getStacks().get(0).getItemStack()).spellContext());
        } else {
            context.copy(ExtraDataUtil.itemManaData(entity.getStacks().get(entity.getStacks().size() - 1).getItemStack()).spellContext());
            for(int i = entity.getStacks().size() - 2; i > -1; --i) {
                SpellContext origin = ExtraDataUtil.itemManaData(entity.getStacks().get(i).getItemStack()).spellContext().copy();
                SpellContext post = origin;
                while (post.postContext != null)
                    post = post.postContext;
                post.post(context);
                context = origin;
            }
        }

        String information = context.toString();
        debugSpellContext = information.split("\n");
        contextLength = 0;
        for (String s : debugSpellContext) {
            if (s.length() > contextLength)
                contextLength = s.length();
        }
    }

    @Override
    public HashMap<RenderMode, Consumer<RenderParams>> getRenderFunction() {
        HashMap<RenderMode, Consumer<RenderParams>> map = new HashMap<>();
        map.put(RenderMode.ORIGIN_RENDER, this::renderItems);
        map.put(new RenderMode(TYPE, LibShaders.slime), this::renderCyclone);
        map.put(new RenderMode(TYPE, LibShaders.opacity), this::renderCylinder);
        return map;
    }
}
