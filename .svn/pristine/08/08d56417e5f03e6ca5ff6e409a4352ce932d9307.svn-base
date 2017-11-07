package com.vtradex.workflow;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.digester.Digester;
import org.xml.sax.SAXException;

import com.vtradex.thorn.server.config.ui.engine.Action;
import com.vtradex.thorn.server.config.ui.engine.EnableExpression;
import com.vtradex.thorn.server.config.ui.engine.Forward;
import com.vtradex.thorn.server.config.ui.engine.Mapping;
import com.vtradex.thorn.server.config.ui.engine.ReportButton;
import com.vtradex.thorn.server.config.ui.engine.button.FormCommitButton;
import com.vtradex.thorn.server.config.ui.engine.button.FormPopupButton;
import com.vtradex.thorn.server.config.ui.engine.button.TableCommitButton;
import com.vtradex.thorn.server.config.ui.engine.button.TablePopupButton;
import com.vtradex.thorn.server.config.ui.engine.workflow.Condition;
import com.vtradex.thorn.server.config.ui.engine.workflow.Decision;
import com.vtradex.thorn.server.config.ui.engine.workflow.Fork;
import com.vtradex.thorn.server.config.ui.engine.workflow.From;
import com.vtradex.thorn.server.config.ui.engine.workflow.PageProcess;
import com.vtradex.thorn.server.config.ui.engine.workflow.State;
import com.vtradex.thorn.server.config.ui.engine.workflow.TaskNode;
import com.vtradex.thorn.server.config.ui.engine.workflow.To;
import com.vtradex.thorn.server.config.ui.engine.workflow.Workflow;


/**
 * @author <a href="mailto:yan.li@vtradex.com">李炎</a>
 * @description 
 */

public class DigesterTest {
	
	protected Map<String, PageProcess> pageProcess = new HashMap<String, PageProcess>();
	
	protected Map<String, Workflow> workflows = new HashMap<String, Workflow>();
	
	public static void main(String[] args) {
		DigesterTest dt = new DigesterTest();
		
		URL url = Thread.currentThread().getContextClassLoader().getResource("config" );
		File f = new File(url.getPath() + "\\process");
		File[] files = f.listFiles();
		for (int i = 0 ; i < files.length ; i ++) {
			System.out.println(files[i].getName());
			try {
				dt.getDigester().parse(files[i]);
			} catch (IOException e) {
				e.printStackTrace();
			} catch (SAXException e) {
				e.printStackTrace();
			}
		}
	}
	
	public Digester getDigester() {
    	Digester digester = new Digester();
    	digester.push(this);
    	processPageDigester(digester);
    	processDigester(digester);
    	return digester;
	}
	
	public void processDigester(Digester digester) {
		digester.addObjectCreate("process", Workflow.class);
        digester.addSetProperties("process");
        digester.addSetNext("process", "addWorkflow");
        
        digester.addObjectCreate("process/state", State.class);
        digester.addSetProperties("process/state");
        digester.addSetNext("process/state", "addState");
        
        
        digester.addObjectCreate("process/decision", Decision.class);
        digester.addSetProperties("process/decision");
        digester.addSetNext("process/decision", "addDecision");
        digester.addObjectCreate("process/decision/to", To.class);
        digester.addSetProperties("process/decision/to");
        digester.addSetNext("process/decision/to", "addTo");
        
        
        
        
        digester.addObjectCreate("process/fork", Fork.class);
        digester.addSetProperties("process/fork");
        digester.addSetNext("process/fork", "addFork");
        digester.addObjectCreate("process/fork/from", From.class);
        digester.addSetProperties("process/fork/from");
        digester.addSetNext("process/fork/from", "setFrom");
        
        
        
        
        digester.addObjectCreate("process/task", TaskNode.class);
        digester.addSetProperties("process/task");
        digester.addSetNext("process/task", "addTaskNode");
        
        digester.addObjectCreate("process/task/condition", Condition.class);
        digester.addSetProperties("process/task/condition");                         
        digester.addCallMethod("process/task/condition", "setExpression",0);   
        digester.addSetNext("process/task/condition", "setCondition");
        
        digester.addObjectCreate("process/task/to", To.class);
        digester.addSetProperties("process/task/to");
        digester.addSetNext("process/task/to", "addTo");
        
        digester.addObjectCreate("process/task/from", From.class);
        digester.addSetProperties("process/task/from");
        digester.addSetNext("process/task/from", "addFrom");
	}
	
