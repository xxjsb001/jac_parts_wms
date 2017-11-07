INSERT INTO global_params
   (`ID`, `DISCRIMINATOR`, `PARAM_ID`, `TYPE`, `GROUP_NAME`, `REF_MODEL`, `PARAM_VALUE`, `PARAM_BEAN`, `DESCRIPTION`, `MODULE`, `CREATOR_ID`, `CREATOR`, `CREATED_TIME`, `LAST_OPERATOR_ID`, `LAST_OPERATOR`, `UPDATE_TIME`)
VALUES
   (1, 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'tempFileDir', 'P_STRING', '临时文件路径配置', 'origen', 'd:/temp/tempFile', '', '上传文件存放的临时文件夹', 'wms', null, '', null,null , '', null);

INSERT INTO global_params
   (`ID`, `DISCRIMINATOR`, `PARAM_ID`, `TYPE`, `GROUP_NAME`, `REF_MODEL`, `PARAM_VALUE`, `PARAM_BEAN`, `DESCRIPTION`, `MODULE`, `CREATOR_ID`, `CREATOR`, `CREATED_TIME`, `LAST_OPERATOR_ID`, `LAST_OPERATOR`, `UPDATE_TIME`)
VALUES
   (2, 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'imageFileDir', 'P_STRING', '临时文件路径配置', 'origen', 'd:/temp/imageFile', '', '图片存放的文件夹', 'wms', null, '', null,null , '', null);

INSERT INTO global_params
   (`ID`, `DISCRIMINATOR`, `PARAM_ID`, `TYPE`, `GROUP_NAME`, `REF_MODEL`, `PARAM_VALUE`, `PARAM_BEAN`, `DESCRIPTION`, `MODULE`, `CREATOR_ID`, `CREATOR`, `CREATED_TIME`, `LAST_OPERATOR_ID`, `LAST_OPERATOR`, `UPDATE_TIME`)
VALUES
   (3, 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'otherFileDir', 'P_STRING', '临时文件路径配置', 'origen', 'd:/temp/otherFile', '', '需要永久存放的非图片文件的文件夹', 'wms', null, '', null,null , '', null);

INSERT INTO global_params
   (`ID`, `DISCRIMINATOR`, `PARAM_ID`, `TYPE`, `GROUP_NAME`, `REF_MODEL`, `PARAM_VALUE`, `PARAM_BEAN`, `DESCRIPTION`, `MODULE`, `CREATOR_ID`, `CREATOR`, `CREATED_TIME`, `LAST_OPERATOR_ID`, `LAST_OPERATOR`, `UPDATE_TIME`)
VALUES
   (6, 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'maintainTableDecimal', 'P_INT', '页面布局设置', 'origen', '6', '', '维护屏二维表格如果是小数需要保留的小数位数', 'wms', null, '', null,null , '', null);

INSERT INTO global_params
   (`ID`, `DISCRIMINATOR`, `PARAM_ID`, `TYPE`, `GROUP_NAME`, `REF_MODEL`, `PARAM_VALUE`, `PARAM_BEAN`, `DESCRIPTION`, `MODULE`, `CREATOR_ID`, `CREATOR`, `CREATED_TIME`, `LAST_OPERATOR_ID`, `LAST_OPERATOR`, `UPDATE_TIME`)
VALUES
   (8, 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'IS_ASH', 'P_BOOLEAN', '页面布局设置', 'origen', 'true', '', '右键弹出菜单不可用时变灰或不可见(true－变灰 false－不可见)', 'wms', null, '', null,null , '', null);

INSERT INTO global_params
   (`ID`, `DISCRIMINATOR`, `PARAM_ID`, `TYPE`, `GROUP_NAME`, `REF_MODEL`, `PARAM_VALUE`, `PARAM_BEAN`, `DESCRIPTION`, `MODULE`, `CREATOR_ID`, `CREATOR`, `CREATED_TIME`, `LAST_OPERATOR_ID`, `LAST_OPERATOR`, `UPDATE_TIME`)
VALUES
   (9, 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'ACK_CLOSEWINDOW', 'P_STRING', '页面布局设置', 'origen', 'ALT-X', '', '弹出窗口关闭热键', 'wms', null, '', null,null , '', null);

