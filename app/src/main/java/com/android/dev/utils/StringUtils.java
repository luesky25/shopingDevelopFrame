package com.android.dev.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import android.text.TextUtils;



public class StringUtils {

	private static String CHINESE_REGEX = "[\\u4E00-\\u9FA5\\uF900-\\uFA2D\\uFF00-\\uFFEF]";
	
	/**
	 * 判断字符串是否为空
	 * 
	 * @param s
	 * @return
	 */
	public static boolean isEmpty(String s) {
		return TextUtils.isEmpty(s) || "null".equals(s);
	}

	/**
	 * 判断是否是合法的手机号码
	 */
	public static boolean patternPhoneNumber(String number) {
		Pattern pattern = Pattern.compile("^[1][3,4,5,8,7][0-9]{9}$");
		Matcher matcher = pattern.matcher(number);
		return matcher.matches();
	}

	/**
	 * 判断是否是合法邮箱
	 */
	public static boolean patternEmail(String str) {
		Pattern pattern = Pattern
				.compile("^(\\w)+(\\.\\w+)*@(\\w)+((\\.\\w+)+)$");
		Matcher matcher = pattern.matcher(str);
		return matcher.matches();
	}

	/**
	 * 判断是否是合法身份证号码
	 */
	public static boolean patternIdCard(String str) {
		if (TextUtils.isEmpty(str))
			return false;
		Pattern pattern1 = Pattern
				.compile("^[1-9]\\d{7}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}$");
		Matcher matcher1 = pattern1.matcher(str);
		Pattern pattern2 = Pattern
				.compile("^[1-9]\\d{5}[1-9]\\d{3}((0\\d)|(1[0-2]))(([0|1|2]\\d)|3[0-1])\\d{3}([0-9]|X|x)$");
		Matcher matcher2 = pattern2.matcher(str);
		return matcher1.matches() || matcher2.matches();
	}
	
	/**
	 * 拼接网络请求的BOBY体字符串
	 * 
	 * @param key
	 * @param value
	 * @return
	 */
	@Deprecated
	public static String getPostBobyString(String[] key, String[] value) {
		if (key.length != value.length)
			return null;
		int length = key.length;
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		for (int i = 0; i < length; i++) {
			builder.append("\"").append(key[i]).append("\":\"")
					.append(uniCodeEncode(value[i])).append("\"");
			if (i != (length - 1))
				builder.append(",");
		}
		builder.append("}");
		return builder.toString();
	}

	/**
	 * 拼接网络请求的BOBY体字符串
	 * 
	 * @param key
	 * @param value
	 * @param bracketAsArray    true---[xx]的内容是数组不加""，false---[xxxx]内容是报文，加""
	 * @return
	 */
	public static String getPostBobyString(Map<String, String> map, boolean bracketAsArray) {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		for (String key : map.keySet()) {
			if (bracketAsArray
					&& map.get(key).startsWith("[") && map.get(key).endsWith("]"))
				builder.append("\"").append(key).append("\":")
						.append(uniCodeEncode(map.get(key))).append(",");
			else
				builder.append("\"").append(key).append("\":\"")
						.append(uniCodeEncode(map.get(key))).append("\"")
						.append(",");
		}
		builder.append("}");
		return builder.toString().replace(",}", "}");
	}
	
	/**
	 * 用户手动输入字符串的拼接方法
	 * 
	 * @param key
	 * @param value
	 * @param bracketAsArray    true---[xx]的内容是数组不加""，false---[xxxx]内容是报文，加""
	 * @return
	 */
	public static String getPostBobyStringByEdit(Map<String, String> map, boolean bracketAsArray) {
		StringBuilder builder = new StringBuilder();
		builder.append("{");
		for (String key : map.keySet()) {
			if (bracketAsArray
					&& map.get(key).startsWith("[") && map.get(key).endsWith("]"))
				builder.append("\"").append(key).append("\":")
						.append(uniCodeEncodeEditText(map.get(key))).append(",");
			else
				builder.append("\"").append(key).append("\":\"")
						.append(uniCodeEncodeEditText(map.get(key))).append("\"")
						.append(",");
		}
		builder.append("}");
		return builder.toString().replace(",}", "}");
	}
	
