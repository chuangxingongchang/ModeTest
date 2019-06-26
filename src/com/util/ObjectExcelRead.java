package com.util;

import com.test.entity.PageData;
import org.apache.commons.lang.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * 从EXCEL导入到数据库
 * 创建人：FH admin
 * 创建时间：2017年12月23日
 * @version
 */
public class ObjectExcelRead {

    private static final Logger log = LoggerFactory.getLogger(ObjectExcelRead.class);

    /**
     *
     * @param filepath 文件路径
     * @param filename 文件名
     * @param startrow 开始行号
     * @param startcol 开始列号
     * @param sheetnum sheet
     * @return list
     */
    public static List<Object> readExcel(String filepath, String filename, int startrow, int startcol, int sheetnum){

        List<Object> varList = new ArrayList<>();

        try {
            File target = new File(filepath,filename);

            FileInputStream fi = new FileInputStream(target);

            HSSFWorkbook wb = new HSSFWorkbook(fi);

            /**sheet 从0开始*/
            HSSFSheet sheet = wb.getSheetAt(sheetnum);

            /**取得最后一行的行号*/
            int rowNom = sheet.getLastRowNum()+1;

            for (int i = startrow; i <rowNom;i++) {

                PageData varpd = new PageData();

                /**行*/
                HSSFRow row = sheet.getRow(i);

                /**每行的最后一个单元格位置*/
                int cellNum = row.getLastCellNum();

                /**列循环开始*/
                for (int j = startcol;j<cellNum;j++){

                    HSSFCell cell = row.getCell(Short.parseShort(j+""));

                    String cellValue = null;

                    if(Objects.nonNull(cell)){

                        /**判断excel单元格内容的格式，并对其进行转换，以便插入数据库*/
                        switch (cell.getCellType()){

                            case 0:
                                cellValue = String.valueOf((double) cell.getNumericCellValue());
                                break;
                            case 1:
                                cellValue = cell.getStringCellValue();
                                break;
                            case 2:
                                cellValue = cell.getNumericCellValue() + "";
                                break;
                            case 3:
                                cellValue = null;
                                break;
                            case 4:
                                cellValue = String.valueOf(cell.getBooleanCellValue());
                                break;
                            case 5:
                                cellValue = String.valueOf(cell.getErrorCellValue());
                                break;
                        }
                    }else{
                        cellValue = null;
                    }
                    varpd.put("var"+j,cellValue);
                }

                varList.add(varpd);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return varList;
    }
    public static List<Object> parseExcel(String realPath, String fileName){

        List<Object> varList = null;

        String [] pfix= fileName.split("\\.");

        String suffix = pfix[pfix.length -1];

        if( suffix!=null&&!suffix.equals("")&&suffix.equals("xls")){

            System.out.println("xls");
            // jxl方法可读取.xls格式
            varList = readExcel(realPath,fileName,1,0,0);

        }else if(suffix .equals("xlsx")){

            System.out.println("xlsx");

            // poi方法可读取Excel2007即.xlsx格式
            varList = poiExcel(realPath,fileName);
        }
        return varList;
    }

    /**
     * 读取 xls JXL
     * @param realPath
     * @param fileName
     */
//    private static void jlxExcel(String realPath,String fileName){
//        //===============jlx方法=================
//        try{
//            File fileDes = new File(realPath);
//            InputStream str = new FileInputStream(fileDes);
//            // 构造Workbook（工作薄）对象
//            Workbook rwb=Workbook.getWorkbook(str);
//            Sheet rs= rwb.getSheet(0);//获取第一张工作表
//            int rsRows= rs.getRows();//获取Sheet表中所包含的总行数
//            int rsCols=rs.getColumns();//获取Sheet表中所包含的总列数
//            log.info("========行========"+rsRows+"=====列========"+rsCols);
//            for(int i=1;i<rsRows;i++){//读取行
//                log.info("========执行第========"+i+"行");
//                for(int j=0;j<rsCols;j++){
//                    log.info("========执行第========"+j+"列");
//                    CommandLine.Help.TextTable.Cell coo=rs.getCell(j, i);//单元格定位列，再定位行
//                    log.info("========coo========"+coo);
//                    String strc=coo.getContents();//读取内容
//                    log.info("========读取内容strc========"+strc);
//                    System.out.println("文件"+fileName+"的内容为："+strc);
//                }
//            }
//            rwb.close();
//        }catch (FileNotFoundException e) {
//            e.printStackTrace();
//        } catch (BiffException e) {
//            e.printStackTrace();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//
//        //==========读取excel文件内容=结束=====================
//
//    }

    /**
     * POI读取   xlsx
     * @param realPath
     * @param fileName
     */
    private static List<Object> poiExcel(String realPath,String fileName){

        List<Object> varList = new ArrayList<>();
        try{
            File fileDes = new File(realPath+File.separator+fileName);
            InputStream str = new FileInputStream(fileDes);
            XSSFWorkbook xwb = new XSSFWorkbook(str);  //利用poi读取excel文件流
            XSSFSheet st = xwb.getSheetAt(0);  //读取sheet的第一个工作表
            int rows=st.getLastRowNum()+1;//总行数
            int cols;//总列数
            log.info("========行========"+rows);
            for(int i=1;i<rows;i++){

                PageData varpd = new PageData();

                XSSFRow row=st.getRow(i);//读取某一行数据

                if(row!=null){
                    //获取行中所有列数据
                    cols=row.getLastCellNum();
                    log.info("========行========"+rows+"=====列========"+cols);
                    for(int j=0; j<cols; j++){

                        XSSFCell cell=row.getCell(j);

                        String cellValue = null;

                        if(cell==null){

                            cellValue = "";
                        }else{
                            //判断单元格的数据类型
                            switch (cell.getCellType()) {

                                case XSSFCell.CELL_TYPE_NUMERIC: // 数字

                                    cellValue = String.valueOf((double)cell.getNumericCellValue());

                                    break;
                                case XSSFCell.CELL_TYPE_STRING: // 字符串

                                    cellValue = cell.getStringCellValue();

                                    break;
                                case XSSFCell.CELL_TYPE_BOOLEAN: // Boolean

                                    cellValue = String.valueOf(cell.getBooleanCellValue());

                                    break;
                                case XSSFCell.CELL_TYPE_FORMULA: // 公式

                                    cellValue = String.valueOf((double)cell.getNumericCellValue());
                                    break;
                                case XSSFCell.CELL_TYPE_BLANK: // 空值

                                    cellValue = "";
                                    break;
                                case XSSFCell.CELL_TYPE_ERROR: // 故障
                                    cellValue = String.valueOf(cell.getErrorCellString());
                                    break;
                                default:
                                    break;
                            }
                        }

                        varpd.put("var"+j,cellValue);
                    }
                }
                if(StringUtils.isBlank(String.valueOf(varpd.get("var0")))){
                    break;
                }else{
                    varList.add(varpd);
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
        return varList;
    }

    public static void main(String[] args) throws Exception {


        /**文件路径*/
        String filepath = "E:\\zxdf\\";

        String filename = "01亚度柜体板件清单.xlsx";

        /**
         * 执行读EXCEL操作,读出的数据导入List 2:从第3行开始；0:从第A列开始；0:第0个sheet
         */


    }
}
