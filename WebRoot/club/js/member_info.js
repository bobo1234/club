var member = {};	// 会员所有信息
var submitText = '提交信息失败, 请注意被标识为红色的部分!';

var moduleCode = $.getUrlParam('moduleCode');
member.id = $.getUrlParam('id');	// 会员ID
// 动态生成url, 通过获取会员编号来决定

var reg = {};	// 正则表达式
reg.date = /^(?:19|20)[0-9][0-9]-(?:(?:0[1-9])|(?:1[0-2]))$/;	// 日期验证
reg.integer = /^[0-9]*[1-9][0-9]*$/;	// 正整数大于0
reg.double = /^\d+(\.\d+)?$/;	// 小数或整数, 含0
reg.number = /^[1-9]\d*|[0]{1,1}$/;	// 正整数, 含0

function initFun() {
	alignmentFns.initialize();
	
	$(".card-type").change(function() {
		var v=$(this).val();
		if(v>0){
			var data=ajax.json.get(localhostUrl+ '/mgr/findCardById?id='+v);
			$("div.details-box h4").text($(this).find("option:selected").text());
			setValueForForm(data.body);// 遍历表单元素,循环赋值
			BootstrapDialog.showModel($('div.details-box'));
		}
	});
	
	if(!secure.find)  $('div.edit-employees-box').remove();
	if(secure.find){
		dialog = BootstrapDialog.loading();
		$('div.edit-employees-box').removeClass('hide');
		if(member.id){
			addBreadcrumb('修改会员信息');//面包屑
			$("#membercard").remove();//修改会员信息时候,不关联办卡功能
			$.findEmployeesInfo(member.id);
			return;
		}
		createSel('', 'id', 'typename', $("select.card-type"), localhostUrl+"mgr/findAllCard", "请选择卡类");
		addBreadcrumb('新增会员');//面包屑
		$(".card-type").change(function() {
			var v=$(this).val();
			if(v>0){
				$(".ctimes").val("1");
			}else
				$(".ctimes").val("0");
		});
		dialog.close();
	}
}

/*
 * jQuery 扩展
 *  
 */
(function($){
	// 验证文本框, 传入获取文本框元素的标识, 是否为空,  加正则匹配
	$.input = function(className, empty, regular){
		$(className).parent().prev().removeClass('data-empty');
		var val = $.removeTrim($(className).val());
		if(!empty) return val;
		if(empty && !regular && val) return val;
		if(regular != null && regular.test(val)) return val;
		$.isSubmit = false;
		$(className).parent().prev().addClass('data-empty');
	};
	// 验证下拉列表
	$.select = function(className, empty, regular){
		$(className).parent().prev().removeClass('data-empty');
		var val = $(className).val();
		if(!empty) return val;
		if(empty && !regular && val && val != 0) return val;
		if(regular != null && regular.test(val)) return val;
		$.isSubmit = false;
		$(className).parent().prev().addClass('data-empty');
	};
	// 获取单选按钮
	$.radio = function(className, empty, regular){
		$(className).parent().parent().prev().removeClass('data-empty');
		var val = $(className).filter(':checked').val();
		if(!empty) return val;
		if(empty && !regular && val) return val;
		if(regular != null && regular.test(val)) return val;
		$.isSubmit = false;
		$(className).parent().parent().prev().addClass('data-empty');
	};
})(jQuery);

/**
 * 验证表单数据
 * @returns
 */
function detectionForm(){
	$.isSubmit = true;	// 重置表单为可提交
	// 基本信息
	member.fullName = $.input('.realname', true, null);	// 姓名
	member.sex = $.radio('.sex', true, null);	// 性别 (女:2,男:1)
	member.phone = $.input('.phone', true, com.RegExps.isPhone);	// 手机号码
	
	member.birthday = $.input('.birthday', true, reg.date);	// 出生日期
	member.griptype = $.radio('.griptype', true, null);	// 握拍方式(直板:1,横板:2)
	member.note = $.input('.remark', false, null);	// 备注
	if(!$.isSubmit)
		BootstrapDialog.alert(submitText,BootstrapDialog.TYPE_DANGER);
	return $.isSubmit;
};

/**
 * 遍历单选或多选项
 */
function findChoose(current){
	$.getJSON('./mgr/employees/internship/findChoose', function(data){
		var educationList = $('td.educationList').empty();
		var marriageList = $('td.marriageList').empty();
		var nationalList = $('td.nationalList').empty();
		var politicListList = $('td.politicsList').empty();
		$.each(data.body.educationList, function(i,v){
			getLabelRadio(0, v.eduId, 'education', v.eduName).appendTo(educationList);
		});
		$.each(data.body.marriageList, function(i,v){
			getLabelRadio(0, v.marId, 'marriage', v.marName).appendTo(marriageList);
		});
		$.each(data.body.nationalList, function(i,v){
			getLabelRadio(0, v.natId, 'national', v.natName).appendTo(nationalList);
		});
		$.each(data.body.politicList, function(i,v){
			getLabelRadio(0, v.polId, 'politics', v.polName).appendTo(politicListList);
		});
		dialog.close();
	});
}

/**
 * 返回单选按钮
 * @param current
 * @param id
 * @param name
 * @param text
 * @returns
 */
function getLabelRadio(current, id, name, text){
	return $("<label class='label'></label>")
	.append($("<input type='radio' class='"+name+"' value='"+id+"' name='"+name+"' "+ $.findChecked(current == id) +" />"))
	.append($("<span class='label-text float-left'></span>").append(text));
}

/**
 * 修改或保存会员信息
 */
function saveOrModifyInfo(){ 
	detectionForm();
	if(!$.isSubmit) return;
	var cardtype=$(".card-type").val();
	var ctimes = $(".ctimes").val();// 是否消费
	if(cardtype==null){
		cardtype="0";
	}
	var url = !$.getUrlParam('id') ? 'addMember'+'?cardtype='+cardtype+"&ctimes="+ctimes : 'modifyMember?id='+member.id;
	dialog = BootstrapDialog.isSubmitted();
	$.ajax({
		type : 'post',
		url : localhostUrl+'/mgr/'+url,
		data : $("#addForm").serialize(),
		success : function(data) {
			dialog.close();
			if (!$.isSuccess(data))
				return;
			BootstrapDialog.showmsg(data.body, func);
//			BootstrapDialog.show({
//				title:"提示信息",
//				type:BootstrapDialog.TYPE_SUCCESS,
//				closable : true,
//				message : data.body,
//				onhidden : function(dialogRef) {
//					window.location.href = "member.html";
//				}
//			});
		}
	});
};
function func() {
	window.location.href = "member.html";
}
/**
 * 获取会员基本信息
 */
$.findEmployeesInfo = function(id){
	if(!id) return;
	$.getJSON(localhostUrl+'/mgr/findMemberById',{id:id}, function(data){
		if(!$.isSuccess(data)) return;
		var info = data.body;
		$('input.realname').val(info.realname);
		com.radioCheck($('input.sex'),info.sex);
		$('input.birthday').val(info.birthday);
		$('input.phone').val(info.phone);
		$('input.contact').val(info.contact);
		com.radioCheck($('input.griptype'),info.griptype);
		$('input.note').val(info.remark);
		dialog.close();
		
	});
};