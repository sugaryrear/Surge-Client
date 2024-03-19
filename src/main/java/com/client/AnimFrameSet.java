package com.client;

public class AnimFrameSet {
    public static AnimFrameSet[] frameset;
    int fileid;
    AnimFrame[] frames;

    AnimFrameSet(int fileid, byte[] fileData) {
        this.fileid = fileid;
        Buffer stream = new Buffer(fileData);
        if (stream.get_unsignedshort() != 710) {
            System.err.println("NOT An OSRS anim file");
        }
        int baseSize = stream.get_int();//need to write this

        byte[] base_data = new byte[baseSize];
        stream.readBytes(baseSize, 0, base_data);
        System.err.println("Loading base bytes " + baseSize);
        Buffer base_buffer = new Buffer(base_data);
        AnimBase class18 = new AnimBase(base_buffer, false, baseSize);
        int frame_count = stream.get_unsignedshort();//byte in osrs

        frames = new AnimFrame[frame_count];
        AnimFrame.load_osrs_frames(stream, fileid, frame_count, class18, frames);

        if(frameset[fileid] == null) {
            frameset[fileid] = this;
        }
    }

    private AnimFrameSet() {

    }

    public static AnimFrameSet create641(int fileid, byte[] fileData) {
        AnimFrameSet frameset = new AnimFrameSet();
        Buffer stream = new Buffer(fileData);
        AnimBase class18 = AnimBase.create641(stream);
        int k1 = stream.readUShort();
        frameset.frames = new AnimFrame[(int)(k1*1.5)];
        AnimFrame.load_641(fileid, stream, k1, class18, frameset.frames);
        return frameset;
    }
    public static AnimFrame get_frame(int frame_id) {
        try {
            String s = "";
            int file = frame_id >>> 16;
            int k = frame_id & 0xffff;
            if(frameset[file] == null) {
                AnimFrame.clientInstance.resourceProvider.provide(1, file);
                return null;
            }
            return frameset[file].frames[k];
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static void load_osrs(int fileid, byte[] fileData) {
        frameset[fileid] = new AnimFrameSet(fileid, fileData);
    }

    public static void load_641(int fileid, byte[] fileData) {
        frameset[fileid] = create641(fileid, fileData);
    }

    public static void init() {
        frameset = new AnimFrameSet[4000];
    }

    public AnimFrame[] get_frames() {
        try {
            if(frameset[fileid] == null) {
                AnimFrame.clientInstance.resourceProvider.provide(1, fileid);
                return null;
            }
            return frameset[fileid].frames;
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public static AnimFrameSet get_frameset(int frame_group) {
        try {
            if(frameset[frame_group] == null) {
                AnimFrame.clientInstance.resourceProvider.provide(1, frame_group);
                return null;
            }
            return frameset[frame_group];
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    public boolean has_transparency_mod(int frame_index) {
        return this.frames[frame_index].mod_alpha;
    }

}
