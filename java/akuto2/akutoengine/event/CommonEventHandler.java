package akuto2.akutoengine.event;

import java.util.Random;

import akuto2.akutoengine.AkutoEngine;
import akuto2.akutoengine.pattern.FillerPatternCore;
import akuto2.akutoengine.utils.AchievementHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;

public class CommonEventHandler{
	@SubscribeEvent
	public void EntityItemPickupEvent(EntityItemPickupEvent e) {
		if(e.entityPlayer != null && e.item != null && e.item.getEntityItem() != null) {
			if(e.item.getEntityItem().getItem() == AkutoEngine.coreElementary1) {
				e.entityPlayer.triggerAchievement(AchievementHandler.getFeather);
			}
			if(e.item.getEntityItem().getItem() == AkutoEngine.coreElementary2) {
				e.entityPlayer.triggerAchievement(AchievementHandler.getLeather);
			}
		}
	}

	@SubscribeEvent
	public void onLivingDrops(LivingDropsEvent event){
		World world = event.entityLiving.worldObj;
		double x = event.entityLiving.posX;
		double y = event.entityLiving.posY;
		double z = event.entityLiving.posZ;

		Random r = event.entityLiving.worldObj.rand;

		int lootinglevel = event.lootingLevel;

		int noA = new java.util.Random().nextInt(100);
		int noB = new java.util.Random().nextInt(100);

		if(event.entityLiving.worldObj.isRemote)
			return;

		if (noA < lootinglevel * 10 + 5){
			if(event.entityLiving instanceof EntityChicken)
				event.drops.add(new EntityItem(world, x, y, z, new ItemStack(AkutoEngine.coreElementary1, 1)));
		}
		if (noB < lootinglevel * 5 + 1){
			if(event.entityLiving instanceof EntityCow)
				event.drops.add(new EntityItem(world, x, y, z, new ItemStack(AkutoEngine.coreElementary2, 1)));
		}
	}

	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void textureHook(TextureStitchEvent.Pre event) {
		for(FillerPatternCore pattern : FillerPatternCore.pattern.values()) {
			pattern.registerIcon(event.map);
		}
	}
}
