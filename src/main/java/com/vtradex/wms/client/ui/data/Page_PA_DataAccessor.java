package com.vtradex.wms.client.ui.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.vtradex.thorn.client.data.DataAccessor;
import com.vtradex.thorn.client.message.IMessagePage;
import com.vtradex.wms.client.ui.constant.CT_PA;
import com.vtradex.wms.client.ui.javabean.PT_ALLOCATED;
import com.vtradex.wms.client.ui.javabean.PT_AVAILABLE;
import com.vtradex.wms.client.ui.javabean.PT_DETAILS;
import com.vtradex.wms.client.ui.javabean.PT_INFO;

@SuppressWarnings("unchecked")
public class Page_PA_DataAccessor extends DataAccessor {
	public static final String INIT_PICK_TICKETS_INFO = "init_pick_tickets_info";
	public static final String INIT_PROCESS_DOCS_INFO = "init_process_docs_info";
	public static final String INIT_ALLOCATED_PROCESS_DOCS_INFO = "initAllocatedProcessDocsInfo";
	public static final String INIT_PROCESS_DOC_DETAILS_INFO = "init_process_doc_details_info";
	public static final String INIT_DETAILS_INFO = "initDetailsInfo";
	public static final String INIT_AVAILABLE_INFO = "initAvailableInfo";
	public static final String INIT_AVAILABLE_PROCESS_INFO = "initAvailableProcessInfo";
	public static final String INIT_ALLOCATED_INFO = "initAllocatedInfo";
	public static final String AUTO_ALLOCATE_INFO = "autoAllocateInfo";
	public static final String AUTO_PROCESSDOC_ALLOCATE_INFO = "autoProcessDocAllocateInfo";
	public static final String SINGLE_CANCEL_ALLOCATE_INFO = "singleCancelAllocateInfo";
	public static final String SINGLE_CANCEL_WAVE_ALLOCATE_INFO = "singleCancelWaveAllocateInfo";
	public static final String SINGLE_CANCEL_PROCESSDOC_ALLOCATE_INFO = "singleCancelProcessDocAllocateInfo";
	public static final String ALL_CANCEL_ALLOCATE_INFO = "allCancelAllocateInfo";
	public static final String MANUAL_ALLOCATE_INFO = "manualAllocateInfo";
	public static final String ALL_CANCEL_PROCESSDOC_ALLOCATE_INFO = "allCancelProcessDocAllocateInfo";
	
	public static final String INIT_WAVE_DOC_INFO = "init_wave_doc_info";
	public static final String INIT_WD_DETAILS_INFO = "initWDDetailsInfo";
	public static final String INIT_WD_AVAILABLE_INFO = "initWDAvailableInfo";
	public static final String INIT_WD_ALLOCATED_INFO = "initWDAllocatedInfo";
	public static final String AUTO_WD_ALLOCATE_INFO = "autoWDAllocateInfo";
	public static final String CANCELL_WD_ALLOCATE_INFO = "cancellWDAllocateInfo";
	public static final String MANUAL_WD_ALLOCATE_INFO = "manualWDAllocateInfo";
	public static final String MANUAL_PROCESSDOC_ALLOCATE_INFO = "manualProcessDocAllocateInfo";
	
	/**当前选中的发货单*/
	private PT_INFO curPickTicket;
	
	/**同一发货单的明细*/
	private List<PT_DETAILS> pds;
	
	/**同一条明细的库存*/
	private List<PT_AVAILABLE> ptas;
	
	/**同一发货单的已分配记录*/
	private List<PT_ALLOCATED> ptad;
	
	/**当前选中的发货单明细号*/
	private Long ptdId;
	
	/**当前选中的波次明细号*/
	private Long wddId;
	
	/**页面标志*/
	private String flag;
	
	/**当前选中的加工单明细号*/
	private Long prdId;

	public Long getPrdId() {
		return prdId;
	}

	public void setPrdId(Long prdId) {
		this.prdId = prdId;
	}

	public Long getPtdId() {
		return ptdId;
	}

	public void setPtdId(Long ptdId) {
		this.ptdId = ptdId;
	}
	
	public Long getWddId() {
		return wddId;
	}

	public void setWddId(Long wddId) {
		this.wddId = wddId;
	}
	
	public String getFlag() {
		return flag;
	}

	public void setFlag(String flag) {
		this.flag = flag;
	}

	public  List<PT_DETAILS> all_PDS(){
		return this.pds;
	}
	
	public  List<PT_AVAILABLE> all_PTAS(){
		return this.ptas;
	}


