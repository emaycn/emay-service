$(function() {
	if(!window.top.hasAuths("ADD_ROLE")){
		$("#addBtn").remove();
	};
});
var util=new Utility();
$(function(){
		$('#table').bootstrapTable({
		    url:util.WEB_SERVER_PATH+"/role/ajax/list",                         //请求数据url
		    method: 'post',                     //请求方式
			dataType : "json",
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",    //汉化
			queryParams: function (params) {
				 return {
			            start: params.offset,       //页码
			            limit: params.limit,        //页面大小
			            roleName : $('#roleName').val(), //角色名称
			        };
		    },
		    sidePagination: "server",            //服务端处理分页      分页方式：client客户端分页，server服务端分页（*）
		    pagination: true,                    //是否在表格底部显示分页组件，默认false 不显示， true 显示
		    pageNumber : 1,                      //初始化加载第一页，默认第一页
		    pageSize : 10,                       //每页的记录行数
		    pageList: [10,20, 50, 100],          //可供选择的每页的行数
		    clickToSelect: false,                //是否启用点击选中行
		    search: false,                       //是否显示搜索框true 显示  false不显示
		    showRefresh: true,                   //是否显示刷新按钮 默认false 不显示， true 显示
		    striped: true,                       //是否显示行间隔色
		    sortable: true,                      //是否启用排序 true为不启动  false 启动
			responseHandler:function(res){       //从服务端请求到的数据
				if(!util.ajaxTableSetup(res)){
					return []
				}
		    	return{
		    		 "total":res.result.totalCount,
		    	     "rows":res.result.list
		    	}
		    },  
		    columns: [
		    	{
	                field: 'no',
	                title: '序号',
	                width : '5%',
	                align: "center",
	                valign: 'middle' ,
	                formatter: function (value, row, index) {
	                    //获取每页显示的数量
	                    var pageSize=$('#table').bootstrapTable('getOptions').pageSize;
	                    //获取当前是第几页
	                    var pageNumber=$('#table').bootstrapTable('getOptions').pageNumber;
	                    //返回序号，注意index是从0开始的，所以要加上1
	                    return pageSize * (pageNumber - 1) + index + 1;
	                }
	            }, {
		            field: 'name',                    //域值
		            title: '角色名称',                 //标题
		            visible: true,                  //是否显示该字段，false表示不显示，true显示 默认是true 可不写
		            width : '10%',                   //设置宽度
		            align: 'center', 				//设置左右居中
		            valign: 'middle'             //设置上下居中
		        },{
		            field: 'id', 
		            title: '操作', 
		            visible: true, 
		            align: 'center',  
		            valign: 'middle',  
		            width : '12%',
		            formatter:actionFormatter
		        }
		    ]
		});
})
//操作栏的格式化
function actionFormatter(value, row, index) {
    var id = value;
    var name=row.name;
    var result = "";
    if(window.top.hasAuths("EDIT_ROLE")){
    	result += '<a href="javascript:;" class="btn btn-xs green" id="modifyRole" title="编辑" onclick="modifyRole(' + id + ')"><span class="glyphicon glyphicon-pencil"></span></a>';
	};
    if(window.top.hasAuths("DELETE_ROLE")){
    	result += '<a href="javascript:;" class="btn btn-xs green" id="deleteRole" title="删除" onclick="delConRole(\'' + id + '\',\''+name+'\')"><span class="glyphicon glyphicon-remove"></span></a>';
	};
    return result;
}

/*查询*/
function searchFun(){
	$("#table").bootstrapTable('selectPage',1);
};
//跳转新增角色的页面
function addUser(){
	window.location.href =  util.WEB_SERVER_PATH+"/role/addrole";
}
/*跳转修改角色的页面*/
function modifyRole(id){
	if(id!=1){
	window.location.href =  util.WEB_SERVER_PATH+"/role/modifyrole?id="+id;
	}else{
		util.tipAlert({message:'不能操作超级管理员',type:2});
	}
}
//删除
function delConRole(id,roleName){
	if(id!=1){
		var addHtml = "<div>确定要删除角色【"+roleName+"】吗?</div>";
	 	var footHtml='<button type="button" class="btn btn-default" data-dismiss="modal" onclick="delConfirm('+id+')">确定</button>';
	 	footHtml+='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>';
	 	util.tipModel({
	 		title:'删除确认',
	 		id: 'delModal',
	 		width: '400px',
			height: '60px',
	 		content : addHtml,
	 		footerHtml : footHtml
	 			
	 	})
		$('#delModal').modal('show');
	}else{
		util.tipAlert({message:'不能操作超级管理员',type:2});
	}
}
function delConfirm(id){	
	  $.ajax({
	  type: "post",
	  url:util.WEB_SERVER_PATH+"/role/ajax/delete",
	  data: {
		  roleId:id
	  }, 
	  dataType:"json",
	  success : function(data) {
		  if(data.success){
			  util.tipAlert({message:'删除成功',type:1});
		     $("#table").bootstrapTable('refresh');//主要是要这种写法
		  }else{
			  util.tipAlert({message:data.message,type:2});
		  }
	 },
	 error:function(data){
		 util.tipAlert({message:"系统异常",type:0});
	 }
 
});
}



