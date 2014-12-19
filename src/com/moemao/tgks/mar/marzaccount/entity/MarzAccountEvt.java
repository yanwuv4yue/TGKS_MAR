package com.moemao.tgks.mar.marzaccount.entity;

import java.util.Date;

public class MarzAccountEvt
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
 * IOS UUID
 */
private String iosUuid;

/**
 * Android UUID
 */
private String androidUuid;

/**
 * IOS KEY
 */
private String iosKey;

/**
 * Android KEY
 */
private String androidKey;

/**
 * 类型（0 IOS；1 Android）
 */
private String type;

/**
 * 状态（0 离线；1 在线）
 */
private String status;

/**
 * SessionId
 */
private String sessionId;

/**
 * VIP等级（0 试用；1 普通；2 白金；3 钻石）
 */
private String vip;

/**
 * 角色名
 */
private String name;

/**
 * USER ID
 */
private String userId;

/**
 * 等级
 */
private int lv;

/**
 * AP
 */
private int ap;

/**
 * AP上限
 */
private int apMax;

/**
 * BP
 */
private int bp;

/**
 * BP上限
 */
private int bpMax;

/**
 * 卡片数量
 */
private int cardNum;

/**
 * 卡片数量上限
 */
private int cardMax;

/**
 * 金钱
 */
private int gold;

/**
 * 友情点
 */
private int fp;

/**
 * 水晶数量
 */
private int coin;

/**
 * 挂机地图
 */
private String bossIds;

private Date endTime;

private Date createTime;

/**
 * 备注
 */
private String remark;

@Override
public String toString()
{
return String.format("id:%S\ntgksId:%S\niosUuid:%S\nandroidUuid:%S\niosKey:%S\nandroidKey:%S\ntype:%S\nstatus:%S\nsessionId:%S\nvip:%S\nname:%S\nuserId:%S\nlv:%S\nap:%S\napMax:%S\nbp:%S\nbpMax:%S\ncardNum:%S\ncardMax:%S\ngold:%S\nfp:%S\ncoin:%S\n", id, tgksId, iosUuid, androidUuid, iosKey, androidKey, type, status, sessionId, vip, name, userId, lv, ap, apMax, bp, bpMax, cardNum, cardMax, gold, fp, coin);
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
 * @return 返回 iosUuid
 */
public String getIosUuid()
{
    return iosUuid;
}

/**
 * @param 对iosUuid进行赋值
 */
public void setIosUuid(String iosUuid)
{
    this.iosUuid = iosUuid;
}

/**
 * @return 返回 androidUuid
 */
public String getAndroidUuid()
{
    return androidUuid;
}

/**
 * @param 对androidUuid进行赋值
 */
public void setAndroidUuid(String androidUuid)
{
    this.androidUuid = androidUuid;
}

/**
 * @return 返回 iosKey
 */
public String getIosKey()
{
    return iosKey;
}

/**
 * @param 对iosKey进行赋值
 */
public void setIosKey(String iosKey)
{
    this.iosKey = iosKey;
}

/**
 * @return 返回 androidKey
 */
public String getAndroidKey()
{
    return androidKey;
}

/**
 * @param 对androidKey进行赋值
 */
public void setAndroidKey(String androidKey)
{
    this.androidKey = androidKey;
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
 * @return 返回 status
 */
public String getStatus()
{
    return status;
}

/**
 * @param 对status进行赋值
 */
public void setStatus(String status)
{
    this.status = status;
}

/**
 * @return 返回 sessionId
 */
public String getSessionId()
{
    return sessionId;
}

/**
 * @param 对sessionId进行赋值
 */
public void setSessionId(String sessionId)
{
    this.sessionId = sessionId;
}

/**
 * @return 返回 vip
 */
public String getVip()
{
    return vip;
}

/**
 * @param 对vip进行赋值
 */
public void setVip(String vip)
{
    this.vip = vip;
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
 * @return 返回 userId
 */
public String getUserId()
{
    return userId;
}

/**
 * @param 对userId进行赋值
 */
public void setUserId(String userId)
{
    this.userId = userId;
}

/**
 * @return 返回 lv
 */
public int getLv()
{
    return lv;
}

/**
 * @param 对lv进行赋值
 */
public void setLv(int lv)
{
    this.lv = lv;
}

/**
 * @return 返回 ap
 */
public int getAp()
{
    return ap;
}

/**
 * @param 对ap进行赋值
 */
public void setAp(int ap)
{
    this.ap = ap;
}

/**
 * @return 返回 apMax
 */
public int getApMax()
{
    return apMax;
}

/**
 * @param 对apMax进行赋值
 */
public void setApMax(int apMax)
{
    this.apMax = apMax;
}

/**
 * @return 返回 bp
 */
public int getBp()
{
    return bp;
}

/**
 * @param 对bp进行赋值
 */
public void setBp(int bp)
{
    this.bp = bp;
}

/**
 * @return 返回 bpMax
 */
public int getBpMax()
{
    return bpMax;
}

/**
 * @param 对bpMax进行赋值
 */
public void setBpMax(int bpMax)
{
    this.bpMax = bpMax;
}

/**
 * @return 返回 cardNum
 */
public int getCardNum()
{
    return cardNum;
}

/**
 * @param 对cardNum进行赋值
 */
public void setCardNum(int cardNum)
{
    this.cardNum = cardNum;
}

/**
 * @return 返回 cardMax
 */
public int getCardMax()
{
    return cardMax;
}

/**
 * @param 对cardMax进行赋值
 */
public void setCardMax(int cardMax)
{
    this.cardMax = cardMax;
}

/**
 * @return 返回 gold
 */
public int getGold()
{
    return gold;
}

/**
 * @param 对gold进行赋值
 */
public void setGold(int gold)
{
    this.gold = gold;
}

/**
 * @return 返回 fp
 */
public int getFp()
{
    return fp;
}

/**
 * @param 对fp进行赋值
 */
public void setFp(int fp)
{
    this.fp = fp;
}

/**
 * @return 返回 coin
 */
public int getCoin()
{
    return coin;
}

/**
 * @param 对coin进行赋值
 */
public void setCoin(int coin)
{
    this.coin = coin;
}

public String getBossIds()
{
    return bossIds;
}

public void setBossIds(String bossIds)
{
    this.bossIds = bossIds;
}

public String getRemark()
{
    return remark;
}

public void setRemark(String remark)
{
    this.remark = remark;
}

public Date getEndTime()
{
    return endTime;
}

public void setEndTime(Date endTime)
{
    this.endTime = endTime;
}

public Date getCreateTime()
{
    return createTime;
}

public void setCreateTime(Date createTime)
{
    this.createTime = createTime;
}

}