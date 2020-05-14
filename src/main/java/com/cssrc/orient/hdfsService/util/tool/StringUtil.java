package com.cssrc.orient.hdfsService.util.tool;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class StringUtil {


    public static String[] split(String str, String splitsign) {
        int index;
        if (str == null || splitsign == null) {
            return null;
        }
        ArrayList<String> al = new ArrayList<String>();
        while ((index = str.indexOf(splitsign)) != -1) {
            al.add(str.substring(0, index));
            str = str.substring(index + splitsign.length());
        }
        al.add(str);
        return (String[]) al.toArray(new String[0]);
    }


    public static String replace(String from, String to, String source) {
        if (source == null || from == null || to == null)
            return null;
        StringBuffer str = new StringBuffer("");
        int index = -1;
        while ((index = source.indexOf(from)) != -1) {
            str.append(source.substring(0, index) + to);
            source = source.substring(index + from.length());
            index = source.indexOf(from);
        }
        str.append(source);
        return str.toString();
    }

    public static String htmlencode(String str) {
        if (str == null) {
            return null;
        }
        return replace("\"", "&quot;", replace("<", "&lt;", str));
    }


    public static String htmldecode(String str) {
        if (str == null) {
            return null;
        }

        return replace("&quot;", "\"", replace("&lt;", "<", str));
    }

    private static final String _BR = "<br/>";


    public static String htmlshow(String str) {
        if (str == null) {
            return null;
        }

        str = replace("<", "&lt;", str);
        str = replace(" ", "&nbsp;", str);
        str = replace("\r\n", _BR, str);
        str = replace("\n", _BR, str);
        str = replace("\t", "&nbsp;&nbsp;&nbsp;&nbsp;", str);
        return str;
    }


    public static String toLength(String str, int length) {
        if (str == null) {
            return null;
        }
        if (length <= 0) {
            return "";
        }
        try {
            if (str.getBytes("GBK").length <= length) {
                return str;
            }
        } catch (Exception e) {
        }
        StringBuffer buff = new StringBuffer();

        int index = 0;
        char c;
        length -= 3;
        while (length > 0) {
            c = str.charAt(index);
            if (c < 128) {
                length--;
            } else {
                length--;
                length--;
            }
            buff.append(c);
            index++;
        }
        buff.append("...");
        return buff.toString();
    }

    public static boolean isInteger(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?[\\d]+$");
        return pattern.matcher(str).matches();
    }


    public static boolean isDouble(String str) {
        Pattern pattern = Pattern.compile("^[-\\+]?\\d+\\.\\d+$");
        return pattern.matcher(str).matches();
    }


    public static boolean isLetter(String str) {
        if (str == null || str.length() < 0) {
            return false;
        }
        Pattern pattern = Pattern.compile("[\\w\\.-_]*");
        return pattern.matcher(str).matches();
    }


    public static String parse(String content) {
        String email = null;
        if (content == null || content.length() < 1) {
            return email;
        }

        int beginPos;
        int i;
        String token = "@";
        String preHalf = "";
        String sufHalf = "";

        beginPos = content.indexOf(token);
        if (beginPos > -1) {

            String s = null;
            i = beginPos;
            while (i > 0) {
                s = content.substring(i - 1, i);
                if (isLetter(s))
                    preHalf = s + preHalf;
                else
                    break;
                i--;
            }

            i = beginPos + 1;
            while (i < content.length()) {
                s = content.substring(i, i + 1);
                if (isLetter(s))
                    sufHalf = sufHalf + s;
                else
                    break;
                i++;
            }

            email = preHalf + "@" + sufHalf;
            if (isEmail(email)) {
                return email;
            }
        }
        return null;
    }


    public static boolean isEmail(String email) {
        if (email == null || email.length() < 1 || email.length() > 256) {
            return false;
        }
        Pattern pattern = Pattern
                .compile("^\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*$");
        return pattern.matcher(email).matches();
    }


    public static boolean isChinese(String str) {
        Pattern pattern = Pattern.compile("[\u0391-\uFFE5]+$");
        return pattern.matcher(str).matches();
    }


    public static boolean isBlank(String str) {
        return str == null || str.trim().length() == 0;
    }


    public static boolean isEmpty(String str) {
        return str == null || str.length() == 0;
    }


    public static boolean isPrime(int x) {
        if (x <= 7) {
            if (x == 2 || x == 3 || x == 5 || x == 7)
                return true;
        }
        int c = 7;
        if (x % 2 == 0)
            return false;
        if (x % 3 == 0)
            return false;
        if (x % 5 == 0)
            return false;
        int end = (int) Math.sqrt(x);
        while (c <= end) {
            if (x % c == 0) {
                return false;
            }
            c += 4;
            if (x % c == 0) {
                return false;
            }
            c += 2;
            if (x % c == 0) {
                return false;
            }
            c += 4;
            if (x % c == 0) {
                return false;
            }
            c += 2;
            if (x % c == 0) {
                return false;
            }
            c += 4;
            if (x % c == 0) {
                return false;
            }
            c += 6;
            if (x % c == 0) {
                return false;
            }
            c += 2;
            if (x % c == 0) {
                return false;
            }
            c += 6;
        }
        return true;
    }




    public static String removeSameString(String str) {
        Set<String> mLinkedSet = new LinkedHashSet<String>();// set���ϵ����������Ӽ��������ظ�
        String[] strArray = str.split(" ");// ���ݿո�(������ʽ)�ָ��ַ���
        StringBuffer sb = new StringBuffer();

        for (int i = 0; i < strArray.length; i++) {
            if (!mLinkedSet.contains(strArray[i])) {
                mLinkedSet.add(strArray[i]);
                sb.append(strArray[i] + " ");
            }
        }
        return sb.toString();
    }


    public static String encoding(String src) {
        if (src == null)
            return "";
        StringBuilder result = new StringBuilder();
        if (src != null) {
            src = src.trim();
            for (int pos = 0; pos < src.length(); pos++) {
                switch (src.charAt(pos)) {
                    case '\"':
                        result.append("&quot;");
                        break;
                    case '<':
                        result.append("&lt;");
                        break;
                    case '>':
                        result.append("&gt;");
                        break;
                    case '\'':
                        result.append("&apos;");
                        break;
                    case '&':
                        result.append("&amp;");
                        break;
                    case '%':
                        result.append("&pc;");
                        break;
                    case '_':
                        result.append("&ul;");
                        break;
                    case '#':
                        result.append("&shap;");
                        break;
                    case '?':
                        result.append("&ques;");
                        break;
                    default:
                        result.append(src.charAt(pos));
                        break;
                }
            }
        }
        return result.toString();
    }


    public static boolean isHandset(String handset) {
        try {
            String regex = "^1[\\d]{10}$";
            Pattern pattern = Pattern.compile(regex);
            Matcher matcher = pattern.matcher(handset);
            return matcher.matches();

        } catch (RuntimeException e) {
            return false;
        }
    }


    public static String decoding(String src) {
        if (src == null)
            return "";
        String result = src;
        result = result.replace("&quot;", "\"").replace("&apos;", "\'");
        result = result.replace("&lt;", "<").replace("&gt;", ">");
        result = result.replace("&amp;", "&");
        result = result.replace("&pc;", "%").replace("&ul", "_");
        result = result.replace("&shap;", "#").replace("&ques", "?");
        return result;
    }


    public static String toString(Object[] objects) {
        if (ObjectUtil.isNull(objects))
            return null;
        if (objects.length == 0)
            return "";
        StringBuffer sb = new StringBuffer();
        for (Object obj : objects) {
            sb.append(',').append(ObjectUtil.toString(obj));
        }
        return sb.substring(1);
    }


    public static String toString(Object[] objects, String splitStr) {
        if (ObjectUtil.isNull(objects))
            return null;
        if (objects.length == 0)
            return "";
        StringBuffer sb = new StringBuffer();
        for (Object obj : objects) {
            sb.append(splitStr).append(ObjectUtil.toString(obj));
        }
        return sb.substring(1);
    }


    public static String toString(Collection<?> coll) {
        if (ObjectUtil.isNull(coll))
            return null;
        if (coll.size() == 0)
            return "";
        StringBuffer sb = new StringBuffer();
        for (Object obj : coll) {
            sb.append(',').append(ObjectUtil.toString(obj));
        }
        return sb.substring(1);
    }


    public static String toString(Collection<?> coll, String splitStr) {
        if (ObjectUtil.isNull(coll))
            return null;
        if (coll.size() == 0)
            return "";
        StringBuffer sb = new StringBuffer();
        for (Object obj : coll) {
            sb.append(splitStr).append(ObjectUtil.toString(obj));
        }
        return sb.substring(1);
    }

    public static String toSameString(String str, int length, String splitStr) {
        if (ObjectUtil.isNull(str))
            return null;
        if (length <= 0)
            return "";
        if (length == 1)
            return str;
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < length; i++) {
            sb.append(splitStr).append(str);
        }
        return sb.substring(1);
    }


    public static Integer[] toIntArray(String str, String splitStr) {
        if (ObjectUtil.isNull(str))
            return null;
        if (StringUtil.isEmpty(str))
            return null;
        String[] array = str.split(splitStr);
        Integer[] array2 = new Integer[array.length];
        for (int i = 0; i < array.length; i++) {
            array2[i] = Integer.parseInt(array[i]);
        }
        return array2;
    }


    public static boolean hasStr(String[] strs, String str) {
        if (ObjectUtil.isNotNull(strs) && ObjectUtil.isNotNull(str)) {
            for (String s : strs) {
                if (str.equals(s)) {
                    return true;
                }
            }
        }
        return false;
    }


    public static String replaceRegex(String str, Object... args) {
        if (ObjectUtil.isNull(args))
            return str;
        for (int i = 0; i < args.length; i++) {
            String arg = args[i].toString();
            str = str.replace("{" + i + "}", arg);
        }
        return str;
    }

    public static String trimSufffix(String toTrim, String trimStr) {
        while (toTrim.endsWith(trimStr)) {
            toTrim = toTrim.substring(0, toTrim.length() - trimStr.length());
        }
        return toTrim;
    }
}
