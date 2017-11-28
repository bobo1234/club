package com.java.back.utils;

import java.util.ArrayList;
import java.util.List;

import com.java.back.model.TeModule;

/**
 * 根据实体类生成属性表格菜单 实体类有两个必须属性（set get方法） 会自动按照（id/pid）顺序递归对list集合内容排序 String
 * depart_id;//唯一id String parentid;//父id
 * 
 * @author Administrator
 */
public class TreeList {
	private List<TeModule> resultNodes = new ArrayList<TeModule>();// 树形结构排序之后list内容
	private List<TeModule> nodes; // 传入list参数

	public TreeList(List<TeModule> nodes) {// 通过构造函数初始化
		this.nodes = nodes;
	}

	/**
	 * 构建树形结构list
	 * 
	 * @return 返回树形结构List列表
	 */
	public List<TeModule> buildTree() {
		for (TeModule node : nodes) {
//			String id = String.valueOf(node.getModuleId());
			if (node.getModuleSuperCode().equals("0")) {// 通过循环一级节点 就可以通过递归获取二级以下节点
				resultNodes.add(node);// 添加一级节点
				build(node);// 递归获取二级、三级、。。。节点
			}
		}
		return resultNodes;
	}

	/**
	 * 递归循环子节点
	 * 
	 * @param node
	 *            当前节点
	 */
	private void build(TeModule node) {
		List<TeModule> children = getChildren(node);
		if (!children.isEmpty()) {// 如果存在子节点
			for (TeModule child : children) {// 将子节点遍历加入返回值中
				resultNodes.add(child);
				build(child);
			}
		}
	}

	/**
	 * @param node
	 * @return 返回
	 */
	private List<TeModule> getChildren(TeModule node) {
		List<TeModule> children = new ArrayList<TeModule>();
		String code = String.valueOf(node.getModuleCode());
		for (TeModule child : nodes) {
			if (code.equals(child.getModuleSuperCode())) {// 如果id等于父id
				children.add(child);// 将该节点加入循环列表中
			}
		}
		return children;
	}

}
