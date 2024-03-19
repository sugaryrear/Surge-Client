package com.client;

import java.util.HashMap;

public class BufferExt {

    public static HashMap<Integer, Object> readStringIntParameters(Buffer buffer) {
        int length = buffer.get_unsignedbyte();

        HashMap<Integer, Object> params = new HashMap<>(length);

        for (int i = 0; i < length; i++) {
            boolean isString = buffer.get_unsignedbyte() == 1;
            int key = buffer.read24Int();
            Object value;

            if (isString) {
                value = buffer.readString();
            } else {
                value = buffer.readInt();
            }

            params.put(key, value);
        }
        return params;
    }

}
