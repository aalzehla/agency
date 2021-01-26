package com._3line.gravity.freedom.commisioncharge.service;

import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.freedom.bankdetails.model.TransactionType;
import com._3line.gravity.freedom.commisioncharge.dtos.CommissionChargeDTO;
import com._3line.gravity.freedom.commisioncharge.models.CommissionCharge;
import com._3line.gravity.freedom.commisioncharge.repository.CommissionChargeRepository;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class CommissionChargeService {

    @Autowired
    private CommissionChargeRepository commissionChargeRepository;

    private Logger logger = LoggerFactory.getLogger(this.getClass()) ;

    @Autowired
    ModelMapper modelMapper ;

//    public Double getCommissionForAmount(Double amount , TransactionType transactionType) throws GravityException {
//
//        List<CommissionCharge> allForTransaction = commissionChargeRepository.
//                findByTransactionTypeAndDelFlagOrderByIdDesc(transactionType.name(),"N");
//
//        Double charge = 0.0;
//        logger.info("amount and transaction type is {} {}", amount , transactionType);
//        for (CommissionCharge c:allForTransaction) {
//
//            if(amount >= c.getLowerBound() && amount <= c.getUpperBound()){
//                charge = c.getAmount() ;
//                logger.info("charge is {}", charge);
//            }
//
//        }
//
//        if(charge ==  0.0){
//            throw new GravityException("No Charge bound found for amount "+amount);
//        }
//
//        return charge ;
//    }

    public Double getCommissionForAmount(Double amount , TransactionType transactionType) throws GravityException {

        return getInstCommissionForAmount(amount,transactionType,"DEFAULT");

    }

    public Double getInstCommissionForAmount(Double amount , TransactionType transactionType,String institution) throws GravityException {

        logger.info("amount and transaction type is {} {} {} ", amount , transactionType,institution);
        CommissionCharge commissionCharge = commissionChargeRepository.
                findByTransactionTypeAndAmountAndInst(transactionType.name(),amount,institution);

        if(commissionCharge==null || commissionCharge.getAmount()==null || commissionCharge.getAmount().equals(0.0)){
            throw new GravityException("No bound found for amount "+amount);
        }

        Double charge = commissionCharge.getAmount();

        logger.info("charge is {}", charge);

        return charge ;
    }


    @PreAuthorize("hasAuthority('MANAGE_COMMISSION_CHARGE')")
    public String createCommissionCharge(CommissionChargeDTO commissionChargeDTO){

        CommissionCharge charge = modelMapper.map(commissionChargeDTO, CommissionCharge.class);
        charge.setTransactionType(commissionChargeDTO.getType());

        if(charge.getLowerBound() < 0 ){
            throw new GravityException("PLease enter a valid lower band") ;
        }

        //TODO check if amount and band not more than global transaction limit

        commissionChargeRepository.save(charge);

        return "Charge Setup Successfully";

    }

    @PreAuthorize("hasAuthority('MANAGE_COMMISSION_CHARGE')")
    public String updateCommissionCharge(CommissionChargeDTO commissionChargeDTO){

        CommissionCharge commissionCharge = commissionChargeRepository.getOne(commissionChargeDTO.getId());
        commissionCharge.setTransactionType(commissionChargeDTO.getType());
        commissionCharge.setAmount(commissionChargeDTO.getAmount());
        commissionCharge.setLowerBound(commissionChargeDTO.getLowerBound());
        commissionCharge.setUpperBound(commissionChargeDTO.getUpperBound());

        if(commissionCharge.getLowerBound() < 0 ){
            throw new GravityException("PLease enter a valid lower band") ;
        }

        //TODO check if amount and band not more than global transaction limit

        commissionChargeRepository.save(commissionCharge);
        return "Charge Setup Successfully";
    }

    @PreAuthorize("hasAuthority('MANAGE_COMMISSION_CHARGE')")
    public String deleteCommissionCharge(Long id){
        CommissionCharge commissionCharge = commissionChargeRepository.getOne(id);
        commissionChargeRepository.delete(commissionCharge);
        return "Charge Deleted Successfully";
    }

    public CommissionCharge findCommissionCharge(Long id){
        CommissionCharge commissionCharge = commissionChargeRepository.getOne(id);
        return commissionCharge;
    }

    public CommissionCharge convertDTOToEntity(CommissionChargeDTO commissionChargeDTO){
        CommissionCharge commissionCharge = modelMapper.map(commissionChargeDTO, CommissionCharge.class);
        return commissionCharge;
    }

    private List<CommissionCharge> convertRoleDTOsToEntities(Iterable<CommissionChargeDTO> commissionChargeDTOS) {
        List<CommissionCharge> commissionCharges = new ArrayList<>();
        for (CommissionChargeDTO commissionChargeDTO : commissionChargeDTOS) {
            CommissionCharge commissionCharge = convertDTOToEntity(commissionChargeDTO);
            commissionCharges.add(commissionCharge);
        }
        return commissionCharges;
    }

    @PreAuthorize("hasAuthority('VIEW_COMMISSION_CHARGES')")
    //@Override
    public Page<CommissionChargeDTO> getCommissionCharge(Pageable pageDetails) throws GravityException {
        List<CommissionCharge> data = commissionChargeRepository.findAll();
        List<CommissionChargeDTO> dtOs = convertEntitiesToDTOs(data);
        long t = data.size() ;
        return new PageImpl<>(dtOs, pageDetails, t);
    }

    public Page<CommissionChargeDTO> getCommissionChargeByInstitution(Pageable pageDetails,String insitutionName) throws GravityException {
        List<CommissionCharge> data = commissionChargeRepository.findByInstitution(insitutionName);
        List<CommissionChargeDTO> dtOs = convertEntitiesToDTOs(data);
        long t = data.size() ;
        return new PageImpl<>(dtOs, pageDetails, t);
    }

    public List<String> getChargeTransactionTypes() throws GravityException {

        List<String> dtOs = new ArrayList<>();
        List<Enum> enumValues = Arrays.asList(TransactionType.values());
        enumValues.forEach(type -> {
            dtOs.add(String.valueOf(type));
        });
        return dtOs;
    }

    public List<CommissionChargeDTO> convertEntitiesToDTOs(List<CommissionCharge> commissionCharges){
        List<CommissionChargeDTO> commissionChargeDTOS = new ArrayList<>();
        for(CommissionCharge commissionCharge: commissionCharges){
            CommissionChargeDTO commissionChargeDTO = convertEntityToDTO(commissionCharge);
            commissionChargeDTOS.add(commissionChargeDTO);
        }
        return commissionChargeDTOS;
    }

    public CommissionChargeDTO convertEntityToDTO(CommissionCharge commissionCharge){
        CommissionChargeDTO commissionChargeDTO = modelMapper.map(commissionCharge, CommissionChargeDTO.class);
        return commissionChargeDTO;
    }


}
