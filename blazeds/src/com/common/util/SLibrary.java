package com.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.FieldPosition;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Hashtable;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.text.MessageFormat;
import java.util.Calendar;
import java.io.Reader;

public class SLibrary {

	public final static String remarksAll = "#";
	public final static String remarksFrom = "//";
	public final static String CRLF = "\r\n";

	/**
	 * Formater
	 */
	public static DecimalFormat fmtBy = new DecimalFormat("##############0");
	public static DecimalFormat fmtBy0 = new DecimalFormat(
			"###,###,###,###,##0");
	public static DecimalFormat fmtBy1 = new DecimalFormat(
			"###,###,###,###,##0.0");
	public static DecimalFormat fmtBy2 = new DecimalFormat("###,###,###,##0.00");

	/**
	 * KSC5601 -> 8859_1
	 * 
	 * @param ko
	 *            - 문자열
	 * @return String
	 */
	public static String to8859_1(String ko) {
		if (ko == null) {
			return null;
		}
		try {
			return new String(ko.getBytes("KSC5601"), "8859_1");
		} catch (Exception e) {
			return ko;
		}
	}

	/**
	 * 8859_1 -> KSC5601
	 * 
	 * @param ko
	 *            - 문자열
	 * @return String
	 */
	public static String toKSC5601(String en) {
		if (en == null) {
			return null;
		}
		try {
			// 적용
			// en = new String(en.getBytes("8859_1"), "KSC5601");
			// 미적용
			return en;
		} catch (Exception e) {
			// return en;
		}
		return en;

	}

	/**
	 * 주어진 문자열이 null일 경우 ""을 리턴한다.
	 * 
	 * @param str
	 * 
	 * @return "" 또는 str
	 */
	public static String IfNull(String str) {

		if (str == null)
			return "";
		else
			return str;
	}

	/**
	 * 주어진 문자열이 null일 경우 ""을 리턴한다.
	 * 
	 * @param str
	 * @return "" 또는 str
	 */
	public static String IfNullDB(String str) {

		if (str == null || str.equals("null"))
			return "";
		else
			return str;
	}

	/**
	 * 주어진 문자열이 null일 경우 ""을 flase를 리턴한다.
	 * 
	 * @param str
	 * 
	 * @return boolean
	 */
	public static boolean isNull(String str) {

		if (str == null || str.trim().equals(""))
			return true;
		else
			return false;
	}
	
	public static String getDateAddSecond(String orgDate, int sec){
		return getDateTimeString( "yyyy-MM-dd HH:mm:ss", ((getTime(orgDate, "yyyy-MM-dd HH:mm:ss")/1000)+sec)*1000 );
	}

	public static String getDateTimeString() {
		return getDateTimeString("yyyy-MM-dd HH:mm:ss");
	}

	public static String getDateTimeString(String format) {

		return getDateTimeString(format, System.currentTimeMillis());
	}

	public static String getUnixtimeStringSecond() {

		String unix = Long.toString(System.currentTimeMillis());
		return unix.substring(0, unix.length() - 3);
	}

	/**
	 * 주어진 포맷에 따라 일자형 데이터를 포맷된 문자열로 리턴한다.
	 * 
	 * @param date
	 *            포맷할 일자값
	 * 
	 * @param format
	 *            일자형 데이터 포맷 문자열
	 * 
	 * @return 포맷된 일자 값
	 */
	public static String getDateTimeString(String format, long date) {

		return getDateTimeString(format, new java.util.Date(date));
	}

	/**
	 * 주어진 포맷에 따라 일자형 데이터를 포맷된 문자열로 리턴한다.
	 * 
	 * @param date
	 *            포맷할 일자값
	 * 
	 * @param format
	 *            일자형 데이터 포맷 문자열
	 * 
	 * @return 포맷된 일자 값
	 */
	public static String getDateTimeString(String format, java.util.Date date) {

		SimpleDateFormat df = new SimpleDateFormat(format, Locale.getDefault());
		StringBuffer ret = new StringBuffer();
		df.format(date, ret, new FieldPosition(0));
		return ret.toString();
	}

	/**
	 * String에 대한 Time을 return 한다.
	 * 
	 * @param data
	 *            문자열
	 * 
	 * @return yyyy-MM-dd형의 unixTime long값
	 */
	public static long getTime(String dateString) {

		return getTime(dateString, "yyyy-MM-dd");
	}

	/**
	 * String에 대한 Time을 return 한다.
	 * 
	 * @param data
	 *            문자열
	 * @param format
	 *            일자형 데이터 포맷 문자열
	 * @return unixTime long값
	 */
	public static long getTime(String dateString, String format) {

		SimpleDateFormat df = new SimpleDateFormat(format, Locale.getDefault());
		try {
			Date d = df.parse(dateString);
			return d.getTime();
		} catch (ParseException e) {
			return 0;
		}
	}

	/**
	 * String에 대한 Time을 return 한다.
	 * 
	 * @param data
	 *            문자열
	 * @param format
	 *            일자형 데이터 포맷 문자열
	 * @return unixTime String값
	 */
	public static String getTimeSecond(String dateString, String format) {

		String rslt = "";
		SimpleDateFormat df = new SimpleDateFormat(format, Locale.getDefault());
		try {
			Date d = df.parse(dateString);
			rslt = Long.toString(d.getTime());
		} catch (ParseException e) {
		}

		return rslt.substring(0, rslt.length() - 3);
	}

