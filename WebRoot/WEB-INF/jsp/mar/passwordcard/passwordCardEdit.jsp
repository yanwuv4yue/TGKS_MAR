<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/WEB-INF/jsp/common/TGKSHeaderList.inc.jsp" %>
<input type="hidden" name="passwordCardEvt.id" id="passwordCardId" value="${passwordCardEvt.id }" />
<table>
	<tr>
		<td>passwordCard</td>
		<td><input type="text" name="passwordCardEvt.password"" id="passwordCardPassword" class="text ui-widget-content ui-corner-all" value="${passwordCardEvt.password }" /></td>
	</tr>
</table>
<script type="text/javascript">
</script>