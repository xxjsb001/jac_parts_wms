SELECT 
 LOC.STATUS AS "组号",
 LOC.ID AS "库位.库位序号",
 LOC.CODE AS "库位.库位代码"
 FROM WMS_LOCATION LOC
 LEFT JOIN WMS_ZONE AREA ON AREA.ID = LOC.ZONE_ID
WHERE 1=1
 AND LOC.STATUS = 'ENABLED'
 AND LOC.TYPE = {库位类型}
 /~仓库ID: AND LOC.WAREHOUSE_ID = {仓库ID}~/
 /~库区: AND AREA.CODE = {库区}~/
 /~区区间开始值: AND LOC.ZONE_NO >= {区区间开始值}~/
 /~区区间结束值: AND LOC.ZONE_NO <= {区区间结束值}~/
 /~排区间开始值: AND LOC.LINE_NO >= {排区间开始值}~/
 /~排区间结束值: AND LOC.LINE_NO <= {排区间结束值}~/
 /~列区间开始值: AND LOC.COL_NO >= {列区间开始值}~/
 /~列区间结束值: AND LOC.COL_NO <= {列区间结束值}~/
 /~层区间开始值: AND LOC.LAYER_NO >= {层区间开始值}~/
 /~层区间结束值: AND LOC.LAYER_NO <= {层区间结束值}~/