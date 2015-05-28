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
    
    private Map<String, JSONObject> map;
    
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
    
    public Map<String, JSONObject> loginIOS(String uuid, String hashToken) throws Exception
    {
        map = new HashMap<String, JSONObject>();
        
        //String paramStr = "{\"uuid\":\"" + uuid + "\",\"clver\":\"1\",\"os\":0,\"carrier\":3,\"market\":1,\"lang\":0,\"device\":\"iPhone5S\",\"token\":\"\"}";
        //String paramStr = "{\"uuid\":\"" + uuid + "\",\"hash_token\":\"" + hashToken + "\",\"clver\":\"3\",\"os\":0,\"carrier\":3,\"market\":1,\"lang\":0,\"device\":\"iPhone5S\",\"token\":\"\"}";
        //String paramStr = "{\"uuid\":\"" + uuid + "\",\"hash_token\":\"" + hashToken + "\",\"clver\":\"4\",\"os\":0,\"carrier\":3,\"market\":1,\"lang\":0,\"device\":\"iPhone5S\",\"token\":\"\",\"os_ver\":\"iPhone OS 7.1.2\"}";
        String paramStr = "{\"uuid\":\"" + uuid + "\",\"hash_token\":\"" + hashToken + "\",\"clver\":\"7\",\"os\":0,\"carrier\":3,\"market\":1,\"lang\":0,\"device\":\"iPhone5S\",\"token\":\"\",\"os_ver\":\"iPhone OS 7.1.2\"}";
        
        if (bDebug)
        {
            System.out.println(paramStr);
        }
        
        String result = httpRequest.sendPost(MarConstant.URL_LOGIN, paramStr);
        System.out.println(MarzConstant.LOG_SYSTEM_INFO + "loginIOS " + uuid + " " + Thread.currentThread().getName());

        JSONObject resCode= JSONObject.fromObject(result);
        
        map.put(MarzConstant.JSON_TAG_SID, this.sidJSONObject(resCode.getString("sess_key").replace("=", "")));
        map.put(MarzConstant.JSON_TAG_RESCODE, resCode);
        
        result = null;
        resCode = null;
        return map;
    }
    
    public Map<String, JSONObject> loginAndroid(String uuid, String hashToken) throws Exception
    {
        map = new HashMap<String, JSONObject>();
        
        //String paramStr = "{\"uuid\":\"" + uuid + "\",\"clver\":\"2\",\"os\":1,\"carrier\":1,\"market\":2,\"lang\":0,\"device\":\"samsung GT-N7100\",\"token\":\"\"}";
        //String paramStr = "{\"uuid\":\"" + uuid + "\",\"hash_token\":\"" + hashToken + "\",\"clver\":\"3\",\"os\":1,\"carrier\":1,\"market\":2,\"lang\":0,\"device\":\"LGE Nexus 5\",\"token\":\"\"}";
        //String paramStr = "{\"uuid\":\"" + uuid + "\",\"hash_token\":\"" + hashToken + "\",\"clver\":\"4\",\"os\":1,\"carrier\":1,\"market\":2,\"lang\":0,\"device\":\"LGE Nexus 5\",\"token\":\"\",\"os_ver\":\"Android OS 4.4.3 / API-19 (FUCK YOU)\"}";
        String paramStr = "{\"uuid\":\"" + uuid + "\",\"hash_token\":\"" + hashToken + "\",\"clver\":\"7\",\"os\":1,\"carrier\":1,\"market\":2,\"lang\":0,\"device\":\"LGE Nexus 5\",\"token\":\"\",\"os_ver\":\"Android OS 4.4.3 / API-19 (FUCK YOU)\"}";
        
        if (bDebug)
        {
            System.out.println(paramStr);
        }
        
        String result = httpRequest.sendPost(MarConstant.URL_LOGIN, paramStr);
        System.out.println(MarzConstant.LOG_SYSTEM_INFO + "loginAndroid " + uuid + " " + Thread.currentThread().getName());

        JSONObject resCode= JSONObject.fromObject(result);
        
        map.put(MarzConstant.JSON_TAG_SID, this.sidJSONObject(resCode.getString("sess_key").replace("=", "")));
        map.put(MarzConstant.JSON_TAG_RESCODE, resCode);
        
        result = null;
        resCode = null;
        return map;
    }
    
    public Map<String, JSONObject> inviteCodeEnter(String sid, String inviteCode) throws Exception
    {
        map = new HashMap<String, JSONObject>();
        
        String paramStr = sid + "={\"inviteid\":\"" + inviteCode + "\"}";
        String[] result = httpRequest.sendPost(MarConstant.URL_INVITECODEENTER, paramStr).split(MarConstant.KRSMA_SPLIT);
        System.out.println(MarConstant.LOG_SYSTEM_INFO + "inviteCodeEnter " + sid + " invite : " + inviteCode);
        
        JSONObject resCode= JSONObject.fromObject(result[1].substring(0, result[1].indexOf("}{") + 1));
        
        map.put(MarzConstant.JSON_TAG_SID, this.sidJSONObject(result[0]));
        map.put(MarzConstant.JSON_TAG_RESCODE, resCode);
        
        result = null;
        resCode = null;
        return map;
    }
    
    public Map<String, JSONObject> connect(String sid) throws Exception
    {
        map = new HashMap<String, JSONObject>();
        
        String paramStr = sid + "=";
        String[] result = httpRequest.sendPost(MarConstant.URL_CONNECT, paramStr).split(MarConstant.KRSMA_SPLIT);
        System.out.println(MarzConstant.LOG_SYSTEM_INFO + "connect " + Thread.currentThread().getName());
        
        JSONObject resCode= JSONObject.fromObject(result[1].substring(0, result[1].indexOf("}{") + 1));
        
        map.put(MarzConstant.JSON_TAG_SID, this.sidJSONObject(result[0]));
        map.put(MarzConstant.JSON_TAG_RESCODE, resCode);

        result = null;
        resCode = null;
        return map;
    }
    
    public Map<String, JSONObject> homeShow(String sid) throws Exception
    {
        map = new HashMap<String, JSONObject>();
        
        String paramStr = sid + "=";
        String[] result = httpRequest.sendPost(MarConstant.URL_HOMESHOW, paramStr).split(MarConstant.KRSMA_SPLIT);
        System.out.println(MarzConstant.LOG_SYSTEM_INFO + "homeShow " + Thread.currentThread().getName());

        JSONObject resCode= JSONObject.fromObject(result[1].substring(0, result[1].indexOf("}{") + 1));
        JSONObject homeShow = JSONObject.fromObject(result[1].substring(result[1].indexOf("}{") + 1, result[1].length()));
        
        map.put(MarzConstant.JSON_TAG_SID, this.sidJSONObject(result[0]));
        map.put(MarzConstant.JSON_TAG_RESCODE, resCode);
        map.put(MarzConstant.JSON_TAG_HOMWSHOW, homeShow);

        result = null;
        resCode = null;
        return map;
    }
    
    public Map<String, JSONObject> exploreStart(String sid, String arthurType, String deckIdx) throws Exception
    {
        map = new HashMap<String, JSONObject>();
        
        String paramStr = sid + "={\"arthur_type\":" + arthurType + ",\"deck_idx\":" + deckIdx + "}";

        if (bDebug)
        {
            System.out.println(paramStr);
        }
        
        String[] result = httpRequest.sendPost(MarConstant.URL_EXPLORESTART, paramStr).split(MarConstant.KRSMA_SPLIT);
        System.out.println(MarzConstant.LOG_SYSTEM_INFO + "exploreStart " + Thread.currentThread().getName());

        JSONObject resCode= JSONObject.fromObject(result[1].substring(0, result[1].indexOf("}{") + 1));
        JSONObject exploreStart = JSONObject.fromObject(result[1].substring(result[1].indexOf("}{") + 1, result[1].length()));
        
        map.put(MarzConstant.JSON_TAG_SID, this.sidJSONObject(result[0]));
        map.put(MarzConstant.JSON_TAG_RESCODE, resCode);
        map.put(MarzConstant.JSON_TAG_EXPLORESTART, exploreStart);

        result = null;
        resCode = null;
        return map;
    }
    
    public Map<String, JSONObject> exploreEnd(String sid) throws Exception
    {
        map = new HashMap<String, JSONObject>();
        
        String paramStr = sid + "=";
        String[] result = httpRequest.sendPost(MarConstant.URL_EXPLOREEND, paramStr).split(MarConstant.KRSMA_SPLIT);
        System.out.println(MarzConstant.LOG_SYSTEM_INFO + "exploreEnd " + Thread.currentThread().getName());
        
        // 有问候语信息 必须设置过滤
        result = this.requestFilter(result);
        
        JSONObject resCode= JSONObject.fromObject(result[1].substring(0, result[1].indexOf("}{") + 1));
        JSONObject exploreEnd = JSONObject.fromObject(result[1].substring(result[1].indexOf("}{") + 1, result[1].length()));
        
        map.put(MarzConstant.JSON_TAG_SID, this.sidJSONObject(result[0]));
        map.put(MarzConstant.JSON_TAG_RESCODE, resCode);
        map.put(MarzConstant.JSON_TAG_EXPLOREEND, exploreEnd);

        result = null;
        resCode = null;
        return map;
    }
    
    public Map<String, JSONObject> cardShow(String sid) throws Exception
    {
        map = new HashMap<String, JSONObject>();
        
        String paramStr = sid + "=";
        String[] result = httpRequest.sendPost(MarConstant.URL_CARDSHOW, paramStr).split(MarConstant.KRSMA_SPLIT);
        System.out.println(MarzConstant.LOG_SYSTEM_INFO + "cardShow " + Thread.currentThread().getName());
        
        JSONObject resCode= JSONObject.fromObject(result[1].substring(0, result[1].indexOf("}{") + 1));
        JSONObject cardShow = JSONObject.fromObject(result[1].substring(result[1].indexOf("}{") + 1, result[1].length()));
        
        map.put(MarzConstant.JSON_TAG_SID, this.sidJSONObject(result[0]));
        map.put(MarzConstant.JSON_TAG_RESCODE, resCode);
        map.put(MarzConstant.JSON_TAG_CARDSHOW, cardShow);

        result = null;
        resCode = null;
        return map;
    }
    
    public Map<String, JSONObject> cardShow2(String sid) throws Exception
    {
        map = new HashMap<String, JSONObject>();
        
        String paramStr = sid + "=";
        String[] result = httpRequest.sendPost(MarConstant.URL_CARDSHOW2, paramStr).split(MarConstant.KRSMA_SPLIT);
        System.out.println(MarzConstant.LOG_SYSTEM_INFO + "cardShow2 " + Thread.currentThread().getName());
        
        JSONObject resCode= JSONObject.fromObject(result[1].substring(0, result[1].indexOf("}{") + 1));
        JSONObject cardShow = JSONObject.fromObject(result[1].substring(result[1].indexOf("}{") + 1, result[1].length()));
        
        map.put(MarzConstant.JSON_TAG_SID, this.sidJSONObject(result[0]));
        map.put(MarzConstant.JSON_TAG_RESCODE, resCode);
        map.put(MarzConstant.JSON_TAG_CARDSHOW, cardShow);

        result = null;
        resCode = null;
        return map;
    }
    
    public Map<String, JSONObject> cardFusion(String sid, String baseId, String addId) throws Exception
    {
        map = new HashMap<String, JSONObject>();
        
        String paramStr = sid + "={\"base_uniqid\":" + baseId + ",\"add_uniqids\":[" + addId + "]}";
        
        if (bDebug)
        {
            System.out.println(paramStr);
        }
        
        String[] result = httpRequest.sendPost(MarConstant.URL_CARDFUSION, paramStr).split(MarConstant.KRSMA_SPLIT);
        System.out.println(MarzConstant.LOG_SYSTEM_INFO + "cardFusion " + Thread.currentThread().getName());
        
        JSONObject resCode= JSONObject.fromObject(result[1].substring(0, result[1].indexOf("}{") + 1));
        JSONObject cardFusion = JSONObject.fromObject(result[1].substring(result[1].indexOf("}{") + 1, result[1].length()));
        
        map.put(MarzConstant.JSON_TAG_SID, this.sidJSONObject(result[0]));
        map.put(MarzConstant.JSON_TAG_RESCODE, resCode);
        map.put(MarzConstant.JSON_TAG_CARDFUSION, cardFusion);

        result = null;
        resCode = null;
        return map;
    }
    
    public Map<String, JSONObject> cardSell(String sid, String uniqiIds) throws Exception
    {
        map = new HashMap<String, JSONObject>();
        
        String paramStr = sid + "={\"uniqids\":["+uniqiIds+"]}";

        if (bDebug)
        {
            System.out.println(paramStr);
        }
        
        String[] result = httpRequest.sendPost(MarConstant.URL_CARDSELL, paramStr).split(MarConstant.KRSMA_SPLIT);
        System.out.println(MarzConstant.LOG_SYSTEM_INFO + "cardSell " + Thread.currentThread().getName());
        
        JSONObject resCode= JSONObject.fromObject(result[1].substring(0, result[1].indexOf("}{") + 1));
        JSONObject cardSell = JSONObject.fromObject(result[1].substring(result[1].indexOf("}{") + 1, result[1].length()));
        
        map.put(MarzConstant.JSON_TAG_SID, this.sidJSONObject(result[0]));
        map.put(MarzConstant.JSON_TAG_RESCODE, resCode);
        map.put(MarzConstant.JSON_TAG_CARDSELL, cardSell);

        result = null;
        resCode = null;
        return map;
    }
    
    public Map<String, JSONObject> presentBoxMultiRecv(String sid) throws Exception
    {
        map = new HashMap<String, JSONObject>();
        
        String paramStr = sid + "=";
        String[] result = httpRequest.sendPost(MarConstant.URL_PRESENTBOXMULTIRECV, paramStr).split(MarConstant.KRSMA_SPLIT);
        System.out.println(MarzConstant.LOG_SYSTEM_INFO + "presentBoxMultiRecv " + Thread.currentThread().getName());
        
        JSONObject resCode= JSONObject.fromObject(result[1].substring(0, result[1].indexOf("}{") + 1));
        JSONObject presentBoxMultiRecv = JSONObject.fromObject(result[1].substring(result[1].indexOf("}{") + 1, result[1].length()));
        
        map.put(MarzConstant.JSON_TAG_SID, this.sidJSONObject(result[0]));
        map.put(MarzConstant.JSON_TAG_RESCODE, resCode);
        map.put(MarzConstant.JSON_TAG_PRESENTBOXMULTIRECV, presentBoxMultiRecv);

        result = null;
        resCode = null;
        return map;
    }
    
    public Map<String, JSONObject> teamBattleSoloShow(String sid) throws Exception
    {
        map = new HashMap<String, JSONObject>();
        
        String paramStr = sid + "=";
        String[] result = httpRequest.sendPost(MarConstant.URL_TEAMBATTLESOLOSHOW, paramStr).split(MarConstant.KRSMA_SPLIT);
        System.out.println(MarzConstant.LOG_SYSTEM_INFO + "teamBattleSoloShow " + Thread.currentThread().getName());
        
        // 有好友信息 必须设置过滤
        result = this.requestFilter(result);
        
        JSONObject resCode= JSONObject.fromObject(result[1].substring(0, result[1].indexOf("}{") + 1));
        JSONObject teamBattleSoloShow = JSONObject.fromObject(result[1].substring(result[1].indexOf("}{") + 1, result[1].length()));
        
        map.put(MarzConstant.JSON_TAG_SID, this.sidJSONObject(result[0]));
        map.put(MarzConstant.JSON_TAG_RESCODE, resCode);
        map.put(MarzConstant.JSON_TAG_TEAMBATTLESOLOSHOW, teamBattleSoloShow);

        result = null;
        resCode = null;
        return map;
    }
    
    public Map<String, JSONObject> teamBattleSoloStart(String sid, String bossId, String userOne, String userTwo, String userFour) throws Exception
    {
        map = new HashMap<String, JSONObject>();
        
        String paramStr = sid + "={\"bossid\":" + bossId + ",\"deck_arthur_type\":3,\"deck_arthur_type_idx\":0,\"partner_deck_selects\":[{\"userid\":" + userOne + ",\"arthur_type\":1,\"deck_idx\":0},{\"userid\":" + userTwo + ",\"arthur_type\":2,\"deck_idx\":0},{\"userid\":" + userFour + ",\"arthur_type\":4,\"deck_idx\":0}]}";

        if (bDebug)
        {
            System.out.println(paramStr);
        }
        
        String[] result = httpRequest.sendPost(MarConstant.URL_TEAMBATTLESOLOSTART, paramStr).split(MarConstant.KRSMA_SPLIT);
        System.out.println(MarzConstant.LOG_SYSTEM_INFO + "teamBattleSoloStart " + Thread.currentThread().getName());
        
        JSONObject resCode= JSONObject.fromObject(result[1].substring(0, result[1].indexOf("}{") + 1));
        JSONObject teamBattleSoloStart = JSONObject.fromObject(result[1].substring(result[1].indexOf("}{") + 1, result[1].length()));
        
        map.put(MarzConstant.JSON_TAG_SID, this.sidJSONObject(result[0]));
        map.put(MarzConstant.JSON_TAG_RESCODE, resCode);
        map.put(MarzConstant.JSON_TAG_TEAMBATTLESOLOSTART, teamBattleSoloStart);

        result = null;
        resCode = null;
        return map;
    }
    
    public Map<String, JSONObject> teamBattleSoloEnd(String sid, String battleInfo) throws Exception
    {
        map = new HashMap<String, JSONObject>();
        
        String paramStr = sid + "=" + battleInfo;

        if (bDebug)
        {
            System.out.println(paramStr);
        }
        
        String[] result = httpRequest.sendPost(MarConstant.URL_TEAMBATTLESOLOEND, paramStr).split(MarConstant.KRSMA_SPLIT);
        System.out.println(MarzConstant.LOG_SYSTEM_INFO + "teamBattleSoloEnd " + Thread.currentThread().getName());
        
        // 有问候语信息 必须设置过滤
        result = this.requestFilter(result);
        
        JSONObject resCode= JSONObject.fromObject(result[1].substring(0, result[1].indexOf("}{") + 1));
        JSONObject teamBattleSoloEnd = JSONObject.fromObject(result[1].substring(result[1].indexOf("}{") + 1, result[1].length()));
        
        map.put(MarzConstant.JSON_TAG_SID, this.sidJSONObject(result[0]));
        map.put(MarzConstant.JSON_TAG_RESCODE, resCode);
        map.put(MarzConstant.JSON_TAG_TEAMBATTLESOLOEND, teamBattleSoloEnd);

        result = null;
        resCode = null;
        return map;
    }
    
    private String[] requestFilter(String[] result)
    {
        if (result.length > 2)
        {
            for (int i = 2; i < result.length; i++)
            {
                result[1] += result[i];
            }
        }
        
        return result;
    }
    
    public static void main(String[] args)
    {
        /*
        String str = "{xxx}{xxxxxxx}";
        System.out.println(str.substring(0, str.indexOf("}{") + 1));
        System.out.println(str.substring(str.indexOf("}{") + 1, str.length()));
        String uuid = "f57e8b2c-acbf-43a7-9d05-a3f0fc439a46";
        String token = "492N+ZrLTxcvj3h/gWTjdX/+RJE=";
        */
    }
}
