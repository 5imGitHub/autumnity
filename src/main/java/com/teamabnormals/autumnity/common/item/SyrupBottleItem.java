package com.teamabnormals.autumnity.common.item;

import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;

public class SyrupBottleItem extends Item {
	public SyrupBottleItem(Properties properties) {
		super(properties);
	}

	public ItemStack finishUsingItem(ItemStack stack, Level worldIn, LivingEntity entityLiving) {
		super.finishUsingItem(stack, worldIn, entityLiving);

		if (stack.isEmpty()) {
			return new ItemStack(Items.GLASS_BOTTLE);
		} else {
			if (entityLiving instanceof Player && !((Player) entityLiving).getAbilities().instabuild) {
				ItemStack itemstack = new ItemStack(Items.GLASS_BOTTLE);
				Player playerentity = (Player) entityLiving;
				if (!playerentity.getInventory().add(itemstack)) {
					playerentity.drop(itemstack, false);
				}
			}

			return stack;
		}
	}

	public UseAnim getUseAnimation(ItemStack stack) {
		return UseAnim.DRINK;
	}

	public SoundEvent getDrinkingSound() {
		return SoundEvents.HONEY_DRINK;
	}

	public SoundEvent getEatingSound() {
		return SoundEvents.HONEY_DRINK;
	}
}