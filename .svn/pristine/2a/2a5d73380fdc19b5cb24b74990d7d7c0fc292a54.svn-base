/*
 * Copyright (c) 2001-2005 vTradEx Information Technology Co.,Ltd. All Rights Reserved.
 *
 * This software is the confidential and proprietary information of vTradEx
 * Information Technology Co.,Ltd. ("Confidential Information").  You shall not
 * disclose such Confidential Information and shall use it only in
 * accordance with the terms of the license agreement you entered into
 * with vTradEx.
 *
 * VTRADEX MAKES NO REPRESENTATIONS OR WARRANTIES ABOUT THE SUITABILITY OF THE
 * SOFTWARE, EITHER EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE, OR NON-INFRINGEMENT. VTRADEX SHALL NOT BE LIABLE FOR ANY DAMAGES
 * SUFFERED BY LICENSEE AS A RESULT OF USING, MODIFYING OR DISTRIBUTING
 * THIS SOFTWARE OR ITS DERIVATIVES.
 */

import com.vtradex.kangaroo.context.ContextLoader;

/**
 * @author: 李炎
 */
public class TelnetLuncher {
    private ContextLoader contextLoader;
    private String locatorFactorySelector = "classpath:telnet-beanRefContext.xml";
    private String parentContextKey = "com.vtradex.telnet";
    
	public String getLocatorFactorySelector() {
		return locatorFactorySelector;
	}
	public void setLocatorFactorySelector(String locatorFactorySelector) {
		this.locatorFactorySelector = locatorFactorySelector;
	}
	public String getParentContextKey() {
		return parentContextKey;
	}
	public void setParentContextKey(String parentContextKey) {
		this.parentContextKey = parentContextKey;
	}
	public static void main(String[] args){
        TelnetLuncher luncher = new TelnetLuncher();
        luncher.startServer();
    }
    public  void startServer(){
        this.contextLoader = new ContextLoader();
        this.contextLoader.setLocatorFactorySelector(locatorFactorySelector);
        this.contextLoader.setParentContextKey(parentContextKey);
        this.contextLoader.createApplicationContext();

    }
}

