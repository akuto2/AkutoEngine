package akuto2.akutoengine.tiles;

import java.io.IOException;

import buildcraft.api.core.BCLog;
import buildcraft.lib.misc.BlockUtil;
import buildcraft.lib.misc.MessageUtil;
import buildcraft.lib.misc.data.IdAllocator;
import buildcraft.lib.net.IPayloadReceiver;
import buildcraft.lib.net.IPayloadWriter;
import buildcraft.lib.net.MessageManager;
import buildcraft.lib.net.MessageUpdateTile;
import buildcraft.lib.net.PacketBufferBC;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.play.server.SPacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ITickable;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.Constants;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.relauncher.Side;

public class TileBuildCraftEX extends TileEntity implements ITickable, IPayloadReceiver{
	protected static final IdAllocator IDS = new IdAllocator("tile");
	public static final int NET_RENDER_DATA;
	private boolean init = false;

	static {
		NET_RENDER_DATA = IDS.allocId("RENDER_DATA");
	}

	@Override
	public void update() {
		if(!init && !isInvalid()) {
			initialize();
			init = true;
		}
	}

	public void initialize() {}

	public final TileEntity getNeighbourTile(EnumFacing offset) {
		return BlockUtil.getTileEntity(world, getPos().offset(offset), true);
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		return super.writeToNBT(compound);
	}

	@Override
	public NBTTagCompound getUpdateTag() {
		ByteBuf buf = Unpooled.buffer();
		buf.writeShort(NET_RENDER_DATA);
		writePayload(NET_RENDER_DATA, new PacketBufferBC(buf), world.isRemote ? Side.CLIENT : Side.SERVER);
		byte[] bytes = new byte[buf.readableBytes()];
		buf.readBytes(bytes);
		NBTTagCompound nbt = super.getUpdateTag();
		nbt.setByteArray("d", bytes);
		return nbt;
	}

	@Override
	public void handleUpdateTag(NBTTagCompound tag) {
		super.readFromNBT(tag);
		if(!tag.hasKey("d", Constants.NBT.TAG_BYTE_ARRAY)) {
			return;
		}
		byte[] bytes = tag.getByteArray("d");
		if(bytes.length < 2) {
			BCLog.logger.warn("[akutoengine] Received an update tag that didn't have any data!\n\t(" + tag + ")");
			return;
		}
		ByteBuf buf = Unpooled.copiedBuffer(bytes);

		try {
			int id = buf.readUnsignedShort();
			PacketBufferBC buffer = new PacketBufferBC(buf);
			readPayload(id, buffer, world.isRemote ? Side.CLIENT : Side.SERVER, null);
			MessageUtil.ensureEmpty(buffer, world.isRemote, getClass() + ", id = " + getIdAllocator().getNameFor(id));
		} catch(IOException e) {
			throw new RuntimeException("Received an update tag that failed to read correctly!", e);
		}
	}

	@Override
	public final IMessage receivePayload(MessageContext ctx, PacketBufferBC buffer) throws IOException {
		int id = buffer.readUnsignedShort();
		readPayload(id, buffer, ctx.side, ctx);

		MessageUtil.ensureEmpty(buffer, world.isRemote, getClass() + ", id = " + getIdAllocator().getNameFor(id));

		return null;
	}

	public void readPayload(int id, PacketBufferBC buffer, Side side, MessageContext ctx) throws IOException{}

	public void writePayload(int id, PacketBufferBC buffer, Side side){}

	@Override
	public SPacketUpdateTileEntity getUpdatePacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new SPacketUpdateTileEntity(pos, 0, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, SPacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		readFromNBT(pkt.getNbtCompound());
	}

	public final MessageUpdateTile createNetworkUpdate(int id) {
		if(hasWorld()) {
			Side side = world.isRemote ? Side.CLIENT : Side.SERVER;
			return createMessage(id, (buffer) -> writePayload(id, buffer, side));
		}
		return null;
	}

	public final MessageUpdateTile createMessage(int id, IPayloadWriter writer) {
		PacketBufferBC buffer = new PacketBufferBC(Unpooled.buffer());
		buffer.writeShort(id);
		writer.write(buffer);
		return new MessageUpdateTile(pos, buffer);
	}

	public final void createAndSendMessage(int id, IPayloadWriter writer) {
		if(hasWorld()) {
			IMessage message = createMessage(id, writer);
			if(world.isRemote) {
				MessageManager.sendToServer(message);
			} else {
				MessageUtil.sendToAllWatching(world, pos, message);
			}
		}
	}

	public final void sendNetworkUpdate(int id) {
		if(hasWorld()) {
			MessageUpdateTile message = createNetworkUpdate(id);
			if(world.isRemote) {
				MessageManager.sendToServer(message);
			} else {
				MessageUtil.sendToAllWatching(world, pos, message);
			}
		}
	}

	public IdAllocator getIdAllocator() {
		return IDS;
	}

	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
		return getCapability(capability, facing) != null;
	}

	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
		return super.getCapability(capability, facing);
	}
}
