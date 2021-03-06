package com.vtradex.wms.server.model.base;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.apache.commons.lang.StringUtils;

import com.vtradex.thorn.server.model.DomainModel;
import com.vtradex.thorn.server.util.DateUtil;
import com.vtradex.wms.server.model.organization.WmsItem;
import com.vtradex.wms.server.model.organization.WmsLotRule;
import com.vtradex.wms.server.model.organization.WmsOrganization;

/**
 * @category 批次属性
 * @author peng.lei
 * @version 1.0
 * @created 15-二月-2011 10:06:35
 */
public class LotInfo extends DomainModel{	
	private static final long serialVersionUID = 6808392063756175939L;
	 /**存货日期*/
    private Date storageDate;
    /**生产日期*/
    private Date productDate;
    /**保质日期*/
    private Date expireDate;
    /**近效期*/
    private Date warnDate;
    /**收货日期*/
    private Date receivedDate;
	/** 收货单号*/
	private String soi;	
	/** 供应商-入库时来源:供应商*/
	private WmsOrganization supplier;
	/** 扩展属性1-工艺状态 @TypeOfExtendPropC1*/
	private String extendPropC1;
	/** 扩展属性2*/
	private String extendPropC2;
	/** 扩展属性3*/
	private String extendPropC3;
	/** 扩展属性4*/
	private String extendPropC4;
	/** 扩展属性5*/
	private String extendPropC5;
	/** 扩展属性6*/
	private String extendPropC6;
	/** 扩展属性7*/
	private String extendPropC7;
	/** 扩展属性8*/
	private String extendPropC8;
	/** 扩展属性9*/
	private String extendPropC9;
	/** 扩展属性10*/
	private String extendPropC10;
	/** 扩展属性11*/
	private String extendPropC11;
	/** 扩展属性12*/
	private String extendPropC12;
	/** 扩展属性13*/
	private String extendPropC13;
	/** 扩展属性14*/
	private String extendPropC14;
	/** 扩展属性15*/
	private String extendPropC15;
	/** 扩展属性16*/
	private String extendPropC16;
	/** 扩展属性17*/
	private String extendPropC17;
	/** 扩展属性18*/
	private String extendPropC18;
	/** 扩展属性19*/
	private String extendPropC19;
	/** 扩展属性20*/
	private String extendPropC20;
	
	public LotInfo(){
	}
	
	public LotInfo(Date productDate, String soi, WmsOrganization supplier,
			String extendPropC1) {
		super();
		this.productDate = productDate;
		this.soi = soi;
		this.supplier = supplier;
		this.extendPropC1 = extendPropC1;
	}

	public LotInfo(Date storageDate, Date productDate, Date expireDate,
			Date warnDate, Date receivedDate, String soi,
			WmsOrganization supplier, String extendPropC1, String extendPropC2,
			String extendPropC3, String extendPropC4, String extendPropC5,
			String extendPropC6, String extendPropC7, String extendPropC8,
			String extendPropC9, String extendPropC10, String extendPropC11,
			String extendPropC12, String extendPropC13, String extendPropC14,
			String extendPropC15, String extendPropC16, String extendPropC17,
			String extendPropC18, String extendPropC19, String extendPropC20) {
		super();
		this.storageDate = storageDate;
		this.productDate = productDate;
		this.expireDate = expireDate;
		this.warnDate = warnDate;
		this.receivedDate = receivedDate;
		this.soi = soi;
		this.supplier = supplier;
		this.extendPropC1 = extendPropC1;
		this.extendPropC2 = extendPropC2;
		this.extendPropC3 = extendPropC3;
		this.extendPropC4 = extendPropC4;
		this.extendPropC5 = extendPropC5;
		this.extendPropC6 = extendPropC6;
		this.extendPropC7 = extendPropC7;
		this.extendPropC8 = extendPropC8;
		this.extendPropC9 = extendPropC9;
		this.extendPropC10 = extendPropC10;
		this.extendPropC11 = extendPropC11;
		this.extendPropC12 = extendPropC12;
		this.extendPropC13 = extendPropC13;
		this.extendPropC14 = extendPropC14;
		this.extendPropC15 = extendPropC15;
		this.extendPropC16 = extendPropC16;
		this.extendPropC17 = extendPropC17;
		this.extendPropC18 = extendPropC18;
		this.extendPropC19 = extendPropC19;
		this.extendPropC20 = extendPropC20;
	}



