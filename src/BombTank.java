import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

/**
 * 爆炸的坦克类（模拟坦克爆炸过程）
 */
public class BombTank extends LivedGameObject{
	private int x, y;
	private static Toolkit tk = Toolkit.getDefaultToolkit();

	private static Image[] imgs = { // 存储爆炸图片（从小到大的爆炸效果图）
			tk.getImage(BombTank.class.getClassLoader().getResource("images/1.gif")),
			tk.getImage(BombTank.class.getClassLoader().getResource("images/2.gif")),
			tk.getImage(BombTank.class.getClassLoader().getResource("images/3.gif")),
			tk.getImage(BombTank.class.getClassLoader().getResource("images/4.gif")),
			tk.getImage(BombTank.class.getClassLoader().getResource("images/5.gif")),
			tk.getImage(BombTank.class.getClassLoader().getResource("images/6.gif")),
			tk.getImage(BombTank.class.getClassLoader().getResource("images/7.gif")),
			tk.getImage(BombTank.class.getClassLoader().getResource("images/8.gif")),
			tk.getImage(BombTank.class.getClassLoader().getResource("images/9.gif")),
			tk.getImage(BombTank.class.getClassLoader().getResource("images/10.gif")), };
	int step = 0;

	public BombTank(int x, int y) { // 构造函数
		super(x, y);
	}

	public void draw(Graphics g) { // 画出爆炸图像
		if (step == imgs.length) {
			setBloodVolume(0);
			step = 0;
			return;
		}
		g.drawImage(imgs[step], x, y, null);
		step++;
	}
}
