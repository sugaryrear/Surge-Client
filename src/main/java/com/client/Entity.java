package com.client;

import com.client.definitions.AnimationDefinition;
import com.client.sound.Sound;
import com.client.sound.SoundType;
import com.client.utilities.LocalPoint_2;
import com.jagex.rs2lib.movement.MoveSpeed;
import net.runelite.api.coords.LocalPoint;

import java.awt.*;

public class Entity extends Renderable {

	int direction_change_tick = 0;
	boolean instant_facing;

	public boolean isLocalPlayer() {
		return this == Client.localPlayer;
	}

	public int getAbsoluteX() {
		int x = Client.baseX + (this.x - 6 >> 7);
		if (this instanceof Npc) {
			return x - ((Npc) this).desc.size / 2;
		}
		return x;
	}

	public int getAbsoluteY() {
		int y = Client.baseY + (this.z - 6 >> 7);
		if (this instanceof Npc) {
			return y - ((Npc) this).desc.size / 2;
		}
		return y;
	}

	public int getDistanceFrom(Entity entity) {
		return getDistanceFrom(entity.getAbsoluteX(), entity.getAbsoluteY());
	}

	public int getDistanceFrom(int x2, int y2) {
		int x = (int) Math.pow(getAbsoluteX() - x2, 2.0D);
		int y = (int) Math.pow(getAbsoluteY() - y2, 2.0D);
		return (int) Math.floor(Math.sqrt(x + y));
	}

	public void makeSound(int framesound) {
		int soundeffect_id = framesound >> 8;
		int soundeffect_replays = framesound >> 4 & 7;
		int soundeffect_delays = framesound & 15;
		double distance = getDistanceFrom(Client.localPlayer);
//		if (Configuration.developerMode) {
//			System.out.println("entity sound: id " + id + " x" + getAbsoluteX() + " y" + getAbsoluteY() + " d" + distance);
//		}
		Sound.getSound().playSound(soundeffect_id, isLocalPlayer() || this instanceof Npc ? SoundType.SOUND : SoundType.AREA_SOUND, distance);
	}

	public final void teleport(int x, int z, boolean flag) {
		if (primaryanim != -1 && AnimationDefinition.anims[primaryanim].movetype == 1)
			primaryanim = -1;
		if (!flag) {
			int k = x - waypoint_x[0];
			int l = z - waypoint_z[0];
			if (k >= -8 && k <= 8 && l >= -8 && l <= 8) {
				if (waypoint_count < 9)
					waypoint_count++;
				for (int i1 = waypoint_count; i1 > 0; i1--) {
					waypoint_x[i1] = waypoint_x[i1 - 1];
					waypoint_z[i1] = waypoint_z[i1 - 1];
					waypoint_movespeed[i1] = waypoint_movespeed[i1 - 1];
				}

				waypoint_x[0] = x;
				waypoint_z[0] = z;
				waypoint_movespeed[0] = MoveSpeed.WALK;
				return;
			}
		}
		waypoint_count = 0;
		anim_delay = 0;
		walkanim_pause = 0;
		waypoint_x[0] = x;
		waypoint_z[0] = z;
		this.x = waypoint_x[0] * 128 + size * 64;
		this.z = waypoint_z[0] * 128 + size * 64;
	}

	public final void clear_waypoint() {
		waypoint_count = 0;
		anim_delay = 0;
	}

	public final void updateHitData(int j, int k, int l) {
		for (int i1 = 0; i1 < 4; i1++)
			if (hitsLoopCycle[i1] <= l) {
				hitArray[i1] = k;
				hitMarkTypes[i1] = j;
				hitsLoopCycle[i1] = l + 70;
				return;
			}
	}

	public final void move_step_direction(MoveSpeed movespeed, int direction) {
		int j = waypoint_x[0];
		int k = waypoint_z[0];
		if (direction == 0) {
			j--;
			k++;
		}
		if (direction == 1)
			k++;
		if (direction == 2) {
			j++;
			k++;
		}
		if (direction == 3)
			j--;
		if (direction == 4)
			j++;
		if (direction == 5) {
			j--;
			k--;
		}
		if (direction == 6)
			k--;
		if (direction == 7) {
			j++;
			k--;
		}
		if (primaryanim != -1 && AnimationDefinition.anims[primaryanim].movetype == 1)
			primaryanim = -1;
		if (waypoint_count < 9)
			waypoint_count++;
		for (int l = waypoint_count; l > 0; l--) {
			waypoint_x[l] = waypoint_x[l - 1];
			waypoint_z[l] = waypoint_z[l - 1];
			waypoint_movespeed[l] = waypoint_movespeed[l - 1];
		}
		waypoint_x[0] = j;
		waypoint_z[0] = k;
		waypoint_movespeed[0] = movespeed;
	}

	public int entScreenX;
	public int entScreenY;

	public boolean isVisible() {
		return false;
	}

	Entity() {
		waypoint_x = new int[10];
		waypoint_z = new int[10];
		interactingEntity = -1;
		turnspeed = 32;
		runanim = -1;
		height = 200;
		readyanim = -1;
		readyanim_l = -1;
		readyanim_r = -1;
		hitArray = new int[4];
		hitMarkTypes = new int[4];
		hitsLoopCycle = new int[4];
		secondaryanim = -1;
		spotanim = -1;
		primaryanim = -1;
		loopCycleStatus = -1000;
		textCycle = 100;
		size = 1;
		is_walking = false;
		waypoint_movespeed = new MoveSpeed[10];
		walkanim = -1;
		walkanim_b = -1;
		walkanim_l = -1;
		walkanim_r = -1;
	}

	public final int[] waypoint_x;
	public final int[] waypoint_z;
	public int interactingEntity;
	int walkanim_pause;
	int turnspeed;
	int runanim;
	public String textSpoken;
	public String lastForceChat;
	public int height;
	private int current_angle;
	int readyanim;
	int readyanim_l;
	int readyanim_r;
	int runanim_b = -1;
	int runanim_r = -1;
	int runanim_l = -1;
	int crawlanim = -1;
	int crawlanim_b = -1;
	int crawlanim_l = -1;
	int crawlanim_r = -1;
	int anInt1513;
	final int[] hitArray;
	final int[] hitMarkTypes;
	final int[] hitsLoopCycle;
	int secondaryanim;
	int secondaryanim_replaycount;
	int secondaryanim_frameindex;
	int secondaryanim_loops_remaining;
	int spotanim;
	int spotanimframe_index;
	int spotanim_loop;
	int spotanim_start_loop;
	int spotanim_height;
	int waypoint_count;
	public int primaryanim;
	int primaryanim_frameindex;
	int primaryanim_loops_remaining;
	int primaryanim_pause;
	int primaryanim_replaycount;
	int anInt1531;
	public int loopCycleStatus;
	public int currentHealth;
	public int maxHealth;
	int textCycle;
	int last_update_tick;
	int face_x;
	int face_z;
	int size;
	boolean is_walking;
	int anim_delay;
	int exactmove_x1;
	int exactmove_x2;
	int exactmove_z1;
	int exactmove_z2;
	int exactmove_start;
	int exactmove_end;
	int forceMovementDirection;
	public int x;
	public int z;
	int target_direction;
	final MoveSpeed[] waypoint_movespeed;
	int walkanim;
	int walkanim_b;
	int walkanim_l;
	int walkanim_r;

	public int getTurnDirection() {
		return current_angle;
	}

	public void setTurnDirection(int turnDirection) {
		this.current_angle = turnDirection;
	}


}