	public  List<PT_ALLOCATED> all_PTAD(){
		return this.ptad;
	}
	
	public PT_INFO currentPickTicket() {
		return this.curPickTicket;
	}

	public void setCurPickTicket(PT_INFO curPickTicket) {
		this.curPickTicket = curPickTicket;
	}

	public Page_PA_DataAccessor(IMessagePage page) {
		super(page);
	}
	
	/*异步获取后台一条加工单明细的库存信息*/
	public void initAvailableProcessInfo(Map params) {
		this.remoteCall(INIT_AVAILABLE_PROCESS_INFO, CT_PA.PD_MANAGER, "init_AL_INFO", params);
	}
	
	/*异步获取后台加工单明细信息*/
	public void initProcessDocDetailsInfo(Map params) {
		this.remoteCall(INIT_PROCESS_DOC_DETAILS_INFO, CT_PA.PD_MANAGER, "init_PRDD_INFO", params);
	}
	
	/*异步获取加工单信息*/
	public void initProcessDocInfo(Map params) {
		this.remoteCall(INIT_PROCESS_DOCS_INFO, CT_PA.PD_MANAGER, "init_PRD_INFO", params);
	}
	
	/*异步获取后台加工单已分配信息*/
	public void initAllocatedProcessDocsInfo(Map params) {
		this.remoteCall(INIT_ALLOCATED_PROCESS_DOCS_INFO, CT_PA.PD_MANAGER, "init_APD_INFO", params);
	}
	
	//加工单部分分配	
	public void manualProcessDocAllocate(Map params) {
		this.remoteCall(MANUAL_PROCESSDOC_ALLOCATE_INFO, CT_PA.TRANSACTIONAL_MANAGER, "manualProcessDocAllocate", params);
	}
	
	/*异步获取后台发货单信息*/
	public void initPickTicketInfo(Map params) {
		this.remoteCall(INIT_PICK_TICKETS_INFO, CT_PA.PA_MANAGER, "init_PA_INFO", params);
	}
	
	/*异步获取后台发货单明细信息*/
	public void initDetailsInfo(Map params) {
		this.remoteCall(INIT_DETAILS_INFO, CT_PA.PA_MANAGER, "init_PD_INFO", params);
	}
	
	/*异步获取后台一条发货单明细的库存信息*/
	public void initAvailableInfo(Map params) {
		this.remoteCall(INIT_AVAILABLE_INFO, CT_PA.PA_MANAGER, "init_AL_INFO", params);
	}
	
	/*异步获取后台发货单已分配信息*/
	public void initAllocatedInfo(Map params) {
		this.remoteCall(INIT_ALLOCATED_INFO, CT_PA.PA_MANAGER, "init_ALD_INFO", params);
	}
	
	//发货单整单分配	
	public void atuoAllocate(Map params) {
		this.remoteCall(AUTO_ALLOCATE_INFO, CT_PA.TRANSACTIONAL_MANAGER, "allocate", params);
	}
	
	//加工单整单分配	
	public void atuoProcessDocAllocate(Map params) {
		this.remoteCall(AUTO_PROCESSDOC_ALLOCATE_INFO, CT_PA.TRANSACTIONAL_MANAGER, "auto_PD_allocate", params);
	}
	
	//发货单单一明细取消分配	
	public void singleCancelAllocate(Map params) {
		this.remoteCall(SINGLE_CANCEL_ALLOCATE_INFO, CT_PA.TRANSACTIONAL_MANAGER, "singleCancelAllocate", params);
	}
	
	//波次单单一明细取消分配	
	public void singleCancellWaveDocAllocate(Map params) {
		this.remoteCall(SINGLE_CANCEL_WAVE_ALLOCATE_INFO, CT_PA.WD_MANAGER, "singleCancellWaveDocAllocate", params);
	}
	
	//加工单单一明细取消分配	
	public void singleCancelProcessDocAllocate(Map params) {
		this.remoteCall(SINGLE_CANCEL_ALLOCATE_INFO, CT_PA.TRANSACTIONAL_MANAGER, "singleCancelAllocate", params);
	}
	
	//加工单整单取消分配
	public void allCancelProcessDocAllocate(Map params) {
		this.remoteCall(ALL_CANCEL_PROCESSDOC_ALLOCATE_INFO, CT_PA.TRANSACTIONAL_MANAGER, "allCancelProcessDocAllocate", params);
	}
	
	//发货单整单取消分配
	public void allCancelAllocate(Map params) {
		this.remoteCall(ALL_CANCEL_ALLOCATE_INFO, CT_PA.TRANSACTIONAL_MANAGER, "allCancelAllocate", params);
	}
	
