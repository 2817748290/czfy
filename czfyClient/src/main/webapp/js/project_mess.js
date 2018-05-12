$(function(){
	$('.announce').html(localStorage.getItem('project_name'));
    
    function  getParma(name) {
        var reg = new RegExp("(^|&)" + name + "=([^&]*)(&|$)");
        var r = window.location.search.substr(1).match(reg);
        if (r != null) return unescape(r[2]); return null;
    }

    //var BASE_URL = "http://139.199.61.16/czfyClient";
    //var BASE_URL ="http://localhost:8080";
    var data = {
        "projectId":getParma('aid')
    }
    $.ajax({
        url: BASE_URL + "/client/getprojectbyid",
        type: 'POST',
        data:JSON.stringify(data),
        contentType:"application/json; charset=utf-8",
        success: function(data){
            if(data.status == 1){
                var html = '<div class="twelve columns">'+

                '<div class="title pencil-icon">项目介绍</div>'+

                '<div class="text margin-tb-20">'+data.project.projectContent+'</div>'+

             '</div> ';
            }
            $('.mess').append(html);
        }
    });
})