create or replace procedure monitorActualInventory is
  cursor safe_data is
    select * from wms_safe_inventory s where s.status = 'ENABLED';--查生效的安全库存
  data_cursor   safe_data%ROWTYPE;--安全库存行数据
  totalQuantity number(19, 0);--实际库存
begin
  OPEN safe_data;
  LOOP--循环处理
    FETCH safe_data
      INTO data_cursor;
    EXIT WHEN safe_data%NOTFOUND;
    
    select sum(ex.quantity_bu)--查实际库存
      into totalQuantity
      from wms_inventory_extend ex
      left join wms_inventory inv
        on ex.inventory_id = inv.id
      left join wms_item_key k
        on inv.item_key_id = k.id
      left join wms_item item
        on item.id = k.item_id
     where inv.status in ('-', '锁定')
       and k.supplier_id = data_cursor.suppiler_id 
       and item.id=data_cursor.item_id;
       
    if (totalQuantity  <= data_cursor.min_invqty ) then--实际库存<报缺库存,则报警
      update wms_safe_inventory set is_red = 1,REAL_INVENTORY=totalQuantity where id = data_cursor.id;
      --dbms_output.put_line('update 报警');
    else --如果实际库存没有低于报缺库存,则更新它的实际库存
        update wms_safe_inventory set REAL_INVENTORY=totalQuantity where id = data_cursor.id;
      --  dbms_output.put_line('update 实际库存');
    end if;
    
  end loop;
  close safe_data;
end; 
