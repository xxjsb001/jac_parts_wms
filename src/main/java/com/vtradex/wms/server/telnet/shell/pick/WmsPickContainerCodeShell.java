package com.vtradex.wms.server.telnet.shell.pick;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import net.wimpi.telnetd.net.Connection;

import com.vtradex.kangaroo.component.support.ObjectOptionDisplayer;
import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.wms.server.model.base.WmsLogTitle;
import com.vtradex.wms.server.model.base.WmsLogType;
import com.vtradex.wms.server.telnet.dto.WmsPickContainerDTO;
import com.vtradex.wms.server.telnet.pick.WmsPickRFManager;
import com.vtradex.wms.server.telnet.shell.ShellExceptions;
import com.vtradex.wms.server.utils.MyUtils;
/**时序件拣货 */
public class WmsPickContainerCodeShell extends Thorn4BaseShell{
	
	public static final String PAGE_ID = "wmsPickContainerCodeShell";
	public static final String IS_CONTAINER = "IS_CONTAINER";
	public static final String IS_ITEMS = "IS_ITEMS";
	/**器具标签*/
	public static final String BOX_TAG = "BOX_TAG";
	/**器具类型*/
	public static final String BOX_TYPE = "BOX_TYPE";
	/**器具名称*/
	public static final String BOX_NAME = "BOX_NAME";
	
	public static final String CONTAINER = "CONTAINER";
	
	private final WmsPickRFManager pickRFManager;
	
	public WmsPickContainerCodeShell(WmsPickRFManager pickRFManager) {
		this.pickRFManager = pickRFManager;
	}
	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		
		String messge = "";
		Long moveDocId = (Long)this.getParentValue(WmsPickMoveDocShell.MOVEDOC_ID);
		
