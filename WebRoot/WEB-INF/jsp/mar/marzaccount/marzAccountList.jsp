<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/WEB-INF/jsp/common/TGKSHeaderList.inc.jsp" %>
<input type="hidden" id="marzAccountListSubmit" name="marzAccountListSubmit" value="0" />
<div id="marzAccountTableDiv" class="ui-widget">
    <table id="marzAccountTable" class="ui-widget ui-widget-content">
        <thead>
            <tr class="ui-widget-header ">
                <th width="20"><input type="checkbox" id="allMarzAccountId" name="allMarzAccountId"  /></th>
				<th>marzAccount</th>
            </tr>
        </thead>
        <tbody>
			<s:iterator  value="list" var="evt">
				<tr>
					<td width="20"><input type="checkbox" name="marzAccountId" value="<s:property value='#evt.id'/>" /></td>
					<td><b id="<s:property value='#evt.id'/>" class="marzAccountUpdate"><s:property value="#evt.name"/></b></td>
				</tr>
			</s:iterator>
		</tbody>
    </table>
</div>
<script type="text/javascript">
	$(function() {
		$('#marzAccountTable').longtable({
			'perPage' : 10
		});

		// 点击记录首栏进入更新操作
		$(".marzAccountUpdate").click(function() {
			$("#marzAccountManagerSubmit").val("1");
			$("#marzAccountEdit").dialog("open");
			var edit = $.ajax({
				url : "../mar/editMarzAccountPage.action?id=" + this.id,
				async : false
			});
			$("#marzAccountForm").html(edit.responseText);
		});

		// 全选
		$("#allMarzAccountId").click(function() {
			var checkbox = $("#marzAccountTable :checkbox");
			for ( var i = 1; i < checkbox.length; i++) {
				if (checkbox[i].hidden == "") {
					checkbox[i].checked = checkbox[0].checked;
				}
			}
		});
	});
</script>
