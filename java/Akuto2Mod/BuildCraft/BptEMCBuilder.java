package Akuto2Mod.BuildCraft;

import java.util.Iterator;
import java.util.LinkedList;

import Akuto2Mod.EMCHandler;
import Akuto2Mod.Utils.EMCWorldSave;
import buildcraft.api.blueprints.IBuilderContext;
import buildcraft.api.blueprints.Schematic;
import buildcraft.core.blueprints.Blueprint;
import buildcraft.core.blueprints.BptBuilderBlueprint;
import buildcraft.core.builders.BuildingSlot;
import buildcraft.core.builders.TileAbstractBuilder;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class BptEMCBuilder extends BptBuilderBlueprint{
	private static EMCWorldSave worldSave;
	boolean isSlotItems = false;

	public BptEMCBuilder(Blueprint bluePrint, World world, int x, int y, int z, EMCWorldSave data) {
		super(bluePrint, world, x, y, z);
		worldSave = data;
	}

	@Override
	public boolean checkRequirements(TileAbstractBuilder builder, Schematic slot) {
		if(super.checkRequirements(builder, slot)) {
			isSlotItems = true;
			return true;
		}
		isSlotItems = false;
		return checkEMC(slot);
	}

	public boolean checkEMC(Schematic slot) {
		long requestEMC = 0L;
		try {
			LinkedList<ItemStack> req = new LinkedList();
			slot.getRequirementsForPlacement(context, req);
			for(Object stack : req) {
				if((stack instanceof ItemStack)) {
					if(EMCHandler.getEMC((ItemStack)stack) == 0) {
						return false;
					}
					requestEMC += EMCHandler.getEMC((ItemStack)stack);
				}
			}
		}
		catch(Throwable throwable) {
			throwable.printStackTrace();
		}
		return EMCWorldSave.emc >= requestEMC;
	}

	@Override
	public void useRequirements(IInventory inv, BuildingSlot slot) {
		if(isSlotItems) {
			super.useRequirements(inv, slot);
			return;
		}
		if(worldSave != null) {
			LinkedList linkedList = new LinkedList();
			try {
				Iterator iterator = slot.getRequirements(context).iterator();
				while(iterator.hasNext()) {
					ItemStack stack = (ItemStack)iterator.next();
					if(stack != null) {
						linkedList.add(stack.copy());
					}
				}
			}
			catch(Throwable throwable) {
				throwable.printStackTrace();
			}
			for(Iterator iterator = linkedList.iterator(); iterator.hasNext();) {
				ItemStack stack = (ItemStack)iterator.next();
				EMCWorldSave.emc -= EMCHandler.getEMC(stack);
				worldSave.markDirty();
				useItem(context, stack, stack.copy());
			}
		}
	}

	public void useItem(IBuilderContext inv, ItemStack req, ItemStack stack) {
		if(stack.isItemStackDamageable()) {
			if(req.getItemDamage() + stack.getItemDamage() <= stack.getMaxDamage()) {
				stack.setItemDamage(req.getItemDamage() + stack.getItemDamage());
				req.stackSize = 0;
			}

			if(stack.getItemDamage() >= stack.getMaxDamage()) {
				stack.stackSize = 0;
			}
		}
		else if(stack.stackSize >= req.stackSize){
			stack.stackSize -= req.stackSize;
			req.stackSize = 0;
		}
		else {
			req.stackSize -= stack.stackSize;
			stack.stackSize = 0;
		}

		if((stack.stackSize == 0) && (stack.getItem().getContainerItem() != null)) {
			Item container = stack.getItem().getContainerItem();

			stack.stackSize = 1;
			stack.setItemDamage(0);
		}
	}
}
