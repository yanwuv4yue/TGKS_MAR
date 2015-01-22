<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/WEB-INF/jsp/common/TGKSHeaderList.inc.jsp" %>
<table>
    <tr>
        <td>角色名</td>
        <td>${marzAccountEvt.name }</td>
        <td>状态</td>
        <td>
            <s:if test="marzAccountEvt.status == 0">离线</s:if>
            <s:elseif test="marzAccountEvt.status == 1">在线</s:elseif>
        </td>
        <td>
            <s:if test="marzAccountEvt.status == 0">
                <a class="button" href="../marweb/accountOnline.action">上线</a>
            </s:if>
            <s:elseif test="marzAccountEvt.status == 1">
                <a class="button" href="../marweb/accountOffline.action">下线</a>
            </s:elseif>
        </td>
        <td>
            <a class="button" href="../marweb/settingPage.action">挂机设置</a>
        </td>
    </tr>
    <tr>
        <td>角色ID</td>
        <td>${marzAccountEvt.userId }</td>
        <td>角色等级</td>
        <td>${marzAccountEvt.lv }</td>
        <td>VIP等级</td>
        <td>
	        <s:if test="marzAccountEvt.vip == 0">试用</s:if>
	        <s:if test="marzAccountEvt.vip == 1">普通</s:if>
	        <s:if test="marzAccountEvt.vip == 2">白金</s:if>
	        <s:if test="marzAccountEvt.vip == 3">钻石</s:if>
        </td>
    </tr>
    <tr>
        <td>AP</td>
        <td>${marzAccountEvt.ap }/${marzAccountEvt.apMax }</td>
        <td>BP</td>
        <td>${marzAccountEvt.bp }/${marzAccountEvt.bpMax }</td>
        <td>卡片数量</td>
        <td>${marzAccountEvt.cardNum }/${marzAccountEvt.cardMax }</td>
    </tr>
    <tr>
        <td>金钱</td>
        <td>${marzAccountEvt.gold }</td>
        <td>友情点</td>
        <td>${marzAccountEvt.fp }</td>
        <td>水晶</td>
        <td>${marzAccountEvt.coin }</td>
    </tr>
    <tr>
        <td>创建时间</td>
        <td><s:date name="marzAccountEvt.createTime" format="yyyy-MM-dd HH:mm:ss"/></td>
        <td>到期时间</td>
        <td><s:date name="marzAccountEvt.endTime" format="yyyy-MM-dd HH:mm:ss"/></td>
        <td colspan="2"></td>
    </tr>
    <tr>
        <td>战斗地图</td>
        <td colspan="5">${marzAccountEvt.bossIds }</td>
    </tr>
</table>