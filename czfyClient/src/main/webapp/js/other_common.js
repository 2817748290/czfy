$(function(){
	var window_height = document.body.scrollHeight +'px';
	var left_width = $('.leftSide').css('width');
	function init(){
		
		$('.leftSide').css('height',$('.rightSide').height());
		$('.bg-color').css({
			'width':left_width,
			'left':left_width
		});
		$('.portfolio-full-width').attr('src',localStorage.getItem('pic_address'));
		$('.title-icon').html(localStorage.getItem('pic_name'));
		if(localStorage.getItem('pic') != null){
			$('.avatar img').attr('src',localStorage.getItem('pic'));
			$('#pic_container img').attr('src',localStorage.getItem('pic'));
		}
	}
	init();

    //导航栏的移入效果
	$('nav > ul >li').hover(function(){
        $(this).find('.bg-color').stop().animate({'left':'0'},200);
        $(this).find('.list_icon').show();

	},function(){
		$(this).find('.bg-color').stop().animate({'left':left_width},200);
		$(this).find('.list_icon').hide();
	});
	
	//登录信息
	if(localStorage.getItem('user')){
		$('.login').show();
		$('.no_login').hide();
		$('.login').html(localStorage.getItem('user'));

	};
	$('.name').hover(function(){
		if($(this).find('.login').css('display') != 'none'){
			
			$(this).find('a').css('color','#ff9900');
			$(this).next().attr('src','images/name_hide.png');
			$('.name_list').show();
		}
		
	},function(){
		$(this).find('a').css('color','#000');
		$(this).next().attr('src','images/name_show.png');
		$('.name_list').hide();
	});

	//头像更换
	$(".pic").click(function(){
		$("#change_box").show();
	});
	$('#close').click(function(){
		$("#change_box").hide();
	});
	var attr;
	$('.file').change(function(){
		var fr = new FileReader();
		var img = new Image();
		fr.readAsDataURL(this.files[0]);
		fr.onload = function(){
			attr = this.result;
			$('#pic_container img').attr('src',this.result);
		}
		
	});
	$('#content_form #submit').click(function(){
		$('.avatar img').attr('src',attr);
		$('#change_box').hide();
		localStorage.setItem("pic",attr);
		return false;
	});

	//退出
	$('.quit').click(function(){
		$('.login').hide();
		$('.no_login').show();
		$('.name').next().hide();
		window.open('login.html','_self');
		localStorage.removeItem("user");
		localStorage.removeItem("isUser");
	});


	//登录
	$('.no_login').click(function(){
		window.open('login.html','_self');
	});
	
	$('.search .search_btn').click(function(){

	});
	$('.list li').click(function(){
    	var item_name = $(this).html();
    	localStorage.setItem('item_name',item_name);
    	window.open('project.html','_self');
    });



})
function openItem(page){

	if(page == 'admin'){
		if(localStorage.getItem('isUser') == 'yes'){
			window.open('data_input_project.html','_self');
		}else if(localStorage.getItem('isUser') == 'no'){
			window.open(page+'.html','_self');
		}else{
			alert('请先登录');
			window.open('login.html','_self');
		}
		
	}else{
		window.open(page+'.html','_self');
	}
    return false;
}