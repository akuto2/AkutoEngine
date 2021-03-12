package akuto2.akutoengine.packet;

import java.io.IOException;

import javax.annotation.Nullable;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufInputStream;
import io.netty.buffer.ByteBufOutputStream;
import io.netty.handler.codec.DecoderException;
import io.netty.handler.codec.EncoderException;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTSizeTracker;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;

public class PacketBufferAE extends PacketBuffer{
	public PacketBufferAE(ByteBuf wrapped) {
		super(wrapped);
	}

	public PacketBufferAE writeByteArray(byte[] array) {
		writeVarIntToBuffer(array.length);
		writeBytes(array);
		return this;
	}

	public byte[] readByteArray() {
		return readByteArray(readableBytes());
	}

	public byte[] readByteArray(int maxLength) {
		int i = readVarIntFromBuffer();

		if(i > maxLength) {
			throw new DecoderException("ByteArray with size " + i + " is bigger than allowed " + maxLength);
		}
		else {
			byte[] bytes = new byte[i];
			readBytes(bytes);
			return bytes;
		}
	}

	public PacketBufferAE writeItemStack(ItemStack stack) {
		if(stack == null) {
			writeShort(-1);
		}
		else {
			writeShort(Item.getIdFromItem(stack.getItem()));
			writeByte(stack.stackSize);
			writeShort(stack.getItemDamage());
			NBTTagCompound compound = null;

			if(stack.getItem().isDamageable() || stack.getItem().getShareTag()) {
				compound = stack.stackTagCompound;
			}

			writeCompundTag(compound);
		}
		return this;
	}

	public ItemStack readItemStack() throws IOException {
		int i = readShort();

		if(i < 0) {
			return null;
		}
		else {
			int count = readByte();
			int meta = readShort();
			ItemStack stack = new ItemStack(Item.getItemById(i), count, meta);
			stack.stackTagCompound = readCompoundTag();
			return stack;
		}
	}

	public PacketBufferAE writeCompundTag(@Nullable NBTTagCompound compound) {
		if(compound == null) {
			writeByte(0);
		}
		else {
			try {
				CompressedStreamTools.write(compound, new ByteBufOutputStream(this));
			}
			catch(IOException e) {
				throw new EncoderException(e);
			}
		}
		return this;
	}

	public NBTTagCompound readCompoundTag() throws IOException {
		int i = readerIndex();
		byte b0 = readByte();

		if(b0 == 0) {
			return null;
		}
		else {
			readerIndex(i);

			try {
				return CompressedStreamTools.func_152456_a(new ByteBufInputStream(this), new NBTSizeTracker(2097152L));
			}
			catch(IOException e) {
				throw new EncoderException(e);
			}
		}
	}
}
