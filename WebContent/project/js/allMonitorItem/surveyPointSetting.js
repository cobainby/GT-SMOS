
//iframe高度

$(function() {
    //增加监测点
    var bodyHeight = $('#monitorBody').height();
    $("#addSurveyPointBtn").bind("click", function() {
        var monitorRowId = $('#monitorItemDG').jqGrid('getGridParam', 'selrow');
        var monitorItemUuid = $("#monitorItemDG").jqGrid('getRowData', monitorRowId).monitorItemUuid;
        var number = $("#monitorItemDG").jqGrid('getRowData', monitorRowId).number;
        //根据不同监测项，添加不同的表单
        //添加之前先清除原来的内容
        $("#spContentDiv").empty();
        var content;
        if (number == 1) {  //围护墙(边坡)顶部水平位移
            content = '<form class="surveyPointForm">'+'<table id="tableHeader" cellpadding="0" cellspacing="0">' + '<tr><th>监测点参数</th></tr>' + '</table>' + '<table class="tableEditDetail"  cellpadding="0" cellspacing="1">' + '<tr><th colspan="1">监测点编号：</th><td colspan="3"><input id="codeChar" name="codeChar" type="text" class="input" style="width:30%;" required></td></tr>' +
	            '<tr>'+
		        	'<th colspan="1">测点起始数：</th><td colspan="1"><input id="beginNum" class="input" name="beginNum" type="text"  style="width:95%;" required></td>'+
		        	'<th colspan="1">测点数：</th><td colspan="1"><input id="spCount" name="spCount" class="input" type="text"  style="width:95%;" required></td>'+
		        '</tr>'+    
	            '<tr>' +
                '<th colspan="1">报警设置：</th><td colspan="1"><input id="warningCB" name="warningCB" class="easyui-combobox" data-options="valueField:\'warningUuid\',textField:\'warningName\'" style="width:120px;" required></td><th colspan="1">虚拟断面：</th><td colspan="1"><input id="sectionCB" class="easyui-combobox" name="sectionCB" data-options="valueField:\'sectionUuid\',textField:\'sectionName\'" style="width:120px;" required></td>' +
                '</tr>' +
                '<tr><th colspan="1">初始累计值：</th><td colspan="3"><input id="originalTotalValue" name="originalTotalValue" type="text" class="input" style="width:60%;" required>mm</td>' +
                '</tr>' + '</table>'+'</form>';
        } else if (number == 4) { //测斜
            content = '<form class="surveyPointForm">'+'<table id="cxtableHeader" cellpadding="0" cellspacing="0">' + '<tr><th>监测点参数</th></tr>' + '</table>' + '<table id="cxTableEdit" class="tableEditDetail"  cellpadding="0" cellspacing="1">' + '<tr><th colspan="1">监测点编号：</th><td colspan="3"><input id="codeChar" name="codeChar" class="input" style="width:24%;" required></td></tr>' +
	            '<tr>'+
	            	'<th colspan="1">测点起始数：</th><td colspan="1"><input id="beginNum" name="beginNum" class="input"  style="width:75%;" required></td>'+
	            	'<th colspan="1">测点数：</th><td colspan="1"><input id="spCount" name="spCount" class="input"  style="width:92px;" required></td>'+
	            '</tr>'+
	            '<tr>' +
	                '<th colspan="1">报警设置：</th><td colspan="1"><input id="warningCB" class="easyui-combobox" data-options="valueField:\'warningUuid\',textField:\'warningName\'" style="width:100px;" required></td>' +
	                '<th colspan="1">测点深度：</th><td colspan="1"><input id="deep" name="deep" class="input" style="width:70%;" required>m</td>' +
                '</tr>' +
                '</table>'+'</form>';
        } else if (number == 18) { //锚索
            // $("#addPointSetting-modal").height(bodyHeight - 120);
            content = '<form class="surveyPointForm">'+'<table id="tableHeader" cellpadding="0" cellspacing="0">' + '<tr><th>监测点参数</th></tr>' + '</table>' + '<table class="tableEditDetail"  cellpadding="0" cellspacing="1">' +
                '<tr><th colspan="1">计算公式：</th><td colspan="3"><img src="project/image/maogan.png" ></img></td>' +
                '</tr></table><table class="tableEditDetail"  cellpadding="0" cellspacing="1">' +
                '<tr><th colspan="1" >备注：</th><td colspan="3">N:锚杆轴力(kN);K:锚索轴力计标定系数(kN/Hz<sup>2</sup>)' +
                '<br />fi:锚索轴力计监测频率(Hz);f<sub>0</sub>:锚索轴力计安装前初始频率(Hz)' + '</td>' +
                '</tr></table><table class="tableEditDetail"  cellpadding="0" cellspacing="1">' +
                '<tr><th colspan="1">监测点编号：</th><td colspan="1"><input id="codeChar" name="codeChar" class="input" style="width:95%;" required></td><th colspan="1">报警设置：</th><td colspan="1"><input id="warningCB" class="easyui-combobox" data-options="valueField:\'warningUuid\',textField:\'warningName\'" style="width:100px;"></td></tr>' +
                '<tr>' +
	                '<th colspan="1">设备型号：</th><td colspan="1"><input id="devModelCB"  class="easyui-combobox" data-options="valueField:\'devModelUuid\',textField:\'devModelName\'" style="width:100px;" required></td>' +
	                '<th colspan="1">设备编号：</th><td colspan="1"><input id="deviceTB" name="deviceTB" class="input" style="width:100px;" required></td>' +
                '</tr>' +
                '<tr>' +
	                '<th colspan="1">初始频率：</th><td colspan="1"><input id="frequency" name="frequency" class="input" style="width:70%;" required>Hz</td>' +
	                '<th colspan="1">初始累计值：</th><td colspan="1"><input id="originalTotalValueMT" name="originalTotalValueMT" class="input" style="width:80%;" required>KN</td>' +
                '</tr>' +
                '<tr>' +
	                '<th colspan="1">标定系数:</th>' +
	                '<td colspan="3"><input id="calibratedMod" name="calibratedMod" style="width:100px;" required>*10<sup><select id="calibratedModCB" class="easyui-combobox" style="width:40px;" required><option>-1</option><option>-2</option><option>-3</option><option>-4</option>'+
	                		'<option>-5</option><option>-6</option><option>-7</option></select></sup>/Hz<sup>2</sup></td>' +
                '</tr>' +
                '</table>'+'</form>';
        } else if (number == 12) { //水位
            content = '<form class="surveyPointForm">'+'<table id="tableHeader" cellpadding="0" cellspacing="0">' + '<tr><th>监测点参数</th></tr>' + '</table>' + '<table class="tableEditDetail"  cellpadding="0" cellspacing="1">' +
                '<tr><th colspan="1">监测点编号：</th><td colspan="1"><input id="codeChar" name="codeChar" class="input" style="width:95%;" required></td><th colspan="1">报警设置：</th><td colspan="1"><input id="warningCB"  class="easyui-combobox" data-options="valueField:\'warningUuid\',textField:\'warningName\'" style="width:120px;" required></td></tr>' +
                '<tr>' +
                '<th colspan="1">设备型号：</th><td colspan="1"><input id="devModel" name="devModel" class="easyui-combobox" data-options="valueField:\'devModelUuid\',textField:\'devModelName\'" style="width:120px;" required></td>' +
                '<th colspan="1">设备编号：</th><td colspan="1"><input id="swDevice" name="swDevice" class="input" style="width:120px;" required></td>' +
                '</tr>' +
                '<tr>' +
                '<th colspan="1">测点深度：</th><td colspan="1"><input id="deep" name="deep" class="input" style="width:80%;" required>m</td>' +
                '<th colspan="1">仪器初始模数：</th><td colspan="1"><input id="originalModule" name="originalModule" class="input" style="width:70%;" required>Hz<sup>2</sup>x<sup>-3</sup></td>' +
                '</tr>' +
                '<tr><th colspan="1">标定常数：</th><td colspan="1"><input id="constant" name="constant" class="input" style="width:75%;" required>mm/F</td><th colspan="1">初始累计值：</th><td colspan="1"><input id="originalTotalValue" name="originalTotalValue" class="input" style="width:75%;" required>mm</td></tr>' +
                '<tr>' +
                '<th colspan="1">选择MCU：</th><td colspan="3"><input id="mcuCB" class="easyui-combobox" data-options="valueField:\'mcuUuid\',textField:\'sn\'" style="width:120px;" required></td>' +
                '</tr>' +
                '<tr>' +
                '<th colspan="1">模块号：</th><td colspan="1"><input id="moduleNum" class="easyui-combobox" data-options="valueField:\'num\',textField:\'num\',editable:false" style="width:120px;" required></td>' +
                '<th colspan="1">通道号：</th><td colspan="1"><input id="channelNum" class="easyui-combobox" data-options="valueField:\'num\',textField:\'num\',editable:false" style="width:120px;" required></td>' +
                '</tr>' +
                '</table>'+'</form>';
        } else if (number == 5 || number == 6 || number == 8) { //5边坡竖向位移，6立柱,8周边环境竖向位移
            content = '<form class="surveyPointForm">'+'<table id="tableHeader" cellpadding="0" cellspacing="0">' + '<tr><th>监测点参数</th></tr>' + '</table>' + '<table class="tableEditDetail"  cellpadding="0" cellspacing="1">' +
                '<tr><th colspan="1">监测点编号：</th><td colspan="3"><input id="codeChar" class="input" style="width:30%;" required></td></tr>' +
                '<tr>'+
                	'<th colspan="1">测点起始数：</th><td colspan="1"><input id="beginNum" name="beginNum" class="input"  style="width:95%;" required></td>'+
                	'<th colspan="1">测点数：</th><td colspan="1"><input id="spCount" name="spCount" class="input"  style="width:95%;" required></td>'+
                '</tr>'+
                '<tr>' +
                '<th colspan="1">报警设置：</th><td colspan="1"><input id="warningCB" name="warningCB" class="easyui-combobox" data-options="valueField:\'warningUuid\',textField:\'warningName\'" style="width:120px;" required></td>' +
                '<th colspan="1">初始累计值：</th><td colspan="1"><input id="originalTotalValue" name="originalTotalValue" class="input" style="width:75%;" required>mm</td>' +
                '</tr>' +
                '</table>'+'</form>';
        } else if (number == 10) {  //周边管线竖向位移
            content = '<form class="surveyPointForm">'+'<table id="tableHeader" cellpadding="0" cellspacing="0">' + '<tr><th>监测点参数</th></tr>' + '</table>' + '<table class="tableEditDetail"  cellpadding="0" cellspacing="1">' +
                '<tr><th colspan="1">监测点编号：</th><td colspan="3"><input id="codeChar" name="codeChar" class="input" style="width:30%;" required></td></tr>' +
                '<tr>'+
	            	'<th colspan="1">测点起始数：</th><td colspan="1"><input id="beginNum" name="beginNum" class="input"  style="width:95%;" required></td>'+
	            	'<th colspan="1">测点数：</th><td colspan="1"><input id="spCount" name="spCount" class="input"  style="width:95%;" required></td>'+
	            '</tr>'+
                '<tr>' +
                '<th colspan="1">报警设置：</th><td colspan="1"><input id="warningCB" name="warningCB" class="easyui-combobox" data-options="valueField:\'warningUuid\',textField:\'warningName\'" style="width:120px;" required></td>' +
                '<th colspan="1">初始累计值：</th><td colspan="1"><input id="originalTotalValue" name="originalTotalValue" class="input" style="width:70%;" required>mm</td>' +
                '</tr>' +
                '</table>'+'</form>';
        } else if (number == 15) { //轴力
            content = '<form class="surveyPointForm">'+'<table id="tableHeader" cellpadding="0" cellspacing="0">' + '<tr><th>监测点参数</th></tr>' + '</table>' + '<table class="tableEditDetail"  cellpadding="0" cellspacing="1">' +
	            '<tr><th colspan="1">计算公式：</th><td colspan="3"><img id="formula" src="project/image/brace.png" ></img></td>' +
	            '</tr></table>'+
	            '<table class="tableEditDetail"  cellpadding="0" cellspacing="1">' +
                '<tr><th colspan="1" >备注：</th><td colspan="3"><label id="braceRemark" style="display:block">N:锚杆轴力(kN);  Es:钢弹性模量(kN/mm<sup>2</sup>);  A:钢支撑截面积(mm<sup>2</sup>)'+
				'<br />Kjz:第j个应变计标定系数(1/Hz<sup>2</sup>);  Fji:第j个应变计监测频率(Hz)<br />Fjo:第j个应变计安装后的初始频率(Hz)</label>'+
	            '<label id="concreteRemark" style="display:none">N:支撑轴力(kN); Ec、Es:混凝土和钢筋的弹性模量(kN/mm<sup>2</sup>)<br />Ac、As:混凝土截面积和钢筋截面面积(mm<sup>2</sup>);<br />Ac+As=A;'+
            		' A:支撑截面积(mm<sup>2</sup>); <br />Kjz:第j个应力计标定系数(1/Hz<sup>2</sup>);Fji 第j个应力计监测频率(Hz);<br />Fjo:第j个应力计安装后初始频率(Hz);</label>'+
	            '<label id="maoganRemark" style="display:none">N:钢支撑轴力(kN);   k 轴力计标定系数(kN/Hz<sup>2</sup>);<br />Fi:轴力计监测频率(Hz);   Fo:轴力计安装后的初始频率(Hz)</label>'+
	            '<label id="rebarRemark" style="display:none">N:支撑轴力(kN);   Ec、Es:混凝土和钢筋的弹性模量(kN/mm<sup>2</sup>);<br />Ac、As:混凝土截面积和钢筋截面面积(mm<sup>2</sup>);<br />  Ac+As=A;   A:支撑截面积(mm<sup>2</sup>)<br />'+
	            	'Kjz:第j个应力计标定系数(kN/Hz<sup>2</sup>);   Fji:第j个应力计监测频率(Hz)<br />Fjo:第j个应力计安装后初始频率(Hz)</label></td>' +
                '</tr></table>' + 
	            '<table id="stressTB" class="tableEditDetail"  cellpadding="0" cellspacing="1">' +
                '<tr><th colspan="1">监测点编号：</th><td colspan="1"><input id="code" name="code" class="input" style="width:95%;" required></td>' +
                '<th colspan="1">报警设置：</th><td colspan="1"><input id="warningCB" name="warningCB" class="easyui-combobox" data-options="valueField:\'warningUuid\',textField:\'warningName\'" style="width:120px;" required></td></tr>' +
                '<tr>' +
                '<th colspan="1">设备类型：</th><td colspan="3"><select id="devTypeCB" name="devTypeCB" class="easyui-combobox" style="width:180px;" required><option>钢支撑应变计</option><option>钢支撑轴力计</option>' +
                '<option>混凝土应变计</option><option>混凝土应力计</option></select></td>' +
                '</tr>' +
                '</table>'+'</form>'
        }
        //添加新的子内容
        $("#spContentDiv").append(content);
        //需要重新渲染dom元素
        $.parser.parse($("#spContentDiv"));
        //输入改变触发事件
        $('#deep').bind('input', function () {
        	if(number!=12){
        		createRows();
        	}
        });
        //获得监测项下的预警设置和断面设置
        $.post("/smosplat/getWarnings", { monitorItemUuid: monitorItemUuid }, function(data, status) {
            $("#warningCB").combobox("loadData", JSON.parse(data).warnings);
            //获取监测点设备编号
            if (number == 12) {
                // 获取水位计的设备型号
                $.post("/smosplat/getDeviceModelByDevType", { devTypeName: "水位计" }, function(data, status) {
                    $("#devModel").combobox("loadData", JSON.parse(data).rows);
                });

                $.post("/smosplat/getMcus", function(data, status) {
                    $("#mcuCB").combobox("loadData", JSON.parse(data).mcus);
                });
                $("#mcuCB").combobox({
                    onChange: function(n, o) {
                        var moduleNum = [{ "num": 1 }, { "num": 2 }, { "num": 3 }, { "num": 4 }];
                        var preModulesw=$("#moduleNum").combobox("setValue");
                        $("#moduleNum").combobox("loadData", moduleNum);
                        $("#moduleNum").combobox("setValue", moduleNum[0].num);
                        if(preModulesw==1){
                            var mcuUuid = $("#mcuCB").combobox('getValue');
                            // 获取使用了该MCU的设备以取得使用了的模块号通道号
                            $.post("/smosplat/getUsingPointNumByMcuAndModuleNum", { mcuUuid: mcuUuid, moduleNum: 1 }, function(data, status) {
                                var deviceData = JSON.parse(data).rows;
                                var pointNum = new Array();
                                for (var j = 1; j < 9; j++) {
                                    var existNum = false;
                                    for (var i = 0; i < deviceData.length; i++) {
                                        if (deviceData[i].moduleNum == 1 && deviceData[i].pointNum == j) {
                                            existNum = true;
                                        }
                                    }
                                    if (existNum == false) {
                                        pointNum.push({ "num": j });
                                    }
                                }
                                if(pointNum.length==0){
                                	$("#channelNum").combobox('setValue', "");
                                }else{
                                	$("#channelNum").combobox("loadData", pointNum);
                                    $("#channelNum").combobox('setValue', pointNum[0].num);
                                }
                                
                            });
                        }
                    }
                });
                // mcu下拉框改变验证模块号通道号
                $("#moduleNum").combobox({
                    onChange: function(n, o) {
                        var mcuUuid = $("#mcuCB").combobox('getValue');
                        // 获取使用了该MCU的设备以取得使用了的模块号通道号
                        $.post("/smosplat/getUsingPointNumByMcuAndModuleNum", { mcuUuid: mcuUuid, moduleNum: n }, function(data, status) {
                            var deviceData = JSON.parse(data).rows;
                            var pointNum = new Array();
                            for (var j = 1; j < 9; j++) {
                                var existNum = false;
                                for (var i = 0; i < deviceData.length; i++) {
                                    if (deviceData[i].moduleNum == n && deviceData[i].pointNum == j) {
                                        existNum = true;
                                    }
                                }
                                if (existNum == false) {
                                    pointNum.push({ "num": j });
                                }
                            }
                            if(pointNum.length==0){
                            	$("#channelNum").combobox('setValue', "");
                            }else{
                            	$("#channelNum").combobox("loadData", pointNum);
                                $("#channelNum").combobox('setValue', pointNum[0].num);
                            }
                        });
                    }
                });
            }else if(number==18){
            	// 获取锚索设备的设备型号
                $.post("/smosplat/getDeviceModelByDevType", { devTypeName: "锚索计" }, function(data, status) {
                    $("#devModelCB").combobox("loadData", JSON.parse(data).rows);
                });
            }

            $.post("/smosplat/getSections", { monitorItemUuid: monitorItemUuid }, function(data, status) {
                $("#sectionCB").combobox("loadData", JSON.parse(data).sections);
                //判断该模态框高度是否大于iframe高度
                $("#surveyPointInfoDialog").modal("show");
            });

        });
        $("#devTypeCB").combobox({
            onChange: function(n, o) {
            	if(n=="钢支撑应变计"){
            		$("#formula").attr("src","project/image/brace.png");
            		document.getElementById("braceRemark").style.display="block";
            		document.getElementById("concreteRemark").style.display="none";
            		document.getElementById("maoganRemark").style.display="none";
            		document.getElementById("rebarRemark").style.display="none";
            	}else if(n=="钢支撑轴力计"){
            		$("#formula").attr("src","project/image/maogan.png");
            		document.getElementById("braceRemark").style.display="none";
            		document.getElementById("concreteRemark").style.display="block";
            		document.getElementById("maoganRemark").style.display="none";
            		document.getElementById("rebarRemark").style.display="none";
            	}else if(n=="混凝土应变计"){
            		$("#formula").attr("src","project/image/concrete.png");
            		document.getElementById("braceRemark").style.display="none";
            		document.getElementById("concreteRemark").style.display="none";
            		document.getElementById("maoganRemark").style.display="block";
            		document.getElementById("rebarRemark").style.display="none";
            	}else if(n=="混凝土应力计"){
            		$("#formula").attr("src","project/image/rebar.png");
            		document.getElementById("braceRemark").style.display="none";
            		document.getElementById("concreteRemark").style.display="none";
            		document.getElementById("maoganRemark").style.display="none";
            		document.getElementById("rebarRemark").style.display="block";
            	}
            }
        });
    });

    //增加监测点保存
    $("#saveSurveyPointBtn").bind("click", function() {
        var monitorRowId = $('#monitorItemDG').jqGrid('getGridParam', 'selrow');
        var monitorItemUuid = $("#monitorItemDG").jqGrid('getRowData', monitorRowId).monitorItemUuid;
        var number = $("#monitorItemDG").jqGrid('getRowData', monitorRowId).number;
      //先验证表单，如果表单验证不通过则不提交
        var val = $(".surveyPointForm").valid();
        if (!val) {
            return;
        }
        //获得参数
        var params = {};
        if (number != 15) {
            params = {
                codeChar: $("#codeChar").val(),
                spCount: $("#spCount").val() == undefined ? 0 : $("#spCount").val(),
                originalTotalValue: $("#originalTotalValue").val()
            };
        }
        var addUrl = "";
        if (number == 1) {
            addUrl = "addSP_WYSs";
            params["beginNum"] = $("#beginNum").val();
            params["section.sectionUuid"] = $("#sectionCB").combobox("getValue");
        } else if (number == 18) {
            addUrl = "addSP_MTs";
            var excal = $("#calibratedMod").val();
            var topcal = $("#calibratedModCB").combobox('getValue');
            params["code"] = $("#codeChar").val();
            params["deviceType"] = "锚索计";
            params["frequency"] = $("#frequency").val();
            params["devCode"] =  $("#deviceTB").val();
            params["deviceModel"] =  $("#devModelCB").combobox('getValue');
            params["originalTotalValue"] = $("#originalTotalValueMT").val();
            params["calibratedMod"] = excal * Math.pow(10, topcal * 1);
            
        } else if (number == 12) {
            addUrl = "addSP_SWs";
            params["deviceSn"] = $("#swDevice").val();
            params["code"] = $("#codeChar").val();
            params["constant"] = $("#constant").val();
            params["originalModule"] = $("#originalModule").val();
            params["deep"] = $("#deep").val();
            params["devModelUuid"] = $("#devModel").combobox("getValue");
            params["mcuUuid"] = $("#mcuCB").combobox("getValue");
            params["moduleNum"] = $("#moduleNum").combobox("getValue");
            params["channelNum"] = $("#channelNum").combobox("getValue");
        } else if (number == 4) {
            addUrl = "addSP_CXs";
            params["beginNum"] = $("#beginNum").val();
            params["deep"] = $("#deep").val();
            params["originalTotalValue"]=getCXOriginalTotalValue($("#deep").val(),0);
//            params["name"] = $("#name").val();
//            params["mcuUuid"] = $("#mcuCB").combobox("getValue");
//            params["deviceModel"] =  $("#devModelCB").combobox('getValue');
//            params["deviceSn"] = $("#deviceTB").val();
//            params["startValue"] = $("#startValue").val();
//            params["gap"] = $("#gap").val();
//            var excal = $("#calibratedMod").val();
//            var topcal = $("#calibratedModCB").combobox('getValue');
//            params["calibratedMod"] = excal * Math.pow(10, topcal * 1);
        } else if (number == 5) {
            addUrl = "addSP_WYDs";
            params["beginNum"] = $("#beginNum").val();
        }else if (number == 6) {
            addUrl = "addSP_LZs";
            params["beginNum"] = $("#beginNum").val();
        } else if (number == 8) {
            addUrl = "addSP_SMs";
            params["beginNum"] = $("#beginNum").val();
        } else if (number == 10) {
            addUrl = "addSP_ZGDs";
            params["beginNum"] = $("#beginNum").val();
        } else if (number == 15) {
            addUrl = "addSP_zc";
            params = {
                code: $("#code").val(),
                deviceType: $("#devTypeCB").combobox("getValue")
            };
        }

        params["monitorItem.monitorItemUuid"] = monitorItemUuid;
        params["warning.warningUuid"] = $("#warningCB").combobox("getValue");
        //获取添加的监测点数据
        $.post("/smosplat/" + addUrl, params, function(data, status) {
            var dataObj = JSON.parse(data);
            if (dataObj.result == 0) {
                var addedEntities = dataObj.surveyPoints;
                for (var i = 0; i < addedEntities.length; i++) {
                    //监测点的表格行数
                    var gridNumbers = $("#surveyPointDG").jqGrid('getRowData').length;
                    $("#surveyPointDG").jqGrid('addRowData', i + gridNumbers + 1, addedEntities[i]);
                }
                $("#surveyPointInfoDialog").modal("hide");
                swal({title:"添加成功！",type:"success"});
            } else {
            	swal({title:"添加失败！",text:dataObj.msg,type:"error"});
            }
        });
    });
    //修改监测点保存
    $("#updateSurveyPointBtn").bind("click", function() {
        var monitorRowId = $('#monitorItemDG').jqGrid('getGridParam', 'selrow');
        var monitorItemUuid = $("#monitorItemDG").jqGrid('getRowData', monitorRowId).monitorItemUuid;
        var number = $("#monitorItemDG").jqGrid('getRowData', monitorRowId).number;
        var surveyRowId = $('#surveyPointDG').jqGrid('getGridParam', 'selrow');
        var spItem = $("#surveyPointDG").jqGrid('getRowData', surveyRowId);
        var dataDG=$("#surveyPointDG").jqGrid('getRowData');
      //先验证表单，如果表单验证不通过则不提交
        var val = $(".editSurveyPointForm").valid();
        if (!val) {
            return;
        }
        //获得参数
        var updateUrl = "";
        var params = {};
            params["code"] = $("#codeCharForUdate").val()+($("#beginNumForUpdate").val()== undefined ? "" : $("#beginNumForUpdate").val());
            params["codeChar"] = $("#codeCharForUdate").val();
            params["originalTotalValue"] = $("#originalTotalValueUpdate").val()== undefined ? 0 : $("#originalTotalValueUpdate").val();
        if (number == 1) {
            updateUrl = "updateSP_WYSs";
            params["section.sectionUuid"] = $("#sectionCBForUpdate").combobox("getValue");
        } else if (number == 4) {
            updateUrl = "updateSP_CXs";
            params["deep"] = $("#deepForUpdate").val();
            params["originalTotalValue"]=getCXOriginalTotalValue($("#deepForUpdate").val(),1);
        } else if (number == 5) {
            updateUrl = "updateSP_WYDs";
        }else if (number == 6) {
            updateUrl = "updateSP_LZs";
        } else if (number == 8) {
            updateUrl = "updateSP_SMs";
        } else if (number == 12) {
            updateUrl = "updateSP_SWs";
            params["device.deviceUuid"] = spItem['device.deviceUuid'];
            params["deviceSn"] = $("#swDeviceForUpdate").val();
            params["code"] = $("#codeCharForUdate").val();
            params["constant"] = $("#constantForUpdate").val();
            params["originalModule"] = $("#originalModuleForUpdate").val();
            params["deep"] = $("#deepForUpdate").val();
            params["devModelUuid"] = $("#devModelForUpdate").combobox("getValue");
            params["mcuUuid"] = $("#mcuCBForUpdate").combobox("getValue");
            params["moduleNum"] = $("#moduleNumForUpdate").combobox("getValue");
            params["channelNum"] = $("#channelNumForUpdate").combobox("getValue");
        } else if (number == 10) {
            updateUrl = "updateSP_ZGDs";
        } else if (number == 15) {
        	params["code"] = $("#codeCharForUdate").val()
            params["deviceType"] = spItem.deviceType;
        } else if (number == 18) {
            updateUrl = "updateSP_MTs";
            var excal = $("#calibratedModUpdate").val();
            var topcal = $("#calibratedModCBUpdate").combobox('getValue');
            params["code"] = $("#codeCharForUdate").val();
            params["deviceType"] = "锚索计";
            params["frequency"] = $("#frequencyUpdate").val();
            params["devCode"] =  $("#deviceTBUpdate").val();
            params["deviceModel"] =  $("#devModelCBUpdate").combobox('getValue');
            params["originalTotalValue"] = $("#originalTotalValueMTUpdate").val();
            params["calibratedMod"] = excal * Math.pow(10, topcal * 1);
        }
        params["monitorItem.monitorItemUuid"] = monitorItemUuid;
        params["surveyPointUuid"] = spItem.surveyPointUuid;
        params["warning.warningUuid"] = $("#warningCBForUpdate").combobox("getValue");

        if (number == 15) {
            $.post("/smosplat/updateSP_zc", params, function(data, status) {
                var dataObj = JSON.parse(data);
                if (dataObj.result == 0) {
                    spItem["warning.warningUuid"] = $("#warningCBForUpdate").combobox("getValue");
                    spItem["warning.warningName"] = $("#warningCBForUpdate").combobox("getText");
                    $("#surveyPointDG").jqGrid('setCell', surveyRowId, 'code', params.code);
                    $("#surveyPointDG").jqGrid('setCell', surveyRowId, 'warning.warningUuid', spItem["warning.warningUuid"]);
                    $("#surveyPointDG").jqGrid('setCell', surveyRowId, 'warning.warningName', spItem["warning.warningName"]);
                    $("#updateSurveyPointInfoDialog").modal("hide");
                    swal({title:"修改成功！",type:"success"});
                } else {
                	swal({title:"修改失败！",text:dataObj.msg,type:"error"});
                }
            });
        } else {
            $.post("/smosplat/" + updateUrl, params, function(data, status) {
                var dataObj = JSON.parse(data);
                if (dataObj.result == 0) {
                    if (number == 1) {
                        spItem["section.sectionUuid"] = $("#sectionCBForUpdate").combobox("getValue");
                        spItem["section.sectionName"] = $("#sectionCBForUpdate").combobox("getText");
                        $("#surveyPointDG").jqGrid('setCell', surveyRowId, 'section.sectionUuid', spItem["section.sectionUuid"]);
                        $("#surveyPointDG").jqGrid('setCell', surveyRowId, 'section.sectionName', spItem["section.sectionName"]);
                    }
                    if(number == 12){
//                    	 params["device.deviceUuid"] = spItem['device.deviceUuid'];
                    	$("#surveyPointDG").jqGrid('setCell', surveyRowId, 'device.deviceModel.devModelUuid', params.devModelUuid);
                    	$("#surveyPointDG").jqGrid('setCell', surveyRowId, 'device.deviceModel.devModelName', $("#devModel").combobox("getText"));
                    	$("#surveyPointDG").jqGrid('setCell', surveyRowId, 'device.sn', params.deviceSn);
                    	$("#surveyPointDG").jqGrid('setCell', surveyRowId, 'deep', params.deep);
                    	$("#surveyPointDG").jqGrid('setCell', surveyRowId, 'originalModule', params.originalModule);
                    	$("#surveyPointDG").jqGrid('setCell', surveyRowId, 'constant', params.constant);
                    	$("#surveyPointDG").jqGrid('setCell', surveyRowId, 'device.mcu.mcuUuid', params.mcuUuid);
                    	$("#surveyPointDG").jqGrid('setCell', surveyRowId, 'device.moduleNum', params.moduleNum);
                    	$("#surveyPointDG").jqGrid('setCell', surveyRowId, 'device.pointNum', params.channelNum);
                    }
                    if(number == 4){
                    	$("#surveyPointDG").jqGrid('setCell', surveyRowId, 'deep', params.deep);
                    }
                    if(number==18){
                    	$("#surveyPointDG").jqGrid('setCell', surveyRowId, 'frequency', params.frequency);
                        $("#surveyPointDG").jqGrid('setCell', surveyRowId, 'calibratedMod', params.calibratedMod);
                    	$("#surveyPointDG").jqGrid('setCell', surveyRowId, 'devCode', params.devCode);
                        $("#surveyPointDG").jqGrid('setCell', surveyRowId, 'calibratedMod', params.calibratedMod);
                        $("#surveyPointDG").jqGrid('setCell', surveyRowId, 'originalTotalValue', params.originalTotalValue);
                    }
                    $("#surveyPointDG").jqGrid('setCell', surveyRowId, 'code', params.code);
                    $("#surveyPointDG").jqGrid('setCell', surveyRowId, 'codeChar', params.codeChar);
                    $("#surveyPointDG").jqGrid('setCell', surveyRowId, 'originalTotalValue', params.originalTotalValue);
                    $("#surveyPointDG").jqGrid('setCell', surveyRowId, 'warning.warningUuid', $("#warningCBForUpdate").combobox("getValue"));
                    $("#surveyPointDG").jqGrid('setCell', surveyRowId, 'warning.warningName', $("#warningCBForUpdate").combobox("getText"));
                    $("#updateSurveyPointInfoDialog").modal("hide");
                    swal({title:"修改成功！",type:"success"});
                } else {
                	swal({title:"修改失败！",text:dataObj.msg,type:"error"});
                }
            });
        }

    });

    //设备设置增加按钮
    $("#addSP_zcBtn").bind("click", function() {
        var monitorRowId = $('#monitorItemDG').jqGrid('getGridParam', 'selrow');
        var number = $("#monitorItemDG").jqGrid('getRowData', monitorRowId).number;
        var spId = $("#surveyPointDG").jqGrid('getGridParam', 'selrow');
        var spData = $("#surveyPointDG").jqGrid('getRowData', spId);
        $("#spZCContentDiv").empty();
        var content;
        if(number == 4){
            content = '<form class="spDsForm">'+'<table id="tableHeader" cellpadding="0" cellspacing="0">' + '<tr><th>监测点参数</th></tr>' + '</table>' + '<table class="tableEditDetail"  cellpadding="0" cellspacing="1">' +
            '<tr>' +
	        	'<th colspan="1">测斜仪名称：</th><td colspan="3"><input id="name" name="name" class="input" style="width:100px;" required></td>' +
	        	'</tr>' +
	        '<tr>' +
	            '<th colspan="1">设备型号：</th><td colspan="1"><input id="devModel"  class="easyui-combobox" data-options="valueField:\'devModelUuid\',textField:\'devModelName\'" style="width:100px;" required></td>' +
	            '<th colspan="1">设备编号：</th><td colspan="1"><input id="deviceTB" name="deviceTB" class="input" style="width:92px;" required></td>' +
	        '</tr>' +
	        '<tr>' +
            '<th colspan="1">选择MCU：</th><td colspan="1"><input id="mcuCB" class="easyui-combobox" data-options="valueField:\'mcuUuid\',textField:\'sn\'" style="width:100px;" required></td>' +
	        	'<th colspan="1">间隔:  </th><td colspan="1"><select id="gap" class="easyui-combobox" style="width:92px;" required><option>0.5</option><option>1</option></select>m</td>' +
	        '</tr>' +
	        '<th colspan="1">标定系数:</th>' +
	            '<td colspan="3"><input id="calibratedMod" name="calibratedMod" style="width:100px;" required>*10<sup><select id="calibratedModCB" class="easyui-combobox" style="width:40px;" required><option>-1</option><option>-2</option><option>-3</option><option>-4</option>'+
	            		'<option>-5</option><option>-6</option><option>-7</option></select></sup>/Hz<sup>2</sup></td>' +
	        '</tr>'+
            '</table>'+'</form>';
        	}else if (number == 15) {
            if (spData.deviceType == "混凝土应变计") {
                content = '<form class="spDsForm">'+'<table id="tableHeader" cellpadding="0" cellspacing="0">' + '<tr><th>监测点参数</th></tr>' + '</table>' + '<table class="tableEditDetail"  cellpadding="0" cellspacing="1">' +
                    '<tr><th colspan="1">设备名称：</th><td colspan="3"><input id="name" name="name" class="input" style="width:100px;" required></td></tr>' +
                    '<tr>' +
                    '<th colspan="1">设备型号：</th><td colspan="1"><input id="devModelCB" name="devModelCB" class="easyui-combobox" data-options="valueField:\'devModelUuid\',textField:\'devModelName\'" style="width:100px;" required></td>' +
                    '<th colspan="1">设备编号：</th><td colspan="1"><input id="deviceTB" name="deviceTB" class="input" style="width:100px;" required></td>' +
                    '</tr>' +
                    '<tr>' +
                    '<th colspan="1">初始模数值：</th><td colspan="1"><input id="startModValue" name="startModValue" class="input" style="width:70%;" required>mm/F</td>' +
                    '<th colspan="1">初始温度值：</th><td colspan="1"><input id="startTemperature" name="startTemperature" class="input" style="width:80%;" required><sup>。</sup>C</td>' +
                    '</tr>' +
                    '<tr>' +
                    '<th colspan="1">标定系数:</th>' +
                    '<td colspan="3"><input id="calibratedMod" class="input" name="calibratedMod" style="width:100px;" required>*10<sup><select id="calibratedModCB" class="easyui-combobox" style="width:40px;" required><option>-1</option><option>-2</option><option>-3</option><option>-4</option>'+
            			'<option>-5</option><option>-6</option><option>-7</option></select></sup>/Hz<sup>2</sup></td>' +
                    '</tr>' +
                    '<tr>' +
                    '<th colspan="1">钢筋计总面积：</th><td colspan="1"><input id="totalArea" name="totalArea" class="input" style="width:70%;" required>mm<sup>2</sup></td>' +
                    '<th colspan="1">支撑截面面积：</td><td colspan="1"><input id="sectionArea" name="sectionArea" class="input" style="width:70%;" required>mm<sup>2</sup></td></tr>' +
                    '<tr>' +
                    '<th colspan="1">混凝土弹性模量：</th><td colspan="1"><input id="ec" name="ec" class="input" style="width:50%;" required>kN/mm<sup>2</sup></td><th colspan="1">钢筋弹性模量：</th><td colspan="1"><input id="es" style="width:50%;" required>kN/mm<sup>2</sup></td></tr>' +
                    '<tr>' +
                    '<th colspan="1">计算值：</th><td colspan="2"><input id="calculatedValue" name="calculatedValue" class="input" style="width:95%;" required></td><th colspan="1"><a id="toCalculate" href="#" class="easyui-linkbutton" required>值计算</a></td>' +
                    '</tr>' +
                    '<tr>' +
                    '<th colspan="1">选择MCU：</th><td colspan="3"><input id="mcuB" name="mcuB" class="easyui-combobox" data-options="valueField:\'mcuUuid\',textField:\'sn\'" style="width:100px;" required></td>' +
                    '</tr>' +
                    '<tr>' +
                    '<th colspan="1">模块号：</th><td colspan="1"><input id="moduleNumCB" name="moduleNumCB" class="easyui-combobox" data-options="valueField:\'num\',textField:\'num\',editable:false" style="width:100px;" required></td>' +
                    '<th colspan="1">通道号：</th><td colspan="1"><input id="channelNumCB" name="channelNumCB" class="easyui-combobox" data-options="valueField:\'num\',textField:\'num\',editable:false" style="width:100px;" required></td>' +
                    '</tr>' +
                    '</table>'+'</form>';
            } else if (spData.deviceType == "混凝土应力计") {
                content = '<form class="spDsForm">'+'<table id="tableHeader" cellpadding="0" cellspacing="0">' + '<tr><th>监测点参数</th></tr>' + '</table>' + '<table class="tableEditDetail"  cellpadding="0" cellspacing="1">' +
                    '<tr><th colspan="1">设备名称：</th><td colspan="3"><input id="name" name="name" class="input" style="width:100px;" required></td></tr>' +
                    '<tr>' +
                    '<th colspan="1">设备型号：</th><td colspan="1"><input id="devModelCB" name="devModelCB"  class="easyui-combobox" data-options="valueField:\'devModelUuid\',textField:\'devModelName\'" style="width:100px;" required></td>' +
                    '<th colspan="1">设备编号：</th><td colspan="1"><input id="deviceTB" name="deviceTB" class="input" style="width:100px;" required></td>' +
                    '</tr>' +
                    '<tr>' +
                    '<th colspan="1">初始模数值：</th><td colspan="1"><input id="startModValue" name="startModValue" class="input" style="width:70%;" required>mm/F</td>' +
                    '<th colspan="1">初始温度值：</th><td colspan="1"><input id="startTemperature" name="startTemperature" class="input" style="width:80%;" required><sup>。</sup>C</td>' +
                    '</tr>' +
                    '<tr>' +
                    '<th colspan="1">标定系数:</th>' +
                    '<td colspan="3"><input id="calibratedMod" class="input"  style="width:100px;" required>*10<sup><select id="calibratedModCB" class="easyui-combobox" style="width:40px;" required><option>-1</option><option>-2</option><option>-3</option><option>-4</option>'+
            		'<option>-5</option><option>-6</option><option>-7</option></select></sup>/Hz<sup>2</sup></td>' +
                    '</tr>' +
                    '<tr>' +
                    '<th colspan="1">钢筋总面积：</th><td colspan="1"><input id="totalArea" name="totalArea" class="input" style="width:70%;" required>mm<sup>2</sup></td>' +
                    '<th colspan="1">支撑截面面积：</th><td colspan="1"><input id="sectionArea" name="sectionArea" class="input" style="width:60%;" required>mm<sup>2</sup></td></tr>' +
                    '<tr>' +
                    '<tr>' +
                    '<th colspan="1">钢筋计面积：</th><td colspan="3"><input id="esArea" name="esArea" class="input" style="width:70%;" required>mm<sup>2</sup></td></tr>' +
                    '<tr>' +
                    '<th colspan="1">混凝土弹性模量：</th><td colspan="1"><input id="ec" name="ec" class="input" style="width:50%;" required>kN/mm<sup>2</sup></td><th colspan="1" >钢筋弹性模量：</th><td colspan="1"><input id="es" name="es" class="input" style="width:50%;" required>kN/mm<sup>2</sup></td></tr>' +
                    '<tr>' +
                    '<th colspan="1">计算值：</th><td colspan="2"><input id="calculatedValue" name="calculatedValue" class="input" style="width:95%;" required></td><td colspan="1"><a id="toCalculate" href="#" class="easyui-linkbutton" >值计算</a></td>' +
                    '</tr>' +
                    '<tr>' +
                    '<th colspan="1">选择MCU：</th><td colspan="3"><input id="mcuB" class="easyui-combobox" data-options="valueField:\'mcuUuid\',textField:\'sn\'" style="width:100px;" required></td>' +
                    '</tr>' +
                    '<tr>' +
                    '<th colspan="1">模块号：</th><td colspan="1"><input id="moduleNumCB" class="easyui-combobox" data-options="valueField:\'num\',textField:\'num\',editable:false" style="width:100px;" required></td>' +
                    '<th colspan="1">通道号：</th><td colspan="1"><input id="channelNumCB" class="easyui-combobox" data-options="valueField:\'num\',textField:\'num\',editable:false" style="width:100px;" required></td>' +
                    '</tr>' +
                    '</table>'+'</form>';
            } else if (spData.deviceType == "钢支撑应变计") {
                content = '<form class="spDsForm">'+'<table id="tableHeader" cellpadding="0" cellspacing="0">' + '<tr><th>监测点参数</th></tr>' + '</table>' + '<table class="tableEditDetail"  cellpadding="0" cellspacing="1">' +
                    '<tr><th colspan="1">设备名称：</th><td colspan="3"><input id="name" name="name" class="input" style="width:100px;" required></td></tr>' +
                    '<tr>' +
                    '<th colspan="1">设备型号：</th><td colspan="1"><input id="devModelCB"  class="easyui-combobox" data-options="valueField:\'devModelUuid\',textField:\'devModelName\'" style="width:100px;" required></td>' +
                    '<th colspan="1">设备编号：</th><td colspan="1"><input id="deviceTB" name="deviceTB" class="input" style="width:100px;" required></td>' +
                    '</tr>' +
                    '<tr>' +
                    '<th colspan="1">初始模数值：</th><td colspan="1"><input id="startModValue" name="startModValue" class="input" style="width:70%;" required>mm/F</td>' +
                    '<th colspan="1"">初始温度值：</th><td colspan="1"><input id="startTemperature" name="startTemperature" class="input" style="width:80%;" required><sup>。</sup>C</td>' +
                    '</tr>' +
                    '<tr>' +
                    '<th colspan="1">标定系数:</th>' +
                    '<td colspan="3"><input id="calibratedMod" name="calibratedMod" class="input" style="width:100px;" required>*10<sup><select id="calibratedModCB" class="easyui-combobox" style="width:40px;" required><option>-1</option><option>-2</option><option>-3</option><option>-4</option>'+
            			'<option>-5</option><option>-6</option><option>-7</option></select></sup>/Hz<sup>2</sup></td>' +
                    '</tr>' +
                    '<tr>' +
                    '<th colspan="1">钢支撑外径：</th><td colspan="1"><input id="outerR" name="outerR" class="input" style="width:70%;" required>mm</td>' +
                    '<th colspan="1">钢支撑内径：</th><td colspan="1"><input id="innerR" name="innerR" class="input" style="width:70%;" required>mm</td>' +
                    '</tr>' +
                    '<tr>' +
                    '<th colspan="1">钢筋弹性模量：</th><td colspan="3"><input id="es" name="es" class="input" style="width:70%;" required>kN/mm<sup>2</sup></td>' +
                    '</tr>' +
                    '<tr>' +
                    '<th colspan="1">计算值：</th><td colspan="2"><input id="calculatedValue" name="calculatedValue" class="input" style="width:95%" required></td><th colspan="1"><a id="toCalculate" href="#" class="easyui-linkbutton">值计算</a></th>' +
                    '</tr>' +
                    '<tr>' +
                    '<th colspan="1">选择MCU：</th><td colspan="3"><input id="mcuB" class="easyui-combobox" data-options="valueField:\'mcuUuid\',textField:\'sn\'" style="width:100px;" required></td>' +
                    '</tr>' +
                    '<tr>' +
                    '<th colspan="1">模块号：</th><td colspan="1"><input id="moduleNumCB" class="easyui-combobox" data-options="valueField:\'num\',textField:\'num\',editable:false" style="width:100px;" required></td>' +
                    '<th colspan="1">通道号：</th><td colspan="1"><input id="channelNumCB" class="easyui-combobox" data-options="valueField:\'num\',textField:\'num\',editable:false" style="width:100px;" required></td>' +
                    '</tr>' +
                    '</table>'+'</form>';
            } else if (spData.deviceType == "钢支撑轴力计") {
                content = '<form class="spDsForm">'+'<table id="tableHeader" cellpadding="0" cellspacing="0">' + '<tr><th>监测点参数</th></tr>' + '</table>' + '<table class="tableEditDetail"  cellpadding="0" cellspacing="1">' +
                    '<tr><th colspan="1">设备名称：</th><td colspan="3"><input id="name" name="name" class="input" style="width:100px;" required></td></tr>' +
                    '<tr>' +
                    '<th colspan="1">设备型号：</th><td colspan="1"><input id="devModelCB"  class="easyui-combobox" data-options="valueField:\'devModelUuid\',textField:\'devModelName\'" style="width:100px;" required></td>' +
                    '<th colspan="1">设备编号：</th><td colspan="1"><input id="deviceTB" name="deviceTB" class="input" style="width:100px;" required></td>' +
                    '</tr>' +
                    '<tr>' +
                    '<th colspan="1">初始模数值：</th><td colspan="1"><input id="startModValue" name="startModValue" class="input" style="width:70%;" required>mm/F</td>' +
                    '<th colspan="1">初始温度值：</th><td colspan="1"><input id="startTemperature" name="startTemperature" class="input" style="width:80%;" required><sup>。</sup>C</td>' +
                    '</tr>' +
                    '<tr>' +
                    '<th colspan="1">标定系数:</th>' +
                    '<td colspan="3"><input id="calibratedMod" name="calibratedMod" style="width:100px;" required>*10<sup><select id="calibratedModCB" class="easyui-combobox" style="width:40px;" required><option>-1</option><option>-2</option><option>-3</option><option>-4</option>'+
            			'<option>-5</option><option>-6</option><option>-7</option></select></sup>/Hz<sup>2</sup></td>' +
                    '</tr>' +
                    '<tr>' +
                    '<th colspan="1">选择MCU：</th><td colspan="3"><input id="mcuB" class="easyui-combobox" data-options="valueField:\'mcuUuid\',textField:\'sn\'" style="width:100px;" required></td>' +
                    '</tr>' +
                    '<tr>' +
                    '<th colspan="1">模块号：</th><td colspan="1"><input id="moduleNumCB" class="easyui-combobox" data-options="valueField:\'num\',textField:\'num\',editable:false" style="width:100px;" required></td>' +
                    '<th colspan="1">通道号：</th><td colspan="1"><input id="channelNumCB" class="easyui-combobox" data-options="valueField:\'num\',textField:\'num\',editable:false" style="width:100px;" required></td>' +
                    '</tr>' +
                    '</table>'+'</form>';
            }
        } else if (number == 18) { //锚索
            content = '<form class="spDsForm">'+'<table id="tableHeader" cellpadding="0" cellspacing="0">' + '<tr><th>监测点参数</th></tr>' + '</table>' + '<table class="tableEditDetail"  cellpadding="0" cellspacing="1">' +
                '<tr>' +
                '<th colspan="1">选择MCU：</th><td colspan="3"><input id="mcuB" class="easyui-combobox" data-options="valueField:\'mcuUuid\',textField:\'sn\'" style="width:100px;" required></td>' +
                '</tr>' +
                '<tr>' +
                '<th colspan="1">模块号：</th><td colspan="1"><input id="moduleNumCB" class="easyui-combobox" data-options="valueField:\'num\',textField:\'num\',editable:false" style="width:100px;" required></td>' +
                '<th colspan="1">通道号：</th><td colspan="1"><input id="channelNumCB" class="easyui-combobox" data-options="valueField:\'num\',textField:\'num\',editable:false" style="width:100px;" required></td>' +
                '</tr>' +
                '</table>'+'</form>';
        }
        $("#spZCContentDiv").append(content);
        //需要重新渲染dom元素
        $.parser.parse($("#spZCContentDiv"));
        $("#spZCInfoDialog").modal("show");
        if(number == 4){
        	// 测斜仪的的设备型号选择
            $.post("/smosplat/getDeviceModelByDevType", { devTypeName: "测斜仪" }, function(data, status) {
                $("#devModel").combobox("loadData", JSON.parse(data).rows);
            });

            $.post("/smosplat/getMcus", function(data, status) {
                $("#mcuCB").combobox("loadData", JSON.parse(data).mcus);
            });
        }else if (number == 15) {
            //获取设备的设备型号
            $.post("/smosplat/getDeviceModelByDevType", { devTypeName: spData.deviceType }, function(data, status) {
                $("#devModelCB").combobox("loadData", JSON.parse(data).rows);
            });
        }

        //获取当前项目下的MCU
        $.post("/smosplat/getMcus", function(data, status) {
            $("#mcuB").combobox("loadData", JSON.parse(data).mcus);
        });
        $("#mcuB").combobox({
            onChange: function(n, o) {
                var moduleNum = [{ "num": 1 }, { "num": 2 }, { "num": 3 }, { "num": 4 }];
                var preModuleNUm=$("#moduleNumCB").combobox('getValue');
                $("#moduleNumCB").combobox("loadData", moduleNum);
                $("#moduleNumCB").combobox('setValue',moduleNum[0].num);
                if(preModuleNUm==1){
                    var mcuUuid = $("#mcuB").combobox('getValue');
                    //获取使用了该MCU的设备以取得使用了的模块号通道号
                    $.post("/smosplat/getUsingPointNumByMcuAndModuleNum", { mcuUuid: mcuUuid, moduleNum: 1 }, function(data, status) {
                        var deviceData = JSON.parse(data).rows;
                        var pointNum = new Array();
                        for (var j = 1; j < 9; j++) {
                            var existNum = false;
                            for (var i = 0; i < deviceData.length; i++) {
                                if (deviceData[i].moduleNum == 1 && deviceData[i].pointNum == j) {
                                    existNum = true;
                                }
                            }
                            if (existNum == false) {
                                pointNum.push({ "num": j });
                            }
                        }
                        if(pointNum.length==0){
                        	$("#channelNumCB").combobox('setValue', "");
                        }else{
                        	$("#channelNumCB").combobox("loadData", pointNum);
                            $("#channelNumCB").combobox('setValue', pointNum[0].num);
                        }
                        
                    });
                }
            }
        });
        //模块号下拉框改变验证通道号
        $("#moduleNumCB").combobox({
            onChange: function(n, o) {
                var mcuUuid = $("#mcuB").combobox('getValue');
                //获取使用了该MCU的设备以取得使用了的模块号通道号
                $.post("/smosplat/getUsingPointNumByMcuAndModuleNum", { mcuUuid: mcuUuid, moduleNum: n }, function(data, status) {
                    var deviceData = JSON.parse(data).rows;
                    var pointNum = new Array();
                    for (var j = 1; j < 9; j++) {
                        var existNum = false;
                        for (var i = 0; i < deviceData.length; i++) {
                            if (deviceData[i].moduleNum == n && deviceData[i].pointNum == j) {
                                existNum = true;
                            }
                        }
                        if (existNum == false) {
                            pointNum.push({ "num": j });
                        }
                    }
                    if(pointNum.length==0){
                    	$("#channelNumCB").combobox('setValue', "");
                    }else{
                    	$("#channelNumCB").combobox("loadData", pointNum);
                        $("#channelNumCB").combobox('setValue', pointNum[0].num);
                    }
                    
                });
            }
        });

        //值计算
        $("#toCalculate").bind("click", function() {
            var excal = $("#calibratedMod").val();
            var topcal = $("#calibratedModCB").combobox('getValue');
            var calibratedMod = excal * Math.pow(10, topcal * 1);
            var prmK;
            if (spData.deviceType == "混凝土应变计") {
                var totalArea = $("#totalArea").val(); //钢筋截面面积
                var sectionArea = $("#sectionArea").val(); //支撑截面面积
                var ec = $("#ec").val(); //混凝土弹性模量
                var es = $("#es").val(); //钢筋计弹性模量
                if (sectionArea * 1 < totalArea * 1) {
                    swal({title:"计算失败！",text:"支撑截面面积不能小于钢筋总面积",type:"warning"});
                    return;
                };
                prmK = calibratedMod * (es * totalArea + sectionArea * ec);
            } else if (spData.deviceType == "混凝土应力计") {
                var totalArea = $("#totalArea").val(); //钢筋截面面积
                var sectionArea = $("#sectionArea").val(); //支撑截面面积
                var esArea = $("#esArea").val(); //钢筋计面积
                var ec = $("#ec").val(); //混凝土弹性模量
                var es = $("#es").val(); //钢筋计弹性模量
                if (sectionArea * 1 < totalArea * 1) {
                    swal({title:"计算失败！",text:"支撑截面面积不能小于钢筋总面积",type:"warning"});
                    return;
                };
                prmK = (calibratedMod / esArea) * ((ec / es) * sectionArea+totalArea);

            } else if (spData.deviceType == "钢支撑应变计") {
                var pI = 3.1415926535898;
                var es = $("#es").val();
                var outerR = $("#outerR").val(); //钢管外径
                var innerR = $("#innerR").val(); //钢管内径
                if (outerR * 1 < innerR * 1) {
                    swal({title:"计算失败！",text:"钢管外径不能小于钢管内径",type:"warning"});
                    return;
                };
                prmK = calibratedMod * es * pI * (outerR * outerR - innerR * innerR);

            } else if (spData.deviceType == "钢支撑轴力计") {
                prmK = calibratedMod;
            }
            $("#calculatedValue").textbox({ value: prmK.toFixed(4) });
        })
    });

    //保存锚索或轴力或测斜仪
    $("#saveSPzctBtn").bind("click", function() {
        var monitorRowId = $('#monitorItemDG').jqGrid('getGridParam', 'selrow');
        var number = $("#monitorItemDG").jqGrid('getRowData', monitorRowId).number;
        var surveyRowId = $('#surveyPointDG').jqGrid('getGridParam', 'selrow');
        var spData = $("#surveyPointDG").jqGrid('getRowData', surveyRowId);
      //先验证表单，如果表单验证不通过则不提交
        var val = $(".spDsForm").valid();
        if (!val) {
            return;
        }
        //设备设置表格行数
        var gridNumbers = $("#sp_zcDG").jqGrid('getRowData').length;
        if (number == 15) {
            var excal = $("#calibratedMod").val();
            var topcal = $("#calibratedModCB").combobox('getValue');
            var params = {
                name: $("#name").val(),
                //                devCode: $("#devCode").val(),
                deviceSn: $("#deviceTB").val(),
                startModValue: $("#startModValue").val(),
                startTemperature: $("#startTemperature").val(),
                calibratedMod: (excal * Math.pow(10, topcal * 1)).toFixed(4),
                calculatedValue: $("#calculatedValue").val(),
                //              elastricity:$("#es").val(),
                devType: spData.deviceType
            };
            //            params["device.deviceUuid"] = $("#deviceCB1").combobox('getValue');
            params["sp_ZC.surveyPointUuid"] = spData.surveyPointUuid;
            params["devModelUuid"] = $("#devModelCB").combobox("getValue");
            params["mcuUuid"] = $("#mcuB").combobox("getValue");
            params["moduleNum"] = $("#moduleNumCB").combobox("getValue");
            params["channelNum"] = $("#channelNumCB").combobox("getValue");
            //判断元素是否存在来区分增加还是修改操作  存在---修改   否则添加
            if(document.getElementById("presetTB")){
            	var zcId=$('#sp_zcDG').jqGrid('getGridParam', 'selrow');
            	var zcItem = $("#sp_zcDG").jqGrid('getRowData', zcId);
            	params["device.deviceUuid"] = zcItem['device.deviceUuid'];
            	params["stressUuid"] = zcItem['stressUuid'];
            	$.post("/smosplat/updateStress", params, function(data, status) {
                    var dataObj = JSON.parse(data);
                    if (dataObj.result == 0) {
                    	$("#sp_zcDG").jqGrid('setCell', zcId, 'stressUuid', params.stressUuid);
                        $("#sp_zcDG").jqGrid('setCell', zcId, 'name', params.name);
                        $("#sp_zcDG").jqGrid('setCell', zcId, 'device.sn', params.deviceSn);
                        $("#sp_zcDG").jqGrid('setCell', zcId, 'startModValue', params.startModValue);
                        $("#sp_zcDG").jqGrid('setCell', zcId, 'startTemperature', params.startTemperature);
                        $("#sp_zcDG").jqGrid('setCell', zcId, 'calibratedMod', params.calibratedMod);
                        $("#sp_zcDG").jqGrid('setCell', zcId, 'calculatedValue', params.calculatedValue);
                        $("#sp_zcDG").jqGrid('setCell', zcId, 'device.deviceUuid', params["device.deviceUuid"]);
                        $("#sp_zcDG").jqGrid('setCell', zcId, 'device.deviceModel.devModelUuid', params.devModelUuid);
                        $("#sp_zcDG").jqGrid('setCell', zcId, 'device.deviceModel.devModelName', $("#devModelCB").combobox("getText"));
                        $("#sp_zcDG").jqGrid('setCell', zcId, 'device.mcu.mcuUuid', params.mcuUuid);
                        $("#sp_zcDG").jqGrid('setCell', zcId, 'device.moduleNum', params.moduleNum);
                        $("#sp_zcDG").jqGrid('setCell', zcId, 'device.pointNum', params.channelNum);
                        
                        $("#spZCInfoDialog").modal("hide");
                        swal({title:"修改成功！",type:"success"});
                    } else {
                    	swal({title:"修改失败！",text:dataObj.msg,type:"error"});
                    }
                });
            }else{
            	$.post("/smosplat/addStress", params, function(data, status) {
                    var dataObj = JSON.parse(data);
                    if (dataObj.result == 0) {
                        $("#sp_zcDG").jqGrid("addRowData", gridNumbers + 1, dataObj.entity, "last");
                        $("#spZCInfoDialog").modal("hide");
                        swal({title:"添加成功！",type:"success"});
                    } else {
                    	swal({title:"添加失败！",text:dataObj.msg,type:"error"});
                    }
                });
            }
            
        } else if (number == 18) {
            var params = {
                deviceSn: spData['devCode'],
            };
            //            params["device.deviceUuid"] = $("#deviceCB1").combobox('getValue');
            params["sp_MT.surveyPointUuid"] = spData.surveyPointUuid;
            params["devModelUuid"] = spData['deviceModel'];
            params["mcuUuid"] = $("#mcuB").combobox("getValue");
            params["moduleNum"] = $("#moduleNumCB").combobox("getValue");
            params["channelNum"] = $("#channelNumCB").combobox("getValue");
			if(document.getElementById("presetTB")){
				var zcId=$('#sp_zcDG').jqGrid('getGridParam', 'selrow');
            	var zcItem = $("#sp_zcDG").jqGrid('getRowData', zcId);
            	params["device.deviceUuid"] = zcItem['device.deviceUuid'];
            	params["cableMeterUuid"] = zcItem['cableMeterUuid'];
				$.post("/smosplat/updateCableMeter", params, function(data, status) {
                    var dataObj = JSON.parse(data);
                    if (dataObj.result == 0) {
                    	$("#sp_zcDG").jqGrid('setCell', zcId, 'cableMeterUuid', params.cableMeterUuid);
                        $("#sp_zcDG").jqGrid('setCell', zcId, 'device.sn', params.deviceSn);
                        $("#sp_zcDG").jqGrid('setCell', zcId, 'device.deviceUuid', params["device.deviceUuid"]);
                        $("#sp_zcDG").jqGrid('setCell', zcId, 'device.mcu.mcuUuid', params.mcuUuid);
                        $("#sp_zcDG").jqGrid('setCell', zcId, 'device.mcu.sn', $("#mcuB").combobox("getText"));
                        $("#sp_zcDG").jqGrid('setCell', zcId, 'device.moduleNum', params.moduleNum);
                        $("#sp_zcDG").jqGrid('setCell', zcId, 'device.pointNum', params.channelNum);
                        $("#spZCInfoDialog").modal("hide");
                        swal({title:"修改成功！",type:"success"});
                    } else {
                        swal({title:"修改失败！",text:dataObj.msg,type:"error"});
                    }
                });        	
            }else{
            	$.post("/smosplat/addCableMeter", params, function(data, status) {
                    var dataObj = JSON.parse(data);
                    if (dataObj.result == 0) {
                        $("#sp_zcDG").jqGrid("addRowData", gridNumbers + 1, dataObj.entity, "last");
                        $("#spZCInfoDialog").modal("hide");
                        swal({title:"添加成功！",type:"success"});
                    } else {
                    	swal({title:"添加失败！",text:dataObj.msg,type:"error"});
                    }
                });
            }
        }else if (number == 4) {
            var params = {
                    deviceSn: $("#deviceTB").val(),
                    devType:"测斜仪"
                };
                //            params["device.deviceUuid"] = $("#deviceCB1").combobox('getValue');
              params["sp_CX.surveyPointUuid"] = spData.surveyPointUuid;
              params["name"] = $("#name").val();
              params["mcuUuid"] = $("#mcuCB").combobox("getValue");
              params["devModelUuid"] =  $("#devModel").combobox('getValue');
              params["gap"] = $("#gap").val();
              var excal = $("#calibratedMod").val();
              var topcal = $("#calibratedModCB").combobox('getValue');
              params["calibratedMod"] = excal * Math.pow(10, topcal * 1);
    			if(document.getElementById("presetTB")){
    				var zcId=$('#sp_zcDG').jqGrid('getGridParam', 'selrow');
                	var zcItem = $("#sp_zcDG").jqGrid('getRowData', zcId);
                	params["device.deviceUuid"] = zcItem['device.deviceUuid'];
                	params["clinometerUuid"] = zcItem['clinometerUuid'];
    				$.post("/smosplat/updateClinometer", params, function(data, status) {
                        var dataObj = JSON.parse(data);
                        if (dataObj.result == 0) {
                        	$("#sp_zcDG").jqGrid('setCell', zcId, 'clinometerUuid', params.clinometerUuid);
                        	$("#sp_zcDG").jqGrid('setCell', zcId, 'name', params.name);
                            $("#sp_zcDG").jqGrid('setCell', zcId, 'device.sn', params.deviceSn);
                            $("#sp_zcDG").jqGrid('setCell', zcId, 'device.deviceUuid', params["device.deviceUuid"]);
                            $("#sp_zcDG").jqGrid('setCell', zcId, 'device.deviceModel.devModelUuid', params.devModelUuid);
                            $("#sp_zcDG").jqGrid('setCell', zcId, 'device.deviceModel.devModelName', $("#devModel").combobox("getText"));
                            $("#sp_zcDG").jqGrid('setCell', zcId, 'device.mcu.mcuUuid', params.mcuUuid);
                            $("#sp_zcDG").jqGrid('setCell', zcId, 'calibratedMod', params.calibratedMod);
                            $("#sp_zcDG").jqGrid('setCell', zcId, 'device.gap', params.gap);
                            $("#spZCInfoDialog").modal("hide");
                            swal({title:"修改成功！",type:"success"});
                        } else {
                            swal({title:"修改失败！",text:dataObj.msg,type:"error"});
                        }
                    });        	
                }else{
                	$.post("/smosplat/addClinometer", params, function(data, status) {
                        var dataObj = JSON.parse(data);
                        if (dataObj.result == 0) {
                            $("#sp_zcDG").jqGrid("addRowData", gridNumbers + 1, dataObj.entity, "last");
                            $("#spZCInfoDialog").modal("hide");
                            swal({title:"添加成功！",type:"success"});
                        } else {
                        	swal({title:"添加失败！",text:dataObj.msg,type:"error"});
                        }
                    });
                }
            }


    })

});

