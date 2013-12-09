import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.lang.model.element.Element;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.ElementIterator;
import javax.swing.text.StyleConstants;
import javax.swing.text.html.HTML;
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
public final class Spider {

	/**
	 * 全局唯一实例
	 */
	private static Spider spider = null;

	private Spider() {
	}

	/**
	 * 获取编码名称字符串
	 * 
	 * @param conn
	 * @return 解析获得的编码名称字符串，若解析不到编码，则返回null
	 * @throws IOException
	 */
	private String getEncodingName(URL url) throws IOException {
		URLConnection conn = url.openConnection();
		/**
		 * 如果能从header中解析出charset，则返回
		 */
		String ret = conn.getContentEncoding();
		if (ret != null) {
			return ret;
		}
		ret = conn.getContentType();
		if (ret != null) {
			ret = ret.toLowerCase();
			int index = ret.indexOf("charset=");
			if (index != -1) {
				return ret.substring(index + 8);
			}
		}

		/**
		 * 如果不能从header中解析出charset，则解析网页meta从中获取charset
		 */
		InputStreamReader isr = new InputStreamReader(conn.getInputStream());
		BufferedReader br = new BufferedReader(isr);
		String regex = "<\\s*meta[^>]*content=\"[^\"]*charset\\s*=\\s*([a-zA-Z_0-9-]+)\\s*\"";
		Pattern pa = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Pattern pb = Pattern.compile(".*<\\s*body\\s*>.*",
				Pattern.CASE_INSENSITIVE);
		String line = null;
		while ((line = br.readLine()) != null) {
			Matcher m = pa.matcher(line);
			if (m.find()) {
				br.close();
				return m.group(1);
			} else {
				if (pb.matcher(line).matches()) {
					break;
				}
			}
		}

		/**
		 * 以上两种方法均解析不出，则返回null
		 */
		br.close();
		return null;
	}

	/**
	 * 给定一个url，返回其内容
	 * 
	 * @param urlstr
	 *            指定的url
	 * @return
	 * @throws IOException
	 */
	public String getContent(String urlstr) throws IOException {
		URL url = new URL(urlstr);
		String encoding = getEncodingName(url);
		URLConnection conn = url.openConnection();
		InputStreamReader isr = null;
		if (encoding != null) {
			isr = new InputStreamReader(conn.getInputStream(), encoding);
		} else {
			isr = new InputStreamReader(conn.getInputStream());
		}
		BufferedReader reader = new BufferedReader(isr);
		StringBuilder sb = new StringBuilder();
		char[] buf = new char[4096];
		int len;
		while ((len = reader.read(buf)) != -1) {
			// System.out.print(buf);
			sb.append(buf, 0, len);
		}
		reader.close();
		return sb.toString();
	}

	public String extractCode(URL url) throws IOException, BadLocationException {
		// String encoding = getEncodingName(url);

		String content = getContent(url.toString());

		// String encoding = "utf-8";
		// URLConnection conn = url.openConnection();
		// InputStreamReader isr = null;
		// isr = new InputStreamReader(conn.getInputStream(), encoding);
		// if (encoding != null) {
		// isr = new InputStreamReader(conn.getInputStream(), encoding);
		// } else {
		// isr = new InputStreamReader(conn.getInputStream());
		// }
		// BufferedReader br = new BufferedReader(isr);
		StringReader sr = new StringReader(content);
		BufferedReader br = new BufferedReader(sr);

		HTMLEditorKit htmlKit = new HTMLEditorKit();
		HTMLDocument htmlDoc = (HTMLDocument) htmlKit.createDefaultDocument();
		HTMLEditorKit.Parser parser = new ParserDelegator();
		HTMLEditorKit.ParserCallback callback = htmlDoc.getReader(0);
		parser.parse(br, callback, true);

		System.out.println(htmlDoc.getText(0, htmlDoc.getLength()));

		// ElementIterator ei = new ElementIterator(htmlDoc);
		// Element e = null;
		// ei.first();
		// while (ei.current() != null) {
		// AttributeSet as = ei.current().getAttributes();
		// Object name = as.getAttribute(StyleConstants.NameAttribute);
		// if (name instanceof HTML.Tag && name == HTML.Tag.PRE) {
		// int ss = ei.current().getStartOffset();
		// int ee = ei.current().getEndOffset();
		// String text = htmlDoc.getText(ss, ee - ss);
		// System.out.printf("s = %d, e = %d, %s\t", ss, ee, text);
		// // System.out.printf("s = %d, e = %d\t", ss, ee);
		// // int cnt = ei.current().getElementCount();
		// // System.out.println(name);
		// // System.out.printf("%s, child: %d\n", name.toString(), cnt);
		// }
		//
		// ei.next();
		// }

		// // HTMLDocument.Iterator it = htmlDoc.getIterator(HTML.Tag.A);
		// HTMLDocument.Iterator it = htmlDoc.getIterator(HTML.Tag.DIV);
		// if(it == null) {
		// System.out.println("benben");
		// return null;
		// }
		//
		//
		// // Parser
		// for (; it.isValid(); it.next()) {
		// // AttributeSet attributes = it.getAttributes();
		// // String srcString = (String) attributes
		// // .getAttribute(HTML.Attribute.HREF);
		// // System.out.println(srcString);
		// int s = it.getStartOffset();
		// int e = it.getEndOffset();
		// String text = htmlDoc.getText(s, e);
		// System.out.printf("s = %, e = %d, %s\n", text);
		// }

		return null;
	}

	public static void test() {
		String[] urls = { "http://www.cnblogs.com/moonbay",
				"http://jwc.bjfu.edu.cn", "http://www.baidu.com",
				"http://www.renren.com", "http://www.google.com.hk",
				"http://xiaoben.imzone.in/ben/" };
		try {
			Spider spider = new Spider();
			for (int i = 0, len = urls.length; i < len; i++) {
				URL url = new URL(urls[i]);
				System.out.printf("url:%s\t%s\n", urls[i],
						spider.getEncodingName(url));
			}
			// String content = spider.getContent(url);
			// String url = "http://www.cnblogs.com/moonbay";
			// System.out.println(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return 一个本类的实例
	 */
	public static synchronized Spider getInstance() {
		if (spider == null) {
			spider = new Spider();
		}
		return spider;
	}

	public static void main(String[] args) {
		test();
	}

}
