package com.rogoshum.magickcore.mixin.fabric.registry;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionBrewing;
import net.minecraft.world.level.levelgen.Heightmap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(PotionBrewing.class)
public abstract class MixinPotionBrewing {
    @Shadow
    private static void addMix(Potion potion, Item item, Potion potion2) {
    }

    protected static void add(Potion potion, Item item, Potion potion2) {
        addMix(potion, item, potion2);
    }
}
