package com.xmm.yc_work;

import com.xmm.commen.ConnTools;
import com.xmm.commen.ConnTools_1;
import org.apache.commons.dbutils.DbUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.MapListHandler;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * Created by 肖明明 on 2017/3/23.
 */
public class TradeTransfer {

    /**
     * 生成新订单表数据
     */
    public static void getNewTradeSQL(){
    /*
    * INSERT INTO `yc_trade` 
    * (`trade_no`, `pay_type`, `transaction_no`, `transaction_account`, `pay_status`, 
    * `pay_date`, `source`, `trade_total_price`, `product_total_price`, `freight_price`, 
    * `discount_amount`, `should_price`, `real_price`, `province`, `city`, `county`, 
    * `contact_name`, `contact_mobile`, `contact_address`, `express_name`, `express_id`, 
    * `express_no`, `express_update_date`, `user_id`, `activity_id`, `status`, 
    * `user_remark`, `service_remark`, `create_date`, `end_date`, `last_update_user_id`, 
    * `last_update_date`) 
    * VALUES 
    * ('170317173400000173', NULL, NULL, NULL, 'U', NULL, 'WeChat', 0.06, 0.06, 0.00, 
    * 0.00, 0.06, 0.00, '130000', '130300', '130304', '几日', '15810614428', 
    * '河北省,秦皇岛市,北戴河区$%$啊考虑考虑', NULL, NULL, NULL, NULL, -1, NULL, 'S', '', 
    * NULL, '2017-3-20 00:00:00', NULL, NULL, NULL);
    * 
    * value项说明：
    * ('订单号', NULL, NULL, NULL,'P','2017-3-20 00:00:00','WeChat',订单总价,商品总价, 0.00, 
    * 0.00,应付金额, 0.00, '省', '市', '区', '收件人', '收件人电话', 
    * '收件地址', NULL, 地址ID, NULL, NULL, 用户ID, NULL, 'E', '', 
    * '洗数据', '2017-3-20 00:00:00', '2017-3-20 00:00:00', 老orderId, '2017-3-20 00:00:00'),
    */
    
     //步骤：
     //1、获取格式化的信息。老用户ID，新用户ID，订单总价，新系统地址信息，
        String sql="select a.order_id,a.user_id old_id,a.goods_amount,a.pay_id,g.* from ecs_order_info a " +
                "LEFT JOIN ecs_wechat_user e on a.user_id=e.uid " +
                "LEFT JOIN lk_user_oauth f on e.openid=f.oauth_openid " +
                "LEFT JOIN yc_user_address g on (f.user_id=g.user_id and a.consignee=g.contact_name) " +
                "order by a.user_id";
        ConnTools ct= new ConnTools_1();
        Connection conn=ct.getConnection();
        QueryRunner qRunner = new QueryRunner();
        try {
            //执行SQL查询，并获取结果 
            List<Map<String, Object>> l=  qRunner.query(conn, sql,new MapListHandler());
            for(Map m:l){
                //2、拼接
                int payType=((Integer)m.get("pay_id")).intValue()==1?0:2;
                String value="('"+getTradeNo((Integer)m.get("user_id"))+"', "+payType+", NULL, NULL,'P','2017-3-20 00:00:00','WeChat',"+m.get("goods_amount")+","+m.get("goods_amount")+", 0.00, " +
                        "0.00,"+m.get("goods_amount")+", 0.00, '"+m.get("province")+"', '"+m.get("city")+"', '"+m.get("county")+"', '"+m.get("contact_name")+"', '"+m.get("contact_mobile")+"', " +
                        "'"+m.get("address")+"$%$"+m.get("contact_address")+"', NULL, "+m.get("id")+", NULL, NULL, "+m.get("user_id")+", NULL, 'E', '', " +
                        "'洗数据', '2017-3-20 00:00:00', '2017-3-20 00:00:00', "+m.get("order_id")+", '2017-3-20 00:00:00'),";
                System.out.println(value);
//                String aa="update yc_trade set pay_type="+payType+" where last_update_user_id="+m.get("order_id")+";";
//                System.out.println(aa);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //关闭数据库连接 
            DbUtils.closeQuietly(conn);
        }
    }
    
    
    public static void getNewTradeDetailsSQL(){
        /*
        INSERT INTO `yc_trade_details` (`trade_no`, `product_id`, `product_name`, `product_url`, `quality`, `unit_price`, `activity_id`, `activity_type`, `product_total_price`, `user_id`, `status`, `create_date`) 
        VALUES 
        ( '170317173400000173', 38, '午茶夫人 天然蜜桃乌龙茶 20g', 'http://img.traveler99.com/image/2017/3/15/1489564091613.jpg', 6, 0.01, NULL, 0, 0.06, -1, 'S', '2017-3-17 17:34:55');
        
        值说明：
        ('订单号', 新商品ID, '商品名称', 'http://img.traveler99.com/image/2017/3/15/1489564091613.jpg', 数量, 单价, NULL, 0, 总价, 用户id, 'S', '2017-3-20 00:00:00');
         */
        String sql="select a.user_id,a.trade_no,b.goods_number,c.* from yc_trade a LEFT JOIN ecs_order_goods b on a.last_update_user_id=b.order_id " +
                "LEFT JOIN yc_product c on b.goods_id=c.subtitle " +
                " where c.id is not null";
        ConnTools ct= new ConnTools_1();
        Connection conn=ct.getConnection();
        QueryRunner qRunner = new QueryRunner();
        try {
            //执行SQL查询，并获取结果 
            List<Map<String, Object>> l=  qRunner.query(conn, sql,new MapListHandler());
            for(Map m:l){
                        //2、拼接
                BigDecimal total=new BigDecimal("0");
                total=((BigDecimal)m.get("unit_price")).multiply(new BigDecimal((Integer)m.get("goods_number")));
                String value="('"+m.get("trade_no")+"', "+m.get("id")+", '"+m.get("name")+"', " +
                        "'"+m.get("banner_url")+"', " +
                        " "+m.get("goods_number")+", "+m.get("unit_price")+", NULL, 0, "+total+", " +
                        ""+m.get("user_id")+", 'E', " +
                        "'2017-3-20 00:00:00'),";
                System.out.println(value);


            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            //关闭数据库连接 
            DbUtils.closeQuietly(conn);
        }
        
        
    }
    
    public static void main(String[] args) {
//        TradeTransfer.getNewTradeSQL();
        TradeTransfer.getNewTradeDetailsSQL();
        
    }

    /**
     * 生成订单号，新系统规则
     * @param id
     * @return
     */
    public static String getTradeNo(int id){
        String orderNo = "";
        String date = new SimpleDateFormat("yyMMddHHmm").format(new Date());
        orderNo = date+""+String.format("%06d", Math.abs(id))+""+(new Random().nextInt(90)+10);
        return orderNo;
    }
    
    
}
