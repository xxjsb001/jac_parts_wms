import java.awt.print.PageFormat;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.StringTokenizer;

import javax.print.PrintService;

import org.apache.tools.ant.util.FileUtils;

import com.runqian.report4.view.applet.AReport;

public class VtradexDirectPrintJob {
	public static SimpleDateFormat dmy_hms = 
			new SimpleDateFormat("dd-MM-yyyy hh:mm:ss");
	private static final String BAK = "bak";
	public static void main(String[] args) {
		while(true){
			System.out.println(dmy_hms.format(new Date())+"----------01");
			//=-=-=-=-=-=-=-=-=-=-=-=-=-=-
			String filePath = thisTools.markDir("D:/logs/biaoqian");
			thisTools.markTxt("initMethod:", filePath+thisTools.LOG);//写日志
			String logPath = thisTools.markDir(filePath + File.separator + BAK);//D:/logs/biaoqian/bak
			//=-=-=-=-=-=-=-=-=-=-=-=-=-=-
			String appRoot = null,reportName=null,reportParams=null,printServiceName=null;
			//getGlobalParams(D:/testJDK/globleParams.txt)
			//appRoot=http://192.168.11.137:8077/jac_fdj_wms#reportName=wmsPalletSerial.raq#printServiceName=Ultra PDF
			File globalFile = new File(thisTools.globalFile);
			if(!globalFile.exists()){
				System.out.println(dmy_hms.format(new Date())+"---"+thisTools.globalFile+" is not exists");
				try {
					//1000=1s,1h=60m,1m=60s,10800000=3h,3600000=1h,2min=120000,1min=60000
					Thread.sleep(300000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				continue;
			}
			Map<Integer,List<Object>> globalMap = thisTools.readTxt(globalFile);
			Iterator<Entry<Integer, List<Object>>> gg = globalMap.entrySet().iterator();
			while(gg.hasNext()){
				Entry<Integer, List<Object>> entry = gg.next();
				List<Object> values = entry.getValue();
				for(Object o : values){
					String[] s = o.toString().split("#");
					for(String ss : s){
						String[] sss = ss.split("=");
						if(sss[0].equals("appRoot")){
							appRoot = sss[1];
						}else if(sss[0].equals("reportName")){
							reportName = sss[1];
						}else if(sss[0].equals("printServiceName")){
							printServiceName = sss[1];
						}
					}
				}
			}
			File srcDir = new File(filePath);
			if (srcDir.exists()) {
				File[] files = srcDir.listFiles();
				if (files != null && files.length > 0) {
					for (int i = 0; i < files.length; i++) {
						if (files[i].isFile()) {
							try {
								File ff = files[i];
								if(ff.getName().contains("asnFdj")){
									Map<Integer,List<Object>> map = thisTools.readTxt(ff);
									Iterator<Entry<Integer, List<Object>>> ii = map.entrySet().iterator();
									while(ii.hasNext()){
										Entry<Integer, List<Object>> entry = ii.next();
										reportParams="id=0;ids="+entry.getValue().toString();//id=0;ids=[172, 173]
										VtradexDirectPrintJobJava a = new VtradexDirectPrintJobJava(appRoot, reportName, reportParams,printServiceName);
										a.print();
									}
									File destFile = new File(logPath + File.separator + ff.getName());//D:/logs/biaoqian/bak/asnFdj-001.txt
									FileUtils.newFileUtils().copyFile(ff, destFile);
									files[i].delete();
									
									try {
										//1000=1s,1h=60m,1m=60s,10800000=3h,3600000=1h,2min=120000,1min=60000
										Thread.sleep(2000);
									} catch (InterruptedException e) {
										e.printStackTrace();
									}
								}
							} catch (Exception e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
			System.out.println(dmy_hms.format(new Date())+"----------02");
			try {
				//1000=1s,1h=60m,1m=60s,10800000=3h,3600000=1h,2min=120000,1min=60000
				Thread.sleep(10000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
    }
}
class thisTools{
	public static String enter = "\r\n";
	public static final String LOG = "/log.txt";
	public static final String globalFile = "D:/vtradex_printer/globleParams.txt";//vtradex_printer testJDK
	public static void markTxt(String message,String txtPath){
		List<Object[]> values = new ArrayList<Object[]>();
		values.add(new Object[]{
				message+thisTools.format(new Date(), "yyyy-MM-dd HH:mm:ss")
		});
		thisTools.createTxt(values, 
				values.size(), txtPath, thisTools.enter, "\t", "utf-8");
	}
	public static String format(Date date,String format) {
		DateFormat df = new SimpleDateFormat(format);
		return df.format(date);
	}
	public static String markDir(String filePath){
    	File shipmentFile = new File(filePath);
    	if(!shipmentFile.exists()){
    		shipmentFile.mkdir();
    	}
		return filePath;
    }
	public static void createTxt(List<Object[]> values,int objSize,String file
    		,String endLine,String spilt,String character){
    	StringBuffer row=null;
    	OutputStreamWriter osw = null;
    	try {
    		osw = new OutputStreamWriter(new FileOutputStream(file,true),character);
    		for(int i=0 ; i<values.size() ; i++){
    			row = new StringBuffer();
    			Object[] obj = values.get(i);
    			for(int j = 0 ; j<objSize ; j++){
    				if(objSize-j == 1){
    					row.append(obj[j]+endLine);//换行
    				}else{
    					row.append(obj[j]+spilt);//"\t":tab键
    				}
    			}
    			osw.write(row.toString());
    		}
    		values.clear();
		} catch (Exception e) {
			e.printStackTrace();
		}finally{
       	 if(osw!=null)//if(osw!=null)
             try {
             	osw.close();//osw.close();
             } catch (IOException e) {
                 e.printStackTrace();
             }
      }
    }
	public static Map<Integer,List<Object>>  readTxt(File file){
    	//jt.readTxt(new File("C:/Users/Administrator/Desktop/aaaaa.txt"));
    	BufferedReader br = null;
    	Map<Integer,List<Object>> map = new HashMap<Integer, List<Object>>();
    	List<Object> values = null;
    	int key = 1;
    	try {
    		InputStreamReader read = new InputStreamReader(new FileInputStream(file), "GBK");
    		br = new BufferedReader(read);
    		String temp = null;
    		while ((temp = br.readLine()) != null) {
    			if("".equals(temp)){
    				continue;
    			}
    			values = new ArrayList<Object>();
    			StringTokenizer st = new StringTokenizer(temp);
    			while(st.hasMoreElements()){
    				String num = st.nextToken("\t").trim();
    				values.add(num);
    			}
    			map.put(key, values);
    			key++;
    		}
		} catch (Exception e) {
			e.printStackTrace();
		}finally {
			try {
				if(null != br){
					br.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return map;
    }
	public static void deleteFile(File file){ 
	   if(file.exists()){                         
		    if(file.isFile()){
		        file.delete();                     
		    }else if(file.isDirectory()){
		        File files[] = file.listFiles();          
		        for(int i=0;i<files.length;i++){           
		            deleteFile(files[i]);             
		        } 
		    } 
		    file.delete(); 
	   }
	}
}
class VtradexDirectPrintJobJava {
    private static final long serialVersionUID = 1L;

    private String appRoot;

    private String dataServlet;

    private String reportName;

    private String type;

    private String reportParams;

    private String cacheId;

    private String needPrintPrompt;

    private String needSelectPrinter;

    private String needSetPageSize;

    private String paramCharset;

    private String savePrintSetup;

    private String printServiceName;
    
    public VtradexDirectPrintJobJava(String appRoot,String reportName,String reportParams,String printServiceName) {
        init(appRoot,reportName,reportParams,printServiceName);
    }

    public void init(String appRoot,String reportName,String reportParams,String printServiceName) {

        this.appRoot = appRoot;//"http://localhost:8088/jac_fdj_wms";//http://192.168.10.92:8078/jac_ht_wms
        this.reportName = reportName;//"wmsPalletSerial.raq";//wmsTrayTag.raq
        this.reportParams = reportParams;//"id=0;ids=[43,44,45]";//230972, 230973
        this.printServiceName = printServiceName;
        
        this.dataServlet = "/reportServlet?action=1";
        this.type = "file";
        this.cacheId = null;
        this.needPrintPrompt = "no";
        this.needSelectPrinter = "no";
        this.savePrintSetup = "no";
        this.needSetPageSize = "no";
        this.paramCharset = "GBK";
    }

    public void print() {
        String s = null;
        this.reportName = this.reportName.trim();
        PrinterJob job = PrinterJob.getPrinterJob();
        PrintService[] pss = PrinterJob.lookupPrintServices();
        //=-=-=-=-=-=-=-=-=-=-=-=-=-=-
        String filePath = thisTools.markDir("D:/logs/biaoqian");
        thisTools.markTxt("metnod001:", filePath+thisTools.LOG);
		//=-=-=-=-=-=-=-=-=-=-=-=-=-=-
        Boolean isMatchprintService = false;
        if (printServiceName != null && !"".equals(printServiceName)) {
            for (PrintService ps : pss) {
                if (ps.getName().equals(printServiceName) || ps.getName().contains(printServiceName)) {
                    try {
                        job.setPrintService(ps);
                        isMatchprintService = true;
                        break;
                    }
                    catch (PrinterException e) {
                    }
                }
            }
        }
        //=-=-=-=-=-=-=-=-=-=-=-=-=-=-
        thisTools.markTxt("metnod002:", filePath+thisTools.LOG);
  		//=-=-=-=-=-=-=-=-=-=-=-=-=-=-
        if(!isMatchprintService){
        	printServiceName = null;
        }
        boolean bsavePrintSetup = "yes".equalsIgnoreCase(this.savePrintSetup);
        PageFormat outerPf = null;
        int count = 1;
        if (this.reportName.startsWith("{")) {
            int index = 0;
            int len = this.reportName.length();
            while (index < len) {
                index = this.reportName.indexOf("{", index);
                if (index < 0)
                    break;
                int end = this.reportName.indexOf("}", index);
                s = this.reportName.substring(index + 1, end).trim();
                index = end + 1;
                String name = s;
                String paramString = null;
                int pos = s.indexOf("(");
                if (pos > 0) {
                    paramString = s.substring(pos + 1, s.length() - 1).trim();
                    name = s.substring(0, pos).trim();
                }
                if ((!name.toLowerCase().endsWith(".raq")) && (!name.toLowerCase().endsWith(".rat")))
                    name = name + ".raq";
                AReport report = new AReport();
                report.appRoot = this.appRoot;
                report.cachedId = this.cacheId;
                report.dataServlet = this.dataServlet;
                report.fileName = name;
                if (paramString != null) {
                    report.paramString = paramString;
                    report.paramEncode = this.paramCharset;
                }
                report.srcType = this.type;
                report.job = job;
                report.savePrintSetup = bsavePrintSetup;
                report.outerPf = outerPf;
                if ((count == 1) && (this.needSetPageSize.equalsIgnoreCase("yes"))) {
                    report.bSetPageSize = true;
                    outerPf = report.print();
                }
                else {
                    report.print();
                }
                count++;
            }
        }
        else {
            StringTokenizer st = new StringTokenizer(this.reportName, ",");
            while (st.hasMoreTokens()) {
                String name = st.nextToken().trim();
                if ((!name.toLowerCase().endsWith(".raq")) && (!name.toLowerCase().endsWith(".rat")))
                    name = name + ".raq";
                AReport report = new AReport();
                report.appRoot = this.appRoot;
                report.cachedId = this.cacheId;
                report.dataServlet = this.dataServlet;
                report.fileName = name;
                report.paramString = this.reportParams;
                report.paramEncode = this.paramCharset;
                report.srcType = this.type;
                report.job = job;
                report.savePrintSetup = bsavePrintSetup;
                report.outerPf = outerPf;
                //=-=-=-=-=-=-=-=-=-=-=-=-=-=-
                thisTools.markTxt("metnod003:", filePath+thisTools.LOG);
          		//=-=-=-=-=-=-=-=-=-=-=-=-=-=-
                if ((count == 1) && (this.needSetPageSize.equalsIgnoreCase("yes"))) {
                    report.bSetPageSize = true;
                    outerPf = report.print();
                }
                else {
                    report.print();
                }
                count++;
            }
        }
    }
}
