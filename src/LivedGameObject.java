abstract public class LivedGameObject extends GameObject{
    protected int bloodVolume; // 血量

    // 构造函数
    public LivedGameObject(int x, int y) {
        super(x, y);
    }
    // 判断元素是否存活
    public boolean isLive() {
        return bloodVolume > 0;
    }
    // 获取元素血量
    public int getBloodVolume() {
        return bloodVolume;
    }
    // 设置元素血量
    public void setBloodVolume(int bloodVolume) {
        this.bloodVolume = bloodVolume;
    }
}
