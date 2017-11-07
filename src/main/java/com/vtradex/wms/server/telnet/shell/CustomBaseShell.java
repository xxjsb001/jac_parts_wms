package com.vtradex.wms.server.telnet.shell;

import java.io.IOException;

import org.apache.commons.lang.StringUtils;

import com.vtradex.kangaroo.shell.BreakException;
import com.vtradex.kangaroo.shell.ContinueException;
import com.vtradex.kangaroo.shell.Thorn4BaseShell;
import com.vtradex.wms.server.model.base.LotInfo;
import com.vtradex.wms.server.model.base.ShipLotInfo;
import com.vtradex.wms.server.model.organization.WmsLotRule;
import com.vtradex.wms.server.model.organization.WmsOrganization;
import com.vtradex.wms.server.telnet.base.WmsBaseModelRFManager;
import com.vtradex.wms.server.utils.DateUtil;

/**
 * 含批次Complex UI
 *
 * @category Custom Shell
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $Date: 2015/03/25 02:48:55 $
 */
public abstract class CustomBaseShell extends Thorn4BaseShell {
	
	protected WmsBaseModelRFManager wmsBaseModeRFManager;
	
	public CustomBaseShell(WmsBaseModelRFManager wmsBaseModeRFManager) {
		this.wmsBaseModeRFManager = wmsBaseModeRFManager;
	}

