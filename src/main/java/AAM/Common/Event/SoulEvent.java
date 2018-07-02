package AAM.Common.Event;

import java.util.List;
import java.util.Random;

import AAM.Common.Blocks.Building.ModBlocks;
import AAM.Common.Entity.SoulCharge;
import AAM.Common.Items.ModItems;
import AAM.Common.Items.Artifacts.LuckyCoin;
import AAM.Common.Items.Soul.SoulSword;
import AAM.Common.Soul.ArtifactTooltips;
import AAM.Common.Soul.Soul;
import AAM.Common.Soul.SoulDamageSource;
import AAM.Common.Transmutations.EnergyProvider;
import AAM.Core.AAMConfig;
import AAM.Core.AAMCore;
import AAM.Network.Packages.AlchemicalDispatcher;
import AAM.Network.Packages.PlayerSyncMessage;
import AAM.Utils.MiscUtils;
import AAM.Utils.PlayerDataHandler;
import AAM.Utils.Wec3;
import DummyCore.Utils.EnumRarityColor;
import baubles.api.BaublesApi;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;

public class SoulEvent
{

	@SubscribeEvent(priority = EventPriority.LOWEST)
	public void entityUpdate(LivingUpdateEvent e)
	{
		if (e.entityLiving instanceof EntityPlayer)
		{
			EntityPlayer p = (EntityPlayer) e.entityLiving;
			PlayerDataHandler ph = PlayerDataHandler.get(p);

			if (AAMCore.soul.getIsKeyPressed() && FMLClientHandler.instance().getClient().inGameHasFocus)
			{
				int mana = 0;
				int c = 0;
				for (int i = 0; i < 36; i++)
				{
					if (p.inventory.mainInventory[i] != null)
					{
						if (p.inventory.mainInventory[i].getItem() == ModItems.SoulSword)
						{
							if (p.inventory.mainInventory[i].hasTagCompound())
							{
								if (p.inventory.mainInventory[i].getTagCompound().getString("Owner").equals(p.getGameProfile().getName()))
								{
									mana += 15;
									c += 1;
								}
							}
						}
					}
				}
				int count = (mana % 15) + 1;
				c = 0;
				mana -= 15;
				if (ph.consumeSoul(-mana))
				{
					for (int i = 0; i < 36; i++)
					{
						if (p.inventory.mainInventory[i] != null)
						{
							if (p.inventory.mainInventory[i].getItem() == ModItems.SoulSword)
							{
								if (p.inventory.mainInventory[i].hasTagCompound())
								{
									if (p.inventory.mainInventory[i].getTagCompound().getString("Owner").equals(p.getGameProfile().getName()))
									{
										if (c >= count)
										{
											PlayerDataHandler.get(p).soulTag = p.inventory.mainInventory[i].getTagCompound();

										}
										p.inventory.mainInventory[i] = null;
										c += 1;
									}
								}
							}
						}
					}
					p.inventory.addItemStackToInventory(ph.getSwordStack());
				}
			}

			if (AAMCore.member.getIsKeyPressed() && FMLClientHandler.instance().getClient().inGameHasFocus)
			{
				AAMCore.proxy.addMember();
			}
			if (Minecraft.getMinecraft().objectMouseOver != null)
			{
				if (FMLClientHandler.instance().getClient().inGameHasFocus && Minecraft.getMinecraft().objectMouseOver.typeOfHit == MovingObjectType.MISS && !p.isBlocking() && ph.lastTickBlocked)
				{
					if (p.getCurrentEquippedItem() != null)
					{
						if (p.getCurrentEquippedItem().getItem() instanceof SoulSword)
						{
							if (ph.bow && MiscUtils.randWPercent(40 + ph.castUpg * 5 + 60))
							{
								if (ph.consumeSoul(ph.soulLevel) && !p.worldObj.isRemote)
								{
									SoulCharge s = new SoulCharge(p.worldObj, p);
									double sp = 0.5;
									s.setLife(500);
									Wec3 look = new Wec3(p.getLookVec()).mult(2);
									look.ptm(s);
									p.worldObj.spawnEntityInWorld(s);
								}
							}
						}
					}
				}
			}
			ph.lastTickBlocked = p.isBlocking();
		}
	}

