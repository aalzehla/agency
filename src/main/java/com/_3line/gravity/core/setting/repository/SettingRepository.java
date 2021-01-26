package com._3line.gravity.core.setting.repository;

import com._3line.gravity.core.repository.AppCommonRepository;
import com._3line.gravity.core.setting.model.Setting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Created by FortunatusE on 8/3/2018.
 */

@Repository
public interface SettingRepository extends AppCommonRepository<Setting, Long> {

    Setting findByCode(String code);

    List<Setting> findByCodeContains(String code);

    boolean existsByCode(String code);
}

