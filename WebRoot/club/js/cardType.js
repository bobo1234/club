var moduleCode = '';
var card = {};

function initFun() {
	alignmentFns.initialize();
	if (secure.find) {
		dialog = BootstrapDialog.loading();
		findListInfo();
	}
	if (!secure.add)
		$('button.add-btn').remove();
	if (secure.add)
		$('button.add-btn').removeClass('hide');
}


/**
 * 获取列表
 */
function findListInfo() {
	var valu = $('input.search-val').val();
	$.post(localhostUrl + 'mgr/cardList', {
		searchInput : valu
	}, function(data) {
		dialog.close();
		var tbody = $('tbody.tbody').empty();
		if (!$.isSuccess(data))
			return;
		$.each(data.body, function(i, v) {
			if(v.usertimes==0){
				v.usertimes='无限';
			}
			$("<tr></tr>").append($("<td></td>").append(i + 1)).append(
					$("<td></td>").append(v.typename)).append(
					$("<td></td>").append(v.createtime)).append(
					$("<td></td>").append("￥" + v.price)).append(
					$("<td></td>").append(v.usertimes+"次")).append(
					$("<td></td>").append(v.validtime + "个月")).append(
					$("<td></td>").append(v.cardamount)).append(
					$("<td></td>").append(analyzeBtns(v))).appendTo(tbody);
		});
	}, 'json');
}

/*
 * 解析操作按钮
 * 
 */
function analyzeBtns(v) {
	var btns = "";
	btns += secure.find ? "<button type='button' class='btn btn-info btn-xs' onclick='showDetails("
			+ v.id
			+ ")'><span class='glyphicon glyphicon-menu-left'></span>详情</button>"
			: "";
	btns += secure.modify ? "<button type='button' class='btn btn-primary btn-xs' onclick='showModifyBox("
			+ v.id
			+ ")'><span class='glyphicon glyphicon-pencil'></span>编辑</button>"
			: "";
	btns += secure.del ? "<button type='button' class='btn btn-danger btn-xs' onclick='hintDelete("
			+ v.id
			+ ")'><span class='glyphicon glyphicon-remove'></span>删除</button>"
			: "";
	return btns;
}

/*
 * 显示详情
 * 
 */
function showDetails(id) {
	if (!id)
		return;
	$.getJSON(localhostUrl + '/mgr/findCardById', {
		id : id
	}, function(data) {
		if (!$.isSuccess(data))
			return;
		setValueForForm(data.body);// 遍历表单元素,循环赋值
		BootstrapDialog.showModel($('div.details-box'));
	});
}

/*
 * 显示编辑窗口
 * 
 */
function showModifyBox(id) {
	$('.empty').removeClass('empty');
	if (!id)
		return;
	dialog = BootstrapDialog.loading();
	$.getJSON(localhostUrl + 'mgr/findCardById', {
		id : id
	}, function(data) {
		dialog.close();
		if (!$.isSuccess(data))
			return;
		setValueForModifyForm(data.body);// 遍历表单元素,循环赋值
		$('.modify-describe').text(
				data.body.description != null ? data.body.description : "");
		BootstrapDialog.showModel($('div.modify-box'));
	});
}

/**
 * 赋值
 * 
 * @param json
 */
function setValueForModifyForm(json) {
	var a = $(".modify-box input");
	for (var i = 0; i < a.length; i++) {
		var iname = a[i].name;
		for ( var key in json) {
			if (iname == key) {
				a[i].value = json[key];
			}
		}
	}
}

/*
 * 修改信息
 * 
 */
function modifyCard() {
	$.isSubmit = true;
	$.verifyNumForm($('input.modify-typename'), true,null);
	$.verifyNumForm($('input.modify-validtime'), true,1);
	$.verifyNumForm($('input.modify-usertimes'), true,0);
	$.verifyNumForm($('input.modify-price'), true,0);
	if (!$.isSubmit)
		return;
	formToSerialize("modify-cardtype", localhostUrl + 'mgr/modifyCard',
			findListInfo, "div.modify-box");
}

/*
 * 显示添加窗口
 * 
 */
function showAddBox() {
	/**
	 * 重置表单
	 */
	$('.empty').removeClass('empty');
	$('input.typename').val("");
	$('input.validtime').val("1");
	$('input.usertimes').val("1");
	$('input.price').val("0");
	$('textarea.describe').val("");
	BootstrapDialog.showModel($('div.add-box'));
}

/*
 * 添加信息
 */
function addCard() {
	$.isSubmit = true;
	$.verifyNumForm($('input.typename'), true);
	$.verifyNumForm($('input.validtime'), true);
	$.verifyNumForm($('input.usertimes'), true);
	$.verifyNumForm($('input.price'), true);
	if (!$.isSubmit)
		return;
	dialog = BootstrapDialog.isSubmitted();
	$.ajax({
		type : 'post',
		url : localhostUrl + 'mgr/addCard',
		data : $("#cardtype").serialize(),
		success : function(data) {
			dialog.close();
			if (!$.isSuccess(data))
				return;
			BootstrapDialog.hideModel($('div.add-box'));
			BootstrapDialog.msg(data.body, BootstrapDialog.TYPE_SUCCESS);
			findListInfo();
		}
	});

}
/*
 * 提示并确定删除信息
 * 
 */
function hintDelete(id) {
	if (!id)
		return;
	BootstrapDialog.confirm("请确认是否删除该数据!", function(result) {
		if (!result)
			return;
		dialog = BootstrapDialog.isSubmitted();
		$.getJSON(localhostUrl + 'mgr/delCard', {
			id : id
		}, function(data) {
			dialog.close();
			if (!$.isSuccess(data))
				return;
			BootstrapDialog.msg(data.body, BootstrapDialog.TYPE_SUCCESS);
			findListInfo();
		});
	});
}