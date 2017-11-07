package com.vtradex.wms.server.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.IOException;

import com.runqian.base4.util.ImageUtils;
import com.runqian.base4.util.ReportError;
import com.runqian.report4.model.expression.Expression;
import com.runqian.report4.model.expression.Function;
import com.runqian.report4.model.expression.Variant2;
import com.runqian.report4.usermodel.Context;
import com.swetake.util.Qrcode;

/** 
 * 二维码QRCode函数。 快逸报表用。
 */
public class QRCode extends Function {

    public Object calculate(Context context, boolean arg1) {
        // 报表中调用该函数传递过来的参数列表  
        byte[] want = null;
        if (this.paramList.size() == 0) {
            throw new ReportError("二维码自定义函数参数列表为空");
        }
        // 取得计算表达式（得到传递给报表的参数）  
        Expression param1 = (Expression) this.paramList.get(0);
        if (param1 == null) {
            throw new ReportError("二维码自定义函数出现无效参数");
        }

        // 运算表达式,并取得运算结果(Object)  
        Object result1 = Variant2.getValue(param1.calculate(context, arg1), false, arg1);
        if (result1 == null) {
            return null;
        }
        if (result1 instanceof String) {
            String str = result1.toString();
            BufferedImage bi = anliCreateQrcode(str);
            try {
                want = ImageUtils.writePNG(bi);
            }
            catch (IOException e) {
                e.printStackTrace();
            }
        }
        return want;
    }
    
    private BufferedImage anliCreateQrcode(String data) {
        char errorCorrection = 'M';
        char encodeMode='B';
        Integer version = 5;
        Integer lineWidth=1;
        Integer lineHeight=1;
        Integer blankWidth=4;
        return createQrcode(data, errorCorrection, encodeMode, version, lineWidth, lineHeight, blankWidth);
    }

    /** 
     * 创建QRCode图形. 
     * @param data  二维码内容 
     * @param errorCorrection  纠错率，包括L（7%）、M（15%）、Q（25%）和H（30%），默认为M级 
     * @param encodeMode  编码模式，包括A、B、N，默认为B 
     * @param version  版本号，介于1-40。版本越高信息容量越大，默认可设为5<br/> 
     *            版本5，37*37模块，信息容量[L级：864；M级：688；Q级：496；H级：368]<br/> 
     *            更多可参考：http://www.qrcode.com/en/vertable1.html 
     * @param lineWidth  线宽，宽度越大图形越宽，默认为4 
     * @param lineWidth  线高，高度度越大图形越高，默认为4 默认与线宽一样 正方形
     * @param blankWidth  图形空白区宽度，应该设为线宽的倍数或0 
     */
    public BufferedImage createQrcode(String data, char errorCorrection, char encodeMode, Integer version, Integer lineWidth,Integer lineHeight, Integer blankWidth) {
        Qrcode qrcode = new Qrcode();
        qrcode.setQrcodeErrorCorrect(errorCorrection); // 纠错率，L, Q, H, M（默认）  
        qrcode.setQrcodeEncodeMode(encodeMode);// 模式，A, N, B（默认）  
        qrcode.setQrcodeVersion(version);// 版本，1-40  
        BufferedImage bi = null;
        Graphics2D g = null;
        try {
            byte[] d = data.getBytes("GB18030");
            boolean[][] b = qrcode.calQrcode(d);
            int qrcodeDataLength = b.length;
            // 图形宽度  
            int imageLength = qrcodeDataLength * lineWidth + blankWidth * 2;
            int imageHeight = qrcodeDataLength * lineHeight  + blankWidth * 2;
//            int imageHeight = imageLength; //图形高度
            bi = new BufferedImage(imageLength, imageHeight, BufferedImage.TYPE_INT_RGB);
            g = bi.createGraphics();
            g.setBackground(Color.WHITE);
            g.clearRect(0, 0, imageLength, imageHeight);
            g.setColor(Color.BLACK);
            for (int i = 0; i < b.length; i++) {
                for (int j = 0; j < b.length; j++) {
                    if (b[j][i]) {
                        g.fillRect(j * lineWidth + blankWidth, i * lineHeight + blankWidth, lineWidth, lineHeight);
                    }
                }
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        finally {
            if (g != null) {
                g.dispose();
            }
            if (bi != null) {
                bi.flush();
            }
        }

        return bi;
    }
}