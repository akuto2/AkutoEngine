package akuto2.akutoengine.compat.storagebox;

import java.math.BigInteger;

import akuto2.akutoengine.AkutoEngine;
import akuto2.akutoengine.tiles.TileEntityInfinityChest;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import lib.utils.LogHelper;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

public class StorageBoxEvent {
	@SubscribeEvent
	public void onActivated(PlayerInteractEvent event) {
		Block activatedBlock = event.world.getBlock(event.x, event.y, event.z);
		if(event.action == Action.RIGHT_CLICK_BLOCK && activatedBlock == AkutoEngine.infinityChest) {
			TileEntityInfinityChest chest = (TileEntityInfinityChest)event.world.getTileEntity(event.x, event.y, event.z);
			ItemStack stack = event.entityPlayer.getCurrentEquippedItem();
			if(chest != null && stack != null && stack.getItem().equals(StorageBox.storageBox) && stack.stackSize == 1 && onStorageBox(event.world, event.entityPlayer, chest, stack)) {
				event.useBlock = Result.DENY;
				event.useItem = Result.DENY;
			}
		}
	}

	private boolean onStorageBox(World world, EntityPlayer player, TileEntityInfinityChest chest, ItemStack stack) {
		NBTTagCompound compound = stack.getTagCompound();
		boolean flag = true;
		int result = 0;
		int size = 0;
		if(compound == null || compound.getInteger("StorageSize") <= 0) {
			if(!chest.hasStack() || !isItemValid(chest.getStack())) {
				return false;
			}
			if(compound == null) {
				compound = new NBTTagCompound();
			}
			int id = Item.getIdFromItem(chest.getStack().getItem());
			int damage = chest.getStack().getItemDamage();
			size = 0;
			compound.setInteger("StorageItem", id);
			compound.setInteger("StorageDamage", damage);
			compound.setInteger("StorageSize", 0);
			Item item = Item.getItemById(id);
			result = calcSize(world, player, chest, new ItemStack(item, size, damage), 1);
		}
		else {
			int id = compound.getInteger("StorageItem");
			int damage = compound.getInteger("StorageDamage");
			size = compound.getInteger("StorageSize");
			if(size <= 0) {
				return false;
			}
			int limit = calcLimit(size);
			Item item = Item.getItemById(id);
			result = calcSize(world, player, chest, new ItemStack(item, size, damage), limit);
		}
		if(size == result) {
			return false;
		}
		compound.setInteger("StorageSize", result);
		stack.setTagCompound(compound);
		return true;
	}

	private boolean isItemValid(ItemStack stack) {
		return (!stack.getItem().equals(StorageBox.storageBox) && !stack.hasTagCompound() && !stack.isItemStackDamageable());
	}

	private int calcLimit(int value) {
		if(value >= 10000000) {
			return 100000000;
		}
		if(value >= 1000000) {
			return 10000000;
		}
		if(value >= 100000) {
			return 1000000;
		}
		if(value >= 10000) {
			return 100000;
		}
		if(value >= 1000) {
			return 10000;
		}
		return 1000;
	}

	private int calcSize(World world, EntityPlayer player, TileEntityInfinityChest chest, ItemStack stack, int limit) {
		int size = stack.stackSize;
		if(player.isSneaking()) {
			int min = (player.getCurrentEquippedItem().getItem().equals(StorageBox.storageBox) && size > 1) ? 1 : 0;
			stack.stackSize -= min;
			chest.addStack(stack);
			world.playSoundAtEntity((Entity)player, "random.pop", 0.5F, 1.4F);
			size = min;
		}
		else {
			LogHelper.logError("Size:" + size);
			int result = chest.decrStack(stack, BigInteger.valueOf(limit)).intValueExact();
			LogHelper.logError("Result:" + result);
			if(result > 0)
				world.playSoundAtEntity((Entity)player, "random.pop", 0.5F, 1.0F);
			size += result;
			LogHelper.logError("Size:" + size);
		}
		return size;
	}
}
