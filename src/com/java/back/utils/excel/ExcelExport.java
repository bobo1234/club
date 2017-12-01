package com.java.back.utils.excel;


import java.io.IOException;
import java.io.OutputStream;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.java.back.utils.StringUtil;



public class ExcelExport {


	/**
	 * 将list内容转换为Excel文件内容(导出方法)
	 */
	@SuppressWarnings("deprecation")
	public void exportExcel(List<Object[]> plist, String[] excelHeader,
			String tableName,HttpServletResponse response) {

		// excel的隶属
		short cellNumber = (short) excelHeader.length;
		// 创建一个Excel
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFCell cell = null;// 列
		HSSFRow row = null;// 行
		HSSFCellStyle style = workbook.createCellStyle();// 设置表头的类型
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFCellStyle style1 = workbook.createCellStyle();// 设置数据类型
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont font = workbook.createFont();// 设置字体
		HSSFSheet sheet = workbook.createSheet("sheet1");// 设置一个sheet

		row = sheet.createRow(0);
		row.setHeight((short) 300);
		for (int k = 0; k < cellNumber; k++) {
			cell = row.createCell(k);// 创建第0行，第k列
			cell.setCellValue(excelHeader[k]);// 设置第0行第k列的值
			sheet.setColumnWidth(k, (short) 4000);// 设置列的宽度
//			font.setColor(HSSFFont.BOLDWEIGHT_BOLD);// 设置单元格字体的样色
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗字体
//			font.setFontHeight((short) 250);// 设置字体的高度
			style1.setFont(font);// 设置字体的风格
			cell.setCellStyle(style1);
		}
		for (int i = 0; i < plist.size(); i++) {

			Object[] p = plist.get(i);
			row = sheet.createRow((short) (i + 1));// 创建第i+1行
			row.setHeight((short) 300);// 设置行高

			for (int j = 0; j < p.length; j++) {
				if (p[j] != null) {
					cell = row.createCell((short) j);// 创建第i+1行的第j+1列
					cell.setCellValue(p[j] + "");// 设置第i+1行第j+1列的值
					cell.setCellStyle(style);// 设置风格
				}
			}

		}

		OutputStream out = null;// 创建一个输出流对象
		try {
			tableName=new String((tableName).getBytes("GB2312"), "iso8859-1");//文件名称设置为中文，兼容
			out = response.getOutputStream();
			response.setHeader("Content-disposition", "attachment;filename="
					+ tableName + ".xls");// filename是下载的xls的名；
			response.setContentType("application/msexcel;charset=UTF-8");// 设置类型
			response.setHeader("Pragma", "NO-cache");// 设置头
			response.setHeader("Cache-Control", "NO-cache");// 设置头
			response.setDateHeader("Expires", 0);// 设置日期头
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	/**
	 * 将list内容转换为Excel文件内容(导出方法),自定义sheet
	 * @param plist
	 * @param excelHeader
	 * @param sheetName
	 * @param tableName
	 * @param response
	 */
	@SuppressWarnings("deprecation")
	public void exportExcel(List<Object[]> plist, String[] excelHeader,String sheetName,
			String tableName,HttpServletResponse response) {

		// excel的隶属
		short cellNumber = (short) excelHeader.length;
		// 创建一个Excel
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFCell cell = null;// 列
		HSSFRow row = null;// 行
		HSSFCellStyle style = workbook.createCellStyle();// 设置表头的类型
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFCellStyle style1 = workbook.createCellStyle();// 设置数据类型
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont font = workbook.createFont();// 设置字体
		if (StringUtil.isEmpty(sheetName)) {
			sheetName="sheet1";
		}
		HSSFSheet sheet = workbook.createSheet(sheetName);// 设置一个sheet

		row = sheet.createRow(0);
		row.setHeight((short) 300);
		for (int k = 0; k < cellNumber; k++) {
			cell = row.createCell(k);// 创建第0行，第k列
			cell.setCellValue(excelHeader[k]);// 设置第0行第k列的值
			sheet.setColumnWidth(k, (short) 4000);// 设置列的宽度
//			font.setColor(HSSFFont.BOLDWEIGHT_BOLD);// 设置单元格字体的样色
			font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗字体
//			font.setFontHeight((short) 250);// 设置字体的高度
			style1.setFont(font);// 设置字体的风格
			cell.setCellStyle(style1);
		}
		for (int i = 0; i < plist.size(); i++) {

			Object[] p = plist.get(i);
			row = sheet.createRow((short) (i + 1));// 创建第i+1行
			row.setHeight((short) 300);// 设置行高

			for (int j = 0; j < p.length; j++) {
				if (p[j] != null) {
					cell = row.createCell((short) j);// 创建第i+1行的第j+1列
					cell.setCellValue(p[j] + "");// 设置第i+1行第j+1列的值
					cell.setCellStyle(style);// 设置风格
				}
			}

		}

		OutputStream out = null;// 创建一个输出流对象
		try {
			tableName=new String((tableName).getBytes("GB2312"), "iso8859-1");//文件名称设置为中文，兼容
			out = response.getOutputStream();
			response.setHeader("Content-disposition", "attachment;filename="
					+ tableName + ".xls");// filename是下载的xls的名；
			response.setContentType("application/msexcel;charset=UTF-8");// 设置类型
			response.setHeader("Pragma", "NO-cache");// 设置头
			response.setHeader("Cache-Control", "NO-cache");// 设置头
			response.setDateHeader("Expires", 0);// 设置日期头
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * 针对多个sheet
	 * @param plist
	 * @param excelHeader
	 * @param sheetsName
	 * @param tableName
	 * @param response
	 */
	public void exportExcel(List<List<Object[]>> plist, String[] excelHeader,String [] sheetsName,
			String tableName,HttpServletResponse response) {

		// excel的隶属
		short cellNumber = (short) excelHeader.length;
		// 创建一个Excel
		HSSFWorkbook workbook = new HSSFWorkbook();
		HSSFCell cell = null;// 列
		HSSFRow row = null;// 行
		HSSFCellStyle style = workbook.createCellStyle();// 设置表头的类型
		style.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFCellStyle style1 = workbook.createCellStyle();// 设置数据类型
		style1.setAlignment(HSSFCellStyle.ALIGN_CENTER);
		HSSFFont font = workbook.createFont();// 设置字体
		HSSFSheet sheet;
		for (int a = 0; a < sheetsName.length; a++) {
			sheet = workbook.createSheet(sheetsName[a]);// 设置一个sheet
			row = sheet.createRow(0);
			row.setHeight((short) 300);
			for (int k = 0; k < cellNumber; k++) {
				cell = row.createCell(k);// 创建第0行，第k列
				cell.setCellValue(excelHeader[k]);// 设置第0行第k列的值
				sheet.setColumnWidth(k, (short) 4000);// 设置列的宽度
				font.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);//加粗字体
//				font.setFontHeight((short) 250);// 设置字体的高度
				style1.setFont(font);// 设置字体的风格
				cell.setCellStyle(style1);
			}
			for (int i = 0; i < plist.get(a).size(); i++) {
				Object[] p = plist.get(a).get(i);
				row = sheet.createRow((short) (i + 1));// 创建第i+1行
				row.setHeight((short) 300);// 设置行高

				for (int j = 0; j < p.length; j++) {
					if (p[j] != null) {
						cell = row.createCell((short) j);// 创建第i+1行的第j+1列
						cell.setCellValue(p[j] + "");// 设置第i+1行第j+1列的值
						cell.setCellStyle(style);// 设置风格
					}
				}

			}
		}
		

		OutputStream out = null;// 创建一个输出流对象
		try {
			tableName=new String((tableName).getBytes("GB2312"), "iso8859-1");//文件名称设置为中文，兼容
			out = response.getOutputStream();
			response.setHeader("Content-disposition", "attachment;filename="
					+ tableName + ".xls");// filename是下载的xls的名；
			response.setContentType("application/msexcel;charset=UTF-8");// 设置类型
			response.setHeader("Pragma", "NO-cache");// 设置头
			response.setHeader("Cache-Control", "NO-cache");// 设置头
			response.setDateHeader("Expires", 0);// 设置日期头
			workbook.write(out);
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (out != null) {
					out.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
}
