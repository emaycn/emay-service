// 公共工具类
function Utility() {
    /**
     * @author 蒋春明
     * @constant  {String} WEB_SERVER_PATH
     * @desc 数据服务host
     */
    this.WEB_SERVER_PATH = "http://" + window.location.host + "/mrp"
    /**
     * 获取文件名称后缀
     * @author 蒋春明
     * @method fileNamePostfix
     * @param {String} filename 文件名称或者文件路径
     * @return {String} 文件名称后缀
     */
    this.fileNamePostfix = function (filename) {
        var index1 = filename.lastIndexOf(".");
        var index2 = filename.length;
        return filename.substring(index1, index2);
    }
    /**
     * 区间时间控件
     * @author 蒋春明
     * @method dateSection
     * @param {String} idSelector 时间控件选择器
     * @param {Object} conf 时间控件配置对象
     * @param {Function} func(start,end,label) 控件回调方法拥有三个参数
     */
    this.dateSection = function (idSelector, conf, func) {
        idSelector || (idSelector = '[data-timedaterangepicker=2]')
        var confData = {
            timePicker: true,
            timePicker24Hour: true,
            linkedCalendars: false,
            autoUpdateInput: false,
            timePickerSeconds: true,
            minDate: '2000',
            maxDate: '2100',
            // 月份和年是下拉框
            showDropdowns: true,
            locale: {
                format: 'YYYY-MM-DD HH:mm:ss',
                separator: ' ~ ',
                monthNames: [1,2,3,4,5,6,7,8,9,10,11,12],
                daysOfWeek: ["日", "一", "二", "三", "四", "五", "六"],
                applyLabel: "应用",
                cancelLabel: "取消",
                resetLabel: "重置",
            }
        }
        conf && conf.locale && (confData.locale = Object.assign(confData.locale, conf.locale),delete conf.locale)
        conf && (confData = Object.assign(confData, conf))
        if (func === null) {
            func = function (start, end, label) {
                // 开始时间 this.startDate.format(this.locale.format)
                // 结束时间 this.endDate.format(this.locale.format)
                if (!this.startDate) {
                    this.element.val('');
                } else {
                    this.element.val(this.startDate.format(this.locale.format) + this.locale.separator + this.endDate.format(this.locale.format));
                }
            }
        }
        $(idSelector).daterangepicker(confData, func);
    }
    /**
     * 单体时间控件
     * @author 蒋春明
     * @method dateMonomer
     * @param {String} idSelector 时间控件选择器
     * @param {Object} conf 时间控件配置对象
     * @param {Function} func(start,end,label) 控件回调方法拥有三个参数
     */
    this.dateMonomer = function (idSelector, conf, func) {
        idSelector || (idSelector = '[data-timedaterangepicker=1]')
        var confData = {
        	singleDatePicker: true,
            timePicker: true,
            timePicker24Hour: true,
            linkedCalendars: false,
            autoUpdateInput: false,
            minDate: '2000',
            maxDate: '2100',
            // 月份和年是下拉框
            showDropdowns: true,
            timePickerSeconds: true,
            locale: {
                format: 'YYYY-MM-DD HH:mm:ss',
                monthNames: [1,2,3,4,5,6,7,8,9,10,11,12],
                daysOfWeek: ["日", "一", "二", "三", "四", "五", "六"],
                applyLabel: "应用",
                cancelLabel: "取消",
                resetLabel: "重置",
            }
        }
        conf && conf.locale && (confData.locale = Object.assign(confData.locale, conf.locale),delete conf.locale)
        conf && (confData = Object.assign(confData, conf))
        if (func === null) {
            func = function (start, end, label) {
                // 开始时间 this.startDate.format(this.locale.format)
                if (!this.startDate) {
                    this.element.val('');
                } else {
                    this.element.val(this.startDate.format(this.locale.format));
                }
            }
        }
        $(idSelector).daterangepicker(confData, func);
    }
    /**
     * 时间格式转换
     * @author 蒋春明
     * @method formatDate
     * @param {Date} date 时间串 or 时间戳
     * @return {Object} 时间格式对象
     */
    this.formatDate = function (date) {
        var yymmddhhssmm = new Date(date).toLocaleString('chinese', {
            hour12: false
        }).replace(/\//g, "-")
        return {
            // "2018-10-24 16:26:18"
            yymmddhhssmm: yymmddhhssmm
        }
    }
    /**
     * 提示框 0:警示 1:成功 2:警告 3:信息
     * @author 蒋春明
     * @method tipAlert
     * @return {Object} option 提示框配置对象
     */
    this.tipAlert = function (option) {
        var defaultAlert = {
            elemId: 'alert' + new Date().getTime(),
            message: '操作成功',
            time: 0.5,
            type: 1,
            width: 200
        }
        var htmlAlert = ""
        if (option) {
            Object.assign(defaultAlert, option)
        }
        var alertClass = 'alert-success'
        var alertIconClass = 'icon-ok'
        switch (defaultAlert.type) {
            case 0:
                alertClass = 'alert-danger';
                alertIconClass = 'icon-cancel'
                break
            case 1:
                alertClass = 'alert-success';
                alertIconClass = 'icon-ok'
                break
            case 2:
                alertClass = 'alert-warning';
                alertIconClass = 'icon-attention-alt'
                break
            case 3:
                alertClass = 'alert-info';
                alertIconClass = 'icon-info'
                break
        }
        htmlAlert = `<div style="min-width:${defaultAlert.width}px;position: fixed;left: 50%;top: 20%; justify-content: center;-webkit-box-pack: center; z-index: 2000;
                    transform: translateX(-50%);transition: opacity .3s,transform .4s,-webkit-transform .4s;  padding: 15px 15px 15px 20px;display: flex; 
                    -webkit-box-align: center;align-items: center;" id="${defaultAlert.elemId}" class="alert ${alertClass} in">
                    <i class="${alertIconClass}" style="color: inherit;margin-right: 10px;"></i>
                    ${defaultAlert.message}
                 </div>`
        $("body").append(htmlAlert)
        setTimeout(function () {
            $("#" + defaultAlert.elemId).fadeOut("slow", function () {
                $("#" + defaultAlert.elemId).remove()
            })
        }, defaultAlert.time * 1000)
    }
    /**
     * 模态框
     * @author 蒋春明
     * @method tipAlert
     * @return {Object} option 提示框配置对象
     */
    this.tipModel = function tipModel(option) {
        var defaultOpt = {
            title: '模态框标题',
            id: 'mymodel',
            width: '800px',
            height: 'auto',
            content: '<div>模态框内容</div>',
            footerHtml: 'footer',
            headClosebtn: false
        }
        var htmlModel = "";
        var closeBtn = "";
        if (option) {
            Object.assign(defaultOpt, option);
        }
        defaultOpt.headClosebtn && (closeBtn = '<button type="button" class="close" data-dismiss="modal" aria-hidden="true">&times;</button>');
        htmlModel = `<div class="modal fade" id="${defaultOpt.id}" tabindex="-1" data-backdrop="static" role="dialog" aria-labelledby="myModalLabel" aria-hidden="true">
		    <div class="modal-dialog" style="width:${defaultOpt.width}">
		        <div class="modal-content" style="width:${defaultOpt.width}">
		            <div class="modal-header">
		            	${closeBtn}
		                <h4 class="modal-title" id="myModalLabel">${defaultOpt.title}</h4>
		            </div>
		            <div class="modal-body" style="height:${defaultOpt.height} ;width:${defaultOpt.width}">
		            	${defaultOpt.content}
		            </div>
		            <div class="modal-footer">
		                ${defaultOpt.footerHtml}
		            </div>
		        </div>
		    </div>
		</div>`
        if ($("#" + defaultOpt.id)[0] === undefined) {
            $('body').append(htmlModel)
        } else {
            $("#" + defaultOpt.id).remove()
            $("body").append(htmlModel)
        }
    }
    // 加载动画
    this.tipLoading = function (onoff) {
        var currentWindowId = window.frameElement !== null?window.frameElement.id:"home"
        var date = new Date().getTime().toString()
        var html = `<div class="modalFade" id="tipLoading">
                        <div class="loading_main">
                            <span></span>
                            <span></span>
                            <span></span>
                            <span></span>
                            <span></span>
                        </div>
                    </div>`
        if(onoff){
            $("#tipLoading").length <= 0 && ($("body").append(html),$("#tipLoading").fadeIn('fast'))
            $("#tipLoading").attr("data-loading", date)
            localStorage.setItem(currentWindowId, date)
        }else{
            $("[data-loading='"+localStorage.getItem(currentWindowId)+"']").fadeOut("slow", function () {
                $(this).remove()
                localStorage.removeItem(currentWindowId);
            })
        }                
    }
    /**
     * 获取地址栏查询数据对象
     * @author 蒋春明
     * @method localSearch
     * @return {Object} option 提示框配置对象
     */
    this.localSearch = function() {
        var str = window.location.search
        if (str == undefined) return
        str = str.substr(1)
        var arr = str.split("&"),
            obj = {},
            newArr = []
        arr.map(function (value, index, arr) {
            newArr = value.split("=")
            if (newArr[0] != undefined) {
                obj[newArr[0]] = newArr[1]
            }
        })
        return obj
    }
    /**
     * tableAjax回掉函数
     * @author 蒋春明
     * @method ajaxTableSetup
     * @return {Object} response 响应数据
     */
    this.ajaxTableSetup = function(response) {
        window.length <= 0 && (util.tipLoading(false))
        if(response.code == '-222'){
			window.top.open(util.WEB_SERVER_PATH + "/login","_self")
		}
        return response.success
    }
    /**
     * 简约Ajax请求方法
     * @method ajax
     * @param {String} url 请求路径
     * @param {Object} data 请求体数据
     * @param {Function} func 请求成功后的回掉函数
     */
    this.ajax = function (url, data, func) {
        func || (func = function (response) {
        })
        $.ajax({
            type: "POST",
            url:util.WEB_SERVER_PATH + url,
            data: data,
            dataType: 'json',
            success: func,
            error: function () {
                util.tipAlert({
                    message: "系统异常",
                    type: 0
                })
            }
        });
    }
    /**
     * 分页查询列表
     * @method pageTable
     * @param {Array<Object>} columns 列表项配置
     * @param {String} field 取值key，table Id
     * @param {String} url 请求接口
     * @param {Function} paramFunc 请求查询方法，返回请求对象
     * @param {Object} config 配置对象
    */
    this.pageTable = function(columns,field,url,paramFunc,config){
        var tableConfig = {
            url: this.WEB_SERVER_PATH+url,                         //请求数据url
            method: 'post',                     //请求方式
            dataType : "json",
            contentType : "application/x-www-form-urlencoded; charset=UTF-8",    //汉化
            queryParams: paramFunc,
            sidePagination: "server",            
            pagination: true,                    //是否在表格底部显示分页组件，默认false 不显示， true 显示
            pageNumber : 1,                      //初始化加载第一页，默认第一页
            pageSize : 5,                       //每页的记录行数
            pageList: [5,10,20],          //可供选择的每页的行数
            clickToSelect: true,                //是否启用点击选中行
            search: false,                       //是否显示搜索框true 显示  false不显示
            showRefresh: false,                   //是否显示刷新按钮 默认false 不显示， true 显示
            striped: true,                       //是否显示行间隔色
            sortable: true,                      //是否启用排序 true为不启动  false 启动
            singleSelect: true, 
            responseHandler:function(res){       //从服务端请求到的数据
                if(!util.ajaxTableSetup(res)){
                    return []
                }
                return{
                    "total":res.result[field].totalCount,
                    "rows":res.result[field].list
                }
            },    	
            columns: columns
        }
        config !== undefined && (tableConfig = Object.assign(tableConfig,config))
        $('#'+field).bootstrapTable(tableConfig);
    }
    /**
     * 数字字符串格式化
     * @method pageTable
     * @param {String} str 数字，数字形式的串
     * @return {Object} 格式化后的数值
    */
    this.numberFormat = function(str){
        var comma,constant,capital
        if(str === "" || str === null || str === undefined){
            capital = constant = comma = ""
            return {
                comma: "",
                constant: "",
                capital: ""
            }
        }
        comma = constant = str
        str = str.toString()
        if(str.indexOf(",") === -1){
            var float = "",newStr = str
            str.indexOf(".") !== -1 && (float = str.substr(str.indexOf(".")),newStr = str.split(float)[0])
            if(newStr.length <= 3){ 
                comma = str
            }else{
                var body = newStr.substring(newStr.length%3).match(/[0-9]{3}/g)
                newStr.substr(0,newStr.length%3) !== "" && (body.unshift(newStr.substr(0,newStr.length%3)))
                comma = body.join(",") + float
            }
        }else{
            constant = str.replace(/,/g,"")
        }
        function changeMoneyToChinese(money){
		    var cnNums = new Array("零","壹","贰","叁","肆","伍","陆","柒","捌","玖"); //汉字的数字
		    var cnIntRadice = new Array("","拾","佰","仟"); //基本单位
		    var cnIntUnits = new Array("","万","亿","兆"); //对应整数部分扩展单位
		    var cnDecUnits = new Array("角","分","毫","厘"); //对应小数部分单位
		    var cnInteger = "整"; //整数金额时后面跟的字符
		    var cnIntLast = "元"; //整型完以后的单位
		    var maxNum = 999999999999999.9999; //最大处理的数字
		    
		    var IntegerNum; //金额整数部分
		    var DecimalNum; //金额小数部分
		    var ChineseStr=""; //输出的中文金额字符串
		    var parts; //分离金额后用的数组，预定义
		    if( money == "" ){
		        return "";
		    }
		    money = parseFloat(money);
		    if( money >= maxNum ){
		        $.alert('超出最大处理数字');
		        return "";
		    }
		    if( money == 0 ){
		        ChineseStr = cnNums[0]+cnIntLast
		        return ChineseStr;
		    }
		    money = money.toString(); //转换为字符串
		    if( money.indexOf(".") == -1 ){
		        IntegerNum = money;
		        DecimalNum = '';
		    }else{
		        parts = money.split(".");
		        IntegerNum = parts[0];
		        DecimalNum = parts[1].substr(0,4);
		    }
		    if( parseInt(IntegerNum,10) > 0 ){//获取整型部分转换
		        var zeroCount = 0;
		        var IntLen = IntegerNum.length;
		        for( i=0;i<IntLen;i++ ){
		            n = IntegerNum.substr(i,1);
		            p = IntLen - i - 1;
		            q = p / 4;
		            m = p % 4;
		            if( n == "0" ){
		                zeroCount++;
		            }else{
		                if( zeroCount > 0 ){
		                    ChineseStr += cnNums[0];
		                }
		                zeroCount = 0; //归零
		                ChineseStr += cnNums[parseInt(n)]+cnIntRadice[m];
		            }
		            if( m==0 && zeroCount<4 ){
		                ChineseStr += cnIntUnits[q];
		            }
		        }
		        ChineseStr += cnIntLast;
		        //整型部分处理完毕
		    }
		    if( DecimalNum!= '' ){//小数部分
		        var decLen = DecimalNum.length;
		        for( i=0; i<decLen; i++ ){
		            n = DecimalNum.substr(i,1);
		            if( n != '0' ){
		                ChineseStr += cnNums[Number(n)]+cnDecUnits[i];
		            }
		        }
		    }
		    if( ChineseStr == '' ){
		        ChineseStr += cnNums[0]+cnIntLast;
		    } else if( DecimalNum == '' ){
		        ChineseStr += cnInteger;
		    }
		    return ChineseStr;
        }
        capital = changeMoneyToChinese(constant)
        return {
            comma: comma,
            constant: constant,
            capital: capital
        }
    }
    /**
     * 时间添加计算
     * @method DateAdd
     * @param {String} interval 要添加的类型，例如 年、月、日
     * @param {String} number 要添加的数值
     * @param {Date} date 被添加的时间 
     * @return {Date} 返回操作后的时间
    */
    this.DateAdd = function(interval, number, date) {
	    switch (interval) {
		    case "y": {
		        date.setFullYear(date.getFullYear() + number);
		        return date;
		        break;
		    }
		    case "q": {
		        date.setMonth(date.getMonth() + number * 3);
		        return date;
		        break;
		    }
		    case "m": {
		        date.setMonth(date.getMonth() + number);
		        return date;
		        break;
		    }
		    case "w": {
		        date.setDate(date.getDate() + number * 7);
		        return date;
		        break;
		    }
		    case "d": {
		        date.setDate(date.getDate() + number);
		        return date;
		        break;
		    }
		    case "h": {
		        date.setHours(date.getHours() + number);
		        return date;
		        break;
		    }
		    case "m": {
		        date.setMinutes(date.getMinutes() + number);
		        return date;
		        break;
		    }
		    case "s": {
		        date.setSeconds(date.getSeconds() + number);
		        return date;
		        break;
		    }
		    default: {
		        date.setDate(date.getDate() + number);
		        return date;
		        break;
		    }
	    }
	}
}
//请求成功后触发
$.ajaxSetup({
    beforeSend:function () {  
        window.length <= 0 && (util.tipLoading(true))
    },
	complete: function (xhr, status) {
		window.length <= 0 && (util.tipLoading(false))
		if (typeof xhr.responseText == 'string') {
	        try {
	            var res=JSON.parse(xhr.responseText);
	            if(res.code == '-222') {
	    			util.tipAlert({message:'离开太久，系统已经登出，请重新登陆',type:2});
	    			window.top.open(util.WEB_SERVER_PATH + "/login","_self")
	    		}
	        } catch(e) {
	            return false;
	        }
	    }
    }
})
