import java.awt.*;
import java.awt.event.*;

import javax.swing.JOptionPane;

public class WindowManager {
    private GameState gameState;
    private GameFrame frame = new GameFrame();

    public WindowManager(GameFrame frame) {
        this.frame = frame;
        gameState = frame.getGameState();
    }
    // 初始化窗口设置
    public void initWindow() {
        frame.setSize(GameFrame.Fram_width, GameFrame.Fram_length);// 设置界面大小
        frame.setLocationRelativeTo(null);// 让窗体居中
        frame.setTitle("坦克大战——(重新开始：R键  开火：F键)");// 设置窗口标题
        frame.setResizable(false);// 禁止改变窗口大小
        frame.setBackground(Color.GREEN);// 设置窗口颜色
        frame.setVisible(true);// 设置窗口可见
        frame.addWindowListener(new WindowAdapter() { // 监听窗口关闭为退出程序
			public void windowClosing(WindowEvent e) {
				System.exit(0);
			}
		});	
        frame.addKeyListener(new KeyMonitor());//添加键盘监听
    }
    // 创建菜单
    public void createMenu() {
        MenuBar menuBar = new MenuBar(); // 创建菜单条
        frame.setMenuBar(menuBar);// 菜单条加入窗口

        String[] menuNames = { "游戏", "暂停/继续", "帮助", "游戏级别" };// 菜单名
        Menu[] menus = new Menu[menuNames.length];// 根据菜单名长度分配空间
        for (int i = 0; i < menuNames.length; i++) {// 遍历菜单名
            menus[i] = new Menu(menuNames[i]);// 创建菜单
            menus[i].setFont(new Font("TimesRoman", Font.BOLD, 15));// 设置字体
            menuBar.add(menus[i]);// 加入菜单条
        }

        String[][] menuItemNames = {// 菜单项名
            {"开始新游戏", "退出"},
            {"暂停", "继续"},
            {"游戏说明"},
            {"级别1", "级别2", "级别3", "级别4"}
        };
        for (int i = 0; i < menuItemNames.length; i++) {// 下标对应菜单
            for (String name : menuItemNames[i]) {  // 各个菜单的菜单项
                MenuItem item = new MenuItem(name);// 创建菜单项
                item.setFont(new Font("TimesRoman", Font.BOLD, 15));// 设置字体
                menus[i].add(item);// 加入对应菜单
                item.addActionListener(e -> handleMenuAction(e.getActionCommand())); // 添加点击监视器
            }
        }

        frame.setMenuBar(menuBar);
    }
    // 处理菜单点击事件
    private void handleMenuAction(String command) {
        switch (command) {
            case "开始新游戏":
                if (showConfirmationDialog("您确认要开始新游戏！")) {
                    frame.resetGame(); // 重置游戏
                }
                break;
            case "暂停":
            case "继续":
                gameState.togglePause(); // 切换暂停状态
                break;
            case "退出":
                gameState.setPaused(); // 暂停游戏
                if (showConfirmationDialog("您确认要退出吗")) {
                    System.exit(0); // 退出游戏
                } else {
                    gameState.setInProgress(); // 继续游戏
                }
                break;
            case "游戏说明":
                gameState.setPaused(); // 暂停游戏
                JOptionPane.showMessageDialog(frame, "用→ ← ↑ ↓控制方向，F键盘发射，R重新开始！", "提示！", JOptionPane.INFORMATION_MESSAGE);
                gameState.setInProgress(); // 游戏继续
                break;
            case "级别1":
                setGameLevel(1, 6, 6, 10, 10);
                break;
            case "级别2":
                setGameLevel(2, 10, 10, 12, 12);
                break;
            case "级别3":
                setGameLevel(3, 14, 14, 16, 16);
                break;
            case "级别4":
                setGameLevel(4, 16, 16, 18, 18);
                break;

            default:
                break;
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
        boolean bR = false, bL = false, bU = false, bD = false;// 键盘监听
        Tank homeTank = frame.getGameElements().getHomeTank(); // 获取己方坦克
		public void keyReleased(KeyEvent e) { // 监听键盘释放
            int key = e.getKeyCode();
            switch (key) {
                case KeyEvent.VK_F: // 按键释放后才开火
                    homeTank.fire();
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
            decideDirection();
		}

		public void keyPressed(KeyEvent e) { // 监听键盘按下
			int key = e.getKeyCode();
            switch (key) {
                case KeyEvent.VK_R: // 当按下R时，重新开始游戏
                    frame.resetGame(); //重置游戏
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
