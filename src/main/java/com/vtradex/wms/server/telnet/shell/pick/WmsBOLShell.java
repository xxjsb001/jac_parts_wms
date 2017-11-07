package com.vtradex.wms.server.telnet.shell.pick;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.lang.StringUtils;

import net.wimpi.telnetd.net.Connection;

import com.vtradex.kangaroo.component.support.ObjectOptionDisplayer;
import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.wms.server.model.carrier.WmsVehicle;
import com.vtradex.wms.server.telnet.dto.WmsBOLDTO;
import com.vtradex.wms.server.telnet.pick.WmsPickRFManager;
import com.vtradex.wms.server.telnet.shell.ShellExceptions;
import com.vtradex.wms.server.utils.MyUtils;

public class WmsBOLShell extends Thorn4BaseShell{
	public static final String CURRENT_LICENSE = "CURRENT_LICENSE";
	public static final String CURRENT_DTOS = "CURRENT_DTOS";
	public static final String ERROR_MESSAGE = "ERROR_MESSAGE";
	public static final String SCAN_CONTAINERS= "SCAN_CONTAINERS";
	public static final String PAGE_ID = "wmsBOLShell";
	private final WmsPickRFManager pickRFManager;
	
	public WmsBOLShell(WmsPickRFManager pickRFManager) {
		this.pickRFManager = pickRFManager;
	}
	@SuppressWarnings("unchecked")
	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException, Exception {

		Map<String,WmsBOLDTO> dtos = (Map<String, WmsBOLDTO>) this.getParentContext().get(CURRENT_DTOS);
		if(dtos==null){
			dtos = new HashMap<String, WmsBOLDTO>();
		}
		String license = (String) this.getParentContext().get(CURRENT_LICENSE);
		Set<String> containers = (Set<String>) this.getParentContext().get(SCAN_CONTAINERS);
		if(containers==null){
			containers = new HashSet<String>();
		}
		if(license == null){
			license = this.getTextField("车牌号");
			WmsVehicle vehicle = pickRFManager.checkVehicleByLicense(license);
			if(vehicle == null){
				this.setStatusMessage(ShellExceptions.VEHICLE_NULL);
			}
			this.put(CURRENT_LICENSE, license);
		}else{
			this.output("车牌号", license);
		}
		String container = this.getTextField("器具编码(00装车结束)");
		if(StringUtils.isEmpty(container) || "-".equals(container)){
			this.setStatusMessage(ShellExceptions.CONTAINER_CODE_IS_NULL);
		}
		if(MyUtils.OVER.equals(container)){
			List<String> goList =new ArrayList<String>();
			goList.add(MyUtils.YESE);
			goList.add(MyUtils.NO);
			String isChanged = (String)getListField("确认装车", goList, new ObjectOptionDisplayer());
			if(MyUtils.YESE.equals(isChanged)){
				
				this.put(CURRENT_DTOS, dtos);
				this.put(CURRENT_LICENSE, license);
				this.forward(WmsBOLOverShell.PAGE_ID,"装车确认中...");
			}else{
				this.put(CURRENT_DTOS, dtos);
				this.put(CURRENT_LICENSE, license);
				this.forward(WmsBOLShell.PAGE_ID,"请继续扫描下一个器具");
			}
		}
		String mes = "成功,扫描下一个器具";
		if(containers.contains(container)){
			mes = container+":已扫过";
		}else{
			containers.add(container);
			Map result = pickRFManager.checkMoveDocByContainer(container,dtos);
			if(StringUtils.isNotEmpty((String) result.get(ERROR_MESSAGE))){
//				this.setStatusMessage((String) result.get(ERROR_MESSAGE));
				mes = (String) result.get(ERROR_MESSAGE);
			}
			dtos = (Map<String, WmsBOLDTO>) result.get(CURRENT_DTOS);
		}
		this.put(SCAN_CONTAINERS, containers);
		this.put(CURRENT_DTOS, dtos);
		this.put(CURRENT_LICENSE, license);
		this.forward(WmsBOLShell.PAGE_ID,mes);
	}
}
