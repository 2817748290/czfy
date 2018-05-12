 var url=BASE_URL_1+"/infor/addInfor";
 $.validator.setDefaults({
     onkeyup: function (element) {
         $(element).valid();
     },
     onfocusout: function (element) {
         $(element).valid();
     },
     submitHandler: function () {
         var data = {};
         var form = $('form').serializeArray();
         $.each(form, function () {
             data[this.name] = this.value;
         });
         //window.alert(JSON.stringify(data));
         //console.log('klnjnh.kljnh.kj');
         console.log(form);
         console.log(data);
         $.ajax({
             url: url,
             type: 'POST',
             data: JSON.stringify(data),
             contentType: "application/json;charset=utf-8",
             success: function (data) {
                 console.log(data);
                 //console.log(data.infor.inforTitle);
                 window.alert("提交成功，谢谢您的支持！");
             }
         });
     }
 });

 var validator =   $("#form").validate({
       // debug:true,
         rules: {
             inforTitle: {
             required: true,
             minlength: 5
             },
             inforAuthor: {
             required: true
             },
             classId: {
             required: true
                },
             email: {
            required: true,
             email: true
             },
             inforContent:{
                 required: true,
                 minlength: 200
             }
         },
         messages: {
             inforTitle: {
             required: "请输入新闻标题",
             minlength: "新闻标题必需由五个或五个以上字符组成"
             },
             inforAuthor: {
             required: "请输入作者姓名"
             },
             inforContent:{
                 required:"请输入新闻内容",
                 minlength:"新闻内容不少于200字"
             },
         }
 });
 console.log("validator");
console.log(validator);

$('#form').attr("function",url);
console.log($('#form').attr("function"));
var cookieValue=$.cookie('islogin');
console.log(cookieValue);
if(cookieValue!='true') location.assign("xinwen.html");