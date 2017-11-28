var member = {};

function initFun() {
	if (secure.find) {
		dialog = BootstrapDialog.loading();
		findListInfo();
	}
	if(!secure.add) $('button.add-empl-btn').remove();
	if(secure.add ) $('button.add-empl-btn').removeClass('hide');
}

/**
 * 新窗口打开新建会员信息页面
 */
function newMember() {
//	window.open('./member_info.html?moduleCode='+moduleCode);
	window.location.href="./member_info.html?moduleCode="+moduleCode;
}

/**
 * 添加会员信息
 */
function addMember() {
	$.isSubmit = true;
	$.verifyForm($('input.typename'), true);
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

/**
 * 获取会员列表信息
 */
function findListInfo() {
	var serVal = $('input.searchInput').val();
	var sex = $('select.select-sex').val();
	$.post(localhostUrl+'mgr/findMemberListInfo', {
		serVal : serVal,
		sex:sex,
		page : page
	}, function(data) {
		var tbody = $('tbody.tbody-info').empty();
		dialog.close();
		if(!$.isSuccess(data)) return;
		$.each(data.body, function(i,v){
			$("<tr></tr>")
			.append($("<td></td>").append(v.id))
			.append($("<td></td>").append(v.realname))
			.append($("<td></td>").append(v.sex==1? '女' : '男'))
			.append($("<td></td>").append(v.phone))
			.append($("<td></td>").append(v.rank))
			.append($("<td></td>").append(v.age))
			.append($("<td></td>").append(v.griptype==1? '直板' : '横板'))
			.append($("<td></td>").append(v.createtime))
			.append($("<td></td>").append(analysisBtns(v)))
			.appendTo(tbody);
		});
	}, 'json');
	// 获取分页信息
	$.post(localhostUrl+'mgr/findMemberPage', {
		serVal : serVal,
		sex:sex,
		page : page
	}, function(data) {$.analysisPage(data.body);}, 'json');
}

/**
 * 解析按钮组
 * @param v
 * @returns {String}
 */
function analysisBtns(v){
	var btns = "";
	btns += secure.modify ? "<button type='button' class='btn btn-primary btn-xs' onclick='modifyMember("+v.id+")'><span class='glyphicon glyphicon-pencil'></span>编辑</button>" : "" ;
	btns += secure.find ? "<button type='button' class='btn btn-success btn-xs' onclick='showRecord("+v.id+")'><span class='glyphicon glyphicon-paperclip'></span>购买记录</button>" : "" ;
	btns += secure.del ? "<button type='button' class='btn btn-danger btn-xs' onclick='hintDelete("
			+ v.id
			+ ")'><span class='glyphicon glyphicon-remove'></span>删除</button>"
			: "";
	return btns;
}

/**
 * 显示购买会员卡记录
 * @param id
 */
function showRecord(id) {
	if (!id) return;
	dialog = BootstrapDialog.loading();
	$.getJSON(localhostUrl+'/mgr/findMemberCardByMid', {memberid : id},function(data) {
		dialog.close();
		if (!$.isSuccess(data)) return;
		var tbody = $('tbody.training-record').empty();
		$.each(data.body, function(i,v){
			var user="";//(1:未使用,2:已使用,3:已作废)
			if(v.ifuseless==1){
				user="未使用";
			}
			if(v.ifuseless==2){
				user="已使用";
			}
			if(v.ifuseless==3){
				user="已用完";
			}
			if(v.ifuseless==4){
				user="已作废";
			}
			$('<tr></tr>')
			.append($('<td></td>').append(i+1))
			.append($('<td></td>').append(v.cardType))
			.append($('<td></td>').append(v.createtime )) 
			.append($('<td></td>').append(user))
			.append($('<td></td>').append(v.residuetimes))
			.append($('<td></td>').append(v.price)) 
			.append($('<td></td>').append(analysisRecordBtns(v)))
			.appendTo(tbody);
		});
		BootstrapDialog.showModel($('div.enroll-list-box'));
	});
}

/**
 * 消费记录
 * @param v
 * @returns {String}
 */
function analysisRecordBtns(v){
	var btns = "";
	if(v.ifuseless!=1){
		btns += "<button type='button' class='btn btn-info btn-xs' onclick='showNote(\"" + v.id + "\")'><span class='glyphicon glyphicon-align-left'></span>消费记录</button>";
	}
	return btns;
}

/**
 * 显示消费历史
 * @param title
 * @param message
 */
function showNote(id){
	$.getJSON(localhostUrl+'/mgr/findConsumeList', {membercardid : id},function(data) {
		if (!$.isSuccess(data)) return;
		var tbody = $('tbody.empl-list-tboal').empty();
		$.each(data.body, function(i,v){
			$('<tr></tr>')
//			$('<tr class="success"></tr>')
			.append($('<td></td>').append(i+1))
			.append($('<td></td>').append(v.createtime))
			.append($('<td></td>').append(v.ctimes))
			.appendTo(tbody);
		});
		BootstrapDialog.showModel($('div.add-empl-box'));
	});
}

/**
 * 显示修改会员页面
 * @param id
 */
function modifyMember(id) {
	window.location.href="./member_info.html?moduleCode="+moduleCode+"&id="+id;
}

/**
 * 提示并确定删除会员信息
 * @param id
 */
function hintDelete(id) {
	if (!id)
		return;
	BootstrapDialog.confirm("请确认是否要删除该用户?<br /><span class='placeholder'>PS: 同时会删除该用户的所有的购买与消费记录等信息!</span>", function(result) {
		if (!result)
			return;
		dialog = BootstrapDialog.isSubmitted();
		$.getJSON(localhostUrl + 'mgr/deleteMember', {
			id : id
		}, function(data) {
			if (!$.isSuccess(data))
				return;
			dialog.close();
			BootstrapDialog.msg(data.body, BootstrapDialog.TYPE_SUCCESS);
			findListInfo();
		});
	});
}

