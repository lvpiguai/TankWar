import java.awt.*;

public class HomeTank extends Tank {
    public HomeTank(int x, int y, Direction dir, GameElements gameElements) {
        super(x, y, dir, gameElements);
        bloodVolume = Config.homeTankConfig.bloodVolume; // 使用配置文件中的血量
        width = length = Config.homeTankConfig.width; // 使用配置文件中的宽度和长度
    }

    private static Image[] images = null;
    static {
        String[] strArr = Config.homeTankConfig.imagePaths; // 使用配置文件中的图像路径数组
        images = new Image[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            images[i] = Toolkit.getDefaultToolkit().getImage(GameFrame.class.getResource(strArr[i]));
        }
    }

    public void move() {
        oldX = x; // 保存移动前的坐标
        oldY = y;
        switch (direction) { // 根据方向移动
            case L:
                x -= speedX;
                break;
            case U:
                y -= speedY;
                break;
            case R:
                x += speedX;
                break;
            case D:
                y += speedY;
                break;
            case STOP:
                break;
        }
        if (this.direction != Direction.STOP) {// 保存移动前的方向
            this.oldDirection = this.direction;
        }
    }

    public void heal() {
        if (bloodVolume < Config.homeTankConfig.bloodVolume)
            setBloodVolume(bloodVolume + 1);
    }

    public void draw(Graphics g) { // 画出坦克 及血条
        g.drawImage(images[oldDirection.ordinal()], x, y, null);
        Color c = g.getColor();
        g.setColor(Color.RED);
        // 使用配置文件中的血条位置和大小
        g.drawRect(Config.homeTankConfig.healthBarX, Config.homeTankConfig.healthBarY, width, Config.homeTankConfig.healthBarHeight);
        int w = width * bloodVolume / 10;
        g.fillRect(Config.homeTankConfig.healthBarX, Config.homeTankConfig.healthBarY, w, Config.homeTankConfig.healthBarHeight);
        g.setColor(c);
    }

    public void fire() { // 发射子弹
        Bullet bullet = new Bullet(x, y, direction, true);
        int x = this.x + width / 2 - bullet.getWidth() / 2;
        int y = this.y + length / 2 - bullet.getLength() / 2;
        gameElements.getBullets().add(new Bullet(x, y+2, oldDirection, true));
    }
}
