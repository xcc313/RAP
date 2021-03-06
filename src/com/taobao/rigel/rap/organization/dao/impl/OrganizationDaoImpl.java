package com.taobao.rigel.rap.organization.dao.impl;

import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import com.taobao.rigel.rap.organization.bo.Corporation;
import com.taobao.rigel.rap.organization.bo.Group;
import com.taobao.rigel.rap.organization.bo.ProductionLine;
import com.taobao.rigel.rap.organization.dao.OrganizationDao;

public class OrganizationDaoImpl extends HibernateDaoSupport implements
		OrganizationDao {

	@SuppressWarnings("unchecked")
	@Override
	public List<Corporation> getCorporationList() {
		return getSession().createQuery("from Corporation").list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Group> getGroupList(int productionLineId) {
		Query query = getSession().createQuery(
				"from Group where productionLineId = :id");
		query.setInteger("id", productionLineId);
		return query.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<ProductionLine> getProductionLineList(int corpId) {
		return getSession()
				.createQuery("from ProductionLine where corporation_id = :id")
				.setInteger("id", corpId).list();
	}

	@Override
	public int addGroup(Group group) {
		Object s = getSession().save(group);
		return (Integer)s;
	}

	@Override
	public int addProductionList(ProductionLine productionLine) {
		Object s = getSession().save(productionLine);
		return (Integer)s;
	}

	@Override
	public void removeGroup(int groupId) {
		Session session = getSession();
		Object group = session.get(Group.class, groupId);
		if (group != null) {
			session.delete((Group) group);
		}
	}

	@Override
	public void removeProductionLine(int productionLineId) {
		Session session = getSession();
		Object productionLine = session.get(ProductionLine.class,
				productionLineId);
		if (productionLine != null) {
			session.delete((ProductionLine) productionLine);
		}
	}

	@Override
	public void updateGroup(Group group) {
		Group g = getGroup(group.getId());
		if (g != null) {
			g.setName(group.getName());
			getSession().update(g);
		}
	}

	@Override
	public void updateProductionLine(ProductionLine line) {
		ProductionLine p = getProductionLine(line.getId());
		p.setName(line.getName());
		getSession().update(p);
	}

	@Override
	public Group getGroup(int id) {
		return (Group) getSession().get(Group.class, id);
	}

	@Override
	public ProductionLine getProductionLine(int id) {
		return (ProductionLine) getSession().get(ProductionLine.class, id);
	}

	@Override
	public void updateCountersInProductionLine(int productionLineId) {
		StringBuilder sql = new StringBuilder();
		sql.append("SELECT COUNT(*) FROM tb_project p ")
		.append("JOIN tb_group g ON p.group_id = g.id ")
		.append("JOIN tb_production_line pl ON pl.id = g.production_line_id ")
		.append("WHERE g.production_line_id = :id");
		Query query = getSession().createSQLQuery(sql.toString());
		query.setInteger("id", productionLineId);
		int num = Integer.parseInt(query.uniqueResult().toString());
		sql = new StringBuilder();
		sql.append("UPDATE tb_production_line SET project_num = :num WHERE id = :id");
		query = getSession().createSQLQuery(sql.toString());
		query.setInteger("num", num);
		query.setInteger("id", productionLineId);
		query.executeUpdate();
	}

	@Override
	public Corporation getCorporation(int id) {
		return (Corporation) getSession().get(Corporation.class, id);
	}

}
