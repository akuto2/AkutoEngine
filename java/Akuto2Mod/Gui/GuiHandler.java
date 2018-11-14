package Akuto2Mod.Gui;

import Akuto2Mod.TileEntity.TileAutoWorkBench;
import Akuto2Mod.TileEntity.TileEMCBuilder;
import Akuto2Mod.TileEntity.TileFillerEX;
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
		switch(ID) {
		case 0:
			if((tileEntity instanceof TileEntityElectricBlock)){
				return new ContainerUmfsUnit(player, (TileEntityElectricBlock) tileEntity);
			}
		case 1:
			if(tileEntity instanceof TileFillerEX) {
				return new ContainerFillerEX(player.inventory, (TileFillerEX) tileEntity);
			}
		case 2:
			if(tileEntity instanceof TileAutoWorkBench) {
				return new ContainerAutoWorkBench(player.inventory, (TileAutoWorkBench)tileEntity);
			}
		case 3:
			if(tileEntity instanceof TileEMCBuilder) {
				return new ContainerTileEMCBuilder(player.inventory, (TileEMCBuilder)tileEntity);
			}
		}
		return null;
	}

	@SideOnly(Side.CLIENT)
	@Override
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		TileEntity tileentity = world.getTileEntity(x, y, z);
		switch(ID) {
		case 0:
			if((tileentity instanceof TileEntityElectricBlock)){
				return new GuiUmfsUnit(new ContainerUmfsUnit(player, (TileEntityElectricBlock) tileentity));
			}
		case 1:
			if(tileentity instanceof TileFillerEX) {
				return new GuiFillerEX(player.inventory, (TileFillerEX) tileentity);
			}
		case 3:
			if(tileentity instanceof TileEMCBuilder) {
				return new GuiTileEMCBuilder(player.inventory, (TileEMCBuilder) tileentity);
			}
		}
		return null;
	}
}
