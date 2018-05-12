$(function(){
	if(localStorage.getItem('page_manage')){
		$('.right iframe').attr('src',localStorage.getItem('page_manage'));
	}
	var left_width = $('.leftSide').css('width');
	$('.bg-color').css({
		'width':left_width,
		'left':left_width
	});
	//导航栏的移入效果
	$('nav a').click(function(){
		localStorage.setItem('page_manage', $(this).attr('href'))
	});
	$('nav > ul >li').hover(function(){
        $(this).find('.bg-color').stop().animate({'left':'0'},200);
        $(this).find('.list_icon').show();

	},function(){
		$(this).find('.bg-color').stop().animate({'left':left_width},200);
		$(this).find('.list_icon').hide();
	});
	var  clickNum = ['0','0','0','0','0','0'];
	$('.manage_item').click(function(){
 
		var click_index = $(this).index();
		if(clickNum[click_index] == 1){
			$(this).css('background-color','transparent');
			$(this).next('.manage_list').hide();
			$(this).find('.list_icon').attr("src",'images/show.png');
			clickNum[click_index] = 0;
		}else{
			$(this).css('background-color','#66cccc');
			$(this).next('.manage_list').show();
			$(this).find('.list_icon').attr("src",'images/hide.png');
			clickNum[click_index] = 1;
		}
	});
	
});