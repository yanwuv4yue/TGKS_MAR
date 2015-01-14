package com.moemao.tgks.mar.marz.thread;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.moemao.tgks.common.core.spring.ContextUtil;
import com.moemao.tgks.mar.marz.task.MarzTask;
import com.moemao.tgks.mar.marzaccount.entity.MarzAccountEvt;
import com.moemao.tgks.mar.marzaccount.service.MarzAccountService;

public class MarzThreadPool implements Runnable, ApplicationContextAware
{
    private MarzThreadPool() {};
    
    private static MarzThreadPool instance;
    
    public static synchronized MarzThreadPool getInstance()
    {
        if (null == instance)
        {
            instance = new MarzThreadPool();
        }
        
        return instance;
    }
    
    private ExecutorService executor = null;
    
    private MarzAccountService marzAccountService;
    
    private boolean bRunning = true;
    
    private int defaultThreadNum = 20;
    
    private int mainThreadSleep = 300000;
    
    private List<MarzAccountEvt> accountList;
    
    public void run()
    {
        bRunning = true;
        
        marzAccountService = (MarzAccountService) ContextUtil.getBean("mar_marzAccountService");
        
        if (null == executor || executor.isTerminated())
        {
            executor = Executors.newFixedThreadPool(defaultThreadNum);
            System.out.println("线程池创建完毕");
        }
        else
        {
            // 存在当前线程池
        }
        
        while (bRunning)
        {
            // 从数据库中查询出需要执行的任务
            accountList = marzAccountService.queryMarzAccountOnline();
            
            System.out.println("取出需要执行的任务数：" + accountList.size());
            
            // 循环建立新的任务 放入线程池执行
            for (MarzAccountEvt account : accountList)
            {
                executor.execute(new MarzTask(account));
            }
            
            try
            {
                Thread.sleep(mainThreadSleep);
            }
            catch (InterruptedException e)
            {
                e.printStackTrace();
            }
        }
        
        // 关闭线程
        executor.shutdown();
        System.out.println("线程池正在关闭...");
    }
    
    public void shutdown()
    {
        this.bRunning = false;
    }
    
    public boolean isbRunning()
    {
        return bRunning;
    }

    public void setbRunning(boolean bRunning)
    {
        this.bRunning = bRunning;
    }
    
    public MarzAccountService getMarzAccountService()
    {
        return marzAccountService;
    }

    public void setMarzAccountService(MarzAccountService marzAccountService)
    {
        this.marzAccountService = marzAccountService;
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext)
            throws BeansException
    {
        ContextUtil.setApplicationContext(applicationContext);
    }

    public static void main(String[] args)
    {
        Thread thread = new Thread(new MarzThreadPool());
        thread.start();
        System.out.println("over==========");
    }
}
