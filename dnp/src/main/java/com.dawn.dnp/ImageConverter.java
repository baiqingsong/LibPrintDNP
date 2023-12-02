package com.dawn.dnp;
import android.graphics.Bitmap;
import android.graphics.Color;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

 class ImageConverter {


    public static byte[] createBmpData(Bitmap jpgBitmap) {
        int width = jpgBitmap.getWidth();
        int height = jpgBitmap.getHeight();

        int bmpSize = 54 + (width * height * 3); // BMP 文件大小
        byte[] bmpData = new byte[bmpSize];

        // BMP 文件头
        bmpData[0] = 0x42; // 'B'
        bmpData[1] = 0x4D; // 'M'
        bmpData[2] = (byte) (bmpSize);
        bmpData[3] = (byte) (bmpSize >> 8);
        bmpData[4] = (byte) (bmpSize >> 16);
        bmpData[5] = (byte) (bmpSize >> 24);
        bmpData[10] = 54; // 偏移量
        bmpData[14] = 40; // 信息头大小
        bmpData[18] = (byte) (width);
        bmpData[19] = (byte) (width >> 8);
        bmpData[20] = (byte) (width >> 16);
        bmpData[21] = (byte) (width >> 24);
        bmpData[22] = (byte) (height);
        bmpData[23] = (byte) (height >> 8);
        bmpData[24] = (byte) (height >> 16);
        bmpData[25] = (byte) (height >> 24);
        bmpData[26] = 1; // 颜色平面
        bmpData[28] = 24; // 每像素位数
        bmpData[34] = (byte) (width * height * 3);
        bmpData[35] = (byte) (width * height * 3 >> 8);
        bmpData[36] = (byte) (width * height * 3 >> 16);
        bmpData[37] = (byte) (width * height * 3 >> 24);

        int rowPadding = (4 - (width * 3) % 4) % 4; // 行字节数补齐
        int p = 54; // 数据起始位置

        for (int i = height - 1; i >= 0; i--) {
            for (int j = 0; j < width; j++) {
                int color = jpgBitmap.getPixel(j, i);
                bmpData[p++] = (byte) Color.blue(color);
                bmpData[p++] = (byte) Color.green(color);
                bmpData[p++] = (byte) Color.red(color);
            }
            for (int k = 0; k < rowPadding; k++) {
                bmpData[p++] = 0; // 补齐的字节
            }
        }

        return bmpData;
    }
    public static void saveBitmapAsBmp(Bitmap bmpBitmap, String filePath) {
        try {
            File file = new File(filePath);
            FileOutputStream fos = new FileOutputStream(file);

            int width = bmpBitmap.getWidth();
            int height = bmpBitmap.getHeight();

            int padding = (4 - (width * 3) % 4) % 4;
            int size = 54 + (3 * width + padding) * height;

            ByteBuffer buffer = ByteBuffer.allocate(size);
            buffer.order(ByteOrder.LITTLE_ENDIAN);

            // BMP 文件头
            buffer.put((byte) 'B');
            buffer.put((byte) 'M');
            buffer.putInt(size);
            buffer.putInt(0);
            buffer.putInt(54);

            // BMP 信息头
            buffer.putInt(40);
            buffer.putInt(width);
            buffer.putInt(height);
            buffer.putShort((short) 1);
            buffer.putShort((short) 24);
            buffer.putInt(0);
            buffer.putInt((3 * width + padding) * height);
            buffer.putInt(0);
            buffer.putInt(0);
            buffer.putInt(0);
            buffer.putInt(0);

            // 像素数据
            int[] pixels = new int[width * height];
            bmpBitmap.getPixels(pixels, 0, width, 0, 0, width, height);

            for (int h = height - 1; h >= 0; h--) {
                for (int w = 0; w < width; w++) {
                    int pixel = pixels[h * width + w];
                    buffer.put((byte) Color.blue(pixel));
                    buffer.put((byte) Color.green(pixel));
                    buffer.put((byte) Color.red(pixel));
                }

                for (int p = 0; p < padding; p++) {
                    buffer.put((byte) 0);
                }
            }

            fos.write(buffer.array());
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
