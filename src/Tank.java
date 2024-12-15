import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * 坦克类（适用敌方坦克和玩家坦克）
 */

public abstract class Tank extends LivedGameObject implements Movable {
	public static int speedX, speedY; // 坦克的移动速度
	private Direction direction; // 初始化状态为静止
	private Direction oldDirection; // 记录绘制方向
	private int oldX, oldY;// 坦克移动前的坐标
	private boolean bL = false, bU = false, bR = false, bD = false; // 按键状态

	private static Toolkit tk = Toolkit.getDefaultToolkit();// 控制面板
	private static Image[] tankImags = null; // 坦克图片数组
	static {
		tankImags = new Image[] { tk.getImage(BombTank.class.getResource("Images/tankD.gif")),
				tk.getImage(BombTank.class.getResource("Images/tankU.gif")),
				tk.getImage(BombTank.class.getResource("Images/tankL.gif")),
				tk.getImage(BombTank.class.getResource("Images/tankR.gif")), };
				speedX = speedY = 6;
	}
	// Tank的构造函数
	public Tank(int x, int y,Direction dir) {
		super(x,y);
		width = length = 35;// 坦克的大小
		direction = dir; // 初始化状态为静止
	}
	public abstract void move(); // 移动
	public abstract void fire();// 发射
	// 画出坦克
	@Override
	public void draw(Graphics g) {
		switch (oldDirection) {
			// 根据方向选择坦克的图片
			case D:
				g.drawImage(tankImags[0], x, y, null);
				break;
			case U:
				g.drawImage(tankImags[1], x, y, null);
				break;
			case L:
				g.drawImage(tankImags[2], x, y, null);
				break;
			case R:
				g.drawImage(tankImags[3], x, y, null);
				break;
			default:break;
		}
	}
	// 恢复移动前的坐标
	@Override
	public void restorePosition() {
		x = oldX;
		y = oldY;
	}
	// 碰撞检测
	@Override
	public boolean collide(GameObject obj){
		return this.getRect().intersects(obj.getRect());
	}
	//越界检查
	@Override
	public boolean isOutOfBounds(){
		return x<0||y<40||x+width>GameFrame.Fram_width||y+length>GameFrame.Fram_length;
	}

	// 监听键盘
	public void keyPressed(KeyEvent e) { // 接受键盘事件
		int key = e.getKeyCode(); // 获取键
		switch (key) {
			case KeyEvent.VK_R: // 当按下R时，重新开始游戏
				tc.getGameElements().clearAllElements();
				tc.getGameElements().getHomeTank().setLive(true);
				if (tc.getGameElements().getTanks().size() == 0) { // 当在区域中没有坦克时，就出来坦克
					for (int i = 0; i < 20; i++) {
						if (i < 9) // 设置坦克出现的位置
							tc.getGameElements().getTanks().add(new Tank(150 + 70 * i, 40, false, Direction.R, tc));
						else if (i < 15)
							tc.getGameElements().getTanks()
									.add(new Tank(700, 140 + 50 * (i - 6), false, Direction.D, tc));
						else
							tc.getGameElements().getTanks().add(new Tank(10, 50 * (i - 12), false, Direction.L, tc));
					}
				}
				Tank homeTank = tc.getGameElements().getHomeTank();
				homeTank = new Tank(300, 560, true, Direction.STOP, tc);// 设置自己出现的位置
				if (!tc.getGameElements().getHome().isLive()) // 将home重置生命
					tc.getGameElements().getHome().setLive(true);
				new GameFrame(); // 重新创建面板
				break;
			case KeyEvent.VK_RIGHT: // 监听向右键
				bR = true;
				break;

			case KeyEvent.VK_LEFT:// 监听向左键
				bL = true;
				break;

			case KeyEvent.VK_UP: // 监听向上键
				bU = true;
				break;

			case KeyEvent.VK_DOWN:// 监听向下键
				bD = true;
				break;
		}
		decideDirection();// 调用函数确定移动方向
	}

	// 决定移动得方向
	void decideDirection() {
		if (!bL && !bU && bR && !bD) // 向右移动
			direction = Direction.R;

		else if (bL && !bU && !bR && !bD) // 向左移
			direction = Direction.L;

		else if (!bL && bU && !bR && !bD) // 向上移动
			direction = Direction.U;

		else if (!bL && !bU && !bR && bD) // 向下移动
			direction = Direction.D;

		else if (!bL && !bU && !bR && !bD)
			direction = Direction.STOP; // 没有按键，就保持不动
	}

	// 键盘释放
	public void keyReleased(KeyEvent e) { // 键盘释放监听
		int key = e.getKeyCode();
		switch (key) {
			case KeyEvent.VK_F: // 按键释放后才开火
				fire();
				break;
			case KeyEvent.VK_RIGHT:
				bR = false;
				break;

			case KeyEvent.VK_LEFT:
				bL = false;
				break;

			case KeyEvent.VK_UP:
				bU = false;
				break;

			case KeyEvent.VK_DOWN:
				bD = false;
				break;

		}
		decideDirection(); // 释放键盘后确定移动方向
	}

}