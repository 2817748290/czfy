// videojs.options.flash.swf = "video/date.mp4";
$(function(){

	var right_width = (document.body.clientWidth-$('.leftSide').width())*0.58+'px';
	$('#example_video1').css('width',right_width);
	function  getParma(name) {
		var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
		var r = window.location.search.substr(1).match(reg);
		if (r != null) return unescape(r[2]); return null;
	}
    // var BASE_URL ="http://localhost:8080";
	//var BASE_URL = "http://139.199.61.16/czfyClient";
	var data = {
		"videoId":getParma('id')
	}
	console.log(data);
	$.ajax({
		url: BASE_URL + "/client/getvideobyid",
		type: 'POST',
		data:JSON.stringify(data),
		contentType:"application/json; charset=utf-8",
		success: function(data){
			console.log("传回数据");
            console.log(data);
			if(data.status ==1){
				var html = '<p class="movie_author_name">'+
								'<label>上&nbsp;&nbsp;传&nbsp;&nbsp;者：</label>'+
								'<span>'+data.video.authorNickname+'</span>'+
							'</p>'+
							'<p class="update">'+
								'<label>上传时间：</label>'+
								'<span>'+data.video.uploadTime+'</span>'+
							'</p>'+
							'<p class="movie_title">'+
								'<label>视频名称：</label>'+
								'<span>'+data.video.videoName+'</span>'+
							'</p>'+
							'<p>'+
								'<label>视频简介：</label>'+
								'<span class="video_txt">'+data.video.videoIntroducation+'</span>'+
							'</p>';
                $('video').attr('src',data.video.videoPath);
                $('.p-title').html(data.video.videoName);
				$('.data').append(html);
                //






































			}

		}
	});
	if($('.movie_title span').width() >= 130){
    	$('.movie_title span').css('text-indent','26px');
    }else{
    	$('.movie_title span').css('text-indent','0px');
    }

	$(window).resize(function(){
		right_width = (document.body.clientWidth-$('.leftSide').width())*0.58+'px';
		$('#example_video1').css('width',right_width);
	});

	//评论
	$(document).on('click','.comment_show',function(e){
		$(this).parent().parent().find('.comment_text').toggle();
		e.stopPropagation();
		localStorage.setItem('people' ,$(this).parent().prev().find('span').html());
		$('.leftSide').css('height',$('.rightSide').height()+'px');
	});
	$(document).on('click','.new_comment_show',function(e){
		$(this).parent().parent().parent().next('.comment_text').toggle();
		$('.leftSide').css('height',$('.rightSide').height()+'px');
		e.stopPropagation();
		localStorage.setItem('people',$(this).parent().prev().find('.reply').html());
	});
	
	$(document).click(function(){
		$('.comment_text').hide();
		$('.leftSide').css('height',$('.rightSide').height()+'px');
	});

	//评论
	$(document).on('click','.comment_btn',function(e){
		var media;
		media = "<div class='media'>"
				  	+"<div class='media-left'>"
						+"<img src='"+$('.avatar img').attr('src')+"' class='media-object'>"
					+"</div>"
					+"<div class='media-body'>"
					+"<h4 class='media-heading'><span><font class='reply'>"+$('.name .login').html()+"</font><font color='black'>回复</font>"+localStorage.getItem('people')+"</span>："+$('.comment_box').html()+"</h4>"
						+"<h5>"
							+"<span>17:56</span>"
							+"<img class='new_comment_show' src='images/movie/comment.png'>"
						+"</h5>"
					+"</div>"
				+"</div>";
		if($('.name .login').html() == localStorage.getItem('people')){
			alert('不能回复自己');
			return;
		}
		if($('.comment_box').html() != ''){
			$('.comment_text').hide();
			$(this).parent().parent().find('.comment_text').before(media);
           
		}
		$('.leftSide').css('height',$('.rightSide').height()+'px');
		$('.comment_box').html('');
		e.stopPropagation();
	});
	//回复
	$('.movie_btn').on('click',function(){
		var media;
		media = "<li class='media'>"
						+"<div class='media-left'>"
							+"<img src='"+$('.avatar img').attr('src')+"' class='media-object'>"
						+"</div>"
						+"<div class='media-body'>"
						+"<h4 class='media-heading'><span>"+$('.name .login').html()+"</span>："+$('.movie_box').html()+"</h4>"
							+"<h5>"
								+"<span>17:56</span>"
								+"<img class='comment_show' src='images/movie/comment.png'>"
							+"</h5>"
							+"<div class='comment_text'>"
								+"<div class='comment_box' contenteditable='true'></div>"
								+"<div class='comment_btn'>回复</div>"
							+"</div>"
						+"</div>"
					+"</li>";
		$('.media-list').prepend(media);
		$('.movie_box').html('');
		$('.leftSide').css('height',$('.rightSide').height()+'px');
	});

	$(document).on('click','.comment_text,.media',function(e){
		e.stopPropagation();
	});

})
