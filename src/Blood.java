import java.awt.*;

/**
 * 血包类（医疗箱，可加血）
 */

public class Blood extends LivedGameObject{
	private int count = 5;
	private int step;
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private Image[] bloodImags = new Image[] { tk.getImage(BrickWall.class.getResource("Images/hp.png")), };

	private int[][] poition = { { 155, 196 }, { 500, 58 }, { 80, 340 }, { 99, 199 }, { 345, 456 }, { 123, 321 },
			{ 258, 413 } };

	public Blood(int x, int y) {
		super(x, y);
		width = length = 36;
	}

	public void draw(Graphics g) {
		g.drawImage(bloodImags[0], x, y, null);
	}

	public void move() {
		//每 5 秒变换位置
		if(--count == 0){
			++step;
			if(step >= poition.length){
				step = 0;
			}
			count = 5;
		}
		x = poition[step][0];
		y = poition[step][1];
	}
	public void setStep(int step) {
		this.step = step;
	}
	public int getStep(){
		return step;
	}

}