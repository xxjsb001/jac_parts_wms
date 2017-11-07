create or replace procedure insert_middle_material_pro
as
type balance is ref cursor;
balanceDo balance;
MATERIALCODE varchar2(50);MATERIALNAME varchar2(100);
BASEUINT varchar2(20);MATERIALTYPE varchar2(50);
begin
  open balanceDo for select i.code,i.name,i.base_unit,i.class1 
       from wms_item i; 
  loop
    fetch balanceDo into MATERIALCODE,MATERIALNAME,BASEUINT,MATERIALTYPE;
    exit when balanceDo%notfound;
    insert into MIDDLE_MATERIAL(ID,COMPANY,MATERIAL_CODE,MATERIAL_NAME,MATERIAL_ABBR,
BASE_UINT,MATERIAL_TYPE,MEMO,Sender) values (seq_enumerate.nextval,'106',MATERIALCODE,
MATERIALNAME,MATERIALNAME,BASEUINT,MATERIALTYPE,'-','FDJ');
  end loop;
  exception 
    when others
      then rollback;
  close balanceDo;
  commit;
  end;                                   
--SQL> exec insert_middle_material_pro;
