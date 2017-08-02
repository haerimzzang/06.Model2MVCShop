package com.model2.mvc.service.user.test;



import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;

import junit.framework.Assert;
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations={"classpath:config/commonservice.xml"})
public class ProductTestApp {

	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	//@Test
	public void testAddProduct() throws Exception{
		Product product = new Product("ÆË´Ï´Ù", "±Â", "170726", 100000, "»ï.jpg");
		
		productService.addProduct(product);
		
		product = productService.getProduct(10000);
		
		System.out.println(product); 
	}
	@Test
	public void testFindProduct() throws Exception{
		Product product = new Product();
		
		product = productService.getProduct(10000);
		
		System.out.println(product);
	}
	
	//@Test
	public void testUpdateProduct() throws Exception{
		Product product = productService.getProduct(10000);
		
		product.setProdDetail("ÇØ¸²Â¯");
		
		productService.updateProduct(product);
		
		product = productService.getProduct(10000);
		
		
		System.out.println(product);
	}
	
	
	//@Test
	public void testGetProductListAll() throws Exception{
		
		Search search = new Search();
		
		search.setCurrentPage(1);
		search.setPageSize(3);
		
		Map<String, Object> map = productService.getProductList(search);
		
		List<Object> list = (List<Object>)map.get("list");
		/*
		Assert.assertEquals(3,  list.size());
		
		System.out.println(list);
		
		Integer totalCount = (Integer)map.get("totalCount");
		System.out.println(totalCount);
		
		System.out.println("=========================");*/
		search.setCurrentPage(1);
		search.setPageSize(3);
		search.setSearchCondition("0");
		search.setSearchKeyword("");
		map = productService.getProductList(search);
		
		list = (List<Object>)map.get("list");
		Assert.assertEquals(3, list.size());
		
		
		System.out.println( list);
		
		Integer totalCount = (Integer)map.get("totalCount");
		System.out.println(totalCount);
	}
	/*public static void main(String[] args) throws Exception {
		// TODO Auto-generated method stub
		
		
		
		
		
		SqlSession sqlSession = SqlSessionFactoryBean.getSqlSession();
		System.out.println();

		Product product = new Product("Ä¿ÇÇ", "¸ÀÀÖ¾î¿ä", "170725", 1000, "coffee.jpg");
		product.setProdNo(20000);
		int prodNo = product.getProdNo();
		System.out.println("insert product");
		System.out.println(":::" + sqlSession.insert("ProductMapper.addProduct", product));
		System.out.println(product + ":::³Ö¾úÀ½");
		
		
		System.out.println("select product");
		System.out.println(":::" + sqlSession.selectOne("ProductMapper.getProduct", prodNo));
		System.out.println("È®ÀÎ");
		
		
		
		
		
		System.out.println("update product");
		product.setProdName("¹Ù²Ü°Å¾ß");
		System.out.println(":::" + sqlSession.update("ProductMapper.updateProduct", product));
		System.out.println(product);
		
		System.out.println("getProductList");
		Search search = new Search();
		SqlSessionFactoryBean.printList(sqlSession.selectList("ProductMapper.getProductList", search));
		
		search.setSearchCondition("prodNo");
		ArrayList<Integer> arrayList = new ArrayList<Integer>();
		arrayList.add(10000);
		search.setProdNo(arrayList);
		SqlSessionFactoryBean.printList(sqlSession.selectList("ProductMapper.getProductList", search));
		
		search.setSearchCondition("prodName");
		ArrayList<String> arrayList1 = new ArrayList<String>();
		arrayList1.add("Ä¿ÇÇ");
		search.setProdName(arrayList1);
		SqlSessionFactoryBean.printList(sqlSession.selectList("ProductMapper.getProductList", search));
		
		
		System.out.println("delete ");
		System.out.println(sqlSession.delete("ProductMapper.removeProduct",prodNo));
	}*/
}
