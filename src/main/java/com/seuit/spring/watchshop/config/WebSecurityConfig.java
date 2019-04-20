package com.seuit.spring.watchshop.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import com.seuit.spring.watchshop.service.UserDetailServiceImpl;

import io.micrometer.core.ipc.http.HttpSender.Method;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private UserDetailServiceImpl userDetailServiceImpl;

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		// TODO Auto-generated method stub
		auth.userDetailsService(userDetailServiceImpl).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// TODO Auto-generated method stub
		http.httpBasic().and().cors().and().authorizeRequests().antMatchers("/admin/CRUD_User/**").hasRole("admin")
				.antMatchers("/admin/CRUD_Employees/**").hasRole("manager").antMatchers("/admin/CRUD_Products/**")
				.hasRole("manager").antMatchers(HttpMethod.GET, "/rest/products").permitAll()
				.antMatchers(HttpMethod.POST, "/rest/products").hasRole("manager")
				.antMatchers(HttpMethod.DELETE, "/rest/products/**").hasRole("manager")
				.antMatchers(HttpMethod.PUT, "/rest/products/**").hasRole("manager")
				.antMatchers(HttpMethod.POST, "/rest/employees/**").hasRole("manager")
				.antMatchers(HttpMethod.PUT, "/rest/employees/**").hasRole("manager")
				.antMatchers(HttpMethod.DELETE, "/rest/employees/**").hasRole("manager")
				.antMatchers(HttpMethod.GET, "/rest/employees/**").hasRole("manager").antMatchers("/admin/**")
				.hasAnyRole("admin", "manager", "employee").antMatchers("/**").permitAll().and().formLogin()
				.loginPage("/login").defaultSuccessUrl("/", true)
				.failureUrl("/login?error=true").permitAll().and().exceptionHandling().and().logout().permitAll()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout", "GET")).deleteCookies("JSESSIONID").and()
				.rememberMe().rememberMeParameter("userRememberMe").rememberMeCookieName("userRememberMe").and().csrf()
				.disable();
	}

}
