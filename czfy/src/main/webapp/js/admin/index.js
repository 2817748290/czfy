$(function(){
	initEvents();
	
	
});




function initEvents(){
	$(".btn-reg").click(function(){
		forwardTo(window,"/register.html")
	});
	$(".btn-login").click(function(){
		forwardTo(window,"/login.html")
	});
}
