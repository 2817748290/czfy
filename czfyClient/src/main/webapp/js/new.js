$(function(){
    $('.leftSide').css('height',$('.footer').get(0).offsetTop+70+'px');

    $(document).on('click','.content_imgs_list > div',function(){
		$('#show,#next,#prev').css('margin-top',document.body.scrollTop+'px');
	});
    $(window).resize(function(){
        $('.leftSide').css('height',$('.footer').get(0).offsetTop+70+'px');
        $('#pan').css('height',$('.footer').get(0).offsetTop+70+'px');
    });
    $(document).on('mouseover','.content > div',function(){
        $(this).find('.num').stop().fadeIn();
    });
    $(document).on('mouseout','.content > div',function(){
        $(this).find('.num').stop().fadeOut();
    });
    //
    //var BASE_URL = "http://139.199.61.16/czfyClient";
    // var BASE_URL ="http://localhost:8080";
    $.ajax({
        url: BASE_URL + "/client/getnewslist",
        type: 'POST',
        success : function(data){
            if(data.status == 1){
                var html = '',li ='';
                $.each(data.newsList,function(index,val){
                    console.log(val);
                    html = '<div class="col-lg-3 col-md-3 col-sm-3 col-xs-3" data-num="0" >'+
                                '<div class="thumbnail" value="'+val.newsId+'">'+
                                  '<img src="'+val.cover+'" style="height:180px;">'+
                                  '<div class="caption">'+
                                      '<p>'+val.newsTitle+'</p>'+
                                  '</div>'+
                                  '<div class="num"><span>'+val.imageNumber+'</span>张</div>'+
                                '</div>'+
                            '</div>';
                    switch (val.newsType){
                       case "民族艺术" : $('.content_imgs_list').eq(0).append(html);break;
                       case "个人荣誉" : $('.content_imgs_list').eq(1).append(html);break;
                       case "学校荣誉" : $('.content_imgs_list').eq(2).append(html);break;
                    }
                });
                
            }
        }
    });
     
    //
    $(document).on('click',".content_imgs_list > div",function(){
        $('#pan').css('height',$('.footer').get(0).offsetTop+$('.footer').get(0).scrollTop+70+'px');
    	$('.showImage').remove();
    	$('#show').append("<div class='showImage'></div>");
    	var Length = parseInt($(this).find('span').html())+1;
        var newid = $(this).find('.thumbnail').attr('value');
        var url="";
        //var BASE_URL = "http://139.199.61.16/czfyClient";
        var BASE_URL ="http://localhost:8080";
        var data = {
            "newsId": newid
        }

        $.ajax({
            url: BASE_URL + "/client/getnewsbyid",
            type: 'POST',
            data: JSON.stringify(data),
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                console.log(data);
                if (data.status == 1) {
                    $.each(data.news,function(index,val){
                        $('.showImage').append("<span><img src='"+val.imageUrl+"'></span>");
                    });
                }
            }
        });

        $("#show").fadeIn();
        $("#pan").fadeIn();
        $("#next").fadeIn();
        $("#prev").fadeIn();
    });
     $(document).on('click','.type span',function(){   
         $("#show").fadeIn();
         $("#pan").fadeIn();
         $("#next").fadeIn();
         $("#prev").fadeIn();
     });
     $(document).on('click',"#pan",function(){ 
       $("#show").fadeOut();
       $("#pan").fadeOut();
       $("#next").fadeOut();
       $("#prev").fadeOut();
    });

     $(document).on('click',"#next",function(){ 
       $(".showImage span:last").animate({"left":"670px"},500,
        function(){
            $(this).animate({"left":"0px"},500);
            $(".showImage").prepend($(this));
        });
        $("#show").animate({"left":"30%"},500,
            function(){
            $(this).animate({"left":"50%"},500);    
        });
    });

    $(document).on('click',"#prev",function(){  
        $(".showImage span:first").animate({"left":"-670px"},500, function(){
            $(this).animate({"left":"0px"},500);
            $(".showImage").append($(this));
        });
        $("#show").animate({"left":"70%"},500,function(){
            $(this).animate({"left":"50%"},500);    
        });
    });
})