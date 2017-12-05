var match = {};
var member = {};
var memberMatch = {};
function initFun() {
	$("input.matchSearch-val").keydown(function(event) {// 给输入框绑定按键事件
		if (event.keyCode == "13") {// 判断如果按下的是回车键则执行下面的代码
			$(this).next(".btn-primary").click();
		}
	});
	if (secure.add) $('button.btn-create').removeClass('hide');
	if (!secure.add) $('button.btn-create').remove();
	if (secure.find) {
		dialog = BootstrapDialog.loading();
		findListInfo();
	}
}

/*
 * 显示添加窗口 
 *  
 */
function showAddBox() {
	$('.empty').removeClass('empty');
	$('input.addName').val("");
	$('input.addStartDate').val("");
	$('input.addEndDate').val("");
	$('textarea.addDesc').val("");
	BootstrapDialog.showModel($('div.add-box'));
}
/*
 * 添加比赛项目 
 *  
 */
function addTraining() {
	$.isSubmit = true;
	match.name = $.verifyForm($('input.addName'), true);
	match.startdate = $.verifyForm($('input.addStartDate'), true);
	match.description = $.verifyForm($('textarea.addDesc'), false);
	if (!$.isSubmit) return;
	// 添加比赛项目前, 需要再次确认
	BootstrapDialog.confirm("请再次确认是否添加 <b>" + match.name + "</b> 这个比赛项目, 创建后该数据不能删除, 请谨慎操作!", function(result) {
		if (!result) return;
		dialog = BootstrapDialog.loading();
		$.post(localhostUrl+'mgr/addMatch', {
			name : match.name,
			mark : match.description,
			matchtime : match.startdate
		}, function(data) {
			dialog.close();
			if (!$.isSuccess(data)) return;
			BootstrapDialog.hideModel($('div.add-box'));
			BootstrapDialog.msg(data.body, BootstrapDialog.TYPE_SUCCESS);
			findListInfo();
		},'json');
	});
}

/**
 * 获取列表数据 
 */
function findListInfo() {
	var searchValue = $('input.searchInput').val();
	$.post(localhostUrl+'/mgr/findMatchListInfo', {searchValue : searchValue, page : page}, function(data) {
		dialog.close();
		var tbody = $('tbody.tbody-list').empty();
		if (!$.isSuccess(data)) return;
		$.each(data.body, function(i, v) {
			$('<tr></tr>')
			.append($('<td></td>').append(i+1))
			.append($('<td></td>').append(v.name))
			.append($('<td></td>').append(v.matchtime))
			.append($('<td></td>').append(v.inmembers))
			.append($('<td></td>').append(v.mstate))
			.append($('<td></td>').append(v.mark))
			.append($('<td></td>').append(analyzeBtns(v)))
			.appendTo(tbody);
		});
		findListPage();
	}, "json");
}
/*
 * 获取分页数据 
 *  
 */
function findListPage() {
	var searchValue = $('input.searchValue').val();
	$.post(localhostUrl+'/mgr/findMatchPage', {searchValue : searchValue, page : page}, function(data) {
		$.analysisPage(data.body);
	}, "json");
}

/*
 * 提示删除 
 *  
 */
function hintDelete(id) {
	if (!id) return;
	BootstrapDialog.confirm("请确认是否删除该比赛项目?<br /><span class='placeholder'>PS: 同时会删除所有会员相关的比赛记录!</span>", function(result) {
		if (!result) return;
		$.getJSON('./mgr/training/delete', {id : id}, function(data) {
			if (!$.isSuccess(data)) return;
			BootstrapDialog.msg(data.body, BootstrapDialog.TYPE_SUCCESS);
			findListInfo();
		});
	});
}

/*
 * 显示编辑窗口
 *   
 */
