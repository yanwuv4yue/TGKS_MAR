package com.moemao.tgks.mar.marzsetting.entity;

public class MarzSettingEvt
{
/**
 * 表唯一主键
 */
private String id;

/**
 * tgks账号ID
 */
private String tgksId;

/**
 * 类型（0 开关；1 参数）
 */
private String type;

/**
 * 参数名
 */
private String name;

/**
 * 参数值
 */
private String value;

private String explore;

private String cardSell;

private String cardSellCommon;

private String cardFusion;

private String battle;

private String battleNowaste;

private String bossIds;

@Override
public String toString()
{
return String.format("id:%S\ntgksId:%S\ntype:%S\nname:%S\nvalue:%S\n", id, tgksId, type, name, value);
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
 * @return 返回 tgksId
 */
public String getTgksId()
{
    return tgksId;
}

/**
 * @param 对tgksId进行赋值
 */
public void setTgksId(String tgksId)
{
    this.tgksId = tgksId;
}

/**
 * @return 返回 type
 */
public String getType()
{
    return type;
}

/**
 * @param 对type进行赋值
 */
public void setType(String type)
{
    this.type = type;
}

/**
 * @return 返回 name
 */
public String getName()
{
    return name;
}

/**
 * @param 对name进行赋值
 */
public void setName(String name)
{
    this.name = name;
}

/**
 * @return 返回 value
 */
public String getValue()
{
    return value;
}

/**
 * @param 对value进行赋值
 */
public void setValue(String value)
{
    this.value = value;
}

public String getExplore()
{
    return explore;
}

public void setExplore(String explore)
{
    this.explore = explore;
}

public String getCardSell()
{
    return cardSell;
}

public void setCardSell(String cardSell)
{
    this.cardSell = cardSell;
}

public String getCardSellCommon()
{
    return cardSellCommon;
}

public void setCardSellCommon(String cardSellCommon)
{
    this.cardSellCommon = cardSellCommon;
}

public String getCardFusion()
{
    return cardFusion;
}

public void setCardFusion(String cardFusion)
{
    this.cardFusion = cardFusion;
}

public String getBattle()
{
    return battle;
}

public void setBattle(String battle)
{
    this.battle = battle;
}

public String getBattleNowaste()
{
    return battleNowaste;
}

public void setBattleNowaste(String battleNowaste)
{
    this.battleNowaste = battleNowaste;
}

public String getBossIds()
{
    return bossIds;
}

public void setBossIds(String bossIds)
{
    this.bossIds = bossIds;
}

}