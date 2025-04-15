package cn.mooyyu.antiplagweb.pojo;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

@Data
public class Chunk implements Serializable {
    private Long id;
    private Integer chunkNumber;
    private Long chunkSize;
    private Long currentChunkSize;
    private Long totalSize;
    private String identifier;
    private String filename;
    private String relativePath;
    private Integer totalChunks;
    private String type;
    private MultipartFile file;


}