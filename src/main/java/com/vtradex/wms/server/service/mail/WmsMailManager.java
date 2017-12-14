package com.vtradex.wms.server.service.mail;

import org.springframework.transaction.annotation.Transactional;

public interface WmsMailManager {
	/**发送带附件的邮件**/
	@Transactional
	void sendFileByEmail();
	
	/**节点邮件*/
	static final String SEND_JOB_EXCEPTION = "sendNotesMail";
	void sendNotesMail(Long id);
}
