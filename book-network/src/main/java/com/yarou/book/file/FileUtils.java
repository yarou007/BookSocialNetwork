package com.yarou.book.file;

import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;


@Slf4j
public class FileUtils {


    public static byte[] readFileFromLocation(String fileUrl) {
        if(StringUtils.isBlank(fileUrl)){
            /// ken mafama hata file url taada return null
            return null;
        }
        try{
            /// mshin kdhina l path
            Path filePath = new File(fileUrl).toPath();
            /// krina l path w raja3na el data w metadata li file
            return Files.readAllBytes(filePath);
        }catch(IOException e){
            log.warn("No file found in the path {}",fileUrl);
        }
        return null;
    }
}
