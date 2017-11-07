
-- 删除业务表

set foreign_key_checks= 0;
set unique_checks=0;

drop table WMS_BOL_STATE_LOG;
drop table THORN_RULE_EXCEPTION_LOG;
drop table WMS_WAVE_DETAIL_MOVE_DETAIL;
drop table WMS_INVENTORY_EXTEND;
drop table WMS_INVENTORY_LOG;
drop table WMS_INVENTORY;
drop table WMS_TASK_LOG; 
drop table WMS_TASK;
drop table WMS_WAVE_DOC_DETAIL;
drop table WMS_MOVE_DOC_DETAIL;
drop table WMS_MOVE_DOC;
drop table WMS_PROCESSPLAN_DETAIL;
drop table WMS_PROCESSPLAN;
drop table WMS_PICK_TICKET_DETAIL;
drop table WMS_PICK_TICKET;
drop table WMS_WAVE_DOC;
drop table WMS_RECEIVED_RECORD;
drop table WMS_ASN_DETAIL;
drop table WMS_ASN;
drop table WMS_WORK_DOC;
drop table WMS_COUNT_RECORD;
drop table WMS_COUNT_DETAIL;
drop table WMS_COUNT_PLAN;
drop table WMS_STORAGE_DAILY;
drop table WMS_ITEM_KEY;
drop table WMS_MASTER_BOL;
drop table WMS_INVENTORY_COUNT;
drop table WMS_INVENTORY_FEE;

set foreign_key_checks= 1;
set unique_checks=1;


ALTER TABLE wms_location   DROP COLUMN UNIT_LINE;
ALTER TABLE wms_location   DROP COLUMN USE_RATE;
ALTER TABLE wms_dock   DROP COLUMN NAME;
ALTER TABLE WMS_RULE_LOT   DROP COLUMN NAME;
ALTER TABLE WMS_PACKAGE_UNIT   DROP COLUMN PRECISION_BU;
ALTER TABLE WMS_MOVE_DOC   DROP COLUMN WORK_DOC_STATUS;
ALTER TABLE WMS_MOVE_DOC   DROP COLUMN PROCESS_DOC_ID;

delete from wms_bill_type where type = 'MOVE';
insert into wms_bill_type(ID, DISCRIMINATOR, COMPANY_ID, CODE, NAME, TYPE, BEINNER, STATUS) 
values (100, 'com.vtradex.wms.server.model.organization.WmsBillType', 1, 'MV_PUTAWAY', '收货上架单', 'MOVE', 'Y', 'ENABLED');
insert into wms_bill_type(ID, DISCRIMINATOR, COMPANY_ID, CODE, NAME, TYPE, BEINNER, STATUS) 
values (101, 'com.vtradex.wms.server.model.organization.WmsBillType', 1, 'MV_MOVE', '库内移位单', 'MOVE', 'Y', 'ENABLED');
insert into wms_bill_type(ID, DISCRIMINATOR, COMPANY_ID, CODE, NAME, TYPE, BEINNER, STATUS) 
values (102, 'com.vtradex.wms.server.model.organization.WmsBillType', 1, 'MV_PICKTICKET_PICKING', '发货拣货单', 'MOVE', 'Y', 'ENABLED');
insert into wms_bill_type(ID, DISCRIMINATOR, COMPANY_ID, CODE, NAME, TYPE, BEINNER, STATUS) 
values (103, 'com.vtradex.wms.server.model.organization.WmsBillType', 1, 'MV_WAVE_PICKING', '波次拣货单', 'MOVE', 'Y', 'ENABLED');
insert into wms_bill_type(ID, DISCRIMINATOR, COMPANY_ID, CODE, NAME, TYPE, BEINNER, STATUS) 
values (104, 'com.vtradex.wms.server.model.organization.WmsBillType', 1, 'MV_PROCESS_PICKING', '加工拣货单', 'MOVE', 'Y', 'ENABLED');
insert into wms_bill_type(ID, DISCRIMINATOR, COMPANY_ID, CODE, NAME, TYPE, BEINNER, STATUS) 
values (105, 'com.vtradex.wms.server.model.organization.WmsBillType', 1, 'MV_REPLENISHMENT_MOVE', '补货移位单', 'MOVE', 'Y', 'ENABLED');

