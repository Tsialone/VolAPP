package com.berd.dev.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping ("/depense")
@RequiredArgsConstructor
public class DepenseController {

    @GetMapping ("/saisie")
    public String form(Model model) {
        




        model.addAttribute("content", "pages/depenses/depense-saisie");
        return "admin-layout";
    }

    @PostMapping ("/saisie")
    public String insert(Model model) throws InterruptedException {
        
        Thread.sleep (5000);



        return "redirect:/depense/saisie";
    }

}