	/**
	 * 拼接网络请求的BOBY体字符串
	 * 默认处理[xx]的内容是数组不加""
	 * @param key
	 * @param value
	 * @return
	 */
	public static String getPostBobyString(Map<String, String> map) {
		return getPostBobyString(map, true);
	}

	/**
	 * 拼接网络请求GET的参数串
	 */
	public static String getGetParamsString(Map<String, String> map) {
		StringBuilder builder = new StringBuilder();
		if (map.size() == 0)
			return "";
		builder.append("?");
		for (String key : map.keySet()) {
			builder.append(key).append("=")
					.append(getUTF8ByString(map.get(key))).append("&");
		}
		builder.append("&&&");
		return builder.toString().replace("&&&&", "");
	}

	/**
	 * 获取中文与英文等组成的字符串的混合长度
	 * 
	 * @param data
	 * @return
	 */
	public static int getStringLength(String data) {
		if (isEmpty(data))
			return 0;
		int valueLength = 0;
		// \\uFF00-\\uFFEF中文标点符号
		String chinese = CHINESE_REGEX;
		String temp = null;
		// 获取字段值的长度，如果含中文字符，则每个中文字符长度为2，否则为1
		for (int i = 0; i < data.length(); i++) {
			// 获取一个字符
			temp = data.substring(i, i + 1);
			// 判断是否为中文字符
			if (temp.matches(chinese)) {
				valueLength += 2;
			} else {
				valueLength += 1;
			}
		}
		return valueLength;
	}

	/**
	 * 获取中文与英文等组成的字符串的混合长度(一个中文长度，英文字符，emoji长度都算1)
	 * 
	 * @param data
	 * @return
	 */
	public static int getStringLengthEmojiAndChineseAsOne(String data) {
		if (isEmpty(data))
			return 0;
		int valueLength = 0;
		for (int i = 0; i < data.length(); i++) {
				if((i+2)<=data.length()){
					String tempEmoji = data.substring(i, i + 2);
					int resultEmoji = isEmojiIcon(tempEmoji);
					if(resultEmoji>0){
						if(resultEmoji==2){
							// 为2的时候，说明处理了最近的两个字符了，需要前进一步
							i++;
						}
					}
				}
				valueLength += 1;
		}
		return valueLength;
	}
	
	/**
	 * 安装中文来截取字符串(一个中文长度，英文字符，emoji长度都算1)
	 * 
	 * @param data
	 *            字符串
	 * @param start
	 *            开始的index 0开始
	 * @param end
	 *            结束的index 0开始
	 * @return
	 */
	public static String subStringEmojiAndChineseAsOne(String data, int start, int end) {
		if (isEmpty(data))
			return null;
		if (end < start) {
			return null;
		}
		int wantedLength = end - start;
		int valueLength = 0;
		int realLength = 0;
		for (int i = start; i < data.length(); i++) {
			int resultEmoji = -1;
			if((i+2)<=data.length()){
				String tempEmoji = data.substring(i, i + 2);
				resultEmoji = isEmojiIcon(tempEmoji);
			}
			
			if(resultEmoji==2){
				i++;
				realLength +=2;
				//KGLog.d("yyyy", "+2 realLength="+realLength);
			}else{
			    realLength++;
			    //KGLog.d("yyyy", "++ realLength="+realLength);
			}
			valueLength++;
			
            if(valueLength>=wantedLength){
            	//KGLog.d("yyyy", "valueLength="+valueLength+",wantedLength="+wantedLength);
			    break;
            }		
		}

		return data.substring(start, realLength);
	}
	/**
	 * 是否是emojiicon
	 * @param temp
	 * @return
	 */
	private static int isEmojiIcon(String temp) {
        int textLength = temp.length();
        int result = -1;
        for(int i=0;i<textLength;i++){
        	 char c = temp.charAt(i);
        	 if(isSoftBankEmoji(c)){
        		 result = 1;
        		 break;
        	 }
        	 int unicode = Character.codePointAt(temp, i);
        	 if(isUnicodeEmoji(unicode)){
        		 result = 2;
        		 break;
        	 }
        }
		return result;
	}
	/**
	 * 是否是软银的emoji
	 * @param c
	 * @return
	 */
    private static boolean isSoftBankEmoji(char c) {
        return ((c >> 12) == 0xe);
    }
	/**
	 * 是否是unicode的emoji
	 * @param unicode
	 * @return
	 */
    private static boolean isUnicodeEmoji(int unicode) {
        return (unicode>=0x1F600 && unicode<=0x1F64F);
    }
    
