package com.service.impl;

import com.service.FTPService;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service
@Transactional(rollbackFor = Exception.class)
public class FtpApcheUtils implements FTPService {

    private static final Logger log = LoggerFactory.getLogger(FtpApcheUtils.class);

    private static FTPClient ftp = null;

    /**ftp服务器地址*/
    private static final String hostname = "192.168.8.202";

    /**ftp服务器端口号默认为21*/
    private static final Integer port = 21;

    /**ftp登录账号*/
    private static final String username = "Administrator";

    /**ftp登录密码*/
    private static final String password = "123";

    /**设置超时时间*/
    private static int defaultTimeout = 1024000;

    /**服务器默认路径*/
    private static String ftpUrl = "/";

    /**本地字符编码*/
    static String LOCAL_CHARSET = "GBK";

    /**FTP协议里面，规定文件名编码为iso-8859-1*/
    static String SERVER_CHARSET = "ISO-8859-1";

    /**
     * @return 判断是否登入成功
     * */
    @Override
    public boolean ftpLogin() {

        /**本地字符编码*/
        boolean isLogin = false;

        ftp = new FTPClient();

        /**设置ftp编码格式*/
        ftp.setControlEncoding("utf-8");
        try {
            /**连接服务器*/
            ftp.connect(hostname,port);

            /**FTP服务器连接回答*/
            int reply = ftp.getReplyCode();

            /**FTP服务器编码设置，防止中文乱码*/
            if (FTPReply.isPositiveCompletion(ftp.sendCommand("OPTS UTF8", "ON"))) {
                LOCAL_CHARSET = "UTF-8";
            }
            ftp.setControlEncoding(LOCAL_CHARSET);

            /**账号密码用用户名、密码进行登录*/
            ftp.login(username, password);

            /**设置传输协议*/
            ftp.enterLocalPassiveMode();
            ftp.setFileType(FTPClient.BINARY_FILE_TYPE);

            log.info("恭喜" + username + "成功登陆FTP服务器");

            isLogin = true;

        }catch (Exception e) {
            log.error(username + "登录FTP服务失败！" + e.getMessage());

            e.printStackTrace();
        }
        /**冲区大小，等从ftp下载的数据存储到缓冲区，等缓冲区满了，进行磁盘读写*/
        ftp.setBufferSize(1024 * 10);

        /**设置传输协议*/
        ftp.setDataTimeout(defaultTimeout);

        return isLogin;
    }
    /**
     * @退出关闭服务器链接
     * */
    @Override
    public void ftpLogOut() {

        if (null != ftp && ftp.isConnected()) {
            try {
               /**退出FTP服务器*/
                boolean reuslt = ftp.logout();
                if (reuslt) {
                    log.info("成功退出服务器");
                }
            }catch (IOException e) {
                log.warn("退出FTP服务器异常！" + e.getMessage());
                e.printStackTrace();
            }finally {
                try {
                    /**关闭FTP服务器的连接*/
                    ftp.disconnect();

                }catch (IOException e) {

                    e.printStackTrace();
                    log.warn("关闭FTP服务器的连接异常！");
                }
            }
        }
    }
    /***
     * 上传Ftp文件
     * @param localFile 当地文件
     * */
    @Override
    public boolean uploadFile(File localFile) {

        BufferedInputStream inStream = null;

        boolean success = false;

            try {
                /**改变工作路径*/
                ftp.changeWorkingDirectory(ftpUrl);

                /**判断ftp服务器是否有该上传文件，有就进行删除，上传最新时间的文件*/
                if(existFile(ftpUrl+localFile.getName())){

                    deleteFile(ftpUrl+localFile.getName());
                }
                /**定义上传流*/
                inStream = new BufferedInputStream(new FileInputStream(localFile));

                log.info(localFile.getName() + "开始上传.....");

                /**上传文件存入ftp服务器*/
                success = ftp.storeFile(localFile.getName(), inStream);

                if (success == true) {

                    log.info(localFile.getName() + "上传成功");

                    return success;
                }
            }catch (FileNotFoundException e) {
                log.error("未找到文件");
                e.printStackTrace();
            }catch (IOException e) {
                e.printStackTrace();
            }finally {
                if (inStream != null) {
                    try {
                        inStream.close();
                    }catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
//        }
        return success;
    }
    /***
     * 下载文件
//     * @param filename 待下载文件名称
//     * @param localDires 下载到当地那个路径下
     * */
    @Override
    public Boolean downloadFile(HttpServletResponse response, String filename) {
        InputStream in = null;
        try {
                /**转移到FTP服务器目录*/
                ftp.changeWorkingDirectory("/"+filename);

                log.debug("远程路径" + filename);

                /**ftp服务器目录下的所有文件*/
                FTPFile[] fs = ftp.listFiles();

                /**设置下载文件名转为中文编码*/
                filename = new String(filename.getBytes("ISO-8859-1"),"utf-8");

                /**遍历目录下文件*/
                for (FTPFile ff : fs) {

                    log.debug("远程文件名" + ff.getName());

                    /**列表与下载文件名进行匹配*/
                    if (ff.getName().equals(filename)) {

                        ftp.enterLocalPassiveMode();

                        /**读取ftp服务器文件，返回输入流)*/
                        in = ftp.retrieveFileStream(ff.getName());

                        /**通过response获取ServletOutputStream对象(out)*/
                        int b = 0;

                        byte[] buffer = new byte[1024];

                        while (b != -1) {

                            b = in.read(buffer);

                            if (b != -1) {
                                /**写到输出流(out)中*/
                                response.getOutputStream().write(buffer, 0, b);
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    if (in != null) {
                        in.close();
                    }
                    response.getOutputStream().flush();
                } catch (IOException e) {
                    log.error("关闭文件IOException!");
                    e.printStackTrace();
                }
            }
            return true;
    }
    /***
     * @上传文件夹
     * @param localDirectory  当地文件夹
     * */
    @Override
    public boolean uploadDirectory(String localDirectory) {

        File src = new File(localDirectory);
            try {
                ftpUrl = ftpUrl + src.getName() + "/";

                ftp.makeDirectory(ftpUrl);

            }catch (IOException e) {
                log.info(ftpUrl + "目录创建失败");
                e.printStackTrace();
            }
            File[] allFile = src.listFiles();

            for (int currentFile = 0;currentFile < allFile.length;currentFile++) {

                if (!allFile[currentFile].isDirectory()) {

                    String srcName = allFile[currentFile].getPath().toString();

                    uploadFile(new File(srcName));
                }
            }
            for (int currentFile = 0;currentFile < allFile.length;currentFile++) {

                if (allFile[currentFile].isDirectory()) {

                   /**递归*/
                    uploadDirectory(allFile[currentFile].getPath().toString());
                }
            }
        return true;
    }
    /***
     * @下载文件夹
     * @param localDirectoryPath 本地地址
     * @param remoteDirectory 远程文件夹
     * */
    @Override
    public boolean downLoadDirectory(HttpServletResponse resp,String localDirectoryPath,String remoteDirectory) {
        try {
            String fileName = new File(remoteDirectory).getName();

            localDirectoryPath = localDirectoryPath + fileName + "//";

            new File(localDirectoryPath).mkdirs();

            FTPFile[] allFile = ftp.listFiles(remoteDirectory);

            for (int currentFile = 0;currentFile < allFile.length;currentFile++) {

                if (!allFile[currentFile].isDirectory()) {

                    downloadFile(resp,allFile[currentFile].getName()/*,localDirectoryPath,remoteDirectory*/);
                }
            }
            for (int currentFile = 0;currentFile < allFile.length;currentFile++) {

                if (allFile[currentFile].isDirectory()) {

                    String strremoteDirectoryPath = remoteDirectory + "/"+ allFile[currentFile].getName();

                    downLoadDirectory(resp,localDirectoryPath,strremoteDirectoryPath);
                }
            }
        }catch (IOException e) {
            e.printStackTrace();
            log.info("下载文件夹失败");
            return false;
        }
        return true;
    }

    /**
     * 删除FTP
     * @param ftpName  ftp上的文件名
//     *  @param path  ftp上的文件路径
     *  @return 成功返回true   失败返回false
     * @throws SocketException
     * @throws IOException
     */
    @Override
    public String deleteFile(String ftpName) {

            try {

                /**切换目录，目录不存在创建目录*/
                if(ftp.changeWorkingDirectory(ftpName)){

                    FTPFile[] files = ftp.listFiles();

                    if(Objects.nonNull(files)||files.length>0){

                        for (FTPFile f : files) {

                            System.out.println("文件名"+f.getName());

                            if(f.isFile()){
                                if(ftp.deleteFile(ftpName.equals(ftpUrl) ? ftpName + f.getName() : ftpName + "/"+f.getName()))

                                    log.info("remove "+f.getName());
                                else
                                    return "error";
                            }else{
                                deleteFile(ftpName.equals(ftpUrl) ? f.getName() :ftpName+"/" + f.getName());
                            }
                        }
                        /**切换到父目录，不然删不掉文件夹*/
                        ftp.changeWorkingDirectory(ftpName.substring(0, ftpName.lastIndexOf("/")));

                        if(!ftp.removeDirectory(ftpName))
                           return "error";
                    }
                }
                ftp.deleteFile(ftpName);

            } catch (SocketException e) {
                return "SocketNotSuccess";
            } catch (IOException e) {
                return "SocketNotSuccess";
            }
        return "success";
    }

    /**
     * 判断ftp服务器文件是否存在
     * */
    @Override
    public boolean existFile(String path) throws IOException {

        FTPFile[] ftpFileArr = ftp.listFiles(path);

        if (ftpFileArr.length > 0) {
            return true;
        }
        return false;
    }
    public boolean hasFile(String filePath) {

        boolean flag = false;

        boolean flag1 = ftpLogin();

        if(flag1){

            try {

                /**设置文件类型为二进制，与ASCII有区别*/
                ftp.setFileType(ftp.BINARY_FILE_TYPE);

                /**设置编码格式*/
                ftp.setControlEncoding("GBK");

                /**提取绝对地址的目录以及文件名*/
                String dir = filePath.substring(0, filePath.lastIndexOf("/"));

                String file = filePath.substring(filePath.lastIndexOf("/") + 1);

               /**进入文件所在目录，注意编码格式，以能够正确识别中文目录*/
                ftp.changeWorkingDirectory(new String(dir.getBytes("GBK"), ftp.DEFAULT_CONTROL_ENCODING));

                /**检验文件是否存在*/
                InputStream is = ftp
                        .retrieveFileStream(new String(file.getBytes("GBK"), ftp.DEFAULT_CONTROL_ENCODING));

                if (is == null || ftp.getReplyCode() == FTPReply.FILE_UNAVAILABLE) {
                    return false;
                }

                if (is != null) {
                    is.close();
                    ftp.completePendingCommand();
                }
                return true;

            } catch (Exception e) {

                e.printStackTrace();
            } finally {
                if (ftp.isConnected()) {
                    try {
                        ftp.disconnect();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return flag;
    }



    /**
     * 递归遍历出目录下面所有文件
     * @param pathName 需要遍历的目录，必须以"/"开始和结束
     * @throws IOException
     */
    @Override
    public Map getFileList(String pathName) throws IOException {

        /**登录ftp服务器*/
        ftpLogin();

        /**设置返回参数*/
        Map<String,Object> resultMap = new HashMap<>();

        FTPFile[] files = null;

            try {
                /**切换到文件对应目录路径*/
                ftp.changeWorkingDirectory(pathName);

                /**获得对应文件*/
                files = ftp.listFiles();
            }catch (Exception e){
                e.printStackTrace();
                System.out.println("FTP读取数据异常！");
            }
        resultMap.put("fileList",files);

        /**退出ftp服务器*/
        ftpLogOut();
        return resultMap;
    }
    /**
     * 递归遍历目录下面指定的文件名
     * @param pathName 需要遍历的目录，必须以"/"开始和结束
     * @param ext      文件的扩展名
     * @throws IOException
     */
    @Override
    public  Map<String,Object> getFileListByPath(String pathName, String ext) throws IOException {

        ftpLogin();

        /**定义一个返回map*/
        Map<String,Object> resultMap = new HashMap<>();

        /**设置文件名集合*/
        List<String> numberList = new ArrayList<>();

        /**设置文件夹集合*/
        List<String> directoryList = new ArrayList<>();

        /**设置文件计量总数*/
        int count = 0;

        /**设置文件夹计量总数*/
        int count2 = 0;

        if (pathName.startsWith("/") && pathName.endsWith("/")) {

           /**更换目录到当前目录*/
            ftp.changeWorkingDirectory(pathName);

            FTPFile[] files = ftp.listFiles();

            for (FTPFile file : files) {

                /**所遍历的文件是文件*/
                if (file.isFile()) {

                    /**判断文件后缀*/
                    if (file.getName().endsWith(ext)) {
                        count++;

                        String s = file.getName();

                        /**存入文件名*/
                        numberList.add(s.substring(0,s.indexOf(".")));
                    }
                    /**所遍历的文件是文件夹*/
                } else if (file.isDirectory()) {

                    count2++;
                    /**存入文件夹名*/
                    directoryList.add(file.getName());

//                    if (!".".equals(file.getName()) && !"..".equals(file.getName())) {
//
//                        getFileListByPath(pathName + file.getName() + "/", ext);
//                    }
                }
            }
        }
        /**文件总数*/
        resultMap.put("count",count);
        /**文件夹总数*/
        resultMap.put("count2",count2);
        /**文件名集合*/
        resultMap.put("numberList",numberList);
        /**文件夹名集合*/
        resultMap.put("directoryList",directoryList);

        ftpLogOut();

        return resultMap;
    }
//    public static void main(String[] args) throws IOException {
//      FtpApcheUtils ftp = new FtpApcheUtils();
////
////        File file = new File("E:\\fhadmin_a6.sql");
////        File file1 = new File("E:\\commons-net-3.6.jar");
////        File file2 = new File("E:\\dingtalk-sdk-java.zip");
////        File file3 = new File("E:\\bcprov-jdk15-1.46-sources.jar");
////        File file4 = new File("E:\\imooc-springboot-wxlogin-master.zip");
//////       boolean flag  =  ftp.uploadFile(file,"/");
////       boolean flag1  =  ftp.uploadFile(file1,"/");
////       boolean flag2 =  ftp.uploadFile(file2,"/");
////       boolean flag3  =  ftp.uploadFile(file3,"/");
////       boolean flag4  =  ftp.uploadFile(file4,"/");
////       System.out.println("flag : 上传结果为"+flag);
//      ftp.ftpLogin();
////        boolean flag1  = ftp.downloadFile("app.js", "C:\\Users\\Administrator\\Desktop\\", "/test");
////        上传文件夹
////        boolean uploadFlag = ftp.uploadDirectory("E:\\test", "/"); //如果是admin/那么传的就是所有文件，如果只是/那么就是传文件夹
////        System.out.println("uploadFlag : " + uploadFlag);
//      //下载文件夹
////
////        ftp.downLoadDirectory("C:\\Users\\Administrator\\Desktop\\", "/test/");
////
////      String result = ftp.deleteFile("/test");
//
////      System.out.println("删除文件"+result);
////
////        FTPFile[] fileList = ftp.getFileList("/");
//
//        Map<String,Object> map = ftp.getFileListByPath("/ts/","");
//
//        List<String> bianNoList = (List<String>) map.get("numberList");
//
//        List<String> directoryList = (List<String>) map.get("directoryList");
//
//       if(bianNoList!=null&&bianNoList.size()>0){
//           for (String s : bianNoList) {
//               System.out.println("编号为:"+s);
//           }
//       }
//
//        if(directoryList!=null&&directoryList.size()>0){
//            for (String s : directoryList) {
//                System.out.println("文件夹名为:"+s);
//            }
//        }
//        int count = (int) map.get("count");
//
//        int count2 = (int) map.get("count2");
//
//        System.out.println("总数为:"+count);
//
//        System.out.println("文件夹总数为:"+count2);
////
//      ftp.ftpLogOut();
//  }
}
