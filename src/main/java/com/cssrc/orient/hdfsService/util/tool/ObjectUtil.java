package com.cssrc.orient.hdfsService.util.tool;



import java.io.*;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * ObjectUtil<br>
 * <ol>
 * <li>isEmpty(); isNotEmpty();
 * <li>toString();
 * <li>equals();
 * <li>clone();
 * </ol>
 * 
 * @author xiaocao000 
 * Date��2009-3-14
 * @version 1.0
 */
public class ObjectUtil
{
    private static final String NULL = "null";
    private static final String EMPTY_STR = "";

    /** ����toStringʱ���Ե����������ַ������� */
    private static final String[] IGNORE_FIELD_NAMES = {"log", "logger",
            "serialVersionUID", // ʵ�����л��ӿ�ʱ�Զ���ӵ��ֶ�
    };

    /** ����toStringʱ���Ե����������ַ������� */
    private static final String[] IGNORE_FIELD_CLASSES = {
            "org.apache.log4j.Logger",
            "java.util.logging.Logger", // ��־��¼������������
            "java.lang.Object", "java.lang.Class",
            "java.lang.reflect.AccessibleObject", "java.lang.reflect.Field",
            "java.lang.reflect.Method", "java.lang.reflect.Constructor",};

    // =====================================================
    // isNull(); isNotNull();
    // isEmpty(); isNotEmpty();
    // =====================================================
    /**
     * �ж�ָ���Ķ����Ƿ�Ϊnull��
     *
     * @param obj Ҫ�жϵĶ���ʵ��
     * @return true��Ϊnull false����null
     */
    public static boolean isNull(Object obj)
    {
        return obj == null;
    }

    /**
     * �ж�ָ���Ķ����Ƿ�Ϊnull��
     *
     * @param obj Ҫ�жϵĶ���ʵ��
     * @return true����null false��Ϊnull
     */
    public static boolean isNotNull(Object obj)
    {
        return !isNull(obj);
    }

    /**
     * �ж�ָ���Ķ��������Ƿ�Ϊ�ա�
     *
     * @param objs Ҫ�жϵĶ�������ʵ��
     * @return true����������Ϊ�� false����������ǿ�
     */
    public static boolean isEmpty(Object[] objs)
    {
        return isNull(objs) || objs.length == 0;
    }

    /**
     * �ж�ָ���Ķ��������Ƿ�ǿա�
     *
     * @param objs Ҫ�жϵĶ�������ʵ��
     * @return true����������ǿ� false����������Ϊ��
     */
    public static boolean isNotEmpty(Object[] objs)
    {
        return !isEmpty(objs);
    }

    /**
     * �ж�ָ���ļ������Ƿ�Ϊ�ա�<BR>
     * Ϊ�յĺ�����ָ���ü���Ϊnull�����߸ü��ϲ������κ�Ԫ�ء�
     *
     * @param coll Ҫ�жϵļ���ʵ��
     * @return true������Ϊ�� false�����Ϸǿ�
     */
    public static boolean isEmpty(Collection<?> coll)
    {
        return isNull(coll) || coll.isEmpty();
    }

    /**
     * �ж�ָ���ļ������Ƿ�ǿա� <BR>
     * �ǿյĺ�����ָ���ü��Ϸ�null���Ҹü��ϰ���Ԫ�ء�
     *
     * @param coll Ҫ�жϵļ���ʵ��
     * @return true�����Ϸǿ� false������Ϊ��
     */
    public static boolean isNotEmpty(Collection<?> coll)
    {
        return !isEmpty(coll);
    }

    /**
     * �ж�ָ����Map���Ƿ�Ϊ�ա� <BR>
     * Ϊ�յĺ�����ָ����MapΪnull�����߸�Map�������κ�Ԫ�ء�
     *
     * @param map Ҫ�жϵ�Mapʵ��
     * @return true��MapΪ�� false��Map�ǿ�
     */
    public static boolean isEmpty(Map<?, ?> map)
    {
        return isNull(map) || map.isEmpty();
    }

    /**
     * �ж�ָ����Map���Ƿ�ǿա�<BR>
     * �ǿյĺ�����ָ����Map��null���Ҹ�Map����Ԫ�ء�
     *
     * @param map Ҫ�жϵ�Mapʵ��
     * @return true��Map�ǿ� false��MapΪ��
     */
    public static boolean isNotEmpty(Map<?, ?> map)
    {
        return !isEmpty(map);
    }

