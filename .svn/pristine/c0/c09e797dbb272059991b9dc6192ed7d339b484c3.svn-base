select bb.sub_code as shdh,to_char(bb.require_arrivedate,'yyyy-MM-dd hh24:mi:ss') as jhsj,item.code as item_code,item.name as item_name,
sup.code as supplier_code,sup.name as supplier_name,bbx.mes_no,bbx.odr_su,bbx.fware,bbx.dware,
bbx.shdk,bbx.station,bbx.is_jp,bbx.container,bbx.loadage,bbx.sx,bbx.bax_tag,bb.item_sub_qty,bbx.seq,bbx.end_seq,sum(bbx.pick_quantitybu) as quantity_bu
,bol.bill_type_name
from wms_bol_detail bb
left join wms_bol bol on bol.id = bb.bol_id
left join wms_item_key ik on ik.id = bb.item_key_id
left join wms_item item on item.id = ik.item_id
left join wms_organization sup on sup.id = ik.supplier_id
left join wms_bol_detail_extend bbx on bbx.bol_detail_id = bb.id
where bol.id = 1323
group by  bb.sub_code,bb.require_arrivedate,item.code,item.name,bbx.shdk,bbx.batch,bbx.bax_tag,
sup.code,sup.name,bbx.mes_no,bbx.odr_su,bbx.fware,bbx.dware,bol.boxtag_nums,bbx.station,
bbx.is_jp,bbx.sx,bol.bill_type_name,bbx.container,bbx.loadage,bb.item_sub_qty,bbx.seq,bbx.end_seq
order by bbx.bax_tag,bbx.seq
