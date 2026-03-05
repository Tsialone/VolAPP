package com.berd.dev.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomeController {

    @GetMapping ("/home")
    public String getListe(Model model) {


        
        model.addAttribute("content", "pages/home");
        return "admin-layout";
    }

}
