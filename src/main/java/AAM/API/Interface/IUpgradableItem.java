package AAM.API.Interface;

import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public interface IUpgradableItem
{
	public int getUpgradeLevel(ItemStack is);

	public void addUpgradeLevel(World w, ItemStack is);

	public void addUpgradeLevel(World w, ItemStack is, int level);

	public void setUpgradeLevel(World w, ItemStack is, int level);

	public int getMaxLevel(ItemStack is);

	public boolean enableLayers(ItemStack is);
}
