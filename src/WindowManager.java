import java.awt.*;
import java.awt.event.*;

import javax.swing.JOptionPane;

public class WindowManager {
    private GameFrame frame;

    public WindowManager(GameFrame gameFrame) {
        this.frame = gameFrame;
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
        GameState gameState = frame.getGameState();

        if (command.equals("开始新游戏")) {
            Object[] options = { "确定", "取消" };
            int response = JOptionPane.showOptionDialog(frame, "您确认要开始新游戏！", "", JOptionPane.YES_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (response == 0) {
                // 设置游戏状态为进行中
                gameState.setInProgress();
                frame.dispose();
                frame = new GameFrame(); // 创建新游戏窗口
            } else {
                gameState.setInProgress();  // 保持游戏状态为进行中
                frame.startRenderingThread();  // 启动绘制线程
            }

        } else if (command.equals("暂停")) { // 暂停
            gameState.setPaused();  // 设置游戏为暂停状态
        } else if (command.equals("继续")) { // 继续
            if (gameState.isPaused()) {
                gameState.setInProgress();  // 设置游戏为进行中
                frame.startRenderingThread();  // 启动绘制线程
            }
        } else if (command.equals("退出")) { // 退出
            gameState.setPaused();  // 暂停游戏
            Object[] options = { "确定", "取消" };
            int response = JOptionPane.showOptionDialog(frame, "您确认要退出吗", "", JOptionPane.YES_OPTION,
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (response == 0) {
                System.exit(0);
            } else {
                gameState.setInProgress();  // 继续游戏
                frame.startRenderingThread();  // 启动绘制线程
            }
        } else if (command.equals("游戏说明")) {
            gameState.setPaused();  // 暂停游戏
            JOptionPane.showMessageDialog(frame, "用→ ← ↑ ↓控制方向，F键盘发射，R重新开始！", "提示！", JOptionPane.INFORMATION_MESSAGE);
            gameState.setInProgress();  // 游戏继续
            frame.startRenderingThread();  // 启动绘制线程
        } else if (command.equals("级别1")) {
            Tank.count = 12;
            Tank.speedX = 6;
            Tank.speedY = 6;
            Bullet.speedX = 10;
            Bullet.speedY = 10;
            frame.dispose();
            new GameFrame();
        } else if (command.equals("级别2")) {
            Tank.count = 12;
            Tank.speedX = 10;
            Tank.speedY = 10;
            Bullet.speedX = 12;
            Bullet.speedY = 12;
            frame.dispose();
            new GameFrame();
        } else if (command.equals("级别3")) {
            Tank.count = 20;
            Tank.speedX = 14;
            Tank.speedY = 14;
            Bullet.speedX = 16;
            Bullet.speedY = 16;
            frame.dispose();
            new GameFrame();
        } else if (command.equals("级别4")) {
            Tank.count = 20;
            Tank.speedX = 16;
            Tank.speedY = 16;
            Bullet.speedX = 18;
            Bullet.speedY = 18;
            frame.dispose();
            new GameFrame();
        }
    }
    // 键盘监听
    private class KeyMonitor extends KeyAdapter {

		public void keyReleased(KeyEvent e) { // 监听键盘释放
			frame.getGameElements().getHomeTank().keyReleased(e);
		}

		public void keyPressed(KeyEvent e) { // 监听键盘按下
			int key = e.getKeyCode();
            switch (key) {
                case KeyEvent.VK_R: // 当按下R时，重新开始游戏
                    frame.resetGame(); //重置游戏
                    break;
                
                default:
                    break;
            }
		}
	}
}
