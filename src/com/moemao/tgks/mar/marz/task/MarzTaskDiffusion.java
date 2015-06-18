package com.moemao.tgks.mar.marz.task;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.moemao.tgks.common.core.spring.ContextUtil;
import com.moemao.tgks.common.tool.CommonConstant;
import com.moemao.tgks.common.tool.CommonUtil;
import com.moemao.tgks.mar.execute.MarzRequest;
import com.moemao.tgks.mar.marz.entity.CardEvt;
import com.moemao.tgks.mar.marz.entity.DeckEvt;
import com.moemao.tgks.mar.marz.entity.MissionEvt;
import com.moemao.tgks.mar.marz.thread.MarzThreadPoolDiffusion;
import com.moemao.tgks.mar.marz.tool.MarzConstant;
import com.moemao.tgks.mar.marz.tool.MarzUtil;
import com.moemao.tgks.mar.marzaccount.entity.MarzAccountEvt;
import com.moemao.tgks.mar.marzaccount.service.MarzAccountService;
import com.moemao.tgks.mar.marzlog.service.MarzLogService;
import com.moemao.tgks.mar.marzmap.entity.MarzMapEvt;
import com.moemao.tgks.mar.marzmap.entity.MarzMapReq;
import com.moemao.tgks.mar.marzmap.service.MarzMapService;
import com.moemao.tgks.mar.marzsetting.entity.MarzSettingEvt;
import com.moemao.tgks.mar.marzsetting.entity.MarzSettingReq;
import com.moemao.tgks.mar.marzsetting.service.MarzSettingService;
import com.moemao.tgks.mar.tool.MarConstant;

public class MarzTaskDiffusion implements Runnable, ApplicationContextAware
{
    private static Log logger = LogFactory.getLog(MarzTaskDiffusion.class);
    
    private MarzRequest request = MarzRequest.getInstance();
    
    private MarzAccountService marzAccountService;
    
    private MarzLogService marzLogService;
    
    private MarzSettingService marzSettingService;
    
    private MarzMapService marzMapService;
    
    private MarzSettingEvt marzSettingEvt;
    
    private MarzAccountEvt account;
    
    private Map<String, JSONObject> map;
    
    private String sid;
    
    private int resultCode = MarzConstant.SUCCESS;
    
    private static int SLEEPTIME = 2 * 60 * 1000;
    
    /**
     * 账户的四职业第一卡组
     */
    private Map<String, String> deckMap;
    
    private Map<String, String> pvpEndMap;
    
    public boolean running = true;
    
    public MarzTaskDiffusion(MarzAccountEvt marzAccountEvt)
    {
        // 初始化一些参数
        account = marzAccountEvt;
        marzAccountService = (MarzAccountService) ContextUtil.getBean("mar_marzAccountService");
        marzLogService = (MarzLogService) ContextUtil.getBean("mar_marzLogService");
        marzSettingService = (MarzSettingService) ContextUtil.getBean("mar_marzSettingService");
        marzMapService = (MarzMapService) ContextUtil.getBean("mar_marzMapService");
        
        // PVP结束参数
        pvpEndMap = new HashMap<String, String>();
        pvpEndMap.put("1", MarConstant.PVPEND_1);
        pvpEndMap.put("2", MarConstant.PVPEND_2);
        pvpEndMap.put("3", MarConstant.PVPEND_3);
        pvpEndMap.put("4", MarConstant.PVPEND_4);
        
        // 默认线程调用的执行方法
        System.out.println("执行任务开始 ID：" + account.getTgksId());
    }

    @Override
    public void run()
    {
        Thread.currentThread().setName(MarConstant.MODULE_TAG + account.getTgksId());
        
        // 尽量保证流程上的简洁 run流程只负责调用以及返回失败时的处理 并不做各个条件判断的限制
        try
        {
            while (running)
            {
                
                // 为了防止出现点击上线按钮立即生成线程却跳出的情况 线程需要先暂停1秒
                Thread.sleep(1000);
                
                account = this.marzAccountService.queryMarzAccountById(account.getId());
                if (MarzConstant.MARZ_ACCOUNT_STATUS_0.equals(account.getStatus()))
                {
                    break;
                }
                
                // 每次循环检查点卡是否到期
                if (new Date().after(account.getEndTime()))
                {
                    this.marzLogService.marzLog(account, MarzConstant.MARZ_LOG_TYPE_0, account.getTgksId() + "已经到期，退出挂机...");
                    account.setStatus(MarzConstant.MARZ_ACCOUNT_STATUS_0);
                    this.marzAccountService.updateMarzAccount(account);
                    break;
                }
                
                // 更新设置
                this.initSetting();
                
                
                // 1、账号登陆
                if (CommonUtil.isEmpty(account.getSessionId()))
                {
                    resultCode = this.login();
                    
                    if (MarzConstant.RES_CODE_SUCCESS_0 != resultCode)
                    {
                        if (MarzConstant.RES_CODE_ERROR_M5 == resultCode)
                        {
                            this.marzLogService.marzLog(account, MarzConstant.MARZ_LOG_TYPE_0, "系统维护中，账号自动下线，请留意游戏公告等开服后手动上线...");
                            this.offLine();
                            break;
                        }
                        else if (MarzConstant.RES_CODE_ERROR_M7 == resultCode)
                        {
                            this.marzLogService.marzLog(account, MarzConstant.MARZ_LOG_TYPE_0, "账号被重新引继过，原存档文件失效，无法继续挂机需要重新绑定账号...");
                            this.offLine();
                            break;
                        }
                        else if (MarzConstant.RES_CODE_ERROR_M8 == resultCode)
                        {
                            this.marzLogService.marzLog(account, MarzConstant.MARZ_LOG_TYPE_0, "游戏客户端已更新，服务器需要同步更新，请关注公告...");
                            this.offLine();
                            break;
                        }
                        
                        this.sleep(SLEEPTIME);
                        continue;
                    }
                }
                else
                {
                    // 当前的逻辑 每次扫描出的任务如果包含sid 则跳过登录
                    // 在用户操作上下线的时候 将account的sid一同清空即可
                    // 如果sid已经失效 会在下面的homeShow中重新登录
                    sid = account.getSessionId();
                }
                
                if (Thread.currentThread().getName().contains(MarzConstant.OVER))
                {
                    break;
                }
                
                // 2、更新当前账号基础数据
                resultCode = this.baseInfo();
                
                if (MarzConstant.SUCCESS > resultCode)
                {
                    System.out.println("发生了错误！当前resultCode：" + resultCode);
                    
                    resultCode = this.login();
                    
                    if (MarzConstant.RES_CODE_SUCCESS_0 != resultCode)
                    {
                        if (MarzConstant.RES_CODE_ERROR_M5 == resultCode)
                        {
                            this.marzLogService.marzLog(account, MarzConstant.MARZ_LOG_TYPE_0, "系统维护中，账号自动下线，请留意游戏公告等开服后手动上线...");
                            this.offLine();
                            break;
                        }
                        else if (MarzConstant.RES_CODE_ERROR_M7 == resultCode)
                        {
                            this.marzLogService.marzLog(account, MarzConstant.MARZ_LOG_TYPE_0, "账号被重新引继过，原存档文件失效，无法继续挂机需要重新绑定账号...");
                            this.offLine();
                            break;
                        }
                        else if (MarzConstant.RES_CODE_ERROR_M8 == resultCode)
                        {
                            this.marzLogService.marzLog(account, MarzConstant.MARZ_LOG_TYPE_0, "游戏客户端已更新，服务器需要同步更新，请关注公告...");
                            this.offLine();
                            break;
                        }
                        
                        this.sleep(SLEEPTIME);
                        continue;
                    }
                    
                    resultCode = this.baseInfo();
                    
                    if (MarzConstant.SUCCESS > resultCode)
                    {
                        account.setSessionId("");
                        account.setRemark("");
                        this.marzAccountService.updateMarzAccount(account);
                        
                        this.sleep(SLEEPTIME);
                        continue;
                    }
                }
                
                if (Thread.currentThread().getName().contains(MarzConstant.OVER))
                {
                    break;
                }
                
                // 3、探索
                resultCode = this.explore();
                
                if (Thread.currentThread().getName().contains(MarzConstant.OVER))
                {
                    break;
                }
                
                // 4、卡片处理
                resultCode = this.card();
                
                if (Thread.currentThread().getName().contains(MarzConstant.OVER))
                {
                    break;
                }
                
                // 5、打副本
                resultCode = this.battle();
                
                if (Thread.currentThread().getName().contains(MarzConstant.OVER))
                {
                    break;
                }
                
                // 6、PVP
                resultCode = this.pvp();
                
                // 最后要保存一下sessionId
                account.setSessionId(sid);
                MarzAccountEvt tempAccount = this.marzAccountService.queryMarzAccountById(account.getId());
                account.setBossIds(tempAccount.getBossIds());
                account.setStatus(tempAccount.getStatus());
                this.marzAccountService.updateMarzAccount(account);
                
                this.marzLogService.marzLog(account, MarzConstant.MARZ_LOG_TYPE_0, account.getTgksId() + "本轮执行完毕，等待下一次执行...");
                
                if (Thread.currentThread().getName().contains(MarzConstant.OVER))
                {
                    break;
                }
                
                System.out.println(Thread.currentThread().getName() + "本次任务执行完成 进入等待时间 [" + new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()) + "]");
                
                SLEEPTIME = ((2 * 60) + (MarzThreadPoolDiffusion.getInstance().getMarzThreadNum() * 6)) * 1000;
                
                // 等待时间间隔
                this.sleep(SLEEPTIME);
            }
        }
        catch (Exception e)
        {
            
        }
        
