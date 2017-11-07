SELECT
move_doc.WAREHOUSE_ID AS "波次",
move_doc.id AS "移位单.序号"
FROM wms_move_doc move_doc
LEFT JOIN wms_pick_ticket pick_ticket ON pick_ticket.ID = move_doc.PICKTICKET_ID
WHERE 1=1
AND move_doc.MASTER_BOL_ID IS NULL
AND move_doc.STATUS = 'OPEN'
AND move_doc.BE_WAVE = 'N'
AND move_doc.TYPE = 'MV_PICKTICKET_PICKING'
AND move_doc.BE_CROSS_DOCK = 'N'
/~仓库ID: AND move_doc.WAREHOUSE_ID = {仓库ID}~/
/~开始时间: AND pick_ticket.ORDER_DATE >= {开始时间}~/
/~结束时间: AND pick_ticket.ORDER_DATE < {结束时间}~/