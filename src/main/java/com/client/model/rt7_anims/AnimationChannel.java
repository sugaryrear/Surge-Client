package com.client.model.rt7_anims;

import com.jagex.core.constants.SerialEnum;

public class AnimationChannel implements SerialEnum {
   static final AnimationChannel TRANSLATE_X = new AnimationChannel(4, 4, (String)null, 3);
   static final AnimationChannel SCALE_X = new AnimationChannel(7, 7, (String)null, 6);
   static final AnimationChannel ROTATE_Y = new AnimationChannel(2, 2, (String)null, 1);
   static final AnimationChannel field2343 = new AnimationChannel(12, 12, (String)null, 2);
   static final AnimationChannel TRANSLATE_Y = new AnimationChannel(5, 5, (String)null, 4);
   static final AnimationChannel TRANSLATE_Z = new AnimationChannel(6, 6, (String)null, 5);
   static final AnimationChannel NULL = new AnimationChannel(0, 0, (String)null, -1);
   static final AnimationChannel SCALE_Y = new AnimationChannel(8, 8, (String)null, 7);
   static final AnimationChannel field2341 = new AnimationChannel(10, 10, (String)null, 0);
   static final AnimationChannel field2342 = new AnimationChannel(11, 11, (String)null, 1);
   static final AnimationChannel SCALE_Z = new AnimationChannel(9, 9, (String)null, 8);
   static final AnimationChannel ROTATE_Z = new AnimationChannel(3, 3, (String)null, 2);
   static final AnimationChannel field2344 = new AnimationChannel(14, 14, (String)null, 4);
   static final AnimationChannel TRANSPARENCY = new AnimationChannel(16, 16, (String)null, 0);
   static final AnimationChannel field2347 = new AnimationChannel(13, 13, (String)null, 3);
   static final AnimationChannel ROTATE_X = new AnimationChannel(1, 1, (String)null, 0);
   static final AnimationChannel field2345 = new AnimationChannel(15, 15, (String)null, 5);
   final int ordinal;
   final int serial_id;
   final int component;

   static AnimationChannel[] values() {
      return new AnimationChannel[]{NULL, ROTATE_X, ROTATE_Y, ROTATE_Z, TRANSLATE_X, TRANSLATE_Y, TRANSLATE_Z, SCALE_X, SCALE_Y, SCALE_Z, field2341, field2342, field2343, field2347, field2344, field2345, TRANSPARENCY};
   }

   AnimationChannel(int ord, int id, String name, int comp) {
      this.ordinal = ord;
      this.serial_id = id;
      this.component = comp;
   }

   public static AnimationChannel lookup_by_id(int arg0) {
      AnimationChannel var2 = (AnimationChannel) SerialEnum.for_id(values(), arg0);
      if (var2 == null) {
         var2 = NULL;
      }

      return var2;
   }

   public int serial_id() {
      return this.serial_id;
   }

   public int get_component() {
      return this.component;
   }

}
