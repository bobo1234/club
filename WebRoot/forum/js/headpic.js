/**
 * 页面初始化加载的方法
 */
function initFun() {
	if (secure.find) {
		dialog = BootstrapDialog.loading();
		findListInfo();
	}
	if (!secure.add) {
		$('button.add-btn').remove();
	}
	if (secure.add) {
		$('button.add-btn').removeClass('hide');
	}
}

/**
 * 图片上传接口
 * 
 * @param base64
 * @param url
 */
function uploadPic(base64, url) {
	var title=$('input.addtitle').val();
	dialog = BootstrapDialog.onloading();
	$.post(url, {
		img : base64,
		title:title
	}, function(data) {
		dialog.close();
		if (!$.isSuccess(data)) {
			BootstrapDialog.msg(data.body, BootstrapDialog.TYPE_DANGER);
			return;
		}
		BootstrapDialog.msg(data.body, BootstrapDialog.TYPE_SUCCESS);
		$('#avatar-modal').modal('hide');
		findListInfo();
	}, 'json');
}

/**
 * 显示未启用的图片列表
 */
function showAddEmplBox() {
	var search = $('input.search-val').val();
	$.post(localhostUrl + 'mgr/findAllPic', {
		title : search
	}, function(data){
		var body = $('tbody.empl-list-tboal').empty();
		if(!$.isSuccess(data)) return;
		$.each(data.body, function(i,v){
			$('<tr></tr>')
			.append($('<td></td>').append(v.id))
			.append($('<td></td>').append(v.title))
			.append($('<td></td>').append($('<a><img  class="hispic" style="width:40px;height:40px" src='+localhostUrl+v.picurl+' /></a>')))
			.append($('<td></td>').append(analyzeAddTrainingBtns(v)))
			.appendTo(body);
		});
	}, 'JSON');
	BootstrapDialog.showModel($('div.add-empl-box'));
}

/**
 * 解析操作按钮
 * @param v
 * @returns {String}
 */
function analyzeAddTrainingBtns(v){
	var btns = "";
	btns += "<button type='button' class='btn btn-success btn-xs' onclick='startuse("+v.id+")'><span class='glyphicon glyphicon-plus'></span>启用</button>" ;
	btns += "&nbsp; <button type='button' class='btn btn-danger btn-xs' onclick='delthis("+v.id+")'><span class='glyphicon glyphicon-trash'></span>删除</button>" ;
	return btns;
}

$(function() {
	$("body").delegate(".hispic", "mouseover", function (e) {
		var s=$(this).attr("src");
		$(this).parent().append("<p id='pic'><img src='"+s+"' id='pic1'></p>");
		   $("#pic").css({
	             "top":"20px",
	             "left":"450px"
//	             "top":(e.pageY-200)+"px",
//	             "left":(e.pageX-10)+"px"
	         }).fadeIn("fast");
	});
	
	$("body").delegate(".hispic", "mouseout", function () {
		 $("#pic").remove();
	});
	
})

/**
 * 启用历史图片
 * @param id
 */
function startuse(id) {
	BootstrapDialog.confirm("请确认此操作?", function(result) {
		if (!result)
			return;
	$.post(localhostUrl+'mgr/updatepic', {
		picid : id,
		ifuss : 1
	}, function(data) {
		dialog.close();
		if (!$.isSuccess(data))
			return;
		BootstrapDialog.msg(data.body, BootstrapDialog.TYPE_SUCCESS);
		showAddEmplBox();
		findListInfo();
	}, 'json');
	});
}

/**
 * 删除
 * @param id
 */
function delthis(id) {
	if (!id)
		return;
	BootstrapDialog.confirm("请确认是否删除该图片", function(result) {
		if (!result)
			return;
	$.post(localhostUrl+'mgr/delPic', {
		picid : id
	}, function(data) {
		dialog.close();
		if (!$.isSuccess(data))
			return;
		BootstrapDialog.msg(data.body, BootstrapDialog.TYPE_SUCCESS);
		showAddEmplBox();
	}, 'json');
});
}


/**
 * 关闭窗口
 */
function closeEmplListBox(){
	BootstrapDialog.hideModel($('div.add-empl-box'));
}


/**
 * 所有启用的图片
 */
function findListInfo() {
	$.post(localhostUrl+'mgr/findUsePic',function(data) {
		dialog.close();
		if (!$.isSuccess(data))
			return;
		var tbody = $('.imglist').empty();
		$.each(data.body, function(i, v) {
			$('<div class="col-sm-6 col-md-2"> </div>')
			.append($('<div class="thumbnail">').append($('<img style="width:170px;height:150px" src='+localhostUrl+v.picurl+' />'))
			.append($("<div class='caption'> </div>").append("<p>"+v.title+"</p>")
			.append($("<p></p>").append('<a href="java:void(0)" class="btn btn-primary" onclick="move2head('+v.id+')" role="button">'+' <span class="glyphicon glyphicon-arrow-left"></span>前移')
			.append('&nbsp;&nbsp;<a href="java:void(0)" class="btn btn-default" onclick="updatepic('+v.id+')" role="button">'+' <span class="glyphicon glyphicon-minus-sign"></span>作废')	
			)		
			)
			)
			.appendTo(tbody);
		});
	}, 'json');
}

/**
 * 图片前移
 * 
 * @param id
 */
function move2head(id) {
	$.post(localhostUrl+'mgr/moveHead', {
		picid : id
	}, function(data) {
		dialog.close();
		if (!$.isSuccess(data))
			return;
// BootstrapDialog.msg(data.body, BootstrapDialog.TYPE_SUCCESS);
		findListInfo();
	}, 'json');
}

/**
 * 提示并确定作废图片
 * 
 * @param id
 */
function updatepic(id) {
	if (!id)
		return;
	BootstrapDialog
			.confirm(
					"请确认是否要作废该图片?",
					function(result) {
						if (!result)
							return;
						dialog = BootstrapDialog.isSubmitted();
						$.getJSON(localhostUrl+'mgr/updatepic', {
							picid : id,
							ifuss : 2,
						}, function(data) {
							dialog.close();
							if (!$.isSuccess(data))
								return;
							findListInfo();
							BootstrapDialog.msg(data.body,
									BootstrapDialog.TYPE_SUCCESS);
						});
					});
}


/*
 * 显示编辑窗口
 * 
 */
function showModifyBox(picId) {
	$('.empty').removeClass('empty');
	if (!picId) return;
	dialog = BootstrapDialog.loading();
	$('input.modifyName').val("");
	$('textarea.modifyDesc').val("");
	$.getJSON('mgr/findPicById', {picid : picId}, function(data) {
		dialog.close();
		if (!$.isSuccess(data)) return;
		$('input.modifyName').val(data.body.deptName);
		$('textarea.modifyDesc').val(data.body.deptDescription);
		$('button.modifyBtn').attr('onclick', 'modifyDept(' + data.body.deptId + ')');
		$('div.modify-box').modal({
			closable : false,
			show : true
		});
	});
}