	public void processPageDigester(Digester digester){
		digester.addObjectCreate("page", PageProcess.class);
        digester.addSetProperties("page");
        digester.addSetNext("page", "addPage");
        
        digester.addObjectCreate("page/process", com.vtradex.thorn.server.config.ui.engine.workflow.Process.class);
        digester.addSetProperties("page/process");
        digester.addSetNext("page/process", "addProcess");
        
        digester.addObjectCreate("page/process/tableCommit", TableCommitButton.class);  
        digester.addSetProperties("page/process/tableCommit");
        digester.addSetNext("page/process/tableCommit", "addButton");
        
        
        digester.addObjectCreate("page/process/tableCommit/forwards", ArrayList.class);
        digester.addSetNext("page/process/tableCommit/forwards","setForwards");
        digester.addObjectCreate("page/process/tableCommit/forwards/forward", Forward.class);
        digester.addSetProperties("page/process/tableCommit/forwards/forward");
        digester.addSetNext("page/process/tableCommit/forwards/forward","add");
        
        
        digester.addObjectCreate("page/process/tableCommit/actions", ArrayList.class);
        digester.addSetNext("page/process/tableCommit/actions","setActions");
        digester.addObjectCreate("page/process/tableCommit/actions/action", Action.class);
        digester.addSetProperties("page/process/tableCommit/actions/action");
        digester.addSetNext("page/process/tableCommit/actions/action","add");

        
        
        
        digester.addObjectCreate("page/process/tableCommit/mappings", ArrayList.class);
        digester.addSetNext("page/process/tableCommit/mappings", "setMappings");
        digester.addObjectCreate("page/process/tableCommit/mappings/mapping", Mapping.class);
        digester.addSetProperties("page/process/tableCommit/mappings/mapping");
        digester.addSetNext("page/process/tableCommit/mappings/mapping", "add");
        
        digester.addObjectCreate("page/process/tableCommit/mappings/mapping/entries", ArrayList.class);
        digester.addSetNext("page/process/tableCommit/mappings/mapping/entries", "setEntries");
        
        digester.addObjectCreate("page/process/tableCommit/mappings/mapping/entries/entry", com.vtradex.thorn.server.config.ui.engine.Entry.class);
        digester.addSetProperties("page/process/tableCommit/mappings/mapping/entries/entry");
        digester.addSetNext("page/process/tableCommit/mappings/mapping/entries/entry", "add");

	
        digester.addObjectCreate("page/process/popup", TablePopupButton.class);  
        digester.addSetProperties("page/process/popup");
        digester.addSetNext("page/process/popup", "addButton");
        
        
        digester.addObjectCreate("page/process/report", ReportButton.class);  
        digester.addSetProperties("page/process/report");
        digester.addSetNext("page/process/report", "addButton");
        
        digester.addObjectCreate("page/process/report/enableExpression", EnableExpression.class);
        digester.addSetProperties("page/process/report/enableExpression");
        digester.addCallMethod("page/process/report/enableExpression" , "setExpression" , 0);
        digester.addSetNext("page/process/report/enableExpression", "setEnableExpression");
        
        
        
        digester.addObjectCreate("pages/editPage/buttons/formCommit", FormCommitButton.class);  // edit page formCommit
        digester.addSetProperties("pages/editPage/buttons/formCommit");
        digester.addSetNext("pages/editPage/buttons/formCommit", "addButton");
        
        digester.addObjectCreate("pages/editPage/buttons/formCommit/forwards", ArrayList.class);
        digester.addSetNext("pages/editPage/buttons/formCommit/forwards","setForwards");
        digester.addObjectCreate("pages/editPage/buttons/formCommit/forwards/forward", Forward.class);
        digester.addSetProperties("pages/editPage/buttons/formCommit/forwards/forward");
        digester.addSetNext("pages/editPage/buttons/formCommit/forwards/forward","add");
        
        
        digester.addObjectCreate("pages/editPage/buttons/formCommit/actions", ArrayList.class);
        digester.addSetNext("pages/editPage/buttons/formCommit/actions","setActions");
        digester.addObjectCreate("pages/editPage/buttons/formCommit/actions/action", Action.class);
        digester.addSetProperties("pages/editPage/buttons/formCommit/actions/action");
        digester.addSetNext("pages/editPage/buttons/formCommit/actions/action","add");
        
        
        digester.addObjectCreate("pages/editPage/buttons/formCommit/mappings", ArrayList.class);
        digester.addSetNext("pages/editPage/buttons/formCommit/mappings", "setMappings");
        digester.addObjectCreate("pages/editPage/buttons/formCommit/mappings/mapping", Mapping.class);
        digester.addSetProperties("pages/editPage/buttons/formCommit/mappings/mapping");
        digester.addSetNext("pages/editPage/buttons/formCommit/mappings/mapping", "add");
        
        digester.addObjectCreate("pages/editPage/buttons/formCommit/mappings/mapping/entries", ArrayList.class);
        digester.addSetNext("pages/editPage/buttons/formCommit/mappings/mapping/entries", "setEntries");
        
        digester.addObjectCreate("pages/editPage/buttons/formCommit/mappings/mapping/entries/entry", com.vtradex.thorn.server.config.ui.engine.Entry.class);
        digester.addSetProperties("pages/editPage/buttons/formCommit/mappings/mapping/entries/entry");
        digester.addSetNext("pages/editPage/buttons/formCommit/mappings/mapping/entries/entry", "add");

	
        digester.addObjectCreate("pages/editPage/buttons/popup", FormPopupButton.class);  // edit page popup
        digester.addSetProperties("pages/editPage/buttons/popup");
        digester.addSetNext("pages/editPage/buttons/popup", "addButton");
	}
	
	public void addPage(PageProcess page){
		pageProcess.put(page.getId(), page);
	}
	
	public void addWorkflow(Workflow workflow){
		workflows.put(workflow.getId(), workflow);
	}

}
