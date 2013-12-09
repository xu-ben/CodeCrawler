import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.MutableAttributeSet;
import javax.swing.text.html.HTML;
import javax.swing.text.html.HTML.Tag;
import javax.swing.text.html.HTMLEditorKit.ParserCallback;

/**
 * 
 */

/**
 * @author Administrator
 * 
 */
public class MyHTMLParser extends ParserCallback {

	public MyHTMLParser(String content) {
		this.content = content;
	}

	private String content = null;

	// private boolean preflag = false;

	private int sIndex = 0;

	/**
	 * 临时存储解析到的代码字符串序列
	 */
	private ArrayList<String> codeList = new ArrayList<String>();

	@Override
	public void handleStartTag(Tag t, MutableAttributeSet a, int pos) {
		if (t == HTML.Tag.PRE) {
			// preflag = true;
			sIndex = pos + 5;// 去掉“<pos>"这五个字符
		}
		super.handleStartTag(t, a, pos);
	}

	@Override
	public void handleEndTag(Tag t, int pos) {
		if (t == HTML.Tag.PRE) {
			// preflag = false;
			String text = this.content.substring(sIndex, pos);
			text = this.handlePreHTML(text);
			codeList.add(text);
		}

		super.handleEndTag(t, pos);
	}

	@Override
	public void handleText(char[] data, int pos) {
		// super.handleText(data, pos);
		// if (preflag) {
		// int len = data.length;
		// System.out.print(new String(data));
		// if()
		// }
		// System.out.printf("data.length = %d, pos = %d, %s\n", data.length,
		// pos, new String(data));
		// System.out.print(data);
	}

	public String handlePreHTML(String html) {
		html = html.replaceAll("<\\s*br\\s*/>", "\n");//<br/>标签都换成换行符
		
		String regex = "([^<>]*)<[^<>]+>([^<>]*)</[^<>]+>";
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		Matcher m = p.matcher(html);
		StringBuilder sb = new StringBuilder();
		while (m.find()) {
			sb.append(formatCodeString(m.group(1)));
			sb.append(formatCodeString(m.group(2)));
		}
		return sb.toString();
	}

	private final String formatCodeString(String str) {
		// str = str.replaceAll("^[ ]*\\d+", "");
		str = str.replaceAll("&lt;", "<");
		str = str.replaceAll("&gt;", ">");
		str = str.replaceAll("&amp;", "&");
		return str;
	}

	/**
	 * @return 一组解析得到的疑似程序代码的字符串
	 */
	public String[] getResults() {
		int len = codeList.size();
		String[] rets = new String[len];
		for (int i = 0; i < len; i++) {
			rets[i] = codeList.get(i);
		}
		return rets;
	}

}
