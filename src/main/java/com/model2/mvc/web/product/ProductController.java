package com.model2.mvc.web.product;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.product.ProductService;
import com.sun.tracing.dtrace.ModuleAttributes;

//==> 회원관리 Controller
@Controller
public class ProductController {

	/// Field
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	// setter Method 구현 않음

	public ProductController() {
		System.out.println(this.getClass());
	}

	// ==> classpath:config/common.properties ,
	// classpath:config/commonservice.xml 참조 할것
	// ==> 아래의 두개를 주석을 풀어 의미를 확인 할것
	@Value("#{commonProperties['pageUnit']}")
	// @Value("#{commonProperties['pageUnit'] ?: 3}")
	int pageUnit;

	@Value("#{commonProperties['pageSize']}")
	// @Value("#{commonProperties['pageSize'] ?: 2}")
	int pageSize;

	@RequestMapping("/addProduct.do")
	public ModelAndView addProduct(@ModelAttribute("product") Product product) throws Exception {

		System.out.println("/addProduct.do");

		product.setManuDate(product.getManuDate().replaceAll("-", ""));

		productService.addProduct(product);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("forward:/product/addProductViewDitail.jsp");
		// modelAndView.addObject("product", product);

		return modelAndView;
	}

	@RequestMapping("/getProduct.do")
	public ModelAndView getProduct(	@RequestParam("prodNo") int prodNo) throws Exception {

		System.out.println("/getProduct.do");
		// Business Logic
		System.out.println(prodNo);
		Product product = productService.getProduct(prodNo);
		// Model 과 View 연결

		ModelAndView modelAndView = new ModelAndView();

		modelAndView.setViewName("forward:/product/getProduct.jsp");

		modelAndView.addObject("product", product);

		return modelAndView;
	}

	@RequestMapping("/updateProductView.do")
	public ModelAndView updateProductView(
			@ModelAttribute("product") Product product/*
														 * @RequestParam(
														 * "prodNo") int prodNo
														 */) throws Exception {

		System.out.println("/updateProductView.do");
		// Business Logic

		product = productService.getProduct(product.getProdNo());
		// Model 과 View 연결

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("forward:/product/updateProduct.jsp");
		modelAndView.addObject("product", product);

		return modelAndView;
	}

	@RequestMapping("/updateProduct.do")
	public ModelAndView updateProduct(@ModelAttribute("product")Product product
		/*@RequestParam("prodNo") int prodNo*/) throws Exception {
		product.setManuDate(product.getManuDate().replaceAll("-", ""));
		System.out.println("/updateProduct.do");
		// Business Logic
		productService.updateProduct(product);

		ModelAndView modelAndView = new ModelAndView();
		modelAndView.setViewName("forward:/getProduct.do");
		 modelAndView.addObject("product", product);

		System.out.println(modelAndView.getViewName());
		System.out.println("/updateProduct.do 끝");
		return modelAndView;
	}

	@RequestMapping("/listProduct.do")
	public ModelAndView listProduct(@ModelAttribute("search") Search search, @RequestParam("menu") String menu,
			HttpServletRequest request) throws Exception {

		System.out.println("/listProduct.do");
	
		if (search.getCurrentPage() == 0) {
			search.setCurrentPage(1);
		}
		search.setPageSize(pageSize);
		
		System.out.println("커런트페이지"+search.getCurrentPage());
		System.out.println("설치컨디션"+search.getSearchCondition());
		System.out.println("설치키워드"+search.getSearchKeyword());
		// Business logic 수행
		Map<String, Object> map = productService.getProductList(search);

		Page resultPage = new Page(search.getCurrentPage(), ((Integer) map.get("totalCount")).intValue(), pageUnit,
				pageSize);
		System.out.println(resultPage);

		// Model 과 View 연결
		ModelAndView modelAndView = new ModelAndView();

		if (menu.equals("search")) {
			modelAndView.setViewName("forward:/product/listProduct.jsp");
		} else {
			modelAndView.setViewName("forward:/product/listProductAdmin.jsp");
		}

		modelAndView.addObject("list", map.get("list"));
		modelAndView.addObject("resultPage", resultPage);
		modelAndView.addObject("search", search);

		return modelAndView;
	}
}