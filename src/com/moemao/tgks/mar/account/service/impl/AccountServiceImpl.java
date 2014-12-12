package com.moemao.tgks.mar.account.service.impl;

import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.moemao.tgks.common.tool.CommonUtil;
import com.moemao.tgks.mar.account.dao.AccountDao;
import com.moemao.tgks.mar.account.entity.AccountEvt;
import com.moemao.tgks.mar.account.entity.AccountReq;
import com.moemao.tgks.mar.account.service.AccountService;
import com.moemao.tgks.mar.execute.KrsmaRequest;
import com.moemao.tgks.mar.invite.service.InviteService;
import com.moemao.tgks.mar.krsmacard.entity.KrsmaCardEvt;
import com.moemao.tgks.mar.krsmacard.entity.KrsmaCardReq;
import com.moemao.tgks.mar.krsmacard.service.KrsmaCardService;
import com.moemao.tgks.mar.tool.MarConstant;
import com.moemao.tgks.mar.tool.MarUtil;

public class AccountServiceImpl implements AccountService
{
    private InviteService mar_inviteService;
    
    private KrsmaCardService mar_krsmaCardService;
    
    /**
     * ﻿AccountDao
     */
    private AccountDao mar_accountDao;
    
    private KrsmaRequest request = KrsmaRequest.getInstance();
    
    public List<AccountEvt> queryAccount(AccountReq accountReq)
    {
    if (CommonUtil.isEmpty(accountReq.getSortSql()))
    {
    accountReq.setSortSql(" t.ID DESC");
    }
    return mar_accountDao.mar_queryAccount(accountReq);
    }
    
    public AccountEvt queryAccountById(String id)
    {
    AccountReq accountReq = new AccountReq();
    accountReq.setId(id);
    AccountEvt accountEvt = null;
    List<AccountEvt> accountList = mar_accountDao.mar_queryAccount(accountReq);
    if (!CommonUtil.isEmpty(accountList))
    {
    accountEvt = accountList.get(0);
    }
    return accountEvt;
    }
    
    public List<AccountEvt> queryAccountByIds(List<String> ids)
    {
        return mar_accountDao.mar_queryAccountByIds(ids);
    }
    
    public int addAccount(AccountEvt accountEvt)
    {
        accountEvt.setId(MarUtil.createUniqueID());
        accountEvt.setStatus(MarConstant.ACCOUNT_STATUS_0);
        accountEvt.setCrystal(0);
        accountEvt.setUrNumA(0);
        accountEvt.setUrNumB(0);
        accountEvt.setUrNumC(0);
        accountEvt.setUrNumD(0);
        return mar_accountDao.mar_addAccount(accountEvt);
    }
    
    public int updateAccount(AccountEvt accountEvt)
    {
    return mar_accountDao.mar_updateAccount(accountEvt);
    }
    
    public int deleteAccount(List<String> ids)
    {
    return mar_accountDao.mar_deleteAccount(ids);
    }
    
    /**
     * 执行刷初始的调用方法
     */
    public void initialAccount(List<String> ids)
    {
        // 入参已经获得了需要跑初始流程的账号ID 先把账号全部查询出来
        List<AccountEvt> list = mar_accountDao.mar_queryAccountByIds(ids);
        
        // 遍历查询出的账号 对每个账号执行过初始的流程
        for (AccountEvt accountEvt : list)
        {
            if (!MarConstant.ACCOUNT_STATUS_3.equals(accountEvt.getStatus()))
            {
                // 执行前先更新状态为执行中
                accountEvt.setStatus(MarConstant.ACCOUNT_STATUS_1);
                this.updateAccount(accountEvt);
                
                this.process(accountEvt);
            }
        }
    }
    