	public Date getStorageDate() {
		return storageDate;
	}


	public void setStorageDate(Date storageDate) {
		this.storageDate = storageDate;
	}


	public Date getProductDate() {
		return productDate;
	}


	public void setProductDate(Date productDate) {
		this.productDate = productDate;
	}


	public Date getExpireDate() {
		return expireDate;
	}


	public void setExpireDate(Date expireDate) {
		this.expireDate = expireDate;
	}


	public Date getWarnDate() {
		return warnDate;
	}


	public void setWarnDate(Date warnDate) {
		this.warnDate = warnDate;
	}


	public Date getReceivedDate() {
		return receivedDate;
	}


	public void setReceivedDate(Date receivedDate) {
		this.receivedDate = receivedDate;
	}


	public String getExtendPropC1() {
		return extendPropC1;
	}

	public void setExtendPropC1(String extendPropC1) {
		this.extendPropC1 = extendPropC1;
	}

	public String getExtendPropC10() {
		return extendPropC10;
	}

	public void setExtendPropC10(String extendPropC10) {
		this.extendPropC10 = extendPropC10;
	}

	public String getExtendPropC2() {
		return extendPropC2;
	}

	public void setExtendPropC2(String extendPropC2) {
		this.extendPropC2 = extendPropC2;
	}

	public String getExtendPropC3() {
		return extendPropC3;
	}

	public void setExtendPropC3(String extendPropC3) {
		this.extendPropC3 = extendPropC3;
	}

	public String getExtendPropC4() {
		return extendPropC4;
	}

	public void setExtendPropC4(String extendPropC4) {
		this.extendPropC4 = extendPropC4;
	}

	public String getExtendPropC5() {
		return extendPropC5;
	}

	public void setExtendPropC5(String extendPropC5) {
		this.extendPropC5 = extendPropC5;
	}

	public String getExtendPropC6() {
		return extendPropC6;
	}

	public void setExtendPropC6(String extendPropC6) {
		this.extendPropC6 = extendPropC6;
	}

	public String getExtendPropC7() {
		return extendPropC7;
	}

	public void setExtendPropC7(String extendPropC7) {
		this.extendPropC7 = extendPropC7;
	}

	public String getExtendPropC8() {
		return extendPropC8;
	}

	public void setExtendPropC8(String extendPropC8) {
		this.extendPropC8 = extendPropC8;
	}

	public String getExtendPropC9() {
		return extendPropC9;
	}

	public void setExtendPropC9(String extendPropC9) {
		this.extendPropC9 = extendPropC9;
	}

	public String getSoi() {
		return soi;
	}

	public void setSoi(String soi) {
		this.soi = soi;
	}

	public WmsOrganization getSupplier() {
		return supplier;
	}

	public void setSupplier(WmsOrganization supplier) {
		this.supplier = supplier;
	}

	public String getExtendPropC11() {
		return extendPropC11;
	}

	public void setExtendPropC11(String extendPropC11) {
		this.extendPropC11 = extendPropC11;
	}

	public String getExtendPropC12() {
		return extendPropC12;
	}

	public void setExtendPropC12(String extendPropC12) {
		this.extendPropC12 = extendPropC12;
	}

	public String getExtendPropC13() {
		return extendPropC13;
	}

	public void setExtendPropC13(String extendPropC13) {
		this.extendPropC13 = extendPropC13;
	}

	public String getExtendPropC14() {
		return extendPropC14;
	}

	public void setExtendPropC14(String extendPropC14) {
		this.extendPropC14 = extendPropC14;
	}

	public String getExtendPropC15() {
		return extendPropC15;
	}

	public void setExtendPropC15(String extendPropC15) {
		this.extendPropC15 = extendPropC15;
	}

	public String getExtendPropC16() {
		return extendPropC16;
	}

	public void setExtendPropC16(String extendPropC16) {
		this.extendPropC16 = extendPropC16;
	}

	public String getExtendPropC17() {
		return extendPropC17;
	}

	public void setExtendPropC17(String extendPropC17) {
		this.extendPropC17 = extendPropC17;
	}

	public String getExtendPropC18() {
		return extendPropC18;
	}

