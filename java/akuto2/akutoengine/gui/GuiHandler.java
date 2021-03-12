package akuto2.akutoengine.gui;

import akuto2.akutoengine.tiles.TileEntityInfinityChest;
import akuto2.akutoengine.tiles.TileFillerEX;
import akuto2.akutoengine.utils.Utils;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.block.wiring.TileEntityElectricBlock;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class GuiHandler implements IGuiHandler{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileEntity = world.getTileEntity(x, y, z);
		if(ID == Utils.ELECTRIC_BLOCK_GUI_ID) {
			if((tileEntity instanceof TileEntityElectricBlock)){
				return new ContainerUmfsUnit(player, (TileEntityElectricBlock) tileEntity);
			}
		}
		if(ID == Utils.FILLER_EX_GUI_ID) {
			if(tileEntity instanceof TileFillerEX) {
				return new ContainerFillerEX(player.inventory, (TileFillerEX) tileEntity);
			}
		}
		if(ID == Utils.INFINITY_CHEST_GUI_ID) {
			if(tileEntity instanceof TileEntityInfinityChest) {
				return new ContainerInfinityChest(player.inventory, (TileEntityInfinityChest)tileEntity);
			}
		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileentity = world.getTileEntity(x, y, z);
		if(ID == Utils.ELECTRIC_BLOCK_GUI_ID) {
			if((tileentity instanceof TileEntityElectricBlock)){
				return new GuiUmfsUnit(new ContainerUmfsUnit(player, (TileEntityElectricBlock) tileentity));
			}
		}
		if(ID == Utils.FILLER_EX_GUI_ID) {
			if(tileentity instanceof TileFillerEX) {
				return new GuiFillerEX(player.inventory, (TileFillerEX) tileentity);
			}
		}
		if(ID == Utils.INFINITY_CHEST_GUI_ID) {
			if(tileentity instanceof TileEntityInfinityChest) {
				return new GuiInfinityChest(player.inventory, (TileEntityInfinityChest)tileentity);
			}
		}
		return null;
	}
}
