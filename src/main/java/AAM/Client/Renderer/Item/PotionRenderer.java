package AAM.Client.Renderer.Item;

import org.lwjgl.opengl.GL11;

import AAM.Common.Potions.ModPotions;
import AAM.Utils.Color;
import AAM.Utils.Render.RenderUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

public class PotionRenderer implements IItemRenderer
{

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type)
	{
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper)
	{
		return false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data)
	{
		String way = "textures/items/emptyphial";

		GL11.glPushMatrix();
		if (type == ItemRenderType.INVENTORY)
		{
			GL11.glScaled(16.0, 16.0, 16.0);
			GL11.glRotated(180, 1.0, 0.0, 0.0);
			GL11.glRotated(180, 0.0, 1.0, 0.0);
			GL11.glTranslated(-1.0, -1.0, 0.0);
		} else
		{
			if (type == ItemRenderType.ENTITY && !item.isOnItemFrame())
			{
				int angle = (int) ((Minecraft.getMinecraft().getSystemTime() % 23040) / 128);
				double append = ((double) (Minecraft.getMinecraft().getSystemTime() % 2048) / 2048);
				if (append > 0.5)
				{
					append = 1 - append;
				}
				GL11.glRotated(angle, 0, 1, 0);
				GL11.glTranslated(-0.5, 0, 0);
				GL11.glTranslated(0, append - 0.25, 0);
			}
			if (item.isOnItemFrame())
			{
				GL11.glTranslated(-0.5, -0.1, 0);
			}
		}
		Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("aam", way + ".png"));
		Tessellator tess = Tessellator.instance;
		RenderUtils.renderItemIn2D(tess, 0.0F, -1.0F, -1.0F, 0.0F, 64, 64, 0.05F);
		GL11.glPopMatrix();

		if (item.hasTagCompound())
		{
			way = "textures/items/potions/potionoffset";
			Color col;
			int id = item.getTagCompound().getInteger("PotionID");
			if (id == Potion.nightVision.id)
				id = 8;
			col = ModPotions.pots[id].col;

			GL11.glPushMatrix();
			if (type == ItemRenderType.INVENTORY)
			{
				GL11.glScaled(16.0, 16.0, 16.0);
				GL11.glRotated(180, 1.0, 0.0, 0.0);
				GL11.glRotated(180, 0.0, 1.0, 0.0);
				GL11.glTranslated(-1.0, -1.0, 0.0);
			} else
			{
				if (type == ItemRenderType.ENTITY && !item.isOnItemFrame())
				{
					int angle = (int) ((Minecraft.getMinecraft().getSystemTime() % 23040) / 128);
					double append = ((double) (Minecraft.getMinecraft().getSystemTime() % 2048) / 2048);
					if (append > 0.5)
					{
						append = 1 - append;
					}
					GL11.glRotated(angle, 0, 1, 0);
					GL11.glTranslated(-0.5, 0, 0);
					GL11.glTranslated(0, append - 0.25, 0);
				}
				if (item.isOnItemFrame())
				{
					GL11.glTranslated(-0.5, -0.1, 0);
				}
			}
			GL11.glTranslated(0.015625, 0, 0);

			Minecraft.getMinecraft().getTextureManager().bindTexture(new ResourceLocation("aam", way + ".png"));
			RenderUtils.renderItemIn2DwithColor(tess, 0.0F, -1.0, 1.0F, 0.0, 64, 64, 0.05F, col.add(new Color(col.red + 70, col.green + 70, col.blue + 70)));
			GL11.glPopMatrix();
		}
	}
}