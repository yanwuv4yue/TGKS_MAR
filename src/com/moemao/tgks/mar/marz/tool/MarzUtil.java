package com.moemao.tgks.mar.marz.tool;

import java.util.ArrayList;
import java.util.List;

import com.moemao.tgks.common.tool.CommonUtil;

public class MarzUtil
{
    public static String resultCodeStr(int resultCode)
    {
        return resultCode == MarzConstant.RES_CODE_SUCCESS_0 ? "成功" : "失败";
    }
    
    public static String listToString(List<String> list)
    {
        if (CommonUtil.isEmpty(list))
        {
            return "";
        }
        
        StringBuffer str = new StringBuffer();
        
        for (String s : list)
        {
            if (!CommonUtil.isEmpty(s.trim()))
            {
                str.append(s.trim()).append(",");
            }
        }
        
        return str.substring(0, str.length() - 1).toString();
    }
    
    public static List<String> stringToList(String str)
    {
        if (CommonUtil.isEmpty(str))
        {
            return new ArrayList<String>();
        }
        
        List<String> list = new ArrayList<String>();
        for (String s : str.split(","))
        {
            if (!CommonUtil.isEmpty(s.trim()))
            {
                list.add(s.trim());
            }
        }
        
        return list;
    }
}
