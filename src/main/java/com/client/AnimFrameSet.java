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
      //  System.out.println("file id: "+fileid+" sze: "+baseSize);
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
    AnimFrameSet(int fileid, byte[] fileData,int id) {
        this.fileid = fileid;

        Buffer stream = new Buffer(fileData);
//        if (stream.get_unsignedshort() != 710) {
//            System.err.println("NOT An OSRS anim file");
//        }
        int baseSize = stream.get_int();//need to write this
       // System.out.println("file id: "+fileid+" size: "+baseSize);
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
       // frameset.frames = new AnimFrame[(int)(k1*1.5)];
        frameset.frames = new AnimFrame[(int)(k1*3)];
        AnimFrame.load_641(fileid, stream, k1, class18, frameset.frames);
        return frameset;
    }
    public static AnimFrameSet createchattheads(int fileid, byte[] fileData) {
        AnimFrameSet frameset = new AnimFrameSet();
        Buffer stream = new Buffer(fileData);
        AnimBase class18 = AnimBase.createchatheads(stream);
        int k1 = stream.readUShort();
        frameset.frames = new AnimFrame[(int)(k1*3)];
        AnimFrame.load_chatheads(fileid, stream, k1, class18, frameset.frames);
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
    public static void load_osrs_forchatheads(int fileid, byte[] fileData) {
        frameset[fileid] = new AnimFrameSet(fileid, fileData,2);
    }
    public static void load_641(int fileid, byte[] fileData) {
        frameset[fileid] = create641(fileid, fileData);
    }
    public static void load_chatheads(int fileid, byte[] fileData) {
//        frameset[fileid] = createchattheads(fileid, fileData);

        try {
            Buffer stream = new Buffer(fileData);
            AnimBase class18 = new AnimBase(stream);//FrameBase class18 = new FrameBase(stream);
            int k1 = stream.readUShort();
//AnimFrame frame = out[l1] = new AnimFrame();//Frame class36 = animationlist[file][i2] = new Frame();
          //  frameset[fileid] = new AnimFrameSet[(int)(k1*3)];
            AnimFrameSet frameset = new AnimFrameSet();
//            Buffer stream = new Buffer(fileData);
//            AnimBase class18 = AnimBase.create641(stream);
//            int k1 = stream.readUShort();
            frameset.frames = new AnimFrame[(int)(k1*3)];//	animationlist[file] = new Frame[(int)(k1*3)];
            int ai[] = new int[500];
            int ai1[] = new int[500];
            int ai2[] = new int[500];
            int ai3[] = new int[500];
            for(int l1 = 0; l1 < k1; l1++) {
                int i2 = stream.readUShort();


                AnimFrame class36 = AnimFrameSet.frameset[fileid].frames[i2] = new AnimFrame();
                class36.base = class18;
                int j2 = stream.readUnsignedByte();
                int l2 = 0;
                int k2 = -1;
                for(int i3 = 0; i3 < j2; i3++) {
                    int j3 = stream.readUnsignedByte();
                    if(j3 > 0) {
                        if(class18.transform_types[i3] != 0) {
                            for(int l3 = i3 - 1; l3 > k2; l3--) {
                                if(class18.transform_types[l3] != 0)
                                    continue;
                                ai[l2] = l3;
                                ai1[l2] = 0;
                                ai2[l2] = 0;
                                ai3[l2] = 0;
                                l2++;
                                break;
                            }
                        }
                        ai[l2] = i3;
                        short c = 0;
                        if(class18.transform_types[i3] == 3)
                            c = (short)128;
                        if((j3 & 1) != 0)
                            ai1[l2] = (short)stream.readShort2();
                        else
                            ai1[l2] = c;
                        if((j3 & 2) != 0)
                            ai2[l2] = stream.readShort2();
                        else
                            ai2[l2] = c;
                        if((j3 & 4) != 0)
                            ai3[l2] = stream.readShort2();
                        else
                            ai3[l2] = c;
                        k2 = i3;
                        l2++;
                    }
                }
                class36.transformationCount = l2;
                class36.transformationIndices = new int[l2];
                class36.transformX = new int[l2];
                class36.transformY = new int[l2];
                class36.transformZ = new int[l2];
                for(int k3 = 0; k3 < l2; k3++) {
                    class36.transformationIndices[k3] = ai[k3];
                    class36.transformX[k3] = ai1[k3];
                    class36.transformY[k3] = ai2[k3];
                    class36.transformZ[k3] = ai3[k3];
                }
            }
        } catch(Exception exception) {
            System.err.println("Error reading Class36 file: " + fileid);
        }
    }

    public static void init() {
        frameset = new AnimFrameSet[9000];
        //animationlist = new Frame[4500][0];
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
