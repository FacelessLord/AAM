package AAM.Common.Items.Artifacts;

import AAM.API.ItemArtifact;
import AAM.Common.Entity.SoulCharge;
import AAM.Common.Items.Soul.Artifact;
import AAM.Common.Items.Soul.SoulSword;
import AAM.Utils.PlayerDataHandler;
import AAM.Utils.Wec3;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;
import net.minecraft.world.World;

public class CrystalBow extends ItemArtifact
{
	public CrystalBow()
	{
		super("aam.crystalbow.name", "aam.crystalbow.descr", 0);
		this.canRepair = true;
		this.setHasSubtypes(true);
	}

	@Override
	public ItemStack onItemRightClick(ItemStack i, World w, EntityPlayer p)
	{
		if (i.hasTagCompound())
		{
			if (i.getTagCompound().getInteger("State") < 5)
			{
				i.getTagCompound().setInteger("State", (i.getTagCompound().getInteger("State") + 1));
			}
			else
			{
				SoulCharge s = new SoulCharge(p.worldObj, p);
				s.setLife(500);
				Wec3 look = new Wec3(p.getLookVec());
				look.ptm(s);
				p.worldObj.spawnEntityInWorld(s);
				i.getTagCompound().setInteger("State", 0);
				i.damageItem(1, p);
			}
		}
		return i;
	}

	@Override
	public void onUpdate(ItemStack i, World w, Entity e, int tick, boolean hand)
	{
		if (e instanceof EntityPlayer)
		{
			PlayerDataHandler ph = PlayerDataHandler.get((EntityPlayer) e);
			if (i.hasTagCompound())
			{
				if (ph.art)
					i.getTagCompound().setInteger("Art", ph.stype.ordinal());
				else
					i.getTagCompound().setInteger("Art", -1);
			}
			else
			{
				NBTTagCompound tag = new NBTTagCompound();
				if (ph.art)
					tag.setInteger("Art", ph.stype.ordinal());
				else
					tag.setInteger("Art", -1);
				tag.setInteger("State", 0);
				i.setTagCompound(tag);
			}
		}
	}

	@Override
	public boolean requiresMultipleRenderPasses()
	{
		return true;
	}

	@Override
	public int getRenderPasses(int metadata)
	{
		return 2;
	}

	public static IIcon[] icon = new IIcon[3];
	public static IIcon pass;
	public static String[] ways = new String[3];

	@Override
	public void registerIcons(IIconRegister ir)
	{
		for (int i = 0; i < 3; i++)
		{
			icon[i] = ir.registerIcon("aam:soulsword/bow_art_" + i);
			ways[i] = "soulsword/bow_art_" + i;
		}
		pass = ir.registerIcon("aam:soulsword/swordpasses/bow");

	}

	@Override
	public IIcon getIcon(ItemStack i, int p)
	{
		if (i.hasTagCompound())
		{
			if (p == 0)
			{
				if (i.getTagCompound().getInteger("Art") != -1)
					return Artifact.icons[i.getTagCompound().getInteger("Art")];
				else
					return SoulSword.artnil;
			}
			else
				if (p == 1)
					return icon[Math.floorDiv(i.getTagCompound().getInteger("State"), 2)];
		}
		return icon[0];
	}

}
