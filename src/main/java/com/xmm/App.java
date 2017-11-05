package com.xmm;

import com.xmm.commen.ConnTools;
import com.xmm.commen.ConnTools_1;
import com.xmm.commen.ConnTools_2;
import com.xmm.work.StockInfoHander;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanListHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Hello world!
 *
 */
public class App {
    public static void main( String[] args ){

        ConnTools ct= new ConnTools_2();
        Connection conn=ct.getConnection();
        QueryRunner qRunner = new QueryRunner();
        //执行SQL查询，并获取结果 
        try {
            List<Map<String, Object>> l=  qRunner.query(conn, "select * from t_stock_info where exchange_company='SZ' ",new MapListHandler());
        
            //输出查询结果 
            for(Map m:l){
                System.out.println( StockInfoHander.getHandelSQL(m));
            }
            
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //关闭数据库连接 
            DbUtils.closeQuietly(conn);
        }
        



    }
}
