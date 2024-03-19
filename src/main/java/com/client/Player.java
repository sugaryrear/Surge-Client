package com.client;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import com.client.definitions.AnimationDefinition;
import com.client.definitions.ItemDefinition;
import com.client.definitions.NpcDefinition;
import com.client.definitions.GraphicsDefinition;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.rs.api.*;
import org.jetbrains.annotations.Nullable;

public final class Player extends Entity implements RSPlayer {

	@Override
	public Model getRotatedModel() {
		if (!visible)
			return null;
		Model model = get_model();
		if (model == null)
			return null;
		model.calculateBoundsCylinder();
		super.height = model.modelBaseY;
		model.singleTile = true;
		if (lowmem)
			return model;
		if (super.spotanim != -1 && super.spotanimframe_index != -1) {
			GraphicsDefinition spotAnim = GraphicsDefinition.cache[super.spotanim];
			Model model_3 = spotAnim.get_transformed_model(super.spotanimframe_index);
			// Added
			if(model_3 != null) {
				model_3.offsetBy(0, -super.spotanim_height, 0);
				Model aclass30_sub2_sub4_sub6_1s[] = {model, model_3};
				model = new Model(aclass30_sub2_sub4_sub6_1s);
			}
		}

		if (mergeloc_model != null) {
			if (Client.update_tick >= mergelocanim_loop_stop)
				mergeloc_model = null;
			if (Client.update_tick >= mergelocanim_loop_start && Client.update_tick < mergelocanim_loop_stop) {
				Model model_1 = mergeloc_model;
				model_1.offsetBy(loc_offset_x - super.x, loc_offset_y - y, loc_offset_z - super.z);
				if (super.getTurnDirection() == 512) {
					model_1.rotate90Degrees();
					model_1.rotate90Degrees();
					model_1.rotate90Degrees();
				} else if (super.getTurnDirection() == 1024) {
					model_1.rotate90Degrees();
					model_1.rotate90Degrees();
				} else if (super.getTurnDirection() == 1536)
					model_1.rotate90Degrees();
				Model aclass30_sub2_sub4_sub6s[] = { model, model_1 };
				model = new Model(aclass30_sub2_sub4_sub6s);
				if (super.getTurnDirection() == 512)
					model_1.rotate90Degrees();
				else if (super.getTurnDirection() == 1024) {
					model_1.rotate90Degrees();
					model_1.rotate90Degrees();
				} else if (super.getTurnDirection() == 1536) {
					model_1.rotate90Degrees();
					model_1.rotate90Degrees();
					model_1.rotate90Degrees();
				}
				model_1.offsetBy(super.x - loc_offset_x, y - loc_offset_y,
						super.z - loc_offset_z);
			}
		}
		model.singleTile = true;
		return model;
	}
	public int usedItemID;
	public String title;
	public String titleColor;

