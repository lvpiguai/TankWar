import java.awt.*;
import java.awt.event.*;

import javax.swing.JOptionPane;

public class WindowManager {
    private GameState gameState;
    private GameFrame frame;

    public WindowManager(GameFrame frame) {
        this.frame = frame;
        gameState = frame.getGameState();
    }
    // 初始化窗口设置
    public void initWindow() {
      // 使用配置文件中的窗口尺寸
      frame.setSize(Config.windowManagerConfig.windowWidth, Config.windowManagerConfig.windowHeight);
      frame.setLocationRelativeTo(null); // 让窗体居中
      frame.setTitle(Config.windowManagerConfig.windowTitle); // 使用配置文件中的窗口标题
      frame.setResizable(false); // 禁止改变窗口大小
      frame.setBackground(Config.windowManagerConfig.backgroundColor); // 使用配置文件中的背景颜色
      frame.setVisible(true); // 设置窗口可见
      frame.addWindowListener(new WindowAdapter() { // 监听窗口关闭为退出程序
          public void windowClosing(WindowEvent e) {
              System.exit(0);
          }
      });	
      frame.addKeyListener(new KeyMonitor()); // 添加键盘监听
    }
    // 创建菜单
    public void createMenu() {
        MenuBar menuBar = new MenuBar(); // 创建菜单条
        frame.setMenuBar(menuBar);// 菜单条加入窗口

        String[] menuNames = Config.windowManagerConfig.menuNames; // 从配置文件加载菜单名称
        Menu[] menus = new Menu[menuNames.length];// 根据菜单名长度分配空间
        for (int i = 0; i < menuNames.length; i++) {// 遍历菜单名
            menus[i] = new Menu(menuNames[i]);// 创建菜单
            menus[i].setFont(new Font(Config.windowManagerConfig.fontName, Config.windowManagerConfig.fontStyle, Config.windowManagerConfig.fontSize)); // 使用配置文件中的字体
            menuBar.add(menus[i]);// 加入菜单条
        }

        String[][] menuItemNames = Config.windowManagerConfig.menuItemNames; // 从配置文件加载菜单项名称
        for (int i = 0; i < menuItemNames.length; i++) {// 下标对应菜单
            for (String name : menuItemNames[i]) {  // 各个菜单的菜单项
                MenuItem item = new MenuItem(name);// 创建菜单项
                item.setFont(new Font(Config.windowManagerConfig.fontName, Config.windowManagerConfig.fontStyle, Config.windowManagerConfig.fontSize)); // 使用配置文件中的字体
                menus[i].add(item);// 加入对应菜单
                item.addActionListener(e -> handleMenuAction(e.getActionCommand())); // 添加点击监视器
            }
        }
    }
    // 处理菜单点击事件
    private void handleMenuAction(String command) {
        if(command==Config.windowManagerConfig.menuItemNames[0][0]){
            if (showConfirmationDialog(Config.windowManagerConfig.startNewGameConfirmation)) {
                frame.resetGame(); // 重置游戏
            }
        }else if(command==Config.windowManagerConfig.menuItemNames[0][1]){
            if (showConfirmationDialog(Config.windowManagerConfig.exitConfirmation)) {
                System.exit(0); // 退出游戏
            }
        }else if(command==Config.windowManagerConfig.menuItemNames[1][0]){
            gameState.setPaused(); // 暂停游戏
        }else if(command==Config.windowManagerConfig.menuItemNames[1][1]){
            gameState.setInProgress(); // 继续游戏
        }else if(command==Config.windowManagerConfig.menuItemNames[2][0]){
            gameState.setPaused(); // 暂停游戏
            JOptionPane.showMessageDialog(frame,Config.windowManagerConfig.gameInstructionsMessage, Config.windowManagerConfig.gameInstructionsTitle, JOptionPane.INFORMATION_MESSAGE);
            gameState.setInProgress(); // 游戏继续
        }else if(command==Config.windowManagerConfig.menuItemNames[3][0]){
            setGameLevel(Config.windowManagerConfig.level1.value, Config.windowManagerConfig.level1.speedX, Config.windowManagerConfig.level1.speedY, Config.windowManagerConfig.level1.bulletSpeedX, Config.windowManagerConfig.level1.bulletSpeedY);
        }else if(command==Config.windowManagerConfig.menuItemNames[3][1]){
            setGameLevel(Config.windowManagerConfig.level2.value, Config.windowManagerConfig.level2.speedX, Config.windowManagerConfig.level2.speedY, Config.windowManagerConfig.level2.bulletSpeedX, Config.windowManagerConfig.level2.bulletSpeedY);
        }else if(command==Config.windowManagerConfig.menuItemNames[3][2]){
            setGameLevel(Config.windowManagerConfig.level3.value, Config.windowManagerConfig.level3.speedX, Config.windowManagerConfig.level3.speedY, Config.windowManagerConfig.level3.bulletSpeedX, Config.windowManagerConfig.level3.bulletSpeedY);
        }else if(command==Config.windowManagerConfig.menuItemNames[3][3]){
            setGameLevel(Config.windowManagerConfig.level4.value, Config.windowManagerConfig.level4.speedX, Config.windowManagerConfig.level4.speedY, Config.windowManagerConfig.level4.bulletSpeedX, Config.windowManagerConfig.level4.bulletSpeedY);
        }
    }
    // 设置游戏级别
    private void setGameLevel(int level, int tankSpeedX, int tankSpeedY, int bulletSpeedX, int bulletSpeedY){
        gameState.setGameLevel(level);
        Tank.speedX = tankSpeedX;
        Tank.speedY = tankSpeedY;
        Bullet.speedX = bulletSpeedX;
        Bullet.speedY = bulletSpeedY;
        frame.resetGame();
    }

