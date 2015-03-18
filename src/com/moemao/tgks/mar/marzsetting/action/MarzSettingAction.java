package com.moemao.tgks.mar.marzsetting.action;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.moemao.tgks.common.core.action.TGKSAction;
import com.moemao.tgks.common.tool.CommonConstant;
import com.moemao.tgks.common.tool.CommonUtil;
import com.moemao.tgks.common.tool.StringUtil;
import com.moemao.tgks.mar.marz.tool.MarzConstant;
import com.moemao.tgks.mar.marzaccount.entity.MarzAccountEvt;
import com.moemao.tgks.mar.marzaccount.entity.MarzAccountReq;
import com.moemao.tgks.mar.marzaccount.service.MarzAccountService;
import com.moemao.tgks.mar.marzmap.entity.MarzMapEvt;
import com.moemao.tgks.mar.marzmap.entity.MarzMapReq;
import com.moemao.tgks.mar.marzmap.service.MarzMapService;
import com.moemao.tgks.mar.marzsetting.entity.MarzSettingEvt;
import com.moemao.tgks.mar.marzsetting.entity.MarzSettingReq;
import com.moemao.tgks.mar.marzsetting.service.MarzSettingService;

public class MarzSettingAction extends TGKSAction
{

    /** 
     * @Fields serialVersionUID
     */ 
    private static final long serialVersionUID = 4112798397731281034L;

    private static Log logger = LogFactory.getLog(MarzSettingAction.class);
    
    /**
     * ﻿MarzSetting业务接口
     */
    private MarzSettingService mar_marzSettingService;

    private MarzAccountService mar_marzAccountService;
    
    private MarzMapService mar_marzMapService;
    
    /**
     * 查询结果集
     */
    private List<MarzSettingEvt> list;
    
    List<MarzMapEvt> allMapList;
    
    MarzAccountEvt account;
    
    /**
     * ﻿MarzSettingEvt对象
     */
    private MarzSettingEvt marzSettingEvt = new MarzSettingEvt();
    
    /**
     * ﻿MarzSetting查询条件封装对象（自动生成代码后需要new对象）
     */
    private MarzSettingReq marzSettingReq = new MarzSettingReq();
    
    public String marzSettingManager()
    {
    return SUCCESS;
    }
    
    public String queryMarzSetting()
    {
    list = mar_marzSettingService.queryMarzSetting(marzSettingReq);
    return SUCCESS;
    }
    
    public String editMarzSettingPage()
    {
    String id = this.getRequest().getParameter("id");
    if (!CommonUtil.isEmpty(id))
    {
    marzSettingEvt = mar_marzSettingService.queryMarzSettingById(id);
    }
    return SUCCESS;
    }
    
    public String editMarzSetting()
    {
    CommonUtil.debugLog(logger, CommonConstant.SYSTEM_INFO_LOG_METHOD_IN, "MarzSettingAction.updateMarzSetting");
    int result = 0;
    if (CommonUtil.isEmpty(marzSettingEvt.getId()))
    {
    result = mar_marzSettingService.addMarzSetting(marzSettingEvt);
    CommonUtil.systemLog("mar/editMarzSetting.action", CommonConstant.SYSTEMLOG_TYPE_1, result == 0 ? CommonConstant.FAILD : CommonConstant.SUCCESS, String.format("新增marzSettingEvt\n%S", marzSettingEvt.toString()));
    }
    else
    {
    result = mar_marzSettingService.updateMarzSetting(marzSettingEvt);
    CommonUtil.systemLog("mar/editMarzSetting.action", CommonConstant.SYSTEMLOG_TYPE_2, result == 0 ? CommonConstant.FAILD : CommonConstant.SUCCESS, String.format("修改marzSettingEvt\n%S", marzSettingEvt.toString()));
    }
    CommonUtil.infoLog(logger, CommonConstant.SYSTEM_INFO_LOG_METHOD_EXECUTE_NUMS, StringUtil.toBeString(result));
    CommonUtil.debugLog(logger, CommonConstant.SYSTEM_INFO_LOG_METHOD_OUT, "MarzSettingAction.updateMarzSetting");
    return SUCCESS;
    }
    
