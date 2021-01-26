package com._3line.gravity.web.system_user.services;

import com._3line.gravity.core.utils.DateUtil;
import com._3line.gravity.freedom.itexintegration.model.PtspDto;
import com._3line.gravity.freedom.itexintegration.service.PosService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.SpecificationBuilder;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping(value = "/core/pos")
public class ItexController {

    @Autowired
    private PosService posService;

    Logger logger = LoggerFactory.getLogger(this.getClass());


    @ModelAttribute
    public void models(Model model) {

        model.addAttribute("yesterday", DateUtil.formatDateToreadable_(DateUtil.yesterday())) ;
        model.addAttribute("today",DateUtil.formatDateToreadable_(DateUtil.tomorrow())) ;

    }


    @PreAuthorize("hasAuthority('VIEW_ITEX_LOGS')")
    @GetMapping("/")
    public String view() {
        return "itex/view";
    }

    @RequestMapping(value = "/all" , method = RequestMethod.GET)
    @ResponseBody
    public DataTablesOutput<PtspDto> getPosNotifications(DataTablesInput input,
                                                         @RequestParam("rrn") String rrn , @RequestParam("terminalId") String terminalId,
                                                         @RequestParam("from") String from , @RequestParam("to") String to ,
                                                         @RequestParam("uploadStatus") String uploadStatus) {
        try {
            Pageable pageable = new SpecificationBuilder<>(input).createPageable();
//            System.out.println(rrn+" "+terminalId+" "+from+" "+to);
            Page<PtspDto> ptspDtos = posService.findAllPtspDTOPageable(uploadStatus,rrn, terminalId , from , to , pageable);
            DataTablesOutput<PtspDto> out = new DataTablesOutput<PtspDto>();
            out.setDraw(input.getDraw());
            out.setData(ptspDtos.getContent());
            out.setRecordsFiltered(ptspDtos.getTotalElements());
            out.setRecordsTotal(ptspDtos.getTotalElements());
            return out;
        }catch (Exception e){
            e.printStackTrace();
        }

        return null ;
    }
}