    // 显示确认对话框
    private boolean showConfirmationDialog(String message) {
        Object[] options = { "确定", "取消" };
        int response = JOptionPane.showOptionDialog(frame, message, "", JOptionPane.YES_OPTION,
                JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
        return response == 0;
    }

    // 键盘监听
    private class KeyMonitor extends KeyAdapter {
        private boolean bR = false, bL = false, bU = false, bD = false;// 键盘监听
        HomeTank homeTank; // 获取己方坦克
		public void keyReleased(KeyEvent e) { // 监听键盘释放
            homeTank = frame.getGameElements().getHomeTank();
            int key = e.getKeyCode();
            // System.out.println("Key released: " + KeyEvent.getKeyText(key));
            switch (key) {
                case KeyEvent.VK_J: // 按键释放后才开火
                    homeTank.fire();
                    break;
                case KeyEvent.VK_D:
                    bR = false;
                    break;
                case KeyEvent.VK_A:
                    bL = false;
                    break;
                case KeyEvent.VK_W:
                    bU = false;
                    break;
                case KeyEvent.VK_S:
                    bD = false;
                    break;
            }
            decideDirection();
		}

		public void keyPressed(KeyEvent e) { // 监听键盘按下
            homeTank = frame.getGameElements().getHomeTank();
			int key = e.getKeyCode();
           // System.out.println("Key pressed: " + KeyEvent.getKeyText(key));  // 调试输出
            switch (key) {
                case KeyEvent.VK_R: // 当按下R时，重新开始游戏
                    frame.resetGame(); //重置游戏
                    break;
                    case KeyEvent.VK_D: // 监听向右键
                    bR = true;
                    break;
                case KeyEvent.VK_A:// 监听向左键
                    bL = true;
                    break;
                case KeyEvent.VK_W: // 监听向上键
                    bU = true;
                    break;
                case KeyEvent.VK_S:// 监听向下键
                    bD = true;
                    break;
                default:
                    break;
            }
            decideDirection();
		}
         // 决定移动得方向
        private void decideDirection() {
            if (!bL && !bU && bR && !bD)homeTank.setDirection(Direction.R);
            else if (bL && !bU && !bR && !bD)homeTank.setDirection(Direction.L);
            else if (!bL && bU && !bR && !bD) homeTank.setDirection(Direction.U);
            else if (!bL && !bU && !bR && bD) homeTank.setDirection(Direction.D);
            else if (!bL && !bU && !bR && !bD)homeTank.setDirection(Direction.STOP);
        }
	}
}