	//发货单部分分配	
	public void manualAllocate(Map params) {
		this.remoteCall(MANUAL_ALLOCATE_INFO, CT_PA.TRANSACTIONAL_MANAGER, "manualAllocate", params);
	}

	/*异步获取后台波次信息*/
	public void initWaveDocInfo(Map params) {
		this.remoteCall(INIT_WAVE_DOC_INFO, CT_PA.WD_MANAGER, "init_WD_INFO", params);
	}
	
	/*异步获取后台波次明细信息*/
	public void initWaveDocDetailsInfo(Map params) {
		this.remoteCall(INIT_WD_DETAILS_INFO, CT_PA.WD_MANAGER, "init_WDD_INFO", params);
	}
	
	/*异步获取后台一条波次明细的库存信息*/
	public void initWaveDocAvailableInfo(Map params) {
		this.remoteCall(INIT_WD_AVAILABLE_INFO, CT_PA.WD_MANAGER, "init_WDAL_INFO", params);
	}
	
	/*异步获取后台波次已分配信息*/
	public void initWaveDocAllocatedInfo(Map params) {
		this.remoteCall(INIT_WD_ALLOCATED_INFO, CT_PA.WD_MANAGER, "init_WDALD_INFO", params);
	}
	//波次整单分配	
	public void atuoWaveDocAllocate(Map params){
		this.remoteCall(AUTO_WD_ALLOCATE_INFO, CT_PA.WD_MANAGER, "auto_WD_Allocate", params);
	}
	
	//波次取消分配	
	public void cancellWaveDocAllocate(Map params){
		this.remoteCall(CANCELL_WD_ALLOCATE_INFO, CT_PA.WD_MANAGER, "cancell_WD_Allocate", params);
	}
	
	//波次部分分配	
	public void manualWaveDocAllocate(Map params){
		this.remoteCall(MANUAL_WD_ALLOCATE_INFO, CT_PA.WD_MANAGER, "manual_WD_Allocate", params);
	}
	
	@Override
	public void onFailure(String message, Map result) {
		System.out.println("读数据失败！");
	}

