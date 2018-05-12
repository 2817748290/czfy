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
    $("#nickname").bind("input propertychange",function(e){
        $("#nickname").validateField();
    });
	//登录按钮单击
	$(".btn-login").click(function(e){
        window.location.href = '/login.html'
    });
    $(".btn-reg").click(function(e){
        if($("#regForm").validateForm()){
            var data = $("#regForm").serializeObject();
            console.log(data);
            $.postJSON(data,"/user/userRegister",function(data){
                if(data.status===1){
                    alert("恭喜您,注册成功!");
                    window.location.href = data.nextPage;
                }else {
                    alert("注册失败")
                }
            });
        }
    });
}