function showModify(id) {
	$('.empty').removeClass('empty');
	$('input.modifyName').val("");
	$('input.modifyId').val("");
	$('input.modifyEndDate').val("");
	$('textarea.modifyDesc').val("");
	if (!id) return;
	dialog = BootstrapDialog.loading();
	$.getJSON(localhostUrl+'mgr/findMatchById', {id : id}, function(data) {
		dialog.close();
		if (!$.isSuccess(data)) return;
		$('input.modifyId').val(data.body.id);
		$('input.modifyName').val(data.body.name);
		$('input.modifyEndDate').val(data.body.matchtime);
		$('textarea.modifyDesc').val(data.body.mark);
		BootstrapDialog.showModel($('div.modify-box'));
	});
}
/*
 * 编辑数据 
 *  
 */
function modifyTraining() {
	$.isSubmit = true;
	match.id = $.verifyForm($('input.modifyId'), false);
	match.name = $.verifyForm($('input.modifyName'), true);
	match.startdate = $.verifyForm($('input.modifyEndDate'), true);
	match.description = $.verifyForm($('textarea.modifyDesc'), false);
	if (!$.isSubmit) return;
	dialog = BootstrapDialog.loading();
	$.post(localhostUrl+'/mgr/modifyMatch', {
		id : match.id,
		name : match.name,
		mark : match.description,
		matchtime : match.startdate
	}, function(data) {
		dialog.close();
		if (!$.isSuccess(data)) return;
		BootstrapDialog.hideModel($('div.modify-box'));
		BootstrapDialog.msg(data.body, BootstrapDialog.TYPE_SUCCESS);
		findListInfo();
	},'json');
}

/**
 *  解析操作按钮 
 * @param v
 * @returns {String}
 */
function analyzeBtns(v) {
	var btns = "";
	btns += secure.find ? "<button type='button' class='btn btn-success btn-xs' onclick='showRecord(\"" + v.id + "\","+v.state+")'><span class='glyphicon glyphicon-paperclip'></span>记录</button>" : "";
	if(v.state==1){
		btns += "<button type='button' class='btn btn btn-warning btn-xs' onclick='start(\"" + v.id + "\")'><span class='glyphicon glyphicon-play'></span>开始</button>";
		btns += "<button type='button' class='btn btn-danger btn-xs' onclick='pushNotice(\"" + v.id + "\")'><span class='glyphicon glyphicon-phone'></span>推送</button>";
		if (secure.modify) {
			btns += "<button type='button' class='btn btn-primary btn-xs' onclick='showModify(\"" + v.id + "\")'><span class='glyphicon glyphicon-pencil'></span>编辑</button>";
		}
		btns += "<button type='button' class='btn btn-warning btn-xs' onclick='stop(\"" + v.id + "\")'><span class='glyphicon glyphicon-off'></span>取消</button>";
	}else{
		btns += "<button type='button' disabled='disabled' class='btn btn btn-warning btn-xs' onclick='start(\"" + v.id + "\")'><span class='glyphicon glyphicon-play'></span>开始</button>";
		btns += "<button type='button' disabled='disabled' class='btn btn-danger btn-xs' onclick='pushNotice(\"" + v.id + "\")'><span class='glyphicon glyphicon-phone'></span>推送</button>";
		btns += "<button type='button' disabled='disabled' class='btn btn-primary btn-xs' onclick='showModify(\"" + v.id + "\")'><span class='glyphicon glyphicon-pencil'></span>编辑</button>";
		btns += "<button type='button' disabled='disabled' class='btn btn-warning btn-xs' onclick='stop(\"" + v.id + "\")'><span class='glyphicon glyphicon-off'></span>取消</button>";
	}
	//状态(1:未开始,2:已开始,3:已结束,4:已取消)
	if(v.state==4){
		btns += "<button type='button' class='btn btn-info btn-xs' onclick='recovery(\"" + v.id + "\")'><span class='glyphicon glyphicon-refresh'></span>恢复</button>";
	}else{
		btns += "<button type='button' disabled='disabled' class='btn btn-info btn-xs' onclick='recovery(\"" + v.id + "\")'><span class='glyphicon glyphicon-refresh'></span>恢复</button>";
	}
	return btns;
}

/**
 * 推送比赛消息
 * @param id
 */
