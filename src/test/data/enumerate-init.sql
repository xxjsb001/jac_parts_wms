-- SELECT * FROM enumerate WHERE ENUM_TYPE = 'BaseStatus';
-- SELECT * FROM enumerate_bak eb WHERE not EXISTS (SELECT * from enumerate e where e.ENUM_TYPE = eb.ENUM_TYPE and e.ENUM_VALUE = eb.ENUM_VALUE)

VALUES (seq_enumerate.nextval
DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'BaseStatus';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'BaseStatus', 'ENABLED');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'BaseStatus', 'DISABLED');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'BaseStatus', 'QUALITY');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'BooleanType';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'BooleanType', 'false');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'BooleanType', 'true');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'CheckInventoryType';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'CheckInventoryType', 'BLIND_CHECK');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'CheckInventoryType', 'NORMAL_CHECK');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'WmsWaveDocWorkMode';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsWaveDocWorkMode', 'WORK_BY_DOC');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsWaveDocWorkMode', 'WORK_BY_WAVE');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'Control';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'Control', 'booleanList');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'Control', 'checkBox');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'Control', 'date');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'Control', 'dateRanger');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'Control', 'list');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'Control', 'multiRemote');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'Control', 'numberText');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'Control', 'remote');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'Control', 'text');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'Control', 'textArea');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'FileGrammarType';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'FileGrammarType', 'HQL');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'FileGrammarType', 'SQL');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'FileStyle';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'FileStyle', 'picture');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'FileStyle', 'pictureText');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'FileStyle', 'sql');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'FileStyle', 'text');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'FileStyle', 'REPORT');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'FixedFee';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'FixedFee', 'DAY');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'FixedFee', 'MONTH');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'GlobalParamType';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'GlobalParamType', 'P_BEAN');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'GlobalParamType', 'P_BOOLEAN');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'GlobalParamType', 'P_CHAR');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'GlobalParamType', 'P_DOUBLE');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'GlobalParamType', 'P_INT');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'GlobalParamType', 'P_STRING');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'HQL';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'HQL', 'boolean');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'HQL', 'char');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'HQL', 'double');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'HQL', 'int');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'HQL', 'long');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'HQL', 'String');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'InventoryLockType';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'InventoryLockType', 'COMPANY');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'InventoryLockType', 'INVENTORY');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'InventoryLockType', 'ITEM');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'InventoryLockType', 'LOCATION');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'InventoryLockType', 'LOT');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'InventoryLockType', 'SOI');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'LocalType';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'LocalType', 'en');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'LocalType', 'zh');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'ModelType';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'ModelType', 'origen');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'NumberList';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'NumberList', 'ONE');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'NumberList', 'TWO');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'OperatorType';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'OperatorType', 'BETWEEN');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'OperatorType', 'BOTH');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'OperatorType', 'EQ');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'OperatorType', 'GE');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'OperatorType', 'GT');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'OperatorType', 'IN');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'OperatorType', 'IS_NOT_NULL');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'OperatorType', 'IS_NULL');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'OperatorType', 'LE');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'OperatorType', 'LEFT');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'OperatorType', 'LT');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'OperatorType', 'NOT');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'OperatorType', 'NOT_IN');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'OperatorType', 'RIGHT');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'ParameterType';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'ParameterType', 'binary');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'ParameterType', 'boolean');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'ParameterType', 'char');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'ParameterType', 'date');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'ParameterType', 'double');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'ParameterType', 'int');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'ParameterType', 'integer');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'ParameterType', 'string');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'ParameterType', 'varchar');


DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'ReferenceType';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'ReferenceType', 'string');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'ReferenceType', 'list');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'ReferenceType', 'double');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'ReferenceType', 'integer');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'ReferenceType', 'boolean');


DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'RuleStatus';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'RuleStatus', 'OFF_LINE');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'RuleStatus', 'ON_LINE');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'RuleTableItem';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'RuleTableItem', '1_BIND');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'RuleTableItem', '2_REFERENCE');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'RuleTableItem', '4_VALUE');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'RuleTableVersionStatus';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'RuleTableVersionStatus', 'MODIFY');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'RuleTableVersionStatus', 'ON_LINE');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'RuleTableVersionStatus', 'UNACTIVE');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'SQL';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'SQL', 'binary');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'SQL', 'date');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'SQL', 'integer');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'SQL', 'varchar');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'ThornApproveProcessStatus';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'ThornApproveProcessStatus', 'CLOSE');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'ThornApproveProcessStatus', 'OPEN');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'ThornApproveProcessStatus', 'AGREE');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'ThornApproveProcessStatus', 'REFUSE');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'ThornApproveProcessStatus', 'SUBMIT_APPROVE');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'TypeOfBill';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'TypeOfBill', 'MOVE');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'TypeOfBill', 'RECEIVE');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'TypeOfBill', 'SHIP');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'TypeOfBill', 'QUALITY');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'WmsCountDetailStatus';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsCountDetailStatus', 'COUNT');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsCountDetailStatus', 'FINISHED');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsCountDetailStatus', 'OPEN');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'WmsCountLockType';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsCountLockType', 'LOCK');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsCountLockType', 'UNLOCK');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'WmsCountType';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsCountType', 'ALL');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsCountType', 'ITEM');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsCountType', 'LOCATION');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsCountType', 'LOCATION_EXCEPTION');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsCountType', 'LOCATION_CYCLE');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsCountType', 'LOCATION_MOVED');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsCountType', 'SUPPLY');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'WmsLocationType';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsLocationType', 'COUNT');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsLocationType', 'PROCESS');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsLocationType', 'RECEIVE');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsLocationType', 'SHIP');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsLocationType', 'SPLIT');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsLocationType', 'STORAGE');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsLocationType', 'CROSS_DOCK');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'WmsPickTicketStatus';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsPickTicketStatus', 'OPEN');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsPickTicketStatus', 'CANCELED');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsPickTicketStatus', 'WORKING');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsPickTicketStatus', 'FINISHED');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'WmsQualityResult';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsQualityResult', 'BAD');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsQualityResult', 'OFFGRADE');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'WmsTaskType';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsTaskType', 'MV_PUTAWAY');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsTaskType', 'MV_PICKTICKET_PICKING');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsTaskType', 'MV_WAVE_PICKING');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsTaskType', 'MV_MOVE');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsTaskType', 'MV_REPLENISHMENT_MOVE');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsTaskType', 'MV_PROCESS_PICKING');


DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'WmsMoveDocType';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsMoveDocType', 'MV_PUTAWAY');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsMoveDocType', 'MV_PICKTICKET_PICKING');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsMoveDocType', 'MV_WAVE_PICKING');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsMoveDocType', 'MV_PROCESS_PICKING');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsMoveDocType', 'MV_MOVE');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsMoveDocType', 'MV_REPLENISHMENT_MOVE');


DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'WmsWorkDocStatus';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsWorkDocStatus', 'OPEN');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsWorkDocStatus', 'WORKING');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsWorkDocStatus', 'FINISHED');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'WmsTaskStatus';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsTaskStatus', 'OPEN');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsTaskStatus', 'DISPATCHED');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsTaskStatus', 'WORKING');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsTaskStatus', 'FINISHED');


DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'WmsCountStatus';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsCountStatus', 'OPEN');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsCountStatus', 'ACTIVE');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsCountStatus', 'COUNTING');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsCountStatus', 'FINISHED');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsCountStatus', 'CANCELED');


DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'WmsWaveDocBaseStatus';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsWaveDocBaseStatus', 'OPEN');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsWaveDocBaseStatus', 'WORKING');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsWaveDocBaseStatus', 'FINISHED');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'ThornApproveProcessType';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'ThornApproveProcessType', 'AGREE');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'ThornApproveProcessType', 'REFUSE');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'ThornApproveProcessType', 'SUBMIT_APPROVE');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'WmsCountPlanType';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsCountPlanType', 'NORMAL');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsCountPlanType', 'RECOUNT');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'WmsASNStatus';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsASNStatus', 'OPEN');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsASNStatus', 'ACTIVE');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsASNStatus', 'RECEIVING');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsASNStatus', 'RECEIVED');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsASNStatus', 'CANCELED');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'WmsASNShelvesStauts';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsASNShelvesStauts', 'UNPUTAWAY');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsASNShelvesStauts', 'PUTAWAY');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsASNShelvesStauts', 'FINISHED');


DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'WmsMoveDocShipStatus';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsMoveDocShipStatus', 'UNSHIP');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsMoveDocShipStatus', 'SHIPPED');


DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'WmsInventoryLogType';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsInventoryLogType', 'CANCEL_RECEIVING');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsInventoryLogType', 'RECEIVING');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsInventoryLogType', 'SHIPPING');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsInventoryLogType', 'MOVE');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsInventoryLogType', 'CONVERT_PACKAGEUNIT');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsInventoryLogType', 'INVENTORY_ADJUST');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsInventoryLogType', 'COUNT_ADJUST');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsInventoryLogType', 'PROCESS_DOWN');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsInventoryLogType', 'PROCESS_UP');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsInventoryLogType', 'LOCK');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsInventoryLogType', 'UNLOCK');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsInventoryLogType', 'QUALITY');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsInventoryLogType', 'INVENTORY_EXCEPTION');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'WmsProcessPlanStatus';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsProcessPlanStatus', 'OPEN');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsProcessPlanStatus', 'ENABLED');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsProcessPlanStatus', 'DISABLED');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'WmsMoveDocStatus';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsMoveDocStatus', 'OPEN');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsMoveDocStatus', 'ACTIVE');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsMoveDocStatus', 'ALLOCATED');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsMoveDocStatus', 'PARTALLOCATED');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsMoveDocStatus', 'WORKING');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsMoveDocStatus', 'FINISHED');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'WmsItemStateType';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsItemStateType', 'NORMAL');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsItemStateType', 'RECEIVE');


DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'WmsASNQualityStauts';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsASNQualityStauts', 'NOQUALITY');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsASNQualityStauts', 'UNQUALITY'); 
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsASNQualityStauts', 'QUALITYFINISHED'); 

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'InOrOutType';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'InOrOutType', '-1');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'InOrOutType', '1'); 

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'WmsMasterBOLStatus';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsMasterBOLStatus', 'UNSHIP');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsMasterBOLStatus', 'SHIPPED');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'WmsMoveDocProcessStatus';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsMoveDocProcessStatus', 'UNPROCESS');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsMoveDocProcessStatus', 'PROCESSING');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsMoveDocProcessStatus', 'PROCESSED');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'AsnReceiveType';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'AsnReceiveType', 'RECEIVE_BLIND');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'AsnReceiveType', 'RECEIVE_NORMAL');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'WmsASNPlanStatus';
DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'WmsBookingStatus';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsBookingStatus', 'OPEN');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsBookingStatus', 'EXECUTING');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsBookingStatus', 'FINISH');
--工艺状态
DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'TypeOfExtendPropC1';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'TypeOfExtendPropC1', '-');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'TypeOfExtendPropC1', 'NEWBEST');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'TypeOfExtendPropC1', 'NEW');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'TypeOfExtendPropC1', 'OLD');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'TypeOfExtendPropC1', 'OLDBEST');
--INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'TypeOfExtendPropC1', 'TEST');
--质检单状态
DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'WmsQualityStatus';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsQualityStatus', '-');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsQualityStatus', 'TRUE');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsQualityStatus', 'FALSE');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'FileType';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'FileType', 'REPORT');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'FileType', 'SQL');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'WmsContainerType';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsContainerType', 'BIGBOX');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsContainerType', 'SMALLBOX');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'WmsCarType';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsCarType', 'BOX');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsCarType', 'UNBOX');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'SexType';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'SexType', 'FEMALE');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'SexType', 'MALE');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'WmsLogType';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsLogType', 'ERROR');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsLogType', 'SUCCESS');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsLogType', 'NOTES');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'WmsLogTitle';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsLogTitle', 'CONTAINER_CHANGE');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsLogTitle', 'CONTAINER_RELEASE');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsLogTitle', 'CONTAINER_PICKING');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsLogTitle', 'CONTAINER_BOL');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsLogTitle', 'LOCATION_CHANGE');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsLogTitle', 'CONTAINER_PIC_BACK');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsLogTitle', 'PICK_PRINT');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsLogTitle', 'CONTAINER_PRINT');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsLogTitle', 'CONTAINER_SHIP');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsLogTitle', 'CONTAINER_SCAN');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsLogTitle', 'CONTAINER_SIGN');

DELETE FROM ENUMERATE WHERE ENUM_TYPE = 'WmsBOLStatus';
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsBOLStatus', 'OPEN');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsBOLStatus', 'ISLOAD');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsBOLStatus', 'WORKING');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsBOLStatus', 'FINISHED');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsBOLStatus', 'UNSHIP');
INSERT INTO ENUMERATE  VALUES (seq_enumerate.nextval,'WmsBOLStatus', 'SHIPPED');