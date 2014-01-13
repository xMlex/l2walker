package fw.test;

import fw.common.ThreadPoolManager;
import xmlex.config.ConfigSystem;

public class testSnippetGL {

	
	public static void main(String [] args) {
		ConfigSystem.load();
		
		Snippet195 _f = new Snippet195();
		
		ThreadPoolManager.getInstance().executeGeneral(_f);
	}
	
}
