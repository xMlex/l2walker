package jext.scripts;

public class Script {
	private Class<?> _class;

	public Script(Class<?> c) {
		_class = c;
	}

	public ScriptObject newInstance() {
		ScriptObject o = null;
		Object instance = null;
		try {
			instance = _class.newInstance();
		} catch (InstantiationException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
		o = new ScriptObject(_class, instance);

		return o;
	}

	public ScriptObject newInstance(Object[] args) {
		ScriptObject o = null;
		Object instance = null;
		try {
			@SuppressWarnings("rawtypes")
			Class[] types = new Class[args.length];
			boolean arg = false;
			for (int i = 0; i < args.length; i++)
				if (args[i] != null) {
					types[i] = args[i].getClass();
					arg = true;
				}
			if (!arg)
				return newInstance();
			instance = _class.getConstructor(types).newInstance(args);
		} catch (InstantiationException e) {
		} catch (Exception e) {
			e.printStackTrace();
		}
		o = new ScriptObject(_class, instance);

		return o;
	}

	public Class<?> getRawClass() {
		return _class;
	}

	public String getName() {
		return _class.getName();
	}

	public boolean isFunctions() {
		return false;// Functions.class.isAssignableFrom(_class);
	}
}