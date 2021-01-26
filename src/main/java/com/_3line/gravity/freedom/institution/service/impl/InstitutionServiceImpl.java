package com._3line.gravity.freedom.institution.service.impl;


import com._3line.gravity.core.code.dto.CodeDTO;
import com._3line.gravity.core.code.model.Code;
import com._3line.gravity.core.code.service.CodeService;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.agents.service.AgentService;
import com._3line.gravity.freedom.institution.dto.InstitutionDTO;
import com._3line.gravity.freedom.institution.model.Institution;
import com._3line.gravity.freedom.institution.repository.InstitutionRepository;
import com._3line.gravity.freedom.institution.service.InstitutionService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class InstitutionServiceImpl implements InstitutionService {

    @Autowired
    InstitutionRepository institutionRepository;

    @Autowired
    AgentService agentService;

    @Autowired
    ModelMapper modelMapper;

    @Autowired
    CodeService codeService;


    @Override
    public InstitutionDTO getInstitutionByName(String name) {
        Institution institution = institutionRepository.findByName(name);
        InstitutionDTO dto = modelMapper.map(institution,InstitutionDTO.class);
        return dto;
    }

    @Override
    public InstitutionDTO getInstitutionById(long recordid) {
        Institution institution = institutionRepository.findOne(recordid);
        InstitutionDTO dto = modelMapper.map(institution,InstitutionDTO.class);
        return dto;
    }

    @Override
    public void deleteInstitutionById(long recordid) {
        InstitutionDTO institutionDTO  = getInstitutionById(recordid);
        institutionDTO.setDel_flag("Y");
        updateInstitution(institutionDTO);
    }

    @Override
    public InstitutionDTO getInstitutionByAgentId(long agentId) {

        Institution institution = institutionRepository.findByInstitutionAgent_Id(agentId);
        if(institution == null){
            return null;
        }
        InstitutionDTO dto = modelMapper.map(institution,InstitutionDTO.class);
        return dto;
    }

    @Override
    public InstitutionDTO addInstitution(InstitutionDTO data) {

        Institution check = institutionRepository.findByName(data.getName().toUpperCase());
        if(check!=null){
            throw new GravityException("Institution already exist");
        }

        Agents agent = validateInstAgent(data.getAgentUsername());

        Institution institution  = modelMapper.map(data,Institution.class);
        institution.setDelFlag("N");
        institution.setName(data.getName().toUpperCase());

        institution.setInstitutionAgent(agent);

        institutionRepository.save(institution);
        data.setId(institution.getId());

        Code code = codeService.getByTypeAndCode("INSTITUTION_CHARGES",data.getName());
        if(code == null){
            CodeDTO codeDTO = new CodeDTO();
            codeDTO.setType("INSTITUTION_CHARGES");
            codeDTO.setCode(data.getName().toUpperCase());
            codeDTO.setVersion(0);
            codeDTO.setDescription(data.getName().toUpperCase());
            codeService.addCode(codeDTO);
        }

        return data;
    }

    @Override
    public InstitutionDTO updateInstitution(InstitutionDTO data) {

        Agents agent = agentService.fetchAgentByAgentName(data.getAgentUsername());


        Institution institution = institutionRepository.findOne(data.getId());
        if(!institution.getInstitutionAgent().getUsername().equals(data.getAgentUsername())){
            Institution check = institutionRepository.findByInstitutionAgent_Username(data.getAgentUsername());
            if(check!=null){
                throw new GravityException("Aggregator already tied to an institution");
            }
        }

        institution.setDelFlag(data.getDel_flag());
        institution.setLine_deposit_commission(data.getLine_deposit_commission());
        institution.setAgent_deposit_commission(data.getAgent_deposit_commission());
        institution.setAggregator_deposit_commission(data.getAggregator_deposit_commission());
        institution.setAgent_withdrawal_commission(data.getAgent_withdrawal_commission());
        institution.setAggregator_withdrawal_commission(data.getAggregator_withdrawal_commission());

        institution.setAgent_bill_commission(data.getAgent_bill_commission());
        institution.setAggregator_bill_commission(data.getAggregator_bill_commission());
        institution.setLine_bill_commission(data.getLine_bill_commission());
        institution.setAgent_recharge_commission(data.getAgent_recharge_commission());
        institution.setAggregator_recharge_commission(data.getAggregator_recharge_commission());
        institution.setLine_recharge_commission(data.getLine_recharge_commission());



        institution.setInstitutionAgent(agent);
        institution.setName(data.getName());

        institutionRepository.save(institution);

        return data;
    }

    @Override
    public List<InstitutionDTO> getAllInstitutions() {
        List<InstitutionDTO> institutionDTOS = new ArrayList<>();
        List<Institution> institutions = (List<Institution>) institutionRepository.findAll();
        institutions.forEach(institution -> {
            InstitutionDTO institutionDTO = modelMapper.map(institution,InstitutionDTO.class);
            institutionDTOS.add(institutionDTO);
        });

        return institutionDTOS;
    }

    private Agents validateInstAgent(String agentUsername){

        Agents agent = agentService.fetchAgentByAgentName(agentUsername);

        if(agent==null){
            throw new GravityException("Aggregator name is invalid");
        }

        Institution check = institutionRepository.findByInstitutionAgent_Username(agentUsername);
        if(check!=null){
            throw new GravityException("Aggregator already tied to an institution");
        }

        return agent;
    }

}
