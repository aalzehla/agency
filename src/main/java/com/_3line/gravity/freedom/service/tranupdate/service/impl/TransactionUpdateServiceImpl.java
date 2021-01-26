package com._3line.gravity.freedom.service.tranupdate.service.impl;

import com._3line.gravity.core.code.service.CodeService;
import com._3line.gravity.core.email.service.MailService;
import com._3line.gravity.core.files.model.Filez;
import com._3line.gravity.core.files.service.FileService;
import com._3line.gravity.core.utils.AppUtility;
import com._3line.gravity.core.verification.annotations.RequireApproval;
import com._3line.gravity.freedom.itexintegration.model.PtspDto;
import com._3line.gravity.freedom.itexintegration.model.PtspModel;
import com._3line.gravity.freedom.itexintegration.repository.PtspRepository;
import com._3line.gravity.freedom.itexintegration.service.PtspService;
import com._3line.gravity.freedom.service.tranupdate.dto.TranFromExcel;
import com._3line.gravity.freedom.service.tranupdate.dto.TransactionUpdateDto;
import com._3line.gravity.freedom.service.tranupdate.model.TransactionUpdate;
import com._3line.gravity.freedom.service.tranupdate.repository.TransactionUpdateRepository;
import com._3line.gravity.freedom.service.tranupdate.service.TransactionUpdateService;
import com._3line.gravity.freedom.utility.FileUtility;
import com.google.gson.Gson;
import io.github.mapper.excel.ExcelMapper;
import org.apache.poi.ss.usermodel.DateUtil;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Service
public class TransactionUpdateServiceImpl implements TransactionUpdateService {
    @Autowired
    private PtspRepository ptspRepository;
    private static Logger logger = LoggerFactory.getLogger(TransactionUpdate.class);

    private TransactionUpdateRepository transactionUpdateRepository;
    private FileService fileService;
    private ModelMapper modelMapper;
    private MessageSource messageSource;
    private PtspService ptspService;
    private MailService mailService;
    private Gson gson;
    private CodeService codeService;
    private final ExecutorService executors = Executors.newFixedThreadPool(100);

    private final Locale locale = LocaleContextHolder.getLocale();


    @Autowired
    public TransactionUpdateServiceImpl(TransactionUpdateRepository transactionUpdateRepository, FileService fileService, CodeService codeService, ModelMapper modelMapper, MessageSource messageSource, PtspService ptspService, MailService mailService) {
        this.transactionUpdateRepository = transactionUpdateRepository;
        this.fileService = fileService;
        this.modelMapper = modelMapper;
        this.messageSource = messageSource;
        this.ptspService = ptspService;
        this.gson = new Gson();
        this.mailService = mailService;
        this.codeService = codeService;
    }

    @Override
    public TransactionUpdateDto updateTransaction(MultipartFile file) {
        logger.info("saving file ..........");
        Long fileId = fileService.storeFile(file, "TRANSACTION_UPDATE");

        TransactionUpdateDto transactionUpdateDto = new TransactionUpdateDto();
        transactionUpdateDto.setFileId(fileId);
        transactionUpdateDto.setInitiatedBy(AppUtility.getCurrentUserName());
        countTransactions(transactionUpdateDto);

        // send mail to team here
        return transactionUpdateDto;
    }


    // Send for verification here
    @RequireApproval(code = "TERMINAL_TRANSACTION_UPDATE", entityType = TransactionUpdate.class)
    public String pushTransaction(TransactionUpdateDto transactionUpdateDto) {

        transactionUpdateDto.setApprovedBy(AppUtility.getCurrentUserName());

        updateTransaction(transactionUpdateDto);


        return messageSource.getMessage("agent.add.success", null, locale);
    }

