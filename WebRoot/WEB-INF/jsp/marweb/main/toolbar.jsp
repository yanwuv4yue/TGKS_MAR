<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<input id="tgksId" type="hidden" value="#session.user_info.username"/>
<li style="width:280px;">
	<a>欢迎：<s:property value="#session.user_info.username"/></a>
	<a href="<%=basePath%>marweb">退出系统</a>
</li>
<!-- 
<li style="width:80px;">
	<div id="gameClassTag">
		<a><b>游戏分类</b></a>
	</div>
	<div id="gameClass" class="gameClass" style="display: none;">
		<a href="javascript:;" class="closeButton" title="关闭" hidefocus="true"></a>
		<a href="<%=basePath%>cgweb/loveLiveCard.action"><img class="logo" src="../resources/images/cgweb/logo_lovelive.png" /></a>
		<a href="<%=basePath%>cgweb/kssmaCard.action"><img class="logo" src="../resources/images/cgweb/logo_kssma.png" /></a>
	 </div>
</li>
 -->
<li style="width:80px;">
    <a id="marzCardUsePageA" href="#"><b>点卡充值</b></a>
</li>

<script type="text/javascript">
$(document).ready(function(){
    $("#marzCardUsePageA").click(function(){
        var table=$.ajax({url:"../marweb/marzCardUsePage.action", async:false});
        $("#mainDiv").html(table.responseText);
        var table2=$.ajax({url:"../marweb/queryMarzLogByTgksIdOnlyMarzCard.action", async:false});
        $("#logDiv").html(table2.responseText);
    });
    
    // 页面禁止右键
   $(document).bind("contextmenu",function(e){  
        return false;  
    }); 
});
</script>