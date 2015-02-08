package com.moemao.tgks.mar.marz.thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import com.moemao.tgks.common.core.spring.ContextUtil;
import com.moemao.tgks.mar.marz.task.MarzTaskDiffusion;
import com.moemao.tgks.mar.marz.tool.MarzConstant;
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
    
    private int defaultThreadNum = 500;
    
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
            account.setSessionId("");
            executor.execute(new MarzTaskDiffusion(account));
        }
        
        while (bRunning)
        {
            // 定时销毁已经释放了的线程
            this.destoryOverThread();
            
            try
            {
                Thread.sleep(mainThreadSleep);
            }
            catch (InterruptedException e)
            {
                
            }
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
            if (!existThread(MarConstant.MODULE_TAG + marzAccountEvt.getTgksId()))
            {
                // 创建新线程
                executor.execute(new MarzTaskDiffusion(marzAccountEvt));
            }
            else
            {
                System.out.println("已经存在同名的线程！" + marzAccountEvt.getTgksId());
                return false;
            }
        }
        
        return true;
    }
    
    public synchronized boolean stopThread(String threadName)
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
                        System.out.println(thread.getName() + "线程关闭中...");
                        thread.setName(thread.getName() + MarzConstant.OVER);
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
    public void destoryOverThread()
    {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        Thread[] threads = new Thread[threadGroup.activeCount()];
        threadGroup.enumerate(threads);
        
        for (Thread thread : threads)
        {
            if (thread != null && thread.getName().contains(MarzConstant.OVER))
            {
                try
                {
                    thread.stop();
                }
                catch (Throwable t)
                {
                    
                }
            }
        }
    }
    
    public boolean existThread(String threadName)
    {
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        Thread[] threads = new Thread[threadGroup.activeCount()];
        threadGroup.enumerate(threads);
        
        for (Thread thread : threads)
        {
            if (thread != null && thread.getName().contains(threadName))
            {
                try
                {
                    return true;
                }
                catch (Throwable t)
                {
                    
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
        System.out.println("线程池已经关闭...");
    }
    
    public List<String> showAllThread()
    {
        List<String> list = new ArrayList<String>();
        ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
        Thread[] threads = new Thread[threadGroup.activeCount()];
        threadGroup.enumerate(threads);
        
        for (Thread thread : threads)
        {
            if (thread != null && thread.getName().contains(MarConstant.MODULE_TAG))
            {
                list.add(thread.getName());
                System.out.println(thread.getName());
            }
        }
        
        return list;
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
