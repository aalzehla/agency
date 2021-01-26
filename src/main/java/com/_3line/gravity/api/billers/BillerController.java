package com._3line.gravity.api.billers;

import com._3line.gravity.api.shared.dto.ApiResponse;
import com._3line.gravity.core.thirdparty.exceptions.ThirdPartyException;
import com._3line.gravity.core.thirdparty.service.ThirdPartyService;
import com._3line.gravity.freedom.billpayment.service.Billservice;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/integration/{appId}/api/v1/biller")
@RestController
public class BillerController {

    @Autowired
    private Billservice billservice;
    @Autowired
    private ThirdPartyService thirdPartyService ;

    @GetMapping("/{categoryId}/")
    public ApiResponse getByCategory(@PathVariable("appId") String appId , @PathVariable ("categoryId") Long Id){

        try {
            thirdPartyService.checkAppId(appId);
            ApiResponse response = new ApiResponse();
            response.setRespCode("00");
            response.setBody(billservice.getServicesForCategory(Id));
            return response;

        }catch (ThirdPartyException ex){
            ApiResponse response = new ApiResponse("999",ex.getMessage(),"");
            return response ;
        }
    }

    @GetMapping("/service/{serviceId}/options")
    public ApiResponse getByOptions(@PathVariable("appId") String appId , @PathVariable ("serviceId") Long Id){

        try {
            thirdPartyService.checkAppId(appId);
            ApiResponse response = new ApiResponse();
            response.setRespCode("00");
            response.setBody(billservice.getOptionsByServiceId(Id));
            return response;

        }catch (ThirdPartyException ex){
            ApiResponse response = new ApiResponse("999",ex.getMessage(),"");
            return response ;
        }
    }
}
