$(function(){
    var Index=0;
    var item_url="/client/pageProject";
    var PAGE_SUM_URL="/client/getProjectSumPage";
    var bata = {
        "projectName":$('#pro_name').val(),
        "classificationId" :-1,
        "levelCode":-1,
        "page":0
    };
    project_item(item_url,bata);
    paging(page_sum(bata,PAGE_SUM_URL));
    pageproject(bata,item_url);

    /**
     * 下拉选择框（种类）填充
     */
    $.ajax({
        url: BASE_URL + "/client/getclassification",
        type: 'POST',
        success : function(data){
            if(data.status == 1){
                 /*加入“全部选项”*/
                var html = '<li><a href="javascript:void(0)" value="0">全部</a></li>';
                $.each(data.classifications, function(index, val) {
                    html += '<li><a href="javascript:void(0)" value="'+val.classificationId+'">'+val.classificationName+'</a></li>';
                });
            }
            $('.dropdown-menu').eq(0).append(html);
        }
    });

    $('.search-btn').click(function(){
        var data = {
            "projectName":$('#pro_name').val(),
            "classificationId" :$('#type_name').val()==0 ? -1:$('#type_name').val(),
            "levelCode":Index== 0 ? -1:Index,
            "page":0
        };
        console.log("输入数据："+Index);
        console.log(data);

        project_item(item_url,data);
        paging(page_sum(data,PAGE_SUM_URL));
        pageproject(data,item_url);

        return false;
    });

    /**
     * 点击页码刷新
     * @param bata
     * @param URL
     */
    function pageproject(data,URL){
        //默认第一个为被点击就效果
        $(".paging_parents li:eq(1)").attr("title","focus");
        $(".paging_parents li:eq(1) a").css("background-color", "#eee");
        $('.paging_parents li a').click(function(){

            var index = $(this).html();
            data.page=(page_item(index)-1)*4;
            console.log("pageProject:");
            console.log(data);
            //调用生成
            project_item(URL,data);
        });
    }

	$('.header .title span').html(localStorage.getItem('item_name'));
	$('.list li').click(function(){
    	var item_name = $(this).html();

    	localStorage.setItem('item_name',item_name);
    	// window.open('project.html','_self');
    });
    $('.leftSide').css('height',$('.footer').get(0).offsetTop+70+'px');
    $(window).resize(function(){
        $('.leftSide').css('height',$('.footer').get(0).offsetTop+70+'px');
    });
    $(document).on('click','.portfolio-image a',function(){
    	var project_name = $(this).parent().find('.portfolio-title').html();
    	localStorage.setItem('project_name',project_name);
    });
    
    $(document).on('click','.dropdown-menu li a',function(){
        $(this).parents('.dropdown').find('.project_title').html($(this).html());
        $(this).parents('.dropdown').find('input').val($(this).attr('value'));
    });
    $(document).on('click','.dropdown-menu-two li',function(){
       Index= $(this).index();
    });
})

//获取projedt列表
function project_item(item_url,data){
    $.ajax({
        url:BASE_URL + item_url,
        type: 'POST',
        data:JSON.stringify(data),
        contentType:"application/json; charset=utf-8",
        success : function(data){
            console.log("data:");
            console.log(data);
            var html = '';
            if(data.status == 1){
                if(data.projects != ''){
                    $.each(data.projects, function(index, val) {
                        html += '<li>'+
                            '<i><img src="images/copy_rignt_24.png" alt=""></i>'+
                            '<a href="project_mess.html?aid='+val.projectId+'" >'+val.projectName+'</a>'+
                            '<span>'+val.publishTime+'</span>'+
                            '</li>';
                    });
                }else{
                    html = '<li>暂无数据</li>';
                }
            }
            $('.content_list').empty();
            $('.content_list').append(html);
        }
    });
}