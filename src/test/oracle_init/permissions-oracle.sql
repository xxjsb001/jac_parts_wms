INSERT INTO ROLES VALUES ('1', 'SUPERVISOR', '系统管理员', 'T',1,'admin',null,null,null,null);

INSERT INTO GROUPS VALUES ('1', 'SYSMAN', '系统管理员组',1,'admin',null,null,null,null);

INSERT INTO GROUP_ROLE VALUES ('1', '1');

INSERT INTO users VALUES ('1', 'com.vtradex.thorn.server.model.security.User', 'admin', 'admin', 'd033e22ae348aeb5660fc2140aec35850c4da997', null, 'ACTIVE', 'zh', null, null, 'origen', null, null, null, null, null, null, null, null, null, '', null, null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO users VALUES ('2', 'com.vtradex.thorn.server.model.security.User', 'test', 'test', 'a94a8fe5ccb19ba61c4c0873d391e987982fbbd3', '', 'ACTIVE', 'zh', null, null, 'origen', null, null, null, null, null, '', null, null, null, '', null, null, null, null, null, null, null, null, null, null, null, null, null, null);
INSERT INTO users VALUES ('3', 'com.vtradex.thorn.server.model.security.User', 't1', 't1', 'e5353879bd69bfddcb465dad176ff52db8319d6f', '', 'ACTIVE', 'zh', null, null, 'origen', null, null, null, null, null, '', null, null, null, '', null, null, null, null, null, null, null, null, null, null, null, null, null, null);

INSERT INTO GROUP_USER VALUES ('1', '1');

INSERT INTO ROLE_USER VALUES ('1', '1');


INSERT INTO wms_warehouse VALUES (1, 'com.vtradex.wms.server.model.warehouse.WmsWarehouse', 'JAC_FDJ', '发动机库', NULL, NULL, NULL, '', '', '', NULL, '', '', '', 'images/inventoryview/warehouse_2.jpg', 668, 202, '', 'ENABLED', 2, 'test', '', 2, 'test', '');


INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (46, 'page', 'versionProcess.delete', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (47, 'page', 'thornApproveProcess.approval', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (48, 'page', 'prettifyRuleTableProcess.new', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (49, 'page', 'thornOrganizationProcess.new', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (50, 'page', 'maintainThornApprovePage', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (51, 'page', 'versionProcess.new', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (52, 'page', 'thornOrganizationProcess.modify', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (53, 'page', 'exceptionProcess.modify', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (54, 'page', 'ruleProcess.deleteDataSource', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (55, 'page', 'versionProcess.upLine', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (56, 'page', 'roleProcess.delete', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (57, 'page', 'ruleExceptionProcess.view', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (58, 'page', 'reportProcess.modify', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (59, 'page', 'groupRuleTableProcess.new', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (60, 'page', 'exceptionProcess.view', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (61, 'page', 'ruleProcess.downLine', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (62, 'page', 'importBOPage', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (63, 'page', 'BOProcess.new', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (64, 'page', 'ruleTableProcess.delete', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (65, 'page', 'welcomeProcess.modify', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (66, 'page', 'ruleProcess.directorySave', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (67, 'page', 'maintainImportRecordPage', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (68, 'page', 'reportProcess.delete', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (69, 'page', 'maintainBacklogPage', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (70, 'page', 'maintainBOPage', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (71, 'page', 'maintainReportConfigPage', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (72, 'page', 'versionProcess.export', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (73, 'page', 'ruleProcess.feeRuleSave', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (74, 'page', 'roleProcess.add', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (75, 'page', 'thornJobProcess.addJobUser', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (76, 'page', 'userProcess.unActive', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (77, 'page', 'maintainIGPPage', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (78, 'page', 'roleProcess.reportPermission', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (79, 'page', 'maintainExceptionLogPage', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (80, 'page', 'thornJobProcess.modify', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (81, 'page', 'ruleProcess.dataSourceSave', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (82, 'page', 'prettifyRuleTableProcess.delete', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (83, 'page', 'reportProcess.unActive', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (84, 'page', 'versionProcess.modify', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (85, 'page', 'ruleTableProcess.setFeference', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (86, 'page', 'IGPProcess.modify', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (87, 'page', 'maintainThornJobPage', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (88, 'page', 'roleProcess.unActive', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (89, 'page', 'reportProcess.colInput', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (90, 'page', 'maintainWelcomePage', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (91, 'page', 'welcomeProcess.new', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (92, 'page', 'ruleProcess.newFeeRuleItem', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (93, 'page', 'groupProcess.delete', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (94, 'page', 'reportProcess.down', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (95, 'page', 'thornJobProcess.removeUser', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (96, 'page', 'reportProcess.export', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (97, 'page', 'roleProcess.new', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (98, 'page', 'reportProcess.add', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (99, 'page', 'reportProcess.paramInput', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (100, 'page', 'userProcess.modify', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (101, 'page', 'maintainGroupPage', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (102, 'page', 'BOProcess.import', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (103, 'page', 'thornApproveProcess.viewLog', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (104, 'page', 'importRecordProcess.download', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (105, 'page', 'customerReportFilePage', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (106, 'page', 'maintainRuleTableVersionPage', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (107, 'page', 'maintainPrettifyRuleTablePage', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (108, 'page', 'groupRuleTableProcess.modify', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (109, 'page', 'groupProcess.add', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (110, 'page', 'prettifyRuleTableProcess.search', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (111, 'page', 'ruleProcess.newDirectory', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (112, 'page', 'thornApproveProcess.agree', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (113, 'page', 'ruleTableProcess.modify', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (114, 'page', 'maintainRulePage', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (115, 'page', 'maintainRuleExceptionLogPage', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (116, 'page', 'groupProcess.new', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (117, 'page', 'thornOrganizationProcess.delete', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (118, 'page', 'ruleProcess.deleteDirectory', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (119, 'page', 'maintainGroupRuleTablePage', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (120, 'page', 'groupProcess.modify', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (121, 'page', 'versionProcess.import', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (122, 'page', 'approveRecordPage', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (123, 'page', 'userProcess.add', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (124, 'page', 'prettifyRuleTableProcess.modify', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (125, 'page', 'exceptionProcess.new', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (126, 'page', 'ruleProcess.export', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (127, 'page', 'groupRuleTableProcess.delete', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (128, 'page', 'userProcess.new', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (129, 'page', 'thornApproveProcess.refuse', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (130, 'page', 'maintainThornOrganizationPage', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (131, 'page', 'IGPProcess.new', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (132, 'page', 'reportProcess.up', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (133, 'page', 'ruleProcess.onLine', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (134, 'page', 'groupRuleTableProcess.import', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (135, 'page', 'thornJobProcess.delete', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (136, 'page', 'welcomeProcess.delete', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (137, 'page', 'ruleTableProcess.new', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (138, 'page', 'groupProcess.unActive', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (139, 'page', 'reportProcess.import', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (140, 'page', 'thornApproveProcess.new', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (141, 'page', 'roleProcess.modify', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (142, 'page', 'reportProcess.new', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (143, 'page', 'reportProcess.view', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (144, 'page', 'thornApproveProcess.delete', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (145, 'page', 'ruleProcess.import', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (146, 'page', 'maintainRuleTablePage', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (147, 'page', 'BOProcess.delete', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (148, 'page', 'groupRuleTableProcess.export', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (149, 'page', 'roleProcess.dataPermission', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (150, 'page', 'maintainRolePage', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (151, 'page', 'ruleProcess.deleteFeeRule', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (152, 'page', 'roleProcess.menuPermission', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (153, 'page', 'editUsersPage', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (154, 'page', 'BOProcess.modify', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (155, 'page', 'ApproveProcessPage', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (156, 'page', 'thornJobProcess.new', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (157, 'page', 'prettifyRuleTableProcess.save', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (158, 'page', 'reportProcess.active', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (159, 'page', 'userProcess.delete', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (160, 'page', 'maintainUserPage', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (161, 'page', 'thornApproveProcess.modify', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (162, 'page', 'groupProcess.active', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (163, 'page', 'versionProcess.editRuleTableDetail', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (164, 'page', 'userProcess.active', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (165, 'page', 'versionProcess.unActive', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (166, 'page', 'roleProcess.active', 'origen', 1, null,null , null, null, null, null, null);

INSERT INTO permissions
   (ID, DISCRIMINATOR, ELEMENT_ID, REF_MODEL, ROLE_ID, EXPRESSION, CREATOR_ID, CREATOR, CREATED_TIME, LAST_OPERATOR_ID, LAST_OPERATOR, UPDATE_TIME)
VALUES
   (167, 'page', 'ruleProcess.newDataSourceItem', 'origen', 1, null,null , null, null, null, null, null);
