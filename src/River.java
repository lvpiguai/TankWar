import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

/**
 * 河流类
 */
public class River extends GameObject {
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] riverImags = null;
	static { // 存储图片
		riverImags = new Image[] { tk.getImage(GameFrame.class.getResource("Images/river.jpg")), };
	}

	public River(int x, int y) { // River的构造方法
		super(x, y);
		width = 40;
		length = 100;
	}

	public void draw(Graphics g) {
		g.drawImage(riverImags[0], x, y, null); // 在对应X，Y出画河
	}

	public int getRiverWidth() {
		return width;
	}

	public int getRiverLength() {
		return length;
	}

}
