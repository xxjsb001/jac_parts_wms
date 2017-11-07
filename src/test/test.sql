SELECT
 WWD.ID AS "序号",
 WWD.CODE AS "代码",
 WWA.CODE AS "工作区",
 (SELECT COUNT(DISTINCT TASK.RELATED_BILL) FROM WMS_TASK TASK  WHERE TASK.WORK_DOC_ID = WWD.ID) AS "单数",
 WWD.EXPECT_QUANTITY_BU AS "件数",
 (SELECT SUM(TASK.PLAN_WEIGHT) FROM WMS_TASK TASK WHERE TASK.WORK_DOC_ID = WWD.ID) AS "重量",
 (SELECT SUM(TASK.PLAN_VOLUME) FROM WMS_TASK TASK WHERE TASK.WORK_DOC_ID = WWD.ID) AS "体积"
 FROM WMS_WORK_DOC WWD LEFT JOIN WMS_WORK_AREA WWA ON WWD.WORK_AREA_ID = WWA.ID 
 WHERE 1=1 
 AND WWD.STATUS = "OPEN"  

 
 
 
 
 
 UPDATE THORN_DIRECTORY SET UP_LINE = 1 WHERE IS_ROOT = 1
 
 
 
 
 
 UPDATE WMS_ITEM ITEM  SET ITEM.BASE_UNIT = (SELECT WPU.UNIT FROM WMS_PACKAGE_UNIT WPU WHERE WPU.ITEM_ID = ITEM.ID AND WPU.LINE_NO = 1)
 
 
 
 
 
 
 --零拣库位占用比例查询
 select location.code , location.USE_RATE from wms_location location  WHERE location.zone_no > 10 AND location.zone_no <= 16  order by location.USE_RATE desc
 
 
 
 
 
 select location.code , location.USE_RATE from wms_location location  WHERE location.code in ('A-11010101','A-11010102','A-11010103','A-11010104','A-11010201')
 
 
 
 
 
 
 
 
 
 
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
        LOCATION.USE_RATE AS "库位.占用比例",
        AREA.CODE AS "库位.库区",
        INV.ID AS "库位.库存.序号",
        UNIT.ID AS "库位.库存.包装序号",
        UNIT.WEIGHT AS "库位.库存.包装重量",
        UNIT.VOLUME AS "库位.库存.包装体积",
        (INV.QUANTITY_BU +  INV.PUTAWAY_QUANTITY_BU) AS "库位.库存.包装数量",
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
 AND LOCATION.COUNT_LOCK = "N"
 AND LOCATION.STATUS = "ENABLED"
 AND LOCATION.USE_RATE < 100  AND AREA.WAREHOUSE_ID = 1
 limit 0,200

 
 
 
 
 
 
 
 
 
 update thorn_rule_table_item set REFERENCE_TYPE = null WHERE TYPE = '4_VALUE'
 
 
 
 
 
 
  select wms_inventory.id ,wms_inventory.ITEM_KEY_ID 
  from  wms_inventory wms_inventory
  where wms_inventory.ITEM_KEY_ID = 
 (select wtl.ITEM_KEY_ID FROM wms_task_log wtl WHERE wtl.id = 
  (select max(wms_task_log.id) from wms_task_log wms_task_log WHERE wms_task_log.SRC_INVENTORY_ID = wms_inventory.id)
  ) 
 

   select wms_inventory.id ,wms_inventory.ITEM_KEY_ID , 
   (select max(wms_task_log.ITEM_KEY_ID) from wms_task_log wms_task_log 
   WHERE wms_task_log.SRC_INVENTORY_ID = wms_inventory.id
   or wms_task_log.DESC_INVENTORY_ID = wms_inventory.id
   )
  from  wms_inventory wms_inventory 
  
 
  
   select wms_inventory.id ,wms_inventory.ITEM_KEY_ID , (select max(wms_task_log.ITEM_KEY_ID) from wms_task_log wms_task_log WHERE wms_task_log.DESC_INVENTORY_ID = wms_inventory.id)
  from  wms_inventory wms_inventory 
 
 
 
 
 update wms_inventory wms_inventory 
 set wms_inventory.ITEM_KEY_ID = 
 (select wtl.ITEM_KEY_ID FROM wms_task_log wtl WHERE wtl.id = 
  (select max(wms_task_log.id) from wms_task_log wms_task_log WHERE wms_task_log.SRC_INVENTORY_ID = wms_inventory.id)
  and (select max(wms_task_log.id) from wms_task_log wms_task_log WHERE wms_task_log.SRC_INVENTORY_ID = wms_inventory.id) is not null
  ) 

   update wms_inventory wms_inventory 
 set wms_inventory.ITEM_KEY_ID = (select max(wms_task_log.ITEM_KEY_ID) from wms_task_log wms_task_log WHERE wms_task_log.DESC_INVENTORY_ID = wms_inventory.id)
  where wms_inventory.ITEM_KEY_ID = 196
  
