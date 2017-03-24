package com.xmm.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by 肖明明 on 2017/3/23.
 */
public class DateUtil {
    
    
    
    public static String getYYYYMMddhhmmssString(Date d){
        SimpleDateFormat sdf=new SimpleDateFormat("YYYY-MM-dd hh:mm:ss");
        return sdf.format(d);
    }
    
}
