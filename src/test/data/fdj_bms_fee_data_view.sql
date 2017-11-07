create or replace view fdj_bms_fee_data_view as
select  fd.id as ID,---
b.id as BILL_ID,
b.bill_code as bill_code,
fd.be_income as be_income,--结算类型
bl.expense as expense,--实际费用
fd.group_no1,
fd.group_no2,
fd.happen_date as happen_date,
fd.refer1 as fee_Category_name,--费用类型 name
fd.refer5 as supCode,
fd.refer6 as supName,
fd.order_no as itemCode,
fd.refer7 as Billing_MODEL,--计费类型
fd.refer8 as warehouseCode_contact_POCode,--拼接字段,仓库code#合同编码#发票号1&发票号2
fd.d_refer1 as qty_amount,--总件数\销售额
fd.d_refer5 as FIXED_PRICE,--一口价
fd.created_time,--
fd.status,
fd.d_refer30 as flag,  ----默认为0,读取成功后,更新为1,wms只读取为"0"数据,失败为2
fd.refer100 as errorMesg
from fdjlfcs.bms_fee_data fd
left join fdjlfcs.bms_ledger bl on bl.fee_data_id=fd.id
left join fdjlfcs.bms_bill b on b.id = bl.bill_id
where 1=1 order by flag;