	protected void getComplexByLotInfo(WmsLotRule lotRule, LotInfo lotInfo) throws IOException, BreakException, ContinueException {
		Boolean beShow = Boolean.FALSE;
		if (lotRule.getTrackSupplier()) {
			if (lotInfo.getSupplier() == null) {
				beShow = Boolean.TRUE;
				String value = this.getTextField("lotRule.supplier");
				if (StringUtils.isNotEmpty(value)) {
					WmsOrganization supplier = wmsBaseModeRFManager.getSupplier(value);
					if (supplier == null) {
						this.setStatusMessage("供应商不存在.");
					}
					lotInfo.setSupplier(supplier);
				}
			}
		}
		if (lotRule.getTrackProduceDate()) {
			if (beShow || lotInfo.getProductDate() == null) {
				beShow = Boolean.TRUE;
				String value = this.getTextField("productDate");
				if (StringUtils.isEmpty(value)) {
					this.setStatusMessage("生产日期必填.");
				}
				if (!DateUtil.verifyDateFormat(value, DateUtil.ymdPattern3)) {
					this.setStatusMessage("生产日期格式错误, 正确格式：19900101");
				}
				lotInfo.setProductDate(DateUtil.getDate(value, "yyyyMMdd"));
			}
		}
		if (lotRule.getTrackExtendPropC1()) {
			if (beShow || StringUtils.isEmpty(lotInfo.getExtendPropC1())) {
				beShow = Boolean.TRUE;
				String value = this.getTextField("lotInfo.extendPropC1");
				lotInfo.setExtendPropC1(value);
			}
		}
		if (lotRule.getTrackExtendPropC2()) {
			if (beShow || StringUtils.isEmpty(lotInfo.getExtendPropC2())) {
				beShow = Boolean.TRUE;
				String value = this.getTextField("lotInfo.extendPropC2");
				lotInfo.setExtendPropC2(value);
			}
		}
		if (lotRule.getTrackExtendPropC3()) {
			if (beShow || StringUtils.isEmpty(lotInfo.getExtendPropC3())) {
				beShow = Boolean.TRUE;
				String value = this.getTextField("lotInfo.extendPropC3");
				lotInfo.setExtendPropC3(value);
			}
		}
		if (lotRule.getTrackExtendPropC4()) {
			if (beShow || StringUtils.isEmpty(lotInfo.getExtendPropC4())) {
				beShow = Boolean.TRUE;
				String value = this.getTextField("lotInfo.extendPropC4");
				lotInfo.setExtendPropC4(value);
			}
		}
		if (lotRule.getTrackExtendPropC5()) {
			if (beShow || StringUtils.isEmpty(lotInfo.getExtendPropC5())) {
				beShow = Boolean.TRUE;
				String value = this.getTextField("lotInfo.extendPropC5");
				lotInfo.setExtendPropC5(value);
			}
		}
		if (lotRule.getTrackExtendPropC6()) {
			if (beShow || StringUtils.isEmpty(lotInfo.getExtendPropC6())) {
				beShow = Boolean.TRUE;
				String value = this.getTextField("lotInfo.extendPropC6");
				lotInfo.setExtendPropC6(value);
			}
		}
		if (lotRule.getTrackExtendPropC7()) {
			if (beShow || StringUtils.isEmpty(lotInfo.getExtendPropC7())) {
				beShow = Boolean.TRUE;
				String value = this.getTextField("lotInfo.extendPropC7");
				lotInfo.setExtendPropC7(value);
			}
		}
		if (lotRule.getTrackExtendPropC8()) {
			if (beShow || StringUtils.isEmpty(lotInfo.getExtendPropC8())) {
				beShow = Boolean.TRUE;
				String value = this.getTextField("lotInfo.extendPropC8");
				lotInfo.setExtendPropC8(value);
			}
		}
		if (lotRule.getTrackExtendPropC9()) {
			if (beShow || StringUtils.isEmpty(lotInfo.getExtendPropC9())) {
				beShow = Boolean.TRUE;
				String value = this.getTextField("lotInfo.extendPropC9");
				lotInfo.setExtendPropC9(value);
			}
		}
		if (lotRule.getTrackExtendPropC10()) {
			if (beShow || StringUtils.isEmpty(lotInfo.getExtendPropC10())) {
				beShow = Boolean.TRUE;
				String value = this.getTextField("lotInfo.extendPropC10");
				lotInfo.setExtendPropC10(value);
			}
		}
		if (lotRule.getTrackExtendPropC11()) {
			if (beShow || StringUtils.isEmpty(lotInfo.getExtendPropC11())) {
				beShow = Boolean.TRUE;
				String value = this.getTextField("lotInfo.extendPropC11");
				lotInfo.setExtendPropC11(value);
			}
		}
		if (lotRule.getTrackExtendPropC12()) {
			if (beShow || StringUtils.isEmpty(lotInfo.getExtendPropC12())) {
				beShow = Boolean.TRUE;
				String value = this.getTextField("lotInfo.extendPropC12");
				lotInfo.setExtendPropC12(value);
			}
		}
		if (lotRule.getTrackExtendPropC13()) {
			if (beShow || StringUtils.isEmpty(lotInfo.getExtendPropC13())) {
				beShow = Boolean.TRUE;
				String value = this.getTextField("lotInfo.extendPropC13", lotInfo.getExtendPropC13());
				lotInfo.setExtendPropC13(value);
			}
		}
		if (lotRule.getTrackExtendPropC14()) {
			if (beShow || StringUtils.isEmpty(lotInfo.getExtendPropC14())) {
				beShow = Boolean.TRUE;
				String value = this.getTextField("lotInfo.extendPropC14");
				lotInfo.setExtendPropC14(value);
			}
		}
		if (lotRule.getTrackExtendPropC15()) {
			if (beShow || StringUtils.isEmpty(lotInfo.getExtendPropC15())) {
				beShow = Boolean.TRUE;
				String value = this.getTextField("lotInfo.extendPropC15");
				lotInfo.setExtendPropC15(value);
			}
		}
		if (lotRule.getTrackExtendPropC16()) {
			if (beShow || StringUtils.isEmpty(lotInfo.getExtendPropC16())) {
				beShow = Boolean.TRUE;
				String value = this.getTextField("lotInfo.extendPropC16");
				lotInfo.setExtendPropC16(value);
			}
		}
		if (lotRule.getTrackExtendPropC17()) {
			if (beShow || StringUtils.isEmpty(lotInfo.getExtendPropC17())) {
				beShow = Boolean.TRUE;
				String value = this.getTextField("lotInfo.extendPropC17");
				lotInfo.setExtendPropC17(value);
			}
		}
		if (lotRule.getTrackExtendPropC18()) {
			if (beShow || StringUtils.isEmpty(lotInfo.getExtendPropC18())) {
				beShow = Boolean.TRUE;
				String value = this.getTextField("lotInfo.extendPropC18");
				lotInfo.setExtendPropC18(value);
			}
		}
		if (lotRule.getTrackExtendPropC19()) {
			if (beShow || StringUtils.isEmpty(lotInfo.getExtendPropC19())) {
				beShow = Boolean.TRUE;
				String value = this.getTextField("lotInfo.extendPropC19");
				lotInfo.setExtendPropC19(value);
			}
		}
		if (lotRule.getTrackExtendPropC20()) {
			if (beShow || StringUtils.isEmpty(lotInfo.getExtendPropC20())) {
				beShow = Boolean.TRUE;
				String value = this.getTextField("lotInfo.extendPropC20");
				lotInfo.setExtendPropC20(value);
			}
		}
	}
	