    /**
     * 
     * @Title: process
     * @Description: 单个账号走刷初始的流程
     * @param accountEvt
     * @return void 返回类型
     * @throws
     */
    public void process(AccountEvt accountEvt)
    {
        String result;
        String sid;
        JSONObject json= null;
        
        // 刷初始的几个配置变量 后续可以改为数据库维护
        String name = "Asuna";
        String chara = "3";
        String gachaIdTen = "20000300";
        String gachaIdEleven = "20000210";
        String payType = "3";
        

        // 刷完10个招待后返回的小号招待ID 给自己填的
        String inviteCode = "";
        List<String> srList = new ArrayList<String>();
        List<String> urList = new ArrayList<String>();
        
        
        // 需要更新到Account中的信息 自身的招待ID
        String inviteId = "";
        // urNumA
        int urNumA = 0;
        // urNumB
        int urNumB = 0;
        // urNumC
        int urNumC = 0;
        // urNumD
        int urNumD = 0;
        // 标题
        String title = "";
        // 卡片ids 先UR后SR 存数据库时使用CommonUtil.listToString()
        List<String> list = new ArrayList<String>();
        // 水晶数量
        int crystal = 0;
        
        try
        {
            // login 登录 并获取sessionId
            result = request.login(accountEvt.getUuid());
            json= JSONObject.fromObject(result);
            sid = json.getString("sess_key").replace("=", "");
            
            // connect
            request.connect(sid);
            
            // 当前版本服务器改为必须等待1分钟才可起名字
            Thread.sleep(61000);
            
            // userCreate 起名字
            request.userCreate(sid, name, chara);
            
            // homeShow 主页
            result = request.homeShow(sid);
            result = ("{\"user\"" + result.split("user\"")[1]);
            result = result.substring(0, result.indexOf(",\"premium_service_grade")) + "}}";
            if (result.contains("�?"))
            {
                result = result.replace("�?", "\"");
            }
            json = JSONObject.fromObject(result);
            crystal = json.getJSONObject("user").getInt("coin_free");
            inviteId = json.getJSONObject("user").getString("inviteid");
            
            // 先保存账号的ID
            accountEvt.setInviteCode(inviteId);
            this.updateAccount(accountEvt);
            
            // 刷10个招待
            inviteCode = this.mar_inviteService.invite(inviteId);
            
            // inviteCodeEnter 刷一个自己的招待
            request.inviteCodeEnter(sid, inviteCode);
            
            // presentBoxMultiRecv 领礼物箱 最好领2次
            request.presentBoxMultiRecv(sid);
            request.presentBoxMultiRecv(sid);
            
            // gachaPlayTen 新人的10连首抽
            request.gachaPlay(sid, gachaIdTen, payType);

            // gachaPlayEleven 当前优惠活动抽取 15
            request.gachaPlay(sid, gachaIdEleven, payType);
            
            // gachaPlayEleven 当前优惠活动抽取 25
            request.gachaPlay(sid, gachaIdEleven, payType);
            
            // cardShow 检索卡组信息
            result = request.cardShow(sid);
            result = ("{\"cards\"" + result.split("cards\"")[1]);
            json = JSONObject.fromObject(result);
            JSONArray cards = json.getJSONArray("cards");
            @SuppressWarnings("unchecked")
            List<JSONObject> jsonList = (List<JSONObject>) JSONArray.toCollection(cards, JSONObject.class);
            
            // 收集SR以及UR信息
            for (JSONObject obj : jsonList)
            {
                if (obj.getString("lv_max").equals("40"))
                {
                    srList.add(obj.getString("cardid"));
                }
                else if (obj.getString("lv_max").equals("50"))
                {
                    urList.add(obj.getString("cardid"));
                }
            }
            
            // 查询数据库卡组信息
            List<KrsmaCardEvt> krsmaCardList = this.mar_krsmaCardService.queryKrsmaCard(new KrsmaCardReq());
            // 处理卡组信息 UR
            for (String id : urList)
            {
                for (KrsmaCardEvt ur : krsmaCardList)
                {
                    if (id.equals(ur.getCardId()))
                    {
                        // 查询到UR 加入ID
                        list.add(id);
                        
                        // 判断该UR是什么职业 相应URNUM+1
                        if (ur.getType().equals(MarConstant.KRSMACARD_TYPE_1))
                        {
                            urNumA++;
                        }
                        else if (ur.getType().equals(MarConstant.KRSMACARD_TYPE_2))
                        {
                            urNumB++;
                        }
                        else if (ur.getType().equals(MarConstant.KRSMACARD_TYPE_3))
                        {
                            urNumC++;
                        }
                        else if (ur.getType().equals(MarConstant.KRSMACARD_TYPE_4))
                        {
                            urNumD++;
                        }
                        
                        // 处理title
                        title += ur.getName() + " ";
                    }
                }
            }
            // 处理卡组信息 SR
            for (String id : srList)
            {
                for (KrsmaCardEvt sr : krsmaCardList)
                {
                    if (id.equals(sr.getCardId()))
                    {
                        list.add(id);
                    }
                }
            }
            
            // TODO 加入战斗信息
        }
        catch (Exception e)
        {
            accountEvt.setStatus(MarConstant.ACCOUNT_STATUS_0);
            this.updateAccount(accountEvt);
            return;
        }
        
        // 更新account信息
        accountEvt.setStatus(MarConstant.ACCOUNT_STATUS_2);
        accountEvt.setTitle(title.trim()); // 处理前后的空格
        accountEvt.setUrNumA(urNumA);
        accountEvt.setUrNumB(urNumB);
        accountEvt.setUrNumC(urNumC);
        accountEvt.setUrNumD(urNumD);
        if (null != list && list.size() > 0)
        {
            accountEvt.setCardIds(CommonUtil.listToString(list));
        }
        accountEvt.setCrystal(crystal);
        this.updateAccount(accountEvt);
    }
    
