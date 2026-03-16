package com.berd.dev.controllers;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.berd.dev.models.User;
import com.berd.dev.repositories.UserRepository;
import com.berd.dev.services.UniteService;
import com.berd.dev.services.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final UniteService uniteService;

    private final UserService userService;


    @GetMapping("/home")
    public String getListe(Model model) {

        uniteService.getAll();
        model.addAttribute("content", "pages/home");
        return "admin-layout";
    }
     @GetMapping("/forgot")
    public String forgot(HttpServletRequest request, Model model) {
       
        return "pages/auths/forgot-saisie";
    }

    @GetMapping("/")
    public String login(HttpServletRequest request, Model model) {
        // On récupère le message précis qu'on a mis juste au-dessus
        String error = (String) request.getSession().getAttribute("loginErrorMessage");
        String username = (String) request.getSession().getAttribute("username");
        String password = (String) request.getSession().getAttribute("password");

        String logout = request.getParameter("logout");

        if (error != null) {
            model.addAttribute("toastMessage", error);
            model.addAttribute("toastType", "error");
            // IMPORTANT : On le supprime pour ne pas qu'il reste au prochain refresh (F5)

            model.addAttribute("username", username);
            model.addAttribute("password", password);
            request.getSession().removeAttribute("loginErrorMessage");
        }
        if (logout != null) {
            model.addAttribute("toastMessage", "Vous êtes déconnecté.");
            model.addAttribute("toastType", "info");
        }
        return "pages/auths/login-saisie";
    }

    @PostMapping("/login")
    public String login(User user) {

        return "pages/auths/login-saisie";
    }

    @GetMapping("/signin")
    public String sign(Model model) {
        User user = new User();
        user.setEmail("tsialone1902@gmail.com");
        model.addAttribute("user", user);


        return "pages/auths/signin-saisie";
    }

    @PostMapping("/signin")
    public String sign(User user, RedirectAttributes rd) {

        try {
            System.out.println(user);
            userService.save(user);
        } catch (Exception e) {
            e.printStackTrace();
            rd.addFlashAttribute("toastMessage", e.getMessage());
            rd.addFlashAttribute("toastType", "error");
            rd.addFlashAttribute("user", user);
            return "redirect:/signin";
        }
        rd.addFlashAttribute("toastMessage", "Inscription réussie ! Vous pouvez maintenant vous connecter.");
        rd.addFlashAttribute("toastType", "success");

        return "redirect:/";
    }

    // @GetMapping("/logout")
    // public String logout(Model model, HttpSession session) {
    //     session.invalidate();
    //     return "pages/auths/login-saisie";
    // }

}
