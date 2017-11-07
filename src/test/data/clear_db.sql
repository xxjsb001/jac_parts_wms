-- 清除业务数据脚本 --

DELETE FROM WMS_ASN_QUALITY;
DELETE FROM WMS_BOX_DETAIL;
DELETE FROM WMS_BOL_STATE_LOG;
DELETE FROM THORN_RULE_EXCEPTION_LOG;
DELETE FROM WMS_WAVE_DETAIL_MOVE_DETAIL;
DELETE FROM WMS_INVENTORY_EXTEND;
DELETE FROM WMS_INVENTORY_LOG;
DELETE FROM WMS_INVENTORY;
DELETE FROM WMS_TASK_LOG; 
DELETE FROM WMS_TASK;
DELETE FROM WMS_WAVE_DOC_DETAIL;
DELETE FROM WMS_MOVE_DOC_DETAIL;
DELETE FROM WMS_MOVE_DOC;
--DELETE FROM WMS_PROCESSPLAN_DETAIL;
--DELETE FROM WMS_PROCESSPLAN;
DELETE FROM WMS_PICK_TICKET_DETAIL;
DELETE FROM WMS_PICK_TICKET;
DELETE FROM WMS_WAVE_DOC;
DELETE FROM WMS_BOOKING;
DELETE FROM WMS_RECEIVED_RECORD;
DELETE FROM JAC_PALLET_SERIAL;
DELETE FROM WMS_ASN_DETAIL;
DELETE FROM WMS_ASN;
DELETE FROM WMS_WORK_DOC; 
DELETE FROM WMS_COUNT_RECORD;
DELETE FROM WMS_COUNT_DETAIL;
DELETE FROM WMS_COUNT_PLAN;
DELETE FROM WMS_STORAGE_DAILY;
DELETE FROM WMS_ITEM_KEY;
DELETE FROM WMS_MASTER_BOL;
DELETE FROM WMS_INVENTORY_COUNT;
DELETE FROM WMS_INVENTORY_FEE;
DELETE FROM EXCEPTION_LOG;
delete from WMS_THE_KONT;
delete from WMS_QUALITY_MOVE_SOI_LOG;--记录质检日志
delete from MES_MIS_INVENTORY;--mes报缺
delete from WMS_BILL_DETAIL;--账单明细
delete from WMS_CONTACT_DETAIL;--合同明细
delete from WMS_PURCHASE_INVOICE_DETAIL;--发动机采购发票
delete from WMS_INVOICE_DETAIL_CATEGORY;--发票明细类别
delete from WMS_INVOICE;--发票信息
delete from WMS_CONTACT;--合同
delete from WMS_BILLING_CATEGORY;--结算类型

truncate table THORN_TASKS;
truncate table HORN_INTERFACE_LOG;
truncate table EXCEPTION_LOG;
truncate table SCREEN_HG_INVENTORY_OUT

UPDATE WMS_LOCATION LOC
   SET LOC.COUNT_LOCK     = 'N',
       LOC.USE_RATE            = 0,
       LOC.PALLET_QTY          = 0,
       LOC.TOUCH_COUNT = 0,
       LOC.LOCATION_STATUS     = 'EMPTY',
       LOC.EXCEPTION_FLAG     = 'N';

UPDATE WMS_WAREHOUSE set IMAGE_URL = 'images/inventoryview/warehouse_1.jpg' WHERE IMAGE_URL = '' OR IMAGE_URL IS NULL;

-- 清除基础数据脚本 --
DELETE FROM WMS_PACKAGE_CHANGE_LOG;
DELETE FROM WMS_PACKAGE_UNIT;
DELETE FROM WMS_ITEM;
DELETE FROM WMS_ORGANIZATION WHERE IS_COMPANY = 'N';
DELETE FROM WMS_DOCK;
UPDATE WMS_LOCATION SET PICK_BACK_LOC = null;
--DELETE FROM WMS_LOCATION;
DELETE FROM WMS_LOCATION where code not in ('A-越库','A-退货','A-收货','A-签收区','A-盘点','A-不合格品区');
delete from wms_worker;





------以下不要玩,测试用的,会死人的
--删除一个角色
DELETE FROM REPORT_ROLE R WHERE R.ROLE_ID =1052--报表授权表
DELETE FROM ROLE_SYSTEM_FUNCTION R WHERE R.ROLE_ID = 1052;--角色他也菜单绑定表
DELETE FROM ROLE_USER R WHERE R.ROLE_ID = 1052;--角色绑定用户表
delete from PERMISSIONS p where p.role_id = 1052;--角色拥有的权限
delete from roles r where r.id = 1052;
--授权一个角色多个菜单
select * from ROLE_SYSTEM_FUNCTION r;
select * from system_function;--流程授权
select * from PERMISSIONS r --角色拥有的权限
--授权一个角色多个流程