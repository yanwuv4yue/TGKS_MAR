<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/WEB-INF/jsp/common/TGKSHeaderList.inc.jsp" %>
<input type="hidden" name="marzMapEvt.id" id="marzMapId" value="${marzMapEvt.id }" />
<table>
    <tr>
        <td>bossId</td>
        <td><input type="text" name="marzMapEvt.bossId" id="marzMapbossId" value="${marzMapEvt.bossId }" /></td>
        <td>BP消耗</td>
        <td><input type="text" name="marzMapEvt.bpCost" id="marzMapbpCost" value="${marzMapEvt.bpCost }" /></td>
    </tr>
    <tr>
        <td>名称</td>
        <td><input type="text" name="marzMapEvt.bossName" id="marzMapbossName" value="${marzMapEvt.bossName }" /></td>
        <td>VIP可见等级</td>
        <td><input type="text" name="marzMapEvt.vip" id="marzMapvip" value="${marzMapEvt.vip }" /></td>
    </tr>
</table>
<script type="text/javascript">
</script>