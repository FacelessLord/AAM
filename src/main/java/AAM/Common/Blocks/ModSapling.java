package AAM.Common.Blocks;

import java.util.List;
import java.util.Random;

import AAM.Common.WorldGen.ModTreeGen;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.BlockSapling;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class ModSapling extends BlockSapling
{
	protected IIcon icon;
	protected String texture;
	public int type = -1;

	public ModSapling(String texture, int type)
	{
		this.texture = texture;
		this.type = type;
	}

	@Override
	public int damageDropped(int p_149692_1_)
	{
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerBlockIcons(IIconRegister ir)
	{
		this.icon = ir.registerIcon("aam:" + texture + "_sapling");
	}

	/**
	 * Gets an icon index based on an item's damage value
	 */
	@SideOnly(Side.CLIENT)
	public IIcon getIconFromDamage(int meta)
	{
		return this.icon;
	}

	@SideOnly(Side.CLIENT)
	public IIcon getIcon(IBlockAccess w, int x, int y, int z, int s)
	{
		return this.getIcon(s, w.getBlockMetadata(x, y, z));
	}

	/**
	 * Gets the block's texture. Args: side, meta
	 */
	@SideOnly(Side.CLIENT)
	public IIcon getIcon(int side, int meta)
	{
		return this.icon;
	}

	/**
	 * returns a list of blocks with the same ID, but different meta (eg: wood
	 * returns 4 blocks)
	 */
	@Override
	public void getSubBlocks(Item i, CreativeTabs tab, List l)
	{
		l.add(new ItemStack(i, 1, 0));
	}

	@Override
	public void func_149878_d(World w, int x, int y, int z, Random r)
	{
		ModTreeGen gen = new ModTreeGen(this.type, true);

		gen.generate(w, r, x, y, z);
	}
}