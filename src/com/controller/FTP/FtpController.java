package com.controller.FTP;

import com.alibaba.fastjson.JSONObject;
import com.service.FTPService;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Description ftp实现文件上传下载
 * @author lengdl
 * @date 2019年6月24日10:32:15
 */
@Controller
@RequestMapping(value = "/FTPFileController")
public class FtpController {

    private static final Logger log = LoggerFactory.getLogger(FtpController.class);

    /**自动注入ftp的服务层*/
    @Autowired
    private FTPService ftpService;

    /**
     * @Description 获取文件列表
     * @return
     */
    @RequestMapping(value = "/fileList")
    @ResponseBody
    public Map fileList()throws Exception{

        return ftpService.getFileList("/");
    }

    /**
     * 递归遍历目录下面指定的文件
     * @param pathName 需要遍历的目录，必须以"/"开始和结束
     * @param ext      文件的扩展名
     */
    @RequestMapping(value = "/getFileAndLenth")
    @ResponseBody
    public Map getFileAndLenth(@RequestParam("pathName") String pathName,@RequestParam("ext") String ext)throws Exception{

        return ftpService.getFileListByPath(pathName,ext);
    }

    /**
     * @Description ftp文件上传
     * @return
     */

    @RequestMapping(value = "/uploadFile")
    @ResponseBody
    public Map uploadFile(HttpServletRequest req,@RequestParam("file") MultipartFile file)throws Exception{

        /**定义一个返回参数*/
        Map<String,Object> upMap = new HashMap<>();

        /**获得上传文件名*/
        String originalName = file.getOriginalFilename();

        if(StringUtils.isBlank(originalName)){

            upMap.put("result","error");

            return upMap;
        }

        /**定义一个上传文件存放路径*/
        String path;
        path = req.getSession().getServletContext().getRealPath("/upload");

        /**定义一个上传文件存放路径的文件*/
        File filePath = new File(path);

        /**如文件不存在，则在path位置下创建*/
          if(!filePath.exists()){

              filePath.mkdirs();
          }
        /**设置文件类型为‘文件’*/
        String ftpType = "文件";

        Boolean flag = false;

        /**ftp服务器登录*/
        ftpService.ftpLogin();

        if(StringUtils.equals("文件",ftpType)){

            /**定义一个ftp服务器的文件，即上传的文件*/
            File files = new File(path+File.separator+originalName);

            /**上传的文件放入tomcat项目下upload文件中*/
            file.transferTo(files);

            /**实现文件上传，并返回一个布尔变量，true为上传成功*/
            flag = ftpService.uploadFile(files);

        }else if(StringUtils.equals("文件夹",ftpType)){

            flag = ftpService.uploadDirectory(originalName);
        }
        /**关闭ftp服务器*/
        ftpService.ftpLogOut();

        /**flag为false，返回error，上传失败*/
        if(!flag){
            upMap.put("result","error");
            return upMap;
        }
        upMap.put("result","success");

        return upMap;
    }
    /**
     * @Description 多文件上传
     * @param
     * @return
     */

    @RequestMapping(value = "/manyFileUpload")
    @ResponseBody
    public Map<String,Object> manyFileUpload(MultipartHttpServletRequest mulReq,HttpServletRequest req)throws Exception{

        /**定义一个返回参数*/
        Map<String,Object> filemap=new HashMap<>();

        /**获取前端上传的所有文件*/
        List<MultipartFile> multipartFile= mulReq.getFiles("file");

        /**登录ftp服务器*/
        ftpService.ftpLogin();
        try {
            if (multipartFile != null&&multipartFile.size()>0){

                /**定义一个上传文件存放路径*/
               String path = req.getSession().getServletContext().getRealPath("/upload");

               /**定义一个上传文件存放路径的文件*/
               File filePath = new File(path);

                if(!filePath.exists()){

                    filePath.mkdirs();
                }
                /**定义一个ftp服务器的文件，即上传的文件*/
                File file= null;

                /**循环获取文件流程上传ftp*/
                for (MultipartFile m: multipartFile){

                    String originalName = m.getOriginalFilename();

                    /**如果上传文件名获取为空，直接返回*/
                    if(StringUtils.isBlank(originalName)){

                        filemap.put("result","error");

                        return filemap;
                    }
                    /**获得上传文件*/
                    file = new File(path+File.separator+originalName);

                    /**上传的文件放入tomcat项目下upload文件中*/
                    m.transferTo(file);

                    /**用ftp对文件进行上传*/
                    ftpService.uploadFile(file);
                }
            }
        }catch (Exception e){
            log.error("msg",e);
            e.printStackTrace();
        }finally {
            /**关闭ftp服务器*/
            ftpService.ftpLogOut();
        }
        filemap.put("result","success");

        return filemap ;
    }

