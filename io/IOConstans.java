package com.fancoff.coffeemaker.io;

/**
 * Created by apple on 2017/12/11.
 * vmc指令
 */

public class IOConstans {
    public static class OPTION{

        public final static byte OPTION_QUE = (byte) 0x01;
        public final static byte OPTION_MAKING = (byte) 0x20;
        public final static byte OPTION_SETCOFFE = (byte) 0x41;
        public final static byte OPTION_CALCLE = (byte) 0x13;
        public final static byte OPTION_CLEAN = (byte) 0x21;
        public final static byte OPTION_CLEAR_PROGRESS = (byte) 0x11;
        public final static byte OPTION_HOT_ON = (byte) 0x22;
        public final static byte OPTION_SET_MACHINE = (byte) 0x40;
        public final static byte OPTION_CLEAR_ERROR = (byte) 0x12;
        public final static byte OPTION_OPEN_DOOR = (byte) 0x23;


    }
    public static class BYTE {
        public final static int BYTE_0841 = 0x0841;
        public final static int BYTE_0900 = 0x0900;
        public final static int BYTE_0910 = 0x0002;


    }

}
