package com._3line.gravity.freedom.institution.service;


import com._3line.gravity.freedom.institution.dto.InstitutionDTO;
import com._3line.gravity.freedom.institution.model.Institution;

import java.util.List;

public interface InstitutionService {

    InstitutionDTO getInstitutionByName(String name);

    InstitutionDTO getInstitutionById(long recordid);

    void deleteInstitutionById(long recordid);

    InstitutionDTO getInstitutionByAgentId(long agentId);

    InstitutionDTO addInstitution(InstitutionDTO data);

    InstitutionDTO updateInstitution(InstitutionDTO data);

    List<InstitutionDTO> getAllInstitutions();



}