    /**
     * �ж�ָ����CharSequence���Ƿ�Ϊ�ա� <BR>
     * Ϊ�յĺ�����ָ����CharSequenceΪnull�����߸�CharSequence����Ϊ0��<BR>
     * ��String���ͣ���ȻҲ����CharSequence��<BR>
     * ����Ϊͨ���ַ�����������Ĵ���Ҫ������ʹ�÷���isEmpty(String str);
     *
     * @param charSeq Ҫ�жϵ�CharSequenceʵ��
     * @return true��CharSequenceΪ�� false��CharSequence�ǿ�
     */
    public static boolean isEmpty(CharSequence charSeq)
    {
        return isNull(charSeq) || charSeq.length() == 0;
    }

    /**
     * �ж�ָ����CharSequence���Ƿ�ǿա� <BR>
     * �ǿյĺ�����ָ����CharSequence��null���Ҹ�CharSequence����Ԫ�ء�<BR>
     * ��String���ͣ���ȻҲ����CharSequence�� <BR>
     * ����Ϊͨ���ַ�����������Ĵ���Ҫ������ʹ�÷���isNotEmpty(String str);
     *
     * @param charSeq Ҫ�жϵ�CharSequenceʵ��
     * @return true��CharSequence�ǿ� false��CharSequenceΪ��
     */
    public static boolean isNotEmpty(CharSequence charSeq)
    {
        return !isEmpty(charSeq);
    }

    /**
     * �ж�ָ����String���Ƿ�Ϊ�ա� <BR>
     * Ϊ�յĺ�����ָ����StringΪnull�����߸�StringΪ�մ�""��
     *
     * @param str Ҫ�жϵ�Stringʵ��
     * @return true��StringΪ�� false��String�ǿ�
     */
    public static boolean isEmpty(String str)
    {
        return isNull(str) || EMPTY_STR.equals(str);
    }

    /**
     * �ж�ָ����String���Ƿ�ǿա� <BR>
     * �ǿյĺ�����ָ����String��null���Ҹ�String��Ϊ�մ���
     *
     * @param str Ҫ�жϵ�Stringʵ��
     * @return true��String�ǿ� false��StringΪ��
     */
    public static boolean isNotEmpty(String str)
    {
        return !isEmpty(str);
    }

    /**
     * �ж�ָ����String���Ƿ�Ϊ�ա�<BR>
     * Ϊ�յĺ�����ָ����StringΪnull�����߸�StringΪ�մ�""��
     *
     * @param str Ҫ�жϵ�Stringʵ��
     * @return true��StringΪ�� false��String�ǿ�
     */
    public static boolean isTrimEmpty(String str)
    {
        return isNull(str) || EMPTY_STR.equals(str.trim());
    }

    /**
     * �ж�ָ����String���Ƿ�ǿա� <BR>
     * �ǿյĺ�����ָ����String��null���Ҹ�String��Ϊ�մ���
     *
     * @param str Ҫ�жϵ�Stringʵ��
     * @return true��String�ǿ� false��StringΪ��
     */
    public static boolean isTrimNotEmpty(String str)
    {
        return !isTrimEmpty(str);
    }

    // =============================================================
    // toString()
    // 1. Java �����ͱ𣬰������������ͱ� ֱ�ӵ��ö������toString()
    // 2. �򵥵�Bean���� ���÷������ȡ��ÿ�����Ե�ֵ
    // 3. Java�������� �磺Collection��Map�Լ���������
    // 4. ��Java�����ͱ�Ķ���������Ϊ����
    // 5. enum��������ȷ����object.toString()������ΪJava��������
    // 6. �̳й�ϵ������ȡ�����и��࣬��ȡ���������� �ݹ�toString
    // 7. �ڲ��ദ���ڲ��������һ�����ⲿ���Ĭ�����ã������ƻ����"this$",���Լ���
    // =============================================================