        this.offLine();
        
        this.marzLogService.marzLog(account, MarzConstant.MARZ_LOG_TYPE_0, account.getTgksId() + "已经下线...");
        System.out.println(MarConstant.MODULE_TAG + account.getTgksId() + "线程已关闭...");
        Thread.currentThread().setName(MarConstant.MODULE_TAG + MarzConstant.OVER);
    }
    
    private void offLine()
    {
        // 初始化状态
        account.setStatus(MarzConstant.MARZ_ACCOUNT_STATUS_0);
        account.setAp(0);
        account.setApMax(0);
        account.setBp(0);
        account.setBpMax(0);
        //account.setCardNum(0);
        //account.setCardMax(0);
        account.setCoin(0);
        account.setFp(0);
        account.setGold(0);
        account.setSessionId("");
        account.setRemark("");
        this.marzAccountService.updateMarzAccount(account);
    }
    
    private void initSetting()
    {
        String tgksId = account.getTgksId();
        marzSettingEvt = new MarzSettingEvt();
        marzSettingEvt.setTgksId(tgksId);
        MarzSettingReq marzSettingReq = new MarzSettingReq();
        marzSettingReq.setTgksId(tgksId);
        
        List<MarzSettingEvt> marzSettinglist = this.marzSettingService.queryMarzSetting(marzSettingReq);
        
        if (!CommonUtil.isEmpty(marzSettinglist))
        {
            for (MarzSettingEvt setting : marzSettinglist)
            {
                if (MarzConstant.VALIDATE_SETTING_EXPLORE == Integer.parseInt(setting.getName()))
                {
                    marzSettingEvt.setExplore(setting.getValue());
                }
                else if (MarzConstant.VALIDATE_SETTING_CARDSELL == Integer.parseInt(setting.getName()))
                {
                    marzSettingEvt.setCardSell(setting.getValue());
                }
                else if (MarzConstant.VALIDATE_SETTING_CARDSELL_COMMON == Integer.parseInt(setting.getName()))
                {
                    marzSettingEvt.setCardSellCommon(setting.getValue());
                }
                else if (MarzConstant.VALIDATE_SETTING_CHIARIFUSION == Integer.parseInt(setting.getName()))
                {
                    marzSettingEvt.setCardFusion(setting.getValue());
                }
                else if (MarzConstant.VALIDATE_SETTING_BATTLE == Integer.parseInt(setting.getName()))
                {
                    marzSettingEvt.setBattle(setting.getValue());
                }
                else if (MarzConstant.VALIDATE_SETTING_BATTLE_NOWASTE == Integer.parseInt(setting.getName()))
                {
                    marzSettingEvt.setBattleNowaste(setting.getValue());
                }
                else if (MarzConstant.VALIDATE_SETTING_BATTLE_NOWASTE_BOSSID == Integer.parseInt(setting.getName()))
                {
                    marzSettingEvt.setBattleNowasteBossId(setting.getValue());
                }
                else if (MarzConstant.VALIDATE_SETTING_BATTLE_GET_STONE == Integer.parseInt(setting.getName()))
                {
                    marzSettingEvt.setBattleGetStone(setting.getValue());
                }
                else if (MarzConstant.VALIDATE_SETTING_PVP == Integer.parseInt(setting.getName()))
                {
                    marzSettingEvt.setPvp(setting.getValue());
                }
                else if (MarzConstant.VALIDATE_SETTING_FAMEFUSION == Integer.parseInt(setting.getName()))
                {
                    marzSettingEvt.setFameFusion(setting.getValue());
                }
                else if (MarzConstant.VALIDATE_SETTING_COINGACHA == Integer.parseInt(setting.getName()))
                {
                    marzSettingEvt.setCoinGacha(setting.getValue());
                }
                else if (MarzConstant.VALIDATE_SETTING_COINGACHA_GACHAID == Integer.parseInt(setting.getName()))
                {
                    marzSettingEvt.setCoinGachaGachaId(setting.getValue());
                }
                else if (MarzConstant.VALIDATE_SETTING_AUTOUSEBPPOTION == Integer.parseInt(setting.getName()))
                {
                    marzSettingEvt.setAutoUseBPPotion(setting.getValue());
                }
                else if (MarzConstant.VALIDATE_SETTING_AUTOBUYBPPOTION == Integer.parseInt(setting.getName()))
                {
                    marzSettingEvt.setAutoBuyBPPotion(setting.getValue());
                }
            }
        }
    }
    
    /**
     * 
     * @Title: sleep
     * @Description: 循环等待
     * @param sleepTime
     * @return void 返回类型
     * @throws
     */
    private void sleep(int sleepTime)
    {
        try
        {
            Thread.sleep(sleepTime);
        }
        catch (Exception e)
        {
            
        }
    }
    
    /**
     * 
     * @Title: login
     * @Description: 账户登陆 login+connect
     * @return void 返回类型
     * @throws
     */
    private int login()
    {
        try
        {
            if (MarzConstant.MARZ_ACCOUNT_TYPE_0.equals(account.getType()))
            {
                map = request.loginIOS(account.getIosUuid(), account.getIosKey());
            }
            else if (MarzConstant.MARZ_ACCOUNT_TYPE_1.equals(account.getType()))
            {
                map = request.loginAndroid(account.getAndroidUuid(), account.getAndroidKey());
            }
            else
            {
                map = request.loginIOS(account.getIosUuid(), account.getIosKey());
            }
            
            resultCode = map.get(MarzConstant.JSON_TAG_RESCODE).getInt(MarzConstant.JSON_TAG_RESCODE);
            
            this.marzLogService.marzLog(account, MarzConstant.MARZ_LOG_TYPE_0, "账号登录" + MarzUtil.resultCodeStr(resultCode));
            
            if (MarzConstant.RES_CODE_SUCCESS_0 == resultCode)
            {
                sid = map.get(MarzConstant.JSON_TAG_SID).getString(MarzConstant.JSON_TAG_SID);
                
                map = request.connect(sid);
                
                account.setSessionId(sid);
                //sid = map.get(MarzConstant.JSON_TAG_SID).getString(MarzConstant.JSON_TAG_SID);
            }
        }
        catch (Exception e)
        {
            System.out.println("登陆失败！退出任务");
            return MarzConstant.FAILED;
        }
        
        return resultCode;
    }
    
    private int baseInfo()
    {
        try
        {
            map = request.homeShow(sid);
            
            resultCode = map.get(MarzConstant.JSON_TAG_RESCODE).getInt(MarzConstant.JSON_TAG_RESCODE);
            
            //this.marzLogService.marzLog(account, MarzConstant.MARZ_LOG_TYPE_0, "账号基本信息更新" + MarzUtil.resultCodeStr(resultCode));
            
            if (MarzConstant.RES_CODE_SUCCESS_0 == resultCode)
            {
                sid = map.get(MarzConstant.JSON_TAG_SID).getString(MarzConstant.JSON_TAG_SID);
                
                JSONObject user = map.get(MarzConstant.JSON_TAG_HOMWSHOW);
                account.setAp(user.getJSONObject("user").getInt("ap"));
                account.setApMax(user.getJSONObject("user").getInt("ap_max"));
                account.setBp(user.getJSONObject("user").getInt("bp"));
                account.setBpMax(user.getJSONObject("user").getInt("bp_max"));
                account.setCardMax(user.getJSONObject("user").getInt("card_max"));
                account.setCardNum(user.getJSONObject("user").getInt("card_num"));
                account.setCoin(user.getJSONObject("user").getInt("coin") + user.getJSONObject("user").getInt("coin_free"));
                account.setFp(user.getJSONObject("user").getInt("fp"));
                account.setGold(user.getJSONObject("user").getInt("gold"));
                account.setLv(user.getJSONObject("user").getInt("lv"));
                account.setName(user.getJSONObject("user").getString("name"));
                account.setUserId(user.getJSONObject("user").getString("userid"));
                
                account.setSessionId(sid);
                // 保存之前先查询最新的BOSSID 防止
                MarzAccountEvt tempAccount = this.marzAccountService.queryMarzAccountById(account.getId());
                account.setBossIds(tempAccount.getBossIds());
                account.setStatus(tempAccount.getStatus());
                this.marzAccountService.updateMarzAccount(account);
            }
        }
        catch (Exception e)
        {
            return MarzConstant.FAILED;
        }
        
        return resultCode;
    }
    
    private int explore()
    {
        // 在开头加入条件限制
        if (!validateSetting(MarzConstant.VALIDATE_SETTING_EXPLORE) || account.getAp() == 0 || (account.getBpMax() - account.getBp()) < 6)
        {
            return MarzConstant.SUCCESS;
        }
        
        try
        {
            // 接口设计的是可以根据职业来做 不过这个没啥意义 就写死了
            request.exploreStart(sid, "1", "0");
            
            map = request.exploreEnd(sid);
            
            resultCode = map.get(MarzConstant.JSON_TAG_RESCODE).getInt(MarzConstant.JSON_TAG_RESCODE);
            
            this.marzLogService.marzLog(account, MarzConstant.MARZ_LOG_TYPE_2, "探索" + MarzUtil.resultCodeStr(resultCode));
            
            if (MarzConstant.RES_CODE_SUCCESS_0 == resultCode)
            {
                sid = map.get(MarzConstant.JSON_TAG_SID).getString(MarzConstant.JSON_TAG_SID);
                
                JSONObject user = map.get(MarzConstant.JSON_TAG_EXPLOREEND);
                account.setAp(user.getJSONObject("user").getInt("ap"));
                account.setApMax(user.getJSONObject("user").getInt("ap_max"));
                account.setCardMax(user.getJSONObject("user").getInt("card_max"));
                account.setCardNum(user.getJSONObject("user").getInt("card_num"));
                account.setGold(user.getJSONObject("user").getInt("gold"));
            }
        }
        catch (Exception e)
        {
            return MarzConstant.FAILED;
        }
        
        return resultCode;
    }
    
    /**
     * 
     * @Title: card
     * @Description: 卡片处理 合成 出售等
     * @return
     * @return int 返回类型
     * @throws
     */
    private int card()
    {
        try
        {
            // 更新卡片信息
            map = request.cardShow2(sid);
            
            resultCode = map.get(MarzConstant.JSON_TAG_RESCODE).getInt(MarzConstant.JSON_TAG_RESCODE);
            
            //this.marzLogService.marzLog(account, MarzConstant.MARZ_LOG_TYPE_0, "卡片信息更新" + MarzUtil.resultCodeStr(resultCode));
            
            if (MarzConstant.RES_CODE_SUCCESS_0 == resultCode)
            {
                sid = map.get(MarzConstant.JSON_TAG_SID).getString(MarzConstant.JSON_TAG_SID);
                
                JSONObject cardShowJSON = map.get(MarzConstant.JSON_TAG_CARDSHOW);
                
                // cardShow2接口中 cards → 0
                JSONArray cardsJSON = cardShowJSON.getJSONArray("0");
                
                List<CardEvt> cardList = new ArrayList<CardEvt>();
                JSONObject cardJSON;
                
                for (int i = 0, size = cardsJSON.size(); i < size; i++)
                {
                    cardJSON = JSONObject.fromObject(cardsJSON.get(i));
                    cardList.add(new CardEvt(cardJSON));
                }
                
                // add by Ken 2015-6-3 for PVP & Solo
                // cardShow2接口中 decks → 1
                JSONArray decksJSON = cardShowJSON.getJSONArray("1");
                deckMap = new HashMap<String, String>();
                JSONObject deckJSON;
                DeckEvt deckEvt;
                
                for (int i = 0, size = decksJSON.size(); i < size; i++)
                {
                    deckJSON = JSONObject.fromObject(decksJSON.get(i));
                    deckEvt = new DeckEvt(deckJSON);
                    if ("1".equals(deckEvt.getArthur_type()) && "0".equals(deckEvt.getIdx()) && "1".equals(deckEvt.getJob_type()))
                    {
                        // 佣兵第一卡组
                        deckMap.put("1", deckEvt.getCard_uniqid().replace("[", "").replace("]", ""));
                    }
                    else if ("2".equals(deckEvt.getArthur_type()) && "0".equals(deckEvt.getIdx()) && "2".equals(deckEvt.getJob_type()))
                    {
                        // 富豪第一卡组
                        deckMap.put("2", deckEvt.getCard_uniqid().replace("[", "").replace("]", ""));
                    }
                    else if ("3".equals(deckEvt.getArthur_type()) && "0".equals(deckEvt.getIdx()) && "3".equals(deckEvt.getJob_type()))
                    {
                        // 盗贼第一卡组
                        deckMap.put("3", deckEvt.getCard_uniqid().replace("[", "").replace("]", ""));
                    }
                    else if ("4".equals(deckEvt.getArthur_type()) && "0".equals(deckEvt.getIdx()) && "4".equals(deckEvt.getJob_type()))
                    {
                        // 歌姬第一卡组
                        deckMap.put("4", deckEvt.getCard_uniqid().replace("[", "").replace("]", ""));
                    }
                }
                
                List<String> cardSellList = new ArrayList<String>();
                List<String> chiariFusionList = new ArrayList<String>();
                List<String> fameFusionList = new ArrayList<String>();
                
                // 自动卖卡
                if (validateSetting(MarzConstant.VALIDATE_SETTING_CARDSELL))
                {
                    // 查询用户设定的售卡列表
                    List<String> userSellList = MarzUtil.stringToList(account.getSellCardIds());
                    
                    if (validateSetting(MarzConstant.VALIDATE_SETTING_CARDSELL_COMMON))
                    {
                        // 金币卡
                        userSellList.add("20000026");
                        userSellList.add("20000027");
                        userSellList.add("20000028");
                        userSellList.add("20000029");
                        // 蓝狗粮
                        userSellList.add("20000001");
                        // 2星进化素材
                        userSellList.add("20000009");
                        userSellList.add("20000008");
                        userSellList.add("20000007");
                        userSellList.add("20000006");
                        userSellList.add("20000005");
                    }
            
                    // 遍历所有卡片 把需要出售的卡片ID放入cardSellList
                    for (CardEvt card : cardList)
                    {
                        // 只能出售未锁定以及是1级的卡
                        if (0 == card.getIs_lock() && 1 == card.getLv())
                        {
                            // 先卖 出售列表中的卡
                            if (userSellList.contains(card.getCardid()))
                            {
                                cardSellList.add(card.getUniqid());
                            }
                            // 然后卖一些基础的垃圾卡 10~30
                            else if (validateSetting(MarzConstant.VALIDATE_SETTING_CARDSELL_COMMON)
                                    && card.getLv_max() >= 10 && card.getLv_max() <= 30)
                            {
                                cardSellList.add(card.getUniqid());
                            }
                        }
                        
                        // 当出售的卡片满10张时 跳出
                        if (cardSellList.size() == 10)
                        {
                            break;
                        }
                    }
                    
                    // 组装卡牌ID调用cardSell请求
                    if (cardSellList.size() > 0)
                    {
                        map = request.cardSell(sid, MarzUtil.listToString(cardSellList));
                        
                        resultCode = map.get(MarzConstant.JSON_TAG_RESCODE).getInt(MarzConstant.JSON_TAG_RESCODE);
                        
                        //this.marzLogService.marzLog(account, MarzConstant.MARZ_LOG_TYPE_5, "卡片出售" + MarzUtil.resultCodeStr(resultCode));
                        
                        if (MarzConstant.RES_CODE_SUCCESS_0 == resultCode)
                        {
                            sid = map.get(MarzConstant.JSON_TAG_SID).getString(MarzConstant.JSON_TAG_SID);
                            
                            account.setCardNum(map.get(MarzConstant.JSON_TAG_CARDSELL).getInt("card_num"));
                            account.setGold(account.getGold() + map.get(MarzConstant.JSON_TAG_CARDSELL).getInt("get_gold"));
                            
                            this.marzLogService.marzLog(account, MarzConstant.MARZ_LOG_TYPE_5, "已卖出卡片ID " + MarzUtil.listToString(cardSellList));
                        }
                    }
                }
                
                // 名声合成
                if (validateSetting(MarzConstant.VALIDATE_SETTING_FAMEFUSION))
                {
                    // TODO
                    // 查出已经设定好的名声合成卡片ID的List，然后遍历整个卡组，把每种卡放入Map<String, List<uniqueId>>中，之后再处理每种卡片
                    fameFusionList.add("");
                }
                
                // 狗粮合成
                if (validateSetting(MarzConstant.VALIDATE_SETTING_CHIARIFUSION))
                {
                    // 只自动喂 蓝狗 红狗 粉狗
                    String[] chiari = {"20000001", "20000002", "20000003"};
                    String baseId = "";
                    
                    for (CardEvt card : cardList)
                    {
                        // 自动合成只支持SR UR跟MR 而且必须手动锁上 先找MR 不锁也可
                        // update by ken 20150503 MR不锁改为不自动喂卡
                        if (card.getLv_max() >= 60 && card.getLv() < card.getLv_max()
                                && 0 != card.getIs_lock())
                        {
                            baseId = card.getUniqid();
                        }
                        // 狗粮 一次喂4个 不能是已经出售了的
                        else if (!cardSellList.contains(card.getUniqid()) 
                                && Arrays.asList(chiari).contains(card.getCardid())
                                && !chiariFusionList.contains(card.getUniqid())
                                && chiariFusionList.size() < 4)
                        {
                            chiariFusionList.add(card.getUniqid());
                        }
                        
                        if (!CommonUtil.isEmpty(baseId) && chiariFusionList.size() == 4)
                        {
                            break;
                        }
                    }
                    // 如果没有MR以上的卡 再挑UR喂
                    if (CommonUtil.isEmpty(baseId))
                    {
                        for (CardEvt card : cardList)
                        {
                            // 没有MR可喂的时候，找锁上的UR喂
                            if (card.getLv_max() >= 50 && card.getLv() < card.getLv_max()
                                && 0 != card.getIs_lock())
                            {
                                baseId = card.getUniqid();
                            }
                            // 狗粮 一次喂4个 不能是已经出售了的
                            else if (!cardSellList.contains(card.getUniqid()) 
                                    && Arrays.asList(chiari).contains(card.getCardid())
                                    && !chiariFusionList.contains(card.getUniqid())
                                    && chiariFusionList.size() < 4)
                            {
                                chiariFusionList.add(card.getUniqid());
                            }
                            
                            if (!CommonUtil.isEmpty(baseId) && chiariFusionList.size() == 4)
                            {
                                break;
                            }
                        }
                    }
                    // 如果没有UR以上的卡 再挑SR喂
                    if (CommonUtil.isEmpty(baseId))
                    {
                        for (CardEvt card : cardList)
                        {
                            // 没有MR UR可喂的时候，找锁上的SR喂
                            if (card.getLv_max() >= 40 && card.getLv() < card.getLv_max()
                                        && 0 != card.getIs_lock())
                            {
                                baseId = card.getUniqid();
                            }
                            // 狗粮 一次喂4个 不能是已经出售了的
                            else if (!cardSellList.contains(card.getUniqid()) 
                                    && Arrays.asList(chiari).contains(card.getCardid())
                                    && !chiariFusionList.contains(card.getUniqid())
                                    && chiariFusionList.size() < 4)
                            {
                                chiariFusionList.add(card.getUniqid());
                            }
                            
                            if (!CommonUtil.isEmpty(baseId) && chiariFusionList.size() == 4)
                            {
                                break;
                            }
                        }
                    }
                    
                    if (!CommonUtil.isEmpty(baseId) && chiariFusionList.size() > 0)
                    {
                        map = request.cardFusion(sid, baseId, MarzUtil.listToString(chiariFusionList));
                        
                        resultCode = map.get(MarzConstant.JSON_TAG_RESCODE).getInt(MarzConstant.JSON_TAG_RESCODE);
                        
                        //this.marzLogService.marzLog(account, MarzConstant.MARZ_LOG_TYPE_4, "卡片合成" + MarzUtil.resultCodeStr(resultCode));
                        
                        if (MarzConstant.RES_CODE_SUCCESS_0 == resultCode)
                        {
                            sid = map.get(MarzConstant.JSON_TAG_SID).getString(MarzConstant.JSON_TAG_SID);
                            
                            account.setCardNum(map.get(MarzConstant.JSON_TAG_CARDFUSION).getInt("card_num"));
                            account.setGold(map.get(MarzConstant.JSON_TAG_CARDFUSION).getInt("gold"));
                            
                            this.marzLogService.marzLog(account, MarzConstant.MARZ_LOG_TYPE_4, "主卡ID " + baseId + " 消耗狗粮 " + MarzUtil.listToString(chiariFusionList));
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            return MarzConstant.FAILED;
        }
        
        return resultCode;
    }
    
    
    
    @SuppressWarnings("unused")
    private int card_old()
    {
        try
        {
            // 更新卡片信息
            map = request.cardShow(sid);
            
            resultCode = map.get(MarzConstant.JSON_TAG_RESCODE).getInt(MarzConstant.JSON_TAG_RESCODE);
            
            //this.marzLogService.marzLog(account, MarzConstant.MARZ_LOG_TYPE_0, "卡片信息更新" + MarzUtil.resultCodeStr(resultCode));
            
            if (MarzConstant.RES_CODE_SUCCESS_0 == resultCode)
            {
                sid = map.get(MarzConstant.JSON_TAG_SID).getString(MarzConstant.JSON_TAG_SID);
                
                JSONObject card = map.get(MarzConstant.JSON_TAG_CARDSHOW);
                JSONArray cards = card.getJSONArray("cards");
                
                List<String> cardSellList = new ArrayList<String>();
                List<String> cardFusionList = new ArrayList<String>();
                JSONObject cardJSON;
                
                // 自动卖卡
                if (validateSetting(MarzConstant.VALIDATE_SETTING_CARDSELL))
                {
                    // 查询用户设定的售卡列表
                    List<String> userSellList = new ArrayList<String>();
                    
                    if (validateSetting(MarzConstant.VALIDATE_SETTING_CARDSELL_COMMON))
                    {
                        // 金币卡
                        userSellList.add("20000026");
                        userSellList.add("20000027");
                        userSellList.add("20000028");
                        userSellList.add("20000029");
                        // 蓝狗粮
                        userSellList.add("20000001");
                        // 2星进化素材
                        userSellList.add("20000009");
                        userSellList.add("20000008");
                        userSellList.add("20000007");
                        userSellList.add("20000006");
                        userSellList.add("20000005");
                    }
            
                    // 遍历所有卡片 把需要出售的卡片ID放入cardSellList
                    for (int i = 0; i < cards.size(); i++)
                    {
                        cardJSON = JSONObject.fromObject(cards.get(i));
                        // 只能出售未锁定以及是1级的卡
                        if (0 == cardJSON.getInt("is_lock") && 1 == cardJSON.getInt("lv"))
                        {
                            // 先卖 出售列表中的卡
                            if (userSellList.contains(cardJSON.getString("cardid")))
                            {
                                cardSellList.add(cardJSON.getString("uniqid"));
                            }
                            // 然后卖一些基础的垃圾卡 10~30
                            else if (validateSetting(MarzConstant.VALIDATE_SETTING_CARDSELL_COMMON)
                                    && cardJSON.getInt("lv_max") >= 10 && cardJSON.getInt("lv_max") <= 30)
                            {
                                cardSellList.add(cardJSON.getString("uniqid"));
                            }
                        }
                        
                        // 当出售的卡片满10张时 跳出
                        if (cardSellList.size() == 10)
                        {
                            break;
                        }
                    }
                    
                    // 组装卡牌ID调用cardSell请求
                    if (cardSellList.size() > 0)
                    {
                        map = request.cardSell(sid, MarzUtil.listToString(cardSellList));
                        
                        resultCode = map.get(MarzConstant.JSON_TAG_RESCODE).getInt(MarzConstant.JSON_TAG_RESCODE);
                        
                        //this.marzLogService.marzLog(account, MarzConstant.MARZ_LOG_TYPE_5, "卡片出售" + MarzUtil.resultCodeStr(resultCode));
                        
                        if (MarzConstant.RES_CODE_SUCCESS_0 == resultCode)
                        {
                            sid = map.get(MarzConstant.JSON_TAG_SID).getString(MarzConstant.JSON_TAG_SID);
                            
                            account.setCardNum(map.get(MarzConstant.JSON_TAG_CARDSELL).getInt("card_num"));
                            account.setGold(account.getGold() + map.get(MarzConstant.JSON_TAG_CARDSELL).getInt("get_gold"));
                            
                            this.marzLogService.marzLog(account, MarzConstant.MARZ_LOG_TYPE_5, "已卖出卡片ID " + MarzUtil.listToString(cardSellList));
                        }
                    }
                }
                
                // 狗粮合成
                if (validateSetting(MarzConstant.VALIDATE_SETTING_CHIARIFUSION))
                {
                    // 只自动喂 蓝狗 红狗 粉狗
                    String[] chiari = {"20000001", "20000002", "20000003"};
                    String baseId = "";
                    
                    for (int i = 0; i < cards.size(); i++)
                    {
                        cardJSON = JSONObject.fromObject(cards.get(i));
                        
                        // 自动合成只支持SR UR跟MR 而且必须手动锁上 先找MR 不锁也可
                        if (cardJSON.getInt("lv_max") >= 60 && cardJSON.getInt("lv") < cardJSON.getInt("lv_max"))
                        {
                            baseId = cardJSON.getString("uniqid");
                        }
                        // 狗粮 一次喂4个 不能是已经出售了的
                        else if (!cardSellList.contains(cardJSON.getString("uniqid")) 
                                && Arrays.asList(chiari).contains(cardJSON.getString("cardid"))
                                && !cardFusionList.contains(cardJSON.getString("uniqid"))
                                && cardFusionList.size() < 4)
                        {
                            cardFusionList.add(cardJSON.getString("uniqid"));
                        }
                        
                        if (!CommonUtil.isEmpty(baseId) && cardFusionList.size() == 4)
                        {
                            break;
                        }
                    }
                    // 如果没有MR以上的卡 再挑UR喂
                    if (CommonUtil.isEmpty(baseId))
                    {
                        for (int i = 0; i < cards.size(); i++)
                        {
                            cardJSON = JSONObject.fromObject(cards.get(i));
                            
                            // 没有MR可喂的时候，找锁上的UR喂
                            if (cardJSON.getInt("lv_max") >= 50 && cardJSON.getInt("lv") < cardJSON.getInt("lv_max")
                                && 0 != cardJSON.getInt("is_lock"))
                            {
                                baseId = cardJSON.getString("uniqid");
                            }
                            // 狗粮 一次喂4个 不能是已经出售了的
                            else if (!cardSellList.contains(cardJSON.getString("uniqid")) 
                                    && Arrays.asList(chiari).contains(cardJSON.getString("cardid"))
                                    && !cardFusionList.contains(cardJSON.getString("uniqid"))
                                    && cardFusionList.size() < 4)
                            {
                                cardFusionList.add(cardJSON.getString("uniqid"));
                            }
                            
                            if (!CommonUtil.isEmpty(baseId) && cardFusionList.size() == 4)
                            {
                                break;
                            }
                        }
                    }
                    // 如果没有UR以上的卡 再挑SR喂
                    if (CommonUtil.isEmpty(baseId))
                    {
                        for (int i = 0; i < cards.size(); i++)
                        {
                            cardJSON = JSONObject.fromObject(cards.get(i));
                            
                            // 没有MR UR可喂的时候，找锁上的SR喂
                            if (cardJSON.getInt("lv_max") >= 40 && cardJSON.getInt("lv") < cardJSON.getInt("lv_max")
                                        && 0 != cardJSON.getInt("is_lock"))
                            {
                                baseId = cardJSON.getString("uniqid");
                            }
                            // 狗粮 一次喂4个 不能是已经出售了的
                            else if (!cardSellList.contains(cardJSON.getString("uniqid")) 
                                    && Arrays.asList(chiari).contains(cardJSON.getString("cardid"))
                                    && !cardFusionList.contains(cardJSON.getString("uniqid"))
                                    && cardFusionList.size() < 4)
                            {
                                cardFusionList.add(cardJSON.getString("uniqid"));
                            }
                            
                            if (!CommonUtil.isEmpty(baseId) && cardFusionList.size() == 4)
                            {
                                break;
                            }
                        }
                    }
                    
                    if (!CommonUtil.isEmpty(baseId) && cardFusionList.size() > 0)
                    {
                        map = request.cardFusion(sid, baseId, MarzUtil.listToString(cardFusionList));
                        
                        resultCode = map.get(MarzConstant.JSON_TAG_RESCODE).getInt(MarzConstant.JSON_TAG_RESCODE);
                        
                        //this.marzLogService.marzLog(account, MarzConstant.MARZ_LOG_TYPE_4, "卡片合成" + MarzUtil.resultCodeStr(resultCode));
                        
                        if (MarzConstant.RES_CODE_SUCCESS_0 == resultCode)
                        {
                            sid = map.get(MarzConstant.JSON_TAG_SID).getString(MarzConstant.JSON_TAG_SID);
                            
                            account.setCardNum(map.get(MarzConstant.JSON_TAG_CARDFUSION).getInt("card_num"));
                            account.setGold(map.get(MarzConstant.JSON_TAG_CARDFUSION).getInt("gold"));
                            
                            this.marzLogService.marzLog(account, MarzConstant.MARZ_LOG_TYPE_4, "主卡ID " + baseId + " 消耗狗粮 " + MarzUtil.listToString(cardFusionList));
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            return MarzConstant.FAILED;
        }
        
        return resultCode;
    }
    
    private int battle()
    {
        if (!validateSetting(MarzConstant.VALIDATE_SETTING_BATTLE) && account.getBp() <= 5)
        {
            return MarzConstant.SUCCESS;
        }
        
        try
        {
            // 先查询单人战斗信息
            map = request.teamBattleSoloShow(sid);
            
            resultCode = map.get(MarzConstant.JSON_TAG_RESCODE).getInt(MarzConstant.JSON_TAG_RESCODE);
            
            //this.marzLogService.marzLog(account, MarzConstant.MARZ_LOG_TYPE_1, "战斗信息查询" + MarzUtil.resultCodeStr(resultCode));
            
            if (MarzConstant.RES_CODE_SUCCESS_0 == resultCode)
            {
                sid = map.get(MarzConstant.JSON_TAG_SID).getString(MarzConstant.JSON_TAG_SID);
                
                List<MarzMapEvt> battleMapList = new ArrayList<MarzMapEvt>();
                List<MarzMapEvt> eventMapList = new ArrayList<MarzMapEvt>();
                MarzMapEvt mapEvt = new MarzMapEvt();
                
                JSONObject mapJSON;
                JSONArray bossArray;
                JSONObject bossJSON;

                String arthur1 = "";
                String arthur2 = "";
                String arthur4 = "";
                
                JSONArray battleMapNormal = map.get(MarzConstant.JSON_TAG_TEAMBATTLESOLOSHOW).getJSONArray("normal_groups");                
                JSONArray battleMapEvent = map.get(MarzConstant.JSON_TAG_TEAMBATTLESOLOSHOW).getJSONArray("event_groups");
                JSONArray arthurs = map.get(MarzConstant.JSON_TAG_TEAMBATTLESOLOSHOW).getJSONArray("arthurs");
                JSONObject arthur;
                
                // 处理4个NPC亚瑟的ID
                for (int i = 0; i < arthurs.size(); i++)
                {
                    arthur = JSONObject.fromObject(arthurs.get(i));
                    
                    if (1 == arthur.getInt("arthur_type"))
                    {
                        arthur1 = JSONObject.fromObject(arthur.getJSONArray("partners").get(0)).getString("userid");
                    }
                    else if (2 == arthur.getInt("arthur_type"))
                    {
                        arthur2 = JSONObject.fromObject(arthur.getJSONArray("partners").get(0)).getString("userid");
                    }
                    else if (3 == arthur.getInt("arthur_type"))
                    {
                        //arthur3 = JSONObject.fromObject(arthur.getJSONArray("partners").get(0)).getString("userid");
                    }
                    else if (4 == arthur.getInt("arthur_type"))
                    {
                        arthur4 = JSONObject.fromObject(arthur.getJSONArray("partners").get(0)).getString("userid");
                    }
                }
                
                // 整理当前可以战斗的BOSSID Normal
                for (int i = 0; i < battleMapNormal.size(); i++)
                {
                    mapJSON = JSONObject.fromObject(battleMapNormal.get(i));
                    
                    if (mapJSON.containsKey("bosses") && !CommonUtil.isEmpty(mapJSON.getString("bosses")))
                    {
                        bossArray = mapJSON.getJSONArray("bosses");
                        
                        for (int j = 0; j < bossArray.size(); j++)
                        {
                            bossJSON = JSONObject.fromObject(bossArray.get(j));
                            
                            mapEvt = new MarzMapEvt();
                            mapEvt.setBossId(bossJSON.getString("bossid"));
                            mapEvt.setBossName(mapJSON.getString("name") + " " + bossJSON.getString("difficulty"));
                            mapEvt.setBpCost(bossJSON.getInt("bp_use"));
                            mapEvt.setTarget(0);
                            battleMapList.add(mapEvt);
                        }
                    }
                }
                
                // 整理当前可以战斗的BOSSID Event
                for (int i = 0; i < battleMapEvent.size(); i++)
                {
                    mapJSON = JSONObject.fromObject(battleMapEvent.get(i));
                    
                    if (mapJSON.containsKey("bosses") && !CommonUtil.isEmpty(mapJSON.getString("bosses")))
                    {
                        bossArray = mapJSON.getJSONArray("bosses");
                        
                        for (int j = 0; j < bossArray.size(); j++)
                        {
                            bossJSON = JSONObject.fromObject(bossArray.get(j));
                            
                            mapEvt = new MarzMapEvt();
                            mapEvt.setBossId(bossJSON.getString("bossid"));
                            mapEvt.setBossName(mapJSON.getString("name") + " " + bossJSON.getString("difficulty"));
                            mapEvt.setBpCost(bossJSON.getInt("bp_use"));
                            mapEvt.setState(bossJSON.getString("state"));
                            
                            // 狗粮本跟每日限定要塞成0
                            if (bossJSON.getString("bossid").startsWith(MarConstant.BATTLE_START_CHIARI_HEAD)
                                    || bossJSON.getString("bossid").startsWith(MarConstant.BATTLE_START_MONDAY_HEAD)
                                    || bossJSON.getString("bossid").startsWith(MarConstant.BATTLE_START_TUESDAY_HEAD)
                                    || bossJSON.getString("bossid").startsWith(MarConstant.BATTLE_START_WEDNESDAY_HEAD)
                                    || bossJSON.getString("bossid").startsWith(MarConstant.BATTLE_START_THURSDAY_HEAD)
                                    || bossJSON.getString("bossid").startsWith(MarConstant.BATTLE_START_FRIDAY_HEAD)
                                    || bossJSON.getString("bossid").startsWith(MarConstant.BATTLE_START_SATURDAY_HEAD)
                                    || bossJSON.getString("bossid").startsWith(MarConstant.BATTLE_START_SUNDAY_HEAD))
                            {
                                mapEvt.setTarget(0);
                            }
                            // 1000W DL祭临时新增的副本
                            else if ("20001001".equals(bossJSON.getString("bossid")))
                            {
                                mapEvt.setTarget(1);
                            }
                            else
                            {
                                mapEvt.setTarget(4);
                            }
                            
                            battleMapList.add(mapEvt);
                            
                            eventMapList.add(mapEvt);
                        }
                    }
                }
                // 至此全部地图都已经处理完毕
                
                // 先初始化地图对象 后续检查是否被赋值，然后再看是否需要走一般流程
                mapEvt = new MarzMapEvt();
                
                // 如果开启了优先拿石模式 则先找没拿过石头的副本 拿石模式下不包含8开头的带锁副本
                if (validateSetting(MarzConstant.VALIDATE_SETTING_BATTLE_GET_STONE))
                {
                    // 拿石模式只处理Event副本
                    for (MarzMapEvt map : eventMapList)
                    {
                        // 首先过滤掉 8开头的钥匙副本
                        if (map.getBossId().startsWith("8"))
                        {
                            continue;
                        }
                        
                        // 如果地图的标志为标明未通过 并且体力够打这个图 那么就打这个
                        if ((MarzConstant.MARZMAP_STATE_0.equals(map.getState()) || MarzConstant.MARZMAP_STATE_1.equals(map.getState()))
                                && account.getBp() >= map.getBpCost())
                        {
                            mapEvt = map;
                            break;
                        }
                    }
                }
                
                // 这里判断上面的是否选中了需要打的图 如果没有 则走用户自定义流程
                if (CommonUtil.isEmpty(mapEvt.getBossId()))
                {
                    // 判断应该打哪张图
                    List<String> userMapList = MarzUtil.stringToList(account.getBossIds());
                    mapEvt = new MarzMapEvt();
                    
                    for (String id : userMapList)
                    {
                        for (MarzMapEvt m : battleMapList)
                        {
                            if (id.equals(m.getBossId()) && account.getBp() >= m.getBpCost())
                            {
                                mapEvt = m;
                                MarzMapReq mapReq = new MarzMapReq();
                                mapReq.setBossId(m.getBossId());
                                List<MarzMapEvt> mapList = this.marzMapService.queryMarzMap(mapReq);
                                if (mapList != null && !mapList.isEmpty())
                                {
                                    mapEvt.setTarget(mapList.get(0).getTarget());
                                    mapEvt.setEnemy(mapList.get(0).getEnemy());
                                }
                                break;
                            }
                        }
                        
                        if (!CommonUtil.isEmpty(mapEvt.getBossId()))
                        {
                            break;
                        }
                    }
                }
                
                // 没有可以打的副本时
                if (CommonUtil.isEmpty(mapEvt.getBossId()))
                {
                    // 是否启用不浪费BP
                    if (validateSetting(MarzConstant.VALIDATE_SETTING_BATTLE_NOWASTE) && (account.getBpMax() - account.getBp()) < 3)
                    {
                        // 不浪费BP打的地图为空或者为日限轮询的0时
                        if (CommonUtil.isEmpty(marzSettingEvt.getBattleNowasteBossId()) || "0".equals(marzSettingEvt.getBattleNowasteBossId()))
                        {
                            for (MarzMapEvt m : battleMapList)
                            {
                                if (MarConstant.BATTLE_START_MONDAY.equals(m.getBossId()) && account.getBp() >= m.getBpCost())
                                {
                                    mapEvt = m;
                                }
                                else if (MarConstant.BATTLE_START_TUESDAY.equals(m.getBossId()) && account.getBp() >= m.getBpCost())
                                {
                                    mapEvt = m;
                                }
                                else if (MarConstant.BATTLE_START_WEDNESDAY.equals(m.getBossId()) && account.getBp() >= m.getBpCost())
                                {
                                    mapEvt = m;
                                }
                                else if (MarConstant.BATTLE_START_THURSDAY.equals(m.getBossId()) && account.getBp() >= m.getBpCost())
                                {
                                    mapEvt = m;
                                }
                                else if (MarConstant.BATTLE_START_FRIDAY.equals(m.getBossId()) && account.getBp() >= m.getBpCost())
                                {
                                    mapEvt = m;
                                }
                                else if (MarConstant.BATTLE_START_SATURDAY.equals(m.getBossId()) && account.getBp() >= m.getBpCost())
                                {
                                    mapEvt = m;
                                }
                                else if (MarConstant.BATTLE_START_SUNDAY.equals(m.getBossId()) && account.getBp() >= m.getBpCost())
                                {
                                    mapEvt = m;
                                }
                            }
                        }
                        else
                        {
                            for (MarzMapEvt m : battleMapList)
                            {
                                if (marzSettingEvt.getBattleNowasteBossId().equals(m.getBossId()) && account.getBp() >= m.getBpCost())
                                {
                                    mapEvt = m;
                                }
                            }
                        }
                    }
                    else
                    {
                        return MarzConstant.SUCCESS;
                    }
                }
                
                // 校验数据
                if (!CommonUtil.isEmpty(mapEvt.getBossId()))
                {
                    map = request.teamBattleSoloStart(sid, mapEvt.getBossId(), arthur1, arthur2, arthur4);
                    
                    resultCode = map.get(MarzConstant.JSON_TAG_RESCODE).getInt(MarzConstant.JSON_TAG_RESCODE);
                    
                    this.marzLogService.marzLog(account, MarzConstant.MARZ_LOG_TYPE_1, "战斗开始" + MarzUtil.resultCodeStr(resultCode) + " 目标副本 " + mapEvt.getBossName());
                    
                    if (MarzConstant.RES_CODE_SUCCESS_0 == resultCode)
                    {
                        sid = map.get(MarzConstant.JSON_TAG_SID).getString(MarzConstant.JSON_TAG_SID);
                        
                        String battleEndParam = "";
                        if (0 == mapEvt.getTarget())
                        {
                            battleEndParam = MarConstant.BATTLESOLOEND_3;
                        }
                        else if (1 == mapEvt.getTarget())
                        {
                            battleEndParam = MarConstant.BATTLESOLOEND_1_1;
                        }
                        else
                        {
                            if (MarzConstant.MARZ_ACCOUNT_VIP_0.equals(account.getVip()))
                            {
                                battleEndParam = MarConstant.BATTLESOLOEND_1_2;
                            }
                            else if (MarzConstant.MARZ_ACCOUNT_VIP_1.equals(account.getVip()))
                            {
                                battleEndParam = MarConstant.BATTLESOLOEND_1_3;
                            }
                            else if (MarzConstant.MARZ_ACCOUNT_VIP_2.equals(account.getVip()))
                            {
                                battleEndParam = MarConstant.BATTLESOLOEND_1_4;
                            }
                            else if (MarzConstant.MARZ_ACCOUNT_VIP_3.equals(account.getVip()))
                            {
                                battleEndParam = MarConstant.BATTLESOLOEND_1_5;
                            }
                        }
                        
                        Thread.sleep(MarzConstant.SLEEPTIME_BATTLE_SOLO);
                        
                        map = request.teamBattleSoloEnd(sid, battleEndParam);
                        
                        resultCode = map.get(MarzConstant.JSON_TAG_RESCODE).getInt(MarzConstant.JSON_TAG_RESCODE);
                        
                        this.marzLogService.marzLog(account, MarzConstant.MARZ_LOG_TYPE_1, "战斗结束" + MarzUtil.resultCodeStr(resultCode) + " 目标副本 " + mapEvt.getBossName());
                        
                        if (MarzConstant.RES_CODE_SUCCESS_0 == resultCode)
                        {
                            sid = map.get(MarzConstant.JSON_TAG_SID).getString(MarzConstant.JSON_TAG_SID);
                            
                            JSONObject user = map.get(MarzConstant.JSON_TAG_TEAMBATTLESOLOEND);
                            account.setAp(user.getJSONObject("user").getInt("ap"));
                            account.setApMax(user.getJSONObject("user").getInt("ap_max"));
                            account.setBp(user.getJSONObject("user").getInt("bp"));
                            account.setBpMax(user.getJSONObject("user").getInt("bp_max"));
                            account.setCardMax(user.getJSONObject("user").getInt("card_max"));
                            account.setCardNum(user.getJSONObject("user").getInt("card_num"));
                            account.setCoin(user.getJSONObject("user").getInt("coin") + user.getJSONObject("user").getInt("coin_free"));
                            account.setFp(user.getJSONObject("user").getInt("fp"));
                            account.setGold(user.getJSONObject("user").getInt("gold"));
                            account.setLv(user.getJSONObject("user").getInt("lv"));
                            account.setName(user.getJSONObject("user").getString("name"));
                            account.setUserId(user.getJSONObject("user").getString("userid"));
                        }
                    }
                }
                else
                {
                    this.marzLogService.marzLog(account, MarzConstant.MARZ_LOG_TYPE_1, account.getTgksId() + "当前时间没有找到适合战斗的副本，返回并等待BP恢复！");
                }
            }
        }
        catch (Exception e)
        {
            CommonUtil.infoLog(logger, CommonConstant.SYSTEM_INFO_LOG_METHOD_OUT, String.format(account.getTgksId() + "战斗过程中发生异常！"));
            return MarzConstant.FAILED;
        }
        
        return resultCode;
    }
    
    private int pvp()
    {
        if (!validateSetting(MarzConstant.VALIDATE_SETTING_PVP))
        {
            return MarzConstant.SUCCESS;
        }
        
        if (this.deckMap.size() < 4)
        {
            this.marzLogService.marzLog(account, MarzConstant.MARZ_LOG_TYPE_6, "四职业卡组没有配齐，请先设定好四职业卡组...");
            return MarzConstant.SUCCESS;
        }
        
        try
        {
            // 先调用pvpShow
            map = request.pvpShow(sid);
            resultCode = map.get(MarzConstant.JSON_TAG_RESCODE).getInt(MarzConstant.JSON_TAG_RESCODE);
            
            if (MarzConstant.RES_CODE_SUCCESS_0 == resultCode)
            {
                sid = map.get(MarzConstant.JSON_TAG_SID).getString(MarzConstant.JSON_TAG_SID);
                
                // 看剩余次数是否>0
                JSONObject pvpShow = map.get(MarzConstant.JSON_TAG_PVPSHOW);
                
                if (pvpShow.getInt("challenge") < 1)
                {
                    // 次数小于1时直接返回
                    return MarzConstant.SUCCESS;
                }
                
                // 调用missionShow 查看本日PVP任务该使用哪个职业
                map = request.missionShow(sid);
                resultCode = map.get(MarzConstant.JSON_TAG_RESCODE).getInt(MarzConstant.JSON_TAG_RESCODE);
                
                if (MarzConstant.RES_CODE_SUCCESS_0 == resultCode)
                {
                    sid = map.get(MarzConstant.JSON_TAG_SID).getString(MarzConstant.JSON_TAG_SID);
                    
                    // 处理报文整理为mission的List
                    JSONObject missionShow = map.get(MarzConstant.JSON_TAG_MISSIONSHOW);
                    JSONArray missions = missionShow.getJSONArray("missions");
                    List<MissionEvt> missionList = new ArrayList<MissionEvt>();
                    JSONObject missionJSON;
                    
                    for (int i = 0, size = missions.size(); i < size; i++)
                    {
                        missionJSON = JSONObject.fromObject(missions.get(i));
                        missionList.add(new MissionEvt(missionJSON));
                    }
                    
                    // 这里选出PVP的主职业
                    String arthur_type = "1";
                    for (MissionEvt m : missionList)
                    {
                        if ("2003001".equals(m.getMissionid()) && "0".equals(m.getState()))
                        {
                            arthur_type = "1";
                            break;
                        }
                        else if ("2003002".equals(m.getMissionid()) && "0".equals(m.getState()))
                        {
                            arthur_type = "2";
                            break;
                        }
                        else if ("2003003".equals(m.getMissionid()) && "0".equals(m.getState()))
                        {
                            arthur_type = "3";
                            break;
                        }
                        else if ("2003004".equals(m.getMissionid()) && "0".equals(m.getState()))
                        {
                            arthur_type = "4";
                            break;
                        }
                    }
                    
                    map = this.request.pvpStart(sid, arthur_type, deckMap);
                    resultCode = map.get(MarzConstant.JSON_TAG_RESCODE).getInt(MarzConstant.JSON_TAG_RESCODE);
                    
                    if (MarzConstant.RES_CODE_SUCCESS_0 == resultCode)
                    {
                        sid = map.get(MarzConstant.JSON_TAG_SID).getString(MarzConstant.JSON_TAG_SID);
                        
                        JSONObject pvpStart = map.get(MarzConstant.JSON_TAG_PVPSTART);
                        // 这个是用来发送结束报文用的 很重要
                        String btluid = pvpStart.getString("btluid");
                        String enemy = pvpStart.getJSONObject("enemy_info").getString("name");
                        
                        this.marzLogService.marzLog(account, MarzConstant.MARZ_LOG_TYPE_6, "斗技场PVP战斗开始，对手为：" + enemy);
                        
                        Thread.sleep(MarzConstant.SLEEPTIME_BATTLE_SOLO);
                        
                        map = this.request.pvpEnd(sid, btluid, pvpEndMap.get(arthur_type));
                        resultCode = map.get(MarzConstant.JSON_TAG_RESCODE).getInt(MarzConstant.JSON_TAG_RESCODE);
                        
                        if (MarzConstant.RES_CODE_SUCCESS_0 == resultCode)
                        {
                            this.marzLogService.marzLog(account, MarzConstant.MARZ_LOG_TYPE_6, "斗技场PVP战斗结束，战斗结果：胜利");
                            
                            sid = map.get(MarzConstant.JSON_TAG_SID).getString(MarzConstant.JSON_TAG_SID);
                            
                            JSONObject pvpEnd = map.get(MarzConstant.JSON_TAG_RESCODE);
                            
                            if (JSONObject.fromObject(pvpEnd.getJSONArray("notification").get(0)).getInt("mission_clear_receive") > 0)
                            {
                                // 如果可回收的任务奖励大于0时 再次调用任务请求
                                map = request.missionShow(sid);
                                resultCode = map.get(MarzConstant.JSON_TAG_RESCODE).getInt(MarzConstant.JSON_TAG_RESCODE);
                                
                                if (MarzConstant.RES_CODE_SUCCESS_0 == resultCode)
                                {
                                    sid = map.get(MarzConstant.JSON_TAG_SID).getString(MarzConstant.JSON_TAG_SID);
                                    
                                    // 处理报文整理为mission的List
                                    missionShow = map.get(MarzConstant.JSON_TAG_MISSIONSHOW);
                                    missions = missionShow.getJSONArray("missions");
                                    missionList = new ArrayList<MissionEvt>();
                                    
                                    for (int i = 0, size = missions.size(); i < size; i++)
                                    {
                                        missionJSON = JSONObject.fromObject(missions.get(i));
                                        missionList.add(new MissionEvt(missionJSON));
                                    }
                                    
                                    for (MissionEvt m : missionList)
                                    {
                                        // 这里过滤任务只做收取任务奖励使用
                                        if ("1".equals(m.getState()))
                                        {
                                            map = this.request.missionReward(sid, m.getMissionid());
                                            sid = map.get(MarzConstant.JSON_TAG_SID).getString(MarzConstant.JSON_TAG_SID);
                                            this.marzLogService.marzLog(account, MarzConstant.MARZ_LOG_TYPE_0, "收取任务奖励：" + m.getTitle());
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        catch (Exception e)
        {
            this.marzLogService.marzLog(account, MarzConstant.MARZ_LOG_TYPE_0, "PVP执行失败...");
        }
        
        return resultCode;
    }
    
    /**
     * 
     * @Title: validateSetting
     * @Description: 校验开关是否开启
     * @param settingTag
     * @return
     * @return boolean 返回类型
     * @throws
     */
    private boolean validateSetting(int settingTag)
    {
        switch (settingTag)
        {
            case MarzConstant.VALIDATE_SETTING_EXPLORE: // 自动跑图开关
                return MarzConstant.MARZSETTING_ON.equals(marzSettingEvt.getExplore());
            case MarzConstant.VALIDATE_SETTING_CARDSELL: // 自动卖卡开关
                return MarzConstant.MARZSETTING_ON.equals(marzSettingEvt.getCardSell());
            case MarzConstant.VALIDATE_SETTING_CARDSELL_COMMON: // 自动卖卡开关-卖普通卡
                return MarzConstant.MARZSETTING_ON.equals(marzSettingEvt.getCardSellCommon());
            case MarzConstant.VALIDATE_SETTING_CHIARIFUSION: // 自动喂狗粮开关
                return MarzConstant.MARZSETTING_ON.equals(marzSettingEvt.getCardFusion());
            case MarzConstant.VALIDATE_SETTING_BATTLE: // 自动战斗开关
                return MarzConstant.MARZSETTING_ON.equals(marzSettingEvt.getBattle());
            case MarzConstant.VALIDATE_SETTING_BATTLE_NOWASTE: // 自动战斗开关-不浪费BP
                return MarzConstant.MARZSETTING_ON.equals(marzSettingEvt.getBattleNowaste());
            case MarzConstant.VALIDATE_SETTING_BATTLE_GET_STONE: // 优先拿石开关
                return MarzConstant.MARZSETTING_ON.equals(marzSettingEvt.getBattleGetStone());
            case MarzConstant.VALIDATE_SETTING_PVP: // PVP开关
                return MarzConstant.MARZSETTING_ON.equals(marzSettingEvt.getPvp());
            case MarzConstant.VALIDATE_SETTING_FAMEFUSION: // 自动名声合成开关
                return MarzConstant.MARZSETTING_ON.equals(marzSettingEvt.getFameFusion());
            case MarzConstant.VALIDATE_SETTING_COINGACHA: // 抽硬币
                return MarzConstant.MARZSETTING_ON.equals(marzSettingEvt.getFameFusion());
            case MarzConstant.VALIDATE_SETTING_AUTOUSEBPPOTION: // 自动喝药
                return MarzConstant.MARZSETTING_ON.equals(marzSettingEvt.getFameFusion());
            case MarzConstant.VALIDATE_SETTING_AUTOBUYBPPOTION: // 自动买药
                return MarzConstant.MARZSETTING_ON.equals(marzSettingEvt.getFameFusion());
            default:
                return false;
        }
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException
    {
        ContextUtil.setApplicationContext(applicationContext);
    }

    public static Log getLogger()
    {
        return logger;
    }

    public static void setLogger(Log logger)
    {
        MarzTaskDiffusion.logger = logger;
    }
}
