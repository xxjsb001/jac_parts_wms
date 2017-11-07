package com.vtradex.rule;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import com.vtradex.rule.server.service.rule.BackRuleManager;
import com.vtradex.wms.WmsTestCase;

/**
 * @author <a href="mailto:yan.li@vtradex.com">李炎</a>
 * @description 
 */

public class ImportRuleTestCase extends WmsTestCase {

	protected BackRuleManager getBackRuleManager(){
		return (BackRuleManager)applicationContext.getBean("backRuleManager");
	}
	public void testImportRule(){
		String path = "H:\\vTradEx\\sony_tms\\规则";
		File file = new File(path);
		File[] listFile = file.listFiles();
		for(File f : listFile){
			String directory = f.getName();
			pasreFile(directory,f);
		}
	}
	
	public static void main(String[] args) {
		String path = "H:\\规则";
		File file = new File(path);
		File[] listFile = file.listFiles();
		for(File f : listFile){
			String directory = f.getName();
			new ImportRuleTestCase().pasreFile(directory,f);
		}
	}
	
	protected void pasreFile(String directory,File file){
		File[] files = file.listFiles();
		for(File f : files){
			if (f.isDirectory()) {
				pasreFile(directory,f);
			}
			else{
				try {
					String name = f.getName();
					InputStreamReader read = new InputStreamReader(new FileInputStream(f),"UTF-8");
					BufferedReader reader = new BufferedReader(read);
					StringBuffer buf = new StringBuffer();
					String line = null;
					while((line = reader.readLine()) != null){
						buf.append(line + "\r\n");
					}
					reader.close();
					getBackRuleManager().importRule(directory, name, buf.toString());
				} catch (FileNotFoundException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
				
			}
		}
	}
}
