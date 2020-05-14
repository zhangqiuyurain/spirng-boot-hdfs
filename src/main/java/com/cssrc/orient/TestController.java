package com.cssrc.orient;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author rain
 * @date 2020/5/14 9:27
 */
@RestController
public class TestController {

    @RequestMapping("test")
    public String getTest() {
        return "Hello HDFS!";
    }
}
