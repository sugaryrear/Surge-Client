package com.client;
// Decompiled by Jad v1.5.8f. Copyright 2001 Pavel Kouznetsov.
// Jad home page: http://www.kpdus.com/jad.html
// Decompiler options: packimports(3) 

public final class Varp {

	public static int cacheSize;

	public static void unpackConfig(FileArchive streamLoader) {
		Buffer stream = new Buffer(streamLoader.readFile("varp.dat"));
		anInt702 = 0;
		cacheSize = stream.readUShort();
		if (cache == null)
			cache = new Varp[cacheSize + 1000];
		if (anIntArray703 == null)
			anIntArray703 = new int[cacheSize];
		for (int j = 0; j < cacheSize; j++) {
			if (cache[j] == null)
				cache[j] = new Varp();
			cache[j].readValues(stream, j);
		}
		if (stream.currentPosition != stream.payload.length)
			System.out.println("varptype load mismatch");
	}

	private void readValues(Buffer stream, int i) {
		do {
			int j = stream.get_unsignedbyte();
			if (j == 0)
				return;
			if (j == 1)
				stream.get_unsignedbyte();
			else if (j == 2)
				stream.get_unsignedbyte();
			else if (j == 3)
				anIntArray703[anInt702++] = i;
			else if (j == 4) {
			} else if (j == 5)
				anInt709 = stream.readUShort();
			else if (j == 6) {
			} else if (j == 7)
				stream.readDWord();
			else if (j == 8)
				aBoolean713 = true;
			else if (j == 10)
				stream.readString();
			else if (j == 11)
				aBoolean713 = true;
			else if (j == 12)
				stream.readDWord();
			else if (j == 13) {
			} else
				System.out.println("Error unrecognised config code: " + j);
		} while (true);
	}

	private Varp() {
		aBoolean713 = false;
	}

	public static Varp cache[];
	private static int anInt702;
	private static int[] anIntArray703;
	public int anInt709;
	public boolean aBoolean713;

}
