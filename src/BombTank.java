import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

/**
 * 爆炸的坦克类（模拟坦克爆炸过程）
 */
public class BombTank extends LivedGameObject{
	private int step;
	private static Image[] images = null;
	static{ // 存储爆炸图片（从小到大的爆炸效果图）
		// 使用从配置文件中读取的爆炸图片路径
        images = new Image[Config.bombTankConfig.explosionImages.length];
        for (int i = 0; i < images.length; i++) {
            images[i] = Toolkit.getDefaultToolkit().getImage(GameFrame.class.getClassLoader().getResource(Config.bombTankConfig.explosionImages[i]));
        }
	}

	public BombTank(int x, int y) { // 构造函数
		super(x, y);
		bloodVolume = Config.bombTankConfig.bloodVolume; // 使用配置中的血量
		step = 0;
	}

	public void draw(Graphics g) { // 画出爆炸图像
		if (step == images.length) {
			setBloodVolume(0);
			step = 0;
			return;
		}
		g.drawImage(images[step], x, y, null);
		step++;
	}
}
