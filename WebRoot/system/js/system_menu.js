var curRoleId;

var type = {};
type.FIND = 1;
type.DELETE = 2;
type.MODIFY = 3;
type.ADD = 4;

var menu = {};
function initFun() {
	if(secure.find){
		dialog = BootstrapDialog.loading();
		getMenu();
	}
	if(!secure.add)
		$('button.add-btn').remove();
	if(secure.add)
		$('button.add-btn').removeClass('hide');
}
/**
 * 获取菜单
 */
function getMenu() {
    console.info("正在执行Ajax请求");
    $.ajax({
        type: "post",
        url: localhostUrl+"mgr/getAllMenu", //Servlet请求地址
        dataType: "json",
        success: function (data) {
            var con = data.rows;//获取json中的list列表
        	dialog.close();
        	$("#treeTable1 tr:gt(0)").remove();//除了第一个tr 其他的都移除
            var showContent = "";//添加内容变量
            for (var i = 0, l = con.length; i < l; i++) {
                var a = con[i];
                var dj="";
                if(a.moduleLevel=='0'){
                	dj='一级';
                }else{
                	dj='二级';
                }
                if (a.moduleSuperCode == '0') { //判断是否是一级节点
                    showContent += "<tr id='" + a.moduleCode + "'>";
                    showContent += "<td><span controller='true'>" + a.moduleName + "</span></td>";
                    showContent += "<td>" + '' + "</td>";
                    showContent += "<td>" + a.moduleCode + "</td>";
                    showContent += "<td>" + dj + "</td>";
                    showContent += "<td>" + analyzeBtns(a)+ "</td>";
                    showContent += "</tr>";
                } else {
                    showContent += "<tr id='" + a.moduleCode + "' pid='" + a.moduleSuperCode + "'>";
                    showContent += "<td>" + a.moduleName + "</td>";
                    showContent += "<td>" + a.modulePage + "</td>";
                    showContent += "<td>" + a.moduleCode + "</td>";
                    showContent += "<td>" + dj + "</td>";
                    showContent += "<td>" + analyzeBtns(a)+ "</td>";
                    showContent += "</tr>";
                }
            }
            $("#treeTable1").append(showContent);
            //以下为初始化表格样式
            var option = {
                theme: 'vsStyle',
                expandLevel: 1,
            };
            $('#treeTable1').treeTable(option);
            console.info("内容已经加载并初始化");
        },
        error: function (XMLHttpRequest, textStatus, errorThrown) {
            alert(errorThrown);
            console.info("数据请求异常 请查看控制台错误 或者检查servlet配置")
        }
    });
}

/**
 * 只有刷新,树形菜单起作用
 */
function findListInfo() {
	setTimeout("history.go(0)", 1500);
}
/**
 * 解析数据列表的操作按钮
 */
function analyzeBtns(v) {
	var btns = ""; 
	btns += secure.modify ? "<button type='button' class='btn btn-primary btn-xs' onclick='showModifyBox(" + v.moduleId + ")'><span class='glyphicon glyphicon-pencil'></span>编辑</button>" : "" ;
	btns += secure.del ? "<button type='button' class='btn btn-danger btn-xs' onclick='menuDelete(" + v.moduleId + ")'><span class='glyphicon glyphicon-remove'></span>删除</button>" : "" ;
	if (v.moduleLevel=='0') {
		btns += secure.add ? "<button type='button' class='btn btn-success btn-xs' onclick='showAddMenuBox(\"" + v.moduleCode+'","'+ v.moduleName+ "\")'><span class='glyphicon glyphicon-plus'></span>新增</button>" : "" ;
	}
	return btns;
}
/**
 * 一级菜单
 */
function showAddBox(){
	$('.empty').removeClass('empty');
	$('input.moduleName').val('');
	BootstrapDialog.showModel($('div.add-box'));
}
/**
 * 二级菜单
 * @param superCode
 * @param spname
 */
function showAddMenuBox(superCode,spname){
	$('.empty').removeClass('empty');
	$('input.spcode').val(superCode);
	$('input.spname').val('所属菜单: '+spname);
	BootstrapDialog.showModel($('div.add-box2'));
}
/**
 * 添加菜单
 */
