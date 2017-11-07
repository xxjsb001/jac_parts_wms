package com.vtradex.wms.client.ui.javabean;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *@author <a href="kaisheng.liu@vtradex.com">刘开胜</a>
 *@version $Project:swms $Date: 2015/03/25 02:48:49 $Version:V1.1
 *库存占用比对象
 */
public class JB_IV implements IsSerializable {

	private Long iv_wh_id;
	private String iv_wh_name;
	private Double iv_rate;
	private int x_Pos;
	private int y_Pos;
	private String image_url;//库区布局图
	
	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}

	public JB_IV(){}
	
	public JB_IV(Object id, Object name , Object rate , Object xPos , Object yPos , Object image) {
		this(id,name,rate,xPos,yPos);
		this.image_url = image == null?"" : (String)image;
	}
	
	public JB_IV(Object id, Object name , Object rate , Object xPos , Object yPos) {
		this((Long)id,(String)name,(Double)rate);
		this.x_Pos = (Integer)xPos;
		this.y_Pos = (Integer)yPos;
	}
	
	public JB_IV(Long id, String name , Double rate) {
		this.iv_wh_id = id;
		this.iv_wh_name = name;
		this.iv_rate = rate == null ? 0D : rate;
	}
	

	public int getX_Pos() {
		return x_Pos;
	}

	public void setX_Pos(int pos) {
		x_Pos = pos;
	}

	public int getY_Pos() {
		return y_Pos;
	}

	public void setY_Pos(int pos) {
		y_Pos = pos;
	}

	public Double getIv_rate() {
		return iv_rate;
	}
	public void setIv_rate(Double iv_rate) {
		this.iv_rate = iv_rate;
	}
	public Long getIv_wh_id() {
		return iv_wh_id;
	}
	public void setIv_wh_id(Long iv_wh_id) {
		this.iv_wh_id = iv_wh_id;
	}
	public String getIv_wh_name() {
		return iv_wh_name;
	}
	public void setIv_wh_name(String iv_wh_name) {
		this.iv_wh_name = iv_wh_name;
	}
	/** 转化为二维数组 */
	public Object[][] to2Array() {
		return new Object[][]{
				new Object[]{this.getIv_wh_name() , this.getIv_rate()}
		};
	}
	/** 获取坐标值 */
	public int[] toXY() {
		return new int[]{this.x_Pos , this.y_Pos};
	}
	
	/** 转化为数组 */
	public String[] toStrArray() {
		return new String[]{this.iv_wh_name , this.x_Pos + "" , this.y_Pos + ""};
	}
	
	/** 属性中文称呼数组 */
	public static String[] toLocaliedArray() {
		return new String[]{
				"序号",
				"仓库名称",
				"仓库容积率",
				"X坐标",
				"Y坐标",
				"仓库布局图"
		};
	}
}