	protected ShipLotInfo getComplexByShipLotInfo(WmsLotRule lotRule, ShipLotInfo lotInfo) throws IOException, BreakException, ContinueException {
		if (lotRule.getTrackSupplier()) {
			String value = this.getTextField("lotRule.supplier");
			if (StringUtils.isNotEmpty(value)) {
				WmsOrganization supplier = wmsBaseModeRFManager.getSupplier(value);
				if (supplier == null) {
					this.setStatusMessage("供应商不存在.");
				}
				lotInfo.setSupplier(supplier.getName());
			}
		} else if (lotRule.getTrackExtendPropC1()) {
			String value = this.getTextField("lotInfo.extendPropC1", lotInfo.getExtendPropC1());
			lotInfo.setExtendPropC1(value);
		} else if (lotRule.getTrackExtendPropC2()) {
			String value = this.getTextField("lotInfo.extendPropC2", lotInfo.getExtendPropC2());
			lotInfo.setExtendPropC2(value);
		} else if (lotRule.getTrackExtendPropC3()) {
			String value = this.getTextField("lotInfo.extendPropC3", lotInfo.getExtendPropC3());
			lotInfo.setExtendPropC3(value);
		} else if (lotRule.getTrackExtendPropC4()) {
			String value = this.getTextField("lotInfo.extendPropC4", lotInfo.getExtendPropC4());
			lotInfo.setExtendPropC4(value);
		} else if (lotRule.getTrackExtendPropC5()) {
			String value = this.getTextField("lotInfo.extendPropC5", lotInfo.getExtendPropC5());
			lotInfo.setExtendPropC5(value);
		} else if (lotRule.getTrackExtendPropC6()) {
			String value = this.getTextField("lotInfo.extendPropC6", lotInfo.getExtendPropC6());
			lotInfo.setExtendPropC6(value);
		} else if (lotRule.getTrackExtendPropC7()) {
			String value = this.getTextField("lotInfo.extendPropC7", lotInfo.getExtendPropC7());
			lotInfo.setExtendPropC7(value);
		} else if (lotRule.getTrackExtendPropC8()) {
			String value = this.getTextField("lotInfo.extendPropC8", lotInfo.getExtendPropC8());
			lotInfo.setExtendPropC8(value);
		} else if (lotRule.getTrackExtendPropC9()) {
			String value = this.getTextField("lotInfo.extendPropC9", lotInfo.getExtendPropC9());
			lotInfo.setExtendPropC9(value);
		} else if (lotRule.getTrackExtendPropC10()) {
			String value = this.getTextField("lotInfo.extendPropC10", lotInfo.getExtendPropC10());
			lotInfo.setExtendPropC10(value);
		} else if (lotRule.getTrackExtendPropC11()) {
			String value = this.getTextField("lotInfo.extendPropC11", lotInfo.getExtendPropC11());
			lotInfo.setExtendPropC11(value);
		} else if (lotRule.getTrackExtendPropC12()) {
			String value = this.getTextField("lotInfo.extendPropC12", lotInfo.getExtendPropC12());
			lotInfo.setExtendPropC12(value);
		} else if (lotRule.getTrackExtendPropC13()) {
			String value = this.getTextField("lotInfo.extendPropC13", lotInfo.getExtendPropC13());
			lotInfo.setExtendPropC13(value);
		} else if (lotRule.getTrackExtendPropC14()) {
			String value = this.getTextField("lotInfo.extendPropC14", lotInfo.getExtendPropC14());
			lotInfo.setExtendPropC14(value);
		} else if (lotRule.getTrackExtendPropC15()) {
			String value = this.getTextField("lotInfo.extendPropC15", lotInfo.getExtendPropC15());
			lotInfo.setExtendPropC15(value);
		} else if (lotRule.getTrackExtendPropC16()) {
			String value = this.getTextField("lotInfo.extendPropC16", lotInfo.getExtendPropC16());
			lotInfo.setExtendPropC16(value);
		} else if (lotRule.getTrackExtendPropC17()) {
			String value = this.getTextField("lotInfo.extendPropC17", lotInfo.getExtendPropC17());
			lotInfo.setExtendPropC17(value);
		} else if (lotRule.getTrackExtendPropC18()) {
			String value = this.getTextField("lotInfo.extendPropC18", lotInfo.getExtendPropC18());
			lotInfo.setExtendPropC18(value);
		} else if (lotRule.getTrackExtendPropC19()) {
			String value = this.getTextField("lotInfo.extendPropC19", lotInfo.getExtendPropC19());
			lotInfo.setExtendPropC19(value);
		} else if (lotRule.getTrackExtendPropC20()) {
			String value = this.getTextField("lotInfo.extendPropC20", lotInfo.getExtendPropC20());
			lotInfo.setExtendPropC20(value);
		}
		return lotInfo;
	}
}
