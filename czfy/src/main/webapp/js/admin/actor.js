var URL = {
    getActorLevelName:'admin/getActorLevelName',
    getActorType:'admin/getactortype',
    addActor:"admin/addactor",
    actorList:"admin/actorlist",
    actorInfo:"admin/actorinfo",
    delActor:"admin/delactor",
    updateActor:"admin/updateactor",
    actorPhotoUpload:"admin/actorphotoupload",
    actorWorkUpload:"admin/actorworkupload",
    queryActor:"admin/queryactor",
    getActorById:"admin/getActorById",
    updateIsPass:'admin/updateIsPass'
};

var levels = null;
var types = null;
var actors = null;
var actor = null;
var query = {
    actorName:'',
    actorClassification:'',
    actorLevel:''
};
var $step;
var stepIndex;
$(window).ready(function(){
    $step = $("#myModal #step");
    initStep($step,["艺人信息", "艺人图片","艺人作品","上传成功"]);
    init();

    //进度条控制
    //actorPhoto
    var id;
    $("#progressBtn").on("click", function(e) {
        stepIndex = $step.getIndex();
        var method = $("#myModal").attr("method");
        //上传资料
        switch(stepIndex)
        {
            case 0:
                if($("#actor-form").validateForm()){
                    var data = {
                        actorName:$("#actorName").val(),
                        actorIntroducation:$("#actorIntroducation").val(),
                        isTop:$("#isTop").is(':checked'),
                        classificationId:$('#actorType').val(),
                        actorLevel:$('#actorLevel').val(),
                    };
                    if(method=="add"){
                        $.postJSON(data,URL.addActor,function(data){
                            if(data.status==1){
                                id = data.actorId;
                                $('.second-item #ssi-upload1').attr('data-id',id);
                                $('.second-item #ssi-upload').attr('data-id',id);
                                $('.first-item').hide();
                                $('.second-item').eq(1).show();
                                $step.nextStep();
                                init();
                                $("#ssi-previewBox table").hide();
                                var id = $('.second-item #ssi-upload').attr('data-id');
                                initFileUploader($('#ssi-upload'),'/admin/actorphotoupload/'+id,
                                    6,1,['jpg','png']
                                );
                                initFileUploader($('#ssi-upload1'),'/admin/actorworkupload/'+id,
                                    6,6,['jpg','png']
                                );

                            }
                        });
                    }else if(method=="modify"){
                        data['actorId'] = actor.actorId;
                        $.postJSON(data,URL.updateActor,function(data){
                            if(data.status==1){
                                $('.first-item').hide();
                                $('.second-item').show();
                                $step.nextStep();
                                initFileUploader($('#ssi-upload'),'/admin/actorphotoupload/'+actor.actorId,
                                    6,1,['jpg','png']
                                );
                                initFileUploader($('#ssi-upload1'),'/admin/actorworkupload/'+actor.actorId,
                                    6,6,['jpg','png']
                                );
                                init();
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
                    if(flag) {
                        $('.second-item').hide();
                        $('.third-item').eq(1).show();
                        $step.nextStep();
                    }
                    else {
                        alert("请先上传图片");
                    }
                }else if(method=="modify"){
                    $('.second-item').hide();
                    $('.third-item').show();
                    $step.nextStep();
                }
                //文件上传框无内容
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
                        $.postJSON(null, URL.updateIsPass, function (data) {
                            init();
                        });
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
            default:
        }
    });

    $('.table').css('margin-top','20px');
    $('#actorIntroducation').css('resize','none');

    //增加艺人按钮
    $('#addBtn').click(function(){
        $('.ssi-clearBtn').click();
        initStep($step,["艺人信息", "艺人图片","艺人作品","上传成功"]);
        $('.second-item').eq(1).find('label').text('上传艺人照片');
        $('.third-item').eq(1).find('label').text('上传艺人作品');
        $('.forth-item h1').text('添加成功');
        $('#deleteBtn').hide();
        $('#progressBtn').show();
        $('.first-item').show();
        $('.first-item:last').nextAll().hide();
        $('checkbox').prop('checked','false');
        $('textarea').val('');
        $("input").val("");
        $("#myModal").attr("method","add");
        $('#myModal').modal({keyboard: false});
    });
    $("#closeBtn").click(function () {
        var index = $step.getIndex();
        if(index == 3) {
            $('.first-item').show();
            $('.second-item').hide();
            $('.forth-item').hide();
            $('#resetBtn').show();
            $('#progressBtn').show();
            $step.toStep(0);
        }
    });
    $('#resetBtn').on("click",function(){
        $("#actorName").val("");
        $("#actorIntroducation").val("");
        $('checkbox').attr('checked','false');
    })
});


function init(){

    $.postJSON(null,URL.getActorType,function(data){
        types = data.typeList;
        var classSelect = $("#actorClassification");
        var typeSelect = $("#actorType");
        typeSelect.empty();
        classSelect.empty();
        option_add(classSelect,0,"艺人类型");
        $.each(types,function(i,value){
            option_add(classSelect,value.classificationId,value.classificationName)
        });
        $.each(types,function(i,value){
            option_add(typeSelect,value.classificationId,value.classificationName)
        })

    });
    //类别
    $.postJSON(null,URL.getActorLevelName,function(data){
        levels = data.levelList;
        var actorLevels = $("#actorLevels");
        var actorLevel = $("#actorLevel");
        actorLevels.empty();
        actorLevel.empty();
        option_add(actorLevels,0,"艺人等级");
        $.each(levels,function(i,value){
            option_add(actorLevel,value.actorLevel,value.levelName)
        });
        $.each(levels,function(i,value){
            option_add(actorLevels,value.actorLevel,value.levelName)
        })

    });

    $.postJSON(null,URL.actorList,function(data){
        table_init(data);
    });

    $("#searchBtn").click(function(){
        query['actorName'] = $("#searchContent").val();
        query['actorClassification'] = $('#actorClassification').val();
        query['actorLevel'] = $('#actorLevels').val();
        var data ={
            actorName:query.actorName,
            actorClassification:query.actorClassification,
            actorLevel:query.actorLevel,
        };
        $.postJSON(data,URL.queryActor,function(data){
            table_init(data)
        })
    })
}

function table_init(data){
    actors = data.actorList;
    var table = $(".table");
    table.empty("");
    $.each(actors, function(i,value) {
        if(i==0){
            table.append('<tr>');
        }
        if(i%5==0 && i!=0){
            table.append('</tr>');
            table.append('<tr>');
        }
        table.find('tr:last').append('<td id='+ value.actorId +'>'+ value.actorName +'</td>');
    });

    //每个艺人
    $('.table').find('td').click(function(){
        $('.ssi-clearBtn').click();
        initStep($step,["艺人信息", "艺人图片","艺人作品","修改成功"]);
        $('.third-item').eq(0).show();
        $('.second-item').eq(0).show();
        $('.second-item').eq(1).find('label').text('修改艺人图片');
        $('.third-item').eq(1).find('label').text('修改艺人作品图片');
        $('.forth-item').find('h1').text('修改成功');
        $('.first-item').show();
        $('.first-item:last').nextAll().hide();
        var $actorphotoBox = $('.curImgs-wrapper.actorphoto');
        var $actorworkBox = $('.curImgs-wrapper.actorwork');
        $actorworkBox.find('ul').empty();
        $actorphotoBox.find('ul').empty();
        var data = {
            actorId:$(this).attr('id'),
        };
        console.log(data);
        $.postJSON(data,URL.getActorById,function(data){
            //艺人图片
            if(data.actorInfo.actor.photo){
                var actorphoto = data.actorInfo.actor.photo;
                var basePath = '/upload/picture/';
                var index = actorphoto.lastIndexOf("\/");
                actorphoto  = actorphoto.substring(index + 1, actorphoto.length);
                var obj = $('<li><img src='+basePath+actorphoto+' alt=""/></li>');
                $actorphotoBox.find('ul').append(obj);
            }else{
                $actorphotoBox.find('ul').append('暂无图片');
            }

            //艺人作品图片
            console.log(data.actorInfo.images);
            if(data.actorInfo.images){
                var actorworkList = data.actorInfo.images;
                console.log(actorworkList)
                for(var i=0;i<actorworkList.length;i++){
                    //去掉第一个重复数据
                    if(i!=0){
                        var imgPath = actorworkList[i].imageUrl;
                        console.log(imgPath)
                        var index = imgPath.lastIndexOf("\/");
                        imgPath  = imgPath.substring(index + 1, imgPath.length);
                        var imgLi = $('<li><img src='+basePath+imgPath+' alt=""/></li>');
                        $actorworkBox.find('ul').append(imgLi);
                    }

                }
            }else{
                $actorworkBox.find('ul').append('暂无图片');
            }

        });
        $('.forth-item h1').text('修改成功')
        $('#deleteBtn').show();
        $('#progressBtn').show();
        $('textarea').val('');
        $("#isTop").removeAttr("checked");
        $("input").val("");
        $("#myModal").attr("method","modify");
        $('#myModal').modal({backdrop: 'static', keyboard: false});
        $.postJSON(data,URL.actorInfo,function(data){
            actor = data.actor;
            $("#actorName").val(actor.actorName);
            $("#actorIntroducation").val(actor.actorIntroducation);
            select_show($('#actorType'),actor.classificationId);
            $('#isTop').prop('',actor.isTop==1);
            select_show($('#actorLevel'),actor.actorLevel);
        });

        $("#deleteBtn").on('click',function(){
            if(confirm("确认删除该艺人的信息么？")){
                $.postJSON(data,URL.delActor,function(data){
                    if(data.status==1){
                        init();
                    }
                });
                $('#myModal').modal('hide');
            }
        })
    });
}

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

