
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

/**
 * 大本营类
 */

public class Home extends LivedGameObject{
	private static Image[] images;
	static {
		// 使用 Config 中的配置来加载图像路径
        images = new Image[] { Toolkit.getDefaultToolkit().getImage(GameFrame.class.getResource(Config.homeConfig.imagePath))};
	}

	public Home(int x, int y) {// 构造函数，传递Home的参数并赋值
		super(x, y);
		bloodVolume = Config.homeConfig.bloodVolume; // 使用配置文件中的血量
	}

	public void draw(Graphics g) {
		g.drawImage(images[0], x, y, null);
	}

	public void beHited() { // 被击中
		setBloodVolume(bloodVolume - 1);
	}
}