insert into wms_bill_type(ID, DISCRIMINATOR, COMPANY_ID, CODE, NAME, TYPE, BEINNER, STATUS) 
values (200, 'com.vtradex.wms.server.model.organization.WmsBillType', 32, 'MV_PUTAWAY', '收货上架单', 'MOVE', 'Y', 'ENABLED');
insert into wms_bill_type(ID, DISCRIMINATOR, COMPANY_ID, CODE, NAME, TYPE, BEINNER, STATUS) 
values (201, 'com.vtradex.wms.server.model.organization.WmsBillType', 32, 'MV_MOVE', '库内移位单', 'MOVE', 'Y', 'ENABLED');
insert into wms_bill_type(ID, DISCRIMINATOR, COMPANY_ID, CODE, NAME, TYPE, BEINNER, STATUS) 
values (202, 'com.vtradex.wms.server.model.organization.WmsBillType', 32, 'MV_PICKTICKET_PICKING', '发货拣货单', 'MOVE', 'Y', 'ENABLED');
insert into wms_bill_type(ID, DISCRIMINATOR, COMPANY_ID, CODE, NAME, TYPE, BEINNER, STATUS) 
values (203, 'com.vtradex.wms.server.model.organization.WmsBillType', 32, 'MV_WAVE_PICKING', '波次拣货单', 'MOVE', 'Y', 'ENABLED');
insert into wms_bill_type(ID, DISCRIMINATOR, COMPANY_ID, CODE, NAME, TYPE, BEINNER, STATUS) 
values (204, 'com.vtradex.wms.server.model.organization.WmsBillType', 32, 'MV_PROCESS_PICKING', '加工拣货单', 'MOVE', 'Y', 'ENABLED');
insert into wms_bill_type(ID, DISCRIMINATOR, COMPANY_ID, CODE, NAME, TYPE, BEINNER, STATUS) 
values (205, 'com.vtradex.wms.server.model.organization.WmsBillType', 32, 'MV_REPLENISHMENT_MOVE', '补货移位单', 'MOVE', 'Y', 'ENABLED');

insert into wms_bill_type(ID, DISCRIMINATOR, COMPANY_ID, CODE, NAME, TYPE, BEINNER, STATUS) 
values (300, 'com.vtradex.wms.server.model.organization.WmsBillType', 34, 'MV_PUTAWAY', '收货上架单', 'MOVE', 'Y', 'ENABLED');
insert into wms_bill_type(ID, DISCRIMINATOR, COMPANY_ID, CODE, NAME, TYPE, BEINNER, STATUS) 
values (301, 'com.vtradex.wms.server.model.organization.WmsBillType', 34, 'MV_MOVE', '库内移位单', 'MOVE', 'Y', 'ENABLED');
insert into wms_bill_type(ID, DISCRIMINATOR, COMPANY_ID, CODE, NAME, TYPE, BEINNER, STATUS) 
values (302, 'com.vtradex.wms.server.model.organization.WmsBillType', 34, 'MV_PICKTICKET_PICKING', '发货拣货单', 'MOVE', 'Y', 'ENABLED');
insert into wms_bill_type(ID, DISCRIMINATOR, COMPANY_ID, CODE, NAME, TYPE, BEINNER, STATUS) 
values (303, 'com.vtradex.wms.server.model.organization.WmsBillType', 34, 'MV_WAVE_PICKING', '波次拣货单', 'MOVE', 'Y', 'ENABLED');
insert into wms_bill_type(ID, DISCRIMINATOR, COMPANY_ID, CODE, NAME, TYPE, BEINNER, STATUS) 
values (304, 'com.vtradex.wms.server.model.organization.WmsBillType', 34, 'MV_PROCESS_PICKING', '加工拣货单', 'MOVE', 'Y', 'ENABLED');
insert into wms_bill_type(ID, DISCRIMINATOR, COMPANY_ID, CODE, NAME, TYPE, BEINNER, STATUS) 
values (305, 'com.vtradex.wms.server.model.organization.WmsBillType', 34, 'MV_REPLENISHMENT_MOVE', '补货移位单', 'MOVE', 'Y', 'ENABLED');