package com._3line.gravity.freedom.service.tranupdate.service;

import com._3line.gravity.freedom.service.tranupdate.dto.TransactionUpdateDto;
import com._3line.gravity.freedom.service.tranupdate.model.TransactionUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface TransactionUpdateService {

    TransactionUpdateDto updateTransaction(MultipartFile file);

    String pushTransaction(TransactionUpdateDto transactionUpdateDto);

    Page<TransactionUpdate> getUpdates(Pageable pageable);
}
