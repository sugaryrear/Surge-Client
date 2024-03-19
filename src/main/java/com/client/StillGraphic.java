package com.client;

import com.client.definitions.GraphicsDefinition;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

final class StillGraphic extends Renderable {

	public StillGraphic(int i, int j, int l, int i1, int j1, int k1, int l1) {
		finished_animating = false;
		spotanim = GraphicsDefinition.cache[i1];
		anInt1560 = i;
		anInt1561 = l1;
		anInt1562 = k1;
		anInt1563 = j1;
		anInt1564 = j + l;
		finished_animating = false;
	}

	@Override
	public Model getRotatedModel() {
		Model model = spotanim.getModel();
		if (model == null)
			return null;
		int frame = cur_frameindex;

		Model model_1 = new Model(true, AnimFrame.noAnimationInProgress(frame), false, model);
		if (!finished_animating) {
			model_1.apply_label_groups();
			model_1.animate_either(spotanim.seqtype, frame);
			model_1.face_label_groups = null;
			model_1.vertex_label_groups = null;
		}
		if (spotanim.resizeXY != 128 || spotanim.resizeZ != 128)
			model_1.scale(spotanim.resizeXY, spotanim.resizeXY,
					spotanim.resizeZ);
		if (spotanim.rotation != 0) {
			if (spotanim.rotation == 90)
				model_1.rotate90Degrees();
			if (spotanim.rotation == 180) {
				model_1.rotate90Degrees();
				model_1.rotate90Degrees();
			}
			if (spotanim.rotation == 270) {
				model_1.rotate90Degrees();
				model_1.rotate90Degrees();
				model_1.rotate90Degrees();
			}
		}
		model_1.light(64 + spotanim.modelBrightness,
				850 + spotanim.modelShadow, -30, -50, -30, true);
		return model_1;
	}

	public void advance_anim(int i) {
		if(spotanim.seqtype.using_keyframes()) {
			frame_loop += i;//is this a mistake in jagex client?
			this.cur_frameindex += i;
			if (this.cur_frameindex >= spotanim.seqtype.get_keyframe_duration()) {
				this.finished_animating = true;
			}
		} else {

			for (frame_loop += i; frame_loop > spotanim.seqtype.frame_durations[cur_frameindex]; ) {
				frame_loop -= spotanim.seqtype.frame_durations[cur_frameindex] + 1;
				cur_frameindex++;
				if (cur_frameindex >= spotanim.seqtype.framecount
						&& (cur_frameindex < 0 || cur_frameindex >= spotanim.seqtype.framecount)) {
					cur_frameindex = 0;
					finished_animating = true;
				}
			}
		}
	}

	public final int anInt1560;
	public final int anInt1561;
	public final int anInt1562;
	public final int anInt1563;
	public final int anInt1564;
	public boolean finished_animating;
	private final GraphicsDefinition spotanim;
	private int cur_frameindex;
	private int frame_loop;
}