    /**
     * 
     * @Title: checkCardAccount
     * @Description: 更新卡组信息
     * @param ids
     * @return void 返回类型
     * @throws
     */
    public void checkCardAccount(List<String> ids)
    {
        List<AccountEvt> list = mar_accountDao.mar_queryAccountByIds(ids);
        
        // 遍历查询出的账号 对每个账号执行过checkCard
        for (AccountEvt accountEvt : list)
        {
            this.checkCard(accountEvt);
        }
    }
    
    public void checkCard(AccountEvt accountEvt)
    {
        String result;
        String sid;
        JSONObject json= null;
        List<String> srList = new ArrayList<String>();
        List<String> urList = new ArrayList<String>();
        int urNumA = 0;
        int urNumB = 0;
        int urNumC = 0;
        int urNumD = 0;
        // 标题
        String title = "";
        // 卡片ids 先UR后SR 存数据库时使用CommonUtil.listToString()
        List<String> list = new ArrayList<String>();
        int crystal = 0;
        String inviteCode = "";
        
        // login 登录 并获取sessionId
        result = request.login(accountEvt.getUuid());
        json= JSONObject.fromObject(result);
        sid = json.getString("sess_key").replace("=", "");
        
        // connect
        request.connect(sid);
        
        // presentBoxMultiRecv 领礼物箱 最好领2次
        request.presentBoxMultiRecv(sid);
        request.presentBoxMultiRecv(sid);
        
        // homeShow 主页
        result = request.homeShow(sid);
        result = ("{\"user\"" + result.split("user\"")[1]);
        result = result.substring(0, result.indexOf(",\"premium_service_grade")) + "}}";
        if (result.contains("�?"))
        {
            result = result.replace("�?", "\"");
        }
        json = JSONObject.fromObject(result);
        crystal = json.getJSONObject("user").getInt("coin_free");
        inviteCode = json.getJSONObject("user").getString("inviteid");
        
        // cardShow 检索卡组信息
        result = request.cardShow(sid);
        result = ("{\"cards\"" + result.split("cards\"")[1]);
        json = JSONObject.fromObject(result);
        JSONArray cards = json.getJSONArray("cards");
        @SuppressWarnings("unchecked")
        List<JSONObject> jsonList = (List<JSONObject>) JSONArray.toCollection(cards, JSONObject.class);
        
        // 收集SR以及UR信息
        for (JSONObject obj : jsonList)
        {
            if (obj.getString("lv_max").equals("40"))
            {
                srList.add(obj.getString("cardid"));
            }
            else if (obj.getString("lv_max").equals("50"))
            {
                urList.add(obj.getString("cardid"));
            }
        }
        
        // 查询数据库卡组信息
        List<KrsmaCardEvt> krsmaCardList = this.mar_krsmaCardService.queryKrsmaCard(new KrsmaCardReq());
        // 处理卡组信息 UR
        for (String id : urList)
        {
            for (KrsmaCardEvt ur : krsmaCardList)
            {
                if (id.equals(ur.getCardId()))
                {
                    // 查询到UR 加入ID
                    list.add(id);
                    
                    // 判断该UR是什么职业 相应URNUM+1
                    if (ur.getType().equals(MarConstant.KRSMACARD_TYPE_1))
                    {
                        urNumA++;
                    }
                    else if (ur.getType().equals(MarConstant.KRSMACARD_TYPE_2))
                    {
                        urNumB++;
                    }
                    else if (ur.getType().equals(MarConstant.KRSMACARD_TYPE_3))
                    {
                        urNumC++;
                    }
                    else if (ur.getType().equals(MarConstant.KRSMACARD_TYPE_4))
                    {
                        urNumD++;
                    }
                    
                    // 处理title
                    title += ur.getName() + " ";
                }
            }
        }
        // 处理卡组信息 SR
        for (String id : srList)
        {
            for (KrsmaCardEvt sr : krsmaCardList)
            {
                if (id.equals(sr.getCardId()))
                {
                    list.add(id);
                }
            }
        }

        accountEvt.setInviteCode(inviteCode);
        accountEvt.setTitle(title.trim()); // 处理前后的空格
        accountEvt.setStatus(MarConstant.ACCOUNT_STATUS_2);
        accountEvt.setUrNumA(urNumA);
        accountEvt.setUrNumB(urNumB);
        accountEvt.setUrNumC(urNumC);
        accountEvt.setUrNumD(urNumD);
        if (null != list && list.size() > 0)
        {
            accountEvt.setCardIds(CommonUtil.listToString(list));
        }
        accountEvt.setCrystal(crystal);
        this.updateAccount(accountEvt);
    }
    
