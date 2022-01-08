package com.weaver.rpa.tender.search;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.weaver.rpa.tender.search.util.HttpClient;
import com.weaver.rpa.tender.search.util.RSAEncrypt;

import java.security.interfaces.RSAPublicKey;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WebBaseTest {

    //本地
    protected static final String URL_PREFIX = "http://localhost:8080/rpa/tender/";
    //测试环境
    //protected static final String URL_PREFIX = "http://123.60.75.158:20015/essential/";


    /**
     *
     * @param url
     * @param paramMap List<Long>、List<String>、
     *                 Map<String,String>、Map<String,Object>、Map<String,? extends Object>、
     *                 String、Integer、
     *                 ......
     * @return
     */
    protected String postUrl(String url, Object paramMap){
        String paramJsonStr = new Gson().toJson(paramMap);
        String result = HttpClient.doPost(url, paramJsonStr);
        return result;
    }

    protected String postUrlWithToken(String url, Object paramMap){
        String token = "";
        try{
            String timestamp = String.valueOf(System.currentTimeMillis());//待加密数据
            //公钥
            RSAPublicKey publicKey = RSAEncrypt.loadPublicKeyByStr(RSAEncrypt.LOGIN_PUBLICKEY);
            //公钥加密
            token = RSAEncrypt.encryptBySegment(publicKey, timestamp.getBytes());//公钥加密后数据(base64字符串)
        }catch (Throwable t){
            t.printStackTrace();
        }


        //head请求参数
        Map<String, String> headers = new HashMap<>();
        headers.put("accessEssentialToken",token);
        //post请求
        String paramJsonStr = new Gson().toJson(paramMap);
        String result = HttpClient.doPostWithAuthorizationAndHead(url, paramJsonStr, "",headers);
        return result;
    }

    /**
     * 格式化打印json: 去除feature属性
     * @param jsonStr
     */
    protected void printNoFeaturePretty(String jsonStr){
        Map<String, Object> map = new Gson().fromJson(jsonStr, new TypeToken<Map<String, Object>>(){}.getType());
        if(map==null || map.isEmpty()){
            System.out.println("没有获取到数据结果");
            return;
        }
        Map<String, Object> data = (Map<String, Object>)map.get("data");
        if(data!=null && !data.isEmpty()){
            List<Object> list = (List<Object>)data.get("data");
            for(Object entity : list){
                Map<String, Object> findUserMap = (Map<String, Object>)entity;
                findUserMap.remove("feature");
                findUserMap.remove("embeddingVectorFeature");
            }
        }


        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String str = gson.toJson(map);
        System.out.println(str);
    }

    /**
     * 格式化打印json
     * @param jsonStr
     */
    protected void printPretty(String jsonStr){
        Map<String, Object> map = new Gson().fromJson(jsonStr, new TypeToken<Map<String, Object>>(){}.getType());
        Gson gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
        String str = gson.toJson(map);
        System.out.println(str);
    }

    /**
     * 打印结果
     * @param result
     */
    protected void printResult(String result){
        System.out.println(result);
    }
}
