$(function(){
	var window_height = document.body.scrollHeight +'px';
	var window_width = window.screen.width +'px';
	
	$('body').css('background-size',window_width,window_height);
	$('.reg').validate({
		highlight:function(element,errorClass){
	       $(element).css({"border":"1px solid red"});
	    },
	    unhighlight:function(element,errorClass){
	       $(element).css({"border":"1px solid #ddd"});
	    },
	    rules:{
	        user:{
	         required:true,
	        },
	        pass:{
	          required:true,
	          minlength:6,
	        },
	        email:{
	          required:true,
	          email : true
	        },
	    },
	    messages:{
	        user:{
	          required:   " 不得为空！",
	        },
	        pass:{
	          required: " 不得为空！",
	          minlength: "不得少于{0}位"
	        },
	        email:{
	          required: " 不得为空！",
	          email : "不合法的邮箱"
	        }
	    }
	});

	$('#email').autocomplete({
      delay : 0,
      source : function(request,response){
        var hosts = ['qq.com','163.com','263.com','sina.com.cn','gmail.com','hotmail.com'],
            term = request.term,
            name = term,
            host = '',
            ix = term.indexOf('@'),
            result = [];
        result.push(term);
        if(ix >-1){
          name = term.slice(0,ix);
          host = term.slice(ix+1);
        }
        if(name){
          var resulthostList = host? resulthostList = $.grep(hosts,function(value,index){
                 return value.indexOf(host)>-1;
              }):hosts,
              resultList = $.map(resulthostList,function(value,index){
                return name +'@'+ value;
              })
          result = result.concat(resultList);
        }
        response(result);
      } 
  });
});