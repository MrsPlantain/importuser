package com.main;

import com.util.Constants;
import com.util.DBConnectionUtil;
import com.util.DBUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.concurrent.CountDownLatch;

/**
 * Created by chao on 2016/4/15.
 */
public class ImportUserMain {

    private final static String QUERY_SQL = "SELECT DISTINCT A.PHONE_NUMBER FROM VWINF_HP_PATIENT A WHERE NOT EXISTS (SELECT B.PHONE FROM APP_PS_PERSON_INFO B WHERE B.PHONE = A.PHONE_NUMBER OR A.ID = B.MPI) AND LENGTH(A.PHONE_NUMBER) = 11";

    public static void main(String[] args) throws SQLException, InterruptedException {
        long startTime = System.currentTimeMillis();
        System.out.println("导入用户数据开始" + startTime);
        Connection connection = DBConnectionUtil.connection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(QUERY_SQL);
        try {
            int k = 0;
            List<Map<String, Object>> list = new ArrayList<Map<String, Object>>(Constants.DATANUMBER);
            while (resultSet.next()) {
                if (k != 0 && k%Constants.DATANUMBER == 0){
                    new DBThread(list, DBConnectionUtil.conn()).start();
                    list = new ArrayList<Map<String, Object>>(Constants.DATANUMBER);
                    System.out.println("Already insert " + Constants.DATANUMBER + " data.");
                }
                String phone = resultSet.getString(1);
                list.add(DBUtil.createUser(phone));

                k++;
            }
            new DBThread(list, DBConnectionUtil.conn()).start();
            System.out.println("共导入" + k + "条数据");
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally {
            resultSet.close();
            statement.close();
            connection.close();
        }
        long endTime = System.currentTimeMillis();
        System.out.println("导入用户数据结束" + endTime);
        System.out.println("共用时" + (endTime - startTime)/(1000*60) + "分钟");
    }

}