function formatSurveyPointSetting(cellvalue, options, rowObject) {
    var rowId = options.rowId;
    return '<img src="/smosplat/common/image/SurveyPoint.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="surveyPointSetting(' + rowId + ')" href="#">测点设置</a>';
}

function surveyPointSetting(rowId) {
    $("#monitorItemDG").jqGrid('setSelection', rowId);
    var monitorItemUuid = $("#monitorItemDG").jqGrid('getRowData', rowId).monitorItemUuid;
    var getDataUrl = "getSPs";
    // 定义监测点列表数组
    var surveyPointsColNames = new Array();
    var surveyPointsColModel = new Array();
    //监测项编号
    var number = $("#monitorItemDG").jqGrid('getRowData', rowId).number;
    //根据监测项编号来决定datagrid中都有哪些列
    if (number == 1) {
        surveyPointsColNames = ['编号类型', '监测点ID', '预警ID', '断面ID', '监测点编号', '初始累计值', '虚拟断面', '报警类型', '修改监测点', '删除监测点'];
        surveyPointsColModel = [{
            name: 'codeChar',
            hidden: true
        }, {
            name: 'surveyPointUuid',
            hidden: true
        }, {
            name: 'warning.warningUuid',
            hidden: true
        }, {
            name: 'section.sectionUuid',
            hidden: true
        }, {
            name: 'code',
            align: 'center'
        }, {
            name: 'originalTotalValue',
            align: 'center'
        }, {
            name: 'section.sectionName',
            align: 'center'
        }, {
            name: 'warning.warningName',
            align: 'center'
        }, {
            name: 'editSP',
            formatter: formatEditSP,
            align: 'center'
        }, {
            name: 'delSP',
            formatter: formatDelSP,
            align: 'center'
        }];
    } else if (number == 4) {
        surveyPointsColNames = ['编号类型', '监测点ID', '预警ID', '监测点编号','测点深度', '报警设置名称','初始累计值','设备设置', '修改监测点', '删除监测点'];
        surveyPointsColModel = [{
            name: 'codeChar',
            hidden: true
        }, {
            name: 'surveyPointUuid',
            hidden: true
        }, {
            name: 'warning.warningUuid',
            hidden: true
        }, {
            name: 'code',
            align: 'center'
        }, {
            name: 'deep',
            align: 'center'
        }, {
            name: 'warning.warningName',
            align: 'center'
        },  {
            name: 'originalTotalValue',
            hidden: true
        },{
            name: 'device',
            formatter: formatEditDev,
            align: 'center'
        },{
            name: 'editSP',
            formatter: formatEditSP,
            align: 'center'
        }, {
            name: 'delSP',
            formatter: formatDelSP,
            align: 'center'
        }];
    } else if (number == 5 || number == 6 || number == 8) {
        surveyPointsColNames = ['编号类型', '监测点ID', '预警ID', '监测点编号', '初始累计值', '报警设置名称', '修改监测点', '删除监测点'];
        surveyPointsColModel = [{
            name: 'codeChar',
            hidden: true
        }, {
            name: 'surveyPointUuid',
            hidden: true
        }, {
            name: 'warning.warningUuid',
            hidden: true
        }, {
            name: 'code',
            align: 'center'
        }, {
            name: 'originalTotalValue',
            align: 'center'
        }, {
            name: 'warning.warningName',
            align: 'center'
        }, {
            name: 'editSP',
            formatter: formatEditSP,
            align: 'center'
        }, {
            name: 'delSP',
            formatter: formatDelSP,
            align: 'center'
        }];
    } else if (number == 10) {
        surveyPointsColNames = ['编号类型', '监测点ID', '预警ID', '监测点编号', '初始累计值', '报警设置名称', '修改监测点', '删除监测点'];
        surveyPointsColModel = [{
            name: 'codeChar',
            hidden: true
        }, {
            name: 'surveyPointUuid',
            hidden: true
        }, {
            name: 'warning.warningUuid',
            hidden: true
        }, {
            name: 'code',
            align: 'center'
        }, {
            name: 'originalTotalValue',
            align: 'center'
        }, {
            name: 'warning.warningName',
            align: 'center'
        }, {
            name: 'editSP',
            formatter: formatEditSP,
            align: 'center'
        }, {
            name: 'delSP',
            formatter: formatDelSP,
            align: 'center'
        }];
    } else if (number == 12) {
        surveyPointsColNames = ['编号类型', '监测点ID', 'mcu','模块号','通道号','设备类型','设备ID', '预警ID', '监测点编号', '设备编号', '设备型号', '测点深度', '标定常数', '仪器初始模数','初始累积值', '报警设置名称', '修改监测点', '删除'];
        surveyPointsColModel = [{
            name: 'codeChar',
            hidden: true
        }, {
            name: 'surveyPointUuid',
            hidden: true
        }, {
            name: 'device.mcu.mcuUuid',
            hidden: true
        }, {
            name: 'device.moduleNum',
            hidden: true
        }, {
            name: 'device.pointNum',
            hidden: true
        }, {
            name: 'device.deviceModel.devModelUuid',
            hidden: true
        },{
            name: 'device.deviceUuid',
            hidden: true
        }, {
            name: 'warning.warningUuid',
            hidden: true
        }, {
            name: 'code',
            align: 'center'
        }, {
            name: 'device.sn',
            align: 'center'
        }, {
            name: 'device.deviceModel.devModelName',
            align: 'center'
        }, {
            name: 'deep',
            align: 'center'
        }, {
            name: 'constant',
            align: 'center'
        }, {
            name: 'originalModule',
            align: 'center'
        },  {
            name: 'originalTotalValue',
            align: 'center'
        },{
            name: 'warning.warningName',
            align: 'center'
        }, {
            name: 'editSP',
            formatter: formatEditSP,
            align: 'center'
        }, {
            name: 'delSP',
            formatter: formatDelSP,
            align: 'center'
        }];
    } else if (number == 15) {
        getDataUrl = "getSP_ZCs"; //轴力
        surveyPointsColNames = ['编号类型', '监测点ID', '预警ID', '监测点编号', '设备类型', '报警设置名称', '修改监测点', '设备设置', '删除监测点'];
        surveyPointsColModel = [{
            name: 'codeChar',
            hidden: true
        }, {
            name: 'surveyPointUuid',
            hidden: true
        }, {
            name: 'warning.warningUuid',
            hidden: true
        }, {
            name: 'code',
            align: 'center'
        }, {
            name: 'deviceType',
            align: 'center'
        }, {
            name: 'warning.warningName',
            align: 'center'
        }, {
            name: 'editSP',
            formatter: formatEditSP,
            align: 'center'
        }, {
            name: 'device',
            formatter: formatEditDev,
            align: 'center'
        }, {
            name: 'delSP',
            formatter: formatDelSP,
            align: 'center'
        }];
    } else if (number == 18) {
        surveyPointsColNames = ['监测点ID', '预警ID', '监测点编号', '设备类型', '报警设置名称','设备型号','设备编号','频率','标定系数值','初始累计值','修改监测点', '设备设置', '删除监测点'];
        surveyPointsColModel = [{
            name: 'surveyPointUuid',
            hidden: true
        }, {
            name: 'warning.warningUuid',
            hidden: true
        }, {
            name: 'code',
            align: 'center'
        }, {
            name: 'deviceType',
            align: 'center'
        }, {
            name: 'warning.warningName',
            align: 'center'
        }, {
            name: 'deviceModel',
            hidden: true
        }, {
            name: 'devCode',
            align: 'center'
        }, {
            name: 'frequency',
            align: 'center'
        }, {
            name: 'calibratedMod',
            align: 'center'
        }, {
            name: 'originalTotalValue',
            align: 'center'
        }, {
            name: 'editSP',
            formatter: formatEditSP,
            align: 'center'
        }, {
            name: 'device',
            formatter: formatEditDev,
            align: 'center'
        }, {
            name: 'delSP',
            formatter: formatDelSP,
            align: 'center'
        }];
    }
    $.jgrid.gridUnload('surveyPointDG');
    $("#surveyPointSettingDialog").modal("show");
    //获得项目、监测项下的所有监测点
    $.post("/smosplat/" + getDataUrl, { monitorItemUuid: monitorItemUuid, number: number }, function(data, status) {
        var surveyData = JSON.parse(data).surveyPoints;
        $("#surveyPointDG").jqGrid({
            datatype: "local",
            styleUI: 'Bootstrap',
            data: surveyData,
            colNames: surveyPointsColNames,
            colModel: surveyPointsColModel,
            autowidth: true,
            viewrecords: true,
            gridview: true,
            rowNum : 20,
            rowList : [ 20, 40, 60 ],
            pager: $("#pager_surveyPointDG")
        });
        $("#surveyPointDG").jqGrid('navGrid', '#pager_surveyPointDG', { edit: false, add: false, del: false });
        $("#surveyPointDG").setGridHeight($("#pointSetting-modal").height() - 175);
        $("#surveyPointDG").setGridWidth($("#pointSetting-modal").width() - 32);
    });

}

