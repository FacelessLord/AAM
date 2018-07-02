package AAM.Common.Items.Alchemy;

import java.util.List;

import AAM.API.ICircleExtender;
import AAM.Utils.Wec3;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class MiniumStone extends Item implements ICircleExtender
{
	public MiniumStone()
	{
		this.setMaxDamage(64);
		this.setTextureName("aam:tools/minium_stone");
		this.setMaxStackSize(1);
	}

	@Override
	public boolean isDamageable()
	{
		return true;
	}

	@Override
	public void onExtended(ItemStack i, EntityPlayer p, World w, Wec3 pos)
	{
		if (i.hasTagCompound())
		{
			int last = i.getTagCompound().getInteger("TrLast");
			if (last <= 0)
			{
				i.damageItem(65, p);
			}
			else
			{
				i.getTagCompound().setInteger("TrLast", last - 1);
			}
		}
		else
		{
			NBTTagCompound tg = new NBTTagCompound();
			tg.setInteger("TrLast", 64);
			i.setTagCompound(tg);
		}
	}

	@Override
	public void addInformation(ItemStack i, EntityPlayer p, List l, boolean sneak)
	{
		if (i.hasTagCompound())
		{
			int left = i.getTagCompound().getInteger("TrLast");
			l.add(EnumChatFormatting.AQUA + "" + left + " Pseudo Souls");
		}
	}
}