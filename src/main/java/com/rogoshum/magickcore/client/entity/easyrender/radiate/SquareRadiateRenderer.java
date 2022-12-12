package com.rogoshum.magickcore.client.entity.easyrender.radiate;

import com.rogoshum.magickcore.client.RenderHelper;
import com.rogoshum.magickcore.client.entity.easyrender.base.EasyRenderer;
import com.rogoshum.magickcore.client.render.BufferContext;
import com.rogoshum.magickcore.client.render.RenderMode;
import com.rogoshum.magickcore.client.render.RenderParams;
import com.rogoshum.magickcore.common.entity.radiated.SphereEntity;
import com.rogoshum.magickcore.common.entity.radiated.SquareEntity;
import com.rogoshum.magickcore.common.magick.Color;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.WorldRenderer;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.math.vector.Vector3f;

import java.util.HashMap;
import java.util.function.Consumer;

public class SquareRadiateRenderer extends EasyRenderer<SquareEntity> {
    float scale;
    private static final RenderType TYPE = RenderHelper.getLineStripPC(5);

    public SquareRadiateRenderer(SquareEntity entity) {
        super(entity);
    }

    public void render(RenderParams params) {
        Color color = entity.spellContext().element.color();
        Vector3d cam = Minecraft.getInstance().gameRenderer.getActiveRenderInfo().getProjectedView();
        WorldRenderer.drawBoundingBox(params.matrixStack, params.buffer, entity.getBoundingBox().grow(scale * 0.5).offset(-cam.x, -cam.y, -cam.z), color.r(), color.g(), color.b(), 1.0F);
    }

    @Override
    public boolean forceRender() {
        return entity.isAlive();
    }

    @Override
    public void update() {
        super.update();
        scale = entity.spellContext().range;
    }

    @Override
    public HashMap<RenderMode, Consumer<RenderParams>> getRenderFunction() {
        HashMap<RenderMode, Consumer<RenderParams>> map = new HashMap<>();
        map.put(new RenderMode(TYPE), this::render);
        return map;
    }
}
