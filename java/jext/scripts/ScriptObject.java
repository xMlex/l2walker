package jext.scripts;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.logging.Logger;

public class ScriptObject {
	private static final Logger _log = Logger.getLogger(ScriptObject.class
			.getName());
	private Class<?> _class;
	private Object _instance;

	public ScriptObject(Class<?> c, Object o) {
		_class = c;
		_instance = o;
	}

	public void setProperty(String s, Object o) {
		if (_class == null || _instance == null)
			return;
		try {
			Field fld = _class.getField(s);
			if (fld == null)
				_log.warning("Class<?> " + getName() + " field " + s
						+ " not found!");
			else if (!Modifier.isPublic(fld.getModifiers()))
				_log.warning("Class<?> " + getName() + " field " + s
						+ " is not public!");
			else
				fld.set(_instance, o);
		} catch (NoSuchFieldException e) {
			_log.warning("Class<?> " + getName() + " field " + s
					+ " not found or private!");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Object getProperty(String s) {
		if (_class == null || _instance == null)
			return null;
		try {
			Field fld = _class.getField(s);
			if (fld == null)
				_log.warning("Class<?> " + getName() + " field " + s
						+ " not found!");
			else if (!Modifier.isPublic(fld.getModifiers()))
				_log.warning("Class<?> " + getName() + " field " + s
						+ " is not public!");
			else
				return fld.get(_instance);
		} catch (NoSuchFieldException e) {
			_log.warning("Class<?> " + getName() + " field " + s
					+ " not found or private!");
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object invokeMethod(Method m) {
		if (_class == null || _instance == null)
			return null;
		try {
			return m.invoke(Modifier.isStatic(m.getModifiers()) ? null
					: _instance);
		} catch (InvocationTargetException f) {
			_log.warning("Class<?> " + getName() + " method " + m
					+ " return a error!");
			f.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object invokeMethod(Method m, Object[] args) {
		if (_class == null || _instance == null)
			return null;
		try {
			return m.invoke(Modifier.isStatic(m.getModifiers()) ? null
					: _instance, args);
		} catch (InvocationTargetException f) {
			_log.warning("Class<?> " + getName() + " method " + m
					+ " return a error!");
			f.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object invokeMethod(String s) {
		if (_class == null || _instance == null)
			return null;
		try {
			Method m = _class.getMethod(s);
			if (m == null)
				_log.warning("Class<?> " + getName() + " method " + s
						+ " not found!");
			else
				return m.invoke(Modifier.isStatic(m.getModifiers()) ? null
						: _instance);
		} catch (NoSuchMethodException e) {
		} catch (InvocationTargetException f) {
			_log.warning("Class<?> " + getName() + " method " + s
					+ " return a error!");
			f.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public Object invokeMethod(String s, Object[] args) {
		if (_class == null || _instance == null)
			return null;
		Object o = null;
		Class<?>[] types = new Class<?>[args.length];
		try {
			boolean onlynull = true;
			for (int i = 0; i < args.length; i++)
				if (args[i] != null) {
					types[i] = args[i].getClass();
					onlynull = false;
				}

			Method meth = null;
			if (!onlynull && getMethod(s, types) == null) {
				boolean[] accept = new boolean[args.length];
				for (Method m : _class.getMethods()) {
					if (m.getName().equals(s)
							&& m.getParameterTypes().length == args.length)
						for (int i = 0; i < m.getParameterTypes().length; i++) {
							Class<?> p = m.getParameterTypes()[i];
							if (args[i] != null && args[i].getClass() == p)
								accept[i] = true;
							else if (args[i] != null) {
								Class<?> argc = args[i].getClass();
								while (true)
									if (argc != null
											&& argc.getSuperclass() == p) {
										accept[i] = true;
										break;
									} else if (argc == null)
										break;
									else
										argc = argc.getSuperclass();
							}
							if (!accept[i])
								accept[i] = args[i] == null;
						}

					boolean result = true;
					for (boolean a : accept)
						if (!a) {
							result = false;
							break;
						}

					if (result) {
						meth = m;
						break;
					}
				}
			} else {
				for (int i = 0; i < args.length; i++)
					types[i] = args[i] != null ? args[i].getClass()
							: Object.class;
				meth = getMethod(s, types);
			}

			if (meth == null && onlynull)
				for (Method m : _class.getMethods())
					if (m.getName().equals(s)
							&& m.getParameterTypes().length == args.length) {
						meth = m;
						break;
					}
			if (meth == null && onlynull)
				return invokeMethod(s);

			if (meth == null) {
				_log.warning("Class<?> " + getName() + " method " + s
						+ " not found!");
				return null;
			}

			if (Modifier.isStatic(meth.getModifiers()))
				o = meth.invoke(null, args);
			else
				o = meth.invoke(_instance, args);
		} catch (InvocationTargetException f) {
			_log.warning("Class<?> " + getName() + " method " + s
					+ " return a error!");
			f.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return o;
	}

	@SuppressWarnings("unchecked")
	private Method getMethod(String s, Class[] types) {
		try {
			return _class.getMethod(s, types);
		} catch (NoSuchMethodException e) {
			return null;
		}
	}

	public String getName() {
		return _class.getName();
	}

	public Class<?> getRawClass() {
		return _class;
	}

	public boolean isFunctions() {
		return false;// Functions.class.isInstance(_instance);
	}
}