package com._3line.gravity.core.verification.service;

import com._3line.gravity.core.verification.dtos.VerifiableActionDto;
import com._3line.gravity.core.verification.models.VerifiableAction;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface VerifiableActionService {

    String addVerifiableAction(String code);

    boolean isEnabled(String code);

    VerifiableActionDto getOne(Long id);

    String updateVerifiableAction(VerifiableActionDto dto);

    List<VerifiableActionDto> findAll();

    Page<VerifiableActionDto> getAll(Pageable pageable);

    Page<VerifiableActionDto> findUsingPattern(String pattern, Pageable pageDetails);
}
