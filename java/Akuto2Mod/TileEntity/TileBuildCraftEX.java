package Akuto2Mod.TileEntity;

import java.util.LinkedList;

import buildcraft.core.LaserData;
import buildcraft.core.builders.TileAbstractBuilder;
import io.netty.buffer.ByteBuf;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;

public abstract class TileBuildCraftEX extends TileAbstractBuilder{
	private boolean init;
	public LinkedList<LaserData> pathLaser = new LinkedList();

	public TileBuildCraftEX() {
		init = false;
	}

	@Override
	public void readData(ByteBuf stream) {
		int size = stream.readUnsignedShort();
		pathLaser.clear();
		for(int i = 0; i < size; i++) {
			LaserData l = new LaserData();
			l.readData(stream);
			pathLaser.add(l);
		}
	}

	@Override
	public void writeData(ByteBuf stream) {
		stream.writeShort(pathLaser.size());
		for(LaserData l : pathLaser) {
			l.writeData(stream);
		}
	}

	public LinkedList<LaserData> getPathLaser(){
		return pathLaser;
	}

	@Override
	public Packet getDescriptionPacket() {
		NBTTagCompound tag = new NBTTagCompound();
		writeToNBT(tag);
		return new S35PacketUpdateTileEntity(xCoord, yCoord, zCoord, -999, tag);
	}

	@Override
	public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt) {
		super.onDataPacket(net, pkt);
		readFromNBT(pkt.func_148857_g());
	}
}
