
public interface Movable {
    void move(); // 移动
    void restorePosition();// 恢复原本位置
    boolean collide(GameObject obj);// 碰撞检测
    boolean isOutOfBounds();// 是否超出边界
}
