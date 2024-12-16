
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

/**
 * 大本营类
 */

public class Home extends LivedGameObject{
	
	private static Toolkit tk = Toolkit.getDefaultToolkit(); // 全局静态变量
	private static Image[] images;
	static {
		images = new Image[] { tk.getImage(BrickWall.class.getResource("Images/home.jpg")), };
	}

	public Home(int x, int y) {// 构造函数，传递Home的参数并赋值
		super(x, y);
		bloodVolume = 1;//血量为 1
	}

	public void draw(Graphics g) {
		g.drawImage(images[0], x, y, null);
	}

	public void beHited() { // 被击中
		setBloodVolume(bloodVolume - 1);
	}
}
