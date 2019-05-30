package com.jet.util;

import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 实现功能描述: 实现把String、int、float、double转换为Byte[]的功能。 实现把Byte[]转换为String、double。
 */
public class ByteTools {

	/**
	 * 获取字符串帧的 byte 数组
	 * 
	 * @param formatString
	 * @return
	 */
	public static byte[] getBytesOfTheDateString(String formatString) {
		SimpleDateFormat fmtDate = new SimpleDateFormat(formatString);
		byte[] resultBytes = hexToBytes(fmtDate.format(new Date()));
		if (resultBytes.length > 5) {
			int dayOfWeek = Calendar.getInstance().get(Calendar.DAY_OF_WEEK) - 1;
			resultBytes[4] = (byte) (resultBytes[4] | (dayOfWeek << 5));
		}
		return resultBytes;
	}

	/**
	 * 根据给定的编码方式，返回字节数组
	 * 
	 * @param srcString
	 * @param encoding
	 * @return
	 */
	public static byte[] string2Bytes(String srcString, String encoding) {
		try {
			if (srcString != null && !("").equals(srcString)) {
				return srcString.getBytes(encoding);
			}
		} catch (Exception e) {
		}
		return null;
	}

	/**
	 * 获取短字符串，字符格式为：1字节（表示字符串长度）+内容字节
	 * 
	 * @param msg
	 * @return
	 */
	public static byte[] getShortString(String msg) {
		try {
			byte[] array = msg.getBytes("UTF-8");
			int arrayLength = array.length;
			ByteBuffer buff = ByteBuffer.allocate(1 + arrayLength);
			buff.put((byte) arrayLength);
			buff.put(array);
			byte[] temp = buff.array();
			buff = null;
			return temp;
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			byte[] temp = new byte[1];
			temp[0] = 0;
			return temp;
		}
	}

	/**
	 * 获取长字符串，格式： 2个字节（表示内容字节长度）+ 内容字节数组
	 * 
	 * @param msg
	 * @return
	 */
	public static byte[] getLongString(String msg) {
		try {
			byte[] array = msg.getBytes("UTF-8");
			int arrayLength = array.length;
			ByteBuffer buff = ByteBuffer.allocate(2 + arrayLength);
			buff.put(short2byte(arrayLength));
			buff.put(array);
			byte[] temp = buff.array();
			buff = null;
			return temp;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			byte[] temp = int2byte(0);
			return temp;
		}
	}

	/**
	 * 方法描述:将int转换为Byte[]
	 * 
	 * @param value
	 *            int
	 * @return byte[]
	 */
	public static byte[] int2Bytes(int value) {
		byte[] bRet = new byte[4];
		bRet[0] = (byte) ((value >> 24) & 0x000000FF);
		bRet[1] = (byte) ((value >> 16) & 0x000000FF);
		bRet[2] = (byte) ((value >> 8) & 0x000000FF);
		bRet[3] = (byte) (value & 0x000000FF);
		return bRet;
	}

	/**
	 * 方法描述:将float转换为Byte[]
	 * 
	 * @param value
	 *            float
	 * @return byte[]
	 */
	public static byte[] float2Bytes(float value) {
		byte[] bRet = new byte[4];
		int nvalue = Float.floatToIntBits(value);
		bRet[0] = (byte) ((nvalue >> 24) & 0x000000FF);
		bRet[1] = (byte) ((nvalue >> 16) & 0x000000FF);
		bRet[2] = (byte) ((nvalue >> 8) & 0x000000FF);
		bRet[3] = (byte) (nvalue & 0x000000FF);
		return bRet;
	}

	/**
	 * 
	 * 方法描述:将double转换为Byte[]
	 * 
	 * @param value
	 *            double
	 * @return byte[]
	 */
	public static byte[] double2Bytes(double value) {
		byte[] bRet = new byte[8];
		long nvalue = Double.doubleToLongBits(value);
		bRet[0] = (byte) ((nvalue >> 56) & 0x000000FF);
		bRet[1] = (byte) ((nvalue >> 48) & 0x000000FF);
		bRet[2] = (byte) ((nvalue >> 40) & 0x000000FF);
		bRet[3] = (byte) ((nvalue >> 32) & 0x000000FF);
		bRet[4] = (byte) ((nvalue >> 24) & 0x000000FF);
		bRet[5] = (byte) ((nvalue >> 16) & 0x000000FF);
		bRet[6] = (byte) ((nvalue >> 8) & 0x000000FF);
		bRet[7] = (byte) (nvalue & 0x000000FF);
		return bRet;
	}

