package cn.mooyyu.antiplagweb.controller;

import cn.mooyyu.antiplagweb.service.ResultService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@RestController
@RequestMapping(value = "result", method = {RequestMethod.GET, RequestMethod.POST}, produces = "application/json;charset=utf-8")
public class ResultController {
    private ResultService resultService;
    @Autowired
    public ResultController(ResultService resultService) {
        this.resultService = resultService;
    }

    @GetMapping("jplag")
    public void jplag(HttpServletRequest request, HttpServletResponse response) {
        resultService.jplag(request.getSession().getId(), response);
    }
}