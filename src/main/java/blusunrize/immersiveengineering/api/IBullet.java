package blusunrize.immersiveengineering.api;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

/**
 * @author BluSunrize - 24.03.2015
 *
 * An interface to be generated by Items which makes them valid bullets for the revolver
 */
public interface IBullet
{
	public boolean canSpawnBullet(ItemStack bulletStack);

	public void spawnBullet(EntityPlayer player, ItemStack bulletStack);
	
	public ItemStack getCasing(ItemStack stack);
}
