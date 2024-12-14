import java.awt.*;
/*
 * 抽象游戏元素对象类
 */
public abstract class GameObject {
    private static int width,length;// 游戏元素的长宽
    private int x,y; // x,y坐标
    private boolean live = true;// 是否存活

    // 构造函数(给出坐标)
    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }
    public abstract void draw(Graphics g);// 画出游戏元素
    // 判断元素是否存活
    boolean isLive() {
        return live;
    }
    // 设置元素死亡
    void setDead() {
        this.live = false;
    }
    // 获取元素的矩形边界，用于碰撞检测
    public Rectangle getBounds() {
        return new Rectangle(x, y, width,length);
    }
    //碰撞检测
    public boolean isCollision(GameObject obj) {
        return this.getBounds().intersects(obj.getBounds());
    }
}
