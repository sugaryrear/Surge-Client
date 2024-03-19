package com.client.graphics.interfaces.impl;

import com.client.Client;
import com.client.Rasterizer3D;
import com.client.Sprite;
import com.client.engine.impl.MouseHandler;
import com.client.features.settings.Preferences;
import com.client.graphics.interfaces.RSInterface;

public class Slider {

	private int position = 86;

	private double value;

	private int x, y;

	private final double minValue, maxValue, length;

	private final Sprite[] images = new Sprite[2];

	public Slider(Sprite icon, Sprite background, double minimumValue, double maximumValue) {
		this.images[0] = icon;
		this.images[1] = background;
		this.minValue = this.value = minimumValue;
		this.maxValue = maximumValue;
		this.length = this.images[1].myWidth;
	}

	public void draw(int x, int y, int alpha) {
		this.x = x;
		this.y = y;
		images[1].drawSprite(x, y);//the background
		images[0].drawTransparentSprite(x + position - (int) (position / length * images[0].myWidth), y - images[0].myHeight / 2 + images[1].myHeight / 2, alpha);
	}

	public void handleClick(int mouseX, int mouseY, int offsetX, int offsetY, int contentType) {
		int mX = MouseHandler.mouseX;
		int mY = MouseHandler.mouseY;
	//	System.out.println("mX:  "+mX+" mY: "+mY+" offsetx: "+offsetX+" and x: "+x+"  offsety: "+offsetY+" y:"+y+" length: "+length);

		if(mX > 710)
			return;
		if (mX - offsetX >= x && mX - offsetX <= x + length
			&& mY - offsetY >= y + (images[1].myHeight / 2) - (images[0].myHeight / 2) && mY - offsetY <= y + (images[1].myHeight / 2) + (images[0].myHeight / 2)) {
			//System.out.println("are we here?");
			position = mouseX - x - offsetX;
			if (position >= length) {
				position = (int) length;
			}
			if (position <= 0) {
				position = 0;
			}
			value = minValue + ((mouseX - x - offsetX) / length) * (maxValue - minValue);
			if (value < minValue) {
				value = minValue;
			}
			if (value > maxValue) {
				value = maxValue;
			}
			int xxx = 525;

			//System.out.println("mX: " + (mouseX - xxx));
			//System.out.println("spriteX: " + images[0].xPosition);
			handleContent(contentType);
		}
	}

	public void handleClickSlide(int mouseX, int mouseY, int offsetX, int offsetY, int contentType) {
		int mX = MouseHandler.mouseX;
		int mY = MouseHandler.mouseY;
		//System.out.println("ur x: "+mX+" offset: "+offsetX+" and x: "+x);
		if(mX > 710)
			return;
		if (mX - offsetX >= x && mX - offsetX <= x + length
			&& mY - offsetY >= y + images[1].myHeight / 2 - images[0].myHeight / 2
			&& mY - offsetY <= y + images[1].myHeight / 2 + images[0].myHeight / 2) {
			//System.out.println("does it get hre?");
			position = mouseX - x - offsetX;
			if (position >= length) {
				position = (int) length;
			}
			if (position <= 0) {
				position = 0;
			}
			value = minValue + ((mouseX - x - offsetX) / length) * (maxValue - minValue);
			if (value < minValue) {
				value = minValue;
			}
			if (value > maxValue) {
				value = maxValue;
			}
			//if(RSInterface.interfaceCache[SettingsTabWidget.ZOOMTOGGLE].active)
			handleContent(contentType);
		} else {
			return;
		}
	}

	public final static int ZOOM = 1;
	public final static int BRIGHTNESS = 2;
	public final static int MUSIC = 3;
	public final static int SOUND = 4;
	public final static int AREA_SOUND = 5;

	private void handleContent(int contentType) {
		switch(contentType) {
			case ZOOM:
//				if(!RSInterface.interfaceCache[SettingsTabWidget.ZOOMTOGGLE].active)
//					return;
				Client.cameraZoom = (int) (minValue + maxValue - value);
				break;
			case BRIGHTNESS:
			//	System.out.println("min: "+minValue+" max: "+maxValue+" vlaue: "+value);
//				if(value < 0.75)
//					return;
//				if(value > 0.8)
//					return;
				Preferences.getPreferences().brightness = minValue + maxValue - value;
				Rasterizer3D.setBrightness(Preferences.getPreferences().brightness);
				break;
			case MUSIC:
				Preferences.getPreferences().musicVolume = value;
				break;
			case SOUND:
				Preferences.getPreferences().soundVolume = value;
				break;
			case AREA_SOUND:
				Preferences.getPreferences().areaSoundVolume = value;
				break;
		}
	}

	public double getPercentage() {
		return ((position / length) * 100);
	}

	public static void handleSlider(int mX, int mY) {

		int tabInterfaceId = Client.tabInterfaceIDs[Client.tabID];
//System.out.println("tab: "+Client.tabID);


		if (tabInterfaceId != -1) {

			if (tabInterfaceId == 42500) {
				if (RSInterface.interfaceCache == null) {
					//System.out.println("whsss");
					return;
				}

				tabInterfaceId = RSInterface.interfaceCache[42500].children[9];
			} // Settings tab adjustment
			//System.out.println("wh:"+tabInterfaceId);


			RSInterface widget = RSInterface.interfaceCache[tabInterfaceId];
			if (widget == null) {
				//System.out.println("whsss");
				return;
			}

			if (widget.children == null) {
			//	System.out.println("null here");
				return;
			}

			for (int childId : widget.children) {//so only children? yeah
				if(RSInterface.interfaceCache == null)
					return;
				RSInterface child = RSInterface.interfaceCache[childId];
				if(RSInterface.interfaceCache == null)
					return;
				if (child == null || child.slider == null)
					continue;
			//	System.out.println("x: "+mX+" my: "+mY+" type: "+MouseHandler.instance.clickType);
				child.slider.handleClick(mX, mY,  0, 0, child.contentType);
				if (MouseHandler.instance.clickType == 0) {
					return;
				}
				if (MouseHandler.instance.clickType == 2) {
					child.slider.handleClickSlide(mX, mY,  0,  0, child.contentType);
				}
			}
			Client.instance.tabAreaAltered = true;
		}
//for all other sliders!
//		int interfaceId = Client.instance.openInterfaceID;
//		if (interfaceId == 78000) {
//			RSInterface widget = RSInterface.interfaceCache[interfaceId];
//			for (int childId : widget.children) {
////				if (RSInterface.interfaceCache[SettingsTabWidget.ZOOMTOGGLE].active) {
////					return;
////				}
//				RSInterface child = RSInterface.interfaceCache[childId];
//				if (child == null || child.slider == null)//this prolly causes random ass scroll error when mouse wheel scrolling near the minimap?
//					continue;
//				child.slider.handleClick(mX, mY, 4, 4, child.contentType);
//			}
//		}
	}

	public void setValue(double value) {

		if (value < minValue) {
			value = minValue;
		}
		else if (value > maxValue) {
			value = maxValue;
		}

		this.value = value;
		double shift = 1 - ((value - minValue) / (maxValue - minValue));

		position = (int) (length * shift);
	}
}
