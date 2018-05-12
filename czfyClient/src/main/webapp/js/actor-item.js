
function actorItem (bata,URL){
    console.log(bata);
    $.ajax({
        //获取艺人 某一类别列表
        url:URL,
        type: 'POST',
        data:JSON.stringify(bata),
        contentType:"application/json; charset=utf-8",
        success : function(data){
           // paging(6);
            console.log(data);
             var html = '';
             if(data.status == 1){
                 if(data.actorId != ''){
                     $.each(data.actors, function(index,val) {
                         html += '<div class="three columns webdesing ecommerce">'+	'<div class="portfolio-image">'+
                             '<a title="portfolio link"><img src="'+val.photo+'" class="portfolio-img" alt="portfolio picture" /></a>'+
                             '<a class="portfolio-title" title="portfolio details"></a>'+
                             '<a  class="portfolio-subtitle" title="portfolio details">'+val.actorIntroducation+'</a>'+
                             '<a href="actor_mess.html?id='+val .actorId+'" class="portfolio-link" title="portfolio details"></a>'+
                             '<div class="background-color portfolio-bg">'+val.actorName+'</div>'+
                             '</div>'+
                             '<a title="portfolio link"  class="portfolio-outer-title"></a>'+
                             '<div class="portfolio-outer-subtitle color">'+val.actorIntroducation+'</div>'+
                             '<div class="text margin-tb-10 portfolio-outer-subtitle"></div>'+
                             '</div>';
                     });
                 }else{
                     html = '<div>暂无数据</div>';
                 }
             }
            $('#action-mess').empty();
            $('#action-mess').append(html);
        }
    });
}

