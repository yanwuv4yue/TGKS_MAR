package com.moemao.tgks.mar.marz.tool;

public class MarzUtil
{
    public static String resultCodeStr(int resultCode)
    {
        return resultCode == MarzConstant.RES_CODE_0 ? "成功" : "失败";
    }
}