	/**
	 * 方法描述:将Byte[]转换为String
	 * 
	 * @param b
	 *            byte[]
	 * @return String
	 * @throws UnsupportedEncodingException
	 *             编码错误异常抛出
	 */
	public static String byteToString(byte[] b) throws UnsupportedEncodingException {
		return new String(b);
	}

	/**
	 * 
	 * 方法描述:将Byte[]转换为String
	 * 
	 * @param b
	 *            byte[]
	 * @param charset
	 *            String
	 * @return String
	 * @throws UnsupportedEncodingException
	 *             编码错误异常抛出
	 */
	public static String byteToString(byte[] b, String charset) throws UnsupportedEncodingException {
		return new String(b, charset);
	}

	/**
	 * 
	 * 方法描述:将Byte[]转换为Double
	 * 
	 * @param b
	 *            byte[]
	 * @return double
	 */
	public static double byteToDouble(byte[] b) {
		long l = (((long) b[0] << 56) + ((long) (b[1] & 255) << 48) + ((long) (b[2] & 255) << 40)
				+ ((long) (b[3] & 255) << 32) + ((long) (b[4] & 255) << 24) + ((b[5] & 255) << 16) + ((b[6] & 255) << 8)
				+ ((b[7] & 255) << 0));
		return Double.longBitsToDouble(l);
	}

	/**
	 * 
	 * 方法描述:将Byte[]转换为Float.
	 * 
	 * @param bytes
	 *            byte[]
	 * @return float
	 */
	public static float byte2Float(byte[] bytes) {
		int addr = bytes[3] & 0xFF;
		addr |= ((bytes[2] << 8) & 0xFF00);
		addr |= ((bytes[1] << 16) & 0xFF0000);
		addr |= ((bytes[0] << 24) & 0xFF000000);
		return Float.intBitsToFloat(addr);
	}

	/**
	 * 方法描述:将long转换为Byte[].
	 * 
	 * @param value
	 *            long
	 * @return byte[]
	 */
	public static byte[] long2Bytes(long value) {
		byte[] bRet = new byte[8];
		bRet[0] = (byte) (value >> 56);
		bRet[1] = (byte) (value >> 48);
		bRet[2] = (byte) (value >> 40);
		bRet[3] = (byte) (value >> 32);
		bRet[4] = (byte) (value >> 24);
		bRet[5] = (byte) (value >> 16);
		bRet[6] = (byte) (value >> 8);
		bRet[7] = (byte) (value >> 0);
		return bRet;
	}

	/**
	 * 方法描述:将Byte[]转换为long.
	 * 
	 * @param bytes
	 *            byte[]
	 * @return byte[]
	 */
	public static long bytes2Long(byte[] bytes) {
		return ((((long) bytes[0] & 0xff) << 56) | (((long) bytes[1] & 0xff) << 48) | (((long) bytes[2] & 0xff) << 40)
				| (((long) bytes[3] & 0xff) << 32) | (((long) bytes[4] & 0xff) << 24) | (((long) bytes[5] & 0xff) << 16)
				| (((long) bytes[6] & 0xff) << 8) | (((long) bytes[7] & 0xff) << 0));
	}

	/**
	 * 把byte转换成十六进制
	 * 
	 * @param b
	 *            要转换的byte
	 * @return Sting
	 */
	public static String byteToHex(byte b) {
		char hexDigit[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
		char[] array = { hexDigit[(b >> 4) & 0x0f], hexDigit[b & 0x0f] };
		return new String(array);
	}

	/**
	 * 把byte数组转换为十六进制
	 * 
	 * @param b
	 *            要转换的字节数组
	 * @return String
	 */
	public static String bytesToHex(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result = result + byteToHex(b[i]) + " ";
		}
		return result;
	}
	
