package com._3line.gravity.core.files.service.impl;

import com._3line.gravity.api.shared.utility.JwtUtility;
import com._3line.gravity.core.code.model.Code;
import com._3line.gravity.core.files.config.FileStorageProperties;
import com._3line.gravity.core.files.exceptions.FileStorageException;
import com._3line.gravity.core.files.model.Filez;
import com._3line.gravity.core.files.repository.FilezRepository;
import com._3line.gravity.core.files.service.FileService;
import com._3line.gravity.core.utils.AppUtility;
import com._3line.gravity.freedom.agents.models.Agents;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections.IteratorUtils;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class FileServiceImplementation implements FileService {

    private static Logger logger = LoggerFactory.getLogger(FileService.class);

    private final FilezRepository fIlezRepository;

    private final Path fileStorageLocation;

    @Value("${file.uploadDir}")
    private String fileDir;

    @Autowired
    JwtUtility jwtUtility;


    @Autowired
    public FileServiceImplementation(FilezRepository fIlezRepository , FileStorageProperties fileStorageProperties) {
        this.fIlezRepository = fIlezRepository;
            logger.info("path {}",fileStorageProperties.getUploadDir());
        this.fileStorageLocation = Paths.get(fileStorageProperties.getUploadDir())
                .toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
            logger.info("directory created !");
        } catch (Exception ex) {
            throw new FileStorageException("Could not create the directory where the uploaded files will be stored.", ex);
        }
    }

    @Override
    public Long storeFile(MultipartFile file, String code) throws FileStorageException {
        // Normalize file name
        String originalName = StringUtils.cleanPath(file.getOriginalFilename());

        try {
            // Check if the file's name contains invalid characters
            if(originalName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + originalName);
            }


            String fileName =  AppUtility.getCurrentUserName() + "_" + System.currentTimeMillis() + "_"+originalName  ;

            // Copy file to the target location (Replacing existing file with the same name)
            Path targetLocation = this.fileStorageLocation.resolve(fileName);
            Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

            // save Filez record for file

            Filez filez = new Filez();
            filez.setName(fileName);
            filez.setPurpose(code);
            filez.setUploadedBy(AppUtility.getCurrentUserName());
            filez.setLocation(targetLocation.toString());
            filez.setContentType(file.getContentType());
            filez.setSize(Long.valueOf(file.getSize()).toString());
            filez.setExtension(FilenameUtils.getExtension(file.getOriginalFilename()));
            Filez saved = fIlezRepository.save(filez);

            return saved.getId() ;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + originalName + ". Please try again!", ex);
        }
    }

    @Override
    public String storeFile(String base64, String fileName,String code) throws FileStorageException {

        try{
            byte[] imageByte = Base64.decodeBase64(base64);

            String directory = fileDir+fileName;
            FileOutputStream outputStream = new FileOutputStream(directory);

            outputStream.write(imageByte);
            outputStream.flush();
            outputStream.close();


            Filez filez = new Filez();
            filez.setName(fileName);
            filez.setPurpose(code);
            filez.setUploadedBy(AppUtility.getCurrentUserName());
            filez.setLocation(directory);
            filez.setUrl("/core/utility/image/"+fileName);
            Filez saved = fIlezRepository.save(filez);

            return filez.getUrl() ;
        }catch (Exception e){
            e.printStackTrace();

        }
        return null;
    }

    @Override
    public Resource loadFileAsResource(String fileName) throws FileStorageException {
        try {
            Path filePath = this.fileStorageLocation.resolve(fileName).normalize();
            Resource resource = new UrlResource(filePath.toUri());
            if(resource.exists()) {
                return resource;
            } else {
                throw new FileStorageException("File not found " + fileName);
            }
        } catch (MalformedURLException ex) {
            throw new FileStorageException("File not found " + fileName, ex);
        }
    }


    @Override
    public Resource loadFileAsResource(Long id) throws FileStorageException {

    Filez filez = fIlezRepository.findOne(id);

         if(Objects.isNull(filez)) throw new FileStorageException("File not found " + id);

        return loadFileAsResource(filez.getName());
    }

    @Override
    public List<Filez> getAllFilez() {
        return StreamSupport.stream(fIlezRepository.findAll().spliterator(), false)
                .collect(Collectors.toList());
    }

    @Override
    public List<Filez> getByCode(String code) {
        return fIlezRepository.findByPurpose(code);
    }

    @Override
    public Filez getFile(Long id) {
        return fIlezRepository.findOne(id);
    }
}
