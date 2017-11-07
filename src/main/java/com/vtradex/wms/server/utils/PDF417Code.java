package com.vtradex.wms.server.utils;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;

import com.lowagie2.text.pdf.BarcodePDF417;
import com.runqian.base4.util.ImageUtils;
import com.runqian.base4.util.ReportError;
import com.runqian.report4.model.expression.Expression;
import com.runqian.report4.model.expression.Function;
import com.runqian.report4.model.expression.Variant2;
import com.runqian.report4.usermodel.Context;

/** 
 * 二维码PDF417函数。 快逸报表用。 
 * 有些条码打印机精度不够，打印qrcode会出现枪扫不出来的情况，此时建议使用pdf417code
 */
public class PDF417Code extends Function {

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
            BarcodePDF417 pdf417 = new BarcodePDF417();
            pdf417.setText(str);//设置文本
            pdf417.setErrorLevel(8);//设置安全等级  
            pdf417.setYHeight(3);//设置宽窄比例
            Image barcode = pdf417.createAwtImage(Color.BLACK, Color.WHITE);
            int width = (int)barcode.getWidth(null);
            int height = (int)barcode.getHeight(null);
            BufferedImage bi = null;
            Graphics2D g = null;
            try {
                bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
                g = bi.createGraphics();
                g.setBackground(Color.WHITE);
                g.drawImage(barcode, 0, 0, Color.WHITE, null);
                want = ImageUtils.writePNG(bi);
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
            
        }
        return want;
    }
}