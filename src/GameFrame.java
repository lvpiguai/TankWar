import java.util.ArrayList;
import java.awt.*;
import java.awt.event.*;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * 坦克大战的主类
 */

public class GameFrame extends Frame implements ActionListener {

	private static final long serialVersionUID = 5972735870004738773L;

	public static final int Fram_width = 800; // 静态全局窗口大小
	public static final int Fram_length = 600;
	public static boolean printable = true; // 记录暂停状态，此时线程不刷新界面
	MenuBar jmb = null;
	Menu jm1 = null, jm2 = null, jm3 = null, jm4 = null;
	MenuItem jmi1 = null, jmi2 = null, jmi3 = null, jmi4 = null, jmi5 = null, jmi6 = null, jmi7 = null, jmi8 = null,
			jmi9 = null;

	Tank homeTank = new Tank(300, 560, true, Direction.STOP, this);// 己方坦克
	Blood blood = new Blood(); // 实例化血包
	Home home = new Home(373, 545, this);// 实例化home

	//
	List<River> theRiver = new ArrayList<River>();
	List<Tank> tanks = new ArrayList<Tank>();
	List<BombTank> bombTanks = new ArrayList<BombTank>();
	List<Bullets> bullets = new ArrayList<Bullets>();
	List<Tree> trees = new ArrayList<Tree>();
	List<BrickWall> homeWall = new ArrayList<BrickWall>(); // 实例化对象容器
	List<BrickWall> otherWall = new ArrayList<BrickWall>();
	List<MetalWall> metalWall = new ArrayList<MetalWall>();

	public static void main(String[] args) {
		new GameFrame(); // 实例化
	}

	// 构造函数（初始化）
	public GameFrame() {
		createMenu(); // 创建菜单
		initGameElements();// 初始化游戏元素
		initWindow();// 初始化窗口设置
		new Thread(new PaintThread()).start(); // 启动绘图线程
	}

	// 创建菜单（菜单条、菜单、菜单项）
	private void createMenu() {
		// 创建菜单条
		jmb = new MenuBar();

		// 把菜单加入菜单条
		String[] menuNames = { "游戏", "暂停/继续", "帮助", "游戏级别" }; // 菜单名
		Menu[] menus = new Menu[menuNames.length]; // 根据菜单名长度分配空间
		for (int i = 0; i < menuNames.length; i++) { // 遍历菜单名
			menus[i] = new Menu(menuNames[i]); // 创建菜单
			menus[i].setFont(new Font("TimesRoman", Font.BOLD, 15));// 设置字体
			jmb.add(menus[i]); // 加入菜单条
		}

		// 把菜单项加入菜单
		String[][] menuItemNames = { // 菜单项名
				{ "开始新游戏", "退出"}, 
				{ "暂停", "继续"}, 
				{ "游戏说明"}, 
				{ "级别1", "级别2", "级别3", "级别4"} 
		};
		for (int i = 0; i < menuItemNames.length; i++) { // 下标对应菜单
			for (String name : menuItemNames[i]) { // 各个菜单的菜单项
				MenuItem item = new MenuItem(name);
				item.setFont(new Font("TimesRoman", Font.BOLD, 15));
				menus[i].add(item);
				item.addActionListener(this); // 添加监视器
			}
		}
		// 菜单Bar放到JFrame上
		this.setMenuBar(jmb);
	}

	// 初始化游戏元素
	private void initGameElements() {
		createHomeWalls(); // 创建 homeWall
		createOtherWalls(); // 创建 otherWall
		createMetalWalls(); // 创建 metalWall
		createTrees(); // 创建 trees
		createTanks(); // 创建 tanks
		theRiver.add(new River(85, 100, this)); // 创建河流
	}

	// 初始化窗口设置
	private void initWindow() {
		this.setSize(Fram_width, Fram_length); // 设置界面大小
		setLocationRelativeTo(null);// 让窗体居中
		this.setTitle("坦克大战——(重新开始：R键  开火：F键) ");// 设置窗口标题
		this.addWindowListener(new WindowAdapter() { // 监听窗口关闭为退出程序
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});
		this.setResizable(false);// 禁止改变窗口大小
		this.setBackground(Color.GREEN);// 设置窗口颜色
		this.setVisible(true);// 设置窗口可见
		this.addKeyListener(new KeyMonitor());// 启动键盘监听
	}

