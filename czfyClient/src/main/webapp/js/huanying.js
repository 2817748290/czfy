$(function(){
	var client_height = document.body.clientHeight+'px';
	var client_width = document.body.clientWidth+'px';
	var img_list_right;
	$('.box').css({
			'height' : client_height,
			'width' : client_width
	});
	if(document.body.scrollWidth > 1200){
	    img_list_right = document.body.clientWidth*100/1440+'px';
	}else{
		img_list_right = document.body.clientWidth*100/2880+'px';
	}
	$('.img_list img.pic').css('margin-right',img_list_right);
  
	function change(){
		client_height = document.body.clientHeight+'px';
		client_width = document.body.clientWidth+'px';
		if(document.body.scrollWidth > 1000){
		    img_list_right = document.body.clientWidth*100/1440+'px';
		}else{
			img_list_right = document.body.clientWidth*100/2000+'px';
		}
		$('.img_list img.pic').css('margin-right',img_list_right);	
		$('.box').css({
			'height' : client_height,
			'width' : client_width
		});
	}
	$(window).resize(function(){
		change();
	    if(document.body.scrollWidth === screen.availWidth){
	    	window.location.reload();  
	    }

	});
})