/**
 * 分页代码
 * @param pages
 */
function paging(pages){
    var html = '<div class="paging">' +
        '<ul class="pagination">';
    console.log("paging :"+$('.pagination li:eq(0) a').val());
    if(pages > 1){
        html += '<li><a href="javascript:void(0)">&laquo;</a></li>';
        for(var i=0 ;i<pages;i++){
            html +='<li><a href="javascript:void(0)">'+(i+1)+'</a></li>';
        }
    html +='<li><a href="javascript:void(0)">&raquo;</a></li>';
    }else{
        html +='<li><a href="javascript:void(0)">仅一页</a></li>';
    }
    html +='</ul><br>\n' +
    '</div>';
    $('.paging_parents').empty();
    $('.paging_parents').append(html);
}

/**
 * 页码效果
 * @param index
 * @returns {*|jQuery}
 */
function page_item(index){
    var page=$("li[title^='focus']").index();
    $(".paging_parents li:eq("+page+")").attr("title","");
    console.log("kshafasdfawer");
    console.log(page);

        if(index=="»"){
            console.log(">>");
            page++;
            console.log(page);
            if(page > ($(".paging_parents li").length-2)){
                window.alert("到尾了，亲");
                page--;
            }
        }
        else if(index=="«"){
            console.log("<<");
            page--;
            if(page<1){
                window.alert("到尾了，亲");
                page++;
            }
        }
        else{
            page=index;
        }
        //换颜色
        for(var n=1;n<$(".paging_parents li").length-1;n++){
            if(n==page) {//$(".paging_parents li:eq("+n+") a").css("color","red");
                $(".paging_parents li:eq("+n+") a").css("background-color","#eee");
            }
            else  {//$(".pagination li:eq("+n+") a").css("color","#111");
                $(".paging_parents li:eq("+n+") a").css("background-color","#fff");
            }

        }
    $(".paging_parents li:eq("+page+")").attr("title","focus");
        console.log(page);
        return page;
}
    /**
     * 返回总页数
     * @param data
     * @param url
     * @returns {*}
     * @constructor
     */
function page_sum(data , url){
        console.log("page_sum");
        var page=0;
        $.ajax({
            url:url,
            data:JSON.stringify(data),
            type: 'POST',
            contentType:"application/json; charset=utf-8",
            async:false,//异步改同步否则不能传值
            success :function a(data){
                console.log("data.sumPage:"+data.sumPage);
                page=data.sumPage;
            }

        });
        console.log(page);
        return page;
}