INSERT INTO global_params
   (`ID`, `DISCRIMINATOR`, `PARAM_ID`, `TYPE`, `GROUP_NAME`, `REF_MODEL`, `PARAM_VALUE`, `PARAM_BEAN`, `DESCRIPTION`, `MODULE`, `CREATOR_ID`, `CREATOR`, `CREATED_TIME`, `LAST_OPERATOR_ID`, `LAST_OPERATOR`, `UPDATE_TIME`)
VALUES
   (10, 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'reportURL', 'P_STRING', '报表服务器配置', 'origen', 'reportJsp/showReport.jsp', '', '报表服务器URL地址', 'wms', null, '', null,null , '', null);

INSERT INTO global_params
   (`ID`, `DISCRIMINATOR`, `PARAM_ID`, `TYPE`, `GROUP_NAME`, `REF_MODEL`, `PARAM_VALUE`, `PARAM_BEAN`, `DESCRIPTION`, `MODULE`, `CREATOR_ID`, `CREATOR`, `CREATED_TIME`, `LAST_OPERATOR_ID`, `LAST_OPERATOR`, `UPDATE_TIME`)
VALUES
   (11, 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'clientUserInfo', 'P_BEAN', '全局变量设置', 'origen', '', 'userInfoInClient', '前台用户登陆时显示的用户信息（如：当前用户：admin）', 'wms', null, '', null,null , '', null);

INSERT INTO global_params
   (`ID`, `DISCRIMINATOR`, `PARAM_ID`, `TYPE`, `GROUP_NAME`, `REF_MODEL`, `PARAM_VALUE`, `PARAM_BEAN`, `DESCRIPTION`, `MODULE`, `CREATOR_ID`, `CREATOR`, `CREATED_TIME`, `LAST_OPERATOR_ID`, `LAST_OPERATOR`, `UPDATE_TIME`)
VALUES
   (12, 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'round', 'P_BOOLEAN', '页面布局设置', 'origen', 'true', '', '小数位数处理时是否四舍五入(默认四舍五入)', 'wms', null, '', null,null , '', null);

INSERT INTO global_params
   (`ID`, `DISCRIMINATOR`, `PARAM_ID`, `TYPE`, `GROUP_NAME`, `REF_MODEL`, `PARAM_VALUE`, `PARAM_BEAN`, `DESCRIPTION`, `MODULE`, `CREATOR_ID`, `CREATOR`, `CREATED_TIME`, `LAST_OPERATOR_ID`, `LAST_OPERATOR`, `UPDATE_TIME`)
VALUES
   (13, 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'imageSource', 'P_STRING', '临时文件路径配置', 'origen', 'd:/temp/image/source', '', '影像归档图片源文件夹', 'wms', null, '', null,null , '', null);

INSERT INTO global_params
   (`ID`, `DISCRIMINATOR`, `PARAM_ID`, `TYPE`, `GROUP_NAME`, `REF_MODEL`, `PARAM_VALUE`, `PARAM_BEAN`, `DESCRIPTION`, `MODULE`, `CREATOR_ID`, `CREATOR`, `CREATED_TIME`, `LAST_OPERATOR_ID`, `LAST_OPERATOR`, `UPDATE_TIME`)
VALUES
   (14, 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'imageTarget', 'P_STRING', '临时文件路径配置', 'origen', 'd:/temp/image/target', '', '影像归档图片目标文件夹', 'wms', null, '', null,null , '', null);

INSERT INTO global_params
   (`ID`, `DISCRIMINATOR`, `PARAM_ID`, `TYPE`, `GROUP_NAME`, `REF_MODEL`, `PARAM_VALUE`, `PARAM_BEAN`, `DESCRIPTION`, `MODULE`, `CREATOR_ID`, `CREATOR`, `CREATED_TIME`, `LAST_OPERATOR_ID`, `LAST_OPERATOR`, `UPDATE_TIME`)
VALUES
   (15, 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'welcomepage', 'P_STRING', '全局变量设置', 'origen', '', '', '默认欢迎页面', 'wms', null, '', null,null , '', null);

