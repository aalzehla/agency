package com._3line.gravity.freedom.transactions.service.implementation;

import com._3line.gravity.core.exceptions.GravityException;
import com._3line.gravity.freedom.transactions.models.TranChannel;
import com._3line.gravity.freedom.transactions.repositories.TranChannelsRepository;
import com._3line.gravity.freedom.transactions.service.TranChannelService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TranChannelServiceImpl implements TranChannelService {

    @Autowired
    TranChannelsRepository tranChannelsRepository;

    @Override
    public List<TranChannel> getChannels() {
        return tranChannelsRepository.findAll();
    }

    @Override
    public Page<TranChannel> getChannelsPageable(Pageable pageable) {
        return tranChannelsRepository.findAll(pageable);
    }

    @Override
    public TranChannel findOne(Long id) {
        return tranChannelsRepository.findById(id).orElse(null);
    }

    @Override
    public TranChannel findByName(String channelName) {
        return tranChannelsRepository.findByChannelName(channelName);
    }

    @Override
    public String createBank(TranChannel tranChannel) {
        if(tranChannel!=null && tranChannel.getChannelName()!=null){
            TranChannel check = tranChannelsRepository.findByChannelName(tranChannel.getChannelName());
            if(check!=null){
                throw new GravityException("Channel Details already exists");
            }
            tranChannelsRepository.save(tranChannel);
            return "saved successfully";
        }else{
            throw new GravityException("Channel Details Cannot be Null");
        }
    }

    @Override
    public String updateBankDetails(TranChannel tranChannel) {
        TranChannel check = tranChannelsRepository.findByChannelName(tranChannel.getChannelName());
        if(check==null){
            throw new GravityException("Channel Details does not exist");
        }
        check.setAcquirerPercentageFee(tranChannel.getAcquirerPercentageFee());
        check.setMaxAcquirerFee(tranChannel.getMaxAcquirerFee());
        check.setMinimumAcquirerFee(tranChannel.getMinimumAcquirerFee());
        check.setMinimumPosTerminalFee(tranChannel.getMinimumPosTerminalFee());
        check.setMaxPosTerminalFee(tranChannel.getMaxPosTerminalFee());
        check.setPosTerminalPercentageFee(tranChannel.getPosTerminalPercentageFee());
        check.setMaxPerMiscAcquirerFee(tranChannel.getMaxPerMiscAcquirerFee());
        tranChannelsRepository.save(check);
        return "updated successfully";
    }
}
