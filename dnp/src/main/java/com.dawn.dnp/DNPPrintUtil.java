package com.dawn.dnp;

import static jp.co.dnp.photoprintlib.DNPPhotoPrint.CUTTER_MODE_STANDARD;
import static jp.co.dnp.photoprintlib.DNPPhotoPrint.RESOLUTION300;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import jp.co.dnp.photoprintlib.DNPPhotoPrint;

/**
 * dnp 打印机工厂类
 */
class DNPPrintUtil {
    private DNPPhotoPrint mPrint;
    private int portNum = 1;

    public DNPPrintUtil(Context context) {
        this.mPrint = new DNPPhotoPrint(context);
    }

    /**
     * 获取打印机端口号
     */
    public void getPortNum(){

        int[][] portAry = new int[4][2];
        if(mPrint != null){
            portNum = mPrint.GetPrinterPortNum(portAry);//返回打印机连接数量，portAry是返回数据
            Log.e("dawn", "get port num = " + portNum);
            Log.e("dawn", "get port num = " + portAry[0][0] + " " + portAry[0][1]);

        }
    }

    public void getFirmVersion(){
        if(mPrint != null){
            char[] rbuf = new char[256];
            int firmVersion = mPrint.GetFirmwVersion(0, rbuf);
            Log.e("dawn", "getFirmVersion firm version = " + firmVersion);
            // char数组转String
            String str = String.valueOf(rbuf);
            Log.e("dawn", "getFirmVersion rbuf = " + str);
        }
    }

    public void getPQTY(){
        if(mPrint != null){
            int num = mPrint.GetPQTY(0);//获取剩余打印图片
            Log.e("dawn", "getPQTY num = " + num);
        }
    }

    /**
     * 设置分辨率
     */
    public void setResolution(){
        if(mPrint != null){
            boolean setResult = mPrint.SetResolution(0, RESOLUTION300);//300或者600dpi
            Log.e("dawn", "setResolution setResult = " + setResult);
        }
    }

    /**
     * 设置数据大小
     * 01 CSP_L 54 CSP_4x4
     * 02 CSP_2L 55 CSP_4x6
     * 03 CSP_PC 57 CSP_4P5x4P5
     * 04 CSP_A5 58 CSP_4P5x6
     * 05 CSP_A5W 59 CSP_4P5x8
     * 06 CSP_PCx2
     * (PC 2-image layout)
     * 07 CSP_Lx2
     * (L 2-image layout)
     * 08 CSP_PC_REWIND
     * (PC Rewind) *1
     * 09 CSP_L_REWIND
     * (L Rewind) *1
     * 10 CSP_5x5 *3
     * 11 CSP_6x6 *3
     * 12 CSP_6x4P5 *2
     * 13 CSP_6x4P5x2
     * (6x4.5 2-image layout) *2
     * 14 CSP_6x4P5_REWIND
     * 54 CSP_4x4
     * 55 CSP_4x6
     * 57 CSP_4P5x4P5
     * 58 CSP_4P5x6
     * 59 CSP_4P5x8
     */
    public void setMediaSize(){
        if(mPrint != null){
            boolean setResult = mPrint.SetMediaSize(0, 8);
            Log.e("dawn", "setMediaSize setResult = " + setResult);
        }
    }

    /**
     * 获取打印机上可用缓存大小
     */
    public void getFreeBuffer(){
        if(mPrint != null){
            int freeBuffer = mPrint.GetFreeBuffer(0);
            Log.e("dawn", "getFreeBuffer freeBuffer = " + freeBuffer);
        }
    }

    /**
     * 设置打印数量
     */
    public void setPQTY(){
        if(mPrint != null){
            boolean setResult = mPrint.SetPQTY(0, 1);
            Log.e("dawn", "setPQTY setResult = " + setResult);
        }
    }

