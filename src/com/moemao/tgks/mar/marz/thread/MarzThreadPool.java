package com.moemao.tgks.mar.marz.thread;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.moemao.tgks.mar.marz.task.MarzTask;
import com.moemao.tgks.mar.marzaccount.entity.MarzAccountEvt;
import com.moemao.tgks.mar.marzaccount.service.MarzAccountService;

public class MarzThreadPool implements Runnable
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
    
    private MarzAccountService mar_marzAccountService;
    
    private boolean bRunning = true;
    
    private int defaultThreadNum = 20;
    
    private int mainThreadSleep = 300000;
    
    private List<MarzAccountEvt> accountList;
    
    public void run()
    {
        bRunning = true;
        
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
            accountList = mar_marzAccountService.queryMarzAccountOnline();
            
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

    public MarzAccountService getMar_marzAccountService()
    {
        return mar_marzAccountService;
    }

    public void setMar_marzAccountService(MarzAccountService mar_marzAccountService)
    {
        this.mar_marzAccountService = mar_marzAccountService;
    }

    public static void main(String[] args)
    {
        Thread thread = new Thread(new MarzThreadPool());
        thread.start();
        System.out.println("over==========");
    }
}
