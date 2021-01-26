package com._3line.gravity.api.pos;

import com._3line.gravity.api.shared.dto.ApiResponse;
import com._3line.gravity.core.thirdparty.dto.ThirdPartyDto;
import com._3line.gravity.core.thirdparty.exceptions.ThirdPartyException;
import com._3line.gravity.core.thirdparty.service.ThirdPartyService;
import com._3line.gravity.freedom.itexintegration.model.PtspDto;
import com._3line.gravity.freedom.itexintegration.service.PtspService;
import com.google.gson.Gson;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RequestMapping("/integration/{appId}/api/v1/pos")
@RestController
public class PosController {
    private static Logger logger = LoggerFactory.getLogger(PosController.class);

    @Autowired
    private PtspService ptspService ;
    @Autowired
    private ThirdPartyService thirdPartyService ;

    @PostMapping("/transaction/log/")
    public String logTransaction(@PathVariable("appId") String appId , @RequestBody Map<String , String> body) {

        try {
            thirdPartyService.checkAppId(appId);
            ThirdPartyDto thirdPartyDto = thirdPartyService.getThridParty(appId);
            String request = thirdPartyService.decryptDataForParty(appId , body.get("request"));
            PtspDto dto = new Gson().fromJson(request ,PtspDto.class);
            dto.setPtsp(thirdPartyDto.getClientName());
            logger.info("decrypted request .....{}",dto.toString());
            String response = ptspService.savePtspDetails(dto,"PTSP");
            logger.info("response    .....{}",response);
            return response ;
        }catch (ThirdPartyException ex){
            ex.printStackTrace();
            ApiResponse response = new ApiResponse("999",ex.getMessage(),"");
            return new Gson().toJson(response);
        }catch (Exception e){
            ApiResponse response = new ApiResponse("400","Cannot Decrypt request , ensure you are encrypting properly","");
            return new Gson().toJson(response);
        }

    }

    public static void main(String...args){
        String data = "";

    }
}