	public void setExtendPropC18(String extendPropC18) {
		this.extendPropC18 = extendPropC18;
	}

	public String getExtendPropC19() {
		return extendPropC19;
	}

	public void setExtendPropC19(String extendPropC19) {
		this.extendPropC19 = extendPropC19;
	}

	public String getExtendPropC20() {
		return extendPropC20;
	}

	public void setExtendPropC20(String extendPropC20) {
		this.extendPropC20 = extendPropC20;
	}

	/**
	 * 格式化批次属性
	 */
	public void format() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		if ("".equals(this.soi)) {
			this.setSoi(null);
		}
		if ("".equals(this.extendPropC1)) {
			this.setExtendPropC1(null);
		}
		if ("".equals(this.extendPropC2)) {
			this.setExtendPropC2(null);
		}
		if ("".equals(this.extendPropC3)) {
			this.setExtendPropC3(null);
		}
		if ("".equals(this.extendPropC4)) {
			this.setExtendPropC4(null);
		}
		if ("".equals(this.extendPropC5)) {
			this.setExtendPropC5(null);
		}
		if ("".equals(this.extendPropC6)) {
			this.setExtendPropC6(null);
		}
		if ("".equals(this.extendPropC7)) {
			this.setExtendPropC7(null);
		}
		if ("".equals(this.extendPropC8)) {
			this.setExtendPropC8(null);
		}
		if ("".equals(this.extendPropC9)) {
			this.setExtendPropC9(null);
		}
		if ("".equals(this.extendPropC10)) {
			this.setExtendPropC10(null);
		}
		if ("".equals(this.extendPropC11)) {
			this.setExtendPropC11(null);
		}
		if ("".equals(this.extendPropC12)) {
			this.setExtendPropC12(null);
		}
		if ("".equals(this.extendPropC13)) {
			this.setExtendPropC13(null);
		}
		if ("".equals(this.extendPropC14)) {
			this.setExtendPropC14(null);
		}
		if ("".equals(this.extendPropC15)) {
			this.setExtendPropC15(null);
		}
		if ("".equals(this.extendPropC16)) {
			this.setExtendPropC16(null);
		}
		if ("".equals(this.extendPropC17)) {
			this.setExtendPropC17(null);
		}
		if ("".equals(this.extendPropC18)) {
			this.setExtendPropC18(null);
		}
		if ("".equals(this.extendPropC19)) {
			this.setExtendPropC19(null);
		}
		if ("".equals(this.extendPropC20)) {
			this.setExtendPropC20(null);
		}
		
