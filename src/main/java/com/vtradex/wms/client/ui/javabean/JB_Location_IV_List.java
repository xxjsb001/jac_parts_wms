package com.vtradex.wms.client.ui.javabean;

import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 *@author <a href="kaisheng.liu@vtradex.com">刘开胜</a>
 *@version $Project:swms $Date: 2015/03/25 02:48:49 $Version:V1.1
 */
public class JB_Location_IV_List extends ArrayList<JB_Location_IV> implements IsSerializable{
	private static final long serialVersionUID = -3394356295212733774L;

	public JB_Location_IV_List(){
		super();
	}
	
	public Object[] toArray() {
		Object[]  objs = new Object[this.size() + 1];
		
		int i = 0;
		
		for (JB_Location_IV liv : this) {
			if (i == 0) {
				objs[0] = liv.getLoc_line() + "排";
			} 
			
			objs[++i] = liv.getLoc_rate();
		}
		
		return objs;
	}
}