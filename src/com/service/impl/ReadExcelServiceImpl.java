package com.service.impl;

import com.dao.DaoSupport;
import com.entity.I_MES_GTBJQD;
import com.entity.I_MES_GTCNC;
import com.entity.I_MES_MBXS;
import com.entity.I_MES_TLMCD;
import com.test.entity.PageData;
import com.util.ObjectExcelRead;
import org.apache.commons.lang.StringUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service("ReadExcelService")
public class ReadExcelServiceImpl implements ReadExcelService {
    @Resource
    private DaoSupport daoSupport;


    @Override
    public void insertExcelData(String type) throws Exception {

        String filepath = "E:\\zxdf\\";
        String filename = "01亚度柜体板件清单.xlsx";

        String filename1 = "02亚度柜体CNC清单.xlsx";
        String filename2 = "03亚度门板吸塑清单.xlsx";
        String filename3 = "04亚度推拉门拆单表（以后）.xlsx";
        String filename4 = "05亚度五金配件清单.xlsx";
        String filename5 = "06亚度装饰附件清单.xlsx";

        switch (type) {
            case "0":
                List<I_MES_GTBJQD> gtbjqdList = new ArrayList<>();

                I_MES_GTBJQD gtbjqd = null;
                List<PageData> objectList = (List) ObjectExcelRead.parseExcel(filepath, filename);

                for (PageData p : objectList) {

                    gtbjqd = new I_MES_GTBJQD();
                    gtbjqd.setSerialNumber(p.getString("var0"));//序号
                    gtbjqd.setProductName(p.getString("var1"));//产品名称
                    gtbjqd.setOrderDate(p.getString("var2"));//物料名称
                    gtbjqd.setCustomerName(p.getString("var3"));//物料名称
                    gtbjqd.setDealerName(p.getString("var4"));//物料名称
                    gtbjqd.setBatchNumber(p.getString("var5"));//物料名称
                    gtbjqd.setOrderNumber(p.getString("var6"));//物料名称
                    gtbjqd.setPartName(p.getString("var7"));//物料名称
                    gtbjqd.setLinePlan(p.getString("var8"));//物料名称
                    gtbjqd.setPartNumber(p.getString("var9"));//物料名称
                    gtbjqd.setPartLong(Double.parseDouble(p.getString("var10")));//物料名称
                    gtbjqd.setPartWith(Double.parseDouble(p.getString("var11")));//物料名称
                    gtbjqd.setPartHeight(Double.parseDouble(p.getString("var12")));//物料名称
                    gtbjqd.setAmount(Integer.parseInt(p.getString("var13")));//物料名称
                    gtbjqd.setColorCode(p.getString("var14"));//物料名称
                    gtbjqd.setMaterial(p.getString("var15"));//物料名称
                    gtbjqd.setColor(p.getString("var16"));//物料名称
                    gtbjqd.setTexture(p.getString("var17"));//物料名称
                    gtbjqd.setDosage(Double.parseDouble(p.getString("var18")));//物料名称
                    System.out.println(gtbjqd.getDosage());
                    gtbjqd.setEdgeBand(Double.parseDouble(p.getString("var19")));//物料名称
                    gtbjqd.setBarCode(p.getString("var20"));//物料名称
                    gtbjqd.setRemarks(p.getString("var21"));//物料名称

                    gtbjqdList.add(gtbjqd);
                }

                if (gtbjqdList != null && gtbjqdList.size() > 0) {
                    daoSupport.save("TestMapper.gtbjqdInsert", gtbjqdList);
                }
                break;
            case "1":
                List<I_MES_GTCNC> gtcncList = new ArrayList<>();

                I_MES_GTCNC gtcnc = null;
                List<PageData> objectList1 = (List) ObjectExcelRead.parseExcel(filepath, filename1);

                for (PageData p : objectList1) {

                    gtcnc = new I_MES_GTCNC();
                    gtcnc.setSerialNumber(p.getString("var0"));//序号
                    gtcnc.setProductName(p.getString("var1"));//产品名称
                    gtcnc.setOrderDate(p.getString("var2"));//物料名称
                    gtcnc.setCustomerName(p.getString("var3"));//物料名称
                    gtcnc.setDealerName(p.getString("var4"));//物料名称
                    gtcnc.setBatchNumber(p.getString("var5"));//物料名称
                    gtcnc.setOrderNumber(p.getString("var6"));//物料名称
                    gtcnc.setPartName(p.getString("var7"));//物料名称
                    gtcnc.setLinePlan(p.getString("var8"));//物料名称
                    gtcnc.setPartNumber(p.getString("var9"));//物料名称
                    gtcnc.setPartLong(Double.parseDouble(p.getString("var10")));//物料名称
                    gtcnc.setPartWith(Double.parseDouble(p.getString("var11")));//物料名称
                    gtcnc.setPartHeight(Double.parseDouble(p.getString("var12")));//物料名称
                    gtcnc.setAmount(Integer.parseInt(p.getString("var13")));//物料名称
                    gtcnc.setColorCode(p.getString("var14"));//物料名称
                    gtcnc.setMaterial(p.getString("var15"));//物料名称
                    gtcnc.setColor(p.getString("var16"));//物料名称
                    gtcnc.setTexture(p.getString("var17"));//物料名称
                    gtcnc.setDosage(Double.parseDouble(p.getString("var18")));//物料名称
                    System.out.println(gtcnc.getDosage());
                    gtcnc.setEdgeBand(Double.parseDouble(p.getString("var19")));//物料名称
                    gtcnc.setBarCode(p.getString("var20"));//物料名称
                    gtcnc.setRemarks(p.getString("var21"));//物料名称

                    gtcncList.add(gtcnc);
                }

                if (gtcncList != null && gtcncList.size() > 0) {
                    daoSupport.save("TestMapper.gtCNCInsert", gtcncList);
                }
                break;
            case "2":
                List<I_MES_MBXS> mbxsList = new ArrayList<>();

                I_MES_MBXS mbxs = null;
                List<PageData> objectList2 = (List) ObjectExcelRead.parseExcel(filepath, filename2);

                for (PageData p : objectList2) {

                    mbxs = new I_MES_MBXS();
                    mbxs.setSerialNumber(p.getString("var0"));//序号
                    mbxs.setProductName(p.getString("var1"));//产品名称
                    mbxs.setOrderDate(p.getString("var2"));//物料名称
                    mbxs.setCustomerName(p.getString("var3"));//物料名称
                    mbxs.setDealerName(p.getString("var4"));//物料名称
                    mbxs.setBatchNumber(p.getString("var5"));//物料名称
                    mbxs.setOrderNumber(p.getString("var6"));//物料名称
                    mbxs.setPartName(p.getString("var7"));//物料名称
                    mbxs.setLinePlan(p.getString("var8"));//物料名称
                    mbxs.setPartNumber(p.getString("var9"));//物料名称
                    mbxs.setPartType(p.getString("var10"));//物料名称
                    mbxs.setPartLong(Double.parseDouble(p.getString("var11")));//物料名称
                    mbxs.setPartWith(Double.parseDouble(p.getString("var12")));//物料名称
                    mbxs.setPartHeight(Double.valueOf(p.getString("var13")));//物料名称
                    mbxs.setAmount(Integer.parseInt(p.getString("var14")));//物料名称
                    mbxs.setColorCode(p.getString("var15"));//物料名称
                    mbxs.setMaterial(p.getString("var16"));//物料名称
                    mbxs.setColor(p.getString("var17"));//物料名称
                    mbxs.setOpenDirection(p.getString("var18"));//物料名称
                    mbxs.setHandlePosition(p.getString("var19"));//物料名称
                    mbxs.setHandleVacancy(p.getString("var20"));//物料名称
                    mbxs.setTexture(p.getString("var21"));//物料名称
                    mbxs.setDosage(Double.parseDouble(p.getString("var22")));//物料名称
                    System.out.println(mbxs.getDosage());
                    Double s = 0.0;
                    if(StringUtils.isBlank(p.getString("var23"))){
                        mbxs.setLineUsage(s);//物料名称
                    }else{
                        mbxs.setLineUsage(Double.parseDouble(p.getString("var23")));//物料名称
                    }
                    mbxs.setBarCode(p.getString("var24"));//物料名称
                    mbxs.setRemarks(p.getString("var25"));//物料名称

                    mbxsList.add(mbxs);
                }
                if (mbxsList != null && mbxsList.size() > 0) {
                    daoSupport.save("TestMapper.mbxsInsert", mbxsList);
                }
                break;
            case "3":
                List<I_MES_TLMCD> tlmcdList = new ArrayList<>();

                I_MES_TLMCD tlmcd = null;
                List<PageData> objectList3 = (List) ObjectExcelRead.parseExcel(filepath, filename3);

                for (PageData p : objectList3) {

                    tlmcd = new I_MES_TLMCD();
                    tlmcd.setSerialNumber(p.getString("var0"));//序号
                    tlmcd.setProductName(p.getString("var1"));//产品名称
                    tlmcd.setOrderDate(p.getString("var2"));//物料名称
                    tlmcd.setCustomerName(p.getString("var3"));//物料名称
                    tlmcd.setDealerName(p.getString("var4"));//物料名称
                    tlmcd.setBatchNumber(p.getString("var5"));//物料名称
                    tlmcd.setOrderNumber(p.getString("var6"));//物料名称
                    tlmcd.setDoorStyle(p.getString("var7"));//物料名称
                    tlmcd.setPartType(p.getString("var8"));//物料名称
                    tlmcd.setAluminumModel(p.getString("var9"));//物料名称
                    tlmcd.setPartLong(Double.parseDouble(p.getString("var10")));//物料名称
                    tlmcd.setPartWith(Double.parseDouble(p.getString("var11")));//物料名称
                    tlmcd.setPartHeight(Double.parseDouble(p.getString("var12")));//物料名称
                    tlmcd.setAmount(Integer.parseInt(p.getString("var13")));//物料名称
                    tlmcd.setColor(p.getString("var14"));//物料名称
                    tlmcd.setTexture(p.getString("var15"));//物料名称
                    tlmcd.setRemarks("");//物料名称

                    tlmcdList.add(tlmcd);
                }
                if (tlmcdList != null && tlmcdList.size() > 0) {
                    daoSupport.save("TestMapper.tlmcdInsert", tlmcdList);
                }
                break;
        }

    }

    public static void main(String[] args) throws Exception {

        ApplicationContext ac = new ClassPathXmlApplicationContext("classpath*:applicationContext.xml");

        ReadExcelService readExcelService = (ReadExcelService) ac.getBean(ReadExcelServiceImpl.class);

        readExcelService.insertExcelData("3");

    }
}
