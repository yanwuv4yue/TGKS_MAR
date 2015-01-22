package com.moemao.tgks.mar.marzaccount.action;

import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.moemao.tgks.common.core.action.TGKSAction;
import com.moemao.tgks.common.tool.CommonConstant;
import com.moemao.tgks.common.tool.CommonUtil;
import com.moemao.tgks.common.tool.StringUtil;
import com.moemao.tgks.mar.marz.tool.MarzConstant;
import com.moemao.tgks.mar.marz.tool.MarzUtil;
import com.moemao.tgks.mar.marzaccount.entity.MarzAccountEvt;
import com.moemao.tgks.mar.marzaccount.entity.MarzAccountReq;
import com.moemao.tgks.mar.marzaccount.service.MarzAccountService;
import com.moemao.tgks.mar.marzmap.entity.MarzMapEvt;
import com.moemao.tgks.mar.marzmap.entity.MarzMapReq;
import com.moemao.tgks.mar.marzmap.service.MarzMapService;

public class MarzAccountAction extends TGKSAction
{

    /** 
     * @Fields serialVersionUID 
     */ 
    private static final long serialVersionUID = -8618477052861281198L;

    private static Log logger = LogFactory.getLog(MarzAccountAction.class);
    
    /**
     * ﻿MarzAccount业务接口
     */
    private MarzAccountService mar_marzAccountService;
    
    private MarzMapService mar_marzMapService;
    
    /**
     * 查询结果集
     */
    private List<MarzAccountEvt> list;
    
    /**
     * ﻿MarzAccountEvt对象
     */
    private MarzAccountEvt marzAccountEvt;
    
    /**
     * ﻿MarzAccount查询条件封装对象（自动生成代码后需要new对象）
     */
    private MarzAccountReq marzAccountReq = new MarzAccountReq();
    
    public String marzAccountManager()
    {
    return SUCCESS;
    }
    
    public String queryMarzAccount()
    {
        list = mar_marzAccountService.queryMarzAccount(marzAccountReq);
        return SUCCESS;
    }
    
    public String queryMarzAccountByTgksId()
    {
        MarzAccountReq marzAccountReq = new MarzAccountReq();
        marzAccountReq.setTgksId(CommonUtil.getUserInfoBySession().getUsername());
        list = mar_marzAccountService.queryMarzAccount(marzAccountReq);
        
        if (CommonUtil.isEmpty(list))
        {
            return ERROR;
        }
        else
        {
            marzAccountEvt = list.get(0);
        }
        
        // 处理设置的战斗地图
        String battleMapName = "";
        List<MarzMapEvt> mapList = this.mar_marzMapService.queryMarzMap(new MarzMapReq());
        for (String mapId : MarzUtil.stringToList(marzAccountEvt.getBossIds()))
        {
            for (MarzMapEvt map : mapList)
            {
                if (mapId.equals(map.getBossId()))
                {
                    battleMapName += "[" + map.getBossName() + "] ";
                }
            }
        }
        marzAccountEvt.setBossIds(battleMapName);
        
        return SUCCESS;
    }
    
    /**
     * 
     * @Title: marzAccountBinding
     * @Description: TGKS登录后绑定账号
     * @return
     * @return String 返回类型
     * @throws
     */
    public String marzAccountBinding()
    {
        String accountId = this.getRequest().getParameter("accountId");
        MarzAccountEvt account = mar_marzAccountService.queryMarzAccountById(accountId);
        
        // 如果不存在该账号 或者该账号已经绑定
        if (CommonUtil.isEmpty(account) || !CommonUtil.isEmpty(account.getTgksId()))
        {
            return ERROR;
        }
        
        account.setTgksId(CommonUtil.getUserInfoBySession().getUsername());
        mar_marzAccountService.updateMarzAccount(account);
        
        return SUCCESS;
    }
    
    public String accountOnline()
    {
        MarzAccountReq marzAccountReq = new MarzAccountReq();
        marzAccountReq.setTgksId(CommonUtil.getUserInfoBySession().getUsername());
        list = mar_marzAccountService.queryMarzAccount(marzAccountReq);
        
        if (CommonUtil.isEmpty(list))
        {
            return ERROR;
        }
        else
        {
            marzAccountEvt = list.get(0);
            marzAccountEvt.setStatus(MarzConstant.MARZ_ACCOUNT_STATUS_1);
            mar_marzAccountService.updateMarzAccount(marzAccountEvt);
        }
        
        return SUCCESS;
    }
    
