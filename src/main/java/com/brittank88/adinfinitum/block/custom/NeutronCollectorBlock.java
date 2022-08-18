package com.brittank88.adinfinitum.block.custom;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalFacingBlock;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.Properties;
import net.minecraft.util.math.Direction;
import org.jetbrains.annotations.Nullable;

/**
 * Neutron Collector Block - A block of Neutron Collector material.
 *
 * @author Brittank88
 */
public class NeutronCollectorBlock extends HorizontalFacingBlock {

    /**
     * Constructs a Neutron Collector Block.
     *
     * @param settings The {@link Settings} to use.
     */
    public NeutronCollectorBlock(Settings settings) {
        super(settings);
        setDefaultState(getStateManager().getDefaultState().with(Properties.HORIZONTAL_FACING, Direction.NORTH));
    }

    /** Returns the current {@link ItemPlacementContext placement state} of the block. */
    @Nullable @Override public BlockState getPlacementState(ItemPlacementContext ctx) { return getDefaultState().with(Properties.HORIZONTAL_FACING, ctx.getPlayerFacing().getOpposite()); }

    /** Adds the {@link Properties#HORIZONTAL_FACING} property to the block, indicating that it can only face horizontal directions. */
    @Override protected void appendProperties(StateManager.Builder<Block, BlockState> builder) { builder.add(Properties.HORIZONTAL_FACING); }
}