function pushNotice(id) {
	if (!id) return;
	BootstrapDialog.confirm("请确认是否推送?", function(result) {
		if (!result) return;
		dialog = BootstrapDialog.isSubmitted();
		$.getJSON(localhostUrl+'mgr/pushNoticeMatch', {id : id}, function(data) {
			dialog.close();
			if (!$.isSuccess(data)) return;
			BootstrapDialog.msg(data.body, BootstrapDialog.TYPE_SUCCESS);
		});
	});
}

/**
 * 开始比赛
 * @param id
 */
function start(id) {
	if (!id) return;
	BootstrapDialog.confirm("请确认是否开始比赛?", function(result) {
		if (!result) return;
		dialog = BootstrapDialog.isSubmitted();
		$.getJSON(localhostUrl+'mgr/startMatch', {id : id}, function(data) {
			dialog.close();
			if (!$.isSuccess(data)) return;
			findListInfo();
			BootstrapDialog.msg(data.body, BootstrapDialog.TYPE_SUCCESS);
		});
	});
}

/**
 * 停止比赛项目 
 * @param id
 */
function stop(id) {
	if (!id) return;
	BootstrapDialog.confirm("请确认是否取消比赛?", function(result) {
		if (!result) return;
		dialog = BootstrapDialog.isSubmitted();
		$.getJSON(localhostUrl+'mgr/stopMatch', {id : id}, function(data) {
			dialog.close();
			if (!$.isSuccess(data)) return;
			findListInfo();
			BootstrapDialog.msg(data.body, BootstrapDialog.TYPE_SUCCESS);
		});
	});
}

/**
 * 恢复比赛项目 
 * @param id
 */
function recovery(id) {
	if (!id) return;
	BootstrapDialog.confirm("请确认是否恢复比赛?", function(result) {
		if (!result) return;
		dialog = BootstrapDialog.isSubmitted();
		$.getJSON(localhostUrl+'mgr/recovery', {id : id}, function(data) {
			dialog.close();
			if (!$.isSuccess(data)) return;
			BootstrapDialog.msg(data.body, BootstrapDialog.TYPE_SUCCESS);
			findListInfo();
		});
	});
}

/**
 * 显示参加比赛列表记录
 * @param id
 */
function showRecord(id,state) {
	if (!id) return;
	match.id=id;
	match.state=state;
	$(".matchSearch-val").val('');
	dialog = BootstrapDialog.loading();
	$.getJSON(localhostUrl+'/mgr/findMeMatchList', {mid : id},function(data) {
		dialog.close();
		$('button.add-empl-btn, table.table-record-list,button.exportButton').addClass('hide');//隐藏打印按钮
		$('div.notdate').addClass('hide');
		if (!$.isSuccess(data)) return;
		var tbody = $('tbody.record-list').empty();
			//状态(1:未开始,2:已开始,3:已结束,4:已取消)
			if(data.body.length>0){
				if(state== 1||state == 2){
					$('button.exportButton').removeClass('hide');//显示打印按钮
					$('button.add-empl-btn').removeClass('hide');//添加会员的按钮
				}
				$('table.table-record-list').removeClass('hide');
				$.each(data.body, function(i,v){
					$('<tr></tr>')
					.append($('<td></td>').append(i+1))
					.append($('<td></td>').append(v.members.realname))
					.append($("<td></td>").append(v.members.sex==1? '女' : '男'))
					.append($('<td></td>').append(v.members.phone))
					.append($('<td></td>').append(v.createtime))
					.append($("<td></td>").append(v.members.griptype==1? '直板' : '横板'))
					.append($('<td></td>').append(v.score>0?v.score:'暂无'))
					.append($('<td></td>').append(analyzeApplyBtns(v.id,state)))
					.appendTo(tbody);
				});
				BootstrapDialog.showModel($('div.record-box'));
				return;
			}
			
		var body = $('div.record-modal-body');
		var button = "<button type='button' class='btn btn-success btn-xs' onclick='showAddEmplBox()'><span class='glyphicon glyphicon-plus'></span>点击这里添加会员</button>";
		//状态(1:未开始,2:已开始,3:已结束,4:已取消)
		if(state == 4)
			$("<div class='notdate'></div>").append("<b>比赛已经取消</b>").appendTo(body);
		else if(state == 3)
			$("<div class='notdate'></div>").append("<b>比赛已经结束</b>").appendTo(body);
		else 
			$("<div class='notdate'></div>").append("该比赛目前未发现已报名参加的会员, 如需报名请"+button).appendTo(body);
		BootstrapDialog.showModel($('div.record-box'));
	});
}

