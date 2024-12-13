import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class GameElements {
    private GameFrame gameFrame = null;
    private Tank homeTank = null;// 己方坦克
    private Blood blood = new Blood(); // 实例化血包
    private Home home = null;// 实例化home
    private List<BombTank> bombTanks = new ArrayList<>();
    private List<Tank> tanks = new ArrayList<>();
    private List<Bullets> bullets = new ArrayList<>();
    private List<BrickWall> homeWall = new ArrayList<>();
    private List<BrickWall> otherWall = new ArrayList<>();
    private List<MetalWall> metalWall = new ArrayList<>();
    private List<River> theRiver = new ArrayList<>();
    private List<Tree> trees = new ArrayList<>();

    // 构造函数
    public GameElements(GameFrame gameFrame) {
        this.gameFrame = gameFrame;
        homeTank = new Tank(300, 560, true, Direction.STOP, gameFrame);// 己方坦克
        home = new Home(373, 545, gameFrame);// 实例化home
    }

    // 初始化游戏元素
	public void initGameElements() {
		createHomeWalls(); // 创建 homeWall
		createOtherWalls(); // 创建 otherWall
		createMetalWalls(); // 创建 metalWall
		createTrees(); // 创建 trees
		createTanks(); // 创建 tanks
		theRiver.add(new River(85, 100, gameFrame)); // 创建河流
	}

    // 绘制所有元素
    public void drawElements(Graphics g) {
        blood.draw(g);// 画出加血包
		home.draw(g); // 画出home
		homeTank.draw(g); // 画出自己家的坦克

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
        for (int i = 0; i < bullets.size(); i++) { // 画出每一个子弹
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
        for (BrickWall wall : homeWall) {// 画出家的墙
            wall.draw(g);
        }
        for(BrickWall w:otherWall){// 画出其他墙
			w.draw(g);
		}
        for (MetalWall wall : metalWall) {  // 画出金属墙
            wall.draw(g);
        }
        for (Tree tree : trees) {// 画出树
            tree.draw(g);
        }
        for (River river : theRiver) {// 画出河
            river.draw(g);
        }
    }

    // 检查元素间的碰撞
    public void checkCollisions(Graphics g) {
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
	//清空所有元素
	public void clearAllElements() {
		bombTanks.clear();
		tanks.clear();
		bullets.clear();
		homeWall.clear();
		otherWall.clear();
		metalWall.clear();
		theRiver.clear();
		trees.clear();
	}


    // 创建家的墙
	private void createHomeWalls() {
		for (int i = 0; i < 10; i++) { // 家的格局
			if (i < 4)
				homeWall.add(new BrickWall(350, 580 - 21 * i, gameFrame));
			else if (i < 7)
				homeWall.add(new BrickWall(372 + 22 * (i - 4), 517, gameFrame));
			else
				homeWall.add(new BrickWall(416, 538 + (i - 7) * 21, gameFrame));
		}
	}

	// 创建其他墙
	private void createOtherWalls() {
		for (int i = 0; i < 32; i++) { // 砖墙
			if (i < 16) {
				otherWall.add(new BrickWall(220 + 20 * i, 300, gameFrame)); // 砖墙布局
				otherWall.add(new BrickWall(500 + 20 * i, 180, gameFrame));
				otherWall.add(new BrickWall(200, 400 + 20 * i, gameFrame));
				otherWall.add(new BrickWall(500, 400 + 20 * i, gameFrame));
			} else if (i < 32) {
				otherWall.add(new BrickWall(220 + 20 * (i - 16), 320, gameFrame));
				otherWall.add(new BrickWall(500 + 20 * (i - 16), 220, gameFrame));
				otherWall.add(new BrickWall(220, 400 + 20 * (i - 16), gameFrame));
				otherWall.add(new BrickWall(520, 400 + 20 * (i - 16), gameFrame));
			}
		}
	}

	// 创建金属墙
	private void createMetalWalls() {
		for (int i = 0; i < 20; i++) { // 金属墙布局
			if (i < 10) {
				metalWall.add(new MetalWall(140 + 30 * i, 150, gameFrame));
				metalWall.add(new MetalWall(600, 400 + 20 * (i), gameFrame));
			} else if (i < 20)
				metalWall.add(new MetalWall(140 + 30 * (i - 10), 180, gameFrame));
			else
				metalWall.add(new MetalWall(500 + 30 * (i - 10), 160, gameFrame));
		}
	}

	// 创建树
	private void createTrees() {
		for (int i = 0; i < 4; i++) { // 树的布局
			if (i < 4) {
				trees.add(new Tree(0 + 30 * i, 360, gameFrame));
				trees.add(new Tree(220 + 30 * i, 360, gameFrame));
				trees.add(new Tree(440 + 30 * i, 360, gameFrame));
				trees.add(new Tree(660 + 30 * i, 360, gameFrame));
			}
		}
	}

	// 创建坦克
	private void createTanks() {
		for (int i = 0; i < 20; i++) { // 初始化20辆坦克
			if (i < 9) // 设置坦克出现的位置
				tanks.add(new Tank(150 + 70 * i, 40, false, Direction.D, gameFrame));
			else if (i < 15)
				tanks.add(new Tank(700, 140 + 50 * (i - 6), false, Direction.D, gameFrame));
			else
				tanks.add(new Tank(10, 50 * (i - 12), false, Direction.D, gameFrame));
		}
	}

    //获取己方坦克
    public Tank getHomeTank() {
        return homeTank;
    }
    //获取敌方坦克
    public List<Tank> getTanks() {
        return tanks;
    }
    //获取子弹
    public List<Bullets> getBullets() {
        return bullets;
    }
    //获取家墙
    public List<BrickWall> getHomeWall() {
        return homeWall;
    }
    //获取其他墙
    public List<BrickWall> getOtherWall() {
        return otherWall;
    }
    //获取金属墙
    public List<MetalWall> getMetalWall() {
        return metalWall;
    }
    //获取河流
    public List<River> getTheRiver() {
        return theRiver;
    }
    //获取树
    public List<Tree> getTrees() {
        return trees;
    }
    //获取加血包
    public Blood getBlood() {
        return blood;
    }
    //获取家
    public Home getHome() {
        return home;
    }
    //获取爆炸坦克
    public List<BombTank> getBombTanks() {
        return bombTanks;
    }
}
