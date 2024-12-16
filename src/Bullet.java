import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * 子弹类
 */

public class Bullet extends LivedGameObject implements Movable {
	public static int speedX = 10;
	public static int speedY = 10; // 子弹的全局静态速度
	Direction diretion;
	private boolean good;
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] bulletImages = null;
	private static Map<String, Image> imgs = new HashMap<String, Image>(); // 定义Map键值对，是不同方向对应不同的弹头

	static {
		bulletImages = new Image[] { // 不同方向的子弹
				tk.getImage(Bullet.class.getClassLoader().getResource("images/bulletL.gif")),

				tk.getImage(Bullet.class.getClassLoader().getResource("images/bulletU.gif")),

				tk.getImage(Bullet.class.getClassLoader().getResource("images/bulletR.gif")),

				tk.getImage(Bullet.class.getClassLoader().getResource("images/bulletD.gif")),

		};

		imgs.put("L", bulletImages[0]); // 加入Map容器

		imgs.put("U", bulletImages[1]);

		imgs.put("R", bulletImages[2]);

		imgs.put("D", bulletImages[3]);

	}
	// 构造函数，传递位置和方向和是否是己方子弹的标记
	public Bullet(int x, int y, Direction dir, boolean good) { 
		super(x, y);
		this.diretion = dir;
		this.good = good;
	}
	// 移动子弹
	public void move() {
		switch (diretion) {
		case L:
			x -= speedX; // 子弹不断向左进攻
			break;
		case U: // 子弹不断向上进攻
			y -= speedY;
			break;
		case R:
			x += speedX; // 子弹不断向右进攻
			break;
		case D: // 子弹不断向下进攻
			y += speedY;
			break;
		case STOP: // 游戏暂停
			break;
		}
	}
	// 获取子弹的宽度
	public int getWidth() {
		return width;
	}
	// 获取子弹的高度
	public int getLength(){
		return length;
	}
	//获取子弹类型
	public boolean isGood() {
		return good;
	}
	//子弹击中
	public void onHit() {
		setBloodVolume(bloodVolume - 1);
	}
	// 画出子弹
	@Override
	public void draw(Graphics g) {
		switch (diretion) { // 选择不同方向的子弹
			case L:
				g.drawImage(imgs.get("L"), x, y, null);
				break;
			case U:
				g.drawImage(imgs.get("U"), x, y, null);
				break;
			case R:
				g.drawImage(imgs.get("R"), x, y, null);
				break;
			case D:
				g.drawImage(imgs.get("D"), x, y, null);
				break;
		}
	}
	//越界检查
	@Override
	public boolean isOutOfBounds() {
		return x < 0 || y < 40 || x + width > GameFrame.Fram_width || y + length > GameFrame.Fram_length;
	}
	//碰撞检测
	@Override
	public boolean collide(GameObject obj) {
		return this.getRect().intersects(obj.getRect());
	}
}
