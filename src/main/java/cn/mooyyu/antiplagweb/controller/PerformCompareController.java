package cn.mooyyu.antiplagweb.controller;

import cn.mooyyu.antiplagweb.service.PerformCompareService;
import it.zielke.moji.MossException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@RestController
@RequestMapping(value = "performCompare", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=utf-8")
public class PerformCompareController {
    private PerformCompareService performCompareService;

//    @Autowired
    @Value("${moss.id}")
    private String mossId;
    @Autowired
    public PerformCompareController(PerformCompareService performCompareService) {
        this.performCompareService = performCompareService;
    }

    @GetMapping("jplag")
    @ResponseBody
    public String[] jplag(@RequestParam String lang, @RequestParam int simValue, HttpServletRequest request) {
        return performCompareService.jplag(lang, simValue, request.getSession().getId());
    }

    @GetMapping("semhash")
    @ResponseBody
    public String[] semhash(@RequestParam String lang, @RequestParam int simValue, HttpServletRequest request) {
        return performCompareService.semhash(lang, simValue, request.getSession().getId());
    }
    @GetMapping("BM25")
    @ResponseBody
    public String[] BM25(@RequestParam String lang, @RequestParam int simValue, HttpServletRequest request) {
        return performCompareService.BM25(lang, simValue, request.getSession().getId());
    }
    @GetMapping("MOSS")
    @ResponseBody
    public String MOSS(@RequestParam String lang, @RequestParam String id, HttpServletRequest request) throws IOException, MossException {
        return performCompareService.MOSS(lang, id, request.getSession().getId());
    }
    @GetMapping("MOSSWithoutId")
    @ResponseBody
    public String MOSSWithoutId(@RequestParam String lang, HttpServletRequest request) throws IOException, MossException {
//        System.out.println(mossId);
//        return "1";
        return performCompareService.MOSS(lang, mossId, request.getSession().getId());
    }
}
