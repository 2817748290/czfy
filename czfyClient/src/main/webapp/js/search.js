$(function(){
	$('.leftSide').css('height',$('.footer').get(0).offsetTop+70+'px');
    $(window).resize(function(){
        $('.leftSide').css('height',$('.footer').get(0).offsetTop+70+'px');
    });

//	$(document).on('click','.actor_list > div',function(){
//		var pic_address = $(this).find('img').attr('src');
//		var pic_name = $(this).find('p').html();
//		localStorage.setItem('pic_address',pic_address);
//		localStorage.setItem('pic_name',pic_name);
//		window.open('doactor_mess.html','_self');
//	});
	$(document).on('click','.project_list > div',function(){
		// var project_address = $(this).find('img').attr('src');
		var project_name = $(this).find('p').html();
		// localStorage.setItem('pic_address',pic_address);
		localStorage.setItem('project_name',project_name);
		window.open('project_mess.html','_self');
	});
	$(document).on('mouseover','.movie_list > div',function(){
		$(this).find('.cover').show();
	});
	$(document).on('mouseout','.movie_list > div',function(){
		$(this).find('.cover').hide();
	});
	$(document).on('click','.movie_list > div',function(){
		window.open('movie_play.html?id='+$(this).data('id'),'_self');
	});
	var right_width = (document.body.clientWidth - $('.leftSide').width())*0.22 + 'px';
    $('.cover img').css('width',right_width);
    $(window).resize(function(){
    	right_width = (document.body.clientWidth - $('.leftSide').width())*0.22 + 'px';
    	$('.cover img').css('width',right_width);
    });
})