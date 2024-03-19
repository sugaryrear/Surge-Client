package com.client;

import com.client.definitions.AnimationDefinition;
import com.client.definitions.NpcDefinition;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 
import com.client.definitions.GraphicsDefinition;
import com.client.features.settings.Preferences;
import com.client.utilities.LocalPoint_2;
import net.runelite.api.*;
import net.runelite.api.Point;
import net.runelite.api.coords.LocalPoint;
import net.runelite.api.coords.WorldArea;
import net.runelite.api.coords.WorldPoint;
import net.runelite.rs.api.*;
import org.jetbrains.annotations.Nullable;

import java.awt.*;
import java.awt.image.BufferedImage;

public final class Npc extends Entity implements RSNPC {

	public Model get_animated_npcmodel() {
		if (super.primaryanim >= 0 && super.primaryanim_pause == 0) {
			int current_index = super.primaryanim_frameindex;
			int next_index = -1;
			if (super.secondaryanim >= 0 && super.secondaryanim != super.readyanim)
				next_index = super.secondaryanim_frameindex;
			return desc.get_animated_entity_model(current_index, AnimationDefinition.anims[super.primaryanim],
					AnimationDefinition.anims[super.primaryanim].mergedseqgroups, next_index, AnimationDefinition.anims[super.secondaryanim]);
		}
		int walk_index = -1;
		if (super.secondaryanim >= 0)
			walk_index = super.secondaryanim_frameindex;
		return desc.get_animated_entity_model(walk_index, AnimationDefinition.anims[super.secondaryanim], null, -1, null);
	}
	public Polygon getCanvasTilePolyactual(Npc npc) {
//		int world_tile_x =  (x * 128) + 64;
//		int world_tile_y =  (z * 128) + 64;
		//return getCanvasTileAreaPolyReal(new LocalPoint_2(world_tile_x,world_tile_y), 1);
	//	System.out.println("lol");
		LocalPoint lp = npc.getLocalLocation();
//		System.out.println("lol: "+lp.getX());
		int size23 = npc.desc.getSize();

		//int size23 = npc.desc.getSize();
		int x = lp.getX() - ((size23 - 1) * Perspective.LOCAL_TILE_SIZE / 2);
		int y = lp.getY() - ((size23 - 1) * Perspective.LOCAL_TILE_SIZE / 2);
		Polygon southWestTilePoly = Perspective.getCanvasTilePoly(Client.instance, new LocalPoint(x, y));
		//renderPoly(graphics, borderColor, borderWidth, fillColor, southWestTilePoly);

return southWestTilePoly;
	//	return Client.instance.getCanvasTileAreaPolyReal(new LocalPoint_2(x,z), size);
		//return Client.instance.getCanvasTilePolyForTile(x,z);
	}
	@Override
	public Model getRotatedModel() {
		if (desc == null)
			return null;
		Model model = get_animated_npcmodel();
		if (model == null)
			return null;
		super.height = model.modelBaseY;
		if (super.spotanim != -1 && super.spotanimframe_index != -1) {
			GraphicsDefinition spotAnim = GraphicsDefinition.cache[super.spotanim];
			Model model_1 = spotAnim.get_transformed_model(super.spotanimframe_index);
			// Added
			if(model_1 != null) {
				model_1.offsetBy(0, -super.spotanim_height, 0);
				Model aModel[] = {model, model_1};
				model = new Model(aModel);
			}
		}
		if (desc.size == 1)
			model.singleTile = true;
		return model;
	}

	@Override
	public boolean isVisible() {
		return desc != null;
	}

	Npc() {
	}

	public boolean isShowMenuOnHover() {
		return npcPetType == 0 || npcPetType == 2 && !Preferences.getPreferences().hidePetOptions;
	}

	public int npcPetType;
	public NpcDefinition desc;

	@Override
	public int getCombatLevel() {
		return 0;
	}

	@Nullable
	@Override
	public NPCComposition getTransformedComposition() {
		return null;
	}

	@Override
	public void onDefinitionChanged(NPCComposition composition) {

	}

	@Override
	public int getId() {
		return 0;
	}

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
		//return null;
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
		return Perspective.getCanvasTilePoly(Client.instance, this.getLocalLocation());
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
	public RSNPCComposition getComposition() {
		return null;
	}

	@Override
	public int getIndex() {
		return 0;
	}

	@Override
	public void setIndex(int id) {

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
