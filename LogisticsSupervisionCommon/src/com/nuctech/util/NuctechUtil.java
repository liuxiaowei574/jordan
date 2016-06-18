package com.nuctech.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.Enumeration;
import java.util.List;
import java.util.logging.Logger;

import javax.imageio.ImageIO;

import jxl.Workbook;

import org.apache.commons.io.FileUtils;
import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;
import org.apache.tools.zip.ZipOutputStream;

/**
 * 工具类
 * 
 * @author liuchao
 * @version 1.0
 */
public class NuctechUtil {
	private static final Logger logger = Logger.getLogger(NuctechUtil.class
			.getName());

	/**
	 * 判断String对象是否不为空
	 * 
	 * @param obj
	 * @return 若为null或"",返回false，否则返回true;
	 */
	public static boolean isNotNull(String obj) {
		if (obj == null) {
			return false;
		} else if (obj.length() == 0) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 判断String对象是否为空
	 * 
	 * @param obj
	 * @return 若为null或"",返回true，否则返回false;
	 */
	public static boolean isNull(String obj) {
		if (obj == null) {
			return true;
		} else if ("".equals(obj)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断一个对象是否不为空
	 * 
	 * @param obj
	 * @return 若为null,返回false，否则返回true;
	 */
	public static boolean isNotNull(Object obj) {
		if (obj == null) {
			return false;
		} else {
			return true;
		}
	}

	/**
	 * 判断一个对象是否为空
	 * 
	 * @param obj
	 * @return 若为null,返回true，否则返回false;
	 */
	public static boolean isNull(Object obj) {
		if (obj == null) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 判断指定的集合是否为空。
	 * 
	 * @param coll
	 * @return 如果指定的集合为<code>null</code> 或 size = 0，则返回true，否则返回false;
	 */
	public static boolean isEmpty(Collection<?> coll) {
		return null == coll || coll.isEmpty();
	}

	/**
	 * 将文件上传到指定路径
	 * 
	 * @param targetDirectory
	 *            String 文件存放路径
	 * @param targetFileName
	 *            String 新的文件名
	 * @param sourceFile
	 *            File 要上传的文件
	 * @return 目标文件
	 */
	public static File uploadFile(String targetDirectory,
			String targetFileName, File sourceFile) throws Exception {
		File target = new File(targetDirectory, targetFileName);
		FileUtils.copyFile(sourceFile, target);
		Logger logger = Logger.getLogger(NuctechUtil.class.getName());
		logger.info("上传文件：" + target.getAbsolutePath());
		return target;
	}

	/**
	 * 为文件重新命名
	 * 
	 * @param fileName
	 *            String 原文件名
	 * @return String 新文件名
	 */
	public static String renameFileName(String fileName) throws Exception {
		String newFileName = String.valueOf(new Date().getTime());
		int position = -1;
		position = fileName.lastIndexOf(".");
		if (position == -1) { // 若上传文件无扩展名
			return fileName + "_" + newFileName;
		}
		String extension = fileName.substring(position);
		String fileN = fileName.substring(0, position);
		return fileN + "_" + newFileName + extension;
	}

	/**
	 * 得到文件上传时候的名称，即重命名前的名称
	 * 
	 * @param fileName
	 *            String 原文件名
	 * @return String 新文件名
	 */
	public static String getFileName(String fileName) {
		int position = -1;
		position = fileName.lastIndexOf(".");
		// 得到文件扩展名
		String extension = "";
		if (position != -1) { // 若上传文件无扩展名
			extension = fileName.substring(position);
		}
		// 得到文件上传时的名称
		position = fileName.lastIndexOf("_");
		String newFileName = "";
		if (position != -1) {
			newFileName = fileName.substring(0, position);
		}
		if (isNotNull(newFileName)) {
			return newFileName + extension;
		} else {
			return fileName;
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param filePath
	 *            文件存放路径
	 * @return boolean 是否删除成功
	 * @throws Exception
	 */
	public static boolean deleteFile(String filePath) throws Exception {
		if (isNull(filePath)) {
			return false;
		}
		File file = new File(filePath);
		if (file.exists() && file.isFile()) { // 若文件存在，则删除文件
			boolean isSuccess = file.delete();
			Logger logger = Logger.getLogger(NuctechUtil.class.getName());
			logger.info("删除文件：" + file.getAbsolutePath());
			return isSuccess;
		} else {
			return false;
		}
	}

	/**
	 * 删除文件
	 * 
	 * @param parentPath
	 *            文件所在文件夹
	 * @param fileName
	 *            文件名称
	 * @return boolean 是否删除成功
	 * @throws Exception
	 */
	public static boolean deleteFile(String parentPath, String fileName)
			throws Exception {
		if (isNull(fileName)) {
			return false;
		}
		File file = new File(parentPath, fileName);
		if (file.exists() && file.isFile()) { // 若文件存在，则删除文件
			boolean isSuccess = file.delete();
			Logger logger = Logger.getLogger(NuctechUtil.class.getName());
			logger.info("删除文件：" + file.getAbsolutePath());
			return isSuccess;
		} else {
			return false;
		}
	}

	/**
	 * 删除文件夹及其下所有文件
	 * 
	 * @param filePath
	 *            文件存放路径
	 * @return boolean 是否删除成功
	 * @throws Exception
	 */
	public static boolean deleteFloder(String floderPath) throws Exception {
		File file = new File(floderPath);
		if (file.exists() && file.isDirectory()) { // 若文件存在，则删除文件
			String[] tempList = file.list();
			File temp = null;
			for (int i = 0; i < tempList.length; i++) {
				if (floderPath.endsWith(File.separator)) {
					temp = new File(floderPath + tempList[i]);
				} else {
					temp = new File(floderPath + File.separator + tempList[i]);
				}
				if (temp.isFile()) {
					temp.delete();
				}
				if (temp.isDirectory()) {
					deleteFloder(floderPath + "/" + tempList[i]);// 先删除文件夹里面的文件
				}
			}
			boolean isSuccess = file.delete();
			Logger logger = Logger.getLogger(NuctechUtil.class.getName());
			logger.info("删除文件夹及其下所有子文件：" + file.getAbsolutePath());
			return isSuccess;
		} else {
			return false;
		}
	}

	/**
	 * 压缩文件-由于out要在递归调用外,所以封装一个方法用来 调用ZipFiles(ZipOutputStream out,String
	 * path,File... srcFiles)
	 * 
	 * @param os
	 *            输出文件流
	 * @param path
	 *            压缩包路径
	 * @param srcFiles
	 *            要压缩文件数组
	 * @throws IOException
	 * @author zuotian
	 */
	public static void ZipFiles(OutputStream os, String path, File... srcFiles)
			throws IOException {
		ZipOutputStream out = new ZipOutputStream(os);
		ZipFiles(out, path, srcFiles);
		out.close();
		os.close(); // 关闭流
	}

	/**
	 * @Title: ZipFiles
	 * @Description: TODO(压缩文件)
	 * @param out
	 *            输出压缩包文件流
	 * @param path
	 *            文件路径
	 * @param srcFiles
	 *            要压缩文件数组
	 */
	public static void ZipFiles(ZipOutputStream out, String path,
			File... srcFiles) {
		if (path.length() > 0) {
			path = path.replaceAll("\\*", "/");
			if (!path.endsWith("/")) {
				path += "/";
			}
		}
		byte[] buf = new byte[1024];

		for (int i = 0; i < srcFiles.length; i++) {
			try {
				if (srcFiles[i].isDirectory()) {
					File[] files = srcFiles[i].listFiles();
					String srcPath = srcFiles[i].getName();
					srcPath = srcPath.replaceAll("\\*", "/");
					if (!srcPath.endsWith("/")) {
						srcPath += "/";
					}
					out.putNextEntry(new ZipEntry(path + srcPath));
					ZipFiles(out, path + srcPath, files);
				} else {
					FileInputStream in = new FileInputStream(srcFiles[i]);
					out.putNextEntry(new ZipEntry(path + srcFiles[i].getName()));
					int len;
					while ((len = in.read(buf)) > 0) {
						out.write(buf, 0, len);
						out.flush();
					}
					out.closeEntry();
					in.close();
				}
			} catch (Exception e) {
				logger.info(String.format("删除文件错误,异常信息:%s", e.getMessage()));

				continue;
			}
		}

	}

	/**
	 * 
	 * @Title: unZipFiles
	 * @Description: TODO(导入ZIP包 根据流水号解压包中指定的文件)
	 * @param zipFile
	 * @param descDir
	 * @throws IOException
	 */
	@SuppressWarnings("rawtypes")
	public static void unZipFiles(File zipFile, String descDir,
			String fileSerialNo) throws IOException {
		File pathFile = new File(descDir);
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}

		// 解压包对象
		ZipFile zip = new ZipFile(zipFile, "gbk");
		// 压缩包中的元素
		ZipEntry entry;
		// 文件流
		InputStream in;
		// 压缩包中元素文件名称
		String zipEntryName;
		int li;
		File file;
		// 输出流
		OutputStream out;
		byte[] buf1 = new byte[1024];
		for (Enumeration entries = zip.getEntries(); entries.hasMoreElements();) {
			entry = (ZipEntry) entries.nextElement();
			zipEntryName = entry.getName();

			if (!zipEntryName.contains(fileSerialNo)
					|| entry.getName().contains("Thumbs")) {
				continue;
			}
			in = zip.getInputStream(entry);

			String outPath = (descDir + zipEntryName).replaceAll("\\*", "/");
			;
			// 判断路径是否存在,不存在则创建文件路径
			li = outPath.lastIndexOf('/') == -1 ? 0 : outPath.lastIndexOf('/');
			file = new File(outPath.substring(0, li));
			if (!file.exists()) {
				file.mkdirs();
			}
			// 判断文件全路径是否为文件夹,如果是上面已经上传,不需要解压
			if (new File(outPath).isDirectory()) {
				continue;
			}

			out = new FileOutputStream(outPath);

			int len;
			while ((len = in.read(buf1)) > 0) {
				out.write(buf1, 0, len);
			}
			in.close();
			out.close();
		}
	}

	/**
	 * @Title: getFileType
	 * @Description: TODO(判断文件类型)
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String getFileType(InputStream in) throws IOException {
		byte[] b = new byte[4];
		in.read(b, 0, b.length);
		String type = bytesToHexString(b).toUpperCase();
		in.close();
		return type;
	}

	/**
	 * @Title: readExcelFile
	 * @Description: TODO(提取出导入压缩包中的Excel文件)
	 * @param zipFile
	 * @param descDir
	 * @return Workbook
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	public static Workbook readExcelFile(File zipFile, String descDir)
			throws Exception {
		File pathFile = new File(descDir);

		Workbook rwb = null;
		if (!pathFile.exists()) {
			pathFile.mkdirs();
		}
		// 解压包对象
		ZipFile zip = new ZipFile(zipFile, "UTF-8");
		;
		// 压缩包中的元素
		ZipEntry entry;
		// 文件流
		InputStream in;
		// 提取文件类型
		String type;
		for (Enumeration entries = zip.getEntries(); entries.hasMoreElements();) {
			entry = (ZipEntry) entries.nextElement();
			in = zip.getInputStream(entry);
			// 提取文件类型
			type = getFileType(in);
			// 因为在提取文件类型时读取的字节数不同，下面要重新读取文件流
			in = zip.getInputStream(entry);

			if (!type.contains("D0CF11E0")
					|| entry.getName().contains("Thumbs")) {
				in.close();
				continue;
			} else {
				rwb = Workbook.getWorkbook(in);
				in.close();
			}
		}
		return rwb;
	}

	/**
	 * @Title: bytesToHexString
	 * @Description: TODO(yte数组转换成16进制字符串)
	 * @param src
	 * @return
	 */
	public static String bytesToHexString(byte[] src) {
		StringBuilder stringBuilder = new StringBuilder();
		if (src == null || src.length <= 0) {
			return null;
		}
		for (int i = 0; i < src.length; i++) {
			int v = src[i] & 0xFF;
			String hv = Integer.toHexString(v);
			if (hv.length() < 2) {
				stringBuilder.append(0);
			}
			stringBuilder.append(hv);
		}
		return stringBuilder.toString();
	}

	/**
	 * @Title: inputstreamtofile
	 * @Description: TODO(InputStream转换成File)
	 * @param ins
	 * @param file
	 */
	public static void inputstreamtofile(InputStream ins, File file) {
		try {
			OutputStream os = new FileOutputStream(file);
			int bytesRead = 0;
			byte[] buffer = new byte[8192];
			while ((bytesRead = ins.read(buffer, 0, 8192)) != -1) {
				os.write(buffer, 0, bytesRead);
			}
			os.close();
			ins.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 读取某个文件夹下的所有文件
	 */
	public static List<File> readfile(String filepath) {
		List<File> files = new ArrayList<File>();
		File file = new File(filepath);
		if (file.isDirectory()) {
			String[] filelist = file.list();
			for (int i = 0; i < filelist.length; i++) {
				File readfile = new File(filepath + "\\" + filelist[i]);
				if (!readfile.isDirectory()) {
					files.add(readfile);
				} else if (readfile.isDirectory()) {
					readfile(filepath + "\\" + filelist[i]);
				}
			}

		}

		return files;
	}

	/**
	 * 读取某个文件夹下的所有文件
	 */
	public static List<File> readFile(List<File> files, String filepath) {
		File file = new File(filepath);
		if (file.isDirectory()) {
			String[] filelist = file.list();
			for (int i = 0; i < filelist.length; i++) {
				File readfile = new File(filepath + "\\" + filelist[i]);
				if (!readfile.isDirectory()) {
					files.add(readfile);
				} else if (readfile.isDirectory()) {
					readFile(files, filepath + "\\" + filelist[i]);
				}
			}

		}

		return files;
	}

	/**
	 * 读取某个文件夹下的所有文件夹
	 */
	public static List<String> readDirectory(String filepath) {
		List<String> filePaths = new ArrayList<String>();
		File file = new File(filepath);
		if (file.isDirectory()) {
			String[] filelist = file.list();
			for (int i = 0; i < filelist.length; i++) {
				File readfile = new File(filepath + "\\" + filelist[i]);
				if (readfile.isDirectory()) {
					filePaths.add(readfile.getName());
				}
			}
		}

		return filePaths;
	}

	public static boolean getFormatName(File resFile) {
		boolean flag = true;
		BufferedImage bi;
		try {
			bi = ImageIO.read(resFile);
			if (bi == null) {
				flag = false;
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flag;

	}

}
