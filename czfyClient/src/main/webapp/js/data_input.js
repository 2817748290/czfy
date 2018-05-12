$(function(){
	$('.succ span').click(function(){
		$(this).before("<input type='text'>");
	});
	$('.other_pic span').click(function(){
		$(this).before("<input type='file' name='file' class='pic_file' value='浏览'/>");
	});

})
function Preview(imgSrc){
	alert(imgSrc);
	$('.pic_div img').attr('src',imgSrc);
};
