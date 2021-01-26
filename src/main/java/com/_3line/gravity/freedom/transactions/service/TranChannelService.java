package com._3line.gravity.freedom.transactions.service;

import com._3line.gravity.freedom.transactions.models.TranChannel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

public interface TranChannelService {

    //This method is used to find All the Banks Details
    List<TranChannel> getChannels();

    //This method is used to find All the Banks Details pageable format
    Page<TranChannel> getChannelsPageable(Pageable pageable);

    //This method is used to find one Bank Detail
    TranChannel findOne(Long id);

    TranChannel findByName(String channelName);


    @PreAuthorize("hasAuthority('MANAGE_FINANCIAL_INSTITUTIONS')")
    String createBank(TranChannel tranChannel);


    @PreAuthorize("hasAuthority('MANAGE_FINANCIAL_INSTITUTIONS')")
    String updateBankDetails(TranChannel tranChannel);
}
