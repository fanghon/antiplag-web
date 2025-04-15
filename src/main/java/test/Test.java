package test;

import java.io.File;
import java.util.Arrays;
import java.util.Collection;
import java.net.URL;
import org.apache.commons.io.FileUtils;
import it.zielke.moji.SocketClient;

public class Test {
    public static void main(String[] args) throws Exception {
//        replaceSpace(new File("D:\\test\\563\\405_10053990"));
        try{
            int qwe = 5/0;
        }catch (Exception e){
            System.out.println(Arrays.stream(e.getStackTrace()).map(StackTraceElement::toString));
        }

    }
    public static void test() throws Exception{
        String filename = "D:\\test\\563\\405_10053990";
        File directory = new File(filename);
        replaceSpace(directory);
        Collection<File> files = FileUtils.listFiles(new File(
                filename), new String[] { "py" }, true);

        SocketClient socketClient = new SocketClient();

        //set your Moss user ID
        socketClient.setUserID("846961665");
        //socketClient.setOpt...

        //set the programming language of all student source codes
        socketClient.setLanguage("python");

        //initialize connection and send parameters
        socketClient.run();

        // upload all base files
//        for (File f : baseFiles) {
//            socketClient.uploadBaseFile(f);
//        }

        //upload all source files of students
        for (File f : files) {
            socketClient.uploadFile(f);
        }

        //finished uploading, tell server to check files
        socketClient.sendQuery();

        //get URL with Moss results and do something with it
        URL results = socketClient.getResultURL();
        System.out.println("Results available at " + results.toString());
    }
    public static void replaceSpace(File directory) {
        directory.renameTo(new File(directory.getAbsolutePath().replaceAll("\\s", "")));
        if (directory.isDirectory()){
            for (File file:directory.listFiles()){
                replaceSpace(file);
            }
        }
    }
}
