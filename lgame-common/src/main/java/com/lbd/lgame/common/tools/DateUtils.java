package com.lbd.lgame.common.tools;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;
import com.lbd.lgame.common.utils.Utils;


public class DateUtils {

    public static List<String> getDate(String begindate,String enddate) {
    	List<String> list = new ArrayList<String>();
    	DateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
    	
    	if(Utils.assertNotNull(begindate) && Utils.assertNotNull(enddate)){
    		Calendar start = Calendar.getInstance();
            int[] date = parseTime(begindate);
            start.set(date[0], date[1], date[2]);
            start.add(Calendar.MONTH, -1);
            Long startTIme = start.getTimeInMillis();
            
            Calendar end = Calendar.getInstance(); 
            date = parseTime(enddate);
            end.set(date[0], date[1], date[2]);
            end.add(Calendar.MONTH, -1);
            Long endTime = end.getTimeInMillis();
            
            Long oneDay = 1000 * 60 * 60 * 24l;
            Long time = startTIme;
            while (time <= endTime) {
                Date d = new Date(time);
                String str = df.format(d) + "【" + getWeekOfDate(d) + "】";
                list.add(str);
                System.out.println(str);
                time += oneDay;
            }
    	}
    	
        return list;
    }
    
    /**
     * 字符串后面不带周几的集合格式
     * @param begindate
     * @param enddate
     * @return
     */
    public static List<String> getDates(String begindate,String enddate) {
    	List<String> list = new ArrayList<String>();
    	DateFormat df = new SimpleDateFormat("yyyy年MM月dd日");
    	
    	if(Utils.assertNotNull(begindate) && Utils.assertNotNull(enddate)){
    		Calendar start = Calendar.getInstance();
    		int[] date = parseTime(begindate);
    		start.set(date[0], date[1], date[2]);
    		start.add(Calendar.MONTH, -1);
    		Long startTIme = start.getTimeInMillis();
    		
    		Calendar end = Calendar.getInstance(); 
    		date = parseTime(enddate);
    		end.set(date[0], date[1], date[2]);
    		end.add(Calendar.MONTH, -1);
    		Long endTime = end.getTimeInMillis();
    		
    		Long oneDay = 1000 * 60 * 60 * 24l;
    		Long time = startTIme;
    		while (time <= endTime) {
    			Date d = new Date(time);
    			String str = df.format(d);
    			list.add(str);
    			System.out.println(str);
    			time += oneDay;
    		}
    	}
    	
    	return list;
    }
    
    //format : YYYY年MM月DD日    
    private static int[] parseTime(final String timeString){        
    	final int [] ret = new int[3];              
    	String year[] = timeString.split("年");
    	if(year.length == 2 && year[0].length() == 4){
    		ret[0] = Integer.parseInt(year[0]);
    		String month[] = year[1].split("月");
    		if(month.length == 2 && month[0].length() == 2){
    			ret[1] = Integer.parseInt(month[0]);
    			String day[] = month[1].split("日");
    			if(day.length == 1 && day[0].length() == 2){
    				ret[2] = Integer.parseInt(day[0]);
    			}
    		}
    	}       
    	return ret;    
    }
    
    
    public static String getWeekOfDate(Date dt) {
        String[] weekDays = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
        Calendar cal = Calendar.getInstance();
        cal.setTime(dt);

        int w = cal.get(Calendar.DAY_OF_WEEK) - 1;
        if (w < 0)
            w = 0;

        return weekDays[w];
    }
    
    /**解析日期字符格式
     * 
     * @param date
     * @param pattern
     * @return
     */
    public static String formatDate(Date date,String pattern){
    	SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    	return sdf.format(date);
    }
    
    /**
   	 * 获取14位系统当前时间
   	 * 
   	 */
   	public static String getSysTime14(){
   		SimpleDateFormat myFormat = new SimpleDateFormat("yyyyMMddHHmmss");
   		Calendar calendar = Calendar.getInstance();
   		return myFormat.format(calendar.getTime());
   	}
    
	/**
	 * 获取系统当天时间到当天23:59:59秒值
	 * @return
	 */
	public static long getDayEndTime() {
		Calendar curDate = Calendar.getInstance();  
	    Calendar tommorowDate = new GregorianCalendar(curDate  
	            .get(Calendar.YEAR), curDate.get(Calendar.MONTH), curDate  
	            .get(Calendar.DATE) + 1, 0, 0, 0);  
	    return (tommorowDate.getTimeInMillis() - curDate .getTimeInMillis()) / 1000; 
	}
   	
   	
    public static void main(String[] args) {
    	String begindate = "2015年06月22日";
    	String enddate = "2015年06月25日";
    	getDates(begindate,enddate);
    }      
}
