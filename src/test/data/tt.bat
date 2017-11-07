@ECHO OFF 
cd /d D:\testJDK\com\vtradex\wms\server\utils
start "标签打印服务窗口" "cmd /k "java -cp .;D:\testJDK\lib\report4.jar;D:\testJDK\lib\rt.jar;D:\testJDK\lib\javax.servlet.jar;D:\testJDK\lib\ant-1.6.2.jar VtradexDirectPrintJob