	public static String bytesToHexTrim(byte[] b) {
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result = result + byteToHex(b[i]);
		}
		return result;
	}

	/**
	 * 把一个十六进制的字符串转换为字节
	 * 
	 * @param h
	 *            要转换的字符串
	 * @return byte
	 */
	public static byte hexToByte(String h) {
		if (h == null) {
			return 0;
		} else {
			if (h.length() <= 0) {
				return 0;
			} else if (h.length() <= 2) {
				return (byte) Short.parseShort(h.substring(0, h.length()), 16);
			} else {
				return (byte) Short.parseShort(h.substring(0, 2), 16);
			}
		}
	}

	/**
	 * 把一个十六进制的字符串转换为字节数组
	 * 
	 * @param h
	 *            要转换的字符串
	 * @return byte[]
	 */
	public static byte[] hexToBytes(String h) {
		int iLen = h.length() / 2;
		byte[] b = new byte[iLen];
		for (int i = 0; i < iLen; i++) {
			b[i] = hexToByte(h.substring(2 * i, 2 * i + 2));
		}
		return b;
	}

	/**
	 * 把一个字符转换为一个十六进制
	 * 
	 * @param c
	 *            要转换的字符
	 */
	public static String charToHex(char c) {
		byte hi = (byte) (c >>> 8);
		byte lo = (byte) (c & 0xff);
		return byteToHex(hi) + byteToHex(lo);
	}

	/**
	 * 把一个字节数组转换为一个整型
	 * 
	 * @param num
	 *            字节数组
	 * @return int
	 */
	public static int bytesToInt(byte[] num) {
		int result = 0;
		for (int i = 0; i < num.length; i++) {
			// TODO 未判断是否为数字
			result = result * 10 + num[i] - 48;
		}
		return result;
	}

	/**
	 * 把一个整型转换为一个字节数组
	 * 
	 * @param num
	 *            整型数据
	 * @param len
	 *            长度
	 * @return byte[]
	 */
	public static byte[] intToBytes(int num, int len) {
		String s = String.valueOf(num);
		byte[] b = new byte[len];
		for (int i = 0; i < len - s.length(); i++) {
			b[i] = 0 + 48;
		}
		int j = 0;
		for (int i = len - s.length(); i < len; i++, j++) {
			if (i >= 0) {
				b[i] = (byte) (Byte.parseByte(s.substring(j, j + 1)) + 48);
			}
		}
		return b;
	}

	/**
	 * 把一个整型转换为一个字符串
	 * 
	 * @param num
	 *            整型数据
	 * @param len
	 *            长度
	 * @return String
	 */
	public static String intToString(int num, int len) {
		String s = String.valueOf(num);
		char[] c = new char[len];
		for (int i = 0; i < len - s.length(); i++) {
			c[i] = '0';
		}
		int j = 0;
		for (int i = len - s.length(); i < len; i++, j++) {
			if (i >= 0) {
				// c[i] = (char) ((byte) s.charAt(j) + 48);
				c[i] = (char) (Byte.parseByte(s.substring(j, j + 1)) + 48);
			}
		}
		return new String(c);
	}

	/**
	 * 字符串转换为字节数组
	 * 
	 * @param s
	 *            字符串
	 * @return byte[]
	 */
	public static byte[] stringToBytes(String s) {
		byte[] result = new byte[s.length()];
		for (int i = 0; i < s.length(); i++) {
			result[i] = (byte) s.charAt(i);
		}
		return result;
	}

	/**
	 * 字节数组转换为字符串
	 * 
	 * @param b
	 *            字节数组
	 * @return String
	 */
	public static String bytesToString(byte[] b) {
		if (b == null)
			return null;
		String result = "";
		for (int i = 0; i < b.length; i++) {
			result = result + (char) b[i];
		}
		return result;
	}

	/**
	 * 字节数组转换为字符串
	 * 
	 * @param abyte0
	 *            字节数组
	 * @param j
	 *            起始位置
	 * @param k
	 *            判断k是不是奇数
	 * @return String
	 */
	public static String convertBytes(byte abyte0[], int j, int k) {
		if (k % 2 != 0) {
			return "";
		}
		int l = 0;
		int i1 = 1;
		if (k > 0) {
			StringBuffer stringbuffer = new StringBuffer();
			if (abyte0[j] == -1 && abyte0[j + 1] == -2) {
				j += 2;
				k -= 2;
			} else if (abyte0[j] == -2 && abyte0[j + 1] == -1) {
				j += 2;
				k -= 2;
				l = 1;
				i1 = 0;
			}
			for (int j1 = 0; j1 < k; j1 += 2) {
				char c1 = (char) (((char) abyte0[j + j1 + i1] & 0xff) << 8 | (char) abyte0[j + j1 + l] & 0xff);
				char c2 = 'A';
				if (c1 == 0) {
					break;
				}
				if (c1 >= 'Ａ' && c1 <= 'Ｚ') {
					c1 = (char) ((c1 - 65313) + 65);
				} else if (c1 >= 'ａ' && c1 <= 'ｚ') {
					c1 = (char) ((c1 - 65345) + 97);
				} else if (c1 >= '⑴' && c1 <= '⑼') {
					c1 = (char) ((c1 - 9332) + 49);
				} else if (c1 >= '⑽' && c1 <= '⒆') {
					c2 = '1';
					c1 = (char) ((c1 - 9341) + 48);
				} else if (c1 == '⒇') {
					c2 = '2';
					c1 = '0';
				}
				if (c2 == '1' || c2 == '2') {
					stringbuffer.append(c2);
				}
				stringbuffer.append(c1);
			}
			return stringbuffer.toString();
		} else {
			return "";
		}
	}

	/**
	 * 通过替换,生成新字节数组
	 * 
	 * @param src
	 *            原来的数组
	 * @param oldByte
	 *            被替换字节
	 * @param newByte
	 *            要添加的字节
	 * @return byte[]
	 */
	public static byte[] replaceByte(byte[] src, byte oldByte, byte newByte) {
		byte[] result = new byte[src.length];
		for (int i = 0; i < result.length; i++) {
			if (src[i] != oldByte) {
				result[i] = src[i];
			} else {
				result[i] = newByte;
			}
		}
		return result;
	}

	/**
	 * 删除字节数组里的所有的b字节
	 * 
	 * @param src
	 *            字节数组
	 * @param b
	 *            被删除的字节
	 * @return byte[]
	 */
	public static byte[] deleteByte(byte[] src, byte b) {
		int count = 0;
		for (int i = 0; i < src.length; i++) {
			if (src[i] == b) {
				count++;
			}
		}
		byte[] result = new byte[src.length - count];
		count = 0;
		for (int i = 0; i < src.length; i++) {
			if (src[i] != b) {
				result[count] = src[i];
				count++;
			}
		}
		return result;
	}

	/**
	 * byte数组转换成Short
	 * 
	 * @param b
	 * @return
	 */
	public static short byte2short(byte b[]) {
		short result = 0;
		if (b.length >= 2) {
			result = (short) (b[0] << 8 | b[1] & 0xff);
		} else if (b.length == 1) {
			result = (short) (b[0] & 0xff);
		}
		return result;
	}

	/**
	 * byte转换成Short，高位在前，低位在后
	 * 
	 * @param b
	 * 
	 * @param length
	 * 
	 * @return
	 */
	public static int bytes2int(byte b[],int length) {
		if (length == 1) {
			return b[0] & 0xff | (0x00 & 0xff) << 8 | (0x00 & 0xff) << 16 | (0x00 & 0xff) << 24;
		} else if (length == 2) {
			return b[1] & 0xff | (b[0] & 0xff) << 8 | (0x00 & 0xff) << 16 | (0x00 & 0xff) << 24;
		}else if (length == 3) {
			return b[2] & 0xff | (b[1] & 0xff) << 8 | (b[0] & 0xff) << 16 | (0x00 & 0xff) << 24;
		}else if (length == 4) {
			return b[3] & 0xff | (b[2] & 0xff) << 8 | (b[1] & 0xff) << 16 | (b[0] & 0xff) << 24;
		}
		return length;
	}
	
	public static short byte2short(byte b[], int offset) {
		return (short) (b[offset] << 8 | b[offset + 1] & 0xFF);
	}

	public static int byte2int(byte b[], int offset) {
		return b[offset + 3] & 0xff | (b[offset + 2] & 0xff) << 8 | (b[offset + 1] & 0xff) << 16
				| (b[offset] & 0xff) << 24;
	}

	public static int byte2int(byte b[]) {
		return b[3] & 0xff | (b[2] & 0xff) << 8 | (b[1] & 0xff) << 16 | (b[0] & 0xff) << 24;
	}

	public static long byte2long(byte b[]) {
		return (long) b[7] & (long) 255 | ((long) b[6] & (long) 255) << 8 | ((long) b[5] & (long) 255) << 16
				| ((long) b[4] & (long) 255) << 24 | ((long) b[3] & (long) 255) << 32 | ((long) b[2] & (long) 255) << 40
				| ((long) b[1] & (long) 255) << 48 | (long) b[0] << 56;
	}

	public static long byte2long(byte b[], int offset) {
		return (long) b[offset + 7] & (long) 255 | ((long) b[offset + 6] & (long) 255) << 8
				| ((long) b[offset + 5] & (long) 255) << 16 | ((long) b[offset + 4] & (long) 255) << 24
				| ((long) b[offset + 3] & (long) 255) << 32 | ((long) b[offset + 2] & (long) 255) << 40
				| ((long) b[offset + 1] & (long) 255) << 48 | (long) b[offset] << 56;
	}

	public static byte[] int2byte(int n) {
		byte b[] = new byte[4];
		b[0] = (byte) (n >> 24);
		b[1] = (byte) (n >> 16);
		b[2] = (byte) (n >> 8);
		b[3] = (byte) n;
		return b;
	}

	public static void int2byte(int n, byte buf[], int offset) {
		buf[offset] = (byte) (n >> 24);
		buf[offset + 1] = (byte) (n >> 16);
		buf[offset + 2] = (byte) (n >> 8);
		buf[offset + 3] = (byte) n;
		// buf[offset] = (byte)n ;
		// buf[offset+1] = (byte)(n>>8) ;
		// buf[offset+2] = (byte)(n>>16) ;
		// buf[offset+3] = (byte)(n>>24) ;
	}

	public static byte[] short2byte(int n) {
		byte b[] = new byte[2];
		b[0] = (byte) (n >> 8);
		b[1] = (byte) n;
		return b;
	}

	public static void short2byte(int n, byte buf[], int offset) {
		buf[offset] = (byte) (n >> 8);
		buf[offset + 1] = (byte) n;
	}

	public static byte[] long2byte(long n) {
		byte b[] = new byte[8];
		b[0] = (byte) (int) (n >> 56);
		b[1] = (byte) (int) (n >> 48);
		b[2] = (byte) (int) (n >> 40);
		b[3] = (byte) (int) (n >> 32);
		b[4] = (byte) (int) (n >> 24);
		b[5] = (byte) (int) (n >> 16);
		b[6] = (byte) (int) (n >> 8);
		b[7] = (byte) (int) n;
		return b;
	}

	public static void long2byte(long n, byte buf[], int offset) {
		buf[offset] = (byte) (int) (n >> 56);
		buf[offset + 1] = (byte) (int) (n >> 48);
		buf[offset + 2] = (byte) (int) (n >> 40);
		buf[offset + 3] = (byte) (int) (n >> 32);
		buf[offset + 4] = (byte) (int) (n >> 24);
		buf[offset + 5] = (byte) (int) (n >> 16);
		buf[offset + 6] = (byte) (int) (n >> 8);
		buf[offset + 7] = (byte) (int) n;
	}

	/**
	 * 判断两个字节数组是否相同
	 * 
	 * @param srcBytes
	 * @param descBytes
	 * @return
	 */
	public static boolean isSame(byte[] srcBytes, byte[] descBytes) {
		if (srcBytes == null || descBytes == null) {
			return false;
		}
		if (srcBytes.length != descBytes.length) {
			return false;
		}
		for (int i = 0; i < srcBytes.length; i++) {
			if (srcBytes[i] != descBytes[i]) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * 获取格式化后的长度为 6 的byte数组
	 * @return
	 */
	public static byte[] getNowDate() {
		SimpleDateFormat df = new SimpleDateFormat("yyMMddHHmmss");
		String datetime = df.format(new Date());
		return hexToBytes(datetime);
	}

	/**
	 * yy-MM-dd HH:mm:ss格式字符串转换byte数组
	 * @param date
	 * @return 格式化后的长度为 6 的byte数组
	 */
	public static byte[] string2DateBytes(String date) {
		SimpleDateFormat df = new SimpleDateFormat("yy-MM-dd HH:mm:ss");
		SimpleDateFormat df2 = new SimpleDateFormat("yyMMddHHmmss");
		String datetime = null;
		try {
			datetime = df2.format(df.parse(date));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return hexToBytes(datetime);
	}
	
	/**
	 * byte数组转换yy-MM-dd HH:mm:ss格式字符串
	 * @param tmpbytes
	 * @return
	 */
	public static String dateBytes2String(byte[] tmpbytes) {
		StringBuilder builder = new StringBuilder();
		try {
			if (!Tools.isEmptyOrNull(tmpbytes)) {
				if (tmpbytes != null && tmpbytes.length > 5) {
					builder.append("20" + ByteTools.byteToHex(tmpbytes[5]) + "-");
					builder.append(ByteTools.byteToHex((byte) (tmpbytes[4])) + "-");
					builder.append(ByteTools.byteToHex(tmpbytes[3]) + " ");
					builder.append(ByteTools.byteToHex(tmpbytes[2]) + ":");
					builder.append(ByteTools.byteToHex(tmpbytes[1]) + ":");
					builder.append(ByteTools.byteToHex(tmpbytes[0]));
				}
			}
		} catch (Exception e) {
		}
		return builder.toString();
	}
	
	/**
	 * 主方法测试
	 * 
	 * @param args
	 *            参数数组
	 */
	public static void main(String[] args) {
//		byte[] bytes = "0000000001".getBytes();
//		for (int i = 0; i < bytes.length; i++) {
//			System.out.println(byteToHex(bytes[i]));
//		}
//		System.out.println(bytesToHex(bytes));
//		for (byte b : string2DateBytes("17-09-27 17:19:33")) {
//			System.out.println(b);
//		};
		System.out.println(Integer.valueOf("000b", 16));
	}
}
