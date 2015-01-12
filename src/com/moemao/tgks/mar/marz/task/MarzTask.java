package com.moemao.tgks.mar.marz.task;

import com.moemao.tgks.mar.marzaccount.entity.MarzAccountEvt;

public class MarzTask implements Runnable
{
    private MarzAccountEvt account;
    
    public MarzTask(MarzAccountEvt marzAccountEvt)
    {
        account = marzAccountEvt;
    }

    @Override
    public void run()
    {
        // 默认线程调用的执行方法
        System.out.println("执行任务！" + account.getSessionId());
        
        try
        {
            Thread.sleep(5000);
        }
        catch (InterruptedException e)
        {
            e.printStackTrace();
        }
    }
    
}
