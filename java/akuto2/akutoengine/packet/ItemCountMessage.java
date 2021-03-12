package akuto2.akutoengine.packet;

import java.io.IOException;
import java.math.BigInteger;

import akuto2.akutoengine.tiles.TileEntityInfinityChest;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import lib.utils.LogHelper;
import lib.world.WorldHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;

public class ItemCountMessage implements IMessage{
	int x, y, z;
	int dim;
	private byte[] bytes;
	private ItemStack out, holding;

	public ItemCountMessage() {}

	public ItemCountMessage(TileEntityInfinityChest chest, BigInteger integer) {
		x = chest.xCoord;
		y = chest.yCoord;
		z = chest.zCoord;
		dim = chest.getWorldObj().provider.dimensionId;
		bytes = integer.toByteArray();
		out = chest.getStackInSlot(1);
		holding = chest.getStack(1);
	}


	@Override
	public void fromBytes(ByteBuf buf) {
		PacketBufferAE buffer = new PacketBufferAE(buf);
		x = buffer.readInt();
		y = buffer.readInt();
		z = buffer.readInt();
		dim = buffer.readInt();
		bytes = buffer.readByteArray();
		try {
			out = buffer.readItemStack();
			holding = buffer.readItemStack();
		} catch(IOException e) {
			LogHelper.logError("ItemStack reading", e);
			out = null;
			holding = null;
		}
	}

	@Override
	public void toBytes(ByteBuf buf) {
		PacketBufferAE buffer = new PacketBufferAE(buf);
		buffer.writeInt(x);
		buffer.writeInt(y);
		buffer.writeInt(z);
		buffer.writeInt(dim);
		buffer.writeByteArray(bytes).writeItemStack(out).writeItemStack(holding);
	}

	public static class Handler implements IMessageHandler<ItemCountMessage, IMessage>{
		@Override
		@SideOnly(Side.CLIENT)
		public IMessage onMessage(ItemCountMessage message, MessageContext ctx) {
			TileEntity tile = WorldHelper.getWorld().getTileEntity(message.x, message.y, message.z);
			if(WorldHelper.getWorld().provider.dimensionId == message.dim && tile instanceof TileEntityInfinityChest) {
				TileEntityInfinityChest chest = (TileEntityInfinityChest)tile;
				chest.setCount(new BigInteger(message.bytes));
				chest.setInventorySlotContents(1, message.out);
				chest.setContents(message.holding);
			}
			return null;
		}
	}
}
