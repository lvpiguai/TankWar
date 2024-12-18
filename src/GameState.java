import java.awt.*;

public class GameState {
    public enum GameStateType { // 游戏状态
        IN_PROGRESS, // 游戏进行中
        PAUSED,      // 游戏暂停
        WON,         // 游戏胜利
        LOST         // 游戏失败
    }
    private GameStateType currentState; // 当前游戏状态
    private int gameLevel;
    private long startTime; // 游戏开始时间
    public GameState() {// 默认游戏状态为进行中
        this.currentState = GameStateType.IN_PROGRESS;
        startTime = System.currentTimeMillis();
        gameLevel = Config.gameStateConfig.initGameLevel;
    }
    // 绘制游戏状态信息到屏幕上
	public void drawState(Graphics g,GameElements gameElements) {
		Color c = g.getColor(); // 保存原本的颜色
		g.setColor(Color.GREEN); // 设置为绿色

		Font f1 = g.getFont(); // 保存原本的字体
		Font font1 = new Font(Config.gameStateConfig.fontName, Config.gameStateConfig.fontStyle, Config.gameStateConfig.fontSize); // 使用配置中的字体
        g.setFont(font1);
        //显示时间
        long elapsedTime = (System.currentTimeMillis() - startTime) / 1000; // 计算经过的秒数 
        g.drawString(Config.gameStateConfig.timeLabel + elapsedTime + " 秒",Config.gameStateConfig.timeTextX, Config.gameStateConfig.timeTextY); // 在屏幕上显示时间
		// 显示地方坦克数目
        g.drawString(Config.gameStateConfig.enemyTankCountLabel, Config.gameStateConfig.enemyTankCountTextX, Config.gameStateConfig.enemyTankCountTextY);
        g.drawString("" + gameElements.getEnemyTanks().size(), Config.gameStateConfig.enemyTankCountTextX + 200, Config.gameStateConfig.enemyTankCountTextY);
		// 显示剩余生命值
		g.drawString(Config.gameStateConfig.homeTankHealthLabel, Config.gameStateConfig.homeTankHealthTextX, Config.gameStateConfig.homeTankHealthTextY);
        g.drawString("" + gameElements.getHomeTank().getBloodVolume(), Config.gameStateConfig.homeTankHealthTextX + 120, Config.gameStateConfig.homeTankHealthTextY);
        
        Font font2 = new Font(Config.gameStateConfig.fontName, Config.gameStateConfig.fontStyle, Config.gameStateConfig.fontSize2); // 使用配置中的字体
        g.setFont(font2);
        // 如果玩家赢了（条件是敌方坦克全灭、大本营健在、玩家坦克仍有血量）
		if (isWon()) {
            g.clearRect(0, 0, GameFrame.Fram_width, GameFrame.Fram_length);// 清空屏幕
            g.setColor(Color.GRAY); //用灰色颜色填充
            g.fillRect(0, 0, GameFrame.Fram_width, GameFrame.Fram_length); // 用灰色背景填充
            g.setColor(Color.GREEN);
			g.drawString(Config.gameStateConfig.winMessage, Config.gameStateConfig.winMessageX, Config.gameStateConfig.winMessageY);
		}
		// 如果玩家输了
		if (isLost()) {
            g.clearRect(0, 0, GameFrame.Fram_width, GameFrame.Fram_length);// 清空屏幕
            g.setColor(Color.GRAY); //用灰色颜色填充
            g.fillRect(0, 0, GameFrame.Fram_width, GameFrame.Fram_length); // 用灰色背景填充
            g.setColor(Color.GREEN);
			g.drawString(Config.gameStateConfig.loseMessage, Config.gameStateConfig.loseMessageX, Config.gameStateConfig.loseMessageY);
		}
        g.setFont(f1);// 恢复原本字体
		g.setColor(c); // 恢复颜色
	}

    // 获取当前游戏状态
    public GameStateType getCurrentState() {
        return currentState;
    }
    // 重置游戏状态和游戏开始时间
    public void reset() {
        this.currentState = GameStateType.IN_PROGRESS;
        startTime = System.currentTimeMillis();
    }
    // 获取当前游戏级别
    public int getGameLevel() {
        return gameLevel;
    }
    // 设置游戏级别
    public void setGameLevel(int level) {
        this.gameLevel = level;
    }

    // 设置游戏状态为进行中
    public void setInProgress() {
        this.currentState = GameStateType.IN_PROGRESS;
    }

    // 设置游戏状态为暂停
    public void setPaused() {
        this.currentState = GameStateType.PAUSED;
    }

    // 设置游戏状态为胜利
    public void setWon() {
        this.currentState = GameStateType.WON;
    }

    // 设置游戏状态为失败
    public void setLost() {
        this.currentState = GameStateType.LOST;
    }

    // 切换暂停状态
    public void togglePause() {
        if (currentState == GameStateType.PAUSED)setInProgress();
        else setPaused();
    }

    // 判断游戏是否处于进行中状态
    public boolean isInProgress() {
        return currentState == GameStateType.IN_PROGRESS;
    }

    // 判断游戏是否处于暂停状态
    public boolean isPaused() {
        return currentState == GameStateType.PAUSED;
    }

    // 判断游戏是否胜利
    public boolean isWon() {
        return currentState == GameStateType.WON;
    }

    // 判断游戏是否失败
    public boolean isLost() {
        return currentState == GameStateType.LOST;
    }
}
