package com.java.back.utils.fileUtils;

import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * 此类用来操作文件
 * 
 */
public class FileOperations {

	// 保存文件
	public boolean saveFile(File file, String path, String filename, int size) {
		boolean bool = true;
		try {
			InputStream is = new FileInputStream(file);
			File files = new File(path);
			if (!files.exists())
				files.mkdirs();
			OutputStream os = new FileOutputStream(path + "/" + filename);
			byte[] bytefer = new byte[size];
			int length = 0;
			while ((length = is.read(bytefer)) != -1) {
				os.write(bytefer, 0, length);
			}
			os.close();
			is.close();
		} catch (Exception e) {
			bool = false;
		}
		return bool;
	}

	// 保存文件
	public boolean saveFile(byte[] bytefer, String path, String filename) {
		boolean bool = true;
		try {
			File files = new File(path);
			if (!files.exists())
				files.mkdirs();
			OutputStream os = new FileOutputStream(path + "/" + filename);
			os.write(bytefer);
			os.close();
		} catch (Exception e) {
			bool = false;
		}
		return bool;
	}

	/**
	 * 获得指定文件的byte数组
	 * 
	 * @param filePath
	 *            文件绝对路径
	 * @return
	 */
	public static byte[] file2Byte(String filePath) {
		ByteArrayOutputStream bos = null;
		BufferedInputStream in = null;
		try {
			File file = new File(filePath);
			if (!file.exists()) {
				throw new FileNotFoundException("file not exists");
			}
			bos = new ByteArrayOutputStream((int) file.length());
			in = new BufferedInputStream(new FileInputStream(file));
			int buf_size = 1024;
			byte[] buffer = new byte[buf_size];
			int len = 0;
			while (-1 != (len = in.read(buffer, 0, buf_size))) {
				bos.write(buffer, 0, len);
			}
			return bos.toByteArray();
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
			return null;
		} finally {
			try {
				if (in != null) {
					in.close();
				}
				if (bos != null) {
					bos.close();
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}

	/**
	 * 根据byte数组，生成文件
	 * 
	 * @param bfile
	 *            文件数组
	 * @param filePath
	 *            文件存放路径
	 * @param fileName
	 *            文件名称
	 */
	public static void byte2File(byte[] bfile, String filePath, String fileName) {
		BufferedOutputStream bos = null;
		FileOutputStream fos = null;
		File file = null;
		try {
			File dir = new File(filePath);
			if (!dir.exists() && !dir.isDirectory()) {// 判断文件目录是否存在
				dir.mkdirs();
			}
			file = new File(filePath + "/" + fileName);
			fos = new FileOutputStream(file);
			bos = new BufferedOutputStream(fos);
			bos.write(bfile);
		} catch (Exception e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				if (bos != null) {
					bos.close();
				}
				if (fos != null) {
					fos.close();
				}
			} catch (Exception e) {
				System.out.println(e.getMessage());
				e.printStackTrace();
			}
		}
	}

	// 删除文件
	public static boolean delefile(String path, String filename) {
		boolean bool = true;
		try {
			File attachfile = new File(path + "/" + filename);
			if (attachfile.exists()) {
				System.out.println("attachfilefound!attachfilefound!");
				if (!attachfile.delete()) {
					bool = false;
				}
			}
		} catch (Exception e) {
			bool = false;
		}
		return bool;
	}

	/**
	 * 复制文件或者目录,复制前后文件完全一样。
	 * 
	 * @param resFilePath
	 *            源文件路径
	 * @param distFolder
	 *            目标文件夹
	 * @IOException 当操作发生异常时抛出
	 */
	public static void copyFile(String resFilePath, String distFolder)
			throws IOException {
		File resFile = new File(resFilePath);
		File distFile = new File(distFolder);
		if (resFile.isDirectory()) {
			FileUtils.copyDirectoryToDirectory(resFile, distFile);
		} else if (resFile.isFile()) {
			FileUtils.copyFileToDirectory(resFile, distFile, true);
		}
	}

	/**
	 * 文件下载
	 * 
	 * @param path
	 * @param response
	 * @return
	 */
	public HttpServletResponse download(String path,
			HttpServletResponse response) {
		try {
			// path是指欲下载的文件的路径。
			File file = new File(path);
			// 取得文件名。
			String filename = file.getName();

			// 以流的形式下载文件。
			InputStream fis = new BufferedInputStream(new FileInputStream(path));
			byte[] buffer = new byte[fis.available()];
			fis.read(buffer);
			fis.close();
			// 清空response
			response.reset();
			// 设置response的Header
			response.addHeader("Content-Disposition", "attachment;filename="
					+ new String(filename.getBytes()));
			response.addHeader("Content-Length", "" + file.length());
			OutputStream toClient = new BufferedOutputStream(
					response.getOutputStream());
			response.setContentType("application/octet-stream");
			toClient.write(buffer);
			toClient.flush();
			toClient.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return response;
	}

	public void downloadNet(HttpServletResponse response, String path,
			String fileName) throws Exception {
		// 下载网络文件
		int bytesum = 0;
		int byteread = 0;

		URL url = new URL(path);

		try {
			URLConnection conn = url.openConnection();
			InputStream inStream = conn.getInputStream();
			FileOutputStream fs = new FileOutputStream("D:/aa.mp3");

			byte[] buffer = new byte[10240];
			while ((byteread = inStream.read(buffer)) != -1) {
				bytesum += byteread;
				System.out.println(bytesum);
				fs.write(buffer, 0, byteread);

			}
			fs.flush();
			fs.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 创建缩略图
	 * 
	 * @param file
	 *            上传的文件流
	 * @param height
	 *            最小的尺寸
	 * @throws IOException
	 */
	public static void createMiniPic(File file, float width, float height)
			throws IOException {
		Image src = javax.imageio.ImageIO.read(file); // 构造Image对象
		int old_w = src.getWidth(null); // 得到源图宽
		int old_h = src.getHeight(null);
		int new_w = 0;
		int new_h = 0; // 得到源图长
		float tempdouble;
		if (old_w >= old_h) {
			tempdouble = old_w / width;
		} else {
			tempdouble = old_h / height;
		}

		if (old_w >= width || old_h >= height) { // 如果文件小于锁略图的尺寸则复制即可
			new_w = Math.round(old_w / tempdouble);
			new_h = Math.round(old_h / tempdouble);// 计算新图长宽
			while (new_w > width && new_h > height) {
				if (new_w > width) {
					tempdouble = new_w / width;
					new_w = Math.round(new_w / tempdouble);
					new_h = Math.round(new_h / tempdouble);
				}
				if (new_h > height) {
					tempdouble = new_h / height;
					new_w = Math.round(new_w / tempdouble);
					new_h = Math.round(new_h / tempdouble);
				}
			}
			BufferedImage tag = new BufferedImage(new_w, new_h,
					BufferedImage.TYPE_INT_RGB);
			tag.getGraphics().drawImage(src, 0, 0, new_w, new_h, null); // 绘制缩小后的图
			FileOutputStream newimage = new FileOutputStream(file); // 输出到文件流
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(newimage);
			JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(tag);
			param.setQuality((float) (100 / 100.0), true);// 设置图片质量,100最大,默认70
			encoder.encode(tag, param);
			encoder.encode(tag); // 将JPEG编码
			newimage.close();
		}
	}

	/**
	 * 网络文件的保存
	 * 
	 * @param inUrl
	 * @param outUrl
	 */
	public void saveImageToDisk(String inUrl, String outUrl) {
		InputStream inputStream = null;
		inputStream = getInputStream(inUrl);// 调用getInputStream()函数。
		byte[] data = new byte[1024];
		int len = 0;
		FileOutputStream fileOutputStream = null;
		try {
			fileOutputStream = new FileOutputStream(outUrl);// 初始化一个FileOutputStream对象。
			while ((len = inputStream.read(data)) != -1) {// 循环读取inputStream流中的数据，存入文件流fileOutputStream
				fileOutputStream.write(data, 0, len);
			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {// finally函数，不管有没有异常发生，都要调用这个函数下的代码
			if (fileOutputStream != null) {
				try {
					fileOutputStream.close();// 记得及时关闭文件流
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			if (inputStream != null) {
				try {
					inputStream.close();// 关闭输入流
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	public static InputStream getInputStream(String url_path) {
		InputStream inputStream = null;
		HttpURLConnection httpURLConnection = null;
		try {
			URL url = new URL(url_path);// 创建的URL
			if (url != null) {
				httpURLConnection = (HttpURLConnection) url.openConnection();// 打开链接
				httpURLConnection.setConnectTimeout(3000);// 设置网络链接超时时间，3秒，链接失败后重新链接
				httpURLConnection.setDoInput(true);// 打开输入流
				httpURLConnection.setRequestMethod("GET");// 表示本次Http请求是GET方式
				int responseCode = httpURLConnection.getResponseCode();// 获取返回码
				if (responseCode == 200) {// 成功为200
					// 从服务器获得一个输入流
					inputStream = httpURLConnection.getInputStream();
				}
			}

		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return inputStream;

	}

	/**
	 * 查找指定目录下的文件
	 * 
	 * @param filepath
	 * @return
	 */
	public static List findFileName(String filepath) {
		File file = new File(filepath);
		List list = new ArrayList();
		File[] files = file.listFiles();
		String name = "";
		if (files == null) {
			return list;
		}
		for (int i = 0; i < files.length; i++) {
			if (files[i].isFile()) {
				list.add(files[i].getPath());
			} else {
				list.addAll(findFileName(files[i].getPath()));
			}
		}
		return list;
	}

	/**
	 * 图片转化成base64字符串
	 * @param imgFile
	 * @return
	 */
	public static String GetImageStr(String imgFile) {// 将图片文件转化为字节数组字符串，并对其进行Base64编码处理
		InputStream in = null;
		byte[] data = null;
		// 读取图片字节数组
		try {
			in = new FileInputStream(imgFile);
			data = new byte[in.available()];
			in.read(data);
			in.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		// 对字节数组Base64编码
		BASE64Encoder encoder = new BASE64Encoder();
		return encoder.encode(data);// 返回Base64编码过的字节数组字符串
	}

	public static void main(String[] args) {
		
	}
}
