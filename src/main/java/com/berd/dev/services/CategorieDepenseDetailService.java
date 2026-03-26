package com.berd.dev.services;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.berd.dev.models.CategorieDepense;
import com.berd.dev.models.CategorieDepenseDetail;
import com.berd.dev.models.User;
import com.berd.dev.repositories.CategorieDepenseDetailRepository;
import com.berd.dev.repositories.CategorieDepenseRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CategorieDepenseDetailService {
    private final CategorieDepenseDetailRepository categorieDepenseDetailRepository;


    private final CategorieDepenseRepository categorieDepenseRepository;


    private final SecurityService securityService;

    public List<CategorieDepenseDetail> getByIdUtilisateur (Long idUtilisateur) {
        List<CategorieDepenseDetail> resp = new ArrayList<>();
        for (CategorieDepenseDetail categorieDepenseDetail : getAll()) {
            if (categorieDepenseDetail.getIdCategorieDepense() == null) continue;
            CategorieDepense  categorieDepense =  categorieDepenseRepository.findById(categorieDepenseDetail.getIdCategorieDepense()).orElse(null);
            if (categorieDepense.getUtilisateur() != null && categorieDepense.getUtilisateur().getIdUtilisateur().equals(idUtilisateur)){
                resp.add(categorieDepenseDetail);
            }
            
        }


        return resp;
    }

    public List<CategorieDepenseDetail> getByIdCategorieDepense(Integer idCd) {
        if (idCd == null)
            return new ArrayList<>();
        return categorieDepenseDetailRepository.findByIdCategorieDepense(idCd);
    }

    public List<CategorieDepenseDetail> getAll() {
        return categorieDepenseDetailRepository.findAll(Sort.by(Sort.Direction.ASC, "libelle"));
    }

    public Page<CategorieDepenseDetail> getFilteredDetails(String search, Integer categorieId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "created"));
        User user = securityService.getAuthenticatedUser();

        if ((search == null || search.trim().isEmpty()) && categorieId == null) {
            return getByIdUtilisateur(user.getIdUtilisateur());
        }

        return categorieDepenseDetailRepository.findByFilters(
                search != null ? search.trim() : "",
                categorieId,
                pageable);
    }

    public Optional<CategorieDepenseDetail> getDetailById(Integer id) {
        return categorieDepenseDetailRepository.findById(id);
    }

    public CategorieDepenseDetail saveDetail(CategorieDepenseDetail detail) {
        return categorieDepenseDetailRepository.save(detail);
    }

    public void deleteDetail(Integer id) {
        categorieDepenseDetailRepository.deleteById(id);
    }
}
