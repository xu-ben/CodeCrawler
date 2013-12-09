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

	private boolean preflag = false;

	@Override
	public void handleStartTag(Tag t, MutableAttributeSet a, int pos) {
		// TODO Auto-generated method stub
		if (t == HTML.Tag.PRE) {
			preflag = true;
		}
		super.handleStartTag(t, a, pos);
	}

	@Override
	public void handleEndTag(Tag t, int pos) {
		// TODO Auto-generated method stub
		if (t == HTML.Tag.PRE) {
			preflag = false;
		}
		super.handleEndTag(t, pos);
	}

	@Override
	public void handleText(char[] data, int pos) {
		// TODO Auto-generated method stub
		// super.handleText(data, pos);
		if (preflag)
			System.out.print(data);
	}

}
