package com.moemao.tgks.mar.execute;

import java.util.UUID;

import org.json.JSONObject;

import com.moemao.tgks.mar.net.HttpRequest;
import com.moemao.tgks.mar.tool.MarConstant;

public class KrsmaRequest
{
    private KrsmaRequest() {}
    
    private static KrsmaRequest instance;

    private HttpRequest httpRequest = new HttpRequest();
    
    private boolean bDebug = false;
    
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
    public String regist() throws Exception
    {
        String uuid = UUID.randomUUID().toString();
        String paramStr = "{\"uuid\":\"" + uuid + "\",\"clver\":\"1\",\"os\":0,\"carrier\":3,\"market\":1,\"lang\":0,\"device\":\"iPhone5S\",\"token\":\"\"}";
        String result = httpRequest.sendPost(MarConstant.URL_REGIST, paramStr);
        System.out.println(MarConstant.LOG_SYSTEM_INFO + "regist " + uuid);
        if (bDebug)
        {
            System.out.println(result);
        }
        return result;
    }
    
    /**
     * 
     * @Title: login
     * @Description: 登录
     * @param uuid 账号的uuid
     * @return
     * @return String 返回类型
     * @throws
     */
    public String login(String uuid) throws Exception
    {
        String paramStr = "{\"uuid\":\"" + uuid + "\",\"clver\":\"1\",\"os\":0,\"carrier\":3,\"market\":1,\"lang\":0,\"device\":\"iPhone5S\",\"token\":\"\"}";
        String result = httpRequest.sendPost(MarConstant.URL_LOGIN, paramStr);
        System.out.println(MarConstant.LOG_SYSTEM_INFO + "login " + uuid);
        if (bDebug)
        {
            System.out.println(result);
        }
        return result;
    }
    