    public String accountOffline()
    {
        MarzAccountReq marzAccountReq = new MarzAccountReq();
        marzAccountReq.setTgksId(CommonUtil.getUserInfoBySession().getUsername());
        list = mar_marzAccountService.queryMarzAccount(marzAccountReq);
        
        if (CommonUtil.isEmpty(list))
        {
            return ERROR;
        }
        else
        {
            marzAccountEvt = list.get(0);
            marzAccountEvt.setStatus(MarzConstant.MARZ_ACCOUNT_STATUS_0);
            marzAccountEvt.setSessionId("");
            mar_marzAccountService.updateMarzAccount(marzAccountEvt);
        }
        
        return SUCCESS;
    }
    
    public String editMarzAccountPage()
    {
        String id = this.getRequest().getParameter("id");
        if (!CommonUtil.isEmpty(id))
        {
        marzAccountEvt = mar_marzAccountService.queryMarzAccountById(id);
        }
        return SUCCESS;
    }
    
    public String editMarzAccount()
    {
        CommonUtil.debugLog(logger, CommonConstant.SYSTEM_INFO_LOG_METHOD_IN, "MarzAccountAction.updateMarzAccount");
        int result = 0;
        if (CommonUtil.isEmpty(marzAccountEvt.getId()))
        {
        result = mar_marzAccountService.addMarzAccount(marzAccountEvt);
        CommonUtil.systemLog("mar/editMarzAccount.action", CommonConstant.SYSTEMLOG_TYPE_1, result == 0 ? CommonConstant.FAILD : CommonConstant.SUCCESS, String.format("新增marzAccountEvt\n%S", marzAccountEvt.toString()));
        }
        else
        {
        result = mar_marzAccountService.updateMarzAccount(marzAccountEvt);
        CommonUtil.systemLog("mar/editMarzAccount.action", CommonConstant.SYSTEMLOG_TYPE_2, result == 0 ? CommonConstant.FAILD : CommonConstant.SUCCESS, String.format("修改marzAccountEvt\n%S", marzAccountEvt.toString()));
        }
        CommonUtil.infoLog(logger, CommonConstant.SYSTEM_INFO_LOG_METHOD_EXECUTE_NUMS, StringUtil.toBeString(result));
        CommonUtil.debugLog(logger, CommonConstant.SYSTEM_INFO_LOG_METHOD_OUT, "MarzAccountAction.updateMarzAccount");
        return SUCCESS;
    }
    
    public String deleteMarzAccount()
    {
    CommonUtil.debugLog(logger, CommonConstant.SYSTEM_INFO_LOG_METHOD_IN, "MarzAccountAction.deleteMarzAccount");
    String ids = this.getRequest().getParameter("ids");
    int result = mar_marzAccountService.deleteMarzAccount(CommonUtil.stringToList(ids));
    CommonUtil.systemLog("mar/deleteMarzAccount.action", CommonConstant.SYSTEMLOG_TYPE_3, result == 0 ? CommonConstant.FAILD : CommonConstant.SUCCESS, String.format("删除marzAccountEvt\nID:%S", ids));
    CommonUtil.infoLog(logger, CommonConstant.SYSTEM_INFO_LOG_METHOD_EXECUTE_NUMS, StringUtil.toBeString(result));
    CommonUtil.debugLog(logger, CommonConstant.SYSTEM_INFO_LOG_METHOD_OUT, "MarzAccountAction.deleteMarzAccount");
    return SUCCESS;
    }
    
    /**
     * @return 返回 mar_marzAccountService
     */
    public MarzAccountService getMar_marzAccountService()
    {
        return mar_marzAccountService;
    }
    
    /**
     * @param 对mar_marzAccountService进行赋值
     */
    public void setMar_marzAccountService(MarzAccountService mar_marzAccountService)
    {
        this.mar_marzAccountService = mar_marzAccountService;
    }
    
    /**
     * @return 返回 list
     */
    public List<MarzAccountEvt> getList()
    {
        return list;
    }
    
    /**
     * @param 对list进行赋值
     */
    public void setList(List<MarzAccountEvt> list)
    {
        this.list = list;
    }
    
    /**
     * @return 返回 marzAccountEvt
     */
    public MarzAccountEvt getMarzAccountEvt()
    {
        return marzAccountEvt;
    }
    
    /**
     * @param 对marzAccountEvt进行赋值
     */
    public void setMarzAccountEvt(MarzAccountEvt marzAccountEvt)
    {
        this.marzAccountEvt = marzAccountEvt;
    }
    
    /**
     * @return 返回 marzAccountReq
     */
    public MarzAccountReq getMarzAccountReq()
    {
        return marzAccountReq;
    }
    
    /**
     * @param 对marzAccountReq进行赋值
     */
    public void setMarzAccountReq(MarzAccountReq marzAccountReq)
    {
        this.marzAccountReq = marzAccountReq;
    }

    public MarzMapService getMar_marzMapService()
    {
        return mar_marzMapService;
    }

    public void setMar_marzMapService(MarzMapService mar_marzMapService)
    {
        this.mar_marzMapService = mar_marzMapService;
    }

}