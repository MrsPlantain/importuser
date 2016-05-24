import com.util.DBConnectionUtil;
import com.util.DBUtil;
import com.util.MD5;
import com.util.PWUtil;
import org.junit.Test;

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * Created by chao on 2016/4/14.
 */
public class DBTest {

    @SuppressWarnings("Since15")
    @Test
    public void test() throws SQLException {
        System.out.println("开始时间" + System.currentTimeMillis());
        String sql = "select * from VWINF_HP_PATIENT";
        try {
            Map map = DBUtil.queryFirst(sql);
            System.out.println(map);

            Map<String, Object> user = new HashMap<String, Object>();
            user.put("ID", UUID.randomUUID().toString().replaceAll("-", ""));
            user.put("PHONE", map.get("PHONE_NUMBER"));
            user.put("PLAIN_PASSWORD", PWUtil.createPassWord(String.valueOf(map.get("PHONE_NUMBER"))));
            user.put("PASSWORD", MD5.instance().getMD5Code(String.valueOf(user.get("PLAIN_PASSWORD"))));
            user.put("STATE", "01");
//            user.put("APP_HXEXT_ID", PWUtil.generHxId());
//            user.put("IS_CREATE_HX",
//                    PWUtil.createHxUser(String.valueOf(user.get("APP_HXEXT_ID")),
//                            String.valueOf(user.get("PASSWORD"))) ? "1" : "2");
            DBUtil.saveUser(user,DBConnectionUtil.connection());
        } catch (SQLException e) {
            e.printStackTrace();
            DBConnectionUtil.rollback();
        }finally {
            DBConnectionUtil.close();
        }
        System.out.println("结束时间" + System.currentTimeMillis());
    }
}
