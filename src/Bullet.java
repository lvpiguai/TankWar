import java.awt.*;
/**
 * 子弹类
 */

public class Bullet extends LivedGameObject implements Movable {
	public static int speedX;
	public static int speedY; // 子弹的全局静态速度
	Direction diretion;
	private boolean good;
	private static Image[] images = null;
	static{
		// 加载子弹图片
        images = new Image[Config.bulletConfig.imagePaths.length];
        for (int i = 0; i < images.length; i++) {
            images[i] = Toolkit.getDefaultToolkit().getImage(GameFrame.class.getResource(Config.bulletConfig.imagePaths[i]));
        }
        // 从配置文件中读取速度
        speedX = Config.bulletConfig.speedX;
        speedY = Config.bulletConfig.speedY;
	}
	// 构造函数，传递位置和方向和是否是己方子弹的标记
	public Bullet(int x, int y, Direction dir, boolean good) { 
		super(x, y);
		this.diretion = dir;
		this.good = good;
		bloodVolume = 1;
        width = Config.bulletConfig.width;
        length = Config.bulletConfig.length;
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
		g.drawImage(images[diretion.ordinal()], x, y, null);
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
