$(function(){
	$("#username").focus();
	$("#submit").click($submmit);
	$("#captcha").keydown(function(event) {
		if (event.keyCode == 13) {
			$submmit();
		}
	});
	$(".mycaptchaimg").css({'cursor':'pointer'});
	$(".captcha_a").click(refulshCaptcha);
	
})
$(window).resize(function() {
	var len=$("#login_body").width();
	if(len<1080){
		$("#login_body").css({"minWidth":"1280px","backgroundSize":"cover"})
	}
		 
});
function $submmit() {
	$(".dltx").show();
	$("#submit").hide();
	var username = $("#username").val();
	var password = $("#password").val();
	var captcha = $("#captcha").val();
	if (!username) {
		errorShow("请输入用户名");
		return;
	}
	if (!password) {
		errorShow("请输入密码");
		return;
	}
	password = hex_md5(password);
	if (!captcha) {
		errorShow("请输入验证码");
		return;
	}

	$.ajax({
		url :"/mrp/login",
		type : 'post',
		dataType : 'json',
		data : {
			username : username,
			password : password,
			captcha : captcha
		},
		success : function(data) {
			if (data.success == true) {
				var userId = data.result.userId;
				var nickname = data.result.nickname;
				var rolesStr = data.result.rolesStr;
				if(!window.localStorage){
					return false;
				}else{
					localStorage.setItem(`userId`, userId);
					localStorage.setItem(`nickname`, nickname);
					localStorage.setItem(`rolesStr`, rolesStr);
				}
				location.href = "/mrp/";
			} else {
				refulshCaptcha();
				errorShow(data.message);
			}
		},
		error : function() {
			refulshCaptcha();
			errorShow("系统错误");
		}
	});
}

function refulshCaptcha() {
	$(".mycaptchaimg").attr("src",'/mrp/captcha?type=login&_=' + new Date().getTime());
}

function errorShow(error) {
	$(".dltx").hide();
	$("#submit").show();
	$(".errorsdiv").show();
	$("#errorspan").html(error);
	$("#errorspan").css("color", "red");
}