	public void update_player_appearance(Buffer stream) {
		stream.currentPosition = 0;
		gender = stream.get_unsignedbyte();
		title = stream.readString();
		titleColor = stream.readString();
		healthState = stream.get_unsignedbyte();
		headIcon = stream.get_unsignedbyte();
		skullIcon = stream.get_unsignedbyte();
		desc = null;
		team = 0;
		for (int j = 0; j < 12; j++) {
			int k = stream.get_unsignedbyte();
			if (k == 0) {
				equipment[j] = 0;
				continue;
			}
			int i1 = stream.get_unsignedbyte();
			equipment[j] = (k << 8) + i1;
			if (j == 0 && equipment[0] == 65535) {
				desc = NpcDefinition.lookup(stream.readUShort());
				break;
			}
			if (equipment[j] >= 512 && equipment[j] - 512 < ItemDefinition.totalItems) {
				int l1 = ItemDefinition.lookup(equipment[j] - 512).team;
				if (l1 != 0)
					team = l1;
			}
		}

		for (int l = 0; l < 5; l++) {
			int j1 = stream.get_unsignedbyte();
			if (j1 < 0 || j1 >= Client.player_body_colours[l].length)
				j1 = 0;
			player_recolour_destination[l] = j1;
		}
		// ADDED NEW TURNS
		super.readyanim = stream.readUShort();
		if (super.readyanim == 65535)
			super.readyanim = -1;
		super.readyanim_l = stream.readUShort();
		if (super.readyanim_l == 65535)
			super.readyanim_l = -1;
		super.readyanim_r = super.readyanim_l;
		super.walkanim = stream.readUShort();
		if (super.walkanim == 65535)
			super.walkanim = -1;
		super.walkanim_b = stream.readUShort();
		if (super.walkanim_b == 65535)
			super.walkanim_b = -1;
		super.walkanim_l = stream.readUShort();
		if (super.walkanim_l == 65535)
			super.walkanim_l = -1;
		super.walkanim_r = stream.readUShort();
		if (super.walkanim_r == 65535)
			super.walkanim_r = -1;
		super.runanim = stream.readUShort();
		if (super.runanim == 65535)
			super.runanim = -1;
		displayName = stream.readString();
		visible = stream.get_unsignedbyte() == 0;
		combatLevel = stream.get_unsignedbyte();
		rights = PlayerRights.readRightsFromPacket(stream).getRight();
		displayedRights = PlayerRights.getDisplayedRights(rights);
		skill = stream.readUShort();
		aLong1718 = 0L;
		for (int k1 = 0; k1 < 12; k1++) {
			aLong1718 <<= 4;
			if (equipment[k1] >= 256)
				aLong1718 += equipment[k1] - 256;
		}

		if (equipment[0] >= 256)
			aLong1718 += equipment[0] - 256 >> 4;
		if (equipment[1] >= 256)
			aLong1718 += equipment[1] - 256 >> 8;
		for (int i2 = 0; i2 < 5; i2++) {
			aLong1718 <<= 3;
			aLong1718 += player_recolour_destination[i2];
		}

		aLong1718 <<= 1;
		aLong1718 += gender;
	}

	public Model get_model() {
		AnimationDefinition readyanim_seqtype = super.primaryanim != -1 && super.primaryanim_pause == 0
				? AnimationDefinition.anims[super.primaryanim] : null;
		AnimationDefinition walkanim_seqtype =
				(super.secondaryanim == -1 ||
						(super.secondaryanim == super.readyanim && readyanim_seqtype != null))
						? null : AnimationDefinition.anims[super.secondaryanim];
		if (desc != null) {

			Model model = desc.get_animated_entity_model(super.primaryanim_frameindex, readyanim_seqtype, null, super.secondaryanim_frameindex, walkanim_seqtype);
			return model;
		}
//System.out.println("uh here?");
		long playermodel_uid = aLong1718;


		int[] player_equipment = this.equipment;
		if (readyanim_seqtype != null && (readyanim_seqtype.offhand >= 0 || readyanim_seqtype.mainhand >= 0)) {
			player_equipment = new int[12];

			for(int i = 0; i < 12; ++i) {
				player_equipment[i] = this.equipment[i];
			}

			if (readyanim_seqtype.offhand >= 0) {
				playermodel_uid += (long)(readyanim_seqtype.offhand - this.equipment[5] << 40);
				player_equipment[5] = readyanim_seqtype.offhand;
			}

			if (readyanim_seqtype.mainhand >= 0) {
				playermodel_uid += (long)(readyanim_seqtype.mainhand - this.equipment[3] << 48);
				player_equipment[3] = readyanim_seqtype.mainhand;
			}
		}
		Model model_1 = (Model) recent_use.get(playermodel_uid);
		if (model_1 == null) {
			boolean models_invalid = false;
			for (int wearpos = 0; wearpos < 12; wearpos++) {
				int wear = player_equipment[wearpos];
				if (wear >= 0x100 && wear < 512 && !IDK.cache[wear - 0x100].method537()) {
					models_invalid = true;
				}
				if (wear >= 0x200 && !ItemDefinition.lookup(wear - 0x200).isEquippedModelCached(gender)) {
					models_invalid = true;
				}
			}

			if (models_invalid) {
				if (aLong1697 != -1L)
					model_1 = (Model) recent_use.get(aLong1697);
				if (model_1 == null)
					return null;
			}
		}
		if (model_1 == null) {
			Model playerkit_meshes[] = new Model[12];
			int worn = 0;
			for (int slot = 0; slot < 12; slot++) {
				int part = player_equipment[slot];
				if (part >= 0x100 && part < 0x200) {
					Model model_3 = IDK.cache[part - 0x100].get_model();
					if (model_3 != null)
						playerkit_meshes[worn++] = model_3;
				}
				if (part >= 0x200) {
					Model model_4 = ItemDefinition.lookup(part - 0x200).get_equipped_model(gender);
					if (model_4 != null)
						playerkit_meshes[worn++] = model_4;
				}
			}

			model_1 = new Model(worn, playerkit_meshes);
			for (int j3 = 0; j3 < 5; j3++)
				if (player_recolour_destination[j3] != 0) {
					model_1.recolor(Client.player_body_colours[j3][0], Client.player_body_colours[j3][player_recolour_destination[j3]]);
					if (j3 == 1)
						model_1.recolor(Client.player_hair_colours[0], Client.player_hair_colours[player_recolour_destination[j3]]);
				}

			//model_1.apply_label_groups();//TODO remove?
			 model_1.light(64, 850, -30, -50, -30, true);
			//model_1.method479(84, 1000, -90, -580, -90, true);
			recent_use.put(model_1, playermodel_uid);
			aLong1697 = playermodel_uid;
		}
		if (lowmem)
			return model_1;


		if (readyanim_seqtype == null && walkanim_seqtype == null) {
			return model_1;
		} else {
			Model var22;
			if (readyanim_seqtype != null && walkanim_seqtype != null) {
				var22 = readyanim_seqtype.animate_multiple(model_1, super.primaryanim_frameindex, walkanim_seqtype, super.secondaryanim_frameindex);
			} else if (readyanim_seqtype == null) {
				var22 = walkanim_seqtype.animate_either(model_1, super.secondaryanim_frameindex);
			} else {
				var22 = readyanim_seqtype.animate_either(model_1, super.primaryanim_frameindex);
			}

			return var22;
		}
	}

