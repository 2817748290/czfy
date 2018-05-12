$(function(){
	initEvents();
});
function initEvents(){
	
	$.base64.utf8encode = true;
	$.base64.utf8decode = true;
	
	$("#username").bind("input propertychange",function(e){
		$("#username").validateField();
	});
	$("#password").bind("input propertychange",function(e){
		$("#password").validateField();
	});
	//登录按钮单击
	$(".btn-login").click(function(e){
        if($("#loginForm").validateForm()){
            var data = $("#loginForm").serializeObject();
            $.postJSON(data,"/user/userLogin",function(data1){
                if(data1.status==1){
                    var search = "?" + $.base64.encode("nickname=" + data1.user.nickname)
                    window.location.href = data1.nextPage+search;
                }else {
                    alert("帐号或密码错误");
                }
            });
        }
    });
    $(".btn-reg").click(function(e){
		window.location.href = '/reg.html'
    });
}
