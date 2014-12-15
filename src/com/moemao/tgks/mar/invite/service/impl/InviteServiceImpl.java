package com.moemao.tgks.mar.invite.service.impl;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONObject;

import com.moemao.tgks.mar.execute.KrsmaRequest;
import com.moemao.tgks.mar.invite.service.InviteService;

public class InviteServiceImpl implements InviteService
{
    private KrsmaRequest request = KrsmaRequest.getInstance();
    
    /**
     * 返回最后一个招待的ID 给自动刷初始流程提供5个石头
     */
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
                json= new JSONObject(result[0]);
                sessionId = (String) json.get("sess_key");
                inviteSessonIdList.add(sessionId.replace("=", ""));
            }
            
            for (String sid : inviteSessonIdList)
            {
                request.connect(sid);
            }
            
            Thread.sleep(60000);
            
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


    public KrsmaRequest getKrsmaRequest()
    {
        return request;
    }

    public void setKrsmaRequest(KrsmaRequest request)
    {
        this.request = request;
    }
}
