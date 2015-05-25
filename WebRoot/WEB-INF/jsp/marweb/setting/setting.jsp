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
                <td>优先拿水晶</td>
                <td>
                    <select id="marzSettingEvt.battleGetStone" name="marzSettingEvt.battleGetStone">
                         <option value="0">关</option>
                         <option value="1" <s:if test="marzSettingEvt.battleGetStone == 1">selected="selected"</s:if>>开</option>
                    </select>
                </td>
                <td>不浪费BP</td>
                <td>
                    <select id="marzSettingEvt.battleNowaste" name="marzSettingEvt.battleNowaste">
                         <option value="0">关</option>
                         <option value="1" <s:if test="marzSettingEvt.battleNowaste == 1">selected="selected"</s:if>>开</option>
                    </select>
                </td>
                <td>不浪费BP地图</td>
                <td>
                    <select id="marzSettingEvt.battleNowasteBossId" name="marzSettingEvt.battleNowasteBossId">
                         <option value="0" <s:if test="marzSettingEvt.battleNowasteBossId == 0">selected="selected"</s:if>>每日限定素材</option>
                         <s:iterator value="allMapList" var="evt" status="idx">
                            <s:if test="#evt.show == 1">
                                <option value="<s:property value='#evt.bossId'/>" <s:if test="marzSettingEvt.battleNowasteBossId == #evt.bossId">selected="selected"</s:if>><s:property value='#evt.bossName'/></option>
                            </s:if>
                         </s:iterator>
                    </select>
                </td>
            </tr>
            <tr>
                <td>战斗副本</td>
                <td colspan="7">
                    <table>
                        <tr>
		                    <s:iterator value="allMapList" var="evt" status="idx">
		                      <td>
		                        <s:if test="#evt.show == 1">
		                        <input type="checkbox" name="marzSettingEvt.bossIds" value="<s:property value='#evt.bossId'/>" <s:if test="#evt.check == 1">checked="checked"</s:if> />
		                        </s:if>
		                      </td>
		                      <td>
		                        <s:property value='#evt.bossName'/>
		                      </td>
                              <s:if test="#idx.index > 0  && (#idx.index + 1)%5 == 0">
                        </tr>
                        <tr>
                              </s:if>
		                    </s:iterator>
                        </tr>
                    </table>
                </td>
            </tr>
            <tr>
                <td colspan="4"></td>
                <td colspan="2"><a id="message" style="color:red;">请在页面上配置挂机方案 点击保存按钮</a></td>
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
                document.getElementById("message").innerHTML= "挂机设置保存成功";
            },
            error:function(){ 
                document.getElementById("message").innerHTML= "挂机设置保存失败";
            }
        };
        
        $("#saveSettingForm").ajaxSubmit(options);
        
        return false;
    });  
});
</script>