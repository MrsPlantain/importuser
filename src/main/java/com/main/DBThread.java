package com.main;

import com.util.DBConnectionUtil;
import com.util.DBUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

/**
 * Created by chao on 2016/4/15.
 */
public class DBThread extends Thread {

    private List<Map<String, Object>> list;
    private Connection connection;

    public DBThread(List<Map<String, Object>> _list,Connection _connection) {
        list = _list;
        connection = _connection;
    }

    @Override
    public void run() {
        try {

            for (Map<String, Object> map : list) {
                try {
                    if (DBUtil.saveHXEXTR(map,connection)){
                        DBUtil.saveUser(map, connection);
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                    try {
                        connection.rollback();
                    } catch (SQLException e1) {
                        e1.printStackTrace();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            DBConnectionUtil.close(connection);
            list.clear();
            System.out.println(System.currentTimeMillis());
        }
    }
}
