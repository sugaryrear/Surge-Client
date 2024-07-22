package com.client;

import ch.qos.logback.classic.Level;
import com.client.broadcast.Broadcast;
import com.client.broadcast.BroadcastManager;
import com.client.definitions.*;
import com.client.definitions.server.ItemDef;
import com.client.draw.expcounter.ExpCounter;
import com.client.draw.restoreoverlay.RestoreOverlay;
import com.client.engine.GameEngine;
import com.client.engine.impl.KeyHandler;
import com.client.engine.impl.MouseHandler;
import com.client.features.EntityTarget;
import com.client.features.ExperienceDrop;
import com.client.features.KillFeed;
import com.client.features.accounts.AccountManager;
import com.client.features.gametimers.GameTimer;
import com.client.features.gametimers.GameTimerHandler;
import com.client.features.settings.InformationFile;
import com.client.features.settings.Preferences;
import com.client.graphics.fadingscreen.BlackFadingScreen;
import com.client.graphics.fadingscreen.FadingScreen;
import com.client.graphics.interfaces.Configs;
import com.client.graphics.interfaces.RSInterface;
import com.client.graphics.interfaces.daily.DailyRewards;
import com.client.graphics.interfaces.eventcalendar.EventCalendar;
import com.client.graphics.interfaces.impl.*;
import com.client.graphics.interfaces.settings.Setting;
import com.client.graphics.interfaces.settings.SettingsInterface;
import com.client.graphics.loaders.SpriteLoader1;
import com.client.graphics.loaders.SpriteLoader2;
import com.client.graphics.loaders.SpriteLoader3;
import com.client.graphics.loaders.SpriteLoader4;
import com.client.graphics.textures.TextureProvider;
import com.client.model.Items;
import com.client.model.rt7_anims.AnimKeyFrameSet;
import com.client.script.ClientScripts;
import com.client.sign.Signlink;
import com.client.sound.Sound;
import com.client.sound.SoundType;
import com.client.ui.DevConsole;
import com.client.utilities.*;
import com.client.utilities.settings.Settings;
import com.client.utilities.settings.SettingsManager;
import com.google.common.base.Preconditions;
import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import com.jagex.rs2lib.movement.MoveSpeed;
import dorkbox.notify.Notify;
import dorkbox.notify.Pos;
import dorkbox.util.ActionHandler;
import lombok.extern.slf4j.Slf4j;
import net.runelite.api.Tile;
import net.runelite.api.*;
import net.runelite.api.clan.ClanRank;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldPoint;
import net.runelite.api.events.BeforeRender;
import net.runelite.api.events.ClientTick;
import net.runelite.api.events.GameStateChanged;
import net.runelite.api.events.ResizeableChanged;
import net.runelite.api.hooks.Callbacks;
import net.runelite.api.hooks.DrawCallbacks;
import net.runelite.api.vars.AccountType;
import net.runelite.api.widgets.WidgetID;
import net.runelite.api.widgets.WidgetInfo;
import net.runelite.rs.api.*;
import org.apache.commons.lang3.tuple.Pair;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.Point;
import java.awt.*;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.*;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.*;
import java.nio.file.Paths;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.Instant;
import java.util.List;
import java.util.Queue;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

import static com.client.SceneGraph.pitchRelaxEnabled;
import static com.client.engine.impl.MouseHandler.clickMode3;

@Slf4j
public class Client extends GameEngine implements RSClient {

	public String calculateRestore(int paramInt) {
		if(paramInt == 0)
			return "60";
		int i = (int) Math.floor(paramInt / 60);
		int j = paramInt - i * 60;
		String str1 = "" + i;
		String str2 = "" + j;
		if (j < 10) {
			str2 = str2;
		}
		if (i < 10) {
			str1 = str1;
		}
		return  str2;
	}
    private final RestoreOverlay restoreOverlay = new RestoreOverlay(this);
    public RestoreOverlay getRestoreOverlay() {
        return restoreOverlay;
    }
	private final SecondsTimer secondsTimer = new SecondsTimer(60);

	public SecondsTimer getSecondsTimer() {
		return secondsTimer;
	}

	public static final MapFunctionLoader mapfunctionCache = new MapFunctionLoader();
	private Npc npcDisplay;
	public int getHeight(int localX, int localY, int plane) {
		int sceneX = localX >> 7;
		int sceneY = localY >> 7;
		if (sceneX >= 0 && sceneY >= 0 && sceneX < 104 && sceneY < 104) {
			int[][][] tileHeights = this.tileHeights;

			int x = localX & ((1 << 7) - 1);
			int y = localY & ((1 << 7) - 1);
			int var8 = x * tileHeights[plane][sceneX + 1][sceneY] + ((1 << 7) - x) * tileHeights[plane][sceneX][sceneY] >> 7;
			int var9 = tileHeights[plane][sceneX][sceneY + 1] * ((1 << 7) - x) + x * tileHeights[plane][sceneX + 1][sceneY + 1] >> 7;
			return ((1 << 7) - y) * var8 + y * var9 >> 7;
		}
		return 0;
	}
//		calcEntityScreenPos(entity.x, i, entity.z);
	public PointInSpace worldToCanvas(int i, int j, int l) {//x = i, y= l
		if (i >= 128 && l >= 128 && i <= 13056 && l <= 13056) {
			int i1 = getCenterHeight(this.plane, l, i) - j; //getCenterHeight
			i -= this.xCameraPos;
			i1 -= this.zCameraPos;
			l -= this.yCameraPos;
			int j1 = Model.SINE[this.yCameraCurve];
			int k1 = Model.COSINE[this.yCameraCurve];
			int l1 = Model.SINE[this.xCameraCurve];
			int i2 = Model.COSINE[this.xCameraCurve];
			int j2 = l * l1 + i * i2 >> 16;
			l = l * i2 - i * l1 >> 16;
			i = j2;
			j2 = i1 * k1 - l * j1 >> 16;
			l = i1 * j1 + l * k1 >> 16;
			i1 = j2;

//			LocalPoint lp = actor.getLocalLocation();
//
//			int x = lp.getX() - ((size - 1) * Perspective.LOCAL_TILE_SIZE / 2);
//			int y = lp.getY() - ((size - 1) * Perspective.LOCAL_TILE_SIZE / 2);


			if (l >= 50) {
				spriteDrawX = (Rasterizer3D.originViewX + i * Rasterizer3D.fieldOfView / l) + (!isResized() ? 4 : 0);
				spriteDrawY = (Rasterizer3D.originViewY + i1 * Rasterizer3D.fieldOfView / l) + (!isResized() ? 4 : 0);
//return new PointInSpace(spriteDrawX,spriteDrawY);
//return new PointInSpace(Rasterizer3D.originViewX + (i << Rasterizer3D.fieldOfView) / y, Rasterizer3D.originViewY + (n9 << Rasterizer3D.fieldOfView) / y);

			}
//			else {
//				spriteDrawX = -1;
//				spriteDrawY = -1;
//			}
//			x -= xCameraPos;
//			y -= yCameraPos;
//			z -= zCameraPos;
//			int xCurve = xCameraCurve;
//			int yCurve = yCameraCurve;
//			int n4 = Model.SINE[yCurve];
//			int n5 = Model.COSINE[yCurve];
//			int n6 = Model.SINE[xCurve];
//			int n7 = Model.COSINE[xCurve];
//			int n8 = n7 * x + y * n6 >> 16;
//			y = n7 * y - n6 * x >> 16;
//			x = n8;
//			int n9 = n5 * z - y * n4 >> 16;
//			y = z * n4 + y * n5 >> 16;
//			if (y >= 50) {
//				//			spriteDrawX = (Rasterizer3D.originViewX + i * Rasterizer3D.fieldOfView / l) + (!isResized() ? 4 : 0);
//				return new PointInSpace(Rasterizer3D.originViewX + (x << Rasterizer3D.fieldOfView) / y, Rasterizer3D.originViewY + (n9 << Rasterizer3D.fieldOfView) / y);
//
//
//			//	return new PointInSpace(Rasterizer3D.originViewX + (x << Rasterizer3D.fieldOfView) / y, Rasterizer3D.originViewY + (n9 << Rasterizer3D.fieldOfView) / y);
//			}
		}//			spriteDrawX = (Rasterizer3D.originViewX + i * Rasterizer3D.fieldOfView / l) + (!isResized() ? 4 : 0);
		return null;
	}
	public Polygon getCanvasTilePolyForTile(int x, int y) {
		int world_tile_x =  (x * 128) + 64;
		int world_tile_y =  (y * 128) + 64;
		return getCanvasTileAreaPolyReal(new LocalPoint_2(world_tile_x,world_tile_y), 1);
	}

	public Polygon getCanvasTileAreaPolyReal(LocalPoint_2 localLocation, int size) {
		final int swX = localLocation.getX() - size * 128 / 2;
		final int swY = localLocation.getY() - size * 128 / 2;
		final int neX = localLocation.getX() + size * 128 / 2;
		final int neY = localLocation.getY() + size * 128 / 2;

		final byte[][][] tileSettings = tileFlags;

		final int sceneX = localLocation.getSceneX();
		final int sceneY = localLocation.getSceneY();

		if (sceneX < 0 || sceneY < 0 || sceneX >= 104 || sceneY >= 104)
			return null;

		int tilePlane = this.plane;
		if (this.plane < 3 && (tileSettings[1][sceneX][sceneY] & 2) == 2)
			tilePlane = this.plane + 1;

		final int swHeight = getHeight(swX, swY, tilePlane);
		final int nwHeight = getHeight(neX, swY, tilePlane);
		final int neHeight = getHeight(neX, neY, tilePlane);
		final int seHeight = getHeight(swX, neY, tilePlane);

		PointInSpace p1 = worldToCanvas(swX, swY, swHeight);
		PointInSpace p2 = worldToCanvas(neX, swY, nwHeight);
		PointInSpace p3 = worldToCanvas(neX, neY, neHeight);
		PointInSpace p4 = worldToCanvas(swX, neY, seHeight);

		if (p1 == null || p2 == null || p3 == null || p4 == null)
			return null;

		Polygon poly = new Polygon();
		poly.addPoint(p1.getX(), p1.getY());
		poly.addPoint(p2.getX(), p2.getY());
		poly.addPoint(p3.getX(), p3.getY());
		poly.addPoint(p4.getX(), p4.getY());
		return poly;
	}
	public final void init() {
		//System.out.println("Init3423");
		Signlink.createCacheDirectory();
		try {
			enableExceptionLogging(); // Don't remove this!
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

		nodeID = 1;
		portOff = 0;
		setHighMem();
		isMembers = true;

		Signlink.storeid = 32;
		try {
			Signlink.startpriv(InetAddress.getLocalHost());
		} catch (UnknownHostException e) {
			throw new RuntimeException(e);
		}

		instance = this;
		newSmallFont = new RSFont(false, "p11_full");
		newBoldFont = new RSFont(false, "b12_full");

		startThread(765, 503, 206, 1);
		setMaxCanvasSize(765, 503);
		setFocusable(false);
		setFocusTraversalKeysEnabled(false);
		Sprite.start();
	}



	@Override
	protected void vmethod1099() {

	}

	private int[] aintIntTest = new int[5];
	protected final void resizeGame() {
		Client.instance.setBounds();
	}

	private static final Logger logger = LoggerFactory.getLogger("Client");
	public String lastViewedDropTable;

	public boolean isServerUpdating() {
		return anInt1104 > 0;
	}

	public DevConsole devConsole = new DevConsole();
	public int scrollbar_position;
	int scrollMax = 0;
	public void draw_scrollbar(int x, int y, int width, int height, int offset, int max_lines, int height_seperator, int y_pad)
	{
		scrollMax = (offset * max_lines + (height_seperator - 1));
		if(scrollMax < height - 3)
			scrollMax = height - 3;

		aClass9_1059.scrollPosition = scrollMax - scrollbar_position - (height - 4);
		if(scrollMax > height)
			method65(x, height, MouseHandler.mouseX - 0, MouseHandler.mouseY - y, aClass9_1059, y_pad, true, scrollMax);

		int pos = scrollMax - (height - 4) - aClass9_1059.scrollPosition;
		if(pos < 0)
			pos = 0;

		if(pos > scrollMax - (height - 4))
			pos = scrollMax - (height - 4);

		if(scrollbar_position != pos)
			scrollbar_position = pos;

		drawScrollbar(height, scrollMax - scrollbar_position - (height - 1), x, y, scrollMax);
	}

	private void drawGrid() {
		for (int index = 0; index < 516; index += 10) {
			if (index < 334) {
				Rasterizer2D.drawHorizontalLine2(0, index, 516, 0xff0000);
			}
			Rasterizer2D.drawVerticalLine2(index, 0, 334, 0xff0000);
		}

		int xPos = MouseHandler.mouseX - 4 - ((MouseHandler.mouseX - 4) % 10);
		int yPos = MouseHandler.mouseY - 4 - ((MouseHandler.mouseY - 4) % 10);

		Rasterizer2D.drawTransparentBox(xPos, yPos, 10, 10, 0xffffff, 255);
		newSmallFont.drawCenteredString("(" + (xPos + 4) + ", " + (yPos + 4) + ")", xPos + 4, yPos - 1, 0xffff00, 0);
	}

	private boolean screenFlashDrawing;
	private boolean screenFlashOpacityDownward;
	private double screenFlashOpacity = 0d;
	private int screenFlashColor = 0;
	private int screenFlashMaxIntensity = 30;
	private boolean screenFlashAutoFadeOut;

	public boolean fogEnabled;
	public int fogOpacity;

	public void drawScreenBox() {
		if (screenFlashDrawing) {
			double rate = 1_000d / fps / 20d;
			if (screenFlashOpacityDownward) {
				screenFlashOpacity -= rate;
				if (screenFlashOpacity <= 10) {
					screenFlashOpacityDownward = false;
					screenFlashDrawing = false;
				}
			} else {
				screenFlashOpacity += rate;
				if (screenFlashOpacity >= screenFlashMaxIntensity) {
					screenFlashOpacityDownward = true;
				}
			}
			Rasterizer2D.drawAlphaGradient(0, 0, canvasWidth, canvasHeight, screenFlashColor, screenFlashColor, (int) screenFlashOpacity);
		}

		if (fogEnabled) {
			Rasterizer2D.drawAlphaBox(0, 0, canvasWidth, canvasHeight, 0xE67D2D, fogOpacity);
		}
	}


	public int getDragSetting(int interfaceId) {
		return interfaceId == 3214 ? Preferences.getPreferences().dragTime : 5;
	}



	public static int IDLE_TIME = 30000; // 1 minute = 3000
	public boolean hintMenu;
	public String hintName;
	public int hintId;

	private boolean pollActive;

	private Pattern pattern;

	private Matcher matcher;

	private boolean placeHolders = true;

	public static boolean debugModels = false;

	private EntityTarget entityTarget;

	private InformationFile informationFile = new InformationFile();

	public static boolean snowVisible = Configuration.CHRISTMAS ? true : false;

	public static int[][] runePouch = new int[][] { { -1, -1 }, { -1, -1 }, { -1, -1 } };

	public static int itemX = 0, itemY = 0;

	private static boolean shiftDrop = true;

	public static boolean shiftDown;

	private static boolean removeShiftDropOnMenuOpen;

	public static Settings getUserSettings() {
		return userSettings;
	}

	public static void setUserSettings(Settings settings) {
		Client.userSettings = settings;
	}

	private static Settings userSettings;

	/*
	 * @Override public void keyPressed(KeyEvent event) { MouseHandler.keyPressed(event);
	 * if(loggedIn) { stream.createFrame(186); stream.writeWord(event.getKeyCode());
	 * } }
	 */
	public static void mouseMoved() {
		if (loggedIn)
			// if(idleTime >= (Client.IDLE_TIME - 500))
			stream.createFrame(187);
	}

	/**
	 * Handles rune pouch input
	 * @return
	 * @author Sky
	 */
	private void loadPlayerData() throws IOException {
		File file = new File(Signlink.getCacheDirectory() + "/fkeys.dat");
		if (!file.exists()) {
			return;
		}

		DataInputStream stream = new DataInputStream(new FileInputStream(file));

		try {


			for(int i = 0; i < Keybinding.KEYBINDINGS.length; i++) {
				Keybinding.KEYBINDINGS[i] = stream.readByte();
			}

		} catch (IOException e) {
			System.out.println("Unable to load player data.");
			file.delete();
		} finally {
			stream.close();
		}
	}
	public void savePlayerData() {
		try {
			File file = new File(Signlink.getCacheDirectory() + "/fkeys.dat");
			if (!file.exists()) {
				file.createNewFile();
			}
			DataOutputStream stream = new DataOutputStream(new FileOutputStream(file));

			try {
				for(int i = 0; i < 14; i++) {
					stream.writeByte(Keybinding.KEYBINDINGS[i]);
				}
			} finally {
				stream.close();
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void handleScrollPosition(String text, int frame) {
		if (text.startsWith(":scp:")) {
			int scrollPosition = Integer.parseInt(text.split(" ")[1]);
			RSInterface widget = RSInterface.interfaceCache[frame];
			widget.scrollPosition = scrollPosition;
		} else if (text.startsWith(":scpfind:")) {
			RSInterface widget = RSInterface.interfaceCache[frame];
			sendString(7, widget.scrollPosition + "");
		}

		return;
	}
	public static boolean handleRunePouch(String text, int frame) {
		if (!(text.startsWith("#") && text.endsWith("$") && frame == 49999)) {
			return false;
		}
		try {
			// System.out.println("got here");
			text = text.replace("#", "");
			text = text.replace("$", "");
			String[] runes = text.split("-");
			for (int index = 0; index < runes.length; index++) {
				String[] args = runes[index].split(":");
				int id = Integer.parseInt(args[0]);
				int amt = Integer.parseInt(args[1]);
				runePouch[index][0] = id;
				runePouch[index][1] = amt;
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}
	}

	private boolean updateGameMode = false;

	public void frameMode(boolean resizable) {
		if(isResized() == resizable) {
			return;
		}

		setResized(resizable);

		Bounds bounds = getFrameContentBounds();
		canvasWidth = !isResized() ? 765 : bounds.highX;
		canvasHeight = !isResized() ? 503 : bounds.highY;
		cameraZoom = !isResized() ? 600 : 850;
		changeTabArea = !isResized() ? false : true;
		changeChatArea = !isResized() ? false : true;
		setMaxCanvasSize(canvasWidth, canvasHeight);
		ResizeableChanged event = new ResizeableChanged();
		event.setResized(resizable);
		callbacks.post(event);

		setBounds();

		showChatComponents = !isResized() || showChatComponents;
		showTabComponents = !isResized() || showTabComponents;
	}

	private void setBounds() {
		Preferences.getPreferences().fixed = !isResized();
		Preferences.getPreferences().screenWidth = canvasWidth;
		Preferences.getPreferences().screenHeight = canvasHeight;

		Rasterizer3D.reposition(canvasWidth, canvasHeight);
		fullScreenTextureArray = Rasterizer3D.scanOffsets;
		anIntArray1180 = Rasterizer3D.scanOffsets;
		anIntArray1181 = Rasterizer3D.scanOffsets;
		Rasterizer3D.scanOffsets = new int[canvasHeight];
		for (int x = 0; x < canvasHeight; x++) {
			Rasterizer3D.scanOffsets[x] = canvasWidth * x;
		}
		anIntArray1182 = Rasterizer3D.scanOffsets;
		Rasterizer3D.originViewX = instance.getViewportWidth() / 2;
		Rasterizer3D.originViewY = instance.getViewportHeight() / 2;

		Rasterizer3D.fieldOfView = instance.getViewportWidth() * instance.getViewportHeight() / 85504 << 1;

		int[] ai = new int[9];
		for (int i8 = 0; i8 < 9; i8++) {
			int k8 = 128 + i8 * 32 + 15;
			int l8 = 600 + k8 * 3;
			int i9 = Rasterizer3D.SINE[k8];
			ai[i8] = (l8 * i9 >> 16);
		}
		SceneGraph.buildVisibilityMap(500, 800, instance.getViewportWidth(), instance.getViewportHeight(), ai);
	}


	private long experienceCounter;
	private Sprite mapBack;
	public Sprite[] smallXpSprites = new Sprite[23];
	public Sprite[] xpbarsprites = new Sprite[24];
	private static final long serialVersionUID = 1L;
	private Sprite[] inputSprites = new Sprite[7];

	private int modifiableXValue = 1; // u dont care if it starts at 1? Can't see a real problem with it :P kk
	private int achievementCutoff = 100;
	private Sprite[] minimapIcons = new Sprite[2];
	private String macAddress;

	public static void dumpModels() {
		int id = 6610;
		NpcDefinition npc = NpcDefinition.lookup(id);
		System.out.println(npc.name);
		if (npc.models != null) {
			for (int modelid = 0; modelid < npc.models.length; modelid++) {
				System.out.println(npc.models[modelid]);
			}
		}
	}

	private static String intToKOrMilLongName(int i) {
		String s = String.valueOf(i);
		for (int k = s.length() - 3; k > 0; k -= 3)
			s = s.substring(0, k) + "," + s.substring(k);
		if (s.length() > 8)
			s = "@gre@" + s.substring(0, s.length() - 8) + " million @whi@(" + s + ")";
		else if (s.length() > 4)
			s = "@cya@" + s.substring(0, s.length() - 4) + "K @whi@(" + s + ")";
		return " " + s;
	}

	private static String intToKOrMil(int j) {
		if (j < 0x186a0)
			return String.valueOf(j);
		if (j < 0x989680)
			return j / 1000 + "K";
		else
			return j / 0xf4240 + "M";
	}

	public void stopMidi() {
		Signlink.midifade = 0;
		Signlink.midi = "stop";
	}

	public static int spellID = 0;

	public static void setTab(int id) {
		needDrawTabArea = true;
		tabID = id;
	//	pushMessage("anything? "+tabID, 0, "");
		tabAreaAltered = true;
	}

	public void setSidebarInterface(int sidebarID, int interfaceID) {
		tabInterfaceIDs[sidebarID] = interfaceID;
		tabID = sidebarID;
		pushMessage("anything? "+tabID, 0, "");
		needDrawTabArea = true;
		tabAreaAltered = true;
	}

	private boolean menuHasAddFriend(int j) {
		if (j < 0)
			return false;
		int k = menuActionID[j];
		if (k >= 2000)
			k -= 2000;
		return k == 337;
	}

	private Sprite channelButtons;
	public Instant startTime;

	public void gettime() {
		startTime = Instant.now();

	}
	public Duration getTimeSinceStart() {
		//   stopwatch.stop();
		//  d.toMillis()
		return Duration.between(startTime, Instant.now());
		//   return String.format("%02d:%02d", d.toMinutes(), d.getSeconds() % 60);
	}
	private void drawChannelButtons() {
		Duration d = getTimeSinceStart();
		String time_reportbutton = String.format("%02d:%02d:%02d", d.toHours(), d.toMinutes() % 60, d.getSeconds() % 60);


		String text[] = { "On", "Friends", "Off", "Hide" };
		int textColor[] = { 65280, 0xffff00, 0xff0000, 65535 };
		int yOffset = (!isResized() ? 338 : canvasHeight - 165);

		if (hideChatArea) {
			channelButtons.drawSprite(0, canvasHeight - 23);
		}
        Date date = new Date();

        DateFormat time = new SimpleDateFormat("h:mm a", Locale.ENGLISH);
        time.setTimeZone(TimeZone.getTimeZone("PST"));
        RSInterface clanchattime = RSInterface.interfaceCache[64890];
        clanchattime.message =  time.format(date)+" PST";


		switch (channelButtonClickPosition) {
			case 0:
				chatButtons[1].drawSprite(5, 142 + yOffset);
				break;
			case 1:
				chatButtons[1].drawSprite(71, 142 + yOffset);
				break;
			case 2:
				chatButtons[1].drawSprite(137, 142 + yOffset);
				break;
			case 3:
				chatButtons[1].drawSprite(203, 142 + yOffset);
				break;
			case 4:
				chatButtons[1].drawSprite(269, 142 + yOffset);
				break;
			case 5:
				chatButtons[1].drawSprite(335, 142 + yOffset);
				break;
		}
		if (channelButtonHoverPosition == channelButtonClickPosition) {
			switch (channelButtonHoverPosition) {
				case 0:
					chatButtons[2].drawSprite(5, 142 + yOffset);
					break;
				case 1:
					chatButtons[2].drawSprite(71, 142 + yOffset);
					break;
				case 2:
					chatButtons[2].drawSprite(137, 142 + yOffset);
					break;
				case 3:
					chatButtons[2].drawSprite(203, 142 + yOffset);
					break;
				case 4:
					chatButtons[2].drawSprite(269, 142 + yOffset);
					break;
				case 5:
					chatButtons[2].drawSprite(335, 142 + yOffset);
					break;
				case 6:
					chatButtons[3].drawSprite(404, 142 + yOffset);
					break;
			}
		} else {
			switch (channelButtonHoverPosition) {
				case 0:
					chatButtons[0].drawSprite(5, 142 + yOffset);
					break;
				case 1:
					chatButtons[0].drawSprite(71, 142 + yOffset);
					break;
				case 2:
					chatButtons[0].drawSprite(137, 142 + yOffset);
					break;
				case 3:
					chatButtons[0].drawSprite(203, 142 + yOffset);
					break;
				case 4:
					chatButtons[0].drawSprite(269, 142 + yOffset);
					break;
				case 5:
					chatButtons[0].drawSprite(335, 142 + yOffset);
					break;
				case 6:
					chatButtons[3].drawSprite(404, 142 + yOffset);
					break;
			}
		}


		newSmallFont.drawCenteredString(time_reportbutton, 459, 157 + yOffset, 0xffffff, 0);
		smallText.method389(true, 26, 0xffffff, "All", 157 + yOffset);
		smallText.method389(true, 86, 0xffffff, "Game", 152 + yOffset);
		smallText.method389(true, 150, 0xffffff, "Public", 152 + yOffset);
		smallText.method389(true, 212, 0xffffff, "Private", 152 + yOffset);
		smallText.method389(true, 286, 0xffffff, "Clan", 152 + yOffset);
		smallText.method389(true, 349, 0xffffff, "Trade", 152 + yOffset);
		smallText.drawCentered(textColor[gameMode], 98, text[gameMode], 163 + yOffset, true);
		smallText.drawCentered(textColor[publicChatMode], 164, text[publicChatMode], 163 + yOffset, true);
		smallText.drawCentered(textColor[privateChatMode], 230, text[privateChatMode], 163 + yOffset, true);
		smallText.drawCentered(textColor[clanChatMode], 296, text[clanChatMode], 163 + yOffset, true);
		smallText.drawCentered(textColor[tradeMode], 362, text[tradeMode], 163 + yOffset, true);
	}

	ItemSearch grandExchangeItemSearch = null;

	public static int grandExchangeSearchScrollPostion;

	private static void handleGEItemSearchClick(int itemId) {
		// TODO Send Item Packet
		System.out.println("Clicking Item: " + itemId);
	}

	private void drawChatArea() {
		if (!isResized())
			hideChatArea = false;

		final int yOffset = (!isResized() ? 338 : canvasHeight - 165);
		final int yOffset2 = !isResized() ? 338 : 0;

		Rasterizer3D.scanOffsets = anIntArray1180;

		if (hideChatArea)
			drawChannelButtons();

		if (!hideChatArea) {
			chatArea.drawSprite(0, yOffset);
			drawChannelButtons();

			TextDrawingArea textDrawingArea = aTextDrawingArea_1271;
			if (MouseHandler.saveClickX >= 0 && MouseHandler.saveClickX <= 518) {
				if (MouseHandler.saveClickY >= (!isResized() ? 335 : canvasHeight - 164)
						&& MouseHandler.saveClickY <= (!isResized() ? 484 : canvasHeight - 30)) {
					if (this.isFieldInFocus()) {
						Client.inputString = "";
						this.resetInputFieldFocus();
					}
				}
			}
			if (messagePromptRaised) {
				newBoldFont.drawCenteredString(aString1121, 259, 60 + yOffset, 0, -1);
				newBoldFont.drawCenteredString(promptInput + "*", 259, 80 + yOffset, 128, -1);
			} else if (inputDialogState == 1) {
				newBoldFont.drawCenteredString(enterInputInChatString, 259, 60 + yOffset, 0, -1);
				newBoldFont.drawCenteredString(amountOrNameInput + "*", 259, 80 + yOffset, 128, -1);
			} else if (inputDialogState == 2) {
				newBoldFont.drawCenteredString(enterInputInChatString, 259, 60 + yOffset, 0, -1);
				newBoldFont.drawCenteredString(amountOrNameInput + "*", 259, 80 + yOffset, 128, -1);
			} else if (inputDialogState == 7) {
				newBoldFont.drawCenteredString("Enter the price for the item:", 259, 60 + yOffset, 0, -1);
				newBoldFont.drawCenteredString(amountOrNameInput + "*", 259, 80 + yOffset, 128, -1);
			} else if (inputDialogState == 8) {
				newBoldFont.drawCenteredString("Amount you want to sell:", 259, 60 + yOffset, 0, -1);
				newBoldFont.drawCenteredString(amountOrNameInput + "*", 259, 80 + yOffset, 128, -1);
			} else if (Bank.isSearchingBank()) {
				newBoldFont.drawCenteredString("Enter an item to search for:", 259, 60 + yOffset, 0, -1);
				newBoldFont.drawCenteredString(Bank.searchingBankString + "*", 259, 80 + yOffset, 128, -1);
			} else if (inputDialogState == 3) {
				Rasterizer2D.fillPixels(8, 505, 108, 0x463214, 28 + yOffset);
				Rasterizer2D.drawAlphaBox(8, 28 + yOffset, 505, 108, 0x746346, 75);

				newBoldFont.drawCenteredString("@bla@What would you like to buy? @blu@" + amountOrNameInput + "*", 259,
						20 + yOffset, 128, -1);
				if (amountOrNameInput != "") {
					grandExchangeItemSearch = new ItemSearch(amountOrNameInput, 100, true);

					final int xPosition = 15;
					final int yPosition = 32 + yOffset - grandExchangeSearchScrollPostion;
					int rowCountX = 0;
					int rowCountY = 0;

					int itemAmount = grandExchangeItemSearch.getItemSearchResultAmount();

					if (amountOrNameInput.length() == 0) {
						newRegularFont.drawCenteredString("Start typing the name of an item to search for it.", 259,
								70 + yOffset, 0, -1);
					} else if (itemAmount == 0) {
						newRegularFont.drawCenteredString("No matching items found!", 259, 70 + yOffset, 0, -1);
					} else {
						Rasterizer2D.setDrawingArea(134 + yOffset, 8, 497, 29 + yOffset);
						for (int itemId = 0; itemId < itemAmount; itemId++) {
							int itemResults[] = grandExchangeItemSearch.getItemSearchResults();

							if (itemResults[itemId] != -1) {
								final int startX = xPosition + rowCountX * 160;
								final int startY = yPosition + rowCountY * 35;
								Sprite itemSprite = ItemDefinition.getSprite(itemResults[itemId], 1, 0);
								if (itemSprite != null)
									itemSprite.drawSprite(startX, startY);

								ItemDefinition itemDef = ItemDefinition.lookup(itemResults[itemId]);
								newRegularFont.drawBasicString(itemDef.name, startX + 40, startY + 14, 0, -1);

								if (MouseHandler.mouseX >= startX && MouseHandler.mouseX <= startX + 160) {
									if (MouseHandler.mouseY >= (startY + yOffset2)
											&& MouseHandler.mouseY <= (startY + yOffset2) + 35) {
										Rasterizer2D.drawAlphaBox(startX, startY, 160, 35, 0xFFFFFF, 120);

										if (clickMode3 == 1)
											handleGEItemSearchClick(itemDef.id);
									}
								}
							}

							rowCountX++;

							if (rowCountX > 2) {
								rowCountY++;
								rowCountX = 0;
							}
						}
						Rasterizer2D.defaultDrawingAreaSize();
					}

					int maxScrollPosition = itemAmount / 3 * 35;
					if (itemAmount > 9)
						drawScrollbar(106, grandExchangeSearchScrollPostion > maxScrollPosition ? 0
								: grandExchangeSearchScrollPostion, 29 + yOffset, 496, itemAmount / 3 * 35);

				}
			} else if (clickToContinueString != null) {
				newBoldFont.drawCenteredString(clickToContinueString, 259, 60 + yOffset, 0, -1);
				newBoldFont.drawCenteredString("Click to continue", 259, 80 + yOffset, 128, -1);
			} else if (backDialogID != -1) {
				drawInterface(0, 20, RSInterface.interfaceCache[backDialogID], 20 + yOffset);
			} else if (dialogID != -1) {
				drawInterface(0, 20, RSInterface.interfaceCache[dialogID], 20 + yOffset);
			} else {
				int j77 = -3;
				int j = 0;
				Rasterizer2D.setDrawingArea(122 + yOffset, 8, 497, 7 + yOffset);
				for (int k = 0; k < 500; k++)
					if (chatMessages[k] != null) {
						// System.out.println(chatMessages[k]);
						int chatType = chatTypes[k];
						int yPos = (70 - j77 * 14) + anInt1089 + 5;
						String s1 = chatNames[k];
						//byte byte0 = 0;

						List<Integer> crowns = new ArrayList<>();

						while (s1.startsWith("@cr")) {
							String s2 = s1.substring(3, s1.length());
							int index = s2.indexOf("@");
							if (index != -1) {
								s2 = s2.substring(0, index);
								crowns.add(Integer.parseInt(s2));
								s1 = s1.substring(4 + s2.length());
							}
						}
						if (chatType == 0) {
							if (chatTypeView == 5 || chatTypeView == 0) {
								if (yPos > 0 && yPos < 210) {
									newRegularFont.drawBasicString(chatMessages[k], 10, yPos - 1 + yOffset, 0, -1);
								}
								j++;
								j77++;
							}
						}
						//chattype view is the buttons on bottom for example "all" is 0 "game" is 5
						//System.out.println("chatType: "+chatType+"  chatTypev: "+chatTypeView);
						if ((chatType == 1 || chatType == 2) && (chatType == 1 || publicChatMode == 0
								|| publicChatMode == 1 && isFriendOrSelf(s1))) {
							if (chatTypeView == 1 || chatTypeView == 0) {//EVERYTHING HEAR IS WHAT HAPPENS WHEN
								//System.out.println("test?");
								if (yPos > 0 && yPos < 210) {
									int xPos = 11;
									if (!crowns.isEmpty()) {
								//	System.out.println("crowns?");
										for (int crown : crowns) {
											for (int right = 0; right < modIcons.length; right++) {
											//	System.out.println("right: "+right);
												if (right == (crown - 1) && modIcons[right] != null) {
												//System.out.println("r: "+right+" crown: "+crown);
													modIcons[right].drawAdvancedSprite(xPos - 1,
															yPos + yOffset - modIcons[right].myHeight);

//this will actually keep going indefinitely lol even at full hp
//                                                    String cHP = RSInterface.interfaceCache[4016].message;
//                                                    String mHP = RSInterface.interfaceCache[4017].message;
//                                                    int currentHP = Integer.parseInt(cHP);
//                                                    int maxHP = Integer.parseInt(mHP);
//                                                    if (regenHealthStart > 0) {
//                                                        float difference = (int) (System.currentTimeMillis() - regenHealthStart);
//                                                        float angle = (difference / (rapidHeal ? REGEN_HEALTH_TIME_RAPID_HEAL : REGEN_HEALTH_TIME)) * 360.0f;
//                                                        //System.out.println("percent: "+percent+" angle: "+angle+" level:"+level);
//                                                        //if (setting.draw_orb_arc) {
//                                                        Rasterizer2D.draw_arc(xPos - 1, yPos + yOffset, 28, 28, 2, 90, -(int) angle, 0xff0000, 210, 0,
//                                                                false);
//                                                        //	}
//                                                        if (angle > 358.0f && currentHP != lastHp) {
//                                                            regenHealthStart = System.currentTimeMillis();
//                                                            restorehealth();
//                                                            lastHp = currentHP;
//                                                        }
//
//                                                    }
													xPos += modIcons[right].myWidth;
													xPos += 2;
													break;
												}
											}
										}
									}
									newRegularFont.drawBasicString( StringUtils.fixName(capitalize(s1)) + ":", xPos - 1, yPos - 1 + yOffset, 0, -1);//the name of urself in regular chat
									xPos += newRegularFont.getTextWidth(s1) + 5;
									newRegularFont.drawBasicString(chatMessages[k], xPos - 1, yPos - 1 + yOffset, 255,-1, false);
								}
								j++;
								j77++;
							}
						}

						if (chatType == 99) {
							if (yPos > 0 && yPos < 210) {
								lastViewedDropTable = chatMessages[k];
								newRegularFont.drawBasicString(s1 + " " + chatMessages[k], 10, yPos - 1 + yOffset, 0x7e3200, -1, false);
							}
							j++;
							j77++;
						}


						if ((chatType == 3 || chatType == 7) && (!splitPrivateChat || chatTypeView == 2)
								&& (chatType == 7 || privateChatMode == 0
								|| privateChatMode == 1 && isFriendOrSelf(s1))) {
							if (chatTypeView == 2 || chatTypeView == 0) {
								if (yPos > 0 && yPos < 210) {
									int k1 = 11;
									newRegularFont.drawBasicString("From", k1, yPos - 1 + yOffset, 0, -1);
									k1 += textDrawingArea.getTextWidth("From ");
									if (!crowns.isEmpty()) {
										for (int crown : crowns) {
											for (int right = 0; right < modIcons.length; right++) {
												if (right == (crown - 1) && modIcons[right] != null) {
													modIcons[right].drawAdvancedSprite(k1,
															yPos + yOffset - modIcons[right].myHeight);
													k1 += modIcons[right].myWidth;
													break;
												}
											}
										}
									}
									newRegularFont.drawBasicString(s1 + ":", k1, yPos - 1 + yOffset, 0, -1);
									k1 += newRegularFont.getTextWidth(s1) + 8;
									newRegularFont.drawBasicString(chatMessages[k], k1, yPos - 1 + yOffset, 0x800000,-1, false);
								}
								j++;
								j77++;
							}
						}
						if (chatType == 4 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s1))) {
							if (chatTypeView == 3 || chatTypeView == 0) {
								if (yPos > 0 && yPos < 210)
									newRegularFont.drawBasicString(s1 + " " + chatMessages[k], 11, yPos - 1 + yOffset,0x800080, -1, false);
								j++;
								j77++;
							}
						}
						if (chatType == 31 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s1))) {
							int crownId = 29;//just flower poker chat
							if (chatTypeView == 3 || chatTypeView == 0) {
								if (yPos > 0 && yPos < 210) {
									int xPos = 11;
									modIcons[crownId].drawAdvancedSprite(xPos - 1, yPos + yOffset - modIcons[crownId].myHeight);
									xPos += modIcons[crownId].myWidth;
									newRegularFont.drawBasicString(s1 + " " + chatMessages[k], xPos - 1, yPos - 1 + yOffset, 0x1950ce, -1, false);
								}
								j++;
								j77++;
							}
						}
						if (chatType == 310) {
						//	int crownId = 29;
							//if (chatTypeView == 3 || chatTypeView == 0) {
								if (yPos > 0 && yPos < 210) {
									int xPos = 11;
								//	modIcons[crownId].drawAdvancedSprite(xPos - 1, yPos + yOffset - modIcons[crownId].myHeight);
									//xPos += modIcons[crownId].myWidth;
									s1 = s1.replaceAll("_", " ");
									newRegularFont.drawBasicString(s1 + " " + chatMessages[k], xPos - 1, yPos - 1 + yOffset, 0x1950ce, -1, false);
								}
								j++;
								j77++;
							//}
						}
						if (chatType == 5 && !splitPrivateChat && privateChatMode < 2) {
							if (chatTypeView == 2 || chatTypeView == 0) {
								if (yPos > 0 && yPos < 210)
									newRegularFont.drawBasicString(chatMessages[k], 10, yPos - 1 + yOffset, 0x800000,-1, false);
								j++;
								j77++;
							}
						}
						if (chatType == 6 && (!splitPrivateChat || chatTypeView == 2) && privateChatMode < 2) {
							if (chatTypeView == 2 || chatTypeView == 0) {
								if (yPos > 0 && yPos < 210) {
									int k1 = 15;
									k1 += textDrawingArea.getTextWidth("To " + s1);
									newRegularFont.drawBasicString("To " + s1 + ":", 10, yPos - 1 + yOffset, 0, -1);
									// newRegularFont.drawBasicString(chatMessages[k], 15 +
									// newRegularFont.getTextWidth("To :" + s1), yPos, 0x800000, -1);
									newRegularFont.drawBasicString(chatMessages[k], k1, yPos - 1 + yOffset, 0x800000,-1, false);
								}
								j++;
								j77++;
							}
						}

						// Clan chat
						if (chatType == 11 && (clanChatMode == 0)) {
							if (chatTypeView == 11 || chatTypeView == 0) {
								if (yPos > 0 && yPos < 210) {
									try {

										String lastChatNameEnd = "@dre@";
										String clanChatMessage = chatMessages[k];
									//	System.out.println("hereeeee msg: "+clanChatMessage);
										String chatMessage = clanChatMessage.substring(clanChatMessage.indexOf(lastChatNameEnd) + lastChatNameEnd.length());
										String usernameAndFormatting = clanChatMessage.substring(0, clanChatMessage.indexOf(lastChatNameEnd) + lastChatNameEnd.length());
										//System.out.println(" msg: "+chatMessage+" us: "+usernameAndFormatting);
										newRegularFont.drawBasicString(usernameAndFormatting, 8, yPos - 0 + yOffset, 0x7e3200, -1, true);
										newRegularFont.drawBasicString(chatMessage, newRegularFont.getTextWidth(usernameAndFormatting) + 8, yPos - 0 + yOffset, 0x7e3200, -1, false);
									} catch (Exception e) {
										e.printStackTrace();
									}
								}
								j++;
								j77++;
							}
						}
						if (chatType == 13 && chatTypeView == 12) {
							if (yPos > 0 && yPos < 210)
								newRegularFont.drawBasicString(s1 + " " + chatMessages[k], 8, yPos - 1 + yOffset,0x7e3200, -1, false);
							j++;
							j77++;
						}
						if (chatType == 8 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s1))) {
							if (chatTypeView == 3 || chatTypeView == 0) {
								if (yPos > 0 && yPos < 210)
									newRegularFont.drawBasicString(s1 + " " + chatMessages[k], 10, yPos - 1 + yOffset,0x7e3200, -1, false);
								j++;
								j77++;
							}
						}
						if (chatType == 16) {
							int j2 = 40 + 11;
							String clanname = clanList[k];
							int clanNameWidth = textDrawingArea.getTextWidth(clanname);
							if (chatTypeView == 11 || chatTypeView == 0) {
							//	System.out.println("here?");
								newRegularFont.drawBasicString("[", 19, yPos - 1 + yOffset, 0, -1);
								newRegularFont.drawBasicString("]", clanNameWidth + 16 + 10, yPos - 1 + yOffset, 0, -1);
								newRegularFont.drawBasicString("" + capitalize(clanname) + "", 25, yPos - 1 + yOffset,
										255, -1);
								newRegularFont.drawBasicString(chatNames[k] + ":", j2 - 17, yPos);
								j2 += newRegularFont.getTextWidth(chatNames[k]) + 7;
								newRegularFont.drawBasicString(capitalize(chatMessages[k]), j2 - 16, yPos - 1 + yOffset,0x800000, -1);

								j++;
								j77++;
							}
						}
					}
				rasterProvider.setRaster();
				chatAreaScrollLength = j * 14 + 7 + 5;
				if (chatAreaScrollLength < 111)
					chatAreaScrollLength = 111;
				drawScrollbar(114, chatAreaScrollLength - anInt1089 - 113, 6 + yOffset, 496, chatAreaScrollLength);
				String fixedString;
				if (localPlayer != null && localPlayer.displayName != null) {
					if (localPlayer.title.length() > 0) {
						fixedString = "<col=" + localPlayer.titleColor + ">" + localPlayer.title + "</col>" + " "
								+ StringUtils.fixName(capitalize(localPlayer.displayName));
					} else {
						fixedString =  StringUtils.fixName(capitalize(myUsername));
					}
				} else {
					fixedString = StringUtils.fixName(capitalize(myUsername));
				}
//				String s;
//				if (localPlayer != null && localPlayer.displayName != null && localPlayer.title.length() > 1)
//					s = localPlayer.title + " " + localPlayer.displayName;
//				else
//					s = StringUtils.fixName(capitalize(myUsername));

				int xOffset = 0;
			//	System.out.println("f: "+fixedString);
				if (localPlayer.hasRightsOtherThan(PlayerRights.PLAYER)) {
					for (PlayerRights right : localPlayer.getDisplayedRights()) {

						if (right.hasCrown()) {
						//	System.out.println("right:  "+right.spriteId());
							modIcons[right.spriteId()].drawSprite(9 + xOffset, 134 + yOffset - modIcons[right.spriteId()].myHeight);
							xOffset += modIcons[right.spriteId()].myWidth;
							xOffset += 2;
						}
					}
			//	System.out.println("right:?111");
					newRegularFont.drawBasicString(fixedString, 10 + xOffset, 133 + yOffset, 0, -1);
				} else {
				//	System.out.println("right:?");
				//	System.out.println("f: "+fixedString);
					newRegularFont.drawBasicString(fixedString, 10 + xOffset, 133 + yOffset, 0, -1);//this moves the ENTIRE everything. not in clan chat.
				}

				xOffset += newRegularFont.getTextWidth(fixedString) + 10;//here to fix the :?
				newRegularFont.drawBasicString(": ", xOffset, 133 + yOffset, 0, -1);
				xOffset += textDrawingArea.getTextWidth(": ");

				if (!isFieldInFocus()) {
					newRegularFont.drawBasicString(inputString + "*",xOffset, 133 + yOffset, 255, -1, false);
				}

				Rasterizer2D.method339(120 + yOffset, 0x807660, 506, 7);
			}

		}
		if (menuOpen) {
			drawMenu(0,0);
		}
		rasterProvider.setRaster();

		Rasterizer3D.scanOffsets = anIntArray1182;
	}

	private String clanUsername;
	private String clanMessage;
	private String clanTitle;
	private String[] clanTitles;
	private EnumSet channelRights;

	public Socket openSocket(int port) throws IOException {
		return new Socket(InetAddress.getByName(server), port);
	}

	public boolean processMenuClick() {
		if (activeInterfaceType != 0)
			return false;
		int j = clickMode3;
		if (spellSelected == 1 && MouseHandler.saveClickX >= 516 && MouseHandler.saveClickY >= 160 && MouseHandler.saveClickX <= 765
				&& MouseHandler.saveClickY <= 205)
			j = 0;
		if (menuOpen) {
			if (j != 1) {
				int k = MouseHandler.mouseX;
				int j1 = MouseHandler.mouseY;
				if (menuScreenArea == 0) {
					k -= 0;
					j1 -= 0;
				}
				if (menuScreenArea == 1) {
					k -= 516;
					j1 -= 160;
				}
				if (menuScreenArea == 2) {
					k -= 17;
					j1 -= 343;
				}
				if (menuScreenArea == 3) {
					k -= 516;
					j1 -= 0;
				}
				if (k < menuOffsetX - 10 || k > menuOffsetX + menuWidth + 10 || j1 < menuOffsetY - 10
						|| j1 > menuOffsetY + menuHeight + 10) {
					menuOpen = false;
					if (menuScreenArea == 1)
						needDrawTabArea = true;
					if (menuScreenArea == 2)
						inputTaken = true;
				}
			}
			if (j == 1) {
				int l = menuOffsetX;
				int k1 = menuOffsetY;
				int i2 = menuWidth;
				int k2 = MouseHandler.saveClickX;
				int l2 = MouseHandler.saveClickY;
				if (menuScreenArea == 0) {
					k2 -= 0;
					l2 -= 0;
				}
				if (menuScreenArea == 1) {
					k2 -= 516;
					l2 -= 160;
				}
				if (menuScreenArea == 2) {
					k2 -= 17;
					l2 -= 343;
				}
				if (menuScreenArea == 3) {
					k2 -= 516;
					l2 -= 0;
				}
				int i3 = -1;
				for (int j3 = 0; j3 < menuActionRow; j3++) {
					int k3 = k1 + 31 + (menuActionRow - 1 - j3) * 15;
					if (k2 > l && k2 < l + i2 && l2 > k3 - 13 && l2 < k3 + 3)
						i3 = j3;
				}
				if (i3 != -1)
					doAction(i3);
				menuOpen = false;
				if (menuScreenArea == 1)
					needDrawTabArea = true;
				if (menuScreenArea == 2) {
					inputTaken = true;
				}
			}
			return true;
		} else {
			if (j == 1 && menuActionRow > 0) {
				int i1 = menuActionID[menuActionRow - 1];
				if (i1 == 632 || i1 == 78 || i1 == 867 || i1 == 431 || i1 == 53 || i1 == 74 || i1 == 454 || i1 == 539
						|| i1 == 493 || i1 == 847 || i1 == 447 || i1 == 1125) {
					int l1 = menuActionCmd2[menuActionRow - 1];
					int j2 = menuActionCmd3[menuActionRow - 1];
					RSInterface class9 = RSInterface.interfaceCache[j2];
					if (class9.aBoolean259 || class9.aBoolean235) {
						aBoolean1242 = false;
						anInt989 = 0;
						draggingItemInterfaceId = j2;
						itemDraggingSlot = l1;
						activeInterfaceType = 2;
						anInt1087 = MouseHandler.saveClickX;
						anInt1088 = MouseHandler.saveClickY;
						if (RSInterface.interfaceCache[j2].parentID == openInterfaceID)
							activeInterfaceType = 1;
						if (RSInterface.interfaceCache[j2].parentID == backDialogID)
							activeInterfaceType = 3;
						return true;
					}
				}
			}
			if (j == 1 && (anInt1253 == 1 || menuHasAddFriend(menuActionRow - 1)) && menuActionRow > 2)
				j = 2;
			if (j == 1 && menuActionRow > 0)
				doAction(menuActionRow - 1);
			if (j == 2 && menuActionRow > 0)
				determineMenuSize();
			minimapHovers();
			return false;
		}
	}

	private final void minimapHovers() {
		final boolean fixed = !isResized();
		prayHover = fixed
				? prayHover = MouseHandler.mouseX >= 517 && MouseHandler.mouseX <= 573
				&& MouseHandler.mouseY >= (Configuration.osbuddyGameframe ? 81 : 76)
				&& MouseHandler.mouseY < (Configuration.osbuddyGameframe ? 109 : 107)
				: MouseHandler.mouseX >= canvasWidth - 210 && MouseHandler.mouseX <= canvasWidth - 157
				&& MouseHandler.mouseY >= (Configuration.osbuddyGameframe ? 90 : 55)
				&& MouseHandler.mouseY < (Configuration.osbuddyGameframe ? 119 : 104);
		runHover = fixed
				? runHover = MouseHandler.mouseX >= (Configuration.osbuddyGameframe ? 530 : 522)
				&& MouseHandler.mouseX <= (Configuration.osbuddyGameframe ? 582 : 594)
				&& MouseHandler.mouseY >= (Configuration.osbuddyGameframe ? 114 : 109)
				&& MouseHandler.mouseY < (Configuration.osbuddyGameframe ? 142 : 142)
				: MouseHandler.mouseX >= canvasWidth - (Configuration.osbuddyGameframe ? 196 : 186)
				&& MouseHandler.mouseX <= canvasWidth - (Configuration.osbuddyGameframe ? 142 : 132)
				&& MouseHandler.mouseY >= (Configuration.osbuddyGameframe ? 123 : 132)
				&& MouseHandler.mouseY < (Configuration.osbuddyGameframe ? 150 : 159);
		counterHover = fixed ? MouseHandler.mouseX >= 522 && MouseHandler.mouseX <= 544 && MouseHandler.mouseY >= 20 && MouseHandler.mouseY <= 47
				: MouseHandler.mouseX >= canvasWidth - 205 && MouseHandler.mouseX <= canvasWidth - 184 && MouseHandler.mouseY >= 27
				&& MouseHandler.mouseY <= 44;
		specialHover = fixed ? MouseHandler.mouseX >= 549 && MouseHandler.mouseX <= 602 && MouseHandler.mouseY >= 137 && MouseHandler.mouseY <= 158
				: MouseHandler.mouseX >= canvasWidth - 205 && MouseHandler.mouseX <= canvasWidth - 184 && MouseHandler.mouseY >= 27
				&& MouseHandler.mouseY <= 44;


//		newHover = fixed ? MouseHandler.mouseX >= 737 && MouseHandler.mouseX <= 761 && MouseHandler.mouseY >= 71 && MouseHandler.mouseY <= 95
//				: MouseHandler.mouseX >= canvasWidth - 34 && MouseHandler.mouseX <= canvasWidth - 5 && MouseHandler.mouseY >= 143 && MouseHandler.mouseY <= 172;

//		donatorHover = fixed ? MouseHandler.mouseX >= 730 && MouseHandler.mouseX <= 755 && MouseHandler.mouseY >= 100 && MouseHandler.mouseY <= 126
//				: MouseHandler.mouseX >= canvasWidth - 34 && MouseHandler.mouseX <= canvasWidth - 5 && MouseHandler.mouseY >= 143 && MouseHandler.mouseY <= 172;
		worldHover = fixed ? MouseHandler.mouseX >= 715 && MouseHandler.mouseX <= 740 && MouseHandler.mouseY >= 125 && MouseHandler.mouseY <= 147
				: MouseHandler.mouseX >= canvasWidth - 34 && MouseHandler.mouseX <= canvasWidth - 5 && MouseHandler.mouseY >= 130 && MouseHandler.mouseY <= 152;

		wikiHover = fixed ?MouseHandler.mouseX >= 704 && MouseHandler.mouseX<= 744 && MouseHandler.mouseY >= 150 &&MouseHandler.mouseY <= 161
				: MouseHandler.mouseX >= canvasWidth - 52 && MouseHandler.mouseX <= canvasWidth - 5 &&MouseHandler.mouseY >= 160 && MouseHandler.mouseY<= 173;

	}

	private boolean prayHover, prayClicked;

	public static boolean counterOn;

	private boolean counterHover;

	private boolean worldHover;
	private boolean wikiHover;

	private boolean donatorHover;

	private boolean newHover;

	private boolean showClanOptions;

	public static int totalRead = 0;

	public static String getFileNameWithoutExtension(String fileName) {
		File tmpFile = new File(fileName);
		tmpFile.getName();
		int whereDot = tmpFile.getName().lastIndexOf('.');
		if (0 < whereDot && whereDot <= tmpFile.getName().length() - 2) {
			return tmpFile.getName().substring(0, whereDot);
		}
		return "";
	}

	public String indexLocation(int cacheIndex, int index) {
		return Signlink.getCacheDirectory() + "index" + cacheIndex + "/" + (index != -1 ? index + ".gz" : "");
	}

	public void repackCacheAll() {
		for (int index = 0; index < 10; index++) {
			try {
				repackCacheIndex(index);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		packCustomModels();
		packCustomAnimations();
	}

	public byte[] fileToByteArray(File file) {
		try {
			byte[] fileData = new byte[(int) file.length()];
			FileInputStream fis = new FileInputStream(file);
			fis.read(fileData);
			fis.close();
			return fileData;
		} catch (Exception e) {
			e.printStackTrace();
		}

		return null;
	}

	public void repackCacheIndex(int cacheIndex) {
		if (!new File(indexLocation(cacheIndex, -1)).exists()) {
			System.out.println("No index data found for: " + cacheIndex);
			return;
		}

		System.out.println("Started repacking index " + cacheIndex + " . " + new File(indexLocation(cacheIndex, -1)).getPath());
		int indexLength = new File(indexLocation(cacheIndex, -1)).listFiles().length;
		File[] file = new File(indexLocation(cacheIndex, -1)).listFiles();
		int packed = 0;
		try {
			for (int index = 0; index < indexLength; index++) {
				if (file[index] != null) {
					if (pack(file[index], cacheIndex)) {
						packed++;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error packing index " + cacheIndex + ".");
		}

	}

	public boolean pack(File file, int cacheIndex) {
		int fileIndex = Integer.parseInt(getFileNameWithoutExtension(file.toString()));
		byte[] data = fileToByteArray(file);
		if (data != null && data.length > 0) {
			decompressors[cacheIndex].write(data.length, data, fileIndex);
			System.out.println("Packed " + file.toString() + " to index " + cacheIndex);
			return true;
		} else {
			System.out.println("No file to pack: " + file);
			return false;
		}
	}

	public void packCustomMaps() {
		if (new File(Configuration.CUSTOM_MAP_DIRECTORY).exists()) {
			System.out.println("Repacking custom maps..");
			packToIndex(4, new File(Configuration.CUSTOM_MAP_DIRECTORY));
		}
	}

	public void packCustomModels() {
		if (new File(Configuration.CUSTOM_MODEL_DIRECTORY).exists()) {
			System.out.println("Repacking custom models..");
			packToIndex(1, new File(Configuration.CUSTOM_MODEL_DIRECTORY));
		}
	}

	public void packCustomAnimations() {
		if (new File(Configuration.CUSTOM_ANIMATION_DIRECTORY).exists()) {
			System.out.println("Repacking custom animations..");
			packToIndex(2, new File(Configuration.CUSTOM_ANIMATION_DIRECTORY));
		}
	}

	private void packToIndex(int index, File file) {
		if (file.isDirectory()) {
			File[] list = file.listFiles();
			if (list != null) {
				Arrays.stream(list).forEach(f -> packToIndex(index, f));
			}
		} else {
			pack(file, index);
			System.out.println("Pack " + file.getName() + " to index " + index);
		}
	}

	public void preloadModels() {
		File models = new File(Signlink.getCacheDirectory() + "/Raw/");
		File[] modelList = models.listFiles();
		for (int model = 0; model < modelList.length; model++) {
			String modelID = modelList[model].getName();
			byte[] modelData = ReadFile(Signlink.getCacheDirectory() + "/Raw/" + modelID);
			Model.loadModel(modelData, Integer.parseInt(getFileNameWithoutExtension(modelID)));
			System.out.println("Loaded " + models.length() + " preloaded models.");
		}
	}

	public static final byte[] ReadFile(String s) {
		try {
			byte abyte0[];
			File file = new File(s);
			int i = (int) file.length();
			abyte0 = new byte[i];
			DataInputStream datainputstream = new DataInputStream(new BufferedInputStream(new FileInputStream(s)));
			datainputstream.readFully(abyte0, 0, i);
			datainputstream.close();
			totalRead++;
			return abyte0;
		} catch (Exception e) {
			System.out.println((new StringBuilder()).append("Read Error: ").append(s).toString());
			return null;
		}
	}

	public void addModels() {
		for (int ModelIndex = 0; ModelIndex < 50000; ModelIndex++) {
			byte[] abyte0 = getModel(ModelIndex);
			if (abyte0 != null && abyte0.length > 0) {
				decompressors[1].write(abyte0.length, abyte0, ModelIndex);
			}
		}
	}

	public byte[] getModel(int Index) {
		try {
			File Model = new File(Signlink.getCacheDirectory() + "./pModels/" + Index + ".gz");
			byte[] aByte = new byte[(int) Model.length()];
			FileInputStream fis = new FileInputStream(Model);
			fis.read(aByte);
			System.out.println("" + Index + " aByte = [" + aByte + "]!");
			fis.close();
			return aByte;
		} catch (Exception e) {
			return null;
		}
	}

	public void addMaps() {
		for (int MapIndex = 0; MapIndex < 3536; MapIndex++) {
			byte[] abyte0 = getMaps(MapIndex);
			if (abyte0 != null && abyte0.length > 0) {
				decompressors[4].write(abyte0.length, abyte0, MapIndex);
				System.out.println("Maps Added");
			}
		}
	}

	public byte[] getMaps(int Index) {
		try {
			File Map = new File(Signlink.getCacheDirectory() + "./maps/" + Index + ".gz");
			byte[] aByte = new byte[(int) Map.length()];
			FileInputStream fis = new FileInputStream(Map);
			fis.read(aByte);
			pushMessage("aByte = [" + aByte + "]!", 0, "");
			fis.close();
			return aByte;
		} catch (Exception e) {
			return null;
		}
	}

	public void saveMidi(boolean flag, byte abyte0[]) {
		Signlink.midifade = flag ? 1 : 0;
		Signlink.midisave(abyte0, abyte0.length);
	}
	public MapRegion currentMapRegion;
	public final void loadRegion() {
		try {
			setGameState(GameState.LOADING);
			anInt985 = -1;
			incompleteAnimables.removeAll();
			projectiles.removeAll();

			unlinkMRUNodes();
			scene.initToNull();
			System.gc();
			load_objects_real();
			//load_objects();
			for (int i = 0; i < 4; i++)
				collisionMaps[i].setDefault();

			for (int l = 0; l < 4; l++) {
				for (int k1 = 0; k1 < 104; k1++) {
					for (int j2 = 0; j2 < 104; j2++)
						tileFlags[l][k1][j2] = 0;
				}
			}
			currentMapRegion = new MapRegion(tileFlags, tileHeights);

			int k2 = terrainData.length;

			/*
			 * int k18 = 62; for (int A = 0; A < k2; A++) for (int B = 0; B < 2000; B++) if
			 * (anIntArray1234[A] == positions[B]) { anIntArray1235[A] = landScapes[B];
			 * anIntArray1236[A] = objects[B]; }
			 */

			stream.createFrame(0);

			if (!isDynamicRegion) {
				for (int i3 = 0; i3 < k2; i3++) {
					int i4 = (mapCoordinates[i3] >> 8) * 64 - baseX;
					int k5 = (mapCoordinates[i3] & 0xff) * 64 - baseY;

					byte abyte0[] = terrainData[i3];

					if (abyte0 != null)
						currentMapRegion.method180(abyte0, k5, i4, (currentRegionX - 6) * 8, (currentRegionY - 6) * 8,
								collisionMaps);
				}

				for (int j4 = 0; j4 < k2; j4++) {
					int l5 = (mapCoordinates[j4] >> 8) * 64 - baseX;
					int k7 = (mapCoordinates[j4] & 0xff) * 64 - baseY;
					byte abyte2[] = terrainData[j4];
					if (abyte2 == null && currentRegionY < 800)
						currentMapRegion.initiateVertexHeights(k7, 64, 64, l5);
				}

				anInt1097++;
				if (anInt1097 > 160) {
					anInt1097 = 0;
					stream.createFrame(238);
					stream.writeUnsignedByte(96);

				}
				stream.createFrame(0);

				for (int i6 = 0; i6 < k2; i6++) {
					byte abyte1[] = objectData[i6];
					if (abyte1 != null) {
						int l8 = (mapCoordinates[i6] >> 8) * 64 - baseX;
						int k9 = (mapCoordinates[i6] & 0xff) * 64 - baseY;
						currentMapRegion.method190(l8, collisionMaps, k9, scene, abyte1);
					}
				}

			}
			if (isDynamicRegion) {
				for (int j3 = 0; j3 < 4; j3++) {
					for (int k4 = 0; k4 < 13; k4++) {
						for (int j6 = 0; j6 < 13; j6++) {
							int l7 = constructRegionData[j3][k4][j6];
							if (l7 != -1) {
								int i9 = l7 >> 24 & 3;
								int l9 = l7 >> 1 & 3;
								int j10 = l7 >> 14 & 0x3ff;
								int l10 = l7 >> 3 & 0x7ff;
								int j11 = (j10 / 8 << 8) + l10 / 8;
								for (int l11 = 0; l11 < mapCoordinates.length; l11++) {
									if (mapCoordinates[l11] != j11 || terrainData[l11] == null)
										continue;
									currentMapRegion.loadMapChunk(i9, l9, collisionMaps, k4 * 8, (j10 & 7) * 8,
											terrainData[l11], (l10 & 7) * 8, j3, j6 * 8);
									break;
								}

							}
						}

					}

				}

				for (int l4 = 0; l4 < 13; l4++) {
					for (int k6 = 0; k6 < 13; k6++) {
						int i8 = constructRegionData[0][l4][k6];
						if (i8 == -1)
							currentMapRegion.initiateVertexHeights(k6 * 8, 8, 8, l4 * 8);
					}

				}

				stream.createFrame(0);

				for (int l6 = 0; l6 < 4; l6++) {
					for (int j8 = 0; j8 < 13; j8++) {
						for (int j9 = 0; j9 < 13; j9++) {
							int chunkBits = constructRegionData[l6][j8][j9];
							if (chunkBits != -1) {
								int z = chunkBits >> 24 & 3;
								int rotation = chunkBits >> 1 & 3;
								int xCoord = chunkBits >> 14 & 0x3ff;
								int yCoord = chunkBits >> 3 & 0x7ff;
								int mapRegion = (xCoord / 8 << 8) + yCoord / 8;
								for (int k12 = 0; k12 < mapCoordinates.length; k12++) {
									if (mapCoordinates[k12] != mapRegion || objectData[k12] == null)
										continue;
									currentMapRegion.loadMapChunk(z, rotation, collisionMaps, x * 8, (xCoord & 7) * 8,
											terrainData[k12], (yCoord & 7) * 8, plane, y * 8);

									break;
								}

							}
						}

					}

				}

			}
			stream.createFrame(0);
			currentMapRegion.createRegionScene(collisionMaps, scene);

			stream.createFrame(0);

			int k3 = MapRegion.maximumPlane;
			if (k3 > plane)
				k3 = plane;

			if (k3 < plane - 1)
				k3 = plane - 1;
			if (lowMem)

				scene.method275(MapRegion.maximumPlane);
			else
				scene.method275(0);
			for (int i5 = 0; i5 < 104; i5++) {
				for (int i7 = 0; i7 < 104; i7++) {
					updateGroundItems(i5, i7);
				}
			}

			anInt1051++;
			if (anInt1051 > 98) {
				anInt1051 = 0;
				stream.createFrame(150);

			}
			method63();
		} catch (Exception e) {
			e.printStackTrace();
		}
		ObjectDefinition.baseModels.unlinkAll();
		if (lowMem && Signlink.cache_dat != null) {
			int j = resourceProvider.getVersionCount(0);
			for (int i1 = 0; i1 < j; i1++) {
				int l1 = resourceProvider.getModelIndex(i1);
				if ((l1 & 0x79) == 0)
					Model.resetModel(i1);
			}

		}
		stream.createFrame(210);
		stream.writeDWord(0x3f008edd);
		System.gc();

		resourceProvider.method566();

		int k = (currentRegionX - 6) / 8 - 1;
		int j1 = (currentRegionX + 6) / 8 + 1;
		int i2 = (currentRegionY - 6) / 8 - 1;
		int l2 = (currentRegionY + 6) / 8 + 1;
		if (inTutorialIsland) {
			k = 49;
			j1 = 50;
			i2 = 49;
			l2 = 50;
		}
		for (int l3 = k; l3 <= j1; l3++) {
			for (int j5 = i2; j5 <= l2; j5++)
				if (l3 == k || l3 == j1 || j5 == i2 || j5 == l2) {
					int j7 = resourceProvider.getMapFiles(0, j5, l3);
					if (j7 != -1)
						resourceProvider.method560(j7, 3);
					int k8 = resourceProvider.getMapFiles(1, j5, l3);
					if (k8 != -1)
						resourceProvider.method560(k8, 3);
				}

		}
		setGameState(GameState.LOGGED_IN);
	}

	public void unlinkMRUNodes() {
		ObjectDefinition.baseModels.unlinkAll();
		ObjectDefinition.recent_models.unlinkAll();
		NpcDefinition.mruNodes.unlinkAll();
		ItemDefinition.models.unlinkAll();
		ItemDefinition.sprites.unlinkAll();
		Player.recent_use.unlinkAll();
		GraphicsDefinition.recent_models.unlinkAll();
	}

	public void renderMapScene(int i) {
		int ai[] = minimapImage.myPixels;
		int j = ai.length;

		for (int k = 0; k < j; k++)
			ai[k] = 0;

		for (int l = 1; l < 103; l++) {
			int i1 = 24628 + (103 - l) * 512 * 4;
			for (int k1 = 1; k1 < 103; k1++) {
				if ((tileFlags[i][k1][l] & 0x18) == 0)
					scene.drawTileMinimap(ai, i1, i, k1, l);
				if (i < 3 && (tileFlags[i + 1][k1][l] & 8) != 0)
					scene.drawTileMinimap(ai, i1, i + 1, k1, l);
				i1 += 4;
			}

		}

		int j1 = ((238 + (int) (Math.random() * 20D)) - 10 << 16) + ((238 + (int) (Math.random() * 20D)) - 10 << 8)
				+ ((238 + (int) (Math.random() * 20D)) - 10);
		int l1 = (238 + (int) (Math.random() * 20D)) - 10 << 16;
		minimapImage.init();
		for (int i2 = 1; i2 < 103; i2++) {
			for (int j2 = 1; j2 < 103; j2++) {
				if ((tileFlags[i][j2][i2] & 0x18) == 0)
					drawMapScenes(i2, j1, j2, l1, i);
				if (i < 3 && (tileFlags[i + 1][j2][i2] & 8) != 0)
					drawMapScenes(i2, j1, j2, l1, i + 1);
			}

		}

		rasterProvider.setRaster();
		mapIconAmount = 0;
		for (int k2 = 0; k2 < 104; k2++) {
			for (int l2 = 0; l2 < 104; l2++) {
				long i3 = scene.getGroundDecorationUid(plane, k2, l2);
				if (i3 != 0) {
					int objId = ObjectKeyUtil.getObjectId(i3);

					int j3 = ObjectDefinition.lookup(objId).minimapFunction;
					//int j3real = ObjectDefinition.lookup(objId).minimapFunctionreal;
//System.out.println("j3: "+j3);
					if (j3 >= 0) {
						int sprite = AreaDefinition.lookup(j3).spriteId;
				//		System.out.println("obj: "+objId+" j3: "+j3+"  and sprite: "+sprite);
						if (sprite != -1) {
							int k3 = k2;
							int l3 = l2;

							mapIconSprite[mapIconAmount] = mapFunctions[sprite > 124 ? 1 : sprite];
							//mapIconSprite[mapIconAmount] = mapFunctions[sprite > 124 ? 1 : sprite];
								anIntArray1072[mapIconAmount] = k3;
								anIntArray1073[mapIconAmount] = l3;
								mapIconAmount++;

						}
					}
				}
			}

		}

	}

	private void updateGroundItems(int i, int j) {
		Deque class19 = groundItems[plane][i][j];
		if (class19 == null) {
			scene.removeGroundItemTile(plane, i, j);
			return;
		}
		int k = 0xfa0a1f01;
		Object obj = null;
		for (Item item = (Item) class19.reverseGetFirst(); item != null; item =
				(Item) class19.reverseGetNext()) {
			ItemDefinition itemDef = ItemDefinition.lookup(item.ID);
			int l = itemDef.cost;
			if (itemDef.stackable)
				l *= item.itemCount + 1;
			// notifyItemSpawn(item, i + baseX, j + baseY);

			if (l > k) {
				k = l;
				obj = item;
			}
		}

		class19.insertTail(((Linkable) (obj)));
		Object obj1 = null;
		Object obj2 = null;
		for (Item class30_sub2_sub4_sub2_1 = (Item) class19
				.reverseGetFirst(); class30_sub2_sub4_sub2_1 != null; class30_sub2_sub4_sub2_1 =
					 (Item) class19.reverseGetNext()) {
			if (class30_sub2_sub4_sub2_1.ID != ((Item) (obj)).ID && obj1 == null)
				obj1 = class30_sub2_sub4_sub2_1;
			if (class30_sub2_sub4_sub2_1.ID != ((Item) (obj)).ID
					&& class30_sub2_sub4_sub2_1.ID != ((Item) (obj1)).ID && obj2 == null)
				obj2 = class30_sub2_sub4_sub2_1;
		}

		int i1 = i + (j << 7) + 0x60000000;
		scene.addGroundItemTile(obj, i, i1, ((Renderable) (obj1)),
				getCenterHeight(plane, j * 128 + 64, i * 128 + 64), ((Renderable) (obj2)),
				((Renderable) (obj)), plane, j);
	}

	public void method26(boolean flag) {
		for (int j = 0; j < npcCount; j++) {
			Npc npc = npcs[highres_npc_list[j]];
			long k = 0x20000000L | (long) highres_npc_list[j] << 32;
			if (npc == null || !npc.isVisible() || npc.desc.visible != flag)
				continue;
			int l = npc.x >> 7;
			int i1 = npc.z >> 7;
			if (l < 0 || l >= 104 || i1 < 0 || i1 >= 104)
				continue;
			if (npc.size == 1 && (npc.x & 0x7f) == 64 && (npc.z & 0x7f) == 64) {
				if (anIntArrayArray929[l][i1] == anInt1265)
					continue;
				anIntArrayArray929[l][i1] = anInt1265;
			}
			if (!npc.desc.active)
				k |= ~0x7fffffffffffffffL;
			scene.addAnimableA(plane, npc.target_direction, getCenterHeight(plane, npc.z, npc.x), k, npc.z,
					(npc.size - 1) * 64 + 60, npc.x, npc, npc.is_walking);
		}
	}

	public void loadError() {
		String s = "ondemand";// was a constant parameter
		System.out.println(s);
		try {
			getAppletContext().showDocument(new URL(getCodeBase(), "loaderror_" + s + ".html"));
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		do
			try {
				Thread.sleep(1000L);
			} catch (Exception _ex) {
			}
		while (true);
	}

	public int drawTabInterfaceHoverParent;
	public int drawTabInterfaceHover;
	public int drawTabInterfaceHoverLast;
	public long drawTabInterfaceHoverTimer;

	public RSInterface lastHovered;

	public void resetTabInterfaceHover() {
		drawTabInterfaceHover = 0;
		if (lastHovered != null) {
			lastHovered.isHovered = false;
			lastHovered.hoverRunnable.run();
			lastHovered = null;
		}
	}

	// interfaceClick
	private void buildInterfaceMenu(int xPosition, RSInterface class9, int mouseX, int yPosition, int mouseY, int j1) {
		if (class9 == null) {
			return;
		}
		if (class9.type != 0 || class9.children == null || class9.isMouseoverTriggered)
			return;
		if (mouseX < xPosition || mouseY < yPosition || mouseX > xPosition + class9.width || mouseY > yPosition + class9.height)
			return;
		int childCount = class9.children.length;
		for (int childIndex = 0; childIndex < childCount; childIndex++) {
			try {
				int drawX = class9.childX[childIndex] + xPosition;
				int drawY = (class9.childY[childIndex] + yPosition) - j1;
				RSInterface class9_1 = RSInterface.interfaceCache[class9.children[childIndex]];
				if (class9_1 == null) {
					break;
				}
				drawX += class9_1.anInt263;
				drawY += class9_1.anInt265;

				if (mouseX >= drawX && mouseY >= drawY && mouseX <= drawX + class9_1.width && mouseY <= drawY + class9_1.height) {
					if (class9_1.hoverInterfaceId != 0) {
						drawTabInterfaceHoverParent = class9_1.id;
						drawTabInterfaceHover = class9_1.hoverInterfaceId;
					}

					if (class9_1.hoverRunnable != null && lastHovered == null) {
						class9_1.isHovered = true;
						class9_1.hoverRunnable.run();
						lastHovered = class9_1;
					}
				}

				if ((class9_1.mOverInterToTrigger >= 0 || class9_1.anInt216 != 0) && mouseX >= drawX && mouseY >= drawY
					&& mouseX < drawX + class9_1.width && mouseY < drawY + class9_1.height) {
					if (class9_1.mOverInterToTrigger >= 0) {
						anInt886 = class9_1.mOverInterToTrigger;
					} else {
						anInt886 = class9_1.id;
					}
				}
				if (class9_1.atActionType == RSInterface.AT_ACTION_TYPE_OPTION_DROPDOWN) {

					boolean flag = false;
					class9_1.hovered = false;
					class9_1.dropdownHover = -1;

					if (class9_1.dropdown.isOpen()) {

						// Inverted keybinds dropdown
						if (class9_1.type == RSInterface.TYPE_KEYBINDS_DROPDOWN && class9_1.inverted && mouseX >= drawX
							&& mouseX < drawX + (class9_1.dropdown.getWidth() - 16)
							&& mouseY >= drawY - class9_1.dropdown.getHeight() - 10 && mouseY < drawY) {
							resetTabInterfaceHover();
							int yy = mouseY - (drawY - class9_1.dropdown.getHeight());

							if (mouseX > drawX + (class9_1.dropdown.getWidth() / 2)) {
								class9_1.dropdownHover = ((yy / 15) * 2) + 1;
							} else {
								class9_1.dropdownHover = (yy / 15) * 2;
							}
							flag = true;
						} else if (!class9_1.inverted && mouseX >= drawX && mouseX < drawX + (class9_1.dropdown.getWidth() - 16)
							&& mouseY >= drawY + 19 && mouseY < drawY + 19 + class9_1.dropdown.getHeight()) {
							resetTabInterfaceHover();
							int yy = mouseY - (drawY + 19);

							if (class9_1.type == RSInterface.TYPE_KEYBINDS_DROPDOWN && class9_1.dropdown.doesSplit()) {
								if (mouseX > drawX + (class9_1.dropdown.getWidth() / 2)) {
									class9_1.dropdownHover = ((yy / 15) * 2) + 1;
								} else {
									class9_1.dropdownHover = (yy / 15) * 2;
								}
							} else {
								class9_1.dropdownHover = yy / 14; // Regular dropdown hover
							}
							flag = true;
						}
						if (flag) {
							if (menuActionRow != 1) {
								menuActionRow = 1;
							}
							menuActionName[menuActionRow] = "Select";
							menuActionID[menuActionRow] = 770;
							menuActionCmd2[menuActionRow] = class9_1.id;
							menuActionCmd1[menuActionRow] = class9_1.dropdownHover;
							menuActionCmd3[menuActionRow] = class9.id;
							menuActionRow++;
						}
					}
					if (mouseX >= drawX && mouseY >= drawY && mouseX < drawX + class9_1.dropdown.getWidth() && mouseY < drawY + 24
						&& menuActionRow == 1) {
						class9_1.hovered = true;
						menuActionName[menuActionRow] = class9_1.dropdown.isOpen() ? "Hide" : "Show";
						menuActionID[menuActionRow] = 769;
						menuActionCmd2[menuActionRow] = class9_1.id;
						menuActionCmd3[menuActionRow] = class9.id;
						menuActionRow++;
					}
				}

				if (mouseX >= drawX && mouseY >= drawY && mouseX < drawX + (class9_1.type == 4 ? 100 : class9_1.width)
					&& mouseY < drawY + class9_1.height) {
					if (class9_1.actions != null && !class9_1.invisible && !class9_1.drawingDisabled) {

						if (!(class9_1.contentType == 206 && cs1_selected(class9_1))) {
//							if (/*(class9_1.type == 4 && class9_1.message.length() > 0) || */class9_1.type == 5) {
							if ((class9_1.type == 4 && class9_1.message.length() > 0) || class9_1.type == 5) {

								boolean drawOptions = true;

								// HARDCODE CLICKABLE TEXT HERE
								if (class9_1.parentID == 37128) { // Clan chat interface, dont show options for guests
									drawOptions = showClanOptions;
								}

								if (drawOptions) {
									for (int action = class9_1.actions.length - 1; action >= 0; action--) {
										if (class9_1.actions[action] != null) {
											String s = class9_1.actions[action]
												+ (class9_1.type == 4 ? " @or1@" + class9_1.message : "");

											if (s.contains("img")) {
//												int prefix = s.indexOf("<img=");
//												int suffix = s.indexOf(">");
//												s = s.replaceAll(s.substring(prefix + 5, suffix), "");
//												s = s.replaceAll("</img>", "");
//												s = s.replaceAll("<img=>", "");
											}
											if (s.contains("ico")) {
												int prefix = s.indexOf("<ico=");
												int suffix = s.indexOf(">");
												s = s.replaceAll(s.substring(prefix + 5, suffix), "");
												s = s.replaceAll("</ico>", "");
												s = s.replaceAll("<ico=>", "");
											}
//											menuActionName[menuActionRow] = s;
//											menuActionID[menuActionRow] = 647;
//											menuActionCmd1[menuActionRow] = action;
//											menuActionCmd2[menuActionRow] = class9_1.id;
//											menuActionRow++;
										}
									}
								}
							}
						}
					}
				}
				if (class9_1.type == 22) {
					RSInterface class9_2;
					int slot = class9_1.grandExchangeSlot;
					if (grandExchangeInformation[slot][0] == -1)
						class9_2 = RSInterface.interfaceCache[GrandExchange.grandExchangeBuyAndSellBoxIds[slot]];
					else
						class9_2 = RSInterface.interfaceCache[GrandExchange.grandExchangeItemBoxIds[slot]];
					buildInterfaceMenu(drawX, class9_2, mouseX, drawY, mouseY, j1);
				}
				if (class9_1.type == 9 && mouseX >= drawX && mouseY >= drawY && mouseX < drawX + class9_1.width && mouseY < drawY + class9_1.height) {
					anInt1315 = class9_1.id;
				}
				if (class9_1.type == 5 && mouseX >= drawX && mouseY >= drawY && mouseX < drawX + class9_1.width && mouseY < drawY + class9_1.height) {
					hoverId = class9_1.id;
				}

				if (class9_1.type == 0) {
					buildInterfaceMenu(drawX, class9_1, mouseX, drawY, mouseY, class9_1.scrollPosition);
					if (class9_1.scrollMax > class9_1.height) {
						if (scrollbarVisible(class9_1)) {
							method65(drawX + class9_1.width, class9_1.height, mouseX, mouseY, class9_1, drawY, true, class9_1.scrollMax);
						}
					}
				} else {
					if (class9_1.atActionType == RSInterface.OPTION_OK && mouseX >= drawX && mouseY >= drawY && mouseX < drawX + class9_1.width
						&& mouseY < drawY + class9_1.height) {
						boolean flag = false;
						if (class9_1.contentType != 0)
							flag = buildFriendsListMenu(class9_1);
						if (!flag) {
							menuActionName[menuActionRow] = class9_1.tooltip;
							menuActionID[menuActionRow] = 315;
							menuActionCmd3[menuActionRow] = class9_1.id;
							menuActionRow++;
						}
						if (class9_1.type == RSInterface.TYPE_HOVER || class9_1.type == RSInterface.TYPE_CONFIG_HOVER
							|| class9_1.type == RSInterface.TYPE_ADJUSTABLE_CONFIG
							|| class9_1.type == RSInterface.TYPE_BOX) {
							class9_1.toggled = true;
						}

					}
					if (class9_1.atActionType == 2 && spellSelected == 0 && mouseX >= drawX && mouseY >= drawY && mouseX < drawX + class9_1.width
						&& mouseY < drawY + class9_1.height) {
						String s = class9_1.selectedActionName;
						if (s.indexOf(" ") != -1)
							s = s.substring(0, s.indexOf(" "));
                        if(class9_1.spellName != null)
							if(class9_1.spellName.contains("surge"))
								class9_1.sprite1.outline(1);
						menuActionName[menuActionRow] = s + " @gre@" + class9_1.spellName;
                      //  outline(1);
						menuActionID[menuActionRow] = 626;
						menuActionCmd3[menuActionRow] = class9_1.id;
						menuActionRow++;
					}
					// close interface button
					if (class9_1.atActionType == 3 && mouseX >= drawX && mouseY >= drawY && mouseX < drawX + class9_1.width
						&& mouseY < drawY + class9_1.height) {
						menuActionName[menuActionRow] = "Close";
						menuActionID[menuActionRow] = 200;
						menuActionCmd3[menuActionRow] = class9_1.id;
						menuActionRow++;
					}
					if (class9_1.atActionType == 4 && mouseX >= drawX && mouseY >= drawY && mouseX < drawX + class9_1.width
						&& mouseY < drawY + class9_1.height) {
						menuActionName[menuActionRow] = class9_1.tooltip;
						menuActionID[menuActionRow] = 169;
						menuActionCmd3[menuActionRow] = class9_1.id;
						menuActionRow++;
					}
					if (class9_1.atActionType == 5 && mouseX >= drawX && mouseY >= drawY && mouseX < drawX + class9_1.width
						&& mouseY < drawY + class9_1.height) {
						menuActionName[menuActionRow] = class9_1.tooltip;
						menuActionID[menuActionRow] = 646;
						menuActionCmd3[menuActionRow] = class9_1.id;
						menuActionRow++;
					}
					if (class9_1.atActionType == 6 && !aBoolean1149 && mouseX >= drawX && mouseY >= drawY && mouseX < drawX + class9_1.width
						&& mouseY < drawY + class9_1.height) {
						menuActionName[menuActionRow] = class9_1.tooltip;
						menuActionID[menuActionRow] = 679;
						menuActionCmd3[menuActionRow] = class9_1.id;
						menuActionRow++;
					}

//					if (spellSelected == 0 && class9_1.atActionType == RSInterface.AT_ACTION_TYPE_AUTOCAST && mouseX >= drawX && mouseY >= drawY && mouseX < drawX + class9_1.width
//						&& mouseY < drawY + class9_1.height) {
//						menuActionName[menuActionRow] = class9_1.tooltip;
//						menuActionID[menuActionRow] = Autocast.AUTOCAST_MENU_ACTION_ID;
//						menuActionCmd3[menuActionRow] = class9_1.id;
//						menuActionRow++;
//					}

					if (class9_1.type == 8 && mouseX >= drawX && mouseY >= drawY && mouseX < drawX + class9_1.width && mouseY < drawY + class9_1.height) {
						anInt1315 = class9_1.id;
						//System.out.println("here drawing: "+class9_1.id+" drawX: "+drawX+"  drawY: "+drawY+" ");
					}
					if (class9_1.atActionType == 8 && !aBoolean1149 && mouseX >= drawX && mouseY >= drawY && mouseX < drawX + class9_1.width
						&& mouseY < drawY + class9_1.height) {
						for (int s1 = 0; s1 < class9_1.tooltips.length; s1++) {
								/*if (!RSInterface.interfaceCache[32007].isMouseoverTriggered) {
									if (class9_1.id > 32016) {
										continue;
									}
								}*/
							menuActionName[menuActionRow] = class9_1.tooltips[s1];
							menuActionID[menuActionRow] = 1700 + s1;
							menuActionCmd3[menuActionRow] = class9_1.id;
							menuActionRow++;
						}
					}
					if (class9_1.atActionType == 9 && !aBoolean1149 && mouseX >= drawX && mouseY >= drawY && mouseX < drawX + class9_1.width
						&& mouseY < drawY + class9_1.height) {
						menuActionName[menuActionRow] = class9_1.tooltip;
						menuActionID[menuActionRow] = 1100;
						menuActionCmd3[menuActionRow] = class9_1.id;
						menuActionRow++;
					}
					if (class9_1.atActionType == 10 && !aBoolean1149 && mouseX >= drawX && mouseY >= drawY && mouseX < drawX + class9_1.width
						&& mouseY < drawY + class9_1.height) {
						menuActionName[menuActionRow] = class9_1.getMenuItem().getText();
						menuActionID[menuActionRow] = 1200;
						menuActionCmd3[menuActionRow] = class9_1.id;
						menuActionRow++;
					}
					if (class9_1.atActionType == 11 && mouseX >= drawX && mouseY >= drawY && mouseX < drawX + class9_1.width
						&& mouseY < drawY + class9_1.height) {
						menuActionName[menuActionRow] = class9_1.tooltip;
						menuActionID[menuActionRow] = 201;
						menuActionCmd3[menuActionRow] = class9_1.id;
						menuActionRow++;
					}
					if (mouseX >= drawX && mouseY >= drawY && mouseX < drawX + (class9_1.type == 4 ? 100 : class9_1.width)
						&& mouseY < drawY + class9_1.height) {

						if (class9_1.actions != null) {
							if ((class9_1.type == 4 && class9_1.message.length() > 0) || class9_1.type == 5) {
								for (int action = class9_1.actions.length - 1; action >= 0; action--) {
									if (class9_1.actions[action] != null) {
										menuActionName[menuActionRow] = class9_1.actions[action]
												+ (class9_1.type == 4 ? " " + class9_1.message : "");
										menuActionID[menuActionRow] = 647;
										menuActionCmd2[menuActionRow] = action;
										menuActionCmd3[menuActionRow] = class9_1.id;
										menuActionRow++;
									}
								}
							}
						}
					}
					if (class9_1.type == 2) {
						int k2 = 0;
						for (int l2 = 0; l2 < class9_1.height; l2++) {
							for (int i3 = 0; i3 < class9_1.width; i3++) {
								boolean smallSprite = openInterfaceID == 26000
									&& GrandExchange.isSmallItemSprite(class9_1.id) || class9_1.smallInvSprites;
								int size = smallSprite ? 18 : 32;
								int j3 = drawX + i3 * (size + class9_1.invSpritePadX);
								int k3 = drawY + l2 * (size + class9_1.invSpritePadY);
								if (k2 < 20) {
									j3 += class9_1.spritesX[k2];
									k3 += class9_1.spritesY[k2];
								}
								if (mouseX >= j3 && mouseY >= k3 && mouseX < j3 + size && mouseY < k3 + size) {
									mouseInvInterfaceIndex = k2;
									lastActiveInvInterface = class9_1.id;
									int itemID = class9_1.inventoryItemId[k2] - 1;
									if (class9_1.id == 23231) {
										itemID = (class9_1.inventoryItemId[k2] & 0x7FFF) - 1;
									}
									if (class9_1.inventoryItemId[k2] > 0) {
										ItemDefinition itemDef = ItemDefinition.lookup(itemID);
										boolean hasDestroyOption = false;
										if (itemSelected == 1 && class9_1.isInventoryInterface) {
											if (class9_1.id != anInt1284 || k2 != anInt1283) {
												menuActionName[menuActionRow] = "Use " + selectedItemName + " with @lre@"
													+ itemDef.name;
												menuActionID[menuActionRow] = 870;
												menuActionCmd1[menuActionRow] = itemDef.id;
												menuActionCmd2[menuActionRow] = k2;
												menuActionCmd3[menuActionRow] = class9_1.id;
												menuActionRow++;
											}
										} else if (spellSelected == 1 && class9_1.isInventoryInterface) {
											if ((spellUsableOn & 0x10) == 16) {
												menuActionName[menuActionRow] = spellTooltip + " @lre@" + itemDef.name;
												menuActionID[menuActionRow] = 543;
												menuActionCmd1[menuActionRow] = itemDef.id;
												menuActionCmd2[menuActionRow] = k2;
												menuActionCmd3[menuActionRow] = class9_1.id;
												menuActionRow++;
											}
										} else {
											if (class9_1.isInventoryInterface) {

												for (int l3 = 4; l3 >= 3; l3--)
													if (itemDef.interfaceOptions != null
														&& itemDef.interfaceOptions[l3] != null) {
														menuActionName[menuActionRow] = itemDef.interfaceOptions[l3]
															+ " @lre@" + itemDef.name;
														if (itemDef.interfaceOptions[l3].contains("Wield") || itemDef.interfaceOptions[l3].contains("Wear")) {
															hintMenu = true;
															hintName = itemDef.name;
															hintId = itemDef.id;
														} else {
															hintMenu = false;
														}
														if (l3 == 3)
															menuActionID[menuActionRow] = 493;
														if (l3 == 4)
															menuActionID[menuActionRow] = 847;
														hasDestroyOption = itemDef.interfaceOptions[l3].contains("Destroy");
														menuActionCmd1[menuActionRow] = itemDef.id;
														menuActionCmd2[menuActionRow] = k2;
														menuActionCmd3[menuActionRow] = class9_1.id;
														menuActionRow++;
													} else if (l3 == 4) {
														menuActionName[menuActionRow] = "Drop @lre@" + itemDef.name;
														menuActionID[menuActionRow] = 847;
														menuActionCmd1[menuActionRow] = itemDef.id;
														menuActionCmd2[menuActionRow] = k2;
														menuActionCmd3[menuActionRow] = class9_1.id;
														menuActionRow++;
													}

											}
											if (class9_1.usableItemInterface) {
												hintMenu = false;
												menuActionName[menuActionRow] = "Use @lre@" + itemDef.name;
												menuActionID[menuActionRow] = 447;
												menuActionCmd1[menuActionRow] = itemDef.id;
												// k2 = inventory spot
												// System.out.println(k2);
												menuActionCmd2[menuActionRow] = k2;
												menuActionCmd3[menuActionRow] = class9_1.id;
												menuActionRow++;
												if (!hasDestroyOption && !menuOpen && informationFile.rememberShiftDrop && shiftDown) {
													menuActionsRow("Drop @lre@" + itemDef.name, 1, 847, 2);
													removeShiftDropOnMenuOpen = true;
												}
											}
											if (class9_1.isInventoryInterface && itemDef.interfaceOptions != null) {
												for (int i4 = 2; i4 >= 0; i4--)
													if (itemDef.interfaceOptions[i4] != null) {

														if (itemDef.interfaceOptions[i4].contains("Wield") || itemDef.interfaceOptions[i4].contains("Wear")) {
															hintMenu = true;
															hintName = "@whi@Wear @lre@"+itemDef.name;
															hintId = itemDef.id;
														} else {
															hintMenu = false;
														}

														menuActionName[menuActionRow] = itemDef.interfaceOptions[i4]
															+ " @lre@" + itemDef.name;
														if (i4 == 0)
															menuActionID[menuActionRow] = 74;
														if (i4 == 1)
															menuActionID[menuActionRow] = 454;
														if (i4 == 2)
															menuActionID[menuActionRow] = 539;
														menuActionCmd1[menuActionRow] = itemDef.id;
														menuActionCmd2[menuActionRow] = k2;
														menuActionCmd3[menuActionRow] = class9_1.id;
														menuActionRow++;
														if (!hasDestroyOption && !menuOpen && informationFile.rememberShiftDrop && shiftDown) {
															menuActionsRow("Drop @lre@" + itemDef.name, 1, 847, 2);
															removeShiftDropOnMenuOpen = true;
														}
													}

											}

											if (class9_1.actions != null) {
												if (openInterfaceID == 21553 && class9_1.parentID == 3213) {
													// Equipment
													String[] options = new String[]{"Place in inventory", "Use as equipment"};
													for (int j4 = 8; j4 >= 0; j4--) {
														if (j4 > options.length - 1)
															continue;
														if (options[j4] != null) {
															menuActionName[menuActionRow] = options[j4] + " @lre@"
																+ itemDef.name;
															if (j4 == 0)
																menuActionID[menuActionRow] = 632;
															if (j4 == 1)
																menuActionID[menuActionRow] = 78;
															if (j4 == 2)
																menuActionID[menuActionRow] = 867;
															if (j4 == 3)
																menuActionID[menuActionRow] = 431;
															if (j4 == 4)
																menuActionID[menuActionRow] = 53;
															if (j4 == 7)
																menuActionID[menuActionRow] = 1337;
															menuActionCmd1[menuActionRow] = itemDef.id;
															menuActionCmd2[menuActionRow] = k2;
															menuActionCmd3[menuActionRow] = class9_1.id;
															menuActionRow++;
														}
													}

												} else {
													// Standard containers
													if (Bank.isBankContainer(class9_1)) {
														if (class9_1.id == 5382 && placeHolders && class9_1.inventoryAmounts[k2] <= 0) {
															class9_1.actions = new String[8];
															class9_1.actions[1] = "Release";
														} else {
															if (modifiableXValue > 0) {// so issue is when x = 0
																if (class9_1.actions.length < 9) {
																	class9_1.actions = new String[]{
																		"Withdraw-1",
																		"Withdraw-5",
																		"Withdraw-10",
																		"Withdraw-All",
																		"Withdraw-X",
																		"Withdraw-" + modifiableXValue,
																		"Withdraw-All-but-1"
																	};
																} else {
																	class9_1.actions[5] = "Withdraw-" + modifiableXValue;
																}
															}
														}
													}

													for (int j4 = 8; j4 >= 0; j4--) {
														if (j4 > class9_1.actions.length - 1)
															continue;
														if (class9_1.actions[j4] != null) {
															menuActionName[menuActionRow] = class9_1.actions[j4] + " @lre@" + itemDef.name;
															if (class9_1.id != 1688/*equipment*/) {
																if (j4 == 0)
																	menuActionID[menuActionRow] = 632;
																if (j4 == 1)
																	menuActionID[menuActionRow] = 78;
																if (j4 == 2)
																	menuActionID[menuActionRow] = 867;
																if (j4 == 3)
																	menuActionID[menuActionRow] = 431;
																if (j4 == 4)
																	menuActionID[menuActionRow] = 53;
																if (j4 == 5)
																	menuActionID[menuActionRow] = 300;
																if (j4 == 6)
																	menuActionID[menuActionRow] = 291;

															} else {
																if (itemDef.equipActions[j4] == null) {
																	menuActionName[3] = "Operate @lre@" + itemDef.name;
																	menuActionName[4] = "Remove @lre@" + itemDef.name;
																} else {
																	class9_1.actions = itemDef.equipActions;
																	menuActionName[menuActionRow] = itemDef.equipActions[j4]
																		+ " @lre@" + itemDef.name;
																}
																if (j4 == 0)
																	menuActionID[menuActionRow] = 632; // remove
																if (j4 == 1)
																	menuActionID[menuActionRow] = 661; // operate 1
																if (j4 == 2)
																	menuActionID[menuActionRow] = 662; // operate 2
																if (j4 == 3)
																	menuActionID[menuActionRow] = 663; // operate 3
																if (j4 == 4)
																	menuActionID[menuActionRow] = 664; // operate 4
															}

															menuActionCmd1[menuActionRow] = itemDef.id;
															menuActionCmd2[menuActionRow] = k2;
															menuActionCmd3[menuActionRow] = class9_1.id;
															menuActionRow++;
														}
													}
												}
											}
										}

										if (class9_1.parentID >= 58040 && class9_1.parentID <= 58048
											|| class9_1.parentID >= 32100 && class9_1.parentID <= 32156
											|| class9_1.parentID >= 32200 && class9_1.parentID <= 32222) {
											return;
										}

										if (class9_1.isItemSearchComponent) {
											menuActionName[menuActionRow] = "Select @lre@" + itemDef.name;
											menuActionID[menuActionRow] = 1130;
											menuActionCmd1[menuActionRow] = itemDef.id;
											menuActionCmd2[menuActionRow] = k2;
											menuActionCmd3[menuActionRow] = class9_1.id;
											menuActionRow++;
										} else {
											if (debugModels) {
												if (System.currentTimeMillis() - debugDelay > 1000) {
													debugDelay = System.currentTimeMillis();
													pushMessage("<col=255>" + itemDef.name + ":</col> Male: <col=255>"
														+ itemDef.manwear + "</col> Female: <col=255>"
														+ itemDef.womanwear + "</col> Model id: <col=255>"
														+ itemDef.inventory_model, 0, "");

													menuActionName[menuActionRow] = "#2 (@whi@"
														+ Arrays.toString(itemDef.colorFind) + ")@gre@)(@whi@"
														+ Arrays.toString(itemDef.colorReplace) + ")@gre@)";
												}
											}

											if (localPlayer.isAdminRights()) {
												menuActionName[menuActionRow] = "Examine @lre@" + itemDef.name + " @whi@(" + (itemID) + ")";
											} else {
												menuActionName[menuActionRow] = "Examine @lre@" + itemDef.name;
											}
											menuActionID[menuActionRow] = 1126;
											menuActionCmd1[menuActionRow] = itemDef.id;
											menuActionCmd2[menuActionRow] = k2;
											menuActionCmd3[menuActionRow] = class9_1.id;
											menuActionRow++;
										}
									}
								}
								k2++;
							}

						}

					}
				}

			} catch (Exception | StackOverflowError e) {
				System.err.println(String.format("Error building interface menu: rootId=%d, childIndex=%d, childId=%d", class9.id, childIndex, class9.children[childIndex]));
				e.printStackTrace();
			}
		}
	}

	private void menuActionsRow(String action, int index, int actionID, int row) {
		if (menuOpen)
			return;
		menuActionName[index] = action;
		menuActionID[index] = actionID;
		menuActionRow = row;
	}

	public void drawScrollbar(int height, int scrollPosition, int yPosition, int xPosition, int scrollMax) {
		scrollBar1.drawSprite(xPosition, yPosition);
		scrollBar2.drawSprite(xPosition, (yPosition + height) - 16);
		Rasterizer2D.drawPixels(height - 32, yPosition + 16, xPosition, 0x000001, 16);
		Rasterizer2D.drawPixels(height - 32, yPosition + 16, xPosition, 0x3d3426, 15);
		Rasterizer2D.drawPixels(height - 32, yPosition + 16, xPosition, 0x342d21, 13);
		Rasterizer2D.drawPixels(height - 32, yPosition + 16, xPosition, 0x2e281d, 11);
		Rasterizer2D.drawPixels(height - 32, yPosition + 16, xPosition, 0x29241b, 10);
		Rasterizer2D.drawPixels(height - 32, yPosition + 16, xPosition, 0x252019, 9);
		Rasterizer2D.drawPixels(height - 32, yPosition + 16, xPosition, 0x000001, 1);
		int k1 = ((height - 32) * height) / scrollMax;
		if (k1 < 8)
			k1 = 8;
		int l1 = ((height - 32 - k1) * scrollPosition) / (scrollMax - height);
		Rasterizer2D.drawPixels(k1, yPosition + 16 + l1, xPosition, barFillColor, 16);
		Rasterizer2D.method341(yPosition + 16 + l1, 0x000001, k1, xPosition);
		Rasterizer2D.method341(yPosition + 16 + l1, 0x817051, k1, xPosition + 1);
		Rasterizer2D.method341(yPosition + 16 + l1, 0x73654a, k1, xPosition + 2);
		Rasterizer2D.method341(yPosition + 16 + l1, 0x6a5c43, k1, xPosition + 3);
		Rasterizer2D.method341(yPosition + 16 + l1, 0x6a5c43, k1, xPosition + 4);
		Rasterizer2D.method341(yPosition + 16 + l1, 0x655841, k1, xPosition + 5);
		Rasterizer2D.method341(yPosition + 16 + l1, 0x655841, k1, xPosition + 6);
		Rasterizer2D.method341(yPosition + 16 + l1, 0x61553e, k1, xPosition + 7);
		Rasterizer2D.method341(yPosition + 16 + l1, 0x61553e, k1, xPosition + 8);
		Rasterizer2D.method341(yPosition + 16 + l1, 0x5d513c, k1, xPosition + 9);
		Rasterizer2D.method341(yPosition + 16 + l1, 0x5d513c, k1, xPosition + 10);
		Rasterizer2D.method341(yPosition + 16 + l1, 0x594e3a, k1, xPosition + 11);
		Rasterizer2D.method341(yPosition + 16 + l1, 0x594e3a, k1, xPosition + 12);
		Rasterizer2D.method341(yPosition + 16 + l1, 0x514635, k1, xPosition + 13);
		Rasterizer2D.method341(yPosition + 16 + l1, 0x4b4131, k1, xPosition + 14);
		Rasterizer2D.method339(yPosition + 16 + l1, 0x000001, 15, xPosition);
		Rasterizer2D.method339(yPosition + 17 + l1, 0x000001, 15, xPosition);
		Rasterizer2D.method339(yPosition + 17 + l1, 0x655841, 14, xPosition);
		Rasterizer2D.method339(yPosition + 17 + l1, 0x6a5c43, 13, xPosition);
		Rasterizer2D.method339(yPosition + 17 + l1, 0x6d5f48, 11, xPosition);
		Rasterizer2D.method339(yPosition + 17 + l1, 0x73654a, 10, xPosition);
		Rasterizer2D.method339(yPosition + 17 + l1, 0x76684b, 7, xPosition);
		Rasterizer2D.method339(yPosition + 17 + l1, 0x7b6a4d, 5, xPosition);
		Rasterizer2D.method339(yPosition + 17 + l1, 0x7e6e50, 4, xPosition);
		Rasterizer2D.method339(yPosition + 17 + l1, 0x817051, 3, xPosition);
		Rasterizer2D.method339(yPosition + 17 + l1, 0x000001, 2, xPosition);
		Rasterizer2D.method339(yPosition + 18 + l1, 0x000001, 16, xPosition);
		Rasterizer2D.method339(yPosition + 18 + l1, 0x564b38, 15, xPosition);
		Rasterizer2D.method339(yPosition + 18 + l1, 0x5d513c, 14, xPosition);
		Rasterizer2D.method339(yPosition + 18 + l1, 0x625640, 11, xPosition);
		Rasterizer2D.method339(yPosition + 18 + l1, 0x655841, 10, xPosition);
		Rasterizer2D.method339(yPosition + 18 + l1, 0x6a5c43, 7, xPosition);
		Rasterizer2D.method339(yPosition + 18 + l1, 0x6e6046, 5, xPosition);
		Rasterizer2D.method339(yPosition + 18 + l1, 0x716247, 4, xPosition);
		Rasterizer2D.method339(yPosition + 18 + l1, 0x7b6a4d, 3, xPosition);
		Rasterizer2D.method339(yPosition + 18 + l1, 0x817051, 2, xPosition);
		Rasterizer2D.method339(yPosition + 18 + l1, 0x000001, 1, xPosition);
		Rasterizer2D.method339(yPosition + 19 + l1, 0x000001, 16, xPosition);
		Rasterizer2D.method339(yPosition + 19 + l1, 0x514635, 15, xPosition);
		Rasterizer2D.method339(yPosition + 19 + l1, 0x564b38, 14, xPosition);
		Rasterizer2D.method339(yPosition + 19 + l1, 0x5d513c, 11, xPosition);
		Rasterizer2D.method339(yPosition + 19 + l1, 0x61553e, 9, xPosition);
		Rasterizer2D.method339(yPosition + 19 + l1, 0x655841, 7, xPosition);
		Rasterizer2D.method339(yPosition + 19 + l1, 0x6a5c43, 5, xPosition);
		Rasterizer2D.method339(yPosition + 19 + l1, 0x6e6046, 4, xPosition);
		Rasterizer2D.method339(yPosition + 19 + l1, 0x73654a, 3, xPosition);
		Rasterizer2D.method339(yPosition + 19 + l1, 0x817051, 2, xPosition);
		Rasterizer2D.method339(yPosition + 19 + l1, 0x000001, 1, xPosition);
		Rasterizer2D.method339(yPosition + 20 + l1, 0x000001, 16, xPosition);
		Rasterizer2D.method339(yPosition + 20 + l1, 0x4b4131, 15, xPosition);
		Rasterizer2D.method339(yPosition + 20 + l1, 0x544936, 14, xPosition);
		Rasterizer2D.method339(yPosition + 20 + l1, 0x594e3a, 13, xPosition);
		Rasterizer2D.method339(yPosition + 20 + l1, 0x5d513c, 10, xPosition);
		Rasterizer2D.method339(yPosition + 20 + l1, 0x61553e, 8, xPosition);
		Rasterizer2D.method339(yPosition + 20 + l1, 0x655841, 6, xPosition);
		Rasterizer2D.method339(yPosition + 20 + l1, 0x6a5c43, 4, xPosition);
		Rasterizer2D.method339(yPosition + 20 + l1, 0x73654a, 3, xPosition);
		Rasterizer2D.method339(yPosition + 20 + l1, 0x817051, 2, xPosition);
		Rasterizer2D.method339(yPosition + 20 + l1, 0x000001, 1, xPosition);
		Rasterizer2D.method341(yPosition + 16 + l1, 0x000001, k1, xPosition + 15);
		Rasterizer2D.method339(yPosition + 15 + l1 + k1, 0x000001, 16, xPosition);
		Rasterizer2D.method339(yPosition + 14 + l1 + k1, 0x000001, 15, xPosition);
		Rasterizer2D.method339(yPosition + 14 + l1 + k1, 0x3f372a, 14, xPosition);
		Rasterizer2D.method339(yPosition + 14 + l1 + k1, 0x443c2d, 10, xPosition);
		Rasterizer2D.method339(yPosition + 14 + l1 + k1, 0x483e2f, 9, xPosition);
		Rasterizer2D.method339(yPosition + 14 + l1 + k1, 0x4a402f, 7, xPosition);
		Rasterizer2D.method339(yPosition + 14 + l1 + k1, 0x4b4131, 4, xPosition);
		Rasterizer2D.method339(yPosition + 14 + l1 + k1, 0x564b38, 3, xPosition);
		Rasterizer2D.method339(yPosition + 14 + l1 + k1, 0x000001, 2, xPosition);
		Rasterizer2D.method339(yPosition + 13 + l1 + k1, 0x000001, 16, xPosition);
		Rasterizer2D.method339(yPosition + 13 + l1 + k1, 0x443c2d, 15, xPosition);
		Rasterizer2D.method339(yPosition + 13 + l1 + k1, 0x4b4131, 11, xPosition);
		Rasterizer2D.method339(yPosition + 13 + l1 + k1, 0x514635, 9, xPosition);
		Rasterizer2D.method339(yPosition + 13 + l1 + k1, 0x544936, 7, xPosition);
		Rasterizer2D.method339(yPosition + 13 + l1 + k1, 0x564b38, 6, xPosition);
		Rasterizer2D.method339(yPosition + 13 + l1 + k1, 0x594e3a, 4, xPosition);
		Rasterizer2D.method339(yPosition + 13 + l1 + k1, 0x625640, 3, xPosition);
		Rasterizer2D.method339(yPosition + 13 + l1 + k1, 0x6a5c43, 2, xPosition);
		Rasterizer2D.method339(yPosition + 13 + l1 + k1, 0x000001, 1, xPosition);
		Rasterizer2D.method339(yPosition + 12 + l1 + k1, 0x000001, 16, xPosition);
		Rasterizer2D.method339(yPosition + 12 + l1 + k1, 0x443c2d, 15, xPosition);
		Rasterizer2D.method339(yPosition + 12 + l1 + k1, 0x4b4131, 14, xPosition);
		Rasterizer2D.method339(yPosition + 12 + l1 + k1, 0x544936, 12, xPosition);
		Rasterizer2D.method339(yPosition + 12 + l1 + k1, 0x564b38, 11, xPosition);
		Rasterizer2D.method339(yPosition + 12 + l1 + k1, 0x594e3a, 10, xPosition);
		Rasterizer2D.method339(yPosition + 12 + l1 + k1, 0x5d513c, 7, xPosition);
		Rasterizer2D.method339(yPosition + 12 + l1 + k1, 0x61553e, 4, xPosition);
		Rasterizer2D.method339(yPosition + 12 + l1 + k1, 0x6e6046, 3, xPosition);
		Rasterizer2D.method339(yPosition + 12 + l1 + k1, 0x7b6a4d, 2, xPosition);
		Rasterizer2D.method339(yPosition + 12 + l1 + k1, 0x000001, 1, xPosition);
		Rasterizer2D.method339(yPosition + 11 + l1 + k1, 0x000001, 16, xPosition);
		Rasterizer2D.method339(yPosition + 11 + l1 + k1, 0x4b4131, 15, xPosition);
		Rasterizer2D.method339(yPosition + 11 + l1 + k1, 0x514635, 14, xPosition);
		Rasterizer2D.method339(yPosition + 11 + l1 + k1, 0x564b38, 13, xPosition);
		Rasterizer2D.method339(yPosition + 11 + l1 + k1, 0x594e3a, 11, xPosition);
		Rasterizer2D.method339(yPosition + 11 + l1 + k1, 0x5d513c, 9, xPosition);
		Rasterizer2D.method339(yPosition + 11 + l1 + k1, 0x61553e, 7, xPosition);
		Rasterizer2D.method339(yPosition + 11 + l1 + k1, 0x655841, 5, xPosition);
		Rasterizer2D.method339(yPosition + 11 + l1 + k1, 0x6a5c43, 4, xPosition);
		Rasterizer2D.method339(yPosition + 11 + l1 + k1, 0x73654a, 3, xPosition);
		Rasterizer2D.method339(yPosition + 11 + l1 + k1, 0x7b6a4d, 2, xPosition);
		Rasterizer2D.method339(yPosition + 11 + l1 + k1, 0x000001, 1, xPosition);
	}
	private Sprite top508;
	private Sprite bottom508;
	private Sprite ignorelisticon;
	private Sprite cursesidebaricon;
	private Sprite firstquesttab,secondquesttab,thirdquesttab,fourthquesttab;
	public void draw508Scrollbar(int height, int pos, int y, int x, int maxScroll, boolean transparent) {


			top508.drawSprite(x, y);
			bottom508.drawSprite(x, (y + height) - 16);
			Rasterizer2D.draw_filled_rect(x, y + 16, 16, height - 32, 0x746241);
			Rasterizer2D.draw_filled_rect(x, y + 16, 15, height - 32, 0x77603e);
			Rasterizer2D.draw_filled_rect(x, y + 16, 14, height - 32, 0x77603e);
			Rasterizer2D.draw_filled_rect(x, y + 16, 13, height - 32, 0x95784a);
			Rasterizer2D.draw_filled_rect(x, y + 16, 12, height - 32, 0x997c52);
			Rasterizer2D.draw_filled_rect(x, y + 16, 11, height - 32, 0x9e8155);
			Rasterizer2D.draw_filled_rect(x, y + 16, 10, height - 32, 0xa48558);
			Rasterizer2D.draw_filled_rect(x, y + 16, 8, height - 32, 0xaa8b5c);
			Rasterizer2D.draw_filled_rect(x, y + 16, 6, height - 32, 0xb09060);
			Rasterizer2D.draw_filled_rect(x, y + 16, 3, height - 32, 0x866c44);
			Rasterizer2D.draw_filled_rect(x, y + 16, 1, height - 32, 0x7c6945);

			int k1 = ((height - 32) * height) / maxScroll;
			if (k1 < 8) {
				k1 = 8;
			}
			int l1 = ((height - 32 - k1) * pos) / (maxScroll - height);
			int l2 = ((height - 32 - k1) * pos) / (maxScroll - height) + 6;
			Rasterizer2D.drawVerticalLine(x + 1, y + 16 + l1, k1, 0x5c492d);
			Rasterizer2D.drawVerticalLine(x + 14, y + 16 + l1, k1, 0x5c492d);
			Rasterizer2D.drawHorizontalLine(x + 1, y + 16 + l1, 14, 0x5c492d);
			Rasterizer2D.drawHorizontalLine(x + 1, y + 15 + l1 + k1, 14, 0x5c492d);
			Rasterizer2D.drawHorizontalLine(x + 4, y + 18 + l1, 8, 0x664f2b);
			Rasterizer2D.drawHorizontalLine(x + 4, y + 13 + l1 + k1, 8, 0x664f2b);
			Rasterizer2D.drawHorizontalLine(x + 3, y + 19 + l1, 2, 0x664f2b);
			Rasterizer2D.drawHorizontalLine(x + 11, y + 19 + l1, 2, 0x664f2b);
			Rasterizer2D.drawHorizontalLine(x + 3, y + 12 + l1 + k1, 2, 0x664f2b);
			Rasterizer2D.drawHorizontalLine(x + 11, y + 12 + l1 + k1, 2, 0x664f2b);
			Rasterizer2D.drawHorizontalLine(x + 3, y + 14 + l1 + k1, 11, 0x866c44);
			Rasterizer2D.drawHorizontalLine(x + 3, y + 17 + l1, 11, 0x866c44);
			Rasterizer2D.drawVerticalLine(x + 13, y + 12 + l2, k1 - 4, 0x866c44);
			Rasterizer2D.drawVerticalLine(x + 3, y + 13 + l2, k1 - 6, 0x664f2b);
			Rasterizer2D.drawVerticalLine(x + 12, y + 13 + l2, k1 - 6, 0x664f2b);
			Rasterizer2D.drawHorizontalLine(x + 2, y + 18 + l1, 2, 0x866c44);
			Rasterizer2D.drawHorizontalLine(x + 2, y + 13 + l1 + k1, 2, 0x866c44);
			Rasterizer2D.drawHorizontalLine(x + 12, y + 18 + l1, 1, 0x866c44);
			Rasterizer2D.drawHorizontalLine(x + 12, y + 13 + l1 + k1, 1, 0x866c44);

	}
	/**
	 * NPC Updating
	 * <p>
	 * There is a crash at random npcs so i did some sort of cheap fix to stop it
	 * from throwing runtime exception which will ultimately log a player out, even
	 * regardless of where and when they are in the game, causes to many issues.
	 *
	 * @param stream
	 * @param i
	 */
	public void updateNPCs(Buffer stream, int i) {
		lowres_entity_count = 0;
		anInt893 = 0;
		update_high_resolution_npcs(stream);
		update_low_resolution_npcs(i, stream);
		updated_extended_npcinfo(stream);
		for (int k = 0; k < lowres_entity_count; k++) {
			int l = lowres_entity_list[k];
			if (npcs[l].last_update_tick != update_tick) {
				npcs[l].desc = null;
				npcs[l] = null;
			}
		}

		// Cheaphax fix to it doesnt throw exception and boot player
		// Cheaphax fix to it doesnt throw exception and boot player
		if (stream.currentPosition == -1 || stream.currentPosition != i) {
			System.out.println("[NPC] Size mismatch : returning");
			return;
		}

		if (stream.currentPosition != i) {
			Signlink.reporterror(
					myUsername + " size mismatch in getnpcpos - pos:" + stream.currentPosition + " psize:" + i);
			throw new RuntimeException("eek");
		}
		for (int i1 = 0; i1 < npcCount; i1++)
			if (npcs[highres_npc_list[i1]] == null) {
				Signlink.reporterror(myUsername + " null entry in npc list - pos:" + i1 + " size:" + npcCount);
				throw new RuntimeException("eek");
			}

	}

	private int channelButtonHoverPosition;
	private int channelButtonClickPosition;

	public void processChatModeClick() {
		if (MouseHandler.mouseY >= canvasHeight - 22 && MouseHandler.mouseY <= canvasHeight) {
			if (MouseHandler.mouseX >= 5 && MouseHandler.mouseX <= 61) {
				channelButtonHoverPosition = 0;
				inputTaken = true;
			} else if (MouseHandler.mouseX >= 71 && MouseHandler.mouseX <= 127) {
				channelButtonHoverPosition = 1;
				inputTaken = true;
			} else if (MouseHandler.mouseX >= 137 && MouseHandler.mouseX <= 193) {
				channelButtonHoverPosition = 2;
				inputTaken = true;
			} else if (MouseHandler.mouseX >= 203 && MouseHandler.mouseX <= 259) {
				channelButtonHoverPosition = 3;
				inputTaken = true;
			} else if (MouseHandler.mouseX >= 269 && MouseHandler.mouseX <= 325) {
				channelButtonHoverPosition = 4;
				inputTaken = true;
			} else if (MouseHandler.mouseX >= 335 && MouseHandler.mouseX <= 391) {
				channelButtonHoverPosition = 5;
				inputTaken = true;
			} else if (MouseHandler.mouseX >= 404 && MouseHandler.mouseX <= 515) {
				channelButtonHoverPosition = 6;
				inputTaken = true;
			}
		} else {
			channelButtonHoverPosition = -1;
			inputTaken = true;
		}
		if (clickMode3 == 1) {
			if (MouseHandler.saveClickY >= canvasHeight - 22 && MouseHandler.saveClickY <= canvasHeight) {
				if (MouseHandler.saveClickX >= 5 && MouseHandler.saveClickX <= 61) {
					if (channelButtonClickPosition == 0)
						toggleHideChatArea();
					channelButtonClickPosition = 0;
					chatTypeView = 0;
					inputTaken = true;
				} else if (MouseHandler.saveClickX >= 71 && MouseHandler.saveClickX <= 127) {
					if (channelButtonClickPosition == 1)
						toggleHideChatArea();
					channelButtonClickPosition = 1;
					chatTypeView = 5;
					inputTaken = true;
				} else if (MouseHandler.saveClickX >= 137 && MouseHandler.saveClickX <= 193) {
					if (channelButtonClickPosition == 2)
						toggleHideChatArea();
					channelButtonClickPosition = 2;
					chatTypeView = 1;
					inputTaken = true;
				} else if (MouseHandler.saveClickX >= 203 && MouseHandler.saveClickX <= 259) {
					if (channelButtonClickPosition == 3)
						toggleHideChatArea();
					channelButtonClickPosition = 3;
					chatTypeView = 2;
					inputTaken = true;
				} else if (MouseHandler.saveClickX >= 269 && MouseHandler.saveClickX <= 325) {
					if (channelButtonClickPosition == 4)
						toggleHideChatArea();
					channelButtonClickPosition = 4;
					chatTypeView = 11;
					inputTaken = true;
				} else if (MouseHandler.saveClickX >= 335 && MouseHandler.saveClickX <= 391) {
					if (channelButtonClickPosition == 5)
						toggleHideChatArea();
					channelButtonClickPosition = 5;
					chatTypeView = 3;
					inputTaken = true;
				}
			}
		}
	}

	private static boolean hideChatArea = false;

	private static void toggleHideChatArea() {
		if (!instance.isResized())
			return;
		hideChatArea = !hideChatArea;
	}

	public void method33(int i) {
		if (i > Varp.cacheSize)
			return;

		if (Varp.cache[i] == null)
			return;

		int j = Varp.cache[i].anInt709;
		if (j == 0)
			return;
		int k = variousSettings[i];
		if (j == 1) {
			if (k == 1)
				Rasterizer3D.setBrightness(0.90000000000000002D);
			if (k == 2)
				Rasterizer3D.setBrightness(0.80000000000000004D);
			if (k == 3)
				Rasterizer3D.setBrightness(0.69999999999999996D);
			if (k == 4)
				Rasterizer3D.setBrightness(0.59999999999999998D);
			ItemDefinition.sprites.unlinkAll();
			welcomeScreenRaised = true;
		}
		if (j == 3) {
			int volume = 0;

			if (k == 0) {
				volume = 255;
			} else if (k == 1) {
				volume = 192;
			} else if (k == 2) {
				volume = 128;
			} else if (k == 3) {
				volume = 64;
			} else if (k == 4) {
				volume = 0;
			}

			if (volume != musicVolume) {
				if (musicVolume != 0 || currentSong == -1) {
					if (volume != 0) {
						// setVolume(volume);
					} else {
						// method55(false);
						prevSong = 0;
					}
				} else {
					// method56(volume, false, currentSong);
					prevSong = 0;
				}

				musicVolume = volume;
			}
		}
		if (j == 4) {
			if (k == 0) {
				soundEffectVolume = 127;
			} else if (k == 1) {
				soundEffectVolume = 96;
			} else if (k == 2) {
				soundEffectVolume = 64;
			} else if (k == 3) {
				soundEffectVolume = 32;
			} else if (k == 4) {
				soundEffectVolume = 0;
			}
		}
		if (j == 5)
			anInt1253 = k;
		if (j == 6)
			anInt1249 = k;
		if (j == 9)
			anInt913 = k;
	}

	public void updateEntities() {
		try {

			int anInt974 = 0;


			for (int j = -1; j < playerCount + npcCount; j++) {
				Object obj;
				if (j == -1)
					obj = localPlayer;
				else if (j < playerCount)
					obj = players[highres_player_list[j]];
				else
					obj = npcs[highres_npc_list[j - playerCount]];
				if (obj == null || !((Entity) (obj)).isVisible())
					continue;
				if (obj instanceof Npc) {
					NpcDefinition entityDef = ((Npc) obj).desc;
					if (entityDef.multi != null)
						entityDef = entityDef.get_multi_npctype();
					if (entityDef == null)
						continue;
				}
				/**
				 * Players
				 */
				if (j < playerCount) {
					int l = 30;//the offset
					Player player = (Player) obj;
					npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height + 15);

						if (spriteDrawX > -1) {

							if(isPlayerNamesEnabled()) {
								int myname= 0x00B8D4;
							int 	otherplayernames = 0x00FF90;
						


								if (player != localPlayer)
									myname = otherplayernames;


	newRegularFont.drawCenteredString(player.displayName, spriteDrawX , spriteDrawY - 7, myname, 0);
								l += 20;
							}
					}
					if (player.headIcon >= 0) {
						if (player.skullIcon < 2) {
							skullIcons[player.skullIcon].drawSprite(spriteDrawX - 12, spriteDrawY - l);
							l += 25;
						}
						if (player.headIcon < 18) {
							headIcons[player.headIcon].drawSprite(spriteDrawX - 12, spriteDrawY - l);
							l += 18;
						}

					}
					if (j >= 0 && hintType == 10 && anInt933 == highres_player_list[j]) {
						npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height + 15);

						if (spriteDrawX > -1)
							headIconsHint[player.hintIcon].drawSprite(spriteDrawX - 12, spriteDrawY - l);//oh this is the flashing arrow above head lmao
					}

				} else {
					int offset = 9;
					NpcDefinition entityDef_1 = ((Npc) obj).desc;
					npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height + 15);

					if (spriteDrawX > -1) {

						if (isNPCNamesEnabled()) {
							int regularnpcs  = 0x50C878;//green

							int attackablenpccolor = 0xf3191a;//red
							boolean isattackablenpc = entityDef_1.combatLevel > 0 ? true : false;
							int colortodraw = isattackablenpc ? attackablenpccolor : regularnpcs;


	newRegularFont.drawCenteredString(entityDef_1.name, spriteDrawX , spriteDrawY - offset, colortodraw, 0);
	
								}
					}

					if (entityDef_1.headicon_prayer >= 0 && entityDef_1.headicon_prayer < headIcons.length) {

						if (spriteDrawX > -1)
							headIcons[entityDef_1.headicon_prayer].drawSprite(spriteDrawX - 12, spriteDrawY - 30);
						offset += 25;
					}
					if (hintType == 1 && anInt1222 == highres_npc_list[j - playerCount] && update_tick % 20 < 10) {
				
						if (spriteDrawX > -1)
							headIconsHint[0].drawSprite(spriteDrawX - 5, spriteDrawY - 28);

					}

				}

				if (((Entity) (obj)).textSpoken != null && (j >= playerCount || publicChatMode == 0
						|| publicChatMode == 3 || publicChatMode == 1 && isFriendOrSelf(((Player) obj).displayName))) {
					npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height);
					if (spriteDrawX > -1 && anInt974 < anInt975) {
						anIntArray979[anInt974] = chatTextDrawingArea.method384(((Entity) (obj)).textSpoken) / 2;
						anIntArray978[anInt974] = chatTextDrawingArea.anInt1497;
						anIntArray976[anInt974] = spriteDrawX;
						anIntArray977[anInt974] = spriteDrawY;
						anIntArray980[anInt974] = ((Entity) (obj)).anInt1513;
						anIntArray981[anInt974] = ((Entity) (obj)).anInt1531;
						anIntArray982[anInt974] = ((Entity) (obj)).textCycle;
						aStringArray983[anInt974++] = ((Entity) (obj)).textSpoken;
						if (informationFile.rememberChatEffects&& ((Entity) (obj)).anInt1531 >= 1 && ((Entity) (obj)).anInt1531 <= 3) {
							anIntArray978[anInt974] += 10;
							anIntArray977[anInt974] += 5;
						}
						if (informationFile.rememberChatEffects&& ((Entity) (obj)).anInt1531 == 4)
							anIntArray979[anInt974] = 60;
						if (informationFile.rememberChatEffects && ((Entity) (obj)).anInt1531 == 5)
							anIntArray978[anInt974] += 5;
					}
				}
				if (((Entity) (obj)).loopCycleStatus > update_tick) {
					try {
						npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height + 15);
						if (spriteDrawX > -1) {//oh this is the fucking hp drawing
							int i1 = (((Entity) (obj)).currentHealth * 30) / ((Entity) (obj)).maxHealth;
							int i2 = (((Entity) (obj)).currentHealth * 30) / ((Entity) (obj)).maxHealth;

							if (i1 > 30)
								i1 = 30;
							Rasterizer2D.drawPixels(5, spriteDrawY - 3, spriteDrawX - 15, 65280, i1);
							Rasterizer2D.drawPixels(5, spriteDrawY - 3, (spriteDrawX - 15) + i1, 0xff0000, 30 - i1);
						}
					} catch (Exception e) {
					}
				}
				for (int j1 = 0; j1 < 4; j1++)
					if (((Entity) (obj)).hitsLoopCycle[j1] > update_tick) {
						npcScreenPos(((Entity) (obj)), ((Entity) (obj)).height / 2);

						if (spriteDrawX > -1) {
							if (j1 == 1)
								spriteDrawY -= 20;
							if (j1 == 2) {
								spriteDrawX -= 15;
								spriteDrawY -= 10;
							}
							if (j1 == 3) {
								spriteDrawX += 15;
								spriteDrawY -= 10;
							}
							if (j1 == 19) { // Heal hitsplat
								spriteDrawX += 1;
								spriteDrawY -= 1;
							}
							hitMarks[((Entity) (obj)).hitMarkTypes[j1]].drawSprite(spriteDrawX - 12, spriteDrawY - 12);
							smallText.drawText(0, String.valueOf(((Entity) (obj)).hitArray[j1]), spriteDrawY + 4,
									spriteDrawX);
							smallText.drawText(0xffffff, String.valueOf(((Entity) (obj)).hitArray[j1]), spriteDrawY + 3,
									spriteDrawX - 1);
						}
					}
			}
			for (int k = 0; k < anInt974; k++) {
				int k1 = anIntArray976[k];
				int l1 = anIntArray977[k];
				int j2 = anIntArray979[k];
				int k2 = anIntArray978[k];
				boolean flag = true;
				while (flag) {
					flag = false;
					for (int l2 = 0; l2 < k; l2++)
						if (l1 + 2 > anIntArray977[l2] - anIntArray978[l2] && l1 - k2 < anIntArray977[l2] + 2
								&& k1 - j2 < anIntArray976[l2] + anIntArray979[l2]
								&& k1 + j2 > anIntArray976[l2] - anIntArray979[l2]
								&& anIntArray977[l2] - anIntArray978[l2] < l1) {
							l1 = anIntArray977[l2] - anIntArray978[l2];
							flag = true;
						}

				}
				spriteDrawX = anIntArray976[k];
				spriteDrawY = anIntArray977[k] = l1;

				String s = aStringArray983[k];
				if (informationFile.isRememberChatEffects()) {
					int i3 = 0xffff00;
					if (anIntArray980[k] < 6)
						i3 = anIntArray965[anIntArray980[k]];
					if (anIntArray980[k] == 6)
						i3 = anInt1265 % 20 >= 10 ? 0xffff00 : 0xff0000;
					if (anIntArray980[k] == 7)
						i3 = anInt1265 % 20 >= 10 ? 65535 : 255;
					if (anIntArray980[k] == 8)
						i3 = anInt1265 % 20 >= 10 ? 0x80ff80 : 45056;
					if (anIntArray980[k] == 9) {
						int j3 = 150 - anIntArray982[k];
						if (j3 < 50)
							i3 = 0xff0000 + 1280 * j3;
						else if (j3 < 100)
							i3 = 0xffff00 - 0x50000 * (j3 - 50);
						else if (j3 < 150)
							i3 = 65280 + 5 * (j3 - 100);
					}
					if (anIntArray980[k] == 10) {
						int k3 = 150 - anIntArray982[k];
						if (k3 < 50)
							i3 = 0xff0000 + 5 * k3;
						else if (k3 < 100)
							i3 = 0xff00ff - 0x50000 * (k3 - 50);
						else if (k3 < 150)
							i3 = (255 + 0x50000 * (k3 - 100)) - 5 * (k3 - 100);
					}
					if (anIntArray980[k] == 11) {
						int l3 = 150 - anIntArray982[k];
						if (l3 < 50)
							i3 = 0xffffff - 0x50005 * l3;
						else if (l3 < 100)
							i3 = 65280 + 0x50005 * (l3 - 50);
						else if (l3 < 150)
							i3 = 0xffffff - 0x50000 * (l3 - 100);
					}
					/**
					 * Entity chat
					 */
					if (anIntArray981[k] == 0) {
						chatTextDrawingArea.drawText(0, s, spriteDrawY + 1, spriteDrawX + 1);
						chatTextDrawingArea.drawText(i3, s, spriteDrawY, spriteDrawX);
					}
					if (anIntArray981[k] == 1) {
						chatTextDrawingArea.method386(0, s, spriteDrawX + 1, anInt1265, spriteDrawY + 1);
						chatTextDrawingArea.method386(i3, s, spriteDrawX, anInt1265, spriteDrawY);
					}
					if (anIntArray981[k] == 2) {
						chatTextDrawingArea.method387(spriteDrawX + 1, s, anInt1265, spriteDrawY + 1, 0);
						chatTextDrawingArea.method387(spriteDrawX, s, anInt1265, spriteDrawY, i3);
					}
					if (anIntArray981[k] == 3) {
						chatTextDrawingArea.method388(150 - anIntArray982[k], s, anInt1265, spriteDrawY + 1,
								spriteDrawX + 1, 0);
						chatTextDrawingArea.method388(150 - anIntArray982[k], s, anInt1265, spriteDrawY, spriteDrawX,
								i3);
					}
					if (anIntArray981[k] == 4) {
						int i4 = chatTextDrawingArea.method384(s);
						int k4 = ((150 - anIntArray982[k]) * (i4 + 100)) / 150;
						Rasterizer2D.setDrawingArea(334, spriteDrawX - 50, spriteDrawX + 50, 0);
						chatTextDrawingArea.method385(0, s, spriteDrawY + 1, (spriteDrawX + 50) - k4);
						chatTextDrawingArea.method385(i3, s, spriteDrawY, (spriteDrawX + 50) - k4);
						Rasterizer2D.defaultDrawingAreaSize();
					}
					if (anIntArray981[k] == 5) {
						int j4 = 150 - anIntArray982[k];
						int l4 = 0;
						if (j4 < 25)
							l4 = j4 - 25;
						else if (j4 > 125)
							l4 = j4 - 125;
						Rasterizer2D.setDrawingArea(spriteDrawY + 5, 0, 516,
								spriteDrawY - chatTextDrawingArea.anInt1497 - 1);
						chatTextDrawingArea.drawText(0, s, spriteDrawY + 1 + l4, spriteDrawX);
						chatTextDrawingArea.drawText(i3, s, spriteDrawY + l4, spriteDrawX);
						Rasterizer2D.defaultDrawingAreaSize();
					}
				} else {
					chatTextDrawingArea.drawText(0, s, spriteDrawY + 1, spriteDrawX);
					chatTextDrawingArea.drawText(0xffff00, s, spriteDrawY, spriteDrawX);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void delFriend(long l) {
		try {
			if (l == 0L)
				return;
			for (int i = 0; i < friendsCount; i++) {
				if (friendsListAsLongs[i] != l)
					continue;
				friendsCount--;
				needDrawTabArea = true;
				for (int j = i; j < friendsCount; j++) {
					friendsList[j] = friendsList[j + 1];
					friendsNodeIDs[j] = friendsNodeIDs[j + 1];
					friendsListAsLongs[j] = friendsListAsLongs[j + 1];
				}

				stream.createFrame(215);
				stream.writeQWord(l);
				break;
			}
		} catch (RuntimeException runtimeexception) {
			Signlink.reporterror("18622, " + false + ", " + l + ", " + runtimeexception.toString());
			throw new RuntimeException();
		}
	}

	Sprite tabAreaFixed;
	Sprite[] tabAreaResizable = new Sprite[3];
	private boolean drawingTabArea = false;

	public void drawTabArea() {
		drawingTabArea = true;
		boolean fixedMode = !isResized();
		final int xOffset = !isResized() ? 516 : 0;
		final int yOffset = !isResized() ? 168 : 0;

		Rasterizer3D.scanOffsets = anIntArray1181;
		if (fixedMode) {
			tabAreaFixed.drawSprite(xOffset,yOffset);
			if (invOverlayInterfaceID == 0)
				drawTabs(xOffset,yOffset);

		} else {
			(stackTabs() ? tabAreaResizable[1] : tabAreaResizable[2]).drawSprite(
					Client.canvasWidth - (stackTabs() ? 231 : 462),
					Client.canvasHeight - (stackTabs() ? 73 : 37) );

			tabAreaResizable[0].drawSprite((Client.canvasWidth - 204),
					Client.canvasHeight - 275 - (stackTabs() ? 73 : 37));

			if (invOverlayInterfaceID == 0)
				drawTabs(xOffset,yOffset);
		}
		int y = stackTabs() ? 73 : 37;
		if(isStatusBarsEnabled()){
			drawHpBar(xOffset,yOffset);
			drawPrayerBar(xOffset,yOffset);
		}

		//drawflashingarrow(xOffset,yOffset);

		// Set open tab to one that has an interface
		if (tabInterfaceIDs[tabID] <= 0) {
			for (int i = tabID; i >= 0; i--) {
				if (tabInterfaceIDs[i] >= 1) {
					tabID = i;
				//	pushMessage("mebe ? "+tabID, 0, "");
					break;
				}
			}

			if (tabInterfaceIDs[tabID] <= 0)
				tabID = 3;
		}

		if (invOverlayInterfaceID != 0) {
			drawInterface(0, (fixedMode ? 31 + xOffset : canvasWidth - 197),
					RSInterface.interfaceCache[invOverlayInterfaceID],
					(fixedMode ? 37 + yOffset : canvasHeight - 275 - y + 10));
		} else if (Client.tabInterfaceIDs[Client.tabID] != -1) {
			try {
				drawInterface(0, (fixedMode ? 31 + xOffset : canvasWidth - 197),
						RSInterface.interfaceCache[Client.tabInterfaceIDs[Client.tabID]],
						(fixedMode ? 37 + yOffset : canvasHeight - 275 - y + 10));
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
//comment this out to this will fix the hovering in quest tab changing the chatbox black / blue for some reason
//		if (drawTabInterfaceHover != 0) {
//			RSInterface parent = RSInterface.interfaceCache[drawTabInterfaceHoverParent];
//			if (drawTabInterfaceHoverTimer == 0 || drawTabInterfaceHoverLast != drawTabInterfaceHover)
//				drawTabInterfaceHoverTimer = System.currentTimeMillis();
//			drawTabInterfaceHoverLast = drawTabInterfaceHover;
//			if (System.currentTimeMillis() - drawTabInterfaceHoverTimer >= parent.hoverInterfaceDelay) {
//				RSInterface hover = RSInterface.interfaceCache[drawTabInterfaceHover];
//				int xOffset1 = (fixedMode ? 516 + xOffset : 0);
//				int yOffset1 = (fixedMode ? 168 + yOffset : 0);
//				int drawingAreaWidth = canvasWidth - xOffset1;
//				int drawingAreaHeight = canvasHeight - yOffset1;
//				int mouseX = MouseHandler.mouseX - xOffset1;
//				int mouseY = MouseHandler.mouseY - yOffset1;
//				int interfaceWidth = hover.width + 12; // Was drawing offscreen a bit, don't have time to proper fix
//
//				if (mouseX + interfaceWidth > drawingAreaWidth) {
//					mouseX = drawingAreaWidth - interfaceWidth;
//				}
//
//				if (mouseY + hover.height > drawingAreaHeight) {
//					mouseY = drawingAreaHeight - hover.height;
//				}
//				Rasterizer2D.setDrawingArea(Client.instance.getViewportHeight(), 0, Client.instance.getViewportWidth(), 0);
//
//				drawInterface(0, mouseX, hover, mouseY, true);
//			}
//		} else {
//			drawTabInterfaceHoverTimer = 0;
//		}

		if(hintMenu && isInventoryMenuEnabled()){
			drawHintMenu(hintName,hintId, 0xff9040);
		}

		
		if (menuOpen) {
			drawMenu(0,0);
		}
		//drawflashingarrow(xOffset,yOffset);
		Rasterizer3D.scanOffsets = anIntArray1182;
		
		drawingTabArea = false;
	}

	Sprite[] sideIcons = new Sprite[15];
	Sprite[] redStones = new Sprite[6];

	Sprite hpBarSprite = new Sprite("bars/hp");
	Sprite prayerBarSprite = new Sprite("bars/prayer");
	
	

	private void drawHpBar(int xOffset,int yOffset) {
		try {
			int offsetX = xOffset;
			int offsetY = yOffset;
			int offsetX2 = 0;
			int offsetWidth = 0;
			if (Client.instance.isResized()) {
				offsetX = canvasWidth - 246;
				offsetY = canvasHeight - 337;
				// if (changeTabArea) {
				offsetX += 2;
				offsetY -= 30;
				offsetX2 -= 3;
				offsetWidth -= 6;
				if (canvasWidth >= 1000)
					offsetY += 36;
				// }
			}
			int level = currentStats[3];
			int max = maxStats[3];
			if (max == 0)
				max = 1;
			if (level > max)
				level = max;
			if(level > 99)//will fix it going too high when eating anglerfish or something
				level = 99;
			int percent = level * 100 / max;
			int toPixels = -250 * percent / 100;
			TextDrawingArea.drawRectangle(offsetX + 12, offsetY + 43, offsetWidth + 18, 250, 0x771111);
			TextDrawingArea.drawTransparentBox(offsetX + 12, offsetY + 43, offsetWidth + 18, 250, 0x000000, 125);
			TextDrawingArea.drawTransparentBox(offsetX + 12, offsetY + 293 + toPixels, offsetWidth + 18,
					250 * percent / 100, 0xaa0000, 100);
			newSmallFont.drawCenteredString(Integer.toString(currentStats[3]), offsetX2 + offsetX + 21, offsetY + 74,
					0xffffff, 1);
			hpBarSprite.drawSprite(offsetX2 + offsetX + 14, offsetY + 45);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	private void drawPrayerBar(int xOffset,int yOffset) {
		try {
			int offsetX = xOffset;
			int offsetY = yOffset;
			int offsetX2 = 0;
			int offsetWidth = 0;
			if (Client.instance.isResized()) {
				offsetX = canvasWidth - 247;
				offsetY = canvasHeight - 337;
				// if (changeTabArea) {
				offsetX -= 193;
				offsetY -= 30;
				offsetX2 -= 3;
				offsetWidth -= 6;
				if (canvasWidth >= 1000)
					offsetY += 36;
				// }
			}


			int level = currentStats[5];
			int max = maxStats[5];
			if (max == 0)
				max = 1;
			if(max > 99)
				max = 99;
			int percent = level * 100 / max;
			int toPixels = -250 * percent / 100;
			TextDrawingArea.drawRectangle(offsetX + 222, offsetY + 43, offsetWidth + 18, 250, 0x118963);
			TextDrawingArea.drawTransparentBox(offsetX + 222, offsetY + 43, offsetWidth + 18, 250, 0x000000, 125);
			TextDrawingArea.drawTransparentBox(offsetX + 222, offsetY + 293 + toPixels, offsetWidth + 18,
					250 * percent / 100, 0x00d9b3, 100);
			newSmallFont.drawCenteredString(Integer.toString(level), offsetX2 + offsetX + 231, offsetY + 74, 0xffffff,
					1);
			prayerBarSprite.drawSprite(offsetX2 + offsetX + 224, offsetY + 45);
			sendFrame126(currentStats[5]+"/"+maxStats[5], 22499);
			// System.out.println(gameScreenWidth);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void drawflashingarrow(int xOffset,int yOffset) {
		try {
			int offsetX = xOffset;
			int offsetY = yOffset;
			int offsetX2 = 0;
			int offsetWidth = 0;
			if (Client.instance.isResized()) {
				offsetX = canvasWidth - 246;
				offsetY = canvasHeight - 337;
				// if (changeTabArea) {
				offsetX += 2;
				offsetY -= 30;
				offsetX2 -= 3;
				offsetWidth -= 6;
				if (canvasWidth >= 1000)
					offsetY += 36;
				// }
			}
			//lets put the flashing arrow under the quest tab
//			flashingarrow.drawSprite(offsetX2 + offsetX + 14, offsetY + 45);
//			flashingarrow.drawSprite(100,100);
		//	flashingarrow.drawSprite1(0,0, 0 + (int) (50 * Math.sin(update_tick / 5.0)));
		//	flashingarrow.drawSprite1(xOffset,yOffset, 0 + (int) (50 * Math.sin(update_tick / 5.0)));

			//flashingarrow.drawSprite1(xOffset+34,yOffset+50, 50 + (int) (50 * Math.sin(update_tick / 5.0)));//correct one
			//flashingarrow.drawSprite1(xOffset+80,yOffset+50, 50 + (int) (100 * Math.sin(update_tick / 1.0)));//correct one
		//	flashingarrow.drawSprite1(xOffset+50,yOffset+50, 50 + (int) (100 * Math.sin(update_tick / 5.0)));//correct one
if(update_tick % 40 < 20){
	flashingarrow.drawSprite(xOffset+80,yOffset+50);
}
			//	flashingarrow.drawSprite1(xOffset+100,yOffset+100, 100 + (int) (50 * Math.sin(update_tick / 5.0)));


		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public boolean flashplayerpanel = false;	public boolean flashworldmap = false;
	public boolean friendstab = true;
	public int questtabselected = 1;
	private void drawTabs(int xOffset,int yOffset) {
		if (!isResized()) {
			final int[][] sideIconCoordinates = new int[][]{{17, 17}, {49, 15}, {83, 15}, {113, 13},
					{146, 10}, {180, 11}, {214, 15}, {14, 311}, {49, 314}, {82, 314}, {116, 310},
					{148, 312}, {184, 311}, {216, 311}, {216, 311}};
			final int[][] sideIconCoordinates1 = new int[][]{{24, 8}, {49, 5}, {79, 5}, {108, 3}, {147, 5},
					{176, 5}, {205, 8}, {22, 300}, {49, 304}, {77, 304}, {111, 303}, {147, 301},
					{180, 303}, {204, 303}, {204, 303}};
			if (Client.tabInterfaceIDs[Client.tabID] != -1) {
				//if(getUserSettings().isOldGameframe() == false) {
				if (!isOldframeEnabled()) {
					if (Client.tabID == 0)
						redStones[0].drawSprite(5 + xOffset, 0 + yOffset);
					if (Client.tabID == 1)
						redStones[4].drawSprite(43 + xOffset, 0 + yOffset);
					if (Client.tabID == 2)
						redStones[4].drawSprite(76 + xOffset, 0 + yOffset);
					if (Client.tabID == 3)
						redStones[4].drawSprite(109 + xOffset, 0 + yOffset);
					if (Client.tabID == 4)
						redStones[4].drawSprite(142 + xOffset, 0 + yOffset);
					if (Client.tabID == 5)
						redStones[4].drawSprite(175 + xOffset, 0 + yOffset);
					if (Client.tabID == 6)
						redStones[1].drawSprite(208 + xOffset, 0 + yOffset);
					if (Client.tabID == 7)
						redStones[2].drawSprite(5 + xOffset, 298 + yOffset);
					if (Client.tabID == 8)
						redStones[4].drawSprite(43 + xOffset, 298 + yOffset);
					if (Client.tabID == 9)
						redStones[4].drawSprite(76 + xOffset, 298 + yOffset);
					if (Client.tabID == 10)
						redStones[4].drawSprite(109 + xOffset, 298 + yOffset);
					if (Client.tabID == 11)
						redStones[4].drawSprite(142 + xOffset, 298 + yOffset);
					if (Client.tabID == 12)
						redStones[4].drawSprite(175 + xOffset, 298 + yOffset);
					if (Client.tabID == 13)
						redStones[3].drawSprite(208 + xOffset, 298 + yOffset);
				} else {
					if (Client.tabID == 0)
						redStones[1].drawSprite(14 + xOffset, 0 + yOffset);
					if (Client.tabID == 1)
						redStones[2].drawSprite(47 + xOffset, 0 + yOffset);
					if (Client.tabID == 2)
						redStones[2].drawSprite(74 + xOffset, 0 + yOffset);
					if (Client.tabID == 3)
						redStones[3].drawSprite(102 + xOffset, 0 + yOffset);
					if (Client.tabID == 4)
						redStones[2].drawSprite(144 + xOffset, 0 + yOffset);
					if (Client.tabID == 5)
						redStones[2].drawSprite(172 + xOffset, 0 + yOffset);
					if (Client.tabID == 6)
						redStones[0].drawSprite(201 + xOffset, 0 + yOffset);
					if (Client.tabID == 7)
						redStones[4].drawSprite(13 + xOffset, 296 + yOffset);
					if (Client.tabID == 8)
						redStones[2].drawSprite(46 + xOffset, 297 + yOffset);
					if (Client.tabID == 9)
						redStones[2].drawSprite(74 + xOffset, 298 + yOffset);
					if (Client.tabID == 10)
						redStones[3].drawSprite(102 + xOffset, 297 + yOffset);
					if (Client.tabID == 11)
						redStones[2].drawSprite(144 + xOffset, 296 + yOffset);
					if (Client.tabID == 12)
						redStones[2].drawSprite(171 + xOffset, 296 + yOffset);
					if (Client.tabID == 13)
						redStones[5].drawSprite(201 + xOffset, 298 + yOffset);
				}

			}
			for (int index = 0; index <= 14; index++) {
				if (Client.tabInterfaceIDs[index] > 0) {
					if (!isOldframeEnabled()) {
						if (index != 13 || (index == 13 && pollActive)) {
							if (index == 8) {//friends tab switching icon between friends and ignore
								if (friendstab)
									sideIcons[index].drawSprite(sideIconCoordinates[index][0] + xOffset, sideIconCoordinates[index][1] + yOffset - 8);
								else
									ignorelisticon.drawSprite(sideIconCoordinates[index][0] + xOffset, sideIconCoordinates[index][1] + yOffset - 8);
								//	sideIcons[index].drawSprite(sideIconCoordinates[index][0] + xOffset, sideIconCoordinates[index][1] + yOffset - 8);
								continue;
							}
							if (index == 2) {//quest tab
								if (questtabselected == 1)
									firstquesttab.drawSprite(sideIconCoordinates[index][0] + xOffset, sideIconCoordinates[index][1] + yOffset - 8);
								else if (questtabselected == 2)
									secondquesttab.drawSprite(sideIconCoordinates[index][0] + xOffset, sideIconCoordinates[index][1] + yOffset - 8);
								else if (questtabselected == 3)
									thirdquesttab.drawSprite(sideIconCoordinates[index][0] + xOffset, sideIconCoordinates[index][1] + yOffset - 8);
								else if (questtabselected == 4)
									fourthquesttab.drawSprite(sideIconCoordinates[index][0] + xOffset, sideIconCoordinates[index][1] + yOffset - 8);

								//	ignorelisticon.drawSprite(sideIconCoordinates[index][0] + xOffset, sideIconCoordinates[index][1] + yOffset - 8);
								//	sideIcons[index].drawSprite(sideIconCoordinates[index][0] + xOffset, sideIconCoordinates[index][1] + yOffset - 8);
								continue;
							}
							if (index == 5) {
								if (cursesicon)
									cursesidebaricon.drawSprite(sideIconCoordinates[index][0] + xOffset, sideIconCoordinates[index][1] + yOffset - 8);
								else
									sideIcons[index].drawSprite(sideIconCoordinates[index][0] + xOffset, sideIconCoordinates[index][1] + yOffset - 8);
								continue;

							}
//							if (index == 6) {//book tab
//								if (spellbookicon == 0)
//									standardspellbook.drawSprite(sideIconCoordinates[index][0] + xOffset, sideIconCoordinates[index][1] + yOffset - 8);
//								else if (spellbookicon == 1)
//									ancientspellbook.drawSprite(sideIconCoordinates[index][0] + xOffset, sideIconCoordinates[index][1] + yOffset - 8);
//								else if (spellbookicon == 2)
//									lunarspellbook.drawSprite(sideIconCoordinates[index][0] + xOffset, sideIconCoordinates[index][1] + yOffset - 8);
//								continue;
//							}
							if (index == 9) {
								if (flashplayerpanel) {

									if (update_tick % 40 < 20)
										sideIcons[index].drawSprite(sideIconCoordinates[index][0] + xOffset, sideIconCoordinates[index][1] + yOffset - 8);
									continue;
								}

								//sideIcons[i].drawSprite1(24, 280, 200 + (int) (50 * Math.sin(update_tick / 15.0)));
							}
							sideIcons[index].drawSprite(sideIconCoordinates[index][0] + xOffset, sideIconCoordinates[index][1] + yOffset - 8);
						} else {
							sideIcons[index + 1].drawSprite(sideIconCoordinates[index][0] + xOffset, sideIconCoordinates[index][1] + yOffset - 8);
						}
					} else {
						sideIcons[index].drawSprite(sideIconCoordinates1[index][0] + xOffset, sideIconCoordinates1[index][1] + yOffset);
					}
				}
			}
		} else {
			final int[][] sideIconOffsets = new int[][]{{7, 8}, {4, 6}, {6, 7}, {3, 4}, {3, 2}, {4, 3},
					{4, 6}, {5, 5}, {5, 6}, {5, 6}, {6, 3}, {5, 5}, {6, 4}, {5, 5}};
			int x = Client.canvasWidth - (stackTabs() ? 231 : 462);
			int y = Client.canvasHeight - (stackTabs() ? 73 : 37);
			for (int tabIndex = 0; tabIndex < 14; tabIndex++) {
				if (Client.tabID == tabIndex) {
					redStones[4].drawSprite(x, y);
				}
				if (stackTabs()) {
					if (tabIndex != 6) {
						x += 33;
					} else if (tabIndex == 6) {
						y += 36;
						x = Client.canvasWidth - 231;
					}
				} else {
					x += 33;
				}
			}
			x = Client.canvasWidth - (stackTabs() ? 231 : 462);
			y = Client.canvasHeight - (stackTabs() ? 73 : 37);
			for (int index = 0; index < 14; index++) {
				if (Client.tabInterfaceIDs[index] != -1) {
//correct way of doing it on resizable
					if (index == 8) {//friends tab switching icon between friends and ignore
						if (friendstab)
							sideIcons[index].drawSprite(x + sideIconOffsets[index][0], y + sideIconOffsets[index][1]);
						else
							ignorelisticon.drawSprite(x + sideIconOffsets[index][0], y + sideIconOffsets[index][1]);

						//continue;
					} else if (index == 2) {//quest tab
						if (questtabselected == 1)
							firstquesttab.drawSprite(x + sideIconOffsets[index][0], y + sideIconOffsets[index][1]);
						else if (questtabselected == 2)
							secondquesttab.drawSprite(x + sideIconOffsets[index][0], y + sideIconOffsets[index][1]);
						else if (questtabselected == 3)
							thirdquesttab.drawSprite(x + sideIconOffsets[index][0], y + sideIconOffsets[index][1]);
						else if (questtabselected == 4)
							fourthquesttab.drawSprite(x + sideIconOffsets[index][0], y + sideIconOffsets[index][1]);


					} else if (index == 9) {
						if (flashplayerpanel) {

							if (update_tick % 40 < 20)
								sideIcons[index].drawSprite(x + sideIconOffsets[index][0], y + sideIconOffsets[index][1]);
							//sideIcons[index].drawSprite1(sideIconCoordinates[index][0] + xOffset, sideIconCoordinates[index][1] + yOffset - 8, 10 + (int) (50 * Math.sin(update_tick / 5.0)));//fast flash
							//continue;
						} else {
							sideIcons[index].drawSprite(x + sideIconOffsets[index][0], y + sideIconOffsets[index][1]);
						}

						//sideIcons[i].drawSprite1(24, 280, 200 + (int) (50 * Math.sin(update_tick / 15.0)));
					} else if (index == 5) {
						if (cursesicon)
							cursesidebaricon.drawSprite(x + sideIconOffsets[index][0], y + sideIconOffsets[index][1]);
						else
							sideIcons[index].drawSprite(x + sideIconOffsets[index][0], y + sideIconOffsets[index][1]);
						//continue;

					} else {

						sideIcons[index].drawSprite(x + sideIconOffsets[index][0], y + sideIconOffsets[index][1]);
					}
					//sideIcons[index].drawSprite(x + sideIconOffsets[index][0], y + sideIconOffsets[index][1]);
				}
				if (stackTabs()) {
					if (index != 6) {
						x += 33;
					} else if (index == 6) {
						y += 36;
						x = Client.canvasWidth - 231;
					}
				} else {
					x += 33;
				}
			}
		}
	}

	public void processMobChatText() {
		for (int i = -1; i < playerCount; i++) {
			int j;
			if (i == -1)
				j = maxPlayerCount;
			else
				j = highres_player_list[i];
			Player player = players[j];
			if (player != null && player.textCycle > 0) {
				player.textCycle--;
				if (player.textCycle == 0)
					player.textSpoken = null;
			}
		}
		for (int k = 0; k < npcCount; k++) {
			int l = highres_npc_list[k];
			Npc npc = npcs[l];
			if (npc != null && npc.textCycle > 0) {
				npc.textCycle--;
				if (npc.textCycle == 0)
					npc.textSpoken = null;
			}
		}
	}

	public void calcCameraPos() {
		int i = x * 128 + 64;
		int j = y * 128 + 64;
		int k = getCenterHeight(plane, j, i) - height;
		if (xCameraPos < i) {
			xCameraPos += anInt1101 + ((i - xCameraPos) * angle) / 1000;
			if (xCameraPos > i)
				xCameraPos = i;
		}
		if (xCameraPos > i) {
			xCameraPos -= anInt1101 + ((xCameraPos - i) * angle) / 1000;
			if (xCameraPos < i)
				xCameraPos = i;
		}
		if (zCameraPos < k) {
			zCameraPos += anInt1101 + ((k - zCameraPos) * angle) / 1000;
			if (zCameraPos > k)
				zCameraPos = k;
		}
		if (zCameraPos > k) {
			zCameraPos -= anInt1101 + ((zCameraPos - k) * angle) / 1000;
			if (zCameraPos < k)
				zCameraPos = k;
		}
		if (yCameraPos < j) {
			yCameraPos += anInt1101 + ((j - yCameraPos) * angle) / 1000;
			if (yCameraPos > j)
				yCameraPos = j;
		}
		if (yCameraPos > j) {
			yCameraPos -= anInt1101 + ((yCameraPos - j) * angle) / 1000;
			if (yCameraPos < j)
				yCameraPos = j;
		}
		i = cinematicCamXViewpointLoc * 128 + 64;
		j = cinematicCamYViewpointLoc * 128 + 64;
		k = getCenterHeight(plane, j, i) - cinematicCamZViewpointLoc;
		int l = i - xCameraPos;
		int i1 = k - zCameraPos;
		int j1 = j - yCameraPos;
		int k1 = (int) Math.sqrt(l * l + j1 * j1);
		int l1 = (int) (Math.atan2(i1, k1) * 325.94900000000001D) & 0x7ff;
		int i2 = (int) (Math.atan2(l, j1) * -325.94900000000001D) & 0x7ff;
		if (l1 < 128)
			l1 = 128;
		if (l1 > 383)
			l1 = 383;
		if (yCameraCurve < l1) {
			yCameraCurve += anInt998 + ((l1 - yCameraCurve) * anInt999) / 1000;
			if (yCameraCurve > l1)
				yCameraCurve = l1;
		}
		if (yCameraCurve > l1) {
			yCameraCurve -= anInt998 + ((yCameraCurve - l1) * anInt999) / 1000;
			if (yCameraCurve < l1)
				yCameraCurve = l1;
		}
		int j2 = i2 - xCameraCurve;
		if (j2 > 1024)
			j2 -= 2048;
		if (j2 < -1024)
			j2 += 2048;
		if (j2 > 0) {
			xCameraCurve += anInt998 + (j2 * anInt999) / 1000;
			xCameraCurve &= 0x7ff;
		}
		if (j2 < 0) {
			xCameraCurve -= anInt998 + (-j2 * anInt999) / 1000;
			xCameraCurve &= 0x7ff;
		}
		int k2 = i2 - xCameraCurve;
		if (k2 > 1024)
			k2 -= 2048;
		if (k2 < -1024)
			k2 += 2048;
		if (k2 < 0 && j2 > 0 || k2 > 0 && j2 < 0)
			xCameraCurve = i2;
	}

	private void drawMenu(int xOffSet, int yOffSet) {
		int xPos = menuOffsetX - (xOffSet);
		int yPos = (-yOffSet) + menuOffsetY;
		int menuW = menuWidth;
		int menuH = menuHeight + 1;
		needDrawTabArea = true;
		inputTaken = true;
		tabAreaAltered = true;
		Rasterizer2D.drawBox(xPos, yPos, menuW, menuH, 0x5d5447);
		Rasterizer2D.drawBox(xPos + 1, yPos + 1, menuW - 2, 16, 0);
		Rasterizer2D.drawBoxOutline(xPos + 1, yPos + 18, menuW - 2, menuH - 19, 0);
		newBoldFont.drawBasicString("Choose Option", xPos + 3, yPos + 14, 0x5d5447, 0x000000);
		int mouseX = MouseHandler.mouseX - (xOffSet);
		int mouseY = (-yOffSet) + MouseHandler.mouseY;
		for (int l1 = 0; l1 < menuActionRow; l1++) {
			int textY = yPos + 31 + (menuActionRow - 1 - l1) * 15;
			int disColor = 0xffffff;
			if (mouseX > xPos && mouseX < xPos + menuW && mouseY > textY - 13 && mouseY < textY + 3) {
				disColor = 0xffff00;
			}
			newBoldFont.drawBasicString(menuActionName[l1], xPos + 3, textY, disColor, 0x000000);
		}
	}

	private void addFriend(long l) {
		try {
			if (l == 0L)
				return;

			String username = StringUtils.fixName(StringUtils.nameForLong(l));

			if (username.equalsIgnoreCase(localPlayer.displayName)) {
				System.err.println("blocked from adding self as friend..");
				return;
			}
			if (friendsCount >= 100 && anInt1046 != 1) {
				pushMessage("Your friendlist is full.", 0, "");
				return;
			}
			if (friendsCount >= 200) {
				pushMessage("Your friendlist is full.", 0, "");
				return;
			}

			for (int i = 0; i < friendsCount; i++)
				if (friendsListAsLongs[i] == l) {

					return;
				}
			for (int j = 0; j < ignoreCount; j++)
				if (ignoreListAsLongs[j] == l) {
					pushMessage("Please remove " + username + " from your ignore list first", 0, "");
					return;
				}

			if (username.equals(localPlayer.displayName)) {
				return;
			} else {
				//friendsList[friendsCount] = s;
				//friendsListAsLongs[friendsCount] = l;
				//friendsNodeIDs[friendsCount] = 0;
				//friendsCount++;
				//needDrawTabArea = true;
				stream.createFrame(188);
				stream.writeQWord(l);
				return;
			}
		} catch (RuntimeException runtimeexception) {
			Signlink.reporterror("15283, " + (byte) 68 + ", " + l + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	private int getCenterHeight(int i, int j, int k) {
		int l = k >> 7;
		int i1 = j >> 7;
		if (l < 0 || i1 < 0 || l > 103 || i1 > 103)
			return 0;
		int j1 = i;
		if (j1 < 3 && (tileFlags[1][l][i1] & 2) == 2)
			j1++;
		int k1 = k & 0x7f;
		int l1 = j & 0x7f;
		int i2 = tileHeights[j1][l][i1] * (128 - k1) + tileHeights[j1][l + 1][i1] * k1 >> 7;
		int j2 = tileHeights[j1][l][i1 + 1] * (128 - k1) + tileHeights[j1][l + 1][i1 + 1] * k1 >> 7;
		return i2 * (128 - l1) + j2 * l1 >> 7;
	}

	public void resetLogout() {
		setGameState(GameState.LOGIN_SCREEN);
		logger.debug("being logged out.. from: "+new Throwable().getStackTrace()[1].toString());
		logger.debug("Logging out..");
		loggedIn = false;
		prayClicked = false;
		loginScreenState = LoginScreenState.LOGIN;
		captcha = null;
		captchaInput = "";
		frameMode(false);
		firstLoginMessage = "";
		try {
			if (socketStream != null)
				socketStream.close();
		} catch (Exception _ex) {
		}
		socketStream = null;
		PacketLog.clear();
		if (entityTarget != null) {
			entityTarget.stop();
		}
		// myUsername = "";
		// myPassword = "";
		unlinkMRUNodes();
		scene.initToNull();
		for (int i = 0; i < 4; i++)
			collisionMaps[i].setDefault();
		System.gc();
		stopMidi();
		currentSong = -1;
		BroadcastManager.broadcasts = new Broadcast[1000];
		nextSong = -1;
		prevSong = 0;
		experienceCounter = 0;
		GameTimerHandler.getSingleton().stopAll();
		Preferences.save();

	}

	public void method45() {
		aBoolean1031 = true;
		for (int j = 0; j < 7; j++) {
			anIntArray1065[j] = -1;
			for (int k = 0; k < IDK.length; k++) {
				if (IDK.cache[k].nonSelectable || IDK.cache[k].bodyPartId != j + (aBoolean1047 ? 0 : 7))
					continue;
				anIntArray1065[j] = k;
				break;
			}
		}
	}

	private void update_low_resolution_npcs(int i, Buffer stream) {
		while (stream.bitPosition + 21 < i * 8) {
			int npc_index = stream.readBits(14);
			if (npc_index == 0x3fff)
				break;
			if (npcs[npc_index] == null)
				npcs[npc_index] = new Npc(); // Teleports works fine if we create a new npc?
			Npc npc = npcs[npc_index];
			highres_npc_list[npcCount++] = npc_index;
			npc.last_update_tick = update_tick;
			int diff_y = stream.readBits(5);
			if (diff_y > 15)
				diff_y -= 32;
			int diff_x = stream.readBits(5);
			if (diff_x > 15)
				diff_x -= 32;
			int has_teleport_update = stream.readBits(1);
			int npcId = stream.readBits(16);
			npc.npcPetType = stream.readBits(2);
			npc.desc = NpcDefinition.lookup(npcId);
			int k1 = stream.readBits(1);
			if (k1 == 1)
				entity_extended_info_list[anInt893++] = npc_index;
			npc.size = npc.desc.size;
			npc.turnspeed = npc.desc.turnspeed;
			npc.walkanim = npc.desc.walkanim;
			npc.walkanim_b = npc.desc.walkanim_b;
			npc.walkanim_l = npc.desc.walkanim_l;
			npc.walkanim_r = npc.desc.walkanim_r;
			npc.readyanim = npc.desc.readyanim;
			npc.readyanim_l = npc.desc.readyanim_l;
			npc.readyanim_r = npc.desc.readyanim_r;
			npc.runanim = npc.desc.runanim;
			npc.runanim_b = npc.desc.runanim_b;
			npc.runanim_r = npc.desc.runanim_r;
			npc.runanim_l = npc.desc.runanim_l;
			npc.crawlanim = npc.desc.crawlanim;
			npc.crawlanim_b = npc.desc.crawlanim_b;
			npc.crawlanim_l = npc.desc.crawlanim_l;
			npc.crawlanim_r = npc.desc.crawlanim_r;

			npc.face_x = 0;
			npc.face_z = 0;
			npc.teleport(localPlayer.waypoint_x[0] + diff_x, localPlayer.waypoint_z[0] + diff_y, has_teleport_update == 1);
		}
		stream.finishBitAccess();
	}

	@Override
	public void processGameLoop() {
		getCallbacks().tick();
		getCallbacks().post(ClientTick.INSTANCE);


		if (rsAlreadyLoaded || loadingError || genericLoadingError)
			return;
		update_tick++;

		if (!loggedIn)
			processLoginScreenInput();
		else
			mainGameProcessor();

		processOnDemandQueue();
		// method49();
		// handleSounds();
	}

	public void method47(boolean flag) {
		if (localPlayer.x >> 7 == destX && localPlayer.z >> 7 == destY)
			destX = 0;
		int j = playerCount;
		if (flag)
			j = 1;
		for (int l = 0; l < j; l++) {
			Player player;
			long i1;
			if (flag) {
				player = localPlayer;
				i1 = (long) maxPlayerCount << 32;
			} else {
				player = players[highres_player_list[l]];
				i1 = (long) highres_player_list[l] << 32;
			}
			if (player == null || !player.isVisible())
				continue;
			player.lowmem = (lowMem && playerCount > 50 || playerCount > 200) && !flag
					&& player.secondaryanim == player.readyanim;
			int j1 = player.x >> 7;
			int k1 = player.z >> 7;
			if (j1 < 0 || j1 >= 104 || k1 < 0 || k1 >= 104)
				continue;
			if (player.mergeloc_model != null && update_tick >= player.mergelocanim_loop_start && update_tick < player.mergelocanim_loop_stop) {
				player.lowmem = false;
				player.y = getCenterHeight(plane, player.z, player.x);
				scene.addToScenePlayerAsObject(plane, player.z, player, player.target_direction, player.max_z, player.x,
						player.y, player.min_x, player.max_x, i1, player.min_z);
				continue;
			}
			if ((player.x & 0x7f) == 64 && (player.z & 0x7f) == 64) {
				if (anIntArrayArray929[j1][k1] == anInt1265)
					continue;
				anIntArrayArray929[j1][k1] = anInt1265;
			}
			player.y = getCenterHeight(plane, player.z, player.x);
			scene.addAnimableA(plane, player.target_direction, player.y, i1, player.z, 60, player.x, player,
					player.is_walking);
		}
	}

	private boolean promptUserForInput(RSInterface class9) {
		int j = class9.contentType;
		if (anInt900 == 2) {
			if (j == 201) {
				inputTaken = true;
				inputDialogState = 0;
				messagePromptRaised = true;
				promptInput = "";
				friendsListAction = 1;
				aString1121 = "Enter name of friend to add to list";
			}
			if (j == 202) {
				inputTaken = true;
				inputDialogState = 0;
				messagePromptRaised = true;
				promptInput = "";
				friendsListAction = 2;
				aString1121 = "Enter name of friend to delete from list";
			}
		}
		if (j == 205) {
			anInt1011 = 250;
			return true;
		}
		if (j == 501) {
			inputTaken = true;
			inputDialogState = 0;
			messagePromptRaised = true;
			promptInput = "";
			friendsListAction = 4;
			aString1121 = "Enter name of player to add to list";
		}
		if (j == 502) {
			inputTaken = true;
			inputDialogState = 0;
			messagePromptRaised = true;
			promptInput = "";
			friendsListAction = 5;
			aString1121 = "Enter name of player to delete from list";
		}
		if (j == 550) {
			inputTaken = true;
			inputDialogState = 0;
			messagePromptRaised = true;
			promptInput = "";
			friendsListAction = 6;
			aString1121 = "Enter the name of the chat you wish to join";
		}
		if (j >= 300 && j <= 313) {
			int k = (j - 300) / 2;
			int j1 = j & 1;
			int i2 = anIntArray1065[k];
			if (i2 != -1) {
				do {
					if (j1 == 0 && --i2 < 0)
						i2 = IDK.length - 1;
					if (j1 == 1 && ++i2 >= IDK.length)
						i2 = 0;
				} while (IDK.cache[i2].nonSelectable || IDK.cache[i2].bodyPartId != k + (aBoolean1047 ? 0 : 7));
				anIntArray1065[k] = i2;
				aBoolean1031 = true;
			}
		}
		if (j >= 314 && j <= 323) {
			int l = (j - 314) / 2;
			int k1 = j & 1;
			int j2 = anIntArray990[l];
			if (k1 == 0 && --j2 < 0)
				j2 = player_body_colours[l].length - 1;
			if (k1 == 1 && ++j2 >= player_body_colours[l].length)
				j2 = 0;
			anIntArray990[l] = j2;
			aBoolean1031 = true;
		}
		if (j == 324 && !aBoolean1047) {
			aBoolean1047 = true;
			method45();
		}
		if (j == 325 && aBoolean1047) {
			aBoolean1047 = false;
			method45();
		}
		if (j == 326) {
			stream.createFrame(101);
			stream.writeUnsignedByte(aBoolean1047 ? 0 : 1);
			for (int i1 = 0; i1 < 7; i1++)
				stream.writeUnsignedByte(anIntArray1065[i1]);

			for (int l1 = 0; l1 < 5; l1++)
				stream.writeUnsignedByte(anIntArray990[l1]);

			return true;
		}
		if (j == 613)
			canMute = !canMute;
		if (j >= 601 && j <= 612) {
			clearTopInterfaces();
			if (reportAbuseInput.length() > 0) {
				stream.createFrame(218);
				stream.writeQWord(StringUtils.longForName(reportAbuseInput));
				stream.writeUnsignedByte(j - 601);
				stream.writeUnsignedByte(canMute ? 1 : 0);
			}
		}
		return false;
	}

	public void update_player_info(Buffer stream) {
		for (int j = 0; j < anInt893; j++) {
			int k = entity_extended_info_list[j];
			Player player = players[k];
			int l = stream.get_unsignedbyte();
			if ((l & 0x40) != 0)
				l += stream.get_unsignedbyte() << 8;
			decode_playerinfo_masks(l, k, stream, player);
		}
	}

	public void drawMapScenes(int i, int k, int l, int i1, int j1) {
		long id = scene.getWallObjectUid(j1, l, i);
		if (id != 0) {
			int k2 = ObjectKeyUtil.getObjectOrientation(id);
			int i3 = ObjectKeyUtil.getObjectType(id);
			int k3 = k;
			if (id > 0) {
				k3 = i1;
			}
			int ai[] = minimapImage.myPixels;
			int k4 = 24624 + l * 4 + (103 - i) * 512 * 4;
			int i5 = ObjectKeyUtil.getObjectId(id);
			ObjectDefinition class46_2 = ObjectDefinition.lookup(i5);
			if (class46_2.mapscene != -1) {
				IndexedImage background_2 = mapScenes[class46_2.mapscene];
				if (background_2 != null) {
					int i6 = (class46_2.length * 4 - background_2.subWidth) / 2;
					int j6 = (class46_2.width * 4 - background_2.subHeight) / 2;
					background_2.draw(48 + l * 4 + i6, 48 + (104 - i - class46_2.width) * 4 + j6);
				}
			} else {
				if (i3 == 0 || i3 == 2)
					if (k2 == 0) {
						ai[k4] = k3;
						ai[k4 + 512] = k3;
						ai[k4 + 1024] = k3;
						ai[k4 + 1536] = k3;
					} else if (k2 == 1) {
						ai[k4] = k3;
						ai[k4 + 1] = k3;
						ai[k4 + 2] = k3;
						ai[k4 + 3] = k3;
					} else if (k2 == 2) {
						ai[k4 + 3] = k3;
						ai[k4 + 3 + 512] = k3;
						ai[k4 + 3 + 1024] = k3;
						ai[k4 + 3 + 1536] = k3;
					} else if (k2 == 3) {
						ai[k4 + 1536] = k3;
						ai[k4 + 1536 + 1] = k3;
						ai[k4 + 1536 + 2] = k3;
						ai[k4 + 1536 + 3] = k3;
					}
				if (i3 == 3)
					if (k2 == 0)
						ai[k4] = k3;
					else if (k2 == 1)
						ai[k4 + 3] = k3;
					else if (k2 == 2)
						ai[k4 + 3 + 1536] = k3;
					else if (k2 == 3)
						ai[k4 + 1536] = k3;
				if (i3 == 2)
					if (k2 == 3) {
						ai[k4] = k3;
						ai[k4 + 512] = k3;
						ai[k4 + 1024] = k3;
						ai[k4 + 1536] = k3;
					} else if (k2 == 0) {
						ai[k4] = k3;
						ai[k4 + 1] = k3;
						ai[k4 + 2] = k3;
						ai[k4 + 3] = k3;
					} else if (k2 == 1) {
						ai[k4 + 3] = k3;
						ai[k4 + 3 + 512] = k3;
						ai[k4 + 3 + 1024] = k3;
						ai[k4 + 3 + 1536] = k3;
					} else if (k2 == 2) {
						ai[k4 + 1536] = k3;
						ai[k4 + 1536 + 1] = k3;
						ai[k4 + 1536 + 2] = k3;
						ai[k4 + 1536 + 3] = k3;
					}
			}
		}
		id = scene.getGameObjectUid(j1, l, i);
		if (id != 0) {
			int l2 = ObjectKeyUtil.getObjectOrientation(id);
			int j3 = ObjectKeyUtil.getObjectType(id);

			int l3 = ObjectKeyUtil.getObjectId(id);
			ObjectDefinition class46_1 = ObjectDefinition.lookup(l3);
			if (class46_1.mapscene != -1) {
				if (class46_1.mapscene < mapScenes.length) {
					IndexedImage background_1 = mapScenes[class46_1.mapscene];
					if (background_1 != null) {
						int j5 = (class46_1.length * 4 - background_1.subWidth) / 2;
						int k5 = (class46_1.width * 4 - background_1.subHeight) / 2;
						background_1.draw(48 + l * 4 + j5, 48 + (104 - i - class46_1.width) * 4 + k5);
					}
				} else if (Configuration.developerMode) {
					System.err.println("Missing map scene: " + class46_1.mapscene);
				}
			} else if (j3 == 9) {
				int l4 = 0xeeeeee;
				if (id > 0)
					l4 = 0xee0000;
				int ai1[] = minimapImage.myPixels;
				int l5 = 24624 + l * 4 + (103 - i) * 512 * 4;
				if (l2 == 0 || l2 == 2) {
					ai1[l5 + 1536] = l4;
					ai1[l5 + 1024 + 1] = l4;
					ai1[l5 + 512 + 2] = l4;
					ai1[l5 + 3] = l4;
				} else {
					ai1[l5] = l4;
					ai1[l5 + 512 + 1] = l4;
					ai1[l5 + 1024 + 2] = l4;
					ai1[l5 + 1536 + 3] = l4;
				}
			}
		}
		id = scene.getGroundDecorationUid(j1, l, i);
		if (id != 0) {
			int j2 = ObjectKeyUtil.getObjectId(id);
			ObjectDefinition class46 = ObjectDefinition.lookup(j2);
			if (class46.mapscene != -1) {
				IndexedImage background = mapScenes[class46.mapscene];
				if (background != null) {
					int i4 = (class46.length * 4 - background.subWidth) / 2;
					int j4 = (class46.width * 4 - background.subHeight) / 2;
					background.draw(48 + l * 4 + i4, 48 + (104 - i - class46.width) * 4 + j4);
				}
			}
		}
	}

	private Sprite aSprite_1201;
	private Sprite aSprite_1202;
	private IndexedImage titleBoxIndexedImage;
	private void loadTitleScreen() {
		if (titleBoxIndexedImage == null) {
			titleBoxIndexedImage = new IndexedImage(titleStreamLoader, "titlebox", 0);
		}
		if (titleButtonIndexedImage == null) {
			titleButtonIndexedImage = new IndexedImage(titleStreamLoader, "titlebutton", 0);
		}
		if (titleButton == null) {
			titleButton = new IndexedImage(titleStreamLoader, "titlebutton", 0);
		}
		if (logologin == null) {
			logologin = new IndexedImage(titleStreamLoader, "logo", 0);
		}
		//titleButtonIndexedImage = new IndexedImage(titleStreamLoader, "titlebutton", 0);
		//titleBoxIndexedImage = new IndexedImage(titleStreamLoader, "titlebox", 0);
//		if (loginHover == null) {
//			loginHover = new Sprite(titleStreamLoader, "titlebutton", 1);
//		}
//		aBackgroundArray1152s = new Background[12];
//		int j = 0;
//		try {
//			j = Integer.parseInt(getParameter("fl_icon"));
//		} catch (Exception _ex) {
//		}
//		if (j == 0) {
//			for (int k = 0; k < 12; k++)
//				aBackgroundArray1152s[k] = new Background(titleStreamLoader, "runes", k);
//
//		} else {
//			for (int l = 0; l < 12; l++)
//				aBackgroundArray1152s[l] = new Background(titleStreamLoader, "runes", 12 + (l & 3));
//
//		}
//		aSprite_1201 = new Sprite(128, 265);
//		aSprite_1202 = new Sprite(128, 265);
		// aSprite_1201 = new Sprite(0, 266);
		// aSprite_1202 = new Sprite(0, 266);
		// System.arraycopy(leftSideFlame.canvasRaster, 0, aSprite_1201.myPixels, 0,
		// 33920);

		// System.arraycopy(rightSideFlame.canvasRaster, 0, aSprite_1202.myPixels, 0,
		// 33920);

//		anIntArray851 = new int[256];
//		for (int k1 = 0; k1 < 64; k1++)
//			anIntArray851[k1] = k1 * 0x40000;
//
//		for (int l1 = 0; l1 < 64; l1++)
//			anIntArray851[l1 + 64] = 0xff0000 + 1024 * l1;
//
//		for (int i2 = 0; i2 < 64; i2++)
//			anIntArray851[i2 + 128] = 0xffff00 + 4 * i2;
//
//		for (int j2 = 0; j2 < 64; j2++)
//			anIntArray851[j2 + 192] = 0xffffff;
//
//		anIntArray852 = new int[256];
//		for (int k2 = 0; k2 < 64; k2++)
//			anIntArray852[k2] = k2 * 1024;
//
//		for (int l2 = 0; l2 < 64; l2++)
//			anIntArray852[l2 + 64] = 65280 + 4 * l2;
//
//		for (int i3 = 0; i3 < 64; i3++)
//			anIntArray852[i3 + 128] = 65535 + 0x40000 * i3;
//
//		for (int j3 = 0; j3 < 64; j3++)
//			anIntArray852[j3 + 192] = 0xffffff;
//
//		anIntArray853 = new int[256];
//		for (int k3 = 0; k3 < 64; k3++)
//			anIntArray853[k3] = k3 * 4;
//
//		for (int l3 = 0; l3 < 64; l3++)
//			anIntArray853[l3 + 64] = 255 + 0x40000 * l3;
//
//		for (int i4 = 0; i4 < 64; i4++)
//			anIntArray853[i4 + 128] = 0xff00ff + 1024 * i4;
//
//		for (int j4 = 0; j4 < 64; j4++)
//			anIntArray853[j4 + 192] = 0xffffff;
//
//		anIntArray850 = new int[256];
//		anIntArray1190 = new int[32768];
//		anIntArray1191 = new int[32768];
//		randomizeBackground(null);
//		anIntArray828 = new int[32768];
//		anIntArray829 = new int[32768];
//		if (!aBoolean831) {
//			drawFlames = true;
//			aBoolean831 = true;
//			startRunnable(this, 2);
//		}
	}

	private static void setHighMem() {
		SceneGraph.lowMem = false;
		// Rasterizer.lowMem = false;
		lowMem = false;
		MapRegion.lowMem = false;
		ObjectDefinition.lowMem = false;
	}

	public static final String[] SKILL_NAME = { "Attack", "Defence", "Strength", "Hitpoints", "Ranged", "Prayer",
			"Magic", "Cooking", "Woodcutting", "Fletching", "Fishing", "Firemaking", "Crafting", "Smithing", "Mining",
			"Herblore", "Agility", "Thieving", "Slayer", "Farming", "Runecrafting", "Hunter", "Construction",
			"Summoning" };

	public static int getXPForLevel(int level) {
		int points = 0;
		int output = 0;
		for (int lvl = 1; lvl <= level; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			if (lvl >= level) {
				return output;
			}
			output = (int) Math.floor(points / 4);
		}
		return 0;
	}

	/**
	 * An array containing all the player's experience.
	 */
	public static double[] experience;
	public static int totalExperience;

	public static int getStaticLevelByExperience(int slot) {
		double exp = experience[slot];

		int points = 0;
		int output = 0;
		for (byte lvl = 1; lvl < 100; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if ((output - 1) >= exp) {
				return lvl;
			}
		}
		return 99;
	}

	private static String[] args = null;
	public static Client instance;
	public static boolean runelite;

	private static int getVersion() {
		String version = System.getProperty("java.version");
		if(version.startsWith("1.")) {
			version = version.substring(2, 3);
		} else {
			int dot = version.indexOf(".");
			if(dot != -1) { version = version.substring(0, dot); }
		} return Integer.parseInt(version);
	}


// bro its still buildding
	public static JFrame appFrame = null;
	public static Container gameContainer = null;

	private static String getErrorLogDirectory() {
		return Signlink.getCacheDirectory() + Configuration.ERROR_LOG_DIRECTORY;
	}

	private static void enableExceptionLogging() throws IOException {
		if (!new File(getErrorLogDirectory()).exists()) {
			Preconditions.checkState(new File(getErrorLogDirectory()).mkdirs());
		}

		TeeOutputStream outputStream = new TeeOutputStream(System.err, new FileOutputStream(getErrorLogDirectory() + Configuration.ERROR_LOG_FILE, true));
		System.setErr(new PrintStream(outputStream));
	}

	private static void setLoggingLevel(ch.qos.logback.classic.Level level) {
		ch.qos.logback.classic.Logger root = (ch.qos.logback.classic.Logger)LoggerFactory.getLogger(org.slf4j.Logger.ROOT_LOGGER_NAME);
		root.setLevel(level);
	}

	public static Client getClient(boolean runelite, String...args) {
		try {
//			try {
//				ClientDiscordRPC.initialize();
//			} catch (Exception e) {
//				System.err.println("Failed to establish Discord RPC.");
//				e.printStackTrace();
//			}

			System.out.println("Running Java version " + getVersion());
			Client.runelite = runelite;
			Client.args = args;
			StringBuilder windowTitleBuilder = new StringBuilder();
			windowTitleBuilder.append(Configuration.CLIENT_TITLE);
			server = Configuration.DEDICATED_SERVER_ADDRESS;
			setLoggingLevel(Level.INFO);

			if (args.length > 0) {
				StringBuilder configurationBuilder = new StringBuilder();
				configurationBuilder.append("[");

				for (int index = 0; index < args.length; index++) {
					configurationBuilder.append(args[index].replace("--", ""));
					if (index != args.length - 1)
						configurationBuilder.append(" ");

					if (args[index].startsWith("server=")) {
						server = args[index].replace("server=", "");
						System.out.println("Custom connection address set through run arguments: " + server);
					} else {
						switch (args[index]) {
							case "--debug":
							case "-db":
								setLoggingLevel(Level.DEBUG);
								System.out.println("Setting log level to debug.");
								break;

							case "--developer":
							case "-d":
								Configuration.developerMode = true;
								Configuration.cacheName = Configuration.CACHE_NAME_DEV;
								System.out.println("Developer mode enabled.");
								break;
							case "--local":
							case "-l":
							case "localhost":
							case "local":
								if (server.equals(Configuration.DEDICATED_SERVER_ADDRESS)) {
									server = "127.0.0.1";
									System.out.println("Localhost client enabled.");
								} else {
									throw new IllegalArgumentException("Cannot have custom ip and local enabled.");
								}
								break;
							case "test_server":
								port = Configuration.TEST_PORT;
								System.out.println("Test server enabled.");
								break;

							case "pack_data":
								Configuration.packIndexData = true;
								break;
							case "dump_maps":
								Configuration.dumpMaps = true;
								break;
							case "dump_animation_data":
								Configuration.dumpAnimationData = true;
								break;
							case "dump_data_lists":
								Configuration.dumpDataLists = true;
								break;
							default:
								throw new IllegalArgumentException("Run argument not recognized: " + args[index]);
						}
					}
				}

				// Build the client title with configuration arguments
				configurationBuilder.append("]");
				windowTitleBuilder.append(" ");
				windowTitleBuilder.append(configurationBuilder.toString().trim());
			}

			Signlink.createCacheDirectory();
			enableExceptionLogging(); // Don't remove this!
			Configuration.clientTitle = windowTitleBuilder.toString();
			nodeID = 1;
			portOff = 0;
			setHighMem();
			isMembers = true;
			Signlink.storeid = 32;
			Signlink.startpriv(InetAddress.getLocalHost());

			instance = new Client();

		} catch (Exception exception) {
			exception.printStackTrace();
		}

		return Client.instance;
	}

	public static Client getInstance() {
		return instance;
	}

	public void loadingStages() {
		if (lowMem && loadingStage == 2 && MapRegion.anInt131 != plane) {

			setGameState(GameState.LOADING);

			loadingStage = 1;
			longStartTime = System.currentTimeMillis();
		}
		if (loadingStage == 1) {
			int j = getMapLoadingState();
			if (j != 0 && System.currentTimeMillis() - longStartTime > 0x57e40L) {
				Signlink.reporterror(
						myUsername + " glcfb " + aLong1215 + "," + j + "," + lowMem + "," + decompressors[0] + ","
								+ resourceProvider.getNodeCount() + "," + plane + "," + currentRegionX + "," + currentRegionY);
				longStartTime = System.currentTimeMillis();
			}
		}
		if (loadingStage == 2 && plane != anInt985) {
			anInt985 = plane;
			renderMapScene(plane);
			stream.createFrame(121);
		}
	}

	private int getMapLoadingState() {
		for (int i = 0; i < terrainData.length; i++) {
			if (terrainData[i] == null && terrainIndices[i] != -1)
				return -1;
			if (objectData[i] == null && objectIndices[i] != -1)
				return -2;
		}
		boolean flag = true;
		for (int j = 0; j < terrainData.length; j++) {
			byte abyte0[] = objectData[j];
			if (abyte0 != null) {
				try {
					int k = (mapCoordinates[j] >> 8) * 64 - baseX;
					int l = (mapCoordinates[j] & 0xff) * 64 - baseY;
					if (isDynamicRegion) {
						k = 10;
						l = 10;
					}
					flag &= MapRegion.method189(k, abyte0, l);
				} catch (Exception e) {
					if (objectIndices[j] != -1)
						System.err.println("Error on map file: " + objectIndices[j]);
					e.printStackTrace();
				}
			}
		}
		if (!flag)
			return -3;// couldn't parse all landscapes
		if (loadingMap) {
			return -4;
		} else {
			loadingStage = 2;
			MapRegion.anInt131 = plane;
			loadRegion();
			stream.createFrame(121);
			logger.debug("Map region loaded.");
			return 0;
		}
	}

	public void method55() {
		for (Projectile class30_sub2_sub4_sub4 = (Projectile) projectiles
				.reverseGetFirst(); class30_sub2_sub4_sub4 != null; class30_sub2_sub4_sub4 = (Projectile) projectiles
				.reverseGetNext())
			if (class30_sub2_sub4_sub4.anInt1597 != plane || update_tick > class30_sub2_sub4_sub4.anInt1572)
				class30_sub2_sub4_sub4.unlink();
			else if (update_tick >= class30_sub2_sub4_sub4.anInt1571) {
				if (class30_sub2_sub4_sub4.anInt1590 > 0) {
					Npc npc = npcs[class30_sub2_sub4_sub4.anInt1590 - 1];
					if (npc != null && npc.x >= 0 && npc.x < 13312 && npc.z >= 0 && npc.z < 13312)
						class30_sub2_sub4_sub4.set_destination(update_tick, npc.z,
								getCenterHeight(class30_sub2_sub4_sub4.anInt1597, npc.z, npc.x)
										- class30_sub2_sub4_sub4.anInt1583,
								npc.x);
				}
				if (class30_sub2_sub4_sub4.anInt1590 < 0) {
					int j = -class30_sub2_sub4_sub4.anInt1590 - 1;
					Player player;
					if (j == unknownInt10)
						player = localPlayer;
					else
						player = players[j];
					if (player != null && player.x >= 0 && player.x < 13312 && player.z >= 0 && player.z < 13312)
						class30_sub2_sub4_sub4.set_destination(update_tick, player.z,
								getCenterHeight(class30_sub2_sub4_sub4.anInt1597, player.z, player.x)
										- class30_sub2_sub4_sub4.anInt1583,
								player.x);
				}
				class30_sub2_sub4_sub4.advance(tickDelta);
				scene.addAnimableA(plane, class30_sub2_sub4_sub4.yaw,
						(int) class30_sub2_sub4_sub4.z, -1, (int) class30_sub2_sub4_sub4.y, 60,
						(int) class30_sub2_sub4_sub4.x, class30_sub2_sub4_sub4, false);
			}

	}

	public static String capitalize(String s) {
		for (int i = 0; i < s.length(); i++) {
			if (i == 0) {
				s = String.format("%s%s", Character.toUpperCase(s.charAt(0)), s.substring(1));
			}
			if (!Character.isLetterOrDigit(s.charAt(i))) {
				if (i + 1 < s.length()) {
					s = String.format("%s%s%s", s.subSequence(0, i + 1), Character.toUpperCase(s.charAt(i + 1)),
							s.substring(i + 2));
				}
			}
		}
		return s;
	}

	private void processOnDemandQueue() {
		do {
			OnDemandData onDemandData;
			do {
				onDemandData = resourceProvider.getNextNode();
				if (onDemandData == null)
					return;
				if (onDemandData.dataType == 0) {
					Model.loadModel(onDemandData.buffer, onDemandData.ID);
					needDrawTabArea = true;
					if (backDialogID != -1)
						inputTaken = true;
				}
				//all your npc chat heads being frozen  / not appearing issues are handled here.
				if (onDemandData.dataType == 1 && onDemandData.buffer != null) {
					int magic = (onDemandData.buffer[1] & 0xff) + ((onDemandData.buffer[0] & 0xff) << 8);
				//	System.out.println("magic: "+magic+" and id: "+onDemandData.ID);
					if(magic == 420) {//load skeletal
						AnimKeyFrameSet.load(onDemandData.ID, onDemandData.buffer);
					} else if(magic == 710) {
						AnimFrameSet.load_osrs(onDemandData.ID, onDemandData.buffer);
					} else if (onDemandData.ID == 3366) {
						AnimFrameSet.load_641(onDemandData.ID, onDemandData.buffer);
					}

					//without this the animation file isnt even being loaded
					else if (magic == 6912) {
						AnimFrameSet.load_chatheads(onDemandData.ID, onDemandData.buffer);

					}
					//641 ANIMS GO HERE
				}
				if (onDemandData.dataType == 2 && onDemandData.ID == nextSong && onDemandData.buffer != null)
					saveMidi(songChanging, onDemandData.buffer);
				if (onDemandData.dataType == 3 && loadingStage == 1) {
					for (int i = 0; i < terrainData.length; i++) {
						if (terrainIndices[i] == onDemandData.ID) {
							if (terrainData[i] == null)
								terrainData[i] = onDemandData.buffer;
							if (onDemandData.buffer == null)
								terrainIndices[i] = -1;
							break;
						}
						if (objectIndices[i] != onDemandData.ID)
							continue;
						if (objectData[i] == null)
							objectData[i] = onDemandData.buffer;
						if (onDemandData.buffer == null)
							objectIndices[i] = -1;
						break;
					}

				}
			} while (onDemandData.dataType != 93 || !resourceProvider.method564(onDemandData.ID));
			//MapRegion.passiveRequestGameObjectModels(new Buffer(onDemandData.payload), resourceProvider);
		} while (true);
	}

	public void calcFlamesPosition() {
		char c = '\u0100';
		for (int j = 10; j < 117; j++) {
			int k = (int) (Math.random() * 100D);
			if (k < 50)
				anIntArray828[j + (c - 2 << 7)] = 255;
		}
		for (int l = 0; l < 100; l++) {
			int i1 = (int) (Math.random() * 124D) + 2;
			int k1 = (int) (Math.random() * 128D) + 128;
			int k2 = i1 + (k1 << 7);
			anIntArray828[k2] = 192;
		}

		for (int j1 = 1; j1 < c - 1; j1++) {
			for (int l1 = 1; l1 < 127; l1++) {
				int l2 = l1 + (j1 << 7);
				anIntArray829[l2] = (anIntArray828[l2 - 1] + anIntArray828[l2 + 1] + anIntArray828[l2 - 128]
						+ anIntArray828[l2 + 128]) / 4;
			}

		}

		anInt1275 += 128;
		if (anInt1275 > anIntArray1190.length) {
			anInt1275 -= anIntArray1190.length;
			int i2 = (int) (Math.random() * 12D);
			randomizeBackground(aBackgroundArray1152s[i2]);
		}
		for (int j2 = 1; j2 < c - 1; j2++) {
			for (int i3 = 1; i3 < 127; i3++) {
				int k3 = i3 + (j2 << 7);
				int i4 = anIntArray829[k3 + 128] - anIntArray1190[k3 + anInt1275 & anIntArray1190.length - 1] / 5;
				if (i4 < 0)
					i4 = 0;
				anIntArray828[k3] = i4;
			}

		}

		System.arraycopy(anIntArray969, 1, anIntArray969, 0, c - 1);

		anIntArray969[c - 1] = (int) (Math.sin(update_tick / 14D) * 16D + Math.sin(update_tick / 15D) * 14D
				+ Math.sin(update_tick / 16D) * 12D);
		if (anInt1040 > 0)
			anInt1040 -= 4;
		if (anInt1041 > 0)
			anInt1041 -= 4;
		if (anInt1040 == 0 && anInt1041 == 0) {
			int l3 = (int) (Math.random() * 2000D);
			if (l3 == 0)
				anInt1040 = 1024;
			if (l3 == 1)
				anInt1041 = 1024;
		}
	}

	public void resetAnimation(int i) {
		RSInterface class9 = RSInterface.interfaceCache[i];
		for (int j = 0; j < class9.children.length; j++) {
			if (class9.children[j] == -1)
				break;
			RSInterface class9_1 = RSInterface.interfaceCache[class9.children[j]];
			if (class9_1 == null)
				System.err.println("Null child of index " + j + " inside interface " + i);
			if (class9_1.type == 1)
				resetAnimation(class9_1.id);
			class9_1.iftype_frameindex = 0;
			class9_1.iftype_loops_remaining = 0;
		}
	}

	public void drawHeadIcon() {
		if (hintType != 2)
			return;
		calcEntityScreenPos((hintIconXpos - baseX << 7) + anInt937, hintIconFloorPos * 2, (hintIconYpos - baseY << 7) + anInt938);
		if (spriteDrawX > -1 && update_tick % 20 < 10)
			headIconsHint[0].drawSprite(spriteDrawX - 12, spriteDrawY - 28);
	}

	private GameTimer gameTimer;

	private void mainGameProcessor() {
		callbacks.tick();
		callbacks.post(new ClientTick());

		timeOutHasLoggedMessages();

		if (gameTimer != null) {
			if (gameTimer.isCompleted()) {
				gameTimer.stop();
			}
		}

//		if (openInterfaceID == 63000) {//interesting
//			if (!PetSystem.isPetAnimationRunning) {
//				PetSystem.updateAnimations();
//			}
//		}


		spin();
		if (anInt1104 > 1)
			anInt1104--;
		if (anInt1011 > 0)
			anInt1011--;

		int packetsHandled = 0;
		while (parsePacket()) {
			if (packetsHandled++ >= 10) {
				break;
			}
		}

		if (!loggedIn)
			return;
//		synchronized (mouseDetection.syncObject) {
//			if (flagged) {
//				if (MouseHandler.instance.clickMode3 != 0 || mouseDetection.coordsIndex >= 40) {
//					stream.createFrame(45);
//					stream.writeUnsignedByte(0);
//					int j2 = stream.currentOffset;
//					int j3 = 0;
//					for (int j4 = 0; j4 < mouseDetection.coordsIndex; j4++) {
//						if (j2 - stream.currentOffset >= 240)
//							break;
//						j3++;
//						int l4 = mouseDetection.coordsY[j4];
//						if (l4 < 0)
//							l4 = 0;
//						else if (l4 > 502)
//							l4 = 502;
//						int k5 = mouseDetection.coordsX[j4];
//						if (k5 < 0)
//							k5 = 0;
//						else if (k5 > 764)
//							k5 = 764;
//						int i6 = l4 * 765 + k5;
//						if (mouseDetection.coordsY[j4] == -1 && mouseDetection.coordsX[j4] == -1) {
//							k5 = -1;
//							l4 = -1;
//							i6 = 0x7ffff;
//						}
//						if (k5 == anInt1237 && l4 == anInt1238) {
//							if (anInt1022 < 2047)
//								anInt1022++;
//						} else {
//							int j6 = k5 - anInt1237;
//							anInt1237 = k5;
//							int k6 = l4 - anInt1238;
//							anInt1238 = l4;
//							if (anInt1022 < 8 && j6 >= -32 && j6 <= 31 && k6 >= -32 && k6 <= 31) {
//								j6 += 32;
//								k6 += 32;
//								stream.writeWord((anInt1022 << 12) + (j6 << 6) + k6);
//								anInt1022 = 0;
//							} else if (anInt1022 < 8) {
//								stream.writeDWordBigEndian(0x800000 + (anInt1022 << 19) + i6);
//								anInt1022 = 0;
//							} else {
//								stream.writeDWord(0xc0000000 + (anInt1022 << 19) + i6);
//								anInt1022 = 0;
//							}
//						}
//					}
//
//					stream.writeBytes(stream.currentOffset - j2);
//					if (j3 >= mouseDetection.coordsIndex) {
//						mouseDetection.coordsIndex = 0;
//					} else {
//						mouseDetection.coordsIndex -= j3;
//						for (int i5 = 0; i5 < mouseDetection.coordsIndex; i5++) {
//							mouseDetection.coordsX[i5] = mouseDetection.coordsX[i5 + j3];
//							mouseDetection.coordsY[i5] = mouseDetection.coordsY[i5 + j3];
//						}
//
//					}
//				}
//			} else {
//				mouseDetection.coordsIndex = 0;
//			}
//		}
//		if (MouseHandler.instance.clickMode3 != 0) {
//			long l = (MouseHandler.aLong29 - aLong1220) / 50L;
//			if (l > 4095L)
//				l = 4095L;
//			aLong1220 = MouseHandler.aLong29;
//			int k2 = MouseHandler.saveClickY; // Needs to be changed to absolute due to stretched mode changes
//			if (k2 < 0)
//				k2 = 0;
//			else if (k2 > 502)
//				k2 = 502;
//			int k3 = MouseHandler.saveClickX;
//			if (k3 < 0)
//				k3 = 0;
//			else if (k3 > 764)
//				k3 = 764;
//			int k4 = k2 * 765 + k3;
//			int j5 = 0;
//			if (MouseHandler.instance.clickMode3 == 2)
//				j5 = 1;
//			int l5 = (int) l;
//			stream.createFrame(241);
//			stream.writeDWord((l5 << 20) + (j5 << 19) + k4);
//		}
		if (anInt1016 > 0)
			anInt1016--;
		if (KeyHandler.keyArray[1] == 1 || KeyHandler.keyArray[2] == 1 || KeyHandler.keyArray[3] == 1 || KeyHandler.keyArray[4] == 1)
			aBoolean1017 = true;
		if (aBoolean1017 && anInt1016 <= 0) {
			anInt1016 = 20;
			aBoolean1017 = false;
			stream.createFrame(86);
			stream.writeWord(camAngleYY);
			stream.method432(viewRotation);
		}
		if (super.canvas.hasFocus() && !aBoolean954) {
			aBoolean954 = true;
			stream.createFrame(3);
			stream.writeUnsignedByte(1);
		}
		if (!super.canvas.hasFocus() && aBoolean954) {
			aBoolean954 = false;
			stream.createFrame(3);
			stream.writeUnsignedByte(0);
		}
		loadingStages();
		method115();
		// method90();
		anInt1009++;
		if (anInt1009 > 750) {
			logger.debug("Dropping client due to packets not being received.");
			dropClient();
		}
		update_all_player_movement();
		update_all_npc_movement();
		processMobChatText();
		tickDelta++;
		if (crossType != 0) {
			crossIndex += 20;
			if (crossIndex >= 400)
				crossType = 0;
		}
		if (atInventoryInterfaceType != 0) {
			atInventoryLoopCycle++;
			if (atInventoryLoopCycle >= 15) {
				if (atInventoryInterfaceType == 2)
					needDrawTabArea = true;
				if (atInventoryInterfaceType == 3)
					inputTaken = true;
				atInventoryInterfaceType = 0;
			}
		}
		if (activeInterfaceType != 0) {
			anInt989++;
			if (MouseHandler.mouseX > anInt1087 + 5 || MouseHandler.mouseX < anInt1087 - 5 || MouseHandler.mouseY > anInt1088 + 5
					|| MouseHandler.mouseY < anInt1088 - 5)
				aBoolean1242 = true;
			if (MouseHandler.instance.clickMode2 == 0) {
				if (activeInterfaceType == 2)
					needDrawTabArea = true;
				if (activeInterfaceType == 3)
					inputTaken = true;
				activeInterfaceType = 0;
				if (aBoolean1242 && anInt989 >= getDragSetting(draggingItemInterfaceId)) {
					lastActiveInvInterface = -1;
					processRightClick();

					// Bank interface adding items to another tab via dragging on the tab icons at the top
					if (Bank.isBankContainer(RSInterface.interfaceCache[draggingItemInterfaceId]) || draggingItemInterfaceId == Bank.SEARCH_CONTAINER) {
						//System.out.println("BANK TAB SLOT " + mouseX + ", " + mouseY);
						Point southWest, northEast;



						int xOffset = !isResized() ? 0 : (canvasWidth / 2) - 356;
						int yOffset = !isResized() ? 0: (canvasHeight / 2) - 230;

						southWest = new Point(68 + xOffset, 75 + yOffset);
						northEast = new Point(457 + xOffset, 41 + yOffset);
						int[] slots = new int[9];
						for (int i = 0; i < slots.length; i++)
							slots[i] = (41 * i) + (int) southWest.getX();
						for (int i = 0; i < slots.length; i++) {
							if (MouseHandler.mouseX >= slots[i] && MouseHandler.mouseX <= slots[i] + 42
									&& MouseHandler.mouseY >= northEast.getY() && MouseHandler.mouseY <= southWest.getY()) {

								if (draggingItemInterfaceId != Bank.SEARCH_CONTAINER) {
									RSInterface rsi = RSInterface.interfaceCache[58050 + i];
									if (rsi.isMouseoverTriggered) {
										continue;
									}
								}

								// Update client side to hide latency
								if (Bank.getCurrentBankTab() == 0) {
									OptionalInt fromTabOptional = Arrays.stream(Bank.ITEM_CONTAINERS).filter(id -> draggingItemInterfaceId == id).findFirst();
									if (fromTabOptional.isPresent()) {
										RSInterface fromTab = RSInterface.interfaceCache[fromTabOptional.getAsInt()];
										RSInterface toTab = RSInterface.interfaceCache[Bank.ITEM_CONTAINERS[i]];
										if (toTab.getInventoryContainerFreeSlots() > 0 && fromTab.id != toTab.id) {
											RSInterface.insertInventoryItem(fromTab, itemDraggingSlot, toTab);
										}
									}
								}

								if (draggingItemInterfaceId != Bank.SEARCH_CONTAINER) {
									stream.createFrame(214);
									stream.method433(draggingItemInterfaceId);
									stream.method424(0);
									stream.method433(itemDraggingSlot);
									stream.method431(1000 + i);
								} else {
									stream.createFrame(243);
									stream.writeWord(i);
									stream.writeWord(RSInterface.get(draggingItemInterfaceId).inventoryItemId[itemDraggingSlot]);
								}
								return;
							}
						}
					}

					try {
						RSInterface class9 = RSInterface.interfaceCache[draggingItemInterfaceId];
						if (class9 != null) {
							if (mouseInvInterfaceIndex != itemDraggingSlot && lastActiveInvInterface == draggingItemInterfaceId) {
								// Dragging item inside the same container

								int insertMode = 0;
								if (anInt913 == 1 && class9.contentType == 206)
									insertMode = 1;
								if (class9.inventoryItemId[mouseInvInterfaceIndex] <= 0)
									insertMode = 0;

								if (class9.aBoolean235) {
									// Move to empty slot?
									int l2 = itemDraggingSlot;
									int l3 = mouseInvInterfaceIndex;
									class9.inventoryItemId[l3] = class9.inventoryItemId[l2];
									class9.inventoryAmounts[l3] = class9.inventoryAmounts[l2];
									class9.inventoryItemId[l2] = -1;
									class9.inventoryAmounts[l2] = 0;
								} else if (insertMode == 1) {
									// Insert
									int i3 = itemDraggingSlot;
									for (int i4 = mouseInvInterfaceIndex; i3 != i4; ) {
										if (i3 > i4) {
											class9.swapInventoryItems(i3, i3 - 1);
											i3--;
										} else if (i3 < i4) {
											class9.swapInventoryItems(i3, i3 + 1);
											i3++;
										}
									}
								} else {
									// Swap
									class9.swapInventoryItems(itemDraggingSlot, mouseInvInterfaceIndex);
								}

								Bank.shiftTabs();
								stream.createFrame(214);
								stream.method433(draggingItemInterfaceId);
								stream.method424(insertMode);
								stream.method433(itemDraggingSlot);
								stream.method431(mouseInvInterfaceIndex);
							} else if (class9.allowInvDraggingToOtherContainers && lastActiveInvInterface != draggingItemInterfaceId) {
								if (lastActiveInvInterface != -1 && draggingItemInterfaceId != -1) {
									RSInterface draggingFrom = RSInterface.interfaceCache[draggingItemInterfaceId];
									RSInterface draggingTo = RSInterface.interfaceCache[lastActiveInvInterface];
									if (draggingTo != null && draggingFrom != null
											&& draggingTo.allowInvDraggingToOtherContainers
											&& draggingFrom.allowInvDraggingToOtherContainers) {
										int fromSlot = itemDraggingSlot;
										int toSlot = mouseInvInterfaceIndex;
										int insertMode = 0;
										if (anInt913 == 1 && class9.contentType == 206)
											insertMode = 1;

										if (insertMode == 1) {
											// insert
											if (draggingTo.getInventoryContainerFreeSlots() > 0) {
												RSInterface.insertInventoryItem(draggingFrom, fromSlot, draggingTo, toSlot);
											} else {
												return;
											}
										} else {
											//swap
											RSInterface.swapInventoryItems(draggingFrom, fromSlot, draggingTo, toSlot);
										}

										stream.createFrame(242);
										stream.writeWord(draggingTo.id);
										stream.writeWord(draggingFrom.id);
										stream.method424(insertMode);
										stream.writeWord(fromSlot);
										stream.writeWord(toSlot);
									}
								}
							}
						}
					} catch (Exception e) {
						e.printStackTrace();
					}

				} else if ((anInt1253 == 1 || menuHasAddFriend(menuActionRow - 1)) && menuActionRow > 2)
					determineMenuSize();
				else if (menuActionRow > 0)
					doAction(menuActionRow - 1);
				atInventoryLoopCycle = 10;
				clickMode3 = 0;
			}
		}
		if (SceneGraph.clickedTileX != -1) {
//			if (fullscreenInterfaceID != -1) {
//				return;
//			}
			int k = SceneGraph.clickedTileX;
			int k1 = SceneGraph.clickedTileY;
			boolean flag = false;
			if (localPlayer.isAdminRights() && controlIsDown) {
				teleport(baseX + k, baseY + k1);
			} else {
				flag = doWalkTo(0, localPlayer.waypoint_x[0], localPlayer.waypoint_z[0], 0, 0, 0, 0, 0, k1, true, k);
			}
			SceneGraph.clickedTileX = -1;
			if (flag) {
				crossX = MouseHandler.saveClickX;
				crossY = MouseHandler.saveClickY;
				crossType = 1;
				crossIndex = 0;
			}
		}
		if (clickMode3 == 1 && clickToContinueString != null) {
			clickToContinueString = null;
			inputTaken = true;
			clickMode3 = 0;
		}
		if (!processMenuClick()) {
			processTabClick();
			processMainScreenClick();

			// processTabClick2();
			processChatModeClick();
		}

		if (MouseHandler.instance.clickMode2 == 1 || clickMode3 == 1)
			mouseClickCount++;
		if (anInt1500 != 0 || anInt1044 != 0 || anInt1129 != 0) {
			if (anInt1501 < 50 && !menuOpen) {
				anInt1501++;
				if (anInt1501 == 50) {
					if (anInt1500 != 0) {
						inputTaken = true;
					}
					if (anInt1044 != 0) {
						needDrawTabArea = true;
					}
				}
			}
		} else if (anInt1501 > 0) {
			anInt1501--;
		}
		if (loadingStage == 2)
			method108();
		if (loadingStage == 2 && inCutScene)
			calcCameraPos();
		for (int i1 = 0; i1 < 5; i1++)
			anIntArray1030[i1]++;
		method73();
		++MouseHandler.idleCycles;
		++KeyHandler.idleCycles;

		if (MouseHandler.idleCycles++ > 9000) {
			anInt1011 = 250;
			MouseHandler.idleCycles = 0;
			stream.createFrame(202);
		}

		anInt1010++;
		if (anInt1010 > 50)
			stream.createFrame(0);

		try {
			if (socketStream != null && stream.currentPosition > 0) {
				socketStream.queueBytes(stream.currentPosition, stream.payload);
				stream.currentPosition = 0;
				anInt1010 = 0;
			}
		} catch (IOException _ex) {
			dropClient();
		} catch (Exception exception) {
			resetLogout();
			if (Configuration.developerMode) {
				exception.printStackTrace();
			}
		}
	}

	public void method63() {
		SpawnedObject spawnedObject = (SpawnedObject) spawns.reverseGetFirst();
		for (; spawnedObject != null; spawnedObject = (SpawnedObject) spawns.reverseGetNext())
			if (spawnedObject.getLongetivity == -1) {
				spawnedObject.delay = 0;
				method89(spawnedObject);
			} else {
				spawnedObject.unlink();
			}

	}
static Image image;
	static Font fontHelvetica;
	static FontMetrics loginScreenFontMetrics;
	/**
	 * Runescape Loading Bar
	 *
	 * @param percentage
	 * @param s
	 * @param downloadSpeed
	 * @param secondsRemaining
	 * @trees
	 */
	public Sprite logo, loginBackground;
	public void drawLoadingText(int percentage, String s) {

		try {
		anInt1079 = percentage;
		aString1049 = s;

		if (titleStreamLoader == null) {
			super.drawInitial(percentage,s,true);
			return;
		}

			Graphics graphics = super.canvas.getGraphics();
			if (fontHelvetica == null) {
				fontHelvetica = new Font("Helvetica", Font.BOLD, 13);
				loginScreenFontMetrics = canvas.getFontMetrics(fontHelvetica);
			}


//		graphics.setColor(Color.blue);//draws entire backgorund
//		graphics.fillRect(0, 0, canvasWidth, canvasHeight);
//graphics.draw
		Color color = new Color(140, 17, 17);
		try {
		if (image == null) {
			 //image =  Toolkit.getDefaultToolkit().getImage(Signlink.getCacheDirectory() + "Sprites/loginscreen/background.png");
			image = canvas.createImage(304, 34);//are you saying everything is black except this 304 x 34 rectangle?
		}
		Graphics imageGraphics = image.getGraphics();

		imageGraphics.setColor(color);
		imageGraphics.drawRect(0, 0, 303, 33);
		imageGraphics.fillRect(2, 2, percentage * 3, 30);
		imageGraphics.setColor(Color.black);
		imageGraphics.drawRect(1, 1, 301, 31);
		imageGraphics.fillRect(percentage * 3 + 2, 2, 300 - percentage * 3, 30);
		imageGraphics.setFont(fontHelvetica);
		imageGraphics.setColor(Color.white);
		imageGraphics.drawString(s, (304 - loginScreenFontMetrics.stringWidth(s)) / 2, 22);

		graphics.drawImage(image, canvasWidth / 2 - 152, canvasHeight / 2 - 18, null);

		} catch (Exception exception) {

			int centerX = canvasWidth / 2 - 152;
			int centerY = canvasHeight / 2 - 18;
			graphics.setColor(color);
			graphics.drawRect(centerX, centerY, 303, 33);
			graphics.fillRect(centerX + 2, centerY + 2, percentage * 3, 30);
			graphics.setColor(Color.black);
			graphics.drawRect(centerX + 1, centerY + 1, 301, 31);
			graphics.fillRect(percentage * 3 + centerX + 2, centerY + 2, 300 - percentage * 3, 30);
			graphics.setFont(fontHelvetica);
			graphics.setColor(Color.white);
			graphics.drawString(s, centerX + (304 - loginScreenFontMetrics.stringWidth(s)) / 2, centerY + 22);
		}


//		//loginBoxImageProducer.initDrawingArea();
//        char c = '\u0168';
//        char c1 = '\310';
//        byte byte1 = 20;
//        newBoldFont.drawCenteredString(Configuration.CLIENT_TITLE+" is loading - please wait...", c/2, c1 / 2 - 26 - byte1, 0xFFFFFF, 0x000000,256);
//        int j = c1 / 2 - 18 - byte1;
//        Rasterizer2D.drawBoxOutline(c / 2 - 152, j, 304, 34, 0);
//
//        Rasterizer2D.drawBoxOutline(c / 2 - 151, j + 1, 302, 32, 0x8c1111);
//        Rasterizer2D.drawBox(c / 2 - 150, j + 2, percentage * 3, 30, 0x8c1111); //red fill
//
//        Rasterizer2D.drawBox((c / 2 - 150) + percentage * 3, j + 2, 300 - percentage * 3, 30, 0);//black fill
//
//        newBoldFont.drawCenteredString(s, c / 2, (c1 / 2 + 5) - byte1, 0xffffff, 0x000000);

//		int x = 765 / 2 - 543 / 2;
//		int y = 475 - 20 + 8;
//		int width = 540;
//		int height = 32;
//		double offset = 5.43;
//		new Sprite("loginscreen/general/emptybar").drawAdvancedSprite(765 / 2 - width / 2, y + 11 - height / 2);
//		new Sprite("loginscreen/general/fullbar").drawAdvancedSprite(765 / 2 - width / 2, y + 11 - height / 2);
//		Rasterizer2D.drawAlphaGradient(x + ((int) Math.round(percentage * offset) / 2), y, width - ((int) Math.round(percentage * offset) / 2), height, 0x000000, 0x000000, 200);
//		if (percentage >= 198) {
//			newBoldFont.drawCenteredString("Finished loading " + Configuration.CLIENT_TITLE, (765 / 2), y + height / 2, 0xffffff, 1);
//		} else {
//			newBoldFont.drawCenteredString("Loading " + Configuration.CLIENT_TITLE + " - Please wait - " + (percentage / 2) + "%", (765 / 2),
//				y + height / 2, 0xffffff, 1);
//		}

	//	rasterProvider.drawFull(0, 0);
		} catch (Exception exception) {
			super.canvas.repaint();
			exception.printStackTrace();
		}
	}

	public void method65(int width, int height, int mouseX, int mouseY, RSInterface class9, int i1, boolean flag, int j1) {
		int anInt992;
		if (aBoolean972)
			anInt992 = 32;
		else
			anInt992 = 0;
		aBoolean972 = false;
		if (mouseX >= width && mouseX < width + 16 && mouseY >= i1 && mouseY < i1 + 16) {
			class9.scrollPosition -= mouseClickCount * 4;
			if (flag) {
				needDrawTabArea = true;
			}
		} else if (mouseX >= width && mouseX < width + 16 && mouseY >= (i1 + height) - 16 && mouseY < i1 + height) {
			class9.scrollPosition += mouseClickCount * 4;
			if (flag) {
				needDrawTabArea = true;
			}
		} else if (mouseX >= width - anInt992 && mouseX < width + 16 + anInt992 && mouseY >= i1 + 16 && mouseY < (i1 + height) - 16 && mouseClickCount > 0) {
			int l1 = ((height - 32) * height) / j1;
			if (l1 < 8)
				l1 = 8;
			int i2 = mouseY - i1 - 16 - l1 / 2;
			int j2 = height - 32 - l1;
			class9.scrollPosition = ((j1 - height) * i2) / j2;
			if (flag)
				needDrawTabArea = true;
			aBoolean972 = true;
		}
	}

	private boolean clickObject(long object, int j, int k) {
		try {
			if (fullscreenInterfaceID != -1) {
				return false;
			}
			int objectType = ObjectKeyUtil.getObjectType(object);
			int orientation = ObjectKeyUtil.getObjectOrientation(object);

			if (objectType == 10 || objectType == 11 || objectType == 22) {
				ObjectDefinition class46 = ObjectDefinition.lookup(ObjectKeyUtil.getObjectId(object));
				int i2;
				int j2;
				if (orientation == 0 || orientation == 2) {
					i2 = class46.length;
					j2 = class46.width;
				} else {
					i2 = class46.width;
					j2 = class46.length;
				}
				int k2 = class46.blocksides;
				if (orientation != 0) {
					k2 = (k2 << orientation & 0xf) + (k2 >> 4 - orientation);
				}
				doWalkTo(2, localPlayer.waypoint_x[0], localPlayer.waypoint_z[0], 0, j2, 0, i2, k2, j, false, k);
			} else {
				doWalkTo(2, localPlayer.waypoint_x[0], localPlayer.waypoint_z[0], orientation, 0, objectType + 1, 0, 0, j, false, k);
			}
			crossX = MouseHandler.saveClickX;
			crossY = MouseHandler.saveClickY;
			crossType = 2;
			crossIndex = 0;
			return true;
		} catch (Exception e) {
			System.err.println("Error while walking.");
			e.printStackTrace();
			return false;
		}
	}

	private FileArchive streamLoaderForName(int i, String archiveName) {
		byte abyte0[] = null;
		try {
			if (decompressors[0] != null) {
				abyte0 = decompressors[0].read(i);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (abyte0 != null) {
			FileArchive streamLoader = new FileArchive(abyte0, archiveName);
			return streamLoader;
		}
		return null;
	}

	public void sendStringAsLong(String string) {
		stream.createFrame(60);
		stream.writeUnsignedByte(1 + string.length());
		stream.writeString(string);
	}

	public void sendString(int identifier, String text) {
		text = identifier + "," + text;
		stream.createFrame(127);
		stream.writeUnsignedByte(text.length() + 1);
		stream.writeString(text);
	}

	public void dropClient() {
		if (anInt1011 > 0) {
			resetLogout();
			return;
		}
		setGameState(GameState.CONNECTION_LOST);
		logger.debug("Dropping client.");

		Rasterizer2D.fillPixels(2, 229, 39, 0xffffff, 2);
		Rasterizer2D.drawPixels(37, 3, 3, 0, 227);
		frameMode(false);
		minimapState = 0;
		Preferences.save();
		destX = 0;
		RSSocket rsSocket = socketStream;
		loggedIn = false;
		loginFailures = 0;
		//login(myUsername, getPassword(), true);
		if (!loggedIn)
			resetLogout();
		try {
			rsSocket.close();
		} catch (Exception _ex) {
		}
	}

	public static String formatNumber(double number) {
		return NumberFormat.getInstance().format(number);
	}

	public int settings[];

	private void updateSettings() {
		settings[809] = Configuration.alwaysLeftClickAttack ? 1 : 0;
		settings[817] = Configuration.escapeCloseInterface ? 1 : 0;
	}

public boolean statadjustmentshidden;
	public int[] cursesstaticons = {99680, 99682, 99683, 99684, 99685, 99686};
	private void doAction(int i) {
		if (i < 0)
			return;
		if (inputDialogState != 0 && inputDialogState != 3) {
			inputDialogState = 0;
			inputTaken = true;
		}
		int j = menuActionCmd2[i];
		int buttonPressed = menuActionCmd3[i];
		int l = menuActionID[i];

		int i1 = (int) menuActionCmd1[i];
		long keyLong = menuActionCmd1[i];
//		RSInterface button_ = RSInterface.interfaceCache[buttonPressed];
//		if(button_ != null)
//		if (button_.hasTooltip()) {
//			if(button_.tooltip.equalsIgnoreCase("Close")){
//				clearTopInterfaces();
//			}
//		}
//		if (clickConfigButton(buttonPressed)) {
//			return;
//		}
	//	System.out.println("buttonpressed: "+buttonPressed);
//the buttonpressed is actually the id of the interface lol.
		switch (buttonPressed) {
			case 15251:
				fullscreenInterfaceID = -1;
				openInterfaceID = -1;
				loadTabArea();
			//	drawTabs();
				handleclosingWelcomeScreen(1,1);
				break;
			case 10407:
				questtabselected  = 1;
				break;
			case 10404:
				questtabselected  = 2;
				break;
			case 10405:
				questtabselected  = 3;
				break;
			case 10406:
				questtabselected  = 4;
				break;
            case 99681://hide stat adjusments in curse prayers
			//	System.out.println("here");
                if(!statadjustmentshidden){
					for (int theint : cursesstaticons) {
						RSInterface.interfaceCache[theint].isMouseoverTriggered = true;
						RSInterface.interfaceCache[theint].interfaceHidden = true;
					}
					RSInterface.interfaceCache[99681].message = "Show stat adjustments";
                } else {
					for (int theint : cursesstaticons) {
						RSInterface.interfaceCache[theint].isMouseoverTriggered = false;
						RSInterface.interfaceCache[theint].interfaceHidden = false;
					}
					RSInterface.interfaceCache[99681].message = "Hide stat adjustments";
				}
				statadjustmentshidden = !statadjustmentshidden;
				break;

			case 68900://when you click on quest tab button in player panel it puts you into the quest tab as if you clicked it!
				RSInterface questpanel = RSInterface.interfaceCache[10404];
				RSInterface.handleConfigHover(questpanel);
				OSRSQuestTabWidget.switchSettings(10404);
				break;

			case 78902:
				RSInterface questpanel2 = RSInterface.interfaceCache[10406];//10406 = the tab from ::debug and realid
				RSInterface.handleConfigHover(questpanel2);
				OSRSQuestTabWidget.switchSettings(10406);
				break;

			case 59006:
				//System.out.println("here");
				friendstab = false;
			//	setSidebarInterface(8,50651);
				break;
				case 59007:
			//	friendstab = true;
				//	setSidebarInterface(8,50650);
					friendstab = true;
				break;
			case 42541://chat effects was 171

				setConfigButton_new(171, informationFile.isRememberChatEffects() ? false : true);
//				if(informationFile.rememberChatEffects)
//					informationFile.rememberChatEffects = false;
//				else
//					informationFile.rememberChatEffects = true;
				informationFile.setRememberChatEffects(informationFile.isRememberChatEffects() ? false : true);
				try {
					informationFile.write();
					System.out.println("Written to: " + informationFile.isRememberChatEffects());
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;



			case 41552://shift drop was 293
				setConfigButton_new(293, informationFile.rememberShiftDrop ? true : false);
				informationFile.setRememberShiftDrop(informationFile.isRememberShiftDrop() ? false : true);
				try {
					informationFile.write();
					System.out.println("Written to: " + informationFile.isRememberShiftDrop());
				} catch (IOException e) {
					e.printStackTrace();
				}
				break;
			case 42522:
				if (isResized()) {
					setConfigButton(i, true);
					setConfigButton(23003, false);
					setConfigButton(23005, false);
					frameMode(false);

				}
				break;

			case 42523:
				/*if (currentScreenMode == ScreenMode.RESIZABLE) {
					setConfigButton(i, true);
					setConfigButton(23001, false);
					setConfigButton(23005, false);
					setGameMode(ScreenMode.FULLSCREEN);
					return;
				}*/
				if (!isResized()) {
					setConfigButton(i, true);
					setConfigButton(23001, false);
					setConfigButton(23005, false);
					frameMode(true);
				}
				break;
		}

		if (buttonPressed == 941) {
			cameraZoom = 200;
		}
		if (buttonPressed == 942) {
			cameraZoom = 400;
		}
		if (buttonPressed == 943) {
			cameraZoom = 600;
		}
		if (buttonPressed == 944) {
			cameraZoom = 800;
		}
		if (buttonPressed == 945) {
			cameraZoom = 1000;
		}
		if (l >= 2000)
			l -= 2000;
		if (l == 1100) {
			RSInterface button = RSInterface.interfaceCache[buttonPressed];
			button.setMenuVisible(button.isMenuVisible() ? false : true);
		}
		for (int i2 = 0; i2 < NewTele.CATEGORY_NAMES.length; i2++) {
			//	resetsidebars_teleportinterface();
			if(buttonPressed == 88005 + i2){
				resetsidebars_teleportinterface();
				//RSInterface.interfaceCache[88005+i2].sprite1 =new Sprite("interfaces/newtele/SPRITE 2065");
				RSInterface.interfaceCache[88005+i2].sprite2 = new Sprite("interfaces/newtele/SPRITE 2065");
			}
		}
		if (l == 474) {
			drawExperienceCounter = !drawExperienceCounter;
		}
//if(l == 1730){
//	sendvotecheck(1);
//}
		if (l == 8992) {//wont work look it says -2000
			/**
			 * for drop interface
			 */

			String messageToSearch = lastViewedDropTable;

			if (messageToSearch == null) {
				return;
			}

			NPCDropInfo info = NPCDropInfo.getEntry(messageToSearch);

			if (info == null) {
				return;
			}
			stream.createFrame(177);
			stream.writeWord(info.npcIndex);
			return;
		}


//		if (l == 475) {
//			stream.createFrame(185);
//			stream.writeWord(-1);
//			experienceCounter = 0L;
//		}
		if (l == 852 || l == 4251 || l == 4252) {
			stream.createFrame(185);
			stream.writeWord(l);
		}
		if (l == 1850) {
			stream.createFrame(185);
			stream.writeWord(5100);
		}
		if (l == 769) {
			RSInterface d = RSInterface.interfaceCache[j];
			RSInterface p = RSInterface.interfaceCache[buttonPressed];
			if (!d.dropdown.isOpen()) {
				if (p.dropdownOpen != null) {
					p.dropdownOpen.dropdown.setOpen(false);
				}
				p.dropdownOpen = d;
			} else {
				p.dropdownOpen = null;
			}
			d.dropdown.setOpen(!d.dropdown.isOpen());
		} else if (l == 770) {
			RSInterface d = RSInterface.interfaceCache[j];
			RSInterface p = RSInterface.interfaceCache[buttonPressed];
			if (i1 >= d.dropdown.getOptions().length)
				return;
			d.dropdown.setSelected(d.dropdown.getOptions()[i1]);
			d.dropdown.setOpen(false);
			d.dropdown.getMenuItem().select(i1, d);
			try {
				SettingsManager.saveSettings(Client.instance);
			} catch (IOException io) {
				io.printStackTrace();
			}
			p.dropdownOpen = null;
		}
		if (l == 850) {
			if (tabInterfaceIDs[tabID] == 17200) {
				stream.createFrame(185);
				stream.writeWord(5200 + buttonPressed);
			}
		}
		if (l == 661) { // intid, slot, itemid;
			stream.createFrame(232);
			stream.method432(1);
			stream.writeWord(i1);
		}
		if (l == 662) {
			stream.createFrame(232);
			stream.method432(2);
			stream.writeWord(i1);
		}
		if (l == 663) {
			stream.createFrame(232);
			stream.method432(3);
			stream.writeWord(i1);
		}
		if (l == 664) {
			stream.createFrame(232);
			stream.method432(4);
			stream.writeWord(i1);
		}

		switch (l) {//put buttons that should be clicked from sprites or  orbs or w/e here

			case 1513: // wiki search

				stream.createFrame(185);
				stream.writeWord(1513);
				break;
			case 1514: // tele world map

				stream.createFrame(185);
				stream.writeWord(1514);
				flashworldmap = false;
				break;
			case 1500: // Toggle quick prayers
				prayClicked = !prayClicked;
				stream.createFrame(185);
				stream.writeWord(5000);
				break;

			case 1506: // Select quick prayers
				stream.createFrame(185);
				stream.writeWord(5001);
				setTab(5);
				break;
		}
		if (l == 1200) {
			stream.createFrame(185);
			stream.writeWord(buttonPressed);
			RSInterface item = RSInterface.interfaceCache[buttonPressed];
			RSInterface menu = RSInterface.interfaceCache[item.mOverInterToTrigger];
			menu.setMenuItem(item.getMenuItem());
			menu.setMenuVisible(false);
		}
		if (l >= 1700 && l <= 1710) {
			RSInterface button = RSInterface.get(buttonPressed);
			if (button.buttonListener != null)
				button.buttonListener.accept(buttonPressed);
			// Button Click Packet
			if (button.newButtonClicking) {
				stream.createFrame(184);
				stream.writeWord(buttonPressed);

			} else {
				stream.createFrame(185);
				int offset = buttonPressed + (buttonPressed - 58030) * 10 + (l - 1700);
				stream.writeWord(offset);
				Bank.handleButton(offset);
			}
		}
		if (l == 300) {
			stream.createFrame(141);
			stream.method432(j);
			stream.writeWord(buttonPressed);
			stream.method432(i1);
			stream.writeDWord(modifiableXValue);
		}
		if (l == 291) {
			stream.createFrame(140);
			stream.writeWord(buttonPressed);
			stream.method433(i1);
			stream.method431(j);
			atInventoryLoopCycle = 0;
			atInventoryInterface = buttonPressed;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[buttonPressed].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[buttonPressed].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (l == 582) {
			Npc npc = npcs[i1];
			if (npc != null) {
				doWalkTo(2, localPlayer.waypoint_x[0], localPlayer.waypoint_z[0], 0, 1, 0, 1, 0, npc.waypoint_z[0], false, npc.waypoint_x[0]);
				crossX = MouseHandler.saveClickX;
				crossY = MouseHandler.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(57);
				stream.method432(anInt1285);
				stream.method432(i1);
				stream.method431(anInt1283);
				stream.method432(anInt1284);
			}
		}
		if (l == 234) {
			boolean flag1 = doWalkTo(2, localPlayer.waypoint_x[0], localPlayer.waypoint_z[0], 0, 0, 0, 0, 0, buttonPressed, false, j);
			if (!flag1)
				flag1 = doWalkTo(2, localPlayer.waypoint_x[0], localPlayer.waypoint_z[0], 0, 1, 0, 1, 0, buttonPressed, false, j);
			crossX = MouseHandler.saveClickX;
			crossY = MouseHandler.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(236);
			stream.method431(buttonPressed + baseY);
			stream.writeWord(i1);
			stream.method431(j + baseX);
		}
		if (l == 62 && clickObject(keyLong, buttonPressed, j)) {
			stream.createFrame(192);
			stream.writeWord(anInt1284);
			stream.writeDWord(ObjectKeyUtil.getObjectId(keyLong));
			stream.method433(buttonPressed + baseY);
			stream.method431(anInt1283);
			stream.method433(j + baseX);
			stream.writeWord(anInt1285);
		}
		if (l == 511) {
			boolean flag2 = doWalkTo(2, localPlayer.waypoint_x[0], localPlayer.waypoint_z[0], 0, 0, 0, 0, 0, buttonPressed, false, j);
			if (!flag2)
				flag2 = doWalkTo(2, localPlayer.waypoint_x[0], localPlayer.waypoint_z[0], 0, 1, 0, 1, 0, buttonPressed, false, j);
			crossX = MouseHandler.saveClickX;
			crossY = MouseHandler.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(25);
			stream.method431(anInt1284);
			stream.method432(anInt1285);
			stream.writeWord(i1);
			stream.method432(buttonPressed + baseY);
			stream.method433(anInt1283);
			stream.writeWord(j + baseX);
		}
		if (l == 74) {
			stream.createFrame(122);
			stream.writeWord(buttonPressed);
			stream.writeWord(j);
			stream.writeWord(i1);
			atInventoryLoopCycle = 0;
			atInventoryInterface = buttonPressed;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[buttonPressed].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[buttonPressed].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (l == 315) {
			RSInterface class9 = RSInterface.interfaceCache[buttonPressed];
			boolean flag8 = true;
			if (class9.type == RSInterface.TYPE_CONFIG || class9.id == 50009) { // Placeholder toggle
				class9.active = !class9.active;
			} else if (class9.type == RSInterface.TYPE_CONFIG_HOVER) {
				RSInterface.handleConfigHover(class9);
			}
			if (class9.contentType > 0)
				flag8 = promptUserForInput(class9);
			if (flag8) {
				Interfaces.settings_clanchat(buttonPressed);//quest tab switching
				SettingsTabWidget.settings(buttonPressed);
				Interfaces.mainPanelButtons(buttonPressed);
				Interfaces.modepanelButtons(buttonPressed);
				OSRSQuestTabWidget.settings(buttonPressed);//quest tab switching
				switch (buttonPressed) {
					case 23003:
						Configuration.alwaysLeftClickAttack = !Configuration.alwaysLeftClickAttack;
						// savePlayerData();
						updateSettings();
						break;
					case 23005:
						Configuration.hideCombatOverlay = !Configuration.hideCombatOverlay;
						// savePlayerData();
						updateSettings();
						break;
					case 37708:
						if (coloredItemColor != 0xffffff) {
							inputTaken = true;
							inputDialogState = 0;
							messagePromptRaised = true;
							promptInput = "";
							friendsListAction = 25;// 25 = enter item name
							aString1121 = "Enter an item name to set this color to. (Name must match exact)";
						} else {
							pushMessage("You must select a color first...", 0, "");
						}
						break;
					case 37710:
						inputTaken = true;
						inputDialogState = 0;
						messagePromptRaised = true;
						promptInput = "";
						friendsListAction = 26;// 25 = enter item name
						aString1121 = "Enter a minimum item value to display on the ground.";
						break;
					case 37706:
						Robot robot2;
						PointerInfo pointer2;
						pointer2 = MouseInfo.getPointerInfo();
						Point coord2 = pointer2.getLocation();
						try {
							robot2 = new Robot();
							coord2 = MouseInfo.getPointerInfo().getLocation();
							Color color = robot2.getPixelColor((int) coord2.getX(), (int) coord2.getY());
							String hex2 = String.format("%02X%02X%02X", color.getRed(), color.getGreen(), color.getBlue());
							int hex3 = Integer.parseInt(hex2, 16);
							// pushMessage("<shad="+hex3+">Yell Color set.</shad>", 0, "");
							coloredItemColor = hex3;
							RSInterface.interfaceCache[37707].message = "Current color chosen!";
							RSInterface.interfaceCache[37707].textColor = coloredItemColor;
						} catch (AWTException e) {
							e.printStackTrace();
						}
						break;

					case 62013:
						RSInterface input = RSInterface.interfaceCache[RSInterface.selectedItemInterfaceId + 1];
						RSInterface itemContainer = RSInterface.interfaceCache[RSInterface.selectedItemInterfaceId];
						if (RSInterface.selectedItemInterfaceId <= 0) {
							return;
						}
						if (input != null && itemContainer != null) {
							int amount = -1;
							try {
								amount = Integer.parseInt(input.message);
							} catch (NumberFormatException nfe) {
								pushMessage("The amount must be a non-negative numerical value.", 0, "");
								break;
							}
							if (itemContainer.itemSearchSelectedId < 0) {
								itemContainer.itemSearchSelectedId = 0;
							}
							if (itemContainer.itemSearchSelectedSlot < 0) {
								itemContainer.itemSearchSelectedSlot = 0;
							}
							stream.createFrame(124);
							stream.writeDWord(RSInterface.selectedItemInterfaceId);
							stream.writeDWord(itemContainer.itemSearchSelectedSlot);
							stream.writeDWord(itemContainer.itemSearchSelectedId - 1);
							stream.writeDWord(amount);
						}
						break;

					case 32013:
						RSInterface input2 = RSInterface.interfaceCache[RSInterface.selectedItemInterfaceId + 1];
						RSInterface itemContainer2 = RSInterface.interfaceCache[RSInterface.selectedItemInterfaceId];
						if (RSInterface.selectedItemInterfaceId <= 0) {
							return;
						}
						if (input2 != null && itemContainer2 != null) {
							int amount = -1;
							try {
								amount = Integer.parseInt(input2.message);
							} catch (NumberFormatException nfe) {
								pushMessage("The amount must be a non-negative numerical value.", 0, "");
								break;
							}
							if (itemContainer2.itemSearchSelectedId < 0) {
								itemContainer2.itemSearchSelectedId = 0;
							}
							if (itemContainer2.itemSearchSelectedSlot < 0) {
								itemContainer2.itemSearchSelectedSlot = 0;
							}
							stream.createFrame(124);
							stream.writeDWord(RSInterface.selectedItemInterfaceId);
							stream.writeDWord(itemContainer2.itemSearchSelectedSlot);
							stream.writeDWord(itemContainer2.itemSearchSelectedId - 1);
							stream.writeDWord(amount);
						}
						break;
					case 19144:
						sendFrame248(15106, 3213);
						resetAnimation(15106);
						inputTaken = true;
						break;
					default:
						if (RSInterface.get(buttonPressed) != null && RSInterface.get(buttonPressed).newButtonClicking) {
							stream.createFrame(184);
							stream.writeWord(buttonPressed);
						} else {
							RSInterface widget = RSInterface.get(buttonPressed);

							// Determine if a special attack button is being pressed
							if (widget.hasTooltip()) {
								String tooltip = widget.tooltip.toLowerCase();
								boolean isSpecialAttackButton = tooltip.contains("use") && tooltip.contains("special attack");
								if (isSpecialAttackButton) {
									// Send custom packet for special attacks
									stream.createFrame(209);
									stream.writeWord(buttonPressed);
									return;
								}
							}

							// If special attack button is not being pressed process normally
							stream.createFrame(185);
							stream.writeWord(buttonPressed);

							switch (buttonPressed) {
								case SettingsTabWidget.HIDE_LOCAL_PET_OPTIONS:
									SettingsTabWidget.toggleHidePetOption();
									break;
							}
						}

						if (buttonPressed >= 61101 && buttonPressed <= 61200) {
							int selected = buttonPressed - 61101;
							for (int ii = 0, slot = -1; ii < ItemDefinition.totalItems && slot < 100; ii++) {
								ItemDefinition def = ItemDefinition.lookup(ii);

								if (def.name == null || def.noted_item_id == ii - 1 || def.unnoted_item_id == ii - 1
										|| RSInterface.interfaceCache[61254].message.length() == 0) {
									continue;
								}

								if (def.name.toLowerCase()
										.contains(RSInterface.interfaceCache[61254].message.toLowerCase())) {
									slot++;
								}

								if (slot != selected) {
									continue;
								}

								int id = def.id;
								long num = Long.valueOf(RSInterface.interfaceCache[61255].message.replaceAll(",", ""));

								if (num > Integer.MAX_VALUE) {
									num = Integer.MAX_VALUE;
								}

								stream.createFrame(149);
								stream.writeWord(id);
								stream.writeDWord((int) num);
								stream.writeUnsignedByte(variousSettings[1075]);
								break;
							}
						}

						break;

				}
			}
		}
		if (l == 561) {
			Player player = players[i1];
			if (player != null) {
				doWalkTo(2, localPlayer.waypoint_x[0], localPlayer.waypoint_z[0], 0, 1, 0, 1, 0, player.waypoint_z[0], false,
						player.waypoint_x[0]);
				crossX = MouseHandler.saveClickX;
				crossY = MouseHandler.saveClickY;
				crossType = 2;
				crossIndex = 0;
				anInt1188 += i1;
				if (anInt1188 >= 90) {
					stream.createFrame(136);
					anInt1188 = 0;
				}
				stream.createFrame(128);
				stream.writeWord(i1);
			}
		}
		if (l == 745) {
			stream.createFrame(8);
			stream.writeDWord(i1);
		}
		if (l == 20) {
			Npc class30_sub2_sub4_sub1_sub1_1 = npcs[i1];
			if (class30_sub2_sub4_sub1_sub1_1 != null) {
				doWalkTo(2, localPlayer.waypoint_x[0], localPlayer.waypoint_z[0], 0, 1, 0, 1, 0, class30_sub2_sub4_sub1_sub1_1.waypoint_z[0],
						false, class30_sub2_sub4_sub1_sub1_1.waypoint_x[0]);
				crossX = MouseHandler.saveClickX;
				crossY = MouseHandler.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(155);
				stream.method431(i1);
			}
		}
		if (l == 779) {
			Player class30_sub2_sub4_sub1_sub2_1 = players[i1];
			if (class30_sub2_sub4_sub1_sub2_1 != null) {
				doWalkTo(2, localPlayer.waypoint_x[0], localPlayer.waypoint_z[0], 0, 1, 0, 1, 0, class30_sub2_sub4_sub1_sub2_1.waypoint_z[0],
						false, class30_sub2_sub4_sub1_sub2_1.waypoint_x[0]);
				crossX = MouseHandler.saveClickX;
				crossY = MouseHandler.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(153);
				stream.method431(i1);
			}
		}
		if (l == 516) {
			int y;
			int x;
			if (!this.menuOpen) {
				x = MouseHandler.saveClickX - 4;
				y = MouseHandler.saveClickY - 4;
			} else {
				x = j - 4;
				y = buttonPressed - 4;
			}
			if (getUserSettings().isAntiAliasing() == true) {
				x <<= 1;
				y <<= 1;
			}
			this.scene.clickTile(y, x);
		}
		if (l == 1062) {//fourth click object like mole hole look-inside
			//anInt924 += baseX;
//			if (anInt924 >= 113) {
//				stream.createFrame(183);
//				stream.writeDWordBigEndian(0xe63271);
//				anInt924 = 0;wilderness
//			}

			clickObject(keyLong, buttonPressed, j);
			stream.createFrame(228);
//			stream.writeDWord(ObjectKeyUtil.getObjectId(keyLong));
//			stream.method432(buttonPressed + baseY);
//			stream.writeWord(j + baseX);

//			stream.method431(j + baseX);
//			stream.writeWord(buttonPressed + baseY);
//			stream.writeDWord(ObjectKeyUtil.getObjectId(keyLong));

			stream.method433(j + baseX);
			stream.writeDWord(ObjectKeyUtil.getObjectId(keyLong));
			stream.method432(buttonPressed + baseY);
		}
		if (l == 679 && !aBoolean1149) {
			stream.createFrame(40);
			stream.writeWord(buttonPressed);
			aBoolean1149 = true;
		}
//		if (l == Autocast.AUTOCAST_MENU_ACTION_ID) {
//			RSInterface button = RSInterface.get(buttonPressed);
//			stream.createFrame(5);
//			stream.writeWord(button.autocastSpellId);
//			stream.writeByte(button.autocastDefensive ? 1 : 0);
//		//	if (Configuration.developerMode) {
//				System.out.println("Autocast: " + button.autocastSpellId + ", defensive: " + button.autocastDefensive);
//		//	}
//		}
		if (l == 431) {
			stream.createFrame(129);
			stream.method432(j);
			stream.writeWord(buttonPressed);
			stream.method432(i1);
			atInventoryLoopCycle = 0;
			atInventoryInterface = buttonPressed;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[buttonPressed].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[buttonPressed].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (l == 337 || l == 42 || l == 792 || l == 322) {
			String s = menuActionName[i];
			int k1 = s.indexOf("@whi@");
			if (k1 != -1) {
				long l3 = StringUtils.longForName(s.substring(k1 + 5).trim());
				if (l == 337)
					addFriend(l3);
				if (l == 42)
					addIgnore(l3);
				if (l == 792)
					delFriend(l3);
				if (l == 322)
					delIgnore(l3);
			}
		}
		if (l == 1337) { // Placeholders
			inputString = "::placeholder-" + j + "-" + i1;
			stream.createFrame(103);
			stream.writeUnsignedByte(inputString.length() - 1);
			stream.writeString(inputString.substring(2));
			inputString = "";
		}
		if (l == 53) {
			stream.createFrame(135);
			stream.method431(j);
			stream.method432(buttonPressed);
			stream.method431(i1);
			atInventoryLoopCycle = 0;
			atInventoryInterface = buttonPressed;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[buttonPressed].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[buttonPressed].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (l == 539) {
			stream.createFrame(16);
			stream.writeWord(i1);
			stream.method433(j);
			stream.method433(buttonPressed);
			atInventoryLoopCycle = 0;
			atInventoryInterface = buttonPressed;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[buttonPressed].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[buttonPressed].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
	if(isbroadcastsEnabled())
		if (l == 9111 || l == 9112 || l == 9115) {

			Broadcast bc = BroadcastManager.getCurrentBroadcast();

			if (bc == null) {
				System.err.println("No broadcast found for this msg..");
				return;
			}
			if (l == 9115) {
				BroadcastManager.removeIndex(bc.index);
				return;
			}
			stream.createFrame(199);
			stream.writeByte(bc.index);
			return;
		}

		if (l == 484 || l == 6 || l == 9501) {
			String s1 = menuActionName[i];
			int l1 = s1.indexOf("@whi@");
			if (l1 != -1) {
				s1 = s1.substring(l1 + 5).trim();
				String s7 = StringUtils.fixName(StringUtils.nameForLong(StringUtils.longForName(s1)));
				boolean flag9 = false;
				for (int j3 = 0; j3 < playerCount; j3++) {
					Player class30_sub2_sub4_sub1_sub2_7 = players[highres_player_list[j3]];
					if (class30_sub2_sub4_sub1_sub2_7 == null || class30_sub2_sub4_sub1_sub2_7.displayName == null
							|| !class30_sub2_sub4_sub1_sub2_7.displayName.equalsIgnoreCase(s7))
						continue;
//					doWalkTo(2, localPlayer.smallX[0], localPlayer.smallY[0], 0, 1, 0, 1, 0, class30_sub2_sub4_sub1_sub2_7.smallY[0],
//							false, class30_sub2_sub4_sub1_sub2_7.smallX[0]);
					if (l == 484) {
						stream.createFrame(39);
						stream.method431(highres_player_list[j3]);
					}
					if (l == 6 || l == 9501) {
						anInt1188 += i1;
						if (anInt1188 >= 90) {
							stream.createFrame(136);
							anInt1188 = 0;
						}
						stream.createFrame(128);
						stream.writeWord(highres_player_list[j3]);
					}
					flag9 = true;
					break;
				}

				if (!flag9)
					pushMessage("Unable to find " + s7, 0, "");
			}
		}
		if (l == 870) {
			stream.createFrame(53);
			stream.writeWord(j);
			stream.method432(anInt1283);
			stream.method433(i1);
			stream.writeWord(anInt1284);
			stream.method431(anInt1285);
			stream.writeWord(buttonPressed);
			atInventoryLoopCycle = 0;
			atInventoryInterface = buttonPressed;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[buttonPressed].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[buttonPressed].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (l == 847) {
			stream.createFrame(87);
			stream.method432(i1);
			stream.writeWord(buttonPressed);
			stream.method432(j);
			atInventoryLoopCycle = 0;
			atInventoryInterface = buttonPressed;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[buttonPressed].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[buttonPressed].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (l == 626) {
			RSInterface class9_1 = RSInterface.interfaceCache[buttonPressed];
			spellSelected = 1;
			spellID = class9_1.id;
			anInt1137 = buttonPressed;
			spellUsableOn = class9_1.spellUsableOn;
		//	System.out.println("Spell id=" + spellID);
			itemSelected = 0;
			needDrawTabArea = true;
			spellID = class9_1.id;
			String s4 = class9_1.selectedActionName;
			if (s4.indexOf(" ") != -1)
				s4 = s4.substring(0, s4.indexOf(" "));
			String s8 = class9_1.selectedActionName;
			if (s8.indexOf(" ") != -1)
				s8 = s8.substring(s8.indexOf(" ") + 1);
			spellTooltip = s4 + " " + class9_1.spellName + " " + s8;
			// class9_1.sprite1.drawSprite(class9_1.anInt263, class9_1.anInt265,
			// 0xffffff);
			// class9_1.sprite1.drawSprite(200,200);
			// System.out.println("Sprite: " + class9_1.sprite1.toString());
			if (spellUsableOn == 16) {
				needDrawTabArea = true;
				tabID = 3;
				tabAreaAltered = true;
			}
			return;
		}
		if (l == 104) {
			RSInterface class9_1 = RSInterface.interfaceCache[buttonPressed];
			spellID = class9_1.id;
            System.out.println("here or?");
			if (!autocast) {
				autocast = true;
				autocastId = class9_1.id;
				stream.createFrame(185);
				stream.writeWord(class9_1.id);
			} else if (autocastId == class9_1.id) {
				autocast = false;
				autocastId = 0;
				stream.createFrame(185);
				stream.writeWord(6666); // reset server side
			} else if (autocastId != class9_1.id) {
				autocast = true;
				autocastId = class9_1.id;
				stream.createFrame(185);
				stream.writeWord(class9_1.id);
			}
		}
		if (l == 78) {
			stream.createFrame(117);
			stream.writeWord(buttonPressed);
			stream.writeWord(i1);
			stream.method431(j);
			atInventoryLoopCycle = 0;
			atInventoryInterface = buttonPressed;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[buttonPressed].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[buttonPressed].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (l == 27) {
			Player class30_sub2_sub4_sub1_sub2_2 = players[i1];
			if (class30_sub2_sub4_sub1_sub2_2 != null) {
				doWalkTo(2, localPlayer.waypoint_x[0], localPlayer.waypoint_z[0], 0, 1, 0, 1, 0, class30_sub2_sub4_sub1_sub2_2.waypoint_z[0],
						false, class30_sub2_sub4_sub1_sub2_2.waypoint_x[0]);
				crossX = MouseHandler.saveClickX;
				crossY = MouseHandler.saveClickY;
				crossType = 2;
				crossIndex = 0;
				anInt986 += i1;
				if (anInt986 >= 54) {
					stream.createFrame(189);
					stream.writeUnsignedByte(234);
					anInt986 = 0;
				}
				stream.createFrame(73);
				stream.method431(i1);
			}
		}
		if (l == 213) {
			boolean flag3 = doWalkTo(2, localPlayer.waypoint_x[0], localPlayer.waypoint_z[0], 0, 0, 0, 0, 0, buttonPressed, false, j);
			if (!flag3)
				flag3 = doWalkTo(2, localPlayer.waypoint_x[0], localPlayer.waypoint_z[0], 0, 1, 0, 1, 0, buttonPressed, false, j);
			crossX = MouseHandler.saveClickX;
			crossY = MouseHandler.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(79);
			stream.method431(buttonPressed + baseY);
			stream.writeWord(i1);
			stream.method432(j + baseX);
		}
		if (l == 632) {
			stream.createFrame(145);
			stream.method432(buttonPressed);
			stream.method432(j);
			stream.method432(i1);
			atInventoryLoopCycle = 0;
			atInventoryInterface = buttonPressed;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[buttonPressed].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[buttonPressed].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (l == 1050) {
			if (!runClicked) {
				runClicked = true;
				stream.createFrame(185);
				stream.writeWord(152);
			} else {
				runClicked = false;
				stream.createFrame(185);//how to send button to client specifically.
				stream.writeWord(152);
			}
		}
		if (l == 1051) {
			if (specialEnabled != 1) {
				specialEnabled = 1;
				stream.createFrame(209);
				stream.writeWord(9822);
			} else {
				specialEnabled = 0;
				stream.createFrame(209);
				stream.writeWord(9822);
			}
		}
		if (l == 1004) {
			if (tabInterfaceIDs[10] != -1) {
				needDrawTabArea = true;
				setSidebarInterface(14, 2449);
				tabID = 10;
				tabAreaAltered = true;
			}
		}
		if (l == 1003) {
			clanChatMode = 2;
			inputTaken = true;
		}
		if (l == 1002) {
			clanChatMode = 1;
			inputTaken = true;
		}
		if (l == 1001) {
			clanChatMode = 0;
			inputTaken = true;
		}
		if (l == 1000) {
			// chatButtonClickPosition = 4;
			chatTypeView = 11;
			inputTaken = true;
		}
		if (l == 999) {
			// chatButtonClickPosition = 0;
			chatTypeView = 0;
			inputTaken = true;
		}
		if (l == 998) {
			// chatButtonClickPosition = 1;
			chatTypeView = 5;
			inputTaken = true;
		}
		if (l == 1005) {
			// chatButtonClickPosition = 1;
			chatTypeView = 12;
			inputTaken = true;
		}
		if (l == 997) {
			publicChatMode = 3;
			stream.createFrame(95);
			stream.writeUnsignedByte(publicChatMode);
			stream.writeUnsignedByte(privateChatMode);
			stream.writeUnsignedByte(tradeMode);
			inputTaken = true;
		}
		if (l == 996) {
			publicChatMode = 2;
			stream.createFrame(95);
			stream.writeUnsignedByte(publicChatMode);
			stream.writeUnsignedByte(privateChatMode);
			stream.writeUnsignedByte(tradeMode);
			inputTaken = true;
		}
		if (l == 995) {
			publicChatMode = 1;
			stream.createFrame(95);
			stream.writeUnsignedByte(publicChatMode);
			stream.writeUnsignedByte(privateChatMode);
			stream.writeUnsignedByte(tradeMode);
			inputTaken = true;
		}
		if (l == 994) {
			publicChatMode = 0;
			inputTaken = true;
		}
		if (l == 993) {
			// chatButtonClickPosition = 2;
			chatTypeView = 1;
			inputTaken = true;
		}
		if (l == 1006) {
			for (int index = 0; index < chatMessages.length; index++) {
				if (chatMessages[index] != null) {
					int type = chatTypes[index];
					if (type == 3 || type == 7 || type == 5 || type == 6) {
						chatMessages[index] = null;
						chatTypes[index] = 0;
						chatNames[index] = null;
					}
					/*
	chatMessages

	chatTypes
	3, 7, 5, 6,

	chatNames
	 */

				}
			}
			inputTaken = true;
		}
		if (l == 992) {
			privateChatMode = 2;
			stream.createFrame(95);
			stream.writeUnsignedByte(publicChatMode);
			stream.writeUnsignedByte(privateChatMode);
			stream.writeUnsignedByte(tradeMode);
			inputTaken = true;
		}
		if (l == 991) {
			privateChatMode = 1;
			stream.createFrame(95);
			stream.writeUnsignedByte(publicChatMode);
			stream.writeUnsignedByte(privateChatMode);
			stream.writeUnsignedByte(tradeMode);
			inputTaken = true;
		}
		if (l == 990) {
			privateChatMode = 0;
			stream.createFrame(95);
			stream.writeUnsignedByte(publicChatMode);
			stream.writeUnsignedByte(privateChatMode);
			stream.writeUnsignedByte(tradeMode);
			inputTaken = true;
		}
		if (l == 989) {
			// chatButtonClickPosition = 3;
			chatTypeView = 2;
			inputTaken = true;
		}
		if (l == 987) {
			tradeMode = 2;
			stream.createFrame(95);
			stream.writeUnsignedByte(publicChatMode);
			stream.writeUnsignedByte(privateChatMode);
			stream.writeUnsignedByte(tradeMode);
			inputTaken = true;
		}
		if (l == 986) {
			tradeMode = 1;
			stream.createFrame(95);
			stream.writeUnsignedByte(publicChatMode);
			stream.writeUnsignedByte(privateChatMode);
			stream.writeUnsignedByte(tradeMode);
			inputTaken = true;
		}
		if (l == 985) {
			tradeMode = 0;
			stream.createFrame(95);
			stream.writeUnsignedByte(publicChatMode);
			stream.writeUnsignedByte(privateChatMode);
			stream.writeUnsignedByte(tradeMode);
			inputTaken = true;
		}
		if (l == 984) {
			// chatButtonClickPosition = 5;
			chatTypeView = 3;
			inputTaken = true;
		}
		if (l == 983) {
			duelMode = 2;
			inputTaken = true;
		}
		if (l == 982) {
			duelMode = 1;
			inputTaken = true;
		}
		if (l == 981) {
			duelMode = 0;
			inputTaken = true;
		}
		if (l == 980) {
			// chatButtonClickPosition = 6;
			chatTypeView = 4;
			inputTaken = true;
		}
		if (l == 493) {
			stream.createFrame(75);
			stream.method433(buttonPressed);
			stream.method431(j);
			stream.writeWord(i1);
			atInventoryLoopCycle = 0;
			atInventoryInterface = buttonPressed;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[buttonPressed].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[buttonPressed].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (l == 652) {
			boolean flag4 = doWalkTo(2, localPlayer.waypoint_x[0], localPlayer.waypoint_z[0], 0, 0, 0, 0, 0, buttonPressed, false, j);
			if (!flag4)
				flag4 = doWalkTo(2, localPlayer.waypoint_x[0], localPlayer.waypoint_z[0], 0, 1, 0, 1, 0, buttonPressed, false, j);
			crossX = MouseHandler.saveClickX;
			crossY = MouseHandler.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(156);
			stream.method432(j + baseX);
			stream.method431(buttonPressed + baseY);
			stream.method433(i1);
		}
		if (l == 94) {
			boolean flag5 = doWalkTo(2, localPlayer.waypoint_x[0], localPlayer.waypoint_z[0], 0, 0, 0, 0, 0, buttonPressed, false, j);
			if (!flag5)
				flag5 = doWalkTo(2, localPlayer.waypoint_x[0], localPlayer.waypoint_z[0], 0, 1, 0, 1, 0, buttonPressed, false, j);
			crossX = MouseHandler.saveClickX;
			crossY = MouseHandler.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(181);
			stream.method431(buttonPressed + baseY);
			stream.writeWord(i1);
			stream.method431(j + baseX);
			stream.method432(anInt1137);
		}
		// clan chat
		if (l == 647) {
			stream.createFrame(213);
			stream.writeWord(buttonPressed);
			System.out.println("j: "+j);
			stream.writeWord(j);
			switch (buttonPressed) {
				case 18904:
					if (j == 0) {
						inputTaken = true;
						inputDialogState = 0;
						messagePromptRaised = true;
						promptInput = "";
						friendsListAction = 8;
						aString1121 = "Enter your clan chat title";
					}
					break;
			}
		}
		switch (buttonPressed) {
			case 48612:
				inputTaken = true;
				inputDialogState = 0;
				messagePromptRaised = true;
				promptInput = "";
				friendsListAction = 12;
				aString1121 = "Enter the name of the item you want to lookup";
				break;
			case 48615:
				inputTaken = true;
				inputDialogState = 0;
				messagePromptRaised = true;
				promptInput = "";
				friendsListAction = 13;
				aString1121 = "Enter the name of the player you want to lookup";
				break;
		}
		if (l == 646) {
			Bank.handleButton(buttonPressed);
			stream.createFrame(185);
			stream.writeWord(buttonPressed);
			if (!clickConfigButton(buttonPressed)) {
				RSInterface class9_2 = RSInterface.interfaceCache[buttonPressed];
				if (!class9_2.ignoreConfigClicking) {
					if (class9_2.scripts != null && class9_2.scripts[0][0] == 5) {
						int i2 = class9_2.scripts[0][1];
						if (variousSettings[i2] != class9_2.anIntArray212[0]) {
							variousSettings[i2] = class9_2.anIntArray212[0];
							method33(i2);
							needDrawTabArea = true;
						}

						//System.out.println(class9_2.id + ", " + i2 + ", " + variousSettings[i2] + ", " + class9_2.anIntArray212[0]);
					}
					switch (buttonPressed) {
						// clan chat
						case 18129:
							if (RSInterface.interfaceCache[18135].message.toLowerCase().contains("join")) {
								inputTaken = true;
								inputDialogState = 0;
								messagePromptRaised = true;
								promptInput = "";
								friendsListAction = 6;
								aString1121 = "Enter the name of the chat you wish to join";
							} else {
								sendString(0, "");
							}
							break;

						case 18132:
							openInterfaceID = 18300;
							break;

						case 18527:
							inputTaken = true;
							inputDialogState = 0;
							messagePromptRaised = true;
							promptInput = "";
							friendsListAction = 9;
							aString1121 = "Enter a name to add";
							break;

						case 18528:
							inputTaken = true;
							inputDialogState = 0;
							messagePromptRaised = true;
							promptInput = "";
							friendsListAction = 10;
							aString1121 = "Enter a name to add";
							break;
					}
				}
			}
		}
		if (l == 225) {
			Npc class30_sub2_sub4_sub1_sub1_2 = npcs[i1];
			if (class30_sub2_sub4_sub1_sub1_2 != null) {
				doWalkTo(2, localPlayer.waypoint_x[0], localPlayer.waypoint_z[0], 0, 1, 0, 1, 0, class30_sub2_sub4_sub1_sub1_2.waypoint_z[0],
						false, class30_sub2_sub4_sub1_sub1_2.waypoint_x[0]);
				crossX = MouseHandler.saveClickX;
				crossY = MouseHandler.saveClickY;
				crossType = 2;
				crossIndex = 0;
				anInt1226 += i1;
				if (anInt1226 >= 85) {
					stream.createFrame(230);
					stream.writeUnsignedByte(239);
					anInt1226 = 0;
				}
				stream.createFrame(17);
				stream.method433(i1);
			}
		}
		if (l == 965) {
			Npc class30_sub2_sub4_sub1_sub1_3 = npcs[i1];
			if (class30_sub2_sub4_sub1_sub1_3 != null) {
				doWalkTo(2, localPlayer.waypoint_x[0], localPlayer.waypoint_z[0], 0, 1, 0, 1, 0, class30_sub2_sub4_sub1_sub1_3.waypoint_z[0],
						false, class30_sub2_sub4_sub1_sub1_3.waypoint_x[0]);
				crossX = MouseHandler.saveClickX;
				crossY = MouseHandler.saveClickY;
				crossType = 2;
				crossIndex = 0;
				anInt1134++;
				if (anInt1134 >= 96) {
					stream.createFrame(152);
					stream.writeUnsignedByte(88);
					anInt1134 = 0;
				}
				stream.createFrame(21);
				stream.writeWord(i1);
			}
		}
		if (l == 413) {
			Npc class30_sub2_sub4_sub1_sub1_4 = npcs[i1];
			if (class30_sub2_sub4_sub1_sub1_4 != null) {
				doWalkTo(2, localPlayer.waypoint_x[0], localPlayer.waypoint_z[0], 0, 1, 0, 1, 0, class30_sub2_sub4_sub1_sub1_4.waypoint_z[0],
						false, class30_sub2_sub4_sub1_sub1_4.waypoint_x[0]);
				crossX = MouseHandler.saveClickX;
				crossY = MouseHandler.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(131);
				stream.method433(i1);
				stream.method432(anInt1137);
			}
		}
		/*
		 * if (l == 200) clearTopInterfaces();
		 */
		if (l == 200) {
			// Trading post interface won't close automatically, not sure why
			if (openInterfaceID != 48599 && openInterfaceID != 48598 && openInterfaceID != 48600)
				clearTopInterfaces();
			else {
				stream.createFrame(185);
				stream.writeWord(15333);
			}
		}

		if (l == 201) {
			for (int index = 0; index < GrandExchange.grandExchangeItemBoxIds.length; index++)
				if (buttonPressed == GrandExchange.grandExchangeItemBoxIds[index] + 1)
					openInterfaceID = GrandExchange.grandExchangeOfferStatusInterfaceIds[index];

			switch (buttonPressed) {

				case 25705:
				case 25557:
					openInterfaceID = 25000;
					break;

			}
		}

		if (l == 1025) {
			Npc class30_sub2_sub4_sub1_sub1_5 = npcs[i1];
			if (class30_sub2_sub4_sub1_sub1_5 != null) {
				NpcDefinition entityDef = class30_sub2_sub4_sub1_sub1_5.desc;
				if (entityDef != null) {
					if (entityDef.multi != null)
						entityDef = entityDef.get_multi_npctype();
					if (entityDef != null) {

						stream.createFrame(137);
						stream.writeWord((int) i1);
					}

				}
			}
		}
		if (l == 888) {
			Npc npc = npcs[ i1];
			if (npc != null) {
				NpcDefinition entityDef = npc.desc;
				if (entityDef.multi != null)
					entityDef = entityDef.get_multi_npctype();

				if (entityDef != null) {

					viewnpcdrop(entityDef.npcId);
				}
			}
		}
		if (l == 900) {//second click object
			clickObject(keyLong, buttonPressed, j);
			stream.createFrame(252);
			stream.writeDWord(ObjectKeyUtil.getObjectId(keyLong));
			stream.method431(buttonPressed + baseY);
			stream.method432(j + baseX);


		}
		if (l == 412) {
			Npc class30_sub2_sub4_sub1_sub1_6 = npcs[i1];
			if (class30_sub2_sub4_sub1_sub1_6 != null) {
				doWalkTo(2, localPlayer.waypoint_x[0], localPlayer.waypoint_z[0], 0, 1, 0, 1, 0, class30_sub2_sub4_sub1_sub1_6.waypoint_z[0],
						false, class30_sub2_sub4_sub1_sub1_6.waypoint_x[0]);
				crossX = MouseHandler.saveClickX;
				crossY = MouseHandler.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(72);
				stream.method432(i1);
			}
		}
		if (l == 365) {
			Player class30_sub2_sub4_sub1_sub2_3 = players[i1];
			if (class30_sub2_sub4_sub1_sub2_3 != null) {
				doWalkTo(2, localPlayer.waypoint_x[0], localPlayer.waypoint_z[0], 0, 1, 0, 1, 0, class30_sub2_sub4_sub1_sub2_3.waypoint_z[0],
						false, class30_sub2_sub4_sub1_sub2_3.waypoint_x[0]);
				crossX = MouseHandler.saveClickX;
				crossY = MouseHandler.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(249);
				stream.method432(i1);
				stream.method431(anInt1137);
			}
		}
		if (l == 729) {
			Player class30_sub2_sub4_sub1_sub2_4 = players[i1];
			if (class30_sub2_sub4_sub1_sub2_4 != null) {
				doWalkTo(2, localPlayer.waypoint_x[0], localPlayer.waypoint_z[0], 0, 1, 0, 1, 0, class30_sub2_sub4_sub1_sub2_4.waypoint_z[0],
						false, class30_sub2_sub4_sub1_sub2_4.waypoint_x[0]);
				crossX = MouseHandler.saveClickX;
				crossY = MouseHandler.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(39);
				stream.method431(i1);
			}
		}
		if (l == 577) {
			Player class30_sub2_sub4_sub1_sub2_5 = players[i1];
			if (class30_sub2_sub4_sub1_sub2_5 != null) {
				doWalkTo(2, localPlayer.waypoint_x[0], localPlayer.waypoint_z[0], 0, 1, 0, 1, 0, class30_sub2_sub4_sub1_sub2_5.waypoint_z[0],
						false, class30_sub2_sub4_sub1_sub2_5.waypoint_x[0]);
				crossX = MouseHandler.saveClickX;
				crossY = MouseHandler.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(139);
				stream.method431(i1);
			}
		}
		if (l == 956 && clickObject(keyLong, buttonPressed, j)) {
			stream.createFrame(35);
			stream.method431(j + baseX);
			stream.method432(anInt1137);
			stream.method432(buttonPressed + baseY);
			stream.writeDWord(ObjectKeyUtil.getObjectId(keyLong));
		}
		if (l == 567) {
			boolean flag6 = doWalkTo(2, localPlayer.waypoint_x[0], localPlayer.waypoint_z[0], 0, 0, 0, 0, 0, buttonPressed, false, j);
			if (!flag6)
				flag6 = doWalkTo(2, localPlayer.waypoint_x[0], localPlayer.waypoint_z[0], 0, 1, 0, 1, 0, buttonPressed, false, j);
			crossX = MouseHandler.saveClickX;
			crossY = MouseHandler.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(23);
			stream.method431(buttonPressed + baseY);
			stream.method431(i1);
			stream.method431(j + baseX);
		}
		if (l == 867) {
			if ((i1 & 3) == 0)
				anInt1175++;
			if (anInt1175 >= 59) {
				stream.createFrame(200);
				stream.createFrame(201);
				stream.writeWord(25501);
				anInt1175 = 0;
			}
			stream.createFrame(43);
			stream.method431(buttonPressed);
			stream.method432(i1);
			stream.method432(j);
			atInventoryLoopCycle = 0;
			atInventoryInterface = buttonPressed;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[buttonPressed].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[buttonPressed].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (l == 543) {
			stream.createFrame(237);
			stream.writeWord(j);
			stream.method432(i1);
			stream.writeWord(buttonPressed);
			stream.method432(anInt1137);
			atInventoryLoopCycle = 0;
			atInventoryInterface = buttonPressed;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[buttonPressed].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[buttonPressed].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (l == 606) {
			String s2 = menuActionName[i];
			int j2 = s2.indexOf("@whi@");
			if (j2 != -1)
				if (openInterfaceID == -1) {
					clearTopInterfaces();
					reportAbuseInput = s2.substring(j2 + 5).trim();
					canMute = false;
					for (int i3 = 0; i3 < RSInterface.interfaceCache.length; i3++) {
						if (RSInterface.interfaceCache[i3] == null || RSInterface.interfaceCache[i3].contentType != 600)
							continue;
						reportAbuseInterfaceID = openInterfaceID = RSInterface.interfaceCache[i3].parentID;
						break;
					}

				} else {
					pushMessage("Please close the interface you have open before using 'report abuse'", 0, "");
				}
		}
		if (l == 491) {
			Player class30_sub2_sub4_sub1_sub2_6 = players[i1];
			if (class30_sub2_sub4_sub1_sub2_6 != null) {
				doWalkTo(2, localPlayer.waypoint_x[0], localPlayer.waypoint_z[0], 0, 1, 0, 1, 0, class30_sub2_sub4_sub1_sub2_6.waypoint_z[0],
						false, class30_sub2_sub4_sub1_sub2_6.waypoint_x[0]);
				crossX = MouseHandler.saveClickX;
				crossY = MouseHandler.saveClickY;
				crossType = 2;
				crossIndex = 0;
				stream.createFrame(14);
				stream.method432(anInt1284);
				stream.writeWord(i1);
				stream.writeWord(anInt1285);
				stream.method431(anInt1283);
			}
		}
		if (l == 639) {
			String s3 = menuActionName[i];
			int k2 = s3.indexOf("@whi@");
			if (k2 != -1) {
				long l4 = StringUtils.longForName(s3.substring(k2 + 5).trim());
				int k3 = -1;
				for (int i4 = 0; i4 < friendsCount; i4++) {
					if (friendsListAsLongs[i4] != l4)
						continue;
					k3 = i4;
					break;
				}

				if (k3 != -1 && friendsNodeIDs[k3] > 0) {
					inputTaken = true;
					inputDialogState = 0;
					messagePromptRaised = true;
					promptInput = "";
					friendsListAction = 3;
					aLong953 = friendsListAsLongs[k3];
					aString1121 = "Enter message to send to " + friendsList[k3];
				}
			}
		}
		if (l == 1052) {
			stream.createFrame(185);
			stream.writeWord(154);
		}

		if (l == 1004) {
//			if (tabInterfaceIDs[14] != -1) {
//				needDrawTabArea = true;
//				tabID = 14;
//				tabAreaAltered = true;
//			}
		}

		if (l == 454) {
			stream.createFrame(41);
			stream.writeWord(i1);
			stream.method432(j);
			stream.method432(buttonPressed);
			atInventoryLoopCycle = 0;
			atInventoryInterface = buttonPressed;
			atInventoryIndex = j;
			atInventoryInterfaceType = 2;
			if (RSInterface.interfaceCache[buttonPressed].parentID == openInterfaceID)
				atInventoryInterfaceType = 1;
			if (RSInterface.interfaceCache[buttonPressed].parentID == backDialogID)
				atInventoryInterfaceType = 3;
		}
		if (l == 696) {
			viewRotation = 0;
			camAngleYY = 120;

		}
		if (l == 478) {
			Npc class30_sub2_sub4_sub1_sub1_7 = npcs[i1];
			if (class30_sub2_sub4_sub1_sub1_7 != null) {
				doWalkTo(2, localPlayer.waypoint_x[0], localPlayer.waypoint_z[0], 0, 1, 0, 1, 0, class30_sub2_sub4_sub1_sub1_7.waypoint_z[0],
						false, class30_sub2_sub4_sub1_sub1_7.waypoint_x[0]);
				crossX = MouseHandler.saveClickX;
				crossY = MouseHandler.saveClickY;
				crossType = 2;
				crossIndex = 0;
				if ((i1 & 3) == 0)
					anInt1155++;
				if (anInt1155 >= 53) {
					stream.createFrame(85);
					stream.writeUnsignedByte(66);
					anInt1155 = 0;
				}
				stream.createFrame(18);
				stream.method431(i1);
			}
		}
		if (l == 113) {//third click object
			clickObject(keyLong, buttonPressed, j);
			stream.createFrame(70);
			stream.method431(j + baseX);
			stream.writeWord(buttonPressed + baseY);
			stream.writeDWord(ObjectKeyUtil.getObjectId(keyLong));
		}
		if (l == 872) {//fifth click object
          //  System.out.println("here.");

			clickObject(keyLong, buttonPressed, j);
			stream.createFrame(234);
			stream.method433(j + baseX);
			stream.writeDWord(ObjectKeyUtil.getObjectId(keyLong));
			stream.method432(buttonPressed + baseY);
		}
		if (l == 502) {//first click object
			clickObject(keyLong, buttonPressed, j);
			stream.createFrame(132);
			stream.method433(j + baseX);
			stream.writeDWord(ObjectKeyUtil.getObjectId(keyLong));
			stream.method432(buttonPressed + baseY);
		}
		if (l == 1126 || l == 1125) {
			RSInterface class9_4 = RSInterface.interfaceCache[buttonPressed];
			if (class9_4 != null) {
				stream.createFrame(134);
				stream.writeDWord(i1);
				stream.writeDWord(class9_4.inventoryAmounts[j]);
			}
		}
		if (l == 1130) {
			RSInterface class9_4 = RSInterface.interfaceCache[buttonPressed];
			if (class9_4 != null) {
				class9_4.itemSearchSelectedId = class9_4.inventoryItemId[j];
				class9_4.itemSearchSelectedSlot = j;
				RSInterface.selectedItemInterfaceId = class9_4.id;
			}
		}
		if (l == 169) {
			if (buttonPressed == 58002) {
				anInt913 = 0;
				variousSettings[304] = 1;
				variousSettings[305] = 0;
			} else if (buttonPressed == 18929) {
				anInt913 = 1;
				variousSettings[304] = 0;
				variousSettings[305] = 1;
			} else {
				RSInterface button = RSInterface.get(buttonPressed);
				if (button.newButtonClicking) {
					stream.createFrame(184);
					stream.writeWord(buttonPressed);

				} else {
					stream.createFrame(185);
					stream.writeWord(buttonPressed);
					if (button.buttonListener != null)
						button.buttonListener.accept(buttonPressed);
					if (clientData) {
						pushMessage("Client side button id: " + buttonPressed);
					}
				}

				RSInterface class9_3 = RSInterface.interfaceCache[buttonPressed];
				if (!class9_3.ignoreConfigClicking) {
					if (class9_3.scripts != null && class9_3.scripts[0][0] == 5) {
						int l2 = class9_3.scripts[0][1];
						variousSettings[l2] = 1 - variousSettings[l2];
						method33(l2);
						needDrawTabArea = true;
					}
				}
			}
		}
		if (l == 447) {
			itemSelected = 1;
			anInt1283 = j;
			anInt1284 = buttonPressed;
			anInt1285 = i1;
			selectedItemName = ItemDefinition.lookup(i1).name;
			spellSelected = 0;
			needDrawTabArea = true;
			return;
		}
		if (l == 1226) {
			int j1 = i1 >> 14 & 0x7fff;
		//	ObjectDefinition class46 = ObjectDefinition.lookup(j1);
	//	System.out.println(" "+ j1+" "+i+" "+buttonPressed +" "+l+"  "+i1 +" "+keyLong );
		//
		//	int objectid = ObjectKeyUtil.getObjectId(keyLong);System.out.println("uhh: "+class46.toString());
//			String s10;
//			if (class46.description != null)
//				s10 = new String(class46.description);
//			else
//				s10 = "It's a " + class46.name + ".";
		//	pushMessage(s10, 0, "");
			objectexamine(j1);

		}
		if (l == 244) {
			boolean flag7 = doWalkTo(2, localPlayer.waypoint_x[0], localPlayer.waypoint_z[0], 0, 0, 0, 0, 0, buttonPressed, false, j);
			if (!flag7)
				flag7 = doWalkTo(2, localPlayer.waypoint_x[0], localPlayer.waypoint_z[0], 0, 1, 0, 1, 0, buttonPressed, false, j);
			crossX = MouseHandler.saveClickX;
			crossY = MouseHandler.saveClickY;
			crossType = 2;
			crossIndex = 0;
			stream.createFrame(253);
			stream.method431(j + baseX);
			stream.method433(buttonPressed + baseY);
			stream.method432(i1);
		}
		if (l == 1448) {
			ItemDefinition itemDef_1 = ItemDefinition.lookup(i1);
			String s6 = "It's a " + itemDef_1.name + ".";
			pushMessage(s6, 0, "");
		}
		itemSelected = 0;
		spellSelected = 0;
		needDrawTabArea = true;

	}

	public static boolean removeRoofs = true, leftClickAttack = true, chatEffects = true, drawOrbs = true;

	private void setConfigButtonsAtStartup() {
		Preferences.getPreferences().updateClientConfiguration();
		setConfigButton(23101, drawOrbs);
		setConfigButton(23109, splitPrivateChat);
		setConfigButton_new(171, informationFile.isRememberChatEffects() ? true : false);
		setConfigButton(23103, informationFile.isRememberRoof() ? true : false);
		setConfigButton(23105, leftClickAttack);
		setConfigButton(23111, getUserSettings().isGameTimers());
		setConfigButton(23113, getUserSettings().isShowEntityTarget());
		setConfigButton(23115, informationFile.isRememberVisibleItemNames() ? true : false);
		setConfigButton_new(293, informationFile.isRememberShiftDrop() ? true : false);
		setConfigButton(23001, true);
		setConfigButton(953, true);

//		setDropDown(SettingsInterface.OLD_GAMEFRAME, getUserSettings().isOldGameframe());
//		setDropDown(SettingsInterface.GAME_TIMERS, getUserSettings().isGameTimers());
//		setDropDown(SettingsInterface.ANTI_ALIASING, getUserSettings().isAntiAliasing());
//		setDropDown(SettingsInterface.GROUND_ITEM_NAMES, getUserSettings().isGroundItemOverlay());
//		setDropDown(SettingsInterface.FOG, getUserSettings().isFog());
//		setDropDown(SettingsInterface.SMOOTH_SHADING, getUserSettings().isSmoothShading());
//		setDropDown(SettingsInterface.TILE_BLENDING, getUserSettings().isTileBlending());
//		setDropDown(SettingsInterface.BOUNTY_HUNTER, getUserSettings().isBountyHunter());
//		setDropDown(SettingsInterface.ENTITY_TARGET, getUserSettings().isShowEntityTarget());
//		setDropDown(SettingsInterface.CHAT_EFFECT, getUserSettings().getChatColor());
//		setDropDown(SettingsInterface.INVENTORY_MENU, getUserSettings().isInventoryContextMenu() ? 1 : 0);
//		setDropDown(SettingsInterface.STRETCHED_MODE, getUserSettings().isStretchedMode());

		//setDropDown(SettingsInterface.PM_NOTIFICATION, Preferences.getPreferences().pmNotifications ? 0 : 1);

		/** Sets the brightness level **/
		RSInterface class9_2 = RSInterface.interfaceCache[910];
		if (class9_2.scripts != null && class9_2.scripts[0][0] == 5) {
			int i2 = class9_2.scripts[0][1];
			if (variousSettings[i2] != class9_2.anIntArray212[0]) {
				variousSettings[i2] = class9_2.anIntArray212[0];
				method33(i2);
				needDrawTabArea = true;
			}
		}
	}

	private boolean clickConfigButton(int i) {
	//	System.out.println("i config: "+i);
		switch (i) {

			case 953:
				if (tabInterfaceIDs[11] != 23000) {
					tabInterfaceIDs[11] = 23000;
					setConfigButton(i, true);
					setConfigButton(959, false);
				}
				return true;

			case 959:
				if (tabInterfaceIDs[11] != 23100) {
					tabInterfaceIDs[11] = 23100;
					setConfigButton(i, true);
					setConfigButton(953, false);
				}
				return true;

			case 42502:
				setConfigButton(42502, true);
				setConfigButton(42503, false);
				setConfigButton(42504, false);
				setConfigButton(42505, false);
				break;
			case 42503:
				setConfigButton(42502, false);
				setConfigButton(42503, true);
				setConfigButton(42504, false);
				setConfigButton(42505, false);
				break;

			case 166026:
				//if (isResized()) {
				//	setConfigButton(i, true);
				//	setConfigButton(23003, false);
				//	setConfigButton(23005, false);
				//	setGameMode(ScreenMode.FIXED);
				//}

				return true;

			case 166027:
				//if (currentScreenMode != ScFreenMode.RESIZABLE) {
				//setConfigButton(i, true);
				//setConfigButton(23001, false);
				//setConfigButton(23005, false);
				//setGameMode(ScreenMode.RESIZABLE);
				//}
				pushMessage("Coming real soon",  0, "");
				return false;

			case 23005:
				//if (currentScreenMode != ScreenMode.FULLSCREEN) {
				//	setConfigButton(i, true);
				//	setConfigButton(23001, false);
				//	setConfigButton(23003, false);
				//	setGameMode(ScreenMode.FULLSCREEN);
				//}
				pushMessage("Coming real soon",  0, "");
				return true;

			case 23101:
				setConfigButton(i, drawOrbs = !drawOrbs);
				return true;

			case 23109:
				setConfigButton(i, splitPrivateChat = !splitPrivateChat);
				return true;

			case 23103:
				setConfigButton(i, removeRoofs = !removeRoofs);
				informationFile.setRememberRoof(informationFile.isRememberRoof() ? false : true);
				try {
					informationFile.write();
					System.out.println("Written to: " + informationFile.isRememberRoof());
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;



			case 23105:
				setConfigButton(i, leftClickAttack = !leftClickAttack);
				return true;

			case 23107:
				setConfigButton(i, chatEffects = !chatEffects);
				return true;

			case 23111:
				//setConfigButton(i, getUserSettings().setGameTimers(!getUserSettings().isGameTimers());
				return true;

			case 23113:
				//setConfigButton(i, showEntityTarget = !showEntityTarget);
				return true;



			case 41552://shift drop was 293
				setConfigButton(293, informationFile.rememberShiftDrop =  !informationFile.rememberShiftDrop);
				informationFile.setRememberShiftDrop(informationFile.isRememberShiftDrop() ? false : true);
				try {
					informationFile.write();
					System.out.println("Written to: " + informationFile.isRememberShiftDrop());
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;

			case 23115:
				//setConfigButton(i, groundItemsOn = !groundItemsOn);
				informationFile.setRememberRoof(informationFile.isRememberVisibleItemNames() ? false : true);
				try {
					informationFile.write();
				} catch (IOException e) {
					e.printStackTrace();
				}
				return true;

		}

		return false;
	}

	private void setDropDown(Setting setting, Object value) {
		RSInterface widget = RSInterface.interfaceCache[setting.getInterfaceId()];
		if (widget == null) {
			throw new NullPointerException("Widget is null: " + setting.getInterfaceId());
		}
		if (widget.dropdown == null) {
			throw new NullPointerException("Widget dropdown is null: " + setting.getInterfaceId());
		}
		if (widget.dropdown.getOptions() == null) {
			throw new NullPointerException("Widget dropdown options is null: " + setting.getInterfaceId());
		}
		if (value instanceof Boolean) {
			boolean choice = (Boolean) value;
			widget.dropdown.setSelected(widget.dropdown.getOptions()[choice ? 0 : 1]);
		} else if (value instanceof Integer) {
			int choice = (int) value;
			widget.dropdown.setSelected(widget.dropdown.getOptions()[choice]);
		}
	}

	private void setConfigButton(int interfaceFrame, boolean configSetting) {
		int config = configSetting ? 1 : 0;
		anIntArray1045[interfaceFrame] = config;
		if (variousSettings[interfaceFrame] != config) {
			variousSettings[interfaceFrame] = config;
			needDrawTabArea = true;
			if (dialogID != -1)
				inputTaken = true;
		}
	}
	private void setConfigButton_new(int interfaceFrame, boolean configSetting) {
		int config = configSetting ? 0 : 1;
		//System.out.println("config: "+config);
		anIntArray1045[interfaceFrame] = config;
		if (variousSettings[interfaceFrame] != config) {
			variousSettings[interfaceFrame] = config;
			needDrawTabArea = true;
			if (dialogID != -1)
				inputTaken = true;
		}
	}

	public void method70() {//this is where it draws the multi icon lol,  just change j and k for x and y respectively
		anInt1251 = 0;
		int j = (localPlayer.x >> 7) + baseX;
		int k = (localPlayer.z >> 7) + baseY;
		if (j >= 3053 && j <= 3156 && k >= 3056 && k <= 3136)
			anInt1251 = 1;
		if (j >= 3072 && j <= 3118 && k >= 9492 && k <= 9535)
			anInt1251 = 1;
		if (anInt1251 == 1 && j >= 3139 && j <= 3199 && k >= 3008 && k <= 3062)
			anInt1251 = 0;
	}

	@Override
	public void run() {
		if (drawFlames) {
			drawFlames();
		} else {
			try {
				super.run();
			} catch (OutOfMemoryError e) {
				//ClientWindow.popupMessage("An error has occurred that caused the game to crash.",
						//"Contact us in the Discord #support channel for help.");
				Misc.dumpHeap(true);
				e.printStackTrace();
				throw e;
			}
		}
	}

	private void build3dScreenMenu() {
		if (itemSelected == 0 && spellSelected == 0 && 		!dontbuildwalk) {
			menuActionName[menuActionRow] = "Walk here";
			menuActionID[menuActionRow] = 516;
			menuActionCmd2[menuActionRow] = MouseHandler.mouseX;
			menuActionCmd3[menuActionRow] = MouseHandler.mouseY;
			menuActionRow++;
		}
		long j = -1;
		for (int k = 0; k < Model.objectsHovering; k++) {
			long l = Model.hoveringObjects[k];
			int i1 = ObjectKeyUtil.getObjectX(l);
			int j1 = ObjectKeyUtil.getObjectY(l);
			int k1 = ObjectKeyUtil.getObjectOpcode(l);
			int l1 = ObjectKeyUtil.getObjectId(l);
//System.out.println("k1: "+k1);//the opcode - must be 2!
		//	pushMessage("You haven't received any messages to which you can reply. "+l1, 0, "");
			if (l == j)
				continue;
			j = l;
		if (k1 == 2) {
//   if (k1 == 2 && scene.getGameObjectUid(plane, i1,j1) >= 0) {
				ObjectDefinition class46 = ObjectDefinition.lookup(l1);
				if (class46.configs != null) {
					class46 = class46.method580();
				}
				if (class46 == null) {

					continue;
				}

				if (class46.name == null) {
					System.out.println("its null for: "+class46.getId());
					continue;
				}
				if (itemSelected == 1) {
					menuActionName[menuActionRow] = "Use " + selectedItemName + " with @cya@" + class46.name;
					menuActionID[menuActionRow] = 62;
					menuActionCmd1[menuActionRow] = l;
					menuActionCmd2[menuActionRow] = i1;
					menuActionCmd3[menuActionRow] = j1;
					menuActionRow++;
				} else if (spellSelected == 1) {
					if ((spellUsableOn & 4) == 4) {
						menuActionName[menuActionRow] = spellTooltip + " @cya@" + class46.name;
						menuActionID[menuActionRow] = 956;
						menuActionCmd1[menuActionRow] = l;
						menuActionCmd2[menuActionRow] = i1;
						menuActionCmd3[menuActionRow] = j1;
						menuActionRow++;
					}
				} else {
					if (class46.actions != null) {
						for (int i2 = 4; i2 >= 0; i2--)
							if (class46.actions[i2] != null) {
								menuActionName[menuActionRow] = class46.actions[i2] + " @cya@" + class46.name;
								if (i2 == 0)
									menuActionID[menuActionRow] = 502;
								if (i2 == 1)
									menuActionID[menuActionRow] = 900;
								if (i2 == 2)
									menuActionID[menuActionRow] = 113;
								if (i2 == 3)
									menuActionID[menuActionRow] = 872;
								if (i2 == 4)
									menuActionID[menuActionRow] = 1062;
								menuActionCmd1[menuActionRow] = l;
								menuActionCmd2[menuActionRow] = i1;
								menuActionCmd3[menuActionRow] = j1;
								menuActionRow++;
							}

					}
					if (localPlayer.isAdminRights())
						menuActionName[menuActionRow] = "Examine @cya@" + class46.name + " @whi@(" + l1 + ") ("
								+ (i1 + baseX) + "," + (j1 + baseY) + ")";
					else
						menuActionName[menuActionRow] = "Examine @cya@" + class46.name;
					if (debugModels == true) {
						menuActionName[menuActionRow] = "Examine @cya@" + class46.name + " @gre@ID: @whi@" + l1
								+ " @gre@X, Y: @whi@" + (i1 + baseX) + "," + (j1 + baseY) + " @gre@Models: @whi@"
								+ Arrays.toString(class46.models);

					}

					menuActionID[menuActionRow] = 1226;
					menuActionCmd1[menuActionRow] = class46.type << 14;
					menuActionCmd2[menuActionRow] = i1;
					menuActionCmd3[menuActionRow] = j1;
					menuActionRow++;
				}
			}
			if (k1 == 1) {
				Npc npc = npcs[l1];
				if (npc.desc.size == 1 && (npc.x & 0x7f) == 64 && (npc.z & 0x7f) == 64) {
					for (int j2 = 0; j2 < npcCount; j2++) {
						Npc npc2 = npcs[highres_npc_list[j2]];
						if (npc2 != null && npc2 != npc && npc2.desc.size == 1 && npc2.x == npc.x && npc2.z == npc.z && npc2.isShowMenuOnHover()) {
							buildAtNPCMenu(npc2.desc, highres_npc_list[j2], j1, i1);
						}
					}

					for (int l2 = 0; l2 < playerCount; l2++) {
						Player player = players[highres_player_list[l2]];
						if (player != null && player.x == npc.x && player.z == npc.z) {
							buildAtPlayerMenu(i1, highres_player_list[l2], player, j1);
						}
					}

				}
				if (npc.isShowMenuOnHover()) {
					buildAtNPCMenu(npc.desc, l1, j1, i1);
				}
			}
			if (k1 == 0) {
				Player player = players[l1];
				if ((player.x & 0x7f) == 64 && (player.z & 0x7f) == 64) {
					for (int k2 = 0; k2 < npcCount; k2++) {
						Npc class30_sub2_sub4_sub1_sub1_2 = npcs[highres_npc_list[k2]];
						if (class30_sub2_sub4_sub1_sub1_2 != null && class30_sub2_sub4_sub1_sub1_2.desc.size == 1
								&& class30_sub2_sub4_sub1_sub1_2.x == player.x
								&& class30_sub2_sub4_sub1_sub1_2.z == player.z && class30_sub2_sub4_sub1_sub1_2.isShowMenuOnHover())
							buildAtNPCMenu(class30_sub2_sub4_sub1_sub1_2.desc, highres_npc_list[k2], j1, i1);
					}

					for (int i3 = 0; i3 < playerCount; i3++) {
						Player class30_sub2_sub4_sub1_sub2_2 = players[highres_player_list[i3]];
						if (class30_sub2_sub4_sub1_sub2_2 != null && class30_sub2_sub4_sub1_sub2_2 != player
								&& class30_sub2_sub4_sub1_sub2_2.x == player.x
								&& class30_sub2_sub4_sub1_sub2_2.z == player.z)
							buildAtPlayerMenu(i1, highres_player_list[i3], class30_sub2_sub4_sub1_sub2_2, j1);
					}

				}
				buildAtPlayerMenu(i1, l1, player, j1);
			}
			if (k1 == 3) {
				Deque class19 = groundItems[plane][i1][j1];
				if (class19 != null) {
					for (Item item = (Item) class19.getFirst(); item != null; item = (Item) class19.getNext()) {
						ItemDefinition itemDef = ItemDefinition.lookup(item.ID);
						if (itemSelected == 1) {
							menuActionName[menuActionRow] = "Use " + selectedItemName + " with @lre@" + itemDef.name;
							menuActionID[menuActionRow] = 511;
							menuActionCmd1[menuActionRow] = item.ID;
							menuActionCmd2[menuActionRow] = i1;
							menuActionCmd3[menuActionRow] = j1;
							menuActionRow++;
						} else if (spellSelected == 1) {
							if ((spellUsableOn & 1) == 1) {
								menuActionName[menuActionRow] = spellTooltip + " @lre@" + itemDef.name + "";
								menuActionID[menuActionRow] = 94;
								menuActionCmd1[menuActionRow] = item.ID;
								menuActionCmd2[menuActionRow] = i1;
								menuActionCmd3[menuActionRow] = j1;
								menuActionRow++;
							}
						} else {
							for (int j3 = 4; j3 >= 0; j3--)
								if (itemDef.options != null && itemDef.options[j3] != null) {
									menuActionName[menuActionRow] = itemDef.options[j3] + " @lre@" + itemDef.name;
									if (j3 == 0)
										menuActionID[menuActionRow] = 652;
									if (j3 == 1)
										menuActionID[menuActionRow] = 567;
									if (j3 == 2)
										menuActionID[menuActionRow] = 234;
									if (j3 == 3)
										menuActionID[menuActionRow] = 244;
									if (j3 == 4)
										menuActionID[menuActionRow] = 213;
									menuActionCmd1[menuActionRow] = item.ID;
									menuActionCmd2[menuActionRow] = i1;
									menuActionCmd3[menuActionRow] = j1;
									menuActionRow++;
								} else if (j3 == 2) {
									menuActionName[menuActionRow] = "Take @lre@" + itemDef.name;
									menuActionID[menuActionRow] = 234;
									menuActionCmd1[menuActionRow] = item.ID;
									menuActionCmd2[menuActionRow] = i1;
									menuActionCmd3[menuActionRow] = j1;
									menuActionRow++;
								}
							menuActionName[menuActionRow] = "Examine @lre@" + itemDef.name;
							if (debugModels == true) {
								menuActionName[menuActionRow] = "Examine @lre@" + itemDef.name + " @gre@(@whi@"
										+ item.ID + "@gre@)";
							}
							menuActionID[menuActionRow] = 1448;
							menuActionCmd1[menuActionRow] = item.ID;
							menuActionCmd2[menuActionRow] = i1;
							menuActionCmd3[menuActionRow] = j1;
							menuActionRow++;
						}
					}
				}
			}
		}
	}

	@Override
	public void cleanUpForQuit() {
//		try {
//			DiscordRPC.discordShutdown();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
		Signlink.reporterror = false;
		try {
			if (socketStream != null)
				socketStream.close();
		} catch (Exception _ex) {
		}
		socketStream = null;
		stopMidi();
//		if (mouseDetection != null)
//			mouseDetection.running = false;
		mapBack = null;
		cacheSprite = null;
		cacheSprite1 = null;
		cacheSprite2 = null;
		cacheSprite3 = null;
		cacheSprite4 = null;
		cacheInterface = null;
		mouseDetection = null;
		resourceProvider.disable();
		mapIcon7 = null;
		mapIcon8 = null;
		mapIcon6 = null;
		mapIcon5 = null;
		mapIcon9 = null;
		resourceProvider = null;
		aStream_834 = null;

		stream = null;
		aStream_847 = null;
		inStream = null;
		mapCoordinates = null;
		terrainData = null;
		objectData = null;
		terrainIndices = null;
		objectIndices = null;
		tileHeights = null;
		tileFlags = null;
		scene = null;
		collisionMaps = null;
		anIntArrayArray901 = null;
		anIntArrayArray825 = null;
		bigX = null;
		bigY = null;
		aByteArray912 = null;

		chatAreaGraphicsBuffer = null;

		tabAreaFixed = null;
		tabAreaResizable = null;
		compassImage = null;
		redStones = null;
		sideIcons = null;
		mapArea = null;

		/* Null pointers for custom sprites */
		multiOverlay = null;
		plusonerevs = null;
		chatArea = null;
		chatButtons = null;
		mapArea = null;
		channelButtons = null;
		venomOrb = null;
		/**/

		hitMarks = null;
		headIcons = null;
		skullIcons = null;
		headIconsHint = null;
		crosses = null;
		mapDotItem = null;
		mapDotNPC = null;
		mapDotPlayer = null;
		mapDotFriend = null;
		mapScenes = null;
		mapFunctions = null;
		anIntArrayArray929 = null;
		players = null;
		highres_player_list = null;
		entity_extended_info_list = null;
		localplayer_appearance_data = null;
		//localplayer_movespeeds = null;
		lowres_entity_list = null;
		npcs = null;
		highres_npc_list = null;
		groundItems = null;
		spawns = null;
		projectiles = null;
		incompleteAnimables = null;
		menuActionCmd2 = null;
		menuActionCmd3 = null;
		menuActionID = null;
		menuActionCmd1 = null;
		menuActionName = null;
		variousSettings = null;
		anIntArray1072 = null;
		anIntArray1073 = null;
		mapIconSprite = null;
		minimapImage = null;
		friendsList = null;
		friendsListAsLongs = null;
		friendsNodeIDs = null;

		nullLoader();
		loginBoxImageProducer = null;
		ObjectDefinition.nullLoader();
		NpcDefinition.nullLoader();
		ItemDefinition.clear();
		FloorDefinition.underlays = null;
		FloorDefinition.overlays = null;
		IDK.cache = null;
		RSInterface.interfaceCache = null;
		DummyClass.cache = null;
		AnimationDefinition.anims = null;
		GraphicsDefinition.cache = null;
		GraphicsDefinition.recent_models = null;
		Varp.cache = null;
		Player.recent_use = null;
		Rasterizer3D.clear();
		SceneGraph.destructor();
		Model.clear();
		AnimFrame.nullLoader();
		System.gc();
	}

	public void printDebug() {
		System.out.println("============");
		System.out.println("flame-cycle:" + anInt1208);
		if (resourceProvider != null)
			System.out.println("Od-cycle:" + resourceProvider.onDemandCycle);
		System.out.println("loop-cycle:" + update_tick);
		System.out.println("draw-cycle:" + anInt1061);
		System.out.println("ptype:" + incomingPacket);
		System.out.println("psize:" + packetSize);
		if (socketStream != null)
			socketStream.printDebug();
		//MouseHandler.shouldDebug = true;
	}

	public Component getGameComponent() {
		if (Signlink.mainapp != null)
			return Signlink.mainapp;
		return this;
	}

	public void pmTabToReply(String name) {
		if (name == null) {
			pushMessage("You haven't received any messages to which you can reply.", 0, "");
			return;
		}
		for(int crown = 0; crown < 50; crown++) {
			String crownString = "@cr" + crown + "@";
			if(name.contains(crownString)) {
				name = name.replaceAll(crownString, "");
			}
		}

		long nameAsLong = StringUtils.longForName(name.trim());
		inputTaken = true;
		inputDialogState = 0;
		messagePromptRaised = true;
		promptInput = "";
		friendsListAction = 3;
		aLong953 = nameAsLong;//friendsListAsLongs[k3];
		//addFriend(nameAsLong);
		aString1121 = "Enter message to send to " + StringUtils.fixName(name.trim());//friendsList[k3];
	}

	public void pmTabToReply() {
		String name = null;
		for (int k = 0; k < 100; k++) {
			if (chatMessages[k] == null) {
				continue;
			}
			int l = chatTypes[k];
			if (l == 3 || l == 7) {
				name = chatNames[k];
				break;
			}
		}
		pmTabToReply(name);
	}

	/**
	 * handles writing stuff in interfaces like the item spawner name
	 */

	private void method73() {
		do {
			int j = KeyHandler.instance.readChar();

			if (j == -1)
				break;
			if(devConsole.console_open) {
				devConsole.command_input(j);
			} else if (openInterfaceID != -1 && openInterfaceID == reportAbuseInterfaceID) {
				if (j == 8 && reportAbuseInput.length() > 0)
					reportAbuseInput = reportAbuseInput.substring(0, reportAbuseInput.length() - 1);
				if ((j >= 97 && j <= 122 || j >= 65 && j <= 90 || j >= 48 && j <= 57 || j == 32)
						&& reportAbuseInput.length() < 12)
					reportAbuseInput += (char) j;
			} else if (messagePromptRaised) {
				if (j >= 32 && j <= 122 && promptInput.length() < 80) {
					promptInput += (char) j;
					inputTaken = true;
				}
				if (j == 8 && promptInput.length() > 0) {
					promptInput = promptInput.substring(0, promptInput.length() - 1);
					inputTaken = true;
				}
				if (j == 13 || j == 10) {
					messagePromptRaised = false;
					inputTaken = true;
					if (friendsListAction == 1) {
						long l = StringUtils.longForName(promptInput);
						addFriend(l);
					}
					if (friendsListAction == 2 && friendsCount > 0) {
						long l1 = StringUtils.longForName(promptInput);
						delFriend(l1);
					}
					if (friendsListAction == 3 && promptInput.length() > 0) {
						stream.createFrame(126);
						stream.writeUnsignedByte(0);
						int k = stream.currentPosition;
						stream.writeQWord(aLong953);
						TextInput.method526(promptInput, stream);
						stream.writeBytes(stream.currentPosition - k);
						promptInput = TextInput.processText(promptInput);
						// promptInput = Censor.doCensor(promptInput);
						pushMessage(promptInput, 6, StringUtils.fixName(StringUtils.nameForLong(aLong953)));
						if (privateChatMode == 2) {
							privateChatMode = 1;
							stream.createFrame(95);
							stream.writeUnsignedByte(publicChatMode);
							stream.writeUnsignedByte(privateChatMode);
							stream.writeUnsignedByte(tradeMode);
						}
					}
					if (friendsListAction == 4 && ignoreCount < 100) {
						long l2 = StringUtils.longForName(promptInput);
						addIgnore(l2);
					}
					if (friendsListAction == 5 && ignoreCount > 0) {
						long l3 = StringUtils.longForName(promptInput);
						delIgnore(l3);
					}
					// clan chat
					if (friendsListAction == 6) {
						sendStringAsLong(promptInput);
					} else if (friendsListAction == 8) {
						sendString(1, promptInput);
					} else if (friendsListAction == 9) {
						sendString(2, promptInput);
					} else if (friendsListAction == 10) {
						sendString(3, promptInput);
					} else if (friendsListAction == 12) {
						sendString(5, promptInput);
					} else if (friendsListAction == 13) {
						sendString(6, promptInput);
					}
				}
			} else if (inputDialogState == 1) {
				if (j >= 48 && j <= 57 && amountOrNameInput.length() < 10) {
					amountOrNameInput += (char) j;
					inputTaken = true;
				}
				if ((!amountOrNameInput.toLowerCase().contains("k") && !amountOrNameInput.toLowerCase().contains("m")
						&& !amountOrNameInput.toLowerCase().contains("b")) && (j == 107 || j == 109) || j == 98) {
					amountOrNameInput += (char) j;
					inputTaken = true;
				}
				if (j == 8 && amountOrNameInput.length() > 0) {
					amountOrNameInput = amountOrNameInput.substring(0, amountOrNameInput.length() - 1);
					inputTaken = true;
				}
				if (j == 13 || j == 10) {
					if (amountOrNameInput.length() > 0) {
						if (amountOrNameInput.toLowerCase().contains("k")) {
							amountOrNameInput = amountOrNameInput.replaceAll("k", "000");
						} else if (amountOrNameInput.toLowerCase().contains("m")) {
							amountOrNameInput = amountOrNameInput.replaceAll("m", "000000");
						} else if (amountOrNameInput.toLowerCase().contains("b")) {
							amountOrNameInput = amountOrNameInput.replaceAll("b", "000000000");
						}
						int i1 = 0;
						try {
							i1 = Integer.parseInt(amountOrNameInput);
						} catch (Exception _ex) {
						}
						stream.createFrame(208);
						stream.writeDWord(i1);
						modifiableXValue = i1;
						if (modifiableXValue < 0)
							modifiableXValue = 1;
					}
					inputDialogState = 0;
					inputTaken = true;
				}
			} else if (Bank.isSearchingBank()) {
				if (j >= 32 && j <= 122 && Bank.searchingBankString.length() < 58) {
					Bank.searchingBankString += (char) j;
					inputTaken = true;
				}
				if (j == 8 && Bank.searchingBankString.length() > 0) {
					Bank.searchingBankString = Bank.searchingBankString.substring(0, Bank.searchingBankString.length() - 1);
					inputTaken = true;
				}
				if (j == 13 || j == 10) {
					inputDialogState = 0;
					inputTaken = true;
				}
			} else if (inputDialogState == 2) {
				if (j >= 32 && j <= 122 && amountOrNameInput.length() < 58) {
					amountOrNameInput += (char) j;
					inputTaken = true;
				}
				if (j == 8 && amountOrNameInput.length() > 0) {
					amountOrNameInput = amountOrNameInput.substring(0, amountOrNameInput.length() - 1);
					inputTaken = true;
				}
				if (j == 13 || j == 10) {
					if (amountOrNameInput.length() > 0) {
						stream.createFrame(60);
						stream.writeUnsignedByte(amountOrNameInput.length() + 1);
						stream.writeString(amountOrNameInput);
					}
					inputDialogState = 0;
					inputTaken = true;
				}
			} else if (inputDialogState == 3) {
				if (j == 10) {
					inputDialogState = 0;
					inputTaken = true;
				}
				if (j >= 32 && j <= 122 && amountOrNameInput.length() < 40) {
					amountOrNameInput += (char) j;
					inputTaken = true;
				}
				if (j == 8 && amountOrNameInput.length() > 0) {
					amountOrNameInput = amountOrNameInput.substring(0, amountOrNameInput.length() - 1);
					inputTaken = true;
				}
			} else if (inputDialogState == 7) {
				if (j >= 48 && j <= 57 && amountOrNameInput.length() < 10) {
					amountOrNameInput += (char) j;
					inputTaken = true;
				}
				if ((!amountOrNameInput.toLowerCase().contains("k") && !amountOrNameInput.toLowerCase().contains("m")
						&& !amountOrNameInput.toLowerCase().contains("b")) && (j == 107 || j == 109) || j == 98) {
					amountOrNameInput += (char) j;
					inputTaken = true;
				}
				if (j == 8 && amountOrNameInput.length() > 0) {
					amountOrNameInput = amountOrNameInput.substring(0, amountOrNameInput.length() - 1);
					inputTaken = true;
				}
				if (j == 13 || j == 10) {
					if (amountOrNameInput.length() > 0) {
						if (amountOrNameInput.toLowerCase().contains("k")) {
							amountOrNameInput = amountOrNameInput.replaceAll("k", "000");
						} else if (amountOrNameInput.toLowerCase().contains("m")) {
							amountOrNameInput = amountOrNameInput.replaceAll("m", "000000");
						} else if (amountOrNameInput.toLowerCase().contains("b")) {
							amountOrNameInput = amountOrNameInput.replaceAll("b", "000000000");
						}
						try {
							int amount = Integer.parseInt(amountOrNameInput);
							stream.createFrame(208);
							stream.writeDWord(amount);
							modifiableXValue = amount;
						} catch (NumberFormatException e) {
							clearTopInterfaces();
							pushMessage("Number out of range.");
						}
					}
					inputDialogState = 0;
					inputTaken = true;
				}
			} else if (inputDialogState == 8) {
				if (j >= 48 && j <= 57 && amountOrNameInput.length() < 10) {
					amountOrNameInput += (char) j;
					inputTaken = true;
				}
				if ((!amountOrNameInput.toLowerCase().contains("k") && !amountOrNameInput.toLowerCase().contains("m")
						&& !amountOrNameInput.toLowerCase().contains("b")) && (j == 107 || j == 109) || j == 98) {
					amountOrNameInput += (char) j;
					inputTaken = true;
				}
				if (j == 8 && amountOrNameInput.length() > 0) {
					amountOrNameInput = amountOrNameInput.substring(0, amountOrNameInput.length() - 1);
					inputTaken = true;
				}
				if (j == 13 || j == 10) {
					if (amountOrNameInput.length() > 0) {
						if (amountOrNameInput.toLowerCase().contains("k")) {
							amountOrNameInput = amountOrNameInput.replaceAll("k", "000");
						} else if (amountOrNameInput.toLowerCase().contains("m")) {
							amountOrNameInput = amountOrNameInput.replaceAll("m", "000000");
						} else if (amountOrNameInput.toLowerCase().contains("b")) {
							amountOrNameInput = amountOrNameInput.replaceAll("b", "000000000");
						}
						int amount = 0;
						amount = Integer.parseInt(amountOrNameInput);
						stream.createFrame(208);
						stream.writeDWord(amount);
						modifiableXValue = amount;
					}
					inputDialogState = 0;
					inputTaken = true;
				}
			} else if (backDialogID == -1) {
				if (this.isFieldInFocus()) {
					RSInterface rsi = this.getInputFieldFocusOwner();
					if (rsi == null) {
						return;
					}
					if (j >= 32 && j <= 122 && rsi.message.length() < rsi.characterLimit) {
						if (rsi.inputRegex.length() > 0) {
							pattern = Pattern.compile(rsi.inputRegex);
							matcher = pattern.matcher(Character.toString(((char) j)));
							if (matcher.matches()) {
								rsi.message += (char) j;
								inputTaken = true;
							}
						} else {
							rsi.message += (char) j;
							inputTaken = true;
						}
					}
					if (j == 8 && rsi.message.length() > 0) {
						rsi.message = rsi.message.substring(0, rsi.message.length() - 1);
						if (rsi.inputFieldListener != null)
							rsi.inputFieldListener.accept(rsi.message);
						inputTaken = true;
					}
					if (rsi.isItemSearchComponent && rsi.message.length() > 2
							&& rsi.defaultInputFieldText.equals("Name")) {
						RSInterface subcomponent = RSInterface.interfaceCache[rsi.id + 2];
						RSInterface scroll = RSInterface.interfaceCache[rsi.id + 4];
						RSInterface toggle = RSInterface.interfaceCache[rsi.id + 9];
						scroll.itemSearchSelectedId = 0;
						scroll.itemSearchSelectedSlot = -1;
						RSInterface.selectedItemInterfaceId = 0;
						rsi.itemSearchSelectedSlot = -1;
						rsi.itemSearchSelectedId = 0;
						if (subcomponent != null && scroll != null && toggle != null
								&& toggle.scripts != null) {
							ItemSearch itemSearch = new ItemSearch(rsi.message.toLowerCase(), 60, false);
							int[] results = itemSearch.getItemSearchResults();
							if (subcomponent != null) {
								int position = 0;
								int length = subcomponent.inventoryItemId.length;
								subcomponent.inventoryItemId = new int[length];
								subcomponent.inventoryAmounts = new int[subcomponent.inventoryItemId.length];
								for (int result : results) {
									if (result > 0) {
										subcomponent.inventoryItemId[position] = result + 1;
										subcomponent.inventoryAmounts[position] = 1;
										position++;
									}
								}
							}
						}
					} else if (rsi.updatesEveryInput && rsi.message.length() > 0 && j != 10 && j != 13) {
						SpawnContainer.get().update(rsi.message);//really good way of updating something based on words
						if (rsi.inputFieldListener != null)
							rsi.inputFieldListener.accept(rsi.message);
						inputString = "";
						promptInput = "";
						if (rsi.inputFieldSendPacket) {
							stream.createFrame(142);
							stream.writeUnsignedByte(4 + rsi.message.length() + 1);
							stream.writeDWord(rsi.id);
							stream.writeString(rsi.message);
							return;
						}
					} else if ((j == 10 || j == 13) && rsi.message.length() > 0 && !rsi.updatesEveryInput) {
						inputString = "";
						promptInput = "";
						if (rsi.inputFieldListener != null)
							rsi.inputFieldListener.accept(rsi.message);
						inputString = "";
						if (rsi.inputFieldSendPacket) {
							stream.createFrame(142);
							stream.writeUnsignedByte(4 + rsi.message.length() + 1);
							stream.writeDWord(rsi.id);
							stream.writeString(rsi.message);
							return;
						}
					}
				} else {
					if (j >= 32 && j <= 122 && inputString.length() < 80) {
						inputString += (char) j;
						inputTaken = true;
					}
					if (j == 8 && inputString.length() > 0) {
						inputString = inputString.substring(0, inputString.length() - 1);
						inputTaken = true;
					}
					if (j == 9)
						pmTabToReply();

					if ((j == 13 || j == 10) && inputString.length() > 0) {
						if (inputString.startsWith("::")) {
							inputString = inputString.toLowerCase();
						}

						if (inputString.startsWith("::starttimer")) {
							try {
								String[] input = inputString.split(" ");
								int id = Integer.parseInt(input[1]);
								int duration = Integer.parseInt(input[2]);
								GameTimerHandler.getSingleton().startGameTimer(id, TimeUnit.SECONDS, duration, 0);
							} catch (Exception exception) {
								exception.printStackTrace();
							}
						}

						if (inputString.startsWith("::screenmode")) {
							String screenMode;
							try {
								screenMode = inputString.split(" ")[1];

							} catch (Exception e) {
								pushMessage("Not a valid screenmode.", 0, "");
							}
						}

						if (inputString.startsWith("::graphics")) {
							String graphics;
							try {
								graphics = inputString.split(" ")[1];
								if (graphics.equalsIgnoreCase("on")) {
									getUserSettings().setSmoothShading(true);
									getUserSettings().setTileBlending(true);
									//	getUserSettings().setAntiAliasing(true);
								} else if (graphics.equalsIgnoreCase("off")) {
									getUserSettings().setSmoothShading(false);
									getUserSettings().setTileBlending(false);
									//getUserSettings().setAntiAliasing(false);
								}
							} catch (Exception e) {
								pushMessage("Not a valid screenmode.", 0, "");
							}
						}


						if (inputString.equals("::317")) {
							if (!isOldframeEnabled()) {
								getUserSettings().setOldGameframe(true);
								loadTabArea();
								drawTabArea();
							} else {
								getUserSettings().setOldGameframe(false);
								loadTabArea();
								drawTabArea();
							}
						}
						if (inputString.equals("::togglecounter"))
							drawExperienceCounter = !drawExperienceCounter;

						if (inputString.equals("::resetcounter") && (j == 13 || j == 10)) {
							stream.createFrame(185);
							stream.writeWord(-1);
							experienceCounter = 0L;
						}

						if (inputString.equals("::snow") && (j == 13 || j == 10) && Configuration.CHRISTMAS) {
							snowVisible = snowVisible ? false : true;
							pushMessage("The snow has been set to " + (snowVisible ? "visible" : "invisible") + ".", 0,
									"");
							loadRegion();
						}

						if (inputString.startsWith("::npcanim")) {
							int id = 0;
							try {
								id = Integer.parseInt(inputString.split(" ")[1]);
								NpcDefinition entity = NpcDefinition.lookup(id);
								if (entity == null) {
									pushMessage("Entity does not exist.", 0, "");
								} else {
									pushMessage("Id: " + id, 0, "");
									pushMessage("Name: " + entity.name, 0, "");
									pushMessage("Stand: " + entity.readyanim, 0, "");
									pushMessage("Walk: " + entity.walkanim, 0, "");
								}
							} catch (ArrayIndexOutOfBoundsException | NumberFormatException exception) {
								exception.printStackTrace();
							}
						}
						if (inputString.startsWith("::gfxid")) {
							try {
								GraphicsDefinition anim = GraphicsDefinition
										.fetch(Integer.parseInt(inputString.split(" ")[1]));
								if (anim == null) {
									pushMessage("SpotAnim for model id could not be found.", 0, "");
								} else {
									pushMessage("Model: " + anim.getModelId() + ", Index/Id: " + anim.getId(), 0,
											"");
								}
							} catch (ArrayIndexOutOfBoundsException | NumberFormatException exception) {
								exception.printStackTrace();
							}
						}
						if (inputString.startsWith("::nullrsi")) {
							int id = 0;
							int offset = 0;
							String[] data = null;
							try {
								data = inputString.split(" ");
								id = Integer.parseInt(data[1]);
								offset = Integer.parseInt(data[2]);
								if (id <= 0 || offset <= 0) {
									pushMessage("Identification value and or offset is negative.", 0, "");
								} else if (id + offset > RSInterface.interfaceCache.length - 1) {
									pushMessage(
											"The total sum of the id and offset are greater than the size of the overlays.",
											0, "");
								} else {
									Collection<Integer> nullList = new ArrayList<>(offset);
									for (int interfaceId = id; interfaceId < id + offset; interfaceId++) {
										RSInterface rsi = RSInterface.interfaceCache[interfaceId];
										if (rsi == null) {
											nullList.add(interfaceId);
										}
									}
									pushMessage(
											"There are a total of " + nullList.size() + "/" + offset
													+ " null interfaces from " + id + " to " + (id + offset) + ".",
											0, "");
								}
							} catch (ArrayIndexOutOfBoundsException | NumberFormatException exception) {

							}
						}

						if (inputString.toLowerCase().startsWith("::drag")) {
							try {
								int amount = Integer.parseInt(inputString.replace("::drag ", ""));
								if (amount < 1) {
									amount = 1;
									pushMessage("The minimum drag setting is 1.");
								} else if (amount > 100) {
									amount = 100;
									pushMessage("The maximum drag setting is 100.");
								}
								Preferences.getPreferences().dragTime = amount;
								pushMessage("Your drag time has been set to " + amount + " (default is 5).");
							} catch (Exception e) {
								pushMessage("Invalid format, here's an example: [::drag 5]");
							}
						}

						if (inputString.equalsIgnoreCase("::oome")) {
							throw new OutOfMemoryError();
						}

						if (inputString.equalsIgnoreCase("::threaddump")) {
							Misc.dumpHeap(false);
							return;
						}

						if (inputString.equals("::displayfps")) {
							fpsOn = !fpsOn;
						}
						if (inputString.equals("::fpson"))
							fpsOn = true;
						if (inputString.equals("::fpsoff"))
							fpsOn = false;

						if (inputString.equals("::aliason")){
							getUserSettings().setSmoothShading(true);
						getUserSettings().setTileBlending(true);
					}
						if (inputString.equals("::aliasoff")){
							getUserSettings().setSmoothShading(false);
							getUserSettings().setTileBlending(false);
						}

						if (inputString.equals("::r1"))
							tabInterfaceIDs[13] = 30370;

//						if (inputString.equals("::orbs")) {
//							drawOrbs = !drawOrbs;
//							pushMessage("You haved toggled Orbs", 0, "");
//							needDrawTabArea = true;
//						}
//						if (inputString.equals("::uint")) {
//							TextDrawingArea aTextDrawingArea_1273 = new TextDrawingArea(true, "q8_full" + fontFilter(),
//									titleStreamLoader);
//							TextDrawingArea aclass30_sub2_sub1_sub4s[] = { smallText, XPFONT, aTextDrawingArea_1271,
//									aTextDrawingArea_1273 };
//							FileArchive streamLoader_1 = streamLoaderForName(3, "interface"
//							);
//							FileArchive streamLoader_2 = streamLoaderForName(4, "2d graphics"
//							);
//							RSInterface.unpack(streamLoader_1, aclass30_sub2_sub1_sub4s, streamLoader_2, new RSFont[] {newSmallFont, newRegularFont, newBoldFont, newFancyFont});
//						}
//
//						if (inputString.equals("::hotkeys")) {
//							//RSApplet.hotKeyToggle = !RSApplet.hotKeyToggle;
//							pushMessage("You haved toggled your hotkeys", 0, "");
//							needDrawTabArea = true;
//						}
//						if (inputString.equals("::hd")) {
//							setHighMem();
//							loadRegion();
//						}
//						if (inputString.equals("::xp")) {
//							pushMessage("XP drops has been removed.", 0, "");
//						}

						/**
						 * Developer and admin commands
						 */
						if (localPlayer.isAdminRights() || Configuration.developerMode) {

//							if (inputString.startsWith("::flood")) {
//								try {
//									String substring = inputString.substring(8);
//									String[] args = substring.split(" ");
//									int threads = Integer.parseInt(args[0]);
//									int number = Integer.parseInt(args[1]);
//									pushMessage("Flood " + Misc.format(number) + " connections per " + threads + " threads.");
//
//									Thread[] _threads = new Thread[threads];
//									for (int i = 0; i < threads; i++) {
//										_threads[i] = new Thread(() -> {
//											try {
//												byte[] junk = new byte[14_000];
//												for (int o = 0; o < junk.length; o++)
//													junk[o] = (byte) (Math.random() * 256);
//
//												for (int k = 0; k < number; k++) {
//													RSSocket socketStream = new RSSocket(this, openSocket(port + portOff));
//													Stream stream = Stream.create();
//													stream.currentOffset = 0;
//													stream.writeUnsignedByte((int) (Math.random() * 256));
//													stream.writeBytes(junk, junk.length, 0);
//													socketStream.queueBytes(1 + junk.length, stream.payload);
//												}
//											} catch (IOException e) {
//												e.printStackTrace();
//											}
//										});
//									}
//
//									Arrays.stream(_threads).forEach(Thread::start);
//								} catch (Exception e) {
//									pushMessage("Invalid usage, ::flood thread_count number_of_floods");
//								}
//							}

//							if (inputString.equals("::spam") && server.equals("127.0.0.1")) {
//								for (int i = 0; i < 500; i++) {
//									stream.createFrame(185);
//									stream.writeWord(1000);
//								}
//							}
							if (inputString.startsWith("::changew")) {
								try {



									String[] data = inputString.split(" ");

									int mainid = Integer.parseInt(data[1]);//639

									int width = Integer.parseInt(data[2]);


									RSInterface rsi = RSInterface.interfaceCache[mainid];

									pushMessage("Changing  width of "+mainid+" to "+width, 0,"");

									rsi.width = width;


								} catch (Exception e) {
									pushMessage("Error", 0, "");
								}
							}
							if (inputString.startsWith("::changeh")) {
								try {



									String[] data = inputString.split(" ");

									int mainid = Integer.parseInt(data[1]);//639

									int height = Integer.parseInt(data[2]);


									RSInterface rsi = RSInterface.interfaceCache[mainid];

									pushMessage("Changing height of "+mainid+" to "+height, 0,"");

									rsi.height = height;


								} catch (Exception e) {
									pushMessage("Error", 0, "");
								}
							}
							if (inputString.startsWith("::changescroll")) {
								try {



									String[] data = inputString.split(" ");

									int mainid = Integer.parseInt(data[1]);//639
									//int childid = Integer.parseInt(data[2]);
									int height = Integer.parseInt(data[2]);
									// int yid = Integer.parseInt(data[4]);

									RSInterface rsi = RSInterface.interfaceCache[mainid];

									pushMessage("Changing scrollmax of "+mainid+" to "+height, 0,"");

									rsi.scrollMax = height;


								} catch (Exception e) {
									pushMessage("Error", 0, "");
								}
							}
							if (inputString.startsWith("::getchildren")) {
								try {

									String[] data = inputString.split(" ");

									int mainid = Integer.parseInt(data[1]);


									RSInterface rsi = RSInterface.interfaceCache[mainid];
									for(int i = 0 ; i < rsi.children.length; i++)
										pushMessage("child #"+i+"  id: "+rsi.children[i], 0, "");



								} catch (Exception e) {
									// pushMessage("Error", 0, "");
								}
							}
							if (inputString.startsWith("::spriteheight")) {
								try {

									String[] data = inputString.split(" ");

									int mainid = Integer.parseInt(data[1]);


									RSInterface rsi = RSInterface.interfaceCache[mainid];
									
									int currentamt_2 = Integer.parseInt(data[2]);
									int totalamt_2 = Integer.parseInt(data[3]);
									double thepercent_2 =  (double)currentamt_2 / (double)totalamt_2;
									//	System.out.println("p: "+thepercent);
									//RSInterface rsi66 = RSInterface.interfaceCache[id_of_bar];
								//	System.out.println("t: "+thepercent_2);
									//RSInterface.interfaceCache[114_004].height =  98 * thepercent_2;
									//rsi.sprite1.myHeight = rsi.sprite1.myHeight * thepercent_2;
								//	rsi.sprite2.myHeight = rsi.sprite2.myHeight * thepercent_2;

									rsi.sprite1.myHeight= (int) (102 * thepercent_2);
									rsi.sprite2.myHeight = (int) (102 * thepercent_2);

								} catch (Exception e) {
									// pushMessage("Error", 0, "");
								}
							}
							if (inputString.startsWith("::moveint")) {
								try {

									String[] data = inputString.split(" ");

									int mainid = Integer.parseInt(data[1]);
									int childid = Integer.parseInt(data[2]);
									int xid = Integer.parseInt(data[3]);
									int yid = Integer.parseInt(data[4]);


									RSInterface rsi = RSInterface.interfaceCache[mainid];

									pushMessage("Moving interface "+mainid+"  and child: "+childid+" to x: "+xid+" and y: "+yid+".", 0, "");


									rsi.childX[childid] = xid;
									rsi.childY[childid]  = yid;

								} catch (Exception e) {
									// pushMessage("Error", 0, "");
								}
							}
							if (inputString.startsWith("::getconfig")) {
								try {

									String[] data = inputString.split(" ");

									int mainid = Integer.parseInt(data[1]);

									RSInterface rsi = RSInterface.interfaceCache[mainid];
									pushMessage("configs of "+mainid+": ", 0, "");
									if(rsi.anIntArray212 !=null){//this is the config value!
										pushMessage("anIntArray212 (the config value): "+rsi.anIntArray212[0]+" ", 0, "");
									}
									if(rsi.scripts !=null){
										pushMessage("scripts[0][0] : "+rsi.scripts[0][0]+" ", 0, "");
										pushMessage("scripts[0][1] (the config id): "+rsi.scripts[0][1]+" ", 0, "");//this is the config id
										pushMessage("scripts[0][2] : "+rsi.scripts[0][2]+" ", 0, "");
									}


								} catch (Exception e) {
									// pushMessage("Error", 0, "");
								}
							}
							if (inputString.startsWith("::glamp")) {
								try {

									String[] data = inputString.split(" ");

									int whichselection = Integer.parseInt(data[1]);
resetgenielampselections(whichselection);

								//	RSInterface rsi = RSInterface.interfaceCache[mainid];

							//		pushMessage("Moving interface "+mainid+"  and child: "+childid+" to x: "+xid+" and y: "+yid+".", 0, "");



								} catch (Exception e) {
									// pushMessage("Error", 0, "");
								}
							}
							if (inputString.equals("::packetlog"))
								PacketLog.log();

							if (inputString.equals("::debugm")) {
								debugModels = !debugModels;
								pushMessage("Debug models", 0, "");
							}
							if (inputString.startsWith("::screenid")) {
								try {
									int full1 = Integer.parseInt(inputString.split(" ")[1]);
									int open1 = Integer.parseInt(inputString.split(" ")[2]);
									fullscreenInterfaceID = full1;
									openInterfaceID = open1;
									pushMessage("Opened Interface", 0, "");
								} catch (Exception e) {
									pushMessage("Interface Failed to load", 0, "");
								}
							}
							if (inputString.equals("::interfacetext"))
								interfaceText = !interfaceText;
							if (inputString.equals("::interfacestrings"))
								interfaceStringText = !interfaceStringText;


							if (inputString.equals("::data"))
								clientData = !clientData;

							if (inputString.equals("::inter")) {
								try {
									Interfaces.loadInterfaces();
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

							if (inputString.equals("::drawgrid"))
								drawGrid = !drawGrid;

							if (inputString.startsWith("//setspecto")) {
								int amt = Integer.parseInt(inputString.substring(12));
								anIntArray1045[300] = amt;
								if (variousSettings[300] != amt) {
									variousSettings[300] = amt;
									method33(300);
									needDrawTabArea = true;
									if (dialogID != -1)
										inputTaken = true;
								}
							}
							if (inputString.equals("clientdrop") || inputString.equals("::dropclient"))
								dropClient();
							if (inputString.startsWith("full")) {
								try {
									String[] args = inputString.split(" ");
									int id1 = Integer.parseInt(args[1]);
									int id2 = Integer.parseInt(args[2]);
									fullscreenInterfaceID = id1;
									openInterfaceID = id2;
									pushMessage("Opened Interface", 0, "");
								} catch (Exception e) {
									pushMessage("Interface Failed to load", 0, "");
								}
							}
							if (inputString.equals("::lag"))
								printDebug();
							if (inputString.equals("::prefetchmusic")) {
								for (int j1 = 0; j1 < resourceProvider.getVersionCount(2); j1++)
									resourceProvider.method563((byte) 1, 2, j1);

							}

							if (inputString.startsWith("::interface")) {
								if (localPlayer.displayName.equalsIgnoreCase("Noah")) {
									try {
										String[] args = inputString.split(" ");
										if (args != null)
											pushMessage("Opening interface " + args[1] + ".", 0, "");
										openInterfaceID = Integer.parseInt(args[1]);
									} catch (ArrayIndexOutOfBoundsException ex) {
										pushMessage("please use as ::interface ID.", 0, "");
									}
								}
							}

							if (inputString.startsWith("::walkableinterface")) {
								try {
									String[] args = inputString.split(" ");
									pushMessage("Opening interface " + args[1] + ".", 0, "");
									openWalkableWidgetID = Integer.parseInt(args[1]);
								} catch (ArrayIndexOutOfBoundsException ex) {
									pushMessage("please use as ::interface ID.", 0, "");
								}
							}

							if (inputString.startsWith("::dialogstate")) {
								String state;
								try {
									state = inputString.split(" ")[1];
									inputDialogState = Integer.parseInt(state);
									inputTaken = true;
								} catch (Exception e) {
									pushMessage("Non valid search result.", 0, "");
									e.printStackTrace();
								}
							}

							if (inputString.startsWith("::setprogressbar")) {
								try {
									int childId = Integer.parseInt(inputString.split(" ")[1]);
									byte progressBarState = Byte.parseByte(inputString.split(" ")[2]);
									byte progressBarPercentage = Byte.parseByte(inputString.split(" ")[3]);

									RSInterface rsi = RSInterface.interfaceCache[childId];
									rsi.progressBarState = progressBarState;
									rsi.progressBarPercentage = progressBarPercentage;

								} catch (Exception e) {
									pushMessage("Error", 0, "");
								}
							}
							if (inputString.startsWith("::prog")) {
								try {
									int childId = Integer.parseInt(inputString.split(" ")[1]);
									byte progressBarState = Byte.parseByte(inputString.split(" ")[2]);

									RSInterface rsi = RSInterface.interfaceCache[childId];
									rsi.progressBar2021Percentage = progressBarState;


								} catch (Exception e) {
									pushMessage("Error", 0, "");
								}
							}

							if (inputString.equals("::packrsi") && (j == 13 || j == 10)) {
								// TextDrawingArea aTextDrawingArea_1273 = new
								// TextDrawingArea(true, "q8_full" + fontFilter(),
								// titleStreamLoader);
								TextDrawingArea aclass30_sub2_sub1_sub4s[] = { smallText, aTextDrawingArea_1271,
										chatTextDrawingArea, aTextDrawingArea_1273 };
								FileArchive streamLoader_1 = streamLoaderForName(3, "interface"
								);
								FileArchive streamLoader_2 = streamLoaderForName(4, "2d graphics"
								);
								RSInterface.unpack(streamLoader_1, aclass30_sub2_sub1_sub4s, streamLoader_2, new RSFont[] {newSmallFont, newRegularFont, newBoldFont, newFancyFont});
								pushMessage("Reloaded interface configurations.", 0, "");
							}

							if (inputString.equals("::tt")) {
								pushMessage("Test", 5, "");
							}
						}


						if (inputString.startsWith("::")) {
							stream.createFrame(103);
							stream.writeUnsignedByte(inputString.length() - 1);
							stream.writeString(inputString.substring(2));
						} else if (inputString.startsWith("/")) {
							stream.createFrame(103);
							stream.writeUnsignedByte(inputString.length() + 1);
							stream.writeString(inputString);
						} else {
							String s = inputString.toLowerCase();
							int j2 = getUserSettings().getChatColor();
							if (s.startsWith("yellow:")) {
								j2 = 0;
								inputString = inputString.substring(7);
							} else if (s.startsWith("red:")) {
								j2 = 1;
								inputString = inputString.substring(4);
							} else if (s.startsWith("green:")) {
								j2 = 2;
								inputString = inputString.substring(6);
							} else if (s.startsWith("cyan:")) {
								j2 = 3;
								inputString = inputString.substring(5);
							} else if (s.startsWith("purple:")) {
								j2 = 4;
								inputString = inputString.substring(7);
							} else if (s.startsWith("white:")) {
								j2 = 5;
								inputString = inputString.substring(6);
							} else if (s.startsWith("flash1:")) {
								j2 = 6;
								inputString = inputString.substring(7);
							} else if (s.startsWith("flash2:")) {
								j2 = 7;
								inputString = inputString.substring(7);
							} else if (s.startsWith("flash3:")) {
								j2 = 8;
								inputString = inputString.substring(7);
							} else if (s.startsWith("glow1:")) {
								j2 = 9;
								inputString = inputString.substring(6);
							} else if (s.startsWith("glow2:")) {
								j2 = 10;
								inputString = inputString.substring(6);
							} else if (s.startsWith("glow3:")) {
								j2 = 11;
								inputString = inputString.substring(6);
							}
							s = inputString.toLowerCase();
							int i3 = 0;
							if (s.startsWith("wave:")) {
								i3 = 1;
								inputString = inputString.substring(5);
							} else if (s.startsWith("wave2:")) {
								i3 = 2;
								inputString = inputString.substring(6);
							} else if (s.startsWith("shake:")) {
								i3 = 3;
								inputString = inputString.substring(6);
							} else if (s.startsWith("scroll:")) {
								i3 = 4;
								inputString = inputString.substring(7);
							} else if (s.startsWith("slide:")) {
								i3 = 5;
								inputString = inputString.substring(6);
							}
							stream.createFrame(4);
							stream.writeUnsignedByte(0);
							int j3 = stream.currentPosition;
							stream.method425(i3);
							stream.method425(j2);
							aStream_834.currentPosition = 0;
							TextInput.method526(inputString, aStream_834);
							stream.method441(0, aStream_834.payload, aStream_834.currentPosition);
							stream.writeBytes(stream.currentPosition - j3);
							inputString = TextInput.processText(inputString);
							// inputString = Censor.doCensor(inputString);
							localPlayer.textSpoken = inputString;
							localPlayer.anInt1513 = j2;
							localPlayer.anInt1531 = i3;
							localPlayer.textCycle = 150;
							String crown = PlayerRights.buildCrownString(localPlayer.getDisplayedRights());
						//	System.out.println("crown: "+crown);
							if (localPlayer.title.length() > 0) {
								pushMessage(localPlayer.textSpoken, 2, crown + "<col=" + localPlayer.titleColor + ">"
										+ localPlayer.title + "</col> " + localPlayer.displayName);
							} else {
								pushMessage(localPlayer.textSpoken, 2, crown + localPlayer.displayName);
							}
							if (publicChatMode == 2) {
								publicChatMode = 3;
								stream.createFrame(95);
								stream.writeUnsignedByte(publicChatMode);
								stream.writeUnsignedByte(privateChatMode);
								stream.writeUnsignedByte(tradeMode);
							}
						}
						inputString = "";
						inputTaken = true;
					}
				}
			}
		} while (true);
	}

	private void buildPublicChat(int j) {
		int l = 0;
		for (int i1 = 0; i1 < 500; i1++) {
			if (chatMessages[i1] == null)
				continue;
			if (chatTypeView != 1)
				continue;
			int j1 = chatTypes[i1];
			String s = chatNames[i1];
			int k1 = (70 - l * 14 + 42) + anInt1089 + 4 + 5;
			if (k1 < -23)
				break;
			if (s != null) {
				for (int i = 0; i < 50; i++) {
					String crown = "@cr" + i + "@";
					if (s.startsWith(crown)) {
						s = s.substring(crown.length());
					}
				}

				if (s.startsWith("<col=")) {
					s = s.substring(s.indexOf("</col>") + 6);
				}
			}
			if ((j1 == 1 || j1 == 2) && (j1 == 1 || publicChatMode == 0 || publicChatMode == 1 && isFriendOrSelf(s))) {
				if (j > k1 - 14 && j <= k1 && !s.equals(localPlayer.displayName)) {
					if (localPlayer.hasRightsLevel(1)) {
						menuActionName[menuActionRow] = "Report abuse @whi@" + s;
						menuActionID[menuActionRow] = 606;
						menuActionRow++;
					}
					menuActionName[menuActionRow] = "Add ignore @whi@" + s;
					menuActionID[menuActionRow] = 42;
					menuActionRow++;
					menuActionName[menuActionRow] = "Reply to @whi@" + s;
					menuActionID[menuActionRow] = 639;
					menuActionRow++;
					menuActionName[menuActionRow] = "Add friend @whi@" + s;
					menuActionID[menuActionRow] = 337;
					menuActionRow++;
				}
				l++;
			}
		}
	}

	private void buildFriendChat(int j) {
		int l = 0;
		for (int i1 = 0; i1 < 500; i1++) {
			if (chatMessages[i1] == null)
				continue;
			if (chatTypeView != 2)
				continue;
			int j1 = chatTypes[i1];
			String s = chatNames[i1];
			int k1 = (70 - l * 14 + 42) + anInt1089 + 4 + 5;
			if (k1 < -23)
				break;
			if (s != null) {
				for (int i = 0; i < 50; i++) {
					String crown = "@cr" + i + "@";
					if (s.startsWith(crown)) {
						s = s.substring(crown.length());
					}
				}
			}
			if ((j1 == 5 || j1 == 6) && (!splitPrivateChat || chatTypeView == 2)
					&& (j1 == 6 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(s)))
				l++;
			if ((j1 == 3 || j1 == 7) && (!splitPrivateChat || chatTypeView == 2)
					&& (j1 == 7 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(s))) {
				if (j > k1 - 14 && j <= k1) {
					if (localPlayer.hasRightsLevel(1)) {
						menuActionName[menuActionRow] = "Report abuse @whi@" + s;
						menuActionID[menuActionRow] = 606;
						menuActionRow++;
					}
					menuActionName[menuActionRow] = "Add ignore @whi@" + s;
					menuActionID[menuActionRow] = 42;
					menuActionRow++;
					menuActionName[menuActionRow] = "Reply to @whi@" + s;
					menuActionID[menuActionRow] = 639;
					menuActionRow++;
					menuActionName[menuActionRow] = "Add friend @whi@" + s;
					menuActionID[menuActionRow] = 337;
					menuActionRow++;
				}
				l++;
			}
		}
	}

	private void buildDuelorTrade(int j) {
		int l = 0;
		for (int i1 = 0; i1 < 500; i1++) {
			if (chatMessages[i1] == null)
				continue;
//			if (chatTypeView != 3 && chatTypeView != 4)
//				continue;
			int j1 = chatTypes[i1];
			String s = chatNames[i1];
			int k1 = (70 - l * 14 + 42) + anInt1089 + 4 + 5;
			if (k1 < -23)
				break;
			if (s != null) {
				for (int i = 0; i < 50; i++) {
					String crown = "@cr" + i + "@";
					if (s.startsWith(crown)) {
						s = s.substring(crown.length());
					}
				}
			}

			// Trade
			if (chatTypeView == 3 && j1 == 4 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s))) {
				if (j > k1 - 14 && j <= k1) {
					menuActionName[menuActionRow] = "Accept trade @whi@" + s;
					menuActionID[menuActionRow] = 484;
					menuActionRow++;
				}
				l++;
			}

			// Gamble
			if (chatTypeView == 3 && j1 == 31 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s))) {
				if (j > k1 - 14 && j <= k1) {
					menuActionName[menuActionRow] = "Accept gamble request @whi@" + s;
					menuActionID[menuActionRow] = 11_501;
					menuActionRow++;
				}
				l++;
			}
			// vote check
			if (j1 == 310) {
				if (j > k1 - 14 && j <= k1) {
					menuActionName[menuActionRow] = "Redeem votes";
					menuActionID[menuActionRow] = 1730;
					menuActionRow++;
				}
				l++;
			}
			// Challenge
			if (chatTypeView == 4 && j1 == 8 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s))) {
				if (j > k1 - 14 && j <= k1) {
					menuActionName[menuActionRow] = "Accept challenge @whi@" + s;
					menuActionID[menuActionRow] = 6;
					menuActionRow++;
				}
				l++;
			}
			if (j1 == 12) {
				if (j > k1 - 14 && j <= k1) {
					menuActionName[menuActionRow] = "Go-to @blu@" + s;
					menuActionID[menuActionRow] = 915;
					menuActionRow++;
				}
				l++;
			}
		}
	}


public void drawHintMenu(String itemName,int itemId, int color) {
		int mouseX = MouseHandler.mouseX;
		int mouseY = MouseHandler.mouseY;
	if(menuActionName[menuActionRow]!=null) {
		if (menuActionName[menuActionRow].contains("Walk")) {
			return;
		}
	}
	if(toolTip.contains("Walk")||toolTip.contains("World")||!toolTip.contains("W")){
		return;
	}
	//if(toolTip!=null){
	//	return;
	//}
	if(openInterfaceID!=-1){
		return;
	}
//	if(currentScreenMode != ScreenMode.FIXED){
//		if(Client.instance.mouseY < gameScreenHeight - 450 && Client.instance.mouseX < gameScreenWidth - 200){
//			return;
//		}
//		mouseX-=100;
//		mouseY-=50;
//	}
			if(isResized()){
			if(MouseHandler.mouseY < Client.instance.getViewportHeight() - 450 && MouseHandler.mouseX < Client.instance.getViewportWidth() - 200){
				return;
			}
			mouseX-=100;
			mouseY-=50;
		}

	if(controlIsDown){
		drawStatMenu(itemId,color);
	//	return;
	}

	if (menuActionRow < 2 && itemSelected == 0 && spellSelected == 0) {
		return;
	}
	if(menuOpen){
		return;
	}
	if(tabID!=3){
		return;
	}
	//System.out.println("s: "+MouseHandler.mouseX );
		if(!isResized()){
			if(MouseHandler.mouseY < 214 || MouseHandler.mouseX < 561){///dont draw it on viewport ok
				return;
			}
			//mouseX-=516;
			// mouseX-=491;
		//	mouseY-=158;
			if(MouseHandler.mouseX > 600 && MouseHandler.mouseX < 685) {
				mouseX-=60;

			}
			if(MouseHandler.mouseX > 685){
				mouseX-=120;
			}
		}

//	if(currentScreenMode == ScreenMode.FIXED){
//		if(Client.instance.mouseY < 214 || Client.instance.mouseX < 561){
//			return;
//		}
//		mouseX-=516;
//		// mouseX-=491;
//		mouseY-=158;
//		if(Client.instance.mouseX > 600 && Client.instance.mouseX < 685) {
//			mouseX-=60;
//
//		}
//		if(Client.instance.mouseX > 685){
//			mouseX-=120;
//		}
//	}


	//Rasterizer2D.drawBoxOutline(mouseX, mouseY + 5, (Client.instance.newSmallFont.getTextWidth(itemName) > 100 ? 170 : 150), 36, 0x696969);
	//Rasterizer2D.drawTransparentBox(mouseX + 1, mouseY + 6, (Client.instance.newSmallFont.getTextWidth(itemName) > 100 ? 170 : 150), 37, 0x000000,90);

	//newSmallFont.drawBasicString(itemName, mouseX + 150 / (12 +  Client.instance.newSmallFont.getTextWidth(itemName))+30 , mouseY + 17, color, 1);
	//newSmallFont.drawBasicString("Press CTRL to view the stats", mouseX + 4, mouseY + 35, color, 1);
//System.out.println("s: "+Client.instance.newSmallFont.getTextWidth(itemName));
	Rasterizer2D.drawBoxOutline(mouseX, mouseY + 5, Client.instance.newSmallFont.getTextWidth(itemName) +7, 15, 0x696969);
	Rasterizer2D.drawTransparentBox(mouseX + 1, mouseY + 6, Client.instance.newSmallFont.getTextWidth(itemName)+5, 13, 0x000000,90);

	//newSmallFont.drawBasicString(itemName, mouseX + 150 / (12 +  Client.instance.newSmallFont.getTextWidth(itemName)) , mouseY + 17, color, 1);
newSmallFont.drawBasicString(itemName, mouseX + 1 , mouseY + 16, color, 1);

//	newSmallFont.drawBasicString("Press CTRL to view the stats", mouseX + 4, mouseY + 35, color, 1);



}
	public String stabatkbonus, slashatkbonus,crushatkbonus,magicatkbonus,rangedatkbonus,stabdefbonus,slashdefbonus,crushdefbonus,magicdefbonus,rangeddefbonus,prayerbonusbonus,strengthbonusbonus = "";

	public void drawStatMenu(int itemId, int color) {
		if (menuActionRow < 2 && itemSelected == 0 && spellSelected == 0) {
			return;
		}
		if(menuActionName[menuActionRow]!=null) {
			if (menuActionName[menuActionRow].contains("Walk")) {
				return;
			}
		}
		//if(toolTip.contains("Walk")||toolTip.contains("Talk-to")||toolTip.contains("Bank")|| toolTip.contains("Steal")|| toolTip.contains("Attack")){
		//	return;
		//}/
		if(toolTip.contains("Walk")||toolTip.contains("World")||!toolTip.contains("W")){
			return;
		}
		if(menuOpen){
			return;
		}
		if(tabID!=3){
			return;
		}
		int mouseX = MouseHandler.mouseX;
		int mouseY = MouseHandler.mouseY;
		if(isResized()){
			mouseX-=100;
			mouseY-=50;
		}

		if(!isResized()){
			if(MouseHandler.mouseY < 214 || MouseHandler.mouseX < 561){
				return;
			}
		//	mouseX-=516;
			// mouseX-=491;
		//	mouseY-=158;
			if(MouseHandler.mouseX > 600 && MouseHandler.mouseX < 685) {
				mouseX-=60;

			}
			if(MouseHandler.mouseX > 685){
				mouseX-=120;
			}
			if(MouseHandler.mouseY > 392){
				mouseY-=130;
			}
		}
//		inputString = "::itemstats-" + itemId;
//		stream.createFrame(103);
//		stream.writeWordBigEndian(inputString.length() - 1);
//		stream.writeString(inputString.substring(2));

	//	inputString = "";

//		String stabAtk = "";
//		String slashAtk = "";
//		String crushAtk = "";
//		String magicAtk ="";
//		String rangedAtk ="";
//
//		String stabDef = "";
//		String slashDef ="";
//		String crushDef ="";
//		String magicDef = "";
//		String rangedDef = "";
//
//		String prayerBonus="";
//		String strengthBonus="";


		//itemstats(itemId);
//		stabAtk = stabatkbonus;
//		slashAtk = slashatkbonus;
//		crushAtk = crushatkbonus;
//		magicAtk = magicatkbonus;
//		rangedAtk = rangedatkbonus;
//
//		stabDef = stabdefbonus;
//		slashDef = slashdefbonus;
//		crushDef = crushdefbonus;
//		magicDef = magicdefbonus;
//		rangedDef = rangeddefbonus;
//
//		prayerBonus= prayerbonusbonus;
//		strengthBonus= strengthbonusbonus;

if(ItemBonusDefinition.getItemBonuses(hintId) == null)
	return;
		short stabAtk = ItemBonusDefinition.getItemBonuses(hintId)[0];
		int slashAtk = ItemBonusDefinition.getItemBonuses(hintId)[1];
		int crushAtk = ItemBonusDefinition.getItemBonuses(hintId)[2];
		int magicAtk = ItemBonusDefinition.getItemBonuses(hintId)[3];
		int rangedAtk = ItemBonusDefinition.getItemBonuses(hintId)[4];

		int stabDef = ItemBonusDefinition.getItemBonuses(hintId)[5];
		int slashDef = ItemBonusDefinition.getItemBonuses(hintId)[6];
		int crushDef = ItemBonusDefinition.getItemBonuses(hintId)[7];
		int magicDef = ItemBonusDefinition.getItemBonuses(hintId)[8];
		int rangedDef = ItemBonusDefinition.getItemBonuses(hintId)[9];

		int prayerBonus = ItemBonusDefinition.getItemBonuses(hintId)[12];
		int strengthBonus = ItemBonusDefinition.getItemBonuses(hintId)[10];
		int rangedstrBonus = ItemBonusDefinition.getItemBonuses(hintId)[11];
//System.out.println(strengthBonus+" "+Integer.parseInt(strengthBonus));
		Rasterizer2D.drawBoxOutline(mouseX ,  mouseY + 20, 120, 120, 0x696969);
		Rasterizer2D.drawTransparentBox(mouseX + 1,  mouseY + 21,119, 119, 0x000000,180);//90 instead of 180

		//Client.instance.newSmallFont.drawBasicString(itemName, mouseX + 150 / (12 +  Client.instance.newSmallFont.getTextWidth(itemName))+30 , mouseY + 17, color, 1);

		Client.instance.newSmallFont.drawBasicString("@whi@ATK", mouseX + 52, mouseY + 30, color, 1);
		Client.instance.newSmallFont.drawBasicString("@whi@DEF", mouseX + 102, mouseY + 30, color, 1);

		Client.instance.newSmallFont.drawBasicString("@whi@Stab", mouseX + 2, mouseY + 43, color, 1);

		Client.instance.newSmallFont.drawBasicString((stabAtk < 0? "@red@"+stabAtk+"" : "@gre@"+stabAtk+""), mouseX + 62, mouseY + 43, color, 1);
		//Client.instance.newSmallFont.drawBasicString(Integer.toString(stabDef), mouseX + 112, mouseY + 43, color, 1);

		Client.instance.newSmallFont.drawBasicString((stabDef < 0?   "@red@ "+stabDef+" " : "@gre@"+stabDef+""), mouseX + 102, mouseY + 43, color, 1);

		Client.instance.newSmallFont.drawBasicString("@whi@Slash", mouseX + 2, mouseY + 56, 0xFF00FF, 1);

		Client.instance.newSmallFont.drawBasicString((slashAtk < 0?  "@red@"+slashAtk+"" : "@gre@"+slashAtk+""), mouseX + 62, mouseY + 56, color, 1);
		// Client.instance.newSmallFont.drawBasicString(Integer.toString(slashAtk), mouseX + 62, mouseY + 56, color, 1);
		// Client.instance.newSmallFont.drawBasicString(Integer.toString(slashDef), mouseX + 112, mouseY + 56, color, 1);
		Client.instance.newSmallFont.drawBasicString((slashDef < 0 ?  "@red@"+slashDef+"" : "@gre@"+slashDef+""), mouseX + 102, mouseY + 56, color, 1);


		Client.instance.newSmallFont.drawBasicString("@whi@Crush", mouseX + 2, mouseY + 69, color, 1);
		// Client.instance.newSmallFont.drawBasicString(Integer.toString(crushAtk), mouseX + 62, mouseY + 69, color, 1);
		//Client.instance.newSmallFont.drawBasicString(Integer.toString(crushDef), mouseX + 112, mouseY + 69, color, 1);
		Client.instance.newSmallFont.drawBasicString((crushAtk < 0 ? "@red@"+crushAtk+"" : "@gre@"+crushAtk+""), mouseX + 62, mouseY + 69, color, 1);
		Client.instance.newSmallFont.drawBasicString((crushDef < 0   ? "@red@"+crushDef+"" : "@gre@"+crushDef+""), mouseX + 102, mouseY + 69, color, 1);


		Client.instance.newSmallFont.drawBasicString("@whi@Magic", mouseX + 2, mouseY + 80, color, 1);
		//Client.instance.newSmallFont.drawBasicString(Integer.toString(magicAtk), mouseX + 62, mouseY + 80, color, 1);
		// Client.instance.newSmallFont.drawBasicString(Integer.toString(magicDef), mouseX + 112, mouseY + 80, color, 1);
		Client.instance.newSmallFont.drawBasicString((magicAtk < 0 ? "@red@"+magicAtk+"" : "@gre@"+magicAtk+""), mouseX + 62, mouseY + 80, color, 1);
		Client.instance.newSmallFont.drawBasicString((magicDef < 0 ?"@red@"+magicDef+"" : "@gre@"+magicDef+""), mouseX + 102, mouseY + 80, color, 1);

		Client.instance.newSmallFont.drawBasicString("@whi@Ranged", mouseX + 2, mouseY + 95, color, 1);
		//Client.instance.newSmallFont.drawBasicString(Integer.toString(rangedAtk), mouseX + 62, mouseY + 95, color, 1);
		// Client.instance.newSmallFont.drawBasicString(Integer.toString(rangedDef), mouseX + 112, mouseY + 95, color, 1);

		Client.instance.newSmallFont.drawBasicString((rangedAtk < 0  ? "@red@"+rangedAtk+"" : "@gre@"+rangedAtk+""), mouseX + 62, mouseY + 95, color, 1);
		Client.instance.newSmallFont.drawBasicString((rangedDef < 0 ? "@red@"+rangedDef+"" : "@gre@"+rangedDef+""), mouseX + 102, mouseY + 95, color, 1);


		Client.instance.newSmallFont.drawBasicString("@whi@Strength", mouseX + 2, mouseY + 108, color, 1);
		Client.instance.newSmallFont.drawBasicString("@whi@Prayer", mouseX + 2, mouseY + 121, color, 1);
		Client.instance.newSmallFont.drawBasicString("@whi@Ranged Str", mouseX + 2, mouseY + 134, color, 1);

		//Client.instance.newSmallFont.drawBasicString(Integer.toString(strengthBonus), mouseX + 112, mouseY + 108, color, 1);
		//Client.instance.newSmallFont.drawBasicString(Integer.toString(prayerBonus), mouseX + 112, mouseY + 121, color, 1);
//		if(strengthBonus.equals(""))
//			strengthBonus = "0";
		Client.instance.newSmallFont.drawBasicString((strengthBonus < 0 ? "@red@"+strengthBonus+"" : "@gre@"+strengthBonus+""), mouseX + 62, mouseY + 108, color, 1);
		Client.instance.newSmallFont.drawBasicString((prayerBonus < 0 ? "@red@"+prayerBonus+"" : "@gre@"+prayerBonus+""), mouseX + 62, mouseY + 121, color, 1);
		Client.instance.newSmallFont.drawBasicString((rangedstrBonus < 0 ? "@red@"+rangedstrBonus+"" : "@gre@"+rangedstrBonus+""), mouseX + 62, mouseY + 134, color, 1);

		Client.instance.newSmallFont.drawBasicString("@whi@Stab", mouseX + 2, mouseY + 43, color, 1);
		Client.instance.newSmallFont.drawBasicString("@whi@Slash", mouseX + 2, mouseY + 56, color, 1);
		Client.instance.newSmallFont.drawBasicString("@whi@Crush", mouseX + 2, mouseY + 69, color, 1);
		Client.instance.newSmallFont.drawBasicString("@whi@Magic", mouseX + 2, mouseY + 80, color, 1);
		Client.instance.newSmallFont.drawBasicString("@whi@Range", mouseX + 2, mouseY + 95, color, 1);
		Client.instance.newSmallFont.drawBasicString("@whi@Strength", mouseX + 2, mouseY + 108, color, 1);
		Client.instance.newSmallFont.drawBasicString("@whi@Prayer", mouseX + 2, mouseY + 121, color, 1);
		Client.instance.newSmallFont.drawBasicString("@whi@Prayer", mouseX + 2, mouseY + 121, color, 1);


	//	sendstatsrequest=  true;
	}

	private void buildChatAreaMenu(int j) {
		int l = 0;
		for (int i1 = 0; i1 < 500; i1++) {
			if (chatMessages[i1] == null)
				continue;
			int j1 = chatTypes[i1];
			int k1 = (70 - l * 14 + 42) + anInt1089 + 4 + 5;
			if (k1 < -23)
				break;
			String s = chatNames[i1];
			if (chatTypeView == 1) {
				buildPublicChat(j);
				break;
			}
			if (chatTypeView == 2) {
				buildFriendChat(j);
				break;
			}
			if (chatTypeView == 3 || chatTypeView == 4) {
				buildDuelorTrade(j);
				break;
			}
			if (chatTypeView == 5) {
				break;
			}
			while (s.startsWith("@cr")) {
				String s2 = s.substring(3, s.length());
				int index = s2.indexOf("@");
				if (index != -1) {
					s2 = s2.substring(0, index);
					s = s.substring(4 + s2.length());
				}
			}
			if (s != null && s.startsWith("<col=") && s.indexOf("</col>") != -1) {
				s = s.substring(s.indexOf("</col>") + 6);
			}
			if (j1 == 0)
				l++;
			if ((j1 == 1 || j1 == 2) && (j1 == 1 || publicChatMode == 0 || publicChatMode == 1 && isFriendOrSelf(s))) {
				if (j > k1 - 14 && j <= k1 && !s.equals(localPlayer.displayName)) {
					if (localPlayer.hasRightsLevel(1)) {
						menuActionName[menuActionRow] = "Report abuse @whi@" + s;
						menuActionID[menuActionRow] = 606;
						menuActionRow++;
					}
					menuActionName[menuActionRow] = "Add ignore @whi@" + s;
					menuActionID[menuActionRow] = 42;
					menuActionRow++;
					menuActionName[menuActionRow] = "Reply to @whi@" + s;
					menuActionID[menuActionRow] = 639;
					menuActionRow++;
					menuActionName[menuActionRow] = "Add friend @whi@" + s;
					menuActionID[menuActionRow] = 337;
					menuActionRow++;
				}
				l++;
			}
			if ((j1 == 3 || j1 == 7) && !splitPrivateChat
					&& (j1 == 7 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(s))) {
				if (j > k1 - 14 && j <= k1) {
					if (localPlayer.hasRightsLevel(1)) {
						menuActionName[menuActionRow] = "Report abuse @whi@" + s;
						menuActionID[menuActionRow] = 606;
						menuActionRow++;
					}
					menuActionName[menuActionRow] = "Add ignore @whi@" + s;
					menuActionID[menuActionRow] = 42;
					menuActionRow++;
					menuActionName[menuActionRow] = "Reply to @whi@" + s;
					menuActionID[menuActionRow] = 639;
					menuActionRow++;
					menuActionName[menuActionRow] = "Add friend @whi@" + s;
					menuActionID[menuActionRow] = 337;
					menuActionRow++;
				}
				l++;
			}

			if (j1 == 99) {
				lastViewedDropTable = chatMessages[j] == null ? "" : chatMessages[j];
				menuActionName[menuActionRow] = "Open Drop Table";
				menuActionID[menuActionRow] = 10_992;
				menuActionRow++;
				l++;
			}

			// Trade
			if (j1 == 4 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s))) {
				if (j > k1 - 14 && j <= k1) {
					menuActionName[menuActionRow] = "Accept trade @whi@" + s;
					menuActionID[menuActionRow] = 484;
					menuActionRow++;
				}
				l++;
			}
			if ((j1 == 5 || j1 == 6) && !splitPrivateChat && privateChatMode < 2)
				l++;
			if (j1 == 8 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s))) {
				if (j > k1 - 14 && j <= k1) {
					menuActionName[menuActionRow] = "Accept challenge @whi@" + s;
					menuActionID[menuActionRow] = 6;
					menuActionRow++;
				}
				l++;
			}

			if (j1 == 31 && (tradeMode == 0 || tradeMode == 1 && isFriendOrSelf(s))) {
				if (j > k1 - 14 && j <= k1) {
					menuActionName[menuActionRow] = "Accept Gamble Request From@whi@ " + s;
					menuActionID[menuActionRow] = 11_501;
					menuActionRow++;
				}
				l++;
			}
			if (j1 == 310) {
				if (j > k1 - 14 && j <= k1) {
					menuActionName[menuActionRow] = "Redeem votes";
					menuActionID[menuActionRow] = 1730;
					menuActionRow++;
				}
				l++;
			}
		}
	}

	public String setSkillTooltip(int skillLevel) {
		String[] getToolTipText = new String[4];
		String setToolTipText = "";
		int maxLevel = 99;

		if (maxStats[skillLevel] > maxLevel) {
			if (skillLevel != 24) {
				maxStats[skillLevel] = 99;
			} else if (maxStats[skillLevel] > 120) {
				maxStats[skillLevel] = 120;
			}
		}

		NumberFormat numberFormat = NumberFormat.getInstance(Locale.US);
		int[] getSkillId = { 0, 0, 2, 1, 4, 5, 6, 20, 21, 3, 16, 15, 17, 12, 9, 18, 22, 14, 14, 13, 10, 7, 11, 8, 19,
				24 };
		if (!Skills.SKILL_NAMES[skillLevel].equals("-1")) {

			if (maxStats[getSkillId[ skillLevel]] >= 99) {
				getToolTipText[0] = Skills.SKILL_NAMES[skillLevel] + " XP: "
						+ numberFormat.format(currentExp[getSkillId[ skillLevel]]) + "\\n";
				setToolTipText = getToolTipText[0];
			} else {
				getToolTipText[0] = Skills.SKILL_NAMES[skillLevel] + " XP: " + "\\r"
						+ numberFormat.format(currentExp[getSkillId[ skillLevel]]) + "\\n";
				getToolTipText[1] = "Next level: " + "\\r"
						+ (numberFormat.format(getXPForLevel(maxStats[getSkillId[skillLevel]] + 1))) + "\\n";
				getToolTipText[2] = "Remainder: " + "\\r" + numberFormat.format(
						getXPForLevel(maxStats[getSkillId[skillLevel]] + 1) - currentExp[getSkillId[skillLevel]])
						+ "\\n";
				getToolTipText[3] = "";

				setToolTipText = getToolTipText[0] + getToolTipText[1] + getToolTipText[2];
			}
		} else {
			setToolTipText = "Click here to logout";
		}
		return setToolTipText;
	}

	public void drawFriendsListOrWelcomeScreen(RSInterface class9) {
		int j = class9.contentType;
		if ((j >= 205) && (j <= (205 + 25))) {
			j -= 205;
			class9.message = setSkillTooltip(j);
			return;
		}
//		if (j == 831) {
//			child.defaultText = setSkillTooltip(0);
//			return;
//		}
		if (j >= 1 && j <= 100 || j >= 701 && j <= 800) {
			if (j == 1 && anInt900 == 0) {
				class9.message = "Loading friend list";
				class9.atActionType = 0;
				return;
			}
			if (j == 1 && anInt900 == 1) {
				class9.message = "Connecting to friendserver";
				class9.atActionType = 0;
				return;
			}
			if (j == 2 && anInt900 != 2) {
				class9.message = "Please wait...";
				class9.atActionType = 0;
				return;
			}
			int k = friendsCount;
			if (anInt900 != 2)
				k = 0;
			if (j > 700)
				j -= 601;
			else
				j--;
			if (j >= k) {
				class9.message = "";
				class9.atActionType = 0;
				return;
			} else {
				class9.message = friendsList[j];
				class9.atActionType = 1;
				return;
			}
		}
		if (j >= 101 && j <= 200 || j >= 801 && j <= 900) {
			int l = friendsCount;
			if (anInt900 != 2)
				l = 0;
			if (j > 800)
				j -= 701;
			else
				j -= 101;
			if (j >= l) {
				class9.message = "";
				class9.atActionType = 0;
				return;
			}
			if (friendsNodeIDs[j] == 0)
				class9.message = "@red@Offline";
			else if (friendsNodeIDs[j] == nodeID)
				class9.message = "@gre@Online";
			else
				class9.message = "@red@Offline";
			class9.atActionType = 1;
			return;
		}
		if (j == 203) {
			int i1 = friendsCount;
			if (anInt900 != 2)
				i1 = 0;
			class9.scrollMax = i1 * 15 + 20;
			if (class9.scrollMax <= class9.height)
				class9.scrollMax = class9.height + 1;
			return;
		}
		if (j >= 401 && j <= 500) {
			if ((j -= 401) == 0 && anInt900 == 0) {
				class9.message = "Loading ignore list";
				class9.atActionType = 0;
				return;
			}
			if (j == 1 && anInt900 == 0) {
				class9.message = "Please wait...";
				class9.atActionType = 0;
				return;
			}
			int j1 = ignoreCount;
			if (anInt900 == 0)
				j1 = 0;
			if (j >= j1) {
				class9.message = "";
				class9.atActionType = 0;
				return;
			} else {
				class9.message = StringUtils.fixName(StringUtils.nameForLong(ignoreListAsLongs[j]));
				class9.atActionType = 1;
				return;
			}
		}
		if (j == 503) {
			class9.scrollMax = ignoreCount * 15 + 20;
			if (class9.scrollMax <= class9.height)
				class9.scrollMax = class9.height + 1;
			return;
		}
		if (j == 327) {
			class9.modelRotation1 = 150;
			class9.modelRotation2 = (int) (Math.sin(update_tick / 40D) * 256D) & 0x7ff;
			if (aBoolean1031) {
				for (int k1 = 0; k1 < 7; k1++) {
					int l1 = anIntArray1065[k1];
					if (l1 >= 0 && !IDK.cache[l1].method537())
						return;
				}

				aBoolean1031 = false;
				Model aclass30_sub2_sub4_sub6s[] = new Model[7];
				int i2 = 0;
				for (int j2 = 0; j2 < 7; j2++) {
					int k2 = anIntArray1065[j2];
					if (k2 >= 0)
						aclass30_sub2_sub4_sub6s[i2++] = IDK.cache[k2].get_model();
				}

				Model model = new Model(i2, aclass30_sub2_sub4_sub6s);
				for (int l2 = 0; l2 < 5; l2++)
					if (anIntArray990[l2] != 0) {
						model.recolor(player_body_colours[l2][0], player_body_colours[l2][anIntArray990[l2]]);
						if (l2 == 1)
							model.recolor(player_hair_colours[0], player_hair_colours[anIntArray990[l2]]);
						// if(l2 == 1)
						// model.method476(Legs2[0], Legs2[anIntArray990[l2]]);
					}
				//model.apply_label_groups();
				AnimationDefinition.anims[localPlayer.readyanim].animate_iftype_model(model, 0);

				//model.animate_either(, 0);
				model.light(64, 1300, 0, -570, 0, true);
				class9.model_cat = 5;
				class9.modelid = 0;
				RSInterface.method208(aBoolean994, model);
			}
			return;
		}
		if (j == 3291 && PetSystem.petSelected != -1) {
			PetSystem petDef = new PetSystem(NpcDefinition.lookup(PetSystem.petSelected));
			if (petDef != null) {
				RSInterface rsInterface = class9;
				int verticleTilt = 150;
//				rsInterface.modelRotation1 = verticleTilt;
//				rsInterface.modelRotation2 = (int) (double) (update_tick / 100D * 1024D) & 2047;
				Model model;
				final Model[] parts = new Model[petDef.getModelArrayLength()];
				for (int i = 0; i < petDef.getModelArrayLength(); i++) {
					parts[i] = Model.getModel(petDef.getModelArray()[i]);
				}
				if (parts.length == 1) {
					model = parts[0];
				} else {
					model = new Model(parts.length, parts);
				}


				if (model == null) {
					return;
				}


//			model.method469();
				model.scale((int) 1.5); // the size of the npc need to find the y axis cause they are just too high not too big
//			model.method470(AnimationDefinition.anims[staticFrame].anIntArray353[0]);
//				model.animate_either(AnimationDefinition.anims[21], rsInterface.iftype_frameindex);
				model.light(64, 850, -30, -50, -30, true);
				rsInterface.model_cat = 5;
				rsInterface.modelid = 0;
				RSInterface.method208(aBoolean994, model); // he named it something other than walk
				return;
			}
		}
//
//		if (j == 3291) {
//			//System.out.println("here before");
//			PetSystem petDef = new PetSystem(NpcDefinition.lookup(PetSystem.petSelected));
//
//			if (petDef != null) {
//			//	System.out.println("here");
//				RSInterface rsInterface = class9;
//				int verticleTilt = 150;
////                rsInterface.modelRotation1 = verticleTilt;
////                rsInterface.modelRotation2 = (int) (double) (update_tick / 100D * 1024D) & 2047;
//				Model model;
//				final Model[] parts = new Model[petDef.getModelArrayLength()];
//				for (int i = 0; i < petDef.getModelArrayLength(); i++) {
//					parts[i] = Model.getModel(petDef.getModelArray()[i]);
//				}
//				if (parts.length == 1) {
//					model = parts[0];
//				} else {
//					model = new Model(parts.length, parts);
//				}
//
//
//				if (model == null) {
//					return;
//				}
//
//
//				model.apply_label_groups();
//				model.animate_either(AnimationDefinition.anims[petDef.getAnimation()], petDef.animationFrame);
//				model.scale((int) 1.5); // the size of the npc need to find the y axis cause they are just too high not too big
//				model.light(64, 850, -30, -50, -30, true);
//				rsInterface.model_cat = 5;
//				rsInterface.modelid = 0;
//				RSInterface.method208(aBoolean994, model); // he named it something other than walk
//				if (!petDef.isPetAnimationRunning) {
//					petDef.updateAnimations();
//				}
//				return;
//			}
//		}

				if (j == 3288) {
								if (npcDisplay == null) {
				return;
			}
			RSInterface rsInterface = class9;
			boolean active = false;//cs1_selected(rsInterface);
			int verticleTilt = 150;
			int animationSpeed = (int) (Math.sin(update_tick / 40D) * 256D) & 0x7ff;
			rsInterface.modelRotation1 = verticleTilt;
			rsInterface.modelRotation2 = animationSpeed;
			if (aBoolean1031) {
				Model characterDisplay = npcDisplay.getRotatedModel();
				//Model characterDisplay = localPlayer.get_model();//what makes it rotate. fuck.


				for (int l2 = 0; l2 < 5; l2++)
					if (anIntArray990[l2] != 0) {
						characterDisplay.recolor(player_body_colours[l2][0],
								player_body_colours[l2][anIntArray990[l2]]);
						if (l2 == 1)
							characterDisplay.recolor(player_hair_colours[0], player_hair_colours[anIntArray990[l2]]);
					}

				rsInterface.model_cat = 5;
				rsInterface.modelid = 0;
				RSInterface.method208(aBoolean994, characterDisplay);
			}
			return;
		}
		if (j == 328) {
			RSInterface rsInterface = class9;
			boolean active = false;//cs1_selected(rsInterface);
			int verticleTilt = 150;
			int animationSpeed = (int) (Math.sin(update_tick / 40D) * 256D) & 0x7ff;
			rsInterface.modelRotation1 = verticleTilt;
			rsInterface.modelRotation2 = animationSpeed;
			if (aBoolean1031) {

				Model characterDisplay = localPlayer.get_model();//what makes it rotate. fuck.


				for (int l2 = 0; l2 < 5; l2++)
					if (anIntArray990[l2] != 0) {
						characterDisplay.recolor(player_body_colours[l2][0],
								player_body_colours[l2][anIntArray990[l2]]);
						if (l2 == 1)
							characterDisplay.recolor(player_hair_colours[0], player_hair_colours[anIntArray990[l2]]);
					}

				rsInterface.model_cat = 5;
				rsInterface.modelid = 0;
				RSInterface.method208(aBoolean994, characterDisplay);
			}
			return;
		}
		if (j == 324) {
			if (aClass30_Sub2_Sub1_Sub1_931 == null) {
				aClass30_Sub2_Sub1_Sub1_931 = class9.sprite1;
				aClass30_Sub2_Sub1_Sub1_932 = class9.sprite2;
			}
			if (aBoolean1047) {
				class9.sprite1 = aClass30_Sub2_Sub1_Sub1_932;
				return;
			} else {
				class9.sprite1 = aClass30_Sub2_Sub1_Sub1_931;
				return;
			}
		}
		if (j == 325) {
			if (aClass30_Sub2_Sub1_Sub1_931 == null) {
				aClass30_Sub2_Sub1_Sub1_931 = class9.sprite1;
				aClass30_Sub2_Sub1_Sub1_932 = class9.sprite2;
			}
			if (aBoolean1047) {
				class9.sprite1 = aClass30_Sub2_Sub1_Sub1_931;
				return;
			} else {
				class9.sprite1 = aClass30_Sub2_Sub1_Sub1_932;
				return;
			}
		}
		if (j == 600) {
			class9.message = reportAbuseInput;
			if (update_tick % 20 < 10) {
				class9.message += "|";
				return;
			} else {
				class9.message += " ";
				return;
			}
		}
		if (j == 613)
			if (localPlayer.hasRightsLevel(1)) {
				if (canMute) {
					class9.textColor = 0xff0000;
					// class9.message =
					// "Moderator option: Mute player for 48 hours: <ON>";
				} else {
					class9.textColor = 0xffffff;
					// class9.message =
					// "Moderator option: Mute player for 48 hours: <OFF>";
				}
			} else {
				class9.message = "";
			}
		if (j == 650 || j == 655)
			if (anInt1193 != 0) {
				String s;
				if (daysSinceLastLogin == 0)
					s = "earlier today";
				else if (daysSinceLastLogin == 1)
					s = "yesterday";
				else
					s = daysSinceLastLogin + " days ago";
				class9.message = "You last logged in " + s + " from: " + Signlink.dns;
			} else {
				class9.message = "";
			}
		if (j == 651) {
			if (unreadMessages == 0) {
				class9.message = "0 unread messages";
				class9.textColor = 0xffff00;
			}
			if (unreadMessages == 1) {
				class9.message = "1 unread message";
				class9.textColor = 65280;
			}
			if (unreadMessages > 1) {
				class9.message = unreadMessages + " unread messages";
				class9.textColor = 65280;
			}
		}
		if (j == 652)
			if (daysSinceRecovChange == 201) {
				if (membersInt == 1)
					class9.message = "@yel@This is a non-members world: @whi@Since you are a member we";
				else
					class9.message = "";
			} else if (daysSinceRecovChange == 200) {
				class9.message = "You have not yet set any password recovery questions.";
			} else {
				String s1;
				if (daysSinceRecovChange == 0)
					s1 = "Earlier today";
				else if (daysSinceRecovChange == 1)
					s1 = "Yesterday";
				else
					s1 = daysSinceRecovChange + " days ago";
				class9.message = s1 + " you changed your recovery questions";
			}
		if (j == 653)
			if (daysSinceRecovChange == 201) {
				if (membersInt == 1)
					class9.message = "@whi@recommend you use a members world instead. You may use";
				else
					class9.message = "";
			} else if (daysSinceRecovChange == 200)
				class9.message = "We strongly recommend you do so now to secure your account.";
			else
				class9.message = "If you do not remember making this change then cancel it immediately";
		if (j == 654) {
			if (daysSinceRecovChange == 201)
				if (membersInt == 1) {
					class9.message = "@whi@this world but member benefits are unavailable whilst here.";
					return;
				} else {
					class9.message = "";
					return;
				}
			if (daysSinceRecovChange == 200) {
				class9.message = "Do this from the 'account management' area on our front webpage";
				return;
			}
			class9.message = "Do this from the 'account management' area on our front webpage";
		}
	}

	private void sortFriendsList() {
		for (boolean flag6 = false; !flag6;) {
			flag6 = true;
			for (int k29 = 0; k29 < friendsCount - 1; k29++)
				if (friendsNodeIDs[k29] != nodeID && friendsNodeIDs[k29 + 1] == nodeID
						|| friendsNodeIDs[k29] == 0 && friendsNodeIDs[k29 + 1] != 0) {
					int j31 = friendsNodeIDs[k29];
					friendsNodeIDs[k29] = friendsNodeIDs[k29 + 1];
					friendsNodeIDs[k29 + 1] = j31;
					String s10 = friendsList[k29];
					friendsList[k29] = friendsList[k29 + 1];
					friendsList[k29 + 1] = s10;
					long l32 = friendsListAsLongs[k29];
					friendsListAsLongs[k29] = friendsListAsLongs[k29 + 1];
					friendsListAsLongs[k29 + 1] = l32;
					needDrawTabArea = true;
					flag6 = false;
				}
		}
	}

	private void drawSplitPrivateChat() {
		if (!splitPrivateChat)
			return;
		RSFont font = newRegularFont;

		int listPosition = 0;
		boolean update = anInt1104 != 0;
		boolean broadcast = BroadcastManager.isDisplayed();
		listPosition = update && broadcast ? 2 : broadcast || update ? 1 : 0;
		int xPosition = 0;
		int yPosition = 0;
		for (int j = 0; j < 100; j++) {
			if (chatMessages[j] != null) {
				int k = chatTypes[j];
				String s = chatNames[j];
				List<Integer> crowns = new ArrayList<>();
				while (s.startsWith("@cr")) {
					String s2 = s.substring(3, s.length());
					int index = s2.indexOf("@");
					if (index != -1) {
						s2 = s2.substring(0, index);
						crowns.add(Integer.parseInt(s2));
						s = s.substring(4 + s2.length());
					}
				}
				if ((k == 3 || k == 7) && (k == 7 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(s))) {
					yPosition = (!isResized() ? 330 : canvasHeight - 173) - listPosition * 13;
					xPosition = !isResized() ? 5 : 0;
					font.drawBasicString("From", xPosition, yPosition, 65535, 0);
					xPosition += font.getTextWidth("From ");
					if (!crowns.isEmpty()) {
						for (int crown : crowns) {
							for (int right = 0; right < modIcons.length; right++) {
								if (right == (crown - 1) && modIcons[right] != null) {
									modIcons[right].drawAdvancedSprite(xPosition, yPosition - modIcons[right].myHeight);
									xPosition += modIcons[right].myWidth;
									xPosition += 2;
									break;
								}
							}
						}
					}
					font.drawBasicString(s + ": " + chatMessages[j], xPosition, yPosition, 65535, 0);
					if (++listPosition >= 5)
						return;
				}
				if (k == 5 && privateChatMode < 2) {
					yPosition = (!isResized() ? 330 : canvasHeight - 173) - listPosition * 13;
					xPosition = !isResized() ? 5 : 0;
					font.drawBasicString(chatMessages[j], xPosition, yPosition, 65535, 0);
					if (++listPosition >= 5)
						return;
				}
				if (k == 6 && privateChatMode < 2) {
					yPosition = (!isResized() ? 330 : canvasHeight - 173) - listPosition * 13;
					xPosition = !isResized() ? 5 : 0;
					font.drawBasicString("To " + s + ": " + chatMessages[j], xPosition, yPosition, 65535, 0);
					if (++listPosition >= 5)
						return;
				}
			}
		}
	}

	private void buildSplitPrivateChatMenu() {
		if (!splitPrivateChat)
			return;
		int i = 0;
		if (anInt1104 != 0)
			i = 1;
		for (int j = 0; j < 100; j++) {
			if (chatMessages[j] != null) {
				int k = chatTypes[j];
				String s = chatNames[j];
				while (s.startsWith("@cr")) {
					String s2 = s.substring(3, s.length());
					int index = s2.indexOf("@");
					if (index != -1) {
						s2 = s2.substring(0, index);
						s = s.substring(4 + s2.length());
					}
				}
				if ((k == 3 || k == 7)
						&& (k == 7 || privateChatMode == 0 || privateChatMode == 1 && isFriendOrSelf(s))) {
					int yPosition = (!isResized() ? 330 : canvasHeight - 173) - i * 13;
					int messageLength = newRegularFont.getTextWidth("From:  " + s + chatMessages[j]) + 25;
					if (MouseHandler.mouseX >= 0 && MouseHandler.mouseX <= messageLength) {
						if (MouseHandler.mouseY >= yPosition - 10 && getMouseY() <= yPosition + 3) {
							if (messageLength > 450)
								messageLength = 450;
							if (localPlayer.hasRightsLevel(1)) {
								menuActionName[menuActionRow] = "Report abuse @whi@" + s;
								menuActionID[menuActionRow] = 2606;
								menuActionRow++;
							}
							menuActionName[menuActionRow] = "Add ignore @whi@" + s;
							menuActionID[menuActionRow] = 2042;
							menuActionRow++;
							menuActionName[menuActionRow] = "Reply to @whi@" + s;
							menuActionID[menuActionRow] = 2639;
							menuActionRow++;
							menuActionName[menuActionRow] = "Add friend @whi@" + s;
							menuActionID[menuActionRow] = 2337;
							menuActionRow++;
						}
					}
					if (++i >= 5)
						return;
				}
				if ((k == 5 || k == 6) && privateChatMode < 2 && ++i >= 5)
					return;
			}
		}
	}
public boolean dontbuildwalk;
	private void buildBroadcasts() {

		int i = 0;
		dontbuildwalk=false;
		if (BroadcastManager.isDisplayed()) {

			Broadcast bc = BroadcastManager.getCurrentBroadcast();

			if (bc == null)
				return;

			boolean update = isServerUpdating();

			int yPosition = (!isResized() ? 330 : canvasHeight - 173) - i * 13;
			yPosition -= update ? 13 : 0;
			int messageLength = newRegularFont.getTextWidth(bc.getDecoratedMessage());
			if (openInterfaceID <= 0 && MouseHandler.mouseX >= 0 && MouseHandler.mouseX <= messageLength) {
				if (MouseHandler.mouseY >= yPosition - 10 && getMouseY() <= yPosition) {
					dontbuildwalk=true;
					menuActionName[menuActionRow] = "Dismiss";
					menuActionID[menuActionRow] = 11_115;
					menuActionRow++;
					if (bc.type != 0) {
						menuActionName[menuActionRow] = bc.type == 1 ? "Open" : bc.type == 2 ? "Teleport To Location" : null;
						menuActionID[menuActionRow] = bc.type == 1 ? 11_111 : 11_112;
						menuActionRow++;
					}
				}
			}
			if (++i >= 1)
				return;
		}
	}

	public void timeOutHasLoggedMessages() {
		for (int i = 0; i < chatTimes.length; i++) {
			if (chatMessages[i] != null && chatMessages[i].contains("has logged") && System.currentTimeMillis() - chatTimes[i] >= 15_000) {
				chatMessages[i] = "";
				shiftMessages();
			}
		}
	}

	public void pushMessage(String s) {
		pushMessage(s, 0, "");
	}

	public void pushMessage(String s, int i, String s1) {
		if (i == 0 && dialogID != -1) {
			clickToContinueString = s;
			clickMode3 = 0;
		}
		if (backDialogID == -1)
			inputTaken = true;
		for (int j = 499; j > 0; j--) {
			chatTypes[j] = chatTypes[j - 1];
			chatNames[j] = chatNames[j - 1];
			chatMessages[j] = chatMessages[j - 1];
			clanTitles[j] = clanTitles[j - 1];
			chatTimes[j] = chatTimes[j - 1];
		}
		chatTypes[0] = i;
		chatNames[0] = s1;
		chatMessages[0] = s;
		clanTitles[0] = clanTitle;
		chatTimes[0] = System.currentTimeMillis();
	}

	public void shiftMessages() {
		int index = 0;
		int[] chatTypes = new int[500];
		String[] chatNames = new String[500];
		String[] chatMessages = new String[500];
		String[] clanTitles = new String[500];
		long[] chatTimes = new long[500];

		for (int i = 0; i < chatTypes.length; i++) {
			if (this.chatMessages[i] != null && this.chatMessages[i].length() > 0) {
				chatMessages[index] = this.chatMessages[i];
				chatTypes[index] = this.chatTypes[i];
				chatNames[index] = this.chatNames[i];
				clanTitles[index] = this.clanTitles[i];
				chatTimes[index] = this.chatTimes[i];
				index++;
			}
		}

		this.chatTypes = chatTypes;
		this.chatMessages = chatMessages;
		this.chatNames = chatNames;
		this.clanTitles = clanTitles;
		this.chatTimes = chatTimes;
	}
	private Queue<KillFeed> killfeed = new LinkedList<>();
	private static final Comparator<KillFeed> HIGHEST_POSITION_KILL_FEED = new Comparator<KillFeed>() {

		@Override
		public int compare(KillFeed o1, KillFeed o2) {
			return Integer.compare(o2.getYPosition(), o1.getYPosition());
		}

	};

	private void processKillFeed() {
		if (update_tick % 1 <= 1 && !killfeed.isEmpty()) {
			Collection<KillFeed> remove = new ArrayList<>();
			for (KillFeed drop : killfeed) {
				drop.pulse();
				if (drop.getYPosition() == -1) {
					remove.add(drop);
				}
			}
			killfeed.removeAll(remove);
		}

		if (!drawKillFeed || openInterfaceID > -1) {
			return;
		}

		for (KillFeed drop : killfeed) {
			String killer = drop.getKiller();
			String victim = drop.getVictim();
			String style = drop.getStyle();
			//	System.out.println("killer: "+killer+" and width: "+newSmallFont.getTextWidth(killer) +"  victim: and width: "+newSmallFont.getTextWidth(victim)+"  style: "+style);
			int x = (!isResized() ? 450 : canvasWidth - 246) - newSmallFont.getTextWidth(killer) - newSmallFont.getTextWidth(victim);
			int y = drop.getYPosition() - 15;
			int transparency = 256;
			newSmallFont.drawString(killer, x, y, 0xFFFFFF, 0x000000, 256);
			Sprite itemSprite = null;
			if (style.equalsIgnoreCase("melee")) {
				itemSprite = ItemDefinition.getSmallSprite(11802);
			} else if (style.equalsIgnoreCase("mage")) {
				itemSprite = ItemDefinition.getSmallSprite(4675);
			} else {
				itemSprite = ItemDefinition.getSmallSprite(20997);
			}
			if (itemSprite != null)
				itemSprite.drawSprite(x + newSmallFont.getTextWidth(killer) + 10, y - 10);
			newSmallFont.drawString(victim, x + newSmallFont.getTextWidth(killer) + 30, y, 0xFFFFFF, 0x000000, 256);
		}
	}
	public void setNorth() {
		cameraOffsetX = 0;
		cameraOffsetY = 0;
		viewRotationOffset = 0;
		viewRotation = 0;
		minimapRotation = 0;
		minimapZoom = 0;
	}

	public void processTabClick() {
		if (clickMode3 == 1) {
			if (!isResized()) {
				int x = 516;
				int y = 168;
				int[] points = new int[] { 3, 41, 74, 107, 140, 173, 206, 244 };
				for (int index = 0; index < points.length - 1; index++) {
					if (MouseHandler.saveClickX >= x + points[index] && MouseHandler.saveClickX <= x + points[index + 1]) {
						if (MouseHandler.saveClickY >= y && MouseHandler.saveClickY <= y + 36) {
							if (Client.tabInterfaceIDs[index] != -1) {
								Client.tabID = index;
								Client.needDrawTabArea = true;
								Client.tabAreaAltered = true;
							}
						} else if (MouseHandler.saveClickY >= y + 298 && MouseHandler.saveClickY <= y + 36 + 298) {
							if (Client.tabInterfaceIDs[index + 7] != -1) {
								if (index + 7 == 13) {
									stream.createFrame(185);
									stream.writeWord(21406);
								}
								Client.tabID = index + 7;
								if(Client.tabID == 9){
								//	System.out.println("here");
									if(flashplayerpanel){
										flashplayerpanel = false;
									}
								}
								Client.needDrawTabArea = true;
								Client.tabAreaAltered = true;
							}
						}
						Client.needDrawTabArea = true;
						Client.tabAreaAltered = true;
					}
				}
			} else {
				int x = Client.canvasWidth - (stackTabs() ? 231 : 462);
				int y = Client.canvasHeight - (stackTabs() ? 73 : 37);
				for (int index = 0; index < 14; index++) {
					if (MouseHandler.saveClickX >= x && MouseHandler.saveClickX <= x + 33) {
						if (MouseHandler.saveClickY >= y && MouseHandler.saveClickY <= y + 36) {
							if (Client.tabInterfaceIDs[index] != -1) {
								Client.tabID = index;
								Client.needDrawTabArea = true;
								Client.tabAreaAltered = true;
							}
						} else if (stackTabs() && MouseHandler.saveClickY >= y + 36 && MouseHandler.saveClickY <= y + 36 + 36) {
							if (Client.tabInterfaceIDs[index + 7] != -1) {
								Client.tabID = index + 7;
								Client.needDrawTabArea = true;
								Client.tabAreaAltered = true;
							}
						}
					}
					x += 33;
				}
			}
		}
	}



	public String getDocumentBaseHost() {
		if (Signlink.mainapp != null) {
			return Signlink.mainapp.getDocumentBase().getHost().toLowerCase();
		}
		return "";
	}

	private void refreshMinimap(Sprite sprite, int j, int k) {
		int l = k * k + j * j;
		if (l > 4225 && l < 0x15f90) {
			int i1 = viewRotation + minimapRotation & 0x7ff;
			int j1 = Model.SINE[i1];
			int k1 = Model.COSINE[i1];
			j1 = (j1 * 256) / (minimapZoom + 256);
			k1 = (k1 * 256) / (minimapZoom + 256);
			int l1 = j * j1 + k * k1 >> 16;
			int i2 = j * k1 - k * j1 >> 16;
			double d = Math.atan2(l1, i2);
		} else {
			markMinimap(sprite, k, j);
		}
	}

	private void rightClickChatButtons() {
		if (fullscreenInterfaceID != -1) {
			return;
		}
		if (MouseHandler.mouseY >= (!isResized() ? 482 : canvasHeight - 22)
				&& MouseHandler.mouseY <= (!isResized() ? 503 : canvasHeight)) {

			if (MouseHandler.mouseX >= 5 && MouseHandler.mouseX <= 61) {
				menuActionName[1] = "View All";
				menuActionID[1] = 999;
				menuActionRow = 2;
			} else if (MouseHandler.mouseX >= 71 && MouseHandler.mouseX <= 127) {

				menuActionName[1] = "Switch tab";
				menuActionID[1] = 998;
				menuActionName[2] = "Configure filter";
				menuActionID[2] = 4005;
				menuActionName[3] = "@yel@Game: @whi@Filter";
				menuActionID[3] = 1005;
				menuActionRow = 4;
			} else if (MouseHandler.mouseX >= 137 && MouseHandler.mouseX <= 193) {
				menuActionName[1] = "@bl3@Setup your autochat";
				menuActionID[1] = 496;
				menuActionName[2] = "@gre@Filter public chat";
				menuActionID[2] = 495;
				menuActionName[3] = "@yel@Public: @whi@Clear history";
				menuActionID[3] = 497;
				menuActionName[4] = "@yel@Public: @whi@Hide";
				menuActionID[4] = 997;
				menuActionName[5] = "@yel@Public: @whi@Off";
				menuActionID[5] = 996;
				menuActionName[6] = "@yel@Public: @whi@Show friends";
				menuActionID[6] = 995;
				menuActionName[7] = "@yel@Public: @whi@Show standard";
				menuActionID[7] = 994;
				menuActionName[8] = "@yel@Public: @whi@Show autochat";
				menuActionID[8] = 494;
				menuActionName[9] = "Switch tab";
				menuActionID[9] = 993;
				menuActionRow = 10;
			} else if (MouseHandler.mouseX >= 203 && MouseHandler.mouseX <= 259) {
				menuActionName[1] = "@yel@Private: @whi@Clear history";
				menuActionID[1] = 1006;
				menuActionName[2] = "@yel@Private: @whi@Off";
				menuActionID[2] = 992;
				menuActionName[3] = "@yel@Private: @whi@Show friends";
				menuActionID[3] = 991;
				menuActionName[4] = "@yel@Private: @whi@Show all";
				menuActionID[4] = 990;
				menuActionName[5] = "Switch tab";
				menuActionID[5] = 989;
				menuActionRow = 6;
			} else if (MouseHandler.mouseX >= 269 && MouseHandler.mouseX <= 325) {
				menuActionName[1] = "@yel@Clan: @whi@Off";
				menuActionID[1] = 1003;
				menuActionName[2] = "@yel@Clan: @whi@Show friends";
				menuActionID[2] = 1002;
				menuActionName[3] = "@yel@Clan: @whi@Show all";
				menuActionID[3] = 1001;
				menuActionName[4] = "Switch tab";
				menuActionID[4] = 1000;
				menuActionRow = 5;
			} else if (MouseHandler.mouseX >= 335 && MouseHandler.mouseX <= 391) {
				menuActionName[1] = "@yel@Trade: @whi@Off";
				menuActionID[1] = 987;
				menuActionName[2] = "@yel@Trade: @whi@Show friends";
				menuActionID[2] = 986;
				menuActionName[3] = "@yel@Trade: @whi@Show all";
				menuActionID[3] = 985;
				menuActionName[4] = "Switch tab";
				menuActionID[4] = 984;
				menuActionRow = 5;
			}
		}
	}

	private boolean checkMainScreenBounds() {
		if (checkBounds(0, canvasWidth - (stackTabs() ? 231 : 462), canvasHeight - (stackTabs() ? 73 : 37),
				canvasWidth, canvasHeight)) {
			return false;
		}
		if (checkBounds(0, canvasWidth - 225, 0, canvasWidth, 170)) {
			return false;
		}
		if (checkBounds(0, canvasWidth - 204, canvasHeight - (stackTabs() ? 73 : 37) - 275, canvasWidth,
				canvasHeight)) {
			return false;
		}
		if (checkBounds(0, 0, canvasHeight - 168, 516, canvasHeight)) {
			return false;
		}
		return true;
	}

	public static boolean showChatComponents = true;
	public static boolean showTabComponents = true;
	public static boolean changeTabArea = false;
	public static boolean changeChatArea = false;

	public boolean getMousePositions() {
		if (mouseInRegion(canvasWidth - (canvasWidth <= 1000 ? 240 : 420),
				canvasHeight - (canvasWidth <= 1000 ? 90 : 37), canvasWidth, canvasHeight)) {
			return false;
		}
		if (showChatComponents) {
			if (changeChatArea) {
				if (MouseHandler.mouseX > 0 && MouseHandler.mouseX < 494
						&& MouseHandler.mouseY > canvasHeight - 175
						&& MouseHandler.mouseY < canvasHeight) {
					return true;
				} else {
					if (MouseHandler.mouseX > 494 && MouseHandler.mouseX < 515
							&& MouseHandler.mouseY > canvasHeight - 175
							&& MouseHandler.mouseY < canvasHeight) {
						return false;
					}
				}
			} else if (!changeChatArea) {
				if (MouseHandler.mouseX > 0 && MouseHandler.mouseX < 519
						&& MouseHandler.mouseY > canvasHeight - 175
						&& MouseHandler.mouseY < canvasHeight) {
					return false;
				}
			}
		}
		if (mouseInRegion(canvasWidth - 216, 0, canvasWidth, 172)) {
			return false;
		}
		if (!changeTabArea) {
			if (MouseHandler.mouseX > 0 && MouseHandler.mouseY > 0 && MouseHandler.mouseY < canvasWidth
					&& MouseHandler.mouseY < canvasHeight) {
				if (MouseHandler.mouseX >= canvasWidth - 242 && MouseHandler.mouseY >= canvasHeight - 335) {
					return false;
				}
				return true;
			}
			return false;
		}
		if (showTabComponents) {
			if (canvasWidth > 1000) {
				if (MouseHandler.mouseX >= canvasWidth - 420 && MouseHandler.mouseX <= canvasWidth
						&& MouseHandler.mouseY >= canvasHeight - 37
						&& MouseHandler.mouseY <= canvasHeight
						|| MouseHandler.mouseX > canvasWidth - 225 && MouseHandler.mouseX < canvasWidth
						&& MouseHandler.mouseY > canvasHeight - 37 - 274
						&& MouseHandler.mouseY < canvasHeight) {
					return false;
				}
			} else {
				if (MouseHandler.mouseX >= canvasWidth - 210 && MouseHandler.mouseX <= canvasWidth
						&& MouseHandler.mouseY >= canvasHeight - 74
						&& MouseHandler.mouseY <= canvasHeight
						|| MouseHandler.mouseX > canvasWidth - 225 && MouseHandler.mouseX < canvasWidth
						&& MouseHandler.mouseY > canvasHeight - 74 - 274
						&& MouseHandler.mouseY < canvasHeight) {
					return false;
				}
			}
		}
		return true;
	}

	private boolean checkBounds(int type, int x1, int y1, int x2, int y2) {
		if (type == 0)
			return getMouseX() >= x1 && getMouseX() <= x2 && getMouseY() >= y1 && getMouseY() <= y2;
		else
			return Math.pow((x1 + type - x2), 2) + Math.pow((y1 + type - y2), 2) < Math.pow(type, 2);
	}


	public Point getOnScreenWidgetOffsets() {
		boolean newTab = true;
		int w = 512, h = 334;
		int x = (canvasWidth / 2) - (newTab ? 286 : 256);
		int y = (canvasHeight / 2) - 169;

		if (x + 65656 > (canvasWidth - 225)) {
			x = x - 90;
			if (x < 0) {
				x = 0;
			}
		}
		if (y + 565656 > (canvasHeight - 192)) {
			y = y - 83;
			if (y < 0) {
				y = 0;
			}
		}
		return new Point(x, y);
	}

	private boolean stackTabs() {
		return !(canvasWidth >= 1100);
	}

	private void processRightClick() {
		if (loggedIn) {
			if (activeInterfaceType != 0)
				return;
			menuActionName[0] = "Cancel";
			menuActionID[0] = 1107;
			menuActionRow = 1;
		//	if (isResized()) {
				if (fullscreenInterfaceID != -1) {
					anInt886 = 0;
					anInt1315 = 0;
					buildInterfaceMenu((canvasWidth / 2) - 765 / 2,
							RSInterface.interfaceCache[fullscreenInterfaceID], MouseHandler.mouseX,
							(canvasHeight / 2) - 503 / 2, MouseHandler.mouseY, 0);
					if (anInt886 != anInt1026) {
						anInt1026 = anInt886;
					}
					if (anInt1315 != anInt1129) {
						anInt1129 = anInt1315;
					}
					return;
				}
		//	}
			buildSplitPrivateChatMenu();
	if(isbroadcastsEnabled())
			buildBroadcasts();
			anInt886 = 0;
			anInt1315 = 0;

			if (!isResized()) {
				if (getMouseX() > 0 && getMouseY() > 0 && getMouseX() < 516 && getMouseY() < 343) {
					if (openInterfaceID != -1) {
						buildInterfaceMenu(0, RSInterface.interfaceCache[openInterfaceID], getMouseX(), 0, getMouseY(), 0);
					} else {
						build3dScreenMenu();
					}
				}
			} else {
				if (checkMainScreenBounds()) {
					int interfaceX = (canvasWidth / 2) - 256 - 99;
					int interfaceY = (canvasHeight / 2) - 167 - 63;
					if (openInterfaceID != -1) {
						buildInterfaceMenu(interfaceX, RSInterface.interfaceCache[openInterfaceID],
							getMouseX(), interfaceY, getMouseY(), 0);
					}

					if (openInterfaceID == -1 || getMouseX() < interfaceX || getMouseY() < interfaceY || getMouseX() > interfaceX + 516 || getMouseY() > interfaceY + 338) {
						build3dScreenMenu();
					}
				}
			}

			if (anInt886 != anInt1026) {
				anInt1026 = anInt886;
			}
			if (anInt1315 != anInt1129) {
				anInt1129 = anInt1315;
			}

			anInt886 = 0;
			anInt1315 = 0;
			resetTabInterfaceHover();
			if (!isResized()) {
				if (getMouseX() > 516 && getMouseY() > 205 && getMouseX() < 765 && getMouseY() < 466) {
					if (invOverlayInterfaceID != 0) {
						buildInterfaceMenu(547, RSInterface.interfaceCache[invOverlayInterfaceID], getMouseX(), 205, getMouseY(),
								0);
					} else if (tabInterfaceIDs[tabID] != -1) {
						buildInterfaceMenu(547, RSInterface.interfaceCache[tabInterfaceIDs[tabID]], getMouseX(), 205, getMouseY(),
								0);
					} //the culprit?
				}
			} else {
				int y = stackTabs() ? 73 : 37;
				if (getMouseX() > canvasWidth - 197 && getMouseY() > canvasHeight - 275 - y + 10
						&& getMouseX() < canvasWidth - 7 && getMouseY() < canvasHeight - y - 5) {
					if (invOverlayInterfaceID != 0) {
						buildInterfaceMenu(canvasWidth - 197, RSInterface.interfaceCache[invOverlayInterfaceID],
								getMouseX(), canvasHeight - 275 - y + 10, getMouseY(), 0);
					} else if (tabInterfaceIDs[tabID] != -1) {
						buildInterfaceMenu(canvasWidth - 197, RSInterface.interfaceCache[tabInterfaceIDs[tabID]],
								getMouseX(), canvasHeight - 275 - y + 10, getMouseY(), 0);
					}
				}
			}
			if (anInt886 != anInt1048) {
				needDrawTabArea = true;
				tabAreaAltered = true;
				anInt1048 = anInt886;
			}
			if (anInt1315 != anInt1044) {
				needDrawTabArea = true;
				tabAreaAltered = true;
				anInt1044 = anInt1315;
			}
			anInt886 = 0;
			anInt1315 = 0;
			/* Chat area clicking */
			if (!isResized()) {
				if (getMouseX() > 0 && getMouseY() > 338 && getMouseX() < 490 && getMouseY() < 463) {
					if (backDialogID != -1)
						buildInterfaceMenu(20, RSInterface.interfaceCache[backDialogID], getMouseX(), 358, getMouseY(), 0);
					else if (getMouseY() < 463 && getMouseX() < 490)
						buildChatAreaMenu(getMouseY() - 338);
				}
			} else {
				if (getMouseX() > 0 && getMouseY() > canvasHeight - 165 && getMouseX() < 490 && getMouseY() < canvasHeight - 40) {
					if (backDialogID != -1)
						buildInterfaceMenu(20, RSInterface.interfaceCache[backDialogID], getMouseX(),
								canvasHeight - 145, getMouseY(), 0);
					else if (getMouseY() < canvasHeight - 40 && getMouseX() < 490)
						buildChatAreaMenu(getMouseY() - (canvasHeight - 165));
				}
			}
			if (backDialogID != -1 && anInt886 != anInt1039) {
				inputTaken = true;
				anInt1039 = anInt886;
			}
			if (backDialogID != -1 && anInt1315 != anInt1500) {
				inputTaken = true;
				anInt1500 = anInt1315;
			}
			/* Enable custom right click areas */
			if (anInt886 != anInt1026)
				anInt1026 = anInt886;
			anInt886 = 0;

			rightClickChatButtons();
			processMinimapActions();

			// Reverses menu actions!
			boolean flag = false;
			while (!flag) {
				flag = true;

				for (int j = 0; j < menuActionRow - 1; j++) {
					if (menuActionID[j] < 1000 && menuActionID[j + 1] > 1000) {
						String s = menuActionName[j];
						menuActionName[j] = menuActionName[j + 1];
						menuActionName[j + 1] = s;
						int k = menuActionID[j];
						menuActionID[j] = menuActionID[j + 1];
						menuActionID[j + 1] = k;
						k = menuActionCmd2[j];
						menuActionCmd2[j] = menuActionCmd2[j + 1];
						menuActionCmd2[j + 1] = k;
						k = menuActionCmd3[j];
						menuActionCmd3[j] = menuActionCmd3[j + 1];
						menuActionCmd3[j + 1] = k;
						long k2 = menuActionCmd1[j];
						menuActionCmd1[j] = menuActionCmd1[j + 1];
						menuActionCmd1[j + 1] = k2;
						flag = false;
					}
				}
			}
		}
	}

	private void processMinimapActions() {
	//	System.out.println("x: "+MouseHandler.mouseX+" y; "+MouseHandler.mouseY);
		final boolean fixed = !isResized();
		if (fixed ? MouseHandler.mouseX >= 542 && MouseHandler.mouseX <= 579 && MouseHandler.mouseY >= 2 && MouseHandler.mouseY <= 38
				: MouseHandler.mouseX >= Client.canvasWidth - 180 && MouseHandler.mouseX <= Client.canvasWidth - 139
				&& MouseHandler.mouseY >= 0 && MouseHandler.mouseY <= 40) {
			menuActionName[1] = "Look North";
			menuActionID[1] = 696;
			menuActionRow = 2;
		}
		if (counterHover && drawOrbs) {
			menuActionName[1] = drawExperienceCounter ? "Hide @or1@XP drops" : "Show @or1@XP drops";
			menuActionID[1] = 474;
			menuActionRow = 2;
		}
		if (worldHover && drawOrbs) {

			menuActionName[1] = "Teleports";
			menuActionID[1] = 1514;
			menuActionRow = 2;
		}
		if (wikiHover && drawOrbs) {
			menuActionName[1] = "1513";
			menuActionID[1] = 1513;
			menuActionRow = 2;
		}
		if (donatorHover && drawOrbs) {
			menuActionName[1] = "Prestige System";
			menuActionID[1] = 6251;
			menuActionRow = 2;
		}
		if (newHover && drawOrbs) {
			menuActionName[1] = "Relic Management";
			menuActionID[1] = 6252;
			menuActionRow = 2;
		}
		int mouseX1 = !isResized() ? 572 : canvasWidth - 175;
		int mouseX2 = !isResized() ? 600 : canvasWidth - 150;
		if (runHover) {
			menuActionName[1] = "Toggle Run";
			menuActionID[1] = 1050;
			menuActionRow = 2;
		}
		if (specialHover) {
			menuActionName[1] = "Toggle Special";
			menuActionID[1] = 1051;
			menuActionRow = 2;
		}
		if (prayHover && drawOrbs) {
			menuActionName[2] = prayClicked ? "Turn Quick Prayers Off" : "Turn Quick Prayers On";
			menuActionID[2] = 1500;
			menuActionRow = 2;
			menuActionName[1] = "Select Quick Prayers";
			menuActionID[1] = 1506;
			menuActionRow = 3;
		}
	}

	private int method83(int i, int j, int k) {
		int l = 256 - k;
		return ((i & 0xff00ff) * l + (j & 0xff00ff) * k & 0xff00ff00)
				+ ((i & 0xff00) * l + (j & 0xff00) * k & 0xff0000) >> 8;
	}

	public boolean missingUsername() {
		if (myUsername == null || myUsername.isEmpty()) {
			loginScreenCursorPos = 0;
			return true;
		}
		return false;
	}

	public boolean missingPassword() {
		if (getPassword() == null || getPassword().isEmpty()) {
			System.out.println("Empty password detected!");
			loginScreenCursorPos = 0;
			return true;
		}
		return false;
	}

	public boolean missingCaptchaInput() {
		if (loginScreenState == LoginScreenState.CAPTCHA && captchaInput.length() == 0 && captcha != null) {
			firstLoginMessage = "You must enter the captcha (case sensitive) or click x to exit.";
			return true;
		}
		return false;
	}

	public boolean nameWhitespace() {
		if (myUsername != null && myUsername.startsWith(" ") || myUsername.endsWith(" ") || myUsername.contains("  ")) {
			firstLoginMessage = "Invalid username whitespace usage. Please try again.";
			return true;
		}
		return false;
	}

	public void login(String s, String s1, boolean flag) {
		if (loggedIn) {
			return;
		}
		if (Configuration.developerMode) {
			System.out.println("Attempting connection to " + server + ":" + port);
		}
		if (missingPassword()) {
			loginScreenState = LoginScreenState.LOGIN;
			return;
		}
		if (missingCaptchaInput())
			return;

		captcha = null;
		fogEnabled = false;
		fogOpacity = 0;
		firstLoginMessage = "Connecting to server..";
		Signlink.errorname = s;
		resetTabInterfaceHover();

		try {
			if(myUsername.length() < 3) {
				firstLoginMessage = "Your username must be at least 3 characters long";
				loginScreenState = LoginScreenState.LOGIN;
				return;
			}
			if (!flag) {
				drawLoginScreen(true);
			}
			setConfigButton(23103, informationFile.isRememberRoof() ? true : false);
			socketStream = new RSSocket(openSocket(port + portOff));
			long l = StringUtils.longForName(s);
			int i = (int) (l >> 16 & 31L);
			stream.currentPosition = 0;
			stream.writeUnsignedByte(14);
			stream.writeUnsignedByte(i);
			socketStream.queueBytes(2, stream.payload);
			for (int j = 0; j < 8; j++)
				socketStream.read();

			int responseCode = socketStream.read();
			int i1 = responseCode;
			if (responseCode == 0) {
				new Identity().createIdentity();
				try {
					new Identity().loadIdentity();
				} catch (FileNotFoundException fnfe) {
					fnfe.printStackTrace();
				}
				socketStream.flushInputStream(inStream.payload, 8);
				inStream.currentPosition = 0;
				aLong1215 = inStream.readQWord();

				// Pow request
				stream.currentPosition = 0;
				stream.writeUnsignedByte(19);
				socketStream.queueBytes(1, stream.payload);
				int powRequestReturnCode = socketStream.read();
				if (powRequestReturnCode == 60) {
					int size = ((socketStream.read()) << 8) + (socketStream.read() & 0xff);
					socketStream.flushInputStream(inStream.payload, size);
					inStream.currentPosition = 0;
					int randomValue = inStream.readUShort();
					int difficulty = inStream.readUShort();
					String seed = inStream.readNewString();

					// Pow process
					long start = System.currentTimeMillis();
					long answer = Hash.run(randomValue, difficulty, seed);
					long time = System.currentTimeMillis() - start;
					logger.debug("Took {}ms to finish POW.", time);

					// Pow check
					Buffer powStream = Buffer.create();
					powStream.writeByte(20); // Pow check opcode
					powStream.writeQWord(answer);
					this.socketStream.queueBytes(powStream.currentPosition, powStream.payload);

					int response = socketStream.read();
					if (response == 0) {
						socketStream.flushInputStream(inStream.payload, 8);
						inStream.currentPosition = 0;
						inStream.readQWord();
					}

					int ai[] = new int[4];
					ai[0] = (int) (Math.random() * 99999999D);
					ai[1] = (int) (Math.random() * 99999999D);
					ai[2] = (int) (aLong1215 >> 32);
					ai[3] = (int) aLong1215;
					stream.currentPosition = 0;
					stream.writeUnsignedByte(10);
					stream.writeDWord(ai[0]);
					stream.writeDWord(ai[1]);
					stream.writeDWord(ai[2]);
					stream.writeDWord(ai[3]);
					stream.writeDWord(Signlink.uid);
					stream.writeString(s);
					stream.writeString(s1);
					stream.writeString(captchaInput);
					stream.writeString(macAddress);
					stream.writeString(FingerPrint.getFingerprint());
					stream.doKeys();
					aStream_847.currentPosition = 0;
					if (flag)
						aStream_847.writeUnsignedByte(18);
					else
						aStream_847.writeUnsignedByte(16);
					aStream_847.writeUnsignedByte(stream.currentPosition + 36 + 1 + 1 + 2);
					aStream_847.writeUnsignedByte(255);
					aStream_847.writeWord(Configuration.CLIENT_VERSION);
					aStream_847.writeUnsignedByte(lowMem ? 1 : 0);
					for (int l1 = 0; l1 < 9; l1++)
						aStream_847.writeDWord(expectedCRCs[l1]);

					aStream_847.writeBytes(stream.payload, stream.currentPosition, 0);
					stream.encryption = new ISAACRandomGen(ai);
					for (int j2 = 0; j2 < 4; j2++)
						ai[j2] += 50;

					encryption = new ISAACRandomGen(ai);
					socketStream.queueBytes(aStream_847.currentPosition, aStream_847.payload);
					responseCode = socketStream.read();
				}
			}
			if (responseCode == 1) {
				try {
					Thread.sleep(2000L);
				} catch (Exception _ex) {
				}
				login(s, s1, flag);
				return;
			}
			if (responseCode == 2) {
				Arrays.stream(RSInterface.interfaceCache).filter(Objects::nonNull).forEach(rsi -> rsi.scrollPosition = 0);//nuts way to reset scroll pos
				setGameState(GameState.LOGGED_IN);
				Preferences.getPreferences().updateClientConfiguration();
				Preferences.getPreferences().updateClientConfiguration();
				@SuppressWarnings("unused")
				int rights = socketStream.read();
				flagged = socketStream.read() == 1;
				logger.debug("Login accepted, rights={}, flagged={}", rights, flagged);
				aLong1220 = 0L;
				anInt1022 = 0;
				//mouseDetection.coordsIndex = 0;

				aBoolean954 = true;
				loggedIn = true;
				stream.currentPosition = 0;
				inStream.currentPosition = 0;
				incomingPacket = -1;
				dealtWithPacket = -1;
				previousPacket1 = -1;
				dealtWithPacketSize = -1;
				previousPacket2 = -1;
				previousPacketSize1 = -1;
				previousPacketSize2 = -1;
				packetSize = 0;
				anInt1009 = 0;
				anInt1104 = 0;
				anInt1011 = 0;
				hintType = 0;
				menuActionRow = 0;
				menuOpen = false;

				for (int j1 = 0; j1 < 500; j1++)
					chatMessages[j1] = null;

				itemSelected = 0;
				spellSelected = 0;
				loadingStage = 0;
				setNorth();
				setBounds();
				minimapState = 0;
				anInt985 = -1;
				destX = 0;
				destY = 0;
				playerCount = 0;
				npcCount = 0;
				for (int i2 = 0; i2 < maxPlayers; i2++) {
					players[i2] = null;
					localplayer_appearance_data[i2] = null;
					//localplayer_movespeeds[i2] = MoveSpeed.WALK;
				}

				for (int k2 = 0; k2 < 16384; k2++)
					npcs[k2] = null;

				localPlayer = players[maxPlayerCount] = new Player();
				projectiles.removeAll();
				incompleteAnimables.removeAll();
				for (int l2 = 0; l2 < 4; l2++) {
					for (int i3 = 0; i3 < 104; i3++) {
						for (int k3 = 0; k3 < 104; k3++)
							groundItems[l2][i3][k3] = null;

					}

				}
				regenSpecStart = System.currentTimeMillis();


				spawns = new Deque();

				fullscreenInterfaceID = isLoginSplashEnabled()  ? 15244 : -1;
				anInt900 = 0;
				friendsCount = 0;
				dialogID = -1;
				backDialogID = -1;
				openInterfaceID = isLoginSplashEnabled()  ? 15767 : -1;;
				invOverlayInterfaceID = 0;
				openWalkableWidgetID = -1;
				aBoolean1149 = false;
				tabID = 3;
				inputDialogState = 0;
				menuOpen = false;
				messagePromptRaised = false;
				clickToContinueString = null;
				anInt1055 = 0;
				aBoolean1047 = true;
				method45();
				for (int j3 = 0; j3 < 5; j3++)
					anIntArray990[j3] = 0;

				for (int l3 = 0; l3 < 6; l3++) {
					atPlayerActions[l3] = null;
					atPlayerArray[l3] = false;
				}

				anInt1175 = 0;
				anInt1134 = 0;
				anInt986 = 0;
				anInt1288 = 0;
				anInt924 = 0;
				anInt1188 = 0;
				anInt1155 = 0;
				anInt1226 = 0;
				rapidHeal = false;
				regenHealthStart = System.currentTimeMillis();
				setBounds();
				Interfaces.taskInterface.actions.onButtonClick(10405);
				Interfaces.taskInterface.actions.onButtonClick(10406);
				if (informationFile.isUsernameRemembered()) {
					informationFile.setStoredUsername(myUsername);
				}

				if (informationFile.isPasswordRemembered()) {
					informationFile.setStoredPassword(getPassword());
				}

				informationFile.write();
//sendvotecheck(0);
//				senddonocheck(0);
				gettime();

				getRestoreOverlay().boostedskills.clear();
				return;
			}
			if (responseCode == 3) {
				loginScreenState = LoginScreenState.LOGIN;
				if (missingPassword()) {
					return;
				}
				if (nameWhitespace()) {
					return;
				}
				firstLoginMessage = "Invalid username or password.";
				return;
			}
			if (responseCode == 4) {
				loginScreenState = LoginScreenState.LOGIN;
				firstLoginMessage = "Your account has been disabled.";
				return;
			}
			if (responseCode == 5) {
				loginScreenState = LoginScreenState.LOGIN;
				firstLoginMessage = "Your account is already logged in.";
				return;
			}
			if (responseCode == 6) {
				loginScreenState = LoginScreenState.LOGIN;
				firstLoginMessage = Configuration.CLIENT_TITLE + " has been updated - restart the launcher!";
				return;
			}
			if (responseCode == 7) {
				loginScreenState = LoginScreenState.LOGIN;
				firstLoginMessage = "This world is full.";
				return;
			}
			if (responseCode == 8) {
				loginScreenState = LoginScreenState.LOGIN;
				firstLoginMessage = "Unable to connect.";
				return;
			}
			if (responseCode == 9) {
				loginScreenState = LoginScreenState.LOGIN;
				firstLoginMessage = "Login limit exceeded.";
				return;
			}
			if (responseCode == 10) {
				loginScreenState = LoginScreenState.LOGIN;
				firstLoginMessage = "Unable to connect. Bad session id.";
				return;
			}
			if (responseCode == 11) {
				loginScreenState = LoginScreenState.LOGIN;
				firstLoginMessage = "Your username is too long.";
				return;
			}
			if (responseCode == 12) {
				loginScreenState = LoginScreenState.LOGIN;
				firstLoginMessage = "Unknown error 12.";
				return;
			}
			if (responseCode == 13) {
				loginScreenState = LoginScreenState.LOGIN;
				firstLoginMessage = "Too many connection attempts, please wait before trying again.";
				return;
			}
			if (responseCode == 14) {
				loginScreenState = LoginScreenState.LOGIN;
				firstLoginMessage = Configuration.CLIENT_TITLE + " is currently being updated.";
				return;
			}
			if (responseCode == 15) {
				loginScreenState = LoginScreenState.LOGIN;
				loggedIn = true;
				stream.currentPosition = 0;
				inStream.currentPosition = 0;
				incomingPacket = -1;
				dealtWithPacket = -1;
				previousPacket1 = -1;
				previousPacket2 = -1;
				packetSize = 0;
				anInt1009 = 0;
				anInt1104 = 0;
				menuActionRow = 0;
				menuOpen = false;
				longStartTime = System.currentTimeMillis();
				return;
			}
			if (responseCode == 16) {
				loginScreenState = LoginScreenState.LOGIN;
				firstLoginMessage = "Unknown error 16.";
				return;
			}
			if (responseCode == 17) {
				loginScreenState = LoginScreenState.LOGIN;
				firstLoginMessage = "Unknown error 17.";
				return;
			}
			if (responseCode == 20) {
				loginScreenState = LoginScreenState.LOGIN;
				firstLoginMessage = "Unknown error 20.";
				return;
			}
			if (responseCode == 21) {
				loginScreenState = LoginScreenState.LOGIN;
				for (int k1 = socketStream.read(); k1 >= 0; k1--) {

					drawLoginScreen(true);
					try {
						Thread.sleep(1000L);
					} catch (Exception _ex) {
					}
				}

				login(s, s1, flag);
				return;
			}
			if (responseCode == 22) {
				loginScreenState = LoginScreenState.LOGIN;
				firstLoginMessage = "Unknown error 22.";
				return;
			}
			if (responseCode == 23) {
				loginScreenState = LoginScreenState.LOGIN;
				for (int k1 = socketStream.read(); k1 >= 0; k1--) {

					drawLoginScreen(true);
					try {
						Thread.sleep(1000L);
					} catch (Exception _ex) {
					}
				}

				login(s, s1, flag);
				return;
			}
			if (responseCode == 24) {
				loginScreenState = LoginScreenState.LOGIN;
				firstLoginMessage = "Unknown error 24.";
				return;
			}
			if (responseCode == 25) {
				loginScreenState = LoginScreenState.LOGIN;
				firstLoginMessage = "Unknown error 25.";
				return;
			}
			if (responseCode == 26) {
				loginScreenState = LoginScreenState.LOGIN;
				firstLoginMessage = "An error occurred while loading your file, contact support.";
				return;
			}
			if (responseCode == 27 || responseCode == 28) {
				try {
					int length = ((socketStream.read() & 0xFF) << 8) + socketStream.read();
					byte[] captchaData = new byte[length];
					for (int i12 = 0; i12 < length; i12++)
						captchaData[i12] = (byte) socketStream.read();
					captcha = new Sprite(BufferedImages.toBufferedImage(captchaData));
					captcha.setTransparency(45, 45, 45);
					firstLoginMessage = responseCode == 27 ? "Enter the captcha (case sensitive)." : "Incorrect, enter the captcha (case sensitive).";
					synchronized (CAPTCHA_LOCK) {
						captchaInput = "";
					}
					loginScreenState = LoginScreenState.CAPTCHA;
				} catch (IOException e) {
					firstLoginMessage = "Captcha error occurred, contact staff.";
					e.printStackTrace();
				}

				return;
			}
			if (responseCode == -1) {
				if (i1 == 0) {
					if (loginFailures < 2) {
						try {
							Thread.sleep(2000L);
						} catch (Exception _ex) {
						}
						loginFailures++;
						login(s, s1, flag);
						return;
					}
				} else {
					try {
						Thread.sleep(2000);
					} catch (InterruptedException ignored) { }
					firstLoginMessage = "Error connecting to server, please try again.";
					return;
				}
			} else {
				return;
			}
		} catch (IOException _ex) {
			if (Configuration.developerMode)
				_ex.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (missingPassword()) {
			loginScreenState = LoginScreenState.LOGIN;
			return;
		}
		loginScreenState = LoginScreenState.LOGIN;
		logger.debug("Went to end of login method.");
		firstLoginMessage = "Updates are incoming... Check our discord!";
	}

public void clearinterface() {
	if (invOverlayInterfaceID != 0) {
		invOverlayInterfaceID = 0;
		needDrawTabArea = true;
		tabAreaAltered = true;
	}
	if (backDialogID != -1) {
		backDialogID = -1;
		inputTaken = true;
	}
	if (inputDialogState != 0) {
		inputDialogState = 0;
		inputTaken = true;
	}
	if (this.isFieldInFocus()) {
		this.resetInputFieldFocus();
		Client.inputString = "";
	}
	openInterfaceID = -1;
	aBoolean1149 = false;
}
	// Original signature int clickType, int j, int k, int i1, int localY, int k1, int l1, int i2, int localX, boolean flag, int k2
	private boolean doWalkTo(int clickType, int localX, int localY, int j, int k, int i1, int k1, int l1, int i2, boolean flag, int k2) {
		byte byte0 = 104;
		byte byte1 = 104;
		for (int l2 = 0; l2 < byte0; l2++) {
			for (int i3 = 0; i3 < byte1; i3++) {
				anIntArrayArray901[l2][i3] = 0;
				anIntArrayArray825[l2][i3] = 0x5f5e0ff;
			}
		}
		int j3 = localX;
		int k3 = localY;
		anIntArrayArray901[localX][localY] = 99;
		anIntArrayArray825[localX][localY] = 0;
		int l3 = 0;
		int i4 = 0;
		bigX[l3] = localX;
		bigY[l3++] = localY;
		boolean flag1 = false;
		int j4 = bigX.length;
		int ai[][] = collisionMaps[plane].clipData;
		while (i4 != l3) {
			j3 = bigX[i4];
			k3 = bigY[i4];
			i4 = (i4 + 1) % j4;
			if (j3 == k2 && k3 == i2) {
				flag1 = true;
				break;
			}
			if (i1 != 0) {
				if ((i1 < 5 || i1 == 10) && collisionMaps[plane].method219(k2, j3, k3, j, i1 - 1, i2)) {
					flag1 = true;
					break;
				}
				if (i1 < 10 && collisionMaps[plane].method220(k2, i2, k3, i1 - 1, j, j3)) {
					flag1 = true;
					break;
				}
			}
			if (k1 != 0 && k != 0 && collisionMaps[plane].atObject(i2, k2, j3, k, l1, k1, k3)) {
				flag1 = true;
				break;
			}
			int l4 = anIntArrayArray825[j3][k3] + 1;
			if (j3 > 0 && anIntArrayArray901[j3 - 1][k3] == 0 && (ai[j3 - 1][k3] & 0x1280108) == 0) {
				bigX[l3] = j3 - 1;
				bigY[l3] = k3;
				l3 = (l3 + 1) % j4;
				anIntArrayArray901[j3 - 1][k3] = 2;
				anIntArrayArray825[j3 - 1][k3] = l4;
			}
			if (j3 < byte0 - 1 && anIntArrayArray901[j3 + 1][k3] == 0 && (ai[j3 + 1][k3] & 0x1280180) == 0) {
				bigX[l3] = j3 + 1;
				bigY[l3] = k3;
				l3 = (l3 + 1) % j4;
				anIntArrayArray901[j3 + 1][k3] = 8;
				anIntArrayArray825[j3 + 1][k3] = l4;
			}
			if (k3 > 0 && anIntArrayArray901[j3][k3 - 1] == 0 && (ai[j3][k3 - 1] & 0x1280102) == 0) {
				bigX[l3] = j3;
				bigY[l3] = k3 - 1;
				l3 = (l3 + 1) % j4;
				anIntArrayArray901[j3][k3 - 1] = 1;
				anIntArrayArray825[j3][k3 - 1] = l4;
			}
			if (k3 < byte1 - 1 && anIntArrayArray901[j3][k3 + 1] == 0 && (ai[j3][k3 + 1] & 0x1280120) == 0) {
				bigX[l3] = j3;
				bigY[l3] = k3 + 1;
				l3 = (l3 + 1) % j4;
				anIntArrayArray901[j3][k3 + 1] = 4;
				anIntArrayArray825[j3][k3 + 1] = l4;
			}
			if (j3 > 0 && k3 > 0 && anIntArrayArray901[j3 - 1][k3 - 1] == 0 && (ai[j3 - 1][k3 - 1] & 0x128010e) == 0
					&& (ai[j3 - 1][k3] & 0x1280108) == 0 && (ai[j3][k3 - 1] & 0x1280102) == 0) {
				bigX[l3] = j3 - 1;
				bigY[l3] = k3 - 1;
				l3 = (l3 + 1) % j4;
				anIntArrayArray901[j3 - 1][k3 - 1] = 3;
				anIntArrayArray825[j3 - 1][k3 - 1] = l4;
			}
			if (j3 < byte0 - 1 && k3 > 0 && anIntArrayArray901[j3 + 1][k3 - 1] == 0
					&& (ai[j3 + 1][k3 - 1] & 0x1280183) == 0 && (ai[j3 + 1][k3] & 0x1280180) == 0
					&& (ai[j3][k3 - 1] & 0x1280102) == 0) {
				bigX[l3] = j3 + 1;
				bigY[l3] = k3 - 1;
				l3 = (l3 + 1) % j4;
				anIntArrayArray901[j3 + 1][k3 - 1] = 9;
				anIntArrayArray825[j3 + 1][k3 - 1] = l4;
			}
			if (j3 > 0 && k3 < byte1 - 1 && anIntArrayArray901[j3 - 1][k3 + 1] == 0
					&& (ai[j3 - 1][k3 + 1] & 0x1280138) == 0 && (ai[j3 - 1][k3] & 0x1280108) == 0
					&& (ai[j3][k3 + 1] & 0x1280120) == 0) {
				bigX[l3] = j3 - 1;
				bigY[l3] = k3 + 1;
				l3 = (l3 + 1) % j4;
				anIntArrayArray901[j3 - 1][k3 + 1] = 6;
				anIntArrayArray825[j3 - 1][k3 + 1] = l4;
			}
			if (j3 < byte0 - 1 && k3 < byte1 - 1 && anIntArrayArray901[j3 + 1][k3 + 1] == 0
					&& (ai[j3 + 1][k3 + 1] & 0x12801e0) == 0 && (ai[j3 + 1][k3] & 0x1280180) == 0
					&& (ai[j3][k3 + 1] & 0x1280120) == 0) {
				bigX[l3] = j3 + 1;
				bigY[l3] = k3 + 1;
				l3 = (l3 + 1) % j4;
				anIntArrayArray901[j3 + 1][k3 + 1] = 12;
				anIntArrayArray825[j3 + 1][k3 + 1] = l4;
			}
		}
		anInt1264 = 0;
		if (!flag1) {
			if (flag) {
				int i5 = 100;
				for (int k5 = 1; k5 < 2; k5++) {
					for (int i6 = k2 - k5; i6 <= k2 + k5; i6++) {
						for (int l6 = i2 - k5; l6 <= i2 + k5; l6++)
							if (i6 >= 0 && l6 >= 0 && i6 < 104 && l6 < 104 && anIntArrayArray825[i6][l6] < i5) {
								i5 = anIntArrayArray825[i6][l6];
								j3 = i6;
								k3 = l6;
								anInt1264 = 1;
								flag1 = true;
							}

					}

					if (flag1)
						break;
				}

			}
			if (!flag1)
				return false;
		}
		i4 = 0;
		bigX[i4] = j3;
		bigY[i4++] = k3;
		int l5;
		for (int j5 = l5 = anIntArrayArray901[j3][k3]; j3 != localX || k3 != localY; j5 = anIntArrayArray901[j3][k3]) {
			if (j5 != l5) {
				l5 = j5;
				bigX[i4] = j3;
				bigY[i4++] = k3;
			}
			if ((j5 & 2) != 0)
				j3++;
			else if ((j5 & 8) != 0)
				j3--;
			if ((j5 & 1) != 0)
				k3++;
			else if ((j5 & 4) != 0)
				k3--;
		}
		// if(cancelWalk) { return i4 > 0; }

		if (i4 > 0) {
			int k4 = i4;
			if (k4 > 25)
				k4 = 25;
			i4--;
			int k6 = bigX[i4];
			int i7 = bigY[i4];
			anInt1288 += k4;
			if (anInt1288 >= 92) {
				stream.createFrame(36);
				stream.writeDWord(0);
				anInt1288 = 0;
			}
			if (clickType == 0) {
				stream.createFrame(164);
				stream.writeUnsignedByte(k4 + k4 + 3);
			}
			if (clickType == 1) {
				stream.createFrame(248);
				stream.writeUnsignedByte(k4 + k4 + 3 + 14);
			}
			if (clickType == 2) {
				stream.createFrame(98);
				stream.writeUnsignedByte(k4 + k4 + 3);
			}
			stream.method433(k6 + baseX);
			destX = bigX[0];
			destY = bigY[0];
			for (int j7 = 1; j7 < k4; j7++) {
				i4--;
				stream.writeUnsignedByte(bigX[i4] - k6);
				stream.writeUnsignedByte(bigY[i4] - i7);
			}

			stream.method431(i7 + baseY);
			stream.method424(KeyHandler.keyArray[5] != 1 ? 0 : 1);
			return true;
		}
		return clickType != 1;
	}

	public void updated_extended_npcinfo(Buffer stream) {
		for (int j = 0; j < anInt893; j++) {
			int k = entity_extended_info_list[j];
			Npc npc = npcs[k];
			int l = stream.readUShort();
			if((l & 0x100) != 0)
			{
				npc.exactmove_x1 = stream.get_unsignedbyte();
				npc.exactmove_z1 = stream.get_unsignedbyte();
				npc.exactmove_x2 = stream.get_unsignedbyte();
				npc.exactmove_z2 = stream.get_unsignedbyte();
				npc.exactmove_start = stream.readUShort() + update_tick;
				npc.exactmove_end = stream.readUShort() + update_tick;
				npc.forceMovementDirection = stream.get_unsignedbyte();
				npc.clear_waypoint();
			}
			if ((l & 0x10) != 0) {
				int i1 = stream.method434();
				if (i1 == 65535)
					i1 = -1;
				int i2 = stream.get_unsignedbyte();
				if (i1 == npc.primaryanim && i1 != -1) {
					int l2 = AnimationDefinition.anims[i1].looptype;
					if (l2 == 1) {
						npc.primaryanim_frameindex = 0;
						npc.primaryanim_loops_remaining = 0;
						npc.primaryanim_pause = i2;
						npc.primaryanim_replaycount = 0;
					}
					if (l2 == 2)
						npc.primaryanim_replaycount = 0;
				} else if (i1 == -1 || npc.primaryanim == -1
						|| AnimationDefinition.anims[i1].priority >= AnimationDefinition.anims[npc.primaryanim].priority) {
					npc.primaryanim = i1;
					npc.primaryanim_frameindex = 0;
					npc.primaryanim_loops_remaining = 0;
					npc.primaryanim_pause = i2;
					npc.primaryanim_replaycount = 0;
					npc.anim_delay = npc.waypoint_count;
				}
			}
			if ((l & 8) != 0) {
				int j1 = stream.method426();
				int j2 = stream.method427();
				npc.updateHitData(j2, j1, update_tick);
				npc.loopCycleStatus = update_tick + 300;
				npc.currentHealth = stream.readUShort();
				npc.maxHealth = stream.readUShort();
			}
			if ((l & 0x80) != 0) {
				npc.spotanim = stream.readUShort();
				int k1 = stream.readDWord();
				npc.spotanim_height = k1 >> 16;
				npc.spotanim_start_loop = update_tick + (k1 & 0xffff);
				npc.spotanimframe_index = 0;
				npc.spotanim_loop = 0;
				if (npc.spotanim_start_loop > update_tick)
					npc.spotanimframe_index = -1;
				if (npc.spotanim == 65535)
					npc.spotanim = -1;
			}
			if ((l & 0x20) != 0) {
				npc.interactingEntity = stream.readUShort();
				if (npc.interactingEntity == 65535)
					npc.interactingEntity = -1;
			}
			if ((l & 1) != 0) {
				npc.textSpoken = stream.readString();
				npc.textCycle = 100;
			}
			if ((l & 0x40) != 0) {
				int l1 = stream.method427();
				int k2 = stream.method428();
				npc.updateHitData(k2, l1, update_tick);
				npc.loopCycleStatus = update_tick + 300;
				npc.currentHealth = stream.readUShort();
				npc.maxHealth = stream.readUShort();
			}
			if ((l & 2) != 0) {
				npc.desc = NpcDefinition.lookup(stream.method436());
				npc.size = npc.desc.size;
				npc.turnspeed = npc.desc.turnspeed;
				npc.walkanim = npc.desc.walkanim;
				npc.walkanim_b = npc.desc.walkanim_b;
				npc.walkanim_l = npc.desc.walkanim_l;
				npc.walkanim_r = npc.desc.walkanim_r;
				npc.readyanim = npc.desc.readyanim;
				npc.readyanim_l = npc.desc.readyanim_l;
				npc.readyanim_r = npc.desc.readyanim_r;

			}
			if ((l & 4) != 0) {
				npc.face_x = stream.method434();
				npc.face_z = stream.method434();
				npc.instant_facing = stream.readByte() == 1;
			}
		}
	}

	private long debugDelay;

	public void buildAtNPCMenu(NpcDefinition entityDef, int i, int j, int k) {
		if (menuActionRow >= 400)
			return;
		if (entityDef.multi != null)
			entityDef = entityDef.get_multi_npctype();
		if (entityDef == null)
			return;
		if (!entityDef.active)
			return;
		String s = entityDef.name;
		if (entityDef.combatLevel != 0)
			s = s + combatDiffColor(localPlayer.combatLevel, entityDef.combatLevel) + " (level-" + entityDef.combatLevel
					+ ")";
		if (itemSelected == 1) {
			menuActionName[menuActionRow] = "Use " + selectedItemName + " with @yel@" + s;
			menuActionID[menuActionRow] = 582;
			menuActionCmd1[menuActionRow] = i;
			menuActionCmd2[menuActionRow] = k;
			menuActionCmd3[menuActionRow] = j;
			menuActionRow++;
			return;
		}
		boolean drawdrops = false;
		if(entityDef.actions != null)
			for(String action : entityDef.actions){
				if(action == null)
					continue;
				if(action.equalsIgnoreCase("Attack")){
					drawdrops = true;
				}
			}
		if(drawdrops){
			menuActionName[menuActionRow] = "View drops <col=ffff00>" + s;
			menuActionID[menuActionRow] = 888;
			menuActionCmd1[menuActionRow] = i;//npcIndex
			menuActionCmd2[menuActionRow] = k;//npcArrayIndex
			menuActionCmd3[menuActionRow] = j;
			menuActionRow++;
		}

		if (spellSelected == 1) {
			if ((spellUsableOn & 2) == 2) {
				menuActionName[menuActionRow] = spellTooltip + " @yel@" + s;
				menuActionID[menuActionRow] = 413;
				menuActionCmd1[menuActionRow] = i;
				menuActionCmd2[menuActionRow] = k;
				menuActionCmd3[menuActionRow] = j;
				menuActionRow++;
			}
		} else {
			if (entityDef.actions != null) {
				for (int l = 4; l >= 0; l--)
					if (entityDef.actions[l] != null && !entityDef.actions[l].equalsIgnoreCase("attack")) {
						menuActionName[menuActionRow] = entityDef.actions[l] + " @yel@" + s;
						if (l == 0)
							menuActionID[menuActionRow] = 20;
						if (l == 1)
							menuActionID[menuActionRow] = 412;
						if (l == 2)
							menuActionID[menuActionRow] = 225;
						if (l == 3)
							menuActionID[menuActionRow] = 965;
						if (l == 4)
							menuActionID[menuActionRow] = 478;
						menuActionCmd1[menuActionRow] = i;
						menuActionCmd2[menuActionRow] = k;
						menuActionCmd3[menuActionRow] = j;
						menuActionRow++;
					}

			}
			if (entityDef.actions != null) {
				for (int i1 = 4; i1 >= 0; i1--) {
					if (entityDef.actions[i1] != null && entityDef.actions[i1].equalsIgnoreCase("attack")) {
						char c = '\0';
						if (Configuration.npcAttackOptionPriority == 3)
							continue;
						if (Configuration.npcAttackOptionPriority == 0 && entityDef.combatLevel > localPlayer.combatLevel
								|| Configuration.npcAttackOptionPriority == 1)
							c = '\u07D0';
						menuActionName[menuActionRow] = entityDef.actions[i1] + " @yel@" + s;
						if (i1 == 0)
							menuActionID[menuActionRow] = 20 + c;
						if (i1 == 1)
							menuActionID[menuActionRow] = 412 + c;
						if (i1 == 2)
							menuActionID[menuActionRow] = 225 + c;
						if (i1 == 3)
							menuActionID[menuActionRow] = 965 + c;
						if (i1 == 4)
							menuActionID[menuActionRow] = 478 + c;
						menuActionCmd1[menuActionRow] = i;
						menuActionCmd2[menuActionRow] = k;
						menuActionCmd3[menuActionRow] = j;
						menuActionRow++;
					}
				}

			}
			//so thats how you do if else without closing it!  look you have a lot of stuff in first {
			if (clientData) {
				Npc npc = npcs[i];
				menuActionName[menuActionRow] = "Examine @yel@" + s
						+ " @whi@("
						+ "id=" + entityDef.npcId
						+ ", index=" + i
						+ ", stand=" + entityDef.readyanim
						+ ", walk=" + entityDef.walkanim
						+ ", pos=[" + npc.getAbsoluteX() + ", " + npc.getAbsoluteY() + "]"
						+ ", size=" + entityDef.size
						+ ")";
			} else
				menuActionName[menuActionRow] = "Examine @yel@" + s;



			menuActionID[menuActionRow] = 1025;
			menuActionCmd1[menuActionRow] = i;
			menuActionCmd2[menuActionRow] = k;
			menuActionCmd3[menuActionRow] = j;
			menuActionRow++;

			if (debugModels == true) {
				if (System.currentTimeMillis() - debugDelay > 1000 && entityDef.models != null) {
					String modelIds = Arrays.toString(entityDef.models);
					String regColors = Arrays.toString(entityDef.originalColors);
					String newColors = Arrays.toString(entityDef.newColors);
					int standAnim = entityDef.readyanim;
					int walkAnim = entityDef.walkanim;
					String name = entityDef.name;
					System.out.println(name + modelIds);
					pushMessage(name + ": " + modelIds, 0, "");
					pushMessage("Reg: " + regColors, 0, "");
					pushMessage("New: " + newColors, 0, "");
					pushMessage("stand: " + standAnim, 0, "");
					pushMessage("walk: " + walkAnim, 0, "");
					debugDelay = System.currentTimeMillis();
					// menuActionName[menuActionRow] = "Examine
					// @gre@O(@whi@"+Arrays.toString(entityDef.models)+")@gre@)";
					// menuActionName[menuActionRow] = "#2
					// (@whi@"+Arrays.toString(entityDef.originalColors)+")@gre@)(@whi@"+Arrays.toString(entityDef.newColors)+")@gre@)";
				}
			}

		}
	}
	public void resetsidebars_teleportinterface() {
		for (int i = 0; i < NewTele.CATEGORY_NAMES.length; i++) {
			RSInterface.interfaceCache[88005 + i].sprite2 = new Sprite("interfaces/newtele/SPRITE 2064");

		}
	}
	public void resetsidebars_mailboxslots(int slots) {
		for (int i = 0; i < 20; i++) {
			RSInterface.interfaceCache[111_005 + i].sprite2 = new Sprite("interfaces/mailbox/SPRITE 1");

		}
		highlightmailboxslots(slots);
	}

	public void highlightmailboxslots(int slots) {
		for (int i = 0; i < slots; i++) {
			RSInterface.interfaceCache[111_005 + i].sprite2 = new Sprite("interfaces/membershipbenefits/SPRITE 2");

		}
	}
	public void resetsidebars_membershipbuttons(int slots) {
		for (int i = 0; i < 5; i++) {
			RSInterface.interfaceCache[113_200 + i].sprite2 = new Sprite("interfaces/membershipbenefits/SPRITE 1");

	}
		highlightresetsidebarsmembershipbuttonss(slots);
	}

	public void highlightresetsidebarsmembershipbuttonss(int slots) {
		//for (int i = 0; i < slots; i++) {
			RSInterface.interfaceCache[113_200 + slots].sprite2 = new Sprite("interfaces/membershipbenefits/SPRITE 8");

		//}
	}
	public void openjustfavorites() {
		resetsidebars_teleportinterface();
		//RSInterface.interfaceCache[88005].sprite1 =  new Sprite("interfaces/newtele/SPRITE 2065");
		RSInterface.interfaceCache[88005].sprite2 =  new Sprite("interfaces/newtele/SPRITE 2065");
	}
	public void resettabselections() {

		//player tab panel
		RSInterface mainpanel = RSInterface.interfaceCache[80100];
		RSInterface.handleConfigHover(mainpanel);
		Interfaces.mainpanelswitchsettings(80100);


		//mode selection panel
		RSInterface modepanel = RSInterface.interfaceCache[42880];
		RSInterface.handleConfigHover(modepanel);
		Interfaces.modepanelswitchsettings(42880);

		//quest tab panel
		RSInterface questpanel = RSInterface.interfaceCache[10407];
		RSInterface.handleConfigHover(questpanel);
		OSRSQuestTabWidget.switchSettings(10407);

		//quest tab panel
		RSInterface clanpanel = RSInterface.interfaceCache[80051];
		RSInterface.handleConfigHover(clanpanel);
		Interfaces.switchSettings_clanchat(80051);

	}



	public void resetgenielampselections(int whichselection) {
		//System.out.println("sele: "+whichselection);
	//	RSInterface genielamp = RSInterface.interfaceCache[2808];
		for (int i = 0; i < 19; i++) {
			RSInterface.interfaceCache[2812 + i].sprite1 = new Sprite("interfaces/genielamp/SPRITE 122");
			//RSInterface.interfaceCache[2812 + i].sprite2 = new Sprite("interfaces/genielamp/SPRITE 122");
		}
		RSInterface.interfaceCache[12034].sprite1 = new Sprite("interfaces/genielamp/SPRITE 122");
		RSInterface.interfaceCache[13914].sprite1 = new Sprite("interfaces/genielamp/SPRITE 122");

		RSInterface.interfaceCache[80001].sprite1 = new Sprite("interfaces/genielamp/SPRITE 122");
		RSInterface.interfaceCache[80003].sprite1 = new Sprite("interfaces/genielamp/SPRITE 122");
if(whichselection < 19)
		RSInterface.interfaceCache[2812 + whichselection].sprite1 = new Sprite("interfaces/genielamp/SPRITE 121");
else if(whichselection == 19)
	RSInterface.interfaceCache[12034].sprite1 = new Sprite("interfaces/genielamp/SPRITE 121");
else if(whichselection == 20)
	RSInterface.interfaceCache[13914].sprite1 = new Sprite("interfaces/genielamp/SPRITE 121");
else if(whichselection == 21)
	RSInterface.interfaceCache[80001].sprite1 = new Sprite("interfaces/genielamp/SPRITE 121");
else if(whichselection == 22)
	RSInterface.interfaceCache[80003].sprite1 = new Sprite("interfaces/genielamp/SPRITE 121");


		//	genielamp.children[i] = new Sprite("interfaces/mailbox/SPRITE 1");
		//	resetsidebars_mailboxslots
		//	RSInterface.interfaceCache[111_005 + i].sprite2 = new Sprite("interfaces/mailbox/SPRITE 1");

		//highlightselection(slots);
	}
	public void buildAtPlayerMenu(int i, int j, Player player, int k) {
		if (player == localPlayer)
			return;
		if (!player.visible)
			return;
		if (menuActionRow >= 400)
			return;
		String s;
		if (player.title.length() < 0)
			s = player.displayName + combatDiffColor(localPlayer.combatLevel, player.combatLevel) + " (level: "
					+ player.combatLevel + ")";
		else if (player.title.length() != 0)
			s = "@or1@" + player.title + "@whi@ " + player.displayName
					+ combatDiffColor(localPlayer.combatLevel, player.combatLevel) + " (level: " + player.combatLevel
					+ ")";
		else
			s = player.displayName + combatDiffColor(localPlayer.combatLevel, player.combatLevel) + " (level: "
					+ player.combatLevel + ")";
		if (itemSelected == 1) {
			menuActionName[menuActionRow] = "Use " + selectedItemName + " with @whi@" + s;
			menuActionID[menuActionRow] = 491;
			menuActionCmd1[menuActionRow] = j;
			menuActionCmd2[menuActionRow] = i;
			menuActionCmd3[menuActionRow] = k;
			menuActionRow++;
		} else if (spellSelected == 1) {
			if ((spellUsableOn & 8) == 8) {
				menuActionName[menuActionRow] = spellTooltip + " @whi@" + s;
				menuActionID[menuActionRow] = 365;
				menuActionCmd1[menuActionRow] = j;
				menuActionCmd2[menuActionRow] = i;
				menuActionCmd3[menuActionRow] = k;
				menuActionRow++;
			}
		} else {
			for (int l = 5; l >= 0; l--)
				if (atPlayerActions[l] != null) {
					menuActionName[menuActionRow] = atPlayerActions[l] + " @whi@" + s;
					char c = '\0';
					if (atPlayerActions[l].equalsIgnoreCase("attack")) {
						if (Configuration.playerAttackOptionPriority == 3)
							continue;
						if (Configuration.playerAttackOptionPriority == 0 && player.combatLevel > localPlayer.combatLevel
								|| Configuration.playerAttackOptionPriority == 1)
							c = '\u07D0';
						if (localPlayer.team != 0 && player.team != 0)
							if (localPlayer.team == player.team)
								c = '\u07D0';
							else
								c = '\0';
					} else if (atPlayerArray[l])
						c = '\u07D0';
					if (l == 0)
						menuActionID[menuActionRow] = 561 + c;
					if (l == 1)
						menuActionID[menuActionRow] = 779 + c;
					if (l == 2)
						menuActionID[menuActionRow] = 27 + c;
					if (l == 3)
						menuActionID[menuActionRow] = 577 + c;
					if (l == 4)
						menuActionID[menuActionRow] = 729 + c;
					if (l == 5) {
						if (localPlayer.hasRightsBetween(0, 4)) {
							menuActionID[menuActionRow] = 745 + c;
						} else {
							continue;
						}
					}
					menuActionCmd1[menuActionRow] = j;
					menuActionCmd2[menuActionRow] = i;
					menuActionCmd3[menuActionRow] = k;
					menuActionRow++;
				}

		}
		for (int i1 = 0; i1 < menuActionRow; i1++)
			if (menuActionID[i1] == 516) {
				menuActionName[i1] = "Walk here @whi@" + s;
				return;
			}

	}

	private void method89(SpawnedObject obj) {
		long id = 0L;
		int key = -1;
		int type = 0;
		int orientation = 0;
		if (obj.group == 0) {
			id = scene.getWallObjectUid(obj.plane, obj.x, obj.y);
		}
		if (obj.group == 1) {
			id = scene.getWallDecorationUid(obj.plane, obj.x, obj.y);
		}
		if (obj.group == 2) {
			id = scene.getGameObjectUid(obj.plane, obj.x, obj.y);
		}
		if (obj.group == 3) {
			id = scene.getGroundDecorationUid(obj.plane, obj.x, obj.y);
		}
		if (id != 0) {
			key = ObjectKeyUtil.getObjectId(id);
			type = ObjectKeyUtil.getObjectType(id);
			orientation = ObjectKeyUtil.getObjectOrientation(id);
		}
		obj.getPreviousId = key;
		obj.previousType = type;
		obj.previousOrientation = orientation;
	}

	public void objectFill(final int objectId, final int x1, final int y1, final int x2, final int y2, final int type,
						   final int face, final int height) {
		// if(height == height) // dunno your height variable but refactor
		// yourself
		for (int x = x1; x <= x2; x++) {
			for (int y = y1; y <= y2; y++) {
				addObject(objectId, x, y, face, type, height);
			}
		}
	}

	public void addObject(int objectId, int x, int y, int face, int type, int height) {
		int mX = currentRegionX - 6;
		int mY = currentRegionY - 6;
		int x2 = x - (mX * 8);
		int y2 = y - (mY * 8);
		int i15 = 40 >> 2;
		int l17 = objectTypes[i15];
		if (y2 > 0 && y2 < 103 && x2 > 0 && x2 < 103) {
			add_loc_change(-1, objectId, face, l17, y2, type, height, x2, 0);
		}
	}

	public void load_objects_real() {
		addObject(3610,3550,9694, 0, 10, 0);
                addObject(30282, 2496,5126,0, 10, 0);//inferno entrance
	}
	public void load_objects() {

		//slayer tower

		//addObject(-1,3434,3537, 0, 3, 0);
		addObject(2119, 3434,3537, 3, 10, 0);
		addObject(2120, 3434,3537, 3, 10, 1);

//		addObject(24306,2854,3546, 0, 0, 0);
//
//		addObject(24309,2855,3546, 2, 0, 0);
//
//		addObject(-1,2854,3545, 0, 3, 0);
//		addObject(-1, 2854,3545, 0,3, 0);

		addObject(-1,2907,9698, 0, 3, 0);
		addObject(-1, 2908,9698, 0,3, 0);

		addObject(20377, 2009, 3695, 0, 10, 0); // ancient altar
		addObject(-1, 2010, 3695, 0, 10, 0);
		addObject(-1, 2012, 3695, 0, 10, 0);
		/**
		 * @link addObject objectId, x, y, face, type, height
		 */

	//	addObject(0, 1544, 3687, 0, 10, 0);
		// Carts
		addObject(7029, 1656, 3542, 0, 10, 0); // Hos
		addObject(7029, 1591, 3621, 0, 10, 0); // Shay
		addObject(7029, 1760, 3710, 0, 10, 0); // Pisc
		addObject(7029, 1254, 3548, 0, 10, 0); // Qu
		addObject(7029, 1670, 3832, 0, 10, 0); // Arc
		addObject(7029, 1518, 3732, 0, 10, 0); // love
		// Abyss
	//	addObject(24101, 3039, 4834, 0, 10, 0);
		//addObject(13642, 3039, 4831, 2, 10, 0);

		// Home objects

//		addObject(2030, 1716, 3469, 1, 10, 0);
//		addObject(2030, 1672, 3745, 3, 10, 0);

		addObject(28900, 1547, 3921, 1, 10, 0);
		addObject(172, 1619, 3659, 3, 10, 0); // ckey
		addObject(6552, 1647, 3676, 3, 10, 0); // anchients
		addObject(10321, 1663, 3635, 3, 10, 0); // molehole
		addObject(3828, 1845, 3810, 3, 10, 0); // kqtunnel
		addObject(882, 1422, 3586, 3, 10, 0); // gw
		// addObject(4150, 3089, 3495, 3, 10, 0); //warriors guild
		addObject(1738, 1661, 3529, 1, 10, 0); // Tavelery
		// addObject(4151, 3089, 3494, 1, 10, 0); //barrows
		addObject(20877, 1572, 3657, 1, 10, 0); // brimhaven
		addObject(26709, 1280, 3551, 1, 10, 0); // stronghold slayer
		addObject(2123, 1257, 3501, 3, 10, 0); // rellekka slayer
		// addObject(11803, 1650, 3619, 0, 10, 0); //icedung
		addObject(-1, 3008, 9550, 0, 10, 0); // iceexit
		addObject(2268, 3009, 9553, 0, 10, 0); // icedungexit
		addObject(29734, 1349, 3591, 0, 10, 0); // demonicentrance
		addObject(2823, 1792, 3708, 0, 10, 0); // mith entrance
		addObject(4152, 1547, 3570, 1, 10, 0); // corpent
		addObject(4153, 1547, 3567, 1, 10, 0); // corpext
		addObject(4154, 1476, 3688, 1, 10, 0); // lizards
		addObject(4154, 1454, 3693, 1, 10, 0); // lizards
		// addObject(4155, 3089, 3496, 1, 10, 0); //zulrah
		addObject(8356, 1626, 3680, 3, 10, 0); // spirittreekourend
		addObject(8356, 1268, 3561, 0, 10, 0); // spirittreeMountQ
		addObject(8356, 1315, 3619, 0, 10, 0); // spirittreeXeric
		addObject(8356, 1477, 3555, 0, 10, 0); // spirittreeHeros
		addObject(11835, 1213, 3539, 1, 10, 0); // tzhaar
		addObject(678, 1605, 3707, 3, 10, 0); // CorpPortal
		addObject(2544, 1672, 3557, 1, 10, 0); // Dagentrence

		// wildysigns
		addObject(14503, 1465, 3704, 2, 10, 0); // wildysign
		addObject(14503, 1470, 3704, 2, 10, 0); // wildysign
		addObject(14503, 1469, 3704, 2, 10, 0); // wildysign
		addObject(14503, 1468, 3704, 2, 10, 0); // wildysign
		// 2=s,1=e
		// RFD
		// Resource area

		//addObject(-1, 3558, 9703, 0, 10, 0);


		addObject(9030, 3191, 3930, 1, 10, 0);
		addObject(9030, 3190, 3931, 1, 10, 0);


		// Barrows
		addObject(3610, 3550, 9695, 0, 10, 0); //maybe thats why they added the chest here..
//		addObject(46277, 3558, 9703, 1, 10, 3);//ahrim stairs
//		addObject(46277, 3557, 9718, 1, 10, 3);//dharok stairs
//		addObject(46277, 3578, 9703, 2, 10, 3);//verac stairs
//		addObject(46277, 3565, 9683, 3, 10, 3);//torag stairs
//		addObject(46277, 3546, 9685, 0, 10, 3);//karil stairs
//		addObject(46277, 3534, 9705, 0, 10, 3);//guthan stairs

		//addObject(23145, 2474, 3435, 0, 22, 0);//agility log


		// Moonclan
	//	objectFill(0, 2097, 3854, 2097, 3954, 10, 0, 0);

		// addObject(11731, 3003, 3384, 0, 10, 0); //Gem Fally

		// Crafting guild
		// objectFill(-1, 2939, 3282, 2943, 3290, 10, 0, 0); // Crafting guild
		addObject(-1, 2938, 3285, 0, 10, 0);
		addObject(-1, 2937, 3284, 0, 10, 0);
		addObject(-1, 2942, 3291, 0, 10, 0);


		/**
		 * Shilo
		 */
		// obelsisk reg
		addObject(2153, 1506, 3869, 0, 10, 0); // fire
		addObject(2152, 1600, 3632, 0, 10, 0); // air
		addObject(2151, 1793, 3704, 0, 10, 0); // water
		addObject(2150, 1240, 3538, 0, 10, 0); // earth

		// Bank room
		addObject(1113, 2850, 2952, 0, 10, 0); // Chair
		objectFill(24101, 2851, 2952, 2853, 2952, 10, 0, 0); // Bank

		// Vet
		//addObject(0, 3189, 3784, 0, 10, 0);
		//addObject(0, 3189, 3782, 0, 10, 0);
		//addObject(0, 3188, 3783, 0, 10, 0);
		//addObject(0, 3191, 3784, 0, 10, 0);
		//addObject(-1, 3189, 3783, 0, 10, 0);
		/**
		 * @link objectFill objectId, SWX, SWY, NEX, NEY, type, face, height
		 */
//		objectFill(-1, 3010, 3371, 3014, 3375, 10, 0, 0);
//		objectFill(-1, 1748, 5322, 1755, 5327, 22, 0, 1);
//		objectFill(14896, 3010, 3372, 3014, 3374, 10, 0, 0);
//		objectFill(14896, 3011, 3371, 3013, 3371, 10, 0, 0);
//		objectFill(14896, 3011, 3375, 3013, 3375, 10, 0, 0);

	}

	public static String fontFilter() {
		if (Configuration.newFonts) {
			return "_2";
		}
		return "";
	}
	public  int sumofTotalLevel(){
		int totalXP = maxStats[0] + maxStats[1] + maxStats[2] + maxStats[3] + maxStats[4] + maxStats[5]
				+ maxStats[6] + maxStats[7] + maxStats[8] + maxStats[9] + maxStats[10] + maxStats[11]
				+ maxStats[12] + maxStats[13] + maxStats[14] + maxStats[15] + maxStats[16] + maxStats[17]
				+ maxStats[18] + maxStats[19] + maxStats[20] + maxStats[21] + maxStats[22];
		return totalXP;
	}
	public FileArchive mediaStreamLoader;
public Sprite login1850 = new Sprite("Login/1850");
	@Override
	protected void startUp() {

		SettingsManager.loadSettings();
		ClientScripts.load();

		drawLoadingText(5, "Starting up...");

		getDocumentBaseHost();
		variousSettings[304] = 1;
		if (Signlink.cache_dat != null) {
			for (int i = 0; i < 5; i++)
				decompressors[i] = new Decompressor(Signlink.cache_dat, Signlink.cache_idx[i], i + 1);
		}

		new CacheDownloader(this).downloadCache();

		SpriteLoader1.loadSprites();
		cacheSprite1 = SpriteLoader1.sprites;
		SpriteLoader1.sprites = null;

		SpriteLoader2.loadSprites();
		cacheSprite2 = SpriteLoader2.sprites;
		SpriteLoader2.sprites = null;

		SpriteLoader3.loadSprites();
		cacheSprite3 = SpriteLoader3.sprites;
		SpriteLoader3.sprites = null;

		SpriteLoader4.loadSprites();
		cacheSprite4 = SpriteLoader4.sprites;
		SpriteLoader4.sprites = null;
		drawLoadingText(10, "Loading Packed Sprites...");

		try {
			mapfunctionCache.init(Paths.get(Signlink.getCacheDirectory(), Configuration.MAPFUNCTION_SPRITE_FILE_NAME + ".dat").toFile(), Paths.get(Signlink.getCacheDirectory(), Configuration.MAPFUNCTION_SPRITE_FILE_NAME + ".idx").toFile());

		} catch (IOException e) {
			e.printStackTrace();
		}


		try {
			ItemDef.load();
			titleStreamLoader = streamLoaderForName(1, "title screen");
			drawLoadingText(12, "Loading Fonts...");
			smallText = new TextDrawingArea(false, "p11_full" + fontFilter(), titleStreamLoader);
			XPFONT = new TextDrawingArea(true, "q8_full" + fontFilter(), titleStreamLoader);
			aTextDrawingArea_1271 = new TextDrawingArea(false, "p12_full" + fontFilter(), titleStreamLoader);
			chatTextDrawingArea = new TextDrawingArea(false, "b12_full", titleStreamLoader);
			drawLoadingText(15, "Loading Fonts...");
			aTextDrawingArea_1273 = new TextDrawingArea(true, "q8_full" + fontFilter(), titleStreamLoader);
			newSmallFont = new RSFont(false, "p11_full" + fontFilter(), titleStreamLoader);
			newRegularFont = new RSFont(false, "p12_full" + fontFilter(), titleStreamLoader);
			newBoldFont = new RSFont(false, "b12_full" + fontFilter(), titleStreamLoader);
			newFancyFont = new RSFont(true, "q8_full" + fontFilter(), titleStreamLoader);
			ItemBonusDefinition.loadItemBonusDefinitions();
			/**
			 * New fonts
			 */
			lato = new RSFont(true, "lato_full", titleStreamLoader);
			latoBold = new RSFont(true, "lato_bold_full", titleStreamLoader);
			kingthingsPetrock = new RSFont(true, "kingthings_petrock_full", titleStreamLoader);
			kingthingsPetrockLight = new RSFont(true, "kingthings_petrock_light_full", titleStreamLoader);
			drawLoadingText(20, "Loading Archives...");

			loadTitleScreen();
			createScreenImages();
			FileArchive streamLoader = streamLoaderForName(2, "config");
			FileArchive streamLoader_1 = streamLoaderForName(3, "interface");
			FileArchive streamLoader_2 = streamLoaderForName(4, "2d graphics");
			accountManager = new AccountManager(this, login1850);
			accountManager.loadAccounts();
			mediaStreamLoader = streamLoader_2;
			drawLoadingText(25, "Loading Archives...");
			FileArchive streamLoader_3 = streamLoaderForName(6, "textures");
			FileArchive streamLoader_4 = streamLoaderForName(7, "chat system");
			@SuppressWarnings("unused")
			FileArchive streamLoader_5 = streamLoaderForName(8, "sound effects");
			drawLoadingText(25, "Loading Scene...");
			tileFlags = new byte[4][104][104];
			tileHeights = new int[4][105][105];
			scene = new SceneGraph(tileHeights);
			for (int j = 0; j < 4; j++)
				collisionMaps[j] = new CollisionMap();

			FileArchive streamLoader_6 = streamLoaderForName(5, "update list");
			drawLoadingText(30, "Connecting to update server");
//
			resourceProvider = new OnDemandFetcher();
			resourceProvider.start(streamLoader_6, this);
	//repackCacheIndex(1);
	//repackCacheIndex(4);
//repackCacheIndex(2);
			if (Configuration.packIndexData) {
				repackCacheAll();
			}

//packCustomMaps();


			//if (Configuration.dumpMaps) {
				//resourceProvider.dumpMaps();
			//}

			AnimFrameSet.init();
			AnimKeyFrameSet.init();

			Model.init();

			drawLoadingText(35, "Unpacking media");
			mapIcon7 = new Sprite(streamLoader_2, "mapfunction", 1);
			mapIcon8 = new Sprite(streamLoader_2, "mapfunction", 51);
			mapIcon6 = new Sprite(streamLoader_2, "mapfunction", 74);
			mapIcon5 = new Sprite(streamLoader_2, "mapfunction", 5);
			mapIcon9 = new Sprite(streamLoader_2, "mapfunction", 56);
			multiOverlay = new Sprite(streamLoader_2, "overlay_multiway", 0);
			plusonerevs = new Sprite("2518");
			eventIcon = new Sprite(streamLoader_2, "mapfunction", 72);
			bankDivider = new Sprite("bank_divider");
			saveButton = new Sprite("Login/savebutton");
			xpbartick = new Sprite("expdrop/spritesforcounter/2031");
			xpbar = new Sprite("expdrop/spritesforcounter/2030");
			minimapImage = new Sprite(512,512);
			// Login
			drawLoadingText(40, "Unpacking media");
			loginAsset0 = new Sprite("Login/remember0");
			loginAsset1 = new Sprite("Login/remember1");
			loginAsset2 = new Sprite("Login/remember2");
			loginAsset3 = new Sprite("Login/remember3");
			loginAsset4 = new Sprite("Login/logo");
			loginScreenBackground = new Sprite("/loginscreen/background");
			logo2021 = new Sprite("/loginscreen/logo");
			loginScreenBackgroundCaptcha = new Sprite("/loginscreen/captcha_background");
			captchaExit = new Sprite("/loginscreen/captcha-exit");
			captchaExitHover = new Sprite("/loginscreen/captcha-exit-hover");
			drawLoadingText(45, "Unpacking media");
			File[] file = new File(Signlink.getCacheDirectory() + "/sprites/sprites/").listFiles();
			int size = file.length;
			cacheSprite = new Sprite[size];
			for (int i = 0; i < size; i++) {
				cacheSprite[i] = new Sprite("Sprites/" + i);
			}
			drawLoadingText(47, "Unpacking media");
			xpSprite = new Sprite("medal");
			for (int i = 0; i < inputSprites.length; i++)
				inputSprites[i] = new Sprite("Interfaces/Inputfield/SPRITE " + (i + 1));

			/*
			 * for (int index = 0; index < minimapSprites.length; index++) {
			 * minimapSprites[index] = new Sprite(streamLoader_2, "gameframe", index); Image
			 * image = minimapSprites[index].getImage(); if (image == null) {
			 * System.out.println("Image is null for: " + index); } }
			 */

			for (int i = 0; i < tabAreaResizable.length; i++)
				tabAreaResizable[i] = new Sprite("Gameframe/resizable/tabArea " + i);
			drawLoadingText(49, "Unpacking media");
			loadTabArea();

			donocurrencysprite = new Sprite("Interfaces/membershipbenefits/donocurrencysprite");
			infinity = new Sprite("infinity");
			zodianlogo = new Sprite("thelogo");
			zodianlogo_h = new Sprite("thelogoh");
			flashingarrow = new Sprite("flashingarrow");
			chatArea = new Sprite("Gameframe/chatarea");
			channelButtons = new Sprite("Gameframe/channelbuttons");
			venomOrb = new Sprite("orbs/venom");
			top508 = new Sprite("508scrollbar/top");
			bottom508 = new Sprite("508scrollbar/bottom");
			ignorelisticon =  new Sprite("Gameframe/sideicons/sideicon88");
			cursesidebaricon =  new Sprite("Curses/cursesideicon");
			firstquesttab =  new Sprite("Interfaces/osrsquesttab/firstquesttab");
			secondquesttab =  new Sprite("Interfaces/osrsquesttab/secondquesttab");
			thirdquesttab =  new Sprite("Interfaces/osrsquesttab/thirdquesttab");
			fourthquesttab =  new Sprite("Interfaces/osrsquesttab/fourthquesttab");
			drawLoadingText(50, "Unpacking media");
			for (int index = 0; index < smallXpSprites.length; index++) {
				smallXpSprites[index] = new Sprite("expdrop/" + index);

			}
			for (int index = 0; index < xpbarsprites.length; index++) {
				xpbarsprites[index] = new Sprite("expdrop/spritesforcounter/" + index);

			}
			for (int c1 = 0; c1 <= 3; c1++)
				chatButtons[c1] = new Sprite(streamLoader_2, "chatbuttons", c1);
			chatButtons[3] = new Sprite("1025_0");
			Sprite[] clanIcons = new Sprite[9];
			for (int index = 0; index < clanIcons.length; index++) {
				clanIcons[index] = new Sprite("Clan Chat/Icons/" + index);
			}
			drawLoadingText(51, "Unpacking media");
			//String iconPackDir = Signlink.getCacheDirectory() + "sprites" + Signlink.separator + "icon_pack";
			Sprite[] iconPack = new Sprite[301];
			for (int index = 0; index < iconPack.length; index++) {
				iconPack[index] = new Sprite("icon_pack/" + index);
			}
			for (int i = 0; i < modIcons.length; i++) {
				modIcons[i] = new Sprite("Player/modicons " + i);
			}
			RSFont.unpackImages(modIcons, clanIcons, iconPack);
			drawLoadingText(53, "Unpacking media");
			mapEdge = new Sprite(streamLoader_2, "mapedge", 0);
			mapEdge.method345();

			try {
				for (int k3 = 0; k3 < 100; k3++)
					mapScenes[k3] = new IndexedImage(streamLoader_2, "mapscene", k3);
			} catch (Exception _ex) {
			}
//			try {
//				for (int l3 = 0; l3 < 119; l3++)
//					mapFunctions[l3] = new Sprite("MapFunctions/" + l3);
//			} catch (Exception _ex) {
//			}
			try {
				for (int index = 0; index < mapfunctionCache.cache.length; index++) {
					//mapFunctions[index] = new SimpleImage(media_archive, "mapfunction", index);
					mapFunctions[index] =mapfunctionCache.get(index);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			System.out.println("Loaded " + mapFunctions.length + " map functions");

			try {
				for (int i4 = 0; i4 < 20; i4++) {
					hitMarks[i4] = new Sprite(streamLoader_2, "hitmarks", i4);
				}
			} catch (Exception _ex) {
			}

			hitMarks[19] = new Sprite("heal_hitsplat");
			drawLoadingText(55, "Unpacking media");
			try {
				for (int h1 = 0; h1 < 6; h1++)
					headIconsHint[h1] = new Sprite(streamLoader_2, "headicons_hint", h1);
			} catch (Exception _ex) {
			}
			try {
				for (int j4 = 0; j4 < 18; j4++)
					headIcons[j4] = new Sprite(streamLoader_2, "headicons_prayer", j4);
				for (int j45 = 0; j45 < 3; j45++)
					skullIcons[j45] = new Sprite(streamLoader_2, "headicons_pk", j45);
			} catch (Exception _ex) {
			}
			for (int i = 0; i < minimapIcons.length; i++) {
				minimapIcons[i] = new Sprite("Mapicons/ICON " + i);
			}
			//loginBackground2 = 	new AnimatedSprite(new URL("https://cdn.discordapp.com/attachments/454138282780131329/454147023046836235/Ascend-CB.gif"));
			mapFlag = new Sprite(streamLoader_2, "mapmarker", 0);
			mapMarker = new Sprite(streamLoader_2, "mapmarker", 1);
			for (int k4 = 0; k4 < 8; k4++)
				crosses[k4] = new Sprite(streamLoader_2, "cross", k4);
			drawLoadingText(56, "Unpacking media");
			mapDotItem = new Sprite(streamLoader_2, "mapdots", 0);
			mapDotNPC = new Sprite(streamLoader_2, "mapdots", 1);
			mapDotPlayer = new Sprite(streamLoader_2, "mapdots", 2);
			mapDotFriend = new Sprite(streamLoader_2, "mapdots", 3);
			mapDotTeam = new Sprite(streamLoader_2, "mapdots", 4);
			drawLoadingText(57, "Unpacking media");
			mapDotClan = new Sprite(streamLoader_2, "mapdots", 5);
			new Sprite(streamLoader_2, "mapdots", 4);
			scrollBar1 = new Sprite(streamLoader_2, "scrollbar", 0);
			scrollBar2 = new Sprite(streamLoader_2, "scrollbar", 1);


			for (int index = 0; index < GameTimerHandler.TIMER_IMAGES.length; index++) {
				GameTimerHandler.TIMER_IMAGES[index] = new Sprite("GameTimer/TIMER " + index);
			}
			drawLoadingText(60, "Unpacking media");
			int i5 = (int) (Math.random() * 21D) - 10;
			int j5 = (int) (Math.random() * 21D) - 10;
			int k5 = (int) (Math.random() * 21D) - 10;
			int l5 = (int) (Math.random() * 41D) - 20;

			for (int i6 = 0; i6 < 100; i6++) {
				if (mapScenes[i6] != null)
					mapScenes[i6].offsetColor(i5 + l5, j5 + l5, k5 + l5);
			}
	//resourceProvider.dumpModels();
			drawLoadingText(65, "Unpacking Textures");
			TextureProvider textureProvider = new TextureProvider(streamLoader_3,streamLoader,20,Rasterizer3D.lowMem ? 64 : 128);
			textureProvider.setBrightness(0.80000000000000004D);
			Rasterizer3D.setTextureLoader(textureProvider);
			drawLoadingText(66, "Unpacking Animations");
			AnimationDefinition.unpackConfig(streamLoader);
			drawLoadingText(67, "Unpacking Objects");
			ObjectDefinition.init(streamLoader);
			drawLoadingText(68, "Unpacking Floors");
			FloorDefinition.init(streamLoader);
			drawLoadingText(70, "Unpacking Items");
			ItemDefinition.init(streamLoader);
			drawLoadingText(73, "Unpacking Npc's");
			NpcDefinition.unpackConfig(streamLoader);
			drawLoadingText(74, "Unpacking Player Kits");
			IDK.unpackConfig(streamLoader);
			drawLoadingText(75, "Unpacking Graphics");
			GraphicsDefinition.unpackConfig(streamLoader);
			drawLoadingText(80, "Unpacking textures");
			AreaDefinition.unpackConfig(streamLoader);
			drawLoadingText(84, "Unpacking Varps");
			Varp.unpackConfig(streamLoader);
			VarBit.unpackConfig(streamLoader);
			drawLoadingText(88, "Unpacking textures");
			loadPlayerData();
		//	resourceProvider.dumpMaps();
			if (Configuration.dumpDataLists) {
				NpcDefinition.dumpList();
				ObjectDefinition.dumpList();
				resourceProvider.dumpModels();
			}
		//	resourceProvider.dumpanims();
		//ObjectDefinition.dumpList();
			drawLoadingText(90, "Unpacking interfaces");

			TextDrawingArea allFonts[] = { smallText, aTextDrawingArea_1271, chatTextDrawingArea,
					aTextDrawingArea_1273 };
			RSInterface.unpack(streamLoader_1, allFonts, streamLoader_2, new RSFont[] {newSmallFont, newRegularFont, newBoldFont, newFancyFont});
			drawLoadingText(92, "Preparing game engine");

			if(!isOldframeEnabled()) {
				mapBack = new Sprite("Gameframe/fixed/mapBack");
			}else {
				mapBack = new Sprite("Gameframe317/fixed/mapBack");
			}
			drawLoadingText(94, "Preparing game engine");
			for (int pixelY = 0; pixelY < 33; pixelY++) {
				int k6 = 999;
				int i7 = 0;
				for (int pixelX = 0; pixelX < 34; pixelX++) {
					if (mapBack.myPixels[pixelX + pixelY * mapBack.myWidth] == 0) {
						if (k6 == 999)
							k6 = pixelX;
						continue;
					}
					if (k6 == 999)
						continue;
					i7 = pixelX;
					break;
				}
				anIntArray968[pixelY] = k6;
				anIntArray1057[pixelY] = i7 - k6;
			}
			drawLoadingText(95, "Preparing game engine");
			for (int pixelY = 1; pixelY < 153; pixelY++) {
				int j7 = 999;
				int l7 = 0;
				for (int pixelX = 24; pixelX < 177; pixelX++) {
					if (mapBack.myPixels[pixelX + pixelY * mapBack.myWidth] == 0 && (pixelX > 34 || pixelY > 34)) {
						if (j7 == 999) {
							j7 = pixelX;
						}
						continue;
					}
					if (j7 == 999) {
						continue;
					}
					l7 = pixelX;
					break;
				}
				anIntArray1052[pixelY - 1] = j7 - 24;
				anIntArray1229[pixelY - 1] = l7 - j7;
			}
			drawLoadingText(97, "Preparing game engine");
			try {
				macAddress = GetNetworkAddress.GetAddress("mac");
				if (macAddress == null)
					macAddress = "";
				if (Configuration.developerMode) {
					System.out.println(macAddress);
				}
			} catch (Exception e) {
				e.printStackTrace();
				macAddress = "";
			}
			drawLoadingText(98, "Preparing game engine");
			//Censor.loadConfig(streamLoader_4);
			setConfigButtonsAtStartup();
			//mouseDetection = new MouseDetection(this);

			try {
				informationFile.read();
			} catch (Exception e) {
				e.printStackTrace();
			}

			if (informationFile.isUsernameRemembered()) {
				myUsername = informationFile.getStoredUsername();
			}
			if (informationFile.isPasswordRemembered()) {
				setPassword(informationFile.getStoredPassword());
			}
			if (informationFile.isRememberRoof()) {
				removeRoofs = true;
			}
			drawLoadingText(99, "Preparing game engine");
			//startRunnable(mouseDetection, 10);
			SceneObject.clientInstance = this;
			ObjectDefinition.clientInstance = this;
			NpcDefinition.clientInstance = this;
			AnimFrame.clientInstance = this;
		//	RSInterface.printEmptyInterfaceSections();
			if (Configuration.PRINT_EMPTY_INTERFACE_SECTIONS) {
				if (Configuration.developerMode) {
					RSInterface.printEmptyInterfaceSections();
				} else {
					System.err.println("PRINT_EMPTY_INTERFACE_SECTIONS is toggled but you must be in dev mode.");
				}
			}

			Preferences.load();
			drawLoadingText(100, "Preparing game engine");

			setGameState(GameState.LOGIN_SCREEN);
			return;
		} catch (Exception exception) {
			exception.printStackTrace();
			Signlink.reporterror("loaderror " + aString1049 + " " + anInt1079);
		}
		loadingError = true;
	}

	public void update_lowres_players(Buffer stream, int i) {
		while (stream.bitPosition + 10 < i * 8) {
			int slot = stream.readBits(11);
			if (slot == 2047)
				break;
			if (players[slot] == null) {
				players[slot] = new Player();
				if (localplayer_appearance_data[slot] != null)
					players[slot].update_player_appearance(localplayer_appearance_data[slot]);
			} else {
				System.err.println("high res player when expecting low res");
			}
			highres_player_list[playerCount++] = slot;
			Player player = players[slot];
			player.last_update_tick = update_tick;
			int k = stream.readBits(1);
			if (k == 1)
				entity_extended_info_list[anInt893++] = slot;
			int l = stream.readBits(1);
			int i1 = stream.readBits(5);
			if (i1 > 15)
				i1 -= 32;
			int j1 = stream.readBits(5);
			if (j1 > 15)
				j1 -= 32;
			player.teleport(localPlayer.waypoint_x[0] + j1, localPlayer.waypoint_z[0] + i1, l == 1);
		}
		stream.finishBitAccess();
	}

	public boolean inCircle(int circleX, int circleY, int clickX, int clickY, int radius) {
		return java.lang.Math.pow((circleX + radius - clickX), 2)
				+ java.lang.Math.pow((circleY + radius - clickY), 2) < java.lang.Math.pow(radius, 2);
	}

	public boolean mouseMapPosition() {
		if (MouseHandler.mouseX >= canvasWidth - 21 && MouseHandler.mouseX <= canvasWidth && MouseHandler.mouseY >= 0
				&& MouseHandler.mouseY <= 21) {
			return false;
		}
		return true;
	}

	private void processMainScreenClick() {
		if (minimapState != 0)
			return;
		if (clickMode3 == 1) {
			int i = MouseHandler.saveClickX - 25 - 547;
			int j = MouseHandler.saveClickY - 5 - 3;
			if (isResized()) {
				i = MouseHandler.saveClickX - (canvasWidth - 182 + 24);
				j = MouseHandler.saveClickY - 8;
			}
			if (inCircle(0, 0, i, j, 76) && mouseMapPosition() && !runHover) {
				i -= 73;
				j -= 75;
				int k = viewRotation + minimapRotation & 0x7ff;
				int i1 = Rasterizer3D.SINE[k];
				int j1 = Rasterizer3D.COSINE[k];
				i1 = i1 * (minimapZoom + 256) >> 8;
				j1 = j1 * (minimapZoom + 256) >> 8;
				int k1 = j * i1 + i * j1 >> 11;
				int l1 = j * j1 - i * i1 >> 11;
				int i2 = localPlayer.x + k1 >> 7;
				int j2 = localPlayer.z - l1 >> 7;
				if (localPlayer.isAdminRights() && controlIsDown) {
					teleport(baseX + i2, baseY + j2);
				} else {
					boolean flag1 = doWalkTo(1, localPlayer.waypoint_x[0], localPlayer.waypoint_z[0], 0, 0, 0, 0, 0, j2, true, i2);
					if (flag1) {
						stream.writeUnsignedByte(i);
						stream.writeUnsignedByte(j);
						stream.writeWord(viewRotation);
						stream.writeUnsignedByte(57);
						stream.writeUnsignedByte(minimapRotation);
						stream.writeUnsignedByte(minimapZoom);
						stream.writeUnsignedByte(89);
						stream.writeWord(localPlayer.x);
						stream.writeWord(localPlayer.z);
						stream.writeUnsignedByte(anInt1264);
						stream.writeUnsignedByte(63);
					}
				}
			}
			anInt1117++;
			if (anInt1117 > 1151) {
				anInt1117 = 0;
				stream.createFrame(246);
				stream.writeUnsignedByte(0);
				int l = stream.currentPosition;
				if ((int) (Math.random() * 2D) == 0)
					stream.writeUnsignedByte(101);
				stream.writeUnsignedByte(197);
				stream.writeWord((int) (Math.random() * 65536D));
				stream.writeUnsignedByte((int) (Math.random() * 256D));
				stream.writeUnsignedByte(67);
				stream.writeWord(14214);
				if ((int) (Math.random() * 2D) == 0)
					stream.writeWord(29487);
				stream.writeWord((int) (Math.random() * 65536D));
				if ((int) (Math.random() * 2D) == 0)
					stream.writeUnsignedByte(220);
				stream.writeUnsignedByte(180);
				stream.writeBytes(stream.currentPosition - l);
			}
		}
	}
	/**
	 * Get the String residing on the clipboard.
	 *
	 * @return any text found on the Clipboard; if none found, return an empty
	 *         String.
	 */
	public static String getClipboardContents() {
		if (System.currentTimeMillis() - timer < 2000) {
			return "";
		}
		String result = "";
		Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
		Transferable contents = clipboard.getContents(null);
		boolean hasTransferableText = (contents != null) && contents.isDataFlavorSupported(DataFlavor.stringFlavor);
		if (hasTransferableText) {
			try {
				result = (String) contents.getTransferData(DataFlavor.stringFlavor);
				timer = System.currentTimeMillis();
			} catch (UnsupportedFlavorException | IOException ex) {
				ex.printStackTrace();
			}
		}
		return result;
	}

	static long timer = 0;
	private void handleclosingWelcomeScreen(int x, int y) {
		String text = "::handlewelcome " + x + " " + y;
		stream.createFrame(103);
		stream.writeUnsignedByte(text.length() - 1);
		stream.writeString(text.substring(2));
	}
	private void teleport(int x, int y) {
		String text = "::tele " + x + " " + y;
		stream.createFrame(103);
		stream.writeUnsignedByte(text.length() - 1);
		stream.writeString(text.substring(2));
	}
	private void spinc(int id) {
		String text = "::spincomp-" + id;
		stream.createFrame(103);
		stream.writeUnsignedByte(text.length() - 1);
		stream.writeString(text.substring(2));
	}
	private void itemstats(int id) {
//		if(sendstatsrequest){
//			return;
//		}
		String text = "::getitemstats-" + id;
		stream.createFrame(103);
		stream.writeUnsignedByte(text.length() - 1);
		stream.writeString(text.substring(2));

	}
	//public boolean sendstatsrequest;
	private void viewnpcdrop(long id) {

		String text = "::npcdrop-" + id;
		stream.createFrame(103);
		stream.writeUnsignedByte(text.length() - 1);
		stream.writeString(text.substring(2));
	}
	private void sendvotecheck(long id) {

		String text = id == 0 ? "::checkvote-" + id : "::voted-" + id;
		stream.createFrame(103);
		stream.writeUnsignedByte(text.length() - 1);
		stream.writeString(text.substring(2));
	}
	private void senddonocheck(long id) {

		String text = "::checkdono-" + id;
		stream.createFrame(103);
		stream.writeUnsignedByte(text.length() - 1);
		stream.writeString(text.substring(2));
	}

	private void objectexamine(int id) {
	//	System.out.println("the id: "+id);
		String text = "::objexamine-" + id;
		stream.createFrame(103);
		stream.writeUnsignedByte(text.length() - 1);
		stream.writeString(text.substring(2));
	}
	private void restorehealth() {
		int id = 0;
		String text = "::restorehealth-"+id;
		stream.createFrame(103);
		stream.writeUnsignedByte(text.length() - 1);
		stream.writeString(text.substring(2));
	}
	public static void teleportInterface() {
		if (System.currentTimeMillis() - timer < 400) {
			return;
		}
		timer = System.currentTimeMillis();
		String text = "::teleport";
		stream.createFrame(103);
		stream.writeUnsignedByte(text.length() - 1);
		stream.writeString(text.substring(2));
	}

	public static void closeInterface() {
		String text = "::close";
		stream.createFrame(103);
		stream.writeUnsignedByte(text.length() - 1);
		stream.writeString(text.substring(2));
	}

	public static void continueDialogue() {
		if (System.currentTimeMillis() - timer < 400) {
			return;
		}
		timer = System.currentTimeMillis();
		String text = "::dialoguecontinuation continue";
		stream.createFrame(103);
		stream.writeUnsignedByte(text.length() - 1);
		stream.writeString(text.substring(2));
	}

	public static void dialogueOptions(String option) {
		if (System.currentTimeMillis() - timer < 400) {
			return;
		}
		timer = System.currentTimeMillis();
		String text = option == "one" ? "::dialoguecontinuation option_one"
				: option == "two" ? "::dialoguecontinuation option_two"
				: option == "three" ? "::dialoguecontinuation option_three"
				: option == "four" ? "::dialoguecontinuation option_four"
				: "::dialoguecontinuation option_five";
		stream.createFrame(103);
		stream.writeUnsignedByte(text.length() - 1);
		stream.writeString(text.substring(2));
	}

	private String interfaceIntToString(int j) {
		if (j < 0x3b9ac9ff)
			return String.valueOf(j);
		else
			return "*";
	}

	public void showErrorScreen() {
		Graphics g = getGameComponent().getGraphics();
		g.setColor(Color.black);
		g.fillRect(0, 0, 765, 503);
		//method4(1);
		if (loadingError) {
			aBoolean831 = false;
			g.setFont(new Font("Helvetica", 1, 16));
			g.setColor(Color.yellow);
			int k = 35;
			g.drawString("Sorry, an error has occured whilst loading " + Configuration.CLIENT_TITLE, 30, k);
			k += 50;
			g.setColor(Color.white);
			g.drawString("To fix this try the following (in order):", 30, k);
			k += 50;
			g.setColor(Color.white);
			g.setFont(new Font("Helvetica", 1, 12));
			g.drawString("1: Try restarting your client.", 30, k);
			k += 30;
			g.drawString("2: Try deleting the cache folder: " + Signlink.getCacheDirectory(), 30, k);
			k += 30;
			g.drawString("3: Try rebooting your computer", 30, k);
			k += 30;
			g.drawString("4: Try installing a different version of Java", 30, k);
			k += 30;
			g.drawString("5: Ask on discord", 30, k);
		}
		if (genericLoadingError) {
			aBoolean831 = false;
			g.setFont(new Font("Helvetica", 1, 20));
			g.setColor(Color.white);
			g.drawString("Error - unable to load game!", 50, 50);
			g.drawString("To play RuneScape make sure you play from", 50, 100);
			g.drawString("http://www.RuneScape.com", 50, 150);
		}
		if (rsAlreadyLoaded) {
			aBoolean831 = false;
			g.setColor(Color.yellow);
			int l = 35;
			g.drawString("Error a copy of RuneScape already appears to be loaded", 30, l);
			l += 50;
			g.setColor(Color.white);
			g.drawString("To fix this try the following (in order):", 30, l);
			l += 50;
			g.setColor(Color.white);
			g.setFont(new Font("Helvetica", 1, 12));
			g.drawString("1: Try closing ALL open web-browser windows, and reloading", 30, l);
			l += 30;
			g.drawString("2: Try rebooting your computer, and reloading", 30, l);
			l += 30;
		}
	}

	@Override
	public URL getCodeBase() {
		try {
			return new URL(server + ":" + (80 + portOff));
		} catch (Exception _ex) {
		}
		return null;
	}

	public void update_all_npc_movement() {
		for (int j = 0; j < npcCount; j++) {
			int k = highres_npc_list[j];
			Npc npc = npcs[k];
			if (npc != null)
				update_movement(npc);
		}
	}

	public void update_movement(Entity entity) {
		if (entity.x < 128 || entity.z < 128 || entity.x >= 13184 || entity.z >= 13184) {
			entity.primaryanim = -1;
			entity.spotanim = -1;
			entity.exactmove_start = 0;
			entity.exactmove_end = 0;
			entity.x = entity.waypoint_x[0] * 128 + entity.size * 64;
			entity.z = entity.waypoint_z[0] * 128 + entity.size * 64;
			entity.clear_waypoint();
		}
		if (entity == localPlayer && (entity.x < 1536 || entity.z < 1536 || entity.x >= 11776 || entity.z >= 11776)) {
			entity.primaryanim = -1;
			entity.spotanim = -1;
			entity.exactmove_start = 0;
			entity.exactmove_end = 0;
			entity.x = entity.waypoint_x[0] * 128 + entity.size * 64;
			entity.z = entity.waypoint_z[0] * 128 + entity.size * 64;
			entity.clear_waypoint();
		}
		if (entity.exactmove_start > update_tick)
			update_move_start(entity);
		else if (entity.exactmove_end >= update_tick)
			update_move_end(entity);
		else
			update_walkstep(entity);
		update_direction(entity);
		animate_pathingentity(entity);
	}

	public void update_move_start(Entity entity) {
		int i = entity.exactmove_start - update_tick;
		int j = entity.exactmove_x1 * 128 + entity.size * 64;
		int k = entity.exactmove_z1 * 128 + entity.size * 64;
		entity.x += (j - entity.x) / i;
		entity.z += (k - entity.z) / i;
		entity.walkanim_pause = 0;
		if (entity.forceMovementDirection == 0)
			entity.setTurnDirection(1024);
		if (entity.forceMovementDirection == 1)
			entity.setTurnDirection(1536);
		if (entity.forceMovementDirection == 2)
			entity.setTurnDirection(0);
		if (entity.forceMovementDirection == 3)
			entity.setTurnDirection(512);
	}

	public void update_move_end(Entity entity) {
		boolean var2 = entity.exactmove_end == update_tick || entity.primaryanim == -1 || entity.primaryanim_pause != 0;
		if (!var2) {
			AnimationDefinition var3 = AnimationDefinition.anims[entity.primaryanim];
			if (null != var3 && !var3.using_keyframes()) {
				var2 = entity.primaryanim_loops_remaining + 1 > var3.frame_durations[entity.primaryanim_frameindex];
			} else {
				var2 = true;
			}
		}

		if (var2) {
			int i = entity.exactmove_end - entity.exactmove_start;
			int j = update_tick - entity.exactmove_start;
			int k = entity.exactmove_x1 * 128 + entity.size * 64;
			int l = entity.exactmove_z1 * 128 + entity.size * 64;
			int i1 = entity.exactmove_x2 * 128 + entity.size * 64;
			int j1 = entity.exactmove_z2 * 128 + entity.size * 64;
			entity.x = (k * (i - j) + i1 * j) / i;
			entity.z = (l * (i - j) + j1 * j) / i;
		}
		entity.walkanim_pause = 0;
		if (entity.forceMovementDirection == 0)
			entity.setTurnDirection(1024);
		if (entity.forceMovementDirection == 1)
			entity.setTurnDirection(1536);
		if (entity.forceMovementDirection == 2)
			entity.setTurnDirection(0);
		if (entity.forceMovementDirection == 3)
			entity.setTurnDirection(512);
		entity.target_direction = entity.getTurnDirection();
	}

	public void update_walkstep(Entity entity) {
		entity.secondaryanim = entity.readyanim;
		if (entity.waypoint_count == 0) {
			entity.walkanim_pause = 0;
			return;
		}
		if (entity.primaryanim != -1 && entity.primaryanim_pause == 0) {
			AnimationDefinition animation = AnimationDefinition.anims[entity.primaryanim];
			if (entity.anim_delay > 0 && animation.exactmove == 0) {
				entity.walkanim_pause++;
				return;
			}
			if (entity.anim_delay <= 0 && animation.movetype == 0) {
				entity.walkanim_pause++;
				return;
			}
		}
		int i = entity.x;
		int j = entity.z;
		int k = entity.waypoint_x[entity.waypoint_count - 1] * 128 + entity.size * 64;
		int l = entity.waypoint_z[entity.waypoint_count - 1] * 128 + entity.size * 64;
		if (k - i > 256 || k - i < -256 || l - j > 256 || l - j < -256) {
			entity.x = k;
			entity.z = l;
			// Added
			entity.waypoint_count--;
			if (entity.anim_delay > 0)
				entity.anim_delay--;
			return;
		}
		if (i < k) {
			if (j < l)
				entity.setTurnDirection(1280);
			else if (j > l)
				entity.setTurnDirection(1792);
			else
				entity.setTurnDirection(1536);
		} else if (i > k) {
			if (j < l)
				entity.setTurnDirection(768);
			else if (j > l)
				entity.setTurnDirection(256);
			else
				entity.setTurnDirection(512);
		} else if (j < l)
			entity.setTurnDirection(1024);
		else
			entity.setTurnDirection(0);
		int i1 = entity.getTurnDirection() - entity.target_direction & 0x7ff;
		if (i1 > 1024)
			i1 -= 2048;
		int j1 = entity.walkanim_b;
		if (i1 >= -256 && i1 <= 256)
			j1 = entity.walkanim;
		else if (i1 >= 256 && i1 < 768)
			j1 = entity.walkanim_r;
		else if (i1 >= -768 && i1 <= -256)
			j1 = entity.walkanim_l;
		if (j1 == -1)
			j1 = entity.walkanim;
		entity.secondaryanim = j1;
		int translate_delta = 4;
		// Added smoothwalk
		boolean smoothwalk = true;
		if (entity instanceof Npc) {
			smoothwalk = ((Npc) entity).desc.smoothwalk;
		}
		if (smoothwalk) {
			if (entity.target_direction != entity.getTurnDirection() && -1 == entity.interactingEntity && entity.turnspeed != 0) {
				translate_delta = 2;
			}

			if (entity.waypoint_count > 2) {
				translate_delta = 6;
			}

			if (entity.waypoint_count > 3) {
				translate_delta = 8;
			}

		} else {
			if (entity.waypoint_count > 1) {
				translate_delta = 6;
			}

			if (entity.waypoint_count > 2) {
				translate_delta = 8;
			}

		}
		if (entity.walkanim_pause > 0 && entity.waypoint_count > 1) {
			translate_delta = 8;
			--entity.walkanim_pause;
		}

		MoveSpeed movespeed = entity.waypoint_movespeed[entity.waypoint_count - 1];
		if (movespeed == MoveSpeed.RUN) {
			translate_delta <<= 1;
		} else if (movespeed == MoveSpeed.CRAWL) {
			translate_delta >>= 1;
		}

		// Added new turns
		if (translate_delta >= 8 && entity.secondaryanim == entity.walkanim && entity.runanim != -1) {
			entity.secondaryanim = entity.runanim;
		}

		if (translate_delta >= 8) {
			if (entity.secondaryanim == entity.walkanim && entity.runanim != -1) {
				entity.secondaryanim = entity.runanim;
			} else if (entity.walkanim_b == entity.secondaryanim && -1 != entity.runanim_b) {
				entity.secondaryanim = entity.runanim_b;
			} else if (entity.walkanim_l == entity.secondaryanim && -1 != entity.runanim_l) {
				entity.secondaryanim = entity.runanim_l;
			} else if (entity.secondaryanim == entity.walkanim_r && -1 != entity.runanim_r) {
				entity.secondaryanim = entity.runanim_r;
			}
		} else if (translate_delta <= 1) {
			if (entity.secondaryanim == entity.walkanim && -1 != entity.crawlanim) {
				entity.secondaryanim = entity.crawlanim;
			} else if (entity.secondaryanim == entity.walkanim_b && entity.crawlanim_b != -1) {
				entity.secondaryanim = entity.crawlanim_b;
			} else if (entity.secondaryanim == entity.walkanim_l && entity.crawlanim_l != -1) {
				entity.secondaryanim = entity.crawlanim_l;
			} else if (entity.secondaryanim == entity.walkanim_r && -1 != entity.crawlanim_r) {
				entity.secondaryanim = entity.crawlanim_r;
			}
		}
		if (i < k) {
			entity.x += translate_delta;
			if (entity.x > k)
				entity.x = k;
		} else if (i > k) {
			entity.x -= translate_delta;
			if (entity.x < k)
				entity.x = k;
		}
		if (j < l) {
			entity.z += translate_delta;
			if (entity.z > l)
				entity.z = l;
		} else if (j > l) {
			entity.z -= translate_delta;
			if (entity.z < l)
				entity.z = l;
		}
		if (entity.x == k && entity.z == l) {
			entity.waypoint_count--;
			if (entity.anim_delay > 0)
				entity.anim_delay--;
		}
	}

	public void update_direction(Entity entity) {
		if (entity.turnspeed == 0)
			return;
		if (entity.interactingEntity != -1 && entity.interactingEntity < 32768) {
			Npc npc = npcs[entity.interactingEntity];
			if (npc != null) {
				int i1 = entity.x - npc.x;
				int k1 = entity.z - npc.z;
				if (i1 != 0 || k1 != 0)
					entity.setTurnDirection((int) (Math.atan2(i1, k1) * 325.94900000000001D) & 0x7ff);
			}
		}
		if (entity.interactingEntity >= 32768) {
			int j = entity.interactingEntity - 32768;
			if (j == unknownInt10)
				j = maxPlayerCount;
			Player player = players[j];
			if (player != null) {
				int l1 = entity.x - player.x;
				int i2 = entity.z - player.z;
				if (l1 != 0 || i2 != 0)
					entity.setTurnDirection((int) (Math.atan2(l1, i2) * 325.94900000000001D) & 0x7ff);
			}
		}
		if ((entity.face_x != 0 || entity.face_z != 0) && (entity.waypoint_count == 0 || entity.walkanim_pause > 0)) {
			int k = entity.x - (entity.face_x - baseX - baseX) * 64;
			int j1 = entity.z - (entity.face_z - baseY - baseY) * 64;
			if (k != 0 || j1 != 0) {
				entity.setTurnDirection((int) (Math.atan2(k, j1) * 325.94900000000001D) & 0x7ff);
			}
			entity.face_x = 0;
			entity.face_z = 0;
		}
		int turn_angle = entity.getTurnDirection() - entity.target_direction & 0x7ff;
		if (turn_angle == 0) {
			entity.direction_change_tick = 0;
		} else {
			entity.direction_change_tick++;
			boolean change_dir;
			if (turn_angle > 1024) {
				entity.target_direction -= entity.instant_facing ? turn_angle : entity.turnspeed;
				change_dir = true;
				if (turn_angle < entity.turnspeed || turn_angle > 2048 - entity.turnspeed) {
					entity.target_direction = entity.getTurnDirection();
					change_dir = false;
				}

				if (!entity.instant_facing && entity.secondaryanim == entity.readyanim && (entity.direction_change_tick > 25 || change_dir)) {
					if (entity.readyanim_l != -1) {
						entity.secondaryanim = entity.readyanim_l;
					} else {
						entity.secondaryanim = entity.walkanim;
					}
				}
			} else {
				entity.target_direction += entity.instant_facing ? turn_angle : entity.turnspeed;
				change_dir = true;
				if (turn_angle < entity.turnspeed || turn_angle > 2048 - entity.turnspeed) {
					entity.target_direction = entity.getTurnDirection();
					change_dir = false;
				}

				if (!entity.instant_facing && entity.secondaryanim == entity.readyanim && (entity.direction_change_tick > 25 || change_dir)) {
					if (entity.readyanim_r != -1) {
						entity.secondaryanim = entity.readyanim_r;
					} else {
						entity.secondaryanim = entity.walkanim;
					}
				}
			}

			entity.target_direction &= 0x7ff;
			entity.instant_facing = false;
		}
	}

	public void animate_pathingentity(Entity e) {
		e.is_walking = false;
		if (e.secondaryanim >= 65535)
			e.secondaryanim = -1;
		if (e.secondaryanim != -1) {
			AnimationDefinition seqtype = AnimationDefinition.anims[e.secondaryanim];
			if(seqtype == null) {
				e.secondaryanim = -1;
			} else if (!seqtype.using_keyframes() && seqtype.frames != null) {
				++e.secondaryanim_loops_remaining;
				if (e.secondaryanim_frameindex < seqtype.frames.length && e.secondaryanim_loops_remaining > seqtype.frame_durations[e.secondaryanim_frameindex]) {
					e.secondaryanim_loops_remaining = 1;
					++e.secondaryanim_frameindex;
					play_frames_sound(seqtype, e.secondaryanim_frameindex, e);
				}

				if (e.secondaryanim_frameindex >= seqtype.frames.length) {
					if (seqtype.loop_delay > 0) {
						e.secondaryanim_frameindex -= seqtype.loop_delay;
						if (seqtype.replay) {
							++e.secondaryanim_replaycount;
						}

						if (e.secondaryanim_frameindex < 0 || e.secondaryanim_frameindex >= seqtype.frames.length || seqtype.replay && e.secondaryanim_replaycount >= seqtype.replaycount) {
							e.secondaryanim_loops_remaining = 0;
							e.secondaryanim_frameindex = 0;
							e.secondaryanim_replaycount = 0;
						}
					} else {
						e.secondaryanim_loops_remaining = 0;
						e.secondaryanim_frameindex = 0;
					}

					play_frames_sound(seqtype, e.secondaryanim_frameindex, e);
				}

			} else if (seqtype.using_keyframes()) {
				++e.secondaryanim_frameindex;
				int var6 = seqtype.get_keyframe_duration();
				if (e.secondaryanim_frameindex < var6) {
					play_keyframes_sound(e.secondaryanim_frameindex, e, seqtype);
				} else {
					if (seqtype.loop_delay > 0) {
						e.secondaryanim_frameindex -= seqtype.loop_delay;
						if (seqtype.replay) {
							++e.secondaryanim_replaycount;
						}

						if (e.secondaryanim_frameindex < 0 || e.secondaryanim_frameindex >= var6 || seqtype.replay && e.secondaryanim_replaycount >= seqtype.replaycount) {
							e.secondaryanim_frameindex = 0;
							e.secondaryanim_loops_remaining = 0;
							e.secondaryanim_replaycount = 0;
						}
					} else {
						e.secondaryanim_loops_remaining = 0;
						e.secondaryanim_frameindex = 0;
					}
					play_keyframes_sound(e.secondaryanim_frameindex, e, seqtype);
				}
			} else {
				e.secondaryanim = -1;
			}
		}
		if (e.spotanim != -1 && update_tick >= e.spotanim_start_loop) {
			if (e.spotanimframe_index < 0)
				e.spotanimframe_index = 0;
			if (GraphicsDefinition.cache[e.spotanim] == null || GraphicsDefinition.cache[e.spotanim].seqtype == null) {
				e.spotanim = -1;//-1 in osrs
			} else {
				AnimationDefinition spotanim_seq = GraphicsDefinition.cache[e.spotanim].seqtype;
				if (spotanim_seq != null && null != spotanim_seq.frames && !spotanim_seq.using_keyframes()) {
					++e.spotanim_loop;
					if (e.spotanimframe_index < spotanim_seq.frames.length && e.spotanim_loop > spotanim_seq.frame_durations[e.spotanimframe_index]) {
						e.spotanim_loop = 1;
						++e.spotanimframe_index;
						play_frames_sound(spotanim_seq, e.spotanimframe_index, e);
					}

					if (e.spotanimframe_index >= spotanim_seq.frames.length && (e.spotanimframe_index < 0 || e.spotanimframe_index >= spotanim_seq.frames.length)) {
						e.spotanim = -1;
					}
				} else if (spotanim_seq.using_keyframes()) {
					++e.spotanimframe_index;
					int var4 = spotanim_seq.get_keyframe_duration();
					if (e.spotanimframe_index < var4) {
						play_keyframes_sound(e.spotanimframe_index, e, spotanim_seq);
					} else if (e.spotanimframe_index < 0 || e.spotanimframe_index >= var4) {
						e.spotanim = -1;
					}
				} else {
					e.spotanim = -1;
				}
			}

		}
		if (e.primaryanim != -1 && e.primaryanim_pause <= 1) {
			if (e.primaryanim >= AnimationDefinition.anims.length) {
				e.primaryanim = -1;//0 in 317
				return;
			}
			AnimationDefinition animation_2 = AnimationDefinition.anims[e.primaryanim];
			if (animation_2.exactmove == 1 && e.anim_delay > 0 && e.exactmove_start <= update_tick
					&& e.exactmove_end < update_tick) {
				e.primaryanim_pause = 1;
				return;
			}
		}
		if (e.primaryanim != -1 && e.primaryanim_pause == 0) {
			AnimationDefinition seqtype = AnimationDefinition.anims[e.primaryanim];
			if (seqtype == null) {
				e.primaryanim = -1;
			} else if (seqtype.using_keyframes() || seqtype.frames == null) {
				if (seqtype.using_keyframes()) {
					++e.primaryanim_frameindex;
					int skeletal_duration = seqtype.get_keyframe_duration();
					if (e.primaryanim_frameindex < skeletal_duration) {
						play_keyframes_sound(e.primaryanim_frameindex, e, seqtype);
					} else {
						e.primaryanim_frameindex -= seqtype.loop_delay;
						++e.primaryanim_replaycount;
						if (e.primaryanim_replaycount >= seqtype.replaycount) {
							e.primaryanim = -1;
						} else if (e.primaryanim_frameindex >= 0 && e.primaryanim_frameindex < skeletal_duration) {
							play_keyframes_sound(e.primaryanim_frameindex, e, seqtype);
						} else {
							e.primaryanim = -1;
						}
					}
				} else {
					e.primaryanim = -1;
				}
			} else {
				++e.primaryanim_loops_remaining;
				if (e.primaryanim_frameindex < seqtype.frames.length && e.primaryanim_loops_remaining > seqtype.frame_durations[e.primaryanim_frameindex]) {
					e.primaryanim_loops_remaining = 1;
					++e.primaryanim_frameindex;
					play_frames_sound(seqtype, e.primaryanim_frameindex, e);
				}

				if (e.primaryanim_frameindex >= seqtype.frames.length) {
					e.primaryanim_frameindex -= seqtype.loop_delay;
					++e.primaryanim_replaycount;
					if (e.primaryanim_replaycount >= seqtype.replaycount) {
						e.primaryanim = -1;
					} else if (e.primaryanim_frameindex >= 0 && e.primaryanim_frameindex < seqtype.frames.length) {
						play_frames_sound(seqtype, e.primaryanim_frameindex, e);
					} else {
						e.primaryanim = -1;
					}
				}

				e.is_walking = seqtype.stretches;
			}
		}
		if (e.primaryanim_pause > 0)
			e.primaryanim_pause--;
	}

	private static void play_frames_sound(AnimationDefinition seqtype, int frameindex, Entity entity) {
		if (seqtype.get_frame_soundeffect(frameindex) != -1) {
			entity.makeSound(seqtype.get_frame_soundeffect(frameindex));
		}
	}

	private static void play_keyframes_sound(int frameindex, Entity entity, AnimationDefinition seqtype) {
		if (seqtype.keyframe_soundeffects != null && seqtype.keyframe_soundeffects.containsKey(frameindex)) {
			entity.makeSound((Integer) seqtype.keyframe_soundeffects.get(frameindex));
		}
	}


	private void drawGameScreen() {
		if (fullscreenInterfaceID != -1 && loadingStage == 2) {
			if (loadingStage == 2) {
				try {
					processWidgetAnimations(tickDelta, fullscreenInterfaceID);
					if (openInterfaceID != -1) {
						processWidgetAnimations(tickDelta, openInterfaceID);
					}
				} catch(Exception ex) {

				}
				tickDelta = 0;
				Rasterizer3D.scanOffsets = fullScreenTextureArray;
				Rasterizer2D.clear();
				welcomeScreenRaised = true;
				if (openInterfaceID != -1) {
					RSInterface rsInterface_1 = RSInterface.interfaceCache[openInterfaceID];
					if (rsInterface_1.width == 512 && rsInterface_1.height == 334
							&& rsInterface_1.type == 0) {
						rsInterface_1.width = 765;
						rsInterface_1.height = 503;
					}
					try {
						drawInterface(0, 8, rsInterface_1, 8);
					} catch(Exception ex) {

					}
				}
				RSInterface rsInterface = RSInterface.interfaceCache[fullscreenInterfaceID];
				if (rsInterface.width == 512 && rsInterface.height == 334
						&& rsInterface.type == 0) {
					rsInterface.width = 765;
					rsInterface.height = 503;
				}
				try {
					drawInterface(0, 8, rsInterface, 8);
				} catch (Exception ex) {

				}

				if (!menuOpen) {

					processRightClick();
					drawTopLeftTooltip();
				} else {
					drawMenu( 0, 0);
				}
			}
			drawCount++;
			return;
		} else {
			if (drawCount != 0) {
				Rasterizer2D.clear();//              resetImageProducers2();
			}
		}

		if (welcomeScreenRaised) {
			welcomeScreenRaised = false;
			inputTaken = true;
			tabAreaAltered = true;//         update_tab_producer = true;
		}
		if (backDialogID != -1) {
			try {
				processWidgetAnimations(tickDelta, backDialogID);
			} catch (Exception ex) {

			}
		}
	//	drawTabArea();
		if (backDialogID == -1) {
			aClass9_1059.scrollPosition = chatAreaScrollLength - anInt1089 - 110;
			if (MouseHandler.mouseX >= 496 && MouseHandler.mouseX <= 511
					&& MouseHandler.mouseY > (!isResized() ? 345
					: canvasHeight - 158))
				method65(494, 110, MouseHandler.mouseX,
						MouseHandler.mouseY - (!isResized() ? 345
								: canvasHeight - 158),
						aClass9_1059, 0, false, chatAreaScrollLength);
			int i = chatAreaScrollLength - 110 - aClass9_1059.scrollPosition;
			if (i < 0) {
				i = 0;
			}
			if (i > chatAreaScrollLength - 110) {
				i = chatAreaScrollLength - 110;
			}
			if (anInt1089 != i) {
				anInt1089 = i;
				inputTaken = true;
			}
		}
		if (backDialogID != -1) {
			boolean flag2 = false;

			try {
				flag2 = processWidgetAnimations(tickDelta, backDialogID);
			} catch(Exception ex) {
				ex.printStackTrace();
			}

			if (flag2) {
				inputTaken = true;
			}
		}
		if (atInventoryInterfaceType == 3)
			inputTaken = true;//            update_chat_producer = true;
		if (activeInterfaceType == 3)
			inputTaken = true;
		if (clickToContinueString != null)
			inputTaken = true;
		if (menuOpen && menuScreenArea == 2)
			inputTaken = true;
		if (inputTaken) {
			inputTaken = false;
		}
		if (loadingStage == 2)
			moveCameraWithPlayer();


		tickDelta = 0;
	}

	private boolean buildFriendsListMenu(RSInterface class9) {
		int i = class9.contentType;
		if (i >= 1 && i <= 200 || i >= 701 && i <= 900) {
			if (i >= 801)
				i -= 701;
			else if (i >= 701)
				i -= 601;
			else if (i >= 101)
				i -= 101;
			else
				i--;
			if (tabInterfaceIDs[tabID] != 42500) {
				menuActionName[menuActionRow] = "Remove @whi@" + friendsList[i];
				menuActionID[menuActionRow] = 792;
				menuActionRow++;
				menuActionName[menuActionRow] = "Message @whi@" + friendsList[i];
				menuActionID[menuActionRow] = 639;
				menuActionRow++;
			}

			return true;
		}
		if (i >= 401 && i <= 500) {
			menuActionName[menuActionRow] = "Remove @whi@" + class9.message;
			menuActionID[menuActionRow] = 322;
			menuActionRow++;
			return true;
		} else {
			return false;
		}
	}

	private int hoverId;

	public void method104() {
		StillGraphic class30_sub2_sub4_sub3 = (StillGraphic) incompleteAnimables.reverseGetFirst();
		for (; class30_sub2_sub4_sub3 != null; class30_sub2_sub4_sub3 = (StillGraphic) incompleteAnimables.reverseGetNext())
			if (class30_sub2_sub4_sub3.anInt1560 != plane || class30_sub2_sub4_sub3.finished_animating)
				class30_sub2_sub4_sub3.unlink();
			else if (update_tick >= class30_sub2_sub4_sub3.anInt1564) {
				class30_sub2_sub4_sub3.advance_anim(tickDelta);
				if (class30_sub2_sub4_sub3.finished_animating)
					class30_sub2_sub4_sub3.unlink();
				else
					scene.addAnimableA(class30_sub2_sub4_sub3.anInt1560, 0, class30_sub2_sub4_sub3.anInt1563, -1,
							class30_sub2_sub4_sub3.anInt1562, 60, class30_sub2_sub4_sub3.anInt1561,
							class30_sub2_sub4_sub3, false);
			}

	}

	public int dropdownInversionFlag;

	public Sprite grandExchangeSprite0 = new Sprite("Grand Exchange/0"),
			grandExchangeSprite1 = new Sprite("Grand Exchange/1"),
			grandExchangeSprite2 = new Sprite("Grand Exchange/2"),
			grandExchangeSprite3 = new Sprite("Grand Exchange/3"),
			grandExchangeSprite4 = new Sprite("Grand Exchange/4");

	private int interfaceDrawY;
	public boolean interfaceText = false;
	public boolean interfaceStringText = false;

	public static final int[] runeChildren = { 1202, 1203, 1209, 1210, 1211, 1218, 1219, 1220, 1227, 1228, 1234, 1235, 1236, 1243,
			1244, 1245, 1252, 1253, 1254, 1261, 1262, 1263, 1270, 1271, 1277, 1278, 1279, 1286, 1287, 1293,
			1294, 1295, 1302, 1303, 1304, 1311, 1312, 1318, 1319, 1320, 1327, 1328, 1329, 1336, 1337, 1343,
			1344, 1345, 1352, 1353, 1354, 1361, 1362, 1363, 1370, 1371, 1377, 1378, 1384, 1385, 1391, 1392,
			1393, 1400, 1401, 1407, 1408, 1410, 1417, 1418, 1424, 1425, 1426, 1433, 1434, 1440, 1441, 1442,
			1449, 1450, 1456, 1457, 1463, 1464, 1465, 1472, 1473, 1474, 1481, 1482, 1488, 1489, 1490, 1497,
			1498, 1499, 1506, 1507, 1508, 1515, 1516, 1517, 1524, 1525, 1526, 1533, 1534, 1535, 1547, 1548,
			1549, 1556, 1557, 1558, 1566, 1567, 1568, 1576, 1577, 1578, 1586, 1587, 1588, 1596, 1597, 1598,
			1605, 1606, 1607, 1616, 1617, 1618, 1627, 1628, 1629, 1638, 1639, 1640, 6007, 6008, 6011, 8673,
			8674, 12041, 12042, 12429, 12430, 12431, 12439, 12440, 12441, 12449, 12450, 12451, 12459, 12460,
			15881, 15882, 15885, 18474, 18475, 18478 };

	public static final int[] SOME_IDS = { 1196, 1199, 1206, 1215, 1224, 1231, 1240, 1249, 1258, 1267, 1274, 1283, 1573, 1290, 1299,
			1308, 1315, 1324, 1333, 1340, 1349, 1358, 1367, 1374, 1381, 1388, 1397, 1404, 1583, 12038, 1414,
			1421, 1430, 1437, 1446, 1453, 1460, 1469, 15878, 1602, 1613, 1624, 7456, 1478, 1485, 1494, 1503,
			1512, 1521, 1530, 1544, 1553, 1563, 1593, 1635, 12426, 12436, 12446, 12456, 6004, 18471,
			/* Ancients */
			12940, 12988, 13036, 12902, 12862, 13046, 12964, 13012, 13054, 12920, 12882, 13062, 12952, 13000,
			13070, 12912, 12872, 13080, 12976, 13024, 13088, 12930, 12892, 13096 };


	private static final int[] BOUNTY_INTERFACE_IDS = {
			21001, 28030, 28006, 28021, 28003, 28004, 28005, 28029,
			28023, 28024, 28025, 28026, 28027, 28028, 28001, 28022
	};

	public void drawInterface(int scrollPosition, int xPosition, RSInterface rsInterface, int yPosition) {
		drawInterface(scrollPosition, xPosition, rsInterface, yPosition, false);
	}

	public void drawInterface(int scrollPosition, int xPosition, RSInterface rsInterface, int yPosition, boolean inheritDrawingArea) {
		try {
			if (rsInterface.type != 0 || rsInterface.children == null)
				return;
			if (rsInterface.isMouseoverTriggered && anInt1026 != rsInterface.id && anInt1048 != rsInterface.id
				&& anInt1039 != rsInterface.id)
				return;

			int clipLeft = Rasterizer2D.leftX;
			int clipTop = Rasterizer2D.topY;
			int clipRight = Rasterizer2D.bottomX;
			int clipBottom = Rasterizer2D.bottomY;

			// Sets the drawing area so things don't draw outside the boundary (scrollbars are a good example)
			// If you have a type 0 (container) inside another type 0 the latest one of the tree will set the drawing area
			// and possibly breaking scrolling (making things draw above or below the scrolling container)
			//if (rsInterface.id <= 24507)
			if (!inheritDrawingArea) {
				Rasterizer2D.setDrawingArea(yPosition + rsInterface.height, xPosition, xPosition + rsInterface.width, yPosition);
			}
			int childCount = rsInterface.children.length;
			for (int childId = 0; childId < childCount; childId++) {
				try {
					int _x = rsInterface.childX[childId] + xPosition;
					int _y = (rsInterface.childY[childId] + yPosition) - scrollPosition;
					RSInterface class9_1 = RSInterface.interfaceCache[rsInterface.children[childId]];

					_x += class9_1.anInt263;
					_y += class9_1.anInt265;

					if (class9_1.interfaceHidden)
						continue;

					if (!getUserSettings().isBountyHunter() && Arrays.stream(BOUNTY_INTERFACE_IDS).anyMatch(id -> id == class9_1.id)) {
						continue;
					}

					if (class9_1.contentType > 0)
						drawFriendsListOrWelcomeScreen(class9_1);

					if (class9_1.id != 28060 && class9_1.id != 28061) {
						for (int m5 = 0; m5 < SOME_IDS.length; m5++) {
							if (class9_1.id == SOME_IDS[m5] + 1) {
								if (m5 > 61) {
									drawBlackBox(_x + 1, _y);
								} else {
									drawBlackBox(_x, _y + 1);
								}
							}
						}
					}

					for (int r = 0; r < runeChildren.length; r++)
						if (class9_1.id == runeChildren[r])
							class9_1.modelZoom = 775;

					if (class9_1.type == 0) {
						if (class9_1.scrollPosition > class9_1.scrollMax - class9_1.height)
							class9_1.scrollPosition = class9_1.scrollMax - class9_1.height;
						if (class9_1.scrollPosition < 0)
							class9_1.scrollPosition = 0;
						drawInterface(class9_1.scrollPosition, _x, class9_1, _y, inheritDrawingArea || rsInterface.scrollMax > 0);

						// Hardcodes
						if (class9_1.scrollMax > class9_1.height) {
							// clan chat
							if (class9_1.id == 18143) {
								int clanMates = 0;
								for (int i = 18155; i < 18244; i++) {
									RSInterface line = RSInterface.interfaceCache[i];
									if (line.message.length() > 0) {
										clanMates++;
									}
								}
								class9_1.scrollMax = (clanMates * 14) + class9_1.height + 1;
							}
							if (class9_1.id == 18322 || class9_1.id == 18423) {
								int members = 0;
								for (int i = class9_1.id + 1; i < class9_1.id + 1 + 100; i++) {
									RSInterface line = RSInterface.interfaceCache[i];
									if (line != null && line.message != null) {
										if (line.message.length() > 0) {
											members++;
										}
									}
								}
								class9_1.scrollMax = (members * 14) + 1;
							}
							if (rsInterface.parentID == 49000 || rsInterface.parentID == 49100 || rsInterface.parentID == 51100
								|| rsInterface.parentID == 53100) {
								int scrollMax = class9_1.scrollMax;

								if (achievementCutoff > 1) {
									scrollMax = achievementCutoff * 65;
								} else {
									achievementCutoff = 282;
								}
								class9_1.scrollMax = scrollMax;
							}
							if (scrollbarVisible(class9_1)) {
								if (class9_1.newScroller) {
									draw508Scrollbar(class9_1.height, class9_1.scrollPosition, _y, _x + class9_1.width,
											class9_1.scrollMax, false);
								} else {
									drawScrollbar(class9_1.height, class9_1.scrollPosition, _y, _x + class9_1.width,
											class9_1.scrollMax);
								}
							}
						}

					} else if (class9_1.type != 1)
						if (class9_1.type == 2) {
							if (interfaceText) {
								newSmallFont.drawBasicString("Container: " + class9_1.id, _x, _y);
							}
							try {
								Bank.setupMainTab(class9_1, _x, _y);
							} catch (Exception e) {
								e.printStackTrace();
							}

							// Item container
							if (class9_1.invAutoScrollHeight) {
								int lastRow = -1;
								int rowCount = 0;
								int i3 = 0;

								for (int l3 = 0; l3 < class9_1.height; l3++) {
									for (int l4 = 0; l4 < class9_1.width; l4++) {
										if (class9_1.inventoryItemId[i3] > 1) {
											if (lastRow != l3) {
												lastRow = l3;
												rowCount++;
											}
										}

										i3++;
									}
								}

								RSInterface scrollable = RSInterface.interfaceCache[class9_1.invAutoScrollInterfaceId];
								scrollable.scrollMax = scrollable.height + 1;
								int heightPerRow = class9_1.invSpritePadY + 32;
								if (heightPerRow * rowCount > scrollable.scrollMax) {
									scrollable.scrollMax += (heightPerRow * rowCount) - scrollable.scrollMax + scrollable.invAutoScrollHeightOffset;
								}
							}

							int i3 = 0;
							for (int l3 = 0; l3 < class9_1.height; l3++) {
								for (int l4 = 0; l4 < class9_1.width; l4++) {
									// System.out.println("id? " + class9_1.inv[i3]);
									int k5 = _x + l4 * (32 + class9_1.invSpritePadX);
									int j6 = _y + l3 * (32 + class9_1.invSpritePadY);
									if (i3 < 20) {
										k5 += class9_1.spritesX[i3];
										j6 += class9_1.spritesY[i3];
									}
									if (class9_1.inventoryItemId[i3] > 0) {
										int k6 = 0;
										int j7 = 0;
										int j9 = class9_1.inventoryItemId[i3] - 1;//the item id
										if (class9_1.id == 23231) {
											j9 = (class9_1.inventoryItemId[i3] & 0x7FFF) - 1;
										}
										if (k5 > Rasterizer2D.leftX - 32 && k5 < Rasterizer2D.bottomX && j6 > Rasterizer2D.topY - 32
											&& j6 < Rasterizer2D.bottomY || activeInterfaceType != 0 && itemDraggingSlot == i3) {
											int l9 = 0;
											if (itemSelected == 1 && anInt1283 == i3 && anInt1284 == class9_1.id)
												l9 = 0xffffff;

											int itemSpriteOpacity = 256;
											/**
											 * Placeholder opacity editing
											 */
											if (class9_1.inventoryAmounts[i3] <= 0)
												itemSpriteOpacity = 100;

											Sprite itemSprite = null;

											if (openInterfaceID == 29875) {
												if (class9_1.inventoryItemId[i3] < 555
													|| class9_1.inventoryItemId[i3] > 567 && class9_1.inventoryItemId[i3] != 9076) {
													itemSpriteOpacity = 100;
												}
											}

											if (class9_1.id == 23121) {
												final boolean isLastBitEnabled = ((class9_1.inventoryItemId[i3] >> 15) & 0x1) == 1;
												if (isLastBitEnabled) {
													itemSpriteOpacity = 90;
												}
											}

											boolean smallSprite = openInterfaceID == 26000
												&& GrandExchange.isSmallItemSprite(class9_1.id) || class9_1.smallInvSprites;

											ItemDefinition itemDef = ItemDefinition.lookup(j9);
											if (smallSprite) {
												itemSprite = ItemDefinition.getSmallSprite(j9, class9_1.inventoryAmounts[i3]);

												int finalJ = j9;
												if (IntStream.of(ItemDefinition.customspriteitems).anyMatch(x -> x == finalJ)) {

													if (itemDef.customSpriteLocation != null) {
														itemSprite = new Sprite(itemDef.customSpriteLocation);
													}
												}

											} else {
												itemSprite = ItemDefinition.getSprite(j9, class9_1.inventoryAmounts[i3], l9);

													int finalJ = j9;
													if (IntStream.of(ItemDefinition.customspriteitems).anyMatch(x -> x == finalJ)) {//ah it keeps getting 31017.
													if (itemDef.customSpriteLocation != null) {
														if(j9 == 31017){
															if(class9_1.inventoryAmounts[i3] < 2){
																itemSprite = new Sprite(itemDef.getCustomSprite("ak47bullet_1.png"));
															} else if(class9_1.inventoryAmounts[i3] >= 2 && class9_1.inventoryAmounts[i3] < 3){
																itemSprite = new Sprite(itemDef.getCustomSprite("ak47bullet_2.png"));
                                                            } else if(class9_1.inventoryAmounts[i3] >= 3 && class9_1.inventoryAmounts[i3] < 4){
                                                                itemSprite = new Sprite(itemDef.getCustomSprite("ak47bullet_3.png"));
                                                            } else if(class9_1.inventoryAmounts[i3] >= 4 && class9_1.inventoryAmounts[i3] < 5){
                                                                itemSprite = new Sprite(itemDef.getCustomSprite("ak47bullet_4.png"));
															} else {
                                                                itemSprite = new Sprite(itemDef.getCustomSprite("ak47bullet_5.png"));
                                                            }
														} else {

															itemSprite = new Sprite(itemDef.customSpriteLocation);
														}
												}
												}
											}

											if (class9_1.id >= 32212 && class9_1.id <= 32212 + 11) {
												if (class9_1.inventoryItemId[i3] > 0) {
													if (class9_1.sprite2 != null) {
														class9_1.sprite2.drawSprite(k5 + k6 - 2, j6 + j7 - 2);
													}
												}
											}

											if (itemSprite != null) {
												if (activeInterfaceType != 0 && itemDraggingSlot == i3 && draggingItemInterfaceId == class9_1.id) {
													k6 = MouseHandler.mouseX - anInt1087;
													j7 = MouseHandler.mouseY - anInt1088;
													if (k6 < 5 && k6 > -5)
														k6 = 0;
													if (j7 < 5 && j7 > -5)
														j7 = 0;
													if (anInt989 < getDragSetting(class9_1.id)) {
														k6 = 0;
														j7 = 0;
													}
													itemSprite.drawTransparentSprite(k5 + k6, j6 + j7, 128);
													if (j6 + j7 < Rasterizer2D.topY && rsInterface.scrollPosition > 0) {
														int i10 = (tickDelta * (Rasterizer2D.topY - j6 - j7)) / 3;
														if (i10 > tickDelta * 10)
															i10 = tickDelta * 10;
														if (i10 > rsInterface.scrollPosition)
															i10 = rsInterface.scrollPosition;
														rsInterface.scrollPosition -= i10;
														anInt1088 += i10;
													}
													if (j6 + j7 + 32 > Rasterizer2D.bottomY
														&& rsInterface.scrollPosition < rsInterface.scrollMax
														- rsInterface.height) {
														int j10 = (tickDelta * ((j6 + j7 + 32) - Rasterizer2D.bottomY)) / 3;
														if (j10 > tickDelta * 10)
															j10 = tickDelta * 10;
														if (j10 > rsInterface.scrollMax - rsInterface.height
															- rsInterface.scrollPosition)
															j10 = rsInterface.scrollMax - rsInterface.height
																- rsInterface.scrollPosition;
														rsInterface.scrollPosition += j10;
														anInt1088 -= j10;
													}
												} else if (atInventoryInterfaceType != 0 && atInventoryIndex == i3
													&& atInventoryInterface == class9_1.id)
													itemSprite.drawTransparentSprite(k5, j6, 128);
												else
												// itemSprite.drawSprite(k5, j6);
												/**
												 * Draws item sprite
												 */
													itemSprite.drawTransparentSprite(k5, j6, itemSpriteOpacity);
												if (class9_1.id == RSInterface.selectedItemInterfaceId
													&& class9_1.itemSearchSelectedSlot > -1
													&& class9_1.itemSearchSelectedSlot == i3) {
													for (int i = 32; i > 0; i--) {
														Rasterizer2D.method338(j6 + j7, i, 256 - Byte.MAX_VALUE, 0x395D84, i,
															k5 + k6);
													}
													Rasterizer2D.method338(j6 + j7, 32, 256, 0x395D84, 32, k5 + k6);
												}

												if (class9_1.forceInvStackSizes || !smallSprite && !class9_1.hideInvStackSizes) {
													if (class9_1.parentID < 58040 || class9_1.parentID > 58048) {
														if (itemSprite.maxWidth == 33 || class9_1.inventoryAmounts[i3] != 1) {
															if (class9_1.id != 23121 || class9_1.id == 23121 && ((class9_1.inventoryItemId[i3] >> 15) & 0x1) == 0) {
																	if (class9_1.invAlwaysInfinity || class9_1.id == Interfaces.SHOP_CONTAINER) {//this is inside shop container


																		if(class9_1.inventoryAmounts[i3] >= 2_000_000_000) {//greater than 2b = inf
																			//infinity.drawSprite(k5 + k6, j6 + j7);
                                                                        //} else if(class9_1.inventoryAmounts[i3] > 1_900_000_000 && class9_1.inventoryAmounts[i3] < 2_000_000_000) {//this amount means itll not show the amount when the amount is big anyway.
                                                                          //  infinity.drawSprite(k5 + k6-3000, j6 + j7);
																		} else if(class9_1.inventoryAmounts[i3] > 1_800_000_000 && class9_1.inventoryAmounts[i3] < 1_900_000_000) {//this amount means its dono currency.
																			infinity.drawSprite(k5 + k6-3000, j6 + j7);
                                                                            donocurrencysprite.drawSprite(k5 + k6-5, j6 + j7+31);
																		} else {
																			int k10 = class9_1.inventoryAmounts[i3];
																			int xOffset = class9_1.forceInvStackSizes ? 10 : 0;
																			if (k10 >= 1)
																				smallText.method385(0xFFFF00, intToKOrMil(k10), j6 + 9 + j7,
																						xOffset+ k5 + k6);
																			smallText.method385(0, intToKOrMil(k10), j6 + 10 + j7, xOffset+ k5 + 1 + k6);
																			if (k10 > 99999 && k10 < 10000000) {
																				smallText.method385(0xFFFFFF, intToKOrMil(k10), j6 + 9 + j7,
																						xOffset+ k5 + k6);
																			} else if (k10 > 9999999) {
																				smallText.method385(0x00ff80, intToKOrMil(k10), j6 + 9 + j7,
																						xOffset+ k5 + k6);
																			} else {
																				smallText.method385(0xFFFF00, intToKOrMil(k10), j6 + 9 + j7,
																						xOffset+ k5 + k6);
																			}
																		}

																} else {//and this is what draws the little amount in the top left corner :)
																	int k10 = class9_1.inventoryAmounts[i3];
																	int xOffset = class9_1.forceInvStackSizes ? 10 : 0;
																	if (k10 >= 1)
																		smallText.method385(0xFFFF00, intToKOrMil(k10), j6 + 9 + j7,
																			xOffset+ k5 + k6);
																	smallText.method385(0, intToKOrMil(k10), j6 + 10 + j7, xOffset+ k5 + 1 + k6);
																	if (k10 > 99999 && k10 < 10000000) {
																		smallText.method385(0xFFFFFF, intToKOrMil(k10), j6 + 9 + j7,
																			xOffset+ k5 + k6);
																	} else if (k10 > 9999999) {
																		smallText.method385(0x00ff80, intToKOrMil(k10), j6 + 9 + j7,
																			xOffset+ k5 + k6);
																	} else {
																		smallText.method385(0xFFFF00, intToKOrMil(k10), j6 + 9 + j7,
																			xOffset+ k5 + k6);
																	}
																}
															}
														}
													}
												}
											}
										}
									} else if (class9_1.sprites != null && i3 < 20) {
										Sprite class30_sub2_sub1_sub1_1 = class9_1.sprites[i3];
										if (class30_sub2_sub1_sub1_1 != null)
											class30_sub2_sub1_sub1_1.drawSprite(k5, j6);
									}
									i3++;
								}
							}
						} else if (class9_1.type == 3) {
							boolean flag = false;
							if (anInt1039 == class9_1.id || anInt1048 == class9_1.id || anInt1026 == class9_1.id)
								flag = true;
							int colour;
							if (cs1_selected(class9_1)) {
								colour = class9_1.secondaryColor;
								if (flag && class9_1.anInt239 != 0)
									colour = class9_1.anInt239;
							} else {
								colour = class9_1.textColor;
								if (flag && class9_1.anInt216 != 0)
									colour = class9_1.anInt216;
							}
							if (class9_1.aByte254 == 0) {//ok 19244 is the outline
								if (class9_1.aBoolean227)
									Rasterizer2D.drawPixels(class9_1.height, _y, _x, colour, class9_1.width);
								else
									Rasterizer2D.fillPixels(_x, class9_1.width, class9_1.height, colour, _y);
							} else if (class9_1.aBoolean227)
//								Rasterizer2D.method335(colour, _y, class9_1.width, class9_1.height,
//									256 - (class9_1.aByte254 & 0xff), _x);
//							Rasterizer2D.fillPixels(_x, class9_1.width, class9_1.height, colour, _y);
								Rasterizer2D.drawTransparentBox(_x, _y, class9_1.width, class9_1.height, colour,
										256 - (class9_1.opacity & 0xff));
							else
								Rasterizer2D.fillPixels(_x, class9_1.width, class9_1.height, colour, _y);
//								Rasterizer2D.method338(_y, class9_1.height, 256 - (class9_1.aByte254 & 0xff), colour,
//									class9_1.width, _x);
						} else if (class9_1.type == 4 || class9_1.type == RSInterface.TYPE_TEXT_DRAW_FROM_LEFT) {
							TextDrawingArea textDrawingArea = class9_1.textDrawingAreas;
							String s = class9_1.message;
							if (interfaceStringText) {
								s = class9_1.id + "";
							}
							boolean flag1 = false;
							if (anInt1039 == class9_1.id || anInt1048 == class9_1.id || anInt1026 == class9_1.id)
								flag1 = true;
							int i4 = 0;
							if (cs1_selected(class9_1)) {
								i4 = class9_1.secondaryColor;
								if (flag1 && class9_1.anInt239 != 0)
									i4 = class9_1.anInt239;
								if (class9_1.aString228.length() > 0)
									s = class9_1.aString228;
							} else {
								i4 = class9_1.textColor;
								if (flag1 && class9_1.anInt216 != 0) {
									i4 = class9_1.anInt216;
									if (i4 == 49152)
										i4 = 16777215;
								}
							}
							if (class9_1.atActionType == 6 && aBoolean1149) {
								s = "Please wait...";
								i4 = 255;
							}
							if ((class9_1.parentID == 1151) || (class9_1.parentID == 12855)) {
								switch (i4) {
									case 16773120:
										i4 = 0xFE981F;
										break;
									case 7040819:
										i4 = 0xAF6A1A;
										break;
								}
							}
							if (class9_1.hoverText != null && !class9_1.hoverText.isEmpty()) {
								if (MouseHandler.mouseX > _x
									&& MouseHandler.mouseX < _x + class9_1.textDrawingAreas.getTextWidth(class9_1.message)
									&& MouseHandler.mouseY > _y && MouseHandler.mouseY < _y + 15) {
									s = class9_1.hoverText;
									i4 = class9_1.hoverTextColor;
								}
							}
							if((backDialogID != -1 || dialogID != -1 || class9_1.message.contains("Click here to continue")) &&
								(rsInterface.id == backDialogID || rsInterface.id == dialogID )){
								if(i4 == 0xffff00)
									i4 = 255;
								if(i4 == 49152)
									i4 = 0xffffff;
							}
							for (int l6 = _y + textDrawingArea.anInt1497; s.length() > 0; l6 += textDrawingArea.anInt1497) {
								if (s.indexOf("%") != -1) {
									do {
										int k7 = s.indexOf("%1");
										if (k7 == -1)
											break;
										if (class9_1.id < 4000 || class9_1.id > 5000 && class9_1.id != 13921
											&& class9_1.id != 13922 && class9_1.id != 12171 && class9_1.id != 12172)
											s = s.substring(0, k7) + methodR(extractInterfaceValues(class9_1, 0))
												+ s.substring(k7 + 2);
										else
											s = s.substring(0, k7) + interfaceIntToString(extractInterfaceValues(class9_1, 0))
												+ s.substring(k7 + 2);
									} while (true);
									do {
										int l7 = s.indexOf("%2");
										if (l7 == -1)
											break;
										s = s.substring(0, l7) + interfaceIntToString(extractInterfaceValues(class9_1, 1))
											+ s.substring(l7 + 2);
									} while (true);
									do {
										int i8 = s.indexOf("%3");
										if (i8 == -1)
											break;
										s = s.substring(0, i8) + interfaceIntToString(extractInterfaceValues(class9_1, 2))
											+ s.substring(i8 + 2);
									} while (true);
									do {
										int j8 = s.indexOf("%4");
										if (j8 == -1)
											break;
										s = s.substring(0, j8) + interfaceIntToString(extractInterfaceValues(class9_1, 3))
											+ s.substring(j8 + 2);
									} while (true);
									do {
										int k8 = s.indexOf("%5");
										if (k8 == -1)
											break;
										s = s.substring(0, k8) + interfaceIntToString(extractInterfaceValues(class9_1, 4))
											+ s.substring(k8 + 2);
									} while (true);
								}
								int l8 = s.indexOf("\\n");
								String s1;
								if (l8 != -1) {
									s1 = s.substring(0, l8);
									s = s.substring(l8 + 2);
								} else {
									s1 = s;
									s = "";
								}
								RSFont font = null;
								if (textDrawingArea == smallText) {
									font = newSmallFont;
								} else if (textDrawingArea == aTextDrawingArea_1271) {
									font = newRegularFont;
								} else if (textDrawingArea == chatTextDrawingArea) {
									font = newBoldFont;
								} else if (textDrawingArea == aTextDrawingArea_1273) {
									font = newFancyFont;
								}
								if (rsInterface.parentID == 49100 || rsInterface.parentID == 51100 || rsInterface.parentID == 53100) {
									int parent = rsInterface.parentID == 49100 ? 49100 : rsInterface.parentID == 51100 ? 51100 : 53100;
									int subId = (class9_1.id - parent) % 100;
									if (subId > achievementCutoff) {
										continue;
									}
								}
								if (interfaceStringText) {
									s1 = "" + class9_1.id;
								}

								if (class9_1.type == RSInterface.TYPE_TEXT_DRAW_FROM_LEFT) {
									int width = font.getTextWidth(s1);
									font.drawBasicString(s1, _x - width, l6, i4, class9_1.textShadow ? 0 : -1);
								} else if (class9_1.centerText) {
									font.drawCenteredString(s1, _x + class9_1.width / 2, class9_1.centerVertically ? l6 + (class9_1.height / 2) - (font.baseCharacterHeight / 2) : l6, i4, class9_1.textShadow ? 0 : -1);
								} else {
									font.drawBasicString(s1, _x, l6, i4, class9_1.textShadow ? 0 : -1);
								}
							}
						} else if (class9_1.type == 5) {
							Sprite sprite;
							if(cs1_selected(class9_1) || class9_1.active) {
								sprite = class9_1.sprite2;
							} else {
								sprite = class9_1.sprite1;
							}

							if(class9_1.isHoverSprite) {
								if(class9_1.atActionType == 22) {
									if (menuActionCmd3[1] == class9_1.id && MouseHandler.mouseX > _x && MouseHandler.mouseX < _x + class9_1.sprite1.myWidth
										&& MouseHandler.mouseY > _y && MouseHandler.mouseY < _y + class9_1.sprite1.myHeight) {
										sprite = class9_1.sprite2;
									} else {
										sprite = class9_1.sprite1;
									}
								} else {
									if (MouseHandler.mouseX > _x && MouseHandler.mouseX < _x + class9_1.sprite1.myWidth
										&& MouseHandler.mouseY > _y && MouseHandler.mouseY < _y + class9_1.sprite1.myHeight) {
										sprite = class9_1.sprite2;
									} else {
										sprite = class9_1.sprite1;
									}
								}
							}

							if(spellSelected == 1 && class9_1.id == spellID && spellID != 0 && sprite != null) {
								sprite.drawSprite(_x, _y, 0xffffff);
							}

							if(sprite != null) {
								if (class9_1.drawsTransparent) {
									sprite.drawTransparentSprite(_x, _y, class9_1.transparency);
								} else {
									sprite.drawSprite(_x, _y);
								}
							}
						} else if (class9_1.type == 6) {
							Rasterizer3D.renderOnGpu = true;
							int k3 = Rasterizer3D.originViewX;
							int j4 = Rasterizer3D.originViewY;
							Rasterizer3D.originViewX = _x + class9_1.width / 2;
							Rasterizer3D.originViewY = _y + class9_1.height / 2;
							int i5 = Rasterizer3D.SINE[class9_1.modelRotation1] * class9_1.modelZoom >> 16;
							int l5 = Rasterizer3D.COSINE[class9_1.modelRotation1] * class9_1.modelZoom >> 16;
							boolean active = cs1_selected(class9_1);
							int seq_id;
							if (active)
								seq_id = class9_1.active_seq_id;
							else
								seq_id = class9_1.seq_id;

						//	System.out.println("seq: "+seq_id+" active? :"+active);
							Model model;
							if (seq_id == -1) {
								model = class9_1.get_if_model(null, -1, active);
							} else {
								AnimationDefinition animation = AnimationDefinition.anims[seq_id];
								if (class9_1.iftype_frameindex >= animation.frame_durations.length || class9_1.iftype_frameindex >= animation.frames.length) {
									class9_1.iftype_frameindex = 0; // Fixes array index out of bounds on npc dialogues
								}
								model = class9_1.get_if_model(animation, class9_1.iftype_frameindex, active);
							}
							if (model != null) {
								Rasterizer3D.world = false;
								model.renderModel(class9_1.modelRotation2, 0, class9_1.modelRotation1, 0, i5, l5);
								Rasterizer3D.world = true;
							}

							Rasterizer3D.originViewX = k3;
							Rasterizer3D.originViewY = j4;
							Rasterizer3D.renderOnGpu = false;

						} else if (class9_1.type == 7) {
							TextDrawingArea textDrawingArea_1 = class9_1.textDrawingAreas;
							int k4 = 0;
							for (int j5 = 0; j5 < class9_1.height; j5++) {
								for (int i6 = 0; i6 < class9_1.width; i6++) {
									if (class9_1.inventoryItemId[k4] > 0) {
										ItemDefinition itemDef = ItemDefinition.lookup(class9_1.inventoryItemId[k4] - 1);
										String s2 = itemDef.name;
										if (itemDef.stackable || class9_1.inventoryAmounts[k4] != 1)
											s2 = s2 + " x" + intToKOrMilLongName(class9_1.inventoryAmounts[k4]);
										int i9 = _x + i6 * (115 + class9_1.invSpritePadX);
										int k9 = _y + j5 * (12 + class9_1.invSpritePadY);
										if (class9_1.centerText)
											textDrawingArea_1.drawCentered(class9_1.textColor, i9 + class9_1.width / 2, s2, k9,
												class9_1.textShadow);
										else
											textDrawingArea_1.method389(class9_1.textShadow, i9, class9_1.textColor, s2, k9);
									}
									k4++;
								}
							}
						} else if (class9_1.type == 8
							&& (anInt1500 == class9_1.id || anInt1044 == class9_1.id || anInt1129 == class9_1.id)
							&& anInt1501 == 50 && !menuOpen) {


							int boxWidth = 0;
							int boxHeight = 0;
							if (class9_1.hoverXOffset != 0) {
								_x += class9_1.hoverXOffset;
							}

							if (class9_1.hoverYOffset != 0) {
								_y += class9_1.hoverYOffset;
							}
if(class9_1.id == 14934){//right side skill icons lets move them left by some pixel amount
	_x -=100;
}
							/**
							 * Skill tab hovers Remove "next level at" and "remaining" for xp if we're level
							 * 99.
							 */

							TextDrawingArea textDrawingArea_2 = aTextDrawingArea_1271;
							for (String s1 = class9_1.message; s1.length() > 0;) {
								if (s1.indexOf("%") != -1) {
									do {
										int k7 = s1.indexOf("%1");
										if (k7 == -1)
											break;
										s1 = s1.substring(0, k7) + interfaceIntToString(extractInterfaceValues(class9_1, 0))
											+ s1.substring(k7 + 2);
									} while (true);
									do {
										int l7 = s1.indexOf("%2");
										if (l7 == -1)
											break;
										s1 = s1.substring(0, l7) + interfaceIntToString(extractInterfaceValues(class9_1, 1))
											+ s1.substring(l7 + 2);
									} while (true);
									do {
										int i8 = s1.indexOf("%3");
										if (i8 == -1)
											break;
										s1 = s1.substring(0, i8) + interfaceIntToString(extractInterfaceValues(class9_1, 2))
											+ s1.substring(i8 + 2);
									} while (true);
									do {
										int j8 = s1.indexOf("%4");
										if (j8 == -1)
											break;
										s1 = s1.substring(0, j8) + interfaceIntToString(extractInterfaceValues(class9_1, 3))
											+ s1.substring(j8 + 2);
									} while (true);
									do {
										int k8 = s1.indexOf("%5");
										if (k8 == -1)
											break;
										s1 = s1.substring(0, k8) + interfaceIntToString(extractInterfaceValues(class9_1, 4))
											+ s1.substring(k8 + 2);
									} while (true);
								}
								int l7 = s1.indexOf("\\n");

								String s4;
								if (l7 != -1) {
									s4 = s1.substring(0, l7);
									s1 = s1.substring(l7 + 2);
								} else {
									s4 = s1;
									s1 = "";
								}
								int j10 = textDrawingArea_2.getTextWidth(s4);
								if (j10 > boxWidth) {
									boxWidth = j10;
								}
								boxHeight += textDrawingArea_2.anInt1497 + 1;
							}
//System.out.println("boxwidth: "+boxWidth);
							if (tabInterfaceIDs[tabID] == 17200) {
								return;
							}
							boxWidth += 6;
							boxHeight += 7;
							int xPos = (_x + class9_1.width) - 5 - boxWidth;
				//		System.out.println("id: "+class9_1.id+" hover? "+class9_1.inventoryhover+"  _x: "+_x+" and xPos: "+xPos);
							int yPos = _y + class9_1.height + 5;
							if (xPos < _x + 5)
								xPos = _x + 5;
							//System.out.println("boxWidth: "+boxWidth+" and rsinterface width: "+rsInterface.width);
							if (xPos + boxWidth > xPosition + rsInterface.width) {
								xPos = (xPosition + rsInterface.width) - boxWidth;
							//	System.out.println("yes id: "+class9_1.id+" ");
							}
							if (yPos + boxHeight > yPosition + rsInterface.height)
								yPos = (_y - boxHeight);
//							switch (class9_1.id) {
//								case 9217:
//								case 9220:
//								case 9223:
//								case 9226:
//								case 9229:
//								case 9232:
//								case 9235:
//								case 9238:
//									xPos -= 80;
//									break;
//								case 9239:
//									yPos -= 100;
//									break;
//							}
							if (!isResized()) {
								if (class9_1.inventoryhover) {

									if (xPos + boxWidth > 249) {
										xPos = 265 - boxWidth - xPosition;
									//	xPos = 50;
									}
									if (yPos + boxHeight + interfaceDrawY > 291 && yPos + boxHeight + interfaceDrawY < 321) {
										yPos = 255 - boxHeight - yPosition;
									} else if (yPos + boxHeight + interfaceDrawY > 322) {
									//	yPos = 275 - boxHeight - yPosition;
										//yPos = 275 - boxHeight - yPosition;
										yPos = (boxHeight == 46 ? 297 : 298) - boxHeight - yPosition;
									}
								}
							} else {
								if (class9_1.inventoryhover) {

									//System.out.println(canvasWidth - 648);
									if (xPos + boxWidth > canvasWidth - 8 - boxWidth + 100) {
										xPos = canvasWidth - 8 - boxWidth;
									}
									if (yPos + boxHeight > canvasHeight - 118 - boxHeight + 100
										&& yPos + boxHeight < canvasHeight - 118 - boxHeight + 120) {
										yPos = canvasHeight - 148 - boxHeight;
									} else if (yPos + boxHeight > canvasHeight - 118 - boxHeight + 100) {
										yPos = canvasHeight - 118 - boxHeight;
									}
								}
							}
							Rasterizer2D.drawPixels(boxHeight, yPos, xPos, 0xFFFFA0, boxWidth);
							Rasterizer2D.fillPixels(xPos, boxWidth, boxHeight, 0, yPos);
						//	System.out.println("x: "+xPos+"  y: "+yPos);
							String s2 = class9_1.message;
							for (int j11 = yPos + textDrawingArea_2.anInt1497 + 2; s2
								.length() > 0; j11 += textDrawingArea_2.anInt1497 + 1) {// anInt1497
								if (s2.indexOf("%") != -1) {
									do {
										int k7 = s2.indexOf("%1");
										if (k7 == -1)
											break;
										s2 = s2.substring(0, k7) + interfaceIntToString(extractInterfaceValues(class9_1, 0))
											+ s2.substring(k7 + 2);
									} while (true);
									do {
										int l7 = s2.indexOf("%2");
										if (l7 == -1)
											break;
										s2 = s2.substring(0, l7) + interfaceIntToString(extractInterfaceValues(class9_1, 1))
											+ s2.substring(l7 + 2);
									} while (true);
									do {
										int i8 = s2.indexOf("%3");
										if (i8 == -1)
											break;
										s2 = s2.substring(0, i8) + interfaceIntToString(extractInterfaceValues(class9_1, 2))
											+ s2.substring(i8 + 2);
									} while (true);
									do {
										int j8 = s2.indexOf("%4");
										if (j8 == -1)
											break;
										s2 = s2.substring(0, j8) + interfaceIntToString(extractInterfaceValues(class9_1, 3))
											+ s2.substring(j8 + 2);
									} while (true);
									do {
										int k8 = s2.indexOf("%5");
										if (k8 == -1)
											break;
										s2 = s2.substring(0, k8) + interfaceIntToString(extractInterfaceValues(class9_1, 4))
											+ s2.substring(k8 + 2);
									} while (true);
								}
								int l11 = s2.indexOf("\\n");
								String s5;
								if (l11 != -1) {
									s5 = s2.substring(0, l11);
									s2 = s2.substring(l11 + 2);
								} else {
									s5 = s2;
									s2 = "";
								}
								if (class9_1.centerText) {
									textDrawingArea_2.drawCentered(yPos, xPos + class9_1.width / 2, s5, j11, false);
								} else {
									if (s5.contains("\\r")) {
										String text = s5.substring(0, s5.indexOf("\\r"));
										String text2 = s5.substring(s5.indexOf("\\r") + 2);
										textDrawingArea_2.method389(false, xPos + 3, 0, text, j11);
										int rightX = boxWidth + xPos - textDrawingArea_2.getTextWidth(text2) - 2;
										textDrawingArea_2.method389(false, rightX, 0, text2, j11);
									} else
										textDrawingArea_2.method389(false, xPos + 3, 0, s5, j11);
								}
							//	System.out.println("here x: "+xPos+" y: "+yPos+" ");
							}

						} else if (class9_1.type == RSInterface.TYPE_HOVER || class9_1.type == RSInterface.TYPE_CONFIG_HOVER) {
							// Draw sprite
							boolean flag = false;

							if (class9_1.toggled) {
								class9_1.sprite1.drawAdvancedSprite(_x, _y, class9_1.spriteOpacity);
								flag = true;
								class9_1.toggled = false;
							} else {
								class9_1.sprite2.drawAdvancedSprite(_x, _y, class9_1.spriteOpacity);
							}

							// Draw text
							if (class9_1.message == null) {
								continue;
							}
							if (class9_1.centerText) {
								newRegularFont.drawCenteredString(class9_1.message, _x + class9_1.msgX, _y + class9_1.msgY,
									flag ? class9_1.anInt216 : class9_1.textColor, 0x000000);

								// class9_1.rsFont.drawCenteredString(class9_1.message, _x + class9_1.msgX, _y +
								// class9_1.msgY,
								// flag ? class9_1.anInt216 : class9_1.textColor, 0);
							} else {
								newRegularFont.drawCenteredString(class9_1.message, _x + class9_1.msgX, _y + class9_1.msgY,
									flag ? class9_1.anInt216 : class9_1.textColor, 0x000000);
								// class9_1.rsFont.drawBasicString(class9_1.message, _x + 5, _y + class9_1.msgY,
								// flag ? class9_1.anInt216 : class9_1.textColor, 0);
							}
						} else if (class9_1.type == RSInterface.TYPE_CONFIG) {
							Sprite sprite = class9_1.active ? class9_1.sprite2 : class9_1.sprite1;
							sprite.drawSprite(_x, _y);
						} else if (class9_1.type == RSInterface.TYPE_SLIDER) {
							Slider slider = class9_1.slider;
							if (slider != null) {
								slider.draw(_x, _y, 255);
							}
						} else if (class9_1.type == RSInterface.TYPE_DROPDOWN) {

							DropdownMenu d = class9_1.dropdown;

							int bgColour = class9_1.dropdownColours[2];
							int fontColour = 0xfe971e;
							int downArrow = 30;

							if (class9_1.hovered || d.isOpen()) {
								downArrow = 31;
								fontColour = 0xffb83f;
								bgColour = class9_1.dropdownColours[3];
							}

							Rasterizer2D.drawPixels(20, _y, _x, class9_1.dropdownColours[0], d.getWidth());
							Rasterizer2D.drawPixels(18, _y + 1, _x + 1, class9_1.dropdownColours[1], d.getWidth() - 2);
							Rasterizer2D.drawPixels(16, _y + 2, _x + 2, bgColour, d.getWidth() - 4);

							int xOffset = class9_1.centerText ? 3 : 16;
							if (rsInterface.id == 41900) {
								newRegularFont.drawCenteredString(d.getSelected(), _x + (d.getWidth() - xOffset) / 2, _y + 14,
									fontColour, 0);
							} else {
								newSmallFont.drawCenteredString(d.getSelected(), _x + (d.getWidth() - xOffset) / 2, _y + 14,
									fontColour, 0);
							}

							if (d.isOpen()) {
								// Up arrow
								cacheSprite3[29].drawSprite(_x + d.getWidth() - 18, _y + 2);

								Rasterizer2D.drawPixels(d.getHeight(), _y + 19, _x, class9_1.dropdownColours[0], d.getWidth());
								Rasterizer2D.drawPixels(d.getHeight() - 2, _y + 20, _x + 1, class9_1.dropdownColours[1],
									d.getWidth() - 2);
								Rasterizer2D.drawPixels(d.getHeight() - 4, _y + 21, _x + 2, class9_1.dropdownColours[3],
									d.getWidth() - 4);

								int yy = 2;
								for (int i = 0; i < d.getOptions().length; i++) {
									if (class9_1.dropdownHover == i) {
										if (class9_1.id == 28102) {
											Rasterizer2D.drawAlphaBox(_x + 2, _y + 19 + yy, d.getWidth() - 4, 13, 0xd0914d, 80);
										} else {
											Rasterizer2D.drawPixels(13, _y + 19 + yy, _x + 2, class9_1.dropdownColours[4],
												d.getWidth() - 4);
										}
										if (rsInterface.id == 41900) {
											newRegularFont.drawCenteredString(d.getOptions()[i],
												_x + (d.getWidth() - xOffset) / 2, _y + 29 + yy, 0xffb83f, 0);
										} else {
											newSmallFont.drawCenteredString(d.getOptions()[i],
												_x + (d.getWidth() - xOffset) / 2, _y + 29 + yy, 0xffb83f, 0);
										}

									} else {
										Rasterizer2D.drawPixels(13, _y + 19 + yy, _x + 2, class9_1.dropdownColours[3],
											d.getWidth() - 4);
										if (rsInterface.id == 41900) {
											newRegularFont.drawCenteredString(d.getOptions()[i],
												_x + (d.getWidth() - xOffset) / 2, _y + 29 + yy, 0xfe971e, 0);
										} else {
											newSmallFont.drawCenteredString(d.getOptions()[i],
												_x + (d.getWidth() - xOffset) / 2, _y + 29 + yy, 0xfe971e, 0);
										}

									}
									yy += 14;
								}
								drawScrollbar(d.getHeight() - 4, class9_1.scrollPosition, _y + 21, _x + d.getWidth() - 18,
									d.getHeight() - 5);

							} else {
								cacheSprite3[downArrow].drawSprite(_x + d.getWidth() - 18, _y + 2);
							}
						} else if (class9_1.type == RSInterface.TYPE_KEYBINDS_DROPDOWN) {

							DropdownMenu d = class9_1.dropdown;

							// If dropdown inverted, don't draw following 2 menus
							if (dropdownInversionFlag > 0) {
								dropdownInversionFlag--;
								continue;
							}

							Rasterizer2D.drawPixels(18, _y + 1, _x + 1, 0x544834, d.getWidth() - 2);
							Rasterizer2D.drawPixels(16, _y + 2, _x + 2, 0x2e281d, d.getWidth() - 4);
							newRegularFont.drawBasicString(d.getSelected(), _x + 7, _y + 15, 0xff8a1f, 0);
							cacheSprite3[82].drawSprite(_x + d.getWidth() - 18, _y + 2); // Arrow TODO

							if (d.isOpen()) {

								RSInterface.interfaceCache[class9_1.id - 1].active = true; // Alter stone colour

								int yPos = _y + 18;

								// Dropdown inversion for lower stones
								if (class9_1.inverted) {
									yPos = _y - d.getHeight() - 10;
									dropdownInversionFlag = 2;
								}

								Rasterizer2D.drawPixels(d.getHeight() + 12, yPos, _x + 1, 0x544834, d.getWidth() - 2);
								Rasterizer2D.drawPixels(d.getHeight() + 10, yPos + 1, _x + 2, 0x2e281d, d.getWidth() - 4);

								int yy = 2;
								int xx = 0;
								int bb = d.getWidth() / 2;

								for (int i = 0; i < d.getOptions().length; i++) {

									int fontColour = 0xff981f;
									if (class9_1.dropdownHover == i) {
										fontColour = 0xffffff;
									}

									if (xx == 0) {
										newRegularFont.drawBasicString(d.getOptions()[i], _x + 5, yPos + 14 + yy, fontColour,
											0x2e281d);
										xx = 1;

									} else {
										newRegularFont.drawBasicString(d.getOptions()[i], _x + 5 + bb, yPos + 14 + yy,
											fontColour, 0x2e281d);
										xx = 0;
										yy += 15;
									}
								}
							} else {
								RSInterface.interfaceCache[class9_1.id - 1].active = false;
							}
						} else if (class9_1.type == RSInterface.TYPE_ADJUSTABLE_CONFIG) {
							if (class9_1.id != 37010) {
								Rasterizer2D.setDrawingArea(canvasHeight, 0, canvasWidth, 0);
							}
							if(class9_1.id == 37100) {
								if (_y < 41 || _y > 230) {
									return;
								}
							}
							Rasterizer2D.drawAlphaBox(_x, _y, class9_1.width, class9_1.height, class9_1.fillColor, class9_1.opacity);
							Rasterizer2D.setDrawingArea(yPosition + class9_1.height, xPosition, xPosition + class9_1.width, yPosition);
							/**
							 int totalWidth = class9_1.width;
							 int spriteWidth = class9_1.sprite2.myWidth;
							 int totalHeight = class9_1.height;
							 int spriteHeight = class9_1.sprite2.myHeight;
							 Sprite behindSprite = class9_1.active ? class9_1.enabledAltSprite : class9_1.disabledAltSprite;

							 if (class9_1.toggled) {
							 behindSprite.drawSprite(_x, _y);
							 class9_1.sprite2.drawSprite(_x + (totalWidth / 2) - spriteWidth / 2,
							 _y + (totalHeight / 2) - spriteHeight / 2, class9_1.spriteOpacity);
							 class9_1.toggled = false;
							 } else {
							 behindSprite.drawSprite(_x, _y);
							 class9_1.sprite2.drawSprite(_x + (totalWidth / 2) - spriteWidth / 2,
							 _y + (totalHeight / 2) - spriteHeight / 2);
							 }
							 **/
						} else if (class9_1.type == 16) {
							drawInputField(class9_1, _x, _y, class9_1.width, class9_1.height);
						}else if (class9_1.type == RSInterface.TYPE_BOX) {
							// Draw outline
							Rasterizer2D.drawBox(_x - 2, _y - 2, class9_1.width + 4, class9_1.height + 4, 0x0e0e0c);
							Rasterizer2D.drawBox(_x - 1, _y - 1, class9_1.width + 2, class9_1.height + 2, 0x474745);
							// Draw base box
							if (class9_1.toggled) {
								Rasterizer2D.drawBox(_x, _y, class9_1.width, class9_1.height, class9_1.anInt239);
								class9_1.toggled = false;
							} else {
								Rasterizer2D.drawBox(_x, _y, class9_1.width, class9_1.height, class9_1.hoverTextColor);
							}
						} else if (class9_1.type == 19) {
							if (class9_1.backgroundSprites.length > 1) {
								if (class9_1.sprite1 != null) {
									class9_1.sprite1.drawAdvancedSprite(_x, _y);
								}
							}
						} else if (class9_1.type == RSInterface.TYPE_STRING_CONTAINER) {
							int x = _x;
							int y = _y;

							// Set the scroll max based on the strings
							if (class9_1.scrollableContainerInterfaceId != 0) {
								RSInterface container = RSInterface.get(class9_1.scrollableContainerInterfaceId);
								int scrollMax = class9_1.invAutoScrollHeightOffset;
								for (String string : class9_1.stringContainer)
									scrollMax += class9_1.invSpritePadY;
								if (scrollMax > container.height + 1) {
									container.scrollMax = scrollMax;
								} else {
									container.scrollMax = container.height + 1;
								}

								container.scrollMax += container.stringContainerContainerExtraScroll;
							}

							// Draw the container
							for (String string : class9_1.stringContainer) {
								if (class9_1.centerText) {
									class9_1.font.drawCenteredString(string, x, y, class9_1.textColor, class9_1.textShadow ? 0 : -1);
								} else {
									class9_1.font.drawBasicString(string, x, y, class9_1.textColor, class9_1.textShadow ? 0 : -1);
								}
								y += class9_1.invSpritePadY;
							}
						} else if (class9_1.type == RSInterface.TYPE_HORIZONTAL_STRING_CONTAINER) {
							int x = _x;
							int y = _y;

							for (int i = 0; i < class9_1.stringContainer.size(); i++) {
								String string = class9_1.stringContainer.get(i);
								if (class9_1.centerText) {
									class9_1.font.drawCenteredString(string, x, y, class9_1.textColor, class9_1.textShadow ? 0 : -1);
								} else {
									class9_1.font.drawBasicString(string, x, y, class9_1.textColor, class9_1.textShadow ? 0 : -1);
								}
								x += 32 + class9_1.invSpritePadX;
								if ((i + 1) % class9_1.width == 0) {
									y += 32 + class9_1.invSpritePadY;
									x = _x;
								}
							}
						} else if (class9_1.type == RSInterface.TYPE_PROGRESS_BAR) {
							Rasterizer2D.drawPixels(class9_1.height, _y, _x, class9_1.fillColor, class9_1.width);
						} else if (class9_1.type == RSInterface.TYPE_PROGRESS_BAR_2021) {//
							double percentage = class9_1.progressBar2021Percentage;
							int color = RSInterface.getRgbProgressColor(percentage);

							int progressBarWidth = (int) ((double) class9_1.width * percentage);
							Rasterizer2D.drawPixels(class9_1.height, _y, _x, color, progressBarWidth);
							Rasterizer2D.drawBorder(_x, _y, class9_1.width, class9_1.height, class9_1.fillColor);
						} else if (class9_1.type == RSInterface.TYPE_DRAW_BOX) {
							//DrawingArea.drawRoundedRectangle(_x, _y, class9_1.width, class9_1.height, class9_1.fillColor, class9_1.transparency, true, true);

							Rasterizer2D.drawTransparentBox(_x, _y, class9_1.width, class9_1.height, class9_1.fillColor, class9_1.transparency);
							Rasterizer2D.drawBorder(_x, _y, class9_1.width, class9_1.height, class9_1.borderColor);
						}

					if (interfaceText) {
						newSmallFont.drawString(class9_1.id + "", _x - 12, _y, 0xFFFFFFFF, 0, 256);
					}
				} catch (Exception | StackOverflowError e) {
					System.err.println(String.format("Error on interface child, parentId=%s, childIndex=%s, childId=%s", rsInterface.id, childId, rsInterface.children[childId]));
					e.printStackTrace();
				}
			}

			Bank.drawOnBank(rsInterface, 0, 0);

			if (!inheritDrawingArea) {
				Rasterizer2D.setDrawingArea(clipBottom, clipLeft, clipRight, clipTop);
			}
			if (rsInterface.id == 42000) {
				cacheSprite2[76].drawSprite1(24, 280, 200 + (int) (50 * Math.sin(update_tick / 15.0)));

			}
			if (rsInterface.id == 16244) {
				if (MouseHandler.mouseX > 165 && MouseHandler.mouseX < 610 && MouseHandler.mouseY > 428 && MouseHandler.mouseY < 470) {
					Rasterizer2D.drawAlphaBox(165, 428, 444, 42, 0xffffff, 40);
				}
				// cacheSprite2[76].drawSprite1(24, 280,
				// 200 + (int) (50 * Math.sin(tick / 15.0)));
				// DrawingArea.drawBox(30, 400, 400, 400, 0xffffff);
			}
			if (tabID == 0) {
				// TODO blue spec bar
				// DrawingArea.drawBox(53, 250, 148, 24, 0xffffff);
			}
			if (!isResized()) {
				callbacks.drawInterface(WidgetID.FIXED_VIEWPORT_GROUP_ID, Collections.emptyList());
			} else {
				callbacks.drawInterface(WidgetID.RESIZABLE_VIEWPORT_OLD_SCHOOL_BOX_GROUP_ID, Collections.emptyList());
			}

		} catch (Exception | StackOverflowError e) {
			System.err.println(String.format("Error on interface j=%d, xPosition=%d, id=%d, yPosition=%d", scrollPosition, xPosition, rsInterface.id, yPosition));
			e.printStackTrace();
		}
	}

	/**
	 * int state -1 for buy and sell buttons, 0 for buying, 1 for selling, 2 for
	 * canceled
	 * <p>
	 * int itemId The item that the player wants to buy
	 * <p>
	 * int currentlyBought/Sold How many items the player has bought/sold so far.
	 * <p>
	 * int totalAmount How many items the player wants to buy/sell in total.
	 */
	private int[][] grandExchangeInformation = new int[][] { { -1, -1, -1, -1 }, { -1, -1, -1, -1 }, { -1, -1, -1, -1 },
			{ -1, -1, -1, -1 }, { -1, -1, -1, -1 }, { -1, -1, -1, -1 }, { -1, -1, -1, -1 }, { -1, -1, -1, -1 },
			{ -1, -1, -1, -1 },
			// { 0, 11802, 0, 10 },
			// { 0, 11804, 4, 14 },
			// { 2, 11826, 2, 2 },
			// { 1, 555, 47500, 52500 },
			// { 0, 560, 35000, 50000 },
			// { 1, 11847, 0, 1 },
			// { -1, -1, -1, -1 }
	};

	public final String methodR(int j) {
		if (j >= 0 && j < 10000)
			return String.valueOf(j);
		if (j >= 10000 && j < 10000000)
			return j / 1000 + "K";
		if (j >= 10000000 && j < 999999999)
			return j / 1000000 + "M";
		if (j >= 999999999)
			return "*";
		else
			return "?";
	}

//what draws the box hover i think it was the tele interface old one and the items
	public void drawHoverBox(int xPos, int yPos, String text) {
		//System.out.println("text: "+text);
		String[] results = text.split("\n");
		int height = (results.length * 16) + 3;
		int width;
		width = aTextDrawingArea_1271.getTextWidth(results[0]) + 6;
		for (int i = 1; i < results.length; i++)
			if (width <= aTextDrawingArea_1271.getTextWidth(results[i]) + 6)
				width = aTextDrawingArea_1271.getTextWidth(results[i]) + 6;
		Rasterizer2D.drawPixels(height, yPos, xPos, 0x544433, width);
		Rasterizer2D.fillPixels(xPos, width, height, 0, yPos);
		yPos += 14;
		for (int i = 0; i < results.length; i++) {
			aTextDrawingArea_1271.method389(false, xPos + 3, 0, results[i], yPos);
			yPos += 16;
		}
	}

	public void drawBlackBox(int xPos, int yPos) {
		Rasterizer2D.drawBox(xPos - 2, yPos - 1, 1, 71, 0x726451);
		Rasterizer2D.drawBox(xPos + 174, yPos, 1, 69, 0x726451);
		Rasterizer2D.drawBox(xPos - 2, yPos - 2, 178, 1, 0x726451);
		Rasterizer2D.drawBox(xPos, yPos + 68, 174, 1, 0x726451);
		Rasterizer2D.drawBox(xPos - 1, yPos - 1, 1, 71, 0x2E2B23);
		Rasterizer2D.drawBox(xPos + 175, yPos - 1, 1, 71, 0x2E2B23);
		Rasterizer2D.drawBox(xPos, yPos - 1, 175, 1, 0x2E2B23);
		Rasterizer2D.drawBox(xPos, yPos + 69, 175, 1, 0x2E2B23);
		Rasterizer2D.drawTransparentBox(xPos, yPos, 174, 68, 0, 220);
	}

	public void loadTabArea() {
		if(!isOldframeEnabled()) {
			for (int i = 0; i < redStones.length; i++)
				redStones[i] = new Sprite("Gameframe/redstones/redstone" + i);

			for (int i = 0; i < sideIcons.length; i++) {

				sideIcons[i] = new Sprite("Gameframe/sideicons/sideicon" + i);
			}
			mapArea[0] = new Sprite("Gameframe/fixed/mapArea");
			mapArea[1] = new Sprite("Gameframe/fixed/mapBorder");
			mapArea[2] = new Sprite("Gameframe/resizable/mapArea");
			mapArea[3] = new Sprite("Gameframe/fixed/blackMapArea");
			mapArea[4] = new Sprite("Gameframe/resizable/mapBlack");
			mapArea[5] = new Sprite("Gameframe/fixed/topframe");
			mapArea[6] = new Sprite("Gameframe/fixed/chatborder");
			mapArea[7] = new Sprite("Gameframe/fixed/frame");

			tabAreaFixed = new Sprite("Gameframe/fixed/tabArea");
			compassImage = new Sprite("Gameframe/compassImage");
		}else {
			for (int i = 0; i < redStones.length; i++)
				redStones[i] = new Sprite("Gameframe317/redstones/redstone" + i);

			for (int i = 0; i < sideIcons.length; i++)
				sideIcons[i] = new Sprite("Gameframe317/sideicons/sideicon" + i);

			mapArea[0] = new Sprite("Gameframe/fixed/mapArea");
			mapArea[1] = new Sprite("Gameframe317/fixed/mapBorder");
			mapArea[2] = new Sprite("Gameframe317/resizable/mapArea");
			mapArea[3] = new Sprite("Gameframe317/fixed/blackMapArea");
			mapArea[4] = new Sprite("Gameframe317/resizable/mapBlack");
			mapArea[5] = new Sprite("Gameframe/fixed/topframe");
			mapArea[6] = new Sprite("Gameframe/fixed/chatborder");
			mapArea[7] = new Sprite("Gameframe/fixed/frame");

			tabAreaFixed = new Sprite("Gameframe317/fixed/tabArea");
			compassImage = new Sprite("Gameframe317/compassImage");
		}
	}
	public void randomizeBackground(IndexedImage background) {
		int j = 256;
		for (int k = 0; k < anIntArray1190.length; k++)
			anIntArray1190[k] = 0;

		for (int l = 0; l < 5000; l++) {
			int i1 = (int) (Math.random() * 128D * j);
			anIntArray1190[i1] = (int) (Math.random() * 256D);
		}
		for (int j1 = 0; j1 < 20; j1++) {
			for (int k1 = 1; k1 < j - 1; k1++) {
				for (int i2 = 1; i2 < 127; i2++) {
					int k2 = i2 + (k1 << 7);
					anIntArray1191[k2] = (anIntArray1190[k2 - 1] + anIntArray1190[k2 + 1] + anIntArray1190[k2 - 128]
							+ anIntArray1190[k2 + 128]) / 4;
				}

			}
			int ai[] = anIntArray1190;
			anIntArray1190 = anIntArray1191;
			anIntArray1191 = ai;
		}
		if (background != null) {
			int l1 = 0;
			for (int j2 = 0; j2 < background.subHeight; j2++) {
				for (int l2 = 0; l2 < background.subWidth; l2++)
					if (background.palettePixels[l1++] != 0) {
						int i3 = l2 + 16 + background.xOffset;
						int j3 = j2 + 16 + background.yOffset;
						int k3 = i3 + (j3 << 7);
						anIntArray1190[k3] = 0;
					}
			}
		}
	}

	private void decode_playerinfo_masks(int i, int j, Buffer stream, Player player) {
//		if ((i & 0x4000) != 0) {
//			move_speeds[player_idx] = (MoveSpeed) SerialEnum.for_id(MoveSpeed.values(), stream.get_unsignedbyte());
//		}
		if ((i & 0x400) != 0) {
			player.exactmove_x1 = stream.method428();
			player.exactmove_z1 = stream.method428();
			player.exactmove_x2 = stream.method428();
			player.exactmove_z2 = stream.method428();
			player.exactmove_start = stream.method436() + update_tick;
			player.exactmove_end = stream.readUShortA() + update_tick;
			player.forceMovementDirection = stream.method428();
			player.clear_waypoint();
		}
		if ((i & 0x100) != 0) {
			player.spotanim = stream.method434();
			int k = stream.readDWord();
			player.spotanim_height = k >> 16;
			player.spotanim_start_loop = update_tick + (k & 0xffff);
			player.spotanimframe_index = 0;
			player.spotanim_loop = 0;
			if (player.spotanim_start_loop > update_tick)
				player.spotanimframe_index = -1;
			if (player.spotanim == 65535)
				player.spotanim = -1;
		}
		if ((i & 8) != 0) {
			int anim_id = stream.method434();
			if (anim_id == 65535)
				anim_id = -1;
			int anim_delay = stream.method427();
			if (anim_id == player.primaryanim && anim_id != -1) {
				int i3 = AnimationDefinition.anims[anim_id].looptype;
				if (i3 == 1) {
					player.primaryanim_frameindex = 0;
					player.primaryanim_loops_remaining = 0;
					player.primaryanim_pause = anim_delay;
					player.primaryanim_replaycount = 0;
				}
				if (i3 == 2)
					player.primaryanim_replaycount = 0;
			} else if (anim_id == -1 || player.primaryanim == -1
					|| AnimationDefinition.anims[anim_id].priority >= AnimationDefinition.anims[player.primaryanim].priority) {
				player.primaryanim = anim_id;
				player.primaryanim_frameindex = 0;
				player.primaryanim_loops_remaining = 0;
				player.primaryanim_pause = anim_delay;
				player.primaryanim_replaycount = 0;
				player.anim_delay = player.waypoint_count;
			}
		}
		if ((i & 4) != 0) {
			String textSpoken = stream.readString();

			// Only show flower poker chat to people within the area
			if (!player.inFlowerPokerArea() || localPlayer.inFlowerPokerChatProximity()) {
				player.textSpoken = textSpoken;
				if (player.textSpoken.charAt(0) == '~') {
					player.textSpoken = player.textSpoken.substring(1);
					if (player.lastForceChat == null || !player.lastForceChat.equals(textSpoken))
						pushMessage(player.textSpoken, 2, player.displayName);
				} else if (player == localPlayer) {
					if (player.lastForceChat == null || !player.lastForceChat.equals(textSpoken))
						pushMessage(player.textSpoken, 2, player.displayName);
				}
				player.anInt1513 = 0;
				player.anInt1531 = 0;
				player.textCycle = 150;
				player.lastForceChat = textSpoken;
			}
		}
		if ((i & 0x80) != 0) {
			int i1 = stream.method434();
			int rightsPrimaryValue = stream.get_unsignedbyte();
			int j3 = stream.method427();
			int k3 = stream.currentPosition;
			PlayerRights rights = PlayerRights.forRightsValue(rightsPrimaryValue);
			if (player.displayName != null && player.visible) {
				long l3 = StringUtils.longForName(player.displayName);
				boolean flag = false;
				if (!rights.isStaffPosition()) {
					for (int i4 = 0; i4 < ignoreCount; i4++) {
						if (ignoreListAsLongs[i4] != l3)
							continue;
						flag = true;
						break;
					}
				}
				if (!flag && anInt1251 == 0)
					try {
						aStream_834.currentPosition = 0;
						stream.method442(j3, 0, aStream_834.payload);
						aStream_834.currentPosition = 0;
						String s = TextInput.method525(j3, aStream_834);

						// Only show flower poker chat to people within the area
						if (!player.inFlowerPokerArea() || localPlayer.inFlowerPokerChatProximity()) {
							player.textSpoken = s;
							player.anInt1513 = i1 >> 8;
							player.privelage = rightsPrimaryValue; // TODO remove privilege sending in send text player update flag
							player.anInt1531 = i1 & 0xff;
							player.textCycle = 150;
							String crown = PlayerRights.buildCrownString(player.getDisplayedRights());
							String title = player.title != null && !player.title.isEmpty()
									? "<col=" + player.titleColor + ">" + player.title + "</col> "
									: "";
							pushMessage(s, 2, crown + title + player.displayName);
						}
					} catch (Exception exception) {
						exception.printStackTrace();
						Signlink.reporterror("cde2");
					}
			}
			stream.currentPosition = k3 + j3;
		}
		if ((i & 1) != 0) {
			player.interactingEntity = stream.method434();
			if (player.interactingEntity == 65535)
				player.interactingEntity = -1;
		}
		if ((i & 0x10) != 0) {
			int j1 = stream.method427();
			byte abyte0[] = new byte[j1];
			Buffer stream_1 = new Buffer(abyte0);
			stream.readBytes(j1, 0, abyte0);
			localplayer_appearance_data[j] = stream_1;
			player.update_player_appearance(stream_1);
		}
		if ((i & 2) != 0) {
			player.face_x = stream.method436();
			player.face_z = stream.method434();
		}
		if ((i & 0x20) != 0) {
			int k1 = stream.get_unsignedbyte();
			int k2 = stream.method426();
			player.updateHitData(k2, k1, update_tick);
			player.loopCycleStatus = update_tick + 300;
			player.currentHealth = stream.method427();
			player.maxHealth = stream.get_unsignedbyte();
		}
		if ((i & 0x200) != 0) {
			int l1 = stream.get_unsignedbyte();
			int l2 = stream.method428();
			player.updateHitData(l2, l1, update_tick);
			player.loopCycleStatus = update_tick + 300;
			player.currentHealth = stream.get_unsignedbyte();
			player.maxHealth = stream.method427();
		}
	}

	public void method108() {
		try {
			int j = localPlayer.x + cameraOffsetX;
			int k = localPlayer.z + cameraOffsetY;
			if (anInt1014 - j < -500 || anInt1014 - j > 500 || anInt1015 - k < -500 || anInt1015 - k > 500) {
				anInt1014 = j;
				anInt1015 = k;
			}
			if (anInt1014 != j)
				anInt1014 += (j - anInt1014) / 16;
			if (anInt1015 != k)
				anInt1015 += (k - anInt1015) / 16;
			if (KeyHandler.keyArray[1] == 1)
				anInt1186 += (-24 - anInt1186) / 2;
			else if (KeyHandler.keyArray[2] == 1)
				anInt1186 += (24 - anInt1186) / 2;
			else
				anInt1186 /= 2;
			if (KeyHandler.keyArray[3] == 1)
				anInt1187 += (12 - anInt1187) / 2;
			else if (KeyHandler.keyArray[4] == 1)
				anInt1187 += (-12 - anInt1187) / 2;
			else
				anInt1187 /= 2;
			viewRotation = viewRotation + anInt1186 / 2 & 0x7ff;
            camAngleYY += anInt1187 / 2;
			if (camAngleYY < 128)
				camAngleYY = 128;
			if (camAngleYY > 383)
				camAngleYY = 383;
			onCameraPitchTargetChanged(camAngleYY);
			int l = anInt1014 >> 7;
			int i1 = anInt1015 >> 7;
			int j1 = getCenterHeight(plane, anInt1015, anInt1014);
			int k1 = 0;
			if (l > 3 && i1 > 3 && l < 100 && i1 < 100) {
				for (int l1 = l - 4; l1 <= l + 4; l1++) {
					for (int k2 = i1 - 4; k2 <= i1 + 4; k2++) {
						int l2 = plane;
						if (l2 < 3 && (tileFlags[1][l1][k2] & 2) == 2)
							l2++;
						int i3 = j1 - tileHeights[l2][l1][k2];
						if (i3 > k1)
							k1 = i3;
					}

				}

			}
			anInt1005++;
			if (anInt1005 > 1512) {
				anInt1005 = 0;
				stream.createFrame(77);
				stream.writeUnsignedByte(0);
				int i2 = stream.currentPosition;
				stream.writeUnsignedByte((int) (Math.random() * 256D));
				stream.writeUnsignedByte(101);
				stream.writeUnsignedByte(233);
				stream.writeWord(45092);
				if ((int) (Math.random() * 2D) == 0)
					stream.writeWord(35784);
				stream.writeUnsignedByte((int) (Math.random() * 256D));
				stream.writeUnsignedByte(64);
				stream.writeUnsignedByte(38);
				stream.writeWord((int) (Math.random() * 65536D));
				stream.writeWord((int) (Math.random() * 65536D));
				stream.writeBytes(stream.currentPosition - i2);
			}
			int j2 = k1 * 192;
			if (j2 > 0x17f00)
				j2 = 0x17f00;
			if (j2 < 32768)
				j2 = 32768;
			if (j2 > field556) {
				field556 += (j2 - field556) / 24;
				return;
			}
			if (j2 < field556) {
				field556 += (j2 - field556) / 80;
			}
		} catch (Exception _ex) {
			Signlink.reporterror("glfc_ex " + localPlayer.x + "," + localPlayer.z + "," + anInt1014 + "," + anInt1015 + ","
					+ currentRegionX + "," + currentRegionY + "," + baseX + "," + baseY);
			throw new RuntimeException("eek");
		}
	}

	public int loadingPercent;
	public String loadingText;

	private void drawLoadingMessage(String messages) {
		int width = 0;
		for (String message : messages.split("<br>")) {
			int size = newRegularFont.getTextWidth(message);
			if(width <= newRegularFont.getTextWidth(message)) {
				width = size;
			}
		}

		int offset = isResized() ? 3 : 6;

		int height =  (12 * messages.split("<br>").length) + 4;

		Rasterizer2D.drawBox(offset,offset, width + 16, height + 6,0x000000);
		Rasterizer2D.drawBoxOutline(offset,offset, width + 16, height + 6,0xFFFFFF);

		int offsetY = 0;
		for (String message : messages.split("<br>")) {
			newRegularFont.drawCenteredString(message, offset + (width + 16) / 2, offset + 15 + offsetY,0xffffff,1);
			offsetY += 12;
		}

	}


	@Override
	public void draw(boolean redraw) {


		callbacks.frame();
		updateCamera();

		if (gameState == GameState.STARTING.getState()) {
			rasterProvider.drawFull(0, 0);
		} else if (gameState == GameState.LOGIN_SCREEN.getState()) {
			drawLoginScreen(false);
		} else if (gameState == GameState.CONNECTION_LOST.getState()) {
			drawLoadingMessage("Connection lost" + "<br>" + "Please wait - attempting to reestablish");
		} else if (gameState == GameState.LOADING.getState()) {
			drawLoadingMessage("Loading - please wait.");
		} else if (gameState == GameState.LOGGED_IN.getState()) {
			drawGameScreen();
		}
		rasterProvider.drawFull(0, 0);
		//anInt1061++;

		mouseClickCount = 0;
	}

	private boolean isFriendOrSelf(String s) {
		if (s == null)
			return false;
		for (int i = 0; i < friendsCount; i++)
			if (s.equalsIgnoreCase(friendsList[i]))
				return true;
		return s.equalsIgnoreCase(localPlayer.displayName);
	}

	private static String combatDiffColor(int i, int j) {
		int k = i - j;
		if (k < -9)
			return "@red@";
		if (k < -6)
			return "@or3@";
		if (k < -3)
			return "@or2@";
		if (k < 0)
			return "@or1@";
		if (k > 9)
			return "@gre@";
		if (k > 6)
			return "@gr3@";
		if (k > 3)
			return "@gr2@";
		if (k > 0)
			return "@gr1@";
		else
			return "@yel@";
	}

	public void setWaveVolume(int i) {
		Signlink.wavevol = i;
	}

	public static boolean centerInterface() {
		int minimumScreenWidth = 765, minimumScreenHeight = 610;
		if (canvasWidth >= minimumScreenWidth && canvasHeight >= minimumScreenHeight)
			return true;
		return false;
	}


	public static boolean goodDistance(int objectX, int objectY, int playerX, int playerY, int distance) {
		return ((objectX - playerX <= distance && objectX - playerX >= -distance)
				&& (objectY - playerY <= distance && objectY - playerY >= -distance));
	}

	public static int distanceToPoint(int x1, int y1, int x2, int y2) {
		int x = (int) Math.pow(x1 - x2, 2.0D);
		int y = (int) Math.pow(y1 - y2, 2.0D);
		return (int) Math.floor(Math.sqrt(x + y));
	}

	private void displayGroundItems() {
		/**
		 * Loop thru all tiles in region
		 */

		for (int x = 0; x < 104; x++) {
			for (int y = 0; y < 104; y++) {
				Deque class19 = groundItems[plane][x][y];
				int count = 0;
				if (class19 != null) {
					Map<Integer, Long> amounts = new HashMap<>();
					HashSet<Integer> drawn = new HashSet<>();

					for (Item item = (Item) class19.getFirst(); item != null; item = (Item) class19.getNext()) {
						amounts.put(item.ID, item.itemCount + amounts.getOrDefault(item.ID, 0L));
					}

					for (Item item = (Item) class19.getFirst(); item != null; item = (Item) class19.getNext()) {
						if (drawn.contains(item.ID)) {
							continue;
						}
						ItemDefinition itemDef = ItemDefinition.lookup(item.ID);
					//	ItemDef itemDef = ItemDef.forId(item.ID);
						long amount = amounts.get(item.ID);
						long value = (long) itemDef.cost* amount;
						drawn.add(item.ID);

						String valueText = Preferences.getPreferences().groundItemTextShowMoreThan;
						int showValue = 0;
						try {
							showValue = valueText.length() == 0 ? 0 : Integer.parseInt(valueText);
						} catch (NumberFormatException e) {
							Preferences.getPreferences().groundItemTextShowMoreThan = "";
							if (Configuration.developerMode) {
								e.printStackTrace();
							}
						}

						boolean rareDropColor = value > 100_000;

						if (itemDef.tradeable || !Preferences.getPreferences().groundItemAlwaysShowUntradables) {
							String[] hide = Preferences.getPreferences().groundItemTextHide.split(",");
							String[] show = Preferences.getPreferences().groundItemTextShow.split(",");
							String itemNameFormatted = itemDef.getName() == null ? "null" : itemDef.getName();


							boolean showItem = Arrays.stream(show).anyMatch(showTxt -> showTxt.length() > 0 && itemNameFormatted.contains(showTxt.trim()));
							boolean hideItem = Arrays.stream(hide).anyMatch(hideTxt -> hideTxt.length() > 0 && itemNameFormatted.contains(hideTxt.trim()));

							if (!showItem && (hideItem || value < showValue)) {
								continue;
							}
						} else {
							rareDropColor = true;
						}

						calcEntityScreenPos((x * 128 + 64), 25, (y * 128 + 64));

						int itemX = baseX + x;
						int itemY = baseY + y;
						int playerX = baseX + (localPlayer.x - 6 >> 7);
						int playerY = baseY + (localPlayer.z - 6 >> 7);

						/**
						 * Fading
						 */
						int transparencyDistance = distanceToPoint(playerX, playerY, itemX, itemY);
						int transparency = 256 - (transparencyDistance * 16);

						if (goodDistance(itemX, itemY, playerX, playerY, 16)) {
							int yMod = (count * 12);

							StringBuilder builder = new StringBuilder();
							builder.append(itemDef.getName());
							if (amount > 1) {
								builder.append(" (" + (int) amount + ")");
							}
							if (value > 1 && itemDef.tradeable) {
								builder.append(" ("+ StringUtils.insertCommas(value) + " gp)");
							}

							latoBold.drawCenteredString(builder.toString(), spriteDrawX, spriteDrawY - yMod, rareDropColor ? 0xDA6EA2 : 0xffffff, 0x00000, transparency);
							count++;
						}
					}
				}
			}
		}


	}
	public static Sprite[] fadingScreenImages = new Sprite[8];
	private FadingScreen fadingScreen;
	private void draw3dScreen() {
		if (!isResized()) {
			mapArea[1].drawSprite(516, 0);
			mapArea[7].drawSprite(0, 0);
		}
		if(fadingScreen != null) {
			fadingScreen.draw();
		}

		if ((snowVisible && Configuration.CHRISTMAS && !Client.instance.isResized()

		)|| inwaterbirthisland()) {
		//	processWidgetAnimations(tickDelta, 11877);//maybe important
		//	drawInterface(0, 0, RSInterface.interfaceCache[11877], 0);
		}

		if (getUserSettings().isShowEntityTarget()) {
			if (entityTarget != null) {
				entityTarget.draw();
			}
		}
		getRestoreOverlay().drawrestoreOverlay();
		if (getUserSettings().isGameTimers()) {
			try {
				int startX = 516;
				int startY = !Client.instance.isResized() ? 294 : Client.canvasHeight - 209;
				GameTimerHandler.getSingleton().drawGameTimers(this, startX, startY);
			} catch (ParseException e) {
				e.printStackTrace();
			}
		}

		drawSplitPrivateChat();

		if (crossType == 1) {
			crosses[crossIndex / 100].drawSprite(crossX - 8, crossY - 8);
			anInt1142++;
			if (anInt1142 > 67) {
				anInt1142 = 0;
				stream.createFrame(78);
			}
		}
		if (crossType == 2) {
			crosses[4 + crossIndex / 100].drawSprite(crossX - 8, crossY - 8);
		}

		if (openWalkableWidgetID != -1) {//what draws walkable interfaces
			processWidgetAnimations(tickDelta, openWalkableWidgetID);
			if (!Nightmare.instance.drawNightmareInterfaces(openWalkableWidgetID)) {
				RSInterface rsinterface = RSInterface.interfaceCache[openWalkableWidgetID];
				if (!isResized()) {
					drawInterface(0, 0, rsinterface, 0);
				} else {
					if (openWalkableWidgetID == 28000 || openWalkableWidgetID == 28020 || openWalkableWidgetID == 16210
							|| openWalkableWidgetID == 27500 || openWalkableWidgetID == 196) {
						/**
						 * Interfaces to draw at the top right corner next to the minimap (Ex. Wildy
						 * Target)
						 **/

						drawInterface(0, canvasWidth - 730, rsinterface, Client.instance.drawExperienceCounter? 50 : 20);
					} else if (openWalkableWidgetID == 197) {
                        System.out.println("here?");
						drawInterface(0, canvasWidth - 530, rsinterface, -70);
					} else if (openWalkableWidgetID == 21100 || openWalkableWidgetID == 21119
							|| openWalkableWidgetID == 2923) {
						/**
						 * Interfaces to draw at top left corner (Ex. Pest Control)
						 **/
						drawInterface(0, 0, rsinterface, 0);
					} else if (openWalkableWidgetID == 201) {
						/** Duel arena interface **/
						drawInterface(0, canvasWidth - 510, rsinterface, -110);
					} else if (centerInterface() || openInterfaceID == 29230) {
						drawInterface(0, (canvasWidth / 2) - 356, rsinterface,
								!isResized() ? 0 : (canvasHeight / 2) - 230);
					} else {
						if (canvasWidth >= 900 && canvasHeight >= 650) {
							drawInterface(0, (canvasWidth / 2) - 356, RSInterface.interfaceCache[openWalkableWidgetID], !isResized() ? 0 : (canvasHeight / 2) - 230);
						} else {
							drawInterface(0, 0, RSInterface.interfaceCache[openWalkableWidgetID], 0);
						}
					}
				}
			}
		}
	//	this will make broadcast messags not appear if you have an interface open
		//if (openInterfaceID != -1){
		//	BroadcastManager.display(this);
		//} else {
			if(isbroadcastsEnabled())
			BroadcastManager.display(this);
		//	System.out.println("here?");
		//}

		if (openInterfaceID == -1) {
			if (anInt1055 == 1) {//what draws multi
				int x = !isResized() ? 476 : (canvasWidth - 30);
				int y = !isResized() ? 300 : 175;
				if (!isResized() && GameTimerHandler.getSingleton().hasActiveGameTimers()) {
					y -= 32;
				}
				//if in revs do +1
				if (inRevs())
					plusonerevs.drawSprite(x, y);
				else
					multiOverlay.drawSprite(x, y);
			}
			if (isgroundnamesEnabled()) {
				displayGroundItems();
			}

		//	System.out.println("here?");
		}
		if (fpsOn) {
			// char c = '\u01FB';
			int yPosition = 30;
			int xPosition = !isResized() ? 515 : canvasWidth - 222;
			int textColor = 0xffff00;
			if (super.fps < 15)
				textColor = 0xff0000;
			aTextDrawingArea_1271.method380("Fps: " + super.fps, xPosition, textColor, yPosition);
			yPosition += 15;
			Runtime runtime = Runtime.getRuntime();
			int j1 = (int) ((runtime.totalMemory() - runtime.freeMemory()) / 1024L);
			textColor = 0xffff00;
			if (j1 > 0x2000000 && lowMem)
				textColor = 0xff0000;
			aTextDrawingArea_1271.method380("Mem: " + j1 / 1000 + " mb", xPosition, textColor, yPosition);
			yPosition += 15;
		}

		int x = baseX + (localPlayer.x - 6 >> 7);
		int y = baseY + (localPlayer.z - 6 >> 7);
		int mapx = currentRegionX; // map region x
		int mapy = currentRegionY; // map region y
		if (clientData) {
			Runtime runtime = Runtime.getRuntime();
			int j1 = (int) ((runtime.totalMemory() - runtime.freeMemory()) / 1024L);
			aTextDrawingArea_1271.method385(0x00FF00, "Players Nearby: " + playerCount, 27, 5);
			aTextDrawingArea_1271.method385(0x00FF00, "Npcs Nearby: " + npcCount, 41, 5);

			if (mapx > 1000 || mapy > 1000) {
				aTextDrawingArea_1271.method385(0xffff00, "Current Region: " + mapx + ", " + mapy, 55, 5);
			} else {
				aTextDrawingArea_1271.method385(0xffff00, "Current Region: 0" + mapx + ", 0" + mapy, 55, 5);
			}
			for (int num = 0; num < terrainIndices.length; num++) {
				int[] flo = terrainIndices;
				aTextDrawingArea_1271.method385(0xffff00, "Floor map: " + Arrays.toString(flo), 69, 5);
			}
			for (int num = 0; num < objectIndices.length; num++) {
				int[] obj = objectIndices;
				aTextDrawingArea_1271.method385(0xffff00, "Object map: " + Arrays.toString(obj), 83, 5);
				// output: "Object map: "[1, 3, 5, 7, 9]"
			}

			aTextDrawingArea_1271.method385(0xffff00, "Map Data: " + terrainIndices[0] + ".dat", 97, 5);
			aTextDrawingArea_1271.method385(0xffff00, "Fps: " + super.fps, 111, 5);
			aTextDrawingArea_1271.method385(0xffff00, "Memory Used: " + j1/1024 + "MB", 125, 5);
			aTextDrawingArea_1271.method385(0xffff00,
					"Mouse: [" + MouseHandler.mouseX + ", " + MouseHandler.mouseY + "] ", 139, 5);
			aTextDrawingArea_1271.method385(0xffff00, "Coordinates: X: " + x + ", Y: " + y, 153, 5);

			aTextDrawingArea_1271.method385(0xffff00,
					"Camera Position: X: " + xCameraPos + ", Y: " + yCameraPos + ", Z: " + zCameraPos, 167, 5);
			aTextDrawingArea_1271.method385(0xffff00, "Camera Curve: X: " + xCameraCurve + ", Y: " + yCameraCurve, 181,
					5);
			y = 181;
			y += 15;

		}

		if (openInterfaceID != -1) {
			processWidgetAnimations(tickDelta, openInterfaceID);
			RSInterface rsinterface = RSInterface.interfaceCache[openInterfaceID];
			if (!isResized())
				drawInterface(0, 4, rsinterface, 4);
			else
				drawInterface(0, (canvasWidth / 2) - 356, rsinterface, !isResized() ? 0 : (canvasHeight / 2) - 230);
		}
		method70();//

		if (!menuOpen) {
			processRightClick();
			drawTopLeftTooltip();
		} else if (menuScreenArea == 0) {
			drawMenu(0,0);
		}

		if (drawGrid) {
			drawGrid();
		}


		if (anInt1104 != 0) {
			int j = anInt1104 / 50;
			int l = j / 60;
			j %= 60;
			int yPosition = !isResized() ? 329 : canvasHeight - 165;
			if (j < 10)
				aTextDrawingArea_1271.method385(0xffff00, "Server will be updating: " + l + ":0" + j, yPosition, 5);
			else
				aTextDrawingArea_1271.method385(0xffff00, "Server will be updating: " + l + ":" + j, yPosition, 5);

			anInt849++;
			if (anInt849 > 75) {
				anInt849 = 0;
				stream.createFrame(148);
			}
		}

		drawScreenBox();
		devConsole.draw_console();
	}

	private void addIgnore(long l) {
		try {
			if (l == 0L)
				return;

			String username = StringUtils.fixName(StringUtils.nameForLong(l));

			if (username.equalsIgnoreCase(localPlayer.displayName))
				return;

			if (ignoreCount >= 100) {
				pushMessage("Your ignore list is full. Max of 100 hit", 0, "");
				return;
			}
			for (int j = 0; j < ignoreCount; j++)
				if (ignoreListAsLongs[j] == l) {
					pushMessage(username + " is already on your ignore list", 0, "");
					return;
				}
			for (int k = 0; k < friendsCount; k++)
				if (friendsListAsLongs[k] == l) {
					pushMessage("Please remove " + username + " from your friend list first", 0, "");
					return;
				}

			//ignoreListAsLongs[ignoreCount++] = l;
			//needDrawTabArea = true;
			stream.createFrame(133);
			stream.writeQWord(l);
			return;
		} catch (RuntimeException runtimeexception) {
			Signlink.reporterror("45688, " + l + ", " + 4 + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	public void update_all_player_movement() {
		for (int i = -1; i < playerCount; i++) {
			int j;
			if (i == -1)
				j = maxPlayerCount;
			else
				j = highres_player_list[i];
			Player player = players[j];
			if (player != null)
				update_movement(player);
		}

	}

	private void method115() {
		if (loadingStage == 2) {
			boolean passedRequest = false;
			for (SpawnedObject spawnedObject = (SpawnedObject) spawns
					.reverseGetFirst(); spawnedObject != null; spawnedObject = (SpawnedObject) spawns
					.reverseGetNext()) {
				if (spawnedObject.getLongetivity > 0)
					spawnedObject.getLongetivity--;
				if (spawnedObject.getLongetivity == 0) {
					if (spawnedObject.getPreviousId < 0
							|| MapRegion.modelReady(spawnedObject.getPreviousId, spawnedObject.previousType)) {
						removeObject(spawnedObject.y, spawnedObject.plane, spawnedObject.previousOrientation,
								spawnedObject.previousType, spawnedObject.x, spawnedObject.group,
								spawnedObject.getPreviousId);
						spawnedObject.unlink();
					}
				} else {
					if (spawnedObject.delay > 0)
						spawnedObject.delay--;
					if (spawnedObject.delay == 0 && spawnedObject.x >= 1 && spawnedObject.y >= 1
							&& spawnedObject.x <= 102 && spawnedObject.y <= 102
							&& (spawnedObject.objectId < 0
							|| MapRegion.modelReady(spawnedObject.objectId, spawnedObject.type))) {
						removeObject(spawnedObject.y, spawnedObject.plane, spawnedObject.orientation,
								spawnedObject.type, spawnedObject.x, spawnedObject.group,
								spawnedObject.objectId);
						spawnedObject.delay = -1;
						if (spawnedObject.objectId == spawnedObject.getPreviousId && spawnedObject.getPreviousId == -1)
							spawnedObject.unlink();
						else if (spawnedObject.objectId == spawnedObject.getPreviousId
								&& spawnedObject.orientation == spawnedObject.previousOrientation
								&& spawnedObject.type == spawnedObject.previousType)
							spawnedObject.unlink();
						passedRequest = true;
					}
				}
			}
			if (passedRequest)
				// anInt985 = plane;
				renderMapScene(plane);

		}
	}

	private void determineMenuSize() {
		int i = chatTextDrawingArea.getTextWidth("Choose Option");
		for (int j = 0; j < menuActionRow; j++) {
			int k = chatTextDrawingArea.getTextWidth(menuActionName[j]);
			if (k > i)
				i = k;
		}
		i += 8;
		int l = 15 * menuActionRow + 21;
		if (MouseHandler.saveClickX > 0 && MouseHandler.saveClickY > 0 && MouseHandler.saveClickX < canvasWidth
				&& MouseHandler.saveClickY < canvasHeight) {
			int xClick = MouseHandler.saveClickX - i / 2;
			if (xClick + i > canvasWidth - 4) {
				xClick = canvasWidth - 4 - i;
			}
			if (xClick < 0) {
				xClick = 0;
			}
			int yClick = MouseHandler.saveClickY - 0;
			if (yClick + l > canvasHeight - 2) {
				yClick = canvasHeight - 2 - l;
			}
			if (yClick < 0) {
				yClick = 0;
			}
			menuOpen = true;
			menuOffsetX = xClick;
			menuOffsetY = yClick;
			menuWidth = i;
			menuHeight = 15 * menuActionRow + 22;
		}

	}

	public void update_self_player(Buffer stream) {
		stream.initBitAccess();
		int j = stream.readBits(1);
		if (j == 0)
			return;
		int k = stream.readBits(2);
		if (k == 0) {
			entity_extended_info_list[anInt893++] = maxPlayerCount;
			return;
		}
		if (k == 1) {
			int dir = stream.readBits(3);
			localPlayer.move_step_direction(MoveSpeed.WALK, dir);
			int k1 = stream.readBits(1);
			if (k1 == 1)
				entity_extended_info_list[anInt893++] = maxPlayerCount;
			return;
		}
		if (k == 2) {
			if (stream.readBits(1) == 1) {
				int i1 = stream.readBits(3);
				localPlayer.move_step_direction(MoveSpeed.RUN, i1);
				int l1 = stream.readBits(3);
				localPlayer.move_step_direction(MoveSpeed.RUN, l1);
			} else {
				int l1 = stream.readBits(3);
				localPlayer.move_step_direction(MoveSpeed.CRAWL, l1);
			}
			int j2 = stream.readBits(1);
			if (j2 == 1)
				entity_extended_info_list[anInt893++] = maxPlayerCount;
			return;
		}
		if (k == 3) {
			plane = stream.readBits(2);
			int j1 = stream.readBits(1);
			int i2 = stream.readBits(1);
			if (i2 == 1)
				entity_extended_info_list[anInt893++] = maxPlayerCount;
			int k2 = stream.readBits(7);
			int l2 = stream.readBits(7);
			localPlayer.teleport(l2, k2, j1 == 1);
		}
	}

	public void nullLoader() {
		aBoolean831 = false;
		while (drawingFlames) {
			aBoolean831 = false;
			try {
				Thread.sleep(50L);
			} catch (Exception _ex) {
			}
		}
		aBackgroundArray1152s = null;
		anIntArray850 = null;
		anIntArray851 = null;
		anIntArray852 = null;
		anIntArray853 = null;
		anIntArray1190 = null;
		anIntArray1191 = null;
		anIntArray828 = null;
		anIntArray829 = null;
		aClass30_Sub2_Sub1_Sub1_1201 = null;
		aClass30_Sub2_Sub1_Sub1_1202 = null;
	}

	private boolean processWidgetAnimations(int tick, int interfaceId) {
		boolean dirty = false;
		RSInterface class9 = RSInterface.interfaceCache[interfaceId];
		for (int k = 0; k < class9.children.length; k++) {
			if (class9.children[k] == -1)
				break;
			RSInterface class9_1 = RSInterface.interfaceCache[class9.children[k]];
			if (class9_1.type == 1)
				dirty |= processWidgetAnimations(tick, class9_1.id);
			if (class9_1.type == 6 && (class9_1.seq_id != -1 || class9_1.active_seq_id != -1)) {
				boolean flag2 = cs1_selected(class9_1);
				int anim_id;
				if (flag2)
					anim_id = class9_1.active_seq_id;
				else
					anim_id = class9_1.seq_id;
//System.out.println("aaaaaa: "+anim_id);
				if (anim_id != -1) {
					AnimationDefinition seqtype = AnimationDefinition.anims[anim_id];
					System.out.println("we using keyframes? "+seqtype.using_keyframes());
					if (seqtype.using_keyframes()) {
						System.out.println("keyframes "+anim_id);
						class9_1.iftype_frameindex += tick;
						int var8 = seqtype.get_keyframe_duration();
						if (class9_1.iftype_frameindex >= var8) {
							class9_1.iftype_frameindex -= seqtype.loop_delay;
							if (class9_1.iftype_frameindex < 0 || class9_1.iftype_frameindex >= var8) {
								class9_1.iftype_frameindex = 0;
							}
						}

						dirty = true;
					} else {
						for (class9_1.iftype_loops_remaining += tick; class9_1.iftype_loops_remaining > seqtype.frame_durations[class9_1.iftype_frameindex]; ) {
							class9_1.iftype_loops_remaining -= seqtype.frame_durations[class9_1.iftype_frameindex] + 1;
							class9_1.iftype_frameindex++;
							if (class9_1.iftype_frameindex >= seqtype.framecount) {
								class9_1.iftype_frameindex -= seqtype.loop_delay;
								if (class9_1.iftype_frameindex < 0 || class9_1.iftype_frameindex >= seqtype.framecount)
									class9_1.iftype_frameindex = 0;
							}
							dirty = true;
						}
					}

				}
			}
		}

		return dirty;
	}

	public int getXCameraPosShift() {
		int i = xCameraPos >> 7;
		return i < 0 ? 0 : i >= 104 ? 103 : i;
	}

	public int getYCameraPosShift() {
		int i = yCameraPos >> 7;
		return i < 0 ? 0 : i >= 104 ? 103 : i;
	}

	private int method120() {
		int j = 3;
		if (yCameraCurve < 310 || removeRoofs) {
			int k = getXCameraPosShift();
			int l = getYCameraPosShift();
			int i1 = localPlayer.x >> 7;
			int j1 = localPlayer.z >> 7;
			if ((tileFlags[plane][k][l] & 4) != 0)
				j = plane;
			int k1;
			if (i1 > k)
				k1 = i1 - k;
			else
				k1 = k - i1;
			int l1;
			if (j1 > l)
				l1 = j1 - l;
			else
				l1 = l - j1;
			if (k1 > l1) {
				int i2 = (l1 * 0x10000) / k1;
				int k2 = 32768;
				while (k != i1) {
					if (k < i1)
						k++;
					else if (k > i1)
						k--;
					if ((tileFlags[plane][k][l] & 4) != 0 || removeRoofs)
						j = plane;
					k2 += i2;
					if (k2 >= 0x10000) {
						k2 -= 0x10000;
						if (l < j1)
							l++;
						else if (l > j1)
							l--;
						if ((tileFlags[plane][k][l] & 4) != 0 || removeRoofs)
							j = plane;
					}
				}
			} else {
				if (l1 == 0) // /zero crash fix
					l1 = 1;
				int j2 = (k1 * 0x10000) / l1;
				int l2 = 32768;
				while (l != j1) {
					if (l < j1)
						l++;
					else if (l > j1)
						l--;
					if ((tileFlags[plane][k][l] & 4) != 0 || removeRoofs)
						j = plane;
					l2 += j2;
					if (l2 >= 0x10000) {
						l2 -= 0x10000;
						if (k < i1)
							k++;
						else if (k > i1)
							k--;
						if ((tileFlags[plane][k][l] & 4) != 0 || removeRoofs)
							j = plane;
					}
				}
			}
		}
		if ((tileFlags[plane][localPlayer.x >> 7][localPlayer.z >> 7] & 4) != 0)
			j = plane;
		return j;
	}

	private int method121() {
		int j = getCenterHeight(plane, yCameraPos, xCameraPos);
		int x = getXCameraPosShift();
		int y = getYCameraPosShift();
		if (j - zCameraPos < 800 && (tileFlags[plane][x][y] & 4) != 0)
			return plane;
		else
			return 3;
	}

	private void delIgnore(long l) {
		try {
			if (l == 0L)
				return;
			for (int j = 0; j < ignoreCount; j++)
				if (ignoreListAsLongs[j] == l) {
					ignoreCount--;
					needDrawTabArea = true;
					System.arraycopy(ignoreListAsLongs, j + 1, ignoreListAsLongs, j, ignoreCount - j);

					stream.createFrame(74);
					stream.writeQWord(l);
					return;
				}

			return;
		} catch (RuntimeException runtimeexception) {
			Signlink.reporterror("47229, " + 3 + ", " + l + ", " + runtimeexception.toString());
		}
		throw new RuntimeException();
	}

	@Override
	public String getParameter(String s) {
		if (Signlink.mainapp != null)
			return Signlink.mainapp.getParameter(s);
		else
			return super.getParameter(s);
	}

	public void adjustVolume(boolean flag, int i) {
		Signlink.midivol = i;
		if (flag)
			Signlink.midi = "voladjust";
	}
	public String widget_tooltip_text_script(String text, RSInterface child) {
		if (text.indexOf("%") != -1) {
			int script_id;
			for (script_id = 1; script_id <= 5; script_id++) {
				while (true) {
					int index = text.indexOf("%" + script_id);
					if (index == -1) {
						break;
					}
					text = text.substring(0, index) + interfaceIntToString(extractInterfaceValues(child, script_id - 1))
							+ text.substring(index + 2);
				}
			}
		}
		return text;
	}
	private int extractInterfaceValues(RSInterface widget, int id) {
		if (widget.scripts == null || id >= widget.scripts.length)
			return -2;
		try {
			int script[] = widget.scripts[id];
			int accumulator = 0;
			int counter = 0;
			int operator = 0;
			do {
				int instruction = script[counter++];
				int value = 0;
				byte next = 0;
				if (instruction == 0)
					return accumulator;
				if (instruction == 1)
					value = currentStats[script[counter++]];
				if (instruction == 2)
					value = maxStats[script[counter++]];
				if (instruction == 3)
					value = currentExp[script[counter++]];
				if (instruction == 4) { // Check for item inside interface, interfaceId = ai[l++]
					RSInterface inventoryContainer = RSInterface.interfaceCache[script[counter++]];
					int item = script[counter++];
					if (item >= 0 && item < ItemDefinition.totalItems
							&& (!ItemDefinition.lookup(item).members || isMembers)) {
						int itemId = item + 1;
						int actualItemId = item;
						for (int slot = 0; slot < inventoryContainer.inventoryItemId.length; slot++) {
							if (inventoryContainer.inventoryItemId[slot] == itemId)
								value += inventoryContainer.inventoryAmounts[slot];
						}

						if (inventoryContainer.id == 3214) {
							RSInterface equipment = RSInterface.interfaceCache[1688];
							if (actualItemId == Items.NATURE_RUNE) {
								if (equipment.hasItem(Items.BRYOPHYTAS_STAFF)) {
									return Integer.MAX_VALUE;
								}
							}
							if (actualItemId == Items.AIR_RUNE) {
								if (equipment.hasItem(Items.STAFF_OF_AIR)) {
									return Integer.MAX_VALUE;
								}
							}
							if (actualItemId == Items.FIRE_RUNE) {
								if (equipment.hasItem(Items.TOME_OF_FIRE) || equipment.hasItem(Items.STAFF_OF_FIRE)) {
									return Integer.MAX_VALUE;
								}
							}
							if (actualItemId == Items.EARTH_RUNE) {
								if (equipment.hasItem(Items.STAFF_OF_EARTH)) {
									return Integer.MAX_VALUE;
								}
							}
							if (actualItemId == Items.WATER_RUNE) {
								if (equipment.hasItem(Items.STAFF_OF_WATER)) {
									return Integer.MAX_VALUE;
								}
							}
						}

						if ((runePouch[0][0] + 1) == itemId) {
							value += runePouch[0][1];
						}
						if ((runePouch[1][0] + 1) == itemId) {
							value += runePouch[1][1];
						}
						if ((runePouch[2][0] + 1) == itemId) {
							value += runePouch[2][1];
						}
					}
				}
				if (instruction == 5) {
					value = variousSettings[script[counter++]];
				}
				if (instruction == 6)
					value = SKILL_EXPERIENCE[maxStats[script[counter++]] - 1];
				if (instruction == 7)
					value = (variousSettings[script[counter++]] * 100) / 46875;
				if (instruction == 8)
					value = localPlayer.combatLevel;
				if (instruction == 9) {
					for (int l1 = 0; l1 < Skills.SKILLS_COUNT; l1++)
						if (Skills.SKILLS_ENABLED[l1])
							value += maxStats[l1];
				}
				if (instruction == 10) {
					RSInterface equipmentContainer = RSInterface.interfaceCache[script[counter++]];
					int item = script[counter++] + 1;
					if (item >= 0 && item < ItemDefinition.totalItems
							&& (!ItemDefinition.lookup(item).members || isMembers)) {
						for (int stored = 0; stored < equipmentContainer.inventoryItemId.length; stored++) {
							if (equipmentContainer.inventoryItemId[stored] != item)
								continue;
							value = 0x3b9ac9ff;
							break;
						}

					}
				}
				if (instruction == 11)
					value = energy;
				if (instruction == 12)
					value = weight;
				if (instruction == 13) {
					int bool = variousSettings[script[counter++]];
					int shift = script[counter++];
					value = (bool & 1 << shift) == 0 ? 0 : 1;
				}
				if (instruction == 14) {
					int index = script[counter++];
					VarBit bits = VarBit.cache[index];
					int setting = bits.setting;
					int low = bits.start;
					int high = bits.end;
					int mask = BIT_MASKS[high - low];
					value = variousSettings[setting] >> low & mask;
				}
				if (instruction == 15)
					next = 1;
				if (instruction == 16)
					next = 2;
				if (instruction == 17)
					next = 3;
				if (instruction == 18)
					value = (localPlayer.x >> 7) + baseX;
				if (instruction == 19)
					value = (localPlayer.z >> 7) + baseY;
				if (instruction == 20)
					value = script[counter++];
				if (next == 0) {
					if (operator == 0)
						accumulator += value;
					if (operator == 1)
						accumulator -= value;
					if (operator == 2 && value != 0)
						accumulator /= value;
					if (operator == 3)
						accumulator *= value;
					operator = 0;
				} else {
					operator = next;
				}
			} while (true);
		} catch (Exception _ex) {
			return -1;
		}
	}

	public void drawTopLeftTooltip() {
		if (devConsole.console_open)
			return;

		if (fullscreenInterfaceID != -1) {
			return;
		}

		if (tabInterfaceIDs[tabID] == 17200 && menuActionName[1].contains("ctivate")) {
			menuActionName[1] = "Select";
			menuActionID[1] = 850;
			menuActionRow = 2;
		}
		if (menuActionRow < 2 && itemSelected == 0 && spellSelected == 0)
			return;
		String s;
		if (itemSelected == 1 && menuActionRow < 2)
			s = "Use " + selectedItemName + " with...";
		else if (spellSelected == 1 && menuActionRow < 2)
			s = spellTooltip + "...";
		else
			s = menuActionName[menuActionRow - 1];
		if (!s.contains("Walk here") && openInterfaceID != 88000)
			drawHoverBox(MouseHandler.mouseX+10, MouseHandler.mouseY-10, s);

		if (menuActionRow > 2)
			s = s + "@whi@ / " + (menuActionRow - 2) + " more options";
		toolTip=s;
		newBoldFont.drawString(s, 8, 19, 0xffffff, 0, 255);
	}
	public String toolTip;

	private Sprite compassImage;
	private Sprite[] mapArea = new Sprite[8];

	private Sprite eventIcon;
	public Sprite bankDivider;
	public Sprite saveButton;

	public Sprite xpbartick;

	public Sprite xpbar;
	private int[][] getCustomMapIcons() {
		return new int[][] {
				//map icons minimap icons map sprites
				/*
				0 gen store
				1 sword
				2 mage
				3 b axe
				4 med helm
				5 bank
				6 quest
				7 jewerlly
				8 mining
				9 furnace
				10 smithing
				11 dummy
				12 dungeon
				13 leave dung
				14 shortcut
				15 bstaff
				16 gray top
				17 gray legs
				18 small sword
				19 arrow
				20 shield
				21 altar
				22 herb
				23 ring
				24 red gem
				25 crafting chisel
				26 ccandle
				27 fishing rod
				28 fish spot
				29 green top
				30 potions
				31 silk
				32 kebab
				33 beer
				34 chisel?
				35 brown top
				36 woodcutting
				37 wheel
				38 bread
				39 fork and knife
				40 minigame
				41 water
				42 pot
				43 pink skirt
				44 bowl
				45 grain
				46 golden axe
				47 chain body
				48 gray rock, spices?
				49 red man
				50 bowl with red stuff
				51 agility
				52 apple
				53 slayer
				54 haircut
				55 shovel
				56 appearence mask editor
				57 travel arrow
				58 purple portal
				59 pot
				60 fence
				61 barrel
				62 butter
				63 water
				64 hunter
				65 vote
				66 leather
				67 house
				68 saw
				69 hammer and rock
				70 shortcut
				71 box
				72 gift box
				73 sand pit
				74 green achievement star
				75 pets
				76 bounty icon
				77 sword and shirt icon
				78 logs
				79 plants
				80 runecrafting
				81 rc rock
				82 grand exchange
				83 purple star
				84 brown star
				85 question mark tutorial icons
				86 bank
				87 lock
				88 wc
				89 mining
				90 fishing
				91 hunter
				92 smithing
				93 crafting
				94 cooking
				95 melee
				96 prayer
				97 dungeon
				98 ironman
				99 bounty hunter
				100 bond
				101 holiday gift
				102 bottle
				103 partyhat
				104 newspaper
				105 cup of tea
				106 bell
				107 red bottle
				108 whichs pot
				109 rope
				110 hand
				111 band aid
				112 ancient altar
				113 bowl
				 */
				// sprite id, x, y
				{75,1998,3595}, // gertrude pets
				{99, 2042, 3584}, // pk durial321

				{72, 	 2008, 3654}, // donator store "gift box"
		//		{53, 2050, 3587}, // slayer
				//{56,2020,3657}, // makeover mage

				//{0,2006,3638}, // makeover mage
//				{53, 3076, 3504}, // slayer area updated
//			//	{94, 3067, 3472}, // wildy slayer area
//				{30, 3080, 3492}, // pool icon
//			//	{9, 3089, 3475}, // hespori entrance
//			//	{98, 3092, 3480}, // ironman store
//				//{77, 3110, 3495}, //supplies area
//				{24, 3089, 3505}, //thieving red gem done
//				//{66, 3091, 3470}, //thieving leather
//				{38, 3095, 3501}, //thieving bakers stall done
//				{31, 3098, 3505}, //thieving silk
//				{34, 3096, 509}, //thieving silver done
//				//{51, 3093, 3474}, //agility
//				//{58, 3103, 3471}, //house portal
//				{5, 3094, 3491}, //bank
//			//	{5, 3119, 3506}, //bank
//			//	{112, 3099, 3513}, //ancient altar
//				{21, 3089, 3491}, // altar
//			//	{57, 3087, 3494}, // travel arrow
//			//	{28, 3125, 3490} //fishing
//				/*{5, 3090, 3499}, // bank
//                {5, 3077, 3517}, // bank
//                {5, 3090, 3517}, // bank
//
//                {56, 3094, 3516}, // teleport
//                {56, 3097, 3500}, // teleport
//                {77, 3097, 3507}, // runecraft
//                {64, 3076, 3508}, // housing portal
//                {19, 3085, 3515}, // altar
//                {79, 3094, 3497}, // grand exchange*/
//
//				// home
////				{5, 3097, 3490}, // bank
////				{5, 3089, 3507}, // bank wild
////				{79, 3091, 3495}, // grand exchange
////				{51, 3079, 3493}, // slayer
////				{0, 3080, 3511}, // general store
////				{56, 3086, 3496}, // teleport
////				{77, 3104, 3491}, // runecraft
////				{64, 3086, 3485}, // housing portal
//				//{54, 2326, 3177}, // makeover
//				//{44, 2329, 3162}, // star sprite
//				//{19, 3086, 3510}, // altar
//				//{72, 3089, 3512}, // bounty shop
//				//{55, 3086, 3490}, // tutorial
//				//{76, 2356, 3152}, // flax
//
//				//skill area
//				//{10, 3109, 3496}, // anvil
//				//{9, 3109, 3500}, // furnace
//				//{35, 3106, 3501}, // spinning wheel
//
//				//{19, 3099, 3507}, // altar
//				//{11, 3110, 3517}, // training dummies
//				//{72, 2326, 3179}, // bounty
//				//{38, 2326, 3175}, // achievement
		};
	}

	private void drawMinimap() {

		int xOffset = !isResized() ? 516 : 0;

		/** Black minimap **/
		if (minimapState == 2) {
			if (!isResized()) {
				mapArea[3].drawSprite(xOffset, 4);
				mapArea[5].drawSprite(xOffset, 0);
				//mapArea[3].drawSprite(xOffset, 4);
			} else {
				mapArea[2].drawSprite(canvasWidth - 183, 0);
				mapArea[4].drawSprite(canvasWidth - 160, 8);
			}
			if(isOldframeEnabled()&& !isResized() ){
				compassImage.rotate(33, viewRotation, anIntArray1057, 256, anIntArray968,
						(!isResized() ? 25: 25), 4,
						(!isResized() ? 29 + xOffset : canvasWidth - 178), 33, 25);//28 + xOffset
			}else {
				compassImage.rotate(33, viewRotation, anIntArray1057, 256, anIntArray968,
						(!isResized() ? 25 : 25) , 4,
						(!isResized() ? 29 + xOffset : canvasWidth - 178) , 33, 25);
			}
            if (drawOrbs)
                loadAllOrbs(!isResized() ? xOffset : canvasWidth - 217);
			return;
		}

		// If not loaded in don't draw mapback
		if (loadingStage == 2) {

			int i = viewRotation + minimapRotation & 0x7ff;
			int j = 48 + localPlayer.x / 32;
			int l2 = 464 - localPlayer.z / 32;

			int positionX = (!isResized() ? 9 : 7);
			int positionY = (!isResized() ? 51 + xOffset : canvasWidth - 160);

			minimapImage.rotate(151, i, anIntArray1229, 256 + minimapZoom, anIntArray1052, l2, positionX , positionY, 151, j);

			for (int[] icon : getCustomMapIcons()) {
				markMinimap(mapFunctions[icon[0]], ((icon[1] - baseX) * 4 + 2) - Client.localPlayer.x / 32, ((icon[2] - baseY) * 4 + 2) - Client.localPlayer.z / 32);
			}

			for (int j5 = 0; j5 < mapIconAmount; j5++) {
				int k = (anIntArray1072[j5] * 4 + 2) - localPlayer.x / 32;
				int i3 = (anIntArray1073[j5] * 4 + 2) - localPlayer.z / 32;
				markMinimap(mapIconSprite[j5], k, i3);
			}

//custom map icons!
			if (Configuration.HALLOWEEN) {
				switch (plane) {
					case 0:
						markMinimap(eventIcon, ((3088 - baseX) * 4 + 2) - localPlayer.x / 32,
								((3494 - baseY) * 4 + 2) - localPlayer.z / 32);
						markMinimap(eventIcon, ((2607 - baseX) * 4 + 2) - localPlayer.x / 32,
								((4773 - baseY) * 4 + 2) - localPlayer.z / 32);
						markMinimap(eventIcon, ((3103 - baseX) * 4 + 2) - localPlayer.x / 32,
								((3482 - baseY) * 4 + 2) - localPlayer.z / 32);
						break;
				}
			}
			if (Configuration.CHRISTMAS && Configuration.CHRISTMAS_EVENT) {
				switch (plane) {
					case 0:
						markMinimap(minimapIcons[0], ((3086 - baseX) * 4 + 2) - localPlayer.x / 32,
								((3498 - baseY) * 4 + 2) - localPlayer.z / 32);
						markMinimap(minimapIcons[0], ((2832 - baseX) * 4 + 2) - localPlayer.x / 32,
								((3798 - baseY) * 4 + 2) - localPlayer.z / 32);
						markMinimap(minimapIcons[0], ((2982 - baseX) * 4 + 2) - localPlayer.x / 32,
								((3643 - baseY) * 4 + 2) - localPlayer.z / 32);
						break;

					case 2:
						markMinimap(minimapIcons[0], ((2827 - baseX) * 4 + 2) - localPlayer.x / 32,
								((3810 - baseY) * 4 + 2) - localPlayer.z / 32);
						break;
				}
			}

			for (int k5 = 0; k5 < 104; k5++) {
				for (int l5 = 0; l5 < 104; l5++) {
					Deque class19 = groundItems[plane][k5][l5];
					if (class19 != null) {
						int l = (k5 * 4 + 2) - localPlayer.x / 32;
						int j3 = (l5 * 4 + 2) - localPlayer.z / 32;
						markMinimap(mapDotItem, l, j3);
					}
				}
			}

			for (int i6 = 0; i6 < npcCount; i6++) {
				Npc npc = npcs[highres_npc_list[i6]];
				if (npc != null && npc.isVisible()) {
					NpcDefinition entityDef = npc.desc;
					if (entityDef.multi != null)
						entityDef = entityDef.get_multi_npctype();
					if (entityDef != null && entityDef.drawmapdot && entityDef.active) {
						int i1 = npc.x / 32 - localPlayer.x / 32;
						int k3 = npc.z / 32 - localPlayer.z / 32;
						markMinimap(mapDotNPC, i1, k3);
					}
				}
			}

			for (int j6 = 0; j6 < playerCount; j6++) {
				Player player = players[highres_player_list[j6]];
				if (player != null && player.isVisible()) {
					int j1 = player.x / 32 - localPlayer.x / 32;
					int l3 = player.z / 32 - localPlayer.z / 32;
					boolean flag1 = false;
					boolean flag3 = false;
					String clanname;
					for (String s : clanList) {
						if (s == null)
							continue;
						clanname = s;
						if (clanname.startsWith("<clan"))
							clanname = clanname.substring(clanname.indexOf(">") + 1);
						if (!clanname.equalsIgnoreCase(player.displayName))
							continue;
						flag3 = true;
						break;
					}
					long l6 = StringUtils.longForName(player.displayName);
					for (int k6 = 0; k6 < friendsCount; k6++) {
						if (l6 != friendsListAsLongs[k6] || friendsNodeIDs[k6] == 0)
							continue;
						flag1 = true;
						break;
					}
					boolean flag2 = localPlayer.team != 0 && player.team != 0 && localPlayer.team == player.team;
					if (flag1)
						markMinimap(mapDotFriend, j1, l3);
					else if (flag3)
						markMinimap(mapDotClan, j1, l3);
					else if (flag2)
						markMinimap(mapDotTeam, j1, l3);
					else
						markMinimap(mapDotPlayer, j1, l3);
				}
			}

			if (hintType != 0 && update_tick % 20 < 10) {
				if (hintType == 1 && anInt1222 >= 0 && anInt1222 < npcs.length) {
					Npc npc = npcs[anInt1222];
					if (npc != null) {
						int k1 = npc.x / 32 - localPlayer.x / 32;
						int i4 = npc.z / 32 - localPlayer.z / 32;
						refreshMinimap(mapMarker, i4, k1);
					}
				}
				if (hintType == 2) {
					int l1 = ((hintIconXpos - baseX) * 4 + 2) - localPlayer.x / 32;
					int j4 = ((hintIconYpos - baseY) * 4 + 2) - localPlayer.z / 32;
					refreshMinimap(mapMarker, j4, l1);
				}
				if (hintType == 10 && anInt933 >= 0 && anInt933 < players.length) {
					Player player = players[anInt933];
					if (player != null) {
						int i2 = player.x / 32 - localPlayer.x / 32;
						int k4 = player.z / 32 - localPlayer.z / 32;
						refreshMinimap(mapMarker, k4, i2);
					}
				}
			}

			if (destX != 0) {
				int j2 = (destX * 4 + 2) - localPlayer.x / 32;
				int l4 = (destY * 4 + 2) - localPlayer.z / 32;
				markMinimap(mapFlag, j2, l4); //764 1068
			}
			Rasterizer2D.drawBox((!isResized() ? xOffset + 127 : canvasWidth - 88), (!isResized() ? 83 : 80), 3, 3,
					0xffffff);
		}

		if (!isResized()) {

			mapArea[0].drawSprite(xOffset, 4);
			mapArea[5].drawSprite(xOffset, 0);
		} else
			mapArea[2].drawSprite(canvasWidth - 183, 0);

		compassImage.rotate(33, viewRotation, anIntArray1057, 256, anIntArray968,
				25, 4,
				(!isResized() ? 29 + xOffset : canvasWidth - 178), 33, 25);
//		if (drawOrbs)
//			loadAllOrbs(!isResized() ? xOffset : canvasWidth - 217);
		    // loadAllOrbs(isResized()? 0 : canvasWidth - 217);
		if (!isResized()) {
//			Sprite utilityOrb = new Sprite("orbs/utility_orb");
//			Sprite utilityOrbHover = new Sprite("orbs/utility_orb_hover");
//			Sprite donatorOrb = new Sprite("orbs/money_orb");
//			Sprite donatorOrbHover = new Sprite("orbs/money_orb_hover");

//			if (newHover) {//huh what is this
//				utilityOrbHover.drawSprite(222 - 2 + xOffset, 10 + 60);
//			} else {
//				utilityOrb.drawSprite(222 - 2+ xOffset, 10 + 60);
//			}
//
//			if (donatorHover) {
//				donatorOrbHover.drawSprite(215 - 2+ xOffset, 10 + 90);
//			} else {
//				donatorOrb.drawSprite(215 - 2+ xOffset, 10 + 90);
//			}

//			Sprite knowOrb = new Sprite("orbs/event_orb");
//			Sprite knowOrbHover = new Sprite("orbs/event_orb_hover");
//
//			Sprite wiki1 =  new Sprite("orbs/1882");
//			Sprite wiki2 = new Sprite("orbs/1883");


			// knowOrb.drawSprite(198 - 2, 17 + 110);
//			if (worldHover) {
//				knowOrbHover.drawSprite(202 - 2+ xOffset, 20 + 108);
//			} else {
//				knowOrb.drawSprite(202 - 2+ xOffset, 20 + 108);
//			}

		} else {


		}
        if (drawOrbs)
            loadAllOrbs(!isResized() ? xOffset : canvasWidth - 217);
		if (menuOpen) {
			drawMenu(0,0);
		}

	}

	public static AbstractRasterProvider rasterProvider;
	
	private boolean hpHover = false;

	private void loadHpOrb(int xOffset) {
		int yOff = !isResized() ? 0 : -5;
		int xOff = !isResized() ? 0 : -6;
		String cHP = RSInterface.interfaceCache[4016].message;
		String mHP = RSInterface.interfaceCache[4017].message;
		int currentHP = Integer.parseInt(cHP);
		int maxHP = Integer.parseInt(mHP);
		int health = (int) (((double) currentHP / (double) maxHP) * 100D);
		int poisonType = variousSettings[Configs.POISON_CONFIG];
		double percent = currentHP / (double) maxHP;

		int hover = 173;
		Sprite bg = cacheSprite3[hpHover ? hover : 172];
		Sprite fg = null;
		if (poisonType == 0) {
			fg = cacheSprite3[161];
		}
		if (poisonType == 1) {
			fg = cacheSprite3[162];
		}
		if (poisonType == 2) {
			fg = venomOrb;
		}
		bg.drawSprite(0 + xOffset - xOff, 41 - yOff);
		fg.drawSprite(27 + xOffset - xOff, 45 - yOff);
		if (getOrbFill(health) <= 26) {
			cacheSprite3[160].myHeight = getOrbFill(health);
		} else {
			cacheSprite3[160].myHeight = 26;
		}
		cacheSprite3[160].drawSprite(27 + xOffset - xOff, 45 - yOff);
		cacheSprite3[168].drawSprite(27 + xOffset - xOff, 45 - yOff);
		if (health > 1_000_000_000) {
			infinity.drawSprite(10 + xOffset - xOff, 59 - yOff);
		} else {
			smallText.drawCentered(getOrbTextColor(health), 15 + xOffset - xOff, "" + cHP, 67 - yOff, true);
		}
		if (percent < 1) {
			if (regenHealthStart > 0) {
				float difference = (int) (System.currentTimeMillis() - regenHealthStart);
				float angle = (difference / (rapidHeal ? REGEN_HEALTH_TIME_RAPID_HEAL : REGEN_HEALTH_TIME)) * 360.0f;
				//System.out.println("percent: "+percent+" angle: "+angle+" level:"+level);
				//if (setting.draw_orb_arc) {
					Rasterizer2D.draw_arc(24 + xOffset - xOff, 42 - yOff, 28, 28, 2, 90, -(int) angle, 0xff0000, 210, 0,
							false);
			//	}
				if (angle > 358.0f && currentHP != lastHp) {
					regenHealthStart = System.currentTimeMillis();
					restorehealth();
					lastHp = currentHP;
				}

			}
		}
	}
public int spellbookicon = 0;

	public boolean rapidHeal;
	private int lastHp = 0;


	private float REGEN_HEALTH_TIME = 60000.0f;
	private float REGEN_HEALTH_TIME_RAPID_HEAL = 30000.0f;
	public double fillHP;

	public Sprite worldMap1, worldMap2, worldMap3;

	private boolean specialHover;
	private int specialEnabled;
	public int specialAttack=100;


	private final float REGEN_SPEC_TIME = 60000.0f;
	private long regenHealthStart = 0;
	private long regenSpecStart = 0;

	private int lastSpec = 0;

	private void drawSpecialOrb(int xOffset) {
		boolean isFixed = !isResized();
		int yOff = isFixed ? -10 : 1;
		int xOff = isFixed ? 138 : 113;
		Sprite image = cacheSprite1[specialHover ? 8 : 7];
		Sprite fill = cacheSprite[specialEnabled == 0 ? 9 : 6];
		Sprite sword = cacheSprite[12];
		double percent = specialAttack  / (double) 100;
		image.drawSprite((isFixed ? 170 - xOff : 159 - xOff) + xOffset, isFixed ? 122 - yOff : 147 - yOff);
		int health = (int) (((double) specialAttack / (double) 100) * 100D);
		fill.drawSprite((isFixed ? 197 - xOff : 186 - xOff) + xOffset, isFixed ? 126 - yOff : 151 - yOff);
		//cacheSprite[6].height = (int) (26 * (1 - percent));
	//	cacheSprite[6].drawSprite((isFixed ? 197 - xOff : 186 - xOff) + xOffset, isFixed ? 126 - yOff : 151 - yOff);
//System.out.println("fill: "+health);

		if (getOrbFill(health) <= 26) {
			blackorb.myHeight = getOrbFill(health);
		} else {
			blackorb.myHeight = 26;
		}

		blackorb.drawSprite((isFixed ? 197 - xOff : 186 - xOff) + xOffset, isFixed ? 126 - yOff : 151 - yOff);


		if (specialEnabled == 1)
			sword.drawSprite((isFixed ? 198 - xOff : 187 - xOff) + xOffset, isFixed ? 126 - yOff : 151 - yOff);
		else
			sword.drawSprite((isFixed ? 197 - xOff : 186 - xOff) + xOffset, isFixed ? 126 - yOff : 151 - yOff);

		smallText.drawCentered(getOrbTextColor((int) (percent * 100)), (isFixed ? 185 - xOff : 173 - xOff) + xOffset, specialAttack + "", isFixed ? 148 - yOff : 172 - yOff, true);
		if (percent < 1) {
			if (regenSpecStart > 0) {
				float difference = (int) (System.currentTimeMillis() - regenSpecStart);
				float angle = (difference / REGEN_SPEC_TIME) * 360.0f;
					Rasterizer2D.draw_arc((isFixed ? 194 - xOff : 183 - xOff) + xOffset, isFixed ? 123 - yOff : 149 - yOff,
							28, 28, 2, 90, -(int) angle, 65535, 210, 0, false);
				if (angle > 358.0f && specialAttack != lastSpec)
					regenSpecStart = System.currentTimeMillis();
				lastSpec = specialAttack;
			}
		}

	}

	private void loadPrayerOrb(int xOffset) {
		int yOff = Configuration.osbuddyGameframe ? !isResized() ? 10 : 2
				: !isResized() ? 0 : -5;
		int xOff = Configuration.osbuddyGameframe ? !isResized() ? -1 : -7
				: !isResized() ? -1 : -7;
		Sprite bg = cacheSprite1[prayHover ? 8 : 7];
		Sprite fg = prayClicked ? new Sprite("Gameframe/newprayclicked") : cacheSprite1[1];
		bg.drawSprite(xOffset - xOff, 75 - yOff);
		fg.drawSprite(27 + xOffset - xOff, 79 - yOff);
		int level = Integer.parseInt(RSInterface.interfaceCache[4012].message.replaceAll("%", ""));
		int max = maxStats[5];
		double percent = level / (double) max;
		cacheSprite1[14].myHeight = (int) (26 * (1 - percent));
		cacheSprite1[14].drawSprite(27 + xOffset - xOff, 79 - yOff);
		if (percent <= .25) {
			cacheSprite1[10].drawSprite(30 + xOffset - xOff, 82 - yOff);
		} else {
			cacheSprite1[10].drawSprite(30 + xOffset - xOff, 82 - yOff);
		}
		if (level > 1_000_000_000) {
			infinity.drawSprite(11 + xOffset - xOff, 94 - yOff);
		} else {
			smallText.drawCentered(getOrbTextColor((int) (percent * 100)), 14 + xOffset - xOff, level + "", 101 - yOff, true);
		}


	}

	private void loadRunOrb(int xOffset) {
		int current = Integer.parseInt(RSInterface.interfaceCache[22539].message.replaceAll("%", ""));
		int yOff = Configuration.osbuddyGameframe ? !isResized() ? 15 : 5 : !isResized() ? 1 : -4;
		int xMinus = Configuration.osbuddyGameframe ? !isResized() ? 11 : 5 : !isResized() ? -1 : -6;
		Sprite bg = cacheSprite1[runHover ? 8 : 7];
		boolean running = anIntArray1045[173] == 1;
		Sprite fg = cacheSprite1[running ? 4 : 3];
		bg.drawSprite(10 + xOffset - xMinus, 109 - yOff);
		fg.drawSprite(37 + xOffset - xMinus, 113 - yOff);
		double percent = current / (double) 100;
		if(infrunenergy)
			cacheSprite1[14].myHeight = (int) (26 * (1 - 1));//myHeight is how you change the sprite height/width! not width or height!
		else
		cacheSprite1[14].myHeight = (int) (26 * (1 - percent));//myHeight is how you change the sprite height/width! not width or height!

		cacheSprite1[14].drawSprite(37 + xOffset - xMinus, 113 - yOff);

		if (percent <= .25) {
			cacheSprite1[running ? 12 : 11].drawSprite(43 + xOffset - xMinus, 117 - yOff);
		} else {
			cacheSprite1[running ? 12 : 11].drawSprite(43 + xOffset - xMinus, 117 - yOff);
		}
		if(infrunenergy)
			infinity.drawSprite(25 + xOffset - xMinus -6,  135 - yOff - 8);
		else
		smallText.drawCentered(getOrbTextColor((int) (percent * 100)), 25 + xOffset - xMinus, current + "", 135 - yOff,
				true);
	}

public boolean infrunenergy;
	public boolean cursesicon;
	private Sprite venomOrb;

	private void loadAllOrbs(int xOffset) {
		try {
			loadHpOrb(xOffset);
			loadPrayerOrb(xOffset);
			loadRunOrb(xOffset);
			drawSpecialOrb(xOffset);
		} catch (Exception ignored) {}
		if (drawExperienceCounter) {
			if (counterHover) {
				cacheSprite2[5].drawSprite(
						drawOrbs && !isResized() ? xOffset : canvasWidth - 211,
						!isResized() ? 21 : 25);
			} else {
				cacheSprite2[3].drawSprite(
						drawOrbs && !isResized() ? xOffset : canvasWidth - 211,
						!isResized() ? 21 : 25);
			}
		} else {
			if (counterHover) {
				cacheSprite2[4].drawSprite(
						drawOrbs && !isResized() ? xOffset : canvasWidth - 211,
						!isResized() ? 21 : 25);
			} else {
				cacheSprite2[2].drawSprite(
						drawOrbs && !isResized() ? xOffset : canvasWidth - 211,
						!isResized() ? 21 : 25);
			}
		}


		/*world map*/
		cacheSprite2[6].drawSprite(!isResized() ? xOffset + 196 : canvasWidth - 36, !isResized() ? 119 : 127);
		if(flashworldmap){
			if(update_tick % 40 < 20)
				zodianlogo.drawSprite(!isResized() ? xOffset + 200 : canvasWidth - 32, !isResized() ? 123 : 131);

		//	zodianlogo.drawSprite1(!isResized() ? xOffset + 200 : canvasWidth - 32, !isResized() ? 123 : 131, 10 + (int) (50 * Math.sin(update_tick / 5.0)));//fast flash

		} else {
			//cacheSprite2[worldHover ? 1 : 0].drawSprite(!isResized() ? xOffset + 200 : canvasWidth - 32, !isResized() ? 123 : 131);
			if (worldHover) {
				zodianlogo_h.drawSprite(!isResized() ? xOffset + 200 : canvasWidth - 32, !isResized() ? 123 : 131);
			} else {
				zodianlogo.drawSprite(!isResized() ? xOffset + 200 : canvasWidth - 32, !isResized() ? 123 : 131);
			}
		}





//System.out.println("w: "+canvasWidth+" and height: "+canvasHeight);
		if (wikiHover) {
			wiki2.drawSprite(!isResized() ? xOffset + 190 : canvasWidth - 45,  !isResized() ? 151 : 160);
		} else {
			wiki1.drawSprite(!isResized() ? xOffset + 190 : canvasWidth - 45,  !isResized() ? 151 : 160);

		}

		if (Configuration.osbuddyGameframe) {
			//loadSpecialOrb(xOffset);
		}
//		Rasterizer2D.drawAlphaBox(xOffset, 0, 1, 200, 0x332B16, 250);
	}

	Sprite wiki1 =  new Sprite("orbs/1882");
	Sprite wiki2 = new Sprite("orbs/1883");

	Sprite standardspellbook =  new Sprite("spellbooks/standard");
	Sprite ancientspellbook =  new Sprite("spellbooks/ancient");
	Sprite lunarspellbook =  new Sprite("spellbooks/lunar");


	Sprite blackorb = new Sprite("orbs/blackorb");
	public boolean runClicked = false;

	public int getOrbTextColor(int i) {
		if (i >= 75 && i <= 0x7fffffff)
			return 65280;
		if (i >= 50 && i <= 74)
			return 0xffff00;
		return i < 25 || i > 49 ? 0xff0000 : 0xFFB400;
	}

	private Sprite mapIcon9, mapIcon7, mapIcon8, mapIcon6, mapIcon5;

	public int getOrbFill(int statusInt) {
		if (statusInt <= Integer.MAX_VALUE && statusInt >= 97)
			return 0;
		else if (statusInt <= 96 && statusInt >= 93)
			return 1;
		else if (statusInt <= 92 && statusInt >= 89)
			return 2;
		else if (statusInt <= 88 && statusInt >= 85)
			return 3;
		else if (statusInt <= 84 && statusInt >= 81)
			return 4;
		else if (statusInt <= 80 && statusInt >= 77)
			return 5;
		else if (statusInt <= 76 && statusInt >= 73)
			return 6;
		else if (statusInt <= 72 && statusInt >= 69)
			return 7;
		else if (statusInt <= 68 && statusInt >= 65)
			return 8;
		else if (statusInt <= 64 && statusInt >= 61)
			return 9;
		else if (statusInt <= 60 && statusInt >= 57)
			return 10;
		else if (statusInt <= 56 && statusInt >= 53)
			return 11;
		else if (statusInt <= 52 && statusInt >= 49)
			return 12;
		else if (statusInt <= 48 && statusInt >= 45)
			return 13;
		else if (statusInt <= 44 && statusInt >= 41)
			return 14;
		else if (statusInt <= 40 && statusInt >= 37)
			return 15;
		else if (statusInt <= 36 && statusInt >= 33)
			return 16;
		else if (statusInt <= 32 && statusInt >= 29)
			return 17;
		else if (statusInt <= 28 && statusInt >= 25)
			return 18;
		else if (statusInt <= 24 && statusInt >= 21)
			return 19;
		else if (statusInt <= 20 && statusInt >= 17)
			return 20;
		else if (statusInt <= 16 && statusInt >= 13)
			return 21;
		else if (statusInt <= 12 && statusInt >= 9)
			return 22;
		else if (statusInt <= 8 && statusInt >= 7)
			return 23;
		else if (statusInt <= 6 && statusInt >= 5)
			return 24;
		else if (statusInt <= 4 && statusInt >= 3)
			return 25;
		else if (statusInt <= 2 && statusInt >= 1)
			return 26;
		else if (statusInt <= 0)
			return 27;
		return 0;
	}

	public Sprite xpSprite;
	public Sprite xpbg1 = new Sprite("487");
	public Sprite xpbg2 = new Sprite("488");

	private Queue<ExperienceDrop> experienceDrops = new LinkedList<>();

//	private void processExperienceCounter() {
//		if (update_tick % 1 <= 1 && !experienceDrops.isEmpty()) {
//			Collection<ExperienceDrop> remove = new ArrayList<>();
//			for (ExperienceDrop drop : experienceDrops) {
//				drop.pulse();
//				if (drop.getYPosition() == -1) {
//					experienceCounter += drop.getAmount();
//					remove.add(drop);
//				}
//			}
//			experienceDrops.removeAll(remove);
//		}
//
//		if (!drawExperienceCounter || openInterfaceID > -1) {
//			return;
//		}
//
//		for (ExperienceDrop drop : experienceDrops) {
//			String text = drop.toString();
//			int x = (!isResized() ? 507 : canvasWidth - 246)
//					- newSmallFont.getTextWidth(text);
//			int y = drop.getYPosition() - 15;
//			int transparency = 256;
//			newSmallFont.drawString(text, x, y, 0xFFFFFF, 0x000000, 256);
//
//			for (int skill : drop.getSkills()) {
//
//
//				Sprite sprite = smallXpSprites[skill];
//				x -= sprite.myWidth + 3;
//				y -= sprite.myHeight - 4;
//				sprite.drawAdvancedSprite(x, y, transparency);
//				y += sprite.myHeight - 4;
//			}
//		}
//
//		String experience = NumberFormat.getInstance().format(experienceCounter);
//
//		xpbg1.drawAdvancedSprite(!isResized() ? 395 : canvasWidth - 365, 6);
//		xpbg2.drawSprite(!isResized() ? 398 : canvasWidth - 363, 9);
//
//		newSmallFont.drawBasicString(experience, (!isResized() ? 510 : canvasWidth - 252)
//				- newSmallFont.getTextWidth(experience), 24, 0xFFFFFF, 0x000000);
//	}

	public boolean drawExperienceCounter = true;

	public void npcScreenPos(Entity entity, int i) {
		calcEntityScreenPos(entity.x, i, entity.z);
	}

	public void calcEntityScreenPos(int i, int j, int l) {

		if ((i < 128) || (l < 128) || (i > 13056) || (l > 13056)) {
			this.spriteDrawX = -1;
			this.spriteDrawY = -1;
			return;
		}
		int i1 = getCenterHeight(this.plane, l, i) - j; //getCenterHeight
		i -= this.xCameraPos;
		i1 -= this.zCameraPos;
		l -= this.yCameraPos;
		int j1 = Model.SINE[this.yCameraCurve];
		int k1 = Model.COSINE[this.yCameraCurve];
		int l1 = Model.SINE[this.xCameraCurve];
		int i2 = Model.COSINE[this.xCameraCurve];
		int j2 = l * l1 + i * i2 >> 16;
		l = l * i2 - i * l1 >> 16;
		i = j2;
		j2 = i1 * k1 - l * j1 >> 16;
		l = i1 * j1 + l * k1 >> 16;
		i1 = j2;
		if (l >= 50) {
			spriteDrawX = (Rasterizer3D.originViewX + i * Rasterizer3D.fieldOfView / l) + (!isResized() ? 4 : 0);
			spriteDrawY = (Rasterizer3D.originViewY + i1 * Rasterizer3D.fieldOfView / l) + (!isResized() ? 4 : 0);
		} else {
			spriteDrawX = -1;
			spriteDrawY = -1;
		}
	}

	public void add_loc_change(int end_tick, int id, int rot, int i1, int j1, int shape, int l1, int i2, int start_tick) {
		SpawnedObject spawnedObject = null;
		for (spawnedObject = (SpawnedObject) spawns
				.reverseGetFirst(); spawnedObject != null; spawnedObject = (SpawnedObject) spawns
				.reverseGetNext()) {
			if (spawnedObject.plane != l1 || spawnedObject.x != i2 || spawnedObject.y != j1
					|| spawnedObject.group != i1)
				continue;
			spawnedObject = spawnedObject;
			break;
		}

		if (spawnedObject == null) {
			spawnedObject = new SpawnedObject();
			spawnedObject.plane = l1;
			spawnedObject.group = i1;
			spawnedObject.x = i2;
			spawnedObject.y = j1;
			method89(spawnedObject);
			spawns.insertHead(spawnedObject);
		}
		spawnedObject.objectId = id;
		spawnedObject.type = shape;
		spawnedObject.orientation = rot;
		spawnedObject.delay = start_tick;
		spawnedObject.getLongetivity = end_tick;
	}

	private boolean cs1_selected(RSInterface class9) {
		if (class9.anIntArray245 == null)
			return false;
		for (int i = 0; i < class9.anIntArray245.length; i++) {
			int j = extractInterfaceValues(class9, i);
			int k = class9.anIntArray212[i];
			if (class9.anIntArray245[i] == 2) {
				if (j >= k)
					return false;
			} else if (class9.anIntArray245[i] == 3) {
				if (j <= k)
					return false;
			} else if (class9.anIntArray245[i] == 4) {
				if (j == k)
					return false;
			} else if (j != k)
				return false;
		}

		return true;
	}

	public void doFlamesDrawing() {



	}

	public void update_hires_players(Buffer stream) {
		int j = stream.readBits(8);
		if (j < playerCount) {
			for (int k = j; k < playerCount; k++)
				lowres_entity_list[lowres_entity_count++] = highres_player_list[k];

		}
		if (j > playerCount) {
			Signlink.reporterror(myUsername + " Too many players");
			throw new RuntimeException("eek");
		}
		playerCount = 0;
		for (int l = 0; l < j; l++) {
			int i1 = highres_player_list[l];
			Player player = players[i1];
			int j1 = stream.readBits(1);
			if (j1 == 0) {
				highres_player_list[playerCount++] = i1;
				player.last_update_tick = update_tick;
			} else {
				int k1 = stream.readBits(2);
				if (k1 == 0) {
					highres_player_list[playerCount++] = i1;
					player.last_update_tick = update_tick;
					entity_extended_info_list[anInt893++] = i1;
				} else if (k1 == 1) {
					highres_player_list[playerCount++] = i1;
					player.last_update_tick = update_tick;
					int l1 = stream.readBits(3);
					player.move_step_direction(MoveSpeed.WALK, l1);
					int j2 = stream.readBits(1);
					if (j2 == 1)
						entity_extended_info_list[anInt893++] = i1;
				} else if (k1 == 2) {
					highres_player_list[playerCount++] = i1;
					player.last_update_tick = update_tick;
					//UPDATED TO HAVE CRAWLING
					if(stream.readBits(1) == 1) {
						int i2 = stream.readBits(3);
						player.move_step_direction(MoveSpeed.RUN, i2);
						int k2 = stream.readBits(3);
						player.move_step_direction(MoveSpeed.RUN, k2);
					} else {
						int k2 = stream.readBits(3);
						player.move_step_direction(MoveSpeed.CRAWL, k2);
					}
					int l2 = stream.readBits(1);
					if (l2 == 1)
						entity_extended_info_list[anInt893++] = i1;
				} else if (k1 == 3)
					lowres_entity_list[lowres_entity_count++] = i1;
			}
		}
	}

	private Map<String, Sprite> screenImages;

	private final void createScreenImages() {
		if (screenImages != null) {
			return;
		}
		screenImages = new HashMap<>();
		screenImages.put("background", new Sprite("Login/background")); // not used
	}

	public Sprite loginAsset0;
	public Sprite loginAsset1;
	public Sprite loginAsset2;
	public Sprite loginAsset3;
	public Sprite loginAsset4;
	public String firstLoginMessage = "";
	public String secondLoginMessage = "";
	Image icon = null;
	String loginscreen_welcome_message_header = "Welcome to " + Configuration.CLIENT_TITLE;
	String loginscreen_message_footer = "Footer here";

	public boolean rememberMeHover;
	long lastLogin = 0;
	long loadDelay = 0;
	private Sprite loginScreenBackground;
	private Sprite loginScreenBackgroundCaptcha;
	private Sprite captchaExit;
	private Sprite captchaExitHover;
	private Sprite logo2021;

	private Sprite captcha;
	private IndexedImage titleButtonIndexedImage;
	private IndexedImage logologin;
	private AccountManager accountManager;

	public void drawLoginScreen(boolean flag) {
	//	int centerX = canvasWidth / 2, centerY = canvasHeight / 2;
		loginScreenBackground.drawAdvancedSprite(0,0);
		accountManager.processAccountDrawing();
//		if(System.currentTimeMillis() - lastLogin > 50) {
//			if (loginScreenState == LoginScreenState.CAPTCHA)
//				loginScreenBackgroundCaptcha.drawAdvancedSprite(0,0);
//			else
//				loginScreenBackground.drawAdvancedSprite(0,0);
//			logo2021.drawAdvancedSprite(386 - (logo2021.myWidth / 2),85 - (logo2021.myHeight / 2));
//		}

	if (Configuration.developerMode) { // so none of the buttons work.. the current login button is actually un der the word password
			newSmallFont.drawString("FPS: " + fps, 4, 12, Integer.MAX_VALUE, 0, 255);
			newSmallFont.drawString("Mouse: [" + MouseHandler.mouseX + ", " + MouseHandler.mouseY + "]", 4, 12 + 12, Integer.MAX_VALUE, 0, 255);
		}


		int xoffset = canvasWidth / 2;
		char c11 = '\u0168';
		int c = 765;
		int c1 = 513;
		char c11111 = '\310';
		//System.out.println(c);
		//int val = (int) c;
	//	int value = (int)c11111.charValue();
//	int a=Integer.parseInt(String.valueOf(c11));
	//	System.out.println(value);
	//	System.out.println(parseInt(String.valueOf(c11111)));
//		int c= Character.getNumericValue(c_orig);
//		int c1 = Character.getNumericValue(c1_orig);
		titleBoxIndexedImage.draw(202,171);//
		//int j = centerY - 40;
		logologin.draw(150 ,10);
		if (loginScreenState == LoginScreenState.ENTRY_SCREEN) {
		//	int i =  c1 / 2 + 80;
			//newBoldFont.drawCenteredString("loaded", i, c / 2, 0x75a9a9, 255);
			int i = c1 / 2 - 20;
			newBoldFont.drawCenteredString("Welcome to Runescape",c/2,i,0xffff00, 0);

			i += 30;
			int l = c / 2 - 80;
			int k1 = c1 / 2 + 20;
			titleButtonIndexedImage.draw(l - 73, k1 - 20);
			newBoldFont.drawCenteredString("New User",l,k1+5,0xffffff, 0);
			l = c / 2 + 80;

			titleButtonIndexedImage.draw(l - 73, k1 - 20);
			newBoldFont.drawCenteredString("Existing User",l,k1+5,0xffffff, 0);
//			if (firstLoginMessage.length() > 0) {
//				newBoldFont.drawCenteredString(firstLoginMessage, centerX - 3, j, 0xffffff, 0x191919, 255);
//				j += 30;
//			}
//
//			if (captcha != null)
//				captcha.drawAdvancedSpriteCentered(382, 446);
//
//			newBoldFont.drawString(
//				captchaInput + ((update_tick % 40 < 20) ? "|" : ""),
//				(canvasWidth / 2) - 119, canvasHeight / 2 + 8, 0xffffff, 0x191919, 255);
//
//
//			int exitX = 494;
//			int exitY = 236;
//			Sprite exit = captchaExit.isMousedOver(exitX, exitY, getMouseX(), getMouseY()) ? captchaExitHover : captchaExit;
//			exit.drawSprite(exitX, exitY);
		} else if (loginScreenState == LoginScreenState.LOGIN) {



			int j = c1 / 2 - 40;
			if (firstLoginMessage.length() > 0) {

				newBoldFont.drawCenteredString(firstLoginMessage,c/2,j-15,0xffff00, 255);
				newBoldFont.drawCenteredString(secondLoginMessage,c/2,j,0xffff00, 255);


				j += 30;
			} else {
				newBoldFont.drawCenteredString(secondLoginMessage,c/2,j-7,0xffff00, 255);

				j += 30;
			}
			//			newSmallFont.drawString(killer, x, y, 0xFFFFFF, 0x000000, 256);
			newBoldFont.drawString("Login:",  c / 2 - 100, j-10, 0xffffff,0x000000, 255);

			newBoldFont.drawString(myUsername + (loginScreenCursorPos == 0 & update_tick % 40 < 20 ? "@yel@|" : ""),  c / 2 - 60, j-10, 0xffffff, 0x000000, 256);
			j += 18;
			newRegularFont.drawString(StringUtils.passwordAsterisks(myPassword) + (((this.loginScreenCursorPos == 1 ? 1 : 0) & (update_tick % 40 < 20 ? 1 : 0)) != 0 ? "|" : ""), c / 2 - 30, j-10, 0xffffff, 0x000000, 256);
			newBoldFont.drawString("Password:",  c / 2 - 100, j-10, 0xffffff,0x000000, 255);
			j += 15;
			if (!flag) {
				int i1 = c / 2 - 80;
				int l1 = c1 / 2 + 50;
				titleButtonIndexedImage.draw(i1 - 73, l1 - 20);
				newBoldFont.drawCenteredString("Login",i1,l1+5,0xffffff, 0);
				i1 = c / 2 + 80;

				titleButtonIndexedImage.draw(i1 - 73, l1 - 20);
				newBoldFont.drawCenteredString("Cancel",i1,l1+5,0xffffff, 0);
			}
			int rememberhoverx = 140;
			int rememberhovery = 110;
			////			if (!informationFile.isUsernameRemembered()) {
			////				if (!rememberMeHover) {
			////					loginAsset0.drawSprite(230, 427 + rememberYOffset);
			////				} else {
			////					loginAsset1.drawSprite(230, 427 + rememberYOffset);
			////				}
			////			} else {
			////				if (!rememberMeHover) {
			////					loginAsset2.drawSprite(230, 427 + rememberYOffset);
			////				} else {
			////					loginAsset3.drawSprite(230, 427 + rememberYOffset);
			////				}
			////			}
			////
//			if (!informationFile.isUsernameRemembered()) {
//				//spriteCache.get(546).drawSprite(rememberhoverx, rememberhovery);
//				loginAsset0.drawSprite(rememberhoverx, rememberhovery);
//			} else {
//				loginAsset0.drawSprite(rememberhoverx, rememberhovery);
//			}
//		newSmallFont.drawCenteredString("@yel@Save login",210,120,0xffffff,0xffffff, 255);
			saveButton.drawSprite(337,334);

		} else if (loginScreenState == LoginScreenState.INFO) {
			//newRegularFont.drawCenteredString("Cancel",c/2,c1/2-60,0xffff00, 255);
			int k = c1 / 2 - 35;
			newRegularFont.drawCenteredString("You don't need to make an account on the forums",c/2,k,0xffffff, 0);
			k += 15;
			newRegularFont.drawCenteredString("to login. Join discord instead!",c/2,k,0xffffff, 0);
			k += 15;
			newRegularFont.drawCenteredString("",c/2,k,0xffffff, 0);
			k += 15;
			newRegularFont.drawCenteredString("",c/2,k,0xffffff, 0);
			k += 15;
			int j1 = c / 2;
			int i2 = c1 / 2 + 50;

			titleButtonIndexedImage.draw(j1 - 73, i2 - 20);

			newRegularFont.drawCenteredString("Cancel",j1,i2+5,0xffffff, 0);
		}

	}

	private void drawFlames() {
		try {
			long l = System.currentTimeMillis();
			int i = 0;
			int j = 20;
			while (aBoolean831) {
				anInt1208++;
				if (++i > 10) {
					long l1 = System.currentTimeMillis();
					int k = (int) (l1 - l) / 10 - j;
					j = 40 - k;
					if (j < 5)
						j = 5;
					i = 0;
					l = l1;
				}
				try {
					Thread.sleep(j);
				} catch (Exception _ex) {
				}
			}
		} catch (Exception _ex) {
			drawingFlames = false;
		}
	}

	public void method137(Buffer stream, int j) {
		if (j == 84) {
			int k = stream.get_unsignedbyte();
			int j3 = anInt1268 + (k >> 4 & 7);
			int i6 = anInt1269 + (k & 7);
			int l8 = stream.readUShort();
			int k11 = stream.readUShort();
			int l13 = stream.readUShort();// edit
			if (j3 >= 0 && i6 >= 0 && j3 < 104 && i6 < 104) {
				Deque class19_1 = groundItems[plane][j3][i6];
				if (class19_1 != null) {
					for (Item class30_sub2_sub4_sub2_3 = (Item) class19_1
							.reverseGetFirst(); class30_sub2_sub4_sub2_3 != null; class30_sub2_sub4_sub2_3 = (Item) class19_1
							.reverseGetNext()) {
						if (class30_sub2_sub4_sub2_3.ID != (l8 & 0x7fff) || class30_sub2_sub4_sub2_3.itemCount != k11)
							continue;
						class30_sub2_sub4_sub2_3.itemCount = l13;
						break;
					}

					updateGroundItems(j3, i6);
				}
			}
			return;
		}
		if (j == 105) {
			int l = stream.get_unsignedbyte();
			int k3 = anInt1268 + (l >> 4 & 7);
			int j6 = anInt1269 + (l & 7);
			int i9 = stream.readUShort();
			int l11 = stream.get_unsignedbyte();
			int i14 = l11 >> 4 & 0xf;
			int i16 = l11 & 7;
			if (localPlayer.waypoint_x[0] >= k3 - i14 && localPlayer.waypoint_x[0] <= k3 + i14 && localPlayer.waypoint_z[0] >= j6 - i14
					&& localPlayer.waypoint_z[0] <= j6 + i14 && soundEffectVolume != 0 && aBoolean848 && !Client.lowMem
					&& soundCount < 50) {
				sound[soundCount] = i9;
				soundType[soundCount] = i16;
				soundDelay[soundCount] = Sounds.anIntArray326[i9];
				// aClass26Array1468[soundCount] = null;
				soundCount++;
			}
		}
		if (j == 215) {
			int i1 = stream.readUShortA();
			int l3 = stream.method428();
			int k6 = anInt1268 + (l3 >> 4 & 7);
			int j9 = anInt1269 + (l3 & 7);
			int i12 = stream.readUShortA();
			int j14 = stream.readUShort();
			if (k6 >= 0 && j9 >= 0 && k6 < 104 && j9 < 104 && i12 != unknownInt10) {
				Item class30_sub2_sub4_sub2_2 = new Item();
				class30_sub2_sub4_sub2_2.ID = i1;
				class30_sub2_sub4_sub2_2.itemCount = j14;
				if (groundItems[plane][k6][j9] == null)
					groundItems[plane][k6][j9] = new Deque();
				groundItems[plane][k6][j9].insertHead(class30_sub2_sub4_sub2_2);
				updateGroundItems(k6, j9);
			}
			return;
		}
		if (j == 156) {
			int j1 = stream.method426();
			int i4 = anInt1268 + (j1 >> 4 & 7);
			int l6 = anInt1269 + (j1 & 7);
			int k9 = stream.readUShort();
			if (i4 >= 0 && l6 >= 0 && i4 < 104 && l6 < 104) {
				Deque class19 = groundItems[plane][i4][l6];
				if (class19 != null) {
					for (Item item = (Item) class19.reverseGetFirst(); item != null; item = (Item) class19
							.reverseGetNext()) {
						if (item.ID != k9)
							continue;
						item.unlink();
						break;
					}

					if (class19.reverseGetFirst() == null)
						groundItems[plane][i4][l6] = null;
					updateGroundItems(i4, l6);
				}
			}
			return;
		}
		if (j == 160) {
			int k1 = stream.method428();
			int j4 = anInt1268 + (k1 >> 4 & 7);
			int i7 = anInt1269 + (k1 & 7);
			int l9 = stream.method428();
			int objectType = l9 >> 2;
			int orientation = l9 & 3;
			int objectTypeGroup = objectTypes[objectType];
			int animationId = stream.readUShortA();
			if (j4 >= 0 && i7 >= 0 && j4 < 103 && i7 < 103) {
				int j18 = tileHeights[plane][j4][i7];
				int i19 = tileHeights[plane][j4 + 1][i7];
				int l19 = tileHeights[plane][j4 + 1][i7 + 1];
				int k20 = tileHeights[plane][j4][i7 + 1];
				if (objectTypeGroup == 0) {
					WallObject class10 = scene.getWallObject(plane, j4, i7);
					if (class10 != null) {
						int k21 = ObjectKeyUtil.getObjectId(class10.uid);
						if (objectType == 2) {
							class10.renderable1 = new SceneObject(k21, 4 + orientation, 2, i19, l19, j18, k20, animationId,
									false);
							class10.renderable2 = new SceneObject(k21, orientation + 1 & 3, 2, i19, l19, j18, k20,
									animationId, false);
						} else {
							class10.renderable1 = new SceneObject(k21, orientation, objectType, i19, l19, j18, k20, animationId,
									false);
						}
					}
				}
				if (objectTypeGroup == 1) {
					WallDecoration class26 = scene.getWallDecoration(j4, i7, plane);
					if (class26 != null)
						class26.renderable = new SceneObject(ObjectKeyUtil.getObjectId(class26.uid), 0, 4, i19, l19,
								j18, k20, animationId, false);
				}
				if (objectTypeGroup == 2) {
					GameObject class28 = scene.getGameObject(j4, i7, plane);
					if (objectType == 11)
						objectType = 10;
					if (class28 != null) {
						class28.renderable = new SceneObject(ObjectKeyUtil.getObjectId(class28.uid), orientation, objectType, i19,
								l19, j18, k20, animationId, false);
					}
				}
				if (objectTypeGroup == 3) {
					GroundDecoration class49 = scene.getGroundDecoration(i7, j4, plane);
					if (class49 != null)
						class49.renderable = new SceneObject(ObjectKeyUtil.getObjectId(class49.uid), orientation, 22, i19,
								l19, j18, k20, animationId, false);
				}
			}
			return;
		}
		if (j == 147) {
			int l1 = stream.method428();
			int x = anInt1268 + (l1 >> 4 & 7);
			int z = anInt1269 + (l1 & 7);
			int i10 = stream.readUShort();
			byte max_x = stream.method430();
			int start_tick = stream.method434();
			byte max_z = stream.method429();
			int end_tick = stream.readUShort();
			int k18 = stream.method428();
			int shape = k18 >> 2;
			int rotation = k18 & 3;
			int layer = objectTypes[shape];
			byte min_x = stream.readSignedByte();
			int l21 = stream.readUShort();
			byte min_z = stream.method429();
			Player player;
			if (i10 == unknownInt10)
				player = localPlayer;
			else
				player = players[i10];
			if (player != null) {
				ObjectDefinition class46 = ObjectDefinition.lookup(l21);
				int i22 = tileHeights[plane][x][z];
				int j22 = tileHeights[plane][x + 1][z];
				int k22 = tileHeights[plane][x + 1][z + 1];
				int l22 = tileHeights[plane][x][z + 1];
				Model model = class46.modelAt(shape, rotation, i22, j22, k22, l22, -1, null);
				if (model != null) {
					add_loc_change(end_tick + 1, -1, 0, layer, z, 0, plane, x, start_tick + 1);
					player.mergelocanim_loop_start = start_tick + update_tick;
					player.mergelocanim_loop_stop = end_tick + update_tick;
					player.mergeloc_model = model;
					int width = class46.length;
					int length = class46.width;
					if (rotation == 1 || rotation == 3) {
						width = class46.width;
						length = class46.length;
					}
					player.loc_offset_x = x * 128 + width * 64;
					player.loc_offset_z = z * 128 + length * 64;
					player.loc_offset_y = getCenterHeight(plane, player.loc_offset_z, player.loc_offset_x);
					if (min_x > max_x) {
						byte byte4 = min_x;
						min_x = max_x;
						max_x = byte4;
					}
					if (min_z > max_z) {
						byte byte5 = min_z;
						min_z = max_z;
						max_z = byte5;
					}
					player.min_x = x + min_x;
					player.max_x = x + max_x;
					player.min_z = z + min_z;
					player.max_z = z + max_z;
				}
			}
		}
		if (j == 151) {
			int i2 = stream.method426();
			int l4 = anInt1268 + (i2 >> 4 & 7);
			int k7 = anInt1269 + (i2 & 7);
			int j10 = stream.method434();
			int k12 = stream.method428();
			int i15 = k12 >> 2;
			int k16 = k12 & 3;
			int l17 = objectTypes[i15];
			if (l4 >= 0 && k7 >= 0 && l4 < 104 && k7 < 104)
				add_loc_change(-1, j10, k16, l17, k7, i15, plane, l4, 0);
			return;
		}
		if (j == 4) {
			int j2 = stream.get_unsignedbyte();
			int i5 = anInt1268 + (j2 >> 4 & 7);
			int l7 = anInt1269 + (j2 & 7);
			int k10 = stream.readUShort();
			int l12 = stream.get_unsignedbyte();
			int j15 = stream.readUShort();
			if (i5 >= 0 && l7 >= 0 && i5 < 104 && l7 < 104) {
				i5 = i5 * 128 + 64;
				l7 = l7 * 128 + 64;
				StillGraphic class30_sub2_sub4_sub3 = new StillGraphic(plane, update_tick, j15, k10,
						getCenterHeight(plane, l7, i5) - l12, l7, i5);
				incompleteAnimables.insertHead(class30_sub2_sub4_sub3);
			}
			return;
		}
		if (j == 44) {
			int k2 = stream.method436();
			int j5 = stream.method439();
			int i8 = stream.get_unsignedbyte();
			int l10 = anInt1268 + (i8 >> 4 & 7);
			int i13 = anInt1269 + (i8 & 7);
			if (l10 >= 0 && i13 >= 0 && l10 < 104 && i13 < 104) {
				Item class30_sub2_sub4_sub2_1 = new Item();
				class30_sub2_sub4_sub2_1.ID = k2;
				class30_sub2_sub4_sub2_1.itemCount = j5;
				if (groundItems[plane][l10][i13] == null)
					groundItems[plane][l10][i13] = new Deque();
				groundItems[plane][l10][i13].insertHead(class30_sub2_sub4_sub2_1);
				updateGroundItems(l10, i13);
			}
			return;
		}
		if (j == 101) {
			int l2 = stream.method427();
			int k5 = l2 >> 2;
			int j8 = l2 & 3;
			int i11 = objectTypes[k5];
			int j13 = stream.get_unsignedbyte();
			int k15 = anInt1268 + (j13 >> 4 & 7);
			int l16 = anInt1269 + (j13 & 7);
			if (k15 >= 0 && l16 >= 0 && k15 < 104 && l16 < 104)
				add_loc_change(-1, -1, j8, i11, l16, k5, plane, k15, 0);
			return;
		}
		if (j == 117) {
			int i3 = stream.get_unsignedbyte();
			int l5 = anInt1268 + (i3 >> 4 & 7);
			int k8 = anInt1269 + (i3 & 7);
			int j11 = l5 + stream.readSignedByte();
			int k13 = k8 + stream.readSignedByte();
			int l15 = stream.readSignedWord();
			int i17 = stream.readUShort();
			int i18 = stream.get_unsignedbyte() * 4;
			int l18 = stream.get_unsignedbyte() * 4;
			int k19 = stream.readUShort();
			int j20 = stream.readUShort();
			int i21 = stream.get_unsignedbyte();
			int j21 = stream.get_unsignedbyte();
			if (l5 >= 0 && k8 >= 0 && l5 < 104 && k8 < 104 && j11 >= 0 && k13 >= 0 && j11 < 104 && k13 < 104
					&& i17 != 65535) {
				l5 = l5 * 128 + 64;
				k8 = k8 * 128 + 64;
				j11 = j11 * 128 + 64;
				k13 = k13 * 128 + 64;
				Projectile class30_sub2_sub4_sub4 = new Projectile(i21, l18, k19 + update_tick, j20 + update_tick,
						j21, plane, getCenterHeight(plane, k8, l5) - i18, k8, l5, l15, i17);
				class30_sub2_sub4_sub4.set_destination(k19 + update_tick, k13, getCenterHeight(plane, k13, j11) - l18, j11);
				projectiles.insertHead(class30_sub2_sub4_sub4);
			}
		}
	}

	public void update_high_resolution_npcs(Buffer stream) {
		try {
			stream.initBitAccess();
			int localNpcListSize = stream.readBits(8);
			if (localNpcListSize < npcCount) {
				for (int l = localNpcListSize; l < npcCount; l++)
					lowres_entity_list[lowres_entity_count++] = highres_npc_list[l];

			}
			if (localNpcListSize > npcCount) {
				Signlink.reporterror(myUsername + " Too many npcs");
				throw new RuntimeException("eek");
			}
			npcCount = 0;
			for (int i1 = 0; i1 < localNpcListSize; i1++) {
				int npcIndex = highres_npc_list[i1];
				Npc npc = npcs[npcIndex];
				int k1 = stream.readBits(1);
				if (k1 == 0) {
					highres_npc_list[npcCount++] = npcIndex;
					npc.last_update_tick = update_tick;
				} else {
					int l1 = stream.readBits(2);
					if (l1 == 0) {
						highres_npc_list[npcCount++] = npcIndex;
						npc.last_update_tick = update_tick;
						entity_extended_info_list[anInt893++] = npcIndex;
					} else if (l1 == 1) {
						highres_npc_list[npcCount++] = npcIndex;
						npc.last_update_tick = update_tick;
						int i2 = stream.readBits(3);
						npc.move_step_direction(MoveSpeed.WALK, i2);
						int k2 = stream.readBits(1);
						if (k2 == 1)
							entity_extended_info_list[anInt893++] = npcIndex;
					} else if (l1 == 2) {
						highres_npc_list[npcCount++] = npcIndex;
						npc.last_update_tick = update_tick;
						//UPDATED NPC UPDATING TO INCLUDE CRAWLING!
						int dir;
						if (stream.readBits(1) == 1) {
							dir = stream.readBits(3);
							npc.move_step_direction(MoveSpeed.RUN, dir);
							dir = stream.readBits(3);
							npc.move_step_direction(MoveSpeed.RUN, dir);
						} else {
							dir = stream.readBits(3);
							npc.move_step_direction(MoveSpeed.CRAWL, dir);
						}
						int info = stream.readBits(1);
						if (info == 1)
							entity_extended_info_list[anInt893++] = npcIndex;
					} else if (l1 == 3)
						lowres_entity_list[lowres_entity_count++] = npcIndex;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean clickInRegion(int x1, int y1, int x2, int y2) {
		if (MouseHandler.saveClickX >= x1 && MouseHandler.saveClickX <= x2 && MouseHandler.saveClickY >= y1 && MouseHandler.saveClickY <= y2) {
			return true;
		}
		return false;
	}

	public boolean mouseInRegion(int x1, int y1, int x2, int y2) {
		if (MouseHandler.mouseX >= x1 && MouseHandler.mouseX <= x2 && MouseHandler.mouseY >= y1 && MouseHandler.mouseY <= y2) {
			return true;
		}
		return false;
	}

	public boolean rememberPasswordHover;

	@SuppressWarnings("static-access")
	private void processLoginScreenInput() {
		try {
			accountManager.processAccountInput();
		} catch (IOException e) {
			e.printStackTrace();
		}




		if (loginScreenState == LoginScreenState.ENTRY_SCREEN) {
			int i = canvasWidth  / 2 - 80;
			int l = canvasHeight / 2 + 20;
			l += 20;
//			if (super.click_type == 1 && super.click_x >= i - 75 && super.cursor_x <= i + 75 && super.click_y >= l - 20 && super.click_y <= l + 20) {
//				loginScreenState = 3;
//				loginScreenCursorPos = 0;
//			}
			i = canvasWidth  / 2 + 80;
			if (MouseHandler.clickMode3  == 1 && MouseHandler.saveClickX  >= i - 75 && MouseHandler.saveClickX <= i + 75 && MouseHandler.saveClickY >= l - 20 && MouseHandler.saveClickY  <= l + 20) {
				firstLoginMessage = "";
				secondLoginMessage = "Enter your username & password.";
				loginScreenState = LoginScreenState.LOGIN;
				loginScreenCursorPos = 0;
//                System.out.println((i-75)+" "+(l-20));
//                System.out.println(super.click_x+"  "+super.click_y);
			}
			if (MouseHandler.clickMode3  == 1 && MouseHandler.saveClickX  >= 228 && MouseHandler.saveClickX <=376 && MouseHandler.saveClickY >= 256 && MouseHandler.saveClickY  <= 297) {
			//	firstLoginMessage = "";
				//secondLoginMessage = "Enter your username & password.";
				loginScreenState = LoginScreenState.INFO;
			//	loginScreenCursorPos = 0;
//                System.out.println((i-75)+" "+(l-20));
//                System.out.println(super.click_x+"  "+super.click_y);
			}
//			if (loginScreenState == 3) {
//				int k = super.myWidth / 2;
//				int j1 = super.myHeight / 2 + 50;
//				j1 += 20;
//				if (super.click_type == 1 && super.click_x >= k - 75 && super.click_x <= k + 75 && super.click_y >= j1 - 20 && super.click_y <= j1 + 20)
//					loginScreenState = 0;
//			}
		} else {
			if (loginScreenState == LoginScreenState.INFO) {
				if (MouseHandler.clickMode3  == 1&& MouseHandler.saveClickX  >= 311 && MouseHandler.saveClickX  <= 452 && MouseHandler.saveClickY>= 285 && MouseHandler.saveClickY<325) {
					loginScreenState = LoginScreenState.ENTRY_SCREEN;
				}
			}
			if (loginScreenState == LoginScreenState.LOGIN) {
				int j = canvasHeight / 2 - 40;
				j += 30;
				j += 20;
				if (MouseHandler.clickMode3  == 1 && MouseHandler.saveClickY >= 225 && MouseHandler.saveClickY < 240) {
					loginScreenCursorPos = 0;
				}
				j += 15;
				if (MouseHandler.clickMode3  == 1 && MouseHandler.saveClickY>= 241 && MouseHandler.saveClickY< 254) {
					loginScreenCursorPos = 1;
				}
				j += 15;
				int i1 = canvasWidth / 2 - 80;
				int k1 = canvasHeight  / 2 + 50;
				int i22 = i1;
				int k22 = k1;


				k1 += 20;
				if (MouseHandler.clickMode3  == 1 && MouseHandler.saveClickX  >= 227 && MouseHandler.saveClickX  <= 374 && MouseHandler.saveClickY>=286 && MouseHandler.saveClickY<= 320) {
					loginFailures = 0;
					//  System.out.println(i22+" "+k22);
					// System.out.println("click x: "+super.click_x+"  click y: "+super.click_y);

					login(myUsername, myPassword, false);
					if (loggedIn) {
						return;
					}
				}
				if (MouseHandler.clickMode3  == 1 && MouseHandler.saveClickX  >= 340 && MouseHandler.saveClickX <= 358
						&& MouseHandler.saveClickY >= 280 && MouseHandler.saveClickY<= 293) {
					informationFile.setUsernameRemembered(!informationFile.isUsernameRemembered());
					if (informationFile.isUsernameRemembered()) {
						informationFile.setStoredUsername(myUsername);
						informationFile.setStoredPassword(myPassword);
					}
				}

				if (MouseHandler.clickMode3  == 1 && MouseHandler.saveClickX >= 3 && MouseHandler.saveClickX  <= 424 && MouseHandler.saveClickY >= 334 && MouseHandler.saveClickY <= 344) {
					try {
						accountManager.addAccount(myUsername, myPassword);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				i1 = canvasWidth / 2 + 80;
				if (MouseHandler.clickMode3  == 1 && MouseHandler.saveClickX  >= i1 - 75 &&MouseHandler.saveClickX  <= i1 + 75 && MouseHandler.saveClickY>= k1 - 20 && MouseHandler.saveClickY <= k1 + 20) {
					loginScreenState = LoginScreenState.ENTRY_SCREEN;
					// myUsername = "";
					// myPassword = "";
				}
				do {
					int l1 = KeyHandler.instance.readChar();
					if (l1 == -1)
						break;
					boolean flag1 = false;
					for (int i2 = 0; i2 < validUserPassChars.length(); i2++) {
						if (l1 != validUserPassChars.charAt(i2))
							continue;
						flag1 = true;
						break;
					}

					if (loginScreenState == LoginScreenState.CAPTCHA) {
						synchronized (CAPTCHA_LOCK) {
							captchaInput = loginScreenInput(captchaInput, l1, flag1, 12,
									null,
									() -> login(myUsername, getPassword(), false)
							);
						}
					} else if (loginScreenState == LoginScreenState.LOGIN) {
						if (loginScreenCursorPos == 0) {
							myUsername = loginScreenInput(myUsername, l1, flag1, 12,
									() -> loginScreenCursorPos = 1,
									() -> loginScreenCursorPos = 1
							);
						} else if (loginScreenCursorPos == 1) {
							myPassword = loginScreenInput(myPassword, l1, flag1, 15,
									() -> loginScreenCursorPos = 0,
									() -> login(myUsername, getPassword(), false)
							);
						}
					}
				} while (true);
			}
		}
	}

	private String loginScreenInput(String current, int l1, boolean flag1, int maxWidth, Runnable tab, Runnable enter) {
		if (l1 == 8 && current.length() > 0)
			current = current.substring(0, current.length() - 1);
		if (l1 == 9) {
			if (tab != null)
				tab.run();
		} else if (l1 == 10 || l1 == 13) {
			if (enter != null)
				enter.run();
			return current;
		}
		if (flag1)
			current += (char) l1;
		if (current.length() > maxWidth)
			current = current.substring(0, maxWidth);
		return current;
	}

	private void markMinimap(Sprite sprite, int xPosition, int yPosition) {
		int k = viewRotation + minimapRotation & 0x7ff;
		int boundry = xPosition * xPosition + yPosition * yPosition;
		if (boundry > 5750)
			return;
		int i1 = Model.SINE[k];
		int j1 = Model.COSINE[k];
		i1 = (i1 * 256) / (minimapZoom + 256);
		j1 = (j1 * 256) / (minimapZoom + 256);
		int k1 = yPosition * i1 + xPosition * j1 >> 16;
		int l1 = yPosition * j1 - xPosition * i1 >> 16;

		try {
			int xOffset = !isResized() ? 516 : 0;
			if (!isResized())
				sprite.drawSprite(((97 + k1) - sprite.maxWidth / 2) + 30 + xOffset, 83 - l1 - sprite.maxHeight / 2 - 4 + 5);
			else
				sprite.drawSprite(((77 + k1) - sprite.maxWidth / 2) + 4 + (canvasWidth - 167) + xOffset,
						85 - l1 - sprite.maxHeight / 2 - 4);
		} catch (Exception e) {
			// e.printStackTrace();
		}
	}

    private void removeObject(int y, int z, int k, int l, int x, int group, int previousId) {
        if (x >= 1 && y >= 1 && x <= 102 && y <= 102) {
            if (lowMem && z != plane)
                return;
            long key = 0L;
            if (group == 0)
                key = scene.getWallObjectUid(z, x, y);
            if (group == 1)
                key = scene.getWallDecorationUid(z, x, y);
            if (group == 2)
                key = scene.getGameObjectUid(z, x, y);
            if (group == 3)
                key = scene.getGroundDecorationUid(z, x, y);
            if (key != 0L) {

                int id = ObjectKeyUtil.getObjectId(key);
                int objectType = ObjectKeyUtil.getObjectType(key);
                int orientation = ObjectKeyUtil.getObjectOrientation(key);

                if (group == 0) {
                    scene.removeWallObject(x, z, y);
                    ObjectDefinition objectDef = ObjectDefinition.lookup(id);
                    if (objectDef.blockWalk)
                        collisionMaps[z].removeObject(orientation, objectType,
                                objectDef.blocksProjectile, x, y);
                }
                if (group == 1)
                    scene.removeWallDecoration(y, z, x);
                if (group == 2) {
                    scene.removeTiledObject(z, x, y);
                    ObjectDefinition objectDef = ObjectDefinition.lookup(id);
                    if (x + objectDef.length > 103 || y + objectDef.length > 103
                            || x + objectDef.width > 103
                            || y + objectDef.width > 103)
                        return;
                    if (objectDef.blockWalk)
                        collisionMaps[z].removeObject(orientation, objectDef.length, x,
                                y, objectDef.width, objectDef.blocksProjectile);
                }
                if (group == 3) {
                    scene.removeGroundDecoration(z, y, x);
                    ObjectDefinition objectDef = ObjectDefinition.lookup(id);
                    if (objectDef.blockWalk && objectDef.isInteractive)
                        collisionMaps[z].removeFloorDecoration(y, x);
                }
            }
            if (previousId >= 0) {
                int plane = z;
                if (plane < 3 && (tileFlags[1][x][y] & 2) == 2)
                    plane++;
                MapRegion.placeObject(scene, k, y, l, plane, collisionMaps[z], tileHeights,
                        x, previousId, z);
            }
        }
    }

	public void updatePlayers(int i, Buffer stream) {
		lowres_entity_count = 0;
		anInt893 = 0;
		update_self_player(stream);
		update_hires_players(stream);
		update_lowres_players(stream, i);
		update_player_info(stream);
		for (int k = 0; k < lowres_entity_count; k++) {
			int l = lowres_entity_list[k];
			if (players[l].last_update_tick != update_tick)
				players[l] = null;
		}

		if (stream.currentPosition != i) {
			Signlink.reporterror("Error packet size mismatch in getplayer pos:" + stream.currentPosition + " psize:" + i);
			throw new RuntimeException("eek");
		}
		for (int i1 = 0; i1 < playerCount; i1++)
			if (players[highres_player_list[i1]] == null) {
				Signlink.reporterror(myUsername + " null entry in pl list - pos:" + i1 + " size:" + playerCount);
				throw new RuntimeException("eek");
			}

	}

	private void setCameraPos(int j, int k, int l, int i1, int j1, int k1) {
		int l1 = 2048 - k & 0x7ff;
		int i2 = 2048 - j1 & 0x7ff;
		int j2 = 0;
		int k2 = 0;
		int l2 = j;
		if (l1 != 0) {
			int i3 = Model.SINE[l1];
			int k3 = Model.COSINE[l1];
			int i4 = k2 * k3 - (int) (l2 * i3) >> 16;
			l2 = k2 * i3 + (int) (l2 * k3) >> 16;
			k2 = i4;
		}
		if (i2 != 0) {
			int j3 = Model.SINE[i2];
			int l3 = Model.COSINE[i2];
			int j4 = (int) (l2 * j3) + j2 * l3 >> 16;
			l2 = (int) (l2 * l3) - j2 * j3 >> 16;
			j2 = j4;
		}
		xCameraPos = l - j2;
		zCameraPos = i1 - k2;
		yCameraPos = k1 - l2;
		yCameraCurve = k;
		xCameraCurve = j1;
		onCameraPitchChanged(k);
	}

	public void updateStrings(String str, int i) {
		switch (i) {
			case 22499:
				sendFrame126(currentStats[5]+"/"+maxStats[5], 22499);
				break;
			case 49020:
				achievementCutoff = Integer.parseInt(str);
				break;
//			case 1675:
//				sendFrame126(str, 17508);
//				break;// Stab
//			case 1676:
//				sendFrame126(str, 17509);
//				break;// Slash
//			case 1678:
//				sendFrame126(str, 17511);
//				break;// Magic
//			case 1679:
//				sendFrame126(str, 17512);
//				break;// Range
//			case 1680:
//				sendFrame126(str, 17513);
//				break;// Stab
//			case 1681:
//				sendFrame126(str, 17514);
//				break;// Slash
//			case 1682:
//				sendFrame126(str, 17515);
//				break;// Crush
//			case 1683:
//				sendFrame126(str, 17516);
//				break;// Magic
//			case 1684:
//				sendFrame126(str, 17517);
//				break;// Range
//			case 1686:
//				sendFrame126(str, 17518);
//				break;// Strength
//			case 1687:
//				sendFrame126(str, 17519);
//				break;// Prayer
		}
	}

	public void sendFrame126(String str, int i) {
  //   System.out.println("str: "+str+" and i: "+i);
		RSInterface component = RSInterface.interfaceCache[i];
		if (component != null) {
			component.message = str;
			if (component.type == 4 && component.atActionType == 1) {
				component.hoverText = str;
			}
//			if (component.parentID == tabInterfaceIDs[tabID]){
//			//	pushMessage("Failed to open URL.", 0, "");
//				needDrawTabArea = true;
//			}

			//the culprit?
		}
	}

	public void sendKeyboardAction(int action) {
		stream.createFrame(201);
		stream.writeByte(action);
	}

	public void sendFrame248(int interfaceID, int sideInterfaceID) {
		if (backDialogID != -1) {
			backDialogID = -1;
			inputTaken = true;
		}
		if (inputDialogState != 0) {
			inputDialogState = 0;
			inputTaken = true;
		}
		openInterfaceID = interfaceID;
		invOverlayInterfaceID = sideInterfaceID;
		needDrawTabArea = true;
		tabAreaAltered = true;
		aBoolean1149 = false;
	}

	private int groundItemValueLimit = 7_000;

	private static final Comparator<ExperienceDrop> HIGHEST_POSITION = new Comparator<ExperienceDrop>() {

		@Override
		public int compare(ExperienceDrop o1, ExperienceDrop o2) {
			return Integer.compare(o2.getYPosition(), o1.getYPosition());
		}

	};

	private boolean parsePacket() {
		if (socketStream == null)
			return false;
		try {
			int i = socketStream.available();
			if (i == 0)
				return false;
			if (incomingPacket == -1) {
				socketStream.flushInputStream(inStream.payload, 1);
				incomingPacket = inStream.payload[0] & 0xff;
				if (encryption != null)
					incomingPacket = incomingPacket - encryption.getNextKey() & 0xff;
				packetSize = SizeConstants.packetSizes[incomingPacket];
				i--;
			}
			if (packetSize == -1)
				if (i > 0) {
					socketStream.flushInputStream(inStream.payload, 1);
					packetSize = inStream.payload[0] & 0xff;
					i--;
				} else {
					return false;
				}
			if (packetSize == -2)
				if (i > 1) {
					socketStream.flushInputStream(inStream.payload, 2);
					inStream.currentPosition = 0;
					packetSize = inStream.readUShort();
					i -= 2;
				} else {
					return false;
				}
			if (i < packetSize) {
//				String message = "";
//				message = String.format("Available=%d less than packet-size=%d, ", i, packetSize);
//				message += " - Packet: " + incomingPacket + ", Packet Size: " + packetSize
//						+ " - Previous packet: " + previousPacket1 + " Previous packet size: " + previousPacketSize1
//						+ ", 2nd Previous packet: " + previousPacket2 + ", 2nd Previous packet size: "
//						+ previousPacketSize2;
//				System.err.println(message);
				return false;
			}

			inStream.currentPosition = 0;
			socketStream.flushInputStream(inStream.payload, packetSize);
			anInt1009 = 0;

			previousPacketSize2 = previousPacketSize1;
			previousPacket2 = previousPacket1;

			previousPacketSize1 = dealtWithPacketSize;
			previousPacket1 = dealtWithPacket;

			dealtWithPacket = incomingPacket;
			dealtWithPacketSize = packetSize;

			PacketLog.add(incomingPacket, packetSize, inStream.payload);

//			if (incomingPacket != 65 && incomingPacket != 81) // Ignore the update packets
//				logger.debug("Parsing packet: " + incomingPacket);
			switch (incomingPacket) {
				case 13: // Client script
					String types = inStream.readString();
					Object[] arguments = new Object[types.length()];

					for (int index = 0; index < types.length(); index++) {
						if (types.charAt(index) == 's') {
							arguments[index] = inStream.readNullTerminatedString();
						} else { // 'i'
							arguments[index] = inStream.readDWord();
						}
					}

					int scriptId = inStream.readUShort();
					ClientScripts.execute(scriptId, arguments);
					incomingPacket = -1;
					return true;
				case 12:
					int soundId = inStream.readUShort();
					SoundType incomingSoundType = SoundType.values()[inStream.get_unsignedbyte()];
					int entitySoundSource = inStream.readUShort();
					if (entitySoundSource == 0) {
						Sound.getSound().playSound(soundId, incomingSoundType, 0);
					} else {
						Entity entity;
						if (entitySoundSource >= Short.MAX_VALUE) {
							entitySoundSource -= Short.MAX_VALUE;
							entity = players[entitySoundSource];
						} else {
							entity = npcs[entitySoundSource];
						}

						if (entity != null) {
							Sound.getSound().playSound(soundId, incomingSoundType, localPlayer.getDistanceFrom(entity));
						} else {
							Sound.getSound().playSound(soundId, incomingSoundType, 0);
						}
					}
					incomingPacket = -1;
					return true;
				case 10:
					screenFlashDrawing = true;
					screenFlashOpacityDownward = false;
					screenFlashOpacity = 0d;
					screenFlashColor = inStream.readDWord();
					screenFlashMaxIntensity = inStream.get_unsignedbyte();
					screenFlashAutoFadeOut = inStream.get_unsignedbyte() == 0;
					incomingPacket = -1;
					return true;

				// Set strings inside string container
				case 5:
					int stringContainerId =inStream.readInt();
					int strings =inStream.readInt();
				//	System.out.println("id: "+stringContainerId);
				//	int l7 = inStream.readInt();
					RSInterface stringContainer = RSInterface.get(stringContainerId);

					Preconditions.checkState(stringContainer != null && stringContainer.stringContainer != null, "No string container at id: " + stringContainer);
					stringContainer.stringContainer.clear();
					for (int index = 0; index < strings; index++) {
						stringContainer.stringContainer.add(inStream.readString());
					}
					EventCalendar.getCalendar().onStringContainerUpdated(stringContainerId);
					incomingPacket = -1;
					return true;

				// Reset scroll position
				case 2:
					int resetScrollInterfaceId = inStream.readUShort();
					RSInterface.interfaceCache[resetScrollInterfaceId].scrollPosition = 0;
					incomingPacket = -1;
					return true;

				case 3:
					int setScrollMaxInterfaceId = inStream.readUShort();
					int scrollMax = inStream.readUShort();
					RSInterface.interfaceCache[setScrollMaxInterfaceId].scrollMax = scrollMax;
					incomingPacket = -1;
					return true;


				/**
				 * Progress Bar Update Packet
				 */
				case 77:
					while (inStream.currentPosition < packetSize) {
						int interfaceId = inStream.readDWord();
						byte progressBarState = inStream.readSignedByte();
						byte progressBarPercentage = inStream.readSignedByte();

						RSInterface rsInterface = RSInterface.interfaceCache[interfaceId];
						rsInterface.progressBarState = progressBarState;
						rsInterface.progressBarPercentage = progressBarPercentage;
					}
					incomingPacket = -1;
					return true;

				case 137:
					specialAttack = inStream.get_unsignedbyte();
					incomingPacket = -1;
					return true;

				/**
				 * EntityTarget Update Packet
				 */
				case 222:
					byte entityState = (byte) inStream.get_unsignedbyte();
					if (entityState != 0) {
						short entityIndex = (short) inStream.readUShort();
						short currentHealth = (short) inStream.readUShort();
						short maximumHealth = (short) inStream.readUShort();
						entityTarget = new EntityTarget(entityState, entityIndex, currentHealth, maximumHealth, newSmallFont);
					} else {
						if (entityTarget != null) {
							entityTarget.stop();
						}
						entityTarget = null;
					}
					incomingPacket = -1;
					return true;

				/**
				 * Timer Update Packet
				 */
				case 223:
					int timerId = inStream.get_unsignedbyte();
					int secondsToAdd = inStream.readUShort();
					GameTimerHandler.getSingleton().startGameTimer(timerId, TimeUnit.SECONDS, secondsToAdd, 0);
					incomingPacket = -1;
					return true;

				case 224:
					timerId = inStream.readUShort();
					int itemId = inStream.readUShort();
					secondsToAdd = inStream.readUShort();
					GameTimerHandler.getSingleton().startGameTimer(timerId, TimeUnit.SECONDS, secondsToAdd, itemId);
					incomingPacket = -1;
					return true;

				case 11:
					long experience = inStream.readQWord();
					byte length = inStream.readSignedByte();
					int[] skills = new int[length];

					for (int j = 0; j < length; j++) {
						skills[j] = inStream.readSignedByte();
					}

				//	ExperienceDrop drop = new ExperienceDrop(experience, skills);
//System.out.println("skills: "+skills.length+" and at 0: "+skills[0]);
//for(Integer skill : skills){
//	System.out.println("the skill: "+skill+"");
//}

//					if (!experienceDrops.isEmpty()) {
//						List<ExperienceDrop> sorted = new ArrayList<ExperienceDrop>(experienceDrops);
//						Collections.sort(sorted, HIGHEST_POSITION);
//						ExperienceDrop highest = sorted.get(0);
//						if (highest.getYPosition() >= ExperienceDrop.START_Y - 5) {
//							drop.increasePosition(highest.getYPosition() - ExperienceDrop.START_Y + 20);
//						}
//					}

				//	experienceDrops.offer(drop);
					boolean increment = true;//by default its true
					if(length > 3){
				//		System.out.println("lots of them");
						increment = false;
						ExpCounter.addXP(23, experience, increment);
					} else if(length == 2){
						ExpCounter.addXP(skills[1], experience, increment);
					} else {
						ExpCounter.addXP(skills[0], experience, increment);
			}

					incomingPacket = -1;
					return true;

				case 81: // update player packet
					updatePlayers(packetSize, inStream);
					loadingMap = false;
					incomingPacket = -1;
					return true;

				case 176:
					daysSinceRecovChange = inStream.method427();
					unreadMessages = inStream.readUShortA();
					membersInt = inStream.get_unsignedbyte();
					anInt1193 = inStream.method440();
					daysSinceLastLogin = inStream.readUShort();
					if (anInt1193 != 0 && openInterfaceID == -1) {
						Signlink.dnslookup(StringUtils.method586(anInt1193));
						clearTopInterfaces();
						char c = '\u028A';
						if (daysSinceRecovChange != 201 || membersInt == 1)
							c = '\u028F';
						reportAbuseInput = "";
						canMute = false;
						for (int k9 = 0; k9 < RSInterface.interfaceCache.length; k9++) {
							if (RSInterface.interfaceCache[k9] == null || RSInterface.interfaceCache[k9].contentType != c)
								continue;
							openInterfaceID = RSInterface.interfaceCache[k9].parentID;

						}
					}
					incomingPacket = -1;
					return true;

				case 64:
					anInt1268 = inStream.method427();
					anInt1269 = inStream.method428();
					for (int j = anInt1268; j < anInt1268 + 8; j++) {
						for (int l9 = anInt1269; l9 < anInt1269 + 8; l9++)
							if (groundItems[plane][j][l9] != null) {
								groundItems[plane][j][l9] = null;
								updateGroundItems(j, l9);
							}
					}
					for (SpawnedObject spawnedObject = (SpawnedObject) spawns
						.reverseGetFirst(); spawnedObject != null; spawnedObject = (SpawnedObject) spawns
						.reverseGetNext())
						if (spawnedObject.x >= anInt1268 && spawnedObject.x < anInt1268 + 8
							&& spawnedObject.y >= anInt1269 && spawnedObject.y < anInt1269 + 8
							&& spawnedObject.plane == plane)
							spawnedObject.getLongetivity = 0;
					incomingPacket = -1;
					return true;

				case 9:
					String text = inStream.readString();
					byte state = inStream.readSignedByte();
					byte seconds = inStream.readSignedByte();
					int drawingWidth = !Client.instance.isResized() ? 519 : Client.canvasWidth;
					int drawingHeight = !Client.instance.isResized() ? 338 : Client.canvasHeight;
					fadingScreen = new BlackFadingScreen(newBoldFont, text, state, seconds, 0, 0, drawingWidth, drawingHeight, 100);
					incomingPacket = -1;
					//                    int drawingWidth = Client.currentScreenMode == ScreenMode.FIXED ? 519 : Client.currentGameWidth;
					//                    int drawingHeight = Client.currentScreenMode == ScreenMode.FIXED ? 338 : Client.currentGameHeight;
					//                    fadingScreen = new BlackFadingScreen(newBoldFont, text, state, seconds, 0, 0, drawingWidth, drawingHeight, 100);
					////				DrawingArea.setDrawingArea(y + height, x, x + width, y);
					////				DrawingArea.drawAlphaBox(x, y, width, height, 0x000000, opacity);
					return true;

				case 185:
				//int k = inStream.method436();
                    int k = inStream.readShortBigEndianA();
					RSInterface.interfaceCache[k].model_cat = 3;
					if (localPlayer.desc == null)
						RSInterface.interfaceCache[k].modelid = (localPlayer.player_recolour_destination[0] << 25)
							+ (localPlayer.player_recolour_destination[4] << 20) + (localPlayer.equipment[0] << 15)
							+ (localPlayer.equipment[8] << 10) + (localPlayer.equipment[11] << 5) + localPlayer.equipment[1];
					else
						RSInterface.interfaceCache[k].modelid = (int) (0x12345678L + localPlayer.desc.npcId);
					incomingPacket = -1;
					return true;

				/* Clan message packet */
//				case 217:
//					try {
//						clanUsername = inStream.readString();
//						clanMessage = TextInput.processText(inStream.readString());
//						clanTitle = inStream.readString();
//						PlayerRights[] rights = PlayerRights.readRightsFromPacket(inStream);
//						channelRights = EnumSet.of(rights);
//						pushMessage(clanMessage, 12, clanUsername);
//					} catch (Exception e) {
//						e.printStackTrace();
//					}
//					incomingPacket = -1;
//					return true;

				case 107:
					inCutScene = false;
					for (int l = 0; l < 5; l++)
						aBooleanArray876[l] = false;
					xpCounter = 0;
					incomingPacket = -1;
					return true;

				case 72:
					int i1 = inStream.method434();
					RSInterface class9 = RSInterface.interfaceCache[i1];
					for (int k15 = 0; k15 < class9.inventoryItemId.length; k15++) {
						class9.inventoryItemId[k15] = -1;
						class9.inventoryItemId[k15] = 0;
					}
					incomingPacket = -1;
					return true;

				case 214:
					ignoreCount = packetSize / 8;
					for (int j1 = 0; j1 < ignoreCount; j1++)
						ignoreListAsLongs[j1] = inStream.readQWord();
					incomingPacket = -1;
					return true;

				case 166:
					inCutScene = true;
					x = inStream.get_unsignedbyte();
					y = inStream.get_unsignedbyte();
					height = inStream.readUShort();
					anInt1101 = inStream.get_unsignedbyte();
					angle = inStream.get_unsignedbyte();
					if (angle >= 100) {
						xCameraPos = x * 128 + 64;
						yCameraPos = y * 128 + 64;
						zCameraPos = getCenterHeight(plane, yCameraPos, xCameraPos) - height;
					}
					incomingPacket = -1;
					return true;

				case 134://send skill
					needDrawTabArea = true;
					int skillId = inStream.get_unsignedbyte();
					int experience2 = inStream.method439();
					int currentLevel = inStream.get_unsignedbyte();
					int xp = currentExp[skillId];

					currentExp[skillId] = experience2;
					currentStats[skillId] = currentLevel;
					maxStats[skillId] = 1;
					xpCounter += currentExp[skillId] - xp;
					expAdded = currentExp[skillId] - xp;
					for (int k20 = 0; k20 < 98; k20++)
						if (experience2 >= SKILL_EXPERIENCE[k20])
							maxStats[skillId] = k20 + 2;
					updateSkillStrings(skillId);
					secondsTimer.start(60);
					getRestoreOverlay().update();
					incomingPacket = -1;
					return true;

				case 71:
					int l1 = inStream.readUShort();//cant read past 65535 lol
					int j10 = inStream.method426();
//					if (l1 == 65535)
//						l1 = -1;
					tabInterfaceIDs[j10] = l1;
					needDrawTabArea = true;
					tabAreaAltered = true;
					incomingPacket = -1;
					return true;

				case 74:
					int songId = inStream.method434();
					if (songId == 65535) {
						songId = -1;
					}
					if (songId != -1 || prevSong != 0) {
						if (songId != -1 && currentSong != songId && musicVolume != 0 && prevSong == 0) {
							// method58(10, musicVolume, false, songId);
						}
					} else {
						// method55(false);
					}
					currentSong = songId;
					incomingPacket = -1;
					return true;

				case 121:
					int j2 = inStream.method436();
					int k10 = inStream.readUShortA();
					if (musicEnabled && !lowMem) {
						nextSong = j2;
						songChanging = false;
						resourceProvider.provide(2, nextSong);
						prevSong = k10;
					}
					incomingPacket = -1;
					return true;

				case 7:
					int componentId = inStream.readInt();
					byte spriteIndex = inStream.readSignedByte();
					RSInterface component = RSInterface.interfaceCache[componentId];
					if (component != null) {
						if (component.backgroundSprites != null && spriteIndex <= component.backgroundSprites.length - 1) {
							Sprite sprite = component.backgroundSprites[spriteIndex];
							if (sprite != null) {
								component.sprite1 = component.backgroundSprites[spriteIndex];
							}
						}
					}
					incomingPacket = -1;
					return true;

				case 109:
					resetLogout();
					incomingPacket = -1;
					return false;

				case 70:

					int x2 = inStream.readShort();
					int y2 = inStream.readShort();
					int id2 = inStream.readInt();

//System.out.println("interface move: "+id2+" x: "+x2+" y: "+y2);
					RSInterface class9_5 = RSInterface.interfaceCache[id2];
					class9_5.anInt263 = x2;
					class9_5.anInt265 = y2;
					incomingPacket = -1;
					return true;

				case 243:
					int fog = inStream.get_unsignedbyte();
					fogEnabled = fog == 1;
					fogOpacity = inStream.readDWord();
					incomingPacket = -1;
					return true;

				case 73:
				case 241:
					setGameState(GameState.LOADING);
					int mapRegionX = currentRegionX;
					int mapRegionY = currentRegionY;
					if (incomingPacket == 73) {
						mapRegionX = inStream.readUShortA();
						mapRegionY = inStream.readUShort();
						isDynamicRegion = false;
					}
					if (incomingPacket == 241) {
						mapRegionY = inStream.readUShortA();
						inStream.initBitAccess();
						for (int z = 0; z < 4; z++) {
							for (int x = 0; x < 13; x++) {
								for (int y = 0; y < 13; y++) {
									int visible = inStream.readBits(1);
									if (visible == 1)
										constructRegionData[z][x][y] = inStream.readBits(26);
									else
										constructRegionData[z][x][y] = -1;
								}
							}
						}
						inStream.finishBitAccess();
						mapRegionX = inStream.readUShort();
						isDynamicRegion = true;
					}
					if (currentRegionX == mapRegionX && currentRegionY == mapRegionY && loadingStage == 2) {
						incomingPacket = -1;
						return true;
					}
					currentRegionX = mapRegionX;
					currentRegionY = mapRegionY;
					baseX = (currentRegionX - 6) * 8;
					baseY = (currentRegionY - 6) * 8;
					inTutorialIsland = (currentRegionX / 8 == 48 || currentRegionX / 8 == 49) && currentRegionY / 8 == 48;
					if (currentRegionX / 8 == 48 && currentRegionY / 8 == 148)
						inTutorialIsland = true;
					loadingStage = 1;
					longStartTime = System.currentTimeMillis();

					setGameState(GameState.LOADING);

					if (incomingPacket == 73) {
						int regionCount = 0;
						for (int x = (currentRegionX - 6) / 8; x <= (currentRegionX + 6) / 8; x++) {
							for (int y = (currentRegionY - 6) / 8; y <= (currentRegionY + 6) / 8; y++)
								regionCount++;
						}
						terrainData = new byte[regionCount][];
						objectData = new byte[regionCount][];
						mapCoordinates = new int[regionCount];
						terrainIndices = new int[regionCount];
						objectIndices = new int[regionCount];
						regionCount = 0;
						List<Integer> mapFiles = Lists.newArrayList();
						for (int x = (currentRegionX - 6) / 8; x <= (currentRegionX + 6) / 8; x++) {
							for (int y = (currentRegionY - 6) / 8; y <= (currentRegionY + 6) / 8; y++) {
								mapCoordinates[regionCount] = (x << 8) + y;
								if (inTutorialIsland
									&& (y == 49 || y == 149 || y == 147 || x == 50 || x == 49 && y == 47)) {
									terrainIndices[regionCount] = -1;
									objectIndices[regionCount] = -1;
									regionCount++;
								} else {
									int map = terrainIndices[regionCount] = resourceProvider.getMapFiles(0, y, x);
									if (map != -1) {
										resourceProvider.provide(3, map);
										mapFiles.add(map);
									}
									//System.out.println("map: "+map);
									int landscape = objectIndices[regionCount] = resourceProvider.getMapFiles(1, y, x);
									if (landscape != -1) {
										resourceProvider.provide(3, landscape);
										mapFiles.add(landscape);
									}
									//System.out.println("land: "+landscape);
									regionCount++;
								}
							}
						}

						if (Configuration.developerMode) {
							System.out.println("Map files: " + mapFiles.toString());
						}
					}
					if (incomingPacket == 241) {
						int totalLegitChunks = 0;
						int totalChunks[] = new int[676];
						for (int z = 0; z < 4; z++) {
							for (int x = 0; x < 13; x++) {
								for (int y = 0; y < 13; y++) {
									int tileBits = constructRegionData[z][x][y];
									if (tileBits != -1) {
										int xCoord = tileBits >> 14 & 0x3ff;
										int yCoord = tileBits >> 3 & 0x7ff;
										int mapRegion = (xCoord / 8 << 8) + yCoord / 8;
										for (int idx = 0; idx < totalLegitChunks; idx++) {
											if (totalChunks[idx] != mapRegion)
												continue;
											mapRegion = -1;

										}
										if (mapRegion != -1)
											totalChunks[totalLegitChunks++] = mapRegion;
									}
								}
							}
						}
						terrainData = new byte[totalLegitChunks][];
						objectData = new byte[totalLegitChunks][];
						mapCoordinates = new int[totalLegitChunks];
						terrainIndices = new int[totalLegitChunks];
						objectIndices = new int[totalLegitChunks];
						for (int idx = 0; idx < totalLegitChunks; idx++) {
							int region = mapCoordinates[idx] = totalChunks[idx];
							int localX = region >> 8 & 0xff;
							int localY = region & 0xff;
							int terrainMapId = terrainIndices[idx] = resourceProvider.getMapFiles(0, localY, localX);

                         //   System.out.println("terranin map: "+terrainMapId);
							if (terrainMapId != -1)
								resourceProvider.provide(3, terrainMapId);
							int objectMapId = objectIndices[idx] = resourceProvider.getMapFiles(1, localY, localX);
							if (objectMapId != -1)
								resourceProvider.provide(3, objectMapId);
						}
					}
					int dx = baseX - previousAbsoluteX;
					int dy = baseY - previousAbsoluteY;
					previousAbsoluteX = baseX;
					previousAbsoluteY = baseY;
					for (int index = 0; index < 16384; index++) {
						Npc npc = npcs[index];
						if (npc != null) {
							for (int poiint = 0; poiint < 10; poiint++) {
								npc.waypoint_x[poiint] -= dx;
								npc.waypoint_z[poiint] -= dy;
							}
							npc.x -= dx * 128;
							npc.z -= dy * 128;
						}
					}
					for (int index = 0; index < maxPlayers; index++) {
						Player player = players[index];
						if (player != null) {
							for (int i31 = 0; i31 < 10; i31++) {
								player.waypoint_x[i31] -= dx;
								player.waypoint_z[i31] -= dy;
							}
							player.x -= dx * 128;
							player.z -= dy * 128;
						}
					}
					loadingMap = true;
					byte startX = 0;
					byte endX = 104;
					byte stepX = 1;
					if (dx < 0) {
						startX = 103;
						endX = -1;
						stepX = -1;
					}
					byte startY = 0;
					byte endY = 104;
					byte stepY = 1;
					if (dy < 0) {
						startY = 103;
						endY = -1;
						stepY = -1;
					}
					for (int x = startX; x != endX; x += stepX) {
						for (int y = startY; y != endY; y += stepY) {
							int shiftedX = x + dx;
							int shiftedY = y + dy;
							for (int plane = 0; plane < 4; plane++)
								if (shiftedX >= 0 && shiftedY >= 0 && shiftedX < 104 && shiftedY < 104)
									groundItems[plane][x][y] = groundItems[plane][shiftedX][shiftedY];
								else
									groundItems[plane][x][y] = null;
						}
					}
					for (SpawnedObject object = (SpawnedObject) spawns
						.reverseGetFirst(); object != null; object = (SpawnedObject) spawns
						.reverseGetNext()) {
						object.x -= dx;
						object.y -= dy;
						if (object.x < 0 || object.y < 0 || object.x >= 104
							|| object.y >= 104)
							object.unlink();
					}
					if (destX != 0) {
						destX -= dx;
						destY -= dy;
					}
					inCutScene = false;
					incomingPacket = -1;
					return true;

				case 208:
					int i3 = inStream.readUShort();
					if (i3 == 65535)
						i3 = -1; // Changed to unsigned short so need to manually make it -1
					if (i3 >= 0)
						resetAnimation(i3);
					openWalkableWidgetID = i3;
					incomingPacket = -1;
					return true;

				case 99:
					minimapState = inStream.get_unsignedbyte();
					incomingPacket = -1;
					return true;

				case 75:
					int mediaType = inStream.get_unsignedbyte(); // media type
					int j31 = inStream.get_unsignedshort(); // npc id
					int j111 = inStream.get_int(); // interface
					if (j31 > 0) {
						PetSystem.petSelected = j31;
					}
					RSInterface itf = RSInterface.interfaceCache[j111];
					itf.model_cat = mediaType;
					itf.modelid = j31;
					System.out.println("type:"+mediaType+", npc:"+j31+", itn:"+j111);
					if(mediaType == 6 && j31 > 0) {
						itf.seq_id = -1;
					//	itf.seq_id =NpcDefinition.lookup(itf.modelid).readyanim;
						itf.iftype_frameindex = -1;
					}
					incomingPacket = -1;
					return true;

				case 114:
					anInt1104 = inStream.method434() * 30;
					//broadcastActive = false;
					broadcastTimer = 0;
					incomingPacket = -1;
					return true;

				case 60:
					anInt1269 = inStream.get_unsignedbyte();
					anInt1268 = inStream.method427();
					while (inStream.currentPosition < packetSize) {
						int k3 = inStream.get_unsignedbyte();
						method137(inStream, k3);
					}
					incomingPacket = -1;
					return true;

				case 35:
					int l3 = inStream.get_unsignedbyte();
					int k11 = inStream.get_unsignedbyte();
					int j17 = inStream.get_unsignedbyte();
					int k21 = inStream.get_unsignedbyte();
					aBooleanArray876[l3] = true;
					aintIntTest[l3] = k11;
					anIntArray1203[l3] = j17;
					anIntArray928[l3] = k21;
					anIntArray1030[l3] = 0;
					incomingPacket = -1;
					return true;

				case 174:
					int id = inStream.readUShort();
					int type = inStream.get_unsignedbyte();
					int delay = inStream.readUShort();
					if (soundEffectVolume != 0 && type != 0 && soundCount < 50) {
						sound[soundCount] = id;
						soundType[soundCount] = type;
						soundDelay[soundCount] = delay;
						// aClass26Array1468[soundCount] = null;
						soundCount++;
					}
					incomingPacket = -1;
					return true;

				case 104:
					int j4 = inStream.method427();
					int i12 = inStream.method426();
					String s6 = inStream.readString();
					if (j4 >= 1 && j4 <= atPlayerActions.length) {
						if (s6.equalsIgnoreCase("null"))
							s6 = null;
						atPlayerActions[j4 - 1] = s6;
						atPlayerArray[j4 - 1] = i12 == 0;
					}
					incomingPacket = -1;
					return true;

				case 78:
					destX = 0;
					incomingPacket = -1;
					return true;

				case 253:
					String s = inStream.readString();
					if (s.equals(":prayertrue:")) {
						prayClicked = true;
					} else if (s.equals(":prayerfalse:")) {
						prayClicked = false;
					} else if (s.equals(":spin:")) {
						setSpinClick(true);
						spin();
						incomingPacket = -1;
						return true;
					} else if (s.equals(":resetpost:")) {
						RSInterface listingWidget = RSInterface.interfaceCache[48020];
						if (listingWidget != null) {
							listingWidget.scrollPosition = 0;
						}
					}
					else if (s.startsWith("sendfavorites##")) {
						openjustfavorites();
						incomingPacket = -1;
						return true;
					}
					else if (s.startsWith("sendvotenotification##")) {
						flashplayerpanel=true;
						incomingPacket = -1;
						return true;
					}
					else if (s.startsWith("flashworldmap##")) {
						flashworldmap=true;
						incomingPacket = -1;
						return true;
					}

					else if (s.startsWith("rapidHeal##")) {
						String[] args = s.split("##");
						String rapidheal = args[1];
						rapidHeal = rapidheal.equalsIgnoreCase("on") ? true : false;

						incomingPacket = -1;//pktType = -1;
						return true;
					}
					else if (s.startsWith("spellbook##")) {
						String[] args = s.split("##");
						int book = Integer.parseInt(args[1]);
	        			spellbookicon = book;
						incomingPacket = -1;
						return true;
					}
					else if (s.startsWith("itemstats##")) {
						String[] args = s.split("##");

	stabatkbonus = args[1];
slashatkbonus = args[2];
crushatkbonus= args[3];
						rangedatkbonus= args[4];

magicatkbonus= args[5];



stabdefbonus= args[6];

slashdefbonus= args[7];

crushdefbonus= args[8];

						rangeddefbonus= args[9];
magicdefbonus= args[10];


						strengthbonusbonus = args[12];
prayerbonusbonus= args[11];

						incomingPacket = -1;//pktType = -1;
						return true;
					}

                    else if (s.startsWith("sendmodelwidget##")) {
                        String[] args = s.split("##");
						int id3 = Integer.parseInt(args[1]);
						int modelid2 = Integer.parseInt(args[2]);
                        RSInterface.interfaceCache[id3].model_cat = 1;
                        RSInterface.interfaceCache[id3].modelid = modelid2;
						incomingPacket = -1;
						return true;
					}
					else if (s.startsWith("lampbox##")) {
						String[] args = s.split("##");
						int whichselection = Integer.parseInt(args[1]);
						resetgenielampselections(whichselection);
						incomingPacket = -1;
						return true;
					}
					else if (s.startsWith("handlemailboxslots##")) {
                        String[] args = s.split("##");
						int slts = Integer.parseInt(args[1]);
						resetsidebars_mailboxslots(slts);
						incomingPacket = -1;
						return true;
					}
					else if (s.startsWith("changemembershipbuttonhighlight##")) {
						String[] args = s.split("##");
						int thecategory = Integer.parseInt(args[1]);

						RSInterface rsi_arrow = RSInterface.interfaceCache[113_000];
			int xid = 0;
			int yid = 20;

			if(thecategory ==0)
				xid = 22;
			else if(thecategory ==1)
				xid = 52;
			else if(thecategory ==2)
				xid =82;
			else if(thecategory ==3)
				xid = 112;
			else if(thecategory ==4)
				xid = 142;
			else
				xid = 0;
			//yid = 10;

						rsi_arrow.childX[2] = xid;
						rsi_arrow.childY[2]  = yid;


						//RSInterface.interfaceCache[113_004].height = heightofit;
						incomingPacket = -1;
						return true;
					}
					else if (s.startsWith("infrunenergy##")) {
						String[] args = s.split("##");
						int runenergy = Integer.parseInt(args[1]);
						if(runenergy == 0) {//turn it on
							infrunenergy = true;
						}else{
							infrunenergy = false;
						}
						incomingPacket = -1;
						return true;
					}
					else if (s.startsWith("curseicon##")) {
						String[] args = s.split("##");
						int curseicon = Integer.parseInt(args[1]);
						if(curseicon == 0) {//turn it on
							cursesicon = true;
						}else{
							cursesicon = false;
						}
						incomingPacket = -1;
						return true;
					}
					else if (s.startsWith("moveshop##")) {
                        String[] args = s.split("##");
						int backorforth = Integer.parseInt(args[1]);
						if(backorforth == 0){//reset
							RSInterface.interfaceCache[64000].childX[1] = 20;
							RSInterface.interfaceCache[64000].childY[1] = 56;
							RSInterface.interfaceCache[64015].height = 220;

							int y = 40;
							for(int i2 = 0 ; i2 < 75; i2++){
								RSInterface.interfaceCache[64015].childY[1+i2] = y;

								if(i2 == 9 || i2 == 19 || i2 == 29 || i2 == 39 || i2 == 49 || i2 == 59  || i2 == 69 || i2 == 79)
									y+=48;
								}

						} else {

							y= 40;
							for(int ii = 0 ; ii < 75; ii++){
								RSInterface.interfaceCache[64015].childY[1+ii] = y;

								if(ii == 9 || ii == 19 || ii == 29 || ii == 39 || ii == 49 || ii == 59 || ii == 69  || ii == 79)
									y+=48;
							}
							RSInterface.interfaceCache[64000].childX[1] = 20;
							RSInterface.interfaceCache[64000].childY[1] = 70;
							RSInterface.interfaceCache[64015].height = 220;
						}

						incomingPacket = -1;
						return true;
					}
					else if (s.startsWith("changemiddlevotepanel##")) {
                        String[] args = s.split("##");
						int backorforth = Integer.parseInt(args[1]);
						if(backorforth == 0){//reset
							RSInterface.interfaceCache[51500].children[4] = 80150;
						}
						if(backorforth == 1){//havent voted yet
							RSInterface.interfaceCache[51500].children[4] = 21406;
						}
						if(backorforth == 2){//havent voted yet
							RSInterface.interfaceCache[51500].children[4] = 21429;
						}
						incomingPacket = -1;
						return true;
					}
					else if (s.startsWith("changetomembershipbenefits##")) {
                        String[] args = s.split("##");
						int backorforth = Integer.parseInt(args[1]);
						if(backorforth == 0){//reset
							RSInterface.interfaceCache[51500].children[4] = 80130;
						}
						if(backorforth == 1){//havent voted yet
							RSInterface.interfaceCache[51500].children[4] = 113_000;
						}
						incomingPacket = -1;
						return true;
					}
                    else if (s.startsWith("changetoupgradenow##")) {
                        String[] args = s.split("##");
                        int backorforth = Integer.parseInt(args[1]);
                        if(backorforth == 0){//reset
                            RSInterface.interfaceCache[51500].children[4] = 80130;
                        }
                        if(backorforth == 1){//havent voted yet
                            RSInterface.interfaceCache[51500].children[4] = 114_000;
                        }
                        incomingPacket = -1;
                        return true;
                    }
					else if (s.startsWith("sendprogbar##")) {
                        String[] args = s.split("##");
						int id_of_bar = Integer.parseInt(args[1]);
						int currentamt = Integer.parseInt(args[2]);
						int totalamt = Integer.parseInt(args[3]);
						double thepercent = (double)currentamt/(double)totalamt;
					//	System.out.println("p: "+thepercent);
						RSInterface rsi66 = RSInterface.interfaceCache[id_of_bar];
						rsi66.progressBar2021Percentage = thepercent;
						incomingPacket = -1;
						return true;
					}
					else if (s.startsWith("upgradenowprogress##")) {
                        String[] args = s.split("##");
						int currentamt_2 = Integer.parseInt(args[1]);//74
						int totalamt_2 = Integer.parseInt(args[2]);//100
					//	int lastmax = Integer.parseInt(args[3]);//100
//int realamt = currentamt_2 - lastmax;

						double thepercent_2 =  (double) currentamt_2/ (double)totalamt_2;

					//	System.out.println("p: "+thepercent);
						//RSInterface rsi66 = RSInterface.interfaceCache[id_of_bar];
						//System.out.println("t: "+thepercent_2);
						//RSInterface.interfaceCache[114_004].height =  98 * thepercent_2;
						RSInterface.interfaceCache[114_004].sprite1.myHeight = 102 - (int) (102 * thepercent_2);
						RSInterface.interfaceCache[114_004].sprite2.myHeight = 102 - (int) (102 * thepercent_2);
						incomingPacket = -1;
						return true;
					}
					else if (s.startsWith("resettabselections##")) {
						resettabselections();
						incomingPacket = -1;
						return true;
					}
					else if (s.startsWith("killfeed##")) {
						String[] args = s.split("##");
						String killer = args[1];
						String victim = args[2];
						String style = args[3];
						System.out.println("killer: "+killer+" victim: "+victim+" style: "+style);
						KillFeed kf = new KillFeed(killer,victim,style);

						if (!killfeed.isEmpty()) {
							List<KillFeed> sorted = new ArrayList<KillFeed>(killfeed);
							Collections.sort(sorted, HIGHEST_POSITION_KILL_FEED);
							KillFeed highest = sorted.get(0);
							if (highest.getYPosition() >= KillFeed.START_Y - 5) {
								kf.increasePosition(highest.getYPosition() - KillFeed.START_Y + 20);
							}
						}

						killfeed.offer(kf);
						incomingPacket = -1;
						return true;
					}
					else if (s.startsWith("scrollbar##")) {
						String[] args = s.split("##");
						int interfaceid = Integer.parseInt(args[1]);
						int height = Integer.parseInt(args[2]);
						RSInterface rsi = RSInterface.interfaceCache[interfaceid];

						rsi.scrollMax = height;
						incomingPacket = -1;
						return true;
					}	else if (s.startsWith("npcdisplay##")) {
						String[] args = s.split("##");
						int npcid = Integer.parseInt(args[1]);
					//	System.out.println("here");
						npcDisplay = new Npc();
						npcDisplay.desc = NpcDefinition.lookup(npcid);
						npcDisplay.size = npcDisplay.desc.size;//abyte68  = size   1540 = size
						npcDisplay.turnspeed = npcDisplay.desc.turnspeed; //1504 = turnspeed
						npcDisplay.desc.npcHeight = 100;//was = size   height
						npcDisplay.desc.npcWidth = 100;//width
						npcDisplay.readyanim = npcDisplay.desc.readyanim;//
						incomingPacket = -1;
						return true;
					}
					else if (s.equals(":resetBox:")) {
						reset();
						incomingPacket = -1;
						return true;
					} else if (s.endsWith(":gamblereq:")) {
						String name = s.substring(0, s.indexOf(":"));
						long nameToLongFormat = StringUtils.longForName(name);
						boolean isIgnored = false;
						for (int index = 0; index < ignoreCount; index++) {
							if (ignoreListAsLongs[index] != nameToLongFormat)
								continue;
							isIgnored = true;
						}
						if (!isIgnored && anInt1251 == 0)
							pushMessage("wishes to Gamble with you.", 31, name);
					} else if (s.endsWith(":votecheck:")) {
						String name = s.substring(0, s.indexOf(":"));
						//name.replaceAll("_", " ");
						if ( anInt1251 == 0)
							pushMessage("", 310, name);
					} else if (s.endsWith(":tradereq:")) {
						String s3 = s.substring(0, s.indexOf(":"));
						long l17 = StringUtils.longForName(s3);
						boolean flag2 = false;
						for (int j27 = 0; j27 < ignoreCount; j27++) {
							if (ignoreListAsLongs[j27] != l17)
								continue;
							flag2 = true;

						}
						if (!flag2 && anInt1251 == 0)
							pushMessage("wishes to trade with you.", 4, s3);
					} else if (s.startsWith("//")) {
						s = s.replaceAll("//", "");
						pushMessage(s, 13, "");
					} else if (s.startsWith("/")) {
						s = s.replaceFirst("/", "");
						pushMessage(s, 11, "");
					} else if (s.endsWith("#url#")) {
						String link = s.substring(0, s.indexOf("#"));
						pushMessage("Join us at: ", 9, link);
					} else if (s.endsWith(":duelreq:")) {
						String s4 = s.substring(0, s.indexOf(":"));
						long l18 = StringUtils.longForName(s4);
						boolean flag3 = false;
						for (int k27 = 0; k27 < ignoreCount; k27++) {
							if (ignoreListAsLongs[k27] != l18)
								continue;
							flag3 = true;

						}
						if (!flag3 && anInt1251 == 0)
							pushMessage("wishes to duel with you.", 8, s4);
					} else if (s.endsWith(":chalreq:")) {
						String s5 = s.substring(0, s.indexOf(":"));
						long l19 = StringUtils.longForName(s5);
						boolean flag4 = false;
						for (int l27 = 0; l27 < ignoreCount; l27++) {
							if (ignoreListAsLongs[l27] != l19)
								continue;
							flag4 = true;

						}
						if (!flag4 && anInt1251 == 0) {
							String s8 = s.substring(s.indexOf(":") + 1, s.length() - 9);
							pushMessage(s8, 8, s5);
						}
					} else {
						pushMessage(s, 0, "");
					}
					incomingPacket = -1;
					return true;

				case 1:
					for (int k4 = 0; k4 < players.length; k4++)
						if (players[k4] != null)
							players[k4].primaryanim = -1;
					for (int j12 = 0; j12 < npcs.length; j12++)
						if (npcs[j12] != null)
							npcs[j12].primaryanim = -1;
					incomingPacket = -1;
					return true;

				case 50:
					long l4 = inStream.readQWord();
					int i18 = inStream.get_unsignedbyte();
					//String s7 = StringUtils.fixName(StringUtils.nameForLong(l4));
					for (int k24 = 0; k24 < friendsCount; k24++) {
						if (l4 != friendsListAsLongs[k24])
							continue;
						if (friendsNodeIDs[k24] != i18) {
							friendsNodeIDs[k24] = i18;
							needDrawTabArea = true;
							if (i18 > 0)
								pushMessage(friendsList[k24] + " has logged in.", 5, "");
							if (i18 == 0)
								pushMessage(friendsList[k24] + " has logged out.", 5, "");
						}
						//s7 = null;
						break;
					}
//					if (s7 != null && friendsCount < 200) {
//						friendsListAsLongs[friendsCount] = l4;
//						friendsList[friendsCount] = s7;
//						friendsNodeIDs[friendsCount] = i18;
//						friendsCount++;
//						needDrawTabArea = true;
//					}
					sortFriendsList();
					incomingPacket = -1;
					return true;

				case 18: // Add friend or ignore
					String displayName = inStream.readNullTerminatedString();
					long displayNameLong = StringUtils.longForName(displayName.toLowerCase());
					boolean friend = inStream.readSignedByte() == 1;
					int world = friend ? inStream.get_unsignedbyte() : 0;

					if (friend) {
						friendsList[friendsCount] = displayName;
						friendsListAsLongs[friendsCount] = displayNameLong;
						friendsNodeIDs[friendsCount] = world;
						friendsCount++;
					} else {
						ignoreListAsLongs[ignoreCount++] = displayNameLong;
					}

					sortFriendsList();
					needDrawTabArea = true;
					incomingPacket = -1;
					return true;

				case 19: // Friend updates display name
					String oldDisplayName = inStream.readNullTerminatedString();
					String newDisplayName = inStream.readNullTerminatedString();
					long oldDisplayNameLong = StringUtils.longForName(oldDisplayName.toLowerCase());
					long newDisplayNameLong = StringUtils.longForName(newDisplayName.toLowerCase());

					for (int i22 = 0; i22 < friendsList.length; i22++) {
						if (friendsList[i22] == null)
							continue;
						if (friendsListAsLongs[i22] == oldDisplayNameLong) {
							friendsList[i22] = newDisplayName;
							friendsListAsLongs[i22] = newDisplayNameLong;
							needDrawTabArea = true;
							pushMessage(String.format("Friend '%s' updated display name to '%s'.", oldDisplayName, newDisplayName));
						}
					}

					sortFriendsList();
					incomingPacket = -1;
					return true;

				case 110:
					if (tabID == 12)
						needDrawTabArea = true;
					energy = inStream.get_unsignedbyte();
					incomingPacket = -1;
					return true;

				case 254:
					hintType = inStream.get_unsignedbyte();
					if (hintType == 1)
						anInt1222 = inStream.readUShort();
					/**
					 * For static icons
					 */
					if (hintType >= 2 && hintType <= 6) {
						if (hintType == 2) {
							anInt937 = 64;
							anInt938 = 64;
						}
						if (hintType == 3) {
							anInt937 = 0;
							anInt938 = 64;
						}
						if (hintType == 4) {
							anInt937 = 128;
							anInt938 = 64;
						}
						if (hintType == 5) {
							anInt937 = 64;
							anInt938 = 0;
						}
						if (hintType == 6) {
							anInt937 = 64;
							anInt938 = 128;
						}
						hintType = 2;
						hintIconXpos = inStream.readUShort();
						hintIconYpos = inStream.readUShort();
						hintIconFloorPos = inStream.get_unsignedbyte();
					}
					if (hintType == 10)
						anInt933 = inStream.readUShort();
					incomingPacket = -1;
					return true;

				case 248:
					int i5 = inStream.readUShortA();
					int k12 = inStream.readUShort();
					if (backDialogID != -1) {
						backDialogID = -1;
						inputTaken = true;
					}
					if (inputDialogState != 0) {
						inputDialogState = 0;
						inputTaken = true;
					}
					if (i5 == 55000) {
						RSInterface.interfaceCache[55010].scrollPosition = 0;
						RSInterface.interfaceCache[55050].scrollPosition = 0;
					}
					openInterfaceID = i5;
					invOverlayInterfaceID = k12;
					needDrawTabArea = true;
					tabAreaAltered = true;
					aBoolean1149 = false;
					incomingPacket = -1;
					return true;

				case 79:
					int j5 = inStream.method434();
					int l12 = inStream.readUShortA();
					RSInterface class9_3 = RSInterface.interfaceCache[j5];
					if (class9_3 != null && class9_3.type == 0) {
						if (l12 < 0)
							l12 = 0;
						if (l12 > class9_3.scrollMax - class9_3.height)
							l12 = class9_3.scrollMax - class9_3.height;
						class9_3.scrollPosition = l12;
					}
					incomingPacket = -1;
					return true;

				case 68:
					for (int k5 = 0; k5 < variousSettings.length; k5++)
						if (variousSettings[k5] != anIntArray1045[k5]) {
							variousSettings[k5] = anIntArray1045[k5];
							method33(k5);
							needDrawTabArea = true;
						}
					incomingPacket = -1;
					return true;

				case 196:
					String l5 = inStream.readNullTerminatedString();
					long l5Long = StringUtils.longForName(l5.toLowerCase());
					inStream.readDWord();
					Pair<Integer, PlayerRights[]> rights = PlayerRights.readRightsFromPacket(inStream);
					boolean flag5 = false;
					if (!PlayerRights.hasRightsBetween(rights.getRight(), 1, 4)) {
						for (int l29 = 0; l29 < ignoreCount; l29++) {
							if (ignoreListAsLongs[l29] != l5Long)
								continue;
							flag5 = true;

						}
					}
					if (!flag5 && anInt1251 == 0)
						try {
							String s9 = TextInput.method525(packetSize - 4 - (l5.length() + 1) - rights.getLeft() - 1, inStream);
							String rightsString = PlayerRights.buildCrownString(rights.getRight());
							pushMessage(s9, 7, rightsString + l5);
							if (Preferences.getPreferences().pmNotifications && !appFrame.isFocused()) {//good way of doing notifications
								Notify.create()
									.title(Configuration.CLIENT_TITLE + " private message from " + l5)
									.text(s9)
									.position(Pos.BOTTOM_RIGHT)
									.onAction( new ActionHandler<Notify>() {
										@Override
										public void handle(Notify value) {
											pmTabToReply(l5);
										}
									})
									.hideAfter(5000)
									.shake(250, 5)
									.darkStyle()      // There are two default themes darkStyle() and default.
									.showConfirm();   // You can use warnings and error as well.
							}
						} catch (Exception exception1) {
							exception1.printStackTrace();
							Signlink.reporterror("cde1");
						}
					incomingPacket = -1;
					return true;

				case 85:
					anInt1269 = inStream.method427();
					anInt1268 = inStream.method427();
					incomingPacket = -1;
					return true;

				case 24://flash sidebar doesn't work obv.
					tabID = inStream.get_unsignedbyte();
					incomingPacket = -1;
					return true;

				case 246:
					int i6 = inStream.method434();
					int i13 = inStream.readUShort();
					int k18 = inStream.readUShort();
					if (k18 == 65535) {
						RSInterface.interfaceCache[i6].model_cat = 0;
						incomingPacket = -1;
						return true;
					} else {
						ItemDefinition itemDef = ItemDefinition.lookup(k18);
						RSInterface.interfaceCache[i6].model_cat = 4;
						RSInterface.interfaceCache[i6].modelid = k18;
						RSInterface.interfaceCache[i6].modelRotation1 = itemDef.xan2d;
						RSInterface.interfaceCache[i6].modelRotation2 = itemDef.yan2d;
						RSInterface.interfaceCache[i6].modelZoom = (itemDef.zoom2d * 100) / i13;
						incomingPacket = -1;
						return true;
					}

				case 171:
					boolean flag1 = inStream.get_unsignedbyte() == 1;
					int j13 = inStream.readUShort();
					if (RSInterface.interfaceCache[j13] != null) {
						RSInterface.interfaceCache[j13].isMouseoverTriggered = flag1;
						RSInterface.interfaceCache[j13].interfaceHidden = flag1;
					}
					incomingPacket = -1;
					return true;

				case 142:
					int j6 = inStream.readUShort();
					if (j6 != 0) {
						resetAnimation(j6);
					}
					if (backDialogID != -1) {
						backDialogID = -1;
						inputTaken = true;
					}
					if (inputDialogState != 0) {
						inputDialogState = 0;
						inputTaken = true;
					}
					invOverlayInterfaceID = j6;
					needDrawTabArea = true;
					tabAreaAltered = true;
					openInterfaceID = -1;
					aBoolean1149 = false;
					incomingPacket = -1;
					return true;

				case 126:
					try {
						text = inStream.readString();
						//int frame = inStream.readUShortA();
						int frame = inStream.readInt();
						handleScrollPosition(text, frame);
						handleRunePouch(text, frame);

						if (text.startsWith("www.") || text.startsWith("http")) {
							launchURL(text);
							incomingPacket = -1;
							return true;
						}
						if (text.startsWith(":pollHeight")) {//todo: only make it move the yes/no down
							int rows = Integer.parseInt(text.split("-")[1]);
							RSInterface rsi = RSInterface.interfaceCache[21406];
							rsi.childY[2] = (rows * 14);//it moves the yes/no option child down based on how amny times it was split |
							incomingPacket = -1;
							return true;
						}
						if (text.startsWith(":pollOn")) {
							String option[] = text.split("-");
							pollActive = Boolean.parseBoolean(option[1]);
							incomingPacket = -1;
							return true;
						}
						updateStrings(text, frame);
						sendFrame126(text, frame);
						if (frame >= 18144 && frame <= 18244) {
							clanList[frame - 18144] = text;
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					incomingPacket = -1;
					return true;

				case 170:
					try {
						text = inStream.readString();
						// broadcastActive = true;
						// broadcastMessage = text;
						// broadcastTimer = System.currentTimeMillis();
					} catch (Exception e) {
						e.printStackTrace();
					}
					incomingPacket = -1;
					return true;

				case 179: {
					try {
						int broadcastType = inStream.get_unsignedbyte();

						int broadcastIndex = inStream.get_unsignedbyte();

						if (broadcastType == -1) {
							BroadcastManager.removeIndex(broadcastIndex);
							return true;
						}

						Broadcast broadcast = new Broadcast();

						broadcast.type = broadcastType;

						broadcast.index = broadcastIndex;

						broadcast.message = inStream.readString();

						if (broadcastType == 1) {
							broadcast.url = inStream.readString();
						} else if (broadcastType == 2) {
							broadcast.x = inStream.readDWord();
							broadcast.y = inStream.readDWord();
							broadcast.z = inStream.get_unsignedbyte();
						}
						broadcast.setExpireDelay();
						BroadcastManager.addBoradcast(broadcast);
					} catch (Exception e) {
						e.printStackTrace();
					}
					incomingPacket = -1;
					return true;
				}

				case 206:
					publicChatMode = inStream.get_unsignedbyte();
					privateChatMode = inStream.get_unsignedbyte();
					tradeMode = inStream.get_unsignedbyte();
					inputTaken = true;
					incomingPacket = -1;
					return true;
				case 204:
					specialAttack = inStream.readSignedByte();
					specialEnabled = inStream.readSignedByte();
					incomingPacket = -1;
					//System.out.println("spec: "+specialEnabled);
					return true;
				case 240:
					if (tabID == 12)
						needDrawTabArea = true;
					weight = inStream.readSignedWord();
					incomingPacket = -1;
					return true;

				case 8:
					int k6 = inStream.method436();
					int l13 = inStream.readUShort();
					RSInterface.interfaceCache[k6].model_cat = 1;
					RSInterface.interfaceCache[k6].modelid = l13;
					incomingPacket = -1;
					return true;

				case 122:
					int l6 = inStream.method436();
					int i14 = inStream.method436();
					int i19 = i14 >> 10 & 0x1f;
					int i22 = i14 >> 5 & 0x1f;
					int l24 = i14 & 0x1f;
					RSInterface.interfaceCache[l6].textColor = (i19 << 19) + (i22 << 11) + (l24 << 3);
					incomingPacket = -1;
					return true;

				case 53:
					needDrawTabArea = true;
					int i7 = inStream.readUShort();
					RSInterface class9_1 = RSInterface.interfaceCache[i7];
					int j19 = inStream.readUShort();

					try {
						//System.out.println("Item container size: " + j19);
						for (int j22 = 0; j22 < j19; j22++) {
							int i25 = inStream.get_unsignedbyte();
							if (i25 == 255) {
								i25 = inStream.method440();
							}
							class9_1.inventoryItemId[j22] = inStream.method436();
							class9_1.inventoryAmounts[j22] = i25;
							//System.out.println(String.format("Added item [%d, %d] to container %d", class9_1.inv[j22], i25, i7));
						}
						for (int j25 = j19; j25 < class9_1.inventoryItemId.length; j25++) {
							class9_1.inventoryItemId[j25] = 0;
							class9_1.inventoryAmounts[j25] = 0;
						}
					} catch (Exception e) {
						System.err.println("Error in container " + i7 + ", length " + j19 + ", actual length " + class9_1.inventoryItemId.length);
						e.printStackTrace();
					}
					incomingPacket = -1;
					return true;

				case 230:
					int j14 = inStream.get_int();
					int j7 = inStream.get_unsignedshort();
					int k19 = inStream.get_unsignedshort();
					int k22 = inStream.get_unsignedshort();
					RSInterface.interfaceCache[j14].modelRotation1 = k19;
					RSInterface.interfaceCache[j14].modelRotation2 = k22;
					RSInterface.interfaceCache[j14].modelZoom = j7;
					incomingPacket = -1;
					return true;

				case 221:
					anInt900 = inStream.get_unsignedbyte();
					needDrawTabArea = true;
					incomingPacket = -1;
					return true;

				case 177:
					// setNorth();
					inCutScene = true;
					cinematicCamXViewpointLoc = inStream.get_unsignedbyte();
					cinematicCamYViewpointLoc = inStream.get_unsignedbyte();
					cinematicCamZViewpointLoc = inStream.readUShort();
					anInt998 = inStream.get_unsignedbyte();
					anInt999 = inStream.get_unsignedbyte();
					if (anInt999 >= 100) {
						int k7 = cinematicCamXViewpointLoc * 128 + 64;
						int k14 = cinematicCamYViewpointLoc * 128 + 64;
						int i20 = getCenterHeight(plane, k14, k7) - cinematicCamZViewpointLoc;
						int l22 = k7 - xCameraPos;
						int k25 = i20 - zCameraPos;
						int j28 = k14 - yCameraPos;
						int i30 = (int) Math.sqrt(l22 * l22 + j28 * j28);
						yCameraCurve = (int) (Math.atan2(k25, i30) * 325.94900000000001D) & 0x7ff;
						xCameraCurve = (int) (Math.atan2(l22, j28) * -325.94900000000001D) & 0x7ff;
						if (yCameraCurve < 128)
							yCameraCurve = 128;
						if (yCameraCurve > 383)
							yCameraCurve = 383;
					}
					incomingPacket = -1;
					return true;

				case 249:
					anInt1046 = inStream.method426();
					unknownInt10 = inStream.method436();
					incomingPacket = -1;
					return true;

				case 65:
					updateNPCs(inStream, packetSize);
					incomingPacket = -1;
					return true;

				case 27:
					enterInputInChatString = inStream.readString();
					messagePromptRaised = false;
					inputDialogState = 1;
					amountOrNameInput = "";
					inputTaken = true;
					incomingPacket = -1;
					return true;

				case 187:
					enterInputInChatString = inStream.readString();
					messagePromptRaised = false;
					inputDialogState = 2;
					amountOrNameInput = "";
					inputTaken = true;
					incomingPacket = -1;
					return true;

				case 191:
					messagePromptRaised = false;
					inputDialogState = 7;
					amountOrNameInput = "";
					inputTaken = true;
					incomingPacket = -1;
					return true;

				case 192:
					messagePromptRaised = false;
					inputDialogState = 8;
					amountOrNameInput = "";
					inputTaken = true;
					incomingPacket = -1;
					return true;

				case 97:
					//int l7 = inStream.readUShort();
					int l7 = inStream.readInt();
					//	System.out.println("interface opened: " + l7);
					resetAnimation(l7);
					if (invOverlayInterfaceID != 0) {
						invOverlayInterfaceID = 0;
						needDrawTabArea = true;
						tabAreaAltered = true;
					}
					if (backDialogID != -1) {
						backDialogID = -1;
						inputTaken = true;
					}
					if (inputDialogState != 0) {
						inputDialogState = 0;
						inputTaken = true;
					}
					// 17511 = Question Type
					// 15819 = Christmas Type
					// 15812 = Security Type
					// 15801 = Item Scam Type
					// 15791 = Password Safety ?
					// 15774 = Good/Bad Password
					// 15767 = Drama Type ????
					if (l7 == 15244) {
						openInterfaceID = 15767;
						fullscreenInterfaceID = 15244;
					} else {
						openInterfaceID = l7;
					}
					aBoolean1149 = false;
					incomingPacket = -1;
					return true;

				case 15:
					autocast = inStream.get_unsignedbyte() == 1 ? true : false;
					incomingPacket = -1;
					return true;

				case 218:
					int i8 = inStream.method438();
					dialogID = i8;
					inputTaken = true;
					incomingPacket = -1;
					return true;
				case 92:
					int varbitId = inStream.method434();
					int varbitValue = inStream.get_unsignedbyte();
					setVarbit(varbitId, varbitValue);
					incomingPacket = -1;
					return true;
				case 93:
					varbitId = inStream.method434();
					varbitValue = inStream.method439();
					setVarbit(varbitId, varbitValue);
					incomingPacket = -1;
					return true;
				case 87:
					int j8 = inStream.method434();
					int l14 = inStream.method439();
					anIntArray1045[j8] = l14;
					Bank.onConfigChanged(j8, l14);
					Nightmare.instance.handleConfig(j8, l14);
				//	Autocast.getSingleton().onConfigChanged(j8, l14);
					DailyRewards.get().onConfigReceived(j8, l14);
					DonatorRewards.getInstance().onConfigChanged(j8, l14);
					if (variousSettings[j8] != l14) {
						QuestTab.onConfigChanged(j8, l14);
						MonsterDropViewer.onConfigChanged(j8, l14);
						variousSettings[j8] = l14;
						method33(j8);
						needDrawTabArea = true;
						if (dialogID != -1)
							inputTaken = true;
					}
					incomingPacket = -1;
					return true;

				case 36:
					int k8 = inStream.method434();
					byte byte0 = inStream.readSignedByte();
					anIntArray1045[k8] = byte0;
					Bank.onConfigChanged(k8, byte0);
					EventCalendar.getCalendar().onConfigReceived(k8, byte0);
					Nightmare.instance.handleConfig(k8, byte0);
					//Autocast.getSingleton().onConfigChanged(k8, byte0);
					DailyRewards.get().onConfigReceived(k8, byte0);
					DonatorRewards.getInstance().onConfigChanged(k8, byte0);
					if (variousSettings[k8] != byte0) {
						QuestTab.onConfigChanged(k8, byte0);
						MonsterDropViewer.onConfigChanged(k8, byte0);
						variousSettings[k8] = byte0;
						method33(k8);
						needDrawTabArea = true;
						if (dialogID != -1)
							inputTaken = true;
					}
					incomingPacket = -1;
					return true;

				case 61:
					anInt1055 = inStream.get_unsignedbyte();
					incomingPacket = -1;
					return true;

				case 197:
					String message = inStream.readString();
					int npcIndex = inStream.readDWord();
					NPCDropInfo info = new NPCDropInfo();
					info.message = message;
					info.npcIndex = npcIndex;
					NPCDropInfo.addEntry(info);
					System.err.println(" "+message+" - "+npcIndex);
					pushMessage(message, 99, "");
					incomingPacket = -1;
					return true;

				case 200:
					int l8 = inStream.readUShort();
					int i15 = inStream.readSignedWord();
					RSInterface class9_4 = RSInterface.interfaceCache[l8];
					class9_4.seq_id = i15;
					System.out.println("l8: "+l8+" i15: "+i15);
					if (i15 == 591 || i15 == 588) {
						class9_4.modelZoom = 900; // anInt269
					}
					if (i15 == -1) {
						class9_4.iftype_frameindex = 0;
						class9_4.iftype_loops_remaining = 0;
					}
					incomingPacket = -1;
					return true;

				case 219:
					if (invOverlayInterfaceID != 0) {
						invOverlayInterfaceID = 0;
						needDrawTabArea = true;
						tabAreaAltered = true;
					}
					if (backDialogID != -1) {
						backDialogID = -1;
						inputTaken = true;
					}
					if (inputDialogState != 0) {
						inputDialogState = 0;
						inputTaken = true;
					}
					if (this.isFieldInFocus()) {
						this.resetInputFieldFocus();
						Client.inputString = "";
					}
					openInterfaceID = -1;
					aBoolean1149 = false;
					incomingPacket = -1;
					return true;

				case 34:
					needDrawTabArea = true;
					int frame = inStream.readUShort();  // frames
					//System.out.println("frame:"+frame+" should be 1688");
//					if (handledPacket34(frame)) {
//						incomingPacket = -1;
//						return true;
//					}
					RSInterface class9_2 = RSInterface.interfaceCache[frame];
					while (inStream.currentPosition < packetSize) {
						int j20 = inStream.get_unsignedbyte(); //shoot payment stuff, think its btc or cashapp ?

						int i23 = inStream.readUShort();
					//	System.out.println("i23:"+(i23+1)+", wearid");

						int l25 = inStream.get_unsignedbyte();
						if (l25 == 255)
							l25 = inStream.readDWord();
						if (j20 >= 0 && j20 < class9_2.inventoryItemId.length) {
							class9_2.inventoryItemId[j20] = i23;
							class9_2.inventoryAmounts[j20] = l25;
						}
					}
					incomingPacket = -1;
					return true;

				case 4:
				case 44:
				case 84:
				case 101:
				case 105:
				case 117:
				case 147:
				case 151:
				case 156:
				case 160:
				case 215:
					method137(inStream, incomingPacket);
					incomingPacket = -1;
					return true;

				case 106:
					tabID = inStream.method427();
				//	pushMessage("Failed to open here? "+tabID, 0, "");
					needDrawTabArea = true;
					tabAreaAltered = true;
					incomingPacket = -1;
					return true;

				case 164:
					int j9 = inStream.method434();
					resetAnimation(j9);
					if (invOverlayInterfaceID != 0) {
						invOverlayInterfaceID = 0;
						needDrawTabArea = true;
						tabAreaAltered = true;
					}
					backDialogID = j9;
					inputTaken = true;
					openInterfaceID = -1;
					aBoolean1149 = false;
					incomingPacket = -1;
					return true;

			}

			Signlink.reporterror("T1 - Packet: " + incomingPacket + ", Packet Size: " + packetSize
				+ " - Previous packet: " + previousPacket1 + " Previous packet size: " + previousPacketSize1
				+ ", 2nd Previous packet: " + previousPacket2 + ", 2nd Previous packet size: "
				+ previousPacketSize2);
			PacketLog.log();
			resetLogout();
			//ClientWindow.errorOccurredMessage("t1");
		} catch (IOException _ex) {
			PacketLog.log();
			_ex.printStackTrace();
			dropClient();
		} catch (Exception exception) {
			PacketLog.log();
			exception.printStackTrace();
			String s2 = "Packet received but exception occurred: (T2) - " + incomingPacket + ", last packet: " + previousPacket1 + ", packet before last: " + previousPacket2 + " - packetsize" + packetSize
				+ ", playerpos: " + (baseX + localPlayer.waypoint_x[0]) + "," + (baseY + localPlayer.waypoint_z[0]) + " - next bytes: ";
			for (int j15 = 0; j15 < packetSize && j15 < 50; j15++)
				s2 = s2 + inStream.payload[j15] + ",";
			Signlink.reporterror(s2);
			resetLogout();
			//ClientWindow.errorOccurredMessage("t2");
		}
		return true;
	}

	public void setVarbit(int varbitId, int value) {
		VarBit varbit = VarBit.cache[varbitId];
		int baseVar = varbit.setting;
		int startBit = varbit.start;
		int endBit = varbit.end;
		int range = BIT_MASKS[endBit - startBit];
		if (value < 0 || value > range) {
			value = 0;
		}
		range <<= startBit;
		int baseValue = variousSettings[baseVar] & (~range) | value << startBit & range;
		if (variousSettings[baseVar] != baseValue) {
			variousSettings[baseVar] = baseValue;
			method33(baseVar);
			needDrawTabArea = true;
			if (dialogID != -1) {
				inputTaken = true;
			}
		}
	}

	public static final int INTERFACE_ID = 47000;
	public static final int BOXES64 = 28; // 28 * 64 boxes
	private boolean spinClick;
	private int spins;
	private int spinNum;

	public void setSpinClick(boolean spinClick) { this.spinClick = spinClick; }

	public void spin() {
		if (openInterfaceID != INTERFACE_ID || !spinClick) {
			return;
		}

		RSInterface items = RSInterface.interfaceCache[47100];
		RSInterface boxes = RSInterface.interfaceCache[47200];
		if (spins < 100) {
			shift(items, boxes, 8);
		}
		else if (spins < 200) {
			shift(items, boxes, 5);
		}
		else if (spins < 300) {
			shift(items, boxes, 4);
		}
		else if (spins < 400) {
			shift(items, boxes, 3);
		}
		else if (spins < 488) {
			shift(items, boxes, 2);
		}
		else if (spins < 562) {
			shift(items, boxes, 1);
		}
		else {
			spinComplete();
		}
	}

	private void shift(RSInterface items, RSInterface boxes, int shiftAmount) {
		items.childX[0] -= shiftAmount;
		for(int i=0; i<BOXES64; i++){ boxes.childX[i] -= shiftAmount; }
		spins++;
	}

	private void spinComplete() {
		// Reset
		spins = 0;
		spinClick = false;
		spinNum++;
		// Notify server: spin complete
		//System.out.println("comp");
		spinc(0);
//		stream.createFrame(145);
//		stream.method432(696969);
//		stream.method432(0);
//		stream.method432(0);
	}

	public boolean handledPacket34(int frame) {
//		if (openInterfaceID != INTERFACE_ID) {
//			return false;
//		}

		RSInterface items = RSInterface.interfaceCache[frame];
		while (inStream.currentPosition < packetSize) {
			int slot = inStream.get_smart_byteorshort();
			int itemId = inStream.readUShort();


			inStream.get_unsignedbyte();
			int amount = inStream.readDWord();

			if (slot >= 0 && slot < items.inventoryItemId.length) {
				items.inventoryItemId[slot] = itemId;
				items.inventoryAmounts[slot] = amount;

				//System.out.println("item: "+itemId+", amount: "+amount);
			}
		}
		return true;
	}

	public void reset() {
		//System.out.println("test1");
		if (spinClick) {
			return;
		}

		//System.out.println("test2");

		spinNum = 0;
		RSInterface items = RSInterface.interfaceCache[47100];
		RSInterface boxes = RSInterface.interfaceCache[47200];
		items.childX[0] = 0;
		int x = 0;
		for (int z=0; z<BOXES64; z++) {
			boxes.childX[z] = x;
			x += 2880;
		}
	}

	private String coloredItemName = "";
	private int coloredItemColor = 0xffffff;

	public int currentFog = 0;
	public int[] rainbowFog = {0xFF0000,0xFF7F00,0xFFFF00,0x00FF00,0x0000FF,0x4B0082,0x9400D3};
	public long lastFog=0;

	public boolean runHover;

	public static int cameraZoom = 600;



	public void moveCameraWithPlayer() {
		anInt1265++;
		method47(true);
		method26(true);
		method47(false);
		method26(false);
		method55();
		method104();
		if (!inCutScene) {
			int i = camAngleYY;
			if (field556 / 256 > i) {
				i = field556 / 256;
			}
			if (aBooleanArray876[4] && anIntArray1203[4] + 128 > i) {
				i = anIntArray1203[4] + 128;
			}

			int k = viewRotation + viewRotationOffset & 0x7ff;
			setCameraPos(600 + i * 3,
					i, anInt1014, getCenterHeight(plane, localPlayer.z, localPlayer.x) - 50, k, anInt1015);
		}

		int j;
		if (!inCutScene)
			j = method120();
		else
			j = method121();

		int l = xCameraPos;
		int i1 = zCameraPos;
		int j1 = yCameraPos;
		int k1 = yCameraCurve;
		int l1 = xCameraCurve;

		for (int i2 = 0; i2 < 5; i2++) {
			if (aBooleanArray876[i2]) {
				int j2 = (int) ((Math.random() * (aintIntTest[i2] * 2 + 1) - aintIntTest[i2])
						+ Math.sin(anIntArray1030[i2] * (anIntArray928[i2] / 100D)) * anIntArray1203[i2]);
				if (i2 == 0)
					xCameraPos += j2;
				if (i2 == 1)
					zCameraPos += j2;
				if (i2 == 2)
					yCameraPos += j2;
				if (i2 == 3)
					xCameraCurve = xCameraCurve + j2 & 0x7ff;
				if (i2 == 4) {
					yCameraCurve += j2;
					if (yCameraCurve < 128)
						yCameraCurve = 128;
					if (yCameraCurve > 383)
						yCameraCurve = 383;
				}
			}
		}

		Model.cursorCalculations();

		Rasterizer2D.clear();
		if (Rasterizer3D.fieldOfView != cameraZoom) {
			Rasterizer3D.fieldOfView = cameraZoom;
		}

		Rasterizer2D.setDrawingArea(getViewportHeight(),
				(!isResized() ? 4 : 0),
				getViewportWidth(),
				(!isResized() ? 4 : 0));
		callbacks.post(BeforeRender.INSTANCE);

		scene.render(xCameraPos, yCameraPos, xCameraCurve, zCameraPos, j, yCameraCurve);
		rasterProvider.setRaster();
		scene.clearGameObjectCache();

		updateEntities();
		drawHeadIcon();

		((TextureProvider)Rasterizer3D.textureLoader).animate(tickDelta);
		draw3dScreen();

		drawMinimap();
		drawTabArea();
		drawChatArea();
		
//		if (isgroundnamesEnabled() && openInterfaceID == -1) {
//			displayGroundItems();
//		}

//		processExperienceCounter();
		ExpCounter.drawExperienceCounter();
		if(inWild())
			processKillFeed();
		xCameraPos = l;
		zCameraPos = i1;
		yCameraPos = j1;
		yCameraCurve = k1;
		xCameraCurve = l1;
	}
//	public static final Boundary FE1 = new Boundary(3123, 3616, 3147, 3638);
	//public static final Boundary FE2 = new Boundary(3138, 3639, 3155, 3646);
	//public static final Boundary FE3 = new Boundary(3147, 3627, 3154, 3642);


	public boolean inWild() {
		int xloc = baseX + (localPlayer.x - 6 >> 7);
		int yloc = baseY + (localPlayer.z - 6 >> 7);

if(xloc >= 3123 && xloc <= 3147   && yloc >= 3616&& yloc <= 3638||
xloc >= 3138&& xloc <= 3155&& yloc >= 3639 && yloc <= 3646  ||
xloc >= 3147&& xloc <= 3154&& yloc >= 3627&& yloc <= 3642){
return false;
}
		return xloc >= 2941 && xloc <= 3392 && yloc >= 3525 && yloc <= 3968  ||
				xloc >= 3128 && xloc <= 3285 && yloc >= 10051 && yloc <= 10226 ||
				xloc >= 2941 && xloc <= 3392 && yloc >= 9918 && yloc <= 10366

				;

	}

	public boolean inwaterbirthisland() {//3160, 10020, 3263, 10226);
		int xloc = baseX + (localPlayer.x - 6 >> 7);
		int yloc = baseY + (localPlayer.z - 6 >> 7);
		return xloc >= 2499 && xloc <= 2573 && yloc >= 3713 && yloc <= 3776;
		//ry(2941, 3525, 3392, 3968);
	}
	public boolean inRevs() {//3160, 10020, 3263, 10226);
		int xloc = baseX + (localPlayer.x - 6 >> 7);
		int yloc = baseY + (localPlayer.z - 6 >> 7);
		return xloc >= 3128 && xloc <= 3285 && yloc >= 10051 && yloc <= 10236;
		//ry(2941, 3525, 3392, 3968);
	}

	private boolean drawKillFeed = true;
	public void clearTopInterfaces() {
		stream.createFrame(130);
		if (invOverlayInterfaceID != 0) {
			invOverlayInterfaceID = 0;
			needDrawTabArea = true;
			aBoolean1149 = false;
			tabAreaAltered = true;
		}
		if (backDialogID != -1) {
			backDialogID = -1;
			inputTaken = true;
			aBoolean1149 = false;
		}
		openInterfaceID = -1;
		fullscreenInterfaceID = -1;
	}
	private Sprite[] chatButtons;

	public Client() {
		fullscreenInterfaceID = -1;
		firstLoginMessage = "";
		xpAddedPos = expAdded = 0;
		xpLock = false;
		experienceCounter = 0;
		soundType = new int[50];
		soundDelay = new int[50];
		sound = new int[50];
		chatTypeView = 0;
		clanTitles = new String[500];
		clanChatMode = 0;
		channelButtonHoverPosition = -1;
		channelButtonClickPosition = 0;
		anIntArrayArray825 = new int[104][104];
		friendsNodeIDs = new int[200];
		groundItems = new Deque[4][104][104];
		aBoolean831 = false;
		aStream_834 = new Buffer(new byte[5000]);
		npcs = new Npc[16384];
		highres_npc_list = new int[16384];
		lowres_entity_list = new int[1000];
		aStream_847 = Buffer.create();
		aBoolean848 = true;
		setGameState(GameState.STARTING);
		openInterfaceID = -1;
		currentExp = new int[Skills.SKILLS_COUNT];
		aBooleanArray876 = new boolean[5];
		drawFlames = false;
		reportAbuseInput = "";
		unknownInt10 = -1;
		menuOpen = false;
		inputString = "";
		maxPlayers = 2048;
		maxPlayerCount = 2047;
		players = new Player[maxPlayers];
		highres_player_list = new int[maxPlayers];
		entity_extended_info_list = new int[maxPlayers];
		localplayer_appearance_data = new Buffer[maxPlayers];
		//localplayer_movespeeds = new MoveSpeed[maxPlayers];
		anIntArrayArray901 = new int[104][104];
		aByteArray912 = new byte[16384];
		currentStats = new int[Skills.SKILLS_COUNT];
		ignoreListAsLongs = new long[100];
		loadingError = false;
		anIntArray928 = new int[5];
		anIntArrayArray929 = new int[104][104];
		chatTypes = new int[500];
		chatNames = new String[500];
		chatMessages = new String[500];
		chatButtons = new Sprite[4];
		aBoolean954 = true;
		friendsListAsLongs = new long[200];
		currentSong = -1;
		drawingFlames = false;
		spriteDrawX = -1;
		spriteDrawY = -1;
		anIntArray968 = new int[33];
		anIntArray969 = new int[256];
		decompressors = new Decompressor[5];
		variousSettings = new int[25000];
		aBoolean972 = false;
		anInt975 = 50;
		anIntArray976 = new int[anInt975];
		anIntArray977 = new int[anInt975];
		anIntArray978 = new int[anInt975];
		anIntArray979 = new int[anInt975];
		anIntArray980 = new int[anInt975];
		anIntArray981 = new int[anInt975];
		anIntArray982 = new int[anInt975];
		aStringArray983 = new String[anInt975];
		anInt985 = -1;
		hitMarks = new Sprite[20];
		anIntArray990 = new int[5];
		aBoolean994 = false;
		amountOrNameInput = "";
		projectiles = new Deque();
		aBoolean1017 = false;
		openWalkableWidgetID = -1;
		anIntArray1030 = new int[5];
		aBoolean1031 = false;
		mapFunctions = new Sprite[124];
		dialogID = -1;
		maxStats = new int[Skills.SKILLS_COUNT];
		anIntArray1045 = new int[25000];
		aBoolean1047 = true;
		anIntArray1052 = new int[152];
		anIntArray1229 = new int[152];
		incompleteAnimables = new Deque();
		anIntArray1057 = new int[33];
		aClass9_1059 = new RSInterface();
		mapScenes = new IndexedImage[100];
		barFillColor = 0x4d4233;
		anIntArray1065 = new int[7];
		anIntArray1072 = new int[1000];
		anIntArray1073 = new int[1000];
		loadingMap = false;
		friendsList = new String[200];
		inStream = Buffer.create();
		expectedCRCs = new int[9];
		menuActionCmd2 = new int[500];
		menuActionCmd3 = new int[500];
		menuActionID = new int[500];
		menuActionCmd1 = new long[500];
		headIcons = new Sprite[20];
		skullIcons = new Sprite[20];
		headIconsHint = new Sprite[20];
		tabAreaAltered = false;
		aString1121 = "";
		atPlayerActions = new String[6];
		atPlayerArray = new boolean[6];
		constructRegionData = new int[4][13][13];
		mapIconSprite = new Sprite[1000];
		inTutorialIsland = false;
		aBoolean1149 = false;
		crosses = new Sprite[8];
		musicEnabled = true;
		needDrawTabArea = false;
		loggedIn = false;
		canMute = false;
		isDynamicRegion = false;
		inCutScene = false;
		myUsername = "";
		setPassword("");
		captchaInput = "";
		genericLoadingError = false;
		reportAbuseInterfaceID = -1;
		spawns = new Deque();
		camAngleYY = 128;
		invOverlayInterfaceID = 0;
		stream = Buffer.create();
		menuActionName = new String[500];
		anIntArray1203 = new int[5];
		chatAreaScrollLength = 78;
		promptInput = "";
		modIcons = new Sprite[36];
		tabID = 3;
		inputTaken = false;
		songChanging = true;
		collisionMaps = new CollisionMap[4];
		aBoolean1242 = false;
		rsAlreadyLoaded = false;
		welcomeScreenRaised = true;
		messagePromptRaised = false;
		backDialogID = -1;
		bigX = new int[4000];
		bigY = new int[4000];
		chatTimes = new long[500];
	}

	public int getLocalPlayerX() {
		return baseX + (localPlayer.x - 6 >> 7);
	}

	public int getLocalPlayerY() {
		return baseY + (localPlayer.z - 6 >> 7);
	}

	public int xpCounter;
	public int expAdded;
	public int xpAddedPos;
	public boolean xpLock;

	private Sprite chatArea;
	private Sprite infinity;
	private Sprite donocurrencysprite;
	private Sprite flashingarrow;
	private Sprite zodianlogo;
	private Sprite zodianlogo_h;
	public String name;
	public String message;
	public int chatTypeView;
	public int clanChatMode;
	public int duelMode;
	public int autocastId = 0;
	public boolean autocast = false;

	public static Sprite[] cacheSprite, cacheSprite1, cacheSprite2, cacheSprite3, cacheSprite4;
	public static Sprite[] cacheInterface;

	private IndexedImage titleButton;
	private int ignoreCount;
	private long longStartTime;
	private int[][] anIntArrayArray825;
	private int[] friendsNodeIDs;
	private Deque[][][] groundItems;
	private int[] anIntArray828;
	private int[] anIntArray829;
	private volatile boolean aBoolean831;
	public LoginScreenState loginScreenState = LoginScreenState.ENTRY_SCREEN;//the default lol
	private Buffer aStream_834;
	public Npc[] npcs;
	private int npcCount;
	private int[] highres_npc_list;
	private int lowres_entity_count;
	private int[] lowres_entity_list;
	private int dealtWithPacket;
	private int dealtWithPacketSize;
	private int previousPacket1;
	private int previousPacket2;
	private int previousPacketSize2;
	private int previousPacketSize1;
	private String clickToContinueString;
	private int privateChatMode;
	private Buffer aStream_847;
	private boolean aBoolean848;
	private static int anInt849;
	private int[] anIntArray850;
	private int[] anIntArray851;
	private int[] anIntArray852;
	private int[] anIntArray853;
	private static int anInt854;
	private int hintType;
	public static int openInterfaceID;
	private int xCameraPos;
	private int zCameraPos;
	private int yCameraPos;
	private int yCameraCurve;
	private int xCameraCurve;
	public final int[] currentExp;
	private Sprite mapFlag;
	private Sprite mapMarker;

	private final boolean[] aBooleanArray876;
	private int weight;
	private MouseDetection mouseDetection;
	private volatile boolean drawFlames;
	private String reportAbuseInput;
	private int unknownInt10;
	public boolean menuOpen;
	private int anInt886;
	public static String inputString;
	private final int maxPlayers;
	public final int maxPlayerCount;
	public Player[] players;
	private int playerCount;
	private int[] highres_player_list;
	private int anInt893;
	private int[] entity_extended_info_list;
	private Buffer[] localplayer_appearance_data;
	//private MoveSpeed[] localplayer_movespeeds;
	private int viewRotationOffset;
	private int friendsCount;
	private int anInt900;
	private int[][] anIntArrayArray901;
	private byte[] aByteArray912;
	private int anInt913;
	private int crossX;
	private int crossY;
	private int crossIndex;
	private int crossType;
	private int plane;
	public final int[] currentStats;
	private int anInt924;
	private final long[] ignoreListAsLongs;
	private boolean loadingError;
	private final int[] anIntArray928;
	private int[][] anIntArrayArray929;
	private Sprite aClass30_Sub2_Sub1_Sub1_931;
	private Sprite aClass30_Sub2_Sub1_Sub1_932;
	private int anInt933;
	private int hintIconXpos;
	private int hintIconYpos;
	private int hintIconFloorPos;
	private int anInt937;
	private int anInt938;
	private long[] chatTimes;
	private int[] chatTypes;
	private String[] chatNames;
	private String[] chatMessages;
	public int tickDelta;
	private SceneGraph scene;
	private int menuScreenArea;
	private int menuOffsetX;
	private int menuOffsetY;
	private int menuWidth;
	private int menuHeight;
	private long aLong953;
	private boolean aBoolean954;
	private long[] friendsListAsLongs;
	private int currentSong;
	private static int nodeID = 1;
	static int portOff;
	static boolean clientData;
	private static boolean isMembers = true;
	private static boolean lowMem;
	private volatile boolean drawingFlames;
	private int spriteDrawX;
	private int spriteDrawY;
	private final int[] anIntArray965 = { 0xffff00, 0xff0000, 65280, 65535, 0xff00ff, 0xffffff };
	private final int[] anIntArray968;
	private final int[] anIntArray969;
	final Decompressor[] decompressors;
	public int variousSettings[];
	private boolean aBoolean972;
	private final int anInt975;
	private final int[] anIntArray976;
	private final int[] anIntArray977;
	private final int[] anIntArray978;
	private final int[] anIntArray979;
	private final int[] anIntArray980;
	private final int[] anIntArray981;
	private final int[] anIntArray982;
	private final String[] aStringArray983;
	private int field556;
	private int anInt985;
	private int anInt986;
	private Sprite[] hitMarks;
	private int anInt989;
	private final int[] anIntArray990;
	private final boolean aBoolean994;
	private int cinematicCamXViewpointLoc;
	private int cinematicCamYViewpointLoc;
	private int cinematicCamZViewpointLoc;
	private int anInt998;
	private int anInt999;
	private ISAACRandomGen encryption;
	private Sprite mapEdge;
	public static int[][] player_body_colours = {
			{ 6798, 107, 10283, 16, 4797, 7744, 5799, 4634, -31839, 22433, 2983, -11343, 8, 5281, 10438, 3650, -27322,
					-21845, 200, 571, 908, 21830, 28946, -15701, -14010 },
			{ 8741, 12, -1506, -22374, 7735, 8404, 1701, -27106, 24094, 10153, -8915, 4783, 1341, 16578, -30533, 25239,
					8, 5281, 10438, 3650, -27322, -21845, 200, 571, 908, 21830, 28946, -15701, -14010 },
			{ 25238, 8742, 12, -1506, -22374, 7735, 8404, 1701, -27106, 24094, 10153, -8915, 4783, 1341, 16578, -30533,
					8, 5281, 10438, 3650, -27322, -21845, 200, 571, 908, 21830, 28946, -15701, -14010 },
			{ 4626, 11146, 6439, 12, 4758, 10270 },

			{ 4550, 4537, 5681, 5673, 5790, 6806, 8076, 4574, 0, 12821, 100,49863,6073,36133 } }; //12821 = green

	private Sprite multiOverlay;
	private Sprite plusonerevs;
	public String amountOrNameInput;
	private int anInt1005;
	private int daysSinceLastLogin;
	private int packetSize;
	private int incomingPacket;
	private int anInt1009;
	private int anInt1010;
	private int anInt1011;
	private Deque projectiles;
	private int anInt1014;
	private int anInt1015;
	private int anInt1016;
	private boolean aBoolean1017;
	public int openWalkableWidgetID;
	private static final int[] SKILL_EXPERIENCE;
	private int minimapState;
	private int anInt1022;
	private int loadingStage;
	private Sprite scrollBar1;
	private Sprite scrollBar2;
	private int anInt1026;
	private final int[] anIntArray1030;
	private boolean aBoolean1031;
	private static Sprite[] mapFunctions;
	static int baseX;
	static int baseY;
	private int previousAbsoluteX;
	private int previousAbsoluteY;
	private int loginFailures;
	private int anInt1039;
	private int anInt1040;
	private int anInt1041;
	private int dialogID;
	public final int[] maxStats;
	private final int[] anIntArray1045;
	private int anInt1046;
	private boolean aBoolean1047;
	public TextDrawingArea smallText;
	public TextDrawingArea XPFONT;
	public TextDrawingArea aTextDrawingArea_1271;
	public TextDrawingArea chatTextDrawingArea;
	public TextDrawingArea aTextDrawingArea_1273;
	public RSFont newSmallFont;
	public RSFont newRegularFont;
	public RSFont newBoldFont;
	public RSFont newFancyFont;

	/**
	 * New fonts
	 */
	public static RSFont lato, latoBold, kingthingsPetrock, kingthingsPetrockLight;

	private int anInt1048;
	private String aString1049;
	private static int anInt1051;
	private final int[] anIntArray1052;
	private FileArchive titleStreamLoader;
	private int anInt1055;
	private Deque incompleteAnimables;
	private final int[] anIntArray1057;
	public final RSInterface aClass9_1059;
	private IndexedImage[] mapScenes;
	private static int anInt1061;
	private final int barFillColor;
	private int friendsListAction;
	private final int[] anIntArray1065;
	private int mouseInvInterfaceIndex;
	private int lastActiveInvInterface;
	public OnDemandFetcher resourceProvider;
	public int currentRegionX;
	public int currentRegionY;
	private int mapIconAmount;
	private int[] anIntArray1072;
	private int[] anIntArray1073;
	private Sprite mapDotItem;
	private Sprite mapDotNPC;
	private Sprite mapDotPlayer;
	private Sprite mapDotFriend;
	private Sprite mapDotTeam;
	private Sprite mapDotClan;
	private int anInt1079;
	private boolean loadingMap;
	private String[] friendsList;
	private Buffer inStream;
	private int draggingItemInterfaceId;
	private int itemDraggingSlot;
	private int activeInterfaceType;
	private int anInt1087;
	private int anInt1088;
	public static int anInt1089;
	private final int[] expectedCRCs;
	private int[] menuActionCmd2;
	private int[] menuActionCmd3;
	private int[] menuActionID;
	private long[] menuActionCmd1;
	private Sprite[] headIcons;
	private Sprite[] skullIcons;
	private Sprite[] headIconsHint;
	private static int anInt1097;
	private int x;
	private int y;
	private int height;
	private int anInt1101;
	private int angle;
	public static boolean tabAreaAltered;

	private int anInt1104;

	private static int anInt1117;
	private int membersInt;
	private String aString1121;
	private String enterInputInChatString;
	public static Player localPlayer;
	public boolean broadcastActive;
	public long broadcastTimer;
	public String broadcastMessage;
	private final String[] atPlayerActions;
	private final boolean[] atPlayerArray;
	private final int[][][] constructRegionData;
	public final static int[] tabInterfaceIDs = { -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1, -1 };
	private int cameraOffsetY;
	public int menuActionRow;
	private static int anInt1134;
	public int spellSelected;
	private int anInt1137;
	private int spellUsableOn;
	private String spellTooltip;
	private Sprite[] mapIconSprite;
	private boolean inTutorialIsland;
	private static int anInt1142;
	private int energy;
	private boolean aBoolean1149;
	private Sprite[] crosses;
	private boolean musicEnabled;
	private IndexedImage[] aBackgroundArray1152s;
	private IndexedImage loginBoxImageProducer;
	public static boolean needDrawTabArea;
	private int unreadMessages;
	private static int anInt1155;
	private static boolean fpsOn;
	private static boolean drawGrid;
	public static boolean loggedIn;
	private boolean canMute;
	private boolean isDynamicRegion;
	private boolean inCutScene;
	public static int update_tick;
	private static final String validUserPassChars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!\"\243$%^&*()-_=+[{]};:'@#~,<.>/?\\| ";
	private AbstractRasterProvider chatAreaGraphicsBuffer;
	private int daysSinceRecovChange;
	private RSSocket socketStream;
	public static int minimapZoom;
	private static int anInt1175;
	private boolean genericLoadingError;
	private final int[] objectTypes = { 0, 0, 0, 0, 1, 1, 1, 1, 1, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 2, 3 };
	private int reportAbuseInterfaceID;
	private Deque spawns;
	private int[] anIntArray1180;
	private int[] anIntArray1181;
	private int[] anIntArray1182;
	private byte[][] terrainData;
	private int camAngleYY;
	private static int viewRotation;
	private int anInt1186;
	private int anInt1187;
	private static int anInt1188;
	private int invOverlayInterfaceID;
	private int[] anIntArray1190;
	private int[] anIntArray1191;
	public static Buffer stream;
	private int anInt1193;
	private boolean splitPrivateChat = true;
	private String[] clanList = new String[100];
	public String[] menuActionName;
	private Sprite aClass30_Sub2_Sub1_Sub1_1201;
	private Sprite aClass30_Sub2_Sub1_Sub1_1202;
	private final int[] anIntArray1203;
	public static int player_hair_colours[] = { 9104, 10275, 7595, 3610, 7975, 8526, 918, -26734, 24466, 10145, -6882, 5027,
			1457, 16565, -30545, 25486, 24, 5392, 10429, 3673, -27335, -21957, 192, 687, 412, 21821, 28835, -15460,
			-14019 };
	private static boolean flagged;
	private int anInt1208;
	private int minimapRotation;
	public static int chatAreaScrollLength;
	private String promptInput;
	private int mouseClickCount;
	private int[][][] tileHeights;
	private long aLong1215;


	public int loginScreenCursorPos;
	@Override
	public int getloginScreenCursorPos() {
		return loginScreenCursorPos;
	}


	@Override
	public void setloginScreenCursorPos(int ls) {
		loginScreenCursorPos = ls;
	}

	private final Sprite[] modIcons;
	private long aLong1220;
	public static int tabID;
	private int anInt1222;
	public static boolean inputTaken;
	public static int inputDialogState;
	private static int anInt1226;
	private int nextSong;
	private boolean songChanging;
	private final int[] anIntArray1229;
	private CollisionMap[] collisionMaps;
	public static int BIT_MASKS[];
	private int[] mapCoordinates;
	private int[] terrainIndices;
	private int[] objectIndices;
	private int anInt1237;
	private int anInt1238;
	public final int anInt1239 = 100;
	private boolean aBoolean1242;
	private int atInventoryLoopCycle;
	private int atInventoryInterface;
	private int atInventoryIndex;
	private int atInventoryInterfaceType;
	private byte[][] objectData;
	private int tradeMode;
	private int gameMode;
	private int anInt1249;
	private int anInt1251;
	private final boolean rsAlreadyLoaded;
	private int anInt1253;
	private boolean welcomeScreenRaised;
	private boolean messagePromptRaised;
	private byte[][][] tileFlags;
	private int prevSong;
	public int destX;
	public int destY;
	private Sprite minimapImage;
	public Sprite backgroundFix;
	private int anInt1264;
	private int anInt1265;
	private int anInt1268;
	private int anInt1269;
	private int anInt1275;
	public static int backDialogID;
	private int cameraOffsetX;
	private int[] bigX;
	private int[] bigY;
	public int itemSelected;
	private int anInt1283;
	private int anInt1284;
	private int anInt1285;
	private String selectedItemName;
	private int publicChatMode;
	private static int anInt1288;
	public static int anInt1290;
	public static String server = Configuration.DEDICATED_SERVER_ADDRESS;
	public static int port = Configuration.PORT;
	public static boolean controlIsDown;
	public int drawCount;
	public int fullscreenInterfaceID;
	public int anInt1044;// 377
	public int anInt1129;// 377
	public int anInt1315;// 377
	public int anInt1500;// 377
	public int anInt1501;// 377
	public int[] fullScreenTextureArray;

	public String myUsername;
	public String myPassword;

	private static final Object CAPTCHA_LOCK = new Object();
	private String captchaInput;

	public void setPassword(String password) {
		myPassword = password;
	}

	public String getPassword() {
		return myPassword;
	}

	public void launchURL(String url) {
		if (server.equals("173.185.70.167")) {
			javax.swing.JOptionPane.showMessageDialog(this, "Staff just tried to direct you to: " + url);
			return;
		}
		if (!url.toLowerCase().startsWith("http")) {
			url = "http://" + url;
		}
		String osName = System.getProperty("os.name");
		try {
			if (osName.startsWith("Mac OS")) {
				@SuppressWarnings("rawtypes")
				Class fileMgr = Class.forName("com.apple.eio.FileManager");
				@SuppressWarnings("unchecked")
				Method openURL = fileMgr.getDeclaredMethod("openURL", new Class[] { String.class });
				openURL.invoke(null, new Object[] { url });
			} else if (osName.startsWith("Windows"))
				Runtime.getRuntime().exec("rundll32 url.dll,FileProtocolHandler " + url);
			else { // assume Unix or Linux
				if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
					Desktop.getDesktop().browse(new URI(url));
				}
			}
		} catch (Exception e) {
			pushMessage("Failed to open URL.", 0, "");
			System.err.println("Failed to launcher url on operating system " + osName);
			e.printStackTrace();
		}
	}
	private void drawInputField(RSInterface child, int xPosition, int yPosition, int width, int height) {
		int clickX = MouseHandler.saveClickX, clickY = MouseHandler.saveClickY;
		Sprite[] inputSprites = this.inputSprites;
		int xModification = 0, yModification = 0;
		for (int row = 0; row < width; row += 12) {
			if (row + 12 > width)
				row -= 12 - (width - row);
			inputSprites[6].drawSprite(xModification <= 0 ? xPosition + row : xPosition + xModification, yPosition);
			for (int collumn = 0; collumn < height; collumn += 12) {
				if (collumn + 12 > height)
					collumn -= 12 - (height - collumn);
				inputSprites[6].drawSprite(xPosition + row,
						yModification <= 0 ? yPosition + collumn : yPosition + yModification);
			}
		}
		inputSprites[1].drawSprite(xPosition, yPosition);
		inputSprites[0].drawSprite(xPosition, yPosition + height - 8);
		inputSprites[2].drawSprite(xPosition + width - 4, yPosition);
		inputSprites[3].drawSprite(xPosition + width - 4, yPosition + height - 8);
		xModification = 0;
		yModification = 0;
		for (int top = 0; top < width; top += 8) {
			if (top + 8 > width)
				top -= 8 - (width - top);
			inputSprites[5].drawSprite(xPosition + top, yPosition);
			inputSprites[5].drawSprite(xPosition + top, yPosition + height - 1);
		}
		for (int bottom = 0; bottom < height; bottom += 8) {
			if (bottom + 8 > height)
				bottom -= 8 - (height - bottom);
			inputSprites[4].drawSprite(xPosition, yPosition + bottom);
			inputSprites[4].drawSprite(xPosition + width - 1, yPosition + bottom);
		}
		String message = child.message;
		if (aTextDrawingArea_1271.getTextWidth(message) > child.width - 10)
			message = message.substring(message.length() - (child.width / 10) - 1, message.length());
		if (child.displayAsterisks)
			this.aTextDrawingArea_1271.method389(false, (xPosition + 4), child.textColor,
					new StringBuilder().append("").append(StringUtils.passwordAsterisks(message))
							.append(((!child.isInFocus ? 0 : 1) & (update_tick % 40 < 20 ? 1 : 0)) != 0 ? "|" : "")
							.toString(),
					(yPosition + (height / 2) + 6));
		else
			this.aTextDrawingArea_1271.method389(false, (xPosition + 4), child.textColor,
					new StringBuilder().append("").append(message)
							.append(((!child.isInFocus ? 0 : 1) & (update_tick % 40 < 20 ? 1 : 0)) != 0 ? "|" : "")
							.toString(),
					(yPosition + (height / 2) + 6));


		boolean clicked = false;
		if (drawingTabArea && !isResized()) {
			int x = 516;
			int y = 168;
			clicked = clickX >= xPosition + x && clickX <= xPosition + x + child.width && clickY >= yPosition + y && clickY <= yPosition + y + child.height;
		} else {
			clicked = clickX >= xPosition && clickX <= xPosition + child.width && clickY >= yPosition && clickY <= yPosition + child.height;
		}

		if (clicked && !child.isInFocus && getInputFieldFocusOwner() != child) {
			if ((MouseHandler.instance.clickMode2 == 1 && !menuOpen)) {
				RSInterface.currentInputFieldId = child.id;
				setInputFieldFocusOwner(child);
				if (child.message != null && child.message.equals(child.defaultInputFieldText))
					child.message = "";
				if (child.message == null)
					child.message = "";
			}
		}
	}
	public void setInputFieldFocusOwner(RSInterface owner) {
		for (RSInterface rsi : RSInterface.interfaceCache)
			if (rsi != null)
				if (rsi == owner)
					rsi.isInFocus = true;
				else
					rsi.isInFocus = false;
	}

	public RSInterface getInputFieldFocusOwner() {
		for (RSInterface rsi : RSInterface.interfaceCache)
			if (rsi != null)
				if (rsi.isInFocus)
					return rsi;
		return null;
	}

	public void resetInputFieldFocus() {
		for (RSInterface rsi : RSInterface.interfaceCache)
			if (rsi != null)
				rsi.isInFocus = false;
		RSInterface.currentInputFieldId = -1;
	}

	public boolean isFieldInFocus() {
		if (openInterfaceID == -1 && invOverlayInterfaceID <= 0) {
			return false;
		}
		for (RSInterface rsi : RSInterface.interfaceCache) {
			if (rsi != null) {
				if (rsi.type == 16 && rsi.isInFocus) {
					return true;
				}
			}
		}
		return false;
	}

	public static boolean scrollbarVisible(RSInterface widget) {
		if (widget.id == 55010) {
			if (RSInterface.interfaceCache[55024].message.length() <= 0) {
				return false;
			}
		} else if (widget.id == 55050) {
			if (RSInterface.interfaceCache[55064].message.length() <= 0) {
				return false;
			}
		}
		return true;
	}

	public void mouseWheelDragged(int i, int j) {
		this.anInt1186 += i * 3;
		this.anInt1187 += (j << 1);
	}

	public static int anInt1401 = 256;
	private int musicVolume = 255;
	public static long aLong1432;
	private final int[] sound;
	public int soundCount;
	private final int[] soundDelay;
	private final int[] soundType;
	private static int soundEffectVolume = 127;
	public static int[] anIntArray385 = new int[] { 12800, 12800, 12800, 12800, 12800, 12800, 12800, 12800, 12800,
			12800, 12800, 12800, 12800, 12800, 12800, 12800 };
	public static boolean LOOP_MUSIC = false;

	public static final void sleep(long time) {
		if (time > 0L) {
			if (time % 10L != 0L) {
				threadSleep(time);
			} else {
				threadSleep(time - 1L);
				threadSleep(1L);
			}
		}
	}

	private static final void threadSleep(long time) {
		try {
			Thread.sleep(time);
		} catch (InterruptedException ex) {
		}
	}

	static {
		SKILL_EXPERIENCE = new int[99];
		int i = 0;
		for (int j = 0; j < 99; j++) {
			int l = j + 1;
			int i1 = (int) (l + 300D * Math.pow(2D, l / 7D));
			i += i1;
			SKILL_EXPERIENCE[j] = i / 4;
		}
		BIT_MASKS = new int[32];
		i = 2;
		for (int k = 0; k < 32; k++) {
			BIT_MASKS[k] = i - 1;
			i += i;
		}
	}

	public static String md5Hash(String md5) {
		try {

			java.security.MessageDigest md = java.security.MessageDigest.getInstance("MD5");
			byte[] array = md.digest(md5.getBytes());

			StringBuffer sb = new StringBuffer();

			for (int i = 0; i < array.length; ++i) {
				sb.append(Integer.toHexString((array[i] & 0xFF) | 0x100).substring(1, 3));
			}

			return sb.toString();

		} catch (java.security.NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return null;
	}

	public int getLevelForXP(int exp) {
		int points = 0;
		int output;
		if (exp > 13034430)
			return 99;
		for (int lvl = 1; lvl <= 99; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if (output >= exp) {
				return lvl;
			}
		}
		return 0;
	}

	public int getLevelForXPSlayer(int exp) {
		int points = 0;
		int output;
		if (exp > 104273167)
			return 120;
		for (int lvl = 1; lvl <= 120; lvl++) {
			points += Math.floor(lvl + 300.0 * Math.pow(2.0, lvl / 7.0));
			output = (int) Math.floor(points / 4);
			if (output >= exp) {
				return lvl;
			}
		}
		return 0;
	}

	public void updateSkillStrings(int i) {
		switch (i) {
			case 0:
				sendFrame126("" + currentStats[0] + "", 4004);
				sendFrame126("" + getLevelForXP(currentExp[0]) + "", 4005);
				sendFrame126("" + currentExp[0] + "", 4044);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[0]) + 1) + "", 4045);
				break;

			case 1:
				sendFrame126("" + currentStats[1] + "", 4008);
				sendFrame126("" + getLevelForXP(currentExp[1]) + "", 4009);
				sendFrame126("" + currentExp[1] + "", 4056);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[1]) + 1) + "", 4057);
				break;

			case 2:
				sendFrame126("" + currentStats[2] + "", 4006);
				sendFrame126("" + getLevelForXP(currentExp[2]) + "", 4007);
				sendFrame126("" + currentExp[2] + "", 4050);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[2]) + 1) + "", 4051);
				break;

			case 3:
				sendFrame126("" + currentStats[3] + "", 4016);
				sendFrame126("" + getLevelForXP(currentExp[3]) + "", 4017);
				sendFrame126("" + currentExp[3] + "", 4080);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[3]) + 1) + "", 4081);
				break;

			case 4:
				sendFrame126("" + currentStats[4] + "", 4010);
				sendFrame126("" + getLevelForXP(currentExp[4]) + "", 4011);
				sendFrame126("" + currentExp[4] + "", 4062);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[4]) + 1) + "", 4063);
				break;

			case 5:
				sendFrame126("" + currentStats[5] + "", 4012);
				sendFrame126("" + getLevelForXP(currentExp[5]) + "", 4013);
				sendFrame126("" + currentExp[5] + "", 4068);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[5]) + 1) + "", 4069);
				sendFrame126("" + currentStats[5] + "/" + getLevelForXP(currentExp[5]) + "", 687);// Prayer
				// frame
				break;

			case 6:
				sendFrame126("" + currentStats[6] + "", 4014);
				sendFrame126("" + getLevelForXP(currentExp[6]) + "", 4015);
				sendFrame126("" + currentExp[6] + "", 4074);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[6]) + 1) + "", 4075);
				break;

			case 7:
				sendFrame126("" + currentStats[7] + "", 4034);
				sendFrame126("" + getLevelForXP(currentExp[7]) + "", 4035);
				sendFrame126("" + currentExp[7] + "", 4134);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[7]) + 1) + "", 4135);
				break;

			case 8:
				sendFrame126("" + currentStats[8] + "", 4038);
				sendFrame126("" + getLevelForXP(currentExp[8]) + "", 4039);
				sendFrame126("" + currentExp[8] + "", 4146);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[8]) + 1) + "", 4147);
				break;

			case 9:
				sendFrame126("" + currentStats[9] + "", 4026);
				sendFrame126("" + getLevelForXP(currentExp[9]) + "", 4027);
				sendFrame126("" + currentExp[9] + "", 4110);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[9]) + 1) + "", 4111);
				break;

			case 10:
				sendFrame126("" + currentStats[10] + "", 4032);
				sendFrame126("" + getLevelForXP(currentExp[10]) + "", 4033);
				sendFrame126("" + currentExp[10] + "", 4128);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[10]) + 1) + "", 4129);
				break;

			case 11:
				sendFrame126("" + currentStats[11] + "", 4036);
				sendFrame126("" + getLevelForXP(currentExp[11]) + "", 4037);
				sendFrame126("" + currentExp[11] + "", 4140);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[11]) + 1) + "", 4141);
				break;

			case 12:
				sendFrame126("" + currentStats[12] + "", 4024);
				sendFrame126("" + getLevelForXP(currentExp[12]) + "", 4025);
				sendFrame126("" + currentExp[12] + "", 4104);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[12]) + 1) + "", 4105);
				break;

			case 13:
				sendFrame126("" + currentStats[13] + "", 4030);
				sendFrame126("" + getLevelForXP(currentExp[13]) + "", 4031);
				sendFrame126("" + currentExp[13] + "", 4122);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[13]) + 1) + "", 4123);
				break;

			case 14:
				sendFrame126("" + currentStats[14] + "", 4028);
				sendFrame126("" + getLevelForXP(currentExp[14]) + "", 4029);
				sendFrame126("" + currentExp[14] + "", 4116);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[14]) + 1) + "", 4117);
				break;

			case 15:
				sendFrame126("" + currentStats[15] + "", 4020);
				sendFrame126("" + getLevelForXP(currentExp[15]) + "", 4021);
				sendFrame126("" + currentExp[15] + "", 4092);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[15]) + 1) + "", 4093);
				break;

			case 16:
				sendFrame126("" + currentStats[16] + "", 4018);
				sendFrame126("" + getLevelForXP(currentExp[16]) + "", 4019);
				sendFrame126("" + currentExp[16] + "", 4086);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[16]) + 1) + "", 4087);
				break;

			case 17:
				sendFrame126("" + currentStats[17] + "", 4022);
				sendFrame126("" + getLevelForXP(currentExp[17]) + "", 4023);
				sendFrame126("" + currentExp[17] + "", 4098);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[17]) + 1) + "", 4099);
				break;

			case 18:
				sendFrame126("" + currentStats[18] + "", 12166);
				sendFrame126("" + getLevelForXPSlayer(currentExp[18]) + "", 12167);
				sendFrame126("" + currentExp[18] + "", 12171);
				sendFrame126("" + getXPForLevel(getLevelForXPSlayer(currentExp[18]) + 1) + "", 12172);
				break;

			case 19:
				sendFrame126("" + currentStats[19] + "", 13926);
				sendFrame126("" + getLevelForXP(currentExp[19]) + "", 13927);
				sendFrame126("" + currentExp[19] + "", 13921);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[19]) + 1) + "", 13922);
				break;

			case 20: // runecraft
				sendFrame126("" + currentStats[20] + "", 4152);
				sendFrame126("" + getLevelForXP(currentExp[20]) + "", 4153);
				sendFrame126("" + currentExp[20] + "", 4157);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[20]) + 1) + "", 4159);
				break;

			case 22://cons
				sendFrame126("" + currentStats[22] + "", 18797);
				sendFrame126("" + getLevelForXP(currentExp[22]) + "", 18798);
				sendFrame126("" + currentExp[22] + "", 18806);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[22]) + 1) + "", 18807);
				break;

			case 21://hunter
				sendFrame126("" +currentStats[21] + "", 18799);
				sendFrame126("" + getLevelForXP(currentExp[21]) + "", 18800);
				sendFrame126("" +currentExp[21] + "", 18820);
				sendFrame126("" + getXPForLevel(getLevelForXP(currentExp[21]) + 1) + "", 18821);
				break;

		}
	}


	/**
	 * Runelite
	 */
	public DrawCallbacks drawCallbacks;
	@javax.inject.Inject
	private Callbacks callbacks;

	private boolean gpu = false;

	@Override
	public Callbacks getCallbacks() {
		return callbacks;
	}

	@Override
	public DrawCallbacks getDrawCallbacks() {
		return drawCallbacks;
	}

	@Override
	public void setDrawCallbacks(DrawCallbacks drawCallbacks) {
		this.drawCallbacks = drawCallbacks;
	}

	@Override
	public Logger getLogger() {
		return log;
	}

	@Override
	public String getBuildID() {
		return "1";
	}

	@Override
	public List<net.runelite.api.Player> getPlayers() {
		return Arrays.asList(players);
	}

	@Override
	public List<NPC> getNpcs() {
		List<NPC> npcs = new ArrayList<NPC>(npcCount);
		for (int i = 0; i < npcCount; ++i)
		{
			npcs.add(this.npcs[highres_npc_list[i]]);
		}
		return npcs;
	}

	@Override
	public RSNPC[] getCachedNPCs() {
		return npcs;
	}

	@Override
	public RSPlayer[] getCachedPlayers() {
		return players;
	}

	@Override
	public int getLocalInteractingIndex() {
		return 0;
	}

	@Override
	public void setLocalInteractingIndex(int idx) {

	}

	@Override
	public RSNodeDeque getTilesDeque() {
		return null;
	}

	@Override
	public RSNodeDeque[][][] getGroundItemDeque() {
		return new RSNodeDeque[0][][];
	}

	@Override
	public RSNodeDeque getProjectilesDeque() {
		return null;
	}

	@Override
	public RSNodeDeque getGraphicsObjectDeque() {
		return null;
	}

	@Override
	public String getUsername() {
		return myUsername;
	}

	@Override
	public int getBoostedSkillLevel(net.runelite.api.Skill skill) {
		return 1;
	}

	@Override
	public int getRealSkillLevel(net.runelite.api.Skill skill) {
		return 1;
	}

	@Override
	public int getTotalLevel() {
		return 0;
	}

	@Override
	public MessageNode addChatMessage(ChatMessageType type, String name, String message, String sender) {
		return null;
	}

	@Override
	public MessageNode addChatMessage(ChatMessageType type, String name, String message, String sender,
									  boolean postEvent) {
		return null;
	}
	public int gameState = -1;
	@Override
	public GameState getGameState() {
		return GameState.of(gameState);
	}

	@Override
	public CinematicState getCinematicState() {
		return null;
	}

	@Override
	public int getRSGameState() {
		return gameState;
	}

	@Override
	public void setRSGameState(int state) {
		gameState = state;
	}

	@Override
	public void setCheckClick(boolean checkClick) {
		scene.clicked = checkClick;
	}

	@Override
	public void setMouseCanvasHoverPositionX(int x) {
		MouseHandler.mouseX = x;
	}

	@Override
	public void setMouseCanvasHoverPositionY(int y) {
		MouseHandler.mouseY = y;
	}

	@Override
	public void setGameState(GameState state) {
		gameState = state.getState();
		GameStateChanged event = new GameStateChanged();
		event.setGameState(state);
		if(callbacks != null) {
			callbacks.post(event);
		}

	}

	@Override
	public void setCinematicState(CinematicState gameState) {

	}

	@Override
	public void setGameState(int gameState) {
		loadingStage = gameState;
	}

	@Override
	public void stopNow() {
	}

	@Override
	public void setUsername(String name) {
		myUsername = name;
	}
	@Override
	public void setOtp(String otp) {

	}

	@Override
	public int getCurrentLoginField() {
		return 0;
	}

	@Override
	public int getLoginIndex() {
		return 0;
	}

	@Override
	public AccountType getAccountType() {
		return AccountType.NORMAL;
	}

	@Override
	public int getFPS() {
		return fps;
	}

	@Override
	public int getCameraX() {
		return this.xCameraPos;
	}

	@Override
	public int getCameraY() {
		return this.yCameraPos;
	}

	@Override
	public int getCameraZ() {
		return this.zCameraPos;
	}

	@Override
	public int getCameraPitch() {
		return yCameraCurve;
	}

	@Override
	public void setCameraPitch(int cameraPitch) {
		yCameraCurve = cameraPitch;
	}

	@Override
	public int getCameraYaw() {
		return xCameraCurve;
	}

	@Override
	public int getWorld() {
		return 1;
	}

	@Override
	public int getCanvasHeight() {
		return canvasHeight;
	}

	@Override
	public int getCanvasWidth() {
		return canvasWidth;
	}

	@Override
	public int getViewportHeight() {
		return !isResized() ? 334 : canvasHeight;
	}

	@Override
	public int getViewportWidth() {
		return !isResized() ? 512 : canvasWidth;

	}

	@Override
	public int getViewportXOffset() {
		return !isResized() ? 4 : 0;
	}

	@Override
	public int getViewportYOffset() {
		return !isResized() ? 4 : 0;
	}

	@Override
	public int getScale() {
		return Rasterizer3D.fieldOfView;
	}

	@Override
	public net.runelite.api.Point getMouseCanvasPosition() {
		return new net.runelite.api.Point(MouseHandler.mouseX, MouseHandler.mouseY);
	}

	@Override
	public int[][][] getTileHeights() {
		return tileHeights;
	}

	@Override
	public byte[][][] getTileSettings() {
		return tileFlags;
	}

	@Override
	public int getPlane() {
		return plane;
	}

	@Override
	public SceneGraph getScene() {
		return scene;
	}

	@Override
	public RSPlayer getLocalPlayer() {
		return localPlayer;
	}

	@Override
	public int getLocalPlayerIndex() {
		return 0;
	}

	@Override
	public int getNpcIndexesCount() {
		return 0;
	}

	@Override
	public int[] getNpcIndices() {
		return new int[0];
	}

	@Override
	public ItemComposition getItemComposition(int id) {
		return ItemDefinition.lookup(id);
	}

	@Override
	public ItemComposition getItemDefinition(int id) {
		return ItemDefinition.lookup(id);
	}

	@Override
	public SpritePixels createItemSprite(int itemId, int quantity, int border, int shadowColor, int stackable,
										 boolean noted, int scale) {
		return null;
	}


	@Override
	public RSSpritePixels[] getSprites(IndexDataBase source, int archiveId, int fileId) {
		return null;
	}

	@Override
	public RSArchive getIndexSprites() {
		return null;
	}

	@Override
	public RSArchive getIndexScripts() {
		return null;
	}

	@Override
	public RSArchive getIndexConfig() {
		return null;
	}

	@Override
	public RSArchive getMusicTracks() {
		return null;
	}

	@Override
	public int getBaseX() {
		return baseX;
	}

	@Override
	public int getBaseY() {
		return baseY;
	}

	@Override
	public int getMouseCurrentButton() {
		return 0;
	}

	@Override
	public int getSelectedSceneTileX() {
		return scene.clickedTileX;
	}

	@Override
	public void setSelectedSceneTileX(int selectedSceneTileX) {
		scene.clickedTileX = selectedSceneTileX;
	}

	@Override
	public int getSelectedSceneTileY() {
		return scene.clickedTileY;
	}

	@Override
	public void setSelectedSceneTileY(int selectedSceneTileY) {
		scene.clickedTileY = selectedSceneTileY;
	}

	@Override
	public Tile getSelectedSceneTile() {
		int tileX = scene.hoverX;
		int tileY = scene.hoverY;

		if (tileX == -1 || tileY == -1)
		{
			return null;
		}

		return getScene().getTiles()[getPlane()][tileX][tileY];

	}

	@Override
	public boolean isDraggingWidget() {
		return false;
	}

	@Override
	public RSWidget getDraggedWidget() {
		return null;
	}

	@Override
	public RSWidget getDraggedOnWidget() {
		return null;
	}

	@Override
	public void setDraggedOnWidget(net.runelite.api.widgets.Widget widget) {
	}

	@Override
	public RSWidget[][] getWidgets() {
		return new RSWidget[0][];
	}

	@Override
	public RSWidget[] getGroup(int groupId) {
		return new RSWidget[0];
	}

	@Override
	public int getTopLevelInterfaceId() {
		return openInterfaceID;
	}

	@Override
	public RSWidget[] getWidgetRoots() {
		return null;
	}

	@Override
	public RSWidget getWidget(WidgetInfo widget) {
		int groupId = widget.getGroupId();
		int childId = widget.getChildId();

		return getWidget(groupId, childId);
	}

	@Override
	public RSWidget getWidget(int groupId, int childId) {
		return null;
	}

	@Override
	public RSWidget getWidget(int packedID) {
		return null;
	}

	@Override
	public int[] getWidgetPositionsX() {
		return null;
	}

	@Override
	public int[] getWidgetPositionsY() {
		return null;
	}

	@Override
	public boolean isMouseCam() {
		return false;
	}

	@Override
	public int getCamAngleDX() {
		return anInt1187;
	}

	@Override
	public void setCamAngleDX(int angle) {
		anInt1187 = angle;
	}

	@Override
	public int getCamAngleDY() {
		return anInt1186;
	}

	@Override
	public void setCamAngleDY(int angle) {
		anInt1186 = angle;
	}

	@Override
	public RSWidget createWidget() {
		return null;
	}

	@Override
	public void revalidateWidget(net.runelite.api.widgets.Widget w) {

	}

	@Override
	public void revalidateWidgetScroll(net.runelite.api.widgets.Widget[] group, net.runelite.api.widgets.Widget w, boolean postEvent) {

	}

	@Override
	public int getEntitiesAtMouseCount() {
		return 0;
	}

	@Override
	public void setEntitiesAtMouseCount(int i) {

	}

	@Override
	public long[] getEntitiesAtMouse() {
		return new long[0];
	}

	@Override
	public int getViewportMouseX() {
		return 0;
	}

	@Override
	public int getViewportMouseY() {
		return 0;
	}

	@Override
	public int getEnergy() {
		return 0;
	}

	@Override
	public int getWeight() {
		return 0;
	}

	@Override
	public String[] getPlayerOptions() {
		return null;
	}

	@Override
	public boolean[] getPlayerOptionsPriorities() {
		return null;
	}

	@Override
	public int[] getPlayerMenuTypes() {
		return null;
	}

	@Override
	public int getMouseX() {
		return MouseHandler.mouseX;
	}

	@Override
	public int getMouseY() {
		return MouseHandler.mouseY;
	}

	@Override
	public int getMouseX2() {
		return scene.clickScreenX;
	}

	@Override
	public int getMouseY2() {
		return scene.clickScreenY;
	}

	@Override
	public boolean containsBounds(int var0, int var1, int var2, int var3, int var4, int var5, int var6, int var7) {
		return scene.inBounds(var0, var1, var2, var3, var4, var5, var6, var7);
	}

	@Override
	public boolean isCheckClick() {
		return SceneGraph.clicked;
	}

	@Override
	public RSWorld[] getWorldList() {
		return null;
	}

	@Override
	public MenuEntry createMenuEntry(int idx) {
		return null;
	}

	@Override
	public void addRSChatMessage(int type, String name, String message, String sender) {

	}

	@Override
	public RSObjectComposition getRSObjectComposition(int objectId) {
		return null;
	}

	@Override
	public RSNPCComposition getRSNpcComposition(int npcId) {
		return null;
	}


	@Override
	public MenuEntry createMenuEntry(String option, String target, int identifier, int opcode, int param1, int param2,
									 boolean forceLeftClick) {
		return null;
	}

	@Override
	public MenuEntry[] getMenuEntries() {
		return null;
	}

	@Override
	public int getMenuOptionCount() {
		return 0;
	}


	@Override
	public void setMenuEntries(MenuEntry[] entries) {

	}

	@Override
	public void setMenuOptionCount(int count) {
		this.menuActionRow = count;
	}

	@Override
	public String[] getMenuOptions() {
		return new String[0];
	}

	@Override
	public String[] getMenuTargets() {
		return new String[0];
	}

	@Override
	public int[] getMenuIdentifiers() {
		return new int[0];
	}

	@Override
	public int[] getMenuOpcodes() {
		return new int[0];
	}

	@Override
	public int[] getMenuArguments1() {
		return new int[0];
	}

	@Override
	public int[] getMenuArguments2() {
		return new int[0];
	}

	@Override
	public boolean[] getMenuForceLeftClick() {
		return new boolean[0];
	}

	@Override
	public boolean isMenuOpen() {
		return menuOpen;
	}

	@Override
	public int getMenuX() {
		return 0;
	}

	@Override
	public int getMenuY() {
		return 0;
	}

	@Override
	public int getMenuHeight() {
		return 0;
	}

	@Override
	public int getMenuWidth() {
		return 0;
	}

	@Override
	public net.runelite.rs.api.RSFont getFontBold12() {
		return null;
	}

	@Override
	public void rasterizerDrawHorizontalLine(int x, int y, int w, int rgb) {

	}

	@Override
	public void rasterizerDrawHorizontalLineAlpha(int x, int y, int w, int rgb, int a) {

	}

	@Override
	public void rasterizerDrawVerticalLine(int x, int y, int h, int rgb) {

	}

	@Override
	public void rasterizerDrawVerticalLineAlpha(int x, int y, int h, int rgb, int a) {

	}

	@Override
	public void rasterizerDrawGradient(int x, int y, int w, int h, int rgbTop, int rgbBottom) {

	}

	@Override
	public void rasterizerDrawGradientAlpha(int x, int y, int w, int h, int rgbTop, int rgbBottom, int alphaTop, int alphaBottom) {

	}

	@Override
	public void rasterizerFillRectangleAlpha(int x, int y, int w, int h, int rgb, int a) {
		Rasterizer2D.drawTransparentBox(x,y,w,h,rgb,a);
	}

	@Override
	public void rasterizerDrawRectangle(int x, int y, int w, int h, int rgb) {

	}

	@Override
	public void rasterizerDrawRectangleAlpha(int x, int y, int w, int h, int rgb, int a) {

	}

	@Override
	public void rasterizerDrawCircle(int x, int y, int r, int rgb) {

	}

	@Override
	public void rasterizerDrawCircleAlpha(int x, int y, int r, int rgb, int a) {

	}

	@Override
	public RSEvictingDualNodeHashTable getHealthBarCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getHealthBarSpriteCache() {
		return null;
	}

	@Override
	public int getMapAngle() {
		return 0;
	}

	@Override
	public void setCameraYawTarget(int cameraYawTarget) {

	}

	public void setResized(boolean resized) {
		this.resized = resized;
	}

	private boolean resized = false;

	@Override
	public boolean isResized() {
		return resized;
	}

	@Override
	public int getRevision() {
		return 1;
	}

	@Override
	public int[] getMapRegions() {
		return new int[0];
	}

	@Override
	public int[][][] getInstanceTemplateChunks() {
		return constructRegionData;
	}

	@Override
	public int[][] getXteaKeys() {
		return null;
	}

	@Override
	public int getCycleCntr() {
		return 0;
	}

	@Override
	public void setChatCycle(int value) {

	}

	@Override
	public int[] getVarps() {
		return settings;
	}

	@Override
	public RSVarcs getVarcs() {
		return null;
	}

	@Override
	public Map<Integer, Object> getVarcMap() {
		return null;
	}

	@Override
	public int getVar(VarPlayer varPlayer) {
		return 0;
	}

	@Override
	public int getVar(Varbits varbit) {
		return 0;
	}

	@Override
	public int getVar(VarClientInt varClientInt) {
		return 0;
	}

	@Override
	public String getVar(VarClientStr varClientStr) {
		return null;
	}

	@Override
	public int getVarbitValue(int varbitId) {
		return 0;
	}

	@Override
	public int getVarcIntValue(int varcIntId) {
		return 0;
	}

	@Override
	public String getVarcStrValue(int varcStrId) {
		return null;
	}

	@Override
	public void setVar(VarClientStr varClientStr, String value) {
	}

	@Override
	public void setVar(VarClientInt varClientStr, int value) {
	}

	@Override
	public void setVarbit(Varbits varbit, int value) {
	}

	@Override
	public VarbitComposition getVarbit(int id) {
		return null;
	}

	@Override
	public int getVarbitValue(int[] varps, int varbitId) {
		return 0;
	}

	@Override
	public int getVarpValue(int[] varps, int varpId) {
		return 0;
	}

	@Override
	public int getVarpValue(int i) {
		return 0;
	}

	@Override
	public void setVarbitValue(int[] varps, int varbit, int value) {
	}

	@Override
	public void queueChangedVarp(int varp) {
	}

	@Override
	public RSNodeHashTable getWidgetFlags() {
		return null;
	}

	@Override
	public RSNodeHashTable getComponentTable() {
		return null;
	}

	@Override
	public RSGrandExchangeOffer[] getGrandExchangeOffers() {
		return null;
	}

	@Override
	public boolean isPrayerActive(Prayer prayer) {
		return false;
	}

	@Override
	public int getSkillExperience(net.runelite.api.Skill skill) {
		return 1;
	}

	@Override
	public long getOverallExperience() {
		return 1;
	}

	@Override
	public void refreshChat() {
	}

	@Override
	public Map<Integer, ChatLineBuffer> getChatLineMap() {
		return null;
	}

	@Override
	public RSIterableNodeHashTable getMessages() {
		return null;
	}

	@Override
	public ObjectComposition getObjectDefinition(int objectId) {
		return ObjectDefinition.lookup(objectId);
	}

	@Override
	public NPCComposition getNpcDefinition(int npcId) {
		return NpcDefinition.lookup(npcId);
	}

	@Override
	public StructComposition getStructComposition(int structID) {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getStructCompositionCache() {
		return null;
	}

	@Override
	public RSWorldMapElement[] getMapElementConfigs() {
		return null;
	}

	@Override
	public RSIndexedSprite[] getMapScene() {
		return null;
	}

	public Sprite[] minimapDot = new Sprite[7];


	@Override
	public RSSpritePixels[] getMapDots() {
		return null;
	}

	@Override
	public int getGameCycle() {
		return update_tick;
	}

	@Override
	public RSSpritePixels[] getMapIcons() {
		return null;
	}

	@Override
	public RSIndexedSprite[] getModIcons() {
		return null;
	}

	@Override
	public void setRSModIcons(RSIndexedSprite[] modIcons) {

	}

	@Override
	public void setModIcons(IndexedSprite[] modIcons) {

	}

	@Override
	public RSIndexedSprite createIndexedSprite() {
		return null;
	}

	@Override
	public RSSpritePixels createSpritePixels(int[] pixels, int width, int height) {
		return null;
	}

	@Override
	public int getDestinationX() {
		return 0;
	}

	@Override
	public int getDestinationY() {
		return 0;
	}

	@Override
	public RSSoundEffect[] getAudioEffects() {
		return new RSSoundEffect[0];
	}

	@Override
	public int[] getQueuedSoundEffectIDs() {
		return new int[0];
	}

	@Override
	public int[] getSoundLocations() {
		return new int[0];
	}

	@Override
	public int[] getQueuedSoundEffectLoops() {
		return new int[0];
	}

	@Override
	public int[] getQueuedSoundEffectDelays() {
		return new int[0];
	}

	@Override
	public int getQueuedSoundEffectCount() {
		return 0;
	}

	@Override
	public void setQueuedSoundEffectCount(int queuedSoundEffectCount) {

	}

	@Override
	public void queueSoundEffect(int id, int numLoops, int delay) {

	}

	@Override
	public LocalPoint getLocalDestinationLocation() {
		return null;
	}

	@Override
	public List<net.runelite.api.Projectile> getProjectiles() {
		List<net.runelite.api.Projectile> projectileList = new ArrayList<>();
		for (net.runelite.api.Projectile projectile = (net.runelite.api.Projectile) projectiles
				.reverseGetFirst(); projectile != null; projectile = (net.runelite.api.Projectile) projectiles.reverseGetNext()) {
			projectileList.add(projectile);
		}
		return projectileList;
	}

	@Override
	public List<GraphicsObject> getGraphicsObjects() {
		List<net.runelite.api.GraphicsObject> list = new ArrayList<>();
		for (GraphicsObject projectile = (GraphicsObject) incompleteAnimables
				.reverseGetFirst(); projectile != null; projectile = (GraphicsObject) incompleteAnimables.reverseGetNext()) {
			list.add(projectile);
		}
		return list;
	}

	@Override
	public RuneLiteObject createRuneLiteObject() {
		return null;
	}

	@Override
	public net.runelite.api.Model loadModel(int id) {
		return null;
	}

	@Override
	public net.runelite.api.Model loadModel(int id, short[] colorToFind, short[] colorToReplace) {
		return null;
	}

	@Override
	public net.runelite.api.Animation loadAnimation(int id) {
		return null;
	}

	@Override
	public int getMusicVolume() {
		return 0;
	}

	@Override
	public void setMusicVolume(int volume) {
	}

	@Override
	public void playSoundEffect(int id) {

	}

	@Override
	public void playSoundEffect(int id, int x, int y, int range) {
	}

	@Override
	public void playSoundEffect(int id, int x, int y, int range, int delay) {
	}

	@Override
	public void playSoundEffect(int id, int volume) {

	}

	@Override
	public RSAbstractRasterProvider getBufferProvider() {
		return rasterProvider;
	}

	@Override
	public int getMouseIdleTicks() {
		return MouseHandler.idleCycles;
	}

	@Override
	public long getMouseLastPressedMillis() {
		return MouseHandler.lastPressed;
	}

	public long getClientMouseLastPressedMillis() {
		return MouseHandler.lastPressed;
	}

	public void setClientMouseLastPressedMillis(long mills) {
		MouseHandler.lastPressed = mills;
	}

	@Override
	public int getKeyboardIdleTicks() {
		return KeyHandler.idleCycles;
	}

	@Override
	public void changeMemoryMode(boolean lowMemory) {
		setLowMemory(lowMemory);
		setSceneLowMemory(lowMemory);
		setAudioHighMemory(true);
		setObjectDefinitionLowDetail(lowMemory);
		if (getGameState() == GameState.LOGGED_IN)
		{
			setGameState(1);
		}
	}

	public HashMap<Integer, ItemContainer> containers = new HashMap<Integer, ItemContainer>();

	@Override
	public ItemContainer getItemContainer(InventoryID inventory) {
		return containers.get(inventory.getId());
	}

	@Override
	public ItemContainer getItemContainer(int id) {
		return containers.get(id);
	}

	@Override
	public RSNodeHashTable getItemContainers() {
		return null;
	}

	@Override
	public RSItemComposition getRSItemDefinition(int itemId) {
		return ItemDefinition.lookup(itemId);
	}

	@Override
	public RSSpritePixels createRSItemSprite(int itemId, int quantity, int thickness, int borderColor, int stackable, boolean noted) {
		return null;
	}

	@Override
	public void sendMenuAction(int n2, int n3, int n4, int n5, String string, String string2, int n6, int n7) {

	}

	@Override
	public void decodeSprite(byte[] data) {

	}

	@Override
	public int getIndexedSpriteCount() {
		return 0;
	}

	@Override
	public int getIndexedSpriteWidth() {
		return 0;
	}

	@Override
	public int getIndexedSpriteHeight() {
		return 0;
	}

	@Override
	public int[] getIndexedSpriteOffsetXs() {
		return new int[0];
	}

	@Override
	public void setIndexedSpriteOffsetXs(int[] indexedSpriteOffsetXs) {

	}

	@Override
	public int[] getIndexedSpriteOffsetYs() {
		return new int[0];
	}

	@Override
	public void setIndexedSpriteOffsetYs(int[] indexedSpriteOffsetYs) {

	}

	@Override
	public int[] getIndexedSpriteWidths() {
		return new int[0];
	}

	@Override
	public void setIndexedSpriteWidths(int[] indexedSpriteWidths) {

	}

	@Override
	public int[] getIndexedSpriteHeights() {
		return new int[0];
	}

	@Override
	public void setIndexedSpriteHeights(int[] indexedSpriteHeights) {

	}

	@Override
	public byte[][] getSpritePixels() {
		return new byte[0][];
	}

	@Override
	public void setSpritePixels(byte[][] spritePixels) {

	}

	@Override
	public int[] getIndexedSpritePalette() {
		return new int[0];
	}

	@Override
	public void setIndexedSpritePalette(int[] indexedSpritePalette) {

	}

	@Override
	public int getIntStackSize() {
		return 0;
	}

	@Override
	public void setIntStackSize(int stackSize) {
	}

	@Override
	public int[] getIntStack() {
		return null;
	}

	@Override
	public int getStringStackSize() {
		return 0;
	}

	@Override
	public void setStringStackSize(int stackSize) {
	}

	@Override
	public String[] getStringStack() {
		return null;
	}

	@Override
	public RSFriendSystem getFriendManager() {
		return null;
	}

	@Override
	public RSWidget getScriptActiveWidget() {
		return null;
	}

	@Override
	public RSWidget getScriptDotWidget() {
		return null;
	}

	@Override
	public RSScriptEvent createRSScriptEvent(Object... args) {
		return null;
	}

	@Override
	public void runScriptEvent(RSScriptEvent event) {

	}

	@Override
	public RSEvictingDualNodeHashTable getScriptCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getRSStructCompositionCache() {
		return null;
	}

	@Override
	public RSStructComposition getRSStructComposition(int id) {
		return null;
	}

	@Override
	public RSParamComposition getRSParamComposition(int id) {
		return null;
	}

	@Override
	public void setMouseLastPressedMillis(long time) {

	}

	@Override
	public int getRootWidgetCount() {
		return 0;
	}

	@Override
	public int getWidgetClickX() {
		return 0;
	}

	@Override
	public int getWidgetClickY() {
		return 0;
	}

	@Override
	public int getStaffModLevel() {
		return 0;
	}

	@Override
	public int getTradeChatMode() {
		return 0;
	}

	@Override
	public int getPublicChatMode() {
		return 0;
	}

	@Override
	public int getClientType() {
		return 0;
	}

	@Override
	public boolean isOnMobile() {
		return false;
	}

	@Override
	public boolean hadFocus() {
		return false;
	}

	@Override
	public int getMouseCrossColor() {
		return 0;
	}

	@Override
	public void setMouseCrossColor(int color) {

	}

	@Override
	public int getLeftClickOpensMenu() {
		return 0;
	}

	@Override
	public boolean getShowMouseOverText() {
		return false;
	}

	@Override
	public void setShowMouseOverText(boolean showMouseOverText) {

	}

	@Override
	public int[] getDefaultRotations() {
		return new int[0];
	}

	@Override
	public boolean getShowLoadingMessages() {
		return false;
	}

	@Override
	public void setShowLoadingMessages(boolean showLoadingMessages) {

	}

	@Override
	public void setStopTimeMs(long time) {

	}

	@Override
	public void clearLoginScreen(boolean shouldClear) {

	}

	@Override
	public void setLeftTitleSprite(SpritePixels background) {

	}

	@Override
	public void setRightTitleSprite(SpritePixels background) {

	}

	@Override
	public RSBuffer newBuffer(byte[] bytes) {
		return null;
	}

	@Override
	public RSVarbitComposition newVarbitDefinition() {
		return null;
	}

	@Override
	public boolean[] getPressedKeys() {
		return null;
	}

	public boolean lowMemoryMusic = false;
	@Override
	public void setLowMemory(boolean lowMemory) {
		this.lowMem = lowMemory;
	}

	@Override
	public void setSceneLowMemory(boolean lowMemory) {
		MapRegion.lowMem = lowMemory;
		SceneGraph.lowMem = lowMemory;
		Rasterizer3D.lowMem = lowMemory;
		((TextureProvider)Rasterizer3D.textureLoader).setTextureSize(Rasterizer3D.lowMem ? 64 : 128);

	}

	@Override
	public void setAudioHighMemory(boolean highMemory) {
		lowMemoryMusic = highMemory;
	}

	@Override
	public void setObjectDefinitionLowDetail(boolean lowDetail) {
		ObjectDefinition.lowMem = lowDetail;
	}


	@Override
	public boolean isFriended(String name, boolean mustBeLoggedIn) {
		return false;
	}

	@Override
	public RSFriendsChat getFriendsChatManager() {
		return null;
	}

	@Override
	public RSLoginType getLoginType() {
		return null;
	}

	@Override
	public RSUsername createName(String name, RSLoginType type) {
		return null;
	}

	@Override
	public int rs$getVarbit(int varbitId) {
		return 0;
	}

	@Override
	public RSEvictingDualNodeHashTable getVarbitCache() {
		return null;
	}

	@Override
	public FriendContainer getFriendContainer() {
		return null;
	}

	@Override
	public NameableContainer<Ignore> getIgnoreContainer() {
		return null;
	}

	@Override
	public RSClientPreferences getPreferences() {
		return null;
	}

	@Override
	public int getCameraPitchTarget() {
		return camAngleYY;
	}

	@Override
	public void setCameraPitchTarget(int pitch) {

	}

	@Override
	public void setPitchSin(int v) {
		scene.camUpDownY = v;
	}

	@Override
	public void setPitchCos(int v) {
		scene.camUpDownX = v;
	}

	@Override
	public void setYawSin(int v) {
		scene.camLeftRightY = v;
	}

	@Override
	public void setYawCos(int v) {
		scene.camLeftRightX = v;
	}

	static int lastPitch = 128;
	static int lastPitchTarget = 128;

	@Override
	public void setCameraPitchRelaxerEnabled(boolean enabled) {

		if (pitchRelaxEnabled == enabled)
		{
			return;
		}
		pitchRelaxEnabled = enabled;
		if (!enabled)
		{
			int pitch = getCameraPitchTarget();
			if (pitch > STANDARD_PITCH_MAX)
			{
				setCameraPitchTarget(STANDARD_PITCH_MAX);
			}
		}

	}

	@Override
	public void setInvertYaw(boolean state) {
		invertYaw = state;
	}

	@Override
	public void setInvertPitch(boolean state) {
		invertPitch = state;
	}

	@Override
	public RSWorldMap getRenderOverview() {
		return null;
	}

	private static boolean stretchedEnabled;

	private static boolean stretchedFast;

	private static boolean stretchedIntegerScaling;

	private static boolean stretchedKeepAspectRatio;

	private static double scalingFactor;

	private static Dimension cachedStretchedDimensions;

	private static Dimension cachedRealDimensions;

	@Override
	public boolean isStretchedEnabled()
	{
		return stretchedEnabled;
	}

	@Override
	public void setStretchedEnabled(boolean state)
	{
		stretchedEnabled = state;
	}

	@Override
	public boolean isStretchedFast()
	{
		return stretchedFast;
	}

	@Override
	public void setStretchedFast(boolean state)
	{
		stretchedFast = state;
	}

	@Override
	public void setStretchedIntegerScaling(boolean state)
	{
		stretchedIntegerScaling = state;
	}

	@Override
	public void setStretchedKeepAspectRatio(boolean state)
	{
		stretchedKeepAspectRatio = state;
	}

	@Override
	public void setScalingFactor(int factor)
	{
		scalingFactor = 1 + (factor / 100D);
	}

	@Override
	public double getScalingFactor()
	{
		return scalingFactor;
	}

	@Override
	public Dimension getRealDimensions()
	{
		if (!isStretchedEnabled())
		{
			return getCanvas().getSize();
		}

		if (cachedRealDimensions == null)
		{
			if (isResized())
			{
				Container canvasParent = getCanvas().getParent();

				int parentWidth = canvasParent.getWidth();
				int parentHeight = canvasParent.getHeight();

				int newWidth = (int) (parentWidth / scalingFactor);
				int newHeight = (int) (parentHeight / scalingFactor);

				if (newWidth < Constants.GAME_FIXED_WIDTH || newHeight < Constants.GAME_FIXED_HEIGHT)
				{
					double scalingFactorW = (double)parentWidth / Constants.GAME_FIXED_WIDTH;
					double scalingFactorH = (double)parentHeight / Constants.GAME_FIXED_HEIGHT;
					double scalingFactor = Math.min(scalingFactorW, scalingFactorH);

					newWidth = (int) (parentWidth / scalingFactor);
					newHeight = (int) (parentHeight / scalingFactor);
				}

				cachedRealDimensions = new Dimension(newWidth, newHeight);
			}
			else
			{
				cachedRealDimensions = Constants.GAME_FIXED_SIZE;
			}
		}

		return cachedRealDimensions;
	}

	@Override
	public Dimension getStretchedDimensions()
	{
		if (cachedStretchedDimensions == null)
		{
			Container canvasParent = getCanvas().getParent();

			int parentWidth = canvasParent.getWidth();
			int parentHeight = canvasParent.getHeight();

			Dimension realDimensions = getRealDimensions();

			if (stretchedKeepAspectRatio)
			{
				double aspectRatio = realDimensions.getWidth() / realDimensions.getHeight();

				int tempNewWidth = (int) (parentHeight * aspectRatio);

				if (tempNewWidth > parentWidth)
				{
					parentHeight = (int) (parentWidth / aspectRatio);
				}
				else
				{
					parentWidth = tempNewWidth;
				}
			}

			if (stretchedIntegerScaling)
			{
				if (parentWidth > realDimensions.width)
				{
					parentWidth = parentWidth - (parentWidth % realDimensions.width);
				}
				if (parentHeight > realDimensions.height)
				{
					parentHeight = parentHeight - (parentHeight % realDimensions.height);
				}
			}

			cachedStretchedDimensions = new Dimension(parentWidth, parentHeight);
		}

		return cachedStretchedDimensions;
	}

	@Override
	public void invalidateStretching(boolean resize)
	{
		cachedRealDimensions = null;
		cachedStretchedDimensions = null;

		if (resize && isResized())
		{
			/*
				Tells the game to run resizeCanvas the next frame.

				This is useful when resizeCanvas wouldn't usually run,
				for example when we've only changed the scaling factor
				and we still want the game's canvas to resize
				with regards to the new maximum bounds.
			 */
			setResizeCanvasNextFrame(true);
		}
	}

	@Override
	public void changeWorld(World world) {

	}

	@Override
	public RSWorld createWorld() {
		return null;
	}

	@Override
	public void setAnimOffsetX(int animOffsetX) {

	}

	@Override
	public void setAnimOffsetY(int animOffsetY) {

	}

	@Override
	public void setAnimOffsetZ(int animOffsetZ) {

	}

	@Override
	public RSSpritePixels drawInstanceMap(int z) {
		return null;
	}

	@Override
	public void setMinimapReceivesClicks(boolean minimapReceivesClicks) {

	}

	@Override
	public void runScript(Object... args) {

	}

	@Override
	public ScriptEvent createScriptEvent(Object... args) {
		return null;
	}

	@Override
	public boolean hasHintArrow() {
		return false;
	}

	@Override
	public HintArrowType getHintArrowType() {
		return null;
	}

	@Override
	public void clearHintArrow() {

	}

	@Override
	public void setHintArrow(WorldPoint point) {

	}

	@Override
	public void setHintArrow(net.runelite.api.Player player) {

	}

	@Override
	public void setHintArrow(net.runelite.api.NPC npc) {

	}

	@Override
	public WorldPoint getHintArrowPoint() {
		return null;
	}

	@Override
	public net.runelite.api.Player getHintArrowPlayer() {
		return null;
	}

	@Override
	public net.runelite.api.NPC getHintArrowNpc() {
		return null;
	}

	@Override
	public boolean isInterpolatePlayerAnimations() {
		return false;
	}

	@Override
	public void setInterpolatePlayerAnimations(boolean interpolate) {

	}

	@Override
	public boolean isInterpolateNpcAnimations() {
		return false;
	}

	@Override
	public void setInterpolateNpcAnimations(boolean interpolate) {

	}

	@Override
	public boolean isInterpolateObjectAnimations() {
		return false;
	}

	@Override
	public void setInterpolateObjectAnimations(boolean interpolate) {

	}

	@Override
	public boolean isInterpolateWidgetAnimations() {
		return false;
	}

	@Override
	public void setInterpolateWidgetAnimations(boolean interpolate) {

	}

	@Override
	public boolean isInInstancedRegion() {
		return false; //TODO:
	}

	@Override
	public int getItemPressedDuration() {
		return 0;
	}

	@Override
	public void setItemPressedDuration(int duration) {

	}

	@Override
	public int getFlags() {
		return 0;
	}


	@Override
	public void setIsHidingEntities(boolean state)
	{

	}

	@Override
	public void setOthersHidden(boolean state)
	{

	}

	@Override
	public void setOthersHidden2D(boolean state)
	{

	}

	@Override
	public void setFriendsHidden(boolean state)
	{

	}

	@Override
	public void setFriendsChatMembersHidden(boolean state)
	{

	}

	@Override
	public void setIgnoresHidden(boolean state)
	{

	}

	@Override
	public void setLocalPlayerHidden(boolean state)
	{

	}

	@Override
	public void setLocalPlayerHidden2D(boolean state)
	{

	}

	@Override
	public void setNPCsHidden(boolean state)
	{

	}

	@Override
	public void setNPCsHidden2D(boolean state)
	{

	}

	@Override
	public void setHideSpecificPlayers(List<String> players)
	{

	}


	@Override
	public void setHiddenNpcIndices(List<Integer> npcIndices)
	{

	}

	@Override
	public List<Integer> getHiddenNpcIndices()
	{
		return null;
	}

	@Override
	public void setPetsHidden(boolean state)
	{

	}

	@Override
	public void setAttackersHidden(boolean state)
	{

	}

	@Override
	public void setProjectilesHidden(boolean state)
	{

	}

	@Override
	public void setDeadNPCsHidden(boolean state)
	{

	}

	@Override
	public void addHiddenNpcName(String npc)
	{

	}

	@Override
	public void removeHiddenNpcName(String npc)
	{

	}


	@Override
	public void setBlacklistDeadNpcs(Set<Integer> blacklist) {

	}

	public boolean addEntityMarker(int x, int y, RSRenderable entity)
	{

		return true;
	}

	public boolean shouldDraw(Object entity, boolean drawingUI)
	{


		return true;
	}

	private static boolean invertPitch;
	private static boolean invertYaw;

	@Override
	public RSCollisionMap[] getCollisionMaps() {
		return collisionMaps;
	}

	@Override
	public int getPlayerIndexesCount() {
		return 0;
	}

	@Override
	public int[] getPlayerIndices() {
		return new int[0];
	}

	@Override
	public int[] getBoostedSkillLevels() {
		return null;
	}

	@Override
	public int[] getRealSkillLevels() {
		return null;
	}

	@Override
	public int[] getSkillExperiences() {
		return null;
	}

	@Override
	public int[] getChangedSkills() {
		return new int[0];
	}

	@Override
	public int getChangedSkillsCount() {
		return 0;
	}

	@Override
	public void setChangedSkillsCount(int i) {

	}

	@Override
	public void queueChangedSkill(Skill skill) {
	}

	@Override
	public Map<Integer, SpritePixels> getSpriteOverrides() {
		return null;
	}

	@Override
	public Map<Integer, SpritePixels> getWidgetSpriteOverrides() {
		return null;
	}

	@Override
	public void setCompass(SpritePixels SpritePixels) {

	}

	@Override
	public RSEvictingDualNodeHashTable getWidgetSpriteCache() {
		return null;
	}

	int tickCount;

	@Override
	public int getTickCount() {
		return tickCount;
	}

	@Override
	public void setTickCount(int tickCount) {
		this.tickCount = tickCount;
	}


	@Override
	public void setInventoryDragDelay(int delay) {

	}


	@Override
	public boolean isHdMinimapEnabled() {
		return scene.hdMinimapEnabled;
	}

	@Override
	public void setHdMinimapEnabled(boolean enabled) {
		scene.hdMinimapEnabled = enabled;
	}

	public static boolean LoginSplash = false;
	@Override
	public void setLoginSplashEnabled(boolean enabled) {
		LoginSplash= enabled;
	}

	@Override
	public boolean isLoginSplashEnabled() {
		return LoginSplash;
	}

	@Override
	public boolean isNPCTileEnabled() {
		return npctile;
	}

	public static boolean npctile = false;
	@Override
	public void setNPCTileEnabled(boolean enabled) {
		npctile= enabled;
	}

	@Override
	public boolean isOldframeEnabled() {
		return oldframe;
	}
	public static boolean oldframe = false;
	@Override
	public void setOldframeEnabled(boolean enabled) {
		oldframe= enabled;
		loadTabArea();
		drawTabArea();
	}

	@Override
	public boolean isgroundnamesEnabled() {
		return groundnames;
	}
	public static boolean groundnames = false;
	@Override
	public void setgroundnamesEnabled(boolean enabled) {
		groundnames= enabled;

	}


	@Override
	public boolean isPlayerTileEnabled() {
		return playertile;
	}
	public static boolean playertile = false;
	@Override
	public void setPlayerTileEnabled(boolean enabled) {
		playertile= enabled;
	}


	@Override
	public boolean isPlayerNamesEnabled() {
		return playernames;
	}
	public static boolean playernames = false;
	@Override
	public void setPlayerNamesEnabled(boolean enabled) {
		playernames= enabled;
	}



	@Override
	public boolean isNPCNamesEnabled() {
		return npcnames;
	}
	public static boolean npcnames = false;
	@Override
	public void setNPCNamesEnabled(boolean enabled) {
		npcnames = enabled;
	}


	@Override
	public boolean isInventoryMenuEnabled() {
		return inventorymenu;
	}
	public static boolean inventorymenu = false;// Do not change from false. let runelite do the settings saving / loading stuff.
	@Override
	public void setInventoryMenuEnabled(boolean enabled) {
		inventorymenu= enabled;
	}


	@Override
	public boolean isstatoverlayEnabled() {
		return statoverlay;
	}
	public static boolean statoverlay = false;
	@Override
	public void setstatoverlayEnabled(boolean enabled) {
		statoverlay= enabled;
	}
	@Override
	public boolean isStatusBarsEnabled() {
		return statusbars;
	}
	public static boolean statusbars = false;
	@Override
	public void setStatusBarsEnabled(boolean enabled) {
		statusbars= enabled;
	}


	@Override
	public boolean isbroadcastsEnabled() {
		return broadcasts;
	}
	public static boolean broadcasts = false;
	@Override
	public void setbroadcastsEnabled(boolean enabled) {
		broadcasts= enabled;
	}




	@Override
	public EnumSet<WorldType> getWorldType() {
		return EnumSet.of(WorldType.MEMBERS);
	}

	@Override
	public int getOculusOrbState() {
		return 0;
	}

	@Override
	public void setOculusOrbState(int state) {

	}

	@Override
	public void setOculusOrbNormalSpeed(int speed) {

	}

	@Override
	public int getOculusOrbFocalPointX() {
		return xCameraPos;
	}

	@Override
	public int getOculusOrbFocalPointY() {
		return yCameraPos;
	}

	@Override
	public void setOculusOrbFocalPointX(int xPos) {

	}

	@Override
	public void setOculusOrbFocalPointY(int yPos) {

	}

	@Override
	public RSTileItem getLastItemDespawn() {
		return null;
	}

	@Override
	public void setLastItemDespawn(RSTileItem lastItemDespawn) {

	}

	@Override
	public void openWorldHopper() {

	}

	@Override
	public void hopToWorld(World world) {

	}

	@Override
	public void setSkyboxColor(int skyboxColor) {
		scene.skyboxColor = skyboxColor;
	}

	@Override
	public int getSkyboxColor() {
		return scene.skyboxColor;
	}

	@Override
	public boolean isGpu() {
		return gpu;
	}

	@Override
	public void setGpu(boolean gpu) {
		this.gpu = gpu;
	}


	@Override
	public int get3dZoom() {
		return cameraZoom;
	}

	@Override
	public void set3dZoom(int zoom) {
		this.cameraZoom = zoom;
	}

	@Override
	public int getCenterX() {
		return getViewportWidth() / 2;
	}

	@Override
	public int getCenterY() {
		return getViewportHeight() / 2;
	}

	@Override
	public int getCameraX2() {
		return SceneGraph.xCameraPos;
	}

	@Override
	public int getCameraY2() {
		return SceneGraph.zCameraPos;
	}

	@Override
	public int getCameraZ2() {
		return SceneGraph.yCameraPos;
	}

	@Override
	public RSTextureProvider getTextureProvider() {
		return ((TextureProvider)Rasterizer3D.textureLoader);
	}

	@Override
	public int[][] getOccupiedTilesTick() {
		return new int[0][];
	}

	@Override
	public RSEvictingDualNodeHashTable getObjectDefinitionModelsCache() {
		return null;
	}

	@Override
	public int getCycle() {
		return scene.cycle;
	}

	@Override
	public void setCycle(int cycle) {
		scene.cycle = cycle;
	}

	@Override
	public boolean[][][][] getVisibilityMaps() {
		return scene.visibilityMap;
	}

	@Override
	public RSEvictingDualNodeHashTable getCachedModels2() {
		return null;
	}

	@Override
	public void setRenderArea(boolean[][] renderArea) {
		scene.renderArea = renderArea;
	}

	@Override
	public void setCameraX2(int cameraX2) {
		scene.xCameraPos = cameraX2;
	}

	@Override
	public void setCameraY2(int cameraY2) {
		scene.zCameraPos = cameraY2;
	}

	@Override
	public void setCameraZ2(int cameraZ2) {
		scene.yCameraPos = cameraZ2;
	}

	@Override
	public void setScreenCenterX(int screenCenterX) {
		scene.screenCenterX = screenCenterX;
	}

	@Override
	public void setScreenCenterZ(int screenCenterZ) {
		scene.screenCenterZ = screenCenterZ;
	}

	@Override
	public void setScenePlane(int scenePlane) {
		scene.currentRenderPlane = scenePlane;
	}

	@Override
	public void setMinTileX(int i) {
		scene.minTileX = i;
	}

	@Override
	public void setMinTileZ(int i) {
		scene.minTileZ = i;
	}

	@Override
	public void setMaxTileX(int i) {
		scene.maxTileX = i;
	}

	@Override
	public void setMaxTileZ(int i) {
		scene.maxTileZ = i;
	}

	@Override
	public int getTileUpdateCount() {
		return scene.tileUpdateCount;
	}

	@Override
	public void setTileUpdateCount(int tileUpdateCount) {
		scene.tileUpdateCount = tileUpdateCount;
	}

	@Override
	public boolean getViewportContainsMouse() {
		return false;
	}

	@Override
	public int getRasterizer3D_clipMidX2() {
		return Rasterizer2D.viewportCenterX;
	}

	@Override
	public int getRasterizer3D_clipNegativeMidX() {
		return -Rasterizer2D.viewportCenterX;
	}

	@Override
	public int getRasterizer3D_clipNegativeMidY() {
		return -Rasterizer2D.viewportCenterY;
	}

	@Override
	public int getRasterizer3D_clipMidY2() {
		return Rasterizer2D.viewportCenterY;
	}

	@Override
	public void checkClickbox(net.runelite.api.Model model, int orientation, int pitchSin, int pitchCos, int yawSin,
							  int yawCos, int x, int y, int z, long hash) {

	}

	@Override
	public RSWidget getIf1DraggedWidget() {
		return null;
	}

	@Override
	public int getIf1DraggedItemIndex() {
		return 0;
	}

	@Override
	public void setSpellSelected(boolean selected) {

	}

	@Override
	public RSEnumComposition getRsEnum(int id) {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getItemCompositionCache() {
		return null;
	}

	@Override
	public RSSpritePixels[] getCrossSprites() {
		return null;
	}

	@Override
	public EnumComposition getEnum(int id) {
		return null;
	}

	@Override
	public void draw2010Menu(int alpha) {

	}

	@Override
	public int[] getGraphicsPixels() {
		return null;
	}

	@Override
	public int getGraphicsPixelsWidth() {
		return 0;
	}

	@Override
	public int getGraphicsPixelsHeight() {
		return 0;
	}

	@Override
	public void rasterizerFillRectangle(int x, int y, int w, int h, int rgb) {
		Rasterizer2D.drawBox(x,y,w,h,rgb);
	}

	@Override
	public int getStartX() {
		return 0;
	}

	@Override
	public int getStartY() {
		return 0;
	}

	@Override
	public int getEndX() {
		return 0;
	}

	@Override
	public int getEndY() {
		return 0;
	}

	@Override
	public void drawOriginalMenu(int alpha) {

	}

	@Override
	public void resetHealthBarCaches() {

	}

	@Override
	public boolean getRenderSelf() {
		return false;
	}

	@Override
	public void setRenderSelf(boolean enabled) {

	}

	@Override
	public void invokeMenuAction(String option, String target, int identifier, int opcode, int param0, int param1) {

	}

	@Override
	public RSMouseRecorder getMouseRecorder() {
		return null;
	}

	@Override
	public void setPrintMenuActions(boolean b) {

	}

	@Override
	public String getSelectedSpellName() {
		return null;
	}

	@Override
	public void setSelectedSpellName(String name) {

	}

	@Override
	public boolean getSpellSelected() {
		return false;
	}

	@Override
	public RSSoundEffect getTrack(RSAbstractArchive indexData, int id, int var0) {
		return null;
	}

	@Override
	public RSRawPcmStream createRawPcmStream(RSRawSound audioNode, int var0, int volume) {
		return null;
	}

	@Override
	public RSPcmStreamMixer getSoundEffectAudioQueue() {
		return null;
	}

	@Override
	public RSArchive getIndexCache4() {
		return null;
	}

	@Override
	public RSDecimator getSoundEffectResampler() {
		return null;
	}

	@Override
	public void setMusicTrackVolume(int volume) {

	}

	@Override
	public void setViewportWalking(boolean viewportWalking) {

	}

	@Override
	public void playMusicTrack(int var0, RSAbstractArchive var1, int var2, int var3, int var4, boolean var5) {

	}

	@Override
	public RSMidiPcmStream getMidiPcmStream() {
		return null;
	}

	@Override
	public int getCurrentTrackGroupId() {
		return 0;
	}

	@Override
	public String getSelectedSpellActionName() {
		return null;
	}

	@Override
	public int getSelectedSpellFlags() {
		return 0;
	}

	@Override
	public void setHideFriendAttackOptions(boolean yes) {

	}

	@Override
	public void setHideFriendCastOptions(boolean yes) {

	}

	@Override
	public void setHideClanmateAttackOptions(boolean yes) {
	}

	@Override
	public void setHideClanmateCastOptions(boolean yes) {

	}

	@Override
	public void setUnhiddenCasts(Set<String> casts) {

	}

	@Override
	public void addFriend(String name) {

	}

	@Override
	public void removeFriend(String name) {

	}

	@Override
	public void setModulus(BigInteger modulus) {

	}

	@Override
	public BigInteger getModulus() {
		return null;
	}

	@Override
	public int getItemCount() {
		return 0;
	}

	@Override
	public void setAllWidgetsAreOpTargetable(boolean value) {

	}

	@Override
	public void insertMenuItem(String action, String target, int opcode, int identifier, int argument1, int argument2,
							   boolean forceLeftClick) {

	}

	@Override
	public void setSelectedItemID(int id) {

	}

	@Override
	public int getSelectedItemWidget() {
		return 0;
	}

	@Override
	public void setSelectedItemWidget(int widgetID) {

	}

	@Override
	public int getSelectedItemSlot() {
		return 0;
	}

	@Override
	public void setSelectedItemSlot(int idx) {

	}

	@Override
	public int getSelectedSpellWidget() {
		return 0;
	}

	@Override
	public int getSelectedSpellChildIndex() {
		return 0;
	}

	@Override
	public void setSelectedSpellWidget(int widgetID) {

	}

	@Override
	public void setSelectedSpellChildIndex(int index) {

	}

	@Override
	public void scaleSprite(int[] canvas, int[] pixels, int color, int pixelX, int pixelY, int canvasIdx,
							int canvasOffset, int newWidth, int newHeight, int pixelWidth, int pixelHeight, int oldWidth) {

	}

	@Override
	public void promptCredentials(boolean clearPass) {

	}

	@Override
	public RSVarpDefinition getVarpDefinition(int id) {
		return null;
	}

	@Override
	public RSTileItem newTileItem() {
		return null;
	}

	@Override
	public RSNodeDeque newNodeDeque() {
		return null;
	}

	@Override
	public void updateItemPile(int localX, int localY) {

	}

	@Override
	public void setHideDisconnect(boolean dontShow) {

	}

	@Override
	public void setTempMenuEntry(MenuEntry entry) {

	}

	@Override
	public void setShowMouseCross(boolean show) {

	}

	@Override
	public int getDraggedWidgetX() {
		return 0;
	}

	@Override
	public int getDraggedWidgetY() {
		return 0;
	}

	@Override
	public int[] getChangedSkillLevels() {
		return new int[0];
	}

	@Override
	public void setMouseIdleTicks(int cycles) {
		MouseHandler.idleCycles = cycles;
	}

	@Override
	public void setKeyboardIdleTicks(int cycles) {
		KeyHandler.idleCycles = cycles;
	}

	@Override
	public void setGeSearchResultCount(int count) {
	}

	@Override
	public void setGeSearchResultIds(short[] ids) {

	}

	@Override
	public void setGeSearchResultIndex(int index) {

	}

	@Override
	public void setComplianceValue(String key, boolean value) {

	}

	@Override
	public boolean getComplianceValue(String key) {
		return false;
	}

	@Override
	public boolean isMirrored() {
		return false;
	}

	@Override
	public void setMirrored(boolean isMirrored) {

	}

	@Override
	public boolean isComparingAppearance() {
		return false;
	}

	@Override
	public void setComparingAppearance(boolean comparingAppearance) {

	}

	@Override
	public void setLoginScreen(SpritePixels pixels) {

	}

	@Override
	public void setShouldRenderLoginScreenFire(boolean val) {

	}

	@Override
	public boolean shouldRenderLoginScreenFire() {
		return false;
	}

	@Override
	public boolean isKeyPressed(int keycode) {
		return false;
	}

	@Override
	public int getFollowerIndex() {
		return 0;
	}

	@Override
	public int isItemSelected() {
		return 0;
	}

	@Override
	public String getSelectedItemName() {
		return null;
	}

	@Override
	public RSWidget getMessageContinueWidget() {
		return null;
	}

	@Override
	public void setMusicPlayerStatus(int var0) {

	}

	@Override
	public void setMusicTrackArchive(RSAbstractArchive var0) {

	}

	@Override
	public void setMusicTrackGroupId(int var0) {

	}

	@Override
	public void setMusicTrackFileId(int var0) {

	}

	@Override
	public void setMusicTrackBoolean(boolean var0) {

	}

	@Override
	public void setPcmSampleLength(int var0) {

	}

	@Override
	public int[] getChangedVarps() {
		return new int[0];
	}

	@Override
	public int getChangedVarpCount() {
		return 0;
	}

	@Override
	public void setChangedVarpCount(int changedVarpCount) {

	}

	@Override
	public void setOutdatedScript(String outdatedScript) {

	}

	@Override
	public List<String> getOutdatedScripts() {
		return null;
	}

	@Override
	public RSFrames getFrames(int frameId) {
		return null;
	}

	@Override
	public RSSpritePixels getMinimapSprite() {
		return minimapImage;
	}

	@Override
	public void setMinimapSprite(SpritePixels spritePixels) {

	}

	@Override
	public void drawObject(int z, int x, int y, int randomColor1, int randomColor2) {

	}

	@Override
	public RSScriptEvent createScriptEvent() {
		return null;
	}

	@Override
	public void runScript(RSScriptEvent ev, int ex, int var2) {

	}

	@Override
	public void setHintArrowTargetType(int value) {
		
	}

	@Override
	public int getHintArrowTargetType() {
		return 0;
	}

	@Override
	public void setHintArrowX(int value) {
		
	}

	@Override
	public int getHintArrowX() {
		return 0;
	}

	@Override
	public void setHintArrowY(int value) {
		
	}

	@Override
	public int getHintArrowY() {
		return 0;
	}

	@Override
	public void setHintArrowOffsetX(int value) {
		
	}

	@Override
	public void setHintArrowOffsetY(int value) {
		
	}

	@Override
	public void setHintArrowNpcTargetIdx(int value) {
		
	}

	@Override
	public int getHintArrowNpcTargetIdx() {
		return 0;
	}

	@Override
	public void setHintArrowPlayerTargetIdx(int value) {
		
	}

	@Override
	public int getHintArrowPlayerTargetIdx() {
		return 0;
	}

	@Override
	public RSSequenceDefinition getSequenceDefinition(int id) {
		return null;
	}

	@Override
	public RSIntegerNode newIntegerNode(int contents) {
		return null;
	}

	@Override
	public RSObjectNode newObjectNode(Object contents) {
		return null;
	}

	@Override
	public RSIterableNodeHashTable newIterableNodeHashTable(int size) {
		return null;
	}

	@Override
	public RSVarbitComposition getVarbitComposition(int id) {
		return null;
	}

	@Override
	public RSAbstractArchive getSequenceDefinition_skeletonsArchive() {
		return null;
	}

	@Override
	public RSAbstractArchive getSequenceDefinition_archive() {
		return null;
	}

	@Override
	public RSAbstractArchive getSequenceDefinition_animationsArchive() {
		return null;
	}

	@Override
	public RSAbstractArchive getNpcDefinition_archive() {
		return null;
	}

	@Override
	public RSAbstractArchive getObjectDefinition_modelsArchive() {
		return null;
	}

	@Override
	public RSAbstractArchive getObjectDefinition_archive() {
		return null;
	}

	@Override
	public RSAbstractArchive getItemDefinition_archive() {
		return null;
	}

	@Override
	public RSAbstractArchive getKitDefinition_archive() {
		return null;
	}

	@Override
	public RSAbstractArchive getKitDefinition_modelsArchive() {
		return null;
	}

	@Override
	public RSAbstractArchive getSpotAnimationDefinition_archive() {
		return null;
	}

	@Override
	public RSAbstractArchive getSpotAnimationDefinition_modelArchive() {
		return null;
	}

	@Override
	public RSBuffer createBuffer(byte[] initialBytes) {
		return null;
	}

	@Override
	public RSSceneTilePaint createSceneTilePaint(int swColor, int seColor, int neColor, int nwColor, int texture, int rgb, boolean isFlat) {
		return null;
	}

	@Override
	public long[] getCrossWorldMessageIds() {
		return null;
	}

	@Override
	public int getCrossWorldMessageIdsIndex() {
		return 0;
	}

	@Override
	public RSClanChannel[] getCurrentClanChannels() {
		return new RSClanChannel[0];
	}

	@Override
	public RSClanSettings[] getCurrentClanSettingsAry() {
		return new RSClanSettings[0];
	}

	@Override
	public RSClanChannel getClanChannel() {
		return null;
	}

	@Override
	public RSClanChannel getGuestClanChannel() {
		return null;
	}

	@Override
	public RSClanSettings getClanSettings() {
		return null;
	}

	@Override
	public RSClanSettings getGuestClanSettings() {
		return null;
	}

	@Override
	public ClanRank getClanRankFromRs(int rank) {
		return null;
	}

	@Override
	public RSIterableNodeHashTable readStringIntParameters(RSBuffer buffer, RSIterableNodeHashTable table) {
		return null;
	}

	@Override
	public int getRndHue() {
		return 0;
	}

	@Override
	public short[][][] getTileUnderlays() {
		return scene.getUnderlayIds();
	}

	@Override
	public short[][][] getTileOverlays() {
		return scene.getOverlayIds();
	}

	@Override
	public byte[][][] getTileShapes() {
		return scene.getTileShapes();
	}

	@Override
	public RSSpotAnimationDefinition getSpotAnimationDefinition(int id) {
		return null;
	}

	@Override
	public RSModelData getModelData(RSAbstractArchive var0, int var1, int var2) {
		return null;
	}

	@Override
	public boolean isCameraLocked() {
		return false;
	}

	@Override
	public boolean getCameraPitchRelaxerEnabled() {
		return pitchRelaxEnabled;
	}

	public static boolean unlockedFps;
	public long delayNanoTime;
	public long lastNanoTime;
	public static double tmpCamAngleY;
	public static double tmpCamAngleX;

	@Override
	public boolean isUnlockedFps() {
		return unlockedFps;
	}

	@Override
	public long getUnlockedFpsTarget() {
		return delayNanoTime;
	}

	public void updateCamera()
	{
		if (unlockedFps)
		{
			long nanoTime = System.nanoTime();
			long diff = nanoTime - this.lastNanoTime;
			this.lastNanoTime = nanoTime;

			if (this.getGameState() == GameState.LOGGED_IN)
			{
				this.interpolateCamera(diff);
			}
		}
	}

	public static final int STANDARD_PITCH_MIN = 128;
	public static final int STANDARD_PITCH_MAX = 383;
	public static final int NEW_PITCH_MAX = 512;

	public void interpolateCamera(long var1)
	{
		double angleDX = diffToDangle(getCamAngleDY(), var1);
		double angleDY = diffToDangle(getCamAngleDX(), var1);

		tmpCamAngleY += angleDX / 2;
		tmpCamAngleX += angleDY / 2;
		tmpCamAngleX = Doubles.constrainToRange(tmpCamAngleX, Perspective.UNIT * STANDARD_PITCH_MIN, getCameraPitchRelaxerEnabled() ? Perspective.UNIT * NEW_PITCH_MAX : Perspective.UNIT * STANDARD_PITCH_MAX);

		int yaw = toCameraPos(tmpCamAngleY);
		int pitch = toCameraPos(tmpCamAngleX);

		setCameraYawTarget(yaw);
		setCameraPitchTarget(pitch);
	}

	public static int toCameraPos(double var0)
	{
		return (int) (var0 / Perspective.UNIT) & 2047;
	}


	public static double diffToDangle(int var0, long var1)
	{
		double var2 = var0 * Perspective.UNIT;
		double var3 = (double) var1 / 2.0E7D;

		return var2 * var3;
	}

	@Override
	public void posToCameraAngle(int var0, int var1) {
		tmpCamAngleY = var0 * Perspective.UNIT;
		tmpCamAngleX = var1 * Perspective.UNIT;
	}

	static void onCameraPitchTargetChanged(int idx)
	{
		int newPitch = instance.getCameraPitchTarget();
		int pitch = newPitch;
		if (pitchRelaxEnabled)
		{
			// This works because the vanilla camera movement code only moves %2
			if (lastPitchTarget > STANDARD_PITCH_MAX && newPitch == STANDARD_PITCH_MAX)
			{
				pitch = lastPitchTarget;
				if (pitch > NEW_PITCH_MAX)
				{
					pitch = NEW_PITCH_MAX;
				}
				instance.setCameraPitchTarget(pitch);
			}
		}
		lastPitchTarget = pitch;
	}

	static void onCameraPitchChanged(int idx)
	{
		int newPitch = instance.getCameraPitch();
		int pitch = newPitch;
		if (pitchRelaxEnabled)
		{
			// This works because the vanilla camera movement code only moves %2
			if (lastPitch > STANDARD_PITCH_MAX && newPitch == STANDARD_PITCH_MAX)
			{
				pitch = lastPitch;
				if (pitch > NEW_PITCH_MAX)
				{
					pitch = NEW_PITCH_MAX;
				}
				instance.setCameraPitch(pitch);
			}
		}
		lastPitch = pitch;
	}

	@Override
	public RSClanChannel getClanChannel(int clanId) {
		return null;
	}

	@Override
	public RSClanSettings getClanSettings(int clanId) {
		return null;
	}

	@Override
	public void setUnlockedFps(boolean unlock) {
		unlockedFps = unlock;
	}

	@Override
	public void setUnlockedFpsTarget(int fps) {
		delayNanoTime = fps;
	}

	@Override
	public net.runelite.api.Deque<AmbientSoundEffect> getAmbientSoundEffects() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getEnumDefinitionCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getFloorUnderlayDefinitionCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getFloorOverlayDefinitionCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getHitSplatDefinitionCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getHitSplatDefinitionSpritesCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getHitSplatDefinitionDontsCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getInvDefinitionCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getItemDefinitionModelsCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getItemDefinitionSpritesCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getKitDefinitionCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getNpcDefinitionCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getNpcDefinitionModelsCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getObjectDefinitionCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getObjectDefinitionModelDataCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getObjectDefinitionEntitiesCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getParamDefinitionCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getPlayerAppearanceModelsCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getSequenceDefinitionCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getSequenceDefinitionFramesCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getSequenceDefinitionModelsCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getSpotAnimationDefinitionCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getSpotAnimationDefinitionModlesCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getVarcIntCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getVarpDefinitionCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getModelsCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getFontsCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getSpriteMasksCache() {
		return null;
	}

	@Override
	public RSEvictingDualNodeHashTable getSpritesCache() {
		return null;
	}

	@Override
	public RSIterableNodeHashTable createIterableNodeHashTable(int size) {
		return null;
	}

	@Override
	public int getSceneMaxPlane() {
		return 0;
	}

	@Override
	public void setIdleTimeout(int id) {

	}

	@Override
	public int getIdleTimeout() {
		return 0;
	}

	boolean canZoomMap = false;

	@Override
	public void setMinimapZoom(boolean minimapZoom) {
		this.canZoomMap = minimapZoom;
	}

	@Override
	public double getMinimapZoom() {
		return minimapZoom;
	}

	@Override
	public boolean isMinimapZoom() {
		return canZoomMap;
	}

	@Override
	public void setMinimapZoom(double zoom) {
		minimapZoom = (int) zoom;
	}


}