	// 把任务包装成可供线程执行的对象
	private class PaintThread implements Runnable {// 实现RUNABLE接口,
		public void run() {
			while (printable) {// 未暂停游戏
				repaint();// 重绘所有组件
				try {
					Thread.sleep(50); // 休眠，避免绘图速度过快
				} catch (InterruptedException e) {// 线程中断异常
					e.printStackTrace();
				}
			}
		}
	}

	// 重写方法，使用双缓冲，避免屏幕闪烁 ，由repaint()方法自动调用
	public void update(Graphics g) {
		Image screenImage = this.createImage(Fram_width, Fram_length);// 创建缓冲区图像
		Graphics gps = screenImage.getGraphics();// 获取缓冲区图像
		Color c = gps.getColor();// 保存颜色
		gps.setColor(Color.GRAY);// 设置颜色为灰色
		gps.fillRect(0, 0, Fram_width, Fram_length);// 绘制背景
		gps.setColor(c);// 恢复颜色

		framPaint(gps);// 画出游戏其他元素

		g.drawImage(screenImage, 0, 0, null); // 将缓冲区图像绘制到窗口
	}

	// 画出游戏界面到缓冲区g
	public void framPaint(Graphics g) {
		drawGameInfo(g);// 画出游戏信息

		homeTank.eat(blood);// 加血--生命值

		// 如果玩家赢了（条件是敌方坦克全灭、大本营健在、玩家坦克仍有血量）
		if (tanks.size() == 0 && home.isLive() && homeTank.isLive()) {
			Font f = g.getFont();
			g.setFont(new Font("TimesRoman", Font.BOLD, 60));
			this.otherWall.clear();
			g.drawString("你赢了！ ", 310, 300);
			g.setFont(f);
		}
		// 如果玩家输了
		if (homeTank.isLive() == false) {
			Font f = g.getFont();
			g.setFont(new Font("TimesRoman", Font.BOLD, 40));
			tanks.clear();
			bullets.clear();
			g.setFont(f);
		}

		for (int i = 0; i < bullets.size(); i++) { // 对每一个子弹
			Bullets m = bullets.get(i);
			m.hitTanks(tanks); // 每一个子弹打到坦克上
			m.hitTank(homeTank); // 每一个子弹打到自己家的坦克上时
			m.hitHome(); // 每一个子弹打到家里时

			for (int j = 0; j < metalWall.size(); j++) { // 每一个子弹打到金属墙上
				MetalWall mw = metalWall.get(j);
				m.hitWall(mw);
			}

			for (int j = 0; j < otherWall.size(); j++) {// 每一个子弹打到其他墙上
				BrickWall w = otherWall.get(j);
				m.hitWall(w);
			}

			for (int j = 0; j < homeWall.size(); j++) {// 每一个子弹打到家的墙上
				BrickWall cw = homeWall.get(j);
				m.hitWall(cw);
			}
			m.draw(g); // 画出效果图
		}

		// 画出每一辆敌方坦克
		for (int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i); // 获得键值对的键

			for (int j = 0; j < homeWall.size(); j++) {
				BrickWall cw = homeWall.get(j);
				t.collideWithWall(cw); // 每一个坦克撞到家里的墙时
				cw.draw(g);
			}
			for (int j = 0; j < otherWall.size(); j++) { // 每一个坦克撞到家以外的墙
				BrickWall cw = otherWall.get(j);
				t.collideWithWall(cw);
				cw.draw(g);
			}
			for (int j = 0; j < metalWall.size(); j++) { // 每一个坦克撞到金属墙
				MetalWall mw = metalWall.get(j);
				t.collideWithWall(mw);
				mw.draw(g);
			}
			for (int j = 0; j < theRiver.size(); j++) {
				River r = theRiver.get(j); // 每一个坦克撞到河流时
				t.collideRiver(r);
				r.draw(g);
				// t.draw(g);
			}

			t.collideWithTanks(tanks); // 撞到自己的人
			t.collideHome(home);

			t.draw(g);
		}