    /**
     * ͨ�õ�toString������ <BR>
     * 1. �Զ�����ʽ���ڵĻ����ͱ����飬�磺byte[],long[]��Ҫ���⴦��<BR>
     *
     * @param obj Ҫת��Ϊ�ַ����Ķ���
     * @return ת������ַ���
     */
    public static String toString(Object obj)
    {
        if (isNull(obj))
        {
            return NULL;
        }

        Class<?> clazz = obj.getClass();
        if (clazz.equals(Object.class) || clazz.equals(Class.class))
        {
            return obj.toString();
        }

        // �����Java�����ͱ� ����ֱ�ӵ���toString�õ���ȷ����Ϣ
        if (ClassUtil.isJavaBasicType(clazz))
        {
            return obj.toString();
        }

        StringBuilder sb = new StringBuilder();

        // �����������
        if (clazz.isArray())
        {
            // Java�����ͱ�����飬��������Arrays.toString()���ת����
            // ����תΪ������������deepToString()��ֻ��Ϊ����Ӹ�����Ϣ�ͼ��ٴ���
            if (ClassUtil.isPrimitiveArray(clazz))
            {
                Object[] objs = new Object[] {obj};
                sb.append(clazz.getSimpleName()).append("=");
                String objsStr = Arrays.deepToString(objs);
                sb.append(objsStr.substring(1, objsStr.length() - 1));
                return sb.toString();
            }

            // �������͵������Ѿ���������ֻ�����Ƕ������顣
            // �������ͱ�������������ַֿ������ԭ���ǣ�
            // �����ͱ������������Arrays.deepToString(Object[] objs)תΪString
            // �����������еĶ���û������toSring ����ʱҲ��ҪdeepToString��
            // Arrays.deepToString����������Ϊ����
            return toString((Object[]) obj);
        }

        // ����Ǽ�����Collection
        if (Collection.class.isAssignableFrom(clazz))
        {
            return toString((Collection<?>) obj);
        }

        // �����ӳ����Map
        if (Map.class.isAssignableFrom(clazz))
        {
            return toString((Map<?, ?>) obj);
        }

        // �������Bean���͵Ķ��󣬷���ÿ���ֶ�toString
        sb.append(clazz.getSimpleName()).append("{");
        Field[] fields = ClassUtil.getAllField(clazz);

        if (fields.length > 0)
        {
            boolean isAppend = false;
            AccessibleObject.setAccessible(fields, true);
            for (Field field : fields)
            {
                // ��Ҫ���Ե�����
                if (isIgnoreField(field))
                {
                    continue;
                }
                isAppend = true;
                sb.append(field.getName()).append("=");
                try
                {
                    // ���������ÿһ�����Զ�����ObjectUtil.toString()
                    // ���ڲ��࣬����ɶ�ջ�������Ҫ���
                    sb.append(toString(field.get(obj))).append(",");
                }
                catch (Exception e)
                {
                    sb.append("???,");
                }
            }
            if (isAppend)
            {
                sb.deleteCharAt(sb.length() - 1);
            }
        }
        sb.append("}");
        return sb.toString();
    }

    public static <T> void addAll(List<T> list, T[] array)
    {
        if (isNull(list) || isNull(array))
        {
            return;
        }

        for (T t : array)
        {
            list.add(t);
        }
    }

    /**
     * �ж������ֶ��Ƿ�ΪtoStringʱ���Ե��ֶ����ԡ�
     *
     * @param field ��Ҫ�ж��Ƿ�������Ե��ֶΡ�
     * @return true��Ϊ�������� false���Ǻ�������
     */
    public static boolean isIgnoreField(Field field)
    {
        if (isNull(field))
        {
            return false;
        }

        // ���������ж��ܹ�ȸ��������ж�Ҫ��
        return isIgnoreFieldByName(field) || isIgnoreFieldByClass(field);
    }

    /**
     * �����ֶε�Name�ж��Ƿ���ֶ�Ϊ�Ѷ���ĺ�������
     *
     * @param field ��Ҫ�ж��Ƿ�������Ե��ֶΡ�
     * @return true��Ϊ�������� false���Ǻ�������
     */
    private static boolean isIgnoreFieldByName(Field field)
    {
        // ������Ѷ������Ҫ���Ե�����
        for (String fieldName : IGNORE_FIELD_NAMES)
        {
            if (fieldName.equals(field.getName()))
            {
                return true;
            }
        }

        // �������Ҫģ��ƥ����Ҫ���Ե�����
        // ˵�������һ���ඨ�����ڲ��࣬�ڲ��ౣ��һ�����ⲿ���Ĭ������
        // ��ᵼ�µݹ�toStringʱ��ջ����������Ĭ�������ֲ���ҪtoString�����Ժ���
        if (field.getName().indexOf("this$") != -1)
        {
            return true;
        }

        return false;
    }

    /**
     * �����ֶε�Class�ж��Ƿ���ֶ�Ϊ�Ѷ���ĺ�������

     *
     * @param field ��Ҫ�ж��Ƿ�������Ե��ֶΡ�
     * @return true��Ϊ�������� false���Ǻ�������
     */
    private static boolean isIgnoreFieldByClass(Field field)
    {
        Class<?> clazz = null;
        for (String className : IGNORE_FIELD_CLASSES)
        {
            clazz = ReflectUtil.forName(className);
            if (clazz != null && clazz.isAssignableFrom(field.getType()))
            {
                return true;
            }
        }

        return false;
    }

