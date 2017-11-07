package com.vtradex.wms.server.service.security;

import java.util.List;

import org.springframework.transaction.annotation.Transactional;

import com.vtradex.thorn.server.service.BaseManager;

/**
 * @author: 李炎
 */
@Transactional(readOnly=true)
public interface ClientUserManager extends
		BaseManager {
	/**
	 * 增加组用户
	 */
	@Transactional
    void addGroupUsers(Long groupId,List<Long> userIds);
    /**
     * 取消组用户
     */
	@Transactional
	void cancelGroupUsers(Long groupId,List<Long> userIds);
}
