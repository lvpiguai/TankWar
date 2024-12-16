import java.awt.*;
public class HomeTank extends Tank{
    public HomeTank(int x, int y, Direction dir,GameElements gameElements) {
        super(x, y, dir, gameElements);
        bloodVolume = 5;// 坦克血量为 5
    }
    private static Image[] images;
    static{
        String[]strArr = {"Images/homeTankU.png","Images/homeTankD.png","Images/homeTankL.png","Images/homeTankR.png"};
        images = new Image[strArr.length];
        for(int i = 0;i<strArr.length;i++){
            String s = strArr[i];
            images[i] = Toolkit.getDefaultToolkit().getImage(GameFrame.class.getResource(s));
        }
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
    }
    public void heal(){
        if(bloodVolume<5)setBloodVolume(bloodVolume + 1);
    }
    public void draw(Graphics g) { // 画出坦克
        g.drawImage(images[oldDirection.ordinal()], x, y, null);
    }
    public void fire() { // 发射子弹
        Bullet bullet = new Bullet(x, y, direction, true);
        int x = this.x+width/2-bullet.getWidth()/2;
        int y = this.y+length/2-bullet.getLength()/2;
        gameElements.getBullets().add(new Bullet(x,y,direction,true));
    }
}
