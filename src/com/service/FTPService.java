package com.service;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;
import java.util.Map;

/**
 * @Description ftp实现文件上传下载
 * @author lengdl
 * @date 2019年6月24日10:32:15
 */
public interface FTPService {
    /**
     * ftp服务器登录
     */
    public boolean ftpLogin();

    /**
     * ftp服务器退出
     */
    public void ftpLogOut();

    /**
     * ftp服务器上传文件
     */
    public boolean uploadFile(File localFile);

    /**
     * ftp服务器文件下载
     */
    public Boolean downloadFile(HttpServletResponse resp,String remoteFileName);

    /**
     * ftp服务器文件夹上传
     */
    public boolean uploadDirectory(String localDirectory);

    /**
     *ftp服务器文件夹下载
     */
    public boolean downLoadDirectory(HttpServletResponse resp,String localDirectoryPath,String remoteDirectory);

    /**
     *ftp服务器文件删除
     */
    public String deleteFile(String ftpName);

    /**
     *ftp服务器文件是否存在
     */
    public boolean existFile(String path) throws IOException;

    /**
     *ftp服务器文件列表
     */
    public Map<String,Object> getFileList(String pathName) throws IOException;

    /**
     *ftp服务器指定文件列表
     */
    public Map<String,Object> getFileListByPath(String pathName, String ext) throws IOException;
}
