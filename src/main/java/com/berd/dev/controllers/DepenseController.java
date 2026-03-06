package com.berd.dev.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.berd.dev.forms.DepenseForm;
import com.berd.dev.services.CategorieDepenseDetailService;
import com.berd.dev.services.CategorieDepenseService;
import com.berd.dev.services.DepenseService;
import com.berd.dev.services.UniteService;

import lombok.RequiredArgsConstructor;

@Controller
@RequestMapping("/depenses")
@RequiredArgsConstructor
public class DepenseController {

    private final CategorieDepenseService categorieDepenseService;
    private final CategorieDepenseDetailService categorieDepenseDetailService;
    private final UniteService uniteService;
    private final DepenseService depenseService;

    @GetMapping("/saisie")
    public String form(Model model) {
        model.addAttribute("categoriesDetail", categorieDepenseDetailService.getAll());
        model.addAttribute("categories", categorieDepenseService.getAll());
        model.addAttribute("unites", uniteService.getAll());
        model.addAttribute("content", "pages/depenses/depense-saisie");
        return "admin-layout";
    }

    @PostMapping("/save")
    public String insert(@ModelAttribute DepenseForm form, RedirectAttributes rd) {
        try {
            // Validation basique
            if (form.getDetails() == null || form.getDetails().isEmpty()) {
                throw new IllegalArgumentException("Veuillez ajouter au moins un détail de dépense");
            }
            
            // Sauvegarder la dépense avec ses détails
            depenseService.save(form);
            
            rd.addFlashAttribute("toastMessage", "Dépense insérée avec succès");
            rd.addFlashAttribute("toastType", "success");

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
            rd.addFlashAttribute("toastMessage", e.getMessage());
            rd.addFlashAttribute("toastType", "warning");
        } catch (Exception e) {
            e.printStackTrace();
            rd.addFlashAttribute("toastMessage", "Erreur lors de l'insertion: " + e.getMessage());
            rd.addFlashAttribute("toastType", "error");
        }

        return "redirect:/depenses/saisie";
    }

}
