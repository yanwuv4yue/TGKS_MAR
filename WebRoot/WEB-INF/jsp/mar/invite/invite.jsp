<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ include file="/WEB-INF/jsp/common/TGKSHeaderManager.inc.jsp" %>
<style type="text/css">
#inviteReq{border:0px solid;}
#inviteReq td{border:0px solid;}
#inviteReq input{width:120px;}
#inviteReq select{width:120px;}
</style>
<input type="hidden" id="inviteManagerSubmit" name="inviteManagerSubmit" value="0" />
<div class="ui-widget">
    <form id="inviteReq" action="../mar/invite.action" method="post">
        <table>
            <tr>
                <td><label>招待ID：</label></td><td><input type="text" name="inviteCode" id="inviteCode" /></td>
                <td>
                
                </td>
                <td>
                <input type="submit" value="提交" />
                </td>
            </tr>
        </table>
    </form>
</div>



<script type="text/javascript">
$(document).ready(function(){
});
</script>
