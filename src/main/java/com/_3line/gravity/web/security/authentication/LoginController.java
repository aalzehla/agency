package com._3line.gravity.web.security.authentication;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author FortunatusE
 * @date 11/2/2018
 */
@Controller
public class LoginController {


    @GetMapping("/core/login")
    public String showLoginPage(@RequestParam(value = "failed", required = false) boolean failed, Model model){

        if(failed){
            model.addAttribute("failed", true);
        }
        return "security/login";
    }

}
