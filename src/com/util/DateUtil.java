package com.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/** 
 * 说明：日期处理
 * 创建人：FH admin
 * 修改时间：2017年11月24日
 * @version
 */
public class DateUtil {
	
	private final static SimpleDateFormat sdfYear = new SimpleDateFormat("yyyy");
	private final static SimpleDateFormat SttaDay = new SimpleDateFormat("yyyy-MM");
	private final static SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
	private final static SimpleDateFormat sdfDays = new SimpleDateFormat("yyyyMMdd");
	private final static SimpleDateFormat sdfTime = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
	private final static SimpleDateFormat sdfTimes = new SimpleDateFormat("yyyyMMddHHmmss");
	private final static SimpleDateFormat sdfTimesSSS = new SimpleDateFormat("yyyyMMddHHmmssSSS");
	private static final String cronftime = "ss mm HH dd MM ? yyyy";

	/**
	 * 获取yyyyMMddHHmmss格式
	 * @return
	 */
	public static String getSdfTimes() {
		return sdfTimes.format(new Date());
	}
	/**
	 * 获取yyyyMMddHHmmssSSS格式
	 * @return
	 */
	public static String getSdfTimesSSS() {
		return sdfTimesSSS.format(new Date());
	}

	
	/**
	 * 获取YYYY格式
	 * @return
	 */
	public static String getYear() {
		return sdfYear.format(new Date());
	}
	/**
	 * 获取YYYY-MM格式
	 * @return
	 */
	public static String getSttaDay() {
		return SttaDay.format(new Date());
	}


	/**
	 * 获取YYYY-MM-DD格式
	 * @return
	 */
	public static String getDay() {
		return sdfDay.format(new Date());
	}
	
	/**
	 * 获取YYYY-MM-DD格式
	 * @return
	 * @throws ParseException 
	 */
	public static Date getNowDay(String day) throws ParseException {
		return sdfDay.parse(day);
	}
	
	/**
	 * 获取YYYYMMDD格式
	 * @return
	 */
	public static String getDays(){
		return sdfDays.format(new Date());
	}

	/**
	 * 获取YYYY-MM-DD HH:mm:ss格式
	 * @return
	 */
	public static String getTime() {
		return sdfTime.format(new Date());
	}

	/**
	* @Title: compareDate
	* @Description: TODO(日期比较，如果s>=e 返回true 否则返回false)
	* @param s
	* @param e
	* @return boolean  
	* @throws
	* @author fh
	 */
	public static boolean compareDate(String s, String e) {
		if(fomatDate(s)==null||fomatDate(e)==null){
			return false;
		}
		return fomatDate(s).getTime() >=fomatDate(e).getTime();
	}

