-----------------拣货----------------
truncate table wms_job_log;
delete from wms_task_and_station;
delete from wms_movedoc_and_station;
delete from wms_bol_state_log;
TRUNCATE TABLE wms_bol_detail_extend;
delete from wms_bol_detail;
delete from wms_bol;
delete from wms_task t where t.type = 'MV_PICKTICKET_PICKING';
delete from wms_move_doc_detail mm where exists (
SELECT 1 from wms_move_doc m where m.type = 'MV_PICKTICKET_PICKING' and m.id = mm.move_doc_id
);
delete from wms_move_doc m where m.type = 'MV_PICKTICKET_PICKING';
update wms_box_type b set b.is_bol = 'N',b.is_picking = 'N';

---------发货单------------------
delete from wms_pick_ticket_detail;
delete from wms_pick_ticket;
delete from w_sps_appliance;
---------质检--------------------
delete from wms_quality_move_soi_log;
delete from wms_task t where t.type = 'MV_QUALITY_MOVE';
delete from wms_move_doc_detail mm where exists (
SELECT 1 from wms_move_doc m where m.type = 'MV_QUALITY_MOVE' and m.id = mm.move_doc_id
);
delete from wms_move_doc m where m.type = 'MV_QUALITY_MOVE';
truncate table w_qisplan;
---------补货移位------------------
DELETE FROM WMS_MOVE_DOC_LOCATION;
delete from wms_task t where t.type = 'MV_REPLENISHMENT_MOVE';
delete from wms_move_doc_detail mm where exists (
SELECT 1 from wms_move_doc m where m.type = 'MV_REPLENISHMENT_MOVE' and m.id = mm.move_doc_id
);
delete from wms_move_doc m where m.type = 'MV_REPLENISHMENT_MOVE';


---------库内移位-----------------
delete from wms_task t where t.type = 'MV_MOVE';
delete from wms_move_doc_detail mm where exists (
SELECT 1 from wms_move_doc m where m.type = 'MV_MOVE' and m.id = mm.move_doc_id
);
delete from wms_move_doc m where m.type = 'MV_MOVE';

----------垃圾库存清理----------------
delete from wms_inventory_extend ix where exists (select 1 from wms_inventory i where i.quantity_bu = 0 and i.id = ix.inventory_id);
delete from wms_inventory i where i.quantity_bu = 0;
update wms_inventory i set i.putaway_quantity_bu = 0,i.allocated_quantity_bu = 0 where (i.putaway_quantity_bu>0 or i.allocated_quantity_bu>0);
update wms_inventory_extend ix set ix.allocated_quantity_bu = 0 where ix.allocated_quantity_bu > 0;
delete from wms_inventory_log;
---------接口相关----------------
truncate table thorn_tasks;
truncate table MIDDLE_ASN_SRM_DETAIL;
truncate table MIDDLE_ORDER_JH;
truncate table MIDDLE_ORDER_KB;
truncate table MIDDLE_ORDER_SPS;
truncate table MIDDLE_SUPPLYITEMERP_A;
truncate table MIDDLE_SUPPLYITEMERP_B;
delete from W_HEAD;
--------ASN--------------------------
truncate table wms_received_record;
truncate table wms_asn_detail;
truncate table jac_pallet_serial;
delete from wms_asn;
delete from wms_task t where t.type = 'MV_PUTAWAY';
----------盘点----------------------
truncate table wms_count_record;
truncate table wms_count_detail;
delete from wms_count_plan;
----------日志-----------------------
truncate table exception_log;
truncate table thorn_rule_exception_log;