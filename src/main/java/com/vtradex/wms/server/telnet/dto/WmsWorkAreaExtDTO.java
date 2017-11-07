package com.vtradex.wms.server.telnet.dto;

/**
 * 工作区扩展信息
 *
 * @category DTO
 * @author <a href="brofe.pan@gmail.com">潘宁波</a>
 * @version $Revision: 1.1.1.1 $ $Date: 2015/03/25 02:48:54 $
 */
public class WmsWorkAreaExtDTO {
	
	// 起止区
	private int startZone;
	private int endZone;
	
	// 起止排
	private int startLine;
	private int endLine;
	
	public WmsWorkAreaExtDTO() {
	}
	
	public WmsWorkAreaExtDTO(int startZone, int endZone, int startLine,
			int endLine) {
		super();
		this.startZone = startZone;
		this.endZone = endZone;
		this.startLine = startLine;
		this.endLine = endLine;
	}



	public int getStartZone() {
		return startZone;
	}

	public void setStartZone(int startZone) {
		this.startZone = startZone;
	}

	public int getEndZone() {
		return endZone;
	}

	public void setEndZone(int endZone) {
		this.endZone = endZone;
	}

	public int getStartLine() {
		return startLine;
	}

	public void setStartLine(int startLine) {
		this.startLine = startLine;
	}

	public int getEndLine() {
		return endLine;
	}

	public void setEndLine(int endLine) {
		this.endLine = endLine;
	}
}
