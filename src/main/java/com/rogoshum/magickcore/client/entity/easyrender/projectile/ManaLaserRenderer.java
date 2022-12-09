package com.rogoshum.magickcore.client.entity.easyrender.projectile;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.rogoshum.magickcore.MagickCore;
import com.rogoshum.magickcore.client.RenderHelper;
import com.rogoshum.magickcore.client.entity.easyrender.base.EasyRenderer;
import com.rogoshum.magickcore.client.render.BufferContext;
import com.rogoshum.magickcore.client.render.RenderMode;
import com.rogoshum.magickcore.client.render.RenderParams;
import com.rogoshum.magickcore.common.entity.projectile.ManaLaserEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

import java.util.HashMap;
import java.util.function.Consumer;

public class ManaLaserRenderer extends EasyRenderer<ManaLaserEntity> {
    private static final ResourceLocation LASER_TOP = new ResourceLocation(MagickCore.MOD_ID,  "textures/laser/laser_top.png");
    private static final ResourceLocation LASER_MID = new ResourceLocation(MagickCore.MOD_ID,  "textures/laser/laser_mid.png");
    private static final ResourceLocation LASER_BOTTOM = new ResourceLocation(MagickCore.MOD_ID,  "textures/laser/laser_bottom.png");
    private float length;

    public ManaLaserRenderer(ManaLaserEntity entity) {
        super(entity);
    }

    @Override
    public void baseOffset(MatrixStack matrixStackIn) {
        super.baseOffset(matrixStackIn);
        Vector3d dir = entity.getMotion().scale(-1).normalize();
        Vector2f rota = getRotationFromVector(dir);
        float scale = 0.5f * entity.getWidth();
        double length = this.length * scale;
        matrixStackIn.translate(dir.x * length - dir.x * entity.getWidth(), dir.y * length - dir.y * entity.getWidth(), dir.z * length - dir.z * entity.getWidth());
        matrixStackIn.rotate(Vector3f.YP.rotationDegrees(rota.x));
        matrixStackIn.rotate(Vector3f.ZP.rotationDegrees(rota.y));
        matrixStackIn.scale(scale, scale, scale);
    }

    @Override
    public void update() {
        super.update();
        length = (float) Math.max(entity.getMotion().length() * 30 + 1, 1);
    }

    public void renderTop(RenderParams params) {
        baseOffset(params.matrixStack);
        RenderHelper.renderLaserTop(
                BufferContext.create(params.matrixStack, params.buffer, RenderHelper.getTexedLaser(LASER_TOP)),
                new RenderHelper.RenderContext(1.0f, entity.spellContext().element.color(), RenderHelper.renderLight),
                length
        );
    }

    public void renderMid(RenderParams params) {
        baseOffset(params.matrixStack);
        RenderHelper.renderLaserMid(
                BufferContext.create(params.matrixStack, params.buffer, RenderHelper.getTexedLaser(LASER_MID)),
                new RenderHelper.RenderContext(1.0f, entity.spellContext().element.color(), RenderHelper.renderLight),
                length
        );
    }

    public void renderBottom(RenderParams params) {
        baseOffset(params.matrixStack);
        RenderHelper.renderLaserBottom(
                BufferContext.create(params.matrixStack, params.buffer, RenderHelper.getTexedLaser(LASER_BOTTOM)),
                new RenderHelper.RenderContext(1.0f, entity.spellContext().element.color(), RenderHelper.renderLight),
                length
        );
    }

    @Override
    public HashMap<RenderMode, Consumer<RenderParams>> getRenderFunction() {
        HashMap<RenderMode, Consumer<RenderParams>> map = new HashMap<>();
        map.put(new RenderMode(RenderHelper.getTexedLaser(LASER_TOP)), this::renderTop);
        map.put(new RenderMode(RenderHelper.getTexedLaser(LASER_MID)), this::renderMid);
        map.put(new RenderMode(RenderHelper.getTexedLaser(LASER_BOTTOM)), this::renderBottom);
        return map;
    }
}