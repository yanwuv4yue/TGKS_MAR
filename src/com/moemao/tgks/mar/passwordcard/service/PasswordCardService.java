package com.moemao.tgks.mar.passwordcard.service;

import java.util.List;

import com.moemao.tgks.mar.passwordcard.entity.PasswordCardEvt;
import com.moemao.tgks.mar.passwordcard.entity.PasswordCardReq;

public interface PasswordCardService
{
    public List<PasswordCardEvt> queryPasswordCard(PasswordCardReq passwordCardReq);
    
    public PasswordCardEvt queryPasswordCardById(String id);
    
    public int addPasswordCard(PasswordCardEvt passwordCardEvt);
    
    public int updatePasswordCard(PasswordCardEvt passwordCardEvt);
    
    public int deletePasswordCard(List<String> ids);
    
}