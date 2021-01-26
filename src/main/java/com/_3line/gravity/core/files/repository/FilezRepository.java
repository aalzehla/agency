package com._3line.gravity.core.files.repository;

import com._3line.gravity.core.code.model.Code;
import com._3line.gravity.core.files.model.Filez;
import com._3line.gravity.core.repository.AppCommonRepository;

import java.util.List;

public interface FilezRepository extends AppCommonRepository<Filez , Long> {


    List<Filez> findByPurpose(String purpose);
}
