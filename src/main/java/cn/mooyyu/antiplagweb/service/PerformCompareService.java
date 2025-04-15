package cn.mooyyu.antiplagweb.service;

import cn.mooyyu.antiplagweb.util.FileIO;
import cn.mooyyu.antiplagweb.util.PythonExec;
import cn.mooyyu.antiplagweb.util.TextExtractor;
import it.zielke.moji.MossException;
import it.zielke.moji.SocketClient;
import jplag.Program;
import jplag.options.CommandLineOptions;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.analysis.cn.smart.SmartChineseAnalyzer;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.queryparser.simple.SimpleQueryParser;
import org.apache.lucene.search.*;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.net.UnknownHostException;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.apache.lucene.analysis.Analyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.DirectoryReader;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.store.ByteBuffersDirectory;
import org.apache.lucene.search.similarities.BM25Similarity;


@Service
public class PerformCompareService {
    private static final File resource = new File("static-file-folder", "resource");
    private static final File result = new File("static-file-folder", "result");
//    private static final File result = new File(Thread.currentThread().getContextClassLoader().getResource("").getPath() + "static");
//    private static final File testResult = new File("static");
    private String convertMatchesFileToString(File file) {
        byte[] fileContent = new byte[(int) file.length()];
        try {
            FileInputStream in = new FileInputStream(file);
            //noinspection ResultOfMethodCallIgnored
            in.read(fileContent);
            in.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String(fileContent).trim();
    }

    private void cleanResult(File dir) {
        File[] files = dir.listFiles();
        if (files != null) for (File file : files) { //noinspection ResultOfMethodCallIgnored
            file.delete();
        }
    }

    private void jplagDocConvert(String sessionId) {
        File convertDir = new File(result, sessionId+"-converted");
        if (convertDir.exists()) cleanResult(convertDir);
        else { //noinspection ResultOfMethodCallIgnored
            convertDir.mkdir();
        }
        File resourceDir = new File(resource, sessionId);
        String[] files = resourceDir.list();
        if (files != null) for (String file : files) {
            FileIO.saveFile(new File(convertDir, file + ".txt"), TextExtractor.convertToText(new File(resourceDir, file)), "utf-8");
        }
    }

    public String[] jplag(String lang, int simValue, String sessionId) {
        cleanResult(new File(result, sessionId));
        if ("doc".equals(lang)) jplagDocConvert(sessionId);
        String[] ret;
        try {
            ArrayList<String> args = new ArrayList<>();
            args.add("-l");
//            args.add("-s");
            if ("java".equals(lang)) args.add("java19");
            else if ("doc".equals(lang)) args.add("text");
            else if("HTML&CSS".equals(lang)){
                File directory = new File(resource,sessionId);
                replaceHtmlToTxt(directory);
                args.add("text");
            }
            else args.add(lang);
            args.add("-s");
            args.add("-r"); //指定结果存放的路径
//            String s = this.getClass().getResource().getPath();
            args.add(new File(result, sessionId).getPath());
            args.add("-m");  //设置相似度检查门限参数值
            args.add(simValue + "%");
            if ("doc".equals(lang)) args.add(new File(result, sessionId+"-converted").getPath());
            else args.add(new File(resource, sessionId).getPath());
            String[] toPass = new String[args.size()];
            toPass = args.toArray(toPass);
            new Program(new CommandLineOptions(toPass)).run();
        } catch (Exception e) {
            System.out.println(e.toString());
            ret = new String[1];
            ret[0] = "error";
            return ret;
        }
        ret = new String[2];
        File file = new File(result, sessionId);
        ret[0] = convertMatchesFileToString(new File(file, "matches_avg.csv"));
        ret[1] = convertMatchesFileToString(new File(file, "matches_max.csv"));
        return ret;
    }

    public String MOSS(String lang,String id,String SessionId){
        /**
         * 暂时只支持这些就好了，剩下的不用支持了
         */
        lang = lang.toLowerCase();
        Logger.getLogger(this.getClass().getName()).info("待检测语言是"+lang);
        HashMap<String,String[]> languageToFile = new HashMap<>(){
            {
//                MOSS: ["c", "cc", "java", "ml", "pascal", "ada", "lisp", "schema",
//                "haskell", "fortran", "ascii", "vhdl", "perl", "matlab", "python",
//                "mips", "prolog", "spice", "vb", "csharp", "modula2", "a8086", "javascript", "plsql"],]
                put("c",new String[]{"c"});
                put("cc",new String[]{"cpp"});
                put("java",new String[]{"java"});
                put("ml",new String[]{"ml"});
                put("pascal",new String[]{"p","pl","pas","pascal"});
                put("ada",new String[]{"ada"});
                put("lisp",new String[]{"lisp"});
                put("schema",new String[]{"schema"});
                put("haskell",new String[]{"hs","lhs"});

                put("python",new String[]{"py"});

                put("matlab",new String[]{"asm"});
                put("mips",new String[]{"s","asm"});
                put("javascript",new String[]{"js"});
                put("plsql",new String[]{"sql"});
            }
        };
        try{

//            String filename = resource+"/"+Sessionid;
            File directory = new File(resource,SessionId);
            replaceSpace(directory);
            if (!languageToFile.containsKey(lang)){
                return "语言不支持，需要支持该语言请直接找fh";
            }
            Collection<File> files = FileUtils.listFiles(directory, languageToFile.get(lang), true);

            SocketClient socketClient = new SocketClient();

            //set your Moss user ID
            socketClient.setUserID(id);
            //socketClient.setOpt...

            //set the programming language of all student source codes
            socketClient.setLanguage(lang);

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
            Logger.getLogger(this.getClass().getName()).info("MOSS Results available at " + results.toString());
            return results.toString();

        } catch (Exception e) {
            Logger.getLogger(this.getClass().getName()).log(Level.WARNING,"MOSS系统调用出错",e);
        }
        return "MOSS调用出错，请联系fh";
    }

    public String[] semhash(String lang, int simValue, String sessionId) {
        cleanResult(new File(result, sessionId));

        String[]  ret = new String[1];
        try {
            if ("doc".equals(lang)) {
                jplagDocConvert(sessionId);
                File pfile = new File("semhashcn.py");
                String ppath = pfile.getPath();

                String opath = new File(result, sessionId + "-converted").getPath();

                ret[0] = PythonExec.call(ppath, opath, simValue / 100f);

                System.out.println(ret[0]);


            }
        } catch (Exception e) {
            System.out.println(e.toString());
            ret[0] = "error";
            return ret;
        }
        return ret;
    }

    public String[] BM25(String lang, int similarityThreshold, String sessionId) {
        cleanResult(new File(result, sessionId));
        jplagDocConvert(sessionId);
        //建立内存索引
        ByteBuffersDirectory directory = new ByteBuffersDirectory(); // 创建一个内存目录,基于内存的索引
        Analyzer analyzer = getAnalyzer(lang);
        IndexWriterConfig config = new IndexWriterConfig(analyzer);
        config.setSimilarity(new BM25Similarity());

        try (IndexWriter writer = new IndexWriter(directory, config)) {
            File resultDir = new File(result, sessionId+"-converted");
            if (resultDir.exists()) {
                for (File file : resultDir.listFiles()) {
                    Document doc = new Document();
                    doc.add(new TextField("content", convertMatchesFileToString(file), Field.Store.YES));
                    writer.addDocument(doc);
                }
            }
            writer.commit();
        } catch (IOException e) {
            e.printStackTrace();
            return new String[]{"error"};
        }

        // 增加布尔子句的限制,默认查询输入字符数1024
        BooleanQuery.setMaxClauseCount(Integer.MAX_VALUE);
        // 搜索文档并计算相似度
        try (DirectoryReader reader = DirectoryReader.open(directory)) {
            IndexSearcher searcher = new IndexSearcher(reader);
            searcher.setSimilarity(new BM25Similarity());

            List<String> results = new ArrayList<String>();
            File resultDir = new File(result, sessionId+"-converted");
            if (resultDir.exists()) {
                File[] files = resultDir.listFiles();
                int i = 0;
                for (File file : files) {
                    String content = convertMatchesFileToString(file);
                    if(content.length()==0){ //空文档
                        System.out.println("empty documents found for file: " + file.getName());
                        i++;
                        continue;
                    }
                    Query query = new SimpleQueryParser(analyzer,"content").parse(content);
                    TopDocs topDocs = searcher.search(query, 5); // 搜索前5个最相似的文档
                    // 检查 topDocs.scoreDocs 是否为空
                    if (topDocs.scoreDocs.length == 0) {
                        System.out.println("No matching documents found for file: " + file.getName());
                        i++;
                        continue; // 如果没有匹配结果，跳过当前文件
                    }
                    // 计算最大分数
                    double maxScore = Arrays.stream(topDocs.scoreDocs).max(Comparator.comparingDouble(ScoreDoc->ScoreDoc.score)).get().score;
                    StringBuilder result = new StringBuilder();
                    result.append(i+1+" ").append(file.getName()).append(":");

                    for (ScoreDoc scoreDoc : topDocs.scoreDocs) {
                      if(scoreDoc.doc>i) {
                          double normalizedScore = scoreDoc.score / maxScore;
                          if (normalizedScore >= similarityThreshold / 100.0) {
                              Document hitDoc = searcher.doc(scoreDoc.doc);
                              //normalizedScore乘100,保留1位小数
                              normalizedScore = Double.parseDouble(String.format("%.1f", normalizedScore * 100)); ;
                              result.append(" ").append(files[scoreDoc.doc].getName()).append(" ").append(normalizedScore).append("%,");
                          }else{
                              break ;
                          }
                      }

                    }
                    i++ ;
                    if(result.lastIndexOf(",")>0) {  //没有满足限值的匹配对
                        results.add(result.append("\n").toString());
                    }
                }
            }
            //将results中的字符串列表合并成一个字符串
            String mergedResult = String.join("", results);
            return new String[]{mergedResult};

        } catch (Exception e) {
            e.printStackTrace();
            return new String[]{"error"};
        }
    }

    private Analyzer getAnalyzer(String lang) {
        if ("doc".equalsIgnoreCase(lang)) {
            return new SmartChineseAnalyzer();
        } else {
            return new StandardAnalyzer();
        }
    }
    public static void replaceSpace(File directory) {
        directory.renameTo(new File(directory.getAbsolutePath().replaceAll("\\s", "")));
        if (directory.isDirectory()){
            for (File file:directory.listFiles()){
                replaceSpace(file);
            }
        }
    }
    public static void replaceHtmlToTxt(File directory){
        if (directory.isFile()){
            directory.renameTo(new File(directory.getAbsolutePath().replaceAll("css$|html$", "$0.txt")));
        }
        if (directory.isDirectory()){
            for (File file:directory.listFiles()){
                replaceHtmlToTxt(file);
            }
        }
    }

//    public static void main(String[] args) {
//        String a = "12312.css.css";
//        System.out.println(a.replaceAll("css$|html$","$0.txt"));
//    }
}
