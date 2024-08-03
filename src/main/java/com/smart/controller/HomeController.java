package com.smart.controller;

import com.smart.dao.UserRepository;
import com.smart.entities.User;
import com.smart.helper.Message;

import jakarta.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class HomeController {
	
	@Autowired
	private UserRepository userRepository;

	@RequestMapping("/")
	public String home(Model model) {
		model.addAttribute("title", "Home-Smart Contact Manager");
		return "home";
	}
	
	@RequestMapping("/about")
	public String about(Model model) {
		model.addAttribute("title", "About-Smart Contact Manager");
		return "about";
	}
	
	@RequestMapping("/signup")
	public String signup(Model model) {
		model.addAttribute("title", "Register-Smart Contact Manager");
		model.addAttribute("user", new User());
		return "signup";
	}
	
	// Handler for user registration
	@RequestMapping(value = "/do_register", method = RequestMethod.POST)
	public String registerUser(@Validated @ModelAttribute("user") User user,BindingResult result1 ,
			
		@RequestParam(value = "agreement", defaultValue = "false") boolean agreement, Model model,HttpSession session) {
		try {
			if (!agreement) {
				System.out.println("You have not agreed to the terms and conditions.");
				throw new Exception("You have not agreed to the terms and conditions.");
			}
			
			if(result1.hasErrors()) {
				
				System.out.println("Error"+result1.toString());
				model.addAttribute("user",user);
				return "signup";
			}

			user.setRole("ROLE_USER");
			user.setEnabled(true);
			user.setImageurl("default.png");

			System.out.println("Agreement: " + agreement);
			System.out.println("USER: " + user);

			User result = this.userRepository.save(user);

			model.addAttribute("user", new User());

			session.setAttribute("message", new Message("Successfully Registered!", "alert-success"));

			return "signup";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("user", user);

			session.setAttribute("message", new Message("something Went Wrong !!"+e.getMessage(),"alert-error"));
			return "signup";
		}
	}
	
	// Test endpoint to save a user
//	@GetMapping("/test")
//	@ResponseBody
//	public String test() {
//		User user = new User();
//		user.setName("Mohit Saini");
//		user.setEmail("mohitsaini@gmail.com");
//
//		userRepository.save(user);
//
//		return "User saved successfully!";
//	}
}