	@SubscribeEvent
	public void tooltip(ItemTooltipEvent e)
	{
		if (e.itemStack.getItem() == ModItems.SoulSword)
		{
			e.toolTip.set(0, EnumRarityColor.EXCEPTIONAL.getRarityColor() + e.toolTip.get(0));
			e.toolTip.remove(e.toolTip.size() - 1);

			if (e.itemStack.hasTagCompound())
			{
				String name = e.itemStack.getTagCompound().getString("Owner");
				if (!name.equals(""))
				{
					PlayerDataHandler ph = PlayerDataHandler.get(e.entityPlayer.worldObj.getPlayerEntityByName(name));

					e.toolTip.add(EnumChatFormatting.DARK_AQUA + "Owner: " + name);
					e.toolTip.add(EnumChatFormatting.BLUE + "+" + ph.soulDamage + "" + EnumChatFormatting.DARK_PURPLE + " Soul Damage");
					if (ph.bow)
						e.toolTip.add(EnumChatFormatting.BLUE + "+" + (ph.soulDamage - 4 * ph.swords.size()) + "" + EnumChatFormatting.DARK_PURPLE + " Ranged Soul Damage");

					e.toolTip.add("Additional:");
					ArtifactTooltips.addToTooltip(ph.stype, ph.player, ph.soulLevel, e.toolTip);
				}
				else
					e.toolTip.add(EnumChatFormatting.BLUE + "+" + 5 + "" + EnumChatFormatting.DARK_PURPLE + " Soul Damage");
			}
			else
				e.toolTip.add(EnumChatFormatting.BLUE + "+" + 5 + "" + EnumChatFormatting.DARK_PURPLE + " Soul Damage");
		}

		if (e.itemStack.getItem() == ModItems.Artifact)
		{

			PlayerDataHandler ph = PlayerDataHandler.get(Minecraft.getMinecraft().thePlayer);

			int meta = e.itemStack.getItemDamage();
			ArtifactTooltips.addToTooltip(Soul.values()[meta], ph.player, ph.soulLevel, e.toolTip);
		}

		if (EnergyProvider.hasValue(e.itemStack))
		{
			e.toolTip.add(EnumChatFormatting.GRAY + "Energy: " + MiscUtils.roundStr(EnergyProvider.getValue(e.itemStack), 1));
			if (e.itemStack.stackSize > 1)
				e.toolTip.add(EnumChatFormatting.GRAY + "Stack Energy: " + MiscUtils.roundStr(EnergyProvider.getValue(e.itemStack) * e.itemStack.stackSize, 1));

		}
		if (EnergyProvider.getStoredEnergy(e.itemStack) > 0)
		{
			e.toolTip.add(EnumChatFormatting.GRAY + "Stored Energy: " + MiscUtils.roundStr(EnergyProvider.getStoredEnergy(e.itemStack), 1));
		}
	}

	@SubscribeEvent
	public void crafted(ItemCraftedEvent e)
	{
	}

