$(function(){
	var images=[{name:"organ/image/portrait1.png",date:"2017/03/01"},
		{name:"organ/image/portrait2.png",date:"2017/03/01"}];
	
	$("#qualifyImagesDG").datagrid("loadData",images);
	//为按钮添加点击事件
	$("#organMgr").bind("click",function(){
		window.location.href="/smosplat/organ";
	});
	$("#workerMgr").bind("click",function(){
		window.location.href="/smosplat/worker";
	});
	$("#accountMgr").bind("click",function(){
		window.location.href="/smosplat/account";
	});
	$("#qualMgr").bind("click",function(){
		window.location.href="/smosplat/qualify";
	});
	$("#deviceMgr").bind("click",function(){
		window.location.href="/smosplat/device";
	});
	$("#selectFileBtn").bind("click",function(){
		$("#file_input").click();
		$("#selectedImagePath").textbox('setValue',"");
		var path=$("#file_input").val();
		$("#selectedImagePath").textbox('setValue',path);
	 });
});
function importFileClick()
{
    //获取上传文件控件内容
    var file=document.getElementById('file_input').files[0];
    //判断控件中是否存在文件内容，如果不存在，弹出提示信息，阻止进一步操作
    if (file == null) { alert('错误，请选择文件'); return; }
    //获取文件名称
    var fileName = file.name;
    //获取文件类型名称
    var file_typename = fileName.substring(fileName.lastIndexOf('.'), fileName.length);
    //这里限定上传文件文件类型必须为几种图片格式，如果文件类型不符，提示错误信息
    if (file_typename == '.png'||file_typename=='.jpg'||file_typename=='.jpeg'||file_typename=='.pdf')
    {
        //计算文件大小
        var fileSize = 0;
        //如果文件大小大于1024字节X1024字节，则显示文件大小单位为MB，否则为KB
        if (file.size > 1024 * 1024) {

　　　　　　fileSize = Math.round(file.size * 100 / (1024 * 1024)) / 100;

　　　　　if (fileSize > 2) {
                alert('错误，文件超过2MB，禁止上传！'); return;
            }
　　　　　fileSize = fileSize.toString() + 'MB';
        }
        else {
            fileSize = (Math.round(file.size * 100 / 1024) / 100).toString() + 'KB';
        }
        //将文件名和文件大小显示在前端label文本中
//        document.getElementById('fileName').innerHTML = "<span style='color:Blue'>文件名: " + file.name + ',大小: ' + fileSize + "</span>";
        //获取form数据
        var formData = new FormData($( "#importFileForm" )[0]);
        //调用apicontroller后台action方法，将form数据传递给后台处理。contentType必须设置为false,否则chrome和firefox不兼容
        $.ajax({
            url: "springUpload",
            type: 'POST',
            data: formData,
            async: false,
            cache: false,
            contentType: false,
            processData: false,
            success: function (returnInfo) {
                //上传成功后将控件内容清空，并显示上传成功信息
                document.getElementById('file_input').value = null;
//                document.getElementById('uploadInfo').innerHTML = "<span style='color:Red'>" + returnInfo + "</span>";
                alert("successed!");
            },
            error: function (returnInfo) {
                //上传失败时显示上传失败信息
//                document.getElementById('uploadInfo').innerHTML = "<span style='color:Red'>" + returnInfo + "</span>";
            	alert(returnInfo.status)
            }
        });
    }
    else {
        alert("文件类型错误");
        //将错误信息显示在前端label文本中
//        document.getElementById('fileName').innerHTML = "<span style='color:Red'>错误提示:上传文件应该是.xlsx后缀而不应该是" + file_typename + ",请重新选择文件</span>"
    }
    
}

function formatDownload(value,row,index){
	var name=row.name;
	return '<img src="/smosplat/common/image/download.png" style="width:16px;height:16px;margin-bottom:-5px;">&nbsp;<a onclick="downloadFile(\''+name+'\')" href="#">下载</a>';
	
}

function downloadFile(name){
	var filename;
	$.post("download",name,function(data,status){
		
	})
}