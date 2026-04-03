package com.berd.dev.mappers;

import java.util.ArrayList;
import java.util.List;

import com.berd.dev.dtos.DepenseDto;
import com.berd.dev.models.Depense;

public class DepenseMapper {
    public static  DepenseDto  toDto (Depense depense){
            DepenseDto resp  = new DepenseDto();
            if (depense == null ) return null;
            resp.setDescription(depense.getDescription());
            resp.setIdDepense(depense.getIdDepense());
            return resp;
    }

    public static  List<DepenseDto> toDtos  (List<Depense> depenses){
        List<DepenseDto> depenseDtos = new ArrayList<> ();
        for (Depense depense : depenses) {
            depenseDtos.add(toDto(depense));
        }
        return depenseDtos;
        
    }
}