	@Override
	public void onSuccess(String message, Map result) {
		if (INIT_PICK_TICKETS_INFO.equals(message)) {
			this.curPickTicket= (PT_INFO)result.get(CT_PA.PT_RESULT);
			this.sendMessage(INIT_PICK_TICKETS_INFO);
		} else if (INIT_WAVE_DOC_INFO.equals(message)) {
			this.curPickTicket= (PT_INFO)result.get(CT_PA.PT_RESULT);
			this.sendMessage(INIT_WAVE_DOC_INFO);
		} else if (INIT_DETAILS_INFO.equals(message)) {
			this.pds= (List)result.get(CT_PA.PD_RESULT);
			this.sendMessage(INIT_DETAILS_INFO);
		} else if (INIT_WD_DETAILS_INFO.equals(message)) {
			this.pds= (List)result.get(CT_PA.PD_RESULT);
			this.sendMessage(INIT_WD_DETAILS_INFO);
		} else if (INIT_AVAILABLE_INFO.equals(message)) {
			this.ptas= (List)result.get(CT_PA.PTA_RESULT);
			this.sendMessage(INIT_AVAILABLE_INFO);
		} else if (INIT_WD_AVAILABLE_INFO.equals(message)) {
			this.ptas= (List)result.get(CT_PA.PTA_RESULT);
			this.sendMessage(INIT_WD_AVAILABLE_INFO);
		} else if (INIT_ALLOCATED_INFO.equals(message)) {
			this.ptad= (List)result.get(CT_PA.PTAD_RESULT);
			this.sendMessage(INIT_ALLOCATED_INFO);
		} else if (INIT_WD_ALLOCATED_INFO.equals(message)) {
			this.ptad= (List)result.get(CT_PA.PTAD_RESULT);
			this.sendMessage(INIT_WD_ALLOCATED_INFO);
		} else if (AUTO_ALLOCATE_INFO.equals(message)) {
			final Map result1 = new HashMap();
			
			result1.put("pickTicketId", curPickTicket.getId());
			reloadPT(result1);
			
			this.sendMessage(AUTO_ALLOCATE_INFO);
		} else if (AUTO_WD_ALLOCATE_INFO.equals(message)) {
			reloadWD(result);
			this.sendMessage(AUTO_WD_ALLOCATE_INFO);
		} else if (SINGLE_CANCEL_ALLOCATE_INFO.equals(message)) {
			final Map result1 = new HashMap();
			
			result1.put("pickTicketId", curPickTicket.getId());
			reloadPT(result1);
			
			this.sendMessage(SINGLE_CANCEL_ALLOCATE_INFO);
		} else if (SINGLE_CANCEL_WAVE_ALLOCATE_INFO.equals(message)) {
			reloadWD(result);
			this.sendMessage(SINGLE_CANCEL_WAVE_ALLOCATE_INFO);
		}else if (ALL_CANCEL_ALLOCATE_INFO.equals(message)) {
			final Map result1 = new HashMap();
			
			result1.put("pickTicketId", curPickTicket.getId());
			reloadPT(result1);
			
			this.sendMessage(ALL_CANCEL_ALLOCATE_INFO);
		} else if (CANCELL_WD_ALLOCATE_INFO.equals(message)) {
			reloadWD(result);
			this.sendMessage(CANCELL_WD_ALLOCATE_INFO);
		} else if (MANUAL_ALLOCATE_INFO.equals(message)) {
			final Map result1 = new HashMap();
			
			result1.put("pickTicketId", curPickTicket.getId());
			reloadPT(result1);
			
			this.sendMessage(MANUAL_ALLOCATE_INFO);
		} else if (MANUAL_WD_ALLOCATE_INFO.equals(message)) {
			final Map result1 = new HashMap();
			
			result1.put("waveDocId", curPickTicket.getId());
			reloadWD(result1);
			
			this.sendMessage(MANUAL_WD_ALLOCATE_INFO);
		}  else if (INIT_PROCESS_DOCS_INFO.equals(message)) {
			this.curPickTicket= (PT_INFO)result.get(CT_PA.PR_RESULT);
			this.sendMessage(INIT_PROCESS_DOCS_INFO);
		} else if (INIT_ALLOCATED_PROCESS_DOCS_INFO.equals(message)) {
			this.ptad= (List)result.get(CT_PA.PRAD_RESULT);
			this.sendMessage(INIT_ALLOCATED_PROCESS_DOCS_INFO);
		} else if (INIT_PROCESS_DOC_DETAILS_INFO.equals(message)) {
			this.pds= (List)result.get(CT_PA.PRD_RESULT);
			this.sendMessage(INIT_PROCESS_DOC_DETAILS_INFO);
		} else if (INIT_AVAILABLE_PROCESS_INFO.equals(message)) {
			this.ptas= (List)result.get(CT_PA.PRA_RESULT);
			this.sendMessage(INIT_AVAILABLE_PROCESS_INFO);
		} else if (AUTO_PROCESSDOC_ALLOCATE_INFO.equals(message)) {
			reloadPR(result);
			this.sendMessage(AUTO_PROCESSDOC_ALLOCATE_INFO);
		} else if (SINGLE_CANCEL_PROCESSDOC_ALLOCATE_INFO.equals(message)) {
			final Map result1 = new HashMap();
			result1.put("processDocId", curPickTicket.getId());
			reloadPR(result1);
			this.sendMessage(SINGLE_CANCEL_PROCESSDOC_ALLOCATE_INFO);
		} else if (ALL_CANCEL_PROCESSDOC_ALLOCATE_INFO.equals(message)) {
			final Map result1 = new HashMap();
			result1.put("processDocId", curPickTicket.getId());
			reloadPR(result1);
			this.sendMessage(ALL_CANCEL_PROCESSDOC_ALLOCATE_INFO);
		} else if (MANUAL_PROCESSDOC_ALLOCATE_INFO.equals(message)) {
			final Map result1 = new HashMap();
			result1.put("processDocId", curPickTicket.getId());
			reloadPR(result1);
			this.sendMessage(MANUAL_PROCESSDOC_ALLOCATE_INFO);
		} 
	}
	
	/*重新初始化发货单信息*/
	public void reloadPT(Map result) {
		initPickTicketInfo(result);
		initDetailsInfo(result);
		initAllocatedInfo(result);
	}
	
	/*重新初始化波次信息*/
	public void reloadWD(Map result) {
		initWaveDocInfo(result);
		initWaveDocDetailsInfo(result);
		initWaveDocAllocatedInfo(result);
	}
	
	/*重新初始化加工单信息*/
	public void reloadPR(Map result) {
		initProcessDocInfo(result);
		initProcessDocDetailsInfo(result);
		initAllocatedProcessDocsInfo(result);
	}

	@Override
	public boolean onTimeOutFailure(String message) {
		return false;
	}
}