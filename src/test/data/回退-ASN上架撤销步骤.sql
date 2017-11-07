-----------强调后期维护人员,SQL不允许提供给甲方
---1
delete from wms_inventory_extend ix where ix.inventory_id in (
select i.id from wms_inventory i where i.item_key_id in (
select t.item_key_id from wms_task t where t.original_billcode='JAC_FDJIN150611000003'
));
---2
delete from wms_inventory i where i.item_key_id in (
select t.item_key_id from wms_task t where t.original_billcode='JAC_FDJIN150611000003'
);
---3
update jac_pallet_serial j set j.to_location_id = null,j.to_loc_code=null,j.EXPECTED_QUANTITY_BU=0,
j.be_putawayauto='N' where j.asn_detail_id in (
select aa.id from wms_asn_detail aa where aa.soi='JAC_FDJIN150611000003'
);
---4
update wms_asn_detail aa set aa.moved_quantity_bu=0,aa.be_received='N',
aa.received_quantity_bu=0 where aa.soi='JAC_FDJIN150611000003';
---5
update wms_asn a set a.allocated_quantity_bu=0,a.moved_quantity_bu=0,a.received_quantity_bu=0
,a.shelves_stauts='UNPUTAWAY',a.auto_allocate_stauts='OPEN',
a.status='ACTIVE' where a.code = 'JAC_FDJIN150611000003';
---6
delete from wms_received_record r where r.item_key_id in (
select t.item_key_id from wms_task t where t.original_billcode='JAC_FDJIN150611000003'
);
---7
update wms_task t set t.original_billcode='-' 
where t.original_billcode='JAC_FDJIN150611000003';
---
commit;
