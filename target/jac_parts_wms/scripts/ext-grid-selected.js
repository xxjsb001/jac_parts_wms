if(checkIE()){
	if  (!Ext.grid.GridView.prototype.templates) {   
	    Ext.grid.GridView.prototype.templates = {};   
	}   
	Ext.grid.GridView.prototype.templates.cell =  new  Ext.Template(   
	    ' <td class="x-grid3-col x-grid3-cell x-grid3-td-{id} x-selectable {css}" style="{style}" tabIndex="0" {cellAttr}>' ,   
	    ' <div class="x-grid3-cell-inner x-grid3-col-{id}" {attr}>{value} </div>' ,   
	    ' </td>'   
	);  
}
function checkIE(){
	var userAgent=window.navigator.userAgent.toLowerCase();
	if(userAgent.indexOf("msie")>0) { 
		return true;
	}
	return false;
}

Ext.override(Ext.Panel,{  
   onDestroy : function(){  
		   Ext.destroy(  
		   this.header,  
		   this.tbar,  
		   this.bbar,  
		   this.footer,  
		   this.body,  
		   this.bwrap,  
		   this.dd  
           );  
           delete this.header;  
           delete this.tbar;  
           delete this.bbar;  
           delete this.footer;  
           delete this.body;  
           delete this.bwrap;  
           delete this.dd;  
           Ext.Panel.superclass.onDestroy.call(this);  
	}  
});  