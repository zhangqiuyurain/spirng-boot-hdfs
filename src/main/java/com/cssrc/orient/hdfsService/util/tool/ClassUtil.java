package com.cssrc.orient.hdfsService.util.tool;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLDecoder;
import java.util.*;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * TODO ClassUtil
 * 
 * @author xiaocao000 
 * Date��2009-3-14
 * @version 1.0
 */
public class ClassUtil
{
    public static boolean isJavaBasicType(Class<?> clazz)
    {
        if (clazz == null)
        {
            return false;
        }

        return clazz.isPrimitive() || Number.class.isAssignableFrom(clazz)
                || Character.class.isAssignableFrom(clazz)
                || Boolean.class.isAssignableFrom(clazz)
                || CharSequence.class.isAssignableFrom(clazz)
                || Enum.class.isAssignableFrom(clazz)
                || Date.class.isAssignableFrom(clazz)
                || Calendar.class.isAssignableFrom(clazz);
    }

    /**
     * �ж�ָ�������Ƿ�ΪCollection������������������ӽӿڣ���
     *
     * @param clazz ��Ҫ�жϵ���
     * @return true����Collection false����Collection
     */
    public static boolean isCollection(Class<?> clazz)
    {
        if (clazz == null)
        {
            return false;
        }

        return Collection.class.isAssignableFrom(clazz);
    }

    /**
     * �ж�ָ�������Ƿ�ΪMap������������������ӽӿ�)��
     *
     * @param clazz ��Ҫ�жϵ���
     * @return true����Map false����Map
     */
    public static boolean isMap(Class<?> clazz)
    {
        if (clazz == null)
        {
            return false;
        }

        return Map.class.isAssignableFrom(clazz);
    }

    /**
     * �ж�ָ�������Ƿ�ΪJava�����ͱ�����顣
     *
     * @param clazz ��Ҫ�жϵ���
     * @return true����Java�����ͱ������ false����Java�����ͱ������
     */
    public static boolean isPrimitiveArray(Class<?> clazz)
    {
        if (clazz == null)
            return false;

        if (clazz == byte[].class)
            return true;
        else if (clazz == short[].class)
            return true;
        else if (clazz == int[].class)
            return true;
        else if (clazz == long[].class)
            return true;
        else if (clazz == char[].class)
            return true;
        else if (clazz == float[].class)
            return true;
        else if (clazz == double[].class)
            return true;
        else if (clazz == boolean[].class)
            return true;
        else
        { // element is an array of object references
            return false;
        }
    }

    /**
     * �ж�ָ�������Ƿ�ΪJava�����ͱ�����顣
     *
     * @param clazz ��Ҫ�жϵ���
     * @return true����Java�����ͱ������ false����Java�����ͱ������
     */
    public static boolean isPrimitiveWrapperArray(Class<?> clazz)
    {
        if (clazz == null)
            return false;

        if (clazz == Byte[].class)
            return true;
        else if (clazz == Short[].class)
            return true;
        else if (clazz == Integer[].class)
            return true;
        else if (clazz == Long[].class)
            return true;
        else if (clazz == Character[].class)
            return true;
        else if (clazz == Float[].class)
            return true;
        else if (clazz == Double[].class)
            return true;
        else if (clazz == Boolean[].class)
            return true;
        else
        { // element is an array of object references
            return false;
        }
    }

    /**
     * ��ȡ�������౾��������java.lang.Object�����г��ࡣ
     *
     * @param clazz Class
     * @return ��������
     */
    public static Class<?>[] getAllClass(Class<?> clazz)
    {
        List<Class<?>> clazzList = new ArrayList<Class<?>>();
        getAllSupperClass0(clazzList, clazz);
        return clazzList.toArray(new Class<?>[] {});
    }

    private static void getAllSupperClass0(List<Class<?>> clazzList,
            Class<?> clazz)
    {
        if (clazz.equals(Object.class))
        {
            return;
        }
        clazzList.add(clazz);
        getAllSupperClass0(clazzList, clazz.getSuperclass());
    }

