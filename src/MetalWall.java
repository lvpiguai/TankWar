import java.awt.Graphics;
import java.awt.Image;
import java.awt.Rectangle;
import java.awt.Toolkit;

/**
 * 金属墙类（钢板,子弹不可打穿）
 */
public class MetalWall extends GameObject {
	private static Image[] wallImags = null;
	static {
		// 使用配置文件中的图像路径
        wallImags = new Image[] { Toolkit.getDefaultToolkit().getImage(MetalWall.class.getResource(Config.metalWallConfig.imagePath)) };
	}

	public MetalWall(int x, int y) {// 构造函数，传递要构造的长宽并赋值
		super(x, y);
		width = Config.metalWallConfig.width;   // 使用配置文件中的宽度
        length = Config.metalWallConfig.length; // 使用配置文件中的长度
	}

	public void draw(Graphics g) { // 画金属墙
		g.drawImage(wallImags[0], x, y, null);
	}

	public Rectangle getRect() { // 构造指定参数的长方形实例
		return new Rectangle(x, y, width, length);
	}
}
