package com.android.dev.utils;

import android.app.ActivityManager;
import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.text.TextUtils;
import android.util.Log;

import com.android.common.utils.KGLog;

import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.lang.reflect.InvocationTargetException;

public class FileUtil {
	//Environment.getExternalStorageDirectory() 在有些机型上甚至得不到数据
	public static String getRootPath(){
		String rootPath = null;
		if( Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED)) {
			rootPath = Environment.getExternalStorageDirectory()// /mnt/sdcard0
					.getAbsolutePath() + File.separator;
		} else if ((new File("/storage/sdcard0")).exists()) { //以下为一些手机常见的SD卡路径，无法用以上方法获取
			rootPath = "/storage/sdcard0" + File.separator;
		} else if((new File("/mnt/sdcard2")).exists()) {
			rootPath = "/mnt/sdcard2" + File.separator;
		} else if((new File("/mnt/sdcard-ext")).exists()) {
			rootPath = "/mnt/sdcard-ext" + File.separator;
		} else if((new File("/mnt/ext_sdcard")).exists()) {
			rootPath = "/mnt/ext_sdcard" + File.separator;
		} else if((new File("/mnt/sdcard/SD_CARD")).exists()) {
			rootPath = "/mnt/sdcard/SD_CARD" + File.separator;
		} else if((new File("/mnt/sdcard/extra_sd")).exists()) {
			rootPath = "/mnt/sdcard/extra_sd" + File.separator;
		} else if((new File("/mnt/extrasd_bind")).exists()) {
			rootPath = "/mnt/extrasd_bind" + File.separator;
		} else if((new File("/mnt/sdcard/ext_sd")).exists()) {
			rootPath = "/mnt/sdcard/ext_sd" + File.separator;
		} else if((new File("/mnt/sdcard/external_SD")).exists()) {
			rootPath = "/mnt/sdcard/external_SD" + File.separator;
		} else if((new File("/storage/sdcard1")).exists()) {
			rootPath = "/storage/sdcard1" + File.separator;
		} else if((new File("/storage/extSdCard")).exists()) {
			rootPath = "/storage/extSdCard" + File.separator;
		}
		return rootPath;
	}

	private static final String DOWNLOAD_ROOT_PATH = "KugouRing";

	public static final String DOWNLOAD_ROOT_DIR_PATH = getRootPath() + DOWNLOAD_ROOT_PATH;

	public static final String DOWNLOAD_CACHE_PATH = DOWNLOAD_ROOT_DIR_PATH + File.separator
			+ ".cache";

	public static final String MAKE_DIR_PATH = Environment.getExternalStorageDirectory()
			.getAbsolutePath()
			+ File.separator
			+ DOWNLOAD_ROOT_PATH
			+ File.separator
			+ "Make";

	public static final String RECORD_DIR_PATH = Environment.getExternalStorageDirectory()
			.getAbsolutePath()
			+ File.separator
			+ DOWNLOAD_ROOT_PATH
			+ File.separator
			+ "record"
			+ File.separator;

	public static final String RECORD_DIR_PATH_CACHE = Environment.getExternalStorageDirectory()
			.getAbsolutePath()
			+ File.separator
			+ DOWNLOAD_ROOT_PATH
			+ File.separator
			+ "record"
			+ File.separator + ".cache" + File.separator;

	public static final String PACK_RANGTONE_DIR_PATH = Environment.getExternalStorageDirectory()
			.getAbsolutePath()
			+ File.separator
			+ DOWNLOAD_ROOT_PATH
			+ File.separator
			+ "packimg"
			+ File.separator;

	public static final String KG_STATISTIC_PATH = DOWNLOAD_ROOT_DIR_PATH + File.separator+"statistic" + File.separator;
	public static final String KG_STATISTIC_FILE_PATH = KG_STATISTIC_PATH + "kgstatistic.data";


	


	/**
	 * 图片文件缓存目录
	 */
	public static final String IMAGE_CACHE_FOLDER = Environment.getExternalStorageDirectory().toString()
			+ File.separator
			+ DOWNLOAD_ROOT_PATH
			+ File.separator
			+"cached";
