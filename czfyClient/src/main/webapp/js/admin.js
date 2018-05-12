$(function(){
	
	var window_height = document.body.scrollHeight +'px';
	var left_width = $('.leftSide').css('width');
	function init(){
		$('.leftSide,.rightSide').css('height',window_height);
		
		$('.rightSide').css('width',document.body.clientWidth-$('.leftSide').width()+'px');

		$('.bg-color').css({
			'width':left_width,
			'left':left_width
		});
	}
	init();


	//日历
	$('#birthday').datepicker({
     changeMonth : true,
     changeYear : true,
     yearRange : "1950:2020",
     dateFormat:'yy-mm-dd',
    });

	//修改信息后未点击保存
	$('form.message').change(function(){
		var result = confirm('确认修改信息');
		if(result){
			$('.mess').show();
			$('.mess_change').hide();
		}
		else{
            return;
		}
	});

    //title的切换
    $('.btn_mess').click(function(){
    	$('.mess').show();
    	$('.mess_change').hide();
    });
    $('.btn_mess_change').click(function(){
    	$('.mess').hide();
    	$('.mess_change').show();
    });
	

	
});