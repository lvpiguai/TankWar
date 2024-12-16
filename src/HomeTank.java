import java.awt.*;

public class HomeTank extends Tank {
    public HomeTank(int x, int y, Direction dir, GameElements gameElements) {
        super(x, y, dir, gameElements);
        bloodVolume = 10;// 坦克血量    
        width = length = 60;// 坦克的大小
    }

    private static Image[] images = null;
    static {
        String[] strArr = { "Images/homeTankU.gif", "Images/homeTankD.gif", "Images/homeTankL.gif", "Images/homeTankR.gif" };
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
        if (bloodVolume < 5)
            setBloodVolume(bloodVolume + 1);
    }

    public void draw(Graphics g) { // 画出坦克 及血条
        g.drawImage(images[oldDirection.ordinal()], x, y, null);
        Color c = g.getColor();
        g.setColor(Color.RED);
        g.drawRect(375, 585, width, 10);// 显示玩家坦克的血量条
        int w = width * bloodVolume / 10;
        g.fillRect(375, 585, w, 10);// 显示玩家坦克的血量条
        g.setColor(c);
    }

    public void fire() { // 发射子弹
        Bullet bullet = new Bullet(x, y, direction, true);
        int x = this.x + width / 2 - bullet.getWidth() / 2;
        int y = this.y + length / 2 - bullet.getLength() / 2;
        gameElements.getBullets().add(new Bullet(x, y+2, oldDirection, true));
    }
}
