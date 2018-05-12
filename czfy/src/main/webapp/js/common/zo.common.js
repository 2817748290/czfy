var FAIL_STATUS = 0;
var SUCCESS_STATUS = 1;
var INVALID_STATUS = -1;
var NOSUCHKEY_STATUS = 404;
var INVALID_PAGE = "/login.html"

var PRODUCER_TYPE_INDIVIDUAL = "individual";
var PRODUCER_TYPE_INSTITUTION = "institution";
	
var PRODUCER_STATE_EXAMINE = "examine";
var PRODUCER_STATE_PASSED = "pass";
var PRODUCER_STATE_REJECT = "reject";
	
var CONTENT_TYPE_TEXT = "text";
var CONTENT_TYPE_VIDEO = "video";
var CONTENT_TYPE_AUDIO = "audio";
	
var CONTENT_STATE_EDIT = "edit";
var CONTENT_STATE_DRAFT = "draft";
var CONTENT_STATE_REVIEW = "review";
var CONTENT_STATE_PUBLISH= "publish";
var CONTENT_STATE_BOUNCE= "bounce";

var BASE_URL = "http://localhost:8085/";//

function forwardTo(win,url){
	win.document.location = url;
}
function showAlert(msg){
    $("#alertModal").find(".modal-body").text(msg);
	$("#alertModal").modal("show");	
}
function hideAlert(){
    $("#alertModal").modal("hide");
}
$.getJSON = function(isAsync, url, callback) {
	return jQuery.ajax({
		type: "GET",
		url: BASE_URL + url,
		async: isAsync,
		success: function(data){
			if(data.status==INVALID_STATUS){
				if(url.indexOf("getloginpage")<0){
					showAlert(data.msg);
					setTimeout(function(){forwardTo(window,INVALID_PAGE)},1000);
				}else{
					forwardTo(window,INVALID_PAGE);
				}
			}else if(data.status==FAIL_STATUS){
				showAlert(data.msg);
			}else{
				callback(data);
			}
		}
	});
}

