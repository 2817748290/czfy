var URL = {
    addNews:'admin/addnews',
    newsList:'admin/newslist',
    queryNews:'admin/querynews',
    newsInfo:'admin/newsinfo',
    delNews:'admin/delnews',
    updateNews:'admin/updatenews',
    newsImageUpload:'admin/newsimageupload',
    getPassInfo:'admin/getPassInfo',
    isPass:'admin/isPass',
    getNewsById:'admin/getnewsbyid',
    uploadNewsCover:'admin/newsA/actorphotoupload',
    uploadNewsPhoto:'admin/newsA/actorworkupload',
    getNewsInfoById:'admin/getNewsInfoById'
};
var isPassInfo = null;
var newsList = null;
var news = null;
var query = {
	newsTitle:'',
	newsType:'',
	isPass:''
};
var $step;
var stepIndex;
$(window).ready(function(){
	
	init();
    $step = $("#myModal #step");
    initStep($step,["新闻信息", "新闻封面","新闻内容图片","上传成功"]);
    //进度条控制
    //newsPhoto
    var id;
    $("#progressBtn").on("click", function(e) {
        stepIndex = $step.getIndex();
        var method = $("#myModal").attr("method");
        //上传资料
        stepIndex = $step.getIndex();
        switch(stepIndex)
        {
            case 0:
                if($("#news-form").validateForm()){
                    var data = {
                        newsTitle: $("#newsTitle").val(),
                        newsType: $("#newsType").val(),
                        isTop: $("#isTop").is(':checked'),
                        isPass: $("#isPass").val()
                    };
                    if(method=="add"){
                        $.postJSON(data,URL.addNews,function(data){
                            if (data.status==1) {
                                $('.table').empty();
                                id = data.newsId;
                                $('.second-item #ssi-upload').attr('data-id', id);
                                $('.third-item #ssi-upload').attr('data-id', id);
                                $('.first-item').hide();
                                $('.second-item').eq(0).hide();
                                $('.second-item').eq(1).show();
                                $('.second-item').eq(1).find('label').text('上传新闻照片');
                                $('.ssi-clearBtn').click();
                                $step.nextStep();
                                init();
                                id = $('.second-item #ssi-upload').attr('data-id');
                                console.log(id);
                                initFileUploader($('#ssi-upload'),'/admin/newsA/actorphotoupload/'+id,
                                    6,1,['jpg','png']
                                );
                                initFileUploader($('#ssi-upload1'),'/admin/newsA/actorworkupload/'+id,
                                    6,6,['jpg','png']
                                );
                            }
                        });
                    }else if(method=="modify"){
                        data['newsId'] = news.newsId;
                        $.postJSON(data,URL.updateNews,function(data){
                            if(data.status==1){
                                $('.first-item').hide();
                                $('.second-item').show();
                                $step.nextStep();
                                $('.table').empty();
                                init();
                                console.log('newsId:'+news.newsId);
                                initFileUploader($('#ssi-upload'),'/admin/newsA/actorphotoupload/'+news.newsId,
                                    6,1,['jpg','png']
                                );
                                initFileUploader($('#ssi-upload1'),'/admin/newsA/actorworkupload/'+news.newsId,
                                    6,6,['jpg','png']
                                );
                            }
                        })
                    }
                }
                break;
            case 1:
                if(method=="add"){
                    var flag = $('.second-item .ssi-uploadBtn').hasClass('ssi-hidden')
                        && $('.second-item .ssi-previewBox').find('table').length!=0;
                    if(flag){
                        $('.second-item').hide();
                        $('.third-item').eq(1).show();
                        $('#resetBtn').hide();
                        $step.nextStep();
                    }else{
                        alert("请先上传图片");
                    }
                }else if(method=="modify"){
                    $('.second-item').hide();
                    $('.third-item').show();
                    $('#resetBtn').hide();
                    $step.nextStep();
                }
                break;
            case 2:
                if(method=="add") {
                    var flag = $('.third-item .ssi-uploadBtn').hasClass('ssi-hidden')
                        && $('.third-item .ssi-previewBox').find('table').length!=0;
                    if(flag){
                        $('.third-item').hide();
                        $('.forth-item').show();
                        $step.nextStep();
                        $('#resetBtn').hide();
                        $('#progressBtn').hide();
                    }else{
                        alert("请先上传图片");
                    }
                }else if(method=="modify"){
                    $('.third-item').hide();
                    $('.forth-item').show();
                    $step.nextStep();
                    $('#resetBtn').hide();
                    $('#progressBtn').hide();
                }
                break;
        }
    });

    $('.table').css('margin-top','20px');
	$('#videoIntroducation').css('resize','none');
	
	//增加新闻按钮
    $('#addBtn').click(function(){
        $('.ssi-clearBtn').click();
        initStep($step,["新闻信息", "新闻封面","新闻内容图片", "上传成功"]);
        $('.second-item').eq(0).hide();
        $('.second-item').eq(1).find('label').text('修改新闻封面');
        $('.third-item').eq(1).find('label').text('修改新闻内容图片');
        $('.forth-item h1').text('添加成功');
        $('#deleteBtn').hide();
        $('#progressBtn').show();
        $('.first-item').show();
        $('.first-item:last').nextAll().hide();
        $('checkbox').prop('checked','false');
        $('.wangEditor-txt').text('');
        $("input").val("");
        $("#myModal").attr("method","add");
        $('#myModal').modal({backdrop: 'static'});
        $('#resetBtn').on("click",function(){
            $("#newsName").val("")
            $('checkbox').attr('checked','false');
            editor.$txt.html('');
        });
        $("#closeBtn").click(function () {
            var index = $step.getIndex();
            if(index == 2) {
                $('.first-item').show();
                $('.second-item').hide();
                $('#resetBtn').show();
                $('#progressBtn').show();
                $step.toStep(0);
            }
        });
        /*$("#submitBtn").on("click",function(e){
            var formValid = $('#news-form').validateForm();
            var editorValid = validateEditor(editor);
            if(formValid&&editorValid){
                var data = {
                    newsName:$("#newsName").val(),
                    newsLevel:$("#newsLevel").val(),
                    newsClassification:$("#newsClassification").val(),
                    isTop:$("#isTop").is(':checked'),
                    newsContent:$('#content').val()
                }
                if($("#myModal").attr("method")=="add"){
                    $.postJSON(data,URL.publishProject,function(data){
                        alert(data.status)
                        if(data.status==1){
                            fileuploadinit(URL.updateProjectCover + "?newsId=" + data.newsId);
                            init()
                        }
                    });
                }
            }
        })*/
    });
});