    /**
     * @return 返回 mar_accountDao
     */
    public AccountDao getMar_accountDao()
    {
        return mar_accountDao;
    }
    
    /**
     * @param 对mar_accountDao进行赋值
     */
    public void setMar_accountDao(AccountDao mar_accountDao)
    {
        this.mar_accountDao = mar_accountDao;
    }

    public InviteService getMar_inviteService()
    {
        return mar_inviteService;
    }

    public void setMar_inviteService(InviteService mar_inviteService)
    {
        this.mar_inviteService = mar_inviteService;
    }

    public KrsmaCardService getMar_krsmaCardService()
    {
        return mar_krsmaCardService;
    }

    public void setMar_krsmaCardService(KrsmaCardService mar_krsmaCardService)
    {
        this.mar_krsmaCardService = mar_krsmaCardService;
    }
    
    public static void main(String[] args)
    {
        //System.out.println("0123456789".substring(0, 9));
        String result = "1Cjk/meOzs/gq8Rz9j/DBB7ADdDKE={\"res_code\":0,\"res_str\":\"OK\"}{\"user\":{\"userid\":12960326,\"name\":\"Asuna\",\"first_arthur_type\":3,\"arthur_rank\":1,\"lv\":1,\"exp\":0,\"now_lv_exp\":0,\"next_lv_exp\":20,\"fame\":0,\"leader_card_uniqid\":0,\"leader_cardid\":0,\"comment\":\"よろしくおねがいしま??,\"ap\":3,\"ap_max\":3,\"ap_next_sec\":6245,\"ap_heal_sec\":7200,\"bp\":20,\"bp_max\":20,\"bp_next_sec\":125,\"bp_heal_sec\":180,\"card_max_extend\":0,\"card_num\":10,\"card_max\":100,\"friend_max\":20,\"friend_max_extend\":0,\"gold\":5000,\"fp\":2010,\"coin\":0,\"coin_free\":0,\"enter_state\":0,\"navi_type\":0,\"inviteid\":\"020016822\",\"premium_service_grade\":0,\"jobs\":[{\"hp\":0,\"atkp\":0,\"intp\":0,\"mndp\":0},{\"hp\":830,\"atkp\":150,\"intp\":20,\"mndp\":60},{\"hp\":1440,\"atkp\":100,\"intp\":20,\"mndp\":10},{\"hp\":650,\"atkp\":30,\"intp\":150,\"mndp\":80},{\"hp\":680,\"atkp\":20,\"intp\":100,\"mndp\":150}]},\"quests\":[{\"now_quest\":[{\"questid\":13,\"reward\":[{\"type\":8,\"num\":1,\"reward_typeid\":2000}],\"feature_flag\":64487488,\"title\":\"クエスト『初めの????』を<br>クリアす??,\"description\":\"クエスト『初めの????』を<br>クリアす??,\"receive_comment\":[\"<color red>クエスト</color>から<color red>ひとりでクエスト</color>を選択し??br>指定のクエストのクリアを目指してください??]}]}],\"banners\":[{\"home_banner_type\":0,\"image_url\":\"http://www.cache.kairisei-ma.jp/top/2014040714/app_banners/141209_windgacha_ba.png\",\"open_url\":\"http://stg.www.kairisei-ma.jp/information/2014/12/gacha-wind.html\"},{\"home_banner_type\":0,\"image_url\":\"http://www.cache.kairisei-ma.jp/top/2014040714/app_banners/ba_gacha_20141210.png\",\"open_url\":\"http://www.kairisei-ma.jp/information/2014/11/gacha3.html\"},{\"home_banner_type\":0,\"image_url\":\"http://www.cache.kairisei-ma.jp/top/2014040714/app_banners/141202_3d_choudokyu_ba.png\",\"open_url\":\"http://www.kairisei-ma.jp/information/2014/12/12.html\"},{\"home_banner_type\":0,\"image_url\":\"http://www.cache.kairisei-ma.jp/top/2014040714/app_banners/ba_info_navi.png\",\"open_url\":\"http://www.kairisei-ma.jp/information/2014/11/post.html\"},{\"home_banner_type\":4,\"image_url\":\"http://www.cache.kairisei-ma.jp/top/2014040714/app_banners/ba_info_fri.png\",\"open_url\":\"teambattleone:200005\"},{\"home_banner_type\":4,\"image_url\":\"http://www.cache.kairisei-ma.jp/top/2014040714/app_banners/ba_info_20141101.png\",\"open_url\":\"teambattleone:400020\"},{\"home_banner_type\":4,\"image_url\":\"http://www.cache.kairisei-ma.jp/top/2014040714/app_banners/ba_info_20141203.png\",\"open_url\":\"teambattleone:400080\"}],\"server_time\":1418372745}";
        //System.out.println(("{\"user\"" + result.split("user\"")[1]).replace("????", "xxxx").replace("??,", "\",").replace("??]", "\"]"));
        result = ("{\"user\"" + result.split("user\"")[1]);
        result = result.substring(0, result.indexOf(",\"premium_service_grade")) + "}}";
        if (result.contains("?"))
        {
            result = result.replace("??,", "\",");
        }
        System.out.println(result);
        JSONObject  json = JSONObject.fromObject(result);
        int crystal = json.getJSONObject("user").getInt("coin_free");
        System.out.println(crystal);
    }
}