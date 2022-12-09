package com.rogoshum.magickcore.client.entity.easyrender;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.rogoshum.magickcore.MagickCore;
import com.rogoshum.magickcore.client.RenderHelper;
import com.rogoshum.magickcore.client.element.ElementRenderer;
import com.rogoshum.magickcore.client.entity.easyrender.base.EasyRenderer;
import com.rogoshum.magickcore.client.render.BufferContext;
import com.rogoshum.magickcore.client.render.RenderMode;
import com.rogoshum.magickcore.client.render.RenderParams;
import com.rogoshum.magickcore.client.render.SingleBuffer;
import com.rogoshum.magickcore.common.entity.pointed.SpinEntity;
import com.rogoshum.magickcore.common.entity.projectile.PhantomEntity;
import com.rogoshum.magickcore.common.lib.LibContext;
import com.rogoshum.magickcore.common.magick.context.child.DirectionContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.vector.Vector2f;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

import java.util.HashMap;
import java.util.function.Consumer;

public class SpinRenderer extends EasyRenderer<SpinEntity> {
    RenderType SPHERE = RenderHelper.getTexedCylinderGlint(sphere_rotate, 1f, 0f);
    float preRotate;
    float postRotate;
    float rotate;
    ElementRenderer renderer;
    private static final ResourceLocation LASER_TOP = new ResourceLocation(MagickCore.MOD_ID,  "textures/laser/ray_top.png");
    private static final ResourceLocation LASER_MID = new ResourceLocation(MagickCore.MOD_ID,  "textures/laser/ray_mid.png");
    private static final ResourceLocation LASER_BOTTOM = new ResourceLocation(MagickCore.MOD_ID,  "textures/laser/ray_bottom.png");
    private float length;

    public SpinRenderer(SpinEntity entity) {
        super(entity);
    }

    @Override
    public void update() {
        super.update();
        preRotate = entity.ticksExisted % 5;
        postRotate = (entity.ticksExisted + 1) % 5;
        rotate = MathHelper.lerp(Minecraft.getInstance().getRenderPartialTicks(), preRotate, postRotate);
        renderer = entity.spellContext().element.getRenderer();
        length = entity.spellContext().range * 5.5f;
    }

    public void render(RenderParams params) {
        MatrixStack matrixStackIn = params.matrixStack;
        BufferBuilder bufferIn = params.buffer;
        baseOffset(matrixStackIn);
        matrixStackIn.push();
        matrixStackIn.rotate(Vector3f.YN.rotationDegrees(360f * (rotate / 4)));

        RenderHelper.CylinderContext context = new RenderHelper.CylinderContext(0.5f, 0.25f, 2
                , 0.5f, 6
                , 0.1f, 1.0f, 0.3f, renderer.getColor());

        RenderHelper.renderCylinder(BufferContext.create(matrixStackIn, bufferIn, RenderHelper.getLineStripGlow(5.0f))
                , context);
        matrixStackIn.pop();
    }

    public void offsetLaser(RenderParams params) {
        baseOffset(params.matrixStack);
        Vector3d dir = Vector3d.ZERO;
        if(entity.spellContext().containChild(LibContext.DIRECTION))
            dir = entity.spellContext().<DirectionContext>getChild(LibContext.DIRECTION).direction.normalize().scale(-1);

        Vector2f rota = getRotationFromVector(dir);
        float scale = 0.1f;
        params.matrixStack.rotate(Vector3f.YP.rotationDegrees(rota.x));
        params.matrixStack.rotate(Vector3f.ZP.rotationDegrees(rota.y));
        params.matrixStack.scale(scale, scale, scale);
    }

    public void renderTop(RenderParams params) {
        Vector3d cam = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
        double camX = cam.x, camY = cam.y, camZ = cam.z;
        Vector3d pos = entity.getHitPoint();
        params.matrixStack.translate(pos.x - camX, pos.y - camY + entity.getHeight() * 0.5, pos.z - camZ);
        RenderHelper.renderCube(BufferContext.create(params.matrixStack, params.buffer, RenderHelper.getLineStripGlow(2.0)), new RenderHelper.RenderContext(1.0f, renderer.getColor(), RenderHelper.renderLight));
    }

    public void renderMid(RenderParams params) {
        offsetLaser(params);
        RenderHelper.renderLaserParticle(
                BufferContext.create(params.matrixStack, params.buffer, RenderHelper.getLinesGlow(2.0)),
                new RenderHelper.RenderContext(0.1f, entity.spellContext().element.color(), RenderHelper.renderLight),
                RenderHelper.EmptyVertexContext, length
        );
    }

    public void renderBottom(RenderParams params) {
        offsetLaser(params);
        RenderHelper.renderLaserBottom(
                BufferContext.create(params.matrixStack, params.buffer, RenderHelper.getTexedLaser(LASER_BOTTOM)),
                new RenderHelper.RenderContext(0.1f, entity.spellContext().element.color(), RenderHelper.renderLight),
                length
        );
    }

    @Override
    public HashMap<RenderMode, Consumer<RenderParams>> getRenderFunction() {
        HashMap<RenderMode, Consumer<RenderParams>> map = new HashMap<>();
        //map.put(new RenderMode(RenderHelper.getTexedLaser(LASER_TOP), RenderMode.ShaderList.SLIME_SHADER), this::renderTop);
        //map.put(new RenderMode(RenderHelper.getLineLoopGlow(2.0)), this::renderMid);
        //map.put(new RenderMode(RenderHelper.getTexedLaser(LASER_BOTTOM), RenderMode.ShaderList.DISTORTION_SMALL_SHADER), this::renderBottom);
        map.put(new RenderMode(SPHERE), this::render);
        return map;
    }
}
