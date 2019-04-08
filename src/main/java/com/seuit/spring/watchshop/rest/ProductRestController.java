package com.seuit.spring.watchshop.rest;

import java.util.List;
import java.util.Set;

import javax.validation.Valid;
import javax.validation.constraints.Min;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.seuit.spring.watchshop.entity.Product;
import com.seuit.spring.watchshop.entity.ProductApi;
import com.seuit.spring.watchshop.entity.ProductDetail;
import com.seuit.spring.watchshop.service.ProductService;

import javassist.NotFoundException;

@RestController
@RequestMapping(value = {"/rest"})
public class ProductRestController {
	@Autowired
	private ProductService productService;
	@PostMapping("/products")
	String newProduct(@Valid @RequestBody ProductApi productApi,BindingResult result) {
		productService.saveOrUpdate(productApi, null);
		return "Add success";
	}
	
	@DeleteMapping("/products/{id}")
	String deleteProduct(@PathVariable(value = "id") @Min(1) Integer id) {
		productService.deleteProductById(id);
		return "Delete Success";
	}
	
	@PutMapping("/products/{id}")
	String updateProduct(@Valid @RequestBody ProductApi productApi,BindingResult result,@PathVariable(value = "id") @Min(1) Integer id) {
		productService.saveOrUpdate(productApi, id);
		return "Update Success";
	}
	
	@GetMapping("/products")
	List<Product> findAllProduct() {
		return productService.listProduct();
	}
	
	@GetMapping("/products/{id}")
	Product findProductByProductId(@PathVariable(value = "id") @Min(1) Integer id) throws NotFoundException {
		return productService.getProductById(id);
	}
	
	@GetMapping("/products/details")
	List<ProductDetail> findAllProductDetail() {
		return productService.listProductDetail();
	}
	
	@GetMapping("/products/details/{id}")
	ProductDetail findAllProductDetail(@PathVariable(value = "id") @Min(1) Integer id) throws NotFoundException {
		return productService.getProductDetailByProductId(id);
	}
	
	@GetMapping("/products/firm/{id}")
	Set<Product> findAllProductByIdFirm(@PathVariable(value = "id") Integer id) throws NotFoundException{
		return productService.listProductByIdFirm(id);
	}
	
	@GetMapping("/products/model/{id}")
	List<Product> findAllProductByIdModel(@PathVariable(value = "id") Integer id){
		return productService.listProductByIdModel(id);
	}
	
	@GetMapping("/products/origin/{id}")
	List<Product> findAllProductByIdOrigin(@PathVariable(value = "id") Integer id) {
		return productService.listProductByIdOrigin(id);
	}
}