select m3.code as pick_code,to_char(m6.require_arrive_date,'yyyy-MM-dd hh24:mm') as require_arrive_date,m4.name as bill_type
,m2.production_line,m7.name as blg_name,
m1.box_tag,m1.type as box_type,m1.type_name,m9.code as sup_code,m9.name as sup_name,m5.code as item_code,m5.name as item_name
,t.plan_quantity_bu as quantity,m1.seq,m1.end_seq,t.from_loc_code 
from wms_task t
left join wms_movedoc_and_station m1 on m1.movedoc_detail_id = t.move_doc_detail_id
left join wms_move_doc_detail m2 on m2.id = m1.movedoc_detail_id
left join wms_move_doc m3 on m3.id = m2.move_doc_id
left join wms_bill_type m4 on m4.id = m3.bill_original_type_id
left join wms_item m5 on m5.id = m1.item_id
left join wms_pick_ticket m6 on m6.id = m3.pickticket_id
left join wms_worker m7 on m7.id = m3.blg_id
left join wms_item_key m8 on m8.id = t.item_key_id
left join wms_organization m9 on m9.id = m8.supplier_id 
where m3.id = ?
group by  m3.code ,to_char(m6.require_arrive_date,'yyyy-MM-dd hh24:mm') ,m4.name 
,m2.production_line,m7.name,
m1.box_tag,m1.type,m1.type_name,m9.code,m9.name,m5.code,m5.name
,t.plan_quantity_bu,m1.seq,m1.end_seq,t.from_loc_code 
order by m1.box_tag,m1.seq desc,m5.code