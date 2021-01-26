package com._3line.gravity.freedom.NIBBS.repository;


import com._3line.gravity.freedom.NIBBS.model.NIBBSReportSettings;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReportSettingsRepo extends JpaRepository<NIBBSReportSettings,Long>{


}
