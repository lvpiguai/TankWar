import java.awt.*;
import java.awt.event.*;
import java.util.*;

/**
 * 坦克类（适用敌方坦克和玩家坦克）
 */

public class Tank extends GameObject {
	public static int speedX = 6, speedY = 6; // 坦克的移动速度
	public static int count = 0; // 坦克的数量
	private Direction direction = Direction.STOP; // 初始化状态为静止
	private Direction Kdirection = Direction.U; // 记录绘制方向
	GameFrame tc;

	private boolean good;// true为己方坦克，false为敌方坦克
	private int oldX, oldY;// 坦克移动前的坐标
	private int life = 200; // 初始生命值

	private static Random r = new Random(); // 随机数变量
	private int step = r.nextInt(10) + 5; // 产生一个随机数,随机模拟坦克的移动路径

	private boolean bL = false, bU = false, bR = false, bD = false; // 按键状态

	private static Toolkit tk = Toolkit.getDefaultToolkit();// 控制面板
	private static Image[] tankImags = null; // 坦克图片数组
	static {
		tankImags = new Image[] { tk.getImage(BombTank.class.getResource("Images/tankD.gif")),
				tk.getImage(BombTank.class.getResource("Images/tankU.gif")),
				tk.getImage(BombTank.class.getResource("Images/tankL.gif")),
				tk.getImage(BombTank.class.getResource("Images/tankR.gif")), };
		width = length = 35;// 坦克的大小

	}

	public Tank(int x, int y, boolean good) {// Tank的构造函数1
		super(x,y);
		this.oldX = x;
		this.oldY = y;
		this.good = good;
	}

	public Tank(int x, int y, boolean good, Direction dir, GameFrame tc) {// Tank的构造函数2
		this(x, y, good);
		this.direction = dir;
		this.tc = tc;
	}

	// 画出坦克
	public void draw(Graphics g) {
		if (!live) {// 如果坦克死了
			if (!good) {//
				tc.getGameElements().getTanks().remove(this); // 删除无效的
			}
			return;
		}

		if (good)// 如果是己方坦克
			new DrawBloodbBar().draw(g); // 玩家坦克的血量条

		switch (Kdirection) {
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

		}
		move(); // 调用move函数
	}

	// 坦克移动
	void move() {

		// 记录移动前的坐标
		this.oldX = x;
		this.oldY = y;

		// 根据方向移动对应距离
		switch (direction) { // 选择移动方向
			case L:
				x -= speedX;
				break;
			case U:
				y -= speedY;
				break;
			case R:
				x += speedX;
				break;
			case D:
				y += speedY;
				break;
			case STOP:
				break;
		}

		// 记录绘制图像得方向
		if (this.direction != Direction.STOP) { // 若暂停则不移动
			this.Kdirection = this.direction;
		}

		// 防止越界，超过区域则恢复到边界
		if (x < 0)
			x = 0;
		if (y < 40)
			y = 40;
		if (x + Tank.width > GameFrame.Fram_width)
			x = GameFrame.Fram_width - Tank.width;
		if (y + Tank.length > GameFrame.Fram_length)
			y = GameFrame.Fram_length - Tank.length;

		// 敌方坦克，随机路径
		if (!good) {
			Direction[] directons = Direction.values();// 获取所有方向得值
			if (step == 0) { // 步数移动完毕
				step = r.nextInt(12) + 3; // 随机步数
				int rn = r.nextInt(directons.length);
				direction = directons[rn]; // 产生随机方向
			}
			step--;// 每次移动一步

			// 5% 的概率 发射子弹
			if (r.nextInt(40) > 38)
				this.fire();
		}
	}

	// 恢复移动前的坐标
	private void changToOldDir() {
		x = oldX;
		y = oldY;
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

	// 发射子弹
	public Bullets fire() { // 开火方法
		if (!live)// 如果坦克死了
			return null;
		int x = this.x + Tank.width / 2 - Bullets.width / 2; // 开火位置
		int y = this.y + Tank.length / 2 - Bullets.length / 2;
		Bullets m = new Bullets(x, y + 2, good, Kdirection, this.tc); // 没有给定方向时，向原来的方向发火
		tc.getGameElements().getBullets().add(m);// 添加子弹
		return m;
	}

	//
	public Rectangle getRect() {
		return new Rectangle(x, y, width, length);
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public boolean isGood() {
		return good;
	}

	public boolean collideWithWall(BrickWall w) { // 碰撞到普通墙时
		if (this.live && this.getRect().intersects(w.getRect())) {
			this.changToOldDir(); // 转换到原来的方向上去
			return true;
		}
		return false;
	}

	public boolean collideWithWall(MetalWall w) { // 撞到金属墙
		if (this.live && this.getRect().intersects(w.getRect())) {
			this.changToOldDir();
			return true;
		}
		return false;
	}

	public boolean collideRiver(River r) { // 撞到河流的时候
		if (this.live && this.getRect().intersects(r.getRect())) {
			this.changToOldDir();
			return true;
		}
		return false;
	}

	public boolean collideHome(Home h) { // 撞到家的时候
		if (this.live && this.getRect().intersects(h.getRect())) {
			this.changToOldDir();
			return true;
		}
		return false;
	}

	public boolean collideWithTanks(java.util.List<Tank> tanks) {// 撞到坦克时
		for (int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);
			if (this != t) {
				if (this.live && t.isLive() && this.getRect().intersects(t.getRect())) {
					this.changToOldDir();
					t.changToOldDir();
					return true;
				}
			}
		}
		return false;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	// 血量条-内部类
	private class DrawBloodbBar {
		public void draw(Graphics g) {
			Color c = g.getColor();// 保存颜色
			g.setColor(Color.RED);// 设置为红色
			g.drawRect(375, 585, width, 10);// 矩阵血量条
			int w = width * life / 200; // 计算血量
			g.fillRect(375, 585, w, 10);// 显示玩家坦克的血量条
			g.setColor(c); // 恢复背景颜色
		}
	}

	// 吃血包
	public boolean eat(Blood b) {
		if (this.live && b.isLive() && this.getRect().intersects(b.getRect())) {
			if (this.life <= 100)
				this.life = this.life + 100; // 每吃一个，增加100生命点
			else
				this.life = 200;
			b.setLive(false);
			return true;
		}
		return false;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}