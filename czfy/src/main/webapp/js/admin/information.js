var URL = {
    addInfor:'admin/infomation/addInfor',
    inforList:'admin/infomation/getInformation',
    queryInfor:'admin/infomation/queryInfor',
    delInfor:'admin/infomation/delInfor',
    getInforById:'admin/infomation/getInforById',
    getPassInfo:'admin/infomation/getPassInfo',
    isPass:'admin/infomation/isPass',
    updateInfor:'admin/infomation/updateInfor'
};

var isPassInfo = null;
var inforList = null;
var infor = null;
var query = {
	inforTitle:'',
	inforType:'',
	isPass:''
};
var $step;
var stepIndex;
$(window).ready(function(){
	
	init();
    $step = $("#myModal #step");
    initStep($step,["项目信息", "上传成功"])
    //进度条控制
    //inforPhoto
    var id;
    $("#progressBtn").on("click", function(e) {
        stepIndex = $step.getIndex();
        //上传资料
        switch (stepIndex){
            case 0:
                if($("#infor-form").validateForm()){
                    var data = {
                        inforTitle: $("#inforTitle").val(),
                        inforType: $("#inforType").val(),
                        isTop: $("#isTop").is(':checked'),
                        isPass: $("#isPass").val()
                    };
                    if($("#myModal").attr("method")=="add"){
                        $.postJSON(data,URL.addInfor,function(data){
                            if (data.status==1) {
                                $('.table').empty();
                                id = data.inforId;
                                $('.second-item #ssi-upload1').attr('data-id', id);
                                $('.second-item #ssi-upload').attr('data-id', id);
                                $('.first-item').hide();
                                $('.second-item').show();
                                $step.nextStep();
                                init();
                                $("#ssi-previewBox table").hide();
                                var id = $('.second-item #ssi-upload').attr('data-id');
                            }
                        });
                    }else if($("#myModal").attr("method")=="modify"){
                        data['inforId'] = infor.inforId;
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
        }
    });

    $('.table').css('margin-top','20px');
	$('#videoIntroducation').css('resize','none');
	
	//增加新闻按钮
    $('#addBtn').click(function(){
        $('.step-main').show();
        $('#submitBtn').hide();
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
            $("#inforName").val("")
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
    });
});

function init(){
    $('.table').empty();

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

	$.postJSON(null,URL.inforList,function(data){
		table_init(data);
	});
	
	$("#searchBtn").click(function(){
		$('.table').empty();
		query['inforTitle'] = $("#searchContent").val();
		query['inforType'] = $('#inforFilter').val();
        query['isPass'] = $('#isPassS').val();
 		var data ={
			inforTitle:query.inforTitle,
			inforType:query.inforType,
			isPass:query.isPass,
		};
		$.postJSON(data,URL.queryInfor,function(data){
			table_init(data);
		})
	})
}
function table_init(data) {

    var table = $(".table");
    table.empty("");
    inforList = data.inforList;
    if(inforList.length){
        table += '<tbody>';
        $.each(inforList, function (i, value) {
            if (i % 3 === 0) {
                table += '<tr>';
            }
            table += '<td id=' + value.inforId + '><a href="#">' + value.inforTitle + '</a><br>';
            $.each(isPassInfo, function (i1, value1) {
                var isPass = parseInt(value.isPass);
                var checked = '';
                if(isPass === value1.isPass){
                    checked = 'checked';
                }
                table += '<input type="radio" class="pass" name="' + value.inforId + '" id="'+value1.isPass+'" ' + checked + '>' +
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
                inforId:id,
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
        $('.step-main').hide();
        $('.first-item').show();
        $('.first-item:last').nextAll().hide();
        $('#progressBtn').hide();
        $('#deleteBtn').show();
        $('#submitBtn').show();
        $("input").val("");
        $("#isTop").removeAttr("checked");
        $("#myModal").attr("method", "modify");
        $('#myModal').modal({backdrop: 'static'});
        var data = {
            inforId: $(this).parent().attr('id')
        };
        $.postJSON(data, URL.getInforById, function (data) {
            infor = data.infor;
            $("#inforTitle").val(infor.inforTitle);
            select_show($('#inforType'), infor.inforType);
            select_show($('#isPass'), infor.isPass);
            $('#isTop').prop('checked', infor.isTop == 1);
        });

        $("#deleteBtn").on('click', function () {
            if (confirm("确认删除该新闻么？")) {
                $('.table').empty();
                var data = {
                    inforId: infor.inforId
                };
                $.postJSON(data, URL.delInfor, function (data) {
                    if (data.status == 1) {
                        init();
                    }
                });
                $('#myModal').modal('hide');
            }
        });

        $("#submitBtn").on("click", function (e) {
            if ($("#infor-form").validateForm()) {
                var data = {
                    inforTitle: $("#inforTitle").val(),
                    inforType: $("#inforType").val(),
                    isTop: $("#isTop").is(':checked'),
                    isPass: $("#isPass").val(),
                };
                if ($("#myModal").attr("method") == "add") {
                    $.postJSON(data, URL.addInfor, function (data) {
                        if (data.status == 1) {
                            $("#myModal").modal('hide');
                            init()
                        }
                    });
                } else if ($("#myModal").attr("method") == "modify") {
                    data['inforId'] = infor.inforId;
                    $.postJSON(data, URL.updateInfor, function (data) {
                        if (data.status == 1) {
                            $("#myModal").modal('hide');
                            init()
                        }
                    })
                }
            }
        })
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
