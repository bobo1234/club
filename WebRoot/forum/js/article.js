var position = {};

function initFun() {
	if(secure.find){
		dialog = BootstrapDialog.loading();
		findUserSelect('', $('select.userList'));
		findListInfo();
	}
	if(!secure.add)
		$('button.add-btn').remove();
	if(secure.add)
		$('button.add-btn').removeClass('hide');
}
/*
 * 获取日志列表数据
 *  
 */
function findListInfo() {
	$.post(localhostUrl+'mgr/findLogListInfo', {
		page : page,
		Logtype : $('select.typeList').val(),
		des : $('input.searchInput').val(),
		beginTime : $('#begin_time').val(),
		endTime : $('#end_time').val(),
		acctId : $('select.userList').val()
	}, function(data){
		dialog.close();
		var tbody = $('tbody.tbody').empty();
		if(!$.isSuccess(data)) return;
		$.each(data.body, function(i, v){
			var type="";
			switch(v.logType) 
			{ 
			case 1: 
				type='FIND'; 
			break; 
			case 2: 
				type='DELETE'; 
			break; 
			case 3: 
				type='MODIFY'; 
				break; 
			case 4: 
				type='ADD'; 
				break; 
			case 5: 
				type='LOGIN';
				break; 
			default:
				type='EXIT'; 
			} 
			$("<tr></tr>")
			.append($("<td></td>").append(i+1))
			.append($("<td></td>").append(v.method))
			.append($("<td></td>").append(v.description))
			.append($("<td></td>").append(v.requestIp))
			.append($("<td></td>").append(type))
			.append($("<td></td>").append(v.createDate))
			.append($("<td></td>").append(analyzeBtns(v)))
			.appendTo(tbody);
		});
	}, 'json');
	$.post(localhostUrl+'mgr/findLogPage', {
		page : page,
		des : $('input.searchInput').val(),
		Logtype : $('select.typeList').val(),
		beginTime : $('#begin_time').val(),
		endTime : $('#end_time').val(),
		acctId : $('select.userList').val()
	}, function(data){
		$.analysisPage(data.body);
	}, 'json');
}
/*
 * 解析操作按钮
 *  
 */
function analyzeBtns(v){
	var btns = "";
	btns += secure.find ? "<button type='button' class='btn btn-info btn-xs' onclick='showDetails(\"" + v.id + "\")'><span class='glyphicon glyphicon-menu-left'></span>详情</button>" : "";
	btns += secure.del ? "<button type='button' class='btn btn-danger btn-xs' onclick='hintDelete(\""+v.id+"\")'><span class='glyphicon glyphicon-remove'></span>删除</button>" : "" ;
	return btns;
}
/**
 * 显示窗口
 * @param id
 */
function showDetails(id){
	if(!id) return;
	dialog = BootstrapDialog.loading();
	$.getJSON(localhostUrl+'mgr/findLogById', {id:id}, function(data){
		dialog.close();
		if (!$.isSuccess(data)) return;
		
		setValueForForm(data.body);
		var type="";
		switch(data.body.logType) 
		{ 
		case 1: 
			type='FIND'; 
		break; 
		case 2: 
			type='DELETE'; 
		break; 
		case 3: 
			type='MODIFY'; 
			break; 
		case 4: 
			type='ADD'; 
			break; 
		case 5: 
			type='LOGIN';
			break; 
		default:
			type='EXIT'; 
		}
		$('.logType').text(type);
		BootstrapDialog.showModel($('div.details-box'));
	});
}

/**
 * 获取管理人员下拉菜单
 * @param curDepartient 当前要被选中的人员
 * @param eml 要被赋值的元素
 *  
 */
function findUserSelect(curDepartient, eml) {
	eml.empty().append("<option value=''>选择操作人员</option>");
	$.getJSON(localhostUrl+'mgr/account/findAccount', function(data) {
		if(!$.isSuccess(data)) return;
		$.each(data.body, function(i,v){
			$("<option "+analyzeSelect(v.acctId,curDepartient) +" value="+v.acctId+"></option>")
			.append(v.acctName)
			.appendTo(eml);
		});
	}); 
}

/**
 * 默认
 * @param id
 * @param curDepartient
 * @returns
 */
function analyzeSelect(id, curDepartient){
	return curDepartient > 0 && id == curDepartient ? " selected=true " : "" ;
}

/**
 * 提示并确定删除日志
 * @param id
 */
function hintDelete(id){
	if(!id) return;
	BootstrapDialog.confirm("请确认是否删除该数据!", function(result){
		if(!result) return;
		dialog = BootstrapDialog.isSubmitted();
		$.getJSON(localhostUrl+'mgr/deleteLog',{id : id}, function(data){
			if(!$.isSuccess(data)) return;
			dialog.close();
			BootstrapDialog.msg(data.body, BootstrapDialog.TYPE_SUCCESS);
			findListInfo();
		});
	});
}