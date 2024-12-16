import java.awt.*;
import java.util.Random;

/**
 * 血包类（医疗箱，可加血）
 */

public class Blood extends LivedGameObject{
	private int count = 100;
	private int posIdx;//位置下标
	private static Random r = new Random();
	private static Image[] images = null;
	static{
		images = new Image[] { Toolkit.getDefaultToolkit().getImage(GameFrame.class.getResource("Images/hp.png")), };
	}
	private int[][] poition = { { 155, 196 }, { 500, 58 }, { 80, 340 }, { 99, 199 }, { 345, 456 }, { 123, 321 },
			{ 258, 413 } };

	public Blood(int x, int y) {
		super(x, y);
		width = length = 36;
		bloodVolume = 1;
		posIdx = r.nextInt(poition.length);//随机一个位置
	}

	public void draw(Graphics g) {
		g.drawImage(images[0], x, y, null);
	}

	public void move() {
		//每 count 次变换位置
		if(--count == 0){
			count = 100;
			posIdx = r.nextInt(poition.length);
		}
		x = poition[posIdx][0];
		y = poition[posIdx][1];
	}
}