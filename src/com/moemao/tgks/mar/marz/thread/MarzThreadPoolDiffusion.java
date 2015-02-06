package com.moemao.tgks.mar.marz.thread;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.moemao.tgks.common.core.spring.ContextUtil;
import com.moemao.tgks.mar.marz.task.MarzTaskDiffusion;
import com.moemao.tgks.mar.marzaccount.entity.MarzAccountEvt;
import com.moemao.tgks.mar.marzaccount.service.MarzAccountService;
import com.moemao.tgks.mar.tool.MarConstant;

/**
 * 
 * 项目名称：TGKS_MAR
 * 类名称：MarzThreadPoolDiffusion
 * 类描述：与之前的挂机池模式不同，Diffusion采用启动时扫描所有未过期的在线账号并创建对应线程执行，直到池关闭或者用户自行修改账户状态
 * 创建人：Administrator
 * 创建时间：2015-1-27 下午10:08:35
 * 修改人：Administrator
 * 修改时间：2015-1-27 下午10:08:35
 * 修改备注：
 * @version
 *
 */
public class MarzThreadPoolDiffusion implements Runnable, ApplicationContextAware
{
    private MarzThreadPoolDiffusion() {};
    
    private static MarzThreadPoolDiffusion instance;
    
    public static synchronized MarzThreadPoolDiffusion getInstance()
    {
        if (null == instance)
        {
            instance = new MarzThreadPoolDiffusion();
        }
        
        return instance;
    }
    
    private ExecutorService executor = null;
    
    private MarzAccountService marzAccountService;
    
    private boolean bRunning = true;
    
    private int defaultThreadNum = 100;
    
    private int mainThreadSleep = 300000;
    
    private List<MarzAccountEvt> accountList;
    
    public void run()
    {
        bRunning = true;
        
        marzAccountService = (MarzAccountService) ContextUtil.getBean("mar_marzAccountService");
        
        if (null == executor || executor.isTerminated())
        {
            executor = Executors.newFixedThreadPool(defaultThreadNum);
            //executor = Executors.newScheduledThreadPool(defaultThreadNum);
            System.out.println("线程池创建完毕");
        }
        else
        {
            // 存在当前线程池
        }
        
        // 从数据库中查询出需要执行的任务
        accountList = marzAccountService.queryMarzAccountOnline();
        
        System.out.println("取出需要执行的任务数：" + accountList.size());
        
        // 循环建立新的任务 放入线程池执行
        for (MarzAccountEvt account : accountList)
        {
            executor.execute(new MarzTaskDiffusion(account));
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
    
    public synchronized boolean createThread(MarzAccountEvt marzAccountEvt)
    {
        if (null == executor || executor.isTerminated())
        {
            // 线程池未启动 返回失败
            return false;
        }
        else
        {
            // 创建新线程
            executor.execute(new MarzTaskDiffusion(marzAccountEvt));
            return true;
        }
    }
    
    @SuppressWarnings("deprecation")
    public synchronized boolean interruptThread(String threadName)
    {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        Thread[] threads = new Thread[threadGroup.activeCount()];
        threadGroup.enumerate(threads);
        
        for (Thread thread : threads)
        {
            if (thread != null && thread.getName().equals(threadName))
            {
                if (Thread.State.RUNNABLE != thread.getState())
                {
                    try
                    {
                        thread.stop();
                        System.out.println("关闭线程..." + thread.getName());
                        return true;
                    }
                    catch (Throwable t)
                    {
                        
                    }
                }
            }
        }
        return false;
    }
    
    @SuppressWarnings("deprecation")
    public void shutdown()
    {
        // 关闭线程
        executor.shutdown();
        
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        Thread[] threads = new Thread[threadGroup.activeCount()];
        threadGroup.enumerate(threads);
        
        for (Thread thread : threads)
        {
            if (thread != null && thread.getName().contains(MarConstant.MODULE_TAG))
            {
                if (Thread.State.RUNNABLE != thread.getState())
                {
                    try
                    {
                        thread.stop();
                        System.out.println("关闭线程..." + thread.getName());
                    }
                    catch (Throwable t)
                    {
                        
                    }
                }
            }
        }
        System.out.println("线程池已经关闭...");
    }
    
    public void showAllThread()
    {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        Thread[] threads = new Thread[threadGroup.activeCount()];
        threadGroup.enumerate(threads);
        
        for (Thread thread : threads)
        {
            if (thread != null && Thread.State.RUNNABLE != thread.getState() && thread.getName().contains(MarConstant.MODULE_TAG))
            {
                System.out.println(thread.getName());
            }
        }
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
        Thread thread = new Thread(new MarzThreadPoolDiffusion());
        thread.start();
        System.out.println("over==========");
    }
}
