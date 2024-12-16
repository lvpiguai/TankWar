import java.awt.*;
import java.util.Random;

public class EnemyTank extends Tank{
    private static Random r = new Random();// 随机数
    private int steps = r.nextInt(10)+5;// 移动步数
    private static Image[] images = null;
    static { // 初始化坦克图片
        String[] strArr = {"Images/tankD.gif","Images/tankU.gif","Images/tankL.gif","Images/tankR.gif"};
        images = new Image[strArr.length];
        for (int i = 0; i < strArr.length; i++) {
            images[i] = Toolkit.getDefaultToolkit().getImage(GameFrame.class.getResource(strArr[i]));
        }
    }
    
    public EnemyTank(int x, int y,Direction dir,GameElements gameElements) {
        super(x, y, dir, gameElements);
        bloodVolume = 1; // 坦克血量为 1
    }
    public void move(){
        oldX = x;   // 保存移动前的坐标
        oldY = y;
        switch (direction) {    // 根据方向移动
            case L: x-=speedX;break;
            case U: y-=speedY;break;
            case R: x+=speedX;break;
            case D: y+=speedY;break;
            case STOP: break;
        }
        if (this.direction != Direction.STOP) {// 保存移动前的方向
            this.oldDirection = this.direction;
        }
        if(steps==0){ //步数走完了
            steps = r.nextInt(10)+5; //随机一个步数
            direction = Direction.values()[r.nextInt(5)]; //随机选择一个方向
        }
        steps--; //每次走一步
    }
    public void draw(Graphics g) { // 画出敌方坦克
        g.drawImage(images[oldDirection.ordinal()], x, y, null);
    }
    public void fire() { // 发射子弹
        Bullet bullet = new Bullet(x, y, direction, false);
        int x = this.x+width/2-bullet.getWidth()/2;
        int y = this.y+length/2-bullet.getLength()/2;
        gameElements.getBullets().add(new Bullet(x,y,direction,false));
    }
}
