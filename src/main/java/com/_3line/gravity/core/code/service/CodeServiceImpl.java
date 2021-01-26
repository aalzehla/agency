package com._3line.gravity.core.code.service;

import com._3line.gravity.core.code.dto.CodeDTO;
import com._3line.gravity.core.code.model.Code;
import com._3line.gravity.core.code.model.CodeType;
import com._3line.gravity.core.code.repository.CodeRepository;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.verification.annotations.RequireApproval;
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
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author FortunatusE
 * @date 11/6/2018
 */

@Service
public class CodeServiceImpl implements CodeService {


    private static final Logger logger = LoggerFactory.getLogger(CodeServiceImpl.class);
    private static final Locale locale = LocaleContextHolder.getLocale();

    private CodeRepository codeRepository;
    private MessageSource messageSource;
    private ModelMapper modelMapper;


    @Autowired
    public CodeServiceImpl(CodeRepository codeRepository, MessageSource messageSource, ModelMapper modelMapper) {
        this.codeRepository = codeRepository;
        this.messageSource = messageSource;
        this.modelMapper = modelMapper;
    }



    @Override
    public CodeDTO getCode(Long id) {

        logger.debug("Retrieving code with Id [{}]", id);
        Code code = this.codeRepository.findOne(id);
        return convertEntityToDTO(code);
    }

    @Override
    public Code getCodeById(Long id) {

        logger.debug("Retrieving code with Id [{}]", id);
        Code code = this.codeRepository.findOne(id);
        return code;
    }

    @Override
    public List<CodeDTO> getCodesByType(String codeType) {

        logger.debug("Retrieving code with type [{}]", codeType);
        Iterable<Code> codes = this.codeRepository.findByType(codeType);
        return convertEntitiesToDTOs(codes);
    }

    @Override
    public Iterable<CodeDTO> getCodes() {
        Iterable<Code> codes = this.codeRepository.findAll();
        return convertEntitiesToDTOs(codes);
    }


    @Transactional
    public String updateCode(CodeDTO codeDTO) throws GravityException {

        logger.debug("Updating code: {}", codeDTO);
        try {
            Code code = convertDTOToEntity(codeDTO);
            codeRepository.save(code);
            logger.info("Updated code with Id {}", code.getId());
            return messageSource.getMessage("code.update.success", null, locale);
        } catch (Exception e) {
            throw new GravityException(messageSource.getMessage("code.update.failure", null, locale), e);
        }


    }


    public CodeDTO convertEntityToDTO(Code code) {
        return modelMapper.map(code, CodeDTO.class);
    }


    public Code convertDTOToEntity(CodeDTO codeDTO) {
        return modelMapper.map(codeDTO, Code.class);
    }

    public List<CodeDTO> convertEntitiesToDTOs(Iterable<Code> codes) {
        List<CodeDTO> codeDTOList = new ArrayList<>();
        for (Code code : codes) {
            CodeDTO codeDTO = convertEntityToDTO(code);
            codeDTOList.add(codeDTO);
        }
        return codeDTOList;
    }




    @Override
    public Page<CodeDTO> getCodesByType(String codeType, Pageable pageDetails) {
        Page<Code> page = codeRepository.findByType(codeType, pageDetails);
        List<CodeDTO> dtOs = convertEntitiesToDTOs(page);
        long t = page.getTotalElements();
        Page<CodeDTO> pageImpl = new PageImpl<CodeDTO>(dtOs, pageDetails, t);
        return pageImpl;
    }

    @Override
    public Page<CodeDTO> findByTypeAndValue(String codeType, String value, Pageable pageDetails) {
        Page<Code> page = codeRepository.findByTypeAndValue(codeType, value, pageDetails);
        List<CodeDTO> dtOs = convertEntitiesToDTOs(page);
        long t = page.getTotalElements();
        Page<CodeDTO> pageImpl = new PageImpl<CodeDTO>(dtOs, pageDetails, t);
        return pageImpl;
    }

    @Override
    public Page<CodeDTO> getCodes(Pageable pageDetails) {

        logger.debug("Retrieving codes");
        Page<Code> page = codeRepository.findAll(pageDetails);
        List<CodeDTO> dtOs = convertEntitiesToDTOs(page.getContent());
        long t = page.getTotalElements();
        Page<CodeDTO> pageImpl = new PageImpl<CodeDTO>(dtOs, pageDetails, t);
        return pageImpl;
    }


//    @RequireApproval(code = "ADD_CODE" , entityType = Code.class)
    @Override
    public String addCode(CodeDTO codeDTO) throws GravityException {
        try {
            Code code = convertDTOToEntity(codeDTO);
            codeRepository.save(code);
            logger.info("Added new code {} of type {}", code.getDescription(), code.getType());
            return messageSource.getMessage("code.add.success", null, locale);
        } catch (Exception e) {
            throw new GravityException(messageSource.getMessage("code.add.failure", null, locale), e);
        }
    }

    @Override

    public Code getByTypeAndCode(String type, String code) {
        return codeRepository.findByTypeAndCode(type, code);
    }


    @Override
    public Page<CodeType> getCodeTypes(Pageable pageDetails) {

        Page<String> allTypes = codeRepository.findAllTypes(pageDetails);
        long t = allTypes.getTotalElements();
        List<CodeType> list = new ArrayList<CodeType>();
        for (String s : allTypes) {
            list.add(new CodeType(s));
        }
        return new PageImpl<CodeType>(list, pageDetails, t);
    }

    @Override
    public Page<CodeType> findByCodeType(String codeType, Pageable pageDetails) {

        Page<String> allTypes = codeRepository.findByCodeType(codeType, pageDetails);
        long t = allTypes.getTotalElements();
        List<CodeType> list = new ArrayList<CodeType>();
        for (String s : allTypes) {
             list.add(new CodeType(s));

        }
        return new PageImpl<CodeType>(list, pageDetails, t);
    }

    @Override
    @Transactional
    public String deleteCode(Long codeId) throws GravityException {
        try {
            Code code = codeRepository.findOne(codeId);
            codeRepository.delete(code);
            logger.info("Code {} has been deleted", codeId.toString());
            return messageSource.getMessage("code.delete.success", null, locale);
        }     catch (Exception e) {
            throw new GravityException(messageSource.getMessage("code.delete.failure", null, locale), e);
        }
    }

}
