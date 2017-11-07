drop table MIDDLE_MATERIAL_WMS;
-- Create table
CREATE TABLE MIDDLE_MATERIAL_WMS (
  ID NUMBER(19) NOT NULL,
  COMPANY varchar2(50) DEFAULT 106 NOT NULL,
  MATERIAL_CODE varchar2(50 CHAR) NOT NULL,
  MATERIAL_NAME varchar2(100 CHAR) NOT NULL,
  SENDER varchar2(100 CHAR) NOT NULL,
  MATERIAL_ABBR varchar2(100 CHAR) DEFAULT NULL,
  BASE_UINT varchar2(20 CHAR) DEFAULT NULL,
  MATERIAL_TYPE varchar2(50 CHAR) DEFAULT '采购件',
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
alter table MIDDLE_MATERIAL_WMS
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
 alter table MIDDLE_MATERIAL_WMS
  add unique (COMPANY, MATERIAL_CODE)
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
