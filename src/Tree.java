import java.awt.Graphics;
import java.awt.Image;
import java.awt.Toolkit;

/**
 * 树（丛林）类
 */

public class Tree extends GameObject {
	private static Image[] treeImags = null;
	static {
		 // 使用配置文件中的图像路径
		 treeImags = new Image[] { Toolkit.getDefaultToolkit().getImage(Tree.class.getResource(Config.treeConfig.imagePath)) };
	}

	// 构造函数
	public Tree(int x, int y) {
		super(x, y);
		width = Config.treeConfig.width;      // 使用配置文件中的宽度
        length = Config.treeConfig.length;    // 使用配置文件中的长度
	}

	public void draw(Graphics g) { // 画出树
		g.drawImage(treeImags[0], x, y, null);
	}
}
