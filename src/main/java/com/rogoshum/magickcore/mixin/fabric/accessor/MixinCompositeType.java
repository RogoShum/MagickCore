package com.rogoshum.magickcore.mixin.fabric.accessor;

import net.minecraft.client.renderer.RenderType;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(targets = "net.minecraft.client.renderer.RenderType$CompositeRenderType")
public interface MixinCompositeType {
    @Accessor("state")
    RenderType.CompositeState getState();
}
