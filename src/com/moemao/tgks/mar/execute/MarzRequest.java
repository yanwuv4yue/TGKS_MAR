package com.moemao.tgks.mar.execute;

import java.util.HashMap;
import java.util.Map;

import net.sf.json.JSONObject;

import com.moemao.tgks.mar.marz.tool.MarzConstant;
import com.moemao.tgks.mar.net.HttpRequest;
import com.moemao.tgks.mar.tool.MarConstant;

public class MarzRequest
{
    private MarzRequest() {}
    
    private static MarzRequest instance;
    
    private HttpRequest httpRequest = new HttpRequest();
    
    private boolean bDebug = false;
    
    public static synchronized MarzRequest getInstance()
    {
        if (null == instance)
        {
            instance = new MarzRequest();
        }
        return instance;
    }
    
    private JSONObject sidJSONObject(String sid)
    {
        JSONObject json = new JSONObject();
        json.put("sid", sid);
        return json;
    }
    
    public Map<String, JSONObject> loginIOS(String uuid) throws Exception
    {
        Map<String, JSONObject> map = new HashMap<String, JSONObject>();
        
        String paramStr = "{\"uuid\":\"" + uuid + "\",\"clver\":\"1\",\"os\":0,\"carrier\":3,\"market\":1,\"lang\":0,\"device\":\"iPhone5S\",\"token\":\"\"}";
        String result = httpRequest.sendPost(MarConstant.URL_LOGIN, paramStr);
        System.out.println(MarzConstant.LOG_SYSTEM_INFO + "loginIOS " + uuid);
        if (bDebug)
        {
            System.out.println(result);
        }

        JSONObject resCode= JSONObject.fromObject(result);
        
        map.put(MarzConstant.JSON_TAG_SID, this.sidJSONObject(resCode.getString("sess_key").replace("=", "")));
        map.put(MarzConstant.JSON_TAG_RESCODE, resCode);
        
        return map;
    }
    
    public Map<String, JSONObject> loginAndroid(String uuid) throws Exception
    {
        Map<String, JSONObject> map = new HashMap<String, JSONObject>();
        
        String paramStr = "{\"uuid\":\"" + uuid + "\",\"clver\":\"2\",\"os\":1,\"carrier\":1,\"market\":2,\"lang\":0,\"device\":\"samsung GT-N7100\",\"token\":\"\"}";
        String result = httpRequest.sendPost(MarConstant.URL_LOGIN, paramStr);
        System.out.println(MarzConstant.LOG_SYSTEM_INFO + "loginAndroid " + uuid);
        if (bDebug)
        {
            System.out.println(result);
        }

        JSONObject resCode= JSONObject.fromObject(result);
        
        map.put(MarzConstant.JSON_TAG_SID, this.sidJSONObject(resCode.getString("sess_key").replace("=", "")));
        map.put(MarzConstant.JSON_TAG_RESCODE, resCode);
        
        return map;
    }
    
    public Map<String, JSONObject> connect(String sid) throws Exception
    {
        Map<String, JSONObject> map = new HashMap<String, JSONObject>();
        
        String paramStr = sid + "=";
        String[] result = httpRequest.sendPost(MarConstant.URL_CONNECT, paramStr).split(MarConstant.KRSMA_SPLIT);
        System.out.println(MarzConstant.LOG_SYSTEM_INFO + "connect " + sid);
        if (bDebug)
        {
            System.out.println(result);
        }
        JSONObject resCode= JSONObject.fromObject(result[1].substring(0, result[1].indexOf("}{") + 1));
        
        map.put(MarzConstant.JSON_TAG_SID, this.sidJSONObject(result[0]));
        map.put(MarzConstant.JSON_TAG_RESCODE, resCode);
        
        return map;
    }
    
    public static void main(String[] args)
    {
        String str = "{xxx}{xxxxxxx}";
        System.out.println(str.substring(0, str.indexOf("}{") + 1));
        System.out.println(str.substring(str.indexOf("}{") + 1, str.length()));
    }
}
