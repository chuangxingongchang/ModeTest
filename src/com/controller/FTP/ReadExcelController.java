package com.controller.FTP;

import com.service.impl.ReadExcelService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;

@Controller
@RequestMapping(value = "/getExcelData")
public class ReadExcelController {

    @Resource
    private ReadExcelService readExcelService;
    /**
     * @Description 获取文件列表
     * @return
     */
    @RequestMapping(value = "/dataList")
    @ResponseBody
    public void fileList(String type)throws Exception{

         readExcelService.insertExcelData(type);
    }
}
