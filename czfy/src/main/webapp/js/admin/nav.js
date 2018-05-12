

var BASE_URL = "/";
var NAV_URL = {
	login:BASE_URL + "login.html",
	index:BASE_URL + "index.html",
	actor:BASE_URL + "actor.html",
	video:BASE_URL + "video.html",
	news:BASE_URL + "news.html",
	project:BASE_URL + "project.html",
	literature:BASE_URL + "literature.html",
    information:BASE_URL + "information.html"
};




function nav_init(){
	
	var nickname = resolveParameter(window);
	console.log(nickname);
	var search = "?" + $.base64.encode("nickname=" + nickname)
	$("#adminNickname").html(nickname);
	$("#logout-btn").bind('click',function(){
		forwardTo(window,NAV_URL.login)
	});
	$("#actor").bind('click',function(){
        forwardTo(window,NAV_URL.actor + search)
    });
	$("#video").bind('click',function(){
		forwardTo(window,NAV_URL.video + search)
	})
	$("#news").bind('click',function(){
		forwardTo(window,NAV_URL.news + search)
	})
	$("#literature").bind('click',function(){
		forwardTo(window,NAV_URL.literature + search)
	})
	$("#project").bind('click',function(){
		forwardTo(window,NAV_URL.project + search)
	})
    $("#information").bind('click',function(){
        forwardTo(window,NAV_URL.information + search)
    })
	
	
}

function resolveParameter(window){
	$.base64.utf8encode = true;
	$.base64.utf8decode = true;
	var search = $.base64.decode(window.location.search.substring(1));
	var newUrl = window.location.origin + window.location.pathname +　"?" + search;
	$.url.setUrl(newUrl);
	return $.url.param("nickname");
}

nav_init();



