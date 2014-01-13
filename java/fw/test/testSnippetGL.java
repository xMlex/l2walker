package fw.test;

import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.FillLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;

import xmlex.config.ConfigSystem;

public class testSnippetGL {

	
	public static void main(String [] args) {
		ConfigSystem.load();	
		
		//ThreadPoolManager.getInstance().executeGeneral(_f);
		
		final Display display = new Display();
		Shell shell = new Shell(display);
		shell.setLayout(new FillLayout());
		
		Composite comp = new Composite(shell, SWT.NONE);
		Composite comp2 = new Composite(shell, SWT.NONE);
		Composite comp3 = new Composite(shell, SWT.NONE);
		
		GLRender _m1 = new GLRender(comp,null);
		GLRender _m2 = new GLRender(comp2,null);
		GLRender _m3 = new GLRender(comp3,null);
		
		shell.setText("SWT/LWJGL Example");
		shell.setSize(640, 480);
		shell.open();
		
		display.asyncExec(_m1);
		display.asyncExec(_m2);
		display.asyncExec(_m3);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
	
}
