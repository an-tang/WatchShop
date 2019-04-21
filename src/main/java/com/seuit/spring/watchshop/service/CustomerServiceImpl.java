package com.seuit.spring.watchshop.service;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.seuit.spring.watchshop.entity.Cart;
import com.seuit.spring.watchshop.entity.CustomUserDetail;
import com.seuit.spring.watchshop.entity.Customer;
import com.seuit.spring.watchshop.entity.CustomerAPI;
import com.seuit.spring.watchshop.entity.User;
import com.seuit.spring.watchshop.repository.CustomerRepository;

import javassist.NotFoundException;


@Service
public class CustomerServiceImpl implements CustomerService {
	
	@Autowired
    private UserService userService;
	
	@Autowired
	private CustomerRepository customerRepository;
	
	@Override
	@Transactional
	public Boolean saveOrUpdateCustomer(CustomerAPI customerApi, Integer id) {
		// TODO Auto-generated method stub
		if(id==null){
            User user = new User();
            Customer customer = new Customer();
            setUserAndCustomer(customerApi, user, customer);
            user.setCustomer(customer);
            customer.setUser(user);
            
            //create cart for customer
            Cart cart = new Cart();
            cart.setCustomer(customer);
            customer.setCart(cart);
            cart.setPrice((double) 0);   
            
            return checkAddUser(user);
        }else{
            try {
            	
                Optional<Customer> customerPersis = customerRepository.findById(id);
                customerPersis.orElseThrow(()->new NotFoundException("Cant find customer"));
                if(customerPersis.isPresent()==true){
                    User userPersis = customerPersis.get().getUser();
                    setUserAndCustomer(customerApi,userPersis,customerPersis.get());
                    return checkAddUser(userPersis);
                }

            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        }
		return true;
	}
	private Boolean checkAddUser(User user) {
		if(userService.addUser(user,"customer")==true) {
			return true;
		}else {
			return false;
		}
	}
	 private void setUserAndCustomer(CustomerAPI customerApi, User user, Customer customer) {
	        user.setUsername(customerApi.getUser().getUsername());
	        user.setPassword(customerApi.getUser().getPassword());
	        user.setEmail(customerApi.getUser().getEmail());
	        customer.setName(customerApi.getCustomer().getName());
	        customer.setPhone(customerApi.getCustomer().getPhone());
	        customer.setAddress(customerApi.getCustomer().getAddress());
	    }
	
	@Override
	@Transactional
	public Integer getIdCustomerByPrincipal() {
		// TODO Auto-generated method stub
		Integer result = null;
		Authentication auth = SecurityContextHolder.getContext().getAuthentication();
		System.out.println(auth);
		if(auth.getPrincipal()!="anonymousUser" ) {
			CustomUserDetail userDetails = (CustomUserDetail) auth.getPrincipal();
			try {
				User user = userService.getUserById(userDetails.getId());
				Customer customer = user.getCustomer();
				result = customer.getId();
			} catch (NotFoundException e) {
				// TODO Auto-generated catch block
				result = null;
				e.printStackTrace();
			}
		}else {
			result = null;
		}
		return result;
	}
	
	
	
	 

}
