$(function() {
	if(!window.top.hasAuths("ADD_USER")){
		$("#addBtn").remove();
	};
});
var util=new Utility();
$(function(){
		$('#table').bootstrapTable({
		    url:util.WEB_SERVER_PATH+"/user/ajax/list",                         //请求数据url
		    method: 'post',                     //请求方式
			dataType : "json",
			contentType : "application/x-www-form-urlencoded; charset=UTF-8",    //汉化
			queryParams: function (params) {
				 return {
			            start: params.offset,       //页码
			            limit: params.limit,        //页面大小
			            username : $('#username').val(), //用户名
			            nickname : $('#nickname').val(), //姓名
						mobile : $('#mobile').val(),//手机号
						state:$('#state option:selected').val()
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
		    columns: [{
	  	                field: 'no',
	  	                title: '序号',
	  	                width : '5%',
	  	                align: "center",
	  	                valign: 'middle' ,
	  	                formatter: function (value, row, index) {
	  	                    var pageSize=$('#table').bootstrapTable('getOptions').pageSize;
	  	                    var pageNumber=$('#table').bootstrapTable('getOptions').pageNumber;
	  	                    return pageSize * (pageNumber - 1) + index + 1;
	  	                }
  	               }, {
		            field: 'username',                    //域值
		            title: '用户名',                 //标题
		            visible: true,                  //是否显示该字段，false表示不显示，true显示 默认是true 可不写
		            width : '10%',                   //设置宽度
		            align: 'center', 				//设置左右居中
		            valign: 'middle'             //设置上下居中
		        },{
		            field: 'nickname', 
		            title: '姓名', 
		            visible: true, 
		            width : '7%',
		            align: 'center',  
		            valign: 'middle'
		        },{
		            field: 'mobile', 
		            title: '手机号', 
		            visible: true, 
		            width : '10%',
		            align: 'center',  
		            valign: 'middle'
		        },{
		            field: 'email', 
		            title: '邮箱', 
		            visible: true, 
		            width : '20%',
		            align: 'center',  
		            valign: 'middle',
		            formatter: function(value,row,index){ 
		            	return '<div class="ellipsisDiv" style="width:220px;" title="'+value+'">'+value+'</div>'
		            }
		        },{
		            field: 'roleNames', 
		            title: '角色', 
		            visible: true, 
		            width : '10%',
		            align: 'center',  
		            valign: 'middle',
		            formatter: function(value,row,index){ 
		            	return '<div class="ellipsisDiv" style="width:100px;" title="'+value+'">'+value+'</div>'
		            }
		        },{
		            field: 'isLeader', 
		            title: '是否领导', 
		            visible: true, 
		            align: 'center',  
		            valign: 'middle',  
		            width : '6%',
		            formatter: function(value,row,index){   //后端返回判断值的情况
		             	if(value==1){
		             		return "是"
		             	}else if(value==0){
		             		return "否"
		             	}else if(value==3){
		             		return "锁定"
		             	}
		             }
		        },{
		            field: 'state', 
		            title: '状态', 
		            visible: true, 
		            align: 'center',  
		            valign: 'middle',  
		            width : '5%',
		            formatter: function(value,row,index){   //后端返回判断值的情况
		             	if(value==0){
		             		return "启用"
		             	}else if(value==1){
		             		return "停用"
		             	}else if(value==2){
		             		return "删除"
		             	}
		             }
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
    var username=row.username;
    var state=row.state;
    var isResignation=row.isResignation;
    var result = "";
     if(window.top.hasAuths("OPER_USER")){
	    if(state==1 || state==3){
	    	result += '<a href="javascript:;" class="btn btn-xs green" onclick="enable(\'' + id + '\',\''+username+'\')" title="启用"><span class="glyphicon glyphicon-play"></span></a>';
	    }else if(state==2){
	    	result += '<a href="javascript:;" class="btn btn-xs green" onclick="Disable(\'' + id + '\',\''+username+'\')" title="停用"><span class="glyphicon glyphicon-pause"></span></a>';
	    }
     };
     if(isResignation==0){//在职
    	 result += '<a href="javascript:;" class="btn btn-xs green" onclick="leaveOffice(\'' + id + '\',\''+username+'\',\''+isResignation+'\')" title="离职"><span class="glyphicon glyphicon-ban-circle"></span></a>';
     }
     if(window.top.hasAuths("EDIT_USER")){
		result += '<a href="javascript:;" class="btn btn-xs green" onclick="modifyUser(' + id + ')" title="编辑"><span class="glyphicon glyphicon-pencil"></span></a>';
	 };
	 if(window.top.hasAuths("OPER_USER")){
		 result += '<a href="javascript:;" class="btn btn-xs green" onclick="delConfirm(\'' + id + '\',\''+username+'\')" title="删除"><span class="glyphicon glyphicon-remove"></span></a>';
	 };
	 if(window.top.hasAuths("RESET_PASSWORD")){
		 result += '<a href="javascript:;" class="btn btn-xs green" onclick="resetPw(\'' + id + '\',\''+username+'\')" title="重置密码"><span class="glyphicon glyphicon-wrench"></span></a>';
	 };
    return result;
}

/*查询*/
function searchFun(){
	$("#table").bootstrapTable('selectPage',1);
};
//跳转新增的页面
function addUser(){
	window.location.href =  util.WEB_SERVER_PATH+"/user/adduser";
}
/*跳转修改的页面*/
function modifyUser(id){
	window.location.href =  util.WEB_SERVER_PATH+"/user/modifyuser?id="+id;
}
/*停用*/
function Disable(id,user){
	if(id!=1){
		var addHtml = "<div>确定要停用用户【"+user+"】?</div>";
	 	var footHtml='<button type="button" class="btn btn-default" data-dismiss="modal" onclick="Dis('+id+')">确定</button>';
	 	footHtml+='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>';
	 	util.tipModel({
	 		title:'停用确认',
	 		id: 'disableModal',
	 		width: '400px',
			height: '60px',
	 		content : addHtml,
	 		footerHtml : footHtml
	 			
	 	})
		$('#disableModal').modal('show');
	}else{
		util.tipAlert({message:'不能操作系统管理员',type:2});
	}	
}
function Dis(id){
	$.ajax({
		url:util.WEB_SERVER_PATH+"/user/ajax/off",
		type:'post',
		dataType:'json',
		data:{
			userId:id
		},
		success:function(data){
			if (data.success) {
				util.tipAlert({message:'停用成功',type:1});
				$("#table").bootstrapTable('refresh');
			} else {
				util.tipAlert({message:data.message,type:2});
			}
		},
		error:function(){
			util.tipAlert({message:"系统异常",type:0});
		}
	});
}

//停用
function enable(id,user){
	if(id!=1){
		var addHtml = "<div>确定要启用用户【"+user+"】?</div>";
	 	var footHtml='<button type="button" class="btn btn-default" data-dismiss="modal" onclick="enab('+id+')">确定</button>';
	 	footHtml+='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>';
	 	util.tipModel({
	 		title:'停用确认',
	 		id: 'enableModal',
	 		width: '400px',
			height: '60px',
	 		content : addHtml,
	 		footerHtml : footHtml
	 			
	 	})
		$('#enableModal').modal('show');
	}else{
		util.tipAlert({message:'不能操作系统管理员',type:2});
	}	
}
function enab(id){
	$.ajax({
		url:util.WEB_SERVER_PATH+"/user/ajax/on",
		type:'post',
		dataType:'json',
		data:{
			userId:id
		},
		success:function(data){
			if (data.success) {
				util.tipAlert({message:'启用成功',type:1});
				$("#table").bootstrapTable('refresh');
			} else {
				util.tipAlert({message:data.message,type:2});
			}
		},
		error:function(){
			util.tipAlert({message:"系统异常",type:0});
		}
	});
}
//离职
function leaveOffice(id,user,type){
	if(id!=1){
		if(type==0){
			var addHtml = "<div>确定要离职用户【"+user+"】?</div>";
		 	var footHtml='<button type="button" class="btn btn-default" data-dismiss="modal" onclick="leaveOfficeSave('+id+')">确定</button>';
		 	footHtml+='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>';
		 	util.tipModel({
		 		title:'离职确认',
		 		id: 'disableModal',
		 		width: '400px',
				height: '60px',
		 		content : addHtml,
		 		footerHtml : footHtml
		 	})
		}
		
		$('#disableModal').modal('show');
	}else{
		util.tipAlert({message:'不能操作系统管理员',type:2});
	}	
}
function leaveOfficeSave(id){
	$.ajax({
		url:util.WEB_SERVER_PATH+"/user/ajax/resignation",
		type:'post',
		dataType:'json',
		data:{
			userId:id
		},
		success:function(data){
			if (data.success) {
				util.tipAlert({message:'用户已离职',type:1});
				$("#table").bootstrapTable('refresh');
			} else {
				util.tipAlert({message:data.message,type:2});
			}
		},
		error:function(){
			util.tipAlert({message:"系统异常",type:0});
		}
	});	
}
//删除
function delConfirm(id,username){
	if(id!=1){
		var addHtml ="";
		addHtml = "<div>确定要删除用户【"+username+"】？</div>";
	 	var footHtml='<button type="button" class="btn btn-default" data-dismiss="modal" onclick="delUser('+id+')">确定</button>';
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
		util.tipAlert({message:'不能操作系统管理员',type:2});
	}	
}
function delUser(id){
	$.ajax({
		url:util.WEB_SERVER_PATH+"/user/ajax/delete",
		type:'post',
		dataType:'json',
		data:{
			userId:id
		},
		success:function(data){
			if (data.success) {
				util.tipAlert({message:'用户已删除',type:1});
				$("#table").bootstrapTable('refresh');
			} else {
				util.tipAlert({message:data.message,type:2});
			}
		},
		error:function(){
			util.tipAlert({message:"系统异常",type:0});
			
		}
	});	
}
//重置密码
function resetPw(id,username){
	if(id!=1){
		var addHtml ="";
		addHtml = "<div>【"+username+"】确定要重置密码吗？</div>";
	 	var footHtml='<button type="button" class="btn btn-default" data-dismiss="modal" onclick="resetPwS('+id+')">确定</button>';
	 	footHtml+='<button type="button" class="btn btn-default" data-dismiss="modal">取消</button>';
	 	util.tipModel({
	 		title:'重置密码确认',
	 		id: 'resetModal',
	 		width: '400px',
			height: '60px',
	 		content : addHtml,
	 		footerHtml : footHtml
	 			
	 	})
		$('#resetModal').modal('show');
	}else{
		util.tipAlert({message:'不能操作系统管理员',type:2});
	}	
}
function resetPwS(id){
	$.ajax({
		url:util.WEB_SERVER_PATH+"/user/ajax/reset",
		type:'post',
		dataType:'json',
		data:{
			userId:id
		},
		success:function(data){
			if (data.success) {
				var addHtml ="";
				var footHtml ="";
				addHtml = "<div>生成的随机6位密码是："+data.result+"</div>";
			 	footHtml+='<button type="button" class="btn btn-default" data-dismiss="modal">关闭</button>';
				util.tipModel({
			 		title:'密码提示',
			 		id: 'resetTipModal',
			 		width: '400px',
					height: '60px',
			 		content : addHtml,
			 		footerHtml : footHtml
			 	})
				$('#resetTipModal').modal('show');
			} else {
				util.tipAlert({message:data.message,type:2});
			}
		},
		error:function(){
			util.tipAlert({message:"系统异常",type:0});
		}
	});		
	
}

