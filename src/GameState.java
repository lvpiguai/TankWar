import java.awt.*;
import java.util.List;

public class GameState {
    public enum GameStateType { // 游戏状态
        IN_PROGRESS, // 游戏进行中
        PAUSED,      // 游戏暂停
        WON,         // 游戏胜利
        LOST         // 游戏失败
    }
    private GameStateType currentState; // 当前游戏状态

    public GameState() {// 默认游戏状态为进行中
        this.currentState = GameStateType.IN_PROGRESS;
    }
    // // 绘制游戏状态信息到屏幕上
	public void drawState(Graphics g,GameElements gameElements) {
		Color c = g.getColor(); // 保存原本的颜色
		g.setColor(Color.green); // 设置为绿色

		Font f1 = g.getFont(); // 保存原本的字体
		Font font1 = new Font("TimesRoman", Font.BOLD, 20); // 设置新的字体
		Font font2 = new Font("TimesRoman", Font.BOLD, 30); // 设置新的字体
		// 显示地方坦克数目
		g.setFont(font1);
		g.drawString("区域内还有敌方坦克: ", 200, 70 ); 
		g.setFont(font2);
		g.drawString("" + gameElements.getTanks().size(), 400, 70);
		// 显示剩余生命值
		g.setFont(font1);
		g.drawString("剩余生命值: ", 500, 70);
		g.setFont(font2);
		g.drawString("" + gameElements.getHomeTank().getLife(), 650, 70);

		g.setFont(f1);// 恢复原本字体
		g.setColor(c); // 恢复颜色

        // 如果玩家赢了（条件是敌方坦克全灭、大本营健在、玩家坦克仍有血量）
		if (gameElements.getTanks().size() == 0 && gameElements.getHome().isLive() &&  gameElements.getHomeTank().isLive()) {
			Font f = g.getFont();
			g.setFont(new Font("TimesRoman", Font.BOLD, 60));
            gameElements.getOtherWall().clear();
			g.drawString("你赢了！ ", 310, 300);
			g.setFont(f);
		}
		// 如果玩家输了
		if (gameElements.getHomeTank().isLive() == false) {
			Font f = g.getFont();
			g.setFont(new Font("TimesRoman", Font.BOLD, 40));
			gameElements.getTanks().clear();
			gameElements.getBullets().clear();
			g.drawString("你输了！ ", 310, 300);
			g.setFont(f);
		}
	}

    // 获取当前游戏状态
    public GameStateType getCurrentState() {
        return currentState;
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