function init(){

    $.postJSON(null,URL.getPassInfo,function(data){
        isPassInfo = data.passList;
        var isPassName = $("#isPass");
        var isPassSearch = $("#isPassS");
        isPassName.empty();
        isPassSearch.empty();
        option_add(isPassSearch,-1,"审核情况");
        $.each(isPassInfo,function(i,value){
            option_add(isPassName,value.isPass,value.pname);
        });
        $.each(isPassInfo,function(i,value){
            option_add(isPassSearch,value.isPass,value.pname);
        })
    });

	$.postJSON(null,URL.newsList,function(data){
		table_init(data);
	});
	
	$("#searchBtn").click(function(){
		$('.table').empty();
		query['newsTitle'] = $("#searchContent").val();
		query['newsType'] = $('#newsFilter').val();
        query['isPass'] = $('#isPassS').val();
 		var data ={
			newsTitle:query.newsTitle,
			newsType:query.newsType,
			isPass:query.isPass,
		};
		$.postJSON(data,URL.queryNews,function(data){
			table_init(data);
		})
	})
}
function table_init(data) {

    var table = '';
    newsList = data.newsList;
    if(newsList.length){
        table += '<tbody>';
        $.each(newsList, function (i, value) {
            if (i % 3 === 0) {
                table += '<tr>';
            }
            table += '<td id=' + value.newsId + '><a href="#">' + value.newsTitle + '</a><br>';
            $.each(isPassInfo, function (i1, value1) {
                var isPass = parseInt(value.isPass);
                var checked = '';
                if(isPass === value1.isPass){
                    checked = 'checked';
                }
                table += '<input type="radio" class="pass" name="' + value.newsId + '" id="'+value1.isPass+'" ' + checked + '>' +
                    '<label class="text-pass">' + isPassInfo[i1].pname + '</label>';
            });
            table += '</td>';
            if ((i + 1) % 3 === 0) {
                table += '</tr>';
            }
        });
        table += '</tbody>';
        $('.table').append(table);
        $('.table td input').click(function () {
            var id = $(this).parent().attr('id');
            var isPass = $(this).attr('id');
            var json = {
                newsId:id,
                isPass:isPass
            };
            $.postJSON(json,URL.isPass,function (data) {
                if(data.status===1){
                    alert("修改成功");
                }
            })
        })
    }
    //每个新闻
    $('.table').find('td a').click(function () {
        $('.ssi-clearBtn').click()
        $('.curImgs-wrapper.newsCoverImg').find('ul').empty();
        initStep($step,["新闻信息", "新闻封面", "新闻内容图片","修改成功"]);
        $('.second-item').eq(1).find('label').text('修改新闻封面');
        $('.third-item').eq(1).find('label').text('修改新闻内容图片');
        $('.forth-item').find('h1').text('修改成功');
        $('#deleteBtn').show();
        $('#progressBtn').show();
        $('.first-item').show();
        $('.first-item:last').nextAll().hide();
        $("input").val("");
        $("#isTop").removeAttr("checked");
        $("#myModal").attr("method", "modify");
        $('#myModal').modal({backdrop: 'static'});
        var data = {
            newsId: $(this).parent().attr('id')
        };
        $.postJSON(data,URL.getNewsInfoById,function(data){
            console.log(data);

            if(data.status==1){
                var $newsCoverBox = $('.curImgs-wrapper.newsCoverImg');
                var $newsContentBox = $('.curImgs-wrapper.newsContentImg');
                $newsCoverBox.find('ul').empty();
                $newsContentBox.find('ul').empty();

                var basePath = '/upload/picture/';

                if(data.newsInfo.news.photo){
                    var newsCoverImg = data.newsInfo.news.photo;
                    var basePath = '/upload/picture/';
                    var index = newsCoverImg.lastIndexOf("\/");
                    newsCoverImg  = newsCoverImg.substring(index + 1, newsCoverImg.length);
                    var obj = $('<li><img src='+basePath+newsCoverImg+' alt=""/></li>');
                    $newsCoverBox.find('ul').append(obj);
                }else{
                    $newsCoverBox.find('ul').append('暂无图片');
                }

                //新闻内容图片
                if(data.newsInfo.images){
                    var newsImgList = data.newsInfo.images;
                    for(var i=0;i<newsImgList.length;i++){
                        //去掉第一个重复数据
                        if(i!=0){
                            var imgPath = newsImgList[i].imageUrl;
                            console.log(imgPath)
                            var index = imgPath.lastIndexOf("\/");
                            imgPath  = imgPath.substring(index + 1, imgPath.length);
                            var imgLi = $('<li><img src='+basePath+imgPath+' alt=""/></li>');
                            $newsContentBox.find('ul').append(imgLi);
                        }
                    }
                }else{
                    $newsContentBox.find('ul').append('暂无图片');
                }
            }
        });
        $.postJSON(data, URL.newsInfo, function (data) {
            news = data.news;
            $("#newsTitle").val(news.newsTitle);
            select_show($('#newsType'), news.newsType);
            select_show($('#isPass'), news.isPass);
            $('#isTop').prop('checked', news.isTop == 1);
        });

        $("#deleteBtn").on('click', function () {
            if (confirm("确认删除该新闻么？")) {
                $('.table').empty();
                var data = {
                    newsId: news.newsId,
                };
                $.postJSON(data, URL.delNews, function (data) {
                    if (data.status == 1) {
                        init();
                    }
                });
                $('#myModal').modal('hide');
            }
        });

        /*$("#submitBtn").on("click", function (e) {
            if ($("#news-form").validateForm()) {
                $('.table').empty();
                var data = {
                    newsTitle: $("#newsTitle").val(),
                    newsType: $("#newsType").val(),
                    isTop: $("#isTop").is(':checked'),
                    isPass: $("#isPass").val(),
                };
                if ($("#myModal").attr("method") == "add") {
                    $.postJSON(data, URL.addNews, function (data) {
                        if (data.status == 1) {
                            $("#myModal").modal('hide');
                            init()
                        }
                    });
                } else if ($("#myModal").attr("method") == "modify") {
                    data['newsId'] = news.newsId;
                    $.postJSON(data, URL.updateNews, function (data) {
                        if (data.status == 1) {
                            $("#myModal").modal('hide');
                            init()
                        }
                    })
                }
            }
        })*/
    });
};

function select_show(select,value){
	for(var i=0;i<select.get(0).options.length;i++){
		if(select.get(0).options[i].value == value){
			select.get(0).options[i].selected = true;
			break;
		}
	}
}

//增加一个option
function option_add(select,value,text) {
    select.append("<option value='"+value+"'>"+text+"</option>");
}
