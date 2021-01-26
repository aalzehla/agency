package com._3line.gravity.freedom.thirftmgt.services;


import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.freedom.thirftmgt.dtos.ContributionDTO;
import com._3line.gravity.freedom.thirftmgt.dtos.ThriftDTO;
import com._3line.gravity.freedom.thirftmgt.dtos.ThriftLiquidationDTO;
import com._3line.gravity.freedom.thirftmgt.models.FreedomThrift;

import java.util.List;

public interface ThriftService {

    String register(ThriftDTO registrationDTO) throws GravityException;

    ThriftDTO checkContribution(String cardNumber) throws GravityException;

    String contribute(ContributionDTO contributionDTO) throws GravityException ;

    String liquidate(String cardNumber) throws GravityException;

    String prematureLiquidation(ThriftLiquidationDTO liquidationDTO) throws GravityException;

     ThriftDTO convertEntityToDto(FreedomThrift thrift) throws GravityException;

     ThriftDTO getByCardOrPhone(String search) throws GravityException;

     List<ThriftDTO> getAll() throws GravityException;


}
