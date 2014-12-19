<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/WEB-INF/jsp/common/TGKSHeaderList.inc.jsp" %>
<input type="hidden" name="marzAccountEvt.id" id="marzAccountId" value="${marzAccountEvt.id }" />
<table>
	<tr>
		<td><label for="name">marzAccount</label></td>
		<td><input type="text" name="marzAccountEvt.name" id="marzAccountName" class="text ui-widget-content ui-corner-all" value="${marzAccountEvt.name }" /></td>
	</tr>
</table>
<script type="text/javascript">
</script>