    /**
     * 刀具设置
     * CUTTER_MODE_STANDARD     (0) Standard cutter operation
     * CUTTER_MODE_NONSCRAP     (1) Non-scrap cutter operation
     * CUTTER_MODE_2INCHCUT  (120) 2inch-cut operation *1
     */
    public void setCutterMode(){
        if(mPrint != null){
            boolean setResult = mPrint.SetCutterMode(0, CUTTER_MODE_STANDARD);
            Log.e("dawn", "setCutterMode setResult = " + setResult);
        }
    }

    /**
     * 设置外涂层精加工
     * OVERCOAT_FINISH_GLOSSY（0）光泽（默认值）
     *
     * OVERCOAT_FINISH_MATTE1（1）床垫1
     *
     * OVERCOAT_FINISH_WATERMARK（15）水印*1，*5
     *
     * OVERCOAT_FINISH_FINEMATTE（21）细哑光*4
     *
     * 面漆（22）光泽*3
     *
     * OVERCOAT_FINISH_PMATTE11（101）局部哑光（哑光）*1，*2
     *
     * OVERCOAT_FINISH_PMATTE12（121）部分哑光（细哑光）*1，*4
     *
     * OVERCOAT_FINISH_PMATTE13（122）部分哑光（光泽）*1，*3
     */
    public void setOvercoatFinish(){
        if(mPrint != null){
            boolean setResult = mPrint.SetOvercoatFinish(0, 0);
            Log.e("dawn", "setOvercoatFinish setResult = " + setResult);
        }
    }

    /**
     * 设置打印重试控制
     * PRINT_RETRY_OFF (0) Print retry [OFF]
     *   PRINT_RETRY_ON  (1) Print retry [ON]
     */
    public void setRetryControl(){
        if(mPrint != null){
            boolean setResult = mPrint.SetRetryControl(0, 0);
            Log.e("dawn", "setRetryControl setResult = " + setResult);
        }
    }

    public void sendImageData(String imagePath){
        if(mPrint != null){
            try{
                Log.e("dawn", "jpg change bmp start");
                byte[] bmpBytes = jpgChangeBmp(imagePath);
                boolean setResult = mPrint.SendImageData(0, bmpBytes, 0, 0, 1844, 1240);
                Log.e("dawn", "sendImageData setResult = " + setResult);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 发送打印数据
     */
    public void sendImageData(final Bitmap bitmap){
        if(mPrint != null && bitmap != null){
            try{
                Log.e("dawn", "jpg change bmp start");
                byte[] bmpBytes = null;
                if(bitmap.getWidth() < bitmap.getHeight()){
                    bmpBytes = bitmapChangeByte(rotateBitmap(bitmap));
                }else{
                    bmpBytes = bitmapChangeByte(bitmap);
                }
                boolean setResult = mPrint.SendImageData(0, bmpBytes, 0, 0, 1844, 1240);
                Log.e("dawn", "sendImageData setResult = " + setResult);

            }catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    /**
     * 图片转换成byte数组
     * @return
     */
    public byte[] jpgChangeBmp(String inputPath){
        // 二进制数据
//        String inputPath = Environment.getExternalStorageDirectory() + "/demo.png"; // 保存路径
        Bitmap jpgBitmap = BitmapFactory.decodeFile(inputPath);
        Bitmap rotatedBitmap = rotateBitmap(jpgBitmap);
        byte[] bmpBytes = bitmapChangeByte(rotatedBitmap);
        rotatedBitmap.recycle();
        return bmpBytes;
    }

    /**
     * 旋转图片90°
     * @return
     */
    Bitmap rotateBitmap(Bitmap bitmap){
        if(bitmap == null)
            return null;
        // 创建旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        // 旋转图片
        Bitmap rotatedBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        bitmap.recycle();
        return rotatedBitmap;
    }

    /**
     * bitmap转换成byte数组
     * bitmap需要自己回收
     * @return
     */
    public byte[] bitmapChangeByte(Bitmap bitmap){
        if(bitmap == null)
            return null;
        byte[] bmpBytes = ImageConverter.createBmpData(bitmap);
        return bmpBytes;
    }
}
