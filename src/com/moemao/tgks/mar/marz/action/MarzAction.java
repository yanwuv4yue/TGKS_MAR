package com.moemao.tgks.mar.marz.action;

import com.moemao.tgks.common.core.action.TGKSAction;
import com.moemao.tgks.mar.marz.thread.MarzThreadPool;

public class MarzAction extends TGKSAction
{

    /** 
     * @Fields serialVersionUID
     */ 
    private static final long serialVersionUID = 4468522957708102512L;
    
    public String marzThreadPoolManager()
    {
        // TODO 查询当前线程池状态
        
        // TODO 查询前100条marzLog记录
        
        return SUCCESS;
    }
    
    /**
     * 
     * @Title: marzThreadPoolStart
     * @Description: 创建MARZ工作线程池并启用
     * @return
     * @return String 返回类型
     * @throws
     */
    public String marzThreadPoolStart()
    {
        Thread thread = new Thread(MarzThreadPool.getInstance());
        thread.start();
        return SUCCESS;
    }
    
    /**
     * 
     * @Title: marzThreadPoolShutdown
     * @Description: 关闭MARZ工作线程池
     * @return
     * @return String 返回类型
     * @throws
     */
    public String marzThreadPoolShutdown()
    {
        MarzThreadPool.getInstance().setbRunning(false);
        return SUCCESS;
    }
}
