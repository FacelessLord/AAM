package AAM.Common.Transmutations.Actions;

import AAM.Common.Tiles.TETransCircle;
import AAM.Common.Transmutations.TransAction;
import AAM.Utils.Wec3;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

public class ActSunny extends TransAction
{

	public ActSunny()
	{
	}

	@Override
	public void act(World w, Wec3 tile, TETransCircle te, EntityPlayer p, double potency, ForgeDirection dir)
	{
		w.getWorldInfo().setRainTime((int) (potency * 6000));
		w.getWorldInfo().setRaining(false);
		w.getWorldInfo().setThundering(false);
	}
}
