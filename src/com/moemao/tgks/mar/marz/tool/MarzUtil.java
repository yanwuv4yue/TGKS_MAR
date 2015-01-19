package com.moemao.tgks.mar.marz.tool;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.moemao.tgks.common.tool.CommonUtil;

public class MarzUtil
{
    public static String resultCodeStr(int resultCode)
    {
        return resultCode == MarzConstant.RES_CODE_0 ? "成功" : "失败";
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
            str.append(s).append(",");
        }
        
        return str.substring(0, str.length() - 1).toString();
    }
    
    public static List<String> stringToList(String str)
    {
        if (CommonUtil.isEmpty(str))
        {
            return new ArrayList<String>();
        }
        
        return Arrays.asList(str.split(","));
    }
}
