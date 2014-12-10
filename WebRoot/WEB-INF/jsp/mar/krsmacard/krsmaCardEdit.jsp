<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/WEB-INF/jsp/common/TGKSHeaderList.inc.jsp" %>
<input type="hidden" name="krsmaCardEvt.id" id="krsmaCardId" value="${krsmaCardEvt.id }" />
<table>
    <tr>
        <td>ID：</td>
        <td><input type="text" name="krsmaCardEvt.cardId" id="krsmaCardCardId" value="${krsmaCardEvt.cardId }" /></td>
        <td>名字：</td>
        <td><input type="text" name="krsmaCardEvt.name" id="krsmaCardName" value="${krsmaCardEvt.name }" /></td>
    </tr>
    <tr>
        <td>昵称：</td>
        <td colspan="3"><input type="text" name="krsmaCardEvt.nickName" id="krsmaCardNickName" value="${krsmaCardEvt.nickName }" /></td>
    </tr>
    <tr>
        <td>职业：</td>
        <td>
            <select name="krsmaCardEvt.type">
                <option value="1" <s:if test='krsmaCardEvt.type== 1"'>selected="selected"</s:if>>佣兵</option>
                <option value="2" <s:if test='krsmaCardEvt.type=="2"'>selected="selected"</s:if>>富豪</option>
                <option value="3" <s:if test='krsmaCardEvt.type=="3"'>selected="selected"</s:if>>盗贼</option>
                <option value="4" <s:if test='krsmaCardEvt.type=="4"'>selected="selected"</s:if>>歌姬</option>
            </select>
        </td>
        <td>稀有度：</td>
        <td>
            <select name="krsmaCardEvt.rarity">
                <option value="UR" <s:if test='krsmaCardEvt.rarity=="MR"'>selected="selected"</s:if>>UR</option>
                <option value="SR" <s:if test='krsmaCardEvt.rarity=="SR"'>selected="selected"</s:if>>SR</option>
            </select>
        </td>
    </tr>
    <tr>
        <td>卡图URL：</td>
        <td><input type="text" name="krsmaCardEvt.imageUrl" value="${krsmaCardEvt.imageUrl }" /></td>
        <td>图标URL：</td>
        <td><input type="text" name="krsmaCardEvt.iconUrl" value="${krsmaCardEvt.iconUrl }" /></td>
    </tr>
</table>
<script type="text/javascript">
</script>