<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ include file="/WEB-INF/jsp/common/TGKSHeaderManager.inc.jsp" %>
<style type="text/css">
#accountReq{border:0px solid;}
#accountReq td{border:0px solid;}
#accountReq input{width:120px;}
#accountReq select{width:120px;}
</style>
<input type="hidden" id="accountManagerSubmit" name="accountManagerSubmit" value="0" />
<div class="ui-widget">
	<form id="accountReq" action="../mar/queryAccount.action" method="post">
		<table>
			<tr>
				<td><label>UUID: </label></td><td><input type="text" name="accountReq.uuid" /></td>
				<td>
				
				</td>
				<td>
				<button id="clearAccount">重置</button>
				<button id="queryAccount">查询</button>
				</td>
			</tr>
		</table>
	</form>
</div>

<button id="addAccount">新增</button>
<button id="deleteAccount">删除</button>
<button id="onAccount">启用</button>
<button id="offAccount">禁用</button>

<div id="accountDiv"></div>

<div id="accountEdit" title="Account Edit">
	<form id="accountForm" action="../mar/editAccount.action" method="post"></form>
</div>

<div id="accountConfirm" title="操作确认" hidden="hidden">
	<p><span class="ui-icon ui-icon-alert" style="float: left; margin: 0 7px 20px 0;"></span>记录将被删除且不可恢复，是否确认？</p>
</div>
<script type="text/javascript">
$(document).ready(function(){
	var table=$.ajax({url:"../mar/queryAccount.action",async:false});
	$("#accountDiv").html(table.responseText);
	
	function query()
	{
		var table=$.ajax({url:"../mar/queryAccount.action", data:$("#accountReq").formSerialize(), async:false});
		$("#accountDiv").html(table.responseText);
	}
	
	// 新增/更新窗口
	$( "#accountEdit" ).dialog({
		modal: true,
		height:500,
		width:750,
		autoOpen: false,
		show: "fold",
		hide: "fold",
		buttons:
		{ 
			"确定":function()
			{
				// 页面校验
				if (!accountFormCheck())
				{
					return false;
				}
				var form = $("#accountForm");
				form.submit();
			}, 
			"关闭": function()
			{
				$("#accountManagerSubmit").val("0");
				$("#accountEdit").dialog("close"); 
			} 
		}
	});
	
	// 提交表单
	$("#accountForm").submit(function()
	{
		if ($("#accountManagerSubmit").val() == "0")
		{
			return false;
		}
		
		$("#accountManagerSubmit").val("0");
		
		var options = { 
			url:"../mar/editAccount.action", // 提交给哪个执行
			type:'POST', 
			success: function(){
				$("#accountEdit").dialog("close");
				// 新增完毕刷新form
				query();
				alert("操作成功");
			},
			error:function(){ 
				$("#accountEdit").dialog("close"); 
				alert("操作失败"); 
			}
		};
		
		$("#accountForm").ajaxSubmit(options);
		
		return false; // 为了不刷新页面,返回false，反正都已经在后台执行完了，没事！
	});  
	
	// 新增按钮
	$( "#addAccount" ).button({
		icons: {
			primary: "ui-icon-plus"
			}
		}).click(function() {
		// 请求提交标志
		$("#accountManagerSubmit").val("1");
		$( "#accountEdit" ).dialog( "open" );
		var edit=$.ajax({url:"../mar/editAccountPage.action",async:false});
		$("#accountForm").html(edit.responseText);
		return false;
	});
	
	 // 删除按钮
	$( "#deleteAccount" ).button({
		icons: {
			primary: "ui-icon-minus"
			}
		}).click(function() {
		$("#accountManagerSubmit").val("1");
		// 获取选中的记录ids
		var ids = "";
		var array = document.getElementsByName("accountId");
		for (var i=0; i<array.length; i++)
	   	{
	   		if (array[i].checked)
  			{
	   			if (ids == "")
   				{
	   				ids += array[i].value;
   				}
	   			else
	   			{
	   				ids += "," + array[i].value;
	   			}
  			}
	   	}
		
		// 操作验证
		if (ids == "")
		{
			alert("请选择至少一条记录");
			$("#accountManagerSubmit").val("0");
			return false;
		}
		
		// ajax调用删除action
		var options = { 
			url:"../mar/deleteAccount.action?ids=" + ids , // 提交给哪个执行
			type:'POST', 
			success: function(){
				alert("删除成功");
				// 执行成功刷新form
				query();
			},
			error:function(){ 
				alert("删除失败"); 
			}
		};
		
		// 确认操作
		$("#accountConfirm").dialog({
			resizable: false,
            height:160,
            modal: true,
            buttons: {
                "确认": function() {
                	$( this ).dialog( "close" );
                	// 异步请求删除操作
                	$("#accountConfirm").ajaxSubmit(options);
                },
                "取消": function() {
                    $( this ).dialog( "close" );
                }
            }
		});
		return false;
	});
	 
	 // 启用按钮
	$( "#onAccount" ).button({
		icons: {
			primary: "ui-icon-check"
			}
		}).click(function() {
			$("#accountManagerSubmit").val("1");
			// 获取选中的记录ids
			var ids = "";
			var array = document.getElementsByName("accountId");
			for (var i=0; i<array.length; i++)
		   	{
		   		if (array[i].checked)
	  			{
		   			if (ids == "")
	   				{
		   				ids += array[i].value;
	   				}
		   			else
		   			{
		   				ids += "," + array[i].value;
		   			}
	  			}
		   	}
			
			// 操作验证
			if (ids == "")
			{
				alert("请选择至少一条记录");
				$("#accountManagerSubmit").val("0");
				return false;
			}
			
			// ajax调用删除action
			var options = { 
				url:"../mar/changeStatusAccount.action?status=1&ids=" + ids , // 提交给哪个执行
				type:'POST', 
				success: function(){
					// 执行成功刷新form
					query();
				},
				error:function(){ 
					alert("操作失败"); 
				}
			};
			
			$("#accountConfirm").ajaxSubmit(options);
			$("#accountManagerSubmit").val("0");
			return false;
	});
	 
	 // 停用按钮
	$( "#offAccount" ).button({
		icons: {
			primary: "ui-icon-close"
			}
		}).click(function() {
			$("#accountManagerSubmit").val("1");
			// 获取选中的记录ids
			var ids = "";
			var array = document.getElementsByName("accountId");
			for (var i=0; i<array.length; i++)
		   	{
		   		if (array[i].checked)
	  			{
		   			if (ids == "")
	   				{
		   				ids += array[i].value;
	   				}
		   			else
		   			{
		   				ids += "," + array[i].value;
		   			}
	  			}
		   	}
			
			// 操作验证
			if (ids == "")
			{
				alert("请选择至少一条记录");
				$("#accountManagerSubmit").val("0");
				return false;
			}
			
			// ajax调用删除action
			var options = { 
				url:"../mar/changeStatusAccount.action?status=0&ids=" + ids , // 提交给哪个执行
				type:'POST', 
				success: function(){
					// 执行成功刷新form
					query();
				},
				error:function(){ 
					alert("操作失败"); 
				}
			};
			
			$("#accountConfirm").ajaxSubmit(options);
			$("#accountManagerSubmit").val("0");
			return false;
	});
	
	 // 刷新按钮
	$( "#queryAccount" ).button().click(function() {
			query();
		return false;
	});
	 
	// 重置按钮
	$( "#clearAccount" ).button().click(function() {
			$("#accountReq").clearForm();
		return false;
	});
	
	// 页面校验
	function accountFormCheck()
	{
		return true;
	}
});
</script>
