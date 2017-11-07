@echo off
java -Xmx512M -cp "%~dp0\src\main\java;%~dp0\target\jac_parts_wms\WEB-INF\classes;%MAVEN_REPO%\thorn4\jars\thorn4-client-4.1.4.4.jar;%MAVEN_REPO%\thorn4\jars\thorn4-rule-4.1.4.4.jar;%MAVEN_REPO%\thorn4\jars\thorn4-rmc-4.1.4.4.jar;%MAVEN_REPO%\gwt\jars\gwt-user-1.5.3.jar;%MAVEN_REPO%\gwt\jars\gwt-dev-windows-1.5.3.jar;%MAVEN_REPO%\gxt\jars\gwtext-2.0.5.jar;%MAVEN_REPO%\gwt-crypto\jars\gwt-crypto-1.0.3.jar;%MAVEN_REPO%\gwt\jars\gwt-voices-1.5.5.jar" com.google.gwt.dev.GWTCompiler -out "%~dp0\www" %* com.vtradex.wms.WMS
if ERRORLEVEL 1 goto error
if ERRORLEVEL 0 goto exit
:error
pause
:exit