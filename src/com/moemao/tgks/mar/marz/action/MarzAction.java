package com.moemao.tgks.mar.marz.action;

import com.moemao.tgks.common.core.action.TGKSAction;
import com.moemao.tgks.mar.marz.thread.MarzThreadPool;

public class MarzAction extends TGKSAction
{

    /** 
     * @Fields serialVersionUID
     */ 
    private static final long serialVersionUID = 4468522957708102512L;
    
    public String marzThreadPoolStart()
    {
        Thread thread = new Thread(MarzThreadPool.getInstance());
        thread.start();
        return SUCCESS;
    }
    
    public String marzThreadPoolShutdown()
    {
        MarzThreadPool.getInstance().setbRunning(false);
        return SUCCESS;
    }
}
