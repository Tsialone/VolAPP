package com.berd.dev.services;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.berd.dev.dtos.CaisseDto;
import com.berd.dev.forms.CaisseForm;
import com.berd.dev.mappers.CaisseMapper;
import com.berd.dev.models.Caisse;
import com.berd.dev.models.CaisseCategorie;
import com.berd.dev.models.User;
import com.berd.dev.repositories.CaisseCategoreRepository;
import com.berd.dev.repositories.CaisseRepository;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class CaisseService {
    private final CaisseRepository caisseRepository;
    private final CaisseCategoreRepository caisseCategoreRepository;

    private final SecurityService securityService;

    public List<CaisseDto> getLastTransaction(int nbrTransaction) {
        User utilisateur = securityService.getAuthenticatedUser();

        List<CaisseDto> resp = CaisseMapper
                .tDtos(caisseRepository.findByUtilisateurIdUtilisateur(utilisateur.getIdUtilisateur()));
        return resp.stream()
                .sorted(Comparator.comparing(CaisseDto::getCreated).reversed())
                .limit(nbrTransaction)
                .collect(Collectors.toList());
    }

    public List<CaisseDto> getAllDto() {
        User utilisateur = securityService.getAuthenticatedUser();
        return CaisseMapper.tDtos(caisseRepository.findByUtilisateurIdUtilisateur(utilisateur.getIdUtilisateur()));
    }

    public Caisse saveByForm(CaisseForm form) {
        User user = securityService.getAuthenticatedUser();
        if (form == null) {
            throw new IllegalArgumentException("CaisseForm cannot be null");
        }
        // Basic validation: ensure a category was selected
        if (form.getIdCaisseCategorie() == null) {
            throw new IllegalArgumentException("Veuillez sélectionner une catégorie pour la caisse");
        }
        if (form.getNom() == null || form.getNom().trim().isEmpty()) {
            throw new IllegalArgumentException("Le nom de la caisse ne peut pas être vide");
        }
        Caisse caisse = new Caisse();
        caisse.setNom(form.getNom());
        caisse.setDescription(form.getDescription());
        caisse.setUtilisateur(user);
        CaisseCategorie categorie = caisseCategoreRepository.findById(form.getIdCaisseCategorie())
                .orElseThrow(() -> new IllegalArgumentException(
                        "CaisseCategorie not found with id: " + form.getIdCaisseCategorie()));
        caisse.setCaisseCategorie(categorie);
        return caisseRepository.save(caisse);
    }

    public Caisse create(Caisse caisse) {
        if (caisse == null) {
            throw new IllegalArgumentException("Caisse cannot be null");
        }
        return caisseRepository.save(caisse);
    }

    public List<Caisse> getAll() {
        return caisseRepository.findAll();
    }
}