function formatEditSP(cellvalue, options, rowObject) {
    var rowId = options.rowId;
    return '<img src="/smosplat/common/image/edit.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="editSurveyPoint(' + rowId + ')" href="#">修改</a>';
}

function formatDelSP(cellvalue, options, rowObject) {
    var rowId = options.rowId;
    return '<img src="/smosplat/common/image/delete.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="deleteSurveyPoint(' + rowId + ')" href="#">删除</a>';
}

function formatEditDev(cellvalue, options, rowObject) {
    var rowId = options.rowId;
    return '<img src="/smosplat/common/image/edit.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="editDev(' + rowId + ')" href="#">设备设置</a>';
}

function editSurveyPoint(rowId) {
    $("#surveyPointDG").jqGrid('setSelection', rowId);
    var monitorRowId = $('#monitorItemDG').jqGrid('getGridParam', 'selrow');
    var monitorItemUuid = $("#monitorItemDG").jqGrid('getRowData', monitorRowId).monitorItemUuid;
    var number = $("#monitorItemDG").jqGrid('getRowData', monitorRowId).number;
    var hasSectionSetting = $("#monitorItemDG").jqGrid('getRowData', monitorRowId).hasSectionSetting;
    $("#updateSPContentDiv").empty();
    var content;
    if (number == 1) {  //围护墙(边坡)顶部水平位移
        content = '<form class="editSurveyPointForm">'+'<table id="tableHeader" cellpadding="0" cellspacing="0">' + '<tr><th>监测点参数</th></tr>' + '</table>' + '<table class="tableEditDetail"  cellpadding="0" cellspacing="1">' +
	        '<tr><th colspan="1">监测点编号：</th><td colspan="1"><input id="codeCharForUdate" name="codeCharForUdate" class="input" style="width:30%;" required></td>'+
	        	'<th colspan="1">测点序数：</th><td colspan="1"><input id="beginNumForUpdate" name="codeCharForUdate" class="input"  style="width:95%;" required></td></tr>' +
            '<tr>' +
	            '<th colspan="1">报警设置：</th><td colspan="1"><input id="warningCBForUpdate" name="codeCharForUdate" class="easyui-combobox" data-options="valueField:\'warningUuid\',textField:\'warningName\'" style="width:120px;" required></td>'+
	            '<th colspan="1">虚拟断面：</th><td colspan="1"><input id="sectionCBForUpdate" name="codeCharForUdate" class="easyui-combobox" data-options="valueField:\'sectionUuid\',textField:\'sectionName\'" style="width:120px;" required></td>' +
            '</tr>' +
            '<tr><th colspan="1">初始累计值：</th><td colspan="3"><input id="originalTotalValueUpdate" name="codeCharForUdate" class="input" style="width:60%;" required>mm</td>' +
            '</tr>' + '</table>'+'</form>';
    } else if (number == 4) { //测斜
        content = '<form class="editSurveyPointForm">'+'<table id="tableHeader" cellpadding="0" cellspacing="0">' + '<tr><th>监测点参数</th></tr>' + '</table>' + '<table id="cxTableUpdate" class="tableEditDetail"  cellpadding="0" cellspacing="1">' +
        	'<tr><th colspan="1">监测点编号：</th><td colspan="1"><input id="codeCharForUdate" name="codeCharForUdate" class="input" style="width:95%;" required></td>'+
        		'<th colspan="1">测点序数：</th><td colspan="1"><input id="beginNumForUpdate"  name="beginNumForUpdate" class="input"  style="width:95%;" required></td>'+
        	'</tr>' +
            '<tr>' +
	            '<th colspan="1">报警设置：</th><td colspan="1"><input id="warningCBForUpdate"  name="warningCBForUpdate" class="easyui-combobox" data-options="valueField:\'warningUuid\',textField:\'warningName\'" style="width:120px;" required></td>' +
	            '<th colspan="1">测点深度：</th><td colspan="1"><input id="deepForUpdate"  name="deepForUpdate" class="input" style="width:70%;" required >m</td>' +
            '</tr>' +
            '</table>'+'</form>';
    } else if (number == 18) { //锚索
        // $("#addPointSetting-modal").height(bodyHeight - 120);
        content = '<form class="editSurveyPointForm">'+'<table id="tableHeader" cellpadding="0" cellspacing="0">' + '<tr><th>监测点参数</th></tr>' + '</table>' + '<table class="tableEditDetail"  cellpadding="0" cellspacing="1">' +
            '<tr><th colspan="1">计算公式：</th><td colspan="3"><img src="project/image/maogan.png" ></img></td>' +
            '</tr></table><table class="tableEditDetail"  cellpadding="0" cellspacing="1">' +
            '<tr><th colspan="1" >备注：</th><td colspan="3">N:锚杆轴力(kN);K:锚索轴力计标定系数(kN/Hz<sup>2</sup>)' +
            '<br />fi:锚索轴力计监测频率(Hz);f<sub>0</sub>:锚索轴力计安装前初始频率(Hz)' + '</td>' +
            '</tr></table><table class="tableEditDetail"  cellpadding="0" cellspacing="1">' +
            '<tr><th colspan="1">监测点编号：</th><td colspan="1"><input id="codeCharForUdate"  name="codeCharForUdate" class="input" style="width:95%;" required></td><th colspan="1">报警设置：</th><td colspan="1"><input id="warningCBForUpdate"  name="warningCBForUpdate" class="easyui-combobox" data-options="valueField:\'warningUuid\',textField:\'warningName\'" style="width:100px;" required></td></tr>' +
            '<tr>' +
	            '<th colspan="1">设备型号：</th><td colspan="1"><input id="devModelCBUpdate"  class="easyui-combobox" data-options="valueField:\'devModelUuid\',textField:\'devModelName\'" style="width:100px;" required></td>' +
	            '<th colspan="1">设备编号：</th><td colspan="1"><input id="deviceTBUpdate" name="deviceTB" class="input" style="width:100px;" required></td>' +
	        '</tr>' +
	        '<tr>' +
	            '<th colspan="1">初始频率：</th><td colspan="1"><input id="frequencyUpdate" name="frequency" class="input" style="width:70%;" required>Hz</td>' +
	            '<th colspan="1">初始累计值：</th><td colspan="1"><input id="originalTotalValueMTUpdate" name="originalTotalValueMT" class="input" style="width:80%;" required>KN</td>' +
	        '</tr>' +
	        '<tr>' +
	            '<th colspan="1">标定系数:</th>' +
	            '<td colspan="3"><input id="calibratedModUpdate" name="calibratedMod" style="width:100px;" required>*10<sup><select id="calibratedModCBUpdate" class="easyui-combobox" style="width:40px;" required><option>-1</option><option>-2</option><option>-3</option><option>-4</option>'+
	            		'<option>-5</option><option>-6</option><option>-7</option></select></sup>/Hz<sup>2</sup></td>' +
	        '</tr>' +
            '</table>'+'</form>';
    } else if (number == 12) { //水位
        content = '<form class="editSurveyPointForm">'+'<table id="tableHeader" cellpadding="0" cellspacing="0">' + '<tr><th>监测点参数</th></tr>' + '</table>' + '<table class="tableEditDetail"  cellpadding="0" cellspacing="1">' +
            '<tr>'+
            	'<th colspan="1">监测点编号：</th><td colspan="1"><input id="codeCharForUdate"  name="codeCharForUdate" class="input" style="width:95%;" required></td><th colspan="1">报警设置：</th><td colspan="1"><input id="warningCBForUpdate"  name="warningCBForUpdate" class="easyui-combobox" data-options="valueField:\'warningUuid\',textField:\'warningName\'" style="width:120px;" required></td></tr>' +
            '<tr>' +
	            '<th colspan="1">设备型号：</th><td colspan="1"><input id="devModelForUpdate"   name="devModelForUpdate" class="easyui-combobox" data-options="valueField:\'devModelUuid\',textField:\'devModelName\'" style="width:120px;" required></td>' +
	            '<th colspan="1">设备编号：</th><td colspan="1"><input id="swDeviceForUpdate"  name="swDeviceForUpdate" class="input" style="width:120px;" required></td>' +
            '</tr>' +
            '<tr>' +
	            '<th colspan="1">测点深度：</th><td colspan="1"><input id="deepForUpdate"  name="deepForUpdate" class="input" style="width:80%;" required>m</td>' +
	            '<th colspan="1">仪器初始模数：</th><td colspan="1"><input id="originalModuleForUpdate"  name="originalModuleForUpdate" class="input" style="width:70%;" required>Hz<sup>2</sup>x<sup>-3</sup></td>' +
            '</tr>' +
            '<tr><th colspan="1">标定常数：</th><td colspan="1"><input id="constantForUpdate"  name="constantForUpdate" class="input" style="width:75%;" required>mm/F</td><th colspan="1">初始累计值：</th><td colspan="1"><input id="originalTotalValueUpdate"  name="originalTotalValueUpdate" class="input" style="width:75%;" required>mm</td></tr>' +
            '<tr>' +
            	'<th colspan="1">选择MCU：</th><td colspan="3"><input id="mcuCBForUpdate"  name="mcuCBForUpdate" class="easyui-combobox" data-options="valueField:\'mcuUuid\',textField:\'sn\'" style="width:120px;" required></td>' +
            '</tr>' +
            '<tr>' +
	            '<th colspan="1">模块号：</th><td colspan="1"><input id="moduleNumForUpdate"  name="moduleNumForUpdate" class="easyui-combobox" data-options="valueField:\'num\',textField:\'num\',editable:false" style="width:120px;" required></td>' +
	            '<th colspan="1">通道号：</th><td colspan="1"><input id="channelNumForUpdate"  name="channelNumForUpdate" class="easyui-combobox" data-options="valueField:\'num\',textField:\'num\',editable:false" style="width:120px;" required></td>' +
            '</tr>' +
            '</table><input id="preset" type="hidden" value="1">'+'</form>';
    } else if (number == 5 || number == 6 || number == 8) { //立柱
        content = '<form class="editSurveyPointForm">'+'<table id="tableHeader" cellpadding="0" cellspacing="0">' + '<tr><th>监测点参数</th></tr>' + '</table>' + '<table class="tableEditDetail"  cellpadding="0" cellspacing="1">' +
	        '<tr><th colspan="1">监测点编号：</th><td colspan="1"><input id="codeCharForUdate"  name="codeCharForUdate" class="input" style="width:95%;" required></td>'+
				'<th colspan="1">测点序数：</th><td colspan="1"><input id="beginNumForUpdate"  name="beginNumForUpdate" class="input"  style="width:95%;" required></td>'+
			'</tr>' +
            '<tr>' +
	            '<th colspan="1">报警设置：</th><td colspan="1"><input id="warningCBForUpdate"  name="warningCBForUpdate" class="easyui-combobox" data-options="valueField:\'warningUuid\',textField:\'warningName\'" style="width:120px;" required></td>' +
	            '<th colspan="1">初始累计值：</th><td colspan="1"><input id="originalTotalValueUpdate"  name="originalTotalValueUpdate"  class="input" style="width:75%;" required>mm</td>' +
            '</tr>' +
            '</table>'+'</form>';
    } else if (number == 10) {  //周边管线竖向位移
        content = '<form class="editSurveyPointForm">'+'<table id="tableHeader" cellpadding="0" cellspacing="0">' + '<tr><th>监测点参数</th></tr>' + '</table>' + '<table class="tableEditDetail"  cellpadding="0" cellspacing="1">' +
		    '<tr><th colspan="1">监测点编号：</th><td colspan="1"><input id="codeCharForUdate"  name="codeCharForUdate" class="input" style="width:95%;" required></td>'+
				'<th colspan="1">测点序数：</th><td colspan="1"><input id="beginNumForUpdate"   name="beginNumForUpdate" class="input"  style="width:95%;" required></td>'+
			'</tr>' +
            '<tr>' +
	            '<th colspan="1">报警设置：</th><td colspan="1"><input id="warningCBForUpdate"  name="warningCBForUpdate" class="easyui-combobox" data-options="valueField:\'warningUuid\',textField:\'warningName\'" style="width:120px;" required></td>' +
	            '<th colspan="1">初始累计值：</th><td colspan="1"><input id="originalTotalValueUpdate"  name="originalTotalValueUpdate" class="input" style="width:70%;" required>mm</td>' +
            '</tr>' +
            '</table>'+'</form>';
    } else if (number == 15) { //轴力
        content = '<form class="editSurveyPointForm">'+'<table id="tableHeader" cellpadding="0" cellspacing="0">' + '<tr><th>监测点参数</th></tr>' + '</table>' + '<table id="stressTB"  name="" class="tableEditDetail"  cellpadding="0" cellspacing="1">' +
            '<tr><th colspan="1">监测点编号：</th><td colspan="1"><input id="codeCharForUdate"  name="codeCharForUdate" class="input" style="width:95%;" required></td>' +
            '<th colspan="1">报警设置：</th><td colspan="1"><input id="warningCBForUpdate"  name="warningCBForUpdate" class="easyui-combobox" data-options="valueField:\'warningUuid\',textField:\'warningName\'" style="width:120px;" required></td></tr>' +
            '</table>'+'</form>';
    }
    //添加新的子内容
    $("#updateSPContentDiv").append(content);
    //需要重新渲染dom元素
    $.parser.parse($("#updateSPContentDiv"));
    
    $('#deepForUpdate').bind('input', function () {
    	if(number!=12){
    		createRowsUpdate(0,0);
    	}
    })
    //获得监测项下的预警设置和断面设置
    $.post("/smosplat/getWarnings", { monitorItemUuid: monitorItemUuid }, function(data, status) {
        $("#warningCBForUpdate").combobox("loadData", JSON.parse(data).warnings);
        //填充动态创修的元素
        var spItem = $("#surveyPointDG").jqGrid('getRowData', rowId);
        if(number==15||number==12||number==18){
        	$("#codeCharForUdate").val(spItem.code);
        	//给水位的各下拉列表接入选择数据
        	if(number==12){
                //获取水位计的设备型号
                $.post("/smosplat/getDeviceModelByDevType", { devTypeName: "水位计" }, function(data, status) {
                    $("#devModelForUpdate").combobox("loadData", JSON.parse(data).rows);
                    $("#devModelForUpdate").combobox("setValue", spItem['device.deviceModel.devModelUuid']);
                });
                $("#originalModuleForUpdate").val(spItem['originalModule']);
                $("#constantForUpdate").val(spItem['constant']);
                $("#swDeviceForUpdate").val(spItem['device.sn']);
                
                $.post("/smosplat/getMcus", function(data, status) {
                    $("#mcuCBForUpdate").combobox("loadData", JSON.parse(data).mcus);
                    $("#mcuCBForUpdate").combobox("setValue", spItem['device.mcu.mcuUuid']);
                });
                $("#mcuCBForUpdate").combobox({
                    onChange: function(n, o) {
                        var moduleNum = [{ "num": 1 }, { "num": 2 }, { "num": 3 }, { "num": 4 }];
                        var preModuleForUpdate=$("#moduleNumForUpdate").combobox("getValue");
                        $("#moduleNumForUpdate").combobox("loadData", moduleNum);
                        var preset=$("#preset").val();
                        if(preset==1){  //第一次进来进行初始化
                        	$("#moduleNumForUpdate").combobox("setValue", spItem['device.moduleNum']);
                        }else{
                        	$("#moduleNumForUpdate").combobox("setValue", moduleNum[0].num);
                        	if(preModuleForUpdate==1){
                                var mcuUuid = $("#mcuCBForUpdate").combobox('getValue');
                                //获取使用了该MCU的设备以取得使用了的模块号通道号
                                $.post("/smosplat/getUsingPointNumByMcuAndModuleNum", { mcuUuid: mcuUuid, moduleNum: 1 }, function(data, status) {
                                	var sId=$('#surveyPointDG').jqGrid('getGridParam', 'selrow');
                                	var spItem2 = $("#surveyPointDG").jqGrid('getRowData', sId);
                                	var deviceData = JSON.parse(data).rows;
                                    var pointNum = new Array();
                                    for (var j = 1; j < 9; j++) {
                                        var existNum = false;
                                        for (var i = 0; i < deviceData.length; i++) {
                                            if (deviceData[i].moduleNum == 1 && deviceData[i].pointNum == j) {
                                                existNum = true;
                                                //修改进来时将本身的通道号加入（getUsingPointNumByMcuAndModuleNum是所有在用的通道号，包括本身）
                                                if(deviceData[i].deviceUuid==spItem2['device.deviceUuid']){
                                                	existNum=false;
                                                	break;
                                                }
                                            }
                                        }
                                        if (existNum == false) {
                                            pointNum.push({ "num": j });
                                        }
                                    }
                                    $("#channelNumForUpdate").combobox("loadData", pointNum);
                                    var preset2=$("#preset").val();
                                    if(preset2==1){   //第一次进来进行初始化
                                    	$("#channelNumForUpdate").combobox('setValue', spItem['device.pointNum']);
                                    	$("#preset").val("0");
                                    }else{
                                    	$("#channelNumForUpdate").combobox('setValue', pointNum[0].num);
                                    }
                                });
                            
                        	}
                        }
                    }
                });
                //mcu下拉框改变验证模块号通道号
                $("#moduleNumForUpdate").combobox({
                    onChange: function(n, o) {
                        var mcuUuid = $("#mcuCBForUpdate").combobox('getValue');
                        //获取使用了该MCU的设备以取得使用了的模块号通道号
                        $.post("/smosplat/getUsingPointNumByMcuAndModuleNum", { mcuUuid: mcuUuid, moduleNum: n }, function(data, status) {
                        	var sId=$('#surveyPointDG').jqGrid('getGridParam', 'selrow');
                        	var spItem2 = $("#surveyPointDG").jqGrid('getRowData', sId);
                        	var deviceData = JSON.parse(data).rows;
                            var pointNum = new Array();
                            for (var j = 1; j < 9; j++) {
                                var existNum = false;
                                for (var i = 0; i < deviceData.length; i++) {
                                    if (deviceData[i].moduleNum == n && deviceData[i].pointNum == j) {
                                        existNum = true;
                                        //修改进来时将本身的通道号加入（getUsingPointNumByMcuAndModuleNum是所有在用的通道号，包括本身）
                                        if(deviceData[i].deviceUuid==spItem2['device.deviceUuid']){
                                        	existNum=false;
                                        	break;
                                        }
                                    }
                                }
                                if (existNum == false) {
                                    pointNum.push({ "num": j });
                                }
                            }
                            $("#channelNumForUpdate").combobox("loadData", pointNum);
                            var preset2=$("#preset").val();
                            if(preset2==1){   //第一次进来进行初始化
                            	$("#channelNumForUpdate").combobox('setValue', spItem['device.pointNum']);
                            	$("#preset").val("0");
                            }else{
                            	$("#channelNumForUpdate").combobox('setValue', pointNum[0].num);
                            }
                        });
                    }
                });
            }
        	if(number==18){
            	//获取锚索设备的设备型号
                $.post("/smosplat/getDeviceModelByDevType", { devTypeName: "锚索计" }, function(data, status) {
                    $("#devModelCBUpdate").combobox("loadData", JSON.parse(data).rows);
                    $("#devModelCBUpdate").combobox('setValue',spItem['deviceModel'])
                });
                $("#frequencyUpdate").val(spItem['frequency']);
                $("#deviceTBUpdate").val(spItem['devCode']);
                $("#originalTotalValueMTUpdate").val(spItem['originalTotalValue']);
                
                var calMod = spItem['calibratedMod'];
                var fushu=false;
                if(calMod<0){
                	calMod=calMod*(-1);
                	fushu=true;
                }
                var supNum = Math.floor(Math.log(calMod)/Math.LN10);
                var baseNum = calMod * Math.pow(10, -1*supNum);
                $("#calibratedModUpdate").val(fushu==true? (baseNum.toFixed(5)*(-1)):(baseNum.toFixed(5)));
                $("#calibratedModCBUpdate").combobox('setValue',supNum);
            }
        }else{
        	$("#codeCharForUdate").val(spItem.codeChar);
        	$("#beginNumForUpdate").val(spItem.code.replace(spItem.codeChar, ""));
        }
        $("#originalTotalValueUpdate").val(spItem.originalTotalValue);
        $("#deepForUpdate").val(spItem.deep);
        if(number==4){
        	createRowsUpdate(spItem['originalTotalValue'],1);
        }
        $("#deviceTypeForUpdate").combobox("setValue", spItem['deviceType']);
        $("#spUuidForUpdate").text(spItem.surveyPointUuid);
        $("#warningCBForUpdate").combobox("setValue", spItem["warning.warningUuid"]);
        //判断是否有断面设置
        if (hasSectionSetting == 0) {
            $.post("/smosplat/getSections", { monitorItemUuid: monitorItemUuid }, function(data, status) {
                $("#sectionCBForUpdate").combobox("loadData", JSON.parse(data).sections);
                $("#sectionCBForUpdate").combobox("setValue", spItem["section.sectionUuid"]);
            });
        }
    });
    $("#updateSurveyPointInfoDialog").modal("show");
}

