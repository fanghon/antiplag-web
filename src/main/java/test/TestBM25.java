package test;

import cn.mooyyu.antiplagweb.service.PerformCompareService;

public class TestBM25 {
    public static void main(String[] args) {
        PerformCompareService performCompareService = new PerformCompareService();
        String[] strs  ;
        strs= performCompareService.BM25("cn", 50, "0356DE96DF6157A48A14CA68646B539E");
        System.out.println(strs[0]);
    }
}
