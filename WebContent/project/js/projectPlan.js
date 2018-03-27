var project;
$(function() {
	$.post("getCurrentProjectInfo",function(data,status){
		project = JSON.parse(data).project;
	    //获取图片
	    $.post("getProjectPicUrls", {projectUuid: project.projectUuid }, function(data, status) {
	        var jsonPaths = JSON.parse(data);
			$("#picForm")[0].innerHTML = "";
			var projectPicPath = jsonPaths["projectPic"];
			if(projectPicPath != undefined){
				for(var i = 0; i < projectPicPath.length;i++){
					var urlPortion = projectPicPath[i];
					var fileName = urlPortion.substring(urlPortion.lastIndexOf('/')+1);
					var htmlContent = "<a href='javascript:void(0);'  onclick=\"viewPic('" + localhostPath+projectPicPath[i] + "')\">"+fileName+"</a>";
					var delet = '&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+'<a href=\"#\" class=\"glyphicon glyphicon-trash\" style = \"color:red\"  data-options=\"iconCls:\'icon-remove\'\" onclick="deleteProjectPicFiles(' +'\''+ project.projectUuid+ '\'' + ','+'\''+ urlPortion +'\''+')">'+"删除"+'</a>'+'&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+'&nbsp';
					htmlContent += delet;
					$("#picForm").append(htmlContent);
				}
			}
	        var projectPic = jsonPaths.projectPic;
	        if (projectPic != null) {
	            $("#projectPic").attr("src", localhostPath + projectPicPath[0]);
	        } else  {
	            return;
	        }
	    });
		
	    $('#addPicFileBtn').on('click', function(e) {
	        $("#projectPic_input").trigger('click');
	    })
	    $("#projectPic_input").bind('change', function(e) {
	    	//上传照片
            var fileParams = new Array();
            fileParams.push(project.organ.organUuid);
            fileParams.push(project.projectUuid);
            //0代表上传工程底图，type=0;
            fileParams.push(0);
	    	//6代表调取上传project项目
	        importFileClick("projectPic_input", "projectPicForm", 6, "img", fileParams, function(_url) {
	        	
	        	if(_url != undefined){
	        		var fileName = _url.substring(_url.lastIndexOf('/')+1);
	        		var htmlContent = "<a href='javascript:void(0);'  onclick=\"viewPic('" + _url + "')\">"+fileName+"</a>";
	        		var delet = '&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+'<a href=\"#\" class=\"glyphicon glyphicon-trash\" style = \"color:red\"  data-options=\"iconCls:\'icon-remove\'\" onclick="deleteProjectPicFiles(' +'\''+ project.projectUuid+ '\'' + ','+'\''+ _url +'\''+')">'+"删除"+'</a>'+'&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+'&nbsp';
	        		htmlContent += delet;
	        		$("#picForm").append(htmlContent);
	        	}
	        	
	        	$("#projectPic").attr("src", localhostPath + _url);
			});
	    });
	    //删除图片
	    $('#delPicFileBtn').on('click', function(e) {
		    $.post("/smosplat/deletProjectFiles",{projectUuid:project.projectUuid,type:"projectPic"},function(data,status){
		    	 //获取图片
			    $.post("getProjectFileUrls", {projectUuid: project.projectUuid }, function(data, status) {
			        var jsonPaths = JSON.parse(data);
			        var projectPic = jsonPaths.projectPic;
			        if (projectPic == null) {
			            $("#projectPic").attr("src", "");
			        }
			    });
		    	swal({title:"操作成功！",type:"success"});
		    });
	    })

	});
});


function viewPic(url){
	$("#projectPic").attr("src", url);
}

function deleteProjectPicFiles(projectUuid,src){
	$.post("/smosplat/deleteProjectOtherFiles",{projectUuid:projectUuid,src:src},function(data,status){
		var dataJson = JSON.parse(data);
			if(dataJson.result == 0){
			    //获取图片
			    $.post("getProjectPicUrls", {projectUuid: project.projectUuid }, function(data, status) {
			        var jsonPaths = JSON.parse(data);
					$("#picForm")[0].innerHTML = "";
					var projectPicPath = jsonPaths["projectPic"];
					if(projectPicPath != undefined){
						for(var i = 0; i < projectPicPath.length;i++){
							var urlPortion = projectPicPath[i];
							var fileName = urlPortion.substring(urlPortion.lastIndexOf('/')+1);
							var htmlContent = "<a href='javascript:void(0);'  onclick=\"viewPic('" + localhostPath+projectPicPath[i] + "')\">"+fileName+"</a>";
							var delet = '&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+'<a href=\"#\" class=\"glyphicon glyphicon-trash\" style = \"color:red\"  data-options=\"iconCls:\'icon-remove\'\" onclick="deleteProjectPicFiles(' +'\''+ project.projectUuid+ '\'' + ','+'\''+ urlPortion +'\''+')">'+"删除"+'</a>'+'&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+'&nbsp'+'&nbsp';
							htmlContent += delet;
							$("#picForm").append(htmlContent);
						}
					}
			        var projectPic = jsonPaths.projectPic;
			        if (projectPic.length != 0) {
			            $("#projectPic").attr("src", localhostPath + projectPicPath[0]);
			        } else  {
			        	$("#projectPic").attr("src", "");
			        }
			    });
			}
		});
}

