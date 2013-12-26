package fw.common;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class TaskCreator
{
	public static Runnable createTask(final Object obj, final Method method, final Object... args)
	{
		return new Runnable(){
			public void run()
			{
				try
				{
					method.invoke(obj, args);
				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
			}
		};
	}

	public static Runnable createTask(Object obj, Class<?> clazz, String methodName, Object... args) throws SecurityException, NoSuchMethodException
	{
		Class<?>[] arg_classes = new Class[args.length];
		for(int i = 0; i < args.length; i++)
			arg_classes[i] = args[i].getClass();
		Method method = getMethod(clazz, methodName, arg_classes);
		return createTask(obj, method, args);
	}

	public static Runnable createTask(Object obj, String className, String methodName, Object... args) throws SecurityException, NoSuchMethodException, ClassNotFoundException
	{
		return createTask(obj, Class.forName(className), methodName, args);
	}

	public static Runnable createTask(Method method, Object... args)
	{
		return createTask(null, method, args);
	}

	public static Runnable createTask(Class<?> clazz, String methodName, Object... args) throws SecurityException, NoSuchMethodException
	{
		return createTask(null, clazz, methodName, args);
	}

	public static Runnable createTask(String className, String methodName, Object... args) throws SecurityException, NoSuchMethodException, ClassNotFoundException
	{
		return createTask(null, className, methodName, args);
	}

	public static String argumentTypesToString(Class<?>[] argTypes)
	{
		StringBuilder buf = new StringBuilder();
		buf.append("(");
		if(argTypes != null)
		{
			for(int i = 0; i < argTypes.length; i++)
			{
				if(i > 0)
					buf.append(", ");
				Class<?> c = argTypes[i];
				buf.append((c == null) ? "null" : c.getName());
			}
		}
		buf.append(")");
		return buf.toString();
	}

	private static Class<?> getPrimitiveClass(Class<?> clazz)
	{
		try
		{
			Field f = clazz.getField("TYPE");
			if(f.getType() == Class.class)
				return (Class<?>) f.get(null);
		}
		catch(Exception e)
		{}
		return null;
	}

	private static boolean paramEq(Class<?> a1, Class<?> a2)
	{
		if(a1 == a2)
			return true;
		Object _a1 = getPrimitiveClass(a1);
		if(_a1 != null && _a1 == a2)
			return true;
		Object _a2 = getPrimitiveClass(a2);
		if(_a2 != null && (a1 == _a2 || _a1 == _a2))
			return true;
		return false;
	}

	private static boolean paramsEq(Class<?>[] a1, Class<?>[] a2)
	{
		if(a1 == null)
			return a2 == null || a2.length == 0;
		if(a2 == null)
			return a1.length == 0;
		if(a1.length != a2.length)
			return false;
		for(int i = 0; i < a1.length; i++)
			if(!paramEq(a1[i], a2[i]))
				return false;
		return true;
	}

	public static Method getMethod(Class<?> clazz, String name, Class<?>... paramTypes) throws NoSuchMethodException, SecurityException
	{
		String internedName = name.intern();
		for(Method method : clazz.getMethods())
		{
			if(method.getName() == internedName && paramsEq(paramTypes, method.getParameterTypes()))
				return method;
		}

		Method res = null;
		// Search superclass's methods
		if(!clazz.isInterface())
		{
			Class<?> c = clazz.getSuperclass();
			if(c != null && (res = getMethod(c, name, paramTypes)) != null)
				return res;
		}
		// Search superinterfaces' methods
		Class<?>[] interfaces = clazz.getInterfaces();
		for(Class<?> c : interfaces)
			if((res = getMethod(c, name, paramTypes)) != null)
				return res;

		throw new NoSuchMethodException(clazz.getName() + "." + name + argumentTypesToString(paramTypes));
	}
}