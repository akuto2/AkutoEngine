package akuto2.akutoengine.gui;

import akuto2.akutoengine.gui.container.ContainerFillerEX;
import akuto2.akutoengine.tiles.TileEntityFillerEX;
import akuto2.akutoengine.utils.Utils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.network.IGuiHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@EventBusSubscriber(modid = "akutoengine")
public class GuiHandler implements IGuiHandler{
	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity tile = world.getTileEntity(pos);

		if(ID == Utils.FILLEREX_GUI) {
			if(tile instanceof TileEntityFillerEX)
				return new ContainerFillerEX(player.inventory, world, (TileEntityFillerEX)tile);
		}
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		BlockPos pos = new BlockPos(x, y, z);
		TileEntity tile = world.getTileEntity(pos);

		if(ID == Utils.FILLEREX_GUI) {
			if(tile instanceof TileEntityFillerEX)
				return new GuiFillerEX(player.inventory, world, (TileEntityFillerEX)tile);
		}
		return null;
	}

	@SubscribeEvent
	public static void onGuiOpened(PlayerContainerEvent.Open event) {
		if(event.getContainer() instanceof ContainerFillerEX) {
			((ContainerFillerEX)event.getContainer()).onCraftMatrixChanged();
		}
	}
}