$.postJSON = function(data, url, callback,type) {
	return jQuery.ajax({
		type: "POST",
		//url:  url,
		url: BASE_URL + url,
		data: JSON.stringify(data),
		contentType: "application/json; charset=utf-8",
		dataType: type==null?"json":type,
        success: function(data){
			if(data.status==INVALID_STATUS){
				showAlert(data.msg);
				setTimeout(function(){forwardTo(window,INVALID_PAGE)},1000);
			}else if(data.status==FAIL_STATUS){
				showAlert(data.msg);
			}else{
				callback(data);
			}
		}
	});
}
$.fn.serializeObject = function(){
    var obj = {};
    var arr = this.serializeArray();
    $.each(arr, function() {
        if (obj[this.name] !== undefined) {
            if (!obj[this.name].push) {
                obj[this.name] = [obj[this.name]];
            }
            obj[this.name].push(this.value || '');
        } else {
            obj[this.name] = this.value || '';
        }
    });
    return obj;
}
$.fn.reset = function(){
	//$("#articleForm :input").not(":button, :submit, :reset").val("").removeAttr("checked").remove("selected");
    $(this).find(":input").not(":button, :submit, :reset").val("").removeAttr("checked").remove("selected");
}
$.fn.validateForm = function(){
    var valid = true;
	var fields = $(this).find("[validator]");
	$.each( fields, function(i, field){
		if(!$(field).validateField()){
			valid = false;
		};
	});
	return valid;
}
$.fn.validateField = function(){
	var valid = true;
	var field = $(this);
	var value = field.val();
	var validatorAttr = field.attr("validator");
	if(!validatorAttr||$.trim(validatorAttr)==""){
		return true;
	}
	var validators = validatorAttr.split(" ");
	var helpId = field.attr("aria-describedby");
	$.each( validators, function(i, validator){
		switch(validator){
		case "required":
			if(requiredValid(value)){
				hideError(field,helpId);
				valid = true;
			}else{
				showError(field,helpId,validatorMsg.required);
				valid = false;
			};
			break;
		case "cellphone":
			if(cellphoneValid(value)){
				hideError(field,helpId);
				valid = true;
			}else{
				showError(field,helpId,validatorMsg.cellphone);
				valid = false;
			};
			break;
		case  (validator.match(/^length/) || {}).input:
			if(lengthValid(value,validator)){
				hideError(field,helpId);
				valid = true;
			}else{
				showError(field,helpId,validatorMsg.length);
				valid = false;
			};
			break;
		case "nospecial":
			if(noSpecialValid(value)){
				hideError(field,helpId);
				valid = true;
			}else{
				showError(field,helpId,validatorMsg.nospecial);
				valid = false;
			};
			break;
		case "idcard":
			if(idCardValid(value)){
				hideError(field,helpId);
				valid = true;
			}else{
				showError(field,helpId,validatorMsg.idcard);
				valid = false;
			};
			break;
		case "linktel":
			if(linkTelValid(value)){
				hideError(field,helpId);
				valid = true;
			}else{
				showError(field,helpId,validatorMsg.linktel);
				valid = false;
			};
			break;
		case "email":
			if(emailValid(value)){
				hideError(field,helpId);
				valid = true;
			}else{
				showError(field,helpId,validatorMsg.email);
				valid = false;
			};
			break;
		case "agree":
			if(field.is(':checked')){
				hideError(field,helpId);
				valid = true;
			}else{
				showError(field,helpId,validatorMsg.agree);
				valid = false;
			};
			break;
		case "district":
			if(districtValid(value)){
				hideError(field,helpId);
				valid = true;
			}else{
				showError(field,helpId,validatorMsg.district);
				valid = false;
			};
			break;
		case "verifycode":
			if(verifycodeValid(value)){
				hideError(field,helpId);
				valid = true;
			}else{
				showError(field,helpId,validatorMsg.verifycode);
				valid = false;
			};
			break;
		}
		
		return valid;
	});
	return valid;
}
var validatorMsg = {
	required:"您输入的字段内容不能为空!",
	cellphone:"请输入正确的手机号!",
	equalto:"两次输入密码不一致!",
	length:"您输入的字段内容不符合长度规范!",
	nospecial:"您输入的字段内容含有非法字符!",
	idcard:"您输入的身份证号码格式不正确!",
	linktel:"您输入的联系电话格式不正确!",
	agree:"您需要选择同意!",
	email:"您输入的邮箱格式不正确!",
	district:"您需要选择“省/市/区”所在地!",
	verifycode:"您输入的验证码格式不正确!"
}
function equal(v1,v2){
	return v1==v2;
}
function requiredValid(v){
	return v!==""&&v!=null;
}
function verifycodeValid(v){
	return v.length == 6 &&/^[0-9]*$/.test(v);
}
function cellphoneValid(v){
	return /^1[3|4|5|7|8|9]\d{9}$/.test(v);
}
function lengthValid(v,validator){
	var reg =  /.*\[(.*)\]/;
	var lenStrs = validator.match(reg);
	var lenArr = lenStrs[1].split(",");
	if(v!=null&&v.length>=lenArr[0]&&v.length<=lenArr[1]){
		return true;
	}
	return false;
}
function noSpecialValid(v){
	return !/[`~!@#$%^&*()_+<>?:"{},.\/;'[\]]/im.test(v);
}
function linkTelValid(v){
	return /^((\(\d{2,3}\))|(\d{3}\-))?(\(0\d{2,3}\)|0\d{2,3}-)?[1-9]\d{6,7}(\-\d{1,4})?$/i
           .test(v)|| /^(13|15|18)\d{9}$/i.test(v);
}
function emailValid(v){
	return /^([a-zA-Z0-9_-])+@([a-zA-Z0-9_-])+(.[a-zA-Z0-9_-])+/.test(v);
}
function districtValid(v){
	console.log('3');
	if(v!=null){
		var region = v.split("/");
		if(region.length==3){
			return true;
		}
	}
	return false;
}
function idCardValid(v) {
    v = v.toUpperCase();
    //身份证号码为15位或者18位，15位时全为数字，18位前17位为数字，最后一位是校验位，可能为数字或字符X。
    if (!(/(^\d{15}$)|(^\d{17}([0-9]|X)$)/.test(v))) {
        return false;
    }
    //校验位按照ISO 7064:1983.MOD 11-2的规定生成，X可以认为是数字10。
    //下面分别分析出生日期和校验位
    var len, re;
    len = v.length;
    if (len == 15) {
        re = new RegExp(
            /^(\d{6})(\d{2})(\d{2})(\d{2})(\d{3})$/);
        var arrSplit = v.match(re);
        //检查生日日期是否正确
        var dtmBirth = new Date('19' + arrSplit[2] + '/' + arrSplit[3] + '/' + arrSplit[4]);
        var bGoodDay;
        bGoodDay = (dtmBirth.getYear() == Number(arrSplit[2])) && ((dtmBirth.getMonth() + 1) == Number(arrSplit[3])) && (dtmBirth.getDate() == Number(arrSplit[4]));
        if (!bGoodDay) {
            return false;
        } else {
            //将15位身份证转成18位
            //校验位按照ISO 7064:1983.MOD 11-2的规定生成，X可以认为是数字10。
            var arrInt = new Array(7, 9, 10, 5, 8, 4,
                2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2);
            var arrCh = new Array('1', '0', 'X', '9',
                '8', '7', '6', '5', '4', '3', '2');
            var nTemp = 0,
                i;
            v = v.substr(0, 6) + '19' + v.substr(6, v.length - 6);
            for (i = 0; i < 17; i++) {
                nTemp += v.substr(i, 1) * arrInt[i];
            }
            v += arrCh[nTemp % 11];
            return true;
        }
    }
    if (len == 18) {
        re = new RegExp(
            /^(\d{6})(\d{4})(\d{2})(\d{2})(\d{3})([0-9]|X)$/);
        var arrSplit = v.match(re);

        //检查生日日期是否正确
        var dtmBirth = new Date(arrSplit[2] + "/" + arrSplit[3] + "/" + arrSplit[4]);
        var bGoodDay;
        bGoodDay = (dtmBirth.getFullYear() == Number(arrSplit[2])) && ((dtmBirth.getMonth() + 1) == Number(arrSplit[3])) && (dtmBirth.getDate() == Number(arrSplit[4]));
        if (!bGoodDay) {
            return false;
        } else {
            //检验18位身份证的校验码是否正确。
            //校验位按照ISO 7064:1983.MOD 11-2的规定生成，X可以认为是数字10。
            var valnum;
            var arrInt = new Array(7, 9, 10, 5, 8, 4,
                2, 1, 6, 3, 7, 9, 10, 5, 8, 4, 2);
            var arrCh = new Array('1', '0', 'X', '9',
                '8', '7', '6', '5', '4', '3', '2');
            var nTemp = 0,
                i;
            for (i = 0; i < 17; i++) {
                nTemp += v.substr(i, 1) * arrInt[i];
            }
            valnum = arrCh[nTemp % 11];
            if (valnum != v.substr(17, 1)) {
                return false;
            }
            return true;
        }
    }
    return false;
}
function showError(field,helpId,msg){
	field.parentsUntil(".form-group").removeClass("has-success").addClass("has-error");
	$("#"+helpId).text(msg);
	field.focus();
}
function hideError(field,helpId){
	field.parentsUntil(".form-group").removeClass("has-error").addClass("has-success");
	$("#"+helpId).empty();
}

function initFileUploader($obj,url,maxFileSize,maxNumberOfFiles,allowed,data) {
    $obj.ssi_uploader({
        url:url,
		data:data||'',
        ajaxOptions:{type:'POST'},
        maxNumberOfFiles:maxNumberOfFiles || 6,
        maxFileSize:maxFileSize || 6,
        allowed:allowed || ['jpg','png']
    });
}

function initStep($obj,title){
    $obj.empty();
    $obj.step({
        index: 0,
        time: 500,
        title: title
    });
}