package Akuto2Mod.Gui;

import Akuto2Mod.TileEntity.TileAutoWorkBench;
import buildcraft.core.lib.gui.slots.SlotOutput;
import buildcraft.factory.gui.SlotWorkbench;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;

public class ContainerAutoWorkBench extends Container{
	public IInventory craftResult;

	private final TileAutoWorkBench tile;

	public ContainerAutoWorkBench(InventoryPlayer player, TileAutoWorkBench t) {
		super();

		craftResult = new InventoryCraftResult();
		tile = t;
		addSlotToContainer(new SlotOutput(tile, 9, 124, 35));
		for(int y = 0; y < 3; y++) {
			for(int x = 0; x < 3; x++) {
				addSlotToContainer(new SlotWorkbench(tile, 10 + x + y * 3, 30 + x * 18, 17 + y * 18));
			}
		}
	}

	@Override
	public boolean canInteractWith(EntityPlayer p_75145_1_) {
		return false;
	}
}
