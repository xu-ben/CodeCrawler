import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 
 */

/**
 * @author Administrator
 *
 */
public final class Test {

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
