package com._3line.gravity.web.system_user.services;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(value = "/core/service/agent")
public class AgentUploadController {


    @PreAuthorize("hasAuthority('PERFORM_AGENT_BULK_UPLOAD')")
    @GetMapping("/")
    public String view() {
        return "itex/view";
    }

}