/**
 * 搜索参赛人员
 */
function findMatchMemberList() {
	if (!match.id) return;
	var searchval= $('input.matchSearch-val').val();
	dialog = BootstrapDialog.loading();
	$.getJSON(localhostUrl+'/mgr/findMeMatchList', {mid:match.id,searchVal:searchval},function(data) {
		dialog.close();
		if (!$.isSuccess(data)) return;
		var tbody = $('tbody.record-list').empty();
			if(data.body.length>0){
				$('table.table-record-list').removeClass('hide');
				$.each(data.body, function(i,v){
					$('<tr></tr>')
					.append($('<td></td>').append(i+1))
					.append($('<td></td>').append(v.members.realname))
					.append($("<td></td>").append(v.members.sex==1? '女' : '男'))
					.append($('<td></td>').append(v.members.phone))
					.append($('<td></td>').append(v.createtime))
					.append($("<td></td>").append(v.members.griptype==1? '直板' : '横板'))
					.append($('<td></td>').append(v.score>0?v.score:'暂无'))
					.append($('<td></td>').append(analyzeApplyBtns(v.id,match.state)))
					.appendTo(tbody);
				});
				BootstrapDialog.showModel($('div.record-box'));
			}
	});
}


/*
 * 渲染比赛记录操作按钮
 *  
 */
function analyzeApplyBtns(id,state){
	var btns = "";
	if(state==1||state==2){
		btns += "<button type='button' class='btn btn-danger btn-xs' onclick='delMemberMatch(\"" + id + "\")'><span class='glyphicon glyphicon-minus'></span>删除</button>" ;
	}
	if(state!=1&&state!=4){
		btns += "<button type='button' class='btn btn-success btn-xs' onclick='evaluationMatch(\"" + id + "\")'><span class='glyphicon glyphicon-bookmark'></span>评分</button>" ;
	}
	return btns;
}

/**
 * 显示评分窗口
 * @param id
 */
function evaluationMatch(id){
	if(!id) return;
	memberMatch.id=id;
	BootstrapDialog.showModel($('div.evaluation-training-box'));
}

/**
 * 为培训评分
 */
function evaluationEmployeesTraining(){
	if(!memberMatch.id) return;
	$.isSubmit = true;
	$('input.matchSearch-val').val('');
	var score = $.verifyNumForm($('input.score'), true,0);
	if(!$.isSubmit) return;
	dialog = BootstrapDialog.loading();
	$.post(localhostUrl+'mgr/matchScore', {id:memberMatch.id,score:score}, function(data){
		dialog.close();
		if(!$.isSuccess(data)) return;
		BootstrapDialog.msg(data.body, BootstrapDialog.TYPE_SUCCESS);
		BootstrapDialog.hideModel($('div.evaluation-training-box'));
		showRecord(match.id,match.state);
	}, 'json');
}
/*
 * 关闭报名参加比赛班会员列表窗口
 *  
 */
function closeApplyRecord(){
	findListInfo();
	$('input.matchSearch-val').val('');
	BootstrapDialog.hideModel($('div.record-box'));
}

/*
 * 显示会员列表框
 *  
 */
function showAddEmplBox() {
	$('tbody.empl-list-tboal').empty();
	BootstrapDialog.hideModel($('div.record-box'));
	BootstrapDialog.showModel($('div.add-empl-box'));
}


/*
 * 获取会员列表
 *  
 */
