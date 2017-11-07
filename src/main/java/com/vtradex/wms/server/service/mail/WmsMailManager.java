package com.vtradex.wms.server.service.mail;

import org.springframework.transaction.annotation.Transactional;

public interface WmsMailManager {
	/**发送带附件的邮件**/
	@Transactional
	void sendFileByEmail();
}
