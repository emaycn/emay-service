var twoLeftArr=[];//二级左侧导航
// 实例化工具类
var util = new Utility()
// 初始化缓存页签对象
var initCacheTags = JSON.parse(localStorage.getItem('mrpPageTag'+localStorage.getItem("userId"))) 
var cacheMrpPageTags = initCacheTags === null?{}:initCacheTags
// 登出存储页签阀值
var saveMrpPageTagToggle = false
//请求成功后触发
// $.ajaxSetup({
// 	complete: function (xhr, status) {
// 		if(xhr.responseText == undefined){
// 			return;
// 		}
// 		var res = eval('('+xhr.responseText+')');
// 		if(res == undefined || res.code == undefined){
// 			return;
// 		}
// 		if(res.code == '-222'){
// 			util.tipAlert({message:'离开太久，系统已经登出，请重新登陆',type:2});
// 			window.top.open(util.WEB_SERVER_PATH + "/login","_self")
// 		}
// 	}
// })
$(function(){
	//计算内容区域高度
	var menuSideBar=$("#menuSideBar").height();
	var calcHeight = function(){
		var leftHeght=$(window).height();
	    $(".leftNav").height(leftHeght-50);
	    $("#headPage").height(leftHeght);
        $('#mainFrameTabs').height(leftHeght);
	    $('#mainFrameTabs .tab-content').height(leftHeght-49-16);
	    $('#mainFrameTabs .tab-content').css("marginTop","16px");
		//左右滑动
		var totalWidth=0;
	    $('ul.ui-sortable li').each(function(i,ele){ 
	        totalWidth+=parseInt($(this).width())+32                    
	    })
	    //ul的宽度                
	    var talTitleWidth=parseInt($('div.navContent').width());
	    if(totalWidth > talTitleWidth){     
	        var getWidth = talTitleWidth-totalWidth                    
	        $('.tabsNav .glyphicon-chevron-left,.tabsNav .glyphicon-chevron-right').css({display:"block"});   
	    }else{
	        $('.tabsNav .glyphicon-chevron-left,.tabsNav .glyphicon-chevron-right').css({display:"none"});
	        $('.tabsNav .ui-sortable').animate({left : "0px"});
	    } 
	};

	//页签初始化
	$('#mainFrameTabs').bTabs({
        loginUrl :window.location.pathname +'index.html',
		resize : calcHeight
	});

	var leftNavH=$(window).height();
	$("#headPage").height(leftNavH);
	$("#headPage").css("margin","0px");
	
	/*加载左侧导航*/
	var userId=localStorage.getItem(`userId`);
	getoneNav(userId);
	
})
/* 排序 */
function sortNavObj(navArr){
	navArr || (navArr=[]) 
	for(i=0;i<navArr.length-1;i++){
        for(j=0;j<navArr.length-1-i;j++){
            if(navArr[j].index>navArr[j+1].index){
                var temp=navArr[j];
                navArr[j]=navArr[j+1];
                navArr[j+1]=temp;
            }
        }
    }
    return navArr;
}
// 存储权限
var authCodeArr=[];
var cachePageIdArray = []
function getAuth(leftNavList){
	for(var j in leftNavList){
		var twoPages=leftNavList[j].pages;
		for(var z in twoPages){
			var opers=twoPages[z].opers;
			cachePageIdArray.push("bTabs_bTabs_tab"+twoPages[z].id)
			if(opers!=null){
				for(var q in opers){
					var authCode=opers[q].authCode;
					authCodeArr.push(authCode);
				}
			}
		}
	}
}
/* 加载左侧一级菜单 */
function getoneNav(userId){
	$.ajax({
  		url:util.WEB_SERVER_PATH + "/auth/ajax/list",
  		type : "post",
  		dataType : "json",
  		/*async:false,*/
  		data : {
  			userId:userId
  		},
  		success:function(data) {
  			var oneNavlist=data.result;
  			var oneHtml="";
  			var oneNameLeft="";
  			oneNavlist = sortNavObj(oneNavlist);
  			for(var i in oneNavlist){
  				var twoNavlist=oneNavlist[i].moduleDTOList;
  				var oneName=oneNavlist[i].name;
  				oneNameLeft=oneNavlist[0].name;
  				var icon=oneNavlist[i].icon;
  				oneHtml+='<li id="left_tab'+i+'" onClick="javascript:switchTab(\'left_tab'+i+'\','+i+',\''+oneName+'\',this);" title="'+oneName+'"><i class="'+icon+'"></i></li>';
  				twoLeftArr.push(twoNavlist);
  				getAuth(twoNavlist)
  			}
  			// 导航加载完成后打开页签
  			navOnLoadOpenTag()
  			$("#headPage").html(oneHtml);
  			$("#headPage li").eq(0).addClass("selected");
  			switchTab("left_tab0",0,oneNameLeft,null);
  		},
		error : function() {
			util.tipAlert({message:"系统异常",type:0});
		}
	})
}
/* 加载左侧二级菜单 */
function switchTab(tabid,num,oneName,obj){
	var $this = $("#"+tabid)
	//点击一级菜单效果展示
    var index = $this.index();
    $this.find('i').css('color', '#044599');
    $this.css({background:'#fff'});
    $this.find('a').css({color:"#000"})
    $('#nav_module').find('i').css('color', '#ffffff');
    $('#headPage li').each(function(i, ele){    
        $(ele).removeClass("selected");      
        if( i!=index ){     
            $(ele).find('i').css('color', '#ffffff');
            $(ele).css({background:'#044599'});
            $(ele).find('a').css({color:"#fff"});
        }
	});
	// 一级菜单点击移动到导航顶部
//    $this.parent().prepend($this.clone(true))
//	$this.remove()
	/*二级菜单*/
	var leftNavList=twoLeftArr[num];
	var twoHtml='<ul id="main-nav" class="nav nav-tabs nav-stacked">';
	twoHtml+='<li class="nav_head"><a href="#systemSetting" class="nav-header collapsed"><span>'+oneName+'</span></a></li>';
	leftNavList = sortNavObj(leftNavList);
	for(var j in leftNavList){
		var twoName=leftNavList[j].name;
		var twoPages=leftNavList[j].pages;
		var icon=leftNavList[j].icon;
		twoHtml+='<li><a class="nav-header navbar"><i class="'+icon+'"></i>'+twoName+'<span class="icon-up-micro"></span></a>';
		twoHtml+='<ul class="nav nav-list secondmenu" style=" overflow: hidden; height:0px;">';
		twoPages = sortNavObj(twoPages);
		for(var z in twoPages){
			var id=twoPages[z].id;
			var name=twoPages[z].name;
			var pageUrl=twoPages[z].pageUrl;
			var opers=twoPages[z].opers;
			twoHtml+='<li data-mid="bTabs_tab'+id+'" data-funurl="'+window.location.pathname+pageUrl+'"><a onclick="cacheMrpPageTagHandler(\''+pageUrl+'\','+id+',\''+name+'\')" href="javascript:;"><i class="icon-th-thumb"></i>'+name+'</a></li>';
		}
		twoHtml+='</ul></li>';
	}
	twoHtml+='</ul>';
	$("#menuSideBar").html(twoHtml);
	/*点击二级菜单*/
	var firstLen=$(".navbar").eq(0).parent().find('.nav-list li').length*40;
	$(".navbar").eq(0).parent().find('.nav-list').animate({'height' :firstLen+'px'});
	$(".navbar").eq(0).find("span").removeClass('icon-up-micro').addClass('icon-down-micro');	
	$(".navbar").eq(0).addClass('a');
	//$(".nav-list a").eq(0).addClass("active");
	$(".navbar").click(function(){
		if($(this).hasClass('a')){
			$('.nav-list').animate({'height':'0px'});
			$(".navbar").removeClass('a');
			$(".navbar").find("span").removeClass('icon-down-micro').addClass('icon-up-micro');
			$(this).find("span").removeClass('icon-down-micro').addClass('icon-up-micro');
		}else{
			$(".navbar").removeClass('a');
			$(this).addClass('a');
			$('.nav-list').animate({'height':'0px'});
			var len=$(this).parent().find('.nav-list li').length*40;
			$(this).parent().find('.nav-list').animate({'height' : len+'px'});
			$(".navbar").find("span").removeClass('icon-down-micro').addClass('icon-up-micro');
			$(this).find("span").removeClass('icon-up-micro').addClass('icon-down-micro');	
		}
	})
	$(".nav-list a").click(function(){
		$(".nav-list a").removeClass("active");
		$(this).addClass("active");
	})
	//左侧菜单点击$('a',$('#menuSideBar'))
    $('a',$('#menuSideBar')).on('click', function(e) {	
    	e.stopPropagation();
    	var li = $(this).closest('li');
    	var menuId = $(li).attr('data-mid');
        var url = $(li).attr('data-funurl'); 
        var title = $(this).text();
    	$('#mainFrameTabs').bTabsAdd(menuId,title,url);
    	//左右滑动
    	var totalWidth=0;
        $('ul.ui-sortable li').each(function(i,ele){ 
            totalWidth+=parseInt($(this).width())+5;                    
        })
        //计算ul的宽度                
        var talTitleWidth=parseInt($('div.navContent').width());
        if(totalWidth > talTitleWidth){     
            var getWidth = talTitleWidth-totalWidth;                   
            $('.tabsNav .ui-sortable').animate({left : getWidth +"px"});
	        $('.tabsNav .glyphicon-chevron-left,.tabsNav .glyphicon-chevron-right').css({display:"block"});   
	    }else{
	        $('.tabsNav .glyphicon-chevron-left,.tabsNav .glyphicon-chevron-right').css({display:"none"});
        } 
    });
 
	//左侧划入划出
	var leftHeight=0
	$(".secondmenu li").each(function(i,ele){ 
		leftHeight+=parseInt($(this).height());                  
	 })
	 
} 
//是否有按钮权限方法
function hasAuths(str){
	if($.inArray(str,authCodeArr)!==-1){
		return true;
	}else{
		return false;
	}
}

