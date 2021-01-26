package com._3line.gravity.core.verification.service.impl;

import com._3line.gravity.core.verification.dtos.VerifiableActionDto;
import com._3line.gravity.core.verification.exceptions.VerificableActionException;
import com._3line.gravity.core.verification.models.VerifiableAction;
import com._3line.gravity.core.verification.repository.VerifiableActionRepository;
import com._3line.gravity.core.verification.service.VerifiableActionService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

@Service
public class VerifiableActionServiceImpl implements VerifiableActionService {


    private static final Logger logger = LoggerFactory.getLogger(VerifiableActionService.class);

    @Autowired
    private VerifiableActionRepository verifiableActionRepository ;
    private ModelMapper modelMapper = new ModelMapper() ;
    @Autowired
    private MessageSource messageSource;
    private final Locale locale = LocaleContextHolder.getLocale() ;

    @Override
    public String addVerifiableAction(String code) throws VerificableActionException {

        if(Objects.isNull(verifiableActionRepository.findByCode(code))){
            VerifiableAction verifiableAction = new VerifiableAction() ;
            verifiableAction.setCode(code);
            verifiableActionRepository.save(verifiableAction);
        }else {
            throw  new VerificableActionException("Action with code already exists");
        }
        return messageSource.getMessage("action.add.success", null, locale);
    }

    @Override
    public boolean isEnabled(String code) {
        VerifiableAction action = verifiableActionRepository.findByCode(code);
        if(Objects.nonNull(action)){
            return action.isEnabled() ;
        }else {
            throw  new VerificableActionException("Action with code { "+code+" } does not exist");
        }
    }

    @Override
    public VerifiableActionDto getOne(Long id) {
        VerifiableAction action = verifiableActionRepository.findOne(id);
        return convertEntityToDto(action);
    }

    @Override
    public String updateVerifiableAction(VerifiableActionDto dto) {

        VerifiableAction verifiableAction = convertDtoToEntity(dto);
        logger.info("dto {} ", dto);
        logger.info("model {}" , verifiableAction);
        verifiableActionRepository.save(verifiableAction);
        return messageSource.getMessage("action.update.success", null, locale);
    }

    @Override
    public List<VerifiableActionDto> findAll() {

        Iterable<VerifiableAction> actions = verifiableActionRepository.findAll() ;

        List<VerifiableActionDto> res = new ArrayList<>();

        actions.forEach( a -> {
            res.add(convertEntityToDto(a));
        });

        return res ;
    }

    @Override
    public Page<VerifiableActionDto> getAll(Pageable pageable) {

        Page<VerifiableAction> page = verifiableActionRepository.findAll(pageable);
        List<VerifiableActionDto> list = new ArrayList<>();
        page.getContent().forEach( p -> {
            list.add(convertEntityToDto(p));
        });
        return new PageImpl<>(list,pageable, page.getTotalElements());
    }

    @Override
    public Page<VerifiableActionDto> findUsingPattern(String pattern, Pageable pageDetails) {
        Page<VerifiableAction> page = verifiableActionRepository.findUsingPattern(pattern, pageDetails);
        List<VerifiableActionDto> list = new ArrayList<>();
        page.getContent().forEach( p -> {
            list.add(convertEntityToDto(p));
        });
        return new PageImpl<>(list,pageDetails, page.getTotalElements());
    }



    VerifiableAction convertDtoToEntity(VerifiableActionDto dto){
        return modelMapper.map(dto , VerifiableAction.class);
    }

    VerifiableActionDto convertEntityToDto(VerifiableAction action){
        return modelMapper.map(action , VerifiableActionDto.class);
    }
}
