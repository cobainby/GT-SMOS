$(function(){
	//默认session会话超时时间为30分钟，这里每隔20分钟调用一次接口保证session会话不会失效
	//因为session失效后导致调用后台接口出现问题
	//浏览器关闭后服务器就会在30分钟之后回收session资源
	setInterval(function(){
   	 	$.post("/smosplat/keepSessionAlive");
	}, 20*60*1000);
});
/**
 * 将timestamp格式化为日期时间字符串,只显示上午下午
 */
function timestamp2String(timestamp) {
    if (timestamp == null) {
        return "";
    }
    var datetime = new Date();
    datetime.setTime(timestamp);
    var year = datetime.getFullYear();
    var month = datetime.getMonth() + 1 < 10 ? "0" + (datetime.getMonth() + 1) : datetime.getMonth() + 1;
    var date = datetime.getDate() < 10 ? "0" + datetime.getDate() : datetime.getDate();
    var hour = datetime.getHours() < 10 ? "0" + datetime.getHours() : datetime.getHours();
    var minute = datetime.getMinutes() < 10 ? "0" + datetime.getMinutes() : datetime.getMinutes();
    var second = datetime.getSeconds() < 10 ? "0" + datetime.getSeconds() : datetime.getSeconds();
//    return year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
//    不显示时分秒，只显示上午还是下午
    if(hour<12){
    	return year + "-" + month + "-" + date + " " + "AM";
    }else if(hour>=12){
    	return year + "-" + month + "-" + date + " " + "PM";
    }
}

/**
 * 将timestamp格式化为日期时间字符串，显示具体时间
 */
function timeStampToTimeString(timestamp) {
    if (timestamp == null) {
        return "";
    }
    var datetime = new Date();
    datetime.setTime(timestamp);
    var year = datetime.getFullYear();
    var month = datetime.getMonth() + 1 < 10 ? "0" + (datetime.getMonth() + 1) : datetime.getMonth() + 1;
    var date = datetime.getDate() < 10 ? "0" + datetime.getDate() : datetime.getDate();
    var hour = datetime.getHours() < 10 ? "0" + datetime.getHours() : datetime.getHours();
    var minute = datetime.getMinutes() < 10 ? "0" + datetime.getMinutes() : datetime.getMinutes();
    var second = datetime.getSeconds() < 10 ? "0" + datetime.getSeconds() : datetime.getSeconds();
    return year + "-" + month + "-" + date + " " + hour + ":" + minute + ":" + second;
}
/**
 * 判断数组中是否有这个对象
 */
function isInArray(array, item) {
    for (var i = 0; i < array.length; i++) {
        if (array[i] == item) {
            return true;
        }
    }
    return false;
}
//获取url中的参数
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg); //匹配目标参数
    if (r != null) return unescape(r[2]);
    return null; //返回参数值
}
//获取当前时间
$(function() {
    var mydate = new Date();
    var year = "现在是" + mydate.getFullYear() + "年";
    var month = (mydate.getMonth() + 1) + "月";
    var date = mydate.getDate() + "日";
    var hour = mydate.getHours() < 10 ? "0" + mydate.getHours() : mydate.getHours();
    var minute = mydate.getMinutes() < 10 ? "0" + mydate.getMinutes() : mydate.getMinutes();
    var second = mydate.getSeconds() < 10 ? "0" + mydate.getSeconds() : mydate.getSeconds();
    var nowTime = year + month + date + " " + hour + ":" + minute + ":" + second;
    $("#nowTime").text(nowTime);
});