function deleteSurveyPoint(rowId) {
    $("#surveyPointDG").jqGrid('setSelection', rowId);
    var spItem = $("#surveyPointDG").jqGrid('getRowData', rowId);
    var monitorRowId = $('#monitorItemDG').jqGrid('getGridParam', 'selrow');
    var number = $("#monitorItemDG").jqGrid('getRowData', monitorRowId).number;
    var deleteUrl = "";
    if (number == 1) {
        deleteUrl = "deleteSP_WYS";
    } else if (number == 4) {
        deleteUrl = "deleteSP_CX";
    } else if (number == 5) {
        deleteUrl = "deleteSP_WYD";
    }else if (number == 6) {
        deleteUrl = "deleteSP_LZ";
    } else if (number == 8) {
        deleteUrl = "deleteSP_SM";
    }else if (number == 10) {
        deleteUrl = "deleteSP_ZGD";
    } else if (number == 12) {
        deleteUrl = "deleteSP_SW";
    } else if (number == 15) {
        deleteUrl = "deleteSP_zc";
    } else if (number == 18) {
        deleteUrl = "deleteSP_MT";
    }
    //弹出提示框确认
    swal({
        title: "警告",
        text: "删除监测点将删除监测点监测的数据！您确定要删除吗?",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "是的，我要删除！",
        cancelButtonText: "让我再考虑一下…",
        closeOnConfirm: false,
        closeOnCancel: false
    }, function(isConfirm) {
        if (isConfirm) {
            $.post("/smosplat/" + deleteUrl, { surveyPointUuid: spItem.surveyPointUuid },function(data, status) {
                if (JSON.parse(data).result == 0) {
                	//刷新本地数据
                    $("#surveyPointDG").jqGrid('delRowData', rowId);
                    swal("删除成功！", "您已经删除了该监测点。", "success");
                } else {
                	swal("删除失败！",JSON.parse(data).msg,"error");
                }
            });
        } else {
            swal("已取消", "您取消了删除操作！", "error")
        }
    });
};

