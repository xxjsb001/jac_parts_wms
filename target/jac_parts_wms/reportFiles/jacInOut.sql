--收发存日结明细报表 jacInOut.raq
select tt.order_date,tt.supcode,tt.supname,tt.itemcode,tt.itemname,tt.inventorystatus,tt.leixing
,sum(tt.quantity) as quantity,sum(tt.before_qty) as before_qty,sum(tt.before_onway_quantity) as before_onway_quantity 
from(
select to_char(t.order_date,'yyyy-MM-dd') as  order_date,
t.supcode as supcode,t.supname as supname,t.itemcode as itemcode,
t.itemname as itemname,
(case when t.inventorystatus like '%不合格%' then '不合格' 
      when t.inventorystatus in ('锁定','待检','报废-合格') then '不合格'
else '合格' end) as inventorystatus,sum(t.quantity) as quantity,
sum(t.before_onway_quantity) as before_onway_quantity,sum(t.BEFORE_QUANTITY) as before_qty,t.type as leixing
from wms_the_kont t
where to_char(t.order_date,'yyyy-MM-dd') >= ?
and to_char(t.order_date,'yyyy-MM-dd') <= ?
and (t.supcode = ? or ? is null) and (t.itemcode = ? or ? is null) 
and (t.inventorystatus = ? or ? is null)
and t.type in ('日结库存','收货','发货','在途库存') and t.note <> '次日发运'
group by t.order_date,t.supcode,t.supname,t.itemcode,t.itemname,t.inventorystatus,t.type)tt 
group by tt.order_date,tt.supcode,tt.supname,tt.itemcode,tt.itemname,tt.inventorystatus,tt.leixing
order by tt.order_date,tt.supcode,tt.itemcode,tt.inventorystatus