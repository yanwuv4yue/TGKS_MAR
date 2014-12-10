<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/WEB-INF/jsp/common/TGKSHeaderList.inc.jsp" %>
<input type="hidden" id="accountListSubmit" name="accountListSubmit" value="0" />
<div id="accountTableDiv" class="ui-widget">
    <table id="accountTable" class="ui-widget ui-widget-content">
        <thead>
            <tr class="ui-widget-header ">
                <th width="20"><input type="checkbox" id="allAccountId" name="allAccountId"  /></th>
				<th>招待ID</th>
                <th>UUID</th>
                <th>加密key</th>
                <th>状态</th>
                <th>UR数量</th>
                <th>标题</th>
                <th>水晶数量</th>
                <th>创建时间</th>
            </tr>
        </thead>
        <tbody>
			<s:iterator  value="list" var="evt">
				<tr>
					<td width="20"><input type="checkbox" name="accountId" value="<s:property value='#evt.id'/>" /></td>
					<td><b id="<s:property value='#evt.id'/>" class="accountUpdate"><s:property value="#evt.inviteCode"/></b></td>
                    <td><s:property value='#evt.uuid'/></td>
                    <td><s:property value='#evt.accountKey'/></td>
                    <td><s:property value='#evt.status'/></td>
                    <td>佣<s:property value='#evt.urNumA'/>壕<s:property value='#evt.urNumB'/>盗<s:property value='#evt.urNumC'/>姬<s:property value='#evt.urNumD'/></td>
                    <td><s:property value='#evt.title'/></td>
                    <td><s:property value='#evt.crystal'/></td>
                    <td><s:date name="#evt.createTime" format="yyyy-MM-dd HH:mm:ss"/></td>
				</tr>
			</s:iterator>
		</tbody>
    </table>
</div>
<script type="text/javascript">
	$(function() {
		$('#accountTable').longtable({
			'perPage' : 10
		});

		// 点击记录首栏进入更新操作
		$(".accountUpdate").click(function() {
			$("#accountManagerSubmit").val("1");
			$("#accountEdit").dialog("open");
			var edit = $.ajax({
				url : "../mar/editAccountPage.action?id=" + this.id,
				async : false
			});
			$("#accountForm").html(edit.responseText);
		});

		// 全选
		$("#allAccountId").click(function() {
			var checkbox = $("#accountTable :checkbox");
			for ( var i = 1; i < checkbox.length; i++) {
				if (checkbox[i].hidden == "") {
					checkbox[i].checked = checkbox[0].checked;
				}
			}
		});
	});
</script>
