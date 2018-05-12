var URL = {
	addContributors:'admin/addcontributors',
	contributorsList:'admin/contributorslist',
	queryContributorsName:'admin/querycontributorsname',
	literatureInfo:"admin/literatureinfo",
	updateContributors:"admin/updatecontributors",
	delContributors:'admin/delcontributors',
	literatureUpload:"admin/literatureupload",
	delLiterature:"admin/delliterature",
}

var literatures = null;
var literature = null;
var query = {
	contributorsName:"",
};
var $step;
var stepIndex;

$(window).ready(function(){
	
	init();
    $step = $("#myModal #step");
    initStep($step,["文献信息", "文献图片", "文献文档", "上传成功"])
    //进度条控制
    //literaturePhoto
    var id;
    $("#progressBtn").on("click", function(e) {
        var method = $("#myModal").attr("method");
        stepIndex = $step.getIndex();
        //上传资料
		switch (stepIndex){
			case 0:
                if($("#literature-form").validateForm()){
                    var data = {
                        contributorsName:$("#contributorsName").val(),
                        literatureName:$('#literatureName').val()
                    };
                    console.log(data);
                    console.log(method)
                    if(method=="add"){
                    	console.log(data);
                        $.postJSON(data,URL.addContributors,function(data){
                            if(data.status==1) {
                                id = data.literatureId;
                                $('.second-item #ssi-upload1').attr('data-id', id);
                                $('.second-item #ssi-upload').attr('data-id', id);
                                $('.first-item').hide();
                                $('.second-item').eq(1).show();
                                $step.nextStep();
                                init();
                                $("#ssi-previewBox table").hide();
                                var id = $('.second-item #ssi-upload').attr('data-id');
                                console.log(id);
                                initFileUploader($('#ssi-upload'),'/admin/literatureupload',
									6,1,['jpg', 'png'],{'literatureId':id});
                                initFileUploader($('#ssi-upload1'),'/admin/literatureupload',
                                    6,1,['pdf'],{'literatureId':id});
                            }
                        });
                    }else if(method=="modify"){
                        data['literatureId'] = literature.literatureId;
                        $.postJSON(data,URL.updateActor,function(data){
                            if(data.status==1){
                                $('.first-item').hide();
                                $('.second-item').show();
                                $step.nextStep();
                                init();
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
                        $step.nextStep();
                    }else{
                        alert("请先上传图片");
                    }
				}else if(method=="modify"){
                    $('.second-item').hide();
                    $('.third-item').eq(1).show();
                    $step.nextStep();
				}

				break;
			case 2:
                if($('.third-item .ssi-uploadBtn').hasClass('ssi-hidden')){
                    $('.third-item').hide();
                    $('.forth-item').show();
                    $step.nextStep();
                    $('#resetBtn').hide();
                    $('#progressBtn').hide();
                }else{
                    alert("请先上传文档");
                }
				break;
		}
    });
	$('.table').css('margin-top','20px');
	
	//文献上传按钮
	$('#addBtn').click(function(){
        $('.ssi-clearBtn').click();
        initStep($step,["文献信息", "文献图片", "文献文档", "上传成功"]);
        $('.third-item').eq(0).hide();
        $('.second-item').eq(0).hide();
        $('.second-item').eq(1).find('label').text('上传文献图片');
        $('.third-item').eq(1).find('label').text('上传文献文档');
        $('.forth-item h1').text('添加成功');
        $('#deleteBtn').hide();
        $('#progressBtn').show();
        $('#submitBtn').hide();
		$('.first-item').show();
		$('.first-item:last').nextAll().hide();
		// alert("请先添加信息后上传文献文件!!!")
		$('#literature-list').empty();
        $('#literatureName').parents('.first-item').show();
        console.log($('#literatureName').parents('.second-item'))
		$("input").val("");
		$("#myModal").attr("method","add")
        $('#myModal').modal({backdrop: 'static'});
        $('#resetBtn').on("click",function(){
			$("#contributorsName").val("")
		});

        /*var method = $("#myModal").attr("method");
		$("#submitBtn").on("click",function(e){
			if($("#literature-form").validateForm()){
				var data = {
					contributorsName:$("#contributorsName").val(),
					literatureName:$('#literatureName').val(),
				}
				if(method=="add"){
					$.postJSON(data,URL.addContributors,function(data){
							// alert("接下来请上传文献文件!");
							init()

					});
				}else if(method=="modify"){
					data['contributorsName'] = literatures.contributorsName;
					$.postJSON(data,URL.updateContributors,function(data){
						if(data.status==1){
							$("#myModal").modal('hide')
							init()
						}
					})
				}
			}
		})*/
	});
})

function init(){
	
	
	$.postJSON(null,URL.contributorsList,function(data){
		table_init(data);
	});
	
	$("#searchBtn").click(function(){
		query['contributorsName'] = $("#searchContent").val();
 		var data ={
 			contributorsName:query.contributorsName,
		};
		$.postJSON(data,URL.queryContributorsName,function(data){
			table_init(data)
		})
	})
}

function table_init(data){
	literatures = data.literatures;
	var table = $(".table");
	table.empty("");
	$.each(literatures, function(i,value) {
		if(i==0){
			table.append('<tr>');
		}
		if(i%5==0 && i!=0){
			table.append('</tr>');
			table.append('<tr>');
		}
		table.find('tr:last').append('<td>'+ value.contributorsName +'</td>');
	});
	
	//每个贡献者
	$('.table').find('td').click(function(){
        $('.ssi-clearBtn').click();
        initStep($step,["文献信息", "文献图片", "文献文档", "上传成功"]);
        $('.second-item').eq(1).find('label').text('修改文献图片');
        $('.third-item').eq(1).find('label').text('修改文献文档');
        $('.forth-item h1').text('添加成功');
        $('.first-item').show();
        $('.first-item:last').nextAll().hide();
        $('#deleteBtn').show();
        $('#submitBtn').show();
        $('#progressBtn').hide();
		$("input").val("");
		$("#myModal").attr("method","modify");
        $('#myModal').modal({backdrop: 'static'});
        $('#literatureName').parents('.first-item').hide();
        var data = {
			contributorsName:$(this).html(),
		};
		$.postJSON(data,URL.literatureInfo,function(data){
			literature = data.literature;
			if(data.status==1){
				//console.log(literature[0])
                // URL.actorPhotoUpload + '/' + actorId
			}
			$("#contributorsName").val(literature[0].contributorsName);
			//循环输出文献列表
			$('#literature-list').empty();
			$.each(literature, function(i,value) {
				if(value.literatureUrlIsNull==1){
					$('#literature-list').append('<li class="list-group-item"><span class="badge">未上传文件</span><span class="badge del" id='+ value.literatureId +'>删</span>' + value.literatureName + '</li>');
				}else{
					$('#literature-list').append('<li class="list-group-item"><span class="badge">已上传文件</span><span class="badge del" id='+ value.literatureId +'>删</span>' + value.literatureName + '</li>');
				}
			});
			$('.del').on("click",function(){
				if(confirm("确认删除该文献么？")){
					console.log($(this).attr('id'))
					var data = {
						literatureId:$(this).attr('id'),
					};
					$.postJSON(data,URL.delLiterature,function(data){
						console.log()
						if(data.status==1){
							alert("删除成功!")
							init();
						}
					})
				}
			})
		})


		
		$("#deleteBtn").on('click',function(){
			if(confirm("确认删除该文献贡献者么？")){
				var data = {
					contributorsName:literature[0].contributorsName,
				}
				$.postJSON(data,URL.delContributors,function(data){
					if(data.status==1){
						init();
					}
				})
				$('#myModal').modal('hide');
			}
		})


		$("#submitBtn").on("click",function(e){
            if($("#literature-form .first-item:first").validateForm()){
				var data = {
					contributorsNewName:$("#contributorsName").val(),
				}
                if($("#myModal").attr("method") =="add"){
					$.postJSON(data,URL.addContributors,function(data){
						if(data.status==1){
							$("#myModal").modal('hide')
							init();
						}
					});
				}else if($("#myModal").attr("method") =="modify"){
					data['contributorsOldName'] = literature[0].contributorsName;
                    $.postJSON(data,URL.updateContributors,function(data){
                        if(data.status==1){
							$("#myModal").modal('hide')
							init()
						}
					})
				}
			}
		})
	});
}
