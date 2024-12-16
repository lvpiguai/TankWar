
public interface Movable {
    void move(); // 移动
    boolean collide(GameObject obj);// 碰撞检测
    boolean isOutOfBounds();// 是否超出边界
}