	@SubscribeEvent
	public void attacked(AttackEntityEvent e)
	{
		if (e.entityPlayer.getCurrentEquippedItem() != null)
		{
			if (e.entityPlayer.getCurrentEquippedItem().getItem() == ModItems.SoulSword && e.entityPlayer.getCurrentEquippedItem().hasTagCompound())
			{
				ItemStack is = e.entityPlayer.getCurrentEquippedItem();
				String name = is.getTagCompound().getString("Owner");
				EntityPlayer ep = e.entityPlayer.worldObj.getPlayerEntityByName(name);
				if (ep == null)
				{
					ep = e.entityPlayer;
				}
				PlayerDataHandler ph = PlayerDataHandler.get(ep);
				if (!e.target.isDead && e.target.canAttackWithItem() && !e.target.isEntityInvulnerable())
				{
					if (is.hasTagCompound())
					{
						SoulDamageSource src = new SoulDamageSource(ph);
						float dmg = ph.soulDamage;
						if (e.target instanceof EntityLivingBase)
						{
							EntityLivingBase l = (EntityLivingBase) e.target;

							if (ph.bloodUpg != 0)
							{

								double f = Math.rint(l.getMaxHealth() * 100) / 100;

								float regen = (float) (f * 0.1F * ph.bloodUpg);
								ep.heal(regen);
							}
							if (ph.moonUpg != 0)
							{
								double f = Math.rint(l.getMaxHealth() * 100f) / 100f;
								dmg += ph.soulDamage * (25f + ph.moonUpg * 4 * (ph.soulLevel - 1) * ph.player.worldObj.getCurrentMoonPhaseFactor()) / 100f;
							}
							if (ph.stype.equals(Soul.Light))
							{
								dmg += ph.soulDamage * (20f + 2 * (ph.soulLevel - 1)) / 100f;
								if (l.getCreatureAttribute().equals(EnumCreatureAttribute.UNDEAD))
								{
									dmg += ph.soulDamage * (15f + 2 * (ph.soulLevel - 1)) / 100f;
								}
							}
							if (ph.stype.equals(Soul.Normal))
							{
								if (l.getCreatureAttribute().equals(EnumCreatureAttribute.UNDEFINED))
								{
									dmg += ph.soulDamage * (35f + 2 * (ph.soulLevel - 1)) / 100f;
								}
							}

							if (ph.stype.equals(Soul.Blood))
							{
								if (ph.consumeSoul(1))
								{
									dmg += ph.soulDamage * (25f + 2 * (ph.soulLevel - 1)) / 100f;
									if (l instanceof EntityPlayer)
									{
										dmg += ph.soulDamage * (25f + 2 * (ph.soulLevel - 1)) / 100f;
									}
								}
							}
							if (ph.stype.equals(Soul.Lunar))
							{
								dmg += ph.soulDamage * (40f + (ph.soulLevel - 1) * (2 + 4 * ph.player.worldObj.getCurrentMoonPhaseFactor())) / 100f;
							}
							if (ph.stype.equals(Soul.Plant))
							{
								dmg += ph.soulDamage * (15f + 2 * (ph.soulLevel - 1)) / 100f;
								if (MiscUtils.randWPercent(25 + 2 * (ph.soulLevel - 1)))
									l.addPotionEffect(new PotionEffect(Potion.poison.id, 150, 2));
							}
							e.target.attackEntityFrom(src, dmg);
						}
						else
						{
							e.target.attackEntityFrom(src, 5.0F);
						}
					}
					Random r = new Random();
					if (r.nextDouble() < 0.25)
					{
						if (e.target.isDead)
						{
							if (!e.entityPlayer.worldObj.isRemote)
							{
								EntityItem et = new EntityItem(e.entityPlayer.worldObj, e.entityPlayer.posX, e.entityPlayer.posY, e.entityPlayer.posZ, new ItemStack(ModItems.materials, 1, 9));
								et.setVelocity(r.nextDouble() * 0.6, r.nextDouble() * 0.6, r.nextDouble() * 0.6);
								e.entityPlayer.worldObj.spawnEntityInWorld(et);
								et.onCollideWithPlayer(e.entityPlayer);
							}
						}
					}
				}
			}

			if (BaublesApi.getBaubles(e.entityPlayer).getStackInSlot(0) != null)
			{
				int mod = 1;
				if (BaublesApi.getBaubles(e.entityPlayer).getStackInSlot(0).getItem() instanceof LuckyCoin)
				{
					mod += 2;
				}
				if (MiscUtils.randWPercent(75d))
				{
					int beta = e.entityPlayer.worldObj.rand.nextInt(50);
					boolean drop = MiscUtils.randWPercent(mod / 24d);
					if (drop)
					{
						MiscUtils.dropStack(e.entityPlayer.worldObj, new Wec3(e.entityPlayer), new ItemStack(ModItems.coins, 1, 2));
					}
					if (!drop)
					{
						drop = MiscUtils.randWPercent(mod / 6d);
						if (drop)
						{
							MiscUtils.dropStack(e.entityPlayer.worldObj, new Wec3(e.entityPlayer), new ItemStack(ModItems.coins, 1, 1));
						}
					}
					if (!drop)
					{
						MiscUtils.dropStack(e.entityPlayer.worldObj, new Wec3(e.entityPlayer), new ItemStack(ModItems.coins, mod, 0));
					}
				}
			}
		}
	}

