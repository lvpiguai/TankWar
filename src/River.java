import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

/**
 * 河流类
 */
public class River extends GameObject {
	private static Image[] riverImags = null;
	static { // 存储图片
		 // 使用配置文件中的图像路径
		 riverImags = new Image[] { Toolkit.getDefaultToolkit().getImage(River.class.getResource(Config.riverConfig.imagePath)) };
	}

	public River(int x, int y) { // River的构造方法
		super(x, y);
		width = Config.riverConfig.width;      // 使用配置文件中的宽度
        length = Config.riverConfig.length;    // 使用配置文件中的长度
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
