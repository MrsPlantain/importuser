package com.util;

import java.sql.*;
import java.sql.Date;
import java.util.*;

/**
 * Created by chao on 2016/4/14.
 */
public class DBUtil {

    public static List<Map<String, Object>> queryList(String sql) throws SQLException {
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        Connection connection = DBConnectionUtil.connection();
        try {

            ResultSet resultSet = connection.prepareStatement(sql).executeQuery();
            while (resultSet.next()) {
                Map<String, Object> map = new HashMap<String, Object>();
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    map.put(resultSet.getMetaData().getColumnLabel(i), resultSet.getObject(i));
                }
                list.add(map);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return list;
    }

    public static Map<String, Object> queryFirst(String sql) throws SQLException {
        Map<String, Object> map = new HashMap<String, Object>();

        Connection connection = DBConnectionUtil.connection();
        try {
            ResultSet resultSet = connection.createStatement().executeQuery(sql);
            if (resultSet.next()) {
                for (int i = 1; i <= resultSet.getMetaData().getColumnCount(); i++) {
                    map.put(resultSet.getMetaData().getColumnLabel(i), resultSet.getObject(i));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    /**
     * 保存用户信息
     *
     * @param map
     * @return true if it saved success false if it failed.
     * @throws SQLException
     */
    public static boolean saveUser(Map<String, Object> map, Connection connection) throws SQLException {

        int count = 0;

        String insert_sql = "insert into APP_PS_PERSON_INFO(" +
                "ID,PASSWORD,PHONE,STATE,CREATE_TIME,IMPORT_SOURCE,PLAIN_PASSWORD,APP_HXEXT_ID,IS_CREATE_HX,AUTH_IS" +
                ") values (?,?,?,?,?,'HIS',?,?,?,?)";
        PreparedStatement preparedStatement = connection.prepareStatement(insert_sql);
        preparedStatement.setString(1, String.valueOf(map.get("ID")));
        preparedStatement.setString(2, String.valueOf(map.get("PASSWORD")));
        preparedStatement.setString(3, String.valueOf(map.get("PHONE")));
        preparedStatement.setString(4, String.valueOf(map.get("STATE")));
        preparedStatement.setDate(5, new Date(System.currentTimeMillis()));
        preparedStatement.setString(6, String.valueOf(map.get("PLAIN_PASSWORD")));
        preparedStatement.setString(7, String.valueOf(map.get("APP_HXEXT_ID")));
        preparedStatement.setString(8, String.valueOf(map.get("IS_CREATE_HX")));
        preparedStatement.setString(9, "0");
        try {
            count = preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
        }finally {
            preparedStatement.close();
        }
        return count > 0;
    }

    public static boolean saveHXEXTR(Map<String,Object> map,Connection connection) throws SQLException {

        int count = 0;
        String insert_sql = "INSERT INTO APP_PATIENT_HXEXT(ID,HX_ID,MOBILEPHONE_NUMBER,HX_PASSWORD) VALUES(?,?,?,?)";

        PreparedStatement preparedStatement = connection.prepareStatement(insert_sql);
        preparedStatement.setString(1, String.valueOf(map.get("HXEXT_ID")));
        preparedStatement.setString(2, String.valueOf(map.get("HXEXT_HX_ID")));
        preparedStatement.setString(3, String.valueOf(map.get("HXEXT_MOBILEPHONE_NUMBER")));
        preparedStatement.setString(4, String.valueOf(map.get("HXEXT_HX_PASSWORD")));

        try {
            count = preparedStatement.executeUpdate();
            connection.commit();
        } catch (SQLException e) {
            e.printStackTrace();
            connection.rollback();
        }finally {
            preparedStatement.close();
        }

        return count > 0;
    }

    public static Map<String, Object> createUser(String phoneNumber) {
        Map<String, Object> user = new HashMap<String, Object>();

        String plainPassword = PWUtil.createPassWord(phoneNumber);
        String password = MD5.instance().getMD5Code(plainPassword);

        String ID = UUID.randomUUID().toString().replaceAll("-", "");
        String HXEXT_ID = UUID.randomUUID().toString().replaceAll("-", "");

        user.put("ID", ID);
        user.put("PHONE", phoneNumber);
        user.put("PLAIN_PASSWORD", plainPassword);
        user.put("PASSWORD", password);
        user.put("STATE", "01");
        user.put("APP_HXEXT_ID", HXEXT_ID);
        user.put("IS_CREATE_HX",
                PWUtil.createHxUser(String.valueOf(user.get("APP_HXEXT_ID")),
                        String.valueOf(user.get("PASSWORD"))) ? "1" : "2");

        user.put("HXEXT_ID", HXEXT_ID);
        user.put("HXEXT_HX_ID", PWUtil.generHxId());
        user.put("HXEXT_MOBILEPHONE_NUMBER", phoneNumber);
        user.put("HXEXT_HX_PASSWORD", password);

        return user;
    }

}
