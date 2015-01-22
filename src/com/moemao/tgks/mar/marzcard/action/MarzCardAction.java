package com.moemao.tgks.mar.marzcard.action;

import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import com.moemao.tgks.common.core.action.TGKSAction;
import com.moemao.tgks.common.tool.CommonConstant;
import com.moemao.tgks.common.tool.CommonUtil;
import com.moemao.tgks.common.tool.StringUtil;
import com.moemao.tgks.mar.marz.tool.MarzConstant;
import com.moemao.tgks.mar.marzcard.entity.MarzCardEvt;
import com.moemao.tgks.mar.marzcard.entity.MarzCardReq;
import com.moemao.tgks.mar.marzcard.service.MarzCardService;

public class MarzCardAction extends TGKSAction
{

    /** 
     * @Fields serialVersionUID
     */ 
    private static final long serialVersionUID = 4801506369257113522L;

private static Log logger = LogFactory.getLog(MarzCardAction.class);

/**
 * ﻿MarzCard业务接口
 */
private MarzCardService mar_marzCardService;

/**
 * 查询结果集
 */
private List<MarzCardEvt> list;

/**
 * ﻿MarzCardEvt对象
 */
private MarzCardEvt marzCardEvt;

/**
 * ﻿MarzCard查询条件封装对象（自动生成代码后需要new对象）
 */
private MarzCardReq marzCardReq = new MarzCardReq();

private String marzCardExport;

public String marzCardManager()
{
return SUCCESS;
}

public String queryMarzCard()
{
list = mar_marzCardService.queryMarzCard(marzCardReq);
return SUCCESS;
}

public String editMarzCardPage()
{
String id = this.getRequest().getParameter("id");
if (!CommonUtil.isEmpty(id))
{
marzCardEvt = mar_marzCardService.queryMarzCardById(id);
}
return SUCCESS;
}

public String editMarzCard()
{
CommonUtil.debugLog(logger, CommonConstant.SYSTEM_INFO_LOG_METHOD_IN, "MarzCardAction.updateMarzCard");
int result = 0;
if (CommonUtil.isEmpty(marzCardEvt.getId()))
{
result = mar_marzCardService.addMarzCard(marzCardEvt);
CommonUtil.systemLog("mar/editMarzCard.action", CommonConstant.SYSTEMLOG_TYPE_1, result == 0 ? CommonConstant.FAILD : CommonConstant.SUCCESS, String.format("新增marzCardEvt\n%S", marzCardEvt.toString()));
}
else
{
result = mar_marzCardService.updateMarzCard(marzCardEvt);
CommonUtil.systemLog("mar/editMarzCard.action", CommonConstant.SYSTEMLOG_TYPE_2, result == 0 ? CommonConstant.FAILD : CommonConstant.SUCCESS, String.format("修改marzCardEvt\n%S", marzCardEvt.toString()));
}
CommonUtil.infoLog(logger, CommonConstant.SYSTEM_INFO_LOG_METHOD_EXECUTE_NUMS, StringUtil.toBeString(result));
CommonUtil.debugLog(logger, CommonConstant.SYSTEM_INFO_LOG_METHOD_OUT, "MarzCardAction.updateMarzCard");
return SUCCESS;
}

public String deleteMarzCard()
{
CommonUtil.debugLog(logger, CommonConstant.SYSTEM_INFO_LOG_METHOD_IN, "MarzCardAction.deleteMarzCard");
String ids = this.getRequest().getParameter("ids");
int result = mar_marzCardService.deleteMarzCard(CommonUtil.stringToList(ids));
CommonUtil.systemLog("mar/deleteMarzCard.action", CommonConstant.SYSTEMLOG_TYPE_3, result == 0 ? CommonConstant.FAILD : CommonConstant.SUCCESS, String.format("删除marzCardEvt\nID:%S", ids));
CommonUtil.infoLog(logger, CommonConstant.SYSTEM_INFO_LOG_METHOD_EXECUTE_NUMS, StringUtil.toBeString(result));
CommonUtil.debugLog(logger, CommonConstant.SYSTEM_INFO_LOG_METHOD_OUT, "MarzCardAction.deleteMarzCard");
return SUCCESS;
}

public String renewMarzCard()
{
    String ids = this.getRequest().getParameter("ids");
    if (!CommonUtil.isEmpty(ids))
    {
        list = mar_marzCardService.queryMarzCardByIds(CommonUtil.stringToList(ids));
        
        for (MarzCardEvt marzCardEvt : list)
        {
            marzCardEvt.setStatus(MarzConstant.MARZCARD_STATUS_0);
            marzCardEvt.setUsedTime(null);
            mar_marzCardService.updateMarzCard(marzCardEvt);
        }
    }
    return SUCCESS;
}

/**
 * 
 * @Title: createMarzCard
 * @Description: 创建一批卡密
 * @return
 * @return String 返回类型
 * @throws
 */
public String createMarzCard()
{
    String type = this.getRequest().getParameter("type");
    int num = 10;
    
    for (int i = 0; i < num; i++)
    {
        marzCardEvt = new MarzCardEvt();
        marzCardEvt.setStatus(MarzConstant.MARZCARD_STATUS_0);
        marzCardEvt.setType(type);
        marzCardEvt.setPassword(UUID.randomUUID().toString().replace("-", ""));
        
        this.mar_marzCardService.addMarzCard(marzCardEvt);
    }
    
    return SUCCESS;
}

public String exportMarzCard()
{
    CommonUtil.debugLog(logger, CommonConstant.SYSTEM_INFO_LOG_METHOD_IN, "MarzCardAction.exportMarzCard");
    marzCardExport = "";
    String ids = this.getRequest().getParameter("ids");
    list = mar_marzCardService.queryMarzCardByIds(CommonUtil.stringToList(ids));
    for (MarzCardEvt marzCardEvt : list)
    {
        if (null != marzCardEvt && marzCardEvt.getPassword() != null && marzCardEvt.getPassword() != "")
        {
            marzCardExport += marzCardEvt.getPassword() + "\n";
        }
    }
    CommonUtil.infoLog(logger, CommonConstant.SYSTEM_INFO_LOG_METHOD_EXECUTE_NUMS, StringUtil.toBeString(list.size()));
    CommonUtil.debugLog(logger, CommonConstant.SYSTEM_INFO_LOG_METHOD_OUT, "MarzCardAction.exportMarzCard");
    return SUCCESS;
}

/**
 * @return 返回 mar_marzCardService
 */
public MarzCardService getMar_marzCardService()
{
    return mar_marzCardService;
}

/**
 * @param 对mar_marzCardService进行赋值
 */
public void setMar_marzCardService(MarzCardService mar_marzCardService)
{
    this.mar_marzCardService = mar_marzCardService;
}

/**
 * @return 返回 list
 */
public List<MarzCardEvt> getList()
{
    return list;
}

/**
 * @param 对list进行赋值
 */
public void setList(List<MarzCardEvt> list)
{
    this.list = list;
}

/**
 * @return 返回 marzCardEvt
 */
public MarzCardEvt getMarzCardEvt()
{
    return marzCardEvt;
}

/**
 * @param 对marzCardEvt进行赋值
 */
public void setMarzCardEvt(MarzCardEvt marzCardEvt)
{
    this.marzCardEvt = marzCardEvt;
}

/**
 * @return 返回 marzCardReq
 */
public MarzCardReq getMarzCardReq()
{
    return marzCardReq;
}

/**
 * @param 对marzCardReq进行赋值
 */
public void setMarzCardReq(MarzCardReq marzCardReq)
{
    this.marzCardReq = marzCardReq;
}

public String getMarzCardExport()
{
    return marzCardExport;
}

public void setMarzCardExport(String marzCardExport)
{
    this.marzCardExport = marzCardExport;
}

}