		try {
			this.setStorageDate(this.getStorageDate() == null ? null : sdf.parse(sdf.format(this.getStorageDate())));
			this.setProductDate(this.getProductDate() == null ? null : sdf.parse(sdf.format(this.getProductDate())));
			this.setExpireDate(this.getExpireDate() == null ? null : sdf.parse(sdf.format(this.getExpireDate())));
			this.setWarnDate(this.getWarnDate() == null ? null : sdf.parse(sdf.format(this.getWarnDate())));
		} catch (ParseException e) {
			e.printStackTrace();
		}
	}

	public void prepare(WmsLotRule lotRule, WmsItem item, String soi) {
		if (lotRule.getTrackSOI()) {
			this.setSoi(soi);
		}
		
		if (lotRule.getTrackStorageDate()) {//如果是批次赋值,则新批次部分属性和旧批次应该一致
			this.setStorageDate(this.storageDate==null?new Date():this.storageDate);
		}
		
		if(this.getSupplier() != null && this.getSupplier().getId() == null){
			this.setSupplier(null);
		}
		
		// 根据生产日期或者失效日期算出近效期
		if(item.getAlertLeadingDays()>0 && item.getValidPeriod()>0) {
			// 生产日期
			Date produceDate = null;
			//失效日期*/
			Date expireDate = null;
			// 近效期*/
			Date warnDate = null;
							
			if(this.getProductDate() != null && lotRule.getTrackProduceDate()) {
						produceDate = this.getProductDate();
						expireDate = DateUtil.increaseDate(produceDate,"DAY", item.getValidPeriod());				
						warnDate = DateUtil.increaseDate(expireDate,"DAY", item.getAlertLeadingDays()*(-1));	
						this.setExpireDate(expireDate);
						this.setWarnDate(warnDate);
						this.setStorageDate(this.getProductDate());
			}
		}
		format();
	}
	
	/**
	 * 批次属性列拼接
	 * @return
	 */
	public Object[] getArraysOfColumns() {
	    
		Object[] objs = {storageDate,productDate,expireDate,warnDate,receivedDate,this.soi,  (this.supplier == null ? null : this.supplier.getId()), 
				this.extendPropC1, this.extendPropC2, this.extendPropC3, this.extendPropC4, this.extendPropC5, 
				this.extendPropC6, this.extendPropC7, this.extendPropC8, this.extendPropC9, this.extendPropC10,
				this.extendPropC11, this.extendPropC12, this.extendPropC13, this.extendPropC14, this.extendPropC15, 
				this.extendPropC16, this.extendPropC17, this.extendPropC18, this.extendPropC19, this.extendPropC20};
		
		return objs;
	}
	
	/**
	 * 将lotInfo转换为String
	 * @return
	 */
	public String stringValue() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String result = "";

		result += (StringUtils.isEmpty(this.soi) ? "#|" : "#" + this.soi);
		result += (this.supplier == null ? "#|" : "#" + this.supplier.getId());
	    result += (storageDate == null ? "#|" : "#" + sdf.format(storageDate));
	    result += (productDate == null ? "#|" : "#" + sdf.format(productDate));
	    result += (expireDate == null ? "#|" : "#" + sdf.format(expireDate));
	    result += (warnDate == null ? "#|" : "#" + sdf.format(warnDate));
	    result += (receivedDate == null ? "#|" : "#" + sdf.format(receivedDate));
	    
	    
		result += (StringUtils.isEmpty(this.extendPropC1) ? "#|" : "#" + this.extendPropC1);
		result += (StringUtils.isEmpty(this.extendPropC2) ? "#|" : "#" + this.extendPropC2);
		result += (StringUtils.isEmpty(this.extendPropC3) ? "#|" : "#" + this.extendPropC3);
		result += (StringUtils.isEmpty(this.extendPropC4) ? "#|" : "#" + this.extendPropC4);
		result += (StringUtils.isEmpty(this.extendPropC5) ? "#|" : "#" + this.extendPropC5);
		result += (StringUtils.isEmpty(this.extendPropC6) ? "#|" : "#" + this.extendPropC6);
		result += (StringUtils.isEmpty(this.extendPropC7) ? "#|" : "#" + this.extendPropC7);
		result += (StringUtils.isEmpty(this.extendPropC8) ? "#|" : "#" + this.extendPropC8);
		result += (StringUtils.isEmpty(this.extendPropC9) ? "#|" : "#" + this.extendPropC9);
		result += (StringUtils.isEmpty(this.extendPropC10) ? "#|" : "#" + this.extendPropC10);
		result += (StringUtils.isEmpty(this.extendPropC11) ? "#|" : "#" + this.extendPropC11);
		result += (StringUtils.isEmpty(this.extendPropC12) ? "#|" : "#" + this.extendPropC12);
		result += (StringUtils.isEmpty(this.extendPropC13) ? "#|" : "#" + this.extendPropC13);
		result += (StringUtils.isEmpty(this.extendPropC14) ? "#|" : "#" + this.extendPropC14);
		result += (StringUtils.isEmpty(this.extendPropC15) ? "#|" : "#" + this.extendPropC15);
		result += (StringUtils.isEmpty(this.extendPropC16) ? "#|" : "#" + this.extendPropC16);
		result += (StringUtils.isEmpty(this.extendPropC17) ? "#|" : "#" + this.extendPropC17);
		result += (StringUtils.isEmpty(this.extendPropC18) ? "#|" : "#" + this.extendPropC18);
		result += (StringUtils.isEmpty(this.extendPropC19) ? "#|" : "#" + this.extendPropC19);
		result += (StringUtils.isEmpty(this.extendPropC20) ? "#|" : "#" + this.extendPropC20);
		
		result = StringUtils.replaceOnce(result,"#","");
		
		return result;
	}
	/**当客户不关注生产日期时,系统默认给值  yc.min 20150404*/
	public void isEmptyProductDate(Date newDate){
		if(this.productDate == null){
			this.productDate = newDate;
		}
	}
	/**
	 * 当需要跟踪生产日期且生产日期不为空的情况下自动计算保质期和近效期
	 */
}