package cn.mooyyu.antiplagweb.service;

import cn.mooyyu.antiplagweb.util.Compress;
import org.springframework.stereotype.Service;
import javax.servlet.http.HttpServletResponse;
import java.io.*;

@Service
public class ResultService {
    private static final File result = new File("static-file-folder", "result");

    public void jplag(String sessionId, HttpServletResponse response) {
        final String fileName = "result.zip";

        try {
            Compress.compress(new File(result, sessionId).getPath(), new File(result, sessionId+".zip").getPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

        response.setHeader("Content-Disposition", "attachment;filename=" + fileName);
        byte[] buff = new byte[1024];
        BufferedInputStream bis = null;
        OutputStream os = null;
        File file = new File(result, sessionId+".zip");
        try {
            os = response.getOutputStream();
            bis = new BufferedInputStream(new FileInputStream(file));
            int i = bis.read(buff);
            while (i != -1) {
                os.write(buff, 0, buff.length);
                os.flush();
                i = bis.read(buff);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bis != null) {
                try {
                    bis.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (file.exists()) {
                //noinspection ResultOfMethodCallIgnored
                file.delete();
            }
        }
    }
}
