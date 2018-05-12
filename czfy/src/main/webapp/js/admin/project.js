var URL = {
uploadImage:'admin/uploadimage',
    publishProject:'admin/publishproject',
    getProjectLevel:'admin/getProjectLevel',
    getProjectType:'admin/getprojecttype',
    projectList:'admin/projectlist',
    queryProject:'admin/queryproject',
    projectInfo:'admin/projectinfo',
    updateProject:'admin/updateproject',
    delProject:'admin/delproject',
    updateProjectCover:'admin/updateprojectcover',
    getProjectById:'admin/getprojectbyid'
};

var levels = null;
var types = null;
var projects = null;
var project = null;
var query = {
	projectName:'',
	projectType:'',
	projectLevel:''
}

var editor = new wangEditor('content');
var editorObj = '';

//存储所有图片的url地址
var imgUrls = [];
var $step;
var stepIndex;
$(window).ready(function(){
	editorObj = initEditor(editor);
	init();
    //进度条控制
    //projectPhoto
    $step = $("#myModal #step");
    initStep($step,["项目信息", "项目封面", "上传成功"]);
    var id;
    $("#progressBtn").on("click", function(e) {
        var method = $("#myModal").attr("method");
        stepIndex = $step.getIndex();
        //上传资料
        switch (stepIndex){
            case 0:
                if($("#project-form").validateForm()){
                    var data = {
                        projectName:$("#projectName").val(),
                        isTop:$("#isTop").is(':checked'),
                        classificationId:$('#projectType').val(),
                        projectLevel:$('#projectLevel').val(),
                        projectClassification:$("#projectClassification").val(),
                        projectContent:$('#content').val(),
                    };
                    if(method=="add"){
                        $.postJSON(data,URL.publishProject,function(data){
                            if(data.status==1) {
                                id = data.projectId;
                                $('.second-item #ssi-upload').attr('data-id', id);
                                $('.first-item').hide();
                                $('.second-item').eq(0).hide();
                                $('.second-item').eq(1).show();
                                $('.second-item').eq(1).find('label').text('上传项目照片');
                                $('.ssi-clearBtn').click();
                                $step.nextStep();
                                init();
                                var id = $('.second-item #ssi-upload').attr('data-id');
                                initFileUploader($('#ssi-upload'),'/admin/updateprojectcover'
                                    ,6,1,['jpg','png'],{'projectId':id})
                            }
                        });
                    }else if(method=="modify"){
                        data['projectId'] = project.projectId;
                        $.postJSON(data,URL.updateProject,function(data){
                            if(data.status==1){
                                $('.first-item').hide();
                                $('.second-item').show();
                                $step.nextStep();
                                $('.table').empty();
                                initFileUploader($('#ssi-upload'),'/admin/updateprojectcover',
                                    6,1,['jpg','png'],{'projectId':project.projectId}
                                );
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
                        $('.third-item').show();
                        $('#resetBtn').hide();
                        $('#progressBtn').hide();
                        $step.nextStep();
                    }else{
                        alert("请先上传图片");
                    }
				}else if(method=="modify"){
                    $('.second-item').hide();
                    $('.third-item').show();
                    $('#resetBtn').hide();
                    $('#progressBtn').hide();
                    $step.nextStep();
				}
                break;
        }
    });

	$('.table').css('margin-top','20px');
	$('#videoIntroducation').css('resize','none');

	//发布项目按钮
	$('#addBtn').click(function(){
        $('.ssi-clearBtn').click();
        initStep($step,["项目信息", "项目封面", "上传成功"]);
        $('.second-item').eq(1).find('label').text('添加项目封面');
        $('.second-item h1').text('添加成功');
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
			$("#projectName").val("")
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
			var formValid = $('#project-form').validateForm();
			var editorValid = validateEditor(editor);
			if(formValid&&editorValid){
				var data = {
					projectName:$("#projectName").val(),
					projectLevel:$("#projectLevel").val(),
					projectClassification:$("#projectClassification").val(),
					isTop:$("#isTop").is(':checked'),
					projectContent:$('#content').val(),
				}
				if($("#myModal").attr("method")=="add"){
					//console.log(data);
					$.postJSON(data,URL.publishProject,function(data){
						if(data.status==1){
							init()
						}
					});
				}
			}
		})*/
	});
})

function init(){
	$.postJSON(null,URL.projectList,function(data){
		table_init(data);
	});
	initProjectType();
	initProjectLevel();
	$("#searchBtn").click(function(){
		query['projectName'] = $("#searchContent").val();
		query['projectType'] = $('#projectType').val();
		query['projectLevel'] = $('#projectLevels').val();
 		var data ={
 			projectName:query.projectName,
 			projectType:query.projectType,
			projectLevel:query.projectLevel,
		};
		$.postJSON(data,URL.queryProject,function(data){
			table_init(data)
		})
	})
}

function table_init(data){
	projects = data.projectList;
	var table = $(".table");
	table.empty("");
	$.each(projects, function(i,value) {
		if(i==0){
			table.append('<tr>');
		}
		if(i%3==0 && i!=0){
			table.append('</tr>');
			table.append('<tr>');
		}
		table.find('tr:last').append('<td id='+ value.projectId +'>'+ value.projectName +'</td>');
	});
	
	//每个项目
	$('.table').find('td').click(function(){
        $('.ssi-clearBtn').click();
        initStep($step,["项目信息", "项目封面", "修改成功"]);
        $('.second-item').eq(1).find('label').text('修改项目封面');
        $('.third-item').find('h1').text('修改成功');
        $('#deleteBtn').show();
		$('.first-item').show();
		$('.first-item:last').nextAll().hide();
		$('#progressBtn').show();
		$("input").val("");
		$("#isTop").removeAttr("checked");
		$("#myModal").attr("method","modify");
        $('#myModal').modal({backdrop: 'static'});
		var data = {
			projectId:$(this).attr('id'),
		};
        $.postJSON(data,URL.getProjectById,function(data){
            if(data.status==1){
                var $projectImgBox = $('.curImgs-wrapper.projectImg');
                $projectImgBox.find('ul').empty();
                var basePath = '/upload/picture/';
                console.log(data.project.projectCoverUrl);
                if(data.project.projectCoverUrl){
                    var projectImg = data.project.projectCoverUrl;
                    var obj = $('<li><img src='+basePath+projectImg+' alt=""/></li>');
                    $projectImgBox.find('ul').append(obj);
                }else{
                    $projectImgBox.find('ul').append('<li>暂无图片</li>')
                }
            }
        });
		$.postJSON(data,URL.projectInfo,function(data){
			project = data.project;
			$("#projectName").val(project.projectName);
			select_show($('#projectLevel'),project.levelCode);
			select_show($('#projectClassification'),project.classificationId);
			editor.$txt.html(project.projectContent);
			$('#isTop').prop('checked',project.isTop==1);
		}) 
		
		$("#deleteBtn").on('click',function(){
			if(confirm("确认删除该项目么？")){
				var data = {
					projectId:project.projectId,
				}
				$.postJSON(data,URL.delProject,function(data){
					if(data.status==1){
						init();
					}
				})
				$('#myModal').modal('hide');
			}
		})
		/*$("#submitBtn").on("click",function(e){
			if($("#project-form").validateForm()){
				var data = {
					projectName:$("#projectName").val(),
					projectLevel:$("#projectLevel").val(),
					projectClassification:$("#projectClassification").val(),
					isTop:$("#isTop").is(':checked'),
					projectContent:$('#content').val(),
				}
				if($("#myModal").attr("method")=="modify"){
					data['projectId'] = project.projectId;
					$.postJSON(data,URL.updateProject,function(data){
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

function initEditor(editor){
	
	editor.config.customUpload = true;  // 配置自定义上传的开关
    editor.config.customUploadInit = initUploader;  // 配置上传事件，uploadInit方法已经在上面定义了
	// 自定义菜单
	editor.config.menus = [
	    'head',
	    'bold',
	    'quote',
	    'fontfamily',
	    'fontsize',
	    'unorderlist',
	    'orderlist',
	    '|',
	    'img',
	    '|',
	    'eraser',
	    'undo',
	    'fullscreen'
	];
	
	editor.onchange=function(){
		validateEditor(this);
		
		$("#content").val(editor.$txt.html());
	}
	editor.create();
	return editor;
}

//------- 配置上传的初始化事件 -------
function initUploader () {
    // this 即 editor 对象
	var editor = this;
    // 编辑器中，触发选择图片的按钮的id
    var btnId = editor.customUploadBtnId;
    // 编辑器中，触发选择图片的按钮的父元素的id
    var containerId = editor.customUploadContainerId;

    //实例化一个上传对象
    var uploader = new plupload.Uploader({
		runtimes : 'html5,flash,silverlight,html4',
		browse_button : btnId, 
		container: $('#container')[0],
		flash_swf_url : '/plupload/Moxie.swf',
		silverlight_xap_url : '/plupload/Moxie.xap',
    	url : URL.uploadImage,
    	
    	filters : {
			mime_types: [
				{title : "图片文件", extensions : "jpg,jpeg,png,bmp,JPG,JPEG,PNG,BMP"}
			],
			max_file_size : '5M',
			prevent_duplicates : true //不允许选取重复文件
		},
		//plupload初始化
		init: {
		//上传文件加入队列是触发函数
			
			FilesAdded: function(uploader, files) {
			    uploader.start();
			},
			BeforeUpload: function(uploader, file) {
				
			},
			//文件上传完成时后触发
			FileUploaded: function(uploader, file, info) {
				//console.log(file);
				//console.log(info);
            	if (info.status == 200){
            		getFileUrl(info);
            	}
			},
        	UploadProgress: function (uploader, file) {
            	// 显示进度条
            	editor.showUploadProgress(file.percent);
        	},
			Error: function(uploader, err) {
            	if (err.code == -600) {
					showAlert("您选择的文件太大了,请重新选择！");
            	}else if (err.code == -601) {
					showAlert("您选择的文件后缀不对，请重新选择！");
            	}else if (err.code == -602) {
					showAlert("这个文件已经上传过一遍了！");
            	}else {
					showAlert("错误:" + err.response);
            	}
			}
		}
	});

    //初始化
    uploader.init();
    
    
}

function getFileUrl(info){
	var retjson = $.parseJSON(info.response);
	//console.log(retjson)
	imgUrls.push(retjson.imageUrl);
	// 用 try catch 兼容IE低版本的异常情况
   	try {
        //打印出所有图片的url地址
        $.each(imgUrls, function (key, value) {
        	//console.log(value);
           	// 插入到编辑器中
           	editor.command(null, 'insertHtml', '<img src="' + value + '" style="max-width:100%;"/>');
        });
    } catch (ex) {
        // 此处可不写代码
    } finally {
        //隐藏进度条
        editor.hideUploadProgress();
    }
	
} 

function validateEditor(editor){
	var valid=true;
	if(""==editor.$txt.text()){
		valid=false;
		showError($("#content"),"contentHelp","您输入的内容不能为空！");
	}else{
		hideError($("#content"),"contentHelp");
		valid=true;
	}
	return valid;
}

function initProjectType(){
	$.postJSON(null,URL.getProjectType,function(data){
		types = data.typeList;
		var typeSelect = $("#projectType");
		var classSelect = $("#projectClassification");
		typeSelect.empty();
		classSelect.empty();
		option_add(typeSelect,0,"全部");
		$.each(types,function(i,value){
			option_add(typeSelect,value.classificationId,value.classificationName)
		})
		$.each(types,function(i,value){
			option_add(classSelect,value.classificationId,value.classificationName)
		})
		
	})
}

function initProjectLevel(){
    $.postJSON(null,URL.getProjectLevel,function(data){
        levels = data.levelList;
        var levelSelect = $("#projectLevel");
        var levelNameSelect = $("#projectLevels");
        levelSelect.empty();
        levelNameSelect.empty();
        option_add(levelNameSelect,0,"全部");
        $.each(levels,function(i,value){
            option_add(levelNameSelect,value.actorLevel,value.levelName)
        })
        $.each(levels,function(i,value){
            option_add(levelSelect,value.actorLevel,value.levelName)
        })

    })
}

//增加一个option
function option_add(select,value,text) {
	 select.append("<option value='"+value+"'>"+text+"</option>");
}

function select_show(select,value){
	for(var i=0;i<select.get(0).options.length;i++){
		if(select.get(0).options[i].value == value){
			select.get(0).options[i].selected = true;
			break;
		}
	}
}
