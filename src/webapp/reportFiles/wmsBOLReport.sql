select bb.sub_code as shdh,to_char(bb.require_arrivedate,'yyyy-MM-dd hh24:mi:ss') as jhsj,bol.vehicle_no,item.code as item_code,item.name as item_name,
sup.code as supplier_code,sup.name as supplier_name,sum(bbx.pick_quantitybu) as quantity_bu,bbx.mes_no,bbx.odr_su,bbx.fware,bbx.dware,
bbx.shdk,bbx.from_source,bbx.batch,bbx.station,bbx.is_jp,bbx.bax_type,bbx.package_qty,bol.bill_type_name
,bbx.item_sx,bol.boxtag_nums
from wms_bol_detail bb
left join wms_bol bol on bol.id = bb.bol_id
left join wms_item_key ik on ik.id = bb.item_key_id
left join wms_item item on item.id = ik.item_id
left join wms_organization sup on sup.id = ik.supplier_id
left join wms_bol_detail_extend bbx on bbx.bol_detail_id = bb.id
where bol.id = 1322
group by  bb.sub_code,bb.require_arrivedate,bol.vehicle_no,item.code,item.name,
sup.code,sup.name,bbx.mes_no,bbx.odr_su,bbx.fware,bbx.dware,bol.boxtag_nums,
bbx.shdk,bbx.from_source,bbx.batch,bbx.station,bbx.is_jp,bbx.bax_type,bbx.package_qty,bbx.item_sx,bol.bill_type_name
order by bbx.item_sx
