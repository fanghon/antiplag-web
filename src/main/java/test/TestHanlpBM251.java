package test;

//import com.hankcs.hanlp.HanLP;
//import com.hankcs.hanlp.summary.BM25;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TestHanlpBM251 {
    private static final File resource = new File("static-file-folder", "resource");
    private static final File result = new File("static-file-folder", "result");

    public String[] bm25Check(String lang, int similarityThreshold, String sessionId) {
          List<String> results = new ArrayList<>();
//        List<List<String>> documents = new ArrayList<>();
//        File resultDir = new File(result, sessionId + "-converted");
//        if (resultDir.exists()) {
//            for (File file : resultDir.listFiles()) {
//                String content = convertMatchesFileToString(file);
//                // 将内容切分为句子
//
//
//                List<String> sentences = HanLP.segment(content).stream().map(term -> term.word).collect(Collectors.toList());
//                documents.add(sentences);
//            }
//        }
//
//        BM25 bm25 = new BM25(documents);
//
//
//        for (int i = 0; i < documents.size(); i++) {
//            List<String> query = documents.get(i);
//            StringBuilder result = new StringBuilder();
//            result.append("File: ").append(resultDir.listFiles()[i].getName()).append("\n");
//
//            for (int j = 0; j < documents.size(); j++) {
//                if (i != j) {
//                    double score = bm25.sim(query, j);
//                    if (score >= similarityThreshold / 100.0) {
//                        result.append("Similar File: ").append(resultDir.listFiles()[j].getName()).append(", Score: ").append(score).append("\n");
//                    }
//                }
//            }
//            results.add(result.toString());
//        }

        return results.toArray(new String[0]);
    }

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
        return new String(fileContent);
    }

    public static void main(String[] args) {
        TestHanlpBM251 test = new TestHanlpBM251();
        String sessionId = "0356DE96DF6157A48A14CA68646B539E";
        String lang = "en"; // 或 "en"
        int similarityThreshold = 80; // 相似度阈值，例如 80%
        String[] results = test.bm25Check(lang, similarityThreshold, sessionId);
        for (String result : results) {
            System.out.println(result);
        }
    }
}
