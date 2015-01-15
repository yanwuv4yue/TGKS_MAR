package com.moemao.tgks.mar.marz.task;

import java.util.Map;

import net.sf.json.JSONObject;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.moemao.tgks.common.core.spring.ContextUtil;
import com.moemao.tgks.mar.execute.MarzRequest;
import com.moemao.tgks.mar.marz.tool.MarzConstant;
import com.moemao.tgks.mar.marz.tool.MarzUtil;
import com.moemao.tgks.mar.marzaccount.entity.MarzAccountEvt;
import com.moemao.tgks.mar.marzaccount.service.MarzAccountService;
import com.moemao.tgks.mar.marzlog.service.MarzLogService;

public class MarzTask implements Runnable, ApplicationContextAware
{
    private MarzRequest request = MarzRequest.getInstance();
    
    private MarzAccountService marzAccountService;
    
    private MarzLogService marzLogService;
    
    private MarzAccountEvt account;
    
    private Map<String, JSONObject> map;
    
    private String sid;
    
    private int resultCode = MarzConstant.SUCCESS;
    
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
        
        if (MarzConstant.SUCCESS > resultCode)
        {
            try
            {
                System.out.println("发生了错误！当前resultCode：" + resultCode);
                Thread.sleep(5000);
            }
            catch (InterruptedException e)
            {
                
            }
            
            resultCode = this.login();
            
            if (MarzConstant.SUCCESS > resultCode)
            {
                // 2次登陆失败 该账号无法执行 直接返回
                return;
            }
        }
        
        resultCode = this.homeShow();
        
        if (MarzConstant.SUCCESS > resultCode)
        {
            try
            {
                System.out.println("发生了错误！当前resultCode：" + resultCode);
                Thread.sleep(5000);
            }
            catch (InterruptedException e)
            {
                
            }
            
            resultCode = this.login();
            resultCode = this.homeShow();
            
            if (MarzConstant.SUCCESS > resultCode)
            {
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
            
            resultCode = map.get(MarzConstant.JSON_TAG_RESCODE).getInt(MarzConstant.JSON_TAG_RESCODE);
            
            this.marzLogService.marzLog(account, MarzConstant.MARZ_LOG_TYPE_0, "账号登录" + MarzUtil.resultCodeStr(resultCode));
            
            if (MarzConstant.RES_CODE_0 == resultCode)
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
        
        return resultCode;
    }
    
    private int homeShow()
    {
        try
        {
            map = request.homeShow(sid);
            
            resultCode = map.get(MarzConstant.JSON_TAG_RESCODE).getInt(MarzConstant.JSON_TAG_RESCODE);
            
            this.marzLogService.marzLog(account, MarzConstant.MARZ_LOG_TYPE_0, "账号更新基本信息" + MarzUtil.resultCodeStr(resultCode));
            
            if (MarzConstant.RES_CODE_0 == resultCode)
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
            }
        }
        catch (Exception e)
        {
            return MarzConstant.FAILED;
        }
        
        return resultCode;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException
    {
        ContextUtil.setApplicationContext(applicationContext);
    }
}
