package com.dawn.libprintdnp;

import static jp.co.dnp.photoprintlib.DNPPhotoPrint.CUTTER_MODE_2INCHCUT;
import static jp.co.dnp.photoprintlib.DNPPhotoPrint.CUTTER_MODE_STANDARD;
import static jp.co.dnp.photoprintlib.DNPPhotoPrint.RESOLUTION300;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import jp.co.dnp.photoprintlib.DNPPhotoPrint;

public class MainActivity extends AppCompatActivity {

//    private DNPPhotoColorCnv mPrint;
    private DNPPhotoPrint mPrint;
    private int portNum = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mPrint = new DNPPhotoPrint(this);
    }

    public void initPrint(View view){
        if(mPrint != null){
            getPortNum(view);
            getFirmVersion(view);
            getPQTY(view);
            setResolution(view);
            setMediaSize(view);
            getFreeBuffer(view);
            setPQTY(view);
            setCutterMode(view);
            setOvercoatFinish(view);
            setRetryControl(view);
        }
    }

    /**
     * 获取打印机端口号
     */
    public void getPortNum(View view){

        int[][] portAry = new int[4][2];
        if(mPrint != null){
            portNum = mPrint.GetPrinterPortNum(portAry);//返回打印机连接数量，portAry是返回数据
            Log.e("dawn", "get port num = " + portNum);
            Log.e("dawn", "get port num = " + portAry[0][0] + " " + portAry[0][1]);

        }
    }

    public void getFirmVersion(View view){
        if(mPrint != null){
            char[] rbuf = new char[256];
            int firmVersion = mPrint.GetFirmwVersion(0, rbuf);
            Log.e("dawn", "getFirmVersion firm version = " + firmVersion);
            // char数组转String
            String str = String.valueOf(rbuf);
            Log.e("dawn", "getFirmVersion rbuf = " + str);
        }
    }

    public void getPQTY(View view){
        if(mPrint != null){
            int num = mPrint.GetPQTY(0);//获取剩余打印图片
            Log.e("dawn", "getPQTY num = " + num);
        }
    }

    /**
     * 设置分辨率
     */
    public void setResolution(View view){
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
    public void setMediaSize(View view){
        if(mPrint != null){
            boolean setResult = mPrint.SetMediaSize(0, 8);
            Log.e("dawn", "setMediaSize setResult = " + setResult);
        }
    }

    /**
     * 获取打印机上可用缓存大小
     */
    public void getFreeBuffer(View view){
        if(mPrint != null){
            int freeBuffer = mPrint.GetFreeBuffer(0);
            Log.e("dawn", "getFreeBuffer freeBuffer = " + freeBuffer);
        }
    }

    /**
     * 设置打印数量
     */
    public void setPQTY(View view){
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
    public void setCutterMode(View view){
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
    public void setOvercoatFinish(View view){
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
    public void setRetryControl(View view){
        if(mPrint != null){
            boolean setResult = mPrint.SetRetryControl(0, 0);
            Log.e("dawn", "setRetryControl setResult = " + setResult);
        }
    }

    public byte[] jpgChangeBmp(){
        // 二进制数据
        String inputPath = Environment.getExternalStorageDirectory() + "/demo.png"; // 保存路径
        String outFilePath = Environment.getExternalStorageDirectory() + "/demo.bmp"; // 保存路径
        Bitmap jpgBitmap = BitmapFactory.decodeFile(inputPath);
        // 创建旋转矩阵
        Matrix matrix = new Matrix();
        matrix.postRotate(90);
        // 旋转图片
        Bitmap rotatedBitmap = Bitmap.createBitmap(jpgBitmap, 0, 0, jpgBitmap.getWidth(), jpgBitmap.getHeight(), matrix, true);
        jpgBitmap.recycle();
        ImageConverter.saveBitmapAsBmp(rotatedBitmap, outFilePath);
        Bitmap outBitmap = BitmapFactory.decodeFile(outFilePath);
        //bitmap转换成byte[]
        byte[] bmpBytes = ImageConverter.createBmpData(outBitmap);
        rotatedBitmap.recycle();
        outBitmap.recycle();
        return bmpBytes;
    }

    /**
     * 发送打印数据
     * @param view
     */
    public void sendImageData(View view){
        if(mPrint != null){

            new Thread(){
                @Override
                public void run() {
                    super.run();
                    try{
                        //从assets中读取bmp图片并且转换成byte[]
//                        InputStream is = getResources().getAssets().open("01_TEST_PC_300.bmp");
//                        InputStream is = getResources().getAssets().open("testt2.bmp");
//                        byte[] data = new byte[is.available()];
//                        is.read(data);
//                        is.close();
                        Log.e("dawn", "jpg change bmp start");
                        byte[] bmpBytes = jpgChangeBmp();
                        Log.e("dawn", "jpg change bmp end");
                        Log.e("dawn", "absolute path " + Environment.getExternalStorageDirectory().getAbsolutePath() + "/output.bmp");
//

                        boolean setResult = mPrint.SendImageData(0, bmpBytes, 0, 0, 1844, 1240);
                        Log.e("dawn", "sendImageData setResult = " + setResult);

                    }catch (Exception e){
                        e.printStackTrace();
                    }

                }
            }.start();
        }
    }

}