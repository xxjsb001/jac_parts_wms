package com.vtradex.wms.server.telnet.shell;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import net.mlw.vlh.ValueList;
import net.mlw.vlh.ValueListInfo;
import net.wimpi.telnetd.net.Connection;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.springframework.util.Assert;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.ShellFactory;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.exception.ThornServerRuntimeException;
import com.vtradex.wms.server.utils.MyUtils;

public abstract class JacPageableBaseShell extends Thorn4BaseShell{

	private List<Object[]> list = new ArrayList<Object[]>();
	private int[] colWidths;
	private int totalEntry;
	private int totalPage;
	private int pagingNumberPer = 9;
	private int currentPage = 1;
	
	/**
	 * eg. Object[] rowDatas = (Object[])this.get(ROW_DATA_KEY);
	 */
	public static final String ROW_DATA_KEY = "rowData";
	
	public static final String UP = "01";
	public static final String DOWN = "02";
	
	@Override
	protected void mainProcess(Connection connection) throws BreakException,
			ContinueException, IOException {
		String[] headers = getTableHeader();
		if(headers == null){
			setStatusMessage("分页Shell表头不能为空");
		}
		outputHeader(headers);
		queryByValueList(getHql(),getParams());
		int i = 1;
		for(Object[] values : list){
			Object[] objs = new Object[values.length];
			objs[0] = i++;
			System.arraycopy(values, 1, objs, 1, values.length - 1);
			output(objs);
		}
		
		int center = getTerminalIO(connection).getColumns() - 10;
		output(StringUtils.repeat("-", center));
		output(UP+":上一页     "+DOWN+":下一页             " + currentPage + "/" + totalPage + "  总数:" + totalEntry);
		String choose = getTextField("choose");
		if(MyUtils.OVER.equals(choose)){
			forward(ShellFactory.getMainShell());  
		}else if(MyUtils.ZERO.equals(choose)){
			forward(getUpShell());
		}
		if(choose.equalsIgnoreCase(UP)){
			if(currentPage == 1){
				setStatusMessage("已经是第一页！");
			}
			currentPage--;
			refresh();
		}
		else if(choose.equalsIgnoreCase(DOWN)){
			if(currentPage == totalPage){
				setStatusMessage("已经最后一页！");
			}
			currentPage++;
			refresh();
		}
		else{
			chooseChange(choose);
		}
	}
	
	protected void chooseChange(String choose) throws BreakException, ContinueException{
		if(NumberUtils.isDigits(choose)){
			int number = Integer.valueOf(choose);
			if(number >= 1 && number <= list.size()){
				Object[] value = list.get(number - 1);
				put("rowData",value);
				forward(getNextShell());
			}
			else{
				setStatusMessage("选择条件不符合要求，请重新输入！");
			}
		}
		else{
			setStatusMessage("选择条件不符合要求，请重新输入！");
		}
	}
	
	private void output(Object[] values) throws IOException{
		StringBuffer buf = new StringBuffer();
		int i = 0;
		for(Object value : values){
			int colWidth = colWidths == null ? 10 : colWidths[i++];
			if(value == null){
				value = "-";
			}
			StringBuffer colBuf = new StringBuffer();
			int length = value.toString().length();
			char[] chars = value.toString().toCharArray();
			for(char c : chars){
				if(isChineseCharacter(c)){
					colWidth--;
				}
			}
			if(length > colWidth){
				//value = StringUtils.substring(value.toString(), 0, colWidth - 2) + "..";
				colBuf.append(value.toString());
			}
			else{
				int left = ((colWidth - length != 0) ? ((colWidth - length) / 2) : (0));
	            int right = colWidth - length - left;
	            appendSpaceString(colBuf, left);
	            colBuf.append(value.toString());
	            appendSpaceString(colBuf, right);
			}
            colBuf.append(" |");
            buf.append(colBuf);
		}
		output(buf.toString());
	}
	
	private void outputHeader(String[] values) throws IOException{
		StringBuffer buf = new StringBuffer();
		int i = 0;
		colWidths = new int[values.length];
		for(String value : values){
			int length = value.length();
			StringBuffer colBuf = new StringBuffer();
			int left = ((10 - length != 0) ? ((10 - length) / 2) : (0));
			int right = 10 - length - left;
			if (i == 0) {
				left = 0;
				right = 0;
			}
			if(i > 0){
				appendSpaceString(colBuf, left);
			}
			colBuf.append(value);
			appendSpaceString(colBuf, right);
            colBuf.append("|");
            colWidths[i++] = colBuf.toString().length();
            buf.append(colBuf);
		}
		output(buf.toString());
	}
	
	/**
	 * 是否中文
	 * @param c
	 * @return
	 */
	protected  boolean  isChineseCharacter(char   c)   { 
	    return   (19968   <=   (int)c)   &&   ((int)c   <=   171941); 
	}
	
	private void appendSpaceString(StringBuffer sbuf, int length) {
        for (int i = 0; i < length; i++) {
            sbuf.append(" ");
        }
    }
	
	@SuppressWarnings("unchecked")
	private void queryByValueList(String hql , Map<String, Object> params){
		params.put("pagingPage","" + currentPage);
		params.put(ValueListInfo.PAGING_NUMBER_PER , "" + pagingNumberPer);
//		ValueList valueList = valueListQueryManager.queryByValueList(removeHqlWithNullParam(hql,params) , params);
		ValueList valueList = null;
		try {
			valueList = invoke("valueListQueryManager", "queryByValueList", removeHqlWithNullParam(hql,params) , params);
		} catch (BusinessException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		Assert.notNull(valueList,"valueList is null by hql ["+hql+"]");
		if(valueList == null){
			throw new ThornServerRuntimeException("Cann't query datas from DataBase! ");
		}
		this.totalEntry = valueList.getValueListInfo().getTotalNumberOfEntries();
		this.totalPage = valueList.getValueListInfo().getTotalNumberOfPages();
		list = valueList.getList();
	}
	
	@SuppressWarnings("rawtypes")
	private String removeHqlWithNullParam(String hql,Map params){
		String _hql = StringUtils.substringBetween(hql,"/~","~/");
		while(StringUtils.isNotEmpty(_hql)){
			String param = StringUtils.substring(_hql,0,_hql.indexOf(":")).trim();
			Object o = params.get(param);
			if(o == null || "".equals(o)){
				params.remove(param);
				hql = StringUtils.remove(hql,"/~"+_hql+"~/");
			}else if(o instanceof Collection){
				if(((Collection)o).isEmpty() || ((Collection)o).size() == 0){
					params.remove(param);
					hql = StringUtils.remove(hql,"/~"+_hql+"~/");
				}
			}
			hql = StringUtils.replace(hql,"/~"+_hql+"~/",StringUtils.substring(_hql,_hql.indexOf(":")+1));
			_hql = StringUtils.substringBetween(hql,"/~","~/");
		}
		return hql;
	}
	
	@SuppressWarnings("unchecked")
	private <T extends Object> T invoke(String beanId, String methodName,
			Object... objects) throws BusinessException, Exception {
		try {
			Object bean = getBean(beanId);
			Method method = getExactlyMethod(bean, methodName, objects);
			return (T) method.invoke(bean, objects);
		} catch (BusinessException e) {
			throw e;
		} catch (Exception e) {
			processException(e);
		}
		return null;

	}
	
	
	abstract public String[] getTableHeader();
	abstract public String getHql();
	abstract public String getNextShell();
	abstract public String getUpShell();
	abstract public Map<String, Object> getParams();
	
	
}