function addMenu(){
	$.isSubmit = true;
	menu.moduleName = $.verifyForm($('input.moduleName'), true);
	if(!$.isSubmit) return;
	dialog = BootstrapDialog.isSubmitted();
	$.post(localhostUrl+'mgr/addModule', {'moduleName' : menu.moduleName,'moduleLevel' : 0}, function(data){
		dialog.close();
		if(!$.isSuccess(data)) return;
		BootstrapDialog.hideModel($('div.add-box'));
		BootstrapDialog.msg(data.body, BootstrapDialog.TYPE_SUCCESS);
		findListInfo();
	}, 'json');
}
/**
 * 添加二级菜单
 */
function addSMenu(){
	$.isSubmit = true;
	menu.moduleName = $.verifyForm($('.add-box2 input.moduleName'), true);
	menu.moduleSuperCode = $.verifyForm($('.add-box2 input.spcode'), true);
	menu.modulePage = $.verifyForm($('.add-box2 input.modulePage'), true);
	if(!$.isSubmit) return;
	dialog = BootstrapDialog.isSubmitted();
	$.post(localhostUrl+'mgr/addModule', {'moduleName' : menu.moduleName,'moduleLevel' : 1,"moduleSuperCode":menu.moduleSuperCode,"modulePage":menu.modulePage}, function(data){
		dialog.close();
		if(!$.isSuccess(data)) return;
		BootstrapDialog.hideModel($('div.add-box2'));//隐藏表单
		BootstrapDialog.msg(data.body, BootstrapDialog.TYPE_SUCCESS);
		findListInfo();
	}, 'json');
}
/**
 * 显示编辑菜单信息窗口
 */
function showModifyBox(id){
	$('.empty').removeClass('empty');
	if(!id) return;
	dialog = BootstrapDialog.loading();
	$.getJSON(localhostUrl+'mgr/findByid', {id : id}, function(data){
		dialog.close();
		if(!$.isSuccess(data)) return;
		$('input.modifymoduleName').val(data.body.moduleName);
		$('input.modifymodulePage').val(data.body.modulePage);
		$('input.modifymoduleId').val(data.body.moduleId);
		$('input.modifymoduleLevel').val(data.body.moduleLevel);
		BootstrapDialog.showModel($('div.modify-box'));
	});
}
/**
 * 编辑保存
 */
function modifyMenu(){
	$.isSubmit = true;
	menu.moduleName = $.verifyForm($('input.modifymoduleName'), true);
	menu.modifymoduleId = $.verifyForm($('input.modifymoduleId'), true);
	menu.modifymoduleLevel=$('input.modifymoduleLevel').val();
	if (menu.modifymoduleLevel=='0') {
//		menu.modulePage=$('input.modifymodulePage').val();
		menu.modulePage = $.verifyForm($('input.modifymodulePage'), false);
	}else{
		menu.modulePage = $.verifyForm($('input.modifymodulePage'), true);
	}
	if(!$.isSubmit) return;
	dialog = BootstrapDialog.isSubmitted();
	$.post(localhostUrl+'mgr/updateModule', {
		"moduleId" : menu.modifymoduleId,
		"moduleName" : menu.moduleName,
		"modulePage" : menu.modulePage,
		"moduleLevel" : menu.modifymoduleLevel
	}, function(data){
		dialog.close();
		if(!$.isSuccess(data)) return;
		BootstrapDialog.hideModel($('div.modify-box'));
		BootstrapDialog.msg(data.body, BootstrapDialog.TYPE_SUCCESS);
		findListInfo();
	}, 'json');
}
/**
 * 提示并删除数据
 */
function menuDelete(id){
	if(!id) return;
	BootstrapDialog.confirm("请确认是否删除该菜单?<br /><span class='placeholder'>PS: 删除后该菜单关联的角色权限全部将失效, 请谨慎操作!<span>", function(result){
		if(!result) return;
		dialog = BootstrapDialog.isSubmitted();
		$.getJSON(localhostUrl+'mgr/delModule', {id:id}, function(data){
			dialog.close();
			if(!$.isSuccess(data)) return;
			BootstrapDialog.msg(data.body, BootstrapDialog.TYPE_SUCCESS);
			findListInfo();
		});
	});
}

