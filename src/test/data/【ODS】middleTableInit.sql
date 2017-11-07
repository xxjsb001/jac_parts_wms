--sys/Jacjqwl123登录
create user fdjmiddle identified by fdjmiddle;
alter user fdjmiddle default tablespace fdj_zzg;
grant create session,create table,create view,create sequence,unlimited tablespace to fdjmiddle;
--fdjmiddle/fdjmiddle登录
--*****执行以下建表语句*******
--MIDDLE_SUPPLIER.sql,MIDDLE_MATERIAL.sql,MIDDLE_SUPPLIERS_OF_MATERIALS.sql
--MIDDLE_DELIVERY_DOC.sql,MIDDLE_DELIVERY_DOC_DETAIL.sql
--sys/Jacjqwl123 登录
create user fdjmiddlesys identified by fdjmiddlesys;
grant create session to fdjmiddlesys;
--MIDDLE_SUPPLIER
create public synonym MIDDLE_SUPPLIER for fdjmiddle.middle_supplier_wms;
grant select,insert,update,delete on MIDDLE_SUPPLIER to fdjmiddlesys;
grant select,insert,update,delete on MIDDLE_SUPPLIER to fdjtest;
--MIDDLE_MATERIAL
create public synonym MIDDLE_MATERIAL for fdjmiddle.Middle_Material_Wms;
grant select,insert,update,delete on MIDDLE_MATERIAL to fdjmiddlesys;
grant select,insert,update,delete on MIDDLE_MATERIAL to fdjtest;
--MIDDLE_SUPPLIERS_OF_MATERIALS
create public synonym MIDDLE_SUPPLIERS_OF_MATERIALS for fdjmiddle.middle_suppliers_materials_wms;
grant select,insert,update,delete on MIDDLE_SUPPLIERS_OF_MATERIALS to fdjmiddlesys;
grant select,insert,update,delete on MIDDLE_SUPPLIERS_OF_MATERIALS to fdjtest;
--MIDDLE_DELIVERY_DOC
create public synonym MIDDLE_DELIVERY_DOC for fdjmiddle.Middle_Delivery_Doc_Wms;
grant select,insert,update,delete on MIDDLE_DELIVERY_DOC to fdjmiddlesys;
grant select,insert,update,delete on MIDDLE_DELIVERY_DOC to fdjtest;
--MIDDLE_DELIVERY_DOC_DETAIL
create public synonym MIDDLE_DELIVERY_DOC_DETAIL for fdjmiddle.Middle_Delivery_Doc_Detail_Wms;
grant select,insert,update,delete on MIDDLE_DELIVERY_DOC_DETAIL to fdjmiddlesys;
grant select,insert,update,delete on MIDDLE_DELIVERY_DOC_DETAIL to fdjtest;

--drop public synonym MIDDLE_SUPPLIER_SYS;
--fdjmiddlesys/fdjmiddlesys登录
select * from MIDDLE_SUPPLIER;
select * from MIDDLE_MATERIAL;
select * from MIDDLE_SUPPLIERS_OF_MATERIALS;
select * from MIDDLE_DELIVERY_DOC;
select * from MIDDLE_DELIVERY_DOC_DETAIL;