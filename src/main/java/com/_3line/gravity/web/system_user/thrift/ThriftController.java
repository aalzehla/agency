package com._3line.gravity.web.system_user.thrift;


import com._3line.gravity.freedom.agents.repository.AgentsRepository;
import com._3line.gravity.freedom.agents.service.AgentService;
import com._3line.gravity.freedom.thirftmgt.dtos.ThriftDTO;
import com._3line.gravity.freedom.thirftmgt.services.ThriftService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.datatables.SpecificationBuilder;
import org.springframework.data.jpa.datatables.mapping.DataTablesInput;
import org.springframework.data.jpa.datatables.mapping.DataTablesOutput;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/core/thrift")
public class ThriftController {

    @Autowired
    ThriftService thriftService ;
    @Autowired
    AgentsRepository agentsRepository;


    @GetMapping("/")
    public String index(){
        return "thrift/index";
    }

    @GetMapping("/{phone}/")
    public String profile(@PathVariable("phone") String phone, Model model){
        ThriftDTO thriftDTO = thriftService.getByCardOrPhone(phone);
        model.addAttribute("customer", thriftDTO);
        model.addAttribute("agent", agentsRepository.findByUsername(thriftDTO.getAgentName()));
        return "thrift/profile";
    }

    @GetMapping(value = "/all")
    @ResponseBody
    public DataTablesOutput<ThriftDTO> getAllAgents(DataTablesInput input) {
        Pageable pageable = new SpecificationBuilder<>(input).createPageable();
        List<ThriftDTO> codes =thriftService.getAll() ;
        DataTablesOutput<ThriftDTO> out = new DataTablesOutput<>();
        out.setDraw(input.getDraw());
        out.setData(codes);
        out.setRecordsFiltered(codes.size());
        out.setRecordsTotal(codes.size());
        return out;
    }


}
