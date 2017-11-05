package com.xmm.work;

import java.util.Map;

public class StockInfoHander {
    
    public static String getHandelSQL(Map<String, Object> resultset){
        //1,处理short_name ,去除两端和中间空格，
        String shortName=(String)resultset.get("short_name");
        shortName=shortName.replace(" ","");
        
        //2,处理industry_name，截取首字母，去除两段和中间空格，
        String industryName=(String)resultset.get("industry_name");
        String letter=industryName.substring(0,1);
        industryName=industryName.substring(1).replace(" ","");

        //3,截取的字母赋予industry_name_letter字段目标： 
        
        //4,组装sql返回
        String sql="UPDATE t_stock_info SET `short_name`='"+shortName+"', `industry_name`='"+industryName+"', `industry_name_letter`='"+letter+"' WHERE `code`='"+resultset.get("code")+"';";
        
        return sql;
    }
    
    
}
