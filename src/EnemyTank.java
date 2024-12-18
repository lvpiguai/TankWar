import java.awt.*;
import java.util.Random;

public class EnemyTank extends Tank{
    private static Random r = new Random();// 随机数
    private int steps;// 移动步数
    private static Image[] images = null;
    static { // 初始化坦克图片
        images = new Image[Config.enemyTankConfig.imagePaths.length];
        for (int i = 0; i < Config.enemyTankConfig.imagePaths.length; i++) {
            images[i] = Toolkit.getDefaultToolkit().getImage(GameFrame.class.getResource(Config.enemyTankConfig.imagePaths[i]));
        }
    }
    
    public EnemyTank(int x, int y,Direction dir,GameElements gameElements) {
        super(x, y, dir, gameElements);
        bloodVolume = Config.enemyTankConfig.bloodVolume; // 坦克血量
        width = Config.enemyTankConfig.width; // 坦克的大小
        length = Config.enemyTankConfig.length;
        steps = r.nextInt(Config.enemyTankConfig.maxSteps - Config.enemyTankConfig.minSteps + 1) + Config.enemyTankConfig.minSteps; // 移动步数
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
            steps = r.nextInt(Config.enemyTankConfig.maxSteps - Config.enemyTankConfig.minSteps + 1) + Config.enemyTankConfig.minSteps; // 随机一个步数
            direction = Direction.values()[r.nextInt(4)]; //随机选择一个方向(不能停下)
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
