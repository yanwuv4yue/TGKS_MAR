package com.moemao.tgks.mar.account.service;

import java.util.List;

import com.moemao.tgks.mar.account.entity.AccountEvt;
import com.moemao.tgks.mar.account.entity.AccountReq;

public interface AccountService
{
public List<AccountEvt> queryAccount(AccountReq accountReq);

public AccountEvt queryAccountById(String id);

public int addAccount(AccountEvt accountEvt);

public int updateAccount(AccountEvt accountEvt);

public int deleteAccount(List<String> ids);

}