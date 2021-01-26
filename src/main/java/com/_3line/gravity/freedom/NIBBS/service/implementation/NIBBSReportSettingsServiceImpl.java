package com._3line.gravity.freedom.NIBBS.service.implementation;

import com._3line.gravity.freedom.NIBBS.dto.ResetReqDTO;
import com._3line.gravity.freedom.NIBBS.model.NIBBSReportSettings;
import com._3line.gravity.freedom.NIBBS.repository.ReportSettingsRepo;
import com._3line.gravity.freedom.NIBBS.service.NIBBSReportSettingsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NIBBSReportSettingsServiceImpl implements NIBBSReportSettingsService {

    @Autowired
    ReportSettingsRepo  reportSettingsRepo;

    @Override
    public void ping() {

    }

    @Override
    public void reset(ResetReqDTO resetReqDTO) {

    }

    @Override
    public NIBBSReportSettings fetchReportSettings() {
        List<NIBBSReportSettings>  nibbsReportSettings = reportSettingsRepo.findAll();
        NIBBSReportSettings nibbsReportSetting;
        if(nibbsReportSettings!=null && nibbsReportSettings.size()>0){

            nibbsReportSetting = reportSettingsRepo.findAll().get(0);
        }else{
            nibbsReportSetting=  new NIBBSReportSettings();
        }

        return nibbsReportSetting;
    }

    @Override
    public NIBBSReportSettings updateReportSettings(NIBBSReportSettings nibbsReportSettings) {
        System.out.println("incoming obj :: "+nibbsReportSettings);

        NIBBSReportSettings reportSettings = fetchReportSettings();
        reportSettings.setNextRunDate(nibbsReportSettings.getNextRunDate());
        reportSettings.setAutomateReportSending(nibbsReportSettings.getAutomateReportSending());
        reportSettings.setFetchInterval(nibbsReportSettings.getFetchInterval());
        reportSettingsRepo.save(reportSettings);
        return reportSettings;
    }
}
