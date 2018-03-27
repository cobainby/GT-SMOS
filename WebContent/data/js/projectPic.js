var project;
$(function() {
	$.post("getCurrentProjectInfo",function(data,status){
		project = JSON.parse(data).project;
	    //获取图片
	    $.post("getProjectPicUrls", {projectUuid: project.projectUuid }, function(data, status) {
	        var jsonPaths = JSON.parse(data);
			var projectPicPath = jsonPaths["projectPic"];
	        var projectPic = jsonPaths.projectPic;
	        if (projectPic != null) {
	            $("#projectPic").attr("src", localhostPath + projectPicPath[0]);
	        } else  {
	            return;
	        }
	    });
	});
});




