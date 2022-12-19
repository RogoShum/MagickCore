package com.rogoshum.magickcore.common.item;

import com.rogoshum.magickcore.MagickCore;
import com.rogoshum.magickcore.client.RenderHelper;
import com.rogoshum.magickcore.api.entity.IManaEntity;
import com.rogoshum.magickcore.api.enums.ApplyType;
import com.rogoshum.magickcore.client.item.ManaEnergyRenderer;
import com.rogoshum.magickcore.common.event.AdvancementsEvent;
import com.rogoshum.magickcore.common.lib.LibAdvancements;
import com.rogoshum.magickcore.common.extradata.entity.EntityStateData;
import com.rogoshum.magickcore.common.init.ModGroups;
import com.rogoshum.magickcore.common.magick.context.child.SpawnContext;
import com.rogoshum.magickcore.common.extradata.ExtraDataUtil;
import com.rogoshum.magickcore.common.magick.context.child.TraceContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;
import net.minecraftforge.registries.ForgeRegistries;

public class MagickContextItem extends ManaItem {
    public MagickContextItem() {
        super(properties().maxStackSize(16).setISTER(() -> ManaEnergyRenderer::new));
    }

    @Override
    public boolean showDurabilityBar(ItemStack stack) {
        return false;
    }

    @Override
    public boolean releaseMagick(LivingEntity playerIn, EntityStateData state, ItemStack stack) {
        /*
                if(stack.hasTag() && playerIn instanceof PlayerEntity) {
            MagickCore.LOGGER.info(stack.getTag().toString());
        }
         */
        return false;
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
        if(group == ModGroups.MAGICK_CONTEXT_GROUP) {
            ItemStack sample = new ItemStack(this);
            for (ApplyType type : ApplyType.values()) {
                if(type == ApplyType.NONE || type == ApplyType.SPAWN_ENTITY || type == ApplyType.POTION || type == ApplyType.HIT_BLOCK || type == ApplyType.HIT_ENTITY || type == ApplyType.ELEMENT_TOOL || type == ApplyType.SUPER)continue;
                ExtraDataUtil.itemManaData(sample, (data) -> {
                    data.spellContext().applyType(type).force(7).range(7).tick(300);
                });
                ItemStack itemStack = sample.copy();
                items.add(itemStack);
            }
            ForgeRegistries.ENTITIES.getEntries().forEach(entityType -> {
                if(entityType.getValue().create(RenderHelper.getPlayer().world) instanceof IManaEntity)
                    fillEntity(items, sample, entityType.getValue());
            });
        }
    }

    public void fillEntity(NonNullList<ItemStack> items, ItemStack stack, EntityType<?> entityType) {
        ItemStack itemStack = stack.copy();
        ExtraDataUtil.itemManaData(itemStack, (data) -> {
            data.spellContext().addChild(SpawnContext.create(entityType));
            data.spellContext().applyType(ApplyType.SPAWN_ENTITY);
        });
        items.add(itemStack);
    }

    @Override
    public void inventoryTick(ItemStack p_77663_1_, World p_77663_2_, Entity p_77663_3_, int p_77663_4_, boolean p_77663_5_) {
        super.inventoryTick(p_77663_1_, p_77663_2_, p_77663_3_, p_77663_4_, p_77663_5_);
        if(p_77663_3_ instanceof ServerPlayerEntity) {
            AdvancementsEvent.STRING_TRIGGER.trigger((ServerPlayerEntity) p_77663_3_, LibAdvancements.MAGICK_CORE);
        }
    }
}
