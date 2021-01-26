package com._3line.gravity.freedom.thirftmgt.repositories;


import com._3line.gravity.freedom.thirftmgt.models.FreedomThrift;
import com._3line.gravity.freedom.thirftmgt.models.LiquidationHistory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LiquidationHistoryRepository extends JpaRepository<LiquidationHistory, Long> {

    List<LiquidationHistory> findLiquidationHistoryByThrift(FreedomThrift freedomThrift);
}
