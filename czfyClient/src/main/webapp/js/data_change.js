$(function(){
	$(document).on('click','.dropdown-menu li a',function(){
		$(this).parents('.center').find('.pic_box').html('');
		$.each($('tr'),function(){
			$(this).find('td').first().css('line-height','10px');
		});
		$(this).parents('.dropdown').find('.project_title').html($(this).html());
		$(this).parents('.center').css('margin-bottom','0px');
		if('封面图片' == $(this).html() || '其他图片' == $(this).html()){
			$(this).parents('.center').find('.accept_content').attr('contentEditable','false');
			for(var i=0;i<3;i++){
				$('.pic_box').append("<div>"
                                       +"<div class='pic_div'>"
                                            +"<img src=''>"
                                        +"</div>"
                                        +"<input type='file' class='pic_box_file' onchange='Preview(this.value)'>"
                                        +"<input type='button' name='btn' class='pic_box_button' value='淇敼'/>" 
                                    +"</div>");
			}
		}else if('头像' == $(this).html() || '单位图片'== $(this).html()){
			$(this).parents('.center').find('.accept_content').attr('contentEditable','false');
			$('.pic_box').append("<div>"
                                       +"<div class='pic_div'>"
                                            +"<img src=''>"
                                        +"</div>"
                                        +"<input type='file' class='pic_box_file' onchange='Preview(this.value)'>"
                                        +"<input type='button' name='btn' class='pic_box_button' value='淇敼'/>" 
                                    +"</div>");
		}else if('所获成就' == $(this).html()){
			$(this).parents('.center').find('.accept_content').attr('contentEditable','false');
			$('.pic_box').append("<input type='text' class='text'>"
                                    +"<input type='text' class='text'>"
                                    +"<input type='text' class='text'>");
		}else{
			$(this).parents('.center').find('.accept_content').attr('contentEditable','true');
		}
		$(this).parents('.center').find('.accept_title').html($(this).html());
		$(this).parents('.center').find('.table').show();
		$.each($('tr'),function(){
			$(this).find('td').first().css('line-height',$(this).height()+'px');
		});
		$(this).parents('.center').find('.btn_change').show();
		$('.leftSide').css('height',$('.footer').get(0).offsetTop+70+'px');
	});
	$('.btn-confirm').click(function(){
		if($(this).parent().find('input').val() == ''){
			alert('查找内容不得为空');
		}else{
			$(this).next().show();
		}
	});
	$(document).on('click','.btn_change',function(){
		$(this).hide();
		$(this).prev('table').hide();
		$(this).parent().find('.dropdown').hide();
		$(this).parent().find('input').val('');
		$(this).parent().find('.project_title').html('选择详细的条目');
		$(this).parents('.center').css('margin-bottom','100px');
		$(this).parent().find('.pic_box').html('');
		$('.leftSide').css('height',$('.footer').get(0).offsetTop+70+'px');
	});


});