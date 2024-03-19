package com.client;

import com.client.model.rt7_anims.SkeletalAnimBase;

public final class AnimBase {
    SkeletalAnimBase skeletal_animbase;
    public int count;
    /**
     *
     * @param highrev As in 700 pre-skeletal rs3
     */
    public AnimBase(Buffer packet, boolean highrev, int buffer_size) {
        int before_read = packet.currentPosition;
        count = highrev ? packet.readUShort() : packet.get_unsignedbyte();
        transform_types = new int[count];
        labels = new int[count][];
        for(int j = 0; j < count; j++)
        	transform_types[j] = highrev ? packet.readUShort() : packet.get_unsignedbyte();
		for(int j = 0; j < count; j++)
			labels[j] = new int[highrev ? packet.readUShort() : packet.get_unsignedbyte()];
        for(int j = 0; j < count; j++)
			for(int l = 0; l < labels[j].length; l++)
				labels[j][l] = highrev ? packet.readUShort() : packet.get_unsignedbyte();

        int read1_size = packet.currentPosition - before_read;

        if(!highrev) {
            if (read1_size != buffer_size) {
                try {
                    int size = packet.get_unsignedshort();
                    if (size > 0) {
                        this.skeletal_animbase = new SkeletalAnimBase(packet, size);
                    }
                } catch (Throwable t) {
                    System.err.println("Tried to load base because there was extra base data but skeletal failed to load.");
                    t.printStackTrace();
                }
            }
            int read2_size = packet.currentPosition - before_read;

            if(read2_size != buffer_size) {
                throw new RuntimeException("base data size mismatch: " + read2_size + ", expected " + buffer_size);
            }
        }
    }

    private AnimBase() {

    }

    public static AnimBase create641(Buffer packet) {
        AnimBase base = new AnimBase();
        base.count = packet.readUShort();
        base.transform_types = new int[base.count];
        base.labels = new int[base.count][];
        for(int j = 0; j < base.count; j++)
            base.transform_types[j] = packet.readUShort();
        for(int j = 0; j < base.count; j++)
            base.labels[j] = new int[packet.readUShort()];
        for(int j = 0; j < base.count; j++)
            for(int l = 0; l < base.labels[j].length; l++)
                base.labels[j][l] = packet.readUShort();
        return base;
    }


    public int transforms_count() {
        return this.count;
    }

    public SkeletalAnimBase get_skeletal_animbase() {
        return this.skeletal_animbase;
    }

    public int[] transform_types;//anIntArray342
    public int[][] labels;//anIntArray343
}