	@Override
	public boolean isVisible() {
		return visible;
	}

	public int privelage;

	public boolean isAdminRights() {
		return hasRights(PlayerRights.ADMINISTRATOR)
				|| hasRights(PlayerRights.OWNER)
				|| hasRights(PlayerRights.GAME_DEVELOPER);
	}

	public boolean hasRightsOtherThan(PlayerRights playerRight) {
		return PlayerRights.hasRightsOtherThan(rights, playerRight);
	}

	public boolean hasRights(PlayerRights playerRights) {
		return PlayerRights.hasRights(rights, playerRights);
	}

	public boolean hasRightsLevel(int rightsId) {
		return PlayerRights.hasRightsLevel(rights, rightsId);
	}

	public boolean hasRightsBetween(int low, int high) {
		return PlayerRights.hasRightsBetween(rights, low, high);
	}

	public Model get_basic_model() {
		if (!visible)
			return null;
		if (desc != null)
			return desc.method160();
		boolean flag = false;
		for (int i = 0; i < 12; i++) {
			int j = equipment[i];
			if (j >= 256 && j < 512 && !IDK.cache[j - 256].method539())
				flag = true;
			if (j >= 512 && !ItemDefinition.lookup(j - 512).isDialogueModelCached(gender))
				flag = true;
		}

		if (flag)
			return null;
		Model aclass30_sub2_sub4_sub6s[] = new Model[12];
		int k = 0;
		for (int l = 0; l < 12; l++) {
			int i1 = equipment[l];
			if (i1 >= 256 && i1 < 512) {
				Model model_1 = IDK.cache[i1 - 256].method540();
				if (model_1 != null)
					aclass30_sub2_sub4_sub6s[k++] = model_1;
			}
			if (i1 >= 512) {
				Model model_2 = ItemDefinition.lookup(i1 - 512).getChatEquipModel(gender);
				if (model_2 != null)
					aclass30_sub2_sub4_sub6s[k++] = model_2;
			}
		}

		Model model = new Model(k, aclass30_sub2_sub4_sub6s);
		for (int j1 = 0; j1 < 5; j1++)
			if (player_recolour_destination[j1] != 0) {
				model.recolor(Client.player_body_colours[j1][0],
						Client.player_body_colours[j1][player_recolour_destination[j1]]);
				if (j1 == 1)
					model.recolor(Client.player_hair_colours[0],
							Client.player_hair_colours[player_recolour_destination[j1]]);
			}

		return model;
	}

	Player() {
		aLong1697 = -1L;
		lowmem = false;
		player_recolour_destination = new int[5];
		visible = false;
		equipment = new int[12];
	}

