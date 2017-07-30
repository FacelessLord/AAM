package AAM.Utils;

import java.util.List;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class Structure
{
	public Structure(String structure)
	{
		this.blocks = structure;
		this.strtag = disparse(structure);
	}

	public Structure(NBTTagCompound structure)
	{
		this.strtag = structure;
		this.blocks = parse(structure);
	}

	public static NBTTagCompound disparse(String blocks)
	{
		NBTTagCompound tg = new NBTTagCompound();
		List<String> l = StructureApi.unpact(blocks);
		int sx = Integer.parseInt(l.get(0));
		int sy = Integer.parseInt(l.get(1));
		int sz = Integer.parseInt(l.get(2));

		int tx = Integer.parseInt(l.get(3));
		int ty = Integer.parseInt(l.get(4));
		int tz = Integer.parseInt(l.get(5));
		tg.setString("Box", StructureApi.compact(sx, sy, sz, tx, ty, tz));

		for (int i = 0; i < (l.size() - 6) / 5; i++)
		{
			int j = i * 5 + 6;

			int x = Integer.parseInt(l.get(j));
			int y = Integer.parseInt(l.get(j + 1));
			int z = Integer.parseInt(l.get(j + 2));

			String name = l.get(j + 3);
			String meta = l.get(j + 4);

			String key = StructureApi.compact(x, y, z);
			tg.setString(key, StructureApi.compact(name, meta));
		}

		return tg;
	}

	public static String parse(NBTTagCompound strctag)
	{
		String st = "";
		List<String> l = StructureApi.unpact(strctag.getString("Box"));
		if (l.size() > 1)
		{
			int sx = Integer.parseInt(l.get(0));
			int sy = Integer.parseInt(l.get(1));
			int sz = Integer.parseInt(l.get(2));

			int tx = Integer.parseInt(l.get(3));
			int ty = Integer.parseInt(l.get(4));
			int tz = Integer.parseInt(l.get(5));

			st = StructureApi.compact(sx, sy, sz, tx, ty, tz);

			for (int i = sx; i < tx; i++)
			{
				for (int j = sy; j < ty; j++)
				{
					for (int k = sz; k < tz; k++)
					{
						String key = StructureApi.compact(i, j, k);
						if (strctag.hasKey(key))
						{
							st = StructureApi.compact(st, key, strctag.getString(key));
						}
					}
				}
			}
		}

		return st;
	}

	public boolean checkStructure(World w, int x, int y, int z)
	{
		List<String> l = StructureApi.unpact(blocks);
		int sx = Integer.parseInt(l.get(0));
		int sy = Integer.parseInt(l.get(1));
		int sz = Integer.parseInt(l.get(2));

		int tx = Integer.parseInt(l.get(3));
		int ty = Integer.parseInt(l.get(4));
		int tz = Integer.parseInt(l.get(5));

		boolean ret = true;

		for (int i = 0; i < (l.size() - 6) / 5; i++)
		{
			int j = i * 5 + 6;

			int cx = Integer.parseInt(l.get(j)) + x;
			int cy = Integer.parseInt(l.get(j + 1)) + y;
			int cz = Integer.parseInt(l.get(j + 2)) + z;

			String name = l.get(j + 3);
			String meta = l.get(j + 4);

			Block b = GameRegistry.findBlock(name.substring(0, name.indexOf(':')), name.substring(name.indexOf(':') + 1));
			int data = Integer.parseInt(meta);

			if (w.getBlock(cx, cy, cz) != b || w.getBlockMetadata(cx, cy, cz) != data)
			{
				ret = false;
				break;
			}
		}

		return ret;
	}

	public boolean checkStructure(World w, WorldPos pos)
	{
		int x = (int) pos.x;
		int y = (int) pos.y;
		int z = (int) pos.z;

		List<String> l = StructureApi.unpact(blocks);
		int sx = Integer.parseInt(l.get(0));
		int sy = Integer.parseInt(l.get(1));
		int sz = Integer.parseInt(l.get(2));

		int tx = Integer.parseInt(l.get(3));
		int ty = Integer.parseInt(l.get(4));
		int tz = Integer.parseInt(l.get(5));

		boolean ret = true;

		for (int i = 0; i < (l.size() - 6) / 5; i++)
		{
			int j = i * 5 + 6;

			int cx = Integer.parseInt(l.get(j)) + x;
			int cy = Integer.parseInt(l.get(j + 1)) + y;
			int cz = Integer.parseInt(l.get(j + 2)) + z;

			String name = l.get(j + 3);
			String meta = l.get(j + 4);

			Block b = GameRegistry.findBlock(name.substring(0, name.indexOf(':')), name.substring(name.indexOf(':') + 1));
			int data = Integer.parseInt(meta);

			if (w.getBlock(cx, cy, cz) != b || w.getBlockMetadata(cx, cy, cz) != data)
			{
				ret = false;
				break;
			}
		}

		return ret;
	}

	public NBTTagCompound strtag;
	public String blocks;
}