//设备设置
function editDev(rowId) {
    var monitorRowId = $('#monitorItemDG').jqGrid('getGridParam', 'selrow');
    var number = $("#monitorItemDG").jqGrid('getRowData', monitorRowId).number;
    $("#surveyPointDG").jqGrid('setSelection', rowId);
    var sp_zcColNames = new Array();
    var sp_zcColModel = new Array();
    var dataUrl = "";
    var data = $("#surveyPointDG").jqGrid('getRowData', rowId);
    var params = {};
    if(number == 4){
        dataUrl = "getClinometerBysp_CX";
        params = { sp_CXUuid: data.surveyPointUuid };
        sp_zcColNames = ['测斜ID', '设备名称', '设备编号','设备ID', '设备型号','设备型号ID', 'MCU', '标定系数值','间隔/m','设备修改', '设备删除'];
        sp_zcColModel = [{
            name: 'clinometerUuid',
            hidden: true
        }, {
            name: 'name',
            align: 'center'
        }, {
            name: 'device.sn',
            align: 'center'
        },{
            name: 'device.deviceUuid',
            hidden: true
        },  {
            name: 'device.deviceModel.devModelName',
            align: 'center'
        }, {
            name: 'device.deviceModel.devModelUuid',
            hidden: true
        },{
            name: 'device.mcu.mcuUuid',
            hidden: true
        }, {
            name: 'calibratedMod',
            align: 'center'
        }, {
            name: 'gap',
            hidden: true
        },{
            name: 'editSPd',
            formatter: formatEditSPDevice,
            align: 'center'
        }, {
            name: 'delSPd',
            formatter: formatDelSPDevice,
            align: 'center'
        }]
    }else if (number == 15) {
        dataUrl = "getStressBySP_zc";
        params = { sp_ZCUuid: data.surveyPointUuid };
        sp_zcColNames = ['轴力ID', '设备名称', '设备编号','设备ID', '设备型号','设备型号ID', 'MCU','模块号','通道号','初始模数', '初始温度值', '标定系数值','计算值','设备修改', '设备删除'];
        sp_zcColModel = [{
            name: 'stressUuid',
            hidden: true
        }, {
            name: 'name',
            align: 'center'
        }, {
            name: 'device.sn',
            align: 'center'
        },{
            name: 'device.deviceUuid',
            hidden: true
        },  {
            name: 'device.deviceModel.devModelName',
            align: 'center'
        }, {
            name: 'device.deviceModel.devModelUuid',
            hidden: true
        },{
            name: 'device.mcu.mcuUuid',
            hidden: true
        }, {
            name: 'device.moduleNum',
            hidden: true
        }, {
            name: 'device.pointNum',
            hidden: true
        }, {
            name: 'startModValue',
            align: 'center'
        }, {
            name: 'startTemperature',
            align: 'center'
        }, {
            name: 'calibratedMod',
            align: 'center'
        }, {
            name: 'calculatedValue',
            hidden: true
        },{
            name: 'editSPd',
            formatter: formatEditSPDevice,
            align: 'center'
        }, {
            name: 'delSPd',
            formatter: formatDelSPDevice,
            align: 'center'
        }]
    } else if (number == 18) {
        dataUrl = "getCableMeterBySP_mt";
        params = { sp_MTUuid: data.surveyPointUuid };
        sp_zcColNames = ['锚索元件ID','设备编号','设备ID', '设备型号', '设备型号ID', 'MCU','mcu编号','模块号','通道号','设备修改','设备删除'];
        sp_zcColModel = [{
            name: 'cableMeterUuid',
            hidden: true
        },  {
            name: 'device.sn',
            align: 'center'
        },{
            name: 'device.deviceUuid',
            hidden: true
        }, {
            name: 'device.deviceModel.devModelName',
            align: 'center'
        },  {
            name: 'device.deviceModel.devModelUuid',
            hidden: true
        },{
            name: 'device.mcu.mcuUuid',
            hidden: true
        },{
            name: 'device.mcu.sn',
            align: 'center'
        }, {
            name: 'device.moduleNum',
            align: 'center'
        }, {
            name: 'device.pointNum',
            align: 'center'
        },{
            name: 'editSPd',
            formatter: formatEditSPDevice,
            align: 'center'
        },{
            name: 'delSPd',
            formatter: formatDelSPDevice,
            align: 'center'
        }]
    }


    $("#spZCDeviceSettingDialog").modal("show");
    $.jgrid.gridUnload('sp_zcDG');
    $.post("/smosplat/" + dataUrl, params, function(data, status) {
        $("#sp_zcDG").jqGrid({
            datatype: "local",
            styleUI: 'Bootstrap',
            data: JSON.parse(data).rows,
            colNames: sp_zcColNames,
            colModel: sp_zcColModel,
            autowidth: true,
            viewrecords: true,
            gridview: true,
            rowNum : 20,
            rowList : [ 20, 40, 60 ],
            pager: $("#pager_sp_zcDG")
        });
        $("#sp_zcDG").jqGrid('navGrid', '#pager_sp_zcDG', { edit: false, add: false, del: false });
        $("#sp_zcDG").setGridHeight($("#spZCDevice-modal").height() - 175);
        $("#sp_zcDG").setGridWidth($("#spZCDevice-modal").width() - 32);
    });

}

