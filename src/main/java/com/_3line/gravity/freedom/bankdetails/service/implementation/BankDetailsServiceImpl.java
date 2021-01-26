package com._3line.gravity.freedom.bankdetails.service.implementation;


import com._3line.gravity.core.usermgt.exception.AppUserServiceException;
import com._3line.gravity.core.usermgt.model.SystemUser;
import com._3line.gravity.core.verification.annotations.RequireApproval;
import com._3line.gravity.freedom.bankdetails.dtos.BankDetailsDTO;
import com._3line.gravity.freedom.bankdetails.dtos.FreedomCommisionDTO;
import com._3line.gravity.freedom.bankdetails.model.BankDetails;
import com._3line.gravity.freedom.bankdetails.model.FreedomCommision;
import com._3line.gravity.freedom.bankdetails.repository.BankDetailsRepository;
import com._3line.gravity.freedom.bankdetails.service.BankDetailsService;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.io.File;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;


@Service
public class BankDetailsServiceImpl implements BankDetailsService {

    @Autowired
    BankDetailsRepository bankDetailsRepository;
    @Autowired
    private MessageSource messageSource ;

    private final Locale locale = LocaleContextHolder.getLocale() ;



    private ModelMapper modelMapper = new ModelMapper();
    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Override
    public List<BankDetails> findAllBankDetails() {
        List<BankDetails> bankDetailsList = bankDetailsRepository.findAll();
        return bankDetailsList;
    }

    @Override
    public Page<BankDetails> findAllBankDetailsPageable(Pageable pageable) {
        Page<BankDetails> bankDetails = bankDetailsRepository.findAll(pageable);
        return bankDetails;
    }

    @Override
    public Page<BankDetails> findAllBankDetailsDTOPageable(Pageable pageable) {
        Page<BankDetails> page = bankDetailsRepository.findAll(pageable);
//        List<BankDetailsDTO> dtOs = convertEntitiesToDTOs(page.getContent());
//        long t = page.getTotalElements();
//        Page<BankDetailsDTO> pageImpl = new PageImpl<BankDetailsDTO>(dtOs, pageable, t);
        return page;
    }

    @Override
    public Page<BankDetails> findAllBankDetailsDTOPageable(String bankName,Pageable pageable) {
        Page<BankDetails> page = bankDetailsRepository.findByBankNameContains(bankName,pageable);
//        List<BankDetailsDTO> dtOs = convertEntitiesToDTOs(page.getContent());
//        long t = page.getTotalElements();
//        Page<BankDetailsDTO> pageImpl = new PageImpl<BankDetailsDTO>(dtOs, pageable, t);
        return page;
    }

    @Override
    public BankDetails findOne(Long id) {
        BankDetails bankDetails = bankDetailsRepository.getOne(id);
        return bankDetails;
    }
    @Override
    public BankDetails findByCode(String bankCode){
        BankDetails bankDetails = bankDetailsRepository.findByBankCode(bankCode);
        return bankDetails ;
    }

    @Override
    public BankDetails findByCBNCode(String cbnCode){
        BankDetails bankDetails = bankDetailsRepository.findByCbnCode(cbnCode);
        return bankDetails ;
    }



    @Override
    public List<BankDetails> findIntegratedBanks(boolean isIntegrated) {
        List<BankDetails> bankDetailsList = bankDetailsRepository.findByIsIntegratedIs(isIntegrated);
        return bankDetailsList;
    }

    @Override
    public List<BankDetails> findCardEnabledBanks() {

        return bankDetailsRepository.findAllByCardRequestFeeIsNotNullAndCardRequestChargeIsNotNull();
    }

    @Override
    public Page<BankDetails> findIntegratedBanks(Pageable pageable) {
        Page<BankDetails> bankDetails = bankDetailsRepository.findAllByIsIntegratedTrue(pageable);
        return bankDetails;
    }

    public BankDetails convertDTOToEntity(BankDetailsDTO bankDetailsDTO){
        BankDetails bankDetails = modelMapper.map(bankDetailsDTO, BankDetails.class);
        return bankDetails;
    }

    public BankDetailsDTO convertEntityToDTO(BankDetails bankDetails){
        BankDetailsDTO bankDetailsDTO = modelMapper.map(bankDetails, BankDetailsDTO.class);
//        List<FreedomCommisionDTO> freedomCommisionDTOList = new ArrayList<>();
//        for (FreedomCommision freedomCommision: bankDetails.getCommissions()){
//            FreedomCommisionDTO freedomCommisionDTO = modelMapper.map(freedomCommision, FreedomCommisionDTO.class);
//            freedomCommisionDTOList.add(freedomCommisionDTO);
//        }
//        bankDetailsDTO.setCommissions(freedomCommisionDTOList);
        return bankDetailsDTO;
    }

    @RequireApproval(code = "CREATE_FINANCIAL_INSTITUTE" , entityType = BankDetails.class)
    @Override
    public String createBank(BankDetailsDTO bankDetailsDTO) {

        BankDetails bank = modelMapper.map(bankDetailsDTO, BankDetails.class);

        try {
            bankDetailsRepository.save(bank);

            return messageSource.getMessage("bank.create.success", null, locale);

        } catch (Exception ex) {
            return messageSource.getMessage("bank.create.error", null, locale);
        }
    }

    @Override
    public BankDetailsDTO getBank(Long id) {

        logger.debug("Retrieving Bank Details for [{}]", id);
        BankDetails bankDetails = bankDetailsRepository.findBankDetailsById(id);
        return  convertEntityToDTO(bankDetails);
    }

    @Override
    public String updateBankDetails(BankDetailsDTO bankDetailsDTO) {

        logger.debug("Updating bank details:  {}", bankDetailsDTO);

        return updateUser(bankDetailsDTO);

    }

    private String updateUser(BankDetailsDTO bankDetailsDTO){

        try {
            BankDetails bankDetails = bankDetailsRepository.findBankDetailsById(bankDetailsDTO.getId());

            bankDetails.setBankName(bankDetailsDTO.getBankName());
            bankDetails.setBankCode(bankDetailsDTO.getBankCode());
            bankDetails.setOpayCode(bankDetailsDTO.getOpayCode());
            bankDetails.setAcquirePercentage(bankDetailsDTO.getAcquirePercentage());
            bankDetails.setComissionPercentage(bankDetailsDTO.getComissionPercentage());

            BankDetails updatedBank = bankDetailsRepository.save(bankDetails);
            logger.info("Updated user [{}]", updatedBank);
            return messageSource.getMessage("user.update.success", null, locale);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            throw new AppUserServiceException(messageSource.getMessage("user.update.failure", null, locale), e);
        }
    }

    public List<BankDetailsDTO> convertEntitiesToDTOs(List<BankDetails> bankDetails){
        List<BankDetailsDTO> bankDetailsList = new ArrayList<>();
        for(BankDetails bankDetail: bankDetails){
            BankDetailsDTO bankDetailsDTO = convertEntityToDTO(bankDetail);
            bankDetailsList.add(bankDetailsDTO);
        }
        return bankDetailsList;
    }

}
