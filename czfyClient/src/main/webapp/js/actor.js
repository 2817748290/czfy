$(function() {
    var SORT = 0;//（分类1——7）
    var CLASSES = 0;//（级别1——3）
    //var BASE_URL = "http://localhost:8080";
    var URL = BASE_URL + "/client/queryactor";
    var url_page=BASE_URL+"/client/getActorSumPage";

    /**
     * 初始化生成actor列表
     */
    console.log("url:" + URL);
    var data = {
        "classificationId": SORT,
        "actorLevel": CLASSES,
        "page": 0
    }
    console.log(data);
    actorItem(data, URL);//获取actorlist
    paging(page_sum(data,url_page));
    pageActor(data, URL);


    $('#actor_sort li').click(function () {
        var i = $(this).index();
        console.log("点击获取的i=" + i);
        //字体颜色变化，以确定选择的哪一个。
        SORT = i;
        console.log("SORT选择中（传入数据值）：SORT:" + SORT + "    CLASSES:" + CLASSES);
        for (var n = 0; n < $("#actor_sort li").length; n++) {
            if (n == SORT) {
                $("#actor_sort li:eq(" + n + ") a").css("color", "red");
                $("#actor_sort li:eq(" + n + ") a").css("font-size", "16px");
            }
            else {
                $("#actor_sort li:eq(" + n + ") a").css("color", "#111");
                $("#actor_sort li:eq(" + n + ") a").css("font-size", "12px");
            }
        }
        var data = {
            "classificationId": SORT,
            "actorLevel": CLASSES,
            "page": 0
        }
        console.log(data);
        actorItem(data, URL);
        paging(page_sum(data,url_page));
        pageActor(data, URL);
    });

        $('#actor_classes li').click(function () {
            var i = $(this).index();
            console.log("i=" + i);

            //字体颜色变化，以确定选择的哪一个。
            CLASSES = i;
            console.log("CLASSES中：SORT:" + SORT + "    CLASSES:" + CLASSES);
            for (var n = 0; n < $("#actor_sort li").length; n++) {
                if (n == CLASSES) {
                    $("#actor_classes li:eq(" + n + ") a").css("color", "red");
                    $("#actor_classes li:eq(" + n + ") a").css("font-size", "16px");
                }
                else {
                    $("#actor_classes li:eq(" + n + ") a").css("color", "#111");
                    $("#actor_classes li:eq(" + n + ") a").css("font-size", "12px");
                }
            }
            var data = {
                "classificationId": SORT,
                "actorLevel": CLASSES,
                "page": 0
            }
            console.log(data);
            actorItem(data, URL);
            paging(page_sum(data,url_page));
            pageActor(data, URL);

        });

        $('.portfolio-image a').click(function () {
            var pic_address = $(this).parent().find('.portfolio-img').attr('src');
            var pic_name = $(this).parent().find('.portfolio-title').html();
            localStorage.setItem('pic_address', pic_address);
            localStorage.setItem('pic_name', pic_name);
        });

        $(document).on('click', '.portfolio-outer-title', function () {
            var pic_address = $(this).prev().find('.portfolio-img').attr('src');
            var pic_name = $(this).prev().find('.portfolio-title').html();
            localStorage.setItem('pic_address', pic_address);
            localStorage.setItem('pic_name', pic_name);
        });

        $(document).on('click', '.ecommerce a', function () {
            localStorage.setItem('page', 'actor_mess.html');
        });

    function pageActor(bata,URL){
       // var page=1;//初始化page ，半个全局变量
        //默认第一个为被点击就效果
        $(".paging_parents li:eq(1)").attr("title","focus");
        $(".paging_parents li:eq(1) a").css("background-color", "#eee");
        $('.paging_parents li a').click(function(){

            var index = $(this).html();
            bata.page=(page_item(index)-1)*4;
            console.log("pageActer:");
            console.log(bata);
            //调用生成
            actorItem(bata,URL);
        });
    }
});

