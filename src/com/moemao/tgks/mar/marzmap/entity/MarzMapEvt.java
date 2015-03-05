package com.moemao.tgks.mar.marzmap.entity;

public class MarzMapEvt
{
/**
 * 表唯一主键
 */
private String id;

/**
 * 战斗地图的BOSSID
 */
private String bossId;

/**
 * BOSS名称难度
 */
private String bossName;

/**
 * VIP等级
 */
private String vip;

/**
 * BP消耗
 */
private Integer bpCost;

/**
 * 攻击目标数
 */
private Integer target;

/**
 * 设置页面是否被选中
 */
private String check;

/**
 * 设置页面是否显示选择框
 */
private String show;

@Override
public String toString()
{
return String.format("id:%S\nbossId:%S\nbossName:%S\nbpCost:%S\n", id, bossId, bossName, bpCost);
}

/**
 * @return 返回 id
 */
public String getId()
{
    return id;
}

/**
 * @param 对id进行赋值
 */
public void setId(String id)
{
    this.id = id;
}

/**
 * @return 返回 bossId
 */
public String getBossId()
{
    return bossId;
}

/**
 * @param 对bossId进行赋值
 */
public void setBossId(String bossId)
{
    this.bossId = bossId;
}

/**
 * @return 返回 bossName
 */
public String getBossName()
{
    return bossName;
}

/**
 * @param 对bossName进行赋值
 */
public void setBossName(String bossName)
{
    this.bossName = bossName;
}

/**
 * @return 返回 bpCost
 */
public Integer getBpCost()
{
    return bpCost;
}

/**
 * @param 对bpCost进行赋值
 */
public void setBpCost(Integer bpCost)
{
    this.bpCost = bpCost;
}

public Integer getTarget()
{
    return target;
}

public void setTarget(Integer target)
{
    this.target = target;
}

public String getVip()
{
    return vip;
}

public void setVip(String vip)
{
    this.vip = vip;
}

public String getCheck()
{
    return check;
}

public void setCheck(String check)
{
    this.check = check;
}

public String getShow()
{
    return show;
}

public void setShow(String show)
{
    this.show = show;
}

}