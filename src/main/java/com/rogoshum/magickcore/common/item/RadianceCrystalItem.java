package com.rogoshum.magickcore.common.item;

import com.rogoshum.magickcore.MagickCore;
import com.rogoshum.magickcore.api.enums.ApplyType;
import com.rogoshum.magickcore.api.registry.MagickRegistry;
import com.rogoshum.magickcore.client.item.OrbBottleRenderer;
import com.rogoshum.magickcore.client.item.RadianceCrystalRenderer;
import com.rogoshum.magickcore.common.init.ModBlocks;
import com.rogoshum.magickcore.common.init.ModGroups;
import com.rogoshum.magickcore.common.lib.LibItem;
import com.rogoshum.magickcore.common.lib.LibRegistry;
import com.rogoshum.magickcore.common.tileentity.ElementWoolTileEntity;
import com.rogoshum.magickcore.common.tileentity.RadianceCrystalTileEntity;
import com.rogoshum.magickcore.common.util.NBTTagHelper;
import net.minecraft.client.renderer.BlockEntityWithoutLevelRenderer;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TranslatableComponent;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.IItemRenderProperties;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Consumer;

public class RadianceCrystalItem extends BlockItem {
    public RadianceCrystalItem() {
        super(ModBlocks.RADIANCE_CRYSTAL.get(), BaseItem.properties());
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flagIn) {
        if(stack.hasTag()) {
            CompoundTag tag = stack.getTag();
            if(tag.contains("ELEMENT")) {
                tooltip.add((new TranslatableComponent(LibItem.ELEMENT)).append(" ").append((new TranslatableComponent(MagickCore.MOD_ID + ".description." + tag.getString("ELEMENT")))));
                tooltip.add((new TranslatableComponent(LibItem.FUNCTION)).append(" ").append((new TranslatableComponent(MagickCore.MOD_ID + ".description.radiance." + tag.getString("ELEMENT")))));
            }
        }
    }

    @Override
    public void initializeClient(Consumer<IItemRenderProperties> consumer) {
        consumer.accept(new IItemRenderProperties() {
            @Override
            public BlockEntityWithoutLevelRenderer getItemStackRenderer() {
                return new RadianceCrystalRenderer();
            }
        });
    }

    @Override
    protected boolean updateCustomBlockEntityTag(BlockPos pos, Level worldIn, @Nullable Player player, ItemStack stack, BlockState state) {
        if(stack.hasTag()) {
            RadianceCrystalTileEntity crystal = (RadianceCrystalTileEntity) worldIn.getBlockEntity(pos);
            if(stack.getTag().contains("ELEMENT"))
                crystal.setElement(MagickRegistry.getElement(stack.getTag().getString("ELEMENT")));
            if(stack.getTag().contains("APPLY_TYPE"))
                crystal.setApplyType(ApplyType.getEnum(stack.getTag().getString("APPLY_TYPE")));
        }
        return super.updateCustomBlockEntityTag(pos, worldIn, player, stack, state);
    }

    @Override
    public void fillItemCategory(CreativeModeTab group, NonNullList<ItemStack> items) {
        if(group == ModGroups.ELEMENT_ITEM_GROUP) {
            MagickRegistry.getRegistry(LibRegistry.ELEMENT).registry().forEach( (key, value) ->
                    items.add(NBTTagHelper.setElement(new ItemStack(this), key))
            );
        }
    }
}