		blood.draw(g);// 画出加血包
		home.draw(g); // 画出home
		homeTank.draw(g); // 画出自己家的坦克

		for (River r : theRiver) { // 画出河流
			r.draw(g);
		}
		for (Tree t : trees) {// 画出trees
			t.draw(g);
		}
		for (BombTank bt : bombTanks) {// 画出爆炸效果
			bt.draw(g);
		}
		for (BrickWall w : otherWall) {// 画出otherWall
			w.draw(g);
		}
		for (MetalWall mw : metalWall) {// 画出metalWall
			mw.draw(g);
		}

		homeTank.collideWithTanks(tanks);// 撞到坦克
		homeTank.collideHome(home);// 撞到家
		for (int i = 0; i < theRiver.size(); i++) {// 撞到河流
			River r = theRiver.get(i);
			homeTank.collideRiver(r);

			r.draw(g);
		}

		for (int i = 0; i < metalWall.size(); i++) {// 撞到金属墙
			MetalWall w = metalWall.get(i);
			homeTank.collideWithWall(w);
			w.draw(g);
		}

		for (int i = 0; i < otherWall.size(); i++) {// 撞到其他墙
			BrickWall cw = otherWall.get(i);
			homeTank.collideWithWall(cw);
			cw.draw(g);
		}

