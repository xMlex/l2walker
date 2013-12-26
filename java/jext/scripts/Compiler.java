package jext.scripts;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.HashMap;

import javax.tools.Diagnostic;
import javax.tools.DiagnosticCollector;
import javax.tools.FileObject;
import javax.tools.ForwardingJavaFileManager;
import javax.tools.JavaCompiler;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import javax.tools.StandardJavaFileManager;
import javax.tools.ToolProvider;

import jext.util.GArray;

public class Compiler {
	private static Compiler _instance;

	public static Compiler getInstance() {
		if (_instance == null)
			_instance = new Compiler();
		return _instance;
	}

	private final JavaCompiler javac = ToolProvider.getSystemJavaCompiler();
	private final DiagnosticCollector<JavaFileObject> diacol = new DiagnosticCollector<JavaFileObject>();
	private final StandardJavaFileManager standartFileManager = javac
			.getStandardFileManager(diacol, null, null);

	public MemoryClassLoader classLoader = null;

	public boolean compile(File[] files, PrintStream err) {
		classLoader = new MemoryClassLoader();
		JavaMemoryFileManager fileManager = new JavaMemoryFileManager(
				standartFileManager, classLoader);

		if (javac.getTask(null, fileManager, diacol, null, null,
				standartFileManager.getJavaFileObjects(files)).call())
			return true;

		if (err != null)
			for (Diagnostic<? extends JavaFileObject> dia : diacol
					.getDiagnostics())
				err.println(dia);
		return false;
	}

	public boolean compile(GArray<File> files, PrintStream err) {
		return compile(files.toArray(new File[files.size()]), err);
	}

	public boolean compile(File file, PrintStream err) {
		return compile(new File[] { file }, err);
	}

	/******************************************/

	public static class MemoryClassLoader extends ClassLoader {
		public final HashMap<String, MemoryJavaByteCode> byteCodes = new HashMap<String, MemoryJavaByteCode>();

		@Override
		protected Class<?> findClass(String name) throws ClassNotFoundException {
			MemoryJavaByteCode byteCode = byteCodes.get(name);
			if (byteCode == null)
				throw new ClassNotFoundException(name);
			byte[] bytecode = byteCode.getBytes();
			return defineClass(name, bytecode, 0, bytecode.length);
		}

		public void addClass(MemoryJavaByteCode code) {
			byteCodes.put(code.getName(), code);
		}
	}

	/******************************************/

	public static class JavaMemoryFileManager extends
			ForwardingJavaFileManager<StandardJavaFileManager> {
		private final MemoryClassLoader classLoader;

		public JavaMemoryFileManager(StandardJavaFileManager fileManager,
				MemoryClassLoader specClassLoader) {
			super(fileManager);
			classLoader = specClassLoader;
		}

		@Override
		@SuppressWarnings("unused")
		public JavaFileObject getJavaFileForOutput(Location location,
				String name, JavaFileObject.Kind kind, FileObject sibling)
				throws IOException {
			MemoryJavaByteCode byteCode = new MemoryJavaByteCode(name);
			classLoader.addClass(byteCode);
			return byteCode;
		}
	}

	/******************************************/

	public static class MemoryJavaByteCode extends SimpleJavaFileObject {
		private ByteArrayOutputStream oStream;
		private final String className;

		public MemoryJavaByteCode(String name) {
			super(URI.create("byte:///" + name), Kind.CLASS);
			className = name;
		}

		@Override
		public OutputStream openOutputStream() {
			oStream = new ByteArrayOutputStream();
			return oStream;
		}

		public byte[] getBytes() {
			return oStream.toByteArray();
		}

		@Override
		public String getName() {
			return className;
		}
	}
}