package blusunrize.immersiveengineering.client.gui.manual;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.renderer.OpenGlHelper;

import org.lwjgl.opengl.GL11;

import blusunrize.immersiveengineering.client.ClientUtils;

public class GuiButtonArrow extends GuiButton
{
	int type;
	public GuiButtonArrow(int id, int x, int y, int type)
	{
		super(id, x, y, type<2?16:10,type<2?10:16, "");
		this.type=type;
	}

	
	@Override
	public void drawButton(Minecraft mc, int mx, int my)
	{
		if (this.visible)
		{
			ClientUtils.bindTexture("immersiveengineering:textures/gui/manual.png");
			GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
			this.field_146123_n = mx >= this.xPosition && my >= this.yPosition && mx < this.xPosition + this.width && my < this.yPosition + this.height;
			GL11.glEnable(GL11.GL_BLEND);
			OpenGlHelper.glBlendFunc(770, 771, 1, 0);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			this.drawTexturedModalRect(this.xPosition, this.yPosition, type<2?0:type<3?16:26, 216+(type==1?10:0)+(field_146123_n?20:0), width,height);
			this.mouseDragged(mc, mx, my);
		}
	}
}