    /**
     * ��ȡ��������ʵ�ֵĽӿ����顣
     *
     * @param clazz Class
     * @return ��������ʵ�ֵĽӿ�����
     */
    public static Class<?>[] getAllInterface(Class<?> clazz)
    {
        List<Class<?>> clazzList = new ArrayList<Class<?>>();
        Class<?>[] interfaces = clazz.getInterfaces();

        for (Class<?> interfaceClazz : interfaces)
        {
            clazzList.add(interfaceClazz);
            getAllSupperInterface0(clazzList, interfaceClazz);
        }

        return clazzList.toArray(new Class<?>[] {});
    }

    private static void getAllSupperInterface0(List<Class<?>> clazzList,
            Class<?> clazz)
    {
        if (clazz.equals(Object.class))
        {
            return;
        }

        Class<?>[] interfaces = clazz.getInterfaces();
        for (Class<?> interfaceClazz : interfaces)
        {
            clazzList.add(interfaceClazz);
            getAllSupperInterface0(clazzList, interfaceClazz);
        }
    }

    /**
     * ��ȡ�������౾���Լ����г��ࣨ����java.lang.Object����ʵ�ֵĽӿ��ж�������ԡ�
     *
     * @param clazz Class
     * @return ��������
     */
    public static Field[] getAllField(Class<?> clazz)
    {
        List<Field> fieldList = new ArrayList<Field>();
        Class<?>[] supperClazzs = ClassUtil.getAllClass(clazz);
        for (Class<?> aClazz : supperClazzs)
        {
            ObjectUtil.addAll(fieldList, aClazz.getDeclaredFields());
        }

        Class<?>[] supperInterfaces = ClassUtil.getAllInterface(clazz);
        for (Class<?> aInterface : supperInterfaces)
        {
            ObjectUtil.addAll(fieldList, aInterface.getDeclaredFields());
        }

        return fieldList.toArray(new Field[] {});
    }
    /**
     * ��ȡ�����������ֶ�
     * @param clazz
     * @return
     */
    public static Field[] getFields(Class<?> clazz)
    {
    	List<Field> fieldList = new ArrayList<Field>();
    	ObjectUtil.addAll(fieldList, clazz.getDeclaredFields());
    	return fieldList.toArray(new Field[] {});
    }
    /**
     * �Ӱ�page�л�ȡ����ע��Ϊclazz��Class
     * @param pack
     * @param clazz
     * @return
     */
    public static Set<Class<?>> getClassesByAnnotation(String pack,Class<? extends Annotation> clazz){
    	Set<Class<?>> aClasses = new LinkedHashSet<Class<?>>();
    	Set<Class<?>> classes = getClasses(pack);
		Iterator<Class<?>> iter = classes.iterator();
		while(iter.hasNext()){
			Class<?> c = iter.next();
			if(c.isAnnotationPresent(clazz))
				aClasses.add(c);
		}
		return aClasses;
    }
    /**
	 * �Ӱ�package�л�ȡ���е�Class
	 *
	 * @param pack
	 * @return
	 */
	public static Set<Class<?>> getClasses(String pack) {

		// ��һ��class��ļ���
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();
		// �Ƿ�ѭ������
		boolean recursive = true;
		// ��ȡ�������� �������滻
		String packageName = pack;
		String packageDirName = packageName.replace('.', '/');
		// ����һ��ö�ٵļ��� ������ѭ�����������Ŀ¼�µ�things
		Enumeration<URL> dirs;
		try {
			dirs = Thread.currentThread().getContextClassLoader().getResources(
					packageDirName);
			// ѭ��������ȥ
			while (dirs.hasMoreElements()) {
				// ��ȡ��һ��Ԫ��
				URL url = dirs.nextElement();
				// �õ�Э�������
				String protocol = url.getProtocol();
				// ��������ļ�����ʽ�����ڷ�������
				if ("file".equals(protocol)) {
					// ��ȡ��������·��
					String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
					// ���ļ��ķ�ʽɨ���������µ��ļ� ����ӵ�������
					findAndAddClassesInPackageByFile(packageName, filePath,
							recursive, classes);
				} else if ("jar".equals(protocol)) {
					// �����jar���ļ�
					// ����һ��JarFile
					JarFile jar;
					try {
						// ��ȡjar
						jar = ((JarURLConnection) url.openConnection())
								.getJarFile();
						// �Ӵ�jar�� �õ�һ��ö����
						Enumeration<JarEntry> entries = jar.entries();
						// ͬ���Ľ���ѭ������
						while (entries.hasMoreElements()) {
							// ��ȡjar���һ��ʵ�� ������Ŀ¼ ��һЩjar����������ļ� ��META-INF���ļ�
							JarEntry entry = entries.nextElement();
							String name = entry.getName();
							// �������/��ͷ��
							if (name.charAt(0) == '/') {
								// ��ȡ������ַ���
								name = name.substring(1);
							}
							// ���ǰ�벿�ֺͶ���İ�����ͬ
							if (name.startsWith(packageDirName)) {
								int idx = name.lastIndexOf('/');
								// �����"/"��β ��һ����
								if (idx != -1) {
									// ��ȡ���� ��"/"�滻��"."
									packageName = name.substring(0, idx)
											.replace('/', '.');
								}
								// ������Ե�����ȥ ������һ����
								if ((idx != -1) || recursive) {
									// �����һ��.class�ļ� ���Ҳ���Ŀ¼
									if (name.endsWith(".class")
											&& !entry.isDirectory()) {
										// ȥ�������".class" ��ȡ����������
										String className = name.substring(
												packageName.length() + 1, name
														.length() - 6);
										try {
											// ��ӵ�classes
											classes.add(Class
													.forName(packageName + '.'
															+ className));
										} catch (ClassNotFoundException e) {
											// log
											// .error("����û��Զ�����ͼ����� �Ҳ��������.class�ļ�");
											e.printStackTrace();
										}
									}
								}
							}
						}
					} catch (IOException e) {
						// log.error("��ɨ���û�������ͼʱ��jar����ȡ�ļ�����");
						e.printStackTrace();
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return classes;
	}
	/**
	 * ���ļ�����ʽ����ȡ���µ�����Class
	 *
	 * @param packageName
	 * @param packagePath
	 * @param recursive
	 * @param classes
	 */
	public static void findAndAddClassesInPackageByFile(String packageName,
			String packagePath, final boolean recursive, Set<Class<?>> classes) {
		// ��ȡ�˰���Ŀ¼ ����һ��File
		File dir = new File(packagePath);
		// ��������ڻ��� Ҳ����Ŀ¼��ֱ�ӷ���
		if (!dir.exists() || !dir.isDirectory()) {
			// log.warn("�û�������� " + packageName + " ��û���κ��ļ�");
			return;
		}
		// ������� �ͻ�ȡ���µ������ļ� ����Ŀ¼
		File[] dirfiles = dir.listFiles(new FileFilter() {
			// �Զ�����˹��� �������ѭ��(������Ŀ¼) ��������.class��β���ļ�(����õ�java���ļ�)
			public boolean accept(File file) {
				return (recursive && file.isDirectory())
						|| (file.getName().endsWith(".class"));
			}
		});
        if(dirfiles == null) {
            return;
        }
		// ѭ�������ļ�
		for (File file : dirfiles) {
			// �����Ŀ¼ �����ɨ��
			if (file.isDirectory()) {
				findAndAddClassesInPackageByFile(
						packageName + "." + file.getName(),
						file.getAbsolutePath(), recursive, classes);
			} else {
				// �����java���ļ� ȥ�������.class ֻ��������
				String className = file.getName().substring(0,
						file.getName().length() - 6);
				try {
					// ��ӵ�������ȥ
					// classes.add(Class.forName(packageName + '.' +
					// className));
					// �����ظ�ͬѧ�����ѣ�������forName��һЩ���ã��ᴥ��static������û��ʹ��classLoader��load�ɾ�
					classes.add(Thread.currentThread().getContextClassLoader()
							.loadClass(packageName + '.' + className));
				} catch (ClassNotFoundException e) {
					// log.error("����û��Զ�����ͼ����� �Ҳ��������.class�ļ�");
					e.printStackTrace();
				}
			}
		}
	}
}
