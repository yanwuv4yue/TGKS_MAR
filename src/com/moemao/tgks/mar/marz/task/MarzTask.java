package com.moemao.tgks.mar.marz.task;

import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.moemao.tgks.common.core.spring.ContextUtil;
import com.moemao.tgks.mar.execute.MarzRequest;
import com.moemao.tgks.mar.marz.tool.MarzConstant;
import com.moemao.tgks.mar.marzaccount.entity.MarzAccountEvt;
import com.moemao.tgks.mar.marzaccount.service.MarzAccountService;
import com.moemao.tgks.mar.marzlog.service.MarzLogService;
import com.moemao.tgks.mar.tool.MarConstant;

public class MarzTask implements Runnable, ApplicationContextAware
{
    private MarzRequest request = MarzRequest.getInstance();
    
    private MarzAccountService marzAccountService;
    
    private MarzLogService marzLogService;
    
    private MarzAccountEvt account;
    
    private Map<String, JSONObject> map;
    
    private String sid;
    
    private int resultCode;
    
    public MarzTask(MarzAccountEvt marzAccountEvt)
    {
        account = marzAccountEvt;
    }

    @Override
    public void run()
    {
        marzAccountService = (MarzAccountService) ContextUtil.getBean("mar_marzAccountService");
        marzLogService = (MarzLogService) ContextUtil.getBean("mar_marzLogService");
        
        // 默认线程调用的执行方法
        System.out.println("执行任务 ID：" + account.getId());
        
        resultCode = this.login();
        
        if (MarzConstant.FAILED == resultCode)
        {
            try
            {
                Thread.sleep(5000);
            }
            catch (InterruptedException e)
            {
                
            }
            
            resultCode = this.login();
            
            if (MarzConstant.FAILED == resultCode)
            {
                // 2次登陆失败 该账号无法执行 直接返回
                return;
            }
        }
        
        // 最后要保存一下sessionId
        account.setSessionId(sid);
        this.marzAccountService.updateMarzAccount(account);
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
                map = request.loginIOS(account.getIosUuid());
            }
            else if (MarzConstant.MARZ_ACCOUNT_TYPE_1.equals(account.getType()))
            {
                map = request.loginAndroid(account.getAndroidUuid());
            }
            else
            {
                map = request.loginIOS(account.getIosUuid());
            }
            
            if (MarzConstant.RES_CODE_0 == map.get(MarzConstant.JSON_TAG_RESCODE).getInt(MarzConstant.JSON_TAG_RESCODE))
            {
                sid = map.get(MarzConstant.JSON_TAG_SID).getString(MarzConstant.JSON_TAG_SID);
                
                map = request.connect(sid);
                
                //sid = map.get(MarzConstant.JSON_TAG_SID).getString(MarzConstant.JSON_TAG_SID);
            }
        }
        catch (Exception e)
        {
            System.out.println("登陆失败！退出任务");
            return MarzConstant.FAILED;
        }
        
        this.marzLogService.marzLog(account, MarzConstant.MARZ_LOG_TYPE_0, String.format("账号%s登录成功", account.getTgksId()));
        
        return MarzConstant.SUCCESS;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException
    {
        ContextUtil.setApplicationContext(applicationContext);
    }
}
