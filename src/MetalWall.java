import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

/**
 * 金属墙类（钢板,子弹不可打穿）
 */
public class MetalWall extends GameObject {
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] wallImags = null;
	static {
		wallImags = new Image[] { tk.getImage(BrickWall.class.getResource("Images/metalWall.gif")), };
	}

	public MetalWall(int x, int y) {// 构造函数，传递要构造的长宽并赋值
		super(x, y);
		width = length =30;
	}

	public void draw(Graphics g) { // 画金属墙
		g.drawImage(wallImags[0], x, y, null);
	}

	public Rectangle getRect() { // 构造指定参数的长方形实例
		return new Rectangle(x, y, width, length);
	}
}
