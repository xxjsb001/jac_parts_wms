ALTER TABLE wms_bill_type ADD BE_SAME_ASN CHAR;
UPDATE wms_bill_type SET BE_SAME_ASN = 'N';
ALTER TABLE wms_item_state ADD ORDERBY_QUALITY int;
alter table wms_pick_ticket_detail add HASH_CODE varchar2(100);
alter table wms_pick_ticket add SENDER varchar2(100);
alter table WMS_ITEM_STATE add BACK_INVENTORY_STATE varchar2(50);
alter table WMS_QUALITYBILL_STATUS add BACK_INVENTORY_STATE_ID number(19);
alter table wms_pick_ticket add RECEIVE_HOUSE varchar2(100);
alter table wms_pick_ticket add RECEIVE_FACTORY varchar2(100);
alter table wms_pick_ticket add RECEIVE_DOC varchar2(100);
alter table wms_pick_ticket_detail add DESCRPTION varchar2(255);
alter table wms_pick_ticket add SEND_TIME TIMESTAMP(6);
alter table USERS add time_zone VARCHAR2(50 CHAR);--4.5的包要执行
alter table wms_count_plan add SUPPLIER_ID number(19);

-- Create table
create table WMS_MIS_INVENTORY
(
  id               NUMBER(19) not null,
  discriminator    VARCHAR2(255 CHAR) not null,
  version          NUMBER(19) not null,
  company_name     VARCHAR2(50 CHAR),
  sup_name         VARCHAR2(50 CHAR),
  item_code        VARCHAR2(50 CHAR),
  extendpropc1     VARCHAR2(50 CHAR),
  inventory_qty    FLOAT not null,
  mis_qty          FLOAT not null,
  warehouse_id     NUMBER(19) not null,
  creator_id       NUMBER(19),
  creator          VARCHAR2(50 CHAR),
  created_time     TIMESTAMP(6),
  last_operator_id NUMBER(19),
  last_operator    VARCHAR2(50 CHAR),
  update_time      TIMESTAMP(6)
)
tablespace FDJ_ZZG
  pctfree 10
  initrans 1
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
-- Create/Recreate primary, unique and foreign key constraints 
alter table WMS_MIS_INVENTORY
  add primary key (ID)
  using index 
  tablespace FDJ_ZZG
  pctfree 10
  initrans 2
  maxtrans 255
  storage
  (
    initial 64K
    next 1M
    minextents 1
    maxextents unlimited
  );
alter table WMS_MIS_INVENTORY
  add constraint FK402BA8B2766B0DB6 foreign key (WAREHOUSE_ID)
  references WMS_WAREHOUSE (ID);
-- Create sequence 
create sequence WSEQ_MISINVENTORY
minvalue 1
maxvalue 9999999999999999999999999999
start with 41
increment by 1
cache 20;
alter table wms_task add PICK_TIME timestamp;
alter table wms_asn_detail add QUALITY_QUANTITY_BU float default 0.0;
alter table wms_asn_detail add QUALITY_CODE varchar2(50);
alter table wms_asn add PRINTASN_REPORT_DATE timestamp;
alter table WMS_QUALITY_MOVE_SOI_LOG add LOCATION varchar2(50);
alter table WMS_QUALITY_MOVE_SOI_LOG add QUALITY_CODE varchar2(50);