package Akuto2Mod.Items;

import Akuto2Mod.Akuto2Core;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import lib.manual.GuiManual;
import lib.proxies.ClientProxy;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class FillerManual extends Item{
	public FillerManual() {
		setTextureName("akutoengine:fillermanual");
		setUnlocalizedName("manual.filler");
	}

	@Override
	public ItemStack onItemRightClick(ItemStack stack, World world, EntityPlayer player) {
		if(world.isRemote) {
			openBook(stack, world, player);
		}

		return stack;
	}

	@SideOnly(Side.CLIENT)
	public void openBook(ItemStack stack, World world, EntityPlayer player){
		player.openGui(Akuto2Core.instance, 4, world, 0, 0, 0);
		FMLClientHandler.instance().displayGuiScreen(player, new GuiManual(stack, ClientProxy.getBookDataFromStack(stack)));
	}

	@Override
	public String getUnlocalizedName() {
		return "akutoengine:" + super.getUnlocalizedName();
	}

	@Override
	public String getUnlocalizedName(ItemStack stack) {
		return getUnlocalizedName();
	}
}