		Boolean isContainer = this.getParentValue(IS_CONTAINER)==null?false:(Boolean) this.getParentValue(IS_CONTAINER);
		Boolean isItems = this.getParentValue(IS_ITEMS)==null?false:(Boolean) this.getParentValue(IS_ITEMS);
		
		
		if(!isContainer && !isItems){
			/************************明细任务选择区域*********************************/
			List<Object[]> list = pickRFManager.findUnAppliance(moveDocId);
			if(list==null || list.size()<=0){
				messge = ShellExceptions.CONTAINER_TAG_NONE;
				this.forward(WmsPickMoveDocShell.PAGE_ID,messge);
			}
			Map<String,String> moveCodes = new HashMap<String,String>();
			Map<String,String[]> tagTypes = new HashMap<String,String[]>();
			Map<String,String> containers = new HashMap<String,String>();
			String key = null,type = "",typeName = "",counts = "",nums = "",boxTag = "",container = "";
			int i = 1;
			output("NO"+" |"+"型号"+" |"+"种类"+" |"+"总量"+" |"+"器具标签");
			Iterator iMes = list.iterator();
			while(iMes.hasNext()){
				Map m = (Map) iMes.next();
				type = m.get("TYPE")==null?"-": m.get("TYPE").toString();
				typeName = m.get("TYPE_NAME")==null?"-": m.get("TYPE_NAME").toString();
				counts = m.get("COUNTS")==null?"-": m.get("COUNTS").toString();
				nums = m.get("NUMS")==null?"-": m.get("NUMS").toString();
				boxTag = m.get("BOX_TAG")==null?"-": m.get("BOX_TAG").toString();
				container = m.get("CONTAINER")==null?"": m.get("CONTAINER").toString();
				key = i+"";
				output(key,type+" | "+counts+" | "+nums+" |"+boxTag);
				
				moveCodes.put(key, boxTag);
				tagTypes.put(boxTag, new String[]{
						type,typeName
				});
				containers.put(boxTag, container);
				i++;
			}
			String boxTagNo = this.getTextField("pickNo");
			if(MyUtils.OVER.equals(boxTagNo)){
				messge = "已返回拣货列表菜单";
				this.forward(WmsPickMoveDocShell.PAGE_ID,messge);
			}
			boxTagNo = moveCodes.get(boxTagNo);
			if(boxTagNo==null){
				messge = "序号有误,请重新录入";
				this.put(WmsPickMoveDocShell.MOVEDOC_ID, moveDocId);
				this.forward(WmsPickContainerCodeShell.PAGE_ID,messge);
			}
			container = containers.get(boxTagNo);
			//还未开始拣货的允许再次扫描容器,已经开始拣货的,就不用再扫描容器了(应该是统计他的明细,要根据容器信息加器具标签来定位)
			int picQty = pickRFManager.sumPickQtys(moveDocId, boxTagNo,container);
			if(picQty>0){
				messge = "容器"+container+"物料摆放顺序";
				this.put(IS_CONTAINER, false);
				this.put(IS_ITEMS, true);
			}else{
				//进入扫码区域
				messge = "请扫描容器编码";
				this.put(IS_CONTAINER, true);
			}
			this.put(CONTAINER, container);
			this.put(WmsPickMoveDocShell.MOVEDOC_ID, moveDocId);
			this.put(BOX_TAG, boxTagNo);
			this.put(BOX_TYPE, tagTypes.get(boxTagNo)[0]);
			this.put(BOX_NAME, tagTypes.get(boxTagNo)[1]);
			this.forward(WmsPickContainerCodeShell.PAGE_ID,messge);
		}
		if(isContainer){
			/************************明细任务扫码区域*********************************/
			String boxType = (String) this.getParentValue(BOX_TYPE);
			String boxTagNo = (String) this.getParentValue(BOX_TAG);
			String boxName = (String) this.getParentValue(BOX_NAME);
			
			this.output("container.type", boxType);
			this.output("container.name", boxName);
			String container = this.getTextField("scan.container");
			if(StringUtils.isEmpty(container)){
				this.setStatusMessage(ShellExceptions.CONTAINER_CODE_IS_NULL);
			}
			container = container.trim();
			if(MyUtils.OVER.equals(container)){
				this.put(WmsPickMoveDocShell.MOVEDOC_ID, moveDocId);
				this.forward(WmsPickContainerCodeShell.PAGE_ID);
			}
			this.put(WmsPickMoveDocShell.MOVEDOC_ID, moveDocId);
			this.put(BOX_TAG, boxTagNo);
			this.put(BOX_TYPE, boxType);
			this.put(BOX_NAME, boxName);
			String globalContainer = this.getParentValue(CONTAINER)==null?"":(String) this.getParentValue(CONTAINER);
			if(!StringUtils.isEmpty(globalContainer)){
				Boolean isChangeContainer = false;
				if(!container.equals(globalContainer)){//容器更换了,要提示是否跟换容器
					List<String> goList =new ArrayList<String>();
					goList.add(MyUtils.YESE);
					goList.add(MyUtils.NO);
					String isChanged = (String)getListField("container.is.change", goList, new ObjectOptionDisplayer());
					if(MyUtils.YESE.equals(isChanged)){
						isChangeContainer = true;
					}
					if(isChangeContainer){
						pickRFManager.saveLogs(WmsLogType.NOTES, WmsLogTitle.CONTAINER_CHANGE, globalContainer+"-->"+container, boxTagNo);
						//源容器释放
						pickRFManager.releaseContainer(globalContainer);
					}else{//重新扫描容器
						this.put(IS_CONTAINER, true);
						this.put(IS_ITEMS, false);
						this.put(CONTAINER, globalContainer);
						this.forward(WmsPickContainerCodeShell.PAGE_ID,messge);
					}
				}
			}
			messge = pickRFManager.checkContainerByBoxType(container, boxType,boxTagNo,moveDocId);
			if(!MyUtils.SUCCESS.equals(messge)){
				this.put(IS_CONTAINER, true);
				this.put(IS_ITEMS, false);
				this.put(CONTAINER, globalContainer);
				this.forward(WmsPickContainerCodeShell.PAGE_ID,messge);
			}else{
				messge = "容器"+container+"物料摆放顺序";
			}
			//绑定器具编码与标签关系 yc.min 20171031 注释 器具关系要数量级拆分
//			pickRFManager.getBindByContainerId(moveDocId, boxTagNo, container);
			this.put(IS_CONTAINER, false);
			this.put(IS_ITEMS, true);
			this.put(CONTAINER, container);
			this.forward(WmsPickContainerCodeShell.PAGE_ID,messge);
		}
		if(isItems){
			/************************明细任务选择区域*********************************/
			String boxType = (String) this.getParentValue(BOX_TYPE);//器具型号
			String boxTagNo = (String) this.getParentValue(BOX_TAG);//器具标签
			String container = (String) this.getParentValue(CONTAINER);//器具编码
			//根据标签找出明细 产线
			List<Object[]> list = pickRFManager.findUnApplianceItems(moveDocId,boxTagNo);
			if(list==null || list.size()<=0){
				//进入:明细任务选择区域
				messge = ShellExceptions.CONTAINER_NEXT;
				this.put(WmsPickMoveDocShell.MOVEDOC_ID, moveDocId);
				this.put(IS_CONTAINER, false);
				this.put(IS_ITEMS, false);
				this.forward(WmsPickContainerCodeShell.PAGE_ID,messge);
			}
			Map<String,Long> moveCodes = new HashMap<String,Long>();
			Map<Long,Long> moveDetailIds = new HashMap<Long,Long>();
			Map<Long,Double> availableQuantity = new HashMap<Long,Double>();
			String key = null,itemCode = "",quantity = "",seq = "",endSeq = "";
			Long id = 0L,moveDetailId = 0L;
			int i = 1;
			output("NO"+" |"+"物料编码"+" |"+"待拣量"+" |"+"排序");
			Iterator iMes = list.iterator();
			while(iMes.hasNext()){
				Map m = (Map) iMes.next();
				id = ((BigDecimal) m.get("ID")).longValue();
				moveDetailId =  ((BigDecimal) m.get("MM_ID")).longValue();
				
				itemCode = m.get("ITEM_CODE")==null?"-": m.get("ITEM_CODE").toString();
				quantity = m.get("QUANTITY")==null?"-": m.get("QUANTITY").toString();
				seq = m.get("SEQ")==null?"-": m.get("SEQ").toString();
				endSeq = m.get("END_SEQ")==null?"-": m.get("END_SEQ").toString();
				key = i+"";
				output(key,itemCode+" | "+quantity+" | "+seq+"-"+endSeq);
				
				moveCodes.put(key, id);
				availableQuantity.put(id, Double.valueOf(quantity));
				moveDetailIds.put(id, moveDetailId);
				i++;
			}
			String pickNo = this.getTextField("pickNo");
			if(MyUtils.OVER.equals(pickNo)){
				messge = "已返回拣货列表菜单";
				this.forward(WmsPickMoveDocShell.PAGE_ID,messge);
			}
			id = moveCodes.get(pickNo);
			if(id==null){
				messge = "序号有误,请重新录入";
				this.put(BOX_TAG, boxTagNo);
				this.put(BOX_TYPE, boxType);
				this.put(WmsPickMoveDocShell.MOVEDOC_ID, moveDocId);
				this.put(CONTAINER, container);
				
				this.put(IS_CONTAINER, false);
				this.put(IS_ITEMS, true);
				this.forward(WmsPickContainerCodeShell.PAGE_ID,messge);
			}
			//要把容器信息给过去
			moveDetailId = moveDetailIds.get(id);
			WmsPickContainerDTO dto = pickRFManager.getWmsTaskByMoveDocId(moveDetailId,MyUtils.SPS_APPLIANCE);
			if(dto == null){
				messge = ShellExceptions.TASKS_NULL+":"+boxTagNo;
				pickRFManager.saveLogs(WmsLogType.ERROR, WmsLogTitle.CONTAINER_PICKING,messge+":"+"moveDetailId>"+moveDetailId,MyUtils.SPS_APPLIANCE);
				this.put(BOX_TAG, boxTagNo);
				this.put(BOX_TYPE, boxType);
				this.put(WmsPickMoveDocShell.MOVEDOC_ID, moveDocId);
				this.put(CONTAINER, container);
				
				this.put(IS_CONTAINER, false);
				this.put(IS_ITEMS, true);
				this.forward(WmsPickContainerCodeShell.PAGE_ID,messge);
			}
			dto.setWmsMoveDocAndStationId(id);
			dto.setOrderBU(availableQuantity.get(id));
			dto.setBoxTag(boxTagNo);
			dto.setContainer(container);
			dto.setType(boxType);
			
			this.put(WmsPickPartShell.PICK_TYPE, MyUtils.SPS_APPLIANCE);
			this.put(WmsPickPartShell.CURRENT_DTO, dto);
			this.put(IS_CONTAINER, false);
			this.put(WmsPickMoveDocShell.MOVEDOC_ID, moveDocId);
			this.put(WmsPickPartShell.MOVE_DETAIL_ID, moveDetailId);
			this.forward(WmsPickPartShell.PAGE_ID);
		}
		this.forward(WmsPickMoveDocShell.PAGE_ID);
	}

}
