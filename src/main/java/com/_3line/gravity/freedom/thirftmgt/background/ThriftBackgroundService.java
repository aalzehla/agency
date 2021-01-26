package com._3line.gravity.freedom.thirftmgt.background;

import com._3line.gravity.freedom.thirftmgt.models.FreedomThrift;
import com._3line.gravity.freedom.thirftmgt.repositories.FreedomThriftRepository;
import com._3line.gravity.freedom.thirftmgt.services.ThriftService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

@Service
public class ThriftBackgroundService {

    Logger logger = LoggerFactory.getLogger(this.getClass());

    @Autowired
    ThriftService thriftService ;
    @Autowired
    FreedomThriftRepository freedomThriftRepository ;

    @Async
    @Scheduled(cron = "${thrift.cron-schedule}")
    public void liquidateDueThrift(){
        logger.info("################################## BEGINING TIRFT LIQUIDATION -BACKGROUND SERVICE #####################");
        List<FreedomThrift> thrifts = freedomThriftRepository.findAllByNextliquidationDate(new Date());
        logger.info("{} due thrifts found",thrifts.size());
        thrifts.forEach( t ->{
            try {
                logger.info("#####  ->BEGINING LIQUIDATION FOR <-{}",t.getCardNumber());
                thriftService.liquidate(t.getCardNumber());
                logger.info("#####  ->DONE LIQUIDATION FOR <-{}",t.getCardNumber());
            }catch (Exception e){
                logger.info("error liquidating {}",e.getMessage());
                e.printStackTrace();
                logger.info("error ends for {}", t.getCardNumber());
            }
        });
    }
}