    private void updateTransaction(TransactionUpdateDto transactionUpdateDto) {
        try {

            File file = fileService.loadFileAsResource(transactionUpdateDto.getFileId()).getFile();
            String bankReportFIle = file.getName();

            System.out.println("bankReportFIle :: "+bankReportFIle);
            List<TranFromExcel> dtos = new ArrayList<>();
            dtos = ExcelMapper.mapFromExcel(file)
                    .toObjectOf(TranFromExcel.class)
                    .fromSheet(0) // if this method not used , called all sheets
                    .map();

            Integer successCount = 0;
            Integer failureCount = 0;

            for (TranFromExcel d : dtos) {
                logger.info("transaction is {}", d.toString());
                try {
                    PtspDto ptspDto = new PtspDto();
                    ptspDto.setTerminalId(d.getTerminal());
                    ptspDto.setRrn(d.getRrn());
                    ptspDto.setPan(d.getPan());
                    //added 00 at the back to signify kobo
                    ptspDto.setAmount(Double.parseDouble(d.getAmount() + "00"));
                    ptspDto.setStan(d.getStan());
                    ptspDto.setBank("NIBBS");
                    ptspDto.setVerifiedBy(bankReportFIle);
                    ptspDto.setUploadedBy(bankReportFIle.split("_")[0]);
                    Date javaDate = DateUtil.getJavaDate((Double.parseDouble(d.getTransactionTime())));
                    ptspDto.setTransactionTime(com._3line.gravity.freedom.utility.DateUtil.formatDateToItexFormat(javaDate));
                    if (d.getStatus().equals("Approved")) {
                        ptspDto.setStatusCode("00");
                    } else {
                        ptspDto.setStatusCode(d.getResponse().substring(0, 2));
                    }

                    if (d.getType().toLowerCase().contains("reversal")) {
                        ptspDto.setReversal("true");
                    } else {
                        ptspDto.setReversal("false");
                    }
                    logger.info("ptsp dto now looks like {}", ptspDto.toString());
                    ptspDto.setPtsp("NIBSS");
                    ptspService.savePtspDetails(ptspDto,"BANKS_UPLOAD");


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            TransactionUpdate update = modelMapper.map(transactionUpdateDto, TransactionUpdate.class);
            Filez filez = fileService.getFile(transactionUpdateDto.getFileId());
            logger.info("file name is  {}", filez.getName());
            update.setFilez(filez);
            update.setApprovedBy(AppUtility.getCurrentUserName());
            transactionUpdateRepository.save(update);


        } catch (Throwable e) {

        }
    }

    private void countTransactions(TransactionUpdateDto transactionUpdateDto) {

        try {
            File file = fileService.loadFileAsResource(transactionUpdateDto.getFileId()).getFile();

            List<TranFromExcel> dtos = new ArrayList<>();
            dtos = ExcelMapper.mapFromExcel(file)
                    .toObjectOf(TranFromExcel.class)
                    .fromSheet(0) // if this method not used , called all sheets
                    .map();

            Integer successCount = 0;
            Integer failureCount = 0;
            Integer reversalCount = 0;
            Double successValue = 0.0;
            Double failureValue = 0.0;

            for (TranFromExcel d : dtos) {

                try {

                    if (d.getType().equals("Reversal")) {
                        // reversal
                        reversalCount++;
                    } else {

                        if (d.getStatus().equals("Approved")) {
                            // successfull transaction
                            successCount++;
                            successValue = successValue + Double.valueOf(d.getAmount());
                        } else {
                            // failed transaction
                            failureCount++;
                            failureValue = failureValue + Double.valueOf(d.getAmount());
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            transactionUpdateDto.setNoOfFailed(failureCount);
            transactionUpdateDto.setNoOfSuccessFull(successCount);
            transactionUpdateDto.setNoOfReversals(reversalCount);
            transactionUpdateDto.setNoOfTransactions(dtos.size());
            transactionUpdateDto.setTotalSuccessFullVolume(successValue);
            transactionUpdateDto.setTotalFailedVolume(failureValue);

        } catch (Throwable e) {
            e.printStackTrace();
        }


    }

    @Override
    public Page<TransactionUpdate> getUpdates(Pageable pageable) {
        return transactionUpdateRepository.findAll(pageable);
    }
}
