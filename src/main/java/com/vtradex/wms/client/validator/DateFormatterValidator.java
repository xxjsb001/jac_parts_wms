package com.vtradex.wms.client.validator;

import com.vtradex.thorn.client.ui.InputUI;
import com.vtradex.thorn.client.validate.AbstractValidator;

public class DateFormatterValidator extends AbstractValidator {

	@Override
	public String validate(InputUI inputUI) {
		if(inputUI.isEmpty()){
			return null;
		}
		if(!dateRange(inputUI.getNullSafeString())){
			return this.getHintMessage(inputUI);
		}
		return null;
	}
	
	public boolean dateRange(String dateRange){
		boolean result = false;
		String startDate = dateRange.substring(0,dateRange.indexOf("-"));
		String endDate = dateRange.substring(dateRange.indexOf("-")+1);
		if(startDate.length() == 8 && endDate.length() == 8){
			result=true;
		}
		return result;
	}
}