// 获取页面id
function getPageId() {
	return $(".navContent", window.parent.document).find("li[class='active']").find('a').attr("href").split("tab")[1];
}

//tab页向左向右移动--点击上一个
$('.tabsNav #leftPage').click(function(){
	$('.tabsNav #rightPage').css({display:"block"});
    var strLeft=$('.tabsNav .ui-sortable').css('left');
    var iLeft = parseInt(strLeft.replace('px', ''));
    if(iLeft>=0){ 
    	$('.tabsNav #leftPage').css({display:"none"});
        return;
    }else{
        var totalWidth=0;
        var lis = $(".tabsNav .ui-sortable li");
        for(var i=0;i<lis.length;i++){
            var item = lis[i];
            totalWidth-= $(item).width()+5;
            if(iLeft>totalWidth){
                iLeft+=$(item).width()+5;
                break;
            }
        };
        if(iLeft>0){iLeft=0;}
        $(".tabsNav .ui-sortable").animate({left : iLeft + 'px'});
    }
});
//点击下一个
$('.tabsNav #rightPage').click(function(){
	$('.tabsNav #leftPage').css({display:"block"});
    var strLeft=$('.tabsNav .ui-sortable').css('left');
    var iLeft = parseInt(strLeft.replace('px', ''));
    var totalWidth=0;
    $.each($(".tabsNav .ui-sortable li"),function(key, item){
        totalWidth+= $(item).width()+5;
    });
    var tabsWidth = $("div.navContent").width();
    if(totalWidth>tabsWidth){
    	if(totalWidth-tabsWidth<=Math.abs(iLeft)){
        	$('.tabsNav #rightPage').css({display:"none"})
            return;
        }
    	
        var lis = $(".tabsNav .ui-sortable li");
        totalWidth=0;
        for(var i=0;i<lis.length;i++){
            var item = lis[i];
            totalWidth-= $(item).width()+5;
            if(iLeft>totalWidth){
                iLeft-=$(item).width()+5;
                break;
            }
        };
        $(".tabsNav .ui-sortable").animate({left : iLeft + 'px'});
    }            
});
//显示隐藏左侧菜单栏
$("#show_hide_btn").click(function() {       
    switchSysBar();
});
function switchSysBar(flag){
    var side = $('.navbar-side');
    var menuSideBar = $('#menuSideBar');
    if( flag==true ){	
        side.css({width:'241px'});
        menuSideBar.show(500, 'linear');
        $('.bTabs').css({marginLeft:'251px'});    
    }else{
        if ( menuSideBar.is(":visible") ) {
        	menuSideBar.hide(100, 'linear');
        	$("#header .header_left").hide(100, 'linear');
            side.css({width:'40px'});
            $("#logo").css({textAlign: "center"});
            $("#header #logo").html('<i id="show_hide_btn" onclick="switchSysBar()" class="icon-indent-right" style="line-height:45px;"></i>');
            $(".bTabs").animate({marginLeft:'50px'},500);
        } else {
        	$("#header .header_left").show(500, 'linear');
        	menuSideBar.show(500, 'linear');
            side.css({width:'241px'});
            $("#header #logo").html('');
            $(".bTabs").animate({marginLeft:'251px'},350);
        }
    }
}
//退出登录
function logout(){
	$.ajax({
		url:util.WEB_SERVER_PATH+"/logout?_=" + (new Date()).getTime(),
		type : 'get',
		dataType : 'json',
		success : function(data) {
			saveMrpPageTagToggle = true
			localStorage.setItem('mrpPageTag'+localStorage.getItem("userId"), JSON.stringify(cacheMrpPageTags))
			localStorage.removeItem(`userId`);
			localStorage.removeItem(`nickname`);
			localStorage.removeItem(`rolesStr`);
			window.top.open(util.WEB_SERVER_PATH +"/login","_self")
		},
		error : function() {
			window.top.open(util.WEB_SERVER_PATH +"/login","_self")
		}
	})
}

