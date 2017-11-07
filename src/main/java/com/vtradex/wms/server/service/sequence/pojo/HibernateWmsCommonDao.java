package com.vtradex.wms.server.service.sequence.pojo;

import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.vtradex.thorn.server.dao.hibernate.HibernateCommonDao;
import com.vtradex.wms.server.service.sequence.WmsCommonDao;

public class HibernateWmsCommonDao extends HibernateCommonDao implements
		WmsCommonDao {

	public Object findByQueryUniqueResult(final String hql,final String[] paramNames, final Object[] values){
		return this.getHibernateTemplate().execute(new HibernateCallback() {

			public Object doInHibernate(Session session) throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				query.setFetchSize(1);
				query.setMaxResults(1);
				if (values != null) {
					for (int i = 0;i < paramNames.length;i ++) {
						if(StringUtils.isEmpty(paramNames[i]))continue;
						if(values[i] != null && (Collection.class.isAssignableFrom(values[i].getClass()))){
							query.setParameterList(paramNames[i], (Collection) values[i]);
						}else{
							query.setParameter(paramNames[i], values[i]);
						}
					}
				}
				List resultsList = query.list();
				if (resultsList.isEmpty() || resultsList == null) {
					return null;
				} else {
					return resultsList.get(0);
				}
			}
		});
	}

}
