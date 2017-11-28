package com.java.back.utils.fileUtils;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.web.multipart.MultipartFile;

import com.java.back.utils.SpringContextUtil;

import sun.misc.BASE64Decoder;

/**
 * 文件的操作
 * 
 * @author JYD
 * 
 */
@SuppressWarnings("serial")
public class FileOperate implements Serializable {
	public FileOperate() {
		System.out.println("init FileOperate");
	}

	private final String imgPath = "/upload/img/";

	/**
	 * 上传文件接口
	 * 
	 * @param file
	 * @param request
	 * @return
	 */
	public static String UploadFile(MultipartFile file,
			HttpServletRequest request) {
		// 文件保存路径
		try {
			String imgName = file.getOriginalFilename();
			String extensionName = imgName.substring(imgName.lastIndexOf('.'));// 后缀名
			// imgName = new Date().getTime() + extensionName;// 重新定义文件名称
			SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
			imgName = df.format(new Date()) + "_" + new Random().nextInt(1000)
					+ "." + extensionName;
			String rootPath = request.getSession().getServletContext()
					.getRealPath("");
			File ufile = new File(rootPath + "/upload/file/" + imgName);
			if (!ufile.exists())
				ufile.mkdirs();
			file.transferTo(ufile);
			return "/upload/file/" + imgName;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	/**
	 * 写一行内容最后换行不覆盖原文件
	 * 
	 * @param name
	 * @param content
	 * @param path
	 */
	public static void writeLineForPath(String name, String content, String path) {
		String fileName = path + "/" + name;
		File file = new File(path);
		if (!file.exists()) {
			file.mkdirs();
		}
		try {
			BufferedWriter out = new BufferedWriter(new FileWriter(fileName,
					true));// 不覆盖原文件内容
			String write = content + "\r\n";// 最后换行
			out.write(write);
			out.close();
		} catch (IOException e) {
			e.printStackTrace();
			System.out.println("写文件 " + fileName + " 失败!");
		}
	}

	/**
	 * 新建目录
	 * 
	 * @param folderPath
	 *            String 如 c:/fqf
	 * @return boolean
	 */
	public void newFolder(String folderPath) {
		try {
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			if (!myFilePath.exists()) {
				myFilePath.mkdir();
			}
		} catch (Exception e) {
			System.out.println("新建目录操作出错 ");
			e.printStackTrace();
		}
	}

	/**
	 * 对字节数组字符串进行Base64解码并生成图片(后缀名不变)
	 * 
	 * @param imgStr
	 *            Base64字符串
	 * @param imgName
	 *            图片名称
	 * @param request
	 * @return boolean
	 */
	public static boolean saveCardImage(String base64, String imgName,
			HttpServletRequest request) {
		if (base64 == null) {
			return false;
		}
		if (base64.indexOf("base64") != -1) {
			base64 = base64.substring(base64.indexOf("base64") + 7,
					base64.length());
		}
		String extensionName = imgName.substring(imgName.lastIndexOf('.'));
		imgName = new Date().getTime() + extensionName;
		String rootPath = request.getSession().getServletContext()
				.getRealPath("");
		extensionName = rootPath + "/upload/img/" + imgName;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] bytes = decoder.decodeBuffer(base64);
			for (int i = 0; i < bytes.length; ++i) {
				if (bytes[i] < 0) {
					bytes[i] += 256;
				}
			}
			File file = new File(extensionName);
			if (!file.exists()) {
				file.getParentFile().mkdir();
			}
			OutputStream out = new FileOutputStream(file);
			out.write(bytes);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * @Descriptionmap 对字节数组字符串进行Base64解码并生成图片(格式皆转为jpg)
	 * @param base64
	 *            图片Base64数据
	 * @param path
	 *            图片路径以及名称
	 * @return
	 */
	public static boolean base64ToImage(String base64, String path) {
		// 图像数据为空
		if (base64 == null) {
			return false;
		}
		if (base64.indexOf("base64") != -1) {
			base64 = base64.substring(base64.indexOf("base64") + 7,
					base64.length());
		}
		// Base64解码
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] bytes = decoder.decodeBuffer(base64);
			for (int i = 0; i < bytes.length; i++) {
				// 调整异常数据
				if (bytes[i] < 0) {
					bytes[i] += 256;
				}
			}
			File file = new File(path);
			File file1 = new File(path.substring(0, path.lastIndexOf("/")));
			if (!file1.exists()) {
				file1.mkdirs();
			}
			// 生成jpeg图片
			OutputStream out = new FileOutputStream(file);
			out.write(bytes);
			out.flush();
			out.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * 将图片格式转成jpg的支持( GIF->JPG GIF->PNG PNG->GIF(X) PNG->JPG)
	 * 
	 * @param src1
	 * @param result
	 * @throws IOException
	 */
	public static void imageToJPG(String src1, String result)
			throws IOException {
		File f = new File(src1);
		f.canRead();
		BufferedImage src = ImageIO.read(f);
		ImageIO.write(src, "jpg", new File(result));
	}

	public static void main(String[] args) {
		delFile("/upload/img/20171025154713.jpg");
	}

	/**
	 * 新建文件
	 * 
	 * @param filePathAndName
	 *            String 文件路径及名称 如c:/fqf.txt
	 * @param fileContent
	 *            String 文件内容
	 * @return boolean
	 */
	public void newFile(String filePathAndName, String fileContent) {

		try {
			String filePath = filePathAndName;
			filePath = filePath.toString();
			File myFilePath = new File(filePath);
			if (!myFilePath.exists()) {
				myFilePath.createNewFile();
			}
			FileWriter resultFile = new FileWriter(myFilePath);
			PrintWriter myFile = new PrintWriter(resultFile);
			String strContent = fileContent;
			myFile.println(strContent);
			resultFile.close();
		} catch (Exception e) {
			System.out.println("新建目录操作出错 ");
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param filePathAndName
	 *            String 文件路径及名称 如c:/fqf.txt
	 * @param fileContent
	 *            String
	 * @return boolean
	 */
	public static void delFile(String filePathAndName) {
		try {
			java.io.File myDelFile = new java.io.File(filePathAndName);
			myDelFile.delete();
		} catch (Exception e) {
			System.out.println("删除文件操作出错 ");
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件夹
	 * 
	 * @param filePathAndName
	 *            String 文件夹路径及名称 如c:/fqf
	 * @param fileContent
	 *            String
	 * @return boolean
	 */
	public void delFolder(String folderPath) {
		try {
			delAllFile(folderPath); // 删除完里面所有内容
			String filePath = folderPath;
			filePath = filePath.toString();
			java.io.File myFilePath = new java.io.File(filePath);
			myFilePath.delete(); // 删除空文件夹
		} catch (Exception e) {
			System.out.println("删除文件夹操作出错 ");
			e.printStackTrace();
		}
	}

	/**
	 * 删除文件夹里面的所有文件
	 * 
	 * @param path
	 *            String 文件夹路径 如 c:/fqf
	 */
	public void delAllFile(String path) {
		File file = new File(path);
		if (!file.exists()) {
			return;
		}
		if (!file.isDirectory()) {
			return;
		}
		String[] tempList = file.list();
		File temp = null;
		for (int i = 0; i < tempList.length; i++) {
			if (path.endsWith(File.separator)) {
				temp = new File(path + tempList[i]);
			} else {
				temp = new File(path + File.separator + tempList[i]);
			}
			if (temp.isFile()) {
				temp.delete();
			}
			if (temp.isDirectory()) {
				delAllFile(path + "/" + tempList[i]);// 先删除文件夹里面的文件
				delFolder(path + "/" + tempList[i]);// 再删除空文件夹
			}
		}
	}

	/**
	 * 复制单个文件
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf.txt
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf.txt
	 * @return boolean
	 */
	public void copyFile(String oldPath, String newPath) {
		try {
			int bytesum = 0;
			int byteread = 0;
			File oldfile = new File(oldPath);
			if (oldfile.exists()) { // 文件存在时
				InputStream inStream = new FileInputStream(oldPath); // 读入原文件
				FileOutputStream fs = new FileOutputStream(newPath);
				byte[] buffer = new byte[1444];
				// int length;
				while ((byteread = inStream.read(buffer)) != -1) {
					bytesum += byteread; // 字节数 文件大小
					System.out.println(bytesum);
					fs.write(buffer, 0, byteread);
				}
				inStream.close();
			}
		} catch (Exception e) {
			System.out.println("复制单个文件操作出错 ");
			e.printStackTrace();
		}
	}

	/**
	 * 复制整个文件夹内容
	 * 
	 * @param oldPath
	 *            String 原文件路径 如：c:/fqf
	 * @param newPath
	 *            String 复制后路径 如：f:/fqf/ff
	 * @return boolean
	 */
	public void copyFolder(String oldPath, String newPath) {
		try {
			(new File(newPath)).mkdirs(); // 如果文件夹不存在 则建立新文件夹
			File a = new File(oldPath);
			String[] file = a.list();
			File temp = null;
			for (int i = 0; i < file.length; i++) {
				if (oldPath.endsWith(File.separator)) {
					temp = new File(oldPath + file[i]);
				} else {
					temp = new File(oldPath + File.separator + file[i]);
				}

				if (temp.isFile()) {
					FileInputStream input = new FileInputStream(temp);
					FileOutputStream output = new FileOutputStream(newPath
							+ "/" + (temp.getName()).toString());
					byte[] b = new byte[1024 * 5];
					int len;
					while ((len = input.read(b)) != -1) {
						output.write(b, 0, len);
					}
					output.flush();
					output.close();
					input.close();
				}
				if (temp.isDirectory()) {// 如果是子文件夹
					copyFolder(oldPath + "/" + file[i], newPath + "/" + file[i]);
				}
			}
		} catch (Exception e) {
			System.out.println("复制整个文件夹内容操作出错 ");
			e.printStackTrace();
		}
	}

	/**
	 * 移动文件到指定目录
	 * 
	 * @param oldPath
	 *            String 如：c:/fqf.txt
	 * @param newPath
	 *            String 如：d:/fqf.txt
	 */
	public void moveFile(String oldPath, String newPath) {
		copyFile(oldPath, newPath);
		delFile(oldPath);
	}

	/**
	 * 移动文件到指定目录
	 * 
	 * @param oldPath
	 *            String 如：c:/fqf.txt
	 * @param newPath
	 *            String 如：d:/fqf.txt
	 */
	public void moveFolder(String oldPath, String newPath) {
		copyFolder(oldPath, newPath);
		delFolder(oldPath);
	}

	/**
	 * 在文件里面的指定行插入一行数据
	 * 
	 * @param inFile
	 *            文件
	 * @param lineno
	 *            行号
	 * @param lineToBeInserted
	 *            要插入的数据
	 * @throws Exception
	 *             IO操作引发的异常
	 */
	public void insertStringInFile(File inFile, int lineno,
			String lineToBeInserted) throws Exception {
		// 临时文件
		File outFile = File.createTempFile("name", ".tmp");

		// 输入
		FileInputStream fis = new FileInputStream(inFile);
		BufferedReader in = new BufferedReader(new InputStreamReader(fis));

		// 输出
		FileOutputStream fos = new FileOutputStream(outFile);
		PrintWriter out = new PrintWriter(fos);

		// 保存一行数据
		String thisLine;
		// 行号从1开始
		int i = 1;
		while ((thisLine = in.readLine()) != null) {
			// 如果行号等于目标行，则输出要插入的数据
			if (i == lineno) {
				out.println(lineToBeInserted);
			}
			// 输出读取到的数据
			out.println(thisLine);
			// 行号增加
			i++;
		}
		out.flush();
		out.close();
		in.close();

		// 删除原始文件
		inFile.delete();
		// 把临时文件改名为原文件名
		outFile.renameTo(inFile);
	}

	/**
	 * 精确查找某个字符串在哪一行
	 * 
	 * @param fileName
	 *            文件
	 * @return
	 * @throws Exception
	 *             IO操作引发的异常
	 */
	public static int rtStringLineNum(File fileName, String fileStr) {
		int lineNum = 0;

		try {
			BufferedReader in = new BufferedReader(new FileReader(fileName));
			String str;

			while ((str = in.readLine()) != null) {
				System.out.println(str);
				lineNum++;
				if (str.equals(fileStr)) {
					return lineNum;
				}
			}
			in.close();
		} catch (IOException e) {
			e.getStackTrace();
		}

		return -1;
	}

	/**
	 * 读文件的某行
	 * 
	 * @param fileName
	 *            文件
	 * @return String
	 * @throws IOException
	 * @throws Exception
	 *             IO操作引发的异常
	 */
	public String readString(String fileName, int line, int column)
			throws IOException {

		String sLine = "";

		BufferedReader bf = new BufferedReader(new FileReader(
				new File(fileName)));
		if (line != 0) {
			for (int i = 0; i < line; i++) {
				bf.readLine();
			}
		}
		// 读取指定行
		sLine = bf.readLine();
		bf.close();

		return sLine.substring(column);
	}

	/**
	 * 读文件的某行
	 * 
	 * @param fileName
	 *            文件
	 * @return String
	 * @throws IOException
	 * @throws Exception
	 *             IO操作引发的异常
	 */
	public String readString(String fileName, int line) throws IOException {
		String sLine = "";
		BufferedReader bf = new BufferedReader(new FileReader(
				new File(fileName)));
		String s = null;
		while ((s = bf.readLine()) != null) {// 使用readLine方法，一次读一行
			sLine += s;
		}
		bf.close();
		return sLine;
	}

	/**
	 * 读取文件内容
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static String readFile2String(String fileName) throws IOException {
		String encoding = "GBK";
		String sLine = "";
		try {
			File file = new File(fileName);
			if (file.isFile() && file.exists()) { // 判断文件是否存在
				InputStreamReader read = new InputStreamReader(
						new FileInputStream(file), encoding);// 考虑到编码格式
				BufferedReader bufferedReader = new BufferedReader(read);
				String lineTxt = "";
				while ((lineTxt = bufferedReader.readLine()) != null) {
					sLine += lineTxt;
				}
				read.close();
			} else {
				System.out.println("找不到指定的文件");
			}
		} catch (Exception e) {
			System.out.println("读取文件内容出错");
			e.printStackTrace();
		}
		return sLine;
	}

}
