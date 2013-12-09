import java.io.BufferedReader;
import java.io.IOException;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.text.html.HTMLEditorKit;
import javax.swing.text.html.parser.ParserDelegator;

/**
 * 
 */

/**
 * @author Administrator
 * 
 */
public final class MySpider {

	private MyURLConnector conn = MyURLConnector.getInstance();

	/**
	 * 从指定的url中抓取代码
	 * 
	 * @param url
	 *            指定的url，目前只支持博客园的网站
	 * @return
	 */
	public String[] getCodesFromURL(String url) {
		try {
			String content = conn.getContent(url);

			StringReader sr = new StringReader(content);
			BufferedReader br = new BufferedReader(sr);

			HTMLEditorKit.Parser parser = new ParserDelegator();
			MyHTMLParser callback = new MyHTMLParser(content);
			parser.parse(br, callback, true);

			String[] rets = callback.getResults();
			return rets;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}

	}

	/**
	 * 根据问题编号去百度上搜索得到链接地址数组
	 * 
	 * @param pid
	 * @return
	 */
	private String[] getURLsByProblemIDFromBaidu(String pid) {
		ArrayList<String> list = new ArrayList<String>();
		String url = String.format(
				"http://www.baidu.com/s?rn=100&wd=%s+site%%3Awww.cnblogs.com", pid);
		try {
			String content = conn.getContent(url);
			String regex = "<h3 class=\"t\"><a[^\"]*\"[^\"]+\"\\s*href=\"([^\"]+)\"";
			Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
			Matcher m = p.matcher(content);
			while (m.find()) {
				list.add(m.group(1));// 先得到加密形式的url，存下
			}
			int len = list.size();
			HttpURLConnection huc = null;
			if (len > 0) {
				String[] rets = new String[len];
				for (int i = 0; i < len; i++) {
					rets[i] = list.get(i);
					URL u = new URL(rets[i]);
					huc = (HttpURLConnection) u.openConnection();
					huc.getResponseCode();
					rets[i] = huc.getURL().toString();
					huc.disconnect();
				}
				return rets;
			}
		} catch (IOException ioe) {
			ioe.printStackTrace();
		}
		return null;
	}

	@SuppressWarnings("unused")
	private String[] getURLsByProblemIDFromGoogle(String pid) {
		// String url =
		// String.format("http://ipv6.google.com.hk/search?q=%s+site%%3Awww.cnblogs.com",
		// pid);
		return null;
	}

	public String[] getURLsByProblemID(String pid) {
		String[] rets = getURLsByProblemIDFromBaidu(pid);
		if (rets == null) {
			return null;
		}
		return rets;
	}

	@SuppressWarnings("unused")
	private static void test() {
		// String url =
		// "http://www.cnblogs.com/SunboyL/archive/2013/04/19/QuoitDesign.html";
		// String url = "http://xiaoben.imzone.in/ben/";
		// String url = "http://jwc.bjfu.edu.cn";
		// String url = "http://www.baidu.com";
		// String url = "http://www.cnblogs.com/rainydays/p/3434733.html";
		// String url = "http://www.cnblogs.com/moonbay/p/3411391.html";
		// String url = "http://www.cnblogs.com/GO-NO-1/p/3443630.html";
		String url = "http://www.baidu.com/link?url=BvSKBTB1_vd7QN-PokL74En9u6Eq9ss4FpQhxa7TJJ8QxDbcDhkTm5W7vlMvgSQ3u4ZdjjtBbDHR__A27XK7uiwWeTx9iYELyurxVIp69Qu";
		// String url =
		// "http://www.cnblogs.com/Kenfly/archive/2011/09/14/2176750.html";
		// String url =
		// "http://www.cnblogs.com/moonbay/archive/2011/08/29/2158841.html";
		MySpider w = new MySpider();
		String[] rets = w.getCodesFromURL(url);
		for (int i = 0; i < rets.length; i++) {
			System.out.println(rets[i]);
			System.out
					.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		}
	}
	
	private static void test2(String pid) {
		MySpider spider = new MySpider();
		String[] urls = spider.getURLsByProblemID(pid);
		if(urls == null) {
			return ;
		}
		int len = urls.length;
		String regex = "www.cnblogs.com/([^/.]+)";
		Pattern p = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
		for(int i = 0; i < len; i++) {
			Matcher m = p.matcher(urls[i]);
			if(!m.find()) {
				continue;
			}
			String[] codes = spider.getCodesFromURL(urls[i]);
			if(codes == null) {
				continue;
			}
			System.out
			.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
			String name = m.group(1);
			System.out.printf("用户名:%s\n", name);
			int leng = codes.length;
			for(int j = 0; j < leng; j++) {
				System.out.println(codes[j]);
			}
			System.out
			.println("@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// test();
		
//		test2("hdu1007");
		test2("poj1007");

	}

}
