package AAM.Common.Container;

import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.IInventory;

public class SoulAltarContainer extends ContainerBase
{

	public SoulAltarContainer(InventoryPlayer p, IInventory tile)
	{
		super(p, tile);

		this.addBackground(234, 234);
		this.addText("Soul", 194, 94, 68, 10, false);

		this.addSlot(0, 4, tile, 100, 10);

		this.addHiddenSlot(1, tile, 100, 80, 18, 7, true);
		this.addHiddenSlot(2, tile, 100, 56, 18, 5, true);
		this.addHiddenSlot(3, tile, 100, 33, 18, 6, true);

		this.addHiddenSlot(4, tile, 10, 20, 18, 8, true);
		this.addHiddenSlot(5, tile, 10, 45, 18, 8, true);
		this.addHiddenSlot(6, tile, 10, 70, 18, 8, true);

		this.addHiddenSlot(7, tile, 190, 20, 18, 8, true);
		this.addHiddenSlot(8, tile, 190, 45, 18, 8, true);
		this.addHiddenSlot(9, tile, 190, 70, 18, 8, true);

		this.addHiddenText("Owner:", 50, -8, 140, 12, true, true);
		this.addHiddenSlot(10, tile, 76, 68, true);
		this.addHiddenSlot(11, tile, 124, 68, true);

		this.addPlayerSlots();
	}

}
