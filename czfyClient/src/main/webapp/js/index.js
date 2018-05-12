$(function(){
	$(document).load(function(){

		$('.leftSide').css('height',$('.footer').get(0).offsetTop+70+'px');
	});

    // var BASE_URL ="http://localhost:8083";

    $.ajax({
        url: BASE_URL + "/client/getindexinfo",
        type: 'POST',
        success : function(data){
            if(data.status == 1){
                var html = '';
                $.each(data.info.actors,function(index,val){
                    html += '<div class="col-lg-3 col-md-3 col-sm-3 col-xs-3">'+
                               ' <div class="actor_avatar">'+
                                   ' <a href="actor_mess.html?id='+val.actorId+'"><img src="'+val.photo+'"></a>'+
                                '</div>'+
                                '<p>'+val.actorName+'</p>'+
                            '</div>';
                });
                $('.actor_list').append(html);

                html = '';
                $.each(data.info.projects,function(index,val){
                    html += '<div class="col-lg-3 col-md-3 col-sm-3 col-xs-3">'+
                               ' <div class="project_avatar">'+
                                '<a href="project_mess.html?aid='+val.projectId+'" >'+
                                   ' <img src="'+val.projectCoverUrl+'"></a>'+
                                '</div>'+
                               ' <p>'+val.projectName+'</p>'+
                            '</div>';
                });
                $('.project_list').append(html);

                html = '';
                $.each(data.info.literatures,function(index,val){
                    html += '<li>'+
                                '<i><img src="images/artical/pdf.png"></i>'+
                                '<a href="'+val.literatureUrl +'" target="_blank">'+val.literatureName +'</a>'+
                            '</li>';
                });
                $('.topic_list ul').append(html);

                html = '';
                $.each(data.info.news,function(index,val){
                    console.log(val);
                    html += '<div class="col-lg-3 col-md-3 col-sm-3 col-xs-3">'+
                                '<div class="thumbnail" value="'+val.newsId+'">'+
                                  '<img src="'+val.newsImage+'">'+
                                  '<div class="caption">'+
                                      '<p>'+val.newsTitle+'</p>'+
                                  '</div>'+
                                  '<div class="num"><span>'+val.imageNumber+'</span>张</div>'+
                               ' </div>'+
                            '</div>';
                });
                $('.new_list').append(html);

                html = '';
                $.each(data.info.videos,function(index,val){
                    html += '<div class="col-lg-3 col-md-3 col-sm-3 col-xs-3" data-address="video/date.mp4" data-avatar="images/0.jpg" data-time="2016-08-06 00:00" data-id="'+val.videoId+'">'+
                                '<div class="movie_avatar">'+
                                    '<img src="'+val.videoCoverUrl+'">'+
                               ' </div>'+
                                '<div class="cover">'+
                                    '<img src="images/cover.png" alt="">'+
                                '</div>'+
                                '<p data-title="'+val.videoName+'">&nbsp;'+val.videoName+'</p>'+
                            '</div>';
                });
                $('.movie_list').append(html);
            }
        }
    });



	$(document).on('click','.new_list > div',function(){
		$('#show,#next,#prev').css('margin-top',document.body.scrollTop+'px');
	});
    $(document).on('click',".new_list > div",function(){
        $('#pan').css('height',$('.footer').get(0).offsetTop+$('.footer').get(0).scrollTop+70+'px');
        $('.showImage').remove();
        $('#show').append("<div class='showImage'></div>");
        var Length = parseInt($(this).find('span').html())+1;
        var newid = $(this).find('.thumbnail').attr('value');
        var url="";
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


    $(window).resize(function(){
        $('.leftSide').css('height',$('.footer').get(0).offsetTop+70+'px');
        $('#pan').css('height',$('.footer').get(0).offsetTop+70+'px');
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
    $(document).on('mouseover','.content > div',function(){
        $(this).find('.num').stop().fadeIn();
    });
    $(document).on('mouseout','.content > div',function(){
        $(this).find('.num').stop().fadeOut();
    });
    $('#myCarousel').carousel({
		interval : 2000,
		pause : 'hover',
		wrap : true
	});

});