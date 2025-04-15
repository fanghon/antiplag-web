package cn.mooyyu.antiplagweb.util;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
public class PythonExec {
    public static String call(String pythonScriptPath, String filePath,float threshold) {

        ProcessBuilder processBuilder = new ProcessBuilder("python", pythonScriptPath, filePath, String.valueOf(threshold));
        // 设置环境变量，确保使用 UTF-8 编码
        processBuilder.environment().put("PYTHONIOENCODING", "utf-8");
        try {
            Process process = processBuilder.start();

            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream(),"UTF-8"));
            StringBuilder output = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                output.append(line).append("\n");
            }
            // 读取标准错误输出
            BufferedReader errorReader = new BufferedReader(new InputStreamReader(process.getErrorStream(),"UTF-8"));
            StringBuilder errorOutput = new StringBuilder();
            while ((line = errorReader.readLine()) != null) {
                errorOutput.append(line).append("\n");
            }

            int exitCode = process.waitFor();
            if (exitCode != 0) {
                System.err.println("Python脚本退出码: " + exitCode);
                System.err.println("Python脚本错误输出: " + errorOutput.toString().trim());
                output.append("error");
            }

            return output.toString().trim();

        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
            return "error" ;
        }
    }

    public static void main(String[] args) {
        //获取java程序运行的当前路径
        File pfile = new File("semhashcn.py");
        String ppath = pfile.getPath();
        System.out.println(ppath);

        String  opath = new File("static-file-folder/result/BBAB830EDE1F3185A9E88EDE03E25383-converted").getPath();
        System.out.println(opath);

        String str = PythonExec.call(ppath, opath,0.9f);
        System.out.println(str);
    }
}
