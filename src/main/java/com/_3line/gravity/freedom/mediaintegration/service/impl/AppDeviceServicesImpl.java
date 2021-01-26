package com._3line.gravity.freedom.mediaintegration.service.impl;

import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.usermgt.exception.UserNotFoundException;
import com._3line.gravity.freedom.mediaintegration.dto.AppDeviceDTO;
import com._3line.gravity.freedom.mediaintegration.model.AppDevice;
import com._3line.gravity.freedom.mediaintegration.repository.AppDeviceRepo;
import com._3line.gravity.freedom.mediaintegration.service.AppDeviceService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


@Service
public class AppDeviceServicesImpl implements AppDeviceService {

    @Autowired
    AppDeviceRepo appDeviceRepo;

    @Autowired
    ModelMapper modelMapper;

    @Override
    public AppDeviceDTO saveMediaType(AppDeviceDTO mediaTypeDTO) {

        AppDevice mediaType = appDeviceRepo.findByDeviceType(mediaTypeDTO.getDeviceType());
        if(mediaType!=null){
            throw new GravityException("Record Already Exist");
        }else{
            mediaType = new AppDevice();
            mediaType.setAppVersion(mediaTypeDTO.getAppVersion());
            mediaType.setDeviceType(mediaTypeDTO.getDeviceType());
            mediaType.setUpdateBy("owolabi");
        }
        appDeviceRepo.save(mediaType);
        return mediaTypeDTO;
    }

    @Override
    public AppDeviceDTO updateMediaType(AppDeviceDTO mediaTypeDTO) {


        AppDevice mediaType = appDeviceRepo.findById(Long.valueOf(mediaTypeDTO.getId())).orElse(null);

        if(mediaType != null){

            mediaType.setDeviceType(mediaTypeDTO.getDeviceType());
            mediaType.setAppVersion(mediaTypeDTO.getAppVersion());
            mediaType.setUpdatedOn(new Date());
            mediaType.setUpdateBy("owolabi");

            appDeviceRepo.save(mediaType);

            return mediaTypeDTO;
        }else{
            throw new GravityException("Record Not Found");
        }

    }

    @Override
    public List<AppDeviceDTO> fetchMediaTypes() {
        List<AppDevice> mediaTypes = appDeviceRepo.findAll();
        List<AppDeviceDTO> mediaTypeDTOS = new ArrayList<>();
        mediaTypes.forEach(mediaType -> {
            AppDeviceDTO typeDTO = modelMapper.map(mediaType,AppDeviceDTO.class);
            mediaTypeDTOS.add(typeDTO);
        });

        return mediaTypeDTOS;
    }

    @Override
    public AppDeviceDTO findByMediaType(String mediaType) {
        AppDevice mediaType1 = appDeviceRepo.findByDeviceType(mediaType);
        AppDeviceDTO typeDTO = modelMapper.map(mediaType1,AppDeviceDTO.class);
        return typeDTO;
    }

    @Override
    public AppDeviceDTO findByMediaId(Long id) {
        AppDevice mediaType1 = appDeviceRepo.findById(id).orElse(null);
        if(mediaType1!=null){
            AppDeviceDTO typeDTO = modelMapper.map(mediaType1,AppDeviceDTO.class);
            return typeDTO;
        }else{
            throw new GravityException("No Record Found");
        }

    }


}