    public String deleteMarzSetting()
    {
    CommonUtil.debugLog(logger, CommonConstant.SYSTEM_INFO_LOG_METHOD_IN, "MarzSettingAction.deleteMarzSetting");
    String ids = this.getRequest().getParameter("ids");
    int result = mar_marzSettingService.deleteMarzSetting(CommonUtil.stringToList(ids));
    CommonUtil.systemLog("mar/deleteMarzSetting.action", CommonConstant.SYSTEMLOG_TYPE_3, result == 0 ? CommonConstant.FAILD : CommonConstant.SUCCESS, String.format("删除marzSettingEvt\nID:%S", ids));
    CommonUtil.infoLog(logger, CommonConstant.SYSTEM_INFO_LOG_METHOD_EXECUTE_NUMS, StringUtil.toBeString(result));
    CommonUtil.debugLog(logger, CommonConstant.SYSTEM_INFO_LOG_METHOD_OUT, "MarzSettingAction.deleteMarzSetting");
    return SUCCESS;
    }
    
    public String settingPage()
    {
        String tgksId = CommonUtil.getUserInfoBySession().getUsername();
        marzSettingEvt = new MarzSettingEvt();
        marzSettingEvt.setTgksId(tgksId);
        marzSettingReq = new MarzSettingReq();
        marzSettingReq.setTgksId(tgksId);
        
        list = this.mar_marzSettingService.queryMarzSetting(marzSettingReq);
        
        if (!CommonUtil.isEmpty(list))
        {
            for (MarzSettingEvt setting : list)
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
                else if (MarzConstant.VALIDATE_SETTING_CARDFUSION == Integer.parseInt(setting.getName()))
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
            }
        }
        
        // 查询当前的战斗地图
        allMapList = this.mar_marzMapService.queryMarzMap(new MarzMapReq());
        
        MarzAccountReq marzAccountReq = new MarzAccountReq();
        marzAccountReq.setTgksId(tgksId);
        account = this.mar_marzAccountService.queryMarzAccount(marzAccountReq).get(0);
        
        for (MarzMapEvt map : allMapList)
        {
            if (!CommonUtil.isEmpty(account.getBossIds()))
            {
                if (account.getBossIds().contains(map.getBossId()))
                {
                    map.setCheck("1");
                }
            }
            
            if (Integer.parseInt(account.getVip()) < Integer.parseInt(map.getVip()))
            {
                map.setShow("0");
            }
            else
            {
                map.setShow("1");
            }
        }
        