		for (int i = 0; i < homeWall.size(); i++) { // 家里的坦克撞到自己家
			BrickWall w = homeWall.get(i);
			homeTank.collideWithWall(w);
			w.draw(g);
		}

	}

	private class KeyMonitor extends KeyAdapter {

		public void keyReleased(KeyEvent e) { // 监听键盘释放
			homeTank.keyReleased(e);
		}

		public void keyPressed(KeyEvent e) { // 监听键盘按下
			homeTank.keyPressed(e);
		}
	}

	// 重写的监视器接口方法
	public void actionPerformed(ActionEvent e) {

		if (e.getActionCommand().equals("NewGame")) {
			printable = false;
			Object[] options = { "确定", "取消" };
			int response = JOptionPane.showOptionDialog(this, "您确认要开始新游戏！", "", JOptionPane.YES_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (response == 0) {

				printable = true;
				this.dispose();
				new GameFrame();
			} else {
				printable = true;
				new Thread(new PaintThread()).start(); // 线程启动
			}

		} else if (e.getActionCommand().endsWith("Stop")) {// 暂停
			printable = false;
		} else if (e.getActionCommand().equals("Continue")) {// 继续
			if (!printable) {
				printable = true;
				new Thread(new PaintThread()).start(); // 线程启动
			}
			// System.out.println("继续");
		} else if (e.getActionCommand().equals("Exit")) {// 退出
			printable = false;
			Object[] options = { "确定", "取消" };
			int response = JOptionPane.showOptionDialog(this, "您确认要退出吗", "", JOptionPane.YES_OPTION,
					JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
			if (response == 0) {
				System.out.println("退出");
				System.exit(0);
			} else {
				printable = true;
				new Thread(new PaintThread()).start(); // 线程启动
			}
		} else if (e.getActionCommand().equals("help")) {
			printable = false;
			JOptionPane.showMessageDialog(null, "用→ ← ↑ ↓控制方向，F键盘发射，R重新开始！", "提示！", JOptionPane.INFORMATION_MESSAGE);
			this.setVisible(true);
			printable = true;
			new Thread(new PaintThread()).start(); // 线程启动
		} else if (e.getActionCommand().equals("level1")) {
			Tank.count = 12;
			Tank.speedX = 6;
			Tank.speedY = 6;
			Bullets.speedX = 10;
			Bullets.speedY = 10;
			this.dispose();
			new GameFrame();
		} else if (e.getActionCommand().equals("level2")) {
			Tank.count = 12;
			Tank.speedX = 10;
			Tank.speedY = 10;
			Bullets.speedX = 12;
			Bullets.speedY = 12;
			this.dispose();
			new GameFrame();

		} else if (e.getActionCommand().equals("level3")) {
			Tank.count = 20;
			Tank.speedX = 14;
			Tank.speedY = 14;
			Bullets.speedX = 16;
			Bullets.speedY = 16;
			this.dispose();
			new GameFrame();
		} else if (e.getActionCommand().equals("level4")) {
			Tank.count = 20;
			Tank.speedX = 16;
			Tank.speedY = 16;
			Bullets.speedX = 18;
			Bullets.speedY = 18;
			this.dispose();
			new GameFrame();
		}
	}

	// 创建家的墙
	private void createHomeWalls() {
		for (int i = 0; i < 10; i++) { // 家的格局
			if (i < 4)
				homeWall.add(new BrickWall(350, 580 - 21 * i, this));
			else if (i < 7)
				homeWall.add(new BrickWall(372 + 22 * (i - 4), 517, this));
			else
				homeWall.add(new BrickWall(416, 538 + (i - 7) * 21, this));
		}
	}

	// 创建其他墙
	private void createOtherWalls() {
		for (int i = 0; i < 32; i++) { // 砖墙
			if (i < 16) {
				otherWall.add(new BrickWall(220 + 20 * i, 300, this)); // 砖墙布局
				otherWall.add(new BrickWall(500 + 20 * i, 180, this));
				otherWall.add(new BrickWall(200, 400 + 20 * i, this));
				otherWall.add(new BrickWall(500, 400 + 20 * i, this));
			} else if (i < 32) {
				otherWall.add(new BrickWall(220 + 20 * (i - 16), 320, this));
				otherWall.add(new BrickWall(500 + 20 * (i - 16), 220, this));
				otherWall.add(new BrickWall(220, 400 + 20 * (i - 16), this));
				otherWall.add(new BrickWall(520, 400 + 20 * (i - 16), this));
			}
		}
	}

	// 创建金属墙
	private void createMetalWalls() {
		for (int i = 0; i < 20; i++) { // 金属墙布局
			if (i < 10) {
				metalWall.add(new MetalWall(140 + 30 * i, 150, this));
				metalWall.add(new MetalWall(600, 400 + 20 * (i), this));
			} else if (i < 20)
				metalWall.add(new MetalWall(140 + 30 * (i - 10), 180, this));
			else
				metalWall.add(new MetalWall(500 + 30 * (i - 10), 160, this));
		}
	}

	// 创建树
	private void createTrees() {
		for (int i = 0; i < 4; i++) { // 树的布局
			if (i < 4) {
				trees.add(new Tree(0 + 30 * i, 360, this));
				trees.add(new Tree(220 + 30 * i, 360, this));
				trees.add(new Tree(440 + 30 * i, 360, this));
				trees.add(new Tree(660 + 30 * i, 360, this));
			}
		}
	}

	// 创建坦克
	private void createTanks() {
		for (int i = 0; i < 20; i++) { // 初始化20辆坦克
			if (i < 9) // 设置坦克出现的位置
				tanks.add(new Tank(150 + 70 * i, 40, false, Direction.D, this));
			else if (i < 15)
				tanks.add(new Tank(700, 140 + 50 * (i - 6), false, Direction.D, this));
			else
				tanks.add(new Tank(10, 50 * (i - 12), false, Direction.D, this));
		}
	}

	// 绘制游戏信息
	private void drawGameInfo(Graphics g) {
		Color c = g.getColor(); // 保存原本的颜色
		g.setColor(Color.green); // 设置为绿色

		Font f1 = g.getFont(); // 保存原本的字体
		Font font1 = new Font("TimesRoman", Font.BOLD, 20); // 设置新的字体
		Font font2 = new Font("TimesRoman", Font.BOLD, 30); // 设置新的字体
		// 显示地方坦克数目
		g.setFont(font1);
		g.drawString("区域内还有敌方坦克: ", 200, 70 ); 
		g.setFont(font2);
		g.drawString("" + tanks.size(), 400, 70);
		// 显示剩余生命值
		g.setFont(font1);
		g.drawString("剩余生命值: ", 500, 70);
		g.setFont(font2);
		g.drawString("" + homeTank.getLife(), 650, 70);

		g.setFont(f1);// 恢复原本字体
		g.setColor(c); // 恢复颜色
	}
}
