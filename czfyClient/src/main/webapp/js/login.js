$(function(){
  $(".screenbg ul li").each(function(){
    $(this).css("opacity","0");
  });
  $(".screenbg ul li").first().css("opacity","1");
  var index = 0;
  var t;
  var li = $(".screenbg ul li");  
  var number = li.length;//length == size()
  function change(index){
    li.css("visibility","visible");
    li.eq(index).siblings().animate({opacity:0},3000);
    li.eq(index).animate({opacity:1},3000);
  }
  function show(){
    index = index + 1;
    if(index<=number-1){
      change(index);
    }else{
      index = 0;
      change(index);
    }
  }
  t = setInterval(show,8000);
  //根据窗口宽度生成图片宽度
  var width = $(window).width();
  $(".screenbg ul img").css("width",width+"px");

  $('.btn1').click(function(){
    // localStorage.setItem("user",$('#user').val());
    // if($('#remember').get(0).checked){
    //   localStorage.setItem('isUser','yes');
    // }else{
    //   localStorage.setItem('isUser','no');
    // }
    $.ajax({
      url: 'admin/login.php',
      type: 'POST',
      dataType: 'json',
      data: {
        username : $('.name input').val(),
        password : $('.password input').val()
      },
      success: function(response,status,xhr){
        if(status == 'success'){
          if(response.statu == 0){
            localStorage.setItem("user",$('.name input').val());

            parent.bb.location.href = 'welcome.html';
            // window.opener.location.reload();

          }else{
            alert('账号或密码错误，请重试！');
          }
        }else{
          alert('未知错误');
        }

      }
    });
    // .open('index.html','_blank');
    return false;
  });
});