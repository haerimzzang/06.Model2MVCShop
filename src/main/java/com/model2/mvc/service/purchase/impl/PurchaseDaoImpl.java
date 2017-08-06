package com.model2.mvc.service.purchase.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.purchase.PurchaseDao;
@Repository("purchaseDaoImpl")
public class PurchaseDaoImpl implements PurchaseDao {

	@Autowired
	@Qualifier("sqlSessionTemplate")
	private SqlSession sqlSession;
	
	public void setSqlSession(SqlSession sqlSession) {
		this.sqlSession = sqlSession;
	}
	
	public PurchaseDaoImpl() {
		// TODO Auto-generated constructor stub
		System.out.println(this.getClass());
	}
	
	@Override
	public void addPurchase(Purchase purchase) throws Exception {
		// TODO Auto-generated method stub
		sqlSession.insert("PurchaseMapper.addPurchase", purchase);
		
	}

	@Override
	public Purchase getPurchase(int tranNo) throws Exception {
		// TODO Auto-generated method stub
		return sqlSession.selectOne("PurchaseMapper.getPurchase", tranNo);
	}

	@Override
	public Purchase getPurchase2(int prodNo) throws Exception {
		// TODO Auto-generated method stub
		return sqlSession.selectOne("PurchaseMapper.getPurchase", prodNo);
	}

	@Override
	public Map<String,Object> getPurchaseList(Search search, String buyerId) throws Exception {
		// TODO Auto-generated method stub
		System.out.println("daoImpl getPurchaseList메소드 시작");
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("search", search);
		map.put("buyerId", buyerId);
		System.out.println("search와 buyerId" + search + buyerId);
		
		
		int totalCount =  sqlSession.selectOne("PurchaseMapper.getTotalCount", search);
		List<Purchase> list = sqlSession.selectList("PurchaseMapper.getPurchaseList", map);
		
		map.put("totalCount", new Integer(totalCount));
		map.put("list", list);
		System.out.println("daoImpl getPurchaseList메소드 끝");
		return map;
		
	}

	@Override
	public List<Purchase> getSaleList(Search search) throws Exception {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void updatePurcahse(Purchase purchase) throws Exception {
		// TODO Auto-generated method stub
		sqlSession.update("PurchaseMapper.updatePurchase",purchase);
	}

	@Override
	public void updateTranCode(Purchase purchase) throws Exception {
		// TODO Auto-generated method stub
		
		sqlSession.update("PurchaseMapper.updateTranCode", purchase);
		
	}

	@Override
	public int getTotalCount(Search search) throws Exception {
		// TODO Auto-generated method stub
		return sqlSession.selectOne("PurchaseMapper.getTotalCount", search);
	}

}
