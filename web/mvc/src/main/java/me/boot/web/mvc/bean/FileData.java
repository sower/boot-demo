package me.boot.web.mvc.bean;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

/**
 * FileData
 *
 * @since 2025/01/12
 **/
@Data
public class FileData {

    private MultipartFile file;

    private String description;

}
