function initFun() {
	
	$("#tt").bigAutocomplete({//联想下拉框
		url:localhostUrl+"mgr/findListByserVal",
		callback:function(data){
			$('#membercard input.memberid').val(data.id);
		}
	});
	
	alignmentFns.initialize();
	$("#membercard .card-type").change(function() {
		var v=$(this).val();
		if(v>0){
			var data=ajax.json.get(localhostUrl+ '/mgr/findCardById?id='+v);
			$("div.details-box h4").text($(this).find("option:selected").text());
			setValueForForm(data.body);// 遍历表单元素,循环赋值
			BootstrapDialog.showModel($('div.details-box'));
		}
	});
	
	if (secure.find) {
		createSel('', 'id', 'typename', $("select.card-type"), localhostUrl+"mgr/findAllCard", "请选择卡类");
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
 * 获取已办理的会员卡列表 
 */
function findListInfo() {
	$.post(localhostUrl+'mgr/findMemberCardListInfo', {
		page : page,
		serVal : $('.searchInput').val(),
		ifuseless : $('.ifuseless').val(),
		cardid:$("select.card-type").val()
	}, function(data) {
		dialog.close();
		var tbody = $('tbody.tbody').empty();
		if (!$.isSuccess(data)) return;
		$.each(data.body, function(i, v) {
			var user="";//(1:未使用,2:已使用,3:已作废)
			$a=$("<tr></tr>");
			if(v.ifuseless==1){
				user="未使用";
			}
			if(v.ifuseless==2){
				user="已使用";
			}
			if(v.ifuseless==3){
				user="已用完";
				$a=$("<tr class='warning'></tr>");
			}
			if(v.ifuseless==4){
				user="已作废";
				$a=$("<tr class='danger'></tr>");
			}
			$a.append($("<td></td>").append(i+1))
			.append($('<td></td>').append(v.realname))
			.append($('<td></td>').append(v.phone))
			.append($('<td></td>').append(v.cardType))
			.append($('<td></td>').append(v.createtime))
			.append($('<td></td>').append(v.residuetimes))
			.append($('<td></td>').append(user))
			.append($('<td></td>').append(v.expiredate))
			.append($('<td></td>').append(analyzeBtns(v)))
			.appendTo(tbody);
		});
	}, 'json');
	$.post(localhostUrl+'mgr/findMemberCardPage', {
		page : page,
		serVal : $('.searchInput').val(),
		ifuseless : $('.ifuseless').val(),
		cardid:$("select.card-type").val()
	}, function(data) {
		$.analysisPage(data.body);
	}, 'json');
}

/**
 * 分析操作按钮 
 * @param v
 * @returns {String}
 */
function analyzeBtns(v) {
	var btns = "";
	btns += secure.del ? "<button type='button' class='btn btn-danger btn-xs' onclick='delthis(\""+v.id+"\")'><span class='glyphicon glyphicon-trash'></span>删除</button>" : "";
	btns += "<button type='button' class='btn btn-primary btn-xs' onclick='showHisBox(\""+v.id+"\")'><span class='glyphicon glyphicon-align-left'></span>消费记录</button>";
	if(v.ifuseless!=3&&v.ifuseless!=4){//不是作废和用完的状态时候
		btns += secure.modify ? "<button type='button' class='btn btn btn-warning btn-xs' onclick='showconsum(\""+v.id+"\" ,"+v.residuetimes+")'><span class='glyphicon glyphicon-play'></span>消费</button>" : "";
	}
	return btns;
}

/**
 * 准备消费
 * @param id
 */
function showconsum(id,residuetimes) {
	if (!id) return;
	$('.ctimes').val(1);
	$("#mcdid").val(id);
	if(residuetimes>0){
		BootstrapDialog.showModel($('div.set-principal-box'));
		return;
	}
	consum();
}

/**
 * 提示并确定删除
 * @param id
 */
function delthis(id) {
	if (!id) return;
	BootstrapDialog.confirm("请确认是否要删除该会员卡?<br /><span class='placeholder'>PS: 同时会删除该会员卡下的消费记录!</span>", function(result) {
		if (!result) return;
		dialog = BootstrapDialog.isSubmitted();
		$.getJSON(localhostUrl+'mgr/deleMCard', {mcdid : id}, function(data) {
			dialog.close();
			if (!$.isSuccess(data)) return;
			findListInfo();
			BootstrapDialog.msg(data.body, BootstrapDialog.TYPE_SUCCESS);
		});
	});
}
/**
 * 月卡直接消费
 */
function consum() {
	BootstrapDialog.confirm("<h4>此卡为月卡,持有者可不限次消费,确定消费吗?</h4>", function(result) {
		if (!result)
			return;
		dialog = BootstrapDialog.isSubmitted();
		$.getJSON(localhostUrl + 'mgr/consumeCard', {
			mcdid : $("#mcdid").val(),
			ctimes : 1
		}, function(data) {
			if (!$.isSuccess(data))
				return;
			dialog.close();
			BootstrapDialog.msg(data.body, BootstrapDialog.TYPE_SUCCESS);
			findListInfo();
		});
	});
}

/**
 * 普通次卡消费
 */
function consumCard() {
	$.getJSON(localhostUrl + 'mgr/consumeCard', {
		mcdid : $("#mcdid").val(),
		ctimes : $('.ctimes').val()
	}, function(data) {
		if (!$.isSuccess(data))
			return;
		BootstrapDialog.hideModel($('div.set-principal-box'));
		BootstrapDialog.msg(data.body, data.type);
		findListInfo();
	});
}

/**
 * 显示消费历史
 * @param id
 */
function showHisBox(id){
	$.getJSON(localhostUrl+'/mgr/findConsumeList', {membercardid : id},function(data) {
		if (!$.isSuccess(data)) return;
		var tbody = $('tbody.empl-list-tboal').empty();
		$.each(data.body, function(i,v){
			$('<tr></tr>')
			.append($('<td></td>').append(i+1))
			.append($('<td></td>').append(v.createtime))
			.append($('<td></td>').append(v.ctimes))
			.appendTo(tbody);
		});
		BootstrapDialog.showModel($('div.add-empl-box'));
	});
}

/**
 * 显示办理会员卡窗口 
 */
function showAddBox() {
	$('.empty').removeClass('empty');
	$('#membercard input.mname').val('');
	$('#membercard input.memberid').val('');
	$('#membercard select.card-type').val('0');
	$('#membercard input.ctimes').val('0');
	/**
	 * 模态窗口里input获得焦点
	 */
	 $.fn.modal.Constructor.prototype.enforceFocus = function() {
	        //$(".chosen-select").trigger("focus");
	        $("#membercard input.mname").focus();
	    };
	BootstrapDialog.showModel($('div.add-box'));
}

/**
 * 办理会员卡或者并消费
 */
function addMemberCard() {
	$.isSubmit = true;
	$.verifyNumForm($('#membercard input.mname'), true,null);
	var memberid= $.verifyNumForm($('#membercard input.memberid'), true,null);
	if(memberid==null||memberid==''){
		BootstrapDialog.alert("会员信息选择有误", "type-danger");
		return;
	}
	var cardid= $.verifyNumForm($('#membercard select.card-type'), true,1);
	var ctimes=$.verifyNumForm($('#membercard input.ctimes'), true,0);
	if (!$.isSubmit) return;
	$.getJSON(localhostUrl + 'mgr/transactCard', {
		cardid : cardid,
		memberid : memberid,
		ctimes : ctimes
	}, function(data) {
		if (!$.isSuccess(data))
			return;
		BootstrapDialog.msg(data.body, BootstrapDialog.TYPE_SUCCESS);
		BootstrapDialog.hideModel($('div.add-box'));
		findListInfo();
	});
}
