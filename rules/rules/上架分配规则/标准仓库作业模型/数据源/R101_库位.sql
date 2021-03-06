SELECT LOCATION.STATUS AS "组号",
        LOCATION.ID AS "库位.库位序号",
        LOCATION.CODE AS "库位.库位代码",
        LOCATION.STATUS AS "库位.库位状态",
        LOCATION.USE_RATE AS "库位.库位占用比例",
        LOCATION.ZONE_NO AS "库位.区",
        LOCATION.LINE_NO AS "库位.排",
        LOCATION.COL_NO AS "库位.列",
        LOCATION.LAYER_NO AS "库位.层",
        LOCATION.ROUTE_NO AS "库位.动线号",
        AREA.CODE AS "库位.库区",
        INV.ID AS "库位.库存.序号",
        UNIT.ID AS "库位.库存.包装序号",
        UNIT.WEIGHT AS "库位.库存.包装重量",
        UNIT.VOLUME AS "库位.库存.包装体积",
        (INV.QUANTITY_BU +  INV.PUTAWAY_QUANTITY_BU) AS "库位.库存.数量",
        ITEM.ID AS "库位.库存.货品序号",
        ITEMKEY.ID AS "库位.库存.批次序号"
 FROM WMS_LOCATION LOCATION
 LEFT JOIN WMS_ZONE AREA ON AREA.ID = LOCATION.ZONE_ID
 LEFT JOIN WMS_INVENTORY INV ON INV.LOCATION_ID = LOCATION.ID
 LEFT JOIN WMS_PACKAGE_UNIT UNIT ON UNIT.ID = INV.PACKAGE_UNIT_ID
 LEFT JOIN WMS_ITEM_KEY ITEMKEY ON ITEMKEY.ID = INV.ITEM_KEY_ID
 LEFT JOIN WMS_ITEM ITEM ON ITEM.ID = ITEMKEY.ITEM_ID
 LEFT JOIN WMS_ORGANIZATION SUPPLIER ON SUPPLIER.ID = ITEMKEY.SUPPLIER_ID
 WHERE 1=1
 AND LOCATION.COUNT_LOCK = 'N'
 AND LOCATION.STATUS = 'ENABLED'
 AND LOCATION.EXCEPTION_FLAG = 'N'
 AND LOCATION.USE_RATE < 100   
 /~仓库ID: AND AREA.WAREHOUSE_ID = {仓库ID}~/
 /~库区: AND AREA.CODE = {库区}~/
 /~区区间开始值: AND LOCATION.ZONE_NO >= {区区间开始值}~/
 /~区区间结束值: AND LOCATION.ZONE_NO <= {区区间结束值}~/
 /~排区间开始值: AND LOCATION.LINE_NO >= {排区间开始值}~/
 /~排区间结束值: AND LOCATION.LINE_NO <= {排区间结束值}~/
 /~列区间开始值: AND LOCATION.COL_NO >= {列区间开始值}~/
 /~列区间结束值: AND LOCATION.COL_NO <= {列区间结束值}~/
 /~层区间开始值: AND LOCATION.LAYER_NO >= {层区间开始值}~/
 /~层区间结束值: AND LOCATION.LAYER_NO <= {层区间结束值}~/
 AND (LOCATION.ID NOT IN (SELECT LOCATION.ID 
       FROM WMS_LOCATION LOCATION
       WHERE 1 <> 1
       /~拼接条件: OR #{拼接条件}~/
 )
) limit 0,200
