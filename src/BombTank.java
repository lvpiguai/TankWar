import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

/**
 * 爆炸的坦克类（模拟坦克爆炸过程）
 */
public class BombTank extends LivedGameObject{
	int step = 0;
	private static Image[] images = null;
	static{ // 存储爆炸图片（从小到大的爆炸效果图）
		String[]strArr = new String[10];
		images = new Image[10];
		for(int i = 0;i<strArr.length;i++){
			strArr[i] = "Images/"+(i+1)+".gif";
			images[i] = Toolkit.getDefaultToolkit().getImage(GameFrame.class.getClassLoader().getResource(strArr[i]));
		}
	}

	public BombTank(int x, int y) { // 构造函数
		super(x, y);
		bloodVolume = 1;
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
