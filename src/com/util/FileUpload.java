package com.util;

import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

/**
 * 上传文件
 * 创建人：FH admin
 * 修改日期：2018年7月23日
 * @version
 */
public class FileUpload {

	/**上传文件
	 * @param file 			//文件对象
	 * @param filePath		//上传路径
	 * @param fileName		//文件名
	 * @return  文件名
	 */
	public static String fileUp(MultipartFile file, String filePath, String fileName){
		String extName = ""; // 扩展名格式：
		try {
			if (file.getOriginalFilename().lastIndexOf(".") >= 0){
				extName = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf("."));
			}
			copyFile(file.getInputStream(), filePath, fileName+extName).replaceAll("-", "");
		} catch (IOException e) {
			System.out.println(e);
		}
		return fileName+extName;
	}

    /**
     * 递归获取文件路径
     * @param file
     * @param fileNamelist
     * @return
     */
    public static List<String> getFileName(File file,List<String> fileNamelist){
        try {
            if(file.exists()){
                if(!file.isFile()){
                    File[] files =file.listFiles();
                    for (File file2:files) {
                        getFileName(file2,fileNamelist);
                    }
                }else{
                    String fileName=file.getPath();
                    String Name= "/unloadFile"+fileName.split("unloadFile")[1];
                    Name=Name.replaceAll("\\\\","/");
                    fileNamelist.add(Name);
                }
            }
            return fileNamelist;
        }catch (Exception e){
            return null;
        }
    }
	/**
	 * 写文件到当前目录的upload目录中
	 * @param in
	 * @param fileName
	 * @throws IOException
	 */
	public static String copyFile(InputStream in, String dir, String realName)
			throws IOException {
		File file = mkdirsmy(dir,realName);
		FileUtils.copyInputStreamToFile(in, file);
		in.close();
		return realName;
	}
	
	
	/**判断路径是否存在，否：创建此路径
	 * @param dir  文件路径
	 * @param realName  文件名
	 * @throws IOException 
	 */
	public static File mkdirsmy(String dir, String realName) throws IOException{
		File file = new File(dir, realName);
		if (!file.exists()) {
			if (!file.getParentFile().exists()) {
				file.getParentFile().mkdirs();
			}
			file.createNewFile();
		}
		return file;
	}
	
	
	/**下载网络图片上传到服务器上
	 * @param httpUrl 图片网络地址
	 * @param filePath	图片保存路径
	 * @param myFileName  图片文件名(null时用网络图片原名)
	 * @return	返回图片名称
	 */
	public static String getHtmlPicture(String httpUrl, String filePath , String myFileName) {
		
		URL url;						//定义URL对象url
		BufferedInputStream in;			//定义输入字节缓冲流对象in
		FileOutputStream file;			//定义文件输出流对象file
		try {
			String fileName = null == myFileName?httpUrl.substring(httpUrl.lastIndexOf("/")).replace("/", ""):myFileName; //图片文件名(null时用网络图片原名)
			url = new URL(httpUrl);		//初始化url对象
			in = new BufferedInputStream(url.openStream());									//初始化in对象，也就是获得url字节流
			//file = new FileOutputStream(new File(filePath +"\\"+ fileName));
			file = new FileOutputStream(mkdirsmy(filePath,fileName));
			int t;
			while ((t = in.read()) != -1) {
				file.write(t);
			}
			file.close();
			in.close();
			return fileName;
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
		
	}

	/**
	 * 针对多文件上传修改上传
	 * @param orderNumber 生成文件夹名称
	 * @param files 文件流
	 * @param previousImgs 需要删除的文件名拼接的字符串
	 * @param path 文件路径orderNumber的上级目录
	 * @return
	 */
	public static String  fileUpload(String orderNumber, List<MultipartFile> files, String previousImgs, String path) {
		File file2=new File(path+"/"+orderNumber);
		if (file2.exists()) { // 判断文件父目录是否存在
			if(Objects.nonNull(previousImgs)&&!Objects.equals("",previousImgs)){
				String[] array=previousImgs.split(",");
				List<String> list= Arrays.asList(array);
				deleteFile(file2,list);
			}else{
				deleteFile(file2,null);
			}
		}
		for (MultipartFile file : files) {
			String fileName = file.getOriginalFilename();
			int size = (int) file.getSize();
			if (file.isEmpty()) {
				return "false";
			} else {
				File dest = new File(path + "/" +orderNumber+"/"+ fileName);
				if (!dest.getParentFile().exists()) { // 判断文件父目录是否存在
					if(!dest.getParentFile().getParentFile().exists()){
						dest.getParentFile().getParentFile().mkdir();
					}
					dest.getParentFile().mkdir();
				}
				try {
					file.transferTo(dest);
				} catch (Exception e) {
					e.printStackTrace();
					return "false";
				}
			}
		}
		return "true";
	}
	/**
	 * 删除文件
	 * */
	public static void  deleteFile(File file,List<String> fileNames){
		try {
			if(Objects.nonNull(fileNames)){
				if(file.exists()){
					if(!file.isFile()){
						File[] files =file.listFiles();
						for (File file2:files) {
							deleteFile(file2,fileNames);
						}
					}else{
						String fileName=file.getName();
						if(fileNames.contains(fileName)){
							file.delete();
						}
					}
				}
			}else{
				if(file.exists()){
					if(!file.isFile()){
						File[] files =file.listFiles();
						for (File file2:files) {
							deleteFile(file2,fileNames);
						}
					}else{
						file.delete();
					}
				}
			}
		}catch (Exception e){
			e.printStackTrace();
		}
	}
}
