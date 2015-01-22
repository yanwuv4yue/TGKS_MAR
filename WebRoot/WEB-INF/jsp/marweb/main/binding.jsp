<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/WEB-INF/jsp/common/TGKSHeaderList.inc.jsp" %>
<div id="marzAccountBinding">
    <table>
        <tr>
            <td><label>代码: </label></td><td><input type="text" id="bandingAccountId" /></td>
            <td>
                <button id="bindAccount">绑定</button>
            </td>
        </tr>
    </table>
</div>
<script type="text/javascript">
$(document).ready(function(){
    $( "#bindAccount" ).button().click(function() {
            // ajax调用删除action
            var options = { 
                url:"../marweb/marzAccountBinding.action?accountId=" + $("#bandingAccountId").val() , // 提交给哪个执行
                type:'POST', 
                success: function(){
                    alert("绑定成功");
                    var table=$.ajax({url:"../marweb/queryMarzAccountByTgksId.action", async:false});
                    $("#mainDiv").html(table.responseText);
                },
                error:function(){ 
                    alert("绑定失败"); 
                }
            };
            
            $("#marzAccountBinding").ajaxSubmit(options);
            return false;
    });
});
</script>