	@SubscribeEvent
	public void onPlayerDeath(LivingHurtEvent e)
	{
		if (e.entityLiving instanceof EntityPlayer)
		{
			EntityPlayer p = (EntityPlayer) e.entityLiving;
			if (e.ammount >= p.getHealth())
			{
				if (MiscUtils.contains(p.inventory, ModItems.RessurectionStone, 0))
				{
					List<Integer> fx = MiscUtils.getList(p.inventory, ModItems.RessurectionStone, 0);
					for (int i = 0; i < fx.size(); i++)
					{
						p.inventory.getStackInSlot(fx.get(i)).setItemDamage(p.dimension == AAMConfig.dungDimId ? Math.max(12000 / fx.size(), 4500) : Math.max(6000 / fx.size(), 1500));
					}
					p.heal(p.getMaxHealth());
					e.setCanceled(true);
				}
				if (MiscUtils.contains(p.inventory, ModItems.MassRessurectionStone, 0))
				{
					p.heal(p.getMaxHealth());
					p.setAbsorptionAmount(20);
					List<String> ps = PlayerDataHandler.get(p).party;
					for (String name : ps)
					{
						if (p.worldObj.getPlayerEntityByName(name) != null)
						{
							EntityPlayer pp = p.worldObj.getPlayerEntityByName(name);
							pp.heal(pp.getMaxHealth());
							pp.setAbsorptionAmount(20);
						}
					}
					p.inventory.getStackInSlot(MiscUtils.get(p.inventory, ModItems.MassRessurectionStone, 0))
							.setItemDamage(p.dimension == AAMConfig.dungDimId ? Math.max((1 + ps.size()) * 24000 / 7, 4500) : Math.max((1 + ps.size()) * 12000 / 7, 1500));

					e.setCanceled(true);
				}
			}
		}
	}

	@SubscribeEvent
	public void entityKilled(LivingDeathEvent e)
	{
		if (e.source.damageType == SoulDamageSource.id)
		{
			PlayerDataHandler ph = ((SoulDamageSource) e.source).ph;
			ph.soulxp += (int) (e.entityLiving.getMaxHealth() - (e.entityLiving.getMaxHealth() / (ph.soulLevel + 1)));
			if (!e.entityLiving.worldObj.isRemote)
			{
				PlayerSyncMessage psm = new PlayerSyncMessage(ph.player);
				AlchemicalDispatcher.sendToClient(psm, ph.player);
			}
		}
	}

	@SubscribeEvent
	public void onBucketFill(FillBucketEvent event)
	{
		World world = event.world;
		MovingObjectPosition pos = event.target;
		Block block = world.getBlock(pos.blockX, pos.blockY, pos.blockZ);
		int meta = world.getBlockMetadata(pos.blockX, pos.blockY, pos.blockZ);

		if (block == ModBlocks.BloodBlock && meta == 0)
		{
			world.setBlockToAir(pos.blockX, pos.blockY, pos.blockZ);
			event.result = new ItemStack(ModItems.BloodBucket, 1, 0);
			event.setResult(Result.ALLOW);
		}

	}

	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void harvestDrops(LivingDropsEvent event)
	{
	}

}
