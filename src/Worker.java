import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.URL;

import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

/**
 * 
 */

/**
 * @author Administrator
 *
 */
public final class Worker {

	
	public void work(String url) {
		Spider s = Spider.getInstance();
		try {
			String content = s.getContent(url);

			long t = System.currentTimeMillis();

			StringReader sr = new StringReader(content);
			BufferedReader br = new BufferedReader(sr);

			HTMLEditorKit htmlKit = new HTMLEditorKit();
//			HTMLDocument htmlDoc = (HTMLDocument) htmlKit.createDefaultDocument();
			HTMLEditorKit.Parser parser = new ParserDelegator();
//			HTMLEditorKit.ParserCallback callback = htmlDoc.getReader(0);
			MyHTMLParser callback = new MyHTMLParser();
			parser.parse(br, callback, true);

			t = System.currentTimeMillis() - t;

//			System.out.println(htmlDoc.getText(0, htmlDoc.getLength()));

//			System.out.println(t);
			// System.out.println(s.extractCode(new URL(url)));
			// System.out.println(s.getContent(url));
		} catch (IOException e) {
			e.printStackTrace();
//		} catch (BadLocationException e) {
//			e.printStackTrace();
		}
		
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String url = "http://www.cnblogs.com/SunboyL/archive/2013/04/19/QuoitDesign.html";
//		 String url = "http://xiaoben.imzone.in/ben/";
//		String url = "http://jwc.bjfu.edu.cn";
//		String url = "http://www.baidu.com";
		Worker w = new Worker();
		w.work(url);
	}

}
