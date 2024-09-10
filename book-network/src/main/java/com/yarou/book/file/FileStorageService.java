package com.yarou.book.file;

import com.yarou.book.book.Book;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.io.File.*;

@Service
@Slf4j // for the log
@RequiredArgsConstructor
public class FileStorageService {


    @Value("${application.file.upload.photos-output-path}")
    private String fileUploadPath;

    public String saveFile(@NonNull MultipartFile sourceFile /*uploaded file’s data and metadata */,
                           @NonNull Integer userId) {
        final String fileUploadSubPath = "users"+ separator+userId;
        return uploadFile(sourceFile,fileUploadSubPath);
    }

    private String uploadFile(@NonNull MultipartFile sourceFile,
                              @NonNull String fileUploadSubPath) {
            final String finalUploadPath = fileUploadPath + separator + fileUploadSubPath; /// ./uploads/users/1
            File targetFolder = new File(finalUploadPath);
            if(!targetFolder.exists()){
                boolean folderCreated = targetFolder.mkdirs();
                if(!folderCreated){
                log.warn("Failed to create the target folder");
                return null;
                }
            }
            final String fileExtension = getFileExtension(sourceFile.getOriginalFilename());
            // ./upload/users/1/6544645498.jpg
            String targetFilePath = finalUploadPath + separator + System.currentTimeMillis() +"."+fileExtension;
            Path targetPath = Paths.get(targetFilePath);
            try{
                Files.write(targetPath,sourceFile.getBytes());
                log.info("File saved to "+targetFilePath);
                return targetFilePath;
            }catch (IOException e){
                log.error("File was not saved",e);
            }
             return null;

    }

    private String getFileExtension(String fileName) {
        if (fileName == null || fileName.isEmpty()){
            return "";
        }
        int lastDotIndex = fileName.lastIndexOf(".");
        if(lastDotIndex == -1){
            return "";
        }
        return fileName.substring(lastDotIndex+1).toLowerCase();
    }
}
