package com.moemao.tgks.mar.invite.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

import com.moemao.tgks.common.tool.CommonUtil;
import com.moemao.tgks.mar.account.entity.AccountEvt;
import com.moemao.tgks.mar.account.entity.AccountReq;
import com.moemao.tgks.mar.account.service.AccountService;
import com.moemao.tgks.mar.execute.KrsmaRequest;
import com.moemao.tgks.mar.execute.MarzRequest;
import com.moemao.tgks.mar.invite.service.InviteService;
import com.moemao.tgks.mar.marz.tool.MarzConstant;
import com.moemao.tgks.mar.tool.MarConstant;

public class InviteServiceImpl implements InviteService
{
    private MarzRequest request = MarzRequest.getInstance();

    private AccountService mar_accountService;
    
    /**
     * 返回最后一个招待的ID 给自动刷初始流程提供5个石头
     
    public String invite(String inviteCode)
    {
        String inviteName = "Kirito";
        String inviteChara = "3";
        String[] result = new String[2];
        String sessionId;
        // 返回最后一个招待的ID
        String returnCode = "";
        // 招待个数
        int num = 10;
        JSONObject json= null;

        List<String> inviteSessonIdList = new ArrayList<String>();
        
        try
        {
            for (int i = 1; i <= num; i++)
            {
                result[0] = request.regist();
                
                // 从regist的result中解析出sessonId
                json= JSONObject.fromObject(result[0]);
                sessionId = (String) json.get("sess_key");
                inviteSessonIdList.add(sessionId.replace("=", ""));
            }
            
            for (String sid : inviteSessonIdList)
            {
                request.connect(sid);
            }
            
            Thread.sleep(5000);
            
            int index = 0;
            for (String sid : inviteSessonIdList)
            {
                index++;
                request.userCreate(sid, inviteName, inviteChara);
                result = request.homeShow(sid);
                sid = result[0];
                request.inviteCodeEnter(sid, inviteCode);
                System.out.println("第" + index + "个招待已经完成！");// 临时打印
                
                if (index == num)
                {
                    // 记录最后一个号的招待ID
                    returnCode = result[1].split("inviteid\":\"")[1].substring(0, 9);
                }
            }
        }
        catch (Exception e)
        {
            
        }
        
        return returnCode;
    }
    */
    public synchronized void invite2(String password, String inviteCode)
    {
        String sid;
        
        // 先查询之前是否已经刷过
        AccountReq accountReq = new AccountReq();
        accountReq.setInviteCode(inviteCode);
        accountReq.setTitle(password);
        List<AccountEvt> accountList = this.mar_accountService.queryAccount(accountReq);
        
        if (CommonUtil.isEmpty(accountList))
        {
            // 如果之前没有刷过
            accountReq = new AccountReq();
            accountReq.setStatus(MarConstant.ACCOUNT_STATUS_4);
            accountReq.setSortSql(" t.ID Limit 0, 10");
            
            // 查询出10个可以用来刷招待的号
            accountList = this.mar_accountService.queryAccount(accountReq);
            
            // 先绑定这10个号
            for (AccountEvt accountEvt : accountList)
            {
                accountEvt.setInviteCode(inviteCode);
                accountEvt.setTitle(password);
                this.mar_accountService.updateAccount(accountEvt);
            }
        }

        try
        {
            Map<String, JSONObject> map = new HashMap<String, JSONObject>();
            
            for (AccountEvt accountEvt : accountList)
            {
                // 1 登录
                map = request.loginIOS(accountEvt.getUuid(), accountEvt.getHashToken());
                sid = map.get(MarzConstant.JSON_TAG_SID).getString(MarzConstant.JSON_TAG_SID);
                map = request.connect(sid);
                
                // 1.5 IOSv1.1.1更新完必须打完第一仗
                this.teamBattleSoloStart(sid, MarConstant.BATTLESOLOSTART_FIRST);
                accountEvt.setSessionId(sid);
            }
            
            for (AccountEvt accountEvt : accountList)
            {
                sid = accountEvt.getSessionId();
                this.teamBattleSoloEnd(sid, MarConstant.BATTLESOLOEND_3);
                
                // 2 填招待ID
                request.inviteCodeEnter(sid, inviteCode);
                
                // 刷完招待更新一下账号状态
                accountEvt.setStatus(MarConstant.ACCOUNT_STATUS_5);
                this.mar_accountService.updateAccount(accountEvt);
            }
        }
        catch (Exception e)
        {
            
        }
    }
    
    public void teamBattleSoloStart(String sid, String battleSoloStart)
    {
        KrsmaRequest request = KrsmaRequest.getInstance();
        try
        {
            String[] result = request.teamBattleSoloShow(sid);
            sid = result[0];
            result[1] = ("{\"normal_groups\"" + result[1].split("normal_groups\"")[1]);
            JSONObject json = JSONObject.fromObject(result[1]);
            JSONArray arthers = json.getJSONArray("arthurs");
            @SuppressWarnings("unchecked")
            List<JSONObject> jsonList = (List<JSONObject>) JSONArray.toCollection(arthers, JSONObject.class);
            JSONArray users;
            String userA = "";
            String userB = "";
            String userD = "";
            for (JSONObject obj : jsonList)
            {
                if (MarConstant.KRSMACARD_TYPE_1.equals(obj.getString("arthur_type")))
                {
                    users = obj.getJSONArray("partners");
                    @SuppressWarnings("unchecked")
                    List<JSONObject> userList = (List<JSONObject>) JSONArray.toCollection(users, JSONObject.class);
                    userA = userList.get(0).getString("userid");
                }
                else if (MarConstant.KRSMACARD_TYPE_2.equals(obj.getString("arthur_type")))
                {
                    users = obj.getJSONArray("partners");
                    @SuppressWarnings("unchecked")
                    List<JSONObject> userList = (List<JSONObject>) JSONArray.toCollection(users, JSONObject.class);
                    userB = userList.get(0).getString("userid");
                }
                else if (MarConstant.KRSMACARD_TYPE_4.equals(obj.getString("arthur_type")))
                {
                    users = obj.getJSONArray("partners");
                    @SuppressWarnings("unchecked")
                    List<JSONObject> userList = (List<JSONObject>) JSONArray.toCollection(users, JSONObject.class);
                    userD = userList.get(0).getString("userid");
                }
            }
            
            result = request.teamBattleSoloStart(sid, battleSoloStart, userA, userB, userD);
            sid = result[0];
            //Thread.sleep(MarConstant.SLEEP_TIME_BATTLE);
            
            //request.teamBattleSoloEnd(sid, battleSoloEnd);
        }
        catch (Exception e)
        {
            return;
        }
    }
    
    public void teamBattleSoloEnd(String sid, String battleSoloEnd)
    {
        KrsmaRequest request = KrsmaRequest.getInstance();
        try
        {
            request.teamBattleSoloEnd(sid, battleSoloEnd);
        }
        catch (Exception e)
        {
            return;
        }
    }

    public MarzRequest getRequest()
    {
        return request;
    }

    public void setRequest(MarzRequest request)
    {
        this.request = request;
    }

    public AccountService getMar_accountService()
    {
        return mar_accountService;
    }

    public void setMar_accountService(AccountService mar_accountService)
    {
        this.mar_accountService = mar_accountService;
    }
}
