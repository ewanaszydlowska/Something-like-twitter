package pl.coderslab.warsztat6.controller;

import javax.servlet.http.HttpSession;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import pl.coderslab.warsztat6.bean.LoginData;
import pl.coderslab.warsztat6.bean.SessionManager;
import pl.coderslab.warsztat6.entity.User;
import pl.coderslab.warsztat6.repository.UserRepository;

@Controller
public class UserController {

	@Autowired
	private UserRepository userRepo;

	@Autowired
	private SessionManager sessionManager;

	@GetMapping("/register")
	public String register(Model m) {
		m.addAttribute("user", new User());
		return "register";
	}

	@PostMapping("/register")
	public String registerPost(@Valid @ModelAttribute User user, BindingResult bindingResult) {
		if (bindingResult.hasErrors()) {
			return "redirect:/register";
		}
		this.userRepo.save(user);
		return "redirect:/";
	}

	@GetMapping("/login")
	public String login(Model m) {
		m.addAttribute("loginData", new LoginData());
		return "login";
	}

	@PostMapping("/login")
	public String loginPost(@ModelAttribute LoginData loginData, Model m) {
		User u = this.userRepo.findOneByEmail(loginData.getEmail());
		if (u != null && u.isPassowrd(loginData.getPassword())) {
			HttpSession s = SessionManager.session();
			s.setAttribute("user", u);
			return "redirect:/";
		}
		m.addAttribute("message", "Wprowadz poprawne dane");
		return "login";
	}
	
	@GetMapping("/logout")
	public String logout(Model m) {
		HttpSession s = SessionManager.session();
		s.invalidate();
		
		m.addAttribute("loginData", new LoginData());
		return "redirect:/";
	}

}