        return SUCCESS;
    }
    
    public String saveSetting()
    {
        String tgksId = CommonUtil.getUserInfoBySession().getUsername();
        
        // 先删除原来的设定
        this.mar_marzSettingService.deleteMarzSettingByTgksId(tgksId);
        
        // 保存战斗地图ID
        MarzAccountReq marzAccountReq = new MarzAccountReq();
        marzAccountReq.setTgksId(tgksId);
        account = this.mar_marzAccountService.queryMarzAccount(marzAccountReq).get(0);
        if (null == marzSettingEvt.getBossIds())
        {
            account.setBossIds("");
        }
        else
        {
            account.setBossIds(marzSettingEvt.getBossIds().replace(" ", ""));
        }
        this.mar_marzAccountService.updateMarzAccount(account);
        
        // 开始保存新设定
        // 跑图
        MarzSettingEvt setting = new MarzSettingEvt();
        setting.setTgksId(tgksId);
        setting.setType(MarzConstant.MARZSETTING_TYPE_0);
        setting.setName(String.valueOf(MarzConstant.VALIDATE_SETTING_EXPLORE));
        setting.setValue(marzSettingEvt.getExplore());
        this.mar_marzSettingService.addMarzSetting(setting);
        
        // 卖卡
        setting = new MarzSettingEvt();
        setting.setTgksId(tgksId);
        setting.setType(MarzConstant.MARZSETTING_TYPE_0);
        setting.setName(String.valueOf(MarzConstant.VALIDATE_SETTING_CARDSELL));
        setting.setValue(marzSettingEvt.getCardSell());
        this.mar_marzSettingService.addMarzSetting(setting);
        
        // 卖卡 普通卡
        setting = new MarzSettingEvt();
        setting.setTgksId(tgksId);
        setting.setType(MarzConstant.MARZSETTING_TYPE_0);
        setting.setName(String.valueOf(MarzConstant.VALIDATE_SETTING_CARDSELL_COMMON));
        setting.setValue(marzSettingEvt.getCardSellCommon());
        this.mar_marzSettingService.addMarzSetting(setting);
        
        // 喂狗粮
        setting = new MarzSettingEvt();
        setting.setTgksId(tgksId);
        setting.setType(MarzConstant.MARZSETTING_TYPE_0);
        setting.setName(String.valueOf(MarzConstant.VALIDATE_SETTING_CARDFUSION));
        setting.setValue(marzSettingEvt.getCardFusion());
        this.mar_marzSettingService.addMarzSetting(setting);
        
        // 战斗
        setting = new MarzSettingEvt();
        setting.setTgksId(tgksId);
        setting.setType(MarzConstant.MARZSETTING_TYPE_0);
        setting.setName(String.valueOf(MarzConstant.VALIDATE_SETTING_BATTLE));
        setting.setValue(marzSettingEvt.getBattle());
        this.mar_marzSettingService.addMarzSetting(setting);
        
        // 战斗 不浪费BP
        setting = new MarzSettingEvt();
        setting.setTgksId(tgksId);
        setting.setType(MarzConstant.MARZSETTING_TYPE_0);
        setting.setName(String.valueOf(MarzConstant.VALIDATE_SETTING_BATTLE_NOWASTE));
        setting.setValue(marzSettingEvt.getBattleNowaste());
        this.mar_marzSettingService.addMarzSetting(setting);
        
        // 战斗 不浪费BP地图ID
        setting = new MarzSettingEvt();
        setting.setTgksId(tgksId);
        setting.setType(MarzConstant.MARZSETTING_TYPE_1);
        setting.setName(String.valueOf(MarzConstant.VALIDATE_SETTING_BATTLE_NOWASTE_BOSSID));
        setting.setValue(marzSettingEvt.getBattleNowasteBossId());
        this.mar_marzSettingService.addMarzSetting(setting);
        
        return SUCCESS;
    }
    
    /**
     * @return 返回 mar_marzSettingService
     */
    public MarzSettingService getMar_marzSettingService()
    {
        return mar_marzSettingService;
    }
    
    /**
     * @param 对mar_marzSettingService进行赋值
     */
    public void setMar_marzSettingService(MarzSettingService mar_marzSettingService)
    {
        this.mar_marzSettingService = mar_marzSettingService;
    }
    
    /**
     * @return 返回 list
     */
    public List<MarzSettingEvt> getList()
    {
        return list;
    }
    
    /**
     * @param 对list进行赋值
     */
    public void setList(List<MarzSettingEvt> list)
    {
        this.list = list;
    }
    
    /**
     * @return 返回 marzSettingEvt
     */
    public MarzSettingEvt getMarzSettingEvt()
    {
        return marzSettingEvt;
    }
    
    /**
     * @param 对marzSettingEvt进行赋值
     */
    public void setMarzSettingEvt(MarzSettingEvt marzSettingEvt)
    {
        this.marzSettingEvt = marzSettingEvt;
    }
    
    /**
     * @return 返回 marzSettingReq
     */
    public MarzSettingReq getMarzSettingReq()
    {
        return marzSettingReq;
    }
    
    /**
     * @param 对marzSettingReq进行赋值
     */
    public void setMarzSettingReq(MarzSettingReq marzSettingReq)
    {
        this.marzSettingReq = marzSettingReq;
    }

    public MarzAccountService getMar_marzAccountService()
    {
        return mar_marzAccountService;
    }

    public void setMar_marzAccountService(MarzAccountService mar_marzAccountService)
    {
        this.mar_marzAccountService = mar_marzAccountService;
    }

    public MarzMapService getMar_marzMapService()
    {
        return mar_marzMapService;
    }

    public void setMar_marzMapService(MarzMapService mar_marzMapService)
    {
        this.mar_marzMapService = mar_marzMapService;
    }

    public List<MarzMapEvt> getAllMapList()
    {
        return allMapList;
    }

    public void setAllMapList(List<MarzMapEvt> allMapList)
    {
        this.allMapList = allMapList;
    }

    public MarzAccountEvt getAccount()
    {
        return account;
    }

    public void setAccount(MarzAccountEvt account)
    {
        this.account = account;
    }

}