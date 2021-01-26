package com._3line.gravity.freedom.mediaintegration.repository;

import com._3line.gravity.freedom.mediaintegration.model.AppDevice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AppDeviceRepo extends JpaRepository<AppDevice,Long> {

    AppDevice findByDeviceType(String deviceType);

}
