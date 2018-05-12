$(function(){
	initProducerEvents();
});

function initProducerEvents(){
	//登录按钮单击
	$("#exitBtn").click(function(e){
		forwardTo(window,INVALID_PAGE);
	});
}

