package akuto2.akutoengine.gui;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import ic2.core.GuiIconButton;
import ic2.core.IC2;
import ic2.core.block.wiring.TileEntityElectricBlock;
import ic2.core.network.NetworkManager;
import ic2.core.util.GuiTooltipHelper;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StatCollector;

@SideOnly(Side.CLIENT)
public class GuiUmfsUnit extends GuiContainer{
	private final ContainerUmfsUnit container;
	private final String armorInv;
	private final String level;
	private final String name;
	private static final ResourceLocation background = new ResourceLocation(IC2.textureDomain, "textures/gui/GUIElectricBlock.png");

	public GuiUmfsUnit(ContainerUmfsUnit container1){
		super(container1);

		this.ySize = 196;
		this.container = container1;
	    this.armorInv = StatCollector.translateToLocal("ic2.EUStorage.gui.info.armor");
	    this.level = StatCollector.translateToLocal("ic2.EUStorage.gui.info.level");
	    this.name = container1.base.getInventoryName();
	}

	@Override
	public void initGui(){
	    super.initGui();
	    this.buttonList.add(new GuiIconButton(0, (this.width - this.xSize) / 2 + 152, (this.height - this.ySize) / 2 + 4, 20, 20, new ItemStack(Items.redstone), true));
	}

	@Override
	protected void drawGuiContainerForegroundLayer(int par1, int par2){
	    this.fontRendererObj.drawString(this.name, (this.xSize - this.fontRendererObj.getStringWidth(this.name)) / 2, 6, 4210752);
	    this.fontRendererObj.drawString(this.armorInv, 8, this.ySize - 126 + 3, 4210752);

	    this.fontRendererObj.drawString(this.level, 79, 25, 4210752);
	    int e = (int)Math.min(((TileEntityElectricBlock)this.container.base).energy, ((TileEntityElectricBlock)this.container.base).maxStorage);
	    this.fontRendererObj.drawString(" " + e, 110, 35, 4210752);
	    this.fontRendererObj.drawString("/" + ((TileEntityElectricBlock)this.container.base).maxStorage, 110, 45, 4210752);

	    String output = StatCollector.translateToLocalFormatted("ic2.EUStorage.gui.info.output", new Object[] { Integer.valueOf(((TileEntityElectricBlock)this.container.base).output) });
	    this.fontRendererObj.drawString(output, 85, 60, 4210752);

	    GuiTooltipHelper.drawAreaTooltip(par1 - this.guiLeft, par2 - this.guiTop, ((TileEntityElectricBlock)this.container.base).getredstoneMode(), 153, 3, 172, 22);
	}

	@Override
	protected void drawGuiContainerBackgroundLayer(float f, int x, int y){
	    GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	    this.mc.getTextureManager().bindTexture(background);
	    int j = (this.width - this.xSize) / 2;
	    int k = (this.height - this.ySize) / 2;
	    drawTexturedModalRect(j, k, 0, 0, this.xSize, this.ySize);
	    if (((TileEntityElectricBlock)this.container.base).energy > 0.0D)
	    {
	      int i1 = (int)(24.0F * ((TileEntityElectricBlock)this.container.base).getChargeLevel());
	      drawTexturedModalRect(j + 79, k + 34, 176, 14, i1 + 1, 16);
	    }
	}

	@Override
	protected void actionPerformed(GuiButton guibutton){
	    super.actionPerformed(guibutton);
	    if (guibutton.id == 0) {
	      ((NetworkManager)IC2.network.get()).initiateClientTileEntityEvent((TileEntity)this.container.base, 0);
	    }
	}
}
