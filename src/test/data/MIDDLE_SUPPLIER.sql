DROP TABLE MIDDLE_SUPPLIER_WMS;
-- Create table
CREATE TABLE MIDDLE_SUPPLIER_WMS (
  ID NUMBER(19) NOT NULL,
  COMPANY varchar2(50) DEFAULT 106 NOT NULL,
  SUPPLIER_CODE varchar2(50 CHAR) NOT NULL,
  SUPPLIER_NAME varchar2(100 CHAR) NOT NULL,
  COUNTRY varchar2(100 CHAR) NOT NULL,
  SENDER varchar2(100 CHAR) NOT NULL,
  PROVINCE varchar2(100 CHAR) DEFAULT NULL,
  CITY varchar2(50 CHAR) DEFAULT NULL,
  POSTCODE varchar2(10 CHAR) DEFAULT NULL,
  CONTACT_PERSON varchar2(50 CHAR) DEFAULT NULL,
  CONTACT_PHONE varchar2(50 CHAR) DEFAULT NULL,
  CELL_PHONE varchar2(50 CHAR) DEFAULT NULL,
  FAX varchar2(50 CHAR) DEFAULT NULL,
  EMAIL varchar2(100 CHAR) DEFAULT NULL,
  ADDRESS varchar2(255 CHAR) DEFAULT NULL,
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
alter table MIDDLE_SUPPLIER_WMS
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
 alter table MIDDLE_SUPPLIER_WMS
  add unique (COMPANY, SUPPLIER_CODE)
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
