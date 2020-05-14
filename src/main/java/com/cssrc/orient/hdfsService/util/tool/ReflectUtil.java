package com.cssrc.orient.hdfsService.util.tool;

import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ReflectUtil {
	public static <M> Object getFieldValue(M m, String fieldName) {
		Object value = null;
		try {
			Field field = m.getClass().getDeclaredField(fieldName);
			if (field != null) {
				field.setAccessible(true);
				value = field.get(m);
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
		return value;
	}
	/**
	 * �ֶ��������ִ�Сд
	 * ���ܱ�getFieldValue��
	 * @param m
	 * @param fieldName
	 * @return
	 */
	public static <M> Object getFieldValueNoCaseSensitive(M m, String fieldName) {
		Object value = null;
		try {
			Field[] fields = m.getClass().getDeclaredFields();
			Field f = null;
			for(Field f1 : fields){
				if(f1.getName().toLowerCase().equals(fieldName.toLowerCase())){
					f = f1;
					break;
				}
			}
			if(f!=null)
				value = getFieldValue(m, f.getName());
		} catch (SecurityException e) {
			e.printStackTrace();
		}
		return value;
	}
	public static <M> void setFieldValue(M m, String fieldName,
			Object fieldValue) {
		try {
			Field[] fields = m.getClass().getDeclaredFields();
			if (fields != null) {
				for (Field f : fields) {
					f.setAccessible(true);
					if (f.getName().toLowerCase()
							.equals(fieldName.trim().toLowerCase())) {
						f.set(m, fieldValue);
					}
				}
			}
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}

	public static Class<?> forName(String className) {
		try {
			return Class.forName(className);
		} catch (ClassNotFoundException e) {
			return null;
		}
	}

	/**
	 * ��Hibernate�־û�����������Ϊ�ӳټ��ص������Ƴ����� ��ԭΪ��ʵ����
	 *
	 * @param m
	 * @param ���Ƴ����ӳټ�������
	 */
	public static <M> M removeLazyProperty(M m, String... noIncludePropertys) {
		if (m instanceof List<?>) {// �����List����
			List<?> list = (List<?>) m;
			for (Object obj : list) {
				if(ObjectUtil.isNull(obj))
					continue;
				Class<?> clazz = obj.getClass();
				Field[] fields = clazz.getDeclaredFields();
				for (Field f : fields) {
					f.setAccessible(false);
					if (!StringUtil.hasStr(noIncludePropertys, f.getName())) {
						Annotation a = f.getAnnotation(ManyToOne.class);
						if (a == null)
							a = f.getAnnotation(OneToMany.class);
						if (a == null)
							a = f.getAnnotation(ManyToMany.class);
						if (a != null) {
							try {
								Method method = a.getClass().getDeclaredMethod(
										"fetch");
								FetchType fetchType = (FetchType) method
										.invoke(a);
								if (fetchType == FetchType.LAZY) {
									String fType = f.getGenericType()
											.toString();
									if (fType.startsWith("class")) {
										fType = fType.replace("class ", "");
										setFieldValue(obj, f.getName(), Class
												.forName(fType).newInstance());
									} else {
										if (fType.indexOf("java.util.List") >= 0
												|| fType.indexOf("java.util.ArrayList") >= 0)
											setFieldValue(obj, f.getName(),
													ArrayList.class
															.newInstance());
										else if (fType.indexOf("java.util.Set") >= 0
												|| fType.indexOf("java.util.HashSet") >= 0)
											setFieldValue(obj, f.getName(),
													HashSet.class.newInstance());
									}
								}
							} catch (SecurityException e) {
								e.printStackTrace();
							} catch (NoSuchMethodException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							} catch (InstantiationException e) {
								e.printStackTrace();
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		} else if (m instanceof Set<?>) {// �����Set����
			Set<?> list = (Set<?>) m;
			for (Object obj : list) {
				Class<?> clazz = obj.getClass();
				Field[] fields = clazz.getDeclaredFields();
				for (Field f : fields) {
					f.setAccessible(false);
					if (!StringUtil.hasStr(noIncludePropertys, f.getName())) {
						Annotation a = f.getAnnotation(ManyToOne.class);
						if (a == null)
							a = f.getAnnotation(OneToMany.class);
						if (a == null)
							a = f.getAnnotation(ManyToMany.class);
						if (a != null) {
							try {
								Method method = a.getClass().getDeclaredMethod(
										"fetch");
								FetchType fetchType = (FetchType) method
										.invoke(a);
								if (fetchType == FetchType.LAZY) {
									String fType = f.getGenericType()
											.toString();
									if (fType.startsWith("class")) {
										fType = fType.replace("class ", "");
										setFieldValue(obj, f.getName(), Class
												.forName(fType).newInstance());
									} else {
										if (fType.indexOf("java.util.List") >= 0
												|| fType.indexOf("java.util.ArrayList") >= 0)
											setFieldValue(obj, f.getName(),
													ArrayList.class
															.newInstance());
										else if (fType.indexOf("java.util.Set") >= 0
												|| fType.indexOf("java.util.HashSet") >= 0)
											setFieldValue(obj, f.getName(),
													HashSet.class.newInstance());
									}
								}
							} catch (SecurityException e) {
								e.printStackTrace();
							} catch (NoSuchMethodException e) {
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								e.printStackTrace();
							} catch (IllegalAccessException e) {
								e.printStackTrace();
							} catch (InvocationTargetException e) {
								e.printStackTrace();
							} catch (InstantiationException e) {
								e.printStackTrace();
							} catch (ClassNotFoundException e) {
								e.printStackTrace();
							}
						}
					}
				}
			}
		} else {
			if(ObjectUtil.isNull(m))
				return null;
			Class<?> clazz = m.getClass();
			Field[] fields = clazz.getDeclaredFields();
			for (Field f : fields) {
				f.setAccessible(false);
				if (!StringUtil.hasStr(noIncludePropertys, f.getName())) {

					Annotation a = f.getAnnotation(ManyToOne.class);
					if (a == null)
						a = f.getAnnotation(OneToMany.class);
					if (a == null)
						a = f.getAnnotation(ManyToMany.class);
					if (a != null) {
						try {
							Method method = a.getClass().getDeclaredMethod(
									"fetch");
							FetchType fetchType = (FetchType) method.invoke(a);
							if (fetchType == FetchType.LAZY) {
								String fType = f.getGenericType().toString();
								if (fType.startsWith("class")) {
									fType = fType.replace("class ", "");
									setFieldValue(m, f.getName(), Class
											.forName(fType).newInstance());
								} else {
									if (fType.indexOf("java.util.List") >= 0
											|| fType.indexOf("java.util.ArrayList") >= 0)
										setFieldValue(m, f.getName(),
												ArrayList.class.newInstance());
									else if (fType.indexOf("java.util.Set") >= 0
											|| fType.indexOf("java.util.HashSet") >= 0)
										setFieldValue(m, f.getName(),
												HashSet.class.newInstance());
								}
							}
						} catch (SecurityException e) {
							e.printStackTrace();
						} catch (NoSuchMethodException e) {
							e.printStackTrace();
						} catch (IllegalArgumentException e) {
							e.printStackTrace();
						} catch (IllegalAccessException e) {
							e.printStackTrace();
						} catch (InvocationTargetException e) {
							e.printStackTrace();
						} catch (InstantiationException e) {
							e.printStackTrace();
						} catch (ClassNotFoundException e) {
							e.printStackTrace();
						}
					}
				}
			}
		}
		return m;
	} 
}
