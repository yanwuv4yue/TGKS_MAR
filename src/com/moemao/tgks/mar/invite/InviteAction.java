package com.moemao.tgks.mar.invite;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import com.moemao.tgks.common.core.action.TGKSAction;
import com.moemao.tgks.common.tool.CommonConstant;
import com.moemao.tgks.common.tool.CommonUtil;
import com.moemao.tgks.mar.execute.KrsmaRequest;
import com.moemao.tgks.mar.passwordcard.entity.PasswordCardEvt;
import com.moemao.tgks.mar.passwordcard.entity.PasswordCardReq;
import com.moemao.tgks.mar.passwordcard.service.PasswordCardService;
import com.moemao.tgks.mar.tool.MarConstant;

public class InviteAction extends TGKSAction
{

    /** 
     * @Fields serialVersionUID
     */ 
    private static final long serialVersionUID = 1679904315491948085L;

    /**
     * ﻿PasswordCard业务接口
     */
    private PasswordCardService mar_passwordCardService;
    
    private List<String> inviteSessonIdList = new ArrayList<String>();
    
    private KrsmaRequest request = KrsmaRequest.getInstance();
    
    private String inviteCode;
    
    private String password;
    
    public String invitePage()
    {
        return SUCCESS;
    }
    
    /**
     * @throws InterruptedException 
     * @throws JSONException 
     * 
     * @Title: invite
     * @Description: 网页入口请求招待
     * @return
     * @return String 返回类型
     * @throws
     */
    public String invite() throws JSONException, InterruptedException
    {
        String inviteName = "Sakura";
        String inviteChara = "3";
        String inviteCode = this.inviteCode;
        String password = this.password;
        String result;
        String sessonId;
        // 招待个数
        int num = 10;
        
        // 验证参数的有效性
        if (CommonUtil.isEmpty(inviteCode) || CommonUtil.isEmpty(password))
        {
            return ERROR;
        }
        
        // ① 验证卡密的有效性
        PasswordCardReq passwordCardReq = new PasswordCardReq();
        passwordCardReq.setPassword(password);
        
        // 根据卡密查询是否有该数据
        List<PasswordCardEvt> list = this.mar_passwordCardService.queryPasswordCard(passwordCardReq);
        
        // 如果卡密本身不存在 返回ERROR
        if (CommonUtil.isEmpty(list))
        {
            return ERROR;
        }

        // 如果存在 则分两种情况 1 没刷过；2 刷到一半失败了
        PasswordCardEvt passwordCardEvt = list.get(0);        
        
        if(MarConstant.PASSWORDCARD_STATUS_2.equals(passwordCardEvt.getStatus()))
        {
            // 如果已经完成 则返回ERROR
            return ERROR;
        }
        else if (MarConstant.PASSWORDCARD_STATUS_1.equals(passwordCardEvt.getStatus()))
        {
            // 该卡密使用到一半 inviteCode从之前的记录中获取
            //inviteCode = passwordCardEvt.getInviteCode();
        }
        else if (MarConstant.PASSWORDCARD_STATUS_0.equals(passwordCardEvt.getStatus()))
        {
            // 如果该卡密未使用过
            passwordCardEvt.setInviteCode(inviteCode);
            // 更新状态为任务中
            passwordCardEvt.setStatus(MarConstant.PASSWORDCARD_STATUS_1);
            this.mar_passwordCardService.updatePasswordCard(passwordCardEvt);
        }        
        
        
        // 开始刷招待
        CommonUtil.systemLog("mar/invite.action", CommonConstant.SYSTEMLOG_TYPE_2, CommonConstant.SUCCESS, MessageFormat.format("执行招待任务 卡密号码：{0} 招待ID：{1}", password, inviteCode));
        
        this.inviteSessonIdList = new ArrayList<String>();

        try
        {
            for (int i = 1; i <= num; i++)
            {
                result = request.regist();
                
                // 从regist的result中解析出sessonId
                JSONObject json= new JSONObject(result);
                sessonId = (String) json.get("sess_key");
                this.inviteSessonIdList.add(sessonId.replace("=", ""));
            }
            
            for (String sid : this.inviteSessonIdList)
            {
                request.connect(sid);
            }
            
            Thread.sleep(60000);
            
            int index = 1;
            for (String sid : this.inviteSessonIdList)
            {
                request.userCreate(sid, inviteName, inviteChara);
                request.homeShow(sid);
                request.inviteCodeEnter(sid, inviteCode);
                System.out.println("第" + index + "个招待已经完成！");// 临时打印
                index++;
            }
        }
        catch (Exception e)
        {
            passwordCardEvt.setInviteCode("");
            passwordCardEvt.setStatus(MarConstant.PASSWORDCARD_STATUS_0);
            passwordCardEvt.setUsedTime(null);
            this.mar_passwordCardService.updatePasswordCard(passwordCardEvt);
        }
        
        System.out.println("====招待已经全部完成====");// 临时打印
        
        // 刷完招待后 将卡密状态更新为已完成
        passwordCardEvt = this.mar_passwordCardService.queryPasswordCard(passwordCardReq).get(0);
        passwordCardEvt.setStatus(MarConstant.PASSWORDCARD_STATUS_2);
        passwordCardEvt.setUsedTime(new Date());
        this.mar_passwordCardService.updatePasswordCard(passwordCardEvt);

        return SUCCESS;
    }

    public PasswordCardService getMar_passwordCardService()
    {
        return mar_passwordCardService;
    }

    public void setMar_passwordCardService(
            PasswordCardService mar_passwordCardService)
    {
        this.mar_passwordCardService = mar_passwordCardService;
    }

    public KrsmaRequest getKrsmaRequest()
    {
        return request;
    }

    public void setKrsmaRequest(KrsmaRequest request)
    {
        this.request = request;
    }

    public String getInviteCode()
    {
        return inviteCode;
    }

    public void setInviteCode(String inviteCode)
    {
        this.inviteCode = inviteCode;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String password)
    {
        this.password = password;
    }
}
