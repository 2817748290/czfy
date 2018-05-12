/**
 *  获取url中的参数
 */
BASE_URL_1 ="http://localhost:8085";
function getUrlParam(name) {
    var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)"); //构造一个含有目标参数的正则表达式对象
    var r = window.location.search.substr(1).match(reg);  //匹配目标参数
    if (r != null) return unescape(r[2]); return null; //返回参数值
}
$('')
var data2 = {
    "inforId":getUrlParam('id')
};

    console.log(data2);
$.ajax({
    url:BASE_URL_1+"/infor/getInforById",
    type: 'POST',
    data:JSON.stringify(data2),
    contentType:"application/json;charset=utf-8",
    success: function(data){
        console.log(data);
        console.log(data.infor.inforTitle);
            var title = '<h1 class="h1">'+ data.infor.inforTitle +'</h1>';


            var html = '<p class="text-left text-muted">作者:'+
                '' + data.infor.inforAuthor + '    时间:' + data.infor.inforDate+'</p>'+
                '<br>'+

                '<p class="contentContainer text-left">' + data.infor.inforContent+'</p>';

            $('#title').empty();
            $('#title').append(title);

            $('#content').empty();
            $('#content').append(html);
        }
});
// });