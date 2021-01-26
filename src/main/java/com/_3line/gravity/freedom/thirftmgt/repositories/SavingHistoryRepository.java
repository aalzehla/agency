package com._3line.gravity.freedom.thirftmgt.repositories;

import com._3line.gravity.freedom.thirftmgt.models.FreedomThrift;
import com._3line.gravity.freedom.thirftmgt.models.SavingHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SavingHistoryRepository extends JpaRepository<SavingHistory, Long> {

    List<SavingHistory> findByCycleUID(String cycleUID);

    List<SavingHistory> findSavingHistoriesByThrift(FreedomThrift freedomThrift);

}