INSERT INTO global_params
   (`ID`, `DISCRIMINATOR`, `PARAM_ID`, `TYPE`, `GROUP_NAME`, `REF_MODEL`, `PARAM_VALUE`, `PARAM_BEAN`, `DESCRIPTION`, `MODULE`, `CREATOR_ID`, `CREATOR`, `CREATED_TIME`, `LAST_OPERATOR_ID`, `LAST_OPERATOR`, `UPDATE_TIME`)
VALUES
   (16, 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'IMPORT_REPLACE', 'P_STRING', '全局变量设置', 'origen', 'false', '', '导入业务数据时是否替换已存在业务数据(true-替换,false-不替换)', 'wms', null, '', null,null , '', null);

INSERT INTO global_params
   (`ID`, `DISCRIMINATOR`, `PARAM_ID`, `TYPE`, `GROUP_NAME`, `REF_MODEL`, `PARAM_VALUE`, `PARAM_BEAN`, `DESCRIPTION`, `MODULE`, `CREATOR_ID`, `CREATOR`, `CREATED_TIME`, `LAST_OPERATOR_ID`, `LAST_OPERATOR`, `UPDATE_TIME`)
VALUES
   (17, 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'SESSION_USERS', 'P_BEAN', '全局变量设置', 'origen', '', 'userSession', '当前用户全局变量', 'wms', null, '', null,null , '', null);

INSERT INTO global_params
   (`ID`, `DISCRIMINATOR`, `PARAM_ID`, `TYPE`, `GROUP_NAME`, `REF_MODEL`, `PARAM_VALUE`, `PARAM_BEAN`, `DESCRIPTION`, `MODULE`, `CREATOR_ID`, `CREATOR`, `CREATED_TIME`, `LAST_OPERATOR_ID`, `LAST_OPERATOR`, `UPDATE_TIME`)
VALUES
   (18, 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'SESSION_WAREHOUSE', 'P_BEAN', '全局变量设置', 'origen', '', 'warehouseSession', '记录用户当前登录的仓库全局变量', 'wms', null, '', null,null , '', null);

INSERT INTO global_params
   (`ID`, `DISCRIMINATOR`, `PARAM_ID`, `TYPE`, `GROUP_NAME`, `REF_MODEL`, `PARAM_VALUE`, `PARAM_BEAN`, `DESCRIPTION`, `MODULE`, `CREATOR_ID`, `CREATOR`, `CREATED_TIME`, `LAST_OPERATOR_ID`, `LAST_OPERATOR`, `UPDATE_TIME`)
VALUES
   (19, 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'clientOtherInfo', 'P_BEAN', '全局变量设置', 'origen', '', 'warehouseInfoInClient', '记录用户当前登录的仓库信息', 'wms', null, '', null,null , '', null);

INSERT INTO global_params
   (`ID`, `DISCRIMINATOR`, `PARAM_ID`, `TYPE`, `GROUP_NAME`, `REF_MODEL`, `PARAM_VALUE`, `PARAM_BEAN`, `DESCRIPTION`, `MODULE`, `CREATOR_ID`, `CREATOR`, `CREATED_TIME`, `LAST_OPERATOR_ID`, `LAST_OPERATOR`, `UPDATE_TIME`)
VALUES
   (20, 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'AUTO_REFRESH', 'P_INT', '全局变量设置', 'origen', '30', '', '定义页面数据刷新最小时间间隔', 'wms', null, '', null,null , '', null);

INSERT INTO global_params
   (`ID`, `DISCRIMINATOR`, `PARAM_ID`, `TYPE`, `GROUP_NAME`, `REF_MODEL`, `PARAM_VALUE`, `PARAM_BEAN`, `DESCRIPTION`, `MODULE`, `CREATOR_ID`, `CREATOR`, `CREATED_TIME`, `LAST_OPERATOR_ID`, `LAST_OPERATOR`, `UPDATE_TIME`)
VALUES
   (21, 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'SAVE_SYSTEM_LOG', 'P_STRING', '全局变量设置', 'origen', 'false', '', '是否开启保存操作日志', 'wms', null, '', null,null , '', null);


INSERT INTO global_params
   (`ID`, `DISCRIMINATOR`, `PARAM_ID`, `TYPE`, `GROUP_NAME`, `REF_MODEL`, `PARAM_VALUE`, `PARAM_BEAN`, `DESCRIPTION`, `MODULE`, `CREATOR_ID`, `CREATOR`, `CREATED_TIME`, `LAST_OPERATOR_ID`, `LAST_OPERATOR`, `UPDATE_TIME`)
