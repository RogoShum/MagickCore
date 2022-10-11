package com.rogoshum.magickcore.item;

import com.rogoshum.magickcore.MagickCore;
import com.rogoshum.magickcore.block.tileentity.ElementCrystalTileEntity;
import com.rogoshum.magickcore.lib.LibItem;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;

import javax.annotation.Nullable;
import java.util.List;

public class ElementSeedsItem extends BlockItem{
    public ElementSeedsItem(Block blockIn, Properties builder) {
        super(blockIn, builder);
    }

    @Override
    public void addInformation(ItemStack stack, @Nullable World worldIn, List<ITextComponent> tooltip, ITooltipFlag flagIn) {
        if(stack.hasTag()) {
            CompoundNBT tag = stack.getTag();
            if(tag.contains("ELEMENT"))
                tooltip.add((new TranslationTextComponent(LibItem.ELEMENT)).appendString(" ").append((new TranslationTextComponent(MagickCore.MOD_ID + ".description." + tag.getString("ELEMENT")))));
        }
    }

    @Override
    public String getTranslationKey() {
        return this.getDefaultTranslationKey();
    }

    @Override
    protected boolean onBlockPlaced(BlockPos pos, World worldIn, @Nullable PlayerEntity player, ItemStack stack, BlockState state) {
        if(stack.hasTag() && stack.getTag().contains("ELEMENT")) {
            ElementCrystalTileEntity crystal = (ElementCrystalTileEntity) worldIn.getTileEntity(pos);
            crystal.eType = stack.getTag().getString("ELEMENT");
        }
        return super.onBlockPlaced(pos, worldIn, player, stack, state);
    }

    @Override
    public void fillItemGroup(ItemGroup group, NonNullList<ItemStack> items) {
    }
}
