package com.vtradex.wms.server.service.rule.pojo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.vtradex.engine.exception.EcadBaseException;
import com.vtradex.rule.server.loader.IRuleTableLoader;
import com.vtradex.rule.server.model.rule.RuleTable;
import com.vtradex.rule.server.model.rule.Version;
import com.vtradex.rule.server.service.rule.RuleManager;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.thorn.server.util.BeanUtils;
import com.vtradex.thorn.server.util.Constant;
import com.vtradex.wms.server.service.rule.WmsRuleManager;

/**
 * @author: 李炎
 */
public class DefaultWmsRuleManager extends DefaultBaseManager implements WmsRuleManager{

	protected RuleManager ruleMananger;
	protected IRuleTableLoader ruleTableLoader;

	public DefaultWmsRuleManager(RuleManager ruleMananger,IRuleTableLoader ruleTableLoader){
		this.ruleMananger = ruleMananger;
		this.ruleTableLoader = ruleTableLoader;
	}

	
	public Map<String, Object> executeRule(String warehouseName, String directoryName,String ruleName,
			Map<String, Object> problem){
		String[] binds = new String[]{
				warehouseName,
				Constant.NULL,
				Constant.NULL,
				Constant.NULL,
				Constant.NULL
		};
		try {
			return ruleMananger.execute(directoryName, ruleName, problem, binds);
		} catch (EcadBaseException e) {
			e.printStackTrace();
			ruleMananger.storeRuleExceptionLog(null, e);
			throw new BusinessException(e.getMess());
		}
	}
	
	
	@SuppressWarnings("unchecked")
	public String getexecuteMainRule(String warehouseName, String orgName, String referenceRule){
		Date invalidDate = new Date();
		String[] binds = new String[]{
				warehouseName,
				Constant.NULL,
				Constant.NULL,
				Constant.NULL,
				Constant.NULL
		};
		
		Object[] objects = new Object[]{"业务规则定义", orgName};
		Map<String,Object> ruleRefs = (Map<String, Object>)ruleTableLoader.getRuleTableDetail(invalidDate, binds, objects);
		if(ruleRefs == null){
			throw new BusinessException(orgName + "在业务规则定义表中未找到数据！");
		}
		
		String[] nullbinds = new String[]{
				Constant.NULL,
				Constant.NULL,
				Constant.NULL,
				Constant.NULL,
				Constant.NULL
		};
		String ruleNo = (String)ruleRefs.get("行业模板");
		objects = new Object[]{"行业模板库", ruleNo, referenceRule};
		Map<String,Object> values = (Map<String,Object>) ruleTableLoader.getRuleTableDetail(invalidDate, nullbinds, objects);
		if(values == null){
			throw new BusinessException(orgName + "对应" + ruleNo + "在行业模板库中未找到数据！");
		}
		String mainRule = (String)values.get("主规则");
		return mainRule;
	}
	
	@SuppressWarnings("unchecked")
	public  List<Map<String,Object>> getExecuteRuleTable(String warehouseName,String orgName,String referenceRule,Object... objs){
		String ruleTable = getexecuteMainRule(warehouseName, orgName, referenceRule);
		
		Date invalidDate = new Date();
		String[] binds = new String[]{
				warehouseName,
				Constant.NULL,
				Constant.NULL,
				Constant.NULL,
				Constant.NULL
		};
		List list = new ArrayList();
		list.add(ruleTable);
		for (int i=0; i<objs.length; i++) {
			list.add(objs[i]);
		}
		//Object[] objects = new Object[]{ruleTable, objs};
		List<Map<String,Object>> ruleRefs = (List<Map<String,Object>>)ruleTableLoader.getMultipleRuleTableDetail(invalidDate, binds, list.toArray());
		if(ruleRefs == null || ruleRefs.isEmpty()){
			throw new BusinessException(orgName + "在[" + ruleTable +  "]中未找到数据！");
		}
		return ruleRefs;
	}
	
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getMultipleRuleTableDetail(String warehouseName,Object... objects){
		Date invalidDate = new Date();
		String[] binds = new String[]{
				warehouseName,
				Constant.NULL,
				Constant.NULL,
				Constant.NULL,
				Constant.NULL
		};
		return (List<Map<String,Object>>)ruleTableLoader.getMultipleRuleTableDetail(invalidDate, binds, objects);
	}
	
	@SuppressWarnings("unchecked")
	public Map<String,Object> getRuleTableDetail(String warehouseName,Object... objects){
		Date invalidDate = new Date();
		String[] binds = new String[]{
				warehouseName,
				Constant.NULL,
				Constant.NULL,
				Constant.NULL,
				Constant.NULL
		};
		return (Map<String,Object>)ruleTableLoader.getRuleTableDetail(invalidDate, binds, objects);
	}
	
	public Object getSingleRuleTableDetail(String warehouseName,Object... objects){
		Date invalidDate = new Date();
		String[] binds = new String[]{
				warehouseName,
				Constant.NULL,
				Constant.NULL,
				Constant.NULL,
				Constant.NULL
		};
		return ruleTableLoader.getSingleRuleTableDetail(invalidDate, binds, objects);
	}
	
	@SuppressWarnings("unchecked")
	public List<Map<String,Object>> getAllRuleTableDetail(String warehouseName,Object... objects){
		Date invalidDate = new Date();
		String[] binds = new String[]{
				warehouseName,
				Constant.NULL,
				Constant.NULL,
				Constant.NULL,
				Constant.NULL
		};
		return (List<Map<String,Object>>)ruleTableLoader.getAllRuleTableDetail(invalidDate, binds, objects);
	}
	
	
	public Map<String, Object> execute(String warehouseName, String orgName,
			String referenceRule, Map<String, Object> problem) {
		String mainRule = getexecuteMainRule(warehouseName, orgName, referenceRule);
		return executeRule(warehouseName, referenceRule,mainRule,problem);
	}

	public void storeRuleExceptionLog(String gourpNo, String process,
			EcadBaseException ecadBaseException) {
		ruleMananger.storeRuleExceptionLog(gourpNo, process, ecadBaseException);
	}

	public void storeRuleExceptionLog(String process,
			EcadBaseException ecadBaseException) {
		ruleMananger.storeRuleExceptionLog(process, ecadBaseException);
	}
	
	@SuppressWarnings("unchecked")
	public void changeVersionHashCode(){
		String[] binds = new String[]{
				"发动机仓库",
				Constant.NULL,
				Constant.NULL,
				Constant.NULL,
				Constant.NULL
		};
		String hashCode = null,checkHashCode=null;
		List<Version> versions = commonDao.findByQuery("FROM Version v WHERE v.bind1 ='北京1号仓库'");
		for(Version version : versions){
			RuleTable ruleTable = version.getRuleTable();
			version.setBind1(binds[0]);
			hashCode = BeanUtils.getFormat(ruleTable.getName(),version.getName(),
					version.getBind1(),version.getBind2(),version.getBind3(),version.getBind4(),version.getBind5());
			checkHashCode = BeanUtils.getFormat(ruleTable.getName(),
					version.getBind1(),version.getBind2(),version.getBind3(),version.getBind4(),version.getBind5());
			version.setHashCode(hashCode);
			version.setCheckHashCode(checkHashCode);
			commonDao.store(version);
			System.out.println(version.getId()+":"+hashCode+","+checkHashCode);
		}
	}
}
