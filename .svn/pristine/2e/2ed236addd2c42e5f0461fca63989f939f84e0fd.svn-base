drop table MIDDLE_SUPPLIERS_MATERIALS_WMS;
-- Create table
CREATE TABLE MIDDLE_SUPPLIERS_MATERIALS_WMS (
  ID NUMBER(19) NOT NULL,
  COMPANY varchar2(50) DEFAULT 106 NOT NULL,
  MATERIAL_CODE varchar2(50 CHAR) NOT NULL,
  SUPPLIER_CODE varchar2(50 CHAR) NOT NULL,
  ACTIVE NUMBER(10) NOT NULL,
  SENDER varchar2(100 CHAR) NOT NULL,
  MEMO varchar2(255 CHAR) DEFAULT NULL,
  CREATE_TIME TIMESTAMP(6) DEFAULT systimestamp,
  DEAL_TIME TIMESTAMP(6) DEFAULT null
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
alter table MIDDLE_SUPPLIERS_MATERIALS_WMS
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
 alter table MIDDLE_SUPPLIERS_MATERIALS_WMS
  add unique (COMPANY, MATERIAL_CODE,SUPPLIER_CODE)
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
