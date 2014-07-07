/*
 * 创建日期2013-12-6
 * 最后修改日期2013-12-9
 */

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 */

/**
 * @author Administrator
 * 
 */
public final class MyURLConnector {

	/**
	 * 全局唯一实例
	 */
	private static MyURLConnector conn = null;

	/**
	 * 微信浏览器的User-Agent
	 */
	private static String microAgentstr = "Mozilla/5.0 (Linux; U; Android 4.1.1; zh-cn; MI2 Build/JRO03L) AppleWebKit/534.30 (KHTML, like Gecko) version/4.0 Mobile Safari/534.30 MicroMessenger/5.0.3.1.355";
	
//	private static String microAgentstr = "Mozilla/5.0 (iPhone; CPU iPhone OS 6_1_3 like Mac OS X) AppleWebKit/536.26 (KHTML, like Gecko) Mobile/10B329 MicroMessenger/5.0.1";

	private MyURLConnector() {
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
		URLConnection uc = url.openConnection();
		return getContent(uc, encoding);
	}
	
	/**
	 * 给定一个url连接，返回其内容
	 * @param uc
	 * @param encoding
	 * @return
	 * @throws IOException
	 */
	public String getContent(URLConnection uc, String encoding) throws IOException {
		uc.connect();
		InputStreamReader isr = null;
		if (encoding != null) {
			isr = new InputStreamReader(uc.getInputStream(), encoding);
		} else {
			isr = new InputStreamReader(uc.getInputStream());
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

	private static void test() {
		String[] urls = { "http://www.cnblogs.com/moonbay",
				"http://jwc.bjfu.edu.cn", "http://www.baidu.com",
				"http://www.renren.com", "http://www.google.com.hk",
				"http://xiaoben.imzone.in/ben/" };
		try {
			MyURLConnector spider = new MyURLConnector();
			for (int i = 0, len = urls.length; i < len; i++) {
				URL url = new URL(urls[i]);
				System.out.printf("url:%s\t%s\n", urls[i],
						spider.getEncodingName(url));
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @return 一个本类的实例
	 */
	public static synchronized MyURLConnector getInstance() {
		if (conn == null) {
			conn = new MyURLConnector();
		}
		return conn;
	}

	public static void test2() throws Exception {
		MyURLConnector muc = new MyURLConnector();
//		String urlstr = "http://www.baidu.com";
		String urlstr = "http://hd.weixin.kongzhong.com/index.php?g=Wap&m=Guajiang&a=index&token=xsubpm1404353542&wecha_id=ocXrWjm2dEKJ1qPkhq2L9wZ1KQ20&id=58";
//		String urlstr = "http://acm.bjfu.edu.cn/ben/test.jsp";
		URL url = new URL(urlstr);
		HttpURLConnection con = (HttpURLConnection) url.openConnection();
		con.setRequestProperty("User-Agent", microAgentstr);
		con.connect();
		System.out.println(muc.getContent(con, "utf-8"));
	}

	public static void main(String[] args) {
		//test();
		
		try {
			test2();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
