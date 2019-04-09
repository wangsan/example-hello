package com.wangsan.study.sqlite;

public class MyFunctions {
    public static boolean cosWang(String x, String y) {
        return Math.abs(Integer.parseInt(x) - Integer.parseInt(y)) < 5;
    }

    public static double cosStr(String x, String y) {
        float[] aFloats = byteToFloat(hexStringToByteArray(x));
        float[] bFloats = byteToFloat(hexStringToByteArray(y));
        return cosine(aFloats, bFloats);
    }

    public static double cosBytes(byte[] a, byte[] b) {
        return cosine(byteToFloat(a), byteToFloat(b));
    }

    public static double cosine(float[] a, float[] b) {
        int length = a.length;

        double inner = 0.0;
        double aSqr = 0.0;
        double bSqr = 0.0;

        for (int i = 0; i < length; i++) {
            inner += a[i] * b[i];
            aSqr += a[i] * a[i];
            bSqr += b[i] * b[i];
        }

        double aMod = Math.sqrt(aSqr);
        double bMod = Math.sqrt(bSqr);
        return inner / (aMod * bMod);
    }

    public static float[] byteToFloat(byte[] bytes) {
        int length = bytes.length;
        float[] floats = new float[length / 4];
        for (int i = 0; i < length; i += 4) {
            int accum = 0;
            accum = accum | (bytes[i] & 0xff) << 0;
            accum = accum | (bytes[i + 1] & 0xff) << 8;
            accum = accum | (bytes[i + 2] & 0xff) << 16;
            accum = accum | (bytes[i + 3] & 0xff) << 24;
            floats[i / 4] = Float.intBitsToFloat(accum);
        }

        return floats;
    }

    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] b = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            // 两位一组，表示一个字节,把这样表示的16进制字符串，还原成一个字节
            b[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4) + Character.digit(s.charAt(i + 1), 16));
        }
        return b;
    }

    public static byte[] getBytes(int data) {
        byte[] bytes = new byte[4];
        bytes[0] = (byte) (data & 0xff);
        bytes[1] = (byte) ((data & 0xff00) >> 8);
        bytes[2] = (byte) ((data & 0xff0000) >> 16);
        bytes[3] = (byte) ((data & 0xff000000) >> 24);
        return bytes;
    }

    public static byte[] floatToByte(float f) {
        int i = Float.floatToRawIntBits(f);
        return getBytes(i);
    }

    public static String byteArray2HexString(byte[] b) {
        String ret = "";
        for (int i = 0; i < b.length; i++) {
            String hex = Integer.toHexString(b[i] & 0xFF);
            if (hex.length() == 1) {
                hex = '0' + hex;
            }
            ret += hex.toUpperCase();
        }
        return ret;
    }

    public static void main(String[] args) {
        float[] featureArray = new float[] {1.1f, 2.3f, 333f, 50000};
        float[] featureArray1 = new float[] {1.1f, 2.3f, 333f, 50001};
        float[] featureArray2 = new float[] {1.1f, 2.3f, 333f, 49999};
        float[] featureArray3 = new float[] {1.1f, 2.3f, 333f, 48999};

        System.err.println(MyFunctions.cosine(featureArray, featureArray1));
        System.err.println(MyFunctions.cosine(featureArray, featureArray2));
        System.err.println(MyFunctions.cosine(featureArray, featureArray3));
    }
}
