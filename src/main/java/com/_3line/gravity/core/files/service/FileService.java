package com._3line.gravity.core.files.service;

import com._3line.gravity.core.code.model.Code;
import com._3line.gravity.core.files.exceptions.FileStorageException;
import com._3line.gravity.core.files.model.Filez;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface FileService {

    Long storeFile(MultipartFile file, String code) throws FileStorageException;

    String storeFile(String base64,String fileName, String code) throws FileStorageException;

    Resource loadFileAsResource(String fileName) throws FileStorageException;

    Resource loadFileAsResource(Long id) throws FileStorageException;

    List <Filez> getAllFilez();

    List <Filez> getByCode(String purpose);

    Filez getFile(Long id);


}