	/**
	 * 格式化日期
	 * @return
	 */
	public static Date fomatDate(String date) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			return fmt.parse(date);
		} catch (ParseException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 校验日期是否合法
	 * @return
	 */
	public static boolean isValidDate(String s) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			fmt.parse(s);
			return true;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return false;
		}
	}
	
	/**
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public static int getDiffYear(String startTime,String endTime) {
		DateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
		try {
			//long aa=0;
			int years=(int) (((fmt.parse(endTime).getTime()-fmt.parse(startTime).getTime())/ (1000 * 60 * 60 * 24))/365);
			return years;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return 0;
		}
	}
	 
	/**
     * <li>功能描述：时间相减得到天数
     * @param beginDateStr
     * @param endDateStr
     * @return
     * long 
     * @author Administrator
     */
    public static long getDaySub(String beginDateStr,String endDateStr){
        long day=0;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Date beginDate = null;
        Date endDate = null;
        
            try {
				beginDate = format.parse(beginDateStr);
				endDate= format.parse(endDateStr);
			} catch (ParseException e) {
				e.printStackTrace();
			}
            day=(endDate.getTime()-beginDate.getTime())/(24*60*60*1000);
            //System.out.println("相隔的天数="+day);
      
        return day;
    }
    
    /**
     * 得到n天之后的日期
     * @param days
     * @return
     */
    public static String getAfterDayDate(String days) {
    	int daysInt = Integer.parseInt(days);
    	
        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();
        
        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdfd.format(date);
        
        return dateStr;
    }
    
    /**
     * 得到n天之后是周几
     * @param days
     * @return
     */
    public static String getAfterDayWeek(String days) {
    	int daysInt = Integer.parseInt(days);
        Calendar canlendar = Calendar.getInstance(); // java.util包
        canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
        Date date = canlendar.getTime();
        SimpleDateFormat sdf = new SimpleDateFormat("E");
        String dateStr = sdf.format(date);
        return dateStr;
    }
    
    /**
     * 得到当前时间距离下一天的秒数
     * @param days
     * @return
     * @throws Exception 
     */
    public static int getNextDayS() throws Exception {
    	SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
        long nowTime = System.currentTimeMillis();
        String str = sdf.format(nowTime + 24 * 3600 * 1000);
        Date nextTime = null;
        try {
            nextTime = sdf.parse(str);
        } catch (ParseException e) {
        	throw new Exception("0000");
        }
        return (int) Math.floor((nextTime.getTime() - nowTime) / (double) 1000);
    }
    /**
     * 根据传入的字符串转换为对应的时间
     * <p>Title: getDate</p>  
     * <p>Description: </p>  
     * @param strDate
     * @return
     * @throws ParseException  
     * @author lcb
     */
    public static Date getDate(String strDate) throws ParseException {
    	return sdfTime.parse(strDate);
    }
    /**
     * 将日期转化为quarz字符
     * <p>Title: getCron</p>  
     * <p>Description: </p>  
     * @param date
     * @return  
     * @author lcb
     */
    public static String getCron(Date  date){
    	SimpleDateFormat sdf = new SimpleDateFormat(cronftime);
    	String str = "";
    	if (date != null) {
    		str = sdf.format(date);
    	}
    	return str;
    }
    
    /**
     * 将日期转化为quarz字符
     * <p>Title: getCron</p>  
     * <p>Description: </p>  
     * @param strDate 
     * @return  
     * @author lcb
     * @throws ParseException 
     */
    public static String getCronforStr(String  strDate) throws ParseException{
    	Date date = null;
    	if(strDate.length() < 12) {
    		date = getNowDay(strDate);
    	}else {
    		date = getDate(strDate);
    	}
    	SimpleDateFormat sdf = new SimpleDateFormat(cronftime);
    	String str = "";
    	if (date != null) {
    		str = sdf.format(date);
    	}
    	return str;
    }
    /**
     * 将日期转化为quarz字符 多次执行
     * <p>Title: getCron</p>  
     * <p>Description: </p>  
     * @param strDate 
     * @return  
     * @author lcb
     * @throws ParseException 
     */
    public static String multipleExecut(String  strDate,String pattern) throws ParseException{
    	Date date = null;
    	if(strDate.length() < 12) {
    		date = getNowDay(strDate);
    	}else {
    		date = getDate(strDate);
    	}
    	SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    	String str = "";
    	if (date != null) {
    		str = sdf.format(date);
    	}
    	return str;
    }
    public static void main(String[] args) {
    	
    }
    /**
     * 获取指定的日期
     * @param days
     * @return
     * @throws ParseException 
     */
    public static String specifiedDate(String nowday,String days) throws ParseException {
    	int daysInt = Integer.parseInt(days);
    	
        Calendar canlendar = Calendar.getInstance(); // java.util包
        
        Date date = null;
    	if(nowday.length() < 12) {
    		date = getNowDay(nowday);
    	}else {
    		date = getDate(nowday);
    	}
    	canlendar.setTime(date);
    	canlendar.add(Calendar.DATE, daysInt); // 日期减 如果不够减会将月变动
    	Date dt1=canlendar.getTime();

        SimpleDateFormat sdfd = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String dateStr = sdfd.format(dt1);
        
        return dateStr;
    }
    /**
     * 给时间加上几个小时
     * @param day 当前时间 格式：yyyy-MM-dd HH:mm:ss
     * @param hour 需要加的时间
     * @return
     */
    public static String addDateMinut(String day, int hour){   
    	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    	Date date = null;   
    	try {   
    		date = format.parse(day);   
    	} catch (Exception ex) {   
    		ex.printStackTrace();   
    	}   
    	if (date == null)   
    		return "";   
    	Calendar cal = Calendar.getInstance();   
    	cal.setTime(date);   
    	cal.add(Calendar.HOUR, hour);// 24小时制   
    	date = cal.getTime();   
    	cal = null;   
    	return format.format(date);   
    	
    }
}
