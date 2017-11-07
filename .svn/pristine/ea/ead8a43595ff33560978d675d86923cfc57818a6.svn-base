package com.vtradex.wms.server.telnet.shell.replenishment;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

import net.wimpi.telnetd.net.Connection;

import com.vtradex.kangaroo.component.support.ObjectOptionDisplayer;
import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.ShellFactory;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.wms.server.telnet.move.WmsMoveRFManager;
/**补货下架*/
public class WmsReplenishmentMovePicDoShell extends Thorn4BaseShell {
	public static final String PAGE_ID = "wmsReplenishmentMovePicDoShell";
	private final WmsMoveRFManager rfWmsMoveManager;

	public WmsReplenishmentMovePicDoShell(WmsMoveRFManager rfWmsMoveManager) {
		this.rfWmsMoveManager = rfWmsMoveManager;
	}
	@SuppressWarnings("unchecked")
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception,RuntimeException {
		String moveCode = this.getParentContext().get("moveCode").toString();
		Long moveId = Long.parseLong(this.getParentValue("moveId").toString());
		List<Object[]> dds = (List<Object[]>) this.getParentContext().get("dds");
		String beGo = "-",messge = moveCode+"已完成,还未激活",point = "-";
		for(int i =0 ; i<dds.size() ; i++){
			Object[] obj = dds.get(i);//[id,item.id,item.code,extendPropC1,supplier,toLocationId,toLocationCode]
			List<Object[]> ix = rfWmsMoveManager.findInventory(obj);
			List<String> goList =new ArrayList<String>();
			goList.add("是");
			goList.add("否");
			if(ix.size()<=0){
				messge = "物料无库存:"+obj[2];
				output("失败",messge);
				beGo = (String)getListField("是否继续", goList, new ObjectOptionDisplayer());
				if(beGo.equals("是")){
					point = "novalue-go";
					break;
				}else{
					point = "novalue-back";
					break;
				}
			}else{
				Object[] ixs = ix.get(0);//ix.id,locationCode,locationId,availableQty,托盘可用量
				output("库位",ixs[1].toString());
				output("物料",obj[2].toString());
				output("库存可用量",ixs[3].toString());
				output("托盘可用量",ixs[4].toString());
				output("工艺状态",obj[3].toString());
				String moveQty = this.getTextField("moveQty");
				if (StringUtils.isEmpty(moveQty)) {
					this.setStatusMessage("补货量必填");
				}
				if(moveQty.contains(".")){
					this.setStatusMessage("补货量不允许含有小数点");
				}
				Double tempQty = 0D;
				try {
					tempQty = Double.valueOf(moveQty);
				} catch (Exception e) {
					this.setStatusMessage("补货量录入错误");
				}
				if(tempQty>Double.valueOf(ixs[3].toString())){
					this.setStatusMessage("补货量超出可用量");
				}
				if(tempQty>Double.valueOf(ixs[4].toString())){
					this.setStatusMessage("补货量超出托盘可用量");
				}
				if(tempQty<0){
					this.setStatusMessage("补货量不允许小于0");
				}
				if(tempQty>0){//处理:当前物料不想补货但上一次未选择否,每次都会循环到该物料而不录入数据处理系统也不让跳过,录入0解决
					//自动分配+激活
					String runtimeMesge = "-";
					Boolean isCatch = false;
					try {
						rfWmsMoveManager.subscriberAutoAllocateWmsMoveDoc(ixs, tempQty, Long.parseLong(obj[0].toString()));
					} catch (Exception be) {
						runtimeMesge = be.getMessage()==null?"空指针,请联系唯智工程师":be.getMessage();
						isCatch = true;
					}
					if(isCatch){
						output("失败",runtimeMesge);
					}
				}
				beGo = (String)getListField("该物料是否继续", goList, new ObjectOptionDisplayer());
				if(beGo.equals("是")){
					point = "do-continue-notdelete";
					break;
				}else{
					point = "do-continue";
					break;
				}
			}
		}
		if(point.equals("do-continue")){//当前物料补货已经完成
			Object[] obj = dds.get(0);//[id,item.id,item.code,extendPropC1,supplier,toLocationId,toLocationCode]
			rfWmsMoveManager.setMoveDetailOver(obj);
		}
		if(point.equals("novalue-go") || point.equals("novalue-back") || point.equals("do-continue")){
			dds.remove(0);
			this.context.put("dds", dds);
			this.context.put("moveCode", moveCode);
			this.context.put("moveId", moveId);
		}else if(point.equals("do-continue-notdelete")){
			this.context.put("dds", dds);
			this.context.put("moveCode", moveCode);
			this.context.put("moveId", moveId);
		}
		if(point.equals("novalue-go")){
			this.forward(WmsReplenishmentMovePicDoShell.PAGE_ID,"上一条物料无库存!,请继续");
		}else if(point.equals("novalue-back")){
			this.forward(WmsReplenishmentMovePicShell.PAGE_ID,messge);
		}else if(point.equals("do-continue") || point.equals("do-continue-notdelete")){
			this.forward(WmsReplenishmentMovePicDoShell.PAGE_ID,"执行完成,请继续");
		}
		this.forward(WmsReplenishmentMovePicShell.PAGE_ID,messge);
	}
	protected void forwardByKeyboard(String value) throws BreakException {  
	    if (value.equalsIgnoreCase("XX")) {  
	        if(StringUtils.isEmpty(getShellByXX()))  
	            forward(ShellFactory.getMainShell());  
	        else  
	            forward(getShellByXX());  
	    } else if (value.equalsIgnoreCase("QQ")) {  
	        if(StringUtils.isEmpty(getShellByQQ()))  
	            forward(ShellFactory.getEntranceShell());  
	        else  
	            forward(getShellByQQ());  
	    }else if(value.equalsIgnoreCase("..")){//跳转至上一屏  
	        forwardByKeyboard("XX");  
	    }else if(value.equalsIgnoreCase("...")){//退出登录  
	        forwardByKeyboard("QQ");  
	    } 
	}
}
