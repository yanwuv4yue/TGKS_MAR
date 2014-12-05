package com.moemao.tgks.mar.tool;

import com.moemao.tgks.common.tool.IDUtil;

public class MarUtil
{
	/**
     * CGMS模块唯一标识生成
     * 根据传入的模块标识生成24位的ID
     * 
     * @return
     */
    public static String createUniqueID()
    {
        return IDUtil.createUniqueID(MarConstant.MODULE_TAG);
    }
}
