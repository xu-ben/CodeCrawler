import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
public final class Spider {

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

	public static void test() {
		String[] urls = { "http://www.cnblogs.com/moonbay",
				"http://jwc.bjfu.edu.cn", "http://www.baidu.com",
				"http://www.renren.com", "https://www.google.com.hk", "http://xiaoben.imzone.in/ben/" };
		try {
			Spider spider = new Spider();
			for (int i = 0, len = urls.length; i < len; i++) {
				URL url = new URL(urls[i]);
				System.out.printf("url:%s\t%s\n", urls[i], spider.getEncodingName(url));
			}
			// String content = spider.getContent(url);
			// String url = "http://www.cnblogs.com/moonbay";
			// System.out.println(content);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		test();
		String url = "http://jwc.bjfu.edu.cn";
		Spider s = new Spider();
		try {
			System.out.println(s.getContent(url));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