//			+ ".image/";
	
	private FileUtil() {

	}

	/**
	 * 文件是否存在
	 *
	 * @param filePath
	 * @return
	 */
	public static boolean fileIsExist(String filePath) {
		if (filePath == null || filePath.length() < 1) {
			KGLog.e("param invalid, filePath: " + filePath);
			return false;
		}

		File f = new File(filePath);
		if (!f.exists()) {
			return false;
		}
		return true;
	}


	/**
	 * 创建文件目录
	 *
	 * @param filePath
	 * @return
	 */
	public static boolean createDirectory(String filePath) {
		if (null == filePath) {
			return false;
		}

		File file = new File(filePath);

		if (file.exists()) {
			return true;
		}

		return file.mkdirs();
	}

	/**
	 * 删除目录 及其 子目录
	 *
	 * @param filePath
	 * @return
	 */
	public static boolean deleteDirectory(String filePath) {
		if (null == filePath) {
			KGLog.e("param invalid, filePath: " + filePath);
			return false;
		}

		File file = new File(filePath);

		if (file == null || !file.exists()) {
			return false;
		}

		if (file.isDirectory()) {
			File[] list = file.listFiles();

			for (int i = 0; i < list.length; i++) {
				KGLog.d("delete filePath: " + list[i].getAbsolutePath());

				if (list[i].isDirectory()) {
					deleteDirectory(list[i].getAbsolutePath());
				} else {
					list[i].delete();
				}
			}
		}

		KGLog.e("delete filePath: " + file.getAbsolutePath());

		file.delete();
		return true;
	}

	/**
	 * 获取文件大小
	 *
	 * @param filePath
	 * @return
	 */
	public static long getFileSize(String filePath) {
		if (null == filePath) {
			KGLog.e("Invalid param. filePath: " + filePath);
			return 0;
		}

		File file = new File(filePath);
		if (file == null || !file.exists()) {
			return 0;
		}

		return file.length();
	}

	/**
	 * 获取文件最后修改时间
	 *
	 * @param filePath
	 * @return
	 */
	public static long getFileModifyTime(String filePath) {
		if (null == filePath) {
			KGLog.e("Invalid param. filePath: " + filePath);
			return 0;
		}

		File file = new File(filePath);
		if (file == null || !file.exists()) {
			return 0;
		}

		return file.lastModified();
	}

	/**
	 * 设置文件最后修改时间
	 *
	 * @param filePath
	 * @param modifyTime
	 * @return
	 */
	public static boolean setFileModifyTime(String filePath, long modifyTime) {
		if (null == filePath) {
			KGLog.e("Invalid param. filePath: " + filePath);
			return false;
		}

		File file = new File(filePath);
		if (file == null || !file.exists()) {
			return false;
		}

		return file.setLastModified(modifyTime);
	}

	/**
	 * 将byte[]写入文件
	 *
	 * @attention 当文件存在将被替换 当其所在目录不存在，将尝试创建
	 * @param filePath 格式如： /sdcard/abc/a.obj
	 * @param content 写入内容byte[]
	 * @return
	 */
	public static boolean writeFile(String filePath, byte[] content) {
		if (null == filePath || null == content) {
			KGLog.e("Invalid param. filePath: " + filePath + ", content: " + content);
			return false;
		}

		FileOutputStream fos = null;
		try {
			String pth = filePath.substring(0, filePath.lastIndexOf("/"));
			File pf = null;
			pf = new File(pth);
			if (pf.exists() && !pf.isDirectory()) {
				pf.delete();
			}
			pf = new File(filePath);
			if (pf.exists()) {
				if (pf.isDirectory())
					deleteDirectory(filePath);
				else
					pf.delete();
			}

			pf = new File(pth + File.separator);
			if (!pf.exists()) {
				if (!pf.mkdirs()) {
					KGLog.e("Can't make dirs, path=" + pth);
				}
			}

			fos = new FileOutputStream(filePath);
			fos.write(content);
			fos.flush();
			fos.close();
			fos = null;
			pf.setLastModified(System.currentTimeMillis());

			return true;

		} catch (Exception ex) {
			KGLog.e("Exception, ex: " + ex.toString());
		} finally {
			if (null != fos) {
				try {
					fos.close();
				} catch (Exception ex) {
					ex.printStackTrace();
				}

			}
		}
		return false;
	}

	/**
	 * 读取文件
	 *
	 * @param filePath
	 * @return 输入流
	 */
	public static InputStream readFile(String filePath) {
		if (null == filePath) {
			KGLog.e("Invalid param. filePath: " + filePath);
			return null;
		}

		InputStream is = null;

		try {
			if (fileIsExist(filePath)) {
				File f = new File(filePath);
				is = new FileInputStream(f);
			} else {
				return null;
			}
		} catch (Exception ex) {
			KGLog.e("Exception, ex: " + ex.toString());
			return null;
		}
		return is;
	}

	/**
	 * 读取输入流 转化为 byte[]
	 *
	 * @param is
	 * @return
	 * @throws Exception
	 */
	public static byte[] readIn(InputStream is) throws Exception {
		ByteArrayOutputStream baos = new ByteArrayOutputStream(1024);
		byte[] buf = new byte[1024];
		int c = is.read(buf);
		while (-1 != c) {
			baos.write(buf, 0, c);
			c = is.read(buf);
		}
		baos.flush();
		baos.close();
		return baos.toByteArray();
	}

	/**
	 * 创建文件的模式，已经存在的文件要覆盖
	 */
	public final static int MODE_COVER = 1;

	/**
	 * 创建文件的模式，文件已经存在则不做其它事
	 */
	public final static int MODE_UNCOVER = 0;

	/**
	 * 获取文件的输入流
	 *
	 * @param path
	 * @return
	 */
	public static FileInputStream getFileInputStream(String path) {
		FileInputStream fis = null;
		try {
			File file = new File(path);
			if (file.exists()) {
				fis = new FileInputStream(file);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return fis;
	}

	/**
	 * 获取文件的输出流
	 *
	 * @param path
	 * @return
	 */
	public static OutputStream getFileOutputStream(String path) {
		FileOutputStream fos = null;
		try {
			File file = new File(path);
			if (file.exists()) {
				fos = new FileOutputStream(file);
			}
		} catch (Exception e) {
			return null;
		}
		return fos;
	}

	/**
	 * 获取文件的数据
	 *
	 * @param path
	 * @return
	 */
	public static byte[] getFileData(String path) {
		byte[] data = null;// 返回的数据
		try {
			File file = new File(path);
			if (file.exists()) {
				data = new byte[(int) file.length()];
				FileInputStream fis = new FileInputStream(file);
				fis.read(data);
				fis.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return data;
	}

	/**
	 * 写入新文件
	 */
	public static void writeData(String path, byte[] data) {
		try {
			File file = new File(path);
			if (!file.exists()) {
				FileOutputStream fos = new FileOutputStream(file, false);
				fos.write(data);
				fos.flush();
				fos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 重写文件的数据
	 *
	 * @param path
	 * @param data
	 */
	public static void rewriteData(String path, byte[] data) {
		try {
			File file = new File(path);
			if (file.exists()) {
				FileOutputStream fos = new FileOutputStream(file, false);
				fos.write(data);
				fos.flush();
				fos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 重写文件的数据
	 *
	 * @param path
	 * @param is
	 */
	public static void rewriteData(String path, InputStream is) {
		try {
			File file = new File(path);
			if (file.exists()) {
				FileOutputStream fos = new FileOutputStream(file, false);
				byte[] data = new byte[1024];
				int receive = 0;
				while ((receive = is.read(data)) != -1) {
					fos.write(data, 0, receive);
					fos.flush();
				}
				fos.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 向文件的末尾添加数据
	 *
	 * @param path
	 * @param data
	 */
	public static boolean appendData(String path, byte[] data) {
		try {
			File file = new File(path);
			if (file.exists()) {
				FileOutputStream fos = new FileOutputStream(file, true);
				fos.write(data);
				fos.flush();
				fos.close();
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 向文件末尾添加数据
	 *
	 * @param path
	 * @param is
	 */
	public static void appendData(String path, InputStream is) {
		try {
			File file = new File(path);
			if (file.exists()) {
				FileOutputStream fos = new FileOutputStream(file, true);
				byte[] data = new byte[1024];
				int receive = 0;
				while ((receive = is.read(data)) != -1) {
					fos.write(data, 0, receive);
					fos.flush();
				}
				fos.close();
			}
		} catch (Exception e) {
		}
	}

	/**
	 * 删除文件或文件夹(包括目录下的文件)
	 *
	 * @param filePath
	 */
	public static void deleteFile(String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return;
		}
		try {
			File f = new File(filePath);
			if (f.exists() && f.isDirectory()) {
				File[] delFiles = f.listFiles();
				if (delFiles != null) {
					for (int i = 0; i < delFiles.length; i++) {
						deleteFile(delFiles[i].getAbsolutePath());
					}
				}
			}
			f.delete();
		} catch (Exception e) {

		}
	}

	/**
	 * 删除文件
	 *
	 * @param filePath
	 * @param deleteParent 是否删除父目录
	 */
	public static void deleteFile(String filePath, boolean deleteParent) {
		if (filePath == null) {
			return;
		}
		try {
			File f = new File(filePath);
			if (f.exists() && f.isDirectory()) {
				File[] delFiles = f.listFiles();
				if (delFiles != null) {
					for (int i = 0; i < delFiles.length; i++) {
						deleteFile(delFiles[i].getAbsolutePath(), deleteParent);
					}
				}
			}
			if (deleteParent) {
				f.delete();
			} else if (f.isFile()) {
				f.delete();
			}
		} catch (Exception e) {

		}
	}

	/**
	 * 创建一个空的文件(创建文件的模式，已经存在的是否要覆盖)
	 *
	 * @param path
	 * @param mode
	 */
	public static boolean createFile(String path, int mode) {
		if (TextUtils.isEmpty(path)) {
			return false;
		}
		try {
			File file = new File(path);
			if (file.exists()) {
				if (mode == FileUtil.MODE_COVER) {
					file.delete();
					file.createNewFile();
				}
			} else {
				// 如果路径不存在，先创建路径
				File mFile = file.getParentFile();
				if (!mFile.exists()) {
					mFile.mkdirs();
				}
				file.createNewFile();
			}
		} catch (Exception e) {
			return false;
		}
		return true;
	}

	/**
	 * 创建一个空的文件夹(创建文件夹的模式，已经存在的是否要覆盖)
	 *
	 * @param path
	 * @param mode
	 */
	public static void createFolder(String path, int mode) {
		try {
			// LogUtil.debug(path);
			File file = new File(path);
			if (file.exists()) {
				if (mode == FileUtil.MODE_COVER) {
					file.delete();
					file.mkdirs();
				}
			} else {
				file.mkdirs();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取文件大小
	 *
	 * @param path
	 * @return
	 */
	public static long getSize(String path) {
		if (TextUtils.isEmpty(path)) {
			return 0;
		}
		long size = 0;
		try {
			File file = new File(path);
			if (file.exists()) {
				size = file.length();
			}
		} catch (Exception e) {
			return 0;
		}
		return size;
	}

	/**
	 * 判断文件或文件夹是否存在
	 *
	 * @param path
	 * @return true 文件存在
	 */
	public static boolean isExist(String path) {
		if (TextUtils.isEmpty(path)) {
			return false;
		}
		boolean exist = false;
		try {
			File file = new File(path);
			exist = file.exists();
		} catch (Exception e) {
			return false;
		}
		return exist;
	}

	/**
	 * 重命名文件/文件夹
	 *
	 * @param path
	 * @param newName
	 */
	public static boolean rename(final String path, final String newName) {
		boolean result = false;
		if (TextUtils.isEmpty(path) || TextUtils.isEmpty(newName)) {
			return result;
		}
		try {
			File file = new File(path);
			if (file.exists()) {
				result = file.renameTo(new File(newName));
			}
		} catch (Exception e) {
		}

		return result;
	}

	/**
	 * 列出目录文件
	 *
	 * @return
	 */
	public static File[] listFiles(String filePath) {
		File file = new File(filePath);
		if (file.exists() && file.isDirectory()) {
			return file.listFiles();
		}
		return null;
	}

	/**
	 * @param path hash.ext.kgtmp
	 * @return
	 */
	public static String getAudioMimeType(String path) {
		boolean isM4A = path.toLowerCase().endsWith(".m4a");
		return isM4A ? "audio/mp4" : "audio/mpeg";
	}

	/**
	 * 是否是m4a文件
	 *
	 * @param m4a m4a文件路径
	 * @return
	 */
	public static boolean isM4A(final String m4a) {
		if (TextUtils.isEmpty(m4a)) {
			return false;
		}
		try {
			FileInputStream stream = new FileInputStream(new File(m4a));
			byte[] buffer = new byte[8];
			if (stream.read(buffer) == 8) {
				stream.close();
				return (buffer[4] == 'f' && buffer[5] == 't' && buffer[6] == 'y' && buffer[7] == 'p');
			} else {
				stream.close();
				return false;
			}
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}

	/**
	 * 哈希值写到m4a
	 *
	 * @param m4a m4a文件路径
	 * @param hash mp3哈希值 kgmp3hash
	 */
	public static void writeMp3HashToM4a(final String m4a, final String hash) {
		if (TextUtils.isEmpty(m4a) || TextUtils.isEmpty(hash)) {
			return;
		}
		try {
			File m4afile = new File(m4a);
			RandomAccessFile accessFile = new RandomAccessFile(m4afile, "rw");
			long m4aLength = m4afile.length();
			byte[] tagbyte = TAG_KGMP3HASH.getBytes();
			byte[] hashbyte = hash.getBytes();
			ByteArrayBuffer buffer = new ByteArrayBuffer(TAG_KGMP3HASH_LENGTH);
			buffer.append(tagbyte, 0, tagbyte.length);
			buffer.append(hashbyte, 0, hashbyte.length);
			accessFile.skipBytes((int) m4aLength);
			accessFile.write(buffer.toByteArray());
			accessFile.close();
		} catch (FileNotFoundException e) {
		} catch (IOException e) {
		}
	}

	private static final String TAG_KGMP3HASH = "kgmp3hash";

	private static final int TAG_KGMP3HASH_LENGTH = TAG_KGMP3HASH.length() + 32;

	/**
	 * 从m4a读取mp3哈希值
	 *
	 * @param m4a m4a文件路径
	 * @return
	 */
	public static String readMp3HashFromM4a(final String m4a) {
		if (TextUtils.isEmpty(m4a)) {
			return null;
		}
		File m4afile = new File(m4a);
		RandomAccessFile accessFile = null;
		try {
			accessFile = new RandomAccessFile(m4afile, "r");
			accessFile.skipBytes((int) (m4afile.length() - TAG_KGMP3HASH_LENGTH));
			byte[] b = new byte[TAG_KGMP3HASH_LENGTH];
			if (accessFile.read(b) == TAG_KGMP3HASH_LENGTH) {
				String taghash = new String(b);
				if (!TextUtils.isEmpty(taghash) && taghash.startsWith(TAG_KGMP3HASH)) {
					return taghash.substring(TAG_KGMP3HASH.length());
				}
			}
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		} finally {
			if (accessFile != null) {
				try {
					accessFile.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}

	/**
	 * 移动文件
	 *
	 * @param oldFilePath 旧路径
	 * @param newFilePath 新路径
	 * @return
	 */
	public static boolean moveFile(String oldFilePath, String newFilePath) {
		if (TextUtils.isEmpty(oldFilePath) || TextUtils.isEmpty(newFilePath)) {
			return false;
		}
		File oldFile = new File(oldFilePath);
		if (oldFile.isDirectory() || !oldFile.exists()) {
			return false;
		}
		try {
			File newFile = new File(newFilePath);
			if (!newFile.exists()) {
				newFile.createNewFile();
			}
			BufferedInputStream bis = new BufferedInputStream(new FileInputStream(oldFile));
			FileOutputStream fos = new FileOutputStream(newFile);
			byte[] buf = new byte[1024];
			int read;
			while ((read = bis.read(buf)) != -1) {
				fos.write(buf, 0, read);
			}
			fos.flush();
			fos.close();
			bis.close();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public static byte[] InputStreamToByte(InputStream iStrm) throws IOException {
		ByteArrayOutputStream bytestream = new ByteArrayOutputStream();
		int ch;
		while ((ch = iStrm.read()) != -1) {
			bytestream.write(ch);
		}
		byte imgdata[] = bytestream.toByteArray();
		bytestream.close();
		return imgdata;
	}

	/**
	 * 是否是下载出错文件（下到错误页面的数据）
	 *
	 * @param filePath 文件路径
	 * @return
	 */
	public static boolean isErrorFile(final String filePath) {
		if (TextUtils.isEmpty(filePath)) {
			return false;
		}
		try {
			FileInputStream stream = new FileInputStream(new File(filePath));
			byte[] buffer = new byte[16];
			if (stream.read(buffer) == 16) {
				stream.close();
				return ((buffer[0] & 0xFF) == 0xFF && (buffer[1] & 0xFF) == 0xD8
						&& (buffer[2] & 0xFF) == 0xFF && (buffer[3] & 0xFF) == 0xE0
						&& (buffer[4] & 0xFF) == 0x00 && (buffer[5] & 0xFF) == 0x10
						&& (buffer[6] & 0xFF) == 0x4A && (buffer[7] & 0xFF) == 0x46
						&& (buffer[8] & 0xFF) == 0x49 && (buffer[9] & 0xFF) == 0x46
						&& (buffer[10] & 0xFF) == 0x00 && (buffer[11] & 0xFF) == 0x01
						&& (buffer[12] & 0xFF) == 0x02 && (buffer[13] & 0xFF) == 0x01
						&& (buffer[14] & 0xFF) == 0x00 && (buffer[15] & 0xFF) == 0x48);
			} else {
				stream.close();
				return false;
			}
		} catch (FileNotFoundException e) {
			return false;
		} catch (IOException e) {
			return false;
		}
	}

	public static long getSDFreeSize() {
		// 取得SD卡文件路径
		File path = Environment.getExternalStorageDirectory();
		StatFs sf = new StatFs(path.getPath());
		// 获取单个数据块的大小(Byte)
		long blockSize = sf.getBlockSize();
		// 空闲的数据块的数量
		long freeBlocks = sf.getAvailableBlocks();
		// 返回SD卡空闲大小
		// return freeBlocks * blockSize; //单位Byte
		// return (freeBlocks * blockSize)/1024; //单位KB
		return (freeBlocks * blockSize) / 1024 / 1024; // 单位MB
	}

	public static long getmem_UNUSED(Context mContext) {
		long MEM_UNUSED;
		// 得到ActivityManager
		ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
		// 创建ActivityManager.MemoryInfo对象

		ActivityManager.MemoryInfo mi = new ActivityManager.MemoryInfo();
		am.getMemoryInfo(mi);

		// 取得剩余的内存空间

		MEM_UNUSED = mi.availMem / 1024;
		return MEM_UNUSED;
	}

	/**
	 * 读取文件转为字符串
	 *
	 * @param filePath
	 * @return
	 */
	public static String readFileToString(String filePath) {
		String str = "";
		try {
			File readFile = new File(filePath);
			if (!readFile.exists()) {
				return null;
			}
			FileInputStream inStream = new FileInputStream(readFile);
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			byte[] buffer = new byte[1024];
			int length = -1;
			while ((length = inStream.read(buffer)) != -1) {
				stream.write(buffer, 0, length);
			}
			str = stream.toString();
			stream.close();
			inStream.close();
			return str;
		} catch (FileNotFoundException e) {
			return null;
		} catch (IOException e) {
			return null;
		}
	}

	/**
	 * 保存文件
	 *
	 * @param toSaveString
	 * @param filePath
	 */
	public static void saveFile(String toSaveString, String filePath) {
		FileOutputStream outStream = null;

		try {
			File saveFile = new File(filePath);
			if (!saveFile.exists()) {
				File dir = new File(saveFile.getParent());
				dir.mkdirs();
				saveFile.createNewFile();
			}

			outStream = new FileOutputStream(saveFile);
			if(outStream != null && !TextUtils.isEmpty(toSaveString)){
				outStream.write(toSaveString.getBytes());
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (outStream != null) {
				try {
					outStream.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 获取所有的sd卡
	 * @param context
	 * @return
     */
	public static String[] getSDCardsStorageDirectory(Context context) {
		StorageManager sm = (StorageManager) context.getSystemService(Context.STORAGE_SERVICE);
		String[] paths = null;
		try {
			paths = (String[]) sm.getClass().getMethod("getVolum" +
					"ePaths", new  Class[0]).invoke(sm, new  Object[]{});
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		if (paths != null && paths.length > 0){
			return  paths;
		}
		return  new String[]{getRootPath()};
	}

	/**
	 * 计算文件或者文件夹的大小 ，单位 MB
	 *
	 * @param file 要计算的文件或者文件夹 ， 类型：java.io.File
	 * @return 大小，单位：MB
	 */
	public static long getALLSize(File file) {
		//判断文件是否存在
		if (file.exists()) {
			//如果是目录则递归计算其内容的总大小，如果是文件则直接返回其大小
			if (!file.isFile()) {
				//获取文件大小
				File[] fl = file.listFiles();
				long ss = 0;
				for (File f : fl)
					ss += getALLSize(f);
				return ss;
			} else {
				long ss =  file.length();
				Log.d("debug",file.getName() + " : " + ss + "K");
				return ss;
			}
		} else {
			Log.d("debug","文件或者文件夹不存在，请检查路径是否正确！");
			return 0;
		}
	}

}
