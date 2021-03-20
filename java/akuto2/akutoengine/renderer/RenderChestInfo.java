package akuto2.akutoengine.renderer;

import java.math.BigInteger;

import org.lwjgl.opengl.GL11;

import akuto2.akutoengine.AkutoEngine;
import akuto2.akutoengine.items.ItemBlockInfinityChest;
import akuto2.akutoengine.proxies.ClientProxy;
import akuto2.akutoengine.tiles.TileEntityInfinityChest;
import akuto2.akutoengine.utils.InfinityChestFormatUtils;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;
import net.minecraftforge.client.event.DrawBlockHighlightEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.util.ForgeDirection;

@SideOnly(Side.CLIENT)
public class RenderChestInfo {
	@SubscribeEvent
	public void renderGameOverlayEvent(RenderGameOverlayEvent.Text event) {
		EntityClientPlayerMP playerMP = ClientProxy.mc.thePlayer;
		ItemStack holdItem = playerMP.getCurrentEquippedItem();
		if(holdItem != null && holdItem.getItem() instanceof ItemBlockInfinityChest)
			renderInfinityChestItenInfoStatusBar(holdItem, event.partialTicks);
	}

	@SubscribeEvent
	public void drawBlockHighlightEvent(DrawBlockHighlightEvent event) {
		if(event.target.typeOfHit == MovingObjectType.BLOCK) {
			MovingObjectPosition pos = event.target;
			World world = event.player.worldObj;
			Block block = world.getBlock(pos.blockX, pos.blockY, pos.blockZ);
			TileEntity tile = world.getTileEntity(pos.blockX, pos.blockY, pos.blockZ);
			if(block == AkutoEngine.infinityChest && tile instanceof TileEntityInfinityChest) {
				BigInteger count = ((TileEntityInfinityChest)tile).getCount();
				if(count.compareTo(BigInteger.ZERO) > 0) {
					renderInfinityChestItemInfoOnBlock(count, pos, event.player, event.partialTicks);
				}
			}
		}
	}

	/**
	 * ツールスロット上部にチェストの中身の情報を表示する
	 */
	private void renderInfinityChestItenInfoStatusBar(ItemStack chest, float partialTicks) {
		if(!chest.hasTagCompound()) {
			return;
		}

		ItemStack contents = TileEntityInfinityChest.readStackFromNBT(chest.getTagCompound());
		if(contents != null) {
			BigInteger count = TileEntityInfinityChest.readCountFromNBT(chest.getTagCompound());
			String data = contents.getDisplayName() + " " + InfinityChestFormatUtils.formatStack(count, true, false);
			ScaledResolution resolution = new ScaledResolution(ClientProxy.mc, ClientProxy.mc.displayWidth, ClientProxy.mc.displayHeight);
			int x = resolution.getScaledWidth() >> 1;
			int y = resolution.getScaledHeight();
			int stringWidth = ClientProxy.mc.fontRenderer.getStringWidth(data) >> 1;
			ClientProxy.mc.fontRenderer.drawString(data, x - stringWidth, y - 72, 16777215);
		}
	}

	/**
	 * 無限チェストのブロックに現在のアイテムの個数を表示する
	 */
	private void renderInfinityChestItemInfoOnBlock(BigInteger count, MovingObjectPosition pos, EntityPlayer player, float partialTicks) {
		String chestData = InfinityChestFormatUtils.formatStack(count, true, false);
		float scale = 0.01F;
		GL11.glPushMatrix();
		GL11.glTranslated(-player.posX, -player.posY, -player.posZ);
		GL11.glTranslatef(pos.blockX + 0.5F, pos.blockY + 0.5F, pos.blockZ + 0.5F);
		int facing = pos.sideHit;
		ForgeDirection direction = ForgeDirection.getOrientation(facing);
		GL11.glTranslatef(0.5F * direction.offsetX, 0.5F * direction.offsetY, 0.5F * direction.offsetZ);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GL11.glScalef(-scale, -scale, scale);
		float rotationYaw = 0.0F;
		if(facing == 2)
			rotationYaw = 0.0F;
		if(facing == 3)
			rotationYaw = 180.0F;
		if(facing == 4)
			rotationYaw = 270.0F;
		if(facing == 5)
			rotationYaw = 90.0F;
		GL11.glRotatef(rotationYaw, 0.0F, 1.0F, 0.0F);
		float rotationPitch = 0.0F;
		if(facing == 1)
			rotationPitch = -90.0F;
		if(facing == 0)
			rotationPitch = 90.0F;
		GL11.glRotatef(rotationPitch, 1.0F, 0.0F, 0.0F);
		int i = ClientProxy.mc.fontRenderer.getStringWidth(chestData) / 2;
		ClientProxy.mc.fontRenderer.drawString(chestData, -i, 0, 16777215);
		GL11.glPopMatrix();
	}
}
