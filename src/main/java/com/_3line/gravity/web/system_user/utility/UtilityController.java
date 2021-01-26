package com._3line.gravity.web.system_user.utility;

import com._3line.gravity.api.account.dto.*;
import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.core.files.service.FileService;
import com._3line.gravity.core.models.Response;
import com._3line.gravity.freedom.financialInstitutions.dtos.AccOpeningGeneral;
import com._3line.gravity.freedom.financialInstitutions.sanef.dto.SanefAccountOpeningResponse;
import com._3line.gravity.freedom.financialInstitutions.sanef.dto.SanefAccountRequest;
import com._3line.gravity.freedom.financialInstitutions.sanef.dto.SanefAgentCreation;
import com._3line.gravity.freedom.financialInstitutions.sanef.dto.SanefAgentCreationResponse;
import com._3line.gravity.freedom.financialInstitutions.sanef.service.SanefService;
import com._3line.gravity.freedom.financialInstitutions.stanbicibtc.dto.StanbicOTPDto;
import com._3line.gravity.freedom.financialInstitutions.stanbicibtc.model.request.StanbicAccountOpeningRequest;
import com._3line.gravity.freedom.financialInstitutions.stanbicibtc.services.StanbicIBTCServices;
import com._3line.gravity.freedom.gravitymobile.service.AccountOpeningService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;

@CrossOrigin(origins = "*", maxAge = 3600)
@Controller
@RequestMapping(value = "/core/utility")
public class UtilityController {



    @Autowired
    private AccountOpeningService accountOpeningService ;

    @Autowired
    private StanbicIBTCServices stanbicIBTCServices;

    @Autowired
    SanefService sanefService;

    @Autowired
    ModelMapper modelMapper;

    private ObjectMapper objectMapper = new ObjectMapper();

    private Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    FileService fileService;


    @RequestMapping(path = "/image/{imagename}", method = RequestMethod.GET)
    public ResponseEntity<Resource> download(@PathVariable  String imagename) throws IOException {


        File file = fileService.loadFileAsResource(imagename).getFile();
        InputStreamResource resource = new InputStreamResource(new FileInputStream(file));


        return ResponseEntity
                    .ok()
                    .contentType(MediaType.IMAGE_PNG)
                    .body(resource);
    }

}
