package com._3line.gravity.freedom.itexintegration.service;


import com._3line.gravity.freedom.itexintegration.model.PtspDto;
import com._3line.gravity.freedom.itexintegration.model.PtspModel;
import com._3line.gravity.freedom.itexintegration.repository.PtspRepository;
import com._3line.gravity.freedom.utility.DateUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PosService {

    @Autowired
    private PtspRepository ptspRepository;


    private ModelMapper modelMapper = new ModelMapper();

    Logger logger = LoggerFactory.getLogger(this.getClass());


    public Page<PtspDto> findAllPtspDTOPageable(String isverified,String rrn , String terminalId , String from , String to , Pageable pageable) {

        List<PtspModel> ptspModels = null;
        if(!isverified.equals("")){
            ptspModels =  ptspRepository.findByIsVerifiedAndRrnContainsAndTerminalIdContainsAndCreatedOnBetweenOrderByIdDesc(Boolean.valueOf(isverified),rrn, terminalId , DateUtil.dateFullFormat(from), DateUtil.AddDays(DateUtil.dateFullFormat(to),1));
        }else{
            ptspModels = ptspRepository.findByRrnContainsAndTerminalIdContainsAndCreatedOnBetweenOrderByIdDesc(rrn, terminalId , DateUtil.dateFullFormat(from), DateUtil.AddDays(DateUtil.dateFullFormat(to),1));
        }
        List<PtspDto> dtOs = convertEntitiesToDTOs(ptspModels);
        Page<PtspDto> pageImpl = new PageImpl<PtspDto>(dtOs, pageable, ptspModels.size());
        return pageImpl;
    }

    public PtspDto convertEntityToDto(PtspModel ptspModel){
        PtspDto ptspDto = modelMapper.map(ptspModel, PtspDto.class);
        return ptspDto;
    }

    public List<PtspDto> convertEntitiesToDTOs(List<PtspModel> ptspModels){
        List<PtspDto> ptspDtoList = new ArrayList<>();
        for(PtspModel ptspModel: ptspModels){
            PtspDto ptspDto = convertEntityToDto(ptspModel);
            ptspDtoList.add(ptspDto);
        }
        return ptspDtoList;
    }

}
