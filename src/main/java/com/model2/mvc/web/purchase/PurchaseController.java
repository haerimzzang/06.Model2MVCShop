package com.model2.mvc.web.purchase;

import java.util.Enumeration;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import com.model2.mvc.common.Page;
import com.model2.mvc.common.Search;
import com.model2.mvc.service.domain.Product;
import com.model2.mvc.service.domain.Purchase;
import com.model2.mvc.service.domain.User;
import com.model2.mvc.service.product.ProductService;
import com.model2.mvc.service.purchase.PurchaseService;
import com.model2.mvc.service.purchase.impl.PurchaseServiceImpl;
import com.model2.mvc.service.user.UserService;
import com.sun.org.apache.xpath.internal.operations.Mod;
import com.sun.tracing.dtrace.ModuleAttributes;

//==> 회원관리 Controller
@Controller
public class PurchaseController {

	/// Field
	@Autowired
	@Qualifier("purchaseServiceImpl")
	private PurchaseService purchaseService;
	// setter Method 구현 않음
	@Autowired
	@Qualifier("productServiceImpl")
	private ProductService productService;
	@Autowired
	@Qualifier("userServiceImpl")
	private UserService userService;
	
	public PurchaseController() {
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
	
	@RequestMapping("/addPurchaseView.do")
	public String addPurchaseView(@ModelAttribute("product") Product product, Model model) throws Exception{
		System.out.println("/addPurchaseView.do");
		
		product = productService.getProduct(product.getProdNo());
		System.out.println(product);
		model.addAttribute("product", product);
		
		return "forward:/purchase/addPurchaseView.jsp";
	}

	@RequestMapping("/addPurchase.do")
	public String addPurchase(@ModelAttribute("purchase") Purchase purchase, @RequestParam("prodNo") int prodNo, @RequestParam("buyerId") String userId) throws Exception {
		
		
		System.out.println("/addPurchase.do");
		User user = userService.getUser(userId);
		Product product = productService.getProduct(prodNo);
		System.out.println(purchase);
		purchase.setBuyer(user);
		purchase.setPurchaseProd(product);
		purchaseService.addPurchase(purchase);
		
		

		return "forward:/purchase/addPurchase.jsp";
	}
	
	@RequestMapping("/getPurchase.do")
	public String getPurchase (@RequestParam("tranNo")int tranNo, Model model) throws Exception{
		
		System.out.println("/getPurchase.do");
		
		System.out.println(tranNo +"<====tranNo");

		Purchase purchase =purchaseService.getPurchase(tranNo);
		purchase.setDivyDate(purchase.getDivyDate().split(" ")[0]);
		//purchase.setDivyDate(purchase.getDivyAddr().substring(0, 8));
		System.out.println(purchase.getOrderDate()+"ddddddd");
		model.addAttribute("purchase", purchase);
		
		return "forward:/purchase/getPurchase.jsp";
	}
	
	
	@RequestMapping("/listPurchase.do")
	public String listPurchaes(@ModelAttribute("search") Search search, 
			@ModelAttribute("user") User user, 
			Model model , HttpSession session) throws Exception {

		System.out.println("/listPurchaes.do");
		
		//String buyerId = user.getUserId();
		String buyerId = ((User)session.getAttribute("user")).getUserId();
		
		
		System.out.println(buyerId);
		if (search.getCurrentPage() == 0) {
			search.setCurrentPage(1);
		}
		
		
		search.setPageSize(pageSize);
		System.out.println("서치" + search);
		System.out.println("커런트페이지"+search.getCurrentPage());
		System.out.println("");

		// Business logic 수행
		Map<String, Object> map = purchaseService.getPurchaseList(search, buyerId);
		System.out.println("여기까지?");
		Page resultPage = new Page(search.getCurrentPage(), ((Integer) map.get("totalCount")).intValue(), pageUnit,
				pageSize);
		System.out.println(resultPage);

		model.addAttribute("list", map.get("list"));
		model.addAttribute("resultPage", resultPage);
		model.addAttribute("search", search);


		return "forward:/purchase/listPurchase.jsp";
	}
	
	@RequestMapping("/updatePurchaseView.do")
	public String updatePurchaseView(@ModelAttribute("purchase") Purchase purchase, Model model) throws Exception{
		
		System.out.println("/updatePurchaseView.do");
				
		purchase = purchaseService.getPurchase(purchase.getTranNo());
		
	
		System.out.println(purchase +":::::::: purchase");
		
		model.addAttribute("purchase", purchase);
		
		return "forward:/purchase/updatePurchase.jsp";
	}
	
	
	@RequestMapping("/updatePurchase.do")
	public String updatePurchase(@ModelAttribute("purchase") Purchase purchase ,@RequestParam("prodNo") int prodNo, 	@ModelAttribute("user") User user, 
			Model model , HttpSession session) throws Exception{
		
		System.out.println("/updatePurchase.do");
		String buyerId = ((User)session.getAttribute("user")).getUserId();
		System.out.println("prodNo =====>> " + prodNo);
		System.out.println(buyerId);
		System.out.println();
		purchase.setBuyer(userService.getUser(buyerId));
		purchase.setPurchaseProd(productService.getProduct(prodNo));
		//purchase.setPurchaseProd(productService.getProduct(prodNo));
		System.out.println(purchase +"purchase");
		
		purchaseService.updatePurcahse(purchase);
		
		
		
		
		
		return "redirect:/getPurchase.do?tranNo="+purchase.getTranNo();
	}
	
	@RequestMapping("updateTranCodeByProd.do")
	public String updateTranCodeByProd(@ModelAttribute("purchase") Purchase purchase, @ModelAttribute("product") Product product, @RequestParam("prodNo") int prodNo, @RequestParam("tranCode") String tranCode, @RequestParam("menu") String menu, Model model, HttpServletRequest request)throws Exception{
		System.out.println("updateTranCodeByProd.do start");
		
		System.out.println(prodNo +"++" +tranCode + "|||" +menu );
		product.setProdNo(prodNo);
//		product.setProTranCode(tranCode);
		purchase.setTranCode(tranCode);
		purchase.setPurchaseProd(product);
		
		purchaseService.updateTranCode(purchase);
//		purchase.setTranCode(tranCode);
//		purchase.setPurchaseProd(product);
		
		//purchaseService.updateTranCode(purchase);
		
		
		System.out.println("어디까지");
		if(menu.equals("manage")){
			return "forward:/listProduct.do?menu=manage";
		}else{
			return "forward:/listPurchase.do";
		}
 
	}
	
	/*
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
			@ModelAttribute("product") Product product
														 * @RequestParam(
														 * "prodNo") int prodNo
														 ) throws Exception {

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
		@RequestParam("prodNo") int prodNo) throws Exception {

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
	}*/
}