function formatEditSPDevice(cellvalue, options, rowObject) {
    var rowId = options.rowId;
    return '<img src="/smosplat/common/image/edit.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="editSPDevice(' + rowId + ')" href="#">修改</a>';
}
function formatDelSPDevice(cellvalue, options, rowObject) {
    var rowId = options.rowId;
    return '<img src="/smosplat/common/image/delete.png" style="width:16px;height:16px;margin-bottom:2px;">&nbsp;<a onclick="deleteSPDevice(' + rowId + ')" href="#">删除</a>';
}

function editSPDevice(rowId) {
    var monitorRowId = $('#monitorItemDG').jqGrid('getGridParam', 'selrow');
    var number = $("#monitorItemDG").jqGrid('getRowData', monitorRowId).number;
    var spId = $("#surveyPointDG").jqGrid('getGridParam', 'selrow');
    var spData = $("#surveyPointDG").jqGrid('getRowData', spId);
    $("#sp_zcDG").jqGrid('setSelection', rowId);
    var devData = $("#sp_zcDG").jqGrid('getRowData', rowId);
    $("#spZCContentDiv").empty();
    var content;
    if(number == 4){
        content = '<form class="spDsForm">'+'<table id="tableHeader" cellpadding="0" cellspacing="0">' + '<tr><th>监测点参数</th></tr>' + '</table>' + '<table class="tableEditDetail"  cellpadding="0" cellspacing="1">' +
        '<tr>' +
        	'<th colspan="1">测斜仪名称：</th><td colspan="3"><input id="name" name="name" class="input" style="width:100px;" required></td>' +
        	'</tr>' +
        '<tr>' +
            '<th colspan="1">设备型号：</th><td colspan="1"><input id="devModel"  class="easyui-combobox" data-options="valueField:\'devModelUuid\',textField:\'devModelName\'" style="width:100px;" required></td>' +
            '<th colspan="1">设备编号：</th><td colspan="1"><input id="deviceTB" name="deviceTB" class="input" style="width:92px;" required></td>' +
        '</tr>' +
        '<tr>' +
        '<th colspan="1">选择MCU：</th><td colspan="1"><input id="mcuCB" class="easyui-combobox" data-options="valueField:\'mcuUuid\',textField:\'sn\'" style="width:100px;" required></td>' +
        	'<th colspan="1">间隔:  </th><td colspan="1"><select id="gap" class="easyui-combobox" style="width:92px;" required><option>0.5</option><option>1</option></select>m</td>' +
        '</tr>' +
        '<th colspan="1">标定系数:</th>' +
            '<td colspan="3"><input id="calibratedMod" name="calibratedMod" style="width:100px;" required>*10<sup><select id="calibratedModCB" class="easyui-combobox" style="width:40px;" required><option>-1</option><option>-2</option><option>-3</option><option>-4</option>'+
            		'<option>-5</option><option>-6</option><option>-7</option></select></sup>/Hz<sup>2</sup></td>' +
        '</tr>'+
        '</table><input id="presetTB" type="hidden" value="1">'+'</form>';
    }else if (number == 15) {
        if (spData.deviceType == "混凝土应变计") {
            content = '<form class="spDsForm">'+'<table id="tableHeader" cellpadding="0" cellspacing="0">' + '<tr><th>监测点参数</th></tr>' + '</table>' + '<table class="tableEditDetail"  cellpadding="0" cellspacing="1">' +
                '<tr><th colspan="1">设备名称：</th><td colspan="3"><input id="name" name="name" class="input" style="width:100px;" required></td></tr>' +
                '<tr>' +
                '<th colspan="1">设备型号：</th><td colspan="1"><input id="devModelCB" name="devModelCB"  class="easyui-combobox" data-options="valueField:\'devModelUuid\',textField:\'devModelName\'" style="width:100px;" required></td>' +
                '<th colspan="1">设备编号：</th><td colspan="1"><input id="deviceTB" name="deviceTB" class="input" style="width:100px;" required></td>' +
                '</tr>' +
                '<tr>' +
                '<th colspan="1">初始模数值：</th><td colspan="1"><input id="startModValue" name="startModValue" class="input" style="width:70%;" required>mm/F</td>' +
                '<th colspan="1">初始温度值：</th><td colspan="1"><input id="startTemperature" name="startTemperature" class="input" style="width:80%;" required><sup>。</sup>C</td>' +
                '</tr>' +
                '<tr>' +
                '<th colspan="1">标定系数:</th>' +
                '<td colspan="3"><input id="calibratedMod" name="calibratedMod" class="input" style="width:100px;" required>*10<sup><select id="calibratedModCB" name="calibratedModCB" class="easyui-combobox" style="width:40px;" required><option>-1</option><option>-2</option><option>-3</option><option>-4</option>'+
        			'<option>-5</option><option>-6</option><option>-7</option></select></sup>/Hz<sup>2</sup></td>' +
                '</tr>' +
                '<tr>' +
                '<th colspan="1">钢筋计总面积：</th><td colspan="1"><input id="totalArea" name="totalArea" class="input" style="width:70%;">mm<sup>2</sup></td>' +
                '<th colspan="1">支撑截面面积：</td><td colspan="1"><input id="sectionArea" name="sectionArea" class="input" style="width:70%;">mm<sup>2</sup></td></tr>' +
                '<tr>' +
                '<th colspan="1">混凝土弹性模量：</th><td colspan="1"><input id="ec" name="ec" class="input" style="width:50%;" >kN/mm<sup>2</sup></td><th colspan="1">钢筋弹性模量：</th><td colspan="1"><input id="es" name="es" style="width:50%;" >kN/mm<sup>2</sup></td></tr>' +
                '<tr>' +
                '<th colspan="1">计算值：</th><td colspan="2"><input id="calculatedValue" name="calculatedValue" class="input" style="width:95%;" required></td><th colspan="1"><a id="toCalculate" href="#" class="easyui-linkbutton">值计算</a></td>' +
                '</tr>' +
                '<tr>' +
                '<th colspan="1">选择MCU：</th><td colspan="3"><input id="mcuB" name="mcuB" class="easyui-combobox" data-options="valueField:\'mcuUuid\',textField:\'sn\'" style="width:100px;" required></td>' +
                '</tr>' +
                '<tr>' +
                '<th colspan="1">模块号：</th><td colspan="1"><input id="moduleNumCB" name="moduleNumCB" class="easyui-combobox" data-options="valueField:\'num\',textField:\'num\',editable:false" style="width:100px;" required></td>' +
                '<th colspan="1">通道号：</th><td colspan="1"><input id="channelNumCB" name="channelNumCB" class="easyui-combobox" data-options="valueField:\'num\',textField:\'num\',editable:false" style="width:100px;" required></td>' +
                '</tr>' +
                '</table><input id="presetTB" type="hidden" value="1">'+'</form>';
        } else if (spData.deviceType == "混凝土应力计") {
            content = '<form class="spDsForm">'+'<table id="tableHeader" cellpadding="0" cellspacing="0">' + '<tr><th>监测点参数</th></tr>' + '</table>' + '<table class="tableEditDetail"  cellpadding="0" cellspacing="1">' +
                '<tr><th colspan="1">设备名称：</th><td colspan="3"><input id="name" name="name" class="input" style="width:100px;" required></td></tr>' +
                '<tr>' +
                '<th colspan="1">设备型号：</th><td colspan="1"><input id="devModelCB" name="devModelCB" class="easyui-combobox" data-options="valueField:\'devModelUuid\',textField:\'devModelName\'" style="width:100px;" required></td>' +
                '<th colspan="1">设备编号：</th><td colspan="1"><input id="deviceTB" name="deviceTB" class="input" style="width:100px;" required></td>' +
                '</tr>' +
                '<tr>' +
                '<th colspan="1">初始模数值：</th><td colspan="1"><input id="startModValue" name="startModValue" class="input" style="width:70%;" required>mm/F</td>' +
                '<th colspan="1">初始温度值：</th><td colspan="1"><input id="startTemperature" name="startTemperature" class="input" style="width:80%;" required><sup>。</sup>C</td>' +
                '</tr>' +
                '<tr>' +
                '<th colspan="1">标定系数:</th>' +
                '<td colspan="3"><input id="calibratedMod" name="calibratedMod" class="input" style="width:100px;">*10<sup><select id="calibratedModCB" name="calibratedModCB" class="easyui-combobox" style="width:40px;" required><option>-1</option><option>-2</option><option>-3</option><option>-4</option>'+
        			'<option>-5</option><option>-6</option><option>-7</option></select></sup>/Hz<sup>2</sup></td>' +
                '</tr>' +
                '<tr>' +
                '<th colspan="1">钢筋总面积：</th><td colspan="1"><input id="totalArea" name="totalArea" class="input" style="width:70%;">mm<sup>2</sup></td>' +
                '<th colspan="1">支撑截面面积：</th><td colspan="1"><input id="sectionArea" name="sectionArea" class="input" style="width:60%;" >mm<sup>2</sup></td></tr>' +
                '<tr>' +
                '<tr>' +
                '<th colspan="1">钢筋计面积：</th><td colspan="3"><input id="esArea" name="esArea" class="input" style="width:70%;">mm<sup>2</sup></td></tr>' +
                '<tr>' +
                '<th colspan="1">混凝土弹性模量：</th><td colspan="1"><input id="ec" name="ec" class="input" style="width:50%;" >kN/mm<sup>2</sup></td><th colspan="1" >钢筋弹性模量：</th><td colspan="1"><input id="es" name="es" class="input" style="width:50%;" >kN/mm<sup>2</sup></td></tr>' +
                '<tr>' +
                '<th colspan="1">计算值：</th><td colspan="2"><input id="calculatedValue" name="calculatedValue" class="input" style="width:95%;" required></td><td colspan="1"><a id="toCalculate" href="#" class="easyui-linkbutton">值计算</a></td>' +
                '</tr>' +
                '<tr>' +
                '<th colspan="1">选择MCU：</th><td colspan="3"><input id="mcuB" name="mcuB" class="easyui-combobox" data-options="valueField:\'mcuUuid\',textField:\'sn\'" style="width:100px;" required></td>' +
                '</tr>' +
                '<tr>' +
                '<th colspan="1">模块号：</th><td colspan="1"><input id="moduleNumCB" name="moduleNumCB" class="easyui-combobox" data-options="valueField:\'num\',textField:\'num\',editable:false" style="width:100px;" required></td>' +
                '<th colspan="1">通道号：</th><td colspan="1"><input id="channelNumCB" name="channelNumCB" class="easyui-combobox" data-options="valueField:\'num\',textField:\'num\',editable:false" style="width:100px;" required></td>' +
                '</tr>' +
                '</table><input id="presetTB" type="hidden" value="1">'+'</form>';
        } else if (spData.deviceType == "钢支撑应变计") {
            content = '<form class="spDsForm">'+ '<table id="tableHeader" cellpadding="0" cellspacing="0">' + '<tr><th>监测点参数</th></tr>' + '</table>' + '<table class="tableEditDetail"  cellpadding="0" cellspacing="1">' +
                '<tr><th colspan="1">设备名称：</th><td colspan="3"><input id="name" name="name" class="input" style="width:100px;" required></td></tr>' +
                '<tr>' +
                '<th colspan="1">设备型号：</th><td colspan="1"><input id="devModelCB" name="devModelCB"  class="easyui-combobox" data-options="valueField:\'devModelUuid\',textField:\'devModelName\'" style="width:100px;" required></td>' +
                '<th colspan="1">设备编号：</th><td colspan="1"><input id="deviceTB" name="deviceTB" class="input" style="width:100px;" required></td>' +
                '</tr>' +
                '<tr>' +
                '<th colspan="1">初始模数值：</th><td colspan="1"><input id="startModValue" name="startModValue" class="input" style="width:70%;" required>mm/F</td>' +
                '<th colspan="1"">初始温度值：</th><td colspan="1"><input id="startTemperature" name="startTemperature" class="input" style="width:80%;" required><sup>。</sup>C</td>' +
                '</tr>' +
                '<tr>' +
                '<th colspan="1">标定系数:</th>' +
                '<td colspan="3"><input id="calibratedMod" name="calibratedMod" class="input" style="width:100px;" required>*10<sup><select id="calibratedModCB" name="calibratedModCB" class="easyui-combobox" style="width:40px;" required><option>-1</option><option>-2</option><option>-3</option><option>-4</option>'+
        			'<option>-5</option><option>-6</option><option>-7</option></select></sup>/Hz<sup>2</sup></td>' +
                '</tr>' +
                '<tr>' +
                '<th colspan="1">钢支撑外径：</th><td colspan="1"><input id="outerR" name="outerR" class="input" style="width:70%;">mm</td>' +
                '<th colspan="1">钢支撑内径：</th><td colspan="1"><input id="innerR" name="innerR" class="input" style="width:70%;">mm</td>' +
                '</tr>' +
                '<tr>' +
                '<th colspan="1">钢筋弹性模量：</th><td colspan="3"><input id="es" name="es" class="input" style="width:70%;" >kN/mm<sup>2</sup></td>' +
                '</tr>' +
                '<tr>' +
                '<th colspan="1">计算值：</th><td colspan="2"><input id="calculatedValue" name="calculatedValue" class="input" style="width:95%" required></td><th colspan="1"><a id="toCalculate" href="#" class="easyui-linkbutton">值计算</a></th>' +
                '</tr>' +
                '<tr>' +
                '<th colspan="1">选择MCU：</th><td colspan="3"><input id="mcuB" name="mcuB" class="easyui-combobox" data-options="valueField:\'mcuUuid\',textField:\'sn\'" style="width:100px;" required></td>' +
                '</tr>' +
                '<tr>' +
                '<th colspan="1">模块号：</th><td colspan="1"><input id="moduleNumCB" name="moduleNumCB" class="easyui-combobox" data-options="valueField:\'num\',textField:\'num\',editable:false" style="width:100px;" required></td>' +
                '<th colspan="1">通道号：</th><td colspan="1"><input id="channelNumCB" name="channelNumCB" class="easyui-combobox" data-options="valueField:\'num\',textField:\'num\',editable:false" style="width:100px;" required></td>' +
                '</tr>' +
                '</table><input id="presetTB" type="hidden" value="1">'+'</form>';
        } else if (spData.deviceType == "钢支撑轴力计") {
            content = '<form class="spDsForm">'+'<table id="tableHeader" cellpadding="0" cellspacing="0">' + '<tr><th>监测点参数</th></tr>' + '</table>' + '<table class="tableEditDetail"  cellpadding="0" cellspacing="1">' +
                '<tr><th colspan="1">设备名称：</th><td colspan="3"><input id="name" name="name" class="input" style="width:100px;" required></td></tr>' +
                '<tr>' +
                '<th colspan="1">设备型号：</th><td colspan="1"><input id="devModelCB" name="devModelCB" class="easyui-combobox" data-options="valueField:\'devModelUuid\',textField:\'devModelName\'" style="width:100px;" required></td>' +
                '<th colspan="1">设备编号：</th><td colspan="1"><input id="deviceTB" name="deviceTB" class="input" style="width:100px;" required></td>' +
                '</tr>' +
                '<tr>' +
                '<th colspan="1">初始模数值：</th><td colspan="1"><input id="startModValue" name="startModValue" class="input" style="width:70%;" required>mm/F</td>' +
                '<th colspan="1">初始温度值：</th><td colspan="1"><input id="startTemperature" name="startTemperature" class="input" style="width:80%;" required><sup>。</sup>C</td>' +
                '</tr>' +
                '<tr>' +
                '<th colspan="1">标定系数:</th>' +
                '<td colspan="3"><input id="calibratedMod" name="calibratedMod" style="width:100px;" required>*10<sup><select id="calibratedModCB" name="calibratedModCB" class="easyui-combobox" style="width:40px;" required><option>-1</option><option>-2</option><option>-3</option><option>-4</option>'+
        			'<option>-5</option><option>-6</option><option>-7</option></select></sup>/Hz<sup>2</sup></td>' +
                '</tr>' +
                '<tr>' +
                '<th colspan="1">选择MCU：</th><td colspan="3"><input id="mcuB" name="mcuB" class="easyui-combobox" data-options="valueField:\'mcuUuid\',textField:\'sn\'" style="width:100px;" required></td>' +
                '</tr>' +
                '<tr>' +
                '<th colspan="1">模块号：</th><td colspan="1"><input id="moduleNumCB" name="moduleNumCB" class="easyui-combobox" data-options="valueField:\'num\',textField:\'num\',editable:false" style="width:100px;" required></td>' +
                '<th colspan="1">通道号：</th><td colspan="1"><input id="channelNumCB" name="channelNumCB" class="easyui-combobox" data-options="valueField:\'num\',textField:\'num\',editable:false" style="width:100px;" required></td>' +
                '</tr>' +
                '</table><input id="presetTB" type="hidden" value="1">'+'</form>';
        }
    } else if (number == 18) { //锚索
        content = '<form class="spDsForm">'+'<table id="tableHeader" cellpadding="0" cellspacing="0">' + '<tr><th>监测点参数</th></tr>' + '</table>' + '<table class="tableEditDetail"  cellpadding="0" cellspacing="1">' +
            '<th colspan="1">选择MCU：</th><td colspan="3"><input id="mcuB" name="mcuB" class="easyui-combobox" data-options="valueField:\'mcuUuid\',textField:\'sn\'" style="width:100px;" required></td>' +
            '</tr>' +
            '<tr>' +
            '<th colspan="1">模块号：</th><td colspan="1"><input id="moduleNumCB" name="moduleNumCB"  class="easyui-combobox" data-options="valueField:\'num\',textField:\'num\',editable:false" style="width:100px;" required></td>' +
            '<th colspan="1">通道号：</th><td colspan="1"><input id="channelNumCB" name="channelNumCB" class="easyui-combobox" data-options="valueField:\'num\',textField:\'num\',editable:false" style="width:100px;" required></td>' +
            '</tr>' +
            '</table><input id="presetTB" type="hidden" value="1">'+'</form>';
    }


    $("#spZCContentDiv").append(content);
    //需要重新渲染dom元素
    $.parser.parse($("#spZCContentDiv"));
    $("#spZCInfoDialog").modal("show");
    if(number == 4){
    	$.post("/smosplat/getDeviceModelByDevType", { devTypeName: "测斜仪" }, function(data, status) {
            $("#devModel").combobox("loadData", JSON.parse(data).rows);
            $("#devModel").combobox("setValue", devData['device.deviceModel.devModelUuid']);
        });

        $.post("/smosplat/getMcus", function(data, status) {
            $("#mcuCB").combobox("loadData", JSON.parse(data).mcus);
            $("#mcuCB").combobox("setValue", devData['device.mcu.mcuUuid']);
        });
    }else if (number == 15) {
        //获取设备的设备型号
        $.post("/smosplat/getDeviceModelByDevType", { devTypeName: spData.deviceType }, function(data, status) {
            $("#devModelCB").combobox("loadData", JSON.parse(data).rows);
            $("#devModelCB").combobox("setValue", devData['device.deviceModel.devModelUuid']);
        });
    } else if (number == 18) {
        //获取设备的设备型号
        $.post("/smosplat/getDeviceModelByDevType", { devTypeName: "锚索计" }, function(data, status) {
            $("#devModelCB").combobox("loadData", JSON.parse(data).rows);
            $("#devModelCB").combobox("setValue", devData['device.deviceModel.devModelUuid']);
        });
    }
    $("#name").val(devData['name']);
    $("#deviceTB").val(devData['device.sn']);
    $("#frequency").val(devData['frequency']);
    var calMod = devData['calibratedMod'];
    var fushu=false;
    if(calMod<0){
    	calMod=calMod*(-1);
    	fushu=true;
    }
    var supNum = Math.floor(Math.log(calMod)/Math.LN10);
    var baseNum = calMod * Math.pow(10, -1*supNum);
    $("#calibratedMod").val(fushu==true? (baseNum.toFixed(5)*(-1)):(baseNum.toFixed(5)));
    $("#calibratedModCB").combobox('setValue',supNum);
    $("#startModValue").val(devData['startModValue']);
    $("#startTemperature").val(devData['startTemperature']);
    $("#calculatedValue").val(devData['calculatedValue']);
    
    //获取当前项目下的MCU
    $.post("/smosplat/getMcus", function(data, status) {
        $("#mcuB").combobox("loadData", JSON.parse(data).mcus);
        $("#mcuB").combobox("setValue", devData['device.mcu.mcuUuid']);
    });
    $("#mcuB").combobox({
        onChange: function(n, o) {
        	var preModule=$("#moduleNumCB").combobox('getValue');
            var moduleNum = [{ "num": 1 }, { "num": 2 }, { "num": 3 }, { "num": 4 }];
            $("#moduleNumCB").combobox("loadData", moduleNum);
            var preset=$("#presetTB").val();
            if(preset==1){  //第一次进来进行初始化
            	$("#moduleNumCB").combobox("setValue", devData['device.moduleNum']);
            }else{
            	$("#moduleNumCB").combobox("setValue", moduleNum[0].num);
            	//MCU改变了，但模块号没改变，依然要重新获取数据到通道号下拉列表中,这种情况下，由于预设模块号为1，所以只要判断之前的模块号是不是1就行
            	if(preModule==1){
                    var mcuUuid = $("#mcuB").combobox('getValue');
                    var devRowId = $('#sp_zcDG').jqGrid('getGridParam', 'selrow');
                    var devData2 = $("#sp_zcDG").jqGrid('getRowData', devRowId);
                    //获取使用了该MCU的设备以取得使用了的模块号通道号
                    $.post("/smosplat/getUsingPointNumByMcuAndModuleNum", { mcuUuid: mcuUuid, moduleNum: 1 }, function(data, status) {
                        var deviceData = JSON.parse(data).rows;
                        var pointNum = new Array();
                        for (var j = 1; j < 9; j++) {
                            var existNum = false;
                            for (var i = 0; i < deviceData.length; i++) {
                                if (deviceData[i].moduleNum == 1 && deviceData[i].pointNum == j) {
                                	if(deviceData[i].deviceUuid==devData2['device.deviceUuid']){
                                    	existNum=false;
                                    	break;
                                    }
                                    existNum = true;
                                }
                            }
                            if (existNum == false) {
                                pointNum.push({ "num": j });
                            }
                        }
                        $("#channelNumCB").combobox("loadData", pointNum);
                        var preset2=$("#presetTB").val();
                        if(preset2==1){   //第一次进来进行初始化
                        	$("#channelNumCB").combobox('setValue', devData2['device.pointNum']);
                        	$("#presetTB").val("0");
                        }else{
                        	if(pointNum.length==0){
                        		$("#channelNumCB").combobox('setValue', "");
                        	}else{
                        		$("#channelNumCB").combobox('setValue', pointNum[0].num);
                        	}
                        }
                    });
                
            	}
            }
        }
    });
    //模块号下拉框改变验证通道号
    $("#moduleNumCB").combobox({
        onChange: function(n, o) {
            var mcuUuid = $("#mcuB").combobox('getValue');
            var devRowId = $('#sp_zcDG').jqGrid('getGridParam', 'selrow');
            var devData2 = $("#sp_zcDG").jqGrid('getRowData', devRowId);
            //获取使用了该MCU的设备以取得使用了的模块号通道号
            $.post("/smosplat/getUsingPointNumByMcuAndModuleNum", { mcuUuid: mcuUuid, moduleNum: n }, function(data, status) {
                var deviceData = JSON.parse(data).rows;
                var pointNum = new Array();
                for (var j = 1; j < 9; j++) {
                    var existNum = false;
                    for (var i = 0; i < deviceData.length; i++) {
                        if (deviceData[i].moduleNum == n && deviceData[i].pointNum == j) {
                        	if(deviceData[i].deviceUuid==devData2['device.deviceUuid']){
                            	existNum=false;
                            	break;
                            }
                            existNum = true;
                        }
                    }
                    if (existNum == false) {
                        pointNum.push({ "num": j });
                    }
                }
                $("#channelNumCB").combobox("loadData", pointNum);
                var preset2=$("#presetTB").val();
                if(preset2==1){   //第一次进来进行初始化
                	$("#channelNumCB").combobox('setValue', devData2['device.pointNum']);
                	$("#presetTB").val("0");
                }else{
                	if(pointNum.length==0){
                		$("#channelNumCB").combobox('setValue', "");
                	}else{
                		$("#channelNumCB").combobox('setValue', pointNum[0].num);
                	}
                }
            });
        }
    });

    //值计算
    $("#toCalculate").bind("click", function() {
        var excal = $("#calibratedMod").val();
        var topcal = $("#calibratedModCB").combobox('getValue');
        var calibratedMod = excal * Math.pow(10, topcal * 1);
        var prmK;
        if (spData.deviceType == "混凝土应变计") {
            var totalArea = $("#totalArea").val(); //钢筋截面面积
            var sectionArea = $("#sectionArea").val(); //支撑截面面积
            var ec = $("#ec").val(); //混凝土弹性模量
            var es = $("#es").val(); //钢筋计弹性模量
            if (sectionArea * 1 < totalArea * 1) {
                swal({title:"计算失败！",text:"支撑截面面积不能小于钢筋总面积",type:"warning"});
                return;
            };
            prmK = calibratedMod * (es * totalArea + sectionArea * ec);
        } else if (spData.deviceType == "混凝土应力计") {
            var totalArea = $("#totalArea").val(); //钢筋截面面积
            var sectionArea = $("#sectionArea").val(); //支撑截面面积
            var esArea = $("#esArea").val(); //钢筋计面积
            var ec = $("#ec").val(); //混凝土弹性模量
            var es = $("#es").val(); //钢筋计弹性模量
            if (sectionArea * 1 < totalArea * 1) {
                swal({title:"计算失败！",text:"支撑截面面积不能小于钢筋总面积",type:"warning"});
                return;
            };

            prmK = (calibratedMod / esArea) * ((ec / es) * sectionArea+totalArea);

        } else if (spData.deviceType == "钢支撑应变计") {
            var pI = 3.1415926535898;
            var es = $("#es").val();
            var outerR = $("#outerR").val(); //钢管外径
            var innerR = $("#innerR").val(); //钢管内径
            if (outerR * 1 < innerR * 1) {
                swal({title:"计算失败！",text:"钢管外径不能小于钢管内径",type:"warning"});
                return;
            };
            prmK = calibratedMod * es * pI * (outerR * outerR - innerR * innerR);

        } else if (spData.deviceType == "钢支撑轴力计") {
            prmK = calibratedMod;
        }
        $("#calculatedValue").textbox({ value: prmK.toFixed(4) });
    })



}

