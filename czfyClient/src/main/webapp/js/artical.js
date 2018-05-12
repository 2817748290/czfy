$(function(){
	$('.leftSide').css('height',$('.footer').get(0).offsetTop+70+'px');
	$(window).resize(function(){
		$('.leftSide').css('height',$('.footer').get(0).offsetTop+70+'px');
	});
    //var BASE_URL = "http://139.199.61.16/czfyClient";
    //var BASE_URL ="http://localhost:8080";
    $.ajax({
        url: BASE_URL + "/client/getliterature",
        type: 'POST',
        success : function(data){
            if(data.status == 1){
                var html = '',li = '';

                $.each(data.literatures,function(index,val){
                	//console.log(data);
                    html = '';
                    li = '';
                    html += '<div class="row actor">【 提供人：<span>'+val[0].contributorsName+'</span> 】</div>';
                    $.each(val,function(index2,val2){
                            li += '<li>'+
                                        '<i><img src="images/artical/pdf.png"></i>'+
                                        '<a href="'+val2.literatureUrl+'" target="_blank">'+val2.literatureName+'</a>'+
                                    '</li>';
                    });
                    if(index%2 == 0){
                        html += '<div class="row center">'+
                                '<div class="col-lg-3 col-md-3 col-sm-3 col-xs-3 read_pic">'+
                                    '<img src="'+val[0].photo+'" alt="">'+
                                '</div>'+
                                '<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6 artical_list">'+
                                    '<ul>'+li+'</ul>'+
                                '</div>'+
                                '<div class="col-lg-3 col-md-3 col-sm-3 col-xs-3"></div>'+
                            '</div>';
                    }else{
                        html += '<div class="row center">'+
                                '<div class="col-lg-3 col-md-3 col-sm-3 col-xs-3">'+
                                '</div>'+
                                '<div class="col-lg-6 col-md-6 col-sm-6 col-xs-6 artical_list">'+
                                    '<ul>'+li+'</ul>'+
                                '</div>'+
                                '<div class="col-lg-3 col-md-3 col-sm-3 col-xs-3 read_pic">'+
                                    '<img src="'+val[0].photo+'" alt="">'+
                                '</div>'+
                            '</div>';
                    }
                    $('.footer').before(html);
                });

            }
        }
    });
    
})