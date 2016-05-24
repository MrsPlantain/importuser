package com.util;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.UUID;

/**
 * Created by chao on 2016/4/14.
 */
public class PWUtil {
    public static String createPassWord(String phoneNO) {
        String md5 = MD5.instance().getMD5Code(phoneNO + String.valueOf(System.currentTimeMillis()));
        int rondom = Double.valueOf(Math.random()*25).intValue();
        return "a" + md5.substring(rondom, rondom + 6);
    }
    /**
     *  生成环信ID
     * @return
     */
    public static String generHxId(){
        return String.valueOf(UUID.randomUUID().toString().replace("-", "").substring(0, 10)+System.currentTimeMillis()).toLowerCase();
    }

    public static boolean createHxUser(String hxId,String password){
        boolean result = false;
        try {
			/*环信注册用户start*/
            ObjectNode datanode = JsonNodeFactory.instance.objectNode();
            datanode.put("username",hxId);
            datanode.put("password", password);
            ObjectNode createNewIMUserSingleNode = EasemobIMUsers.createNewIMUserSingle(datanode);
            if (null != createNewIMUserSingleNode) {
                JsonNode statusCode = createNewIMUserSingleNode.get("statusCode");
                if("200".equals(statusCode.toString())){
                    result = true;
                }
            }
			/*环信注册用户end*/
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

}