function deleteSPDevice(rowId) {
    var monitorRowId = $('#monitorItemDG').jqGrid('getGridParam', 'selrow');
    var number = $("#monitorItemDG").jqGrid('getRowData', monitorRowId).number;
    var delUrl = "";
    var uuid = "";
    $("#sp_zcDG").jqGrid('setSelection', rowId);
    var data = $("#sp_zcDG").jqGrid('getRowData', rowId);
    if(number == 4){
    	delUrl = "deleteClinometer";
        uuid = data.clinometerUuid;
    }if (number == 15) {
        delUrl = "deleteStress";
        uuid = data.stressUuid;
    } else if (number == 18) {
        delUrl = "deleteCableMeter";
        uuid = data.cableMeterUuid;
    }
    swal({
        title: "警告",
        text: "将设备的设置数据！您确定要删除吗?",
        type: "warning",
        showCancelButton: true,
        confirmButtonColor: "#DD6B55",
        confirmButtonText: "是的，我要删除！",
        cancelButtonText: "让我再考虑一下…",
        closeOnConfirm: false,
        closeOnCancel: false
    }, function(isConfirm) {
        if (isConfirm) {
            $.post("/smosplat/" + delUrl, { stressUuid: uuid }, function(data, status) {
            	//刷新本地数据
                $("#sp_zcDG").jqGrid('delRowData', rowId);
                swal("删除成功！", "您已经删除了该监测点。", "success");
            });
        } else {
            swal("已取消", "您取消了删除操作！", "error")
        }
    })

}
function createRows(){
	var deep=$("#deep").val();
	//当输入非0.5的整数倍时将输入的小数点后面的数删除
	if(deep%0.5!=0){
		$("#deep").val(deep-deep%1+".");
		deep=deep-deep%1;
	}
	var cxTable=document.getElementById('cxTableEdit');
	var deepRowIndex=document.getElementById('deep').parentNode.parentNode.rowIndex;
	var tableRows=cxTable.rows.length;
	//删除表中的所有初始累积值输入框的行
	for(var ro=0;ro<(tableRows-deepRowIndex-1);ro++){
		cxTable.deleteRow(deepRowIndex+1);
	}
    
	var deepNum=0;
	var preDeep=0;
	for(var i=0;i<(deep/0.5);i++){
		deepNum=deepNum+0.5;
		preDeep=deepNum-0.5;
		var tr="";
		//判断是否为偶数项
		if((i+1)%2==0){
			tr='<tr><th colspan="1">'+preDeep+'m初始累计值：</th><td colspan="1"><input id="originalTotalValue'+(i-1)+'" name="originalTotalValue" class="input" style="width:70%;" required value=0>mm</td>'+
			   '<th colspan="1">'+deepNum+'m初始累计值：</th><td colspan="1"><input id="originalTotalValue'+i+'" name="originalTotalValue" class="input" style="width:70%;" required value=0>mm</td></tr>';
		}else{
			if(deepNum==deep){
				tr='<tr><th colspan="1">'+deepNum+'m初始累计值：</th><td colspan="3"><input id="originalTotalValue'+i+'" name="originalTotalValue" class="input" style="width:22.5%;" required value=0>mm</td></tr>';
			}
		};
		if(tr.length>0){
			$("#cxTableEdit").append(tr)
		}
	}

}

