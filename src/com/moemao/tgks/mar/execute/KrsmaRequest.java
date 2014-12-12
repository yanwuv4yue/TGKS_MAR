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
    public String regist()
    {
        String uuid = UUID.randomUUID().toString();
        String paramStr = "{\"uuid\":\"" + uuid + "\",\"clver\":\"1\",\"os\":0,\"carrier\":3,\"market\":1,\"lang\":0,\"device\":\"iPhone5S\",\"token\":\"\"}";
        String result = httpRequest.sendPost(MarConstant.URL_REGIST, paramStr);
        System.out.println("[ System Info ] regist " + uuid);
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
    public String login(String uuid)
    {
        String paramStr = "{\"uuid\":\"" + uuid + "\",\"clver\":\"1\",\"os\":0,\"carrier\":3,\"market\":1,\"lang\":0,\"device\":\"iPhone5S\",\"token\":\"\"}";
        String result = httpRequest.sendPost(MarConstant.URL_LOGIN, paramStr);
        System.out.println("[ System Info ] login " + uuid);
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
    public String connect(String sid)
    {
        String paramStr = sid + "=";
        String result = httpRequest.sendPost(MarConstant.URL_CONNECT, paramStr);
        System.out.println("[ System Info ] connect " + sid);
        if (bDebug)
        {
            System.out.println(result);
        }
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
        String paramStr = sid + "={\"name\":\"" + name + "\",\"arthur_type\":" + chara + "}";
        String result = httpRequest.sendPost(MarConstant.URL_USERCREATE, paramStr);
        System.out.println("[ System Info ] userCreate " + sid);
        if (bDebug)
        {
            System.out.println(result);
        }
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
        System.out.println("[ System Info ] homeShow " + sid);
        if (bDebug)
        {
            System.out.println(result);
        }
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
        String paramStr = sid + "={\"inviteid\":\"" + inviteCode + "\"}";
        String result = httpRequest.sendPost(MarConstant.URL_INVITECODEENTER, paramStr);
        System.out.println("[ System Info ] inviteCodeEnter " + sid + " inviteCode : " + inviteCode);
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
    public String cardShow(String sid)
    {
        String paramStr = sid + "=";
        String result = httpRequest.sendPost(MarConstant.URL_CARDSHOW, paramStr);
        System.out.println("[ System Info ] cardShow " + sid);
        if (bDebug)
        {
            System.out.println(result);
        }
        return result;
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
    public String cardFusion(String sid, String baseId, String addId)
    {
        String paramStr = sid + "={\"base_uniqid\":" + baseId + ",\"add_uniqids\":[" + addId + "]}";
        String result = httpRequest.sendPost(MarConstant.URL_CARDFUSION, paramStr);
        System.out.println("[ System Info ] cardFusion " + sid);
        if (bDebug)
        {
            System.out.println(result);
        }
        return result;
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
    public String presentBoxMultiRecv(String sid)
    {
        String paramStr = sid + "=";
        String result = httpRequest.sendPost(MarConstant.URL_PRESENTBOXMULTIRECV, paramStr);
        System.out.println("[ System Info ] presentBoxMultiRecv " + sid);
        if (bDebug)
        {
            System.out.println(result);
        }
        return result;
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
    public String gachaPlay(String sid, String gachaId, String payType)
    {
        String paramStr = sid + "={\"gachaid\":" + gachaId + ",\"pay_type\":" + payType + "}";
        String result = httpRequest.sendPost(MarConstant.URL_GACHAPLAY, paramStr);
        System.out.println("[ System Info ] gachaPlay " + sid + " gachaId : " + gachaId);
        if (bDebug)
        {
            System.out.println(result);
        }
        return result;
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
    public String teamBattleSoloShow(String sid)
    {
        String paramStr = sid + "=";
        String result = httpRequest.sendPost(MarConstant.URL_TEAMBATTLESOLOSHOW, paramStr);
        System.out.println("[ System Info ] teamBattleSoloShow " + sid);
        if (bDebug)
        {
            System.out.println(result);
        }
        return result;
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
    public String teamBattleSoloStart(String sid, String bossId, String userOne, String userTwo, String userFour)
    {
        String paramStr = sid + "={\"bossid\":" + bossId + ",\"deck_arthur_type\":3,\"deck_arthur_type_idx\":0,\"partner_deck_selects\":[{\"userid\":" + userOne + ",\"arthur_type\":1,\"deck_idx\":0},{\"userid\":" + userTwo + ",\"arthur_type\":2,\"deck_idx\":0},{\"userid\":" + userFour + ",\"arthur_type\":4,\"deck_idx\":0}]}";
        String result = httpRequest.sendPost(MarConstant.URL_TEAMBATTLESOLOSTART, paramStr);
        System.out.println("[ System Info ] teamBattleSoloStart " + sid);
        if (bDebug)
        {
            System.out.println(result);
        }
        return result;
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
    public String teamBattleSoloEnd(String sid)
    {
        String paramStr = sid + "={\"progress\":3,\"is_clear\":1,\"input_cmd\":[\"6473,12,1,-1449869062\n6473,13,1,1598444520,-890705031,-1631118539,-1849612821,1842312998,-274709162,256483103,-708051355,-1156505390,-1491876984\n6473,12,2,-1774364196\n6473,13,2,1540732331,-1210319740,-1631118539,-1849612821,1842312998,-274709162,256483103,-708051355,-1156505390,-1491876984\n6473,12,3,-1248297927\n6473,13,3,-1948409170,457245596,-1631118539,-1849612821,1842312998,-274709162,256483103,-708051355,-1156505390,-1491876984\n6473,12,4,1823145572\n6473,13,4,73661817,-890705031,-1631118539,457245596,1842312998,-274709162,256483103,-708051355,-1156505390,-1491876984\n6473,14,5,-1609999787,1407176610\n6473,14,6,-1136481224,1602949194\n6473,14,7,-1136481224,1602949194\n6473,14,8,-1136481224,1602949194\n6473,15,-117239005\n6473,10\n6473,0\n6473,3,4\n6473,3,3\n6473,3,2\n6473,3,1\n6473,6\n\",\"6473,12,1,-1449869062\n6473,13,1,1598444520,-890705031,-1631118539,-1849612821,1842312998,-274709162,256483103,-708051355,-1156505390,-1491876984\n6473,12,2,-1774364196\n6473,13,2,1540732331,-1210319740,-1631118539,-1849612821,1842312998,-274709162,256483103,-708051355,-1156505390,-1491876984\n6473,12,3,-1248297927\n6473,13,3,-1948409170,457245596,-1631118539,-1849612821,1842312998,-274709162,256483103,-708051355,-1156505390,-1491876984\n6473,12,4,1823145572\n6473,13,4,73661817,-890705031,-1631118539,457245596,1842312998,-274709162,256483103,-708051355,-1156505390,-1491876984\n6473,14,5,1922126263,-54477811\n6473,14,6,-1609999787,1407176610\n6473,14,7,-1136481224,1602949194\n6473,14,8,-1136481224,1602949194\n6473,15,-117239005\n6473,10\n6473,0\n6473,3,4\n6473,3,3\n6473,3,2\n6473,3,1\n6473,6\n\",\"6473,12,1,-1449869062\n6473,13,1,1598444520,-890705031,-1631118539,-1849612821,1842312998,-274709162,256483103,-708051355,-1156505390,-1491876984\n6473,12,2,-1774364196\n6473,13,2,1540732331,-1210319740,-1631118539,-1849612821,1842312998,-274709162,256483103,-708051355,-1156505390,-1491876984\n6473,12,3,-1248297927\n6473,13,3,-1948409170,457245596,-1631118539,-1849612821,1842312998,-274709162,256483103,-708051355,-1156505390,-1491876984\n6473,12,4,1823145572\n6473,13,4,73661817,-890705031,-1631118539,457245596,1842312998,-274709162,256483103,-708051355,-1156505390,-1491876984\n6473,14,5,-1609999787,1407176610\n6473,14,6,1922126263,-54477811\n6473,14,7,-1609999787,1407176610\n6473,14,8,-1136481224,1602949194\n6473,15,-117239005\n6473,10\n6473,0\n6473,3,4\n6473,3,3\n6473,3,2\n6473,3,1\n6473,6\n6473,1\n5967,10\n5967,0\n5967,3,4\n5967,3,3\n5967,3,2\n5967,3,1\n5967,6\n\"],\"enemy_dead_bit\":[1,3,7]}";
        String result = httpRequest.sendPost(MarConstant.URL_TEAMBATTLESOLOEND, paramStr);
        System.out.println("[ System Info ] teamBattleSoloEnd " + sid);
        if (bDebug)
        {
            System.out.println(result);
        }
        return result;
    }
    
    public static void main(String[] args)
    {
        try
        {
            String uuid = "f3c79ef8-b68f-43a8-8015-9363f510d85c";
            String result = KrsmaRequest.getInstance().login(uuid);
            JSONObject json= new JSONObject(result);
            String sid = json.getString("sess_key").replace("=", "");
            result = KrsmaRequest.getInstance().connect(sid);
            System.out.println(result);
            result = KrsmaRequest.getInstance().homeShow(sid);
            System.out.println(result);
        }
        catch (Exception e)
        {
            
        }
    }
}
