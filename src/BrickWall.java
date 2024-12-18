import java.awt.*;

/**
 * 砖墙类（子弹可打穿）
 */
public class BrickWall extends LivedGameObject{
	private static Image[] images = null;
	static {
		images = new Image[] { // 储存commonWall的图片
			Toolkit.getDefaultToolkit().getImage(GameFrame.class.getResource(Config.brickWallConfig.imagePath))
		};
	}
	public BrickWall(int x, int y) { // 构造函数
		super(x, y);
		width = Config.brickWallConfig.width; // 使用配置中的宽度
        length = Config.brickWallConfig.length; // 使用配置中的长度
        bloodVolume = Config.brickWallConfig.bloodVolume; // 使用配置中的血量
	}
	public void beHited() { // 被击中
		setBloodVolume(bloodVolume - 1);
	}

	public void draw(Graphics g) {// 画commonWall
		g.drawImage(images[0], x, y, null);
	}

	public Rectangle getRect() { // 构造指定参数的长方形实例
		return new Rectangle(x, y, width, length);
	}
}
