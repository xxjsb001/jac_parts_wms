SELECT t.组号 AS "组号",t.库位序号 AS "库位.库位序号",t.库位代码 AS "库位.库位代码",t.区 AS "库位.区",
 t.排 AS "库位.排",t.层 AS "库位.层",t.列 AS "库位.列",t.动线号 AS "库位.动线号"
FROM (
SELECT LOCATION.STATUS AS 组号,
        LOCATION.ID AS 库位序号,
        LOCATION.CODE AS 库位代码,
    LOCATION.ZONE_NO AS 区,
        LOCATION.LINE_NO AS 排,
    LOCATION.LAYER_NO AS 层,
        LOCATION.COL_NO AS 列,
        LOCATION.ROUTE_NO AS 动线号,
        AREA.WAREHOUSE_ID AS 仓库ID,
        AREA.CODE AS 库区
 FROM WMS_LOCATION LOCATION
 LEFT JOIN WMS_ZONE AREA ON AREA.ID = LOCATION.ZONE_ID
 WHERE 1=1
 AND LOCATION.COUNT_LOCK = 'N'
 AND LOCATION.STATUS = 'ENABLED'
 AND LOCATION.EXCEPTION_FLAG = 'N'
 AND LOCATION.TYPE = 'STORAGE'
 AND not exists (SELECT 1 FROM wms_inventory i where (i.quantity_bu+i.allocated_quantity_bu+i.putaway_quantity_bu)>0 
 and i.location_id=LOCATION.Id)
 ORDER BY LOCATION.ZONE_NO ASC,LOCATION.LINE_NO ASC,
 LOCATION.LAYER_NO DESC,LOCATION.COL_NO ASC,LOCATION.ROUTE_NO ASC
 )t WHERE 1=1 
 /~仓库ID: AND t.仓库ID = {仓库ID}~/
 /~库区: AND t.库区 = {库区}~/
 /~区区间开始值: AND t.区 >= {区区间开始值}~/
 /~区区间结束值: AND t.区 <= {区区间结束值}~/
 /~排区间开始值: AND t.排 >= {排区间开始值}~/
 /~排区间结束值: AND t.排 <= {排区间结束值}~/
 /~列区间开始值: AND t.列 >= {列区间开始值}~/
 /~列区间结束值: AND t.列 <= {列区间结束值}~/
 /~层区间开始值: AND t.层 >= {层区间开始值}~/
 /~层区间结束值: AND t.层 <= {层区间结束值}~/
 AND rownum<={托盘个数}