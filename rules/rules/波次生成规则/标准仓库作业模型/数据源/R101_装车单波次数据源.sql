SELECT
move_doc.WAREHOUSE_ID AS "波次",
master_bol.CODE AS "装车单.装车单号",
master_bol.INTEND_SHIP_DATE AS "装车单.预计发车时间",
move_doc.id AS "装车单.移位单.序号"
FROM wms_move_doc move_doc
LEFT JOIN wms_master_bol master_bol ON master_bol.ID = move_doc.MASTER_BOL_ID
LEFT JOIN wms_pick_ticket pick_ticket ON pick_ticket.id = move_doc.PICKTICKET_ID
WHERE 1=1 
AND move_doc.MASTER_BOL_ID IS NOT NULL
AND move_doc.STATUS = 'OPEN'
AND move_doc.BE_WAVE = 'N'
AND move_doc.TYPE = 'MV_PICKTICKET_PICKING'
AND move_doc.BE_CROSS_DOCK = 'N'
/~仓库ID: AND move_doc.WAREHOUSE_ID = {仓库ID}~/
GROUP BY move_doc.WAREHOUSE_ID,move_doc.id