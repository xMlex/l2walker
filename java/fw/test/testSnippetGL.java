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
		
		Main2DElement _m1 = new Main2DElement(comp);
		Main2DElement _m2 = new Main2DElement(comp2);
		
		shell.setText("SWT/LWJGL Example");
		shell.setSize(640, 480);
		shell.open();
		
		display.asyncExec(_m1);
		display.asyncExec(_m2);
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch())
				display.sleep();
		}
		display.dispose();
	}
	
}
