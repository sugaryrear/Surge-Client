package com.client.definitions;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.Arrays;

import com.client.*;
import com.client.definitions.custom.GraphicsDefinitionCustom;
import com.client.utilities.FileOperations;

public final class GraphicsDefinition {
	public static int aAnimation_407;

	public static void unpackConfig(FileArchive streamLoader) {
		Buffer stream = new Buffer(streamLoader.readFile("spotanim.dat"));
		int length = stream.readUShort();
		if (cache == null)
			cache = new GraphicsDefinition[length + 15000];
		for (int j = 0; j < length; j++) {
			if (cache[j] == null) {
				cache[j] = new GraphicsDefinition();
			}
			if (j == 65535) {
				j = -1;
			}
			cache[j].id = j;
			cache[j].setDefault();
			cache[j].readValues(stream);
			GraphicsDefinitionCustom.custom1(j, cache);
		}
		cache[1282] = new GraphicsDefinition();
		cache[1282].id = 1282;
		cache[1282].modelId = 44811;
		cache[1282].animationId = 7155;
		cache[1282].seqtype = AnimationDefinition.anims[cache[1282].animationId];

		if (Configuration.dumpDataLists) {
			gfxDump();
		}
	}


	public static void gfxDump() {
		try {
			BufferedWriter fw = new BufferedWriter(new FileWriter("./temp/gfx_list.txt"));
			for (int i = 0; i < cache.length; i++) {
				GraphicsDefinition item = cache[i];
				if (item == null)
					continue;
				fw.write("case " + i + ":");
				fw.write(System.getProperty("line.separator"));

				fw.write("gfx.anIntArray409 = \"" + Arrays.toString(item.recolorToReplace) + "\";");
				fw.write(System.getProperty("line.separator"));

				fw.write("gfx.modelId = \"" + item.modelId + "\";");
				fw.write(System.getProperty("line.separator"));

				fw.write("break;");
				fw.write(System.getProperty("line.separator"));
				fw.write(System.getProperty("line.separator"));
			}
			fw.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public short[] textureReplace;
	public short[] textureFind;

	private void readValues(Buffer buffer) {
		while (true) {
			int opcode = buffer.get_unsignedbyte();
			if (opcode == 0) {
				return;
			}
			if (opcode == 1) {
				modelId = buffer.readUShort();
			} else if (opcode == 2) {
				animationId = buffer.readUShort();
				if (AnimationDefinition.anims != null) {
					seqtype = AnimationDefinition.anims[animationId];
				}
			} else if (opcode == 4) {
				resizeXY = buffer.readUShort();
			} else if (opcode == 5) {
				resizeZ = buffer.readUShort();
			} else if (opcode == 6) {
				rotation = buffer.readUShort();
			} else if (opcode == 7) {
				modelBrightness = buffer.get_unsignedbyte();
			} else if (opcode == 8) {
				modelShadow = buffer.get_unsignedbyte();
			} else if (opcode == 40) {
				int j = buffer.get_unsignedbyte();
				for (int k = 0; k < j; k++) {
					recolorToFind[k] = buffer.readUShort();
					recolorToReplace[k] = buffer.readUShort();
				}
//			} else if (opcode >= 40 && opcode < 50) {
//				anIntArray408[opcode - 40] = class30_sub2_sub2.method410();
//			} else if (opcode >= 50 && opcode < 60) {
//				anIntArray409[opcode - 50] = class30_sub2_sub2.method410();

			} else if (opcode == 41) { // re-texture
				int len = buffer.get_unsignedbyte();
				textureFind = new short[len];
				textureReplace = new short[len];
				for (int i = 0; i < len; i++) {
					textureFind[i] = (short) buffer.readUShort();
					textureReplace[i] = (short) buffer.readUShort();
				}
			} else {
				System.out.println("Error unrecognised spotanim config code: " + opcode);
			}
		}
	}
	public int anIntArray408[];
	public int anIntArray409[];
	public static GraphicsDefinition fetch(int modelId) {
		for (GraphicsDefinition anim : cache) {
			if (anim == null) {
				continue;
			}
			if (anim.modelId == modelId) {
				return anim;
			}
		}
		return null;
	}

	public Model getModel() {
		Model model = (Model) recent_models.get(id);
		if (model != null)
			return model;
		model = Model.getModel(modelId);
		if (model == null)
			return null;
		for (int i = 0; i < recolorToFind.length; i++)
			if (recolorToFind[0] != 0) //default frame id
				model.recolor(recolorToFind[i], recolorToReplace[i]);
		if (textureReplace != null) {
			for (int i1 = 0; i1 < textureReplace.length; i1++)
				model.retexture(textureReplace[i1], textureFind[i1]);
		}
		recent_models.put(model, id);
		return model;
	}
	public Model get_transformed_model(int frameindex) {
		Model model = (Model) recent_models.get(id);
		if(model == null) {
			model = Model.getModel(modelId);
			if (model == null)
				return null;
			for (int i = 0; i < recolorToFind.length; i++)
				if (recolorToFind[0] != 0) //default frame id
					model.recolor(recolorToFind[i], recolorToReplace[i]);
			if (textureReplace != null) {
				for (int i1 = 0; i1 < textureReplace.length; i1++)
					model.retexture(textureReplace[i1], textureFind[i1]);
			}
			recent_models.put(model, id);
		}
		Model var6;
		if (animationId != -1 && frameindex != -1) {
			var6 = seqtype.bake_and_animate_spotanim(model, frameindex);
		} else {
			var6 = model.bake_shared_model(true);
		}

		//new_model.animate_either(seqtype, frameindex);
		var6.face_label_groups = null;
		var6.vertex_label_groups = null;
		if (resizeXY != 128 || resizeZ != 128)
			var6.scale(resizeXY, resizeXY, resizeZ);
		var6.light(64 + modelBrightness, 850 + modelShadow, -30, -50, -30, true);

//		if (this.rotation != 0) {
//			if (this.rotation == 90) {
//				var6.rotate90Degrees();
//			}
//
//			if (this.rotation == 180) {
//				var6.rotate90Degrees();
//				var6.rotate90Degrees();
//			}
//
//			if (this.rotation == 270) {
//				var6.rotate90Degrees();
//				var6.rotate90Degrees();
//				var6.rotate90Degrees();
//			}
//		}
		return var6;
	}
	private void setDefault() {
		modelId = -1;
		animationId = -1;
		recolorToFind = new int[6];
		recolorToReplace = new int[6];
		resizeXY = 128;
		resizeZ = 128;
		rotation = 0;
		modelBrightness = 0;
		modelShadow = 0;
		modelId = -1;
		animationId = -1;
		originalColours = new int[6];
		replacementColours = new int[6];
		breadthScale = 128;
		depthScale = 128;
		orientation = 0;
		ambience = 0;
		modelShadow = 0;
		scaleX = 128;
		scaleY = 128;
		scaleZ = 128;
		translateX = 0;
		translateY = 0;
		translateZ = 0;
	}

	public GraphicsDefinition() {
		anInt400 = 9;
		animationId = -1;
		recolorToFind = new int[6];
		recolorToReplace = new int[6];
		resizeXY = 128;
		resizeZ = 128;
		originalColours = new int[6];
		replacementColours = new int[6];
		breadthScale = 128;
		depthScale = 128;
		scaleX = 128;
		scaleY = 128;
		scaleZ = 128;
		translateX = 0;
		translateY = 0;
		translateZ = 0;
	}

	public int getModelId() {
		return modelId;
	}

	public int getId() {
		return id;
	}



	public final int anInt400;
	public static GraphicsDefinition cache[];
	public int id;
	public int modelId;
	public int animationId;
	public AnimationDefinition seqtype;
	public int[] recolorToFind;
	public int[] recolorToReplace;
	public int resizeXY;
	public int resizeZ;
	public int rotation;

	public int modelBrightness;
	public int modelShadow;
	public static ReferenceCache recent_models = new ReferenceCache(30);

	private int scaleX = 128, scaleY = 128, scaleZ = 128;
	private int translateX, translateY, translateZ;


	public AnimationDefinition animation;
	public int[] originalColours;
	public int[] replacementColours;
	public int breadthScale;
	public int depthScale;
	public int orientation;
	public int ambience;

	public static int length() {
		return cache.length;
	}

	public static void reset() {
		cache = null;
	}
}