	/**
	 * 判断密码是否符合规则
	 * 
	 * @param data
	 * @return
	 */
	public static boolean patternPassword(String data) {
		int count = getStringLength(data);
		if (count >= 6 && count <= 16)
			return true;
		else
			return false;
	}

	/**
	 * 转换UTF-8格式
	 * 
	 * @param data
	 * @return
	 */
	public static String getUTF8ByString(String data) {
		String result = data;
		try {
			result = URLEncoder.encode(data, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 将UTF-8格式字符串转换整正常字符串
	 * 
	 * @param data
	 * @return
	 */
	public static String getStringByUtf8(String data) {
		String result = "";
		try {
			result = URLDecoder.decode(data, "utf-8");
		} catch (Exception e) {
			e.printStackTrace();
		}
		return result;
	}

	/**
	 * 转义unicode编码
	 * 
	 * @param string
	 * @return
	 */
	public static String string2Unicode(String string) {
		StringBuffer unicode = new StringBuffer();
		for (int i = 0; i < string.length(); i++) {
			// 取出每一个字符
			char c = string.charAt(i);
			// 转换为unicode
			if (c >= 19968 && c <= 171941) {// 汉字范围 \u4e00-\u9fa5 (中文)
				unicode.append("u").append(Integer.toHexString(c));
			} else {
				unicode.append(c);
			}
		}

		return unicode.toString();
	}

	public static String uniCodeEncode(String string) {
		StringBuffer unicode = new StringBuffer();
		for (int i = 0; i < string.length(); i++) {
			// 取出每一个字符
			char c = string.charAt(i);
			// 转换为unicode
			if (c >= 19968 && c <= 171941) {// 汉字范围 \u4e00-\u9fa5 (中文)
				unicode.append("\\u").append(Integer.toHexString(c));
			}else{
				unicode.append(c);
			}
		}

		return unicode.toString();
	}

	/**
	 * 判断不是两个16进制的就转unicode
	 * @param string
	 * @return
     */
		public static String uniCodeForEncode(String string) {
		StringBuffer unicode = new StringBuffer();
		for (int i = 0; i < string.length(); i++) {
			// 取出每一个字符
			char c = string.charAt(i);
			
			// 转换为unicode

			String mChar =Integer.toHexString(c);
		
			if (mChar.length()==1) 
				unicode.append("\\u000").append(mChar);
			else if (mChar.length()==2) 
				unicode.append("\\u00").append(mChar);
			else if (mChar.length()==3)
				unicode.append("\\u0").append(mChar);
			else
				unicode.append("\\u").append(mChar);
		
			
//			if (c >= 'A' && c <= 'Z' || c >= 'a' && c <= 'z'||c >= '0' && c <= '9')
//			{
//				unicode.append(c);
//			}else if (Character.isWhitespace(c)){//是否是空格
//				unicode.append(c);
//			}else{
//				unicode.append("\\u").append(Integer.toHexString(c));
//			}
		}
		return unicode.toString();
	}
	public static boolean isEnglish(String charaString) {
		return charaString.matches("^[a-zA-Z]*");
	}
	
	public static String uniCodeEncodeEditText(String string) {
		StringBuffer unicode = new StringBuffer();
		for (int i = 0; i < string.length(); i++) {
			// 取出每一个字符
			char c = string.charAt(i);
			// 转换为unicode
			if (c >= 19968 && c <= 171941) {// 汉字范围 \u4e00-\u9fa5 (中文)
				unicode.append("\\u").append(Integer.toHexString(c));
			} else if(c == '"'){
				unicode.append("\\").append(c);
			}else{
				unicode.append(c);
			}
		}

		return unicode.toString();
	}


	public static String convert(String utfString) {
		StringBuilder sb = new StringBuilder();
		int i = -1;
		int pos = 0;

		while ((i = utfString.indexOf("\\u", pos)) != -1) {
			sb.append(utfString.substring(pos, i));
			if (i + 5 < utfString.length()) {
				pos = i + 6;
				sb.append((char) Integer.parseInt(
						utfString.substring(i + 2, i + 6), 16));
			}
		}

		return sb.toString();
	}

	/**
	 * 判断是否为中文字符
	 * 
	 * @param c
	 * @return
	 */
	public static boolean isChinese(char c) {
		Character.UnicodeBlock ub = Character.UnicodeBlock.of(c);
		if (ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_COMPATIBILITY_IDEOGRAPHS
				|| ub == Character.UnicodeBlock.CJK_UNIFIED_IDEOGRAPHS_EXTENSION_A
				|| ub == Character.UnicodeBlock.GENERAL_PUNCTUATION
				|| ub == Character.UnicodeBlock.CJK_SYMBOLS_AND_PUNCTUATION
				|| ub == Character.UnicodeBlock.HALFWIDTH_AND_FULLWIDTH_FORMS) {
			return true;
		}
		return false;
	}

	/**
	 * 判断输入的密码是否是数字，字母，符号(不包括空格，密码中不允许出现空格)的组合,6~16位 如果strInput为空直接返回false
	 * 
	 * @param strInput
	 *            输入的字符串
	 * @return 是否符合
	 */
	public static boolean isValidPassword(String strInput) {
		if (isEmpty(strInput)) {
			return false;
		}
		String strPattern = "^[0-9a-zA-Z-`=\\\\|\\[\\];',.\\/~!@#$%^&*()_+{}:<>?]{6,16}$";
		Pattern p = Pattern.compile(strPattern);
		Matcher m = p.matcher(strInput);
		return m.matches();
	}

	/**
	 * check nickname是否正确，只能有汉字，字母字符,标点符号
	 * 
	 * @param name
	 * @return
	 */
	public static boolean isValidNickName(String name) {
		if (isEmpty(name)) {
			return false;
		}
		int length = getStringLength(name);
		if (length > 16 || length < 1) {
			return false;
		}
		Pattern p = Pattern
				.compile("^[\\u4E00-\\u9FA5\\uF900-\\uFA2D\\uFF00-\\uFFEF\\w-`=\\\\|\\[\\];',.\\/~!@#$%^&*()_+{}:<>?]*$");
		Matcher m = p.matcher(name);
		if (!m.matches()) {
			return false;
		}
		return true;
	}

	/**
	 * 去掉字符串里面的空白字符
	 * 
	 * @param str
	 *            字符串
	 * @return 返回的字符
	 */
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			/*
			 * 注：\n 回车( ) \t 水平制表符( ) \s 空格(\u0008) \r 换行( )
			 */
			Pattern p = Pattern.compile("\\s*|\\t|\\r|\\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/**
	 * 获取object的simpleName
	 * 
	 * @param o
	 *            object
	 * @return 对应的simpleName
	 */
	public static String getSimpleName(Object o) {
		String result = null;
		if (o != null) {
			result = o.getClass().getName();
		}
		return result;
	}

	/**
	 * 安装中文来截取字符串(一个中文长度算2，英文字符长度算1)
	 * 
	 * @param data
	 *            字符串
	 * @param start
	 *            开始的index 0开始
	 * @param end
	 *            结束的index 0开始
	 * @return
	 */
	public static String subStringChinese(String data, int start, int end) {
		if (isEmpty(data))
			return null;
		if (end < start) {
			return null;
		}
		int wantedLength = end - start + 1;
		int valueLength = 0;
		int realLength = 0;
		String chinese = CHINESE_REGEX;
		String temp = null;
		for (int i = start; i < data.length(); i++) {
			temp = data.substring(i, i + 1);
			if (temp.matches(chinese)) {
				valueLength += 2;
			} else {
				valueLength += 1;
			}
			realLength++;
			
			if(valueLength==wantedLength){
			    break;
			}else if(valueLength>wantedLength){
				// 走到这里说明最后最后的一次加了一个中文就超出的情况，所以需要substring的时候-1
				realLength -= 1;
				break;
			}			
		}

		return data.substring(start, realLength);
	}
	
	public static String subStringChineseAsOne(String data, int start, int end) {
		if (isEmpty(data))
			return null;
		if (end < start) {
			return null;
		}
		int wantedLength = end - start + 1;
		int valueLength = 0;
		int realLength = 0;
		for (int i = start; i < data.length(); i++) {
			valueLength += 1;
			realLength++;
			
			if(valueLength==wantedLength){
			    break;
			}else if(valueLength>wantedLength){
				// 走到这里说明最后最后的一次加了一个中文就超出的情况，所以需要substring的时候-1
				realLength -= 1;
				break;
			}			
		}

		return data.substring(start, realLength);
	}
	
	
	/**
	 * 输出字符串，用于打印错误调试信息所用
	 */
	public static String showString(String str) {
		if (TextUtils.isEmpty(str)) {
			return "@NULL";
		}
		else {
			return str;
		}
	}
	

	
	/**
	 * 获取格式化的金币字符串
	 */
	public static DecimalFormat mDecimalFormat_00 = new DecimalFormat("#0.00");
	public static String getMoneyTextWith_00(double value, boolean hideZreo) {
		String result = mDecimalFormat_00.format(value);
		return hideZreo ? result.replace(".00", "") : result;
	}
	
	public static DecimalFormat mDecimalFormat_0 = new DecimalFormat("#0.0");
	public static String getMoneyTextWith_0(double value, boolean hideZreo) {
		String result = mDecimalFormat_0.format(value);
		return hideZreo ? result.replace(".0", "") : result;
	}
	
	public static String replaceLast(String input, String oldStr, String newStr) {
		return input.replaceFirst("(?s)(.*)" + oldStr, "$1" + newStr);
	}

	/**
	 * 给字符串中有regx特殊字符的转义
	 * @param keyword
	 * @return
	 */
	public static String escapeExprSpecialWord(String keyword) {
	    if (!StringUtils.isEmpty(keyword)) {  
	        String[] fbsArr = { "\\", "$", "(", ")", "*", "+", ".", "[", "]", "?", "^", "{", "}", "|" };  
	        for (String key : fbsArr) {  
	            if (keyword.contains(key)) {  
	                keyword = keyword.replace(key, "\\" + key);  
	            }  
	        }  
	    }  
	    return keyword;  
	}

	/**
	 * 判断是否包含多个歌手
	 */
	public static String patternSinger(String str) {
//		Pattern pattern = Pattern.compile("^(?!\\\\d+\\\\.).+?(?=[,、/(（]|\\\\s\\\\B|$)");
		Pattern pattern = Pattern.compile("^(?!\\d+\\.).+?(?=[,、/(（]|\\s\\B|$)|(?<=\\d{1,9}\\.).*");
		Matcher matcher = pattern.matcher(str);
		String singer;
		if (matcher.find()){
			singer = matcher.group(0);
		}else{
			singer = str;
		}
		return singer;
	}

	public static boolean isNumeric(String str) {
		String regEx = "^-?[0-9]+$";
		Pattern pat = Pattern.compile(regEx);
		Matcher mat = pat.matcher(str);
		if (mat.find()) {
			return true;
		}
		else {
			return false;
		}
	}


}