function createRowsUpdate(originalTotalValueString,isPreSet){
	var deep=$("#deepForUpdate").val();
	if(deep%0.5!=0){
		$("#deepForUpdate").val(deep-deep%1+".");
		deep=deep-deep%1;
	}
	
	var cxTable=document.getElementById('cxTableUpdate');
	var deepRowIndex=document.getElementById('deepForUpdate').parentNode.parentNode.rowIndex;
	var tableRows=cxTable.rows.length;
	for(var ro=0;ro<(tableRows-deepRowIndex-1);ro++){
		cxTable.deleteRow(deepRowIndex+1);
	}
    
	var deepNum=0;
	var preDeep=0;
	var originalTotalValueArr=new Array();
	if(isPreSet){
		originalTotalValueArr=originalTotalValueString.split("#");
	}
	for(var i=0;i<(deep/0.5);i++){
		deepNum=deepNum+0.5;
		preDeep=deepNum-0.5;
		var valueNum=0;
		var preValueNum=0;
		var tr="";
		//判断是否为偶数项时新增一行两个初始累计值的框
		if((i+1)%2==0){
			if(isPreSet){
				preValueNum=(isNaN(parseInt(originalTotalValueArr[i-1],10))) ? 0 : originalTotalValueArr[i-1];
				valueNum=(isNaN(parseInt(originalTotalValueArr[i],10))) ? 0 : originalTotalValueArr[i];
			};
			tr='<tr><th colspan="1">'+preDeep+'m初始累计值：</th><td colspan="1"><input id="originalTotalValueUpdate'+(i-1)+'" name="originalTotalValue" class="input" style="width:70%;" value="'+preValueNum+'"required>mm</td>'+
			   '<th colspan="1">'+deepNum+'m初始累计值：</th><td colspan="1"><input id="originalTotalValueUpdate'+i+'" name="deep" class="input" style="width:70%;" value="'+valueNum+'"required>mm</td></tr>';
			$("#cxTableUpdate").append(tr);
		}else{
			//当为奇数项且是最后一项时仅新增一行一个初始累计值的框
			if(deepNum==deep){
				if(isPreSet){
					preValueNum=originalTotalValueArr[i];
				};
				tr='<tr><th colspan="1">'+deepNum+'m初始累计值：</th><td colspan="3"><input id="originalTotalValueUpdate'+i+'" name="originalTotalValue" class="input" style="width:22.5%;" value="'+preValueNum+'" required>mm</td></tr>';
				$("#cxTableUpdate").append(tr);
			}
		};
	}
}
function getCXOriginalTotalValue(deep,isUpdate){
	var cxOriginalTotalValue="";
	var deepNum=0;
	var elementId="";
	if(isUpdate){
		elementId='#originalTotalValueUpdate';
	}else{
		elementId='#originalTotalValue';
	}
	for(var i=0;i<(deep/0.5);i++){
		deepNum=deepNum+0.5;
		theElementId=elementId+i;
		if(deepNum==deep){
			cxOriginalTotalValue=cxOriginalTotalValue+$(theElementId).val()*1;
		}else{
			cxOriginalTotalValue=cxOriginalTotalValue+$(theElementId).val()*1+"#";
		}
	}
	return cxOriginalTotalValue;
}