package com._3line.gravity.freedom.wallet.repository;

import com._3line.gravity.freedom.wallet.models.NIBBSReconcilation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NIBSSReconcilationRepository extends JpaRepository<NIBBSReconcilation, Long> {

    List<NIBBSReconcilation> findByRemark(String remark);
}