function findMemberList(){
	var search = $('input.search-val').val();
	var griptype = $('select.select-griptype').val();
	dialog = BootstrapDialog.loading();
	$.post(localhostUrl+'mgr/findMemberAll', {
		searchVal : search,
		griptype:griptype,
		matchid:match.id
	}, function(data){
		dialog.close();
		var body = $('tbody.empl-list-tboal').empty();
		if(!$.isSuccess(data)) return;
		$.each(data.body, function(i,v){
			$('<tr></tr>')
			.append($('<td></td>').append(v.id))
			.append($('<td></td>').append(v.realname))
			.append($("<td></td>").append(v.sex==1? '女' : '男'))
			.append($('<td></td>').append(v.phone))
			.append($('<td></td>').append(v.age))
			.append($("<td></td>").append(v.griptype==1? '直板' : '横板'))
			.append($('<td></td>').append(analyzeAddTrainingBtns(v)))
			.appendTo(body);
		});
	}, 'JSON');
}

/*
 * 渲染添加比赛记录按钮
 *  
 */
function analyzeAddTrainingBtns(v){
	var btns = "";
	btns += secure.modify ? "<button type='button' class='btn btn-success btn-xs' onclick='addMemberMatch("+v.id+", this)'><span class='glyphicon glyphicon-plus'></span>添加</button>" : "" ;
	return btns;
}

/**
 * 添加报名记录
 * @param emplId
 * @param obj
 */
function addMemberMatch(id, obj){
	if(!id || !match.id) return;
	dialog = BootstrapDialog.loading();
	$.getJSON(localhostUrl+'mgr/addMemberMatch', {matchid:match.id ,memberId:id}, function(data){
		if(!$.isSuccess(data)) return;
		BootstrapDialog.msg(data.body, BootstrapDialog.TYPE_SUCCESS);
		dialog.close();
		if(!$.isSuccess(data)) return;
		$(obj).prop('disabled', true);
	}, 'json');
}; 

/**
 * 关闭搜索会员列表窗口
 */
function closeEmplListBox(){
	$(".select-griptype").val(0);
	$(".search-val").val('');
	showRecord(match.id,match.state);
	BootstrapDialog.hideModel($('div.add-empl-box'));
}

/**
 * 删除会员报名记录
 * @param id
 */
function delMemberMatch(id){
	BootstrapDialog.confirm('请确认是否删除报名记录?', function(result){
		if(!result) return;
		$.getJSON(localhostUrl+'/mgr/deleteMemberMatch', {
			memhid : id
		}, function(data){
			if(!$.isSuccess(data)) return;
			BootstrapDialog.msg(data.body, BootstrapDialog.TYPE_SUCCESS);
			showRecord(match.id,match.state);//查询报名列表
		});
	});
}

/**
 * 提示并添加所有显示的记录
 */
function addAllMember(){
	BootstrapDialog.confirm('请确认是否添加当前搜索结果中的所有会员!', function(result){
		if(!result) return;
		if(!match.id) return;
		var leng = $("tbody.empl-list-tboal tr").length; 
		var ids = [];
		//列表所有的id
		for (var i = 0; i <= leng; i++) {
			numberStr = $("tbody.empl-list-tboal tr").eq(i).find("td:first").html();
			ids.push(numberStr);
		}
		dialog = BootstrapDialog.loading();
		$.post(localhostUrl+'mgr/addMemberMatchAll', {
			matchid : match.id, 
			memberIds : ids
		}, function(data){
			dialog.close();
			if(!$.isSuccess(data)) return;
			BootstrapDialog.msg(data.body, BootstrapDialog.TYPE_SUCCESS);
			$("tbody.empl-list-tboal tr td button").prop('disabled', true);
		}, 'JSON');
	});
}

/**
 * 导出参赛人员列表
 */
function exportMember(){
	var leng = $("tbody.record-list tr").length; 
	var ids = [];
	//列表所有的id
	for (var i = 0; i <= leng; i++) {
		numberStr = $("tbody.record-list tr").eq(i).find("td:eq(1)").html();
		ids.push(numberStr);
	}
	$("#memberNames").val(ids);
	$("#matchid").val(match.id);
	$("#mbNs").attr("action",localhostUrl+'mgr/exportMember').submit();
}