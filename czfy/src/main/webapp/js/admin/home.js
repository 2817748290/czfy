$(function(){
	initTable();
});

function initTable(){
  $('#noticeTable').bootstrapTable({
  	classes:"table table-no-bordered",
  	striped:false,
	locale: "zh-CN",
	cache: false,
	url: "/web/getbulletins",
	//data: dataJson.rows,
	pagination: "true",
	sidePagination: "server",
	pageSize: 10,
	showHeader: false,
	//showRefresh: true,
	idField: "id",
	uniqueId: "id",
	method: "post",
    columns: [{
        field: 'id',
        title: '公告ID',
		visible: false
    }, {
        field: 'title',
        title: '公告标题'
    }, {
        field: 'content',
        title: '公告内容',
        visible: false
    }, {
        field: 'createDate',
        title: '公告时间',
        align: 'right',
        width:'20%'
    }],
	//得到查询的参数
	queryParams:function(params){
		return params;
	}
  });
}


