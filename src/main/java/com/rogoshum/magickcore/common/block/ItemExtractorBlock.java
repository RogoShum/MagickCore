package com.rogoshum.magickcore.common.block;

import com.rogoshum.magickcore.common.init.ModBlocks;
import com.rogoshum.magickcore.common.tileentity.ItemExtractorTileEntity;
import net.minecraft.block.*;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.IntegerProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.AttachFace;
import net.minecraft.state.properties.BlockStateProperties;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class ItemExtractorBlock extends BaseBlock {
    public static final DirectionProperty FACING = DirectionalBlock.FACING;
    public static final IntegerProperty POWER = BlockStateProperties.POWER_0_15;
    public ItemExtractorBlock(Properties properties) {
        super(properties);
        this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.UP).with(POWER, 0));
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

    public BlockState rotate(BlockState state, Rotation rot) {
        return state.with(FACING, rot.rotate(state.get(FACING)));
    }

    public BlockState mirror(BlockState state, Mirror mirrorIn) {
        return state.rotate(mirrorIn.toRotation(state.get(FACING)));
    }

    public static BlockState withPower(BlockState state, int power) {
        if(power > 15)
            power = 15;
        if(state.get(POWER) == power)
            return state;
        return state.with(POWER, power);
    }

    public static void updatePower(World world, BlockPos pos, BlockState state, int power) {
        BlockState newState = withPower(state, power);
        if(newState != state) {
            world.setBlockState(pos, newState);
            updateNeighbors(newState, world, pos);
        }
    }

    public BlockState getStateForPlacement(BlockItemUseContext context) {
        return this.getDefaultState().with(FACING, context.getNearestLookingDirection().getOpposite());
    }

    private static void updateNeighbors(BlockState state, World world, BlockPos pos) {
        world.notifyNeighborsOfStateChange(pos, ModBlocks.ITEM_EXTRACTOR.get());
    }

    protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder) {
        builder.add(FACING, POWER);
    }

    @Override
    public boolean canProvidePower(BlockState state) {
        return true;
    }

    @Override
    public int getStrongPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return getWeakPower(blockState, blockAccess, pos, side);
    }

    @Override
    public int getWeakPower(BlockState blockState, IBlockReader blockAccess, BlockPos pos, Direction side) {
        return blockState.get(POWER);
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return new ItemExtractorTileEntity();
    }
}