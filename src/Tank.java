import java.awt.*;

/**
 * 坦克类（适用敌方坦克和玩家坦克）
 */

public abstract class Tank extends LivedGameObject implements Movable {
	public static int speedX, speedY; // 坦克的移动速度
	protected Direction direction; // 初始化状态为静止
	protected Direction oldDirection; // 记录绘制方向
	protected int oldX, oldY;// 坦克移动前的坐标
	protected GameElements gameElements;// 游戏元素
	private static Image[] tankImags = null; // 坦克图片数组
	static {
		 // 从配置文件加载坦克的图片路径
		 tankImags = new Image[Config.tankConfig.imagePaths.length];
		 for (int i = 0; i < Config.tankConfig.imagePaths.length; i++) {
			 tankImags[i] = Toolkit.getDefaultToolkit().getImage(BombTank.class.getResource(Config.tankConfig.imagePaths[i]));
		 }
		 // 从配置文件中读取速度
		 speedX = Config.tankConfig.speedX;
		 speedY = Config.tankConfig.speedY;
	}
	// Tank的构造函数
	public Tank(int x, int y,Direction dir,GameElements gameElements) {
		super(x,y);
		direction = dir; // 初始化方向
		oldDirection = Direction.valueOf(Config.tankConfig.initialDirection); // 从配置文件获取初始方向
		this.gameElements = gameElements;// 游戏元素
	}
	public abstract void move(); // 移动
	public abstract void fire();
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
	public void restorePosition() {
		x = oldX;
		y = oldY;
	}
	//坦克被击中
	public void beHited() {
		setBloodVolume(bloodVolume - 1);
	}
	public int getX(){
		return x;
	}
	public int getY(){
		return y;
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
	public void setDirection(Direction dir){
		this.direction = dir;
	}

}