	public boolean inFlowerPokerArea() {
		int x = getAbsoluteX();
		int y = getAbsoluteY();
		return x >= 3109 && y >= 3504 && x <= 3121 && y <= 3515;
	}

	public boolean inFlowerPokerChatProximity() {
		int x = getAbsoluteX();
		int y = getAbsoluteY();
		return x >= 3106 && y >= 3502 && x <= 3123 && y <= 3517;
	}
	
	public PlayerRights[] getRights() {
		return rights;
	}

	public List<PlayerRights> getDisplayedRights() {
		return displayedRights;
	}
	
	public int getHealthState() {
		return healthState;
	}

	private PlayerRights[] rights = new PlayerRights[] {PlayerRights.PLAYER};
	private List<PlayerRights> displayedRights = new ArrayList<>();
	private long aLong1697;
	public NpcDefinition desc;
	boolean lowmem;
	final int[] player_recolour_destination;
	public int team;
	private int gender;
	public String displayName;
	static ReferenceCache recent_use = new ReferenceCache(260);
	public int combatLevel;
	public int headIcon;
	public int skullIcon;
	public int hintIcon;
	public int mergelocanim_loop_start;
	int mergelocanim_loop_stop;
	int y;
	boolean visible;
	int loc_offset_x;
	int loc_offset_y;
	int loc_offset_z;
	Model mergeloc_model;
	public final int[] equipment;
	private long aLong1718;
	int min_x;
	int min_z;
	int max_x;
	int max_z;
	int skill;
	private int healthState;

	@Nullable
	@Override
	public String getName() {
		return null;
	}

	@Override
	public Actor getInteracting() {
		return null;
	}

	@Override
	public int getHealthRatio() {
		return 0;
	}

	@Override
	public int getHealthScale() {
		return 0;
	}

	@Override
	public WorldPoint getWorldLocation() {
		return null;
	}

	@Override
	public LocalPoint getLocalLocation() {
		return new LocalPoint(this.getX(), this.getY());
	}

	@Override
	public void setIdleRotateLeft(int animationID) {

	}

	@Override
	public void setIdleRotateRight(int animationID) {

	}

	@Override
	public void setWalkAnimation(int animationID) {

	}

	@Override
	public void setWalkRotateLeft(int animationID) {

	}

	@Override
	public void setWalkRotateRight(int animationID) {

	}

	@Override
	public void setWalkRotate180(int animationID) {

	}

	@Override
	public void setRunAnimation(int animationID) {

	}

	@Override
	public Polygon getCanvasTilePoly() {

	int x = 	Client.instance.destX * 128 + 1 * 64;
		int y = 	Client.instance.destY * 128 + 1 * 64;
	//	System.out.println("waypoint: "+Client.instance.localPlayer.waypoint_x+" destx: "+Client.instance.destX);
		LocalPoint thetile = new LocalPoint(x,y);
		return Perspective.getCanvasTilePoly(Client.instance, thetile);
	//	return null;
	}

	@Nullable
	@Override
	public Point getCanvasTextLocation(Graphics2D graphics, String text, int zOffset) {
		return null;
	}

	@Override
	public Point getCanvasImageLocation(BufferedImage image, int zOffset) {
		return null;
	}

	@Override
	public Point getCanvasSpriteLocation(SpritePixels sprite, int zOffset) {
		return null;
	}

	@Override
	public Point getMinimapLocation() {
		return null;
	}

	@Override
	public Shape getConvexHull() {
		return null;
	}

	@Override
	public WorldArea getWorldArea() {
		return null;
	}

	@Override
	public boolean isDead() {
		return false;
	}

	@Override
	public boolean isMoving() {
		return false;
	}

	@Override
	public Polygon[] getPolygons() {
		return new Polygon[0];
	}

	@Nullable
	@Override
	public HeadIcon getOverheadIcon() {
		return null;
	}

	@Nullable
	@Override
	public SkullIcon getSkullIcon() {
		return null;
	}

	@Override
	public boolean isHidden() {
		return false;
	}

	@Override
	public int getRSInteracting() {
		return 0;
	}

	@Override
	public String getOverheadText() {
		return null;
	}

	@Override
	public void setOverheadText(String overheadText) {

	}

	@Override
	public int getX() {
		return this.x;
	}

	@Override
	public int getY() {
		return this.z;
	}

	@Override
	public int[] getPathX() {
		return new int[0];
	}

	@Override
	public int[] getPathY() {
		return new int[0];
	}

