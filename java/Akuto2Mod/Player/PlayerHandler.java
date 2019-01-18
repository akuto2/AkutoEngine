package Akuto2Mod.Player;

import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

import Akuto2Mod.Akuto2Core;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemCraftedEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import cpw.mods.fml.relauncher.Side;
import lib.utils.AbilityHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class PlayerHandler {
	private ConcurrentHashMap<UUID, PlayerStats> playerStats = new ConcurrentHashMap();

	@SubscribeEvent
	public void onPlayerLogin(PlayerLoggedInEvent event) {
		if(Akuto2Core.update != null) {
			Akuto2Core.update.notifyUpdate(event.player, Side.CLIENT);
		}
	}

	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent event) {
		onPlayerRespawn(event.player);
	}

	@SubscribeEvent
	public void onEntityConstructing(EntityConstructing event) {
		if((event.entity instanceof EntityPlayer) && (PlayerStats.get((EntityPlayer)event.entity) == null)) {
			PlayerStats.register((EntityPlayer)event.entity);
		}
	}

	public void onPlayerRespawn(EntityPlayer player) {
		PlayerStats playerData = (PlayerStats)playerStats.remove(player.getPersistentID());
		PlayerStats stats = PlayerStats.get(player);
		if(playerData != null)
		{
			stats.copyFrom(playerData);
		}
		stats.init(player, player.worldObj);
	}

	@SubscribeEvent
	public void onPlayerDeath(LivingDeathEvent event) {
		if(!(event.entity instanceof EntityPlayer)) {
			return;
		}
		if(!event.entity.worldObj.isRemote) {
			PlayerStats stats = (PlayerStats)event.entity.getExtendedProperties("AkutoEngine");
			playerStats.put(((EntityPlayer)event.entity).getPersistentID(), stats);
		}
	}

	@SubscribeEvent
	public void onCrafting(ItemCraftedEvent event) {
		Item item = event.crafting.getItem();
		if(!event.player.worldObj.isRemote) {
			if((item == Item.getItemFromBlock(Akuto2Core.fillerEX))) {
				PlayerStats stats = PlayerStats.get(event.player);
				if(!stats.fillerManual) {
					stats.fillerManual = true;
					AbilityHelper.spawnItemAtPlayer(event.player, new ItemStack(Akuto2Core.manualFiller));
				}
			}
		}
	}
}
