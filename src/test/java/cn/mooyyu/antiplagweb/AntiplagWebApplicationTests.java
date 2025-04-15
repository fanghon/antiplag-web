package cn.mooyyu.antiplagweb;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import cn.mooyyu.antiplagweb.service.PerformCompareService;

@SpringBootTest
class AntiplagWebApplicationTests {
   @Autowired
   PerformCompareService performCompareService;
    @Test
    void contextLoads() {
    }

    @Test
    void BM25test(){
       String[] strs  ;
       strs= performCompareService.BM25("en", 80, "0356DE96DF6157A48A14CA68646B539E");
       System.out.println(strs[0]);
    }
}
