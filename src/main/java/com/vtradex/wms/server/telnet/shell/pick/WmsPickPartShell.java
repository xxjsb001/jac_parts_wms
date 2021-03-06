package com.vtradex.wms.server.telnet.shell.pick;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

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

/**
 * RF拣货(散件,器具,时序)
 * @author yc.min
 *
 */
public class WmsPickPartShell extends Thorn4BaseShell{
	
	private final WmsPickRFManager pickRFManager;
	
	public WmsPickPartShell(WmsPickRFManager pickRFManager) {
		this.pickRFManager = pickRFManager;
	}

	public static final String PAGE_ID = "wmsPickPartShell"; 
	public static final String MOVE_DETAIL_ID = "MOVE_DETAIL_ID";
	public static final String PICK_TYPE = "PICK_TYPE";//容器/散件/时序
	public static final String CURRENT_DTO = "CURRENT_DTO";
	public static final String IS_CONTAINER = "IS_CONTAINER";
	public static final String IS_LOC = "IS_LOC";
	public static final String IS_SAME_CONTAINER_DIF_TASK = "IS_SAME_CONTAINER_DIF_TASK";//同一个容器不同task
	public static final String GLOBAL_CONTAINER = "GLOBAL_CONTAINER";
	
	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {
		String message = "";
		Long moveDocId = (Long)this.getParentValue(WmsRfMoveDocDetailShell.MOVEDOC_ID);
		Long moveDetailId = Long.parseLong(this.getParentValue(MOVE_DETAIL_ID).toString());
		String picktype = (String) this.getParentValue(PICK_TYPE);//容器、散件、时序
		WmsPickContainerDTO dto = (WmsPickContainerDTO) this.getParentContext().get(CURRENT_DTO);
		if(dto == null || dto.getUnMoveQuantityBU() == 0D){
			dto = pickRFManager.getWmsTaskByMoveDocId(moveDetailId,picktype);
		}
		if(dto == null){
			message = ShellExceptions.NEXT_ITEM;
			this.context.put(WmsRfMoveDocDetailShell.MOVEDOC_ID, moveDocId);
			this.forward(WmsRfMoveDocDetailShell.PAGE_ID,message);
		}
		Boolean isContainer = this.getParentValue(IS_CONTAINER)==null?true:(Boolean) this.getParentValue(IS_CONTAINER);
		Boolean isLoc = this.getParentValue(IS_LOC)==null?true:(Boolean) this.getParentValue(IS_LOC);
		Boolean isSameContainerDifTask = this.getParentValue(IS_SAME_CONTAINER_DIF_TASK)==null?false:(Boolean) this.getParentValue(IS_SAME_CONTAINER_DIF_TASK);
		
		this.output("item.code", dto.getItemCode());
		this.output("item.name", dto.getItemName());
		this.output("supplier.name", dto.getSupplier());
		if(dto.getProductionLine()!=null){
			this.output("production.line", dto.getProductionLine());
		}
		if(isContainer){
			this.output("will.pickQty", dto.getUnMoveQuantityBU().toString());
			this.output("container.type", dto.getType());
			this.output("container.name", dto.getTypeName());
			String container = "";
			if(isSameContainerDifTask){
				container = (String) this.getParentValue(GLOBAL_CONTAINER);
			}else{
				container = this.getTextField("scan.container");
				if(StringUtils.isEmpty(container)){
					this.setStatusMessage(ShellExceptions.CONTAINER_CODE_IS_NULL);
				}
				container = container.trim();
				if(MyUtils.OVER.equals(container)){//返回明细界面
					//返回明细菜单
					this.put(WmsRfMoveDocDetailShell.MOVEDOC_ID, moveDocId);
					this.forward(WmsRfMoveDocDetailShell.PAGE_ID);
				}
				String mes = pickRFManager.checkContainerByBoxType(container, dto.getType(),dto.getBoxTag(),moveDocId);
				if(!MyUtils.SUCCESS.equals(mes)){
					this.put(CURRENT_DTO, dto);
					this.put(MOVE_DETAIL_ID, moveDetailId);
					this.put(PICK_TYPE, picktype);
					this.put(IS_CONTAINER, true);
					this.put(WmsRfMoveDocDetailShell.MOVEDOC_ID, moveDocId);
					this.forward(WmsPickPartShell.PAGE_ID,mes);
				}
			}
			dto.setContainer(container);
			//校验通过,绑定当前拣选的关系表对象
			dto = pickRFManager.getBindByContainerId(moveDetailId, dto);
			if(ShellExceptions.CONTAINER_NULL_FINISH.equals(dto.getErrorMes())){
				this.remove("scan.container");
				this.setStatusMessage(dto.getErrorMes());
			}
			this.put(CURRENT_DTO, dto);//此时dto具备容器所有信息
			this.put(MOVE_DETAIL_ID, moveDetailId);
			this.put(PICK_TYPE, picktype);
			this.put(IS_CONTAINER, false);
			this.put(WmsRfMoveDocDetailShell.MOVEDOC_ID, moveDocId);
			this.forward(WmsPickPartShell.PAGE_ID);
		}else{
			Double viewQty = 99999D;
			this.output("container.code", dto.getContainer());
			if(MyUtils.PARTS.equals(dto.getType())){
				this.output("item.qty", dto.getOrderBU().toString());
			}else if(MyUtils.SPS_APPLIANCE.equals(picktype)){
				//时序件,只显示任务待拣量,因为task任务量永远小于等于容器总装载量
				viewQty = pickRFManager.viewQty(moveDetailId, dto.getBoxTag());
			}else{
				this.output("container.qty", dto.getOrderBU().toString());
			}
			if(isLoc){
				this.output("pull.location", dto.getLocationCode());
				if(viewQty>=dto.getUnMoveQuantityBU()){
					this.output("will.pickQty", dto.getUnMoveQuantityBU().toString());
				}else{
					this.output("container.qty", viewQty.toString());
				}
				
				String locCode = this.getTextField("pick.location");
				if(StringUtils.isEmpty(locCode)){
					this.setStatusMessage(ShellExceptions.PICK_LOC_IS_NULL);
				}
				if(MyUtils.THIS.equals(locCode)){
					locCode = dto.getLocationCode();
				}
				locCode = locCode.trim();
				if(MyUtils.OVER.equals(locCode)){
					this.context.put(WmsRfMoveDocDetailShell.MOVEDOC_ID, moveDocId);
					if(MyUtils.SPS_APPLIANCE.equals(picktype)){
						this.put(WmsPickContainerCodeShell.BOX_TAG, dto.getBoxTag());
						this.put(WmsPickContainerCodeShell.BOX_TYPE, dto.getType());
						this.put(WmsPickMoveDocShell.MOVEDOC_ID, moveDocId);
						this.forward(WmsPickContainerCodeShell.PAGE_ID);
					}else{
						this.forward(WmsRfMoveDocDetailShell.PAGE_ID);
					}
				}
				dto.setPickLocCode(locCode);
				if(!dto.getLocationCode().equals(locCode)){
					List<String> goList =new ArrayList<String>();
					goList.add(MyUtils.YESE);
					goList.add(MyUtils.NO);
					String isChanged = (String)getListField("pull.location.change", goList, new ObjectOptionDisplayer());
					if(MyUtils.YESE.equals(isChanged)){
						pickRFManager.saveLogs(WmsLogType.NOTES, WmsLogTitle.LOCATION_CHANGE, dto.getLocationCode()+"-->"+locCode, dto.getContainer());
						message = "录入拣货量";
						this.context.put(IS_LOC, false);
					}else{
						message = "请重新扫描库位";
						this.context.put(IS_LOC, true);
					}
				}else{
					message = "录入拣货量";
					this.put(IS_LOC, false);
				}
				this.put(CURRENT_DTO, dto);
				this.put(MOVE_DETAIL_ID, moveDetailId);
				this.put(PICK_TYPE, picktype);
				this.put(IS_CONTAINER, false);
				this.put(WmsRfMoveDocDetailShell.MOVEDOC_ID, moveDocId);
				this.forward(WmsPickPartShell.PAGE_ID,message);
			}else{
				this.output("picked.location", dto.getPickLocCode());
				Double availableQuantity = 0D;
				if(viewQty>=dto.getUnMoveQuantityBU()){
					availableQuantity = dto.getUnMoveQuantityBU();
					this.output("will.pickQty", dto.getUnMoveQuantityBU().toString());
				}else{
					availableQuantity = viewQty;
					this.output("container.qty", viewQty.toString());
				}
				Double pickQuantity = this.getNumberField("pick.qty");

				if(pickQuantity==null || pickQuantity<0){
					this.setStatusMessage(ShellExceptions.PICK_QTY_IS_ERROE);
				}
				if(pickQuantity >  availableQuantity){
					this.setStatusMessage(ShellExceptions.PICK_QTY_IS_OVER);
				}
				dto.setPickQuantity(pickQuantity);
				dto = pickRFManager.confimPickByPart(dto,picktype);
				if(ShellExceptions.CONTAINER_FULL.equals(dto.getErrorMes())){
					if(MyUtils.SPS_APPLIANCE.equals(picktype)){//时序件跳转到容器明细界面,继续下一个料的拣货操作
						this.put(WmsPickContainerCodeShell.IS_ITEMS, true);
						this.put(IS_CONTAINER, false);
						this.put(WmsPickContainerCodeShell.CONTAINER, dto.getContainer());
						this.put(WmsPickMoveDocShell.MOVEDOC_ID, moveDocId);
						this.put(WmsPickContainerCodeShell.BOX_TAG, dto.getBoxTag());
						this.put(WmsPickContainerCodeShell.BOX_TYPE, dto.getType());
						
						message = "容器"+dto.getContainer()+"物料摆放顺序";
						this.forward(WmsPickContainerCodeShell.PAGE_ID,message);
					}else{
						message = ShellExceptions.CONTAINER_NEXT;
						this.put(IS_CONTAINER, true);//扫下一个容器
					}
				}else if(ShellExceptions.CONTAINER_NOT_FULL.equals(dto.getErrorMes())){
					message = ShellExceptions.CONTAINER_NOT_FULL;
					this.put(IS_CONTAINER, false);//不用扫描,继续当前容器
					if(dto.getUnMoveQuantityBU()>0){
						this.put(CURRENT_DTO, dto);
					}else{//业务场景:A001容器订单量10,分别有两个task满足,t1和t2,t1=5pcs/L001库位,t2=5pcs/L002库位,假设t1已完成,界面直接跳到t2任务扫描库位
						this.put(IS_SAME_CONTAINER_DIF_TASK, true);
						this.put(IS_CONTAINER, true);
						this.put(GLOBAL_CONTAINER, dto.getContainer());
					}
				}else if(ShellExceptions.PICK_QTY_NOT_FULL.equals(dto.getErrorMes())){
					//返回明细菜单
					this.put(WmsRfMoveDocDetailShell.MOVEDOC_ID, moveDocId);
					this.forward(WmsRfMoveDocDetailShell.PAGE_ID,dto.getErrorMes());
				}else{
					this.put(CURRENT_DTO, dto);
					this.put(MOVE_DETAIL_ID, moveDetailId);
					this.put(PICK_TYPE, picktype);
					this.put(IS_CONTAINER, false);
					this.put(WmsRfMoveDocDetailShell.MOVEDOC_ID, moveDocId);
					this.forward(WmsPickPartShell.PAGE_ID,dto.getErrorMes());
				}
				//拣货确认之后,继续找该物料的下一个task,直到找不到task跳到拣货单明细界面,重新选择下一个物料拣选
				this.put(MOVE_DETAIL_ID, moveDetailId);
				this.put(PICK_TYPE, picktype);
				this.put(WmsRfMoveDocDetailShell.MOVEDOC_ID, moveDocId);
				this.forward(WmsPickPartShell.PAGE_ID,message);
			}

		}
		
	}
}
