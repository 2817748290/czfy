$(function(){
	// $('.top').css("background-size","100%");
	$('.back img,.back span').click(function(){
		window.open('actor.html','_self');
	});
	function  getParma(name) {
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if (r != null) return unescape(r[2]); return null;
	}
	//var BASE_URL = "http://139.199.61.16/czfyClient";
    //var BASE_URL ="http://localhost:8080"
	var data = {
		"actorId":getParma('id')
	} 
	$.ajax({
		url: BASE_URL + "/client/getactorbyid",
		type: 'POST',
		data:JSON.stringify(data),
		contentType:"application/json; charset=utf-8",
		success: function(data){
			if(data.status ==1){
				var html = '<div class="nine columns">'+

					'<img src="'+data.actorInfo.actor.photo+'" class="portfolio-full-width" style="height:500px;">'+

					'</div>'+

					'<div class="three columns">'+

					'<div class="title title-icon">'+data.actorInfo.actor.actorName+'</div>'+

					'<div class="text margin-tb-10">'+data.actorInfo.actor.actorIntroducation+'</div>'+

				'<br><br><br><br><br><br>'+
				'<div class="clear"></div>'+
				'<a href="javascript:void(0)" title="Check Our Work" class="icon-with-small-text magnifier margin-tb-20">详细查询</a>'+
				'<a href="javascript:void(0)" title="Share This" class="icon-with-small-text connection-icon margin-tb-20">分享</a>'+
				'</div>';
				$('.mess_list').append(html);
				html = '';
				$.each(data.actorInfo.images,function(index,val){
					html += '<div class="item">'+

						'<img src="'+val.imageUrl+'" alt="carousel item" />'+

						'<div class="item-background background-color"></div>'+

						'<a class="itemtitle" title="carousel item"> </a>'+

						'</div>';
				});
				$('.items_list').append(html);
			}
		}
	});
});