	//var BASE_URL = "http://139.199.61.16/czfyClient";

   // var BASE_URL ="http://localhost:8080";
    var PAGE_SUM_URL=BASE_URL+"/client/getVideoSumPage";
    // var data={"videoName":"",
    //             "page":0
    // };

    // /**
    //  * 初始化:加载视频列表
    //  */
    // video_item(data,BASE_URL);
    // paging(page_sum(data,PAGE_SUM_URL));
    // pagevideo(data,BASE_URL);

$(function(){

        var data={"videoName":"",
            "page":0
        };

        /**
         * 初始化:加载视频列表
         */
        video_item(data,BASE_URL);
        paging(page_sum(data,PAGE_SUM_URL));
        pagevideo(data,BASE_URL);
    /**
     * 点击搜索按钮
     */
    $('.search-button').click(function(){
        var videoname = $('.searchname-input').val();
        data={"videoName":videoname,
            "page":0 }
        console.log("searchname-input:"+videoname);
        video_item(data,BASE_URL);
        paging(page_sum(data,PAGE_SUM_URL));
        pagevideo(data,BASE_URL);
    });

    /**
     * 点击页码刷新
     * @param bata
     * @param URL
     */
    function pagevideo(bata,URL){
        //默认第一个为被点击就效果
        $(".paging_parents li:eq(1)").attr("title","focus");
        $(".paging_parents li:eq(1) a").css("background-color", "#eee");
        $('.paging_parents li a').click(function(){

            var index = $(this).html();
            bata.page=(page_item(index)-1)*4;
            console.log("pageActer:");
            console.log(bata);
            //调用生成
            video_item(bata,URL);
        });
    }

	$('.leftSide').css('height',$('.footer').get(0).offsetTop+70+'px');

    $(window).resize(function(){
        $('.leftSide').css('height',$('.footer').get(0).offsetTop+70+'px');
    });
	
    $(document).on('mouseover','.movie',function(){
        $(this).find('p').css({
            'color':'blue',
            'height':'40px'
        });
        $(this).find('.time_length').stop().animate({'right':'0'},300);
        $(this).find('.text_btn').stop().animate({'top':'210px'},300);
    });

    $(document).on('mouseout','.movie',function(){
        $(this).find('p').css({
            'color':'black',
            'height':'20px'
        });
        $(this).find('.time_length').stop().animate({'right':'-30px'},300);
        $(this).find('.text_btn').stop().animate({'top':'175px'},300);
    });

    $(document).on('click','.movie',function(){
        window.open('movie_play.html?id='+$(this).data('id'),'_self');
    });
});


/**
 * 加载视频列表
 * @param BASE_URL
 * @param data
 */
function video_item(data,BASE_URL){
    console.log("video_item");
    $.ajax({
        url: BASE_URL+"/client/getVideoByName",
        data:JSON.stringify(data),
        contentType:"application/json; charset=utf-8",
        type: 'POST',
        dataType :'json',
        success: function(data){
            console.log(data);
            //     data.forEach((ele , index) => {
            //
            // });
            if(data.status == 1){
                var html = '';
                $.each(data.videopage,function(index,val){
                    //console.log(index , val);
                    html += '<div class="box">'+

                        '<div class="movie" data-id="'+val.videoId+'" data-address="video/date.mp4" data-avatar="'+val.videoCoverUrl+'" data-time="2016-08-06 00:00">'+
                        '<img src="'+val.videoCoverUrl+'" alt="">'+
                        '<div class="time_length">3:06</div>'+
                        '<div class="text">'+
                        '<p data-title="'+val.videoName+'">'+val.videoName+'</p>'+
                        '</div>'+
                        '<div class="text_btn">'+
                        '<div class="playNum">'+
                        '<i></i>'+
                        '<span></span>'+
                        '</div>'+
                        '<div class="comment">'+
                        '<i></i>'+
                        '<span></span>'+
                        '</div>'+
                        '</div>'+
                        '</div>'+

                        '</div>';
                });
            }
           else{
                html = '获取视频列表失败，请刷新重试';
                }
            $('.movie_list').empty();
            $('.movie_list').append(html);
        }
    });
}