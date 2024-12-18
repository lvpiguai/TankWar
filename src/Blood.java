import java.awt.*;
import java.util.Random;

/**
 * 血包类（医疗箱，可加血）
 */

public class Blood extends LivedGameObject{
	private static int count;//每 count 次变换位置
	private int posIdx;//位置下标
	private static Random r = new Random();
	private static Image[] images = null;
	private static int[][] poition;
	static{
		images = new Image[] { Toolkit.getDefaultToolkit().getImage(GameFrame.class.getResource(Config.bloodConfig.imagesPath)), };
		count = Config.bloodConfig.count;
		poition = Config.bloodConfig.position;
	}
	

	public Blood(int x, int y) {
		super(x, y);
		width = Config.bloodConfig.width;
		length = Config.bloodConfig.length;
		bloodVolume = Config.bloodConfig.bloodVolume;
		posIdx = r.nextInt(poition.length);//随机一个位置
	}

	@Override
	public void draw(Graphics g) {
		g.drawImage(images[0], x, y, null);
	}

	public void move() {
		//每 count 次变换位置
		if(--count == 0){
			count = Config.bloodConfig.count;
			posIdx = r.nextInt(poition.length);
		}
		x = poition[posIdx][0];
		y = poition[posIdx][1];
	}
}