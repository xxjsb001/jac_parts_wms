package com.vtradex.wms.server.telnet.manager.pojo;

import java.sql.SQLException;
import java.util.Collection;

import org.hibernate.HibernateException;
import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;

import com.vtradex.thorn.server.dao.hibernate.HibernateCommonDao;
import com.vtradex.thorn.server.exception.BusinessException;
import com.vtradex.thorn.server.service.pojo.DefaultBaseManager;
import com.vtradex.wms.server.model.warehouse.WmsLocation;
import com.vtradex.wms.server.model.warehouse.WmsLocationType;
import com.vtradex.wms.server.telnet.manager.WmsCommonRFManager;
import com.vtradex.wms.server.web.filter.WmsWarehouseHolder;

public class DefaultLimitQueryBaseManager extends DefaultBaseManager implements WmsCommonRFManager{

	protected Object findByHqlLimitQuery(final String hql , final String[] paramNames , final Object[] values , final int limit){
		return ((HibernateCommonDao)commonDao).getHibernateTemplate().execute(new HibernateCallback() {
			public Object doInHibernate(Session session)
					throws HibernateException, SQLException {
				Query query = session.createQuery(hql);
				if (values != null) {
					for (int i = 0;i < paramNames.length;i ++) {
						if(values[i] != null && (Collection.class.isAssignableFrom(values[i].getClass()))){
							query.setParameterList(paramNames[i], (Collection) values[i]);
						}else{
							query.setParameter(paramNames[i], values[i]);
						}
					}
				}
				query.setFetchSize(limit);
				query.setMaxResults(limit);
				if(limit == 1)
					return query.uniqueResult();
				else
					return query.list();
			}
		});
	}
	
	protected WmsLocation queryWmsLocationByCode(String code) {
		return (WmsLocation)commonDao.findByQueryUniqueResult("FROM WmsLocation loc WHERE loc.code=:code AND loc.warehouse.id=:whId", new String[]{"code","whId"}, new Object[]{code , WmsWarehouseHolder.getWmsWarehouse().getId()});
	}
	
	public void checkLocationValid(String locationCode) {
		WmsLocation location = this.queryWmsLocationByCode(locationCode);
		if(location == null || WmsLocationType.COUNT.equals(location.getType())) {
			throw new BusinessException("库位非法");
		}
	}
}
