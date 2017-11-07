/**
 * 
 */
package com.vtradex.wms.client.ui.javabean;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author <a href="mailto:jin.liu@vtradex.net">刘晋</a>
 * @since 2012-11-6 下午04:50:09
 */
public class JB_Zone_Location_data implements IsSerializable{

	private int currentMaxLine = 0;
	private int currentMaxRow = 0;
	private int currentMaxCol = 0;
	
	public JB_Zone_Location_data(){}
	
	public JB_Zone_Location_data(int currentMaxLine,int currentMaxRow,int currentMaxCol ) {
		this.currentMaxLine = currentMaxLine;
		this.currentMaxRow = currentMaxRow;
		this.currentMaxCol = currentMaxCol;
	}

	public int getCurrentMaxLine() {
		return currentMaxLine;
	}
	public void setCurrentMaxLine(int currentMaxLine) {
		this.currentMaxLine = currentMaxLine;
	}
	public int getCurrentMaxRow() {
		return currentMaxRow;
	}
	public void setCurrentMaxRow(int currentMaxRow) {
		this.currentMaxRow = currentMaxRow;
	}
	public int getCurrentMaxCol() {
		return currentMaxCol;
	}
	public void setCurrentMaxCol(int currentMaxCol) {
		this.currentMaxCol = currentMaxCol;
	}
	
	
}
