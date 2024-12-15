import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

/**
 * 树（丛林）类
 */

public class Tree extends GameObject {
	private static Toolkit tk = Toolkit.getDefaultToolkit();
	private static Image[] treeImags = null;
	static {
		treeImags = new Image[] { tk.getImage(BrickWall.class.getResource("Images/tree.gif")), };
	}

	// 构造函数
	public Tree(int x, int y) {
		super(x, y);
		width = length = 30;
	}

	public void draw(Graphics g) { // 画出树
		g.drawImage(treeImags[0], x, y, null);
	}
}