    /**
     * @Description 文件或文件夹夹下载
     * @param ftpPath 下载文件名
     * @param fileType 下载文件类型
     * @return
     */

    @RequestMapping(value = "/folderDownLoad",method= RequestMethod.GET)
    @ResponseBody
    public void folderDownLoad(HttpServletResponse resp,HttpServletRequest req,
                                                 @RequestParam("ftpPath") String ftpPath,
                                                 @RequestParam("fileType") String fileType)throws Exception{
        /**定义一个布尔，用于判断下载成功与否*/
        Boolean flag = false;

        /**登录ftp服务器*/
        ftpService.ftpLogin();

        /**设置ftp服务器编码格式，防止中文乱码*/
        String userAgent=(String)req.getHeader("USER-AGENT");

        if(-1!=userAgent.toLowerCase().indexOf("chrome")){
            /**如果是谷歌*/
            ftpPath = new String(ftpPath.getBytes("UTF-8"),"iso-8859-1");
        }else{
            /**如果是IE*/
            ftpPath = new String(ftpPath.getBytes("GBK"),"iso-8859-1");
        }
        /**设置请求编码格式*/
        resp.setCharacterEncoding("UTF-8");

        /**设置ContentType字段值*/
        resp.setContentType("text/html;charset=utf-8");

        /**通知浏览器以下载的方式打开*/
        resp.setContentType("application/octet-stream");

        /**设置文件的名字*/
        resp.addHeader("Content-Disposition", "attachment; filename="+ftpPath);

        /**定义一个下载本地存放路径*/
        String localPath ="";

        /**分文件及文件夹进行下载*/
        if(StringUtils.equals("文件夹",fileType)){

            /**调用ftp下载文件夹方法，下载成功返回true*/
           flag = ftpService.downLoadDirectory(resp,localPath,"/"+ftpPath+"/");

        }else if (StringUtils.equals("文件",fileType)){

            /**调用ftp下载文件方法，下载成功返回true*/
           flag = ftpService.downloadFile(resp,ftpPath/*,localPath,"/"*/);
        }
        /**退出ftp服务器*/
        ftpService.ftpLogOut();
        if(!flag){
            log.info("文件夹下载失败");
        }
    }

    /**
     * @Description 文件删除
     * @param  params 删除文件名
     * @return
     */

    @RequestMapping(value = "/fileDelete")
    @ResponseBody
    public Map<String,Object> fileDelete(String params)throws Exception{

        /**设置返回参数*/
        Map<String,Object> resultMap = new HashMap<>();

        /**判断传入参数是否为空*/
        if(StringUtils.isBlank(params)){

            resultMap.put("result","error");

            log.error("Msg","传入参数为空");

            return resultMap;
        }
        /**获得删除文件名*/
        String pathName = JSONObject.parseObject(params).getString("pathName");

        /**登录ftp服务器*/
        ftpService.ftpLogin();

        /**调用ftp方法，进行删除*/
        String msg = ftpService.deleteFile("/"+pathName);

        /**退出ftp服务器*/
        ftpService.ftpLogOut();

        resultMap.put("result",msg);

        /**返回参数*/
        return resultMap;
    }
}

