package com.moemao.tgks.mar.account.service.impl;

import java.util.List;

import com.moemao.tgks.common.tool.CommonUtil;
import com.moemao.tgks.mar.account.dao.AccountDao;
import com.moemao.tgks.mar.account.entity.AccountEvt;
import com.moemao.tgks.mar.account.entity.AccountReq;
import com.moemao.tgks.mar.account.service.AccountService;
import com.moemao.tgks.mar.tool.MarConstant;
import com.moemao.tgks.mar.tool.MarUtil;

public class AccountServiceImpl implements AccountService
{
/**
 * ﻿AccountDao
 */
private AccountDao mar_accountDao;

public List<AccountEvt> queryAccount(AccountReq accountReq)
{
if (CommonUtil.isEmpty(accountReq.getSortSql()))
{
accountReq.setSortSql(" t.ID DESC");
}
return mar_accountDao.mar_queryAccount(accountReq);
}

public AccountEvt queryAccountById(String id)
{
AccountReq accountReq = new AccountReq();
accountReq.setId(id);
AccountEvt accountEvt = null;
List<AccountEvt> accountList = mar_accountDao.mar_queryAccount(accountReq);
if (!CommonUtil.isEmpty(accountList))
{
accountEvt = accountList.get(0);
}
return accountEvt;
}

public int addAccount(AccountEvt accountEvt)
{
    accountEvt.setId(MarUtil.createUniqueID());
    accountEvt.setStatus(MarConstant.ACCOUNT_STATUS_0);
    accountEvt.setCrystal(0);
    accountEvt.setUrNumA(0);
    accountEvt.setUrNumB(0);
    accountEvt.setUrNumC(0);
    accountEvt.setUrNumD(0);
    return mar_accountDao.mar_addAccount(accountEvt);
}

public int updateAccount(AccountEvt accountEvt)
{
return mar_accountDao.mar_updateAccount(accountEvt);
}

public int deleteAccount(List<String> ids)
{
return mar_accountDao.mar_deleteAccount(ids);
}

/**
 * @return 返回 mar_accountDao
 */
public AccountDao getMar_accountDao()
{
    return mar_accountDao;
}

/**
 * @param 对mar_accountDao进行赋值
 */
public void setMar_accountDao(AccountDao mar_accountDao)
{
    this.mar_accountDao = mar_accountDao;
}

}