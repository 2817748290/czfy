BASE_URL_1 ="http://localhost:8085";

 var item_url=BASE_URL_1+'/infor/queryInfor';
    var data={
        "inforTitle":'',
        "classId":parseInt(getUrlParam("classification"))
    };
console.log("开始");
console.log(data);
datatable(data);
isCookie();

//$.cookie('the_cookie', 'the_value', { expires: 7 });
//$.cookie('the_cookie', null);
/**
 * 登陆
 */
function login1(){
    var username=$('.form-control4').val();
    var password=$('.form-control5').val();
    var data1={
        'username':username,
        'password':password
    };
    console.log('data1:');
    console.log(data1);
    $.ajax({
        url: BASE_URL_1 + "/user/userLogin",
        type: 'POST',
        data:JSON.stringify(data1),
        contentType:"application/json; charset=utf-8",
        success: function(data){
            console.log(data.user);
             if(data.user){
                var text="欢迎你，"+data.user.nickname+"！";
                 $('.form-horizontal').hide();
                 $('.modal-title1').text(text);
                 $.cookie('username', username);
                 $.cookie('password', password);
                 $.cookie('islogin', 'true');
                 isCookie();
             }
             else{
                 console.log("错误");
                 var text="登陆失败请重试！！";
                 $('.modal-title').text(text);

             }
        }
    });
}

/**
 * 注册
 */
function register(){
    if($('.form-horizontal1').valid()==true) {
        var username = $('.form-control1').val();
        var password = $('.form-control3').val();
        var data1 = {
            'username': username,
            'password': password,
            'nickname': $('.form-control2').val()
        };
        console.log('data1:');
        console.log(data1);
        $.ajax({
            url: BASE_URL_1 + "/user/userRegister",
            type: 'POST',
            data: JSON.stringify(data1),
            contentType: "application/json; charset=utf-8",
            success: function (data) {
                console.log(data);
                var text = "注册成功！！ 请保存好账号密码！！";
                $('.form-horizontal').hide();
                $('.modal-title2').text(text);
                $.cookie('username', username);
                $.cookie('password', password);
                $.cookie('islogin', 'true');
                isCookie();
            }

        });
    }
    else{
        window.alert("信息不通过！！！");
    }
}

//$.cookie('the_cookie'); // cookie存在 => 'the_value'

//$.cookie('not_existing'); // cookie不存在 => null
/**
 * 获取url参数值
 * @param name
 * @returns {null}
 */
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]); return null; //返回参数值
}
/**
 * batatable
 * @param data
 */
function datatable(data){
    $('.datatable').DataTable({
        "dom": '<"top"fl<"clear">>rt<"bottom"ip<"clear">>',
        "sAjaxSource": item_url,
        //如果加上下面这段内容，则使用post方式传递数据
        "fnServerData": function ( sSource, aoData, fnCallback ) {
            $.ajax( {
                "dataType": 'json',
                "type": "POST",
                "contentType":"application/json; charset=utf-8",
                "url": sSource,
                "data":JSON.stringify(data),
                "success": fnCallback
            } );
        },
        "oLanguage": {
            "sUrl": "cn.txt"
        },
        "aoColumns": [
            // { "data": "inforId" },
            { "data": "inforTitle" },
            { "data": "inforAuthor" },
            { "data": "inforDate" },
            {"sWidth": "10px", "data": "inforId","mRender": function(data, type, full) {
                return '<a target="view_window" class="btn btn-link btn-sm" style="line-height: 10px;margin: 0;" href="/new/html/oneNews.html?id='+data+'">查看详情</a>'
                    // +'<button id="+data+" class="btn btn-link btn-sm">查看详情</button>'
                    ;}
            }
        ],//$_GET['sColumns']将接收到aoColumns传递数据
        "oLanguage": {                          //汉化
            "sLengthMenu": "每页显示 _MENU_ 条记录",
            "sZeroRecords": "没有检索到数据",
            "sInfo": "总共_PAGES_ 页，显示第_START_ 到第 _END_ ，总共有 _TOTAL_ 条记录",
            "sInfoEmtpy": "没有数据",
            "sProcessing": "正在加载数据...",
            "oPaginate": {
                "sFirst": "",
                "sPrevious": "上一页",
                "sNext": "下一页",
                "sLast": ""
            }
        }
    });
}

/**
 * 登陆状态判断
 * @param i
 */
