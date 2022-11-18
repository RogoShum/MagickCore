package com.rogoshum.magickcore.common.block;

import com.rogoshum.magickcore.common.tileentity.ElementCrystalTileEntity;
import com.rogoshum.magickcore.common.init.ModItems;
import net.minecraft.block.BlockState;
import net.minecraft.block.CropsBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Hand;
import net.minecraft.util.IItemProvider;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ElementCrystalBlock extends CropsBlock{
    public ElementCrystalBlock(Properties properties) {
        super(properties);
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    @Override
    public BlockState withAge(int age) {
        return super.withAge(age);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ElementCrystalTileEntity();
    }

    @Override
    protected IItemProvider getSeedsItem() {
        return ModItems.ELEMENT_CRYSTAL_SEEDS.get();
    }

    @Override
    public int getLightValue(BlockState state, IBlockReader world, BlockPos pos) {
        return 10;
    }

    @Override
    public void onBlockHarvested(World worldIn, BlockPos pos, BlockState state, PlayerEntity player) {
        TileEntity tileentity = worldIn.getTileEntity(pos);
        if (tileentity instanceof ElementCrystalTileEntity) {
            ElementCrystalTileEntity tile = (ElementCrystalTileEntity)tileentity;
            tile.dropItem();
        }
        super.onBlockHarvested(worldIn, pos, state, player);
    }

    @Override
    public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit) {
        if(this.isMaxAge(state)) {
            TileEntity tileentity = worldIn.getTileEntity(pos);
            if (tileentity instanceof ElementCrystalTileEntity) {
                ElementCrystalTileEntity tile = (ElementCrystalTileEntity)tileentity;
                tile.dropItem();
            }
            worldIn.setBlockState(pos, getDefaultState());
        }
        return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
    }
}
