package com.markus1002.autumnity.common.block;

import com.markus1002.autumnity.common.block.properties.CarvedSide;
import com.markus1002.autumnity.core.registry.AutumnityBlocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.state.StateContainer;
import net.minecraft.state.properties.Half;
import net.minecraft.util.ActionResultType;
import net.minecraft.util.Direction;
import net.minecraft.util.Direction.Axis;
import net.minecraft.util.Hand;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.SoundEvents;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class LargePumpkinSliceBlock extends AbstractLargePumpkinSliceBlock
{
	public LargePumpkinSliceBlock(Properties properties)
	{
		super(properties);
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(HALF, Half.BOTTOM));
	}

	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(FACING, HALF);
	}

	public ActionResultType onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		ItemStack itemstack = player.getHeldItem(handIn);
		if (itemstack.getItem() == Items.SHEARS)
		{
			Direction direction = hit.getFace();
			Direction direction1 = state.get(FACING);

			if (canCarve(direction, direction1))
			{
				if (!worldIn.isRemote)
				{
					CarvedSide carvedside = direction.getAxis() == Axis.X ? CarvedSide.X : CarvedSide.Z;
					BlockState blockstate = AutumnityBlocks.CARVED_LARGE_PUMPKIN_SLICE.get().getDefaultState().with(CarvedLargePumpkinSliceBlock.FACING, direction1).with(CarvedLargePumpkinSliceBlock.HALF, state.get(HALF)).with(CarvedLargePumpkinSliceBlock.CARVED_SIDE, carvedside);

					worldIn.playSound((PlayerEntity)null, pos, SoundEvents.BLOCK_PUMPKIN_CARVE, SoundCategory.BLOCKS, 1.0F, 1.0F);
					worldIn.setBlockState(pos, blockstate, 11);
					itemstack.damageItem(1, player, (p_220282_1_) -> {
						p_220282_1_.sendBreakAnimation(handIn);
					});
				}

				return ActionResultType.func_233537_a_(worldIn.isRemote);
			}
		}

		return super.onBlockActivated(state, worldIn, pos, player, handIn, hit);
	}

	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		BlockPos blockpos = context.getPos();
		BlockState bottomblock = context.getWorld().getBlockState(blockpos.down());
		BlockState topblock = context.getWorld().getBlockState(blockpos.up());

		if (bottomblock.getBlock() instanceof AbstractLargePumpkinSliceBlock && bottomblock.get(HALF) == Half.BOTTOM)
		{
			return this.getDefaultState().with(FACING, bottomblock.get(FACING)).with(HALF, Half.TOP);
		}
		else if (topblock.getBlock() instanceof AbstractLargePumpkinSliceBlock && topblock.get(HALF) == Half.TOP)
		{
			return this.getDefaultState().with(FACING, topblock.get(FACING)).with(HALF, Half.BOTTOM);
		}

		return this.getDefaultState().with(FACING, getFacing(context).getOpposite()).with(HALF, MathHelper.sin(context.getPlayer().getPitch(1.0F) * ((float)Math.PI / 180F)) > 0 ? Half.BOTTOM : Half.TOP);
	}
}