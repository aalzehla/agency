package com._3line.gravity.freedom.NIBBS.service;

import com._3line.gravity.freedom.NIBBS.dto.ResetReqDTO;
import com._3line.gravity.freedom.NIBBS.model.NIBBSReportSettings;

public interface NIBBSReportSettingsService {

    void ping();

    void reset(ResetReqDTO resetReqDTO);

    NIBBSReportSettings fetchReportSettings();

    NIBBSReportSettings updateReportSettings(NIBBSReportSettings nibbsReportSettings);

}
