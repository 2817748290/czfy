var URL = {
		addVideo:"admin/addvideo",
		videoList:"admin/videolist",
		queryVideo:"admin/queryvideo",
		videoInfo:"admin/videoinfo",
		updateVideo:"admin/updatevideo",
		videoUpload:"admin/videoupload",
		delVideo:"admin/delvideo",
};

var videos = null;
var video = null;
var query = {
	videoName:'',
}
var $step;
var stepIndex;
$(window).ready(function(){
	init();
    $step = $("#myModal #step");
    initStep($step, ["视频信息", "视频封面", "视频文件", "上传成功"]);
	$('.table').css('margin-top','20px');
	$('#videoIntroducation').css('resize','none');

    $("#progressBtn").on("click", function(e) {
        stepIndex = $step.getIndex();
        var method = $("#myModal").attr("method");
        //上传资料
        switch (stepIndex) {
            case 0:
                if($("#video-form").validateForm()){
                    var data = {
                        videoName:$("#videoName").val(),
                        videoIntroducation:$("#videoIntroducation").val(),
                        nickname:$("#nickname").val(),
                        isTop:$("#isTop").is(':checked')
                    };
                    if(method=="add"){
                        $.postJSON(data,URL.addVideo,function(data){
                            if(data.status==1){
                                id = data.videoId;
                                $('.second-item #ssi-upload1').attr('data-id',id);
                                $('.second-item #ssi-upload').attr('data-id',id);
                                $('.first-item').hide();
                                $('.second-item').eq(1).show();
                                $('.second-item').eq(1).find('label').text('上传视频封面');
                                $step.nextStep();
                                init();
                                $("#ssi-previewBox table").hide();
                                var id = $('.second-item #ssi-upload').attr('data-id');
                                console.log(id);
                                initFileUploader($('#ssi-upload'),'/admin/videoupload/'+id
                                    ,6,1,['jpg','png']);
                                initFileUploader($('#ssi-upload1'),'/admin/videoupload/'+id
                                    ,6,1,['avi','mp4']);
                            }
                        });
                    }else if(method=="modify"){
                        data['videoId'] = video.videoId;
                        $.postJSON(data,URL.updateVideo,function(data){
                            if(data.status==1){
                                $('.first-item').hide();
                                $('.second-item').show();
                                $step.nextStep();
                                init();
                                initFileUploader($('#ssi-upload'),'/admin/videoupload/'+video.videoId
                                    ,6,1,['jpg','png']);
                                initFileUploader($('#ssi-upload1'),'/admin/videoupload/'+video.videoId
                                    ,6,1,['avi','mp4']);
                            }
                        })
                    }
                }
                break;
            case 1:
                if(method=="add") {
                    $('.third-item').eq(0).hide();
                    var flag = $('.second-item .ssi-uploadBtn').hasClass('ssi-hidden')
                        && $('.second-item .ssi-previewBox').find('table').length!=0;
                    if(flag){
                        $('.second-item').hide();
                        $('.third-item').eq(1).show();
                        $('.third-item').eq(1).find('label').text('上传视频文件');
                        $step.nextStep();
                    }else{
                        alert("请先上传图片");
                    }
                }else if(method=="modify"){
                    $('.second-item').hide();
                    $('.third-item').show();
                    $('.third-item').eq(1).find('label').text('上传视频文件');
                    $step.nextStep();
                }
                break;
            case 2:
                if(method=="add"){
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
    //增加艺人按钮
    $('#addBtn').click(function(){
        $('.ssi-clearBtn').click();
        initStep($step,["视频信息", "视频封面","视频文件","上传成功"]);
        $('.third-item').eq(0).hide();
        $('.second-item').eq(0).hide();
        $('.second-item').eq(1).find('label').text('添加视频封面');
        $('.third-item').eq(1).find('label').text('添加视频文件');
        $('.forth-item h1').text('添加成功');
        $('#deleteBtn').hide();
        $('#progressBtn').show();
        $('.first-item').show();
        $('.first-item:last').nextAll().hide();
        $('checkbox').prop('checked','false');
        $('textarea').val('');
        $("input").val("");
        $('#myModal').modal({keyboard: false});
        $("#myModal").attr("method","add");
        $("#closeBtn").click(function () {
            stepIndex = $step.getIndex();
            if(stepIndex == 3) {
                $('.first-item').show();
                $('.second-item').hide();
                $('.forth-item').hide();
                $('#resetBtn').show();
                $('#progressBtn').show();
                $step.toStep(0);
            }
        });
        $('#resetBtn').on("click",function(){
            $("#videoName").val("");
            $("#videoIntroducation").val("");
            $('checkbox').attr('checked','false');
        })
    });
    $.postJSON(null,URL.videoList,function(data){
        table_init(data);
    })
});


function init(){
    $.postJSON(null,URL.videoList,function(data){
        table_init(data);
    });
	$("#searchBtn").click(function(){
		query['videoName'] = $("#searchContent").val();
		var data ={
				videoName:query.videoName,
		};
        $.postJSON(data,URL.queryVideo,function(data){
            table_init(data)
        })
	})
}

function table_init(data){
	videos = data.videoList;
	var table = $(".table");
	table.empty("");
	$.each(videos, function(i,value) {
		if(i==0){
			table.append('<tr>');
		}
		if(i%2==0 && i!=0){
			table.append('</tr>');
			table.append('<tr>');
		}
		table.find('tr:last').append('<td id='+ value.videoId +'>'+ value.videoName +'</td>');
	});
	
	//每个视频
	$('.table').find('td').click(function(){
        $('.ssi-clearBtn').click();
        initStep($step, ["视频信息", "视频封面", "视频文件", "上传成功"]);
        $('.third-item').eq(0).show();
        $('.second-item').eq(0).show();
        $('.second-item').eq(1).find('label').text('修改视频封面');
        $('.third-item').eq(1).find('label').text('修改视频文件');
        $('.forth-item').find('h1').text('修改成功');
        $('#deleteBtn').show();
        $('#progressBtn').show();
        $('.first-item').show();
        $('.first-item:last').nextAll().hide();
        var data = {
            videoId:$(this).attr('id'),
        };
        $.postJSON(data,URL.videoInfo,function(data){
            var $videoImgBox = $('.curImgs-wrapper.videoImg');
            var $videoFileBox = $('.curImgs-wrapper.videoFile');
            $videoImgBox.find('ul').empty();
            $videoFileBox.find('ul').empty();
            var basePath = '/upload/picture/';

            if(data.video.videoCoverUrl){
                var videoImg = data.video.videoCoverUrl;
                console.log(data);
                //视频封面
                var obj1 = $('<li><img src='+basePath+videoImg+' alt=""/></li>');
                $videoImgBox.find('ul').append(obj1);
            }else {
                $videoImgBox.find('ul').append('暂无封面');
            }
            //视频文件
            if(data.video.videoPath){
                var videoFile = data.video.videoPath;
                console.log(videoFile);
                var obj2 = $('<li>' +
                    '<img src="/imgs/icon-file.png" alt=""/>' +
                    '<span class="text">'+videoFile+'</span>' +
                    '</li>');
                $videoFileBox.find('ul').append(obj2);
            }else{
                $videoFileBox.find('ul').append('暂无视频');
            }

        });
		$("#isTop").removeAttr("checked");
		$('textarea').val('');
		$("input").val("");
		$("#myModal").attr("method","modify");
        $('#myModal').modal({backdrop: 'static'});
		$.postJSON(data,URL.videoInfo,function(data){
			video = data.video;
			$("#videoName").val(video.videoName);
			$("#videoIntroducation").val(video.videoIntroducation);
			$("#nickname").val(video.authorNickname);
			$('#isTop').prop('checked',video.isTop==1);
		});
		$("#deleteBtn").on('click',function(){
			if(confirm("确认删除该视频么？")){
				var data = {
						videoId:video.videoId,
				};
				$.postJSON(data,URL.delVideo,function(data){
					if(data.status==1){
						init();
					}
				});
				$('#myModal').modal('hide');
			}
		})
		
		/*$("#submitBtn").on("click",function(e){
			if($("#video-form").validateForm()){
				var data = {
					videoName:$("#videoName").val(),
					videoIntroducation:$("#videoIntroducation").val(),
					isTop:$("#isTop").is(':checked'),
				}
				if($("#myModal").attr("method")=="add"){
					$.postJSON(data,URL.addVideo,function(data){
						if(data.status==1){
							$("#myModal").modal('hide')
							init()
						}
					});
				}else if($("#myModal").attr("method")=="modify"){
					data['videoId'] = video.videoId;
					//console.log(data);
					$.postJSON(data,URL.updateVideo,function(data){
						if(data.status==1){
							$("#myModal").modal('hide')
							init()
						}
					})
				}
			}
		})*/
	});
}

