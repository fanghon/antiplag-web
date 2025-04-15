package cn.mooyyu.antiplagweb.service;

import cn.mooyyu.antiplagweb.pojo.Chunk;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class FileManagerService {
    private static final File folder = new File("static-file-folder", "resource");

    public void uploadFile(Chunk chunk, String sessionId) {
        String single_folder = new File(folder, sessionId).getPath();
        MultipartFile file = chunk.getFile();
        String filename = chunk.getFilename();
        String filePath = chunk.getRelativePath();
        String parent = single_folder + "/" + filePath.replaceFirst(".*?/","").replace(filename,"");

        try {
            byte[] bytes = file.getBytes();
//            if (!Files.isWritable(Paths.get(single_folder))) {
//                Files.createDirectories(Paths.get(single_folder));
//            }
            Files.createDirectories(Paths.get(parent));

            Path path = Paths.get(parent,filename);
            Files.write(path, bytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean deleteFile(String fileName, String sessionId) {
        File file = new File(new File(folder, sessionId), fileName);
        try {
            if (file.isDirectory()) {
                FileUtils.deleteDirectory(file);
            }
            else {
                file.delete();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean deleteAllFiles(String sessionId) {
        try {
            FileUtils.deleteDirectory(new File(folder, sessionId));
        } catch (IOException e) {
            return false;
        }
        return true;
    }

    public String[] getFilesName(String sessionId) {
        File dir = new File(folder, sessionId);
        if (!dir.exists()) return null;
        return dir.list();
    }
}
