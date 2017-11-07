--质检往质检表插入数据
create or replace procedure insert_middle_quality_testing(MOVECODE in varchar2,ASNCODE in varchar2,SUPPLIERCODE in varchar2,MATERIALCODE in varchar2,
       PROCESSSTATE in varchar2,SENDQTY in float,MESQUALITYCODE in varchar2,RECEIVEDQUANTITYBU in float)
as
begin
  insert into middle_quality_testing(ID,code,company,asn_Code,supplier_code,material_code,process_state,send_qty,MES_QUALITY_CODE,RECEIVED_QUANTITY_BU,SENDER)
values(seq_enumerate.nextval,MOVECODE,'106',ASNCODE,SUPPLIERCODE,MATERIALCODE,PROCESSSTATE,SENDQTY,MESQUALITYCODE,RECEIVEDQUANTITYBU,'FDJ');
  commit;
  exception
    when others
      then
        dbms_output.enable(buffer_size => null);
        dbms_output.put_line('sqlerrm:'||sqlerrm);
      rollback;
end;