package com.moemao.tgks.mar.execute;

import java.util.UUID;

import com.moemao.tgks.mar.net.HttpRequest;
import com.moemao.tgks.mar.tool.MarConstant;

public class KrsmaRequest
{
    private KrsmaRequest() {}
    
    private static KrsmaRequest instance;

    private HttpRequest httpRequest = new HttpRequest();
    
    public static synchronized KrsmaRequest getInstance()
    {
        if (null == instance)
        {
            instance = new KrsmaRequest();
        }
        return instance;
    }
    
    /**
     * 
     * @Title: regist
     * @Description: 注册以及登录方法，使用uuid，与账号文件的stringId应该是ACE+Base64的加密方式
     * @return
     * @return String 返回类型
     * @throws
     */
    public String regist()
    {
        String paramStr = "{\"uuid\":\"" + UUID.randomUUID().toString() + "\",\"clver\":\"1\",\"os\":0,\"carrier\":3,\"market\":1,\"lang\":0,\"device\":\"iPhone5S\",\"token\":\"\"}";
        String result = httpRequest.sendPost(MarConstant.URL_REGIST, paramStr);
        System.out.println("regist");
        System.out.println(result);
        
        return result;
    }
    
    /**
     * 
     * @Title: connect
     * @Description: 进首页之前的connect方法 校验连接
     * @param sid
     * @return
     * @return String 返回类型
     * @throws
     */
    public String connect(String sid)
    {
        String paramStr = sid + "=";
        String result = httpRequest.sendPost(MarConstant.URL_CONNECT, paramStr);
        System.out.println("connect");
        System.out.println(result);
        
        return result;
    }
    
    /**
     * 
     * @Title: userCreate
     * @Description: 选职业以及写名字
     * @param sid
     * @param name
     * @param chara
     * @return
     * @return String 返回类型
     * @throws
     */
    public String userCreate(String sid, String name, String chara)
    {
        String paramStr = sid + "=" + "{\"name\":\"" + name + "\",\"arthur_type\":" + chara + "}";
        String result = httpRequest.sendPost(MarConstant.URL_USERCREATE, paramStr);
        System.out.println("userCreate");
        System.out.println(result);
        
        return result;
    }
    
    /**
     * 
     * @Title: homeShow
     * @Description: 回主界面的请求
     * @param sid
     * @return
     * @return String 返回类型
     * @throws
     */
    public String homeShow(String sid)
    {
        String paramStr = sid + "=";
        String result = httpRequest.sendPost(MarConstant.URL_HOMESHOW, paramStr);
        System.out.println("connect");
        System.out.println(result);
        
        return result;
    }
    
    /**
     * 
     * @Title: inviteCodeEnter
     * @Description: 填招待
     * @param sid
     * @param inviteCode
     * @return
     * @return String 返回类型
     * @throws
     */
    public String inviteCodeEnter(String sid, String inviteCode)
    {
        String paramStr = sid + "=" + "{\"inviteid\":\"" + inviteCode + "\"}";
        String result = httpRequest.sendPost(MarConstant.URL_INVITECODEENTER, paramStr);
        System.out.println("inviteCodeEnter");
        System.out.println(result);
        
        return result;
    }
    
    public HttpRequest getHttpRequest()
    {
        return httpRequest;
    }

    public void setHttpRequest(HttpRequest httpRequest)
    {
        this.httpRequest = httpRequest;
    }
    
    public static void main(String[] args)
    {
        KrsmaRequest.getInstance().connect("1CJ/jcFexUs6tQIfdHbBMBnK48XbQ");
    }
}
