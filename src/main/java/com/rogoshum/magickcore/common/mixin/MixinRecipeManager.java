package com.rogoshum.magickcore.common.mixin;

import com.google.common.collect.ImmutableMap;
import com.google.gson.JsonElement;
import com.rogoshum.magickcore.common.api.event.RecipeLoadedEvent;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.RecipeManager;
import net.minecraft.profiler.IProfiler;
import net.minecraft.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Map;

@Mixin(RecipeManager.class)
public class MixinRecipeManager {
    @Inject(method = "apply(Ljava/util/Map;Lnet/minecraft/resources/IResourceManager;Lnet/minecraft/profiler/IProfiler;)V"
            , locals = LocalCapture.CAPTURE_FAILSOFT
            , at = @At(value = "INVOKE", target = "java/util/Map.entrySet ()Ljava/util/Set;", ordinal = 1)
    )
    public void onApply(Map<ResourceLocation, JsonElement> objectIn, IResourceManager resourceManagerIn, IProfiler profilerIn, CallbackInfo ci, Map<IRecipeType<?>, ImmutableMap.Builder<ResourceLocation, IRecipe<?>>> map) {
        MinecraftForge.EVENT_BUS.post(new RecipeLoadedEvent(map));
    }
}