// 切换tab iframe子页面调用
function changeTab(url){
	var currentDom = $("[data-funurl='"+url+"']");
	$("[title='流程中心']").click()
	currentDom = $("[data-funurl='"+url+"']");
	currentDom.children("a").addClass("active")
	var data = currentDom.data();
	var title = currentDom.text();
	$('#mainFrameTabs').bTabsAdd(data.mid,title,url);
} 
// 将tab标签默认为首页
function cacheMrpPageTagsActiveSet(){
	for(var item in cacheMrpPageTags){
		cacheMrpPageTags[item]['active'] = 'false'
	}
	$("title").text("EMRP-亿美软通")
}
// 存储页签
function cacheMrpPageTagHandler(url,id,name){
	cacheMrpPageTagsActiveSet()
	cacheMrpPageTags['bTabs_bTabs_tab'+id] = {
			url: url,
			id: 'bTabs_tab'+id,
			title: name,
			active: 'true'
	}
	$("title").text("EMRP-"+name)
}
// tab签替换
$('ul.nav-tabs',$("#mainFrameTabs")).on('click','a',function(e){
	var id = $(this).attr('href').replace('#', '');
	cacheMrpPageTagsActiveSet()
	cacheMrpPageTags[id] !== undefined && (cacheMrpPageTags[id]['active'] = "true",$("title").text("EMRP-"+cacheMrpPageTags[id].title))
});
// 删除页签
$('ul.nav-tabs',$("#mainFrameTabs")).on('click','button',function(e){
	var id = $(this).parent().attr('href').replace('#', '');
	delete cacheMrpPageTags[id]
	setTimeout(function(){
		var previd = $("[aria-expanded=true]").attr('href').replace('#', '')
		cacheMrpPageTagsActiveSet()
		cacheMrpPageTags[previd] !== undefined && (cacheMrpPageTags[previd]['active'] = "true",$("title").text("EMRP-"+cacheMrpPageTags[previd].title))
	},10)
});
// 页签存储
window.onunload = function(){
	// 当是登出刷新时不存储页签
	if(saveMrpPageTagToggle){
		return
	}
    localStorage.setItem('mrpPageTag'+localStorage.getItem("userId"), JSON.stringify(cacheMrpPageTags))
}
// 导航加载完成后打开页签
function navOnLoadOpenTag(){
	var mrpPageTages = JSON.parse(localStorage.getItem("mrpPageTag"+localStorage.getItem("userId")))
	var activeTag = {}
	for(var item in mrpPageTages){
		// 失效页面过滤
		if($.inArray(item,cachePageIdArray) === -1){
			delete mrpPageTages[item]
			continue
		}
		var value = mrpPageTages[item]
		// 打开页签
		$('#mainFrameTabs').bTabsAdd(value.id,value.title,value.url)
		if(value.active === 'true'){
			activeTag = value
		}
	}
	activeTag.active === 'true' && ($('#mainFrameTabs').bTabsAdd(activeTag.id,activeTag.title,activeTag.url),$("title").text("EMRP-"+activeTag.title))
	activeTag.active === undefined && ($('#mainFrameTabs [href=#bTabs_tab111]')[0].click())
}
// 子页面404删除页签  iframe子页面调用方法
function delTagMethod(){
	$("[aria-expanded=true] button")[0].click()
}
// 欢迎您
$("#welcome").text(localStorage.getItem("nickname"))