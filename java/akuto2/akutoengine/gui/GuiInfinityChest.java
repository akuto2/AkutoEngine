package akuto2.akutoengine.gui;

import org.lwjgl.opengl.GL11;

import akuto2.akutoengine.tiles.TileEntityInfinityChest;
import akuto2.akutoengine.utils.InfinityChestFormatUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

@SideOnly(Side.CLIENT)
public class GuiInfinityChest extends GuiContainer{
	private static final ResourceLocation TEXTURE = new ResourceLocation("akutoengine", "textures/gui/infinity_chest_gui.png");

	private InventoryPlayer inventoryPlayer;
	private TileEntityInfinityChest chest;

	public GuiInfinityChest(InventoryPlayer inventoryPlayer, TileEntityInfinityChest tile) {
		super(new ContainerInfinityChest(inventoryPlayer, tile));
		this.inventoryPlayer = inventoryPlayer;
		chest = tile;
		xSize = 176;
		ySize = 158;
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int x, int y) {
		fontRendererObj.drawString(StatCollector.translateToLocal("infinityChest.inventory"), 8, 5, 4210752);
		fontRendererObj.drawString(StatCollector.translateToLocal("container.inventory"), 8, 64, 4210752);
		if(chest.hasStack()) {
			ItemStack stack = chest.getStack(1);
			fontRendererObj.drawString(stack.getDisplayName(), 35, 16, 4210752);
			fontRendererObj.drawString(InfinityChestFormatUtils.formatStack(chest.getCount()), 47, 29, 4210752);
		}
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y) {
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		mc.renderEngine.bindTexture(TEXTURE);
		int j = (width - xSize) / 2;
		int k = (height - ySize) / 2;
		drawTexturedModalRect(j, k, 0, 0, xSize, ySize);
	}
}
