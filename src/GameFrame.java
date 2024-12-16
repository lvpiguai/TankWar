import java.awt.*;

/**
 * 坦克大战的主类
 */

public class GameFrame extends Frame{

	private static final long serialVersionUID = 5972735870004738773L;
	public static final int Fram_width = 800; // 静态全局窗口大小
	public static final int Fram_length = 600;// 静态全局窗口大小

	private WindowManager windowManager;// 窗口和菜单管理
	private GameState gameState;// 游戏状态
	private GameElements gameElements;// 游戏元素
	public static void main(String[] args) {
		new GameFrame().startGame(); //启动游戏
	}

	// 构造函数(创建对象)
	public GameFrame() {
		gameState = new GameState();// 游戏状态 (必须在windowManager之前)
		windowManager = new WindowManager(this); // 窗口和菜单管理
		gameElements = new GameElements(gameState);// 游戏元素
	}
	//启动游戏
	public void startGame(){
		windowManager.initWindow();// 初始化窗口
		windowManager.createMenu();// 创建菜单
		gameElements.initGameElements();// 初始化游戏元素
		new Thread(new GameLoopThread()).start();// 启动游戏循环线程
	}
	// 把任务包装成可供线程执行的对象
	private class GameLoopThread implements Runnable {// 实现RUNABLE接口,
		public void run() {
			System.out.println(gameState.getCurrentState());
			while (true) {//游戏进行中
				if(gameState.isInProgress()){
					repaint();// 重绘所有组件
					gameElements.update();	//更新所有游戏元素
				}
				try {
					Thread.sleep(50); // 休眠，避免绘图速度过快
				} catch (InterruptedException e) {// 线程中断异常
					e.printStackTrace();
				}
			}
		}
	}

	//重写方法，使用双缓冲，避免屏幕闪烁 ，由repaint()方法自动调用
	public void update(Graphics g) {
		Image screenImage = this.createImage(Fram_width, Fram_length);// 创建缓冲区图像
		Graphics gps = screenImage.getGraphics();// 获取缓冲区图像
		Color c = gps.getColor();// 保存颜色
		gps.setColor(Color.GRAY);// 设置颜色为灰色
		gps.fillRect(0, 0, Fram_width, Fram_length);// 绘制背景
		gps.setColor(c);// 恢复颜色

		framPaint(gps);// 绘制整个游戏

		g.drawImage(screenImage, 0, 0, null); // 将缓冲区图像绘制到窗口
	}
	// 画出游戏界面到缓冲区g
	public void framPaint(Graphics g) {
		gameElements.drawElements(g);// 画出所有元素
		gameState.drawState(g, gameElements);// 画出游戏状态
	}
	// 提供一个 get 方法访问 gameState
	public GameState getGameState() {
        return gameState; // 提供一个 getter 方法访问 gameState
    }
	// 提供一个 get 方法访问 gameElements
	public GameElements getGameElements() {
		return gameElements;
	}
	// 重置游戏(开始新游戏)
	public void resetGame(){
		gameState.reset();
		gameElements.reset();
	}
}