VALUES
   (22, 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'EXPORT_SUPPORT', 'P_STRING', '全局变量设置', 'origen', 'false', '', '标准表格页面是否支持导出,刷新', 'wms', null, '', null,null , '', null);
   
INSERT INTO global_params
   (`ID`, `DISCRIMINATOR`, `PARAM_ID`, `TYPE`, `GROUP_NAME`, `REF_MODEL`, `PARAM_VALUE`, `PARAM_BEAN`, `DESCRIPTION`, `MODULE`, `CREATOR_ID`, `CREATOR`, `CREATED_TIME`, `LAST_OPERATOR_ID`, `LAST_OPERATOR`, `UPDATE_TIME`)
VALUES
   (23, 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'QUERY_THROUGHPUT_RECORD', 'P_INT', '全局变量设置', 'origen', '2', '', '表格查询吞吐量统计,耗时超过此值时记录(单位秒)', 'wms', null, '', null,null , '', null);
   
   
INSERT INTO global_params
   (`ID`, `DISCRIMINATOR`, `PARAM_ID`, `TYPE`, `GROUP_NAME`, `REF_MODEL`, `PARAM_VALUE`, `PARAM_BEAN`, `DESCRIPTION`, `MODULE`, `CREATOR_ID`, `CREATOR`, `CREATED_TIME`, `LAST_OPERATOR_ID`, `LAST_OPERATOR`, `UPDATE_TIME`)
VALUES
   (24, 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'BE_SYNC_MESSAGE', 'P_STRING', '全局变量设置', 'origen', 'false', '', '异步消息同步转换处理,false异步,true同步', 'wms', null, '', null,null , '', null);
   
   INSERT INTO global_params
   (`ID`, `DISCRIMINATOR`, `PARAM_ID`, `TYPE`, `GROUP_NAME`, `REF_MODEL`, `PARAM_VALUE`, `PARAM_BEAN`, `DESCRIPTION`, `MODULE`, `CREATOR_ID`, `CREATOR`, `CREATED_TIME`, `LAST_OPERATOR_ID`, `LAST_OPERATOR`, `UPDATE_TIME`)
VALUES
   (25, 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'w_bg_image_url', 'P_STRING', '仓库布局图URL配置', 'origen', 'images/inventoryview/wms_bg.jpg', '', '仓库布局图URL配置', 'wms', null, '', null,null , '', null);
   ---jac
   INSERT INTO global_params
   (ID, DISCRIMINATOR, PARAM_ID, TYPE, GROUP_NAME, REF_MODEL, PARAM_VALUE, PARAM_BEAN, DESCRIPTION, MODULE, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (SEQ_ENUMERATE.NEXTVAL, 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'rf_reportURL', 'P_STRING', 'RF报表URL配置', 'origen', 'http://localhost:8088/jac_fdj_wms', '', '报RF报表直接全局URL配置', 'wms', null, '', null,null , '', null);
   ---jac
    INSERT INTO global_params
   (ID, DISCRIMINATOR, PARAM_ID, TYPE, GROUP_NAME, REF_MODEL, PARAM_VALUE, PARAM_BEAN, DESCRIPTION, MODULE, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (SEQ_ENUMERATE.NEXTVAL, 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'rijie_sys', 'P_STRING', '日结仓库ID取值', 'origen', '1', '', '日结仓库ID取值,后续增加用#分割', 'wms', null, '', null,null , '', null);
---jac
   INSERT INTO global_params
   (ID, DISCRIMINATOR, PARAM_ID, TYPE, GROUP_NAME, REF_MODEL, PARAM_VALUE, PARAM_BEAN, DESCRIPTION, MODULE, CREATOR_ID, 
CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (SEQ_ENUMERATE.NEXTVAL, 'com.vtradex.thorn.server.config.globalparam.GlobalParam', 'rule_rf', 'P_STRING', '规则上线RF同步URL', 'origen', 'http://localhost:8082/jac_fdj_wms/rf', '', '
规则上线RF同步URL', 'wms', null, '', null,null , '', null);