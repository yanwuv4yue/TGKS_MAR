<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" isELIgnored="false" %>
<%@ include file="/WEB-INF/jsp/common/TGKSHeaderList.inc.jsp" %>
<div id="saveSettingDiv">
    <form id="saveSettingForm" action="../marweb/saveSetting.action">
        <table>
            <tr>
                <td>跑图</td>
                <td>
                    <select id="marzSettingEvt.explore" name="marzSettingEvt.explore">
                         <option value="0">关</option>
                         <option value="1" <s:if test="marzSettingEvt.explore == 1">selected="selected"</s:if>>开</option>
                    </select>
                </td>
                <td>卖卡</td>
                <td>
                    <select id="marzSettingEvt.cardSell" name="marzSettingEvt.cardSell">
                         <option value="0">关</option>
                         <option value="1" <s:if test="marzSettingEvt.cardSell == 1">selected="selected"</s:if>>开</option>
                    </select>
                </td>
                <td>卖普通卡</td>
                <td>
                    <select id="marzSettingEvt.cardSellCommon" name="marzSettingEvt.cardSellCommon">
                         <option value="0">关</option>
                         <option value="1" <s:if test="marzSettingEvt.cardSellCommon == 1">selected="selected"</s:if>>开</option>
                    </select>
                </td>
                <td>喂狗粮</td>
                <td>
                    <select id="marzSettingEvt.cardFusion" name="marzSettingEvt.cardFusion">
                         <option value="0">关</option>
                         <option value="1" <s:if test="marzSettingEvt.cardFusion == 1">selected="selected"</s:if>>开</option>
                    </select>
                </td>
            </tr>
            <tr>
                <td>战斗</td>
                <td>
                    <select id="marzSettingEvt.battle" name="marzSettingEvt.battle">
                         <option value="0">关</option>
                         <option value="1" <s:if test="marzSettingEvt.battle == 1">selected="selected"</s:if>>开</option>
                    </select>
                </td>
                <td>不浪费BP</td>
                <td>
                    <select id="marzSettingEvt.battleNowaste" name="marzSettingEvt.battleNowaste">
                         <option value="0">关</option>
                         <option value="1" <s:if test="marzSettingEvt.battleNowaste == 1">selected="selected"</s:if>>开</option>
                    </select>
                </td>
                <td></td>
                <td></td>
                <td></td>
                <td></td>
            </tr>
            <tr>
                <td colspan="6"></td>
                <td><a id="returnAccountButton" class="button" href="#">返回</a></td>
                <td><a id="saveSettingButton" class="button" href="#">保存</a></td>
            </tr>
        </table>
    </form>
</div>
<script type="text/javascript">
$(document).ready(function(){
    $("#returnAccountButton").click(function(){
        var table=$.ajax({url:"../marweb/queryMarzAccountByTgksId.action", async:false});
        $("#mainDiv").html(table.responseText);
    });
    
    $("#saveSettingButton").click(function(){
        $("#saveSettingForm").submit();
    });
    
    $("#saveSettingForm").submit(function()
    {
        var options = { 
            url:"../marweb/saveSetting.action", // 提交给哪个执行
            data:$("#saveSettingForm").formSerialize(),
            type:'POST', 
            success: function(){
                alert("保存成功");
            },
            error:function(){ 
                alert("保存失败"); 
            }
        };
        
        $("#saveSettingForm").ajaxSubmit(options);
        
        return false;
    });  
});
</script>