package akuto2.event;

import java.util.Random;

import akuto2.ObjHandler;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.passive.EntityChicken;
import net.minecraft.entity.passive.EntityCow;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

@EventBusSubscriber(modid = "akutoengine")
public class CommonEventHandler {
	@SubscribeEvent
	public static void onLivingDrops(LivingDropsEvent event) {
		World world = event.getEntityLiving().world;
		double x = event.getEntityLiving().posX;
		double y = event.getEntityLiving().posY;
		double z = event.getEntityLiving().posZ;

		Random r = event.getEntityLiving().world.rand;

		int lootingLevel = event.getLootingLevel();
		int noA = new Random().nextInt(100);
		int noB = new Random().nextInt(100);

		if(world.isRemote)
			return;

		if(noA < lootingLevel * 10 + 5) {
			if(event.getEntityLiving() instanceof EntityChicken) {
				event.getDrops().add(new EntityItem(world, x, y, z, new ItemStack(ObjHandler.coreElementary, 1)));
			}
		}

		if(noB < lootingLevel * 5 + 1) {
			if(event.getEntityLiving() instanceof EntityCow) {
				event.getDrops().add(new EntityItem(world, x, y, z, new ItemStack(ObjHandler.coreElementary2, 1)));
			}
		}
	}
}