	@Override
	public int getAnimation() {
		return 0;
	}

	@Override
	public void setAnimation(int animation) {

	}

	@Override
	public int getAnimationFrame() {
		return 0;
	}

	@Override
	public int getActionFrame() {
		return 0;
	}

	@Override
	public void setAnimationFrame(int frame) {

	}

	@Override
	public void setActionFrame(int frame) {

	}

	@Override
	public int getActionFrameCycle() {
		return 0;
	}

	@Override
	public int getGraphic() {
		return 0;
	}

	@Override
	public void setGraphic(int id) {

	}

	@Override
	public int getSpotAnimFrame() {
		return 0;
	}

	@Override
	public void setSpotAnimFrame(int id) {

	}

	@Override
	public int getSpotAnimationFrameCycle() {
		return 0;
	}

	@Override
	public int getIdlePoseAnimation() {
		return 0;
	}

	@Override
	public void setIdlePoseAnimation(int animation) {

	}

	@Override
	public int getPoseAnimation() {
		return 0;
	}

	@Override
	public void setPoseAnimation(int animation) {

	}

	@Override
	public int getPoseFrame() {
		return 0;
	}

	@Override
	public void setPoseFrame(int frame) {

	}

	@Override
	public int getPoseFrameCycle() {
		return 0;
	}

	@Override
	public int getLogicalHeight() {
		return 0;
	}

	@Override
	public int getOrientation() {
		return 0;
	}

	@Override
	public int getCurrentOrientation() {
		return 0;
	}

	@Override
	public RSIterableNodeDeque getHealthBars() {
		return null;
	}

	@Override
	public int[] getHitsplatValues() {
		return new int[0];
	}

	@Override
	public int[] getHitsplatTypes() {
		return new int[0];
	}

	@Override
	public int[] getHitsplatCycles() {
		return new int[0];
	}

	@Override
	public int getIdleRotateLeft() {
		return 0;
	}

	@Override
	public int getIdleRotateRight() {
		return 0;
	}

	@Override
	public int getWalkAnimation() {
		return 0;
	}

	@Override
	public int getWalkRotate180() {
		return 0;
	}

	@Override
	public int getWalkRotateLeft() {
		return 0;
	}

	@Override
	public int getWalkRotateRight() {
		return 0;
	}

	@Override
	public int getRunAnimation() {
		return 0;
	}

	@Override
	public void setDead(boolean dead) {

	}

	@Override
	public int getPathLength() {
		return 0;
	}

	@Override
	public int getOverheadCycle() {
		return 0;
	}

	@Override
	public void setOverheadCycle(int cycle) {

	}

	@Override
	public int getPoseAnimationFrame() {
		return 0;
	}

	@Override
	public void setPoseAnimationFrame(int frame) {

	}

	@Override
	public RSNode getNext() {
		return null;
	}

	@Override
	public long getHash() {
		return 0;
	}

	@Override
	public RSNode getPrevious() {
		return null;
	}

	@Override
	public void onUnlink() {

	}

	@Override
	public RSUsername getRsName() {
		return null;
	}

	@Override
	public int getPlayerId() {
		return 0;
	}

	@Override
	public RSPlayerComposition getPlayerComposition() {
		return null;
	}

	@Override
	public int getCombatLevel() {
		return 0;
	}

	@Override
	public int getTotalLevel() {
		return 0;
	}

	@Override
	public int getTeam() {
		return 0;
	}

	@Override
	public boolean isFriendsChatMember() {
		return false;
	}

	@Override
	public boolean isClanMember() {
		return false;
	}

	@Override
	public boolean isFriend() {
		return false;
	}

	@Override
	public boolean isFriended() {
		return false;
	}

	@Override
	public int getRsOverheadIcon() {
		return 0;
	}

	@Override
	public int getRsSkullIcon() {
		return 0;
	}

	@Override
	public int getRSSkillLevel() {
		return 0;
	}

	@Override
	public String[] getActions() {
		return new String[0];
	}

	@Override
	public int getModelHeight() {
		return 0;
	}

	@Override
	public void setModelHeight(int modelHeight) {

	}

	@Override
	public RSModel getModel() {
		return null;
	}

	@Override
	public void draw(int orientation, int pitchSin, int pitchCos, int yawSin, int yawCos, int x, int y, int z, long hash) {

	}
}
