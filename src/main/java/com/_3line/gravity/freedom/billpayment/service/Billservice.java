package com._3line.gravity.freedom.billpayment.service;


import com._3line.gravity.freedom.billpayment.dto.BillPaymentViewDto;
import com._3line.gravity.freedom.billpayment.dto.BillServicesViewDto;

import com._3line.gravity.freedom.agents.models.Agents;
import com._3line.gravity.freedom.billpayment.dtos.BillPaymentDto;
import com._3line.gravity.freedom.billpayment.dtos.BillServicesDto;
import com._3line.gravity.freedom.billpayment.dtos.ValidateCustomerRequest;
import com._3line.gravity.freedom.billpayment.dtos.ValidateCustomerResponse;
import com._3line.gravity.freedom.billpayment.models.BillCategory;
import com._3line.gravity.freedom.billpayment.models.BillOption;
import com._3line.gravity.freedom.billpayment.models.BillServices;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.Collection;
import java.util.List;

/**
 * @author JoyU
 * @date 10/15/2018
 */

public interface Billservice {
    List<BillCategory> getAllCategories();

    BillPaymentDto convertEntityToDTO(Agents agents);

    BillPaymentDto convertEntityToDTO(BillServicesDto agents);

    Page<BillServicesDto> getBillservices(Pageable pageDetails);

    Collection<BillServices> getServicesByCategoryId(Long id);

    Collection<BillServices> getServicesForCategory(Long id);

    Collection<BillOption> getOptionsByServiceId(Long id);

    String updateBillServices() throws Exception;

    BillPaymentDto payBills(BillPaymentDto billPaymentDto);

    ValidateCustomerResponse validate(ValidateCustomerRequest request,Agents agent);

    void updateCategory(Long serviceId, Long category);

    Page<BillServicesViewDto> findAllBillServiceDTOPageable(Pageable pageable);

    BillServicesViewDto getBillById(Long id);
    @PreAuthorize("hasAuthority('MANAGE_BILLERS')")
    String updateService(BillServicesViewDto billServicesViewDto);

    Page<BillPaymentViewDto> findAllBillPaymentDTOPageable(Pageable pageable);
}