    /**
     * ����������ת��ΪString��
     *
     * @param objs ��������
     * @return ת������ַ���
     */
    public static String toString(Object[] objs)
    {
        if (isNull(objs))
        {
            return NULL;
        }
        if (objs.length == 0)
        {
            return "[]";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(objs.getClass().getSimpleName()).append("(length=").append(
                objs.length).append(")=[");
        for (Object obj : objs)
        {
            sb.append(toString(obj)).append(',');
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append(']');

        return sb.toString();
    }

    /**
     * ��Collectionת��ΪString��
     *
     * @param coll Collection
     * @return ת������ַ���
     */
    public static String toString(Collection<?> coll)
    {
        if (isNull(coll))
        {
            return NULL;
        }
        if (coll.isEmpty())
        {
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(coll.getClass().getSimpleName()).append("(size=").append(
                coll.size()).append(")={");
        for (Object obj : coll)
        {
            sb.append(toString(obj)).append(',');
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append('}');

        return sb.toString();
    }

    /**
     * ��Mapת��ΪString��
     *
     * @param map Map
     * @return ת������ַ���
     */
    public static String toString(Map<?, ?> map)
    {
        if (isNull(map))
        {
            return NULL;
        }
        if (map.isEmpty())
        {
            return "{}";
        }

        StringBuilder sb = new StringBuilder();
        sb.append(map.getClass().getSimpleName());
        sb.append("(size=").append(map.size()).append(")={");
        for (Map.Entry<?, ?> entry : map.entrySet())
        {
            sb.append(toString(entry.getKey())).append('=');
            sb.append(toString(entry.getValue())).append(',');
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.append('}');

        return sb.toString();
    }
    // =============================================================
    // equals()
    // 1. Java �����ͱ𣬰������������ͱ� ֱ�ӵ��ö������equals()
    // 2. �򵥵�Bean���� ���÷�����ƱȽ�ÿ�����Ե�equals
    // 3. Java�������� �磺Collection��Map�Լ���������
    // 4. ��Java�����ͱ�Ķ���������Ϊ������δ���
    // 5. enum����
    // 6. �ڲ�����δ���
    // =============================================================

    /**
     * ͨ�õ�equals ����
     *
     * @param a ����a
     * @param b ����b
     * @return true ��������ֵ��� false ��������ֵ�����
     */
    public static boolean equals(Object a, Object b)
    {
        // ����κ�һ������Ϊnull����Ϊֵ���ȡ�
        if (isNull(a) || isNull(b))
        {
            return false;
        }

        // �����ͬһ��������Ϊֵ��ȡ�
        if (a == b)
        {
            return true;
        }

        Class<?> aClazz = a.getClass();

        // �������������ͬһ�ͱ���Ϊֵ���ȡ�
        if (!aClazz.equals(b.getClass()))
        {
            return false;
        }

        // ���¿�ʼ����Ϊ������������ͬ�ͱ�Ķ���
        // ��Java�������͵ģ�����ֱ��equals
        if (ClassUtil.isJavaBasicType(aClazz))
        {
            return a.equals(b);
        }

        // �����������
        if (aClazz.isArray())
        {
            // Java�����ͱ�����飬��������Arrays.toString()���ת����
            // ����תΪ������������deepToString()��ֻ��Ϊ����Ӹ�����Ϣ�ͼ��ٴ���
            if (ClassUtil.isPrimitiveArray(aClazz)
                    || ClassUtil.isPrimitiveWrapperArray(aClazz))
            {
                return Arrays.deepEquals(new Object[] {a}, new Object[] {b});
            }

            // �������͵������Ѿ���������ֻ�����Ƕ������顣
            // �������ͱ�������������ַֿ������ԭ���ǣ�
            // �����ͱ������������Arrays.deepEquals()
            // �����������еĶ���û������equals ����ʱҲ��ҪdeepEquals��
            // Arrays.deepEquals����������Ϊ����
            return equals((Object[]) a, (Object[]) b);
        }

        // ����Ǽ�����
        if (ClassUtil.isCollection(aClazz))
        {
            return equals((Collection<?>) a, (Collection<?>) b);
        }

        // �����ӳ����
        if (ClassUtil.isMap(aClazz))
        {
            return equals((Map<?, ?>) a, (Map<?, ?>) b);
        }

        // ��������Ϊ�ǻ���Bean���ͣ��Ƚ�ÿһ�����ԡ�
        try
        {
            Field[] fields = aClazz.getDeclaredFields();

            // û����������
            if (isEmpty(fields))
            {
                return true;
            }

            AccessibleObject.setAccessible(fields, true);
            for (int i = 0; i < fields.length; i++)
            {
                Field field = fields[i];

                if (!equals(field.get(a), field.get(b)))
                {
                    return false;
                }
            }
        }
        catch (Exception e)
        {
            return false;
        }

        return true;
    }

    /**
     * ͨ�õ�equals ����
     *
     * @param a ��������a
     * @param b ��������b
     * @return true ������������ֵ��� false ������������ֵ�����
     */
    public static boolean equals(Object[] a, Object[] b)
    {
        if (isNull(a) || isNull(b))
        {
            return false;
        }

        // ȥ����δ��룬�����ֶ�������������ͣ�ֻ�Ƚ�ֵ
        if (!a.getClass().equals(b.getClass()))
        {
            return false;
        }

        if (a.length != b.length)
        {
            return false;
        }

        for (int i = 0; i < a.length; i++)
        {
            if (!equals(a[i], b[i]))
            {
                return false;
            }
        }

        return true;
    }

    /**
     * ͨ�õ�equals ����
     *
     * @param a Collection a
     * @param b Collection b
     * @return true ����Collectionֵ��� false ����Collectionֵ�����
     */
    public static boolean equals(Collection<?> a, Collection<?> b)
    {
        if (isNull(a) || isNull(b))
        {
            return false;
        }

        // ȥ����δ��룬�����ּ��Ͼ��������ͣ�ֻ�Ƚ�ֵ
        if (!a.getClass().equals(b.getClass()))
        {
            return false;
        }

        if (a.size() != b.size())
        {
            return false;
        }

        return equals(a.toArray(), b.toArray());
    }

    /**
     * ͨ�õ�equals ����
     *
     * @param a Map a
     * @param b Map b
     * @return true ����Mapֵ��� false ����Mapֵ�����
     */
    public static boolean equals(Map<?, ?> a, Map<?, ?> b)
    {
        if (isNull(a) || isNull(b))
        {
            return false;
        }

        // ȥ����δ��룬������ӳ����������ͣ�ֻ�Ƚ�ֵ
        if (!a.getClass().equals(b.getClass()))
        {
            return false;
        }

        if (a.size() != b.size())
        {
            return false;
        }

        for (Map.Entry<?, ?> entry : a.entrySet())
        {
            if (!b.containsKey(entry.getKey())
                    || !equals(entry.getValue(), b.get(entry.getKey())))
            {
                return false;
            }
        }

        return true;
    }

    /**
     * clone��������Ҫ��¡�Ķ������ʵ��Serializable�ӿ�
     *
     * @param obj ��Ҫ��¡�Ķ���
     * @return ��¡�Ķ���
     */
    public static Object clone(Object obj)
    {
        if (isNull(obj))
        {
            return null;
        }

        Object cloneObj = null;
        if (obj instanceof Serializable)
        {
            ByteArrayOutputStream baos = null;
            ObjectOutputStream oos = null;
            ObjectInputStream ois = null;

            try
            {
                baos = new ByteArrayOutputStream();
                oos = new ObjectOutputStream(baos);
                oos.writeObject(obj);
                ois = new ObjectInputStream(new ByteArrayInputStream(baos
                        .toByteArray()));

                cloneObj = ois.readObject();
            }
            catch (Exception e)
            {
                return cloneObj;
            }
            finally
            {
                try {
                	if(baos!=null)
                		baos.close();
                	if(oos!=null)
                		oos.close();
                	if(ois!=null)
                		ois.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
            }
        }

        return cloneObj;
    }
    /**
     * �Ա����������е�һЩ�ֶ�ֵ�Ƿ���ͬ
     * �ֶ�ֵ��ȫ��ͬ����True
     * ��һ�����߶���ֶ�ֵ����ͬ����false
     * @param obj1
     * @param obj2
     * @param properties
     * @return
     */
    public static boolean equalProperty(Object obj1,Object obj2,String[] properties){
    	int equalCount = 0;
    	if(properties != null){
    		for(String prop : properties){
    			Object val1 = ReflectUtil.getFieldValue(obj1, prop);
    			Object val2 = ReflectUtil.getFieldValue(obj2, prop);
    			if(val1 != null && val2 !=null){
    				if(val1 instanceof Enum){//ö�������õȺ��ж�
    					if(val1 == val2){
    						equalCount++;
    					}
    				}else if(val1.equals(val2)){
    					equalCount++;
    				}
    			}else if(val1 == null && val2 ==null){
    				equalCount++;
    			}
    		}
    		if(equalCount == properties.length){
    			return true;
    		}
    	}
    	return false;
    }
}
