package com.rogoshum.magickcore.mixin.fabric.reflection;

import net.minecraft.client.renderer.RenderStateShard;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

import java.util.Optional;

@Mixin(targets = "net.minecraft.client.renderer.RenderType$CompositeRenderType")
public class MixinRenderType implements ICompositeType{

    @Shadow @Final private RenderType.CompositeState state;

    @Override
    public RenderType.CompositeState getState() {
        return this.state;
    }

}
