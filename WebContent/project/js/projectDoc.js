var project;
//基坑监测方案文件路径
var monitoringPlanPath;
//监测方案审批表文件路径
var monitorPlanFormPath;
//其他文件路径
var projectOtherPath;
$(function(){
	$('#getTxt').on('click',function(e){
//		var tsJson=readTxt("C:\\Users\\Administrator\\Desktop\\123.txt");
//		$.post("checkManualWYSData",{tsData:tsJson,sourceData:""},function(data,status){});
//		window.location.href="download?fileName=201709121003.txt&path=''";
		return;
//		$.get("download?"+"fileName=201709121003.txt",function(data,status){});
//			var jsonObj = JSON.parse(data);
//			if(jsonObj.result == 0){
//				var projectUuid = jsonObj.project.projectUuid;
//				$.post("getProjectByUuid",{projectUuid:"5e8fb5cb-0d9c-4e7d-b11a-54938bc18f9f"},function(data,status){});
//			}
//		});
	});
//	$('#stop').on('click',function(e){
//		$.post("stopCallCollect",{networkUuid:"50346dc4-4c15-4db0-8e81-4806ffbda370"},function(data,status){});
//	});

	$.post("getCurrentProjectInfo",function(data,status){
		var jsonData=JSON.parse(data);
		project=jsonData.project;
		$.post("getProjectFileUrls",{projectUuid:project.projectUuid},function(data,status){
			var paths = JSON.parse(data);
			//基坑监测方案审批表，监测方案
			showFiles(paths);
		})
		//其他文件
		$.post("getProjectOtherFileUrls",{projectUuid:project.projectUuid},function(data,status){
			var paths = JSON.parse(data);
			$("#other")[0].innerHTML = "";
			projectOtherPath = paths["other"];
			if(projectOtherPath != undefined){
				for(var i = 0; i < projectOtherPath.length;i++){
					var urlPortion = projectOtherPath[i].split('\\');
					var src ="";
					for(var j=0;j<urlPortion.length;j++){
						src+=urlPortion[j];
						if(j<urlPortion.length-1){
							src+="\\";
							src+="\\";
						}
					}
					var htmlContent = "<a "+"href=\""+localhostPath+projectOtherPath[i]+"\""+">"+urlPortion[urlPortion.length-1]+"</a>";
					var delet = '&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+'<a href=\"#\" class=\"glyphicon glyphicon-trash\" style = \"color:red\"  data-options=\"iconCls:\'icon-remove\'\" onclick="deleteProjectOtherFiles(' +'\''+ project.projectUuid+ '\'' + ','+'\''+ src +'\''+')">'+"删除"+'</a>'+'&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+'&nbsp';
					htmlContent += delet;
					$("#other").append(htmlContent);
				}
			}
		})
	});
	$('#addApprovalFormBtn').on('click', function(e) {
		$("#project_input1").trigger('click');
	})
	$("#project_input1").bind('change',function(e){
    	//上传
        var fileParams = new Array();
        fileParams.push(project.organ.organUuid);
        fileParams.push(project.projectUuid);
        //1代表上传监测方案审批表，type=1;
        fileParams.push(1);
    	//6代表调取上传project项目
        importFileClick("project_input1", "projectDocForm1", 6, "doc", fileParams, function(_url) {
        	if(_url != undefined){
        		var urlPortion = _url.split('/');
        		var htmlContent = "<a "+"href=\""+localhostPath+_url+"\""+">"+urlPortion[urlPortion.length-1]+"</a>";
        		var delet = '&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+'<a href=\"#\" class=\"glyphicon glyphicon-trash\" style = \"color:red\"  onclick="deletProjectFile(' +'\''+ project.projectUuid+ '\'' + ','+'\''+'/monitorPlanForm'+'\''+')">'+"删除"+'</a>'
        		htmlContent += delet;
        		$("#approvalForm").append(htmlContent);
        	}
		});
	});
	$('#addPlanBtn').on('click', function(e) {
		$("#project_input2").trigger('click');
	})
	$("#project_input2").bind('change',function(e){
    	//上传
        var fileParams = new Array();
        fileParams.push(project.organ.organUuid);
        fileParams.push(project.projectUuid);
        //2代表上传监测方案，type=2;
        fileParams.push(2);
    	//6代表调取上传project项目
        importFileClick("project_input2", "projectDocForm2", 6, "doc", fileParams, function(_url) {
        	if(_url != undefined){
        		var urlPortion = _url.split('/');
        		var htmlContent = "<a "+"href=\""+localhostPath+_url+"\""+">"+urlPortion[urlPortion.length-1]+"</a>";
        		var delet = '&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+'<a href=\"#\" class=\"glyphicon glyphicon-trash\" style = \"color:red\"  onclick="deletProjectFile(' +'\''+ project.projectUuid+ '\'' + ','+'\''+'/monitoringPlan'+'\''+')">'+"删除"+'</a>'
        		htmlContent += delet;
        		$("#plan").append(htmlContent);
        	}
		});
	});
	$('#addOtherFileBtn').on('click', function(e) {
		$("#project_input3").trigger('click');
	})
	$("#project_input3").bind('change',function(e){
    	//上传
        var fileParams = new Array();
        fileParams.push(project.organ.organUuid);
        fileParams.push(project.projectUuid);
        //3代表上传其他监测方案文件，type=3;
        fileParams.push(3);
    	//6代表调取上传project项目
        importFileClick("project_input3", "projectDocForm3", 6, "all", fileParams, function(_url) {
        	if(_url != undefined){
				var urlPortion = _url.split('\\');
				var src ="";
				for(var j=0;j<urlPortion.length;j++){
					src+=urlPortion[j];
					if(j<urlPortion.length-1){
						//如果这里不加两个反斜杠，onclick传参数过去之后反斜杠就被去掉了
						src+="\\";
						src+="\\";
					}
				}
        		var htmlContent ="<a "+"href=\""+localhostPath+_url+"\""+">"+urlPortion[urlPortion.length-1]+"</a>";
        		var delet = '&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+'<a href=\"#\" class=\"glyphicon glyphicon-trash\" data-options=\"iconCls:\'icon-remove\'\" style = \"color:red\"  onclick="deleteProjectOtherFiles(' +'\''+ project.projectUuid+ '\'' + ','+'\''+ src +'\''+')">'+"删除"+'</a>'+'&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+'&nbsp';
        		htmlContent += delet;
        		$("#other").append(htmlContent);
        	}
		});
	});

	
});

function showFiles(paths){
	//清空
	$("#approvalForm")[0].innerHTML ="";
	$("#plan")[0].innerHTML = "";

	
	monitorPlanFormPath = paths["monitorPlanForm"];
	if(monitorPlanFormPath != undefined){
		var urlPortion = monitorPlanFormPath.split('\\');
		var htmlContent = "<a "+"href=\""+localhostPath+monitorPlanFormPath+"\""+">"+urlPortion[urlPortion.length-1]+"</a>";
		var delet = '&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+'<a href=\"#\" style = \"color:red\" class=\"glyphicon glyphicon-trash\" data-options=\"iconCls:\'icon-remove\'\" onclick="deletProjectFile(' +'\''+ project.projectUuid+ '\'' + ','+'\''+'/monitorPlanForm'+'\''+')">'+"删除"+'</a>'
		htmlContent += delet;
		$("#approvalForm").append(htmlContent);
	}
	monitoringPlanPath = paths["monitoringPlan"];
	if(monitoringPlanPath != undefined){
		var urlPortion = monitoringPlanPath.split('\\');
		var htmlContent = "<a "+"href=\""+localhostPath+monitoringPlanPath+"\""+">"+urlPortion[urlPortion.length-1]+"</a>";
		var delet = '&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+'<a href=\"#\" class=\"glyphicon glyphicon-trash\" style = \"color:red\"  onclick="deletProjectFile(' +'\''+ project.projectUuid+ '\'' + ','+'\''+'/monitoringPlan'+'\''+')">'+"删除"+'</a>'
		htmlContent += delet;
		$("#plan").append(htmlContent);
	}

}

function deletProjectFile(projectUuid,type){
	$.post("/smosplat/deletProjectFiles",{projectUuid:projectUuid,type:type},function(data,status){
		var dataJson = JSON.parse(data);
			if(dataJson.result == 0){
				$.post("getProjectFileUrls",{projectUuid:project.projectUuid},function(data,status){
					var paths = JSON.parse(data);
					showFiles(paths);
				})
			}
		});
}

function deleteProjectOtherFiles(projectUuid,src){
	$.post("/smosplat/deleteProjectOtherFiles",{projectUuid:projectUuid,src:src},function(data,status){
		var dataJson = JSON.parse(data);
			if(dataJson.result == 0){
				$.post("getProjectOtherFileUrls",{projectUuid:project.projectUuid},function(data,status){
					var paths = JSON.parse(data);
					$("#other")[0].innerHTML = "";
					projectOtherPath = paths["other"];
					if(projectOtherPath != undefined){
						for(var i = 0; i < projectOtherPath.length;i++){
							var urlPortion = projectOtherPath[i].split('\\');
							var src ="";
							for(var j=0;j<urlPortion.length;j++){
								src+=urlPortion[j];
								if(j<urlPortion.length-1){
									src+="\\";
									src+="\\";
								}
							}
							var htmlContent = '&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+"<a "+"href=\""+localhostPath+projectOtherPath[i]+"\""+">"+urlPortion[urlPortion.length-1]+"</a>";
							var delet = '&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+'<a href=\"#\" class=\"easyui-linkbutton\" data-options=\"iconCls:\'icon-remove\'\" onclick="deleteProjectOtherFiles(' +'\''+ project.projectUuid+ '\'' + ','+'\''+ src +'\''+')">'+"删除"+'</a>'
							htmlContent += delet;
							$("#other").append(htmlContent);
						}
					}
				})
			}
		});
}

function readTxt(filePath){
	var fso = new ActiveXObject("Scripting.FileSystemObject");
	var f = fso.OpenTextFile(filePath,1);
	var s = "";
	while (!f.AtEndOfStream)
	s += f.ReadLine()+"\n";
	f.Close();
	return s;
	}