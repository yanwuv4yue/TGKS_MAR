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
    
    public Map<String, JSONObject> loginIOS(String uuid, String hashToken) throws Exception
    {
        Map<String, JSONObject> map = new HashMap<String, JSONObject>();
        
        //String paramStr = "{\"uuid\":\"" + uuid + "\",\"clver\":\"1\",\"os\":0,\"carrier\":3,\"market\":1,\"lang\":0,\"device\":\"iPhone5S\",\"token\":\"\"}";
        String paramStr = "{\"uuid\":\"" + uuid + "\",\"hash_token\":\"" + hashToken + "\",\"clver\":\"3\",\"os\":0,\"carrier\":3,\"market\":1,\"lang\":0,\"device\":\"iPhone5S\",\"token\":\"\"}";

        if (bDebug)
        {
            System.out.println(paramStr);
        }
        
        String result = httpRequest.sendPost(MarConstant.URL_LOGIN, paramStr);
        System.out.println(MarzConstant.LOG_SYSTEM_INFO + "loginIOS " + uuid);

        JSONObject resCode= JSONObject.fromObject(result);
        
        map.put(MarzConstant.JSON_TAG_SID, this.sidJSONObject(resCode.getString("sess_key").replace("=", "")));
        map.put(MarzConstant.JSON_TAG_RESCODE, resCode);
        
        return map;
    }
    
    public Map<String, JSONObject> loginAndroid(String uuid, String hashToken) throws Exception
    {
        Map<String, JSONObject> map = new HashMap<String, JSONObject>();
        
        //String paramStr = "{\"uuid\":\"" + uuid + "\",\"clver\":\"2\",\"os\":1,\"carrier\":1,\"market\":2,\"lang\":0,\"device\":\"samsung GT-N7100\",\"token\":\"\"}";
        String paramStr = "{\"uuid\":\"" + uuid + "\",\"hash_token\":\"" + hashToken + "\",\"clver\":\"3\",\"os\":1,\"carrier\":1,\"market\":2,\"lang\":0,\"device\":\"LGE Nexus 5\",\"token\":\"\"}";

        if (bDebug)
        {
            System.out.println(paramStr);
        }
        
        String result = httpRequest.sendPost(MarConstant.URL_LOGIN, paramStr);
        System.out.println(MarzConstant.LOG_SYSTEM_INFO + "loginAndroid " + uuid);

        JSONObject resCode= JSONObject.fromObject(result);
        
        map.put(MarzConstant.JSON_TAG_SID, this.sidJSONObject(resCode.getString("sess_key").replace("=", "")));
        map.put(MarzConstant.JSON_TAG_RESCODE, resCode);
        
        return map;
    }
    
    public String inviteCodeEnter(String sid, String inviteCode) throws Exception
    {
        String paramStr = sid + "={\"inviteid\":\"" + inviteCode + "\"}";
        String result = httpRequest.sendPost(MarConstant.URL_INVITECODEENTER, paramStr);
        System.out.println(MarConstant.LOG_SYSTEM_INFO + "inviteCodeEnter " + sid + " invite : " + inviteCode);
        if (bDebug)
        {
            System.out.println(result);
        }
        return result;
    }
    
    public Map<String, JSONObject> connect(String sid) throws Exception
    {
        Map<String, JSONObject> map = new HashMap<String, JSONObject>();
        
        String paramStr = sid + "=";
        String[] result = httpRequest.sendPost(MarConstant.URL_CONNECT, paramStr).split(MarConstant.KRSMA_SPLIT);
        System.out.println(MarzConstant.LOG_SYSTEM_INFO + "connect " + sid);
        
        JSONObject resCode= JSONObject.fromObject(result[1].substring(0, result[1].indexOf("}{") + 1));
        
        map.put(MarzConstant.JSON_TAG_SID, this.sidJSONObject(result[0]));
        map.put(MarzConstant.JSON_TAG_RESCODE, resCode);
        
        return map;
    }
    
    public Map<String, JSONObject> homeShow(String sid) throws Exception
    {
        Map<String, JSONObject> map = new HashMap<String, JSONObject>();
        
        String paramStr = sid + "=";
        String[] result = httpRequest.sendPost(MarConstant.URL_HOMESHOW, paramStr).split(MarConstant.KRSMA_SPLIT);
        System.out.println(MarzConstant.LOG_SYSTEM_INFO + "homeShow " + sid);

        JSONObject resCode= JSONObject.fromObject(result[1].substring(0, result[1].indexOf("}{") + 1));
        JSONObject homeShow = JSONObject.fromObject(result[1].substring(result[1].indexOf("}{") + 1, result[1].length()));
        
        map.put(MarzConstant.JSON_TAG_SID, this.sidJSONObject(result[0]));
        map.put(MarzConstant.JSON_TAG_RESCODE, resCode);
        map.put(MarzConstant.JSON_TAG_HOMWSHOW, homeShow);
        
        return map;
    }
    
    public Map<String, JSONObject> exploreStart(String sid, String arthurType, String deckIdx) throws Exception
    {
        Map<String, JSONObject> map = new HashMap<String, JSONObject>();
        
        String paramStr = sid + "={\"arthur_type\":" + arthurType + ",\"deck_idx\":" + deckIdx + "}";

        if (bDebug)
        {
            System.out.println(paramStr);
        }
        
        String[] result = httpRequest.sendPost(MarConstant.URL_EXPLORESTART, paramStr).split(MarConstant.KRSMA_SPLIT);
        System.out.println(MarzConstant.LOG_SYSTEM_INFO + "exploreStart " + sid);

        JSONObject resCode= JSONObject.fromObject(result[1].substring(0, result[1].indexOf("}{") + 1));
        JSONObject exploreStart = JSONObject.fromObject(result[1].substring(result[1].indexOf("}{") + 1, result[1].length()));
        
        map.put(MarzConstant.JSON_TAG_SID, this.sidJSONObject(result[0]));
        map.put(MarzConstant.JSON_TAG_RESCODE, resCode);
        map.put(MarzConstant.JSON_TAG_EXPLORESTART, exploreStart);
        
        return map;
    }
    
    public Map<String, JSONObject> exploreEnd(String sid) throws Exception
    {
        Map<String, JSONObject> map = new HashMap<String, JSONObject>();
        
        String paramStr = sid + "=";
        String[] result = httpRequest.sendPost(MarConstant.URL_EXPLOREEND, paramStr).split(MarConstant.KRSMA_SPLIT);
        System.out.println(MarzConstant.LOG_SYSTEM_INFO + "exploreEnd " + sid);
        
        // 有问候语信息 必须设置过滤
        result = this.requestFilter(result);
        
        JSONObject resCode= JSONObject.fromObject(result[1].substring(0, result[1].indexOf("}{") + 1));
        JSONObject exploreEnd = JSONObject.fromObject(result[1].substring(result[1].indexOf("}{") + 1, result[1].length()));
        
        map.put(MarzConstant.JSON_TAG_SID, this.sidJSONObject(result[0]));
        map.put(MarzConstant.JSON_TAG_RESCODE, resCode);
        map.put(MarzConstant.JSON_TAG_EXPLOREEND, exploreEnd);
        
        return map;
    }
    
    public Map<String, JSONObject> cardShow(String sid) throws Exception
    {
        Map<String, JSONObject> map = new HashMap<String, JSONObject>();
        
        String paramStr = sid + "=";
        String[] result = httpRequest.sendPost(MarConstant.URL_CARDSHOW, paramStr).split(MarConstant.KRSMA_SPLIT);
        System.out.println(MarzConstant.LOG_SYSTEM_INFO + "cardShow " + sid);
        
        JSONObject resCode= JSONObject.fromObject(result[1].substring(0, result[1].indexOf("}{") + 1));
        JSONObject cardShow = JSONObject.fromObject(result[1].substring(result[1].indexOf("}{") + 1, result[1].length()));
        
        map.put(MarzConstant.JSON_TAG_SID, this.sidJSONObject(result[0]));
        map.put(MarzConstant.JSON_TAG_RESCODE, resCode);
        map.put(MarzConstant.JSON_TAG_CARDSHOW, cardShow);
        
        return map;
    }
    
    public Map<String, JSONObject> cardFusion(String sid, String baseId, String addId) throws Exception
    {
        Map<String, JSONObject> map = new HashMap<String, JSONObject>();
        
        String paramStr = sid + "={\"base_uniqid\":" + baseId + ",\"add_uniqids\":[" + addId + "]}";
        
        if (bDebug)
        {
            System.out.println(paramStr);
        }
        
        String[] result = httpRequest.sendPost(MarConstant.URL_CARDFUSION, paramStr).split(MarConstant.KRSMA_SPLIT);
        System.out.println(MarzConstant.LOG_SYSTEM_INFO + "cardFusion " + sid);
        
        JSONObject resCode= JSONObject.fromObject(result[1].substring(0, result[1].indexOf("}{") + 1));
        JSONObject cardFusion = JSONObject.fromObject(result[1].substring(result[1].indexOf("}{") + 1, result[1].length()));
        
        map.put(MarzConstant.JSON_TAG_SID, this.sidJSONObject(result[0]));
        map.put(MarzConstant.JSON_TAG_RESCODE, resCode);
        map.put(MarzConstant.JSON_TAG_CARDFUSION, cardFusion);
        
        return map;
    }
    
    public Map<String, JSONObject> cardSell(String sid, String uniqiIds) throws Exception
    {
        Map<String, JSONObject> map = new HashMap<String, JSONObject>();
        
        String paramStr = sid + "={\"uniqids\":["+uniqiIds+"]}";

        if (bDebug)
        {
            System.out.println(paramStr);
        }
        
        String[] result = httpRequest.sendPost(MarConstant.URL_CARDSELL, paramStr).split(MarConstant.KRSMA_SPLIT);
        System.out.println(MarzConstant.LOG_SYSTEM_INFO + "cardSell " + sid);
        
        JSONObject resCode= JSONObject.fromObject(result[1].substring(0, result[1].indexOf("}{") + 1));
        JSONObject cardSell = JSONObject.fromObject(result[1].substring(result[1].indexOf("}{") + 1, result[1].length()));
        
        map.put(MarzConstant.JSON_TAG_SID, this.sidJSONObject(result[0]));
        map.put(MarzConstant.JSON_TAG_RESCODE, resCode);
        map.put(MarzConstant.JSON_TAG_CARDSELL, cardSell);
        
        return map;
    }
    
    public Map<String, JSONObject> presentBoxMultiRecv(String sid) throws Exception
    {
        Map<String, JSONObject> map = new HashMap<String, JSONObject>();
        
        String paramStr = sid + "=";
        String[] result = httpRequest.sendPost(MarConstant.URL_PRESENTBOXMULTIRECV, paramStr).split(MarConstant.KRSMA_SPLIT);
        System.out.println(MarzConstant.LOG_SYSTEM_INFO + "presentBoxMultiRecv " + sid);
        
        JSONObject resCode= JSONObject.fromObject(result[1].substring(0, result[1].indexOf("}{") + 1));
        JSONObject presentBoxMultiRecv = JSONObject.fromObject(result[1].substring(result[1].indexOf("}{") + 1, result[1].length()));
        
        map.put(MarzConstant.JSON_TAG_SID, this.sidJSONObject(result[0]));
        map.put(MarzConstant.JSON_TAG_RESCODE, resCode);
        map.put(MarzConstant.JSON_TAG_PRESENTBOXMULTIRECV, presentBoxMultiRecv);
        
        return map;
    }
    
    public Map<String, JSONObject> teamBattleSoloShow(String sid) throws Exception
    {
        Map<String, JSONObject> map = new HashMap<String, JSONObject>();
        
        String paramStr = sid + "=";
        String[] result = httpRequest.sendPost(MarConstant.URL_TEAMBATTLESOLOSHOW, paramStr).split(MarConstant.KRSMA_SPLIT);
        System.out.println(MarzConstant.LOG_SYSTEM_INFO + "teamBattleSoloShow " + sid);
        
        // 有好友信息 必须设置过滤
        result = this.requestFilter(result);
        
        JSONObject resCode= JSONObject.fromObject(result[1].substring(0, result[1].indexOf("}{") + 1));
        JSONObject teamBattleSoloShow = JSONObject.fromObject(result[1].substring(result[1].indexOf("}{") + 1, result[1].length()));
        
        map.put(MarzConstant.JSON_TAG_SID, this.sidJSONObject(result[0]));
        map.put(MarzConstant.JSON_TAG_RESCODE, resCode);
        map.put(MarzConstant.JSON_TAG_TEAMBATTLESOLOSHOW, teamBattleSoloShow);
        
        return map;
    }
    
    public Map<String, JSONObject> teamBattleSoloStart(String sid, String bossId, String userOne, String userTwo, String userFour) throws Exception
    {
        Map<String, JSONObject> map = new HashMap<String, JSONObject>();
        
        String paramStr = sid + "={\"bossid\":" + bossId + ",\"deck_arthur_type\":3,\"deck_arthur_type_idx\":0,\"partner_deck_selects\":[{\"userid\":" + userOne + ",\"arthur_type\":1,\"deck_idx\":0},{\"userid\":" + userTwo + ",\"arthur_type\":2,\"deck_idx\":0},{\"userid\":" + userFour + ",\"arthur_type\":4,\"deck_idx\":0}]}";

        if (bDebug)
        {
            System.out.println(paramStr);
        }
        
        String[] result = httpRequest.sendPost(MarConstant.URL_TEAMBATTLESOLOSTART, paramStr).split(MarConstant.KRSMA_SPLIT);
        System.out.println(MarzConstant.LOG_SYSTEM_INFO + "teamBattleSoloStart " + sid);
        
        JSONObject resCode= JSONObject.fromObject(result[1].substring(0, result[1].indexOf("}{") + 1));
        JSONObject teamBattleSoloStart = JSONObject.fromObject(result[1].substring(result[1].indexOf("}{") + 1, result[1].length()));
        
        map.put(MarzConstant.JSON_TAG_SID, this.sidJSONObject(result[0]));
        map.put(MarzConstant.JSON_TAG_RESCODE, resCode);
        map.put(MarzConstant.JSON_TAG_TEAMBATTLESOLOSTART, teamBattleSoloStart);
        
        return map;
    }
    
    public Map<String, JSONObject> teamBattleSoloEnd(String sid, String battleInfo) throws Exception
    {
        Map<String, JSONObject> map = new HashMap<String, JSONObject>();
        
        String paramStr = sid + "=" + battleInfo;

        if (bDebug)
        {
            System.out.println(paramStr);
        }
        
        String[] result = httpRequest.sendPost(MarConstant.URL_TEAMBATTLESOLOEND, paramStr).split(MarConstant.KRSMA_SPLIT);
        System.out.println(MarzConstant.LOG_SYSTEM_INFO + "teamBattleSoloEnd " + sid);
        
        // 有问候语信息 必须设置过滤
        result = this.requestFilter(result);
        
        JSONObject resCode= JSONObject.fromObject(result[1].substring(0, result[1].indexOf("}{") + 1));
        JSONObject teamBattleSoloEnd = JSONObject.fromObject(result[1].substring(result[1].indexOf("}{") + 1, result[1].length()));
        
        map.put(MarzConstant.JSON_TAG_SID, this.sidJSONObject(result[0]));
        map.put(MarzConstant.JSON_TAG_RESCODE, resCode);
        map.put(MarzConstant.JSON_TAG_TEAMBATTLESOLOEND, teamBattleSoloEnd);
        
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
        */
        
    }
}
