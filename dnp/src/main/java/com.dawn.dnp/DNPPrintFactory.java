package com.dawn.dnp;

import android.content.Context;
import android.graphics.Bitmap;

/**
 * dnp打印机工厂类
 */
public class DNPPrintFactory {
    private static DNPPrintFactory instance;
    private Context mContext;
    private DNPPrintFactory(Context context) {
        mContext = context;
        dnpPrintUtil = new DNPPrintUtil(context);
    }

    public static DNPPrintFactory getInstance(Context context) {
        if (instance == null) {
            synchronized (DNPPrintFactory.class) {
                if (instance == null) {
                    instance = new DNPPrintFactory(context);
                }
            }
        }
        return instance;
    }

    private DNPPrintUtil dnpPrintUtil;
    //初始化打印机
    public void initPrint(){
        if(dnpPrintUtil == null)
            dnpPrintUtil = new DNPPrintUtil(mContext);
        dnpPrintUtil.getPortNum();
        dnpPrintUtil.getFirmVersion();
        dnpPrintUtil.getPQTY();
        dnpPrintUtil.setResolution();
        dnpPrintUtil.setMediaSize();
        dnpPrintUtil.getFreeBuffer();
        dnpPrintUtil.setPQTY();
        dnpPrintUtil.setCutterMode();
        dnpPrintUtil.setOvercoatFinish();
        dnpPrintUtil.setRetryControl();
    }

    /**
     * 打印照片
     * @param path
     */
    public void printImage(String path){
        dnpPrintUtil.sendImageData(path);
    }

    /**
     * 打印照片，Bitmap需要自己回收
     * @param bitmap
     */
    public void printImage(Bitmap bitmap){
        dnpPrintUtil.sendImageData(bitmap);
    }

}
