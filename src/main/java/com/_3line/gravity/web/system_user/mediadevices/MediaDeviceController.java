package com._3line.gravity.web.system_user.mediadevices;


import com._3line.gravity.core.thirdparty.dto.ThirdPartyDto;
import com._3line.gravity.core.thirdparty.exceptions.ThirdPartyException;
import com._3line.gravity.core.thirdparty.service.ThirdPartyService;
import com._3line.gravity.core.verification.exceptions.PendingVerificationException;
import com._3line.gravity.core.verification.exceptions.VerificationException;
import com._3line.gravity.freedom.mediaintegration.dto.AppDeviceDTO;
import com._3line.gravity.freedom.mediaintegration.service.AppDeviceService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.SpecificationBuilder;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;
import java.util.List;
import java.util.Locale;

@Controller
@RequestMapping("/core/media")
public class MediaDeviceController {

    @Autowired
    private AppDeviceService appDeviceService ;

    @Autowired
    private MessageSource messageSource;

    private static Logger logger = LoggerFactory.getLogger(MediaDeviceController.class);

    @GetMapping("/")
    public String index(){
        return "mediadevices/index";
    }

    @GetMapping("/create")
    public String create(Model model){
        model.addAttribute("mediadevice", new AppDeviceDTO());
        return "mediadevices/create";
    }

    @GetMapping("/{id}/view")
    public String view(@PathVariable("id") Long id , Model model){
        System.out.println("got here");
        model.addAttribute("media_setting", appDeviceService.findByMediaId(id));
        return "mediadevices/view";
    }

    @GetMapping(value = "/all")
    @ResponseBody
    public DataTablesOutput<AppDeviceDTO> getAll(DataTablesInput input) {
        Pageable pageable = new SpecificationBuilder<>(input).createPageable();
        List<AppDeviceDTO> codes = appDeviceService.fetchMediaTypes() ;
        DataTablesOutput<AppDeviceDTO> out = new DataTablesOutput<>();
        out.setDraw(input.getDraw());
        out.setData(codes);
        out.setRecordsFiltered(codes.size());
        out.setRecordsTotal(codes.size());
        return out;
    }

    @PostMapping("/create")
    public String addMedia(@Valid @ModelAttribute("mediadevice") AppDeviceDTO appDeviceDTO, BindingResult result, RedirectAttributes redirectAttributes, Model model, Locale locale) {


        if (result.hasErrors()) {
            model.addAttribute("errorMessage", messageSource.getMessage("form.fields.required", null, locale));
            return  "mediadevices/create";
        }

        try {

            appDeviceService.saveMediaType(appDeviceDTO);
            redirectAttributes.addFlashAttribute("successMessage", "Media Added Successfully");
            return "redirect:/core/media/";
        } catch (VerificationException e) {
            redirectAttributes.addFlashAttribute("successMessage", e.getMessage());
            return "redirect:/core/media/";
        } catch (Exception e) {
            e.printStackTrace();
            logger.error(e.getMessage());
            model.addAttribute("errorMessage", e.getMessage());
            return "mediadevices/create";
        }

    }

    @PostMapping("/update")
    public String updateMedia(@ModelAttribute("media_setting") AppDeviceDTO appDeviceDTO, BindingResult result, RedirectAttributes redirectAttributes, Model model, Locale locale) {

        AppDeviceDTO appDeviceDTO1;

        try{
            appDeviceDTO1 = appDeviceService.updateMediaType(appDeviceDTO);
            if(appDeviceDTO1!=null){
                redirectAttributes.addFlashAttribute("successMessage", "Updated Successfully");
            }
        }catch(Exception e){
            e.printStackTrace();
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }

        return "mediadevices/index";

    }





}
