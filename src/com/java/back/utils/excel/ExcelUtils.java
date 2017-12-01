package com.java.back.utils.excel;

import org.apache.poi.hssf.usermodel.DVConstraint;
import org.apache.poi.hssf.usermodel.HSSFDataValidation;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.util.CellRangeAddressList;

public class ExcelUtils {
	
	/**
	 * 设置某些列的值只能输入预制的数据,显示下拉框.
	 * 
	 * @param sheet
	 *            要设置的sheet.
	 * @param textlist
	 *            下拉框显示的内容
	 * @param firstRow
	 *            开始行
	 * @param endRow
	 *            结束行
	 * @param firstCol
	 *            开始列
	 * @param endCol
	 *            结束列
	 * @return 设置好的sheet.
	 */
	public static HSSFSheet setHSSFValidation(HSSFSheet sheet,
			String[] textlist, int firstRow, int endRow, int firstCol,
			int endCol) {
		// 加载下拉列表内容
		DVConstraint constraint = DVConstraint
				.createExplicitListConstraint(textlist);
		// 设置数据有效性加载在哪个单元格上,四个参数分别是：起始行、终止行、起始列、终止列
		CellRangeAddressList regions = new CellRangeAddressList(firstRow,
				endRow, firstCol, endCol);
		// 数据有效性对象
		HSSFDataValidation data_validation_list = new HSSFDataValidation(
				regions, constraint);
		sheet.addValidationData(data_validation_list);
		return sheet;
	}

	/**
	 * 设置单元格上提示
	 * 
	 * @param sheet
	 *            要设置的sheet.
	 * @param promptTitle
	 *            标题
	 * @param promptContent
	 *            内容
	 * @param firstRow
	 *            开始行
	 * @param endRow
	 *            结束行
	 * @param firstCol
	 *            开始列
	 * @param endCol
	 *            结束列
	 * @return 设置好的sheet.
	 */
	public static HSSFSheet setHSSFPrompt(HSSFSheet sheet, String promptTitle,
			String promptContent, int firstRow, int endRow, int firstCol,
			int endCol) {
		// 构造constraint对象
		DVConstraint constraint = DVConstraint
				.createCustomFormulaConstraint("BB1");
		// 四个参数分别是：起始行、终止行、起始列、终止列
		CellRangeAddressList regions = new CellRangeAddressList(firstRow,
				endRow, firstCol, endCol);
		// 数据有效性对象
		HSSFDataValidation data_validation_view = new HSSFDataValidation(
				regions, constraint);
		data_validation_view.createPromptBox(promptTitle, promptContent);
		sheet.addValidationData(data_validation_view);
		return sheet;
	}
}
