$(function(){
	$.each($('td').not('.error'),function(){
		$(this).css('line-height',$(this).height()+'px');
	});
	$.each($('.num'),function(){
		$(this).html($(this).parent().index()+1);
	});
	$(document).on('click','.delete',function(){
		$(this).parent().parent().remove();
		$.each($('.num'),function(){
			$(this).html($(this).parent().index()+1);
		});
		$('.leftSide').css('height',$('.footer').get(0).offsetTop+70+'px');
	});
	$('.user .table_search input').keydown(function(e){
		if($(this).val() == ''){
			return;
		}
		if(13 == e.keyCode){
			user_search();
		}
	});
	$('.mistake .table_search input').keydown(function(e){
		if($(this).val() == ''){
			return;
		}
		if(13 == e.keyCode){
			error_search();
		}
	});
	$('.table_search a').click(function(){
		window.location.reload();
	});
	

});
function user_search(){
	var result = 0;
	for(var i=0;i<$('tbody tr').length;i++){
		var sTab=$('.user tbody tr').eq(i).find('td').eq(1).html().toLowerCase();
        var sTxt=$('.table_search input').val().toLowerCase();
        var arr=sTxt.split(' ');
        $('.user tbody tr').eq(i).hide();
        for(var j=0;j<arr.length;j++)
        {
            if(sTab.search(arr[j])!=-1)
             {
               $('.user tbody tr').eq(i).show();
               result =1;
             }
        }
	}
	if(0 == result){
		$('tbody').append("<tr><td colspan='4'>未查到结果。。。</td></tr>");
	}
	$('.leftSide').css('height',$('.footer').get(0).offsetTop+70+'px');
}
function error_search(){
	var result = 0;
	for(var i=0;i<$('tbody tr').length;i++){
		var sTab=$('.mistake tbody tr').eq(i).find('td').eq(2).html().toLowerCase();
        var sTxt=$('.table_search input').val().toLowerCase();
        var arr=sTxt.split(' ');
        $('.mistake tbody tr').eq(i).hide();
        for(var j=0;j<arr.length;j++)
        {
            if(sTab.search(arr[j])!=-1)
             {
               $('.mistake tbody tr').eq(i).find('td').eq(2).html($('.mistake tbody tr').eq(i).find('td').eq(2).html().replace(arr[j],"<font color='red'>"+arr[j]+"</font>"));
               $('.mistake tbody tr').eq(i).show();
               result =1;
             }
        }
	}
	if(0 == result){
		$('tbody').append("<tr><td colspan='4'>未查到结果。。。</td></tr>");
	}
	$('.leftSide').css('height',$('.footer').get(0).offsetTop+70+'px');
}
