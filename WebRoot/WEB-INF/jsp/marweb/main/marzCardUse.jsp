<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/WEB-INF/jsp/common/TGKSHeaderList.inc.jsp" %>
<div id="marzCardUseDiv">
    <table>
        <tr>
            <td><label>点卡号码: </label></td><td><input type="text" id="marzCardUsePassword" /></td>
            <td>
                <a id="marzCardUseButton" class="button" href="#">充值</a>
            </td>
            <td>
                <a id="returnAccountButton" class="button" href="#">返回</a>
            </td>
        </tr>
    </table>
</div>
<script type="text/javascript">
$(document).ready(function(){
    $( "#marzCardUseButton" ).click(function() {
        if ($("#marzCardUsePassword").val() == "")
        {
            alert("请填入点卡号码");
            return false;
        }
        
        var options = { 
            url:"../marweb/marzCardUse.action?password=" + $("#marzCardUsePassword").val() , // 提交给哪个执行
            type:'POST', 
            success: function(result){
                dealResult(result);
                log();
            },
            error:function(){ 
                alert("充值失败"); 
            }
        };
        
        $("#marzCardUseDiv").ajaxSubmit(options);
        return false;
    });
    
    $("#returnAccountButton").click(function(){
        var table=$.ajax({url:"../marweb/queryMarzAccountByTgksId.action", async:false});
        $("#mainDiv").html(table.responseText);
    });
    
    function dealResult(result)
    {
        if ("success!" == result)
        {
            alert("充值成功");
        }
        else
        {
            alert("点卡错误或已使用");
            return false;
        }
    }
});
</script>