function login(i){
    var html='';
    if(i==0){
        $('#tougao').hide();
        html+='<h4 class="username">'+'</h4>'+
            '<p>'+'请登录后投稿'+'</p>'+
            '<div class="btn-group margin-bottom-2x" role="group">'+
            '<button type="button" class="btn btn-default" ' +
                'class="btn-lg" data-toggle="modal" data-target="#myModal1">' +
                '<i class="fa fa-user"></i> 登陆</button>'+
            '<button type="button" class="btn btn-default"' +
            'class="btn-lg" data-toggle="modal" data-target="#myModal2">' +
            '<i class="fa fa-sign-out"></i> 注册</button>'+
            '</div>'
    }
    else{
        $('#tougao').show();
        var data=userinfo();
        html+='<h4 class="username">'+data+'</h4>'+
            '<p>id:'+$.cookie('username')+'</p>'+
            '<div class="btn-group margin-bottom-2x" role="group">'+
            '<button onclick="cancel()" type="button" class="btn btn-default"' +
            '><i class="fa fa-sign-out cancel" ></i>注销</button>'+
            '</div>'
    }
    $('.profile-info').empty();
    $('.profile-info').append(html);
}
/**
 * Cookie验证
 */
function isCookie() {
    var cookieValue=$.cookie('islogin');
    console.log(cookieValue);
    if(cookieValue=='true') login(1);
    else login(0);
}

/**
 * 注销
 */
function cancel(){
    console.log("注销");
    $.cookie('username',null);
    $.cookie('password', null);
    $.cookie('islogin', null);
    login(0);
    $('.form-horizontal').show();
    $('.modal-title2').text("注册");
    $('.modal-title1').text("登陆");
    window.alert("注销成功！！");
}

/**
 * cookie-->nickname
 * @returns {*}
 */
function userinfo(){
    var data1={
        'username':$.cookie('username'),
        'password':$.cookie('password')
    };
    console.log(data1);
    var data2;
    $.ajax({
        url: BASE_URL_1 + "/user/userLogin",
        type: 'POST',
        data:JSON.stringify(data1),
        contentType:"application/json; charset=utf-8",
        async:false,
        success: function(data){
            data2=data.user.nickname;
            }
    });
    console.log(data2);
    return data2;
}

/**
 * 表单信息验证
 */
//alert($('.form-horizontal').valid());(通过为true 不通过为false)
$(".form-horizontal").validate({
        rules: {
            username: {
                required: true,
                minlength: 5
            },
            nickname: {
                required: true,
                minlength: 5
            },
            password: {
                required: true,
                minlength: 5
            },
            confirm_password: {
                required: true,
                minlength: 5,
                equalTo: "#password"
            },
            email: {
                required: true,
                email: true
            }
        },
        messages: {
            username: {
                required: "请输入账号",
                    minlength: "账号必需由五个或五个以上字符组成"
            },
            nickname: {
                required: "请输入昵称",
                    minlength: "昵称必需由五个或五个以上字符组成"
            },
            password: {
                required: "请输入密码",
                    minlength: "密码长度不能小于 5 个字符"
            },
            confirm_password: {
                required: "请输入密码",
                    minlength: "密码长度不能小于 5 个字符",
                    equalTo: "两次密码输入不一致"
            },
            email: "请输入一个正确的邮箱"
        }
    });
$(".form-horizontal1").validate({
    rules: {
        username: {
            required: true,
            minlength: 5
        },
        nickname: {
            required: true,
            minlength: 5
        },
        password: {
            required: true,
            minlength: 5
        },
        confirm_password: {
            required: true,
            minlength: 5,
            equalTo: "#password"
        },
        email: {
            required: true,
            email: true
        }
    },
    messages: {
        username: {
            required: "请输入账号",
            minlength: "账号必需由五个或五个以上字符组成"
        },
        nickname: {
            required: "请输入昵称",
            minlength: "昵称必需由五个或五个以上字符组成"
        },
        password: {
            required: "请输入密码",
            minlength: "密码长度不能小于 5 个字符"
        },
        confirm_password: {
            required: "请输入密码",
            minlength: "密码长度不能小于 5 个字符",
            equalTo: "两次密码输入不一致"
        },
        email: "请输入一个正确的邮箱"
    }
});
$.validator.setDefaults({
    onfocusout: function (element) {
        $(element).valid();
    },
    onkeyup: function(element){
        $(element).valid();
    }
});
