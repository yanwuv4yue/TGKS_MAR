package com.moemao.tgks.mar.account.entity;

import java.util.Date;

public class AccountEvt
{
/**
 * 表唯一主键
 */
private String id;

/**
 * UUID（登录用）
 */
private String uuid;

/**
 * 账号加密accountKey
 */
private String accountKey;

/**
 * 状态（0 新建；1 执行中；2 已完成；3 已售出）
 */
private String status;

/**
 * 招待ID
 */
private String inviteCode;

/**
 * 标题
 */
private String title;

/**
 * UR数量 佣兵
 */
private Integer urNumA;

/**
 * UR数量 富豪
 */
private Integer urNumB;

/**
 * UR数量 盗贼
 */
private Integer urNumC;

/**
 * UR数量 歌姬
 */
private Integer urNumD;

/**
 * 卡片ID（只包含UR SR）
 */
private String cardIds;

/**
 * 水晶数量
 */
private Integer crystal;

/**
 * 创建时间
 */
private Date createTime;

/**
 * 备注
 */
private String remark;

@Override
public String toString()
{
return String.format("id:%S\nuuid:%S\nkey:%S\nstatus:%S\ninviteCode:%S\ntitle:%S\nurNumA:%S\nurNumB:%S\nurNumC:%S\nurNumD:%S\ncardIds:%S\ncrystal:%S\ncreateTime:%S\nremark:%S\n", id, uuid, accountKey, status, inviteCode, title, urNumA, urNumB, urNumC, urNumD, cardIds, crystal, createTime, remark);
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
 * @return 返回 uuid
 */
public String getUuid()
{
    return uuid;
}

/**
 * @param 对uuid进行赋值
 */
public void setUuid(String uuid)
{
    this.uuid = uuid;
}

public String getAccountKey()
{
    return accountKey;
}

public void setAccountKey(String accountKey)
{
    this.accountKey = accountKey;
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
 * @return 返回 inviteCode
 */
public String getInviteCode()
{
    return inviteCode;
}

/**
 * @param 对inviteCode进行赋值
 */
public void setInviteCode(String inviteCode)
{
    this.inviteCode = inviteCode;
}

/**
 * @return 返回 title
 */
public String getTitle()
{
    return title;
}

/**
 * @param 对title进行赋值
 */
public void setTitle(String title)
{
    this.title = title;
}

/**
 * @return 返回 urNumA
 */
public Integer getUrNumA()
{
    return urNumA;
}

/**
 * @param 对urNumA进行赋值
 */
public void setUrNumA(Integer urNumA)
{
    this.urNumA = urNumA;
}

/**
 * @return 返回 urNumB
 */
public Integer getUrNumB()
{
    return urNumB;
}

/**
 * @param 对urNumB进行赋值
 */
public void setUrNumB(Integer urNumB)
{
    this.urNumB = urNumB;
}

/**
 * @return 返回 urNumC
 */
public Integer getUrNumC()
{
    return urNumC;
}

/**
 * @param 对urNumC进行赋值
 */
public void setUrNumC(Integer urNumC)
{
    this.urNumC = urNumC;
}

/**
 * @return 返回 urNumD
 */
public Integer getUrNumD()
{
    return urNumD;
}

/**
 * @param 对urNumD进行赋值
 */
public void setUrNumD(Integer urNumD)
{
    this.urNumD = urNumD;
}

/**
 * @return 返回 cardIds
 */
public String getCardIds()
{
    return cardIds;
}

/**
 * @param 对cardIds进行赋值
 */
public void setCardIds(String cardIds)
{
    this.cardIds = cardIds;
}

/**
 * @return 返回 crystal
 */
public Integer getCrystal()
{
    return crystal;
}

/**
 * @param 对crystal进行赋值
 */
public void setCrystal(Integer crystal)
{
    this.crystal = crystal;
}

/**
 * @return 返回 createTime
 */
public Date getCreateTime()
{
    return createTime;
}

/**
 * @param 对createTime进行赋值
 */
public void setCreateTime(Date createTime)
{
    this.createTime = createTime;
}

/**
 * @return 返回 remark
 */
public String getRemark()
{
    return remark;
}

/**
 * @param 对remark进行赋值
 */
public void setRemark(String remark)
{
    this.remark = remark;
}

}