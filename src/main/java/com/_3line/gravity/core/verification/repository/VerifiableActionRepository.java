package com._3line.gravity.core.verification.repository;

import com._3line.gravity.core.repository.AppCommonRepository;
import com._3line.gravity.core.verification.models.VerifiableAction;

public interface VerifiableActionRepository extends AppCommonRepository<VerifiableAction , Long> {

    VerifiableAction findByCode(String code);
}