	/**
	 * String에 대한 Time을 일정 format으로 return 한다.
	 * 
	 * @param data
	 *            문자열 yyyyMMddhhmmss
	 * @param format
	 *            일자형 데이터 포맷 문자열
	 * @return unixTime long값
	 */
	public static String getDateTimeString(String dateString, String format) {

		long dLong = 0;

		SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss",
				Locale.getDefault());
		try {
			Date d = df.parse(dateString);
			dLong = d.getTime();
		} catch (ParseException e) {
			dLong = 0;
		}
		return getDateTimeString(format, dLong);
	}

	/**
	 * String에 대한 Time을 일정 format으로 return 한다.
	 * 
	 * @param data
	 *            문자열 yyyy-MM-dd hh:mm:ss
	 * @param format
	 *            일자형 데이터 포맷 문자열
	 * @return unixTime long값
	 */
	public static String getDateTimeStringStandard(String dateString,
			String format) {

		long dLong = 0;

		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss",
				Locale.getDefault());
		try {
			Date d = df.parse(dateString);
			dLong = d.getTime();
		} catch (ParseException e) {
			dLong = 0;
		}
		return getDateTimeString(format, dLong);
	}

	/**
	 * String에 대한 Time을 일정 format으로 return 한다.
	 * 
	 * @param data
	 *            문자열 yyyyMMddhhmmss
	 * @param format
	 *            일자형 데이터 포맷 문자열
	 * @return unixTime long값
	 */
	public static String getDateTimeString(String dateString, String format,
			String parmFormat) {

		long dLong = 0;

		SimpleDateFormat df = new SimpleDateFormat(parmFormat,
				Locale.getDefault());
		try {
			Date d = df.parse(dateString);
			dLong = d.getTime();
		} catch (ParseException e) {
			dLong = 0;
		}
		return getDateTimeString(format, dLong);
	}

	/**
	 * 현재 기준으로 n달 전후 날짜 반환
	 * 
	 * @param n
	 *            - 적용 달
	 * @param format
	 * @return String
	 */
	public static String diffOfMonth(int n, String format) {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, n);
		return SLibrary.getDateTimeString(format, cal.getTime());
	}

	/**
	 * 현재 기준으로 n일 전후 날짜 반환
	 * 
	 * @param n
	 *            - 적용 일
	 * @param format
	 * @return String
	 */
	public static String diffOfDay(int n, String format) {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, n);
		return SLibrary.getDateTimeString(format, cal.getTime());
	}

	/**
	 * 현재 기준으로 n달 전후 날짜 반환
	 * 
	 * @param n
	 *            - 적용 달
	 * @param format
	 * @return String
	 */
	public static String diffOfHour(int n, String format) {

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, n);
		return SLibrary.getDateTimeString(format, cal.getTime());
	}

	/**
	 * 해당 년 월의 말일을 구한다.
	 */
	public static int getLastDate(int year, int month) {

		Calendar calendar = Calendar.getInstance();

		month = month - 1;
		calendar.set(year, month, 1);
		int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		return lastDayOfMonth;
	}

	/**
	 * 해당 년 월의 말일을 구한다.
	 */
	public static int getLastDate(String pyear, String pmonth) {

		int year = Integer.parseInt(pyear);
		int month = Integer.parseInt(pmonth);

		Calendar calendar = Calendar.getInstance();

		month = month - 1;
		calendar.set(year, month, 1);
		int lastDayOfMonth = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);

		return lastDayOfMonth;
	}

	/**
	 * 문자열을 오른쪽에서 덧붙여 일정한 길이를 만든다
	 * 
	 * @param src
	 *            원형 문자열
	 * 
	 * @param length
	 *            리턴받을 길이
	 * 
	 * @return 만들어낸 문자열
	 */
	public static String padR(String src, int length) {
		return padR(src, length, " ");
	}

	/**
	 * 문자열을 오른쪽에서 덧붙여 일정한 길이를 만든다
	 * 
	 * @param src
	 *            원형 문자열
	 * 
	 * @param unit
	 *            덧붙여질 원소 문자열
	 * 
	 * @param length
	 *            리턴받을 길이
	 * 
	 * @return 만들어낸 문자열
	 */
	public static String padR(String src, int length, String unit) {

		StringBuffer ret = src == null ? new StringBuffer() : new StringBuffer(
				src);
		for (; ret.length() < length;) {
			ret.append(unit);
		}

		if (ret.length() > length)
			return ret.toString().substring(0, length);

		return ret.toString();
	}

	/**
	 * 문자열을 왼쪽에서 덧붙여 일정한 길이를 만든다
	 * 
	 * @param src
	 *            원형 문자열
	 * 
	 * @param length
	 *            리턴받을 길이
	 * 
	 * @return 만들어낸 문자열
	 */
	public static String padL(String src, int length) {
		return padL(src, length, " ");
	}

	/**
	 * 문자열을 왼쪽에서 덧붙여 일정한 길이를 만든다
	 * 
	 * @param src
	 *            원형 문자열
	 * 
	 * @param unit
	 *            덧붙여질 원소 문자열
	 * 
	 * @param length
	 *            리턴받을 길이
	 * 
	 * @return 만들어낸 문자열
	 */
	public static String padL(String src, int length, String unit) {
		// StringBuffer ret = src == null ? new StringBuffer() : new
		// StringBuffer(src);
		StringBuffer ret = new StringBuffer();
		for (; ret.length() + src.length() < length;) {
			ret.append(unit);
		}

		ret.insert(ret.length(), src);

		if (ret.length() > length) {
			return length <= 0 ? ret.toString() : ret.toString().substring(
					ret.length() - length, length);
		}

		return ret.toString();
	}

	/**
	 * 원형 문자열로 부터 일정한 부분을 찾아 목적하는 문자열로 모두 대치한다.
	 * 
	 * @param originalString
	 *            원형 문자열
	 * 
	 * @param findString
	 *            찾아낼 문자열
	 * 
	 * @param replacString
	 *            대치할 문자열
	 * 
	 * @return 만들어낸 문자열
	 */

	public static String replaceAll(String originalString, String findString,
			String replacString) {

		int s = 0;
		int e = 0;
		StringBuffer result = new StringBuffer();

		while ((e = originalString.indexOf(findString, s)) >= 0) {
			result.append(originalString.substring(s, e));
			result.append(replacString);
			s = e + findString.length();
		}

		result.append(originalString.substring(s));
		return result.toString();
	}

	/**
	 * 대상문자열(strTarget)에서 특정문자열(strSearch)을 찾아 지정문자열(strReplace 배열 Object[])로
	 * 치환하여 변경한 문자열을 반환한다.
	 * 
	 * @param strTarget
	 *            대상문자열
	 * 
	 * @param strSearch
	 *            변경대상의 특정문자열
	 * 
	 * @param strReplace
	 *            변경 시키는 지정문자열 배열 Object[]
	 * 
	 * @param isWhere
	 *            조건문임을 나타내는 스트링. 단, 대소문자 구분이 없다. 예) where
	 * 
	 * @return 변경완료된 문자열
	 */
	public static String replaceArrayStringWhere(String strTarget,
			String[] strSearch, Object[] strReplace, String isWhere) {

		if (isWhere == null) {
			return replaceArrayString(strTarget, strSearch, strReplace);
		}

		String result = null;

		String strCheck = new String(strTarget);
		StringBuffer strBuf = new StringBuffer();
		int i = 0;

		while (strCheck.length() != 0) {
			int begin = strCheck.indexOf(strSearch[i]);
			if (begin == -1) {
				strBuf.append(strCheck);
				break;
			} else {
				int end = begin + strSearch[i].length();
				strBuf.append(strCheck.substring(0, begin));
				if (strReplace[i] instanceof String) {
					// 조건절에 들어가는 조건문인경우
					if (strSearch[i].toUpperCase().startsWith(
							isWhere.toUpperCase())) {
						strBuf.append(strReplace[i]);
					}
					// 조건값인 경우
					else {
						strBuf.append("'" + strReplace[i] + "'");
					}
				} else {
					strBuf.append(strReplace[i]);
				}
				strCheck = strCheck.substring(end);
			}
			if (i < strSearch.length - 1)
				i++;
		}
		result = strBuf.toString();

		return result;
	}

	/**
	 * 대상문자열(strTarget)에서 특정문자열(strSearch)을 찾아 지정문자열(strReplace 배열 Object[])로
	 * 치환하여 변경한 문자열을 반환한다.
	 * 
	 * @param strTarget
	 *            대상문자열
	 * 
	 * @param strSearch
	 *            변경대상의 특정문자열
	 * 
	 * @param strReplace
	 *            변경 시키는 지정문자열 배열 Object[]
	 * 
	 * @return 변경완료된 문자열
	 */
	public static String replaceArrayString(String strTarget,
			String[] strSearch, Object[] strReplace) {
		String result = null;

		String strCheck = new String(strTarget);
		StringBuffer strBuf = new StringBuffer();
		int i = 0;

		while (strCheck.length() != 0) {
			int begin = strCheck.indexOf(strSearch[i]);
			if (begin == -1) {
				strBuf.append(strCheck);
				break;
			} else {
				int end = begin + strSearch[i].length();
				strBuf.append(strCheck.substring(0, begin));
				if (strReplace[i] instanceof String) {
					strBuf.append("'" + strReplace[i] + "'");
				} else {
					strBuf.append(strReplace[i]);
				}
				strCheck = strCheck.substring(end);
			}
			if (i < strSearch.length - 1)
				i++;
		}
		result = strBuf.toString();

		return result;
	}

	/**
	 * 패턴을 검사하여 특정 문자열로 변경한다
	 * 
	 * @param strTarget
	 *            대상문자열
	 * 
	 * @param pattern
	 *            패턴
	 * 
	 * @param pattern
	 *            변경문자열
	 * 
	 * @return 변경완료된 문자열
	 */
	public static String replacePattern(String strTarget, String pattern,
			String replaceString) {

		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(strTarget);
		return m.replaceAll(replaceString);
	}

	/**
	 * 패턴검사
	 * 
	 * @param strTarget
	 *            대상문자열
	 * 
	 * @param pattern
	 *            패턴
	 * 
	 * @return 매치 결과
	 */
	public static boolean searchPattern(String strTarget, String pattern) {

		Pattern p = Pattern.compile(pattern);
		Matcher m = p.matcher(strTarget);
		return m.matches();

	}

	/**
	 * 쿼리문자열(strTarget)에서 특정문자열(?)을 찾아 지정문자열(strReplace 배열 Object[])로 치환하여 변경한
	 * 문자열을 반환한다.
	 * 
	 * @param strTarget
	 *            대상문자열
	 * 
	 * @param strReplace
	 *            변경 시키는 지정문자열 배열 Object[]
	 * 
	 * @return 변경완료된 문자열
	 */
	public static String setStmt(String strTarget, Object[] strReplace) {
		String result = null;

		String strCheck = new String(strTarget);
		StringBuffer strBuf = new StringBuffer();
		int i = 0;

		while (strCheck.length() != 0) {
			int begin = strCheck.indexOf("?");
			if (begin == -1) {
				strBuf.append(strCheck);
				break;
			} else {
				int end = begin + 1;
				strBuf.append(strCheck.substring(0, begin));
				if (strReplace[i] instanceof String) {
					strBuf.append("'" + strReplace[i] + "'");
				} else {
					strBuf.append(strReplace[i]);
				}
				strCheck = strCheck.substring(end);
			}
			if (i < strReplace.length - 1)
				i++;
		}
		result = strBuf.toString();

		return result;
	}

	/**
	 * 게시물에서 최대 페이지 갯수
	 * 
	 * @return int
	 * 
	 * @param allPage
	 *            int
	 * 
	 * @param list_num
	 *            int
	 */
	public static int getMaxNum(int allPage, int list_num) {
		if ((allPage % list_num) == 0) {
			return allPage / list_num;
		} else {
			return allPage / list_num + 1;
		}
	}

	public static String readAll(InputStream in) throws IOException {

		return readAll(in, null);
	}

	public static String readAll(InputStream in, String cr) throws IOException {

		if (cr == null)
			cr = CRLF;

		BufferedReader bin = new BufferedReader(new InputStreamReader(in));
		int pos;
		String line;
		StringBuffer sbuf = new StringBuffer();
		while ((line = bin.readLine()) != null) {
			line = line.trim();
			if (line.startsWith(remarksAll))
				continue;

			pos = line.indexOf(remarksFrom);
			if (pos == 0)
				continue;
			else if (pos != -1)
				line = line.substring(0, pos);

			sbuf.append(line);
			sbuf.append(cr);
		}

		return sbuf.toString();
	}

	/**
	 * 체크박스 Y/N return
	 * 
	 * @param: String check
	 * 
	 * @return: String
	 */
	public static String getisCheck(String check) {
		String reStr = "";
		if (check != null) {
			if (check.equals("-1"))
				reStr = "Y";
			else
				reStr = "N";
		} else {
			reStr = "";
		}
		return reStr;
	}

	/**
	 * 윤년체크
	 * 
	 * @param: String strNm
	 * 
	 * @return: boolean
	 */
	public static boolean checkDay(String strNm) {
		if (strNm == null || strNm.length() != 8)
			return true;
		int p_year = Integer.parseInt(strNm.substring(0, 4));
		if ((p_year % 4) == 0) {
			if ((p_year % 100) == 0 && (p_year % 400) != 0)
				return true;
			return false;
		} else
			return true;
	}

	/**
	 * 한달전 날짜 계산
	 * 
	 * @param: String strNm
	 * 
	 * @return: String
	 */
	public static String beforeDay(String strNm) {
		if (strNm != null && strNm.length() == 8) {
			int[] monStr = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

			String reYear = strNm.substring(0, 4);
			int reYear_a = Integer.parseInt(reYear) - 1;

			int reMon = Integer.parseInt(strNm.substring(4, 6));
			String reMon_s = zeroFill(reMon + "", 2);
			String reMon_sa = zeroFill((reMon - 1) + "", 2);

			int reDay = Integer.parseInt(strNm.substring(6, 8));

			String reDay_sa = zeroFill((reDay + 1) + "", 2);

			if (reMon == 1) {
				if ("31".equals(strNm.substring(6, 8))) {
					return reYear + "0101";
				} else {
					return reYear_a + "12" + reDay_sa;
				}
			} else if (monStr[reMon - 2] <= (reDay)) {
				return reYear + reMon_s + "01";
			} else {
				return reYear + reMon_sa + reDay_sa;
			}
		}
		return "";
	}

	/**
	 * 한달후 날짜 계산
	 * 
	 * @param: String strNm
	 * 
	 * @return: String
	 */
	public static String afterDay(String strNm) {
		if (strNm != null && strNm.length() == 8) {
			int[] monStr = { 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31 };

			String reYear = strNm.substring(0, 4);
			int reYear_a = Integer.parseInt(reYear) + 1;

			int reMon = Integer.parseInt(strNm.substring(4, 6));
			String reMon_s = zeroFill(reMon + "", 2);
			String reMon_sa = zeroFill((reMon + 1) + "", 2);

			int reDay = Integer.parseInt(strNm.substring(6, 8));

			String reDay_sa = zeroFill((reDay - 1) + "", 2);

			if (reMon == 12) {
				if ("01".equals(strNm.substring(6, 8))) {
					return reYear + "1231";
				} else {
					return reYear_a + "01" + reDay_sa;
				}
			} else if (monStr[reMon] <= (reDay)) {
				return reYear + reMon_sa + monStr[reMon];
			} else if (reDay == 1) {
				return reYear + reMon_s + monStr[reMon - 1];
			} else {
				return reYear + reMon_sa + reDay_sa;
			}
		}
		return "";
	}

	/**
	 * '1' ==> size + str(i) = '000001'
	 * 
	 * @param: String strNm
	 * 
	 * @return: String
	 */
	public static String zeroFill(String strNm, int size) {
		String reStr = "";
		int NmSize;

		NmSize = strNm.length();

		for (int i = 0; size - NmSize > i; i++) {
			reStr = "0" + reStr;
		}
		reStr = reStr + strNm;

		return reStr;
	}

	/**
	 * '000001' ==> '1'
	 * 
	 * @param: String strNm
	 * 
	 * @return: String
	 */
	public static String cutFill(String strNm) {
		String reStr;
		reStr = Integer.parseInt(strNm) + "";

		return reStr;
	}

	/**
	 * 날짜, 수량,금액의 mask를 remove 후 숫자만 return[음수포함]
	 * 
	 * @return java.lang.String
	 * 
	 * @param s
	 *            java.lang.String
	 */
	public static String removeMask(String s) {
		String format = "-0123456789";

		if (s == null || s.equals("")) {
			return "";
		}

		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);

			if (c == '-' && s.indexOf(c) > 0)
				continue;

			if (format.indexOf(c) > -1)
				buf.append(c);
		}

		return buf.toString();

	}

	/**
	 * 숫자에 대해 ',' 를 3자리마다 삽입(문자열로 리턴) 작성 날짜: <2004.09.15>
	 * 
	 * @param: <|>
	 * 
	 * @return:
	 */
	public static String addComma(String src) {

		String resultValue = "";
		int count = 0;
		int pos = 3;

		if (src == null || src.length() == 0)
			return "0";

		String[] data = SLibrary.split(src, ".", true);

		if (data.length > 0) {
			for (int i = data[0].length() - 1; i >= 0; i--) {
				resultValue = data[0].charAt(i) + resultValue;
				count++;
				if (count == pos && i != 0) {
					resultValue = "," + resultValue;
					count = 0;
				}
			}
		}

		if (data.length == 2)
			resultValue += "." + data[1];

		return resultValue;
	}

	/**
	 * 숫자에 대해 ',' 를 3자리마다 삽입(문자열로 리턴) 작성 날짜: <2004.09.15>
	 * 
	 * @param: <|>
	 * 
	 * @return:
	 */
	public static String addComma(int num) {

		String src = Integer.toString(num);
		String resultValue = "";
		int count = 0;
		int pos = 3;

		if (src == null || src.length() == 0)
			return resultValue;

		String[] data = SLibrary.split(src, ".", true);

		if (data.length > 0) {
			for (int i = data[0].length() - 1; i >= 0; i--) {
				resultValue = data[0].charAt(i) + resultValue;
				count++;
				if (count == pos && i != 0) {
					resultValue = "," + resultValue;
					count = 0;
				}
			}
		}

		if (data.length == 2)
			resultValue += "." + data[1];

		return resultValue;
	}

	/**
	 * 입력받은 String에서 Comma를 삭제한다.
	 * 
	 * @return String Comma가 삭제된 String
	 * 
	 * @param value
	 *            String Comma가 있는 String
	 */
	public static String commaRemove(String value) {
		String newValue = "";
		for (int i = 0; i < value.length(); i++) {
			if (value.charAt(i) != ',')
				newValue += String.valueOf(value.charAt(i));
		}
		return newValue;
	}

	/**
	 * 대상문자열(strTarget)에서 구분문자열(strDelim)을 기준으로 문자열을 분리하여 각 분리된 문자열을 배열에 할당하여
	 * 반환한다.
	 * 
	 * @param strTarget
	 *            분리 대상 문자열
	 * @param strDelim
	 *            구분시킬 문자열로서 결과 문자열에는 포함되지 않는다.
	 * @param bContainNull
	 *            구분되어진 문자열중 공백문자열의 포함여부. true : 포함, false : 포함하지 않음.
	 * @return 분리된 문자열을 순서대로 배열에 격납하여 반환한다.
	 */

	public static String[] split(String strTarget, String strDelim,
			boolean bContainNull) {
		// StringTokenizer는 구분자가 연속으로 중첩되어 있을 경우 공백 문자열을 반환하지 않음.
		// 따라서 아래와 같이 작성함.
		int index = 0;
		String[] resultStrArray = new String[search(strTarget, strDelim) + 1];
		String strCheck = new String(strTarget);

		while (strCheck.length() != 0) {
			int begin = strCheck.indexOf(strDelim);
			if (begin == -1) {
				resultStrArray[index] = strCheck;
				break;
			} else {
				int end = begin + strDelim.length();
				if (bContainNull) {
					resultStrArray[index++] = strCheck.substring(0, begin);
				}
				strCheck = strCheck.substring(end);
				if (strCheck.length() == 0 && bContainNull) {
					resultStrArray[index] = strCheck;
					break;
				}
			}
		}
		return resultStrArray;
	}

	/**
	 * 대상문자열(strTarget)에서 지정문자열(strSearch)이 검색된 횟수를, 지정문자열이 없으면 0 을 반환한다.
	 * 
	 * @param strTarget
	 *            대상문자열
	 * 
	 * @param strSearch
	 *            검색할 문자열
	 * 
	 * @return 지정문자열이 검색되었으면 검색된 횟수를, 검색되지 않았으면 0 을 반환한다.
	 */
	public static int search(String strTarget, String strSearch) {
		int result = 0;
		String strCheck = new String(strTarget);

		for (int i = 0; i < strTarget.length();) {
			int loc = strCheck.indexOf(strSearch);
			if (loc == -1) {
				break;
			} else {
				result++;
				i = loc + strSearch.length();
				strCheck = strCheck.substring(i);
			}
		}
		return result;
	}

	// string int로 형변환
	public static int intValue(String str) {

		try {
			if (str == null || str.length() == 0)
				return 0;

			return Integer.parseInt(str);
		} catch (NumberFormatException nf) {
			return 0;
		}
	}

	/**
	 * 처리 결과 Check
	 */
	public static boolean isResult(int[] r) {
		if (r == null || r.length == 0)
			return false;
		else {
			for (int i = 0; i < r.length; i++)
				if (r[i] == 0)
					return false;
		}
		return true;
	}

	/**
	 * 문자배열을 숫자 배열로 변화
	 * 
	 * @param String
	 *            [] 문자 배열
	 * @return int [] 정수 배열
	 */
	public static int[] changeIntArrayToStringArray(String[] strArray) {

		int[] rslt = null;
		if (strArray != null) {

			rslt = new int[strArray.length];
			int cnt = strArray.length;
			for (int i = 0; i < cnt; i++) {

				rslt[i] = SLibrary.intValue(strArray[i]);
			}
		}
		return rslt;
	}

	/**
	 * 문자배열을 숫자 배열로 변화
	 * 
	 * @param String
	 *            [] 문자 배열
	 * @return int [] 정수 배열
	 */
	public static int[] changeIntArrayToStringArray(String[] strArray,
			int returnlength) {

		int[] rslt = null;
		if (strArray != null) {

			rslt = new int[returnlength];
			int cnt = strArray.length;
			for (int i = 0; i < cnt; i++) {

				rslt[i] = SLibrary.intValue(strArray[i]);
			}
		}
		return rslt;
	}

	/**
	 * Object[]중 공백문자가 제거된 배열을 리턴한다.
	 */
	public static Object[] getNotBlankArray(Object[] obj) {

		List<Object> list = new ArrayList<Object>();

		for (int i = 0; i < list.size(); i++) {

			if (obj[i] instanceof String) {
				if (!obj[i].equals(""))
					list.add(obj[i]);
			} else {
				list.add(obj[i]);
			}
		}

		return list.toArray();
	}

	/**
	 * 자바스크립트 경고창 띄우기및 스크립트 @param alert - 경고 내용 @param script - 이후 script
	 * 
	 * @return str : script 문자열
	 */
	public static String alertScript(String alert, String script) {

		if (alert == null || alert.trim().equals(""))
			return "<script type=\"text/javascript\" language=\"javascript\"> "
					+ script + "</script>";
		else
			return "<script type=\"text/javascript\" language=\"javascript\"> alert(\""
					+ alert + "\"); " + script + "</script>";
	}

	/**
	 * 문자열을 숫자로 변환
	 */
	public static int parseInt(String str) {

		int rslt = 0;
		try {
			rslt = Integer.parseInt(str);
		} catch (Exception e) {
		}

		return rslt;
	}

	/**
	 * 문자열을 숫자로 변환
	 */
	public static long parseLong(String str) {

		long rslt = 0;
		try {
			rslt = Long.parseLong(str);
		} catch (Exception e) {
		}

		return rslt;
	}

	/**
	 * 문자열에 Object배열을 Format하여 반환한다.
	 * */
	public static String messageFormat(String pattern, Object[] obj) {

		return MessageFormat.format(pattern, obj);
	}

	/**
	 * 구분자로 나누어 첫 배열의 중복을 제거&정렬 후 return
	 */
	public static String[] removeDupList(String list, String strSplit) {

		String[] phoneList = list.split("\\r\\n");
		StringTokenizer st;
		Hashtable<String, String> hashTable = new Hashtable<String, String>();
		String phone = null;
		String name = null;
		StringBuffer result = new StringBuffer();
		StringBuffer dupList = new StringBuffer();
		boolean b = false;

		String[] arrResult = new String[2];

		for (int i = 0; i < phoneList.length; i++) {
			st = new StringTokenizer(phoneList[i], strSplit);
			b = false;

			phone = "";
			name = "";

			while (st.hasMoreTokens()) {
				if (b == false) {
					phone = st.nextToken();
					b = true;
				} else {
					name += st.nextToken();
				}
			}
			if (hashTable.containsKey(phone)) {
				dupList.append(" + " + phone + " " + name + "\\r\\n");
			} else {
				hashTable.put(phone, name);
				result.append(phone + " " + name + "\\r\\n");
			}
		}

		/*
		 * Enumeration keys = hashTable.keys();
		 * 
		 * while (keys.hasMoreElements()) {
		 * 
		 * key = (String) keys.nextElement(); result.append(key + " " + (String)
		 * hashTable.get(key) + "\\r\\n");
		 * 
		 * }
		 */
		arrResult[0] = result.toString();
		arrResult[1] = dupList.toString();
		return arrResult;
	}

	/**
	 * 구분자로 나누어 첫 배열의 중복을 제거&정렬 후 return
	 */
	public static String[] removeCheckDeletList(String list,
			Hashtable<String, String> hs) {

		String[] phoneList = list.split("\\r\\n");
		StringTokenizer st;
		String phone = null;
		String name = null;
		// String key = null;
		StringBuffer result = new StringBuffer();
		StringBuffer noList = new StringBuffer();
		boolean b = false;

		String[] arrResult = new String[2];

		for (int i = 0; i < phoneList.length; i++) {
			st = new StringTokenizer(phoneList[i]);
			b = false;

			phone = "";
			name = "";

			while (st.hasMoreTokens()) {
				if (b == false) {
					phone = st.nextToken();
					b = true;
				} else {
					name += st.nextToken();
				}
			}
			if (hs.containsKey(phone)) {
				noList.append(" + " + phone + " " + name + "\\r\\n");
			} else {
				result.append(phone + " " + name + "\\r\\n");
			}
		}

		/*
		 * Enumeration keys = hashTable.keys();
		 * 
		 * while (keys.hasMoreElements()) {
		 * 
		 * key = (String) keys.nextElement(); result.append(key + " " + (String)
		 * hashTable.get(key) + "\\r\\n");
		 * 
		 * }
		 */
		arrResult[0] = result.toString();
		arrResult[1] = noList.toString();
		return arrResult;
	}

	/**
	 * 기본 구분자로 나누어 첫 배열의 중복을 제거&정렬 후 return
	 */
	public static String[] removeDupList(String list) {

		String[] phoneList = list.split("\\r\\n");
		StringTokenizer st;
		Hashtable<String, String> hashTable = new Hashtable<String, String>();
		String phone = null;
		String name = null;
		// String key = null;
		StringBuffer result = new StringBuffer();
		StringBuffer dupList = new StringBuffer();
		boolean b = false;

		String[] arrResult = new String[2];

		for (int i = 0; i < phoneList.length; i++) {
			st = new StringTokenizer(phoneList[i]);
			b = false;

			phone = "";
			name = "";

			while (st.hasMoreTokens()) {
				if (b == false) {
					phone = st.nextToken();
					b = true;
				} else {
					name += st.nextToken();
				}
			}
			if (hashTable.containsKey(phone)) {
				dupList.append(" + " + phone + " " + name + "\\r\\n");
			} else {
				hashTable.put(phone, name);
				result.append(phone + " " + name + "\\r\\n");
			}
		}

		/*
		 * Enumeration keys = hashTable.keys();
		 * 
		 * while (keys.hasMoreElements()) {
		 * 
		 * key = (String) keys.nextElement(); result.append(key + " " + (String)
		 * hashTable.get(key) + "\\r\\n");
		 * 
		 * }
		 */
		arrResult[0] = result.toString();
		arrResult[1] = dupList.toString();
		return arrResult;
	}

	/**
	 * 문자열의 byte를 구한다.
	 * 
	 * @param strSource
	 * @return
	 */
	public static int getByte(String strSource) {

		int byteSize = 0;
		char[] charArray = strSource.toCharArray();
		for (int i = 0; i < strSource.length(); i++) {

			if (charArray[i] < 256)
				byteSize += 1;
			else
				byteSize += 2;
		}

		return byteSize;
	}

	/**
	 * String을 지정된 길이만큼만 출력할 수 있도록 하며, 만약 일부분만이 출력되는 경우에는 지정된 postfix 문자열을 끝에
	 * 추가한다.
	 * 
	 * cutByte값은 문자열의 byte 길이를 의미한다. 한글과 같이 2byte character는 2로 계산하며, 영문 및 반각
	 * 기호는 1로 계산한다.
	 * 
	 * @param strSource
	 *            변환하고자 하는 원본 문자열. null인 경우 공백문자열이 반환된다.
	 * @param cutByte
	 *            변환후 총 길이(postfix 문자열의 길이 포함). 반드시 strPostfix문자열의 byte크기 이상의
	 *            숫자를 입력해야 한다. 그렇지 않은 경우 원본 문자열을 그대로 반환한다.
	 * @param bTrim
	 *            원본 문자열의 앞뒤에 공백문자가 있을경우 trim을 수행할지를 결정한다.
	 * @param strPostfix
	 *            문자열이 잘릴경우 이를 표현하기 위한 문자열. null인 경우 "..."이 추가된다.
	 * @return 변환된 결과 문자열을 return 한다. 단, strSource가 null인 경우 공백문자열이 반환되며
	 *         cutByte가 strPostfix문자열의 byte크기 미만의 숫자가 오는 경우 원본 문자열을 그대로 반환한다.
	 */
	public static String cutBytes(String strSource, int cutByte, boolean bTrim,
			String strPostfix) {

		if (strSource == null)
			return "";

		int postfixSize = 0;
		for (int i = 0; i < strPostfix.length(); i++) {
			if (strPostfix.charAt(i) < 256)
				postfixSize += 1;
			else
				postfixSize += 2;
		}

		if (postfixSize > cutByte)
			return strSource;

		if (bTrim)
			strSource = strSource.trim();
		char[] charArray = strSource.toCharArray();

		int strIndex = 0;
		int byteLength = 0;
		for (; strIndex < strSource.length(); strIndex++) {

			int byteSize = 0;
			if (charArray[strIndex] < 256) {
				// 1byte character 이면
				byteSize = 1;
			} else {
				// 2byte character 이면
				byteSize = 2;
			}

			if ((byteLength + byteSize) > cutByte - postfixSize) {
				break;
			}

			byteLength = byteLength += byteSize;
		}

		if (strIndex == strSource.length())
			strPostfix = "";
		else {
			if ((byteLength + postfixSize) < cutByte)
				strPostfix = " " + strPostfix;
		}

		return strSource.substring(0, strIndex) + strPostfix;
	}

	/**
	 * String을 지정된 길이만큼만 출력할 수 있도록 하며, 만약 일부분만이 출력되는 경우에는 지정된 postfix 문자열을 끝에
	 * 추가한다.
	 * 
	 * cutByte값은 문자열의 byte 길이를 의미한다. 한글과 같이 2byte character는 2로 계산하며, 영문 및 반각
	 * 기호는 1로 계산한다.
	 * 
	 * @param strSource
	 *            변환하고자 하는 원본 문자열. null인 경우 공백문자열이 반환된다.
	 * @param cutByte
	 *            변환후 총 길이(postfix 문자열의 길이 포함). 반드시 strPostfix문자열의 byte크기 이상의
	 *            숫자를 입력해야 한다. 그렇지 않은 경우 원본 문자열을 그대로 반환한다.
	 * @param bTrim
	 *            원본 문자열의 앞뒤에 공백문자가 있을경우 trim을 수행할지를 결정한다.
	 * @param strPostfix
	 *            문자열이 잘릴경우 이를 표현하기 위한 문자열. null인 경우 "..."이 추가된다.
	 * @return 변환된 결과 문자열과 나머지 문자열
	 */
	public static int cutBytesIndex(String strSource, int cutByte, boolean bTrim) {

		int strIndex = 0;

		if (strSource == null)
			return strIndex;

		if (bTrim)
			strSource = strSource.trim();
		char[] charArray = strSource.toCharArray();

		int byteLength = 0;
		for (; strIndex < strSource.length(); strIndex++) {

			int byteSize = 0;
			if (charArray[strIndex] < 256) {
				// 1byte character 이면
				byteSize = 1;
			} else {
				// 2byte character 이면
				byteSize = 2;
			}

			if ((byteLength + byteSize) > cutByte) {
				break;
			}

			byteLength = byteLength += byteSize;
		}

		return strIndex;
	}

	/**
	 * String을 지정된 길이 단위로 나누어 반환한다.
	 * 
	 * cutByte값은 문자열의 byte 길이를 의미한다. 한글과 같이 2byte character는 2로 계산하며, 영문 및 반각
	 * 기호는 1로 계산한다.
	 * 
	 * @param strSource
	 *            변환하고자 하는 원본 문자열. null인 경우 공백문자열이 반환된다.
	 * @param cutByte
	 *            변환후 총 길이(postfix 문자열의 길이 포함). 반드시 strPostfix문자열의 byte크기 이상의
	 *            숫자를 입력해야 한다. 그렇지 않은 경우 원본 문자열을 그대로 반환한다.
	 * @param bTrim
	 *            원본 문자열의 앞뒤에 공백문자가 있을경우 trim을 수행할지를 결정한다.
	 * @return 변환된 결과 문자열을 return 한다. 단, strSource가 null인 경우 공백문자열이 반환되며
	 *         cutByte가 strPostfix문자열의 byte크기 미만의 숫자가 오는 경우 원본 문자열을 그대로 반환한다.
	 */
	public static String[] cutBytesGetArray(String strSource, int cutByte,
			boolean bTrim) {

		String[] arrRslt = null;
		int totalSize = SLibrary.getByte(strSource);
		int cnt = (int) Math.ceil(totalSize / cutByte);// 올림
		cnt++;// 나머지 byte를 위해

		arrRslt = new String[cnt];

		int pre = 0;
		int cur = 0;
		for (int i = 0; i < cnt; i++) {

			cur = SLibrary.cutBytesIndex(strSource, cutByte, bTrim);
			arrRslt[i] = strSource.substring(pre, cur);
			pre = cur;

		}

		return arrRslt;
	}

	/**
	 * 휴대폰번호 검사
	 */
	public static boolean checkHp(String str) {

		String hp = SLibrary.replaceAll(str.trim(), "-", "");
		return SLibrary.searchPattern(hp, "(0\\d{2})(\\d{3,4})(\\d{4})");
	}

	/**
	 * FAX 검사
	 */
	public static boolean checkFax(String str) {

		String hp = SLibrary.replaceAll(str.trim(), "-", "");
		return SLibrary.searchPattern(hp, "(0\\d{1,3})(\\d{3,4})(\\d{4})");
	}

	/**
	 * Email 검사
	 */
	public static boolean checkEmail(String str) {

		String pattern = "(^[_0-9a-zA-Z-\\.]+[_0-9a-zA-Z-\\.]*@[_0-9a-zA-Z-]+\\.[a-zA-Z]+[a-zA-Z\\.]+([a-zA-Z]+)*$)";
		return SLibrary.searchPattern(str, pattern);
	}

	/**
	 * String [] 을 string '','','' 로 변환
	 * 
	 * @param temp
	 *            - 문자배열
	 * @return String
	 */
	public static String inQuery(String[] temp) {

		String in = "";
		if (temp != null) {
			for (int i = 0; i < temp.length; i++) {
				if (i == temp.length - 1) {
					in += "'" + temp[i] + "'";
				} else {
					in += "'" + temp[i] + "',";
				}
			}
		}
		return in;
	}

	/**
	 * int [] 을 string 1,23,1 로 변환
	 * 
	 * @param temp
	 *            - 문자배열
	 * @return String
	 */
	public static String inQuery(int[] temp) {

		String in = "";
		if (temp != null) {
			for (int i = 0; i < temp.length; i++) {
				if (i == temp.length - 1) {
					in += Integer.toString(temp[i]);
				} else {
					in += Integer.toString(temp[i]) + ",";
				}
			}
		}
		return in;
	}

	/**
	 * 한자리 숫자에 '0'을 붙여 준다
	 * 
	 * @param str
	 *            - 문자열
	 */
	public static String addTwoSizeNumber(String str) {
		int num = Integer.parseInt(str);
		if (num < 10) {
			return "0" + Integer.toString(num);
		} else {
			return str;
		}

	}

	/**
	 * 한자리 숫자에 '0'을 붙여 준다
	 * 
	 * @param str
	 *            - 문자열
	 */
	public static String addTwoSizeNumber(int num) {
		if (num < 10) {
			return "0" + Integer.toString(num);
		} else {
			return Integer.toString(num);
		}

	}

	/**
	 * 페이지 쿼리 문자열을 반환한다. 역순
	 */
	public static String pgString(String sql) {

		String preString = "SELECT * FROM ( SELECT A.*, ROWNUM RNUM , MAX(ROWNUM) OVER(ORDER BY ROWNUM DESC) TOTALCNT FROM ( ";
		String lastString = " ) A  ) WHERE RNUM BETWEEN (TOTALCNT +1)-? AND (TOTALCNT +1)-? ORDER BY RNUM DESC";

		return preString + sql + lastString;
	}

	/**
	 * 페이지 쿼리 문자열을 반환한다. 정순
	 */
	public static String pgStringASC(String sql) {

		String preString = "SELECT * FROM ( SELECT A.*, ROWNUM RNUM , MAX(ROWNUM) OVER(ORDER BY ROWNUM DESC) TOTALCNT FROM ( ";
		String lastString = " ) A  ) WHERE RNUM BETWEEN ? AND ? ORDER BY RNUM ASC";

		return preString + sql + lastString;
	}

	/**
	 * 전체건수 쿼리 문자열을 반환한다.
	 */
	public static String countQueryString(String sql) {

		String preString = "SELECT count(*) as cnt FROM (  ";
		String lastString = " ) ";

		return preString + sql + lastString;
	}

	/**
	 * 문자열 배열에서 해당 문자가 있는지 확인한다
	 */
	public static boolean isArrayValue(String[] arr, String findString) {

		boolean b = false;
		int cnt = 0;
		if (arr != null) {
			cnt = arr.length;
			for (int i = 0; i < cnt; i++) {
				if (arr[i].equals(findString)) {
					b = true;
					break;
				}

			}
		}

		return b;

	}

	/**
	 * 배열을 받아 연결될 문자열로 연결한다. 이때 각 엘레멘트 사이에 구분문자열을 추가한다.<br>
	 * 
	 * @param aobj
	 *            문자열로 만들 배열
	 * @param s
	 *            각 엘레멘트의 구분 문자열
	 * @return 연결된 문자열
	 * 
	 *         <code>
	 * String[] source = new String[] {"AAA","BBB","CCC"};<br>
	 * String result = TextUtil.join(source,"+");<br>
	 * </code> <code>result</code>는 <code>"AAABBBCCC"</code>를 가지게 된다.
	 */
	public static String join(Object aobj[], String s) {
		StringBuffer stringbuffer = new StringBuffer();
		int i = aobj.length;
		if (i > 0) {
			stringbuffer.append(aobj[0].toString());
		}
		for (int j = 1; j < i; j++) {
			stringbuffer.append(s);
			stringbuffer.append(aobj[j].toString());
		}

		return stringbuffer.toString();
	}

	/**
	 * 2009-08-01 12:00:0.0 뒤에 두자리 삭제
	 */
	public static String yyyymmddhhmiss(String strDate) {

		if (strDate == null || strDate.length() < 20)
			return strDate;
		else
			return strDate.substring(0, strDate.length() - 2);
	}

	/**
	 * Clob Data type -> String type
	 * 
	 * @param re
	 * @return
	 * @throws IOException
	 */
	public static String getClobString(Reader re) throws IOException {

		StringBuffer data = new StringBuffer();
		char[] buf = new char[1024];

		int cnt = 0;
		if (re != null) {
			while ((cnt = re.read(buf)) != -1) {
				data.append(buf, 0, cnt);
			}
		}

		return data.toString();

	}

	/**
	 * html을 텍스트로 보이도록 변경한다.
	 * 
	 * @param str
	 * @return
	 */
	public static String textToHtml(String str) {

		return str.replaceAll("<", "&lt").replaceAll("</", "&lt/");

	}

	/*
	 * public static void loadingBegin(String msg, javax.servlet.jsp.JspWriter
	 * out) throws Exception{ out.print(alertScript("",
	 * "parent.loadingBegin(\""+msg+"\");")); out.flush(); }
	 * 
	 * public static void loadingEnd(javax.servlet.jsp.JspWriter out) throws
	 * Exception{ out.print(alertScript("", "parent.loadingEnd();"));
	 * out.flush(); }
	 */
	public static String IfNull(HashMap<String, String> hm, String key) {

		if (hm.containsKey(key))
			return hm.get(key);
		else
			return "";
	}
	
	public static String IfNull(String[] arr, int index) {

		if (arr != null && arr.length > index)
			return SLibrary.IfNull( arr[index] );
		else
			return "";
	}

	/**
	 * HashMap 을 <option> 테그로 변환한다.
	 * 
	 * @param rs
	 * @param selectedValue
	 * @return
	 * @throws Exception
	 */
	public static String getSelectTag(HashMap<String, String> hm,
			String selectedValue) {

		StringBuffer strBuffer = new StringBuffer();
		String tmp = "";
		String selected = "";
		Iterator<String> keys = hm.keySet().iterator();

		while (keys.hasNext()) {
			tmp = keys.next();
			selected = hm.get(tmp).equals(selectedValue) ? "selected" : "";
			strBuffer.append("<option value=\"" + tmp + "\" " + selected + ">"
					+ hm.get(tmp) + "</option>");
		}

		return strBuffer.toString();
	}

	public static String propertiesHangle(String s) {

		String rslt = "";
		try {
			if (s != null)
				rslt = new String(s.getBytes("UTF-8"), "euc-kr");
		} catch (UnsupportedEncodingException e) {
			System.out.println(e.toString());
		}

		return rslt;
	}

	public static String getExcelColumnTitle(int index) {

		int base = (int) (char) 'A';
		int div = (int) (char) 'Z' - base + 1;
		StringBuffer buf = new StringBuffer();

		if ((index - 1) >= 0) {

			// twoLength String
			if (index - 1 >= div) {

				buf.append(new Character((char) (base
						+ (int) ((index - 1) / div) - 1)).toString());
				buf.append(new Character(
						(char) (base + (int) ((index - 1) % div))).toString());
			} else {
				buf.append(new Character((char) (base + index - 1)).toString());
			}
		}

		return buf.toString();
	}

	public static String getPhone(String phone) {
		String number = phone.replaceAll("[^\\d]*", "");
		boolean b = Pattern
				.matches("^0[17][016789]-?\\d{3,4}-?\\d{4}$", number);
		return b ? number : null;
	}

	// 파일을 존재여부를 확인하는 메소드
	public static Boolean isFile(String path) {
		
		Boolean b = false;
		File f = new File(path);
		if (f.exists())	b = true;
		return b;
	}
	
	public static int mysqlTotalCount(Connection conn) {
		
		Statement stmt = null;
		ResultSet rs = null;
		int rslt = 0;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery("SELECT FOUND_ROWS()");
			while (rs.next()){ rslt = rs.getInt(1); }
			rs.close();
			stmt.close();
		} catch (Exception e) {	}
		
		return rslt;
	}

}
