package com.moemao.tgks.mar.invite;

import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONException;
import org.json.JSONObject;

import com.moemao.tgks.common.core.action.TGKSAction;
import com.moemao.tgks.common.tool.CommonConstant;
import com.moemao.tgks.common.tool.CommonUtil;
import com.moemao.tgks.mar.net.HttpRequest;
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

    private static Log logger = LogFactory.getLog(InviteAction.class);
    
    /**
     * ﻿PasswordCard业务接口
     */
    private PasswordCardService mar_passwordCardService;

    private HttpRequest httpRequest = new HttpRequest();
    
    private List<String> inviteSessonIdList = new ArrayList<String>();
    
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
        String inviteCode = this.getRequest().getParameter("inviteCode");
        String password = this.getRequest().getParameter("password");
        String result;
        String sessonId;
        int num = 10;
        
        if (CommonUtil.isEmpty(inviteCode) || CommonUtil.isEmpty(password))
        {
            return ERROR;
        }
        
        // 验证卡密的有效性
        PasswordCardReq passwordCardReq = new PasswordCardReq();
        passwordCardReq.setPassword(password);
        
        List<PasswordCardEvt> list = this.mar_passwordCardService.queryPasswordCard(passwordCardReq);
        if (CommonUtil.isEmpty(list))
        {
            return ERROR;
        }
        
        // 更新卡密状态 写入操作的招待ID
        PasswordCardEvt passwordCardEvt = list.get(0);
        passwordCardEvt.setInviteCode(inviteCode);
        passwordCardEvt.setStatus(MarConstant.PASSWORDCARD_STATUS_1);
        passwordCardEvt.setUsedTime(new Date());
        this.mar_passwordCardService.updatePasswordCard(passwordCardEvt);
        
        // 开始刷招待
        CommonUtil.systemLog("mar/invite.action", CommonConstant.SYSTEMLOG_TYPE_2, CommonConstant.SUCCESS, MessageFormat.format("执行招待任务 卡密号码：{0} 招待ID：{1}", password, inviteCode));
        
        System.out.println("inviteCode = " + inviteCode);
        
        System.out.println("启动招待任务：共" + num + "个......");
        this.inviteSessonIdList = new ArrayList<String>();

        try
        {
            for (int i = 1; i <= num; i++)
            {
                result = this.regist();
                
                // 从regist的result中解析出sessonId
                JSONObject json= new JSONObject(result);
                sessonId = (String) json.get("sess_key");
                this.inviteSessonIdList.add(sessonId.replace("=", ""));
            }
            
            for (String sid : this.inviteSessonIdList)
            {
                this.connect(sid);
            }
            
            Thread.sleep(60000);
            
            int index = 1;
            for (String sid : this.inviteSessonIdList)
            {
                this.userCreate(sid, inviteName, inviteChara);
                this.homeShow(sid);
                this.inviteCodeEnter(sid, inviteCode);
                System.out.println("第" + index + "个招待已经完成！");
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
        
        System.out.println("====招待已经全部完成====");

        return SUCCESS;
    }
    
    public String regist()
    {
        String paramStr = "{\"uuid\":\"" + UUID.randomUUID().toString() + "\",\"clver\":\"1\",\"os\":0,\"carrier\":3,\"market\":1,\"lang\":0,\"device\":\"iPhone5S\",\"token\":\"\"}";
        String result = httpRequest.sendPost(MarConstant.URL_REGIST, paramStr);
        System.out.println("regist");
        System.out.println(result);
        
        return result;
    }
    
    public String connect(String sid)
    {
        String paramStr = sid + "=";
        String result = httpRequest.sendPost(MarConstant.URL_CONNECT, paramStr);
        System.out.println("connect");
        System.out.println(result);
        
        return result;
    }
    
    public String userCreate(String sid, String name, String chara)
    {
        String paramStr = sid + "=" + "{\"name\":\"" + name + "\",\"arthur_type\":" + chara + "}";
        String result = httpRequest.sendPost(MarConstant.URL_USERCREATE, paramStr);
        System.out.println("userCreate");
        System.out.println(result);
        
        return result;
    }
    
    public String homeShow(String sid)
    {
        String paramStr = sid + "=";
        String result = httpRequest.sendPost(MarConstant.URL_HOMESHOW, paramStr);
        System.out.println("connect");
        System.out.println(result);
        
        return result;
    }
    
    public String inviteCodeEnter(String sid, String inviteCode)
    {
        String paramStr = sid + "=" + "{\"inviteid\":\"" + inviteCode + "\"}";
        String result = httpRequest.sendPost(MarConstant.URL_INVITECODEENTER, paramStr);
        System.out.println("inviteCodeEnter");
        System.out.println(result);
        
        return result;
    }
    
    public HttpRequest getHttpRequest()
    {
        return httpRequest;
    }

    public void setHttpRequest(HttpRequest httpRequest)
    {
        this.httpRequest = httpRequest;
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
}