    /**
     * 
     * @Title: loginAndroid
     * @Description: 登录
     * @param uuid 账号的uuid
     * @return
     * @return String 返回类型
     * @throws
     */
    public String loginAndroid(String uuid) throws Exception
    {
        String paramStr = "{\"uuid\":\"" + uuid + "\",\"clver\":\"2\",\"os\":1,\"carrier\":1,\"market\":2,\"lang\":0,\"device\":\"samsung GT-N7100\",\"token\":\"\"}";
        String result = httpRequest.sendPost(MarConstant.URL_LOGIN, paramStr);
        System.out.println(MarConstant.LOG_SYSTEM_INFO + "loginAndroid " + uuid);
        if (bDebug)
        {
            System.out.println(result);
        }
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
    public String[] connect(String sid) throws Exception
    {
        String paramStr = sid + "=";
        String result = httpRequest.sendPost(MarConstant.URL_CONNECT, paramStr);
        System.out.println(MarConstant.LOG_SYSTEM_INFO + "connect " + sid);
        if (bDebug)
        {
            System.out.println(result);
        }
        return result.split(MarConstant.KRSMA_SPLIT);
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
    public String[] userCreate(String sid, String name, String chara) throws Exception
    {
        String paramStr = sid + "={\"name\":\"" + name + "\",\"arthur_type\":" + chara + "}";
        String result = httpRequest.sendPost(MarConstant.URL_USERCREATE, paramStr);
        System.out.println(MarConstant.LOG_SYSTEM_INFO + "userCreate " + sid);
        if (bDebug)
        {
            System.out.println(result);
        }
        return result.split(MarConstant.KRSMA_SPLIT);
    }
    
    /**
     * @throws Exception 
     * 
     * @Title: homeShow
     * @Description: 回主界面的请求
     * @param sid
     * @return
     * @return String 返回类型
     * @throws
     */
    public String[] homeShow(String sid) throws Exception
    {
        String paramStr = sid + "=";
        String result = httpRequest.sendPost(MarConstant.URL_HOMESHOW, paramStr);
        System.out.println(MarConstant.LOG_SYSTEM_INFO + "homeShow " + sid);
        if (bDebug)
        {
            System.out.println(result);
        }
        return result.split(MarConstant.KRSMA_SPLIT);
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
    
    /**
     * 
     * @Title: cardShow
     * @Description: 卡牌信息
     * @param sid
     * @return
     * @return String 返回类型
     * @throws
     */
    public String[] cardShow(String sid) throws Exception
    {
        String paramStr = sid + "=";
        String result = httpRequest.sendPost(MarConstant.URL_CARDSHOW, paramStr);
        System.out.println(MarConstant.LOG_SYSTEM_INFO + "cardShow " + sid);
        if (bDebug)
        {
            System.out.println(result);
        }
        return result.split(MarConstant.KRSMA_SPLIT);
    }
    
    /**
     * 
     * @Title: cardFusion
     * @Description: 卡片合成
     * @param sid
     * @param baseId
     * @param addId
     * @return
     * @return String 返回类型
     * @throws
     */
    public String[] cardFusion(String sid, String baseId, String addId) throws Exception
    {
        String paramStr = sid + "={\"base_uniqid\":" + baseId + ",\"add_uniqids\":[" + addId + "]}";
        String result = httpRequest.sendPost(MarConstant.URL_CARDFUSION, paramStr);
        System.out.println(MarConstant.LOG_SYSTEM_INFO + "cardFusion " + sid);
        if (bDebug)
        {
            System.out.println(result);
        }
        return result.split(MarConstant.KRSMA_SPLIT);
    }
    
    /**
     * 
     * @Title: cardSell
     * @Description: 卡片出售
     * @param sid
     * @param uniqiIds
     * @return
     * @throws Exception
     * @return String[] 返回类型
     * @throws
     */
    public String[] cardSell(String sid, String uniqiIds) throws Exception
    {
        String paramStr = sid + "={\"uniqids\":["+uniqiIds+"]}";
        String result = httpRequest.sendPost(MarConstant.URL_CARDSELL, paramStr);
        System.out.println(MarConstant.LOG_SYSTEM_INFO + "cardSell " + sid);
        if (bDebug)
        {
            System.out.println(result);
        }
        return result.split(MarConstant.KRSMA_SPLIT);
    }
    
    /**
     * 
     * @Title: presentBoxMultiRecv
     * @Description: 收取礼物箱
     * @param sid
     * @return
     * @return String 返回类型
     * @throws
     */
    public String[] presentBoxMultiRecv(String sid) throws Exception
    {
        String paramStr = sid + "=";
        String result = httpRequest.sendPost(MarConstant.URL_PRESENTBOXMULTIRECV, paramStr);
        System.out.println(MarConstant.LOG_SYSTEM_INFO + "presentBoxMultiRecv " + sid);
        if (bDebug)
        {
            System.out.println(result);
        }
        return result.split(MarConstant.KRSMA_SPLIT);
    }
    
    /**
     * 
     * @Title: gachaShow
     * @Description: 抽卡准备
     * @param sid
     * @return
     * @throws Exception
     * @return String[] 返回类型
     * @throws
     */
    public String[] gachaShow(String sid) throws Exception
    {
        String paramStr = sid + "=";
        String result = httpRequest.sendPost(MarConstant.URL_GACHASHOW, paramStr);
        System.out.println(MarConstant.LOG_SYSTEM_INFO + "gachaShow " + sid);
        if (bDebug)
        {
            System.out.println(result);
        }
        return result.split(MarConstant.KRSMA_SPLIT);
    }
    
    /**
     * 
     * @Title: gachaPlay
     * @Description: 抽卡
     * @param sid
     * @param gachaId 抽卡类型ID
     * @param payType
     * @return
     * @return String 返回类型
     * @throws
     */
    public String[] gachaPlay(String sid, String gachaId, String payType) throws Exception
    {
        String paramStr = sid + "={\"gachaid\":" + gachaId + ",\"pay_type\":" + payType + "}";
        String result = httpRequest.sendPost(MarConstant.URL_GACHAPLAY, paramStr);
        System.out.println(MarConstant.LOG_SYSTEM_INFO + "gachaPlay " + sid + " gachaId : " + gachaId);
        if (bDebug)
        {
            System.out.println(result);
        }
        return result.split(MarConstant.KRSMA_SPLIT);
    }
    
    /**
     * 
     * @Title: teamBattleSoloShow
     * @Description: 单人战斗初始化信息 含地图 队友等
     * @param sid
     * @return
     * @return String 返回类型
     * @throws
     */
    public String[] teamBattleSoloShow(String sid) throws Exception
    {
        String paramStr = sid + "=";
        String result = httpRequest.sendPost(MarConstant.URL_TEAMBATTLESOLOSHOW, paramStr);
        System.out.println(MarConstant.LOG_SYSTEM_INFO + "teamBattleSoloShow " + sid);
        if (bDebug)
        {
            System.out.println(result);
        }
        return result.split(MarConstant.KRSMA_SPLIT);
    }
    
    /**
     * 
     * @Title: teamBattleSoloStart
     * @Description: 单人战斗开始
     * @param sid
     * @param bossId
     * @param userOne
     * @param userTwo
     * @param userFour
     * @return
     * @return String 返回类型
     * @throws
     */
    public String[] teamBattleSoloStart(String sid, String bossId, String userOne, String userTwo, String userFour) throws Exception
    {
        String paramStr = sid + "={\"bossid\":" + bossId + ",\"deck_arthur_type\":3,\"deck_arthur_type_idx\":0,\"partner_deck_selects\":[{\"userid\":" + userOne + ",\"arthur_type\":1,\"deck_idx\":0},{\"userid\":" + userTwo + ",\"arthur_type\":2,\"deck_idx\":0},{\"userid\":" + userFour + ",\"arthur_type\":4,\"deck_idx\":0}]}";
        String result = httpRequest.sendPost(MarConstant.URL_TEAMBATTLESOLOSTART, paramStr);
        System.out.println(MarConstant.LOG_SYSTEM_INFO + "teamBattleSoloStart " + sid);
        if (bDebug)
        {
            System.out.println(result);
        }
        return result.split(MarConstant.KRSMA_SPLIT);
    }
    
    /**
     * 
     * @Title: teamBattleSoloEnd
     * @Description: 单人战斗结束
     * @param sid
     * @return
     * @return String 返回类型
     * @throws
     */
    public String[] teamBattleSoloEnd(String sid, String battleInfo) throws Exception
    {
        String paramStr = sid + "=" + battleInfo;
        String result = httpRequest.sendPost(MarConstant.URL_TEAMBATTLESOLOEND, paramStr);
        System.out.println(MarConstant.LOG_SYSTEM_INFO + "teamBattleSoloEnd " + sid);
        if (bDebug)
        {
            System.out.println(result);
        }
        return result.split(MarConstant.KRSMA_SPLIT);
    }
    
    public static void main(String[] args)
    {
        try
        {
            String uuid = "f3c79ef8-b68f-43a8-8015-9363f510d85c";
            String result = KrsmaRequest.getInstance().login(uuid);
            JSONObject json= new JSONObject(result);
            String sid = json.getString("sess_key").replace("=", "");
            result = KrsmaRequest.getInstance().connect(sid)[1];
            System.out.println(result);
            result = KrsmaRequest.getInstance().homeShow(sid)[1];
            System.out.println(result);
        }
        catch (Exception e)
        {
            
        }
    }
}
