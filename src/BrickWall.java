import java.awt.*;

/**
 * 砖墙类（子弹可打穿）
 */
public class BrickWall extends LivedGameObject{
	
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] wallImags = null;
	static {
		wallImags = new Image[] { // 储存commonWall的图片
				tk.getImage(BrickWall.class.getResource("Images/commonWall.gif")), };
	}

	public BrickWall(int x, int y) { // 构造函数
		super(x, y);
		width = length = 20;
	}
	public void beHited() { // 被击中
		setBloodVolume(bloodVolume - 1);
	}

	public void draw(Graphics g) {// 画commonWall
		g.drawImage(wallImags[0], x, y, null);
	}

	public Rectangle getRect() { // 构造指定参数的长方形实例
		return new Rectangle(x, y, width, length);
	}
}
