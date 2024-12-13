import java.awt.*;
import java.awt.event.*;
import java.util.*;

import javax.swing.plaf.basic.BasicSliderUI.TrackListener;

/**
 * ̹���ࣨ���õз�̹�˺����̹�ˣ�
 */

public class Tank {
	public static int speedX = 6, speedY = 6; // ̹�˵��ƶ��ٶ�
	public static int count = 0;	// ̹�˵�����
	public static final int width = 35, length = 35; // ̹�˵�ȫ�ִ�С�����в��ɸı���
	private Direction direction = Direction.STOP; // ��ʼ��״̬Ϊ��ֹ
	private Direction Kdirection = Direction.U; //  ��¼���Ʒ���
	GameFrame tc;

	private boolean good;// trueΪ����̹�ˣ�falseΪ�з�̹��
	private int x, y;// ̹�˵����� ����
	private int oldX, oldY;// ̹���ƶ�ǰ������
	private boolean live = true; // ��ʼ��Ϊ����
	private int life = 200; // ��ʼ����ֵ

	private static Random r = new Random(); //���������
	private int step = r.nextInt(10) + 5; // ����һ�������,���ģ��̹�˵��ƶ�·��

	private boolean bL = false, bU = false, bR = false, bD = false; // ����״̬

	private static Toolkit tk = Toolkit.getDefaultToolkit();// �������
	private static Image[] tankImags = null; // ̹��ͼƬ����
	static {
		tankImags = new Image[] { tk.getImage(BombTank.class.getResource("Images/tankD.gif")),
				tk.getImage(BombTank.class.getResource("Images/tankU.gif")),
				tk.getImage(BombTank.class.getResource("Images/tankL.gif")),
				tk.getImage(BombTank.class.getResource("Images/tankR.gif")), };
	}

	public Tank(int x, int y, boolean good) {// Tank�Ĺ��캯��1
		this.x = x;
		this.y = y;
		this.oldX = x;
		this.oldY = y;
		this.good = good;
	}

	public Tank(int x, int y, boolean good, Direction dir, GameFrame tc) {// Tank�Ĺ��캯��2
		this(x, y, good);
		this.direction = dir;
		this.tc = tc;
	}

	// ����̹��
	public void draw(Graphics g) {
		if (!live) {// ���̹������
			if (!good) {//
				tc.getGameElements().getTanks().remove(this); // ɾ����Ч��
			}
			return;
		}

		if (good)// ����Ǽ���̹��
			new DrawBloodbBar().draw(g); // ���̹�˵�Ѫ����

		switch (Kdirection) {
		// ���ݷ���ѡ��̹�˵�ͼƬ
			case D:
				g.drawImage(tankImags[0], x, y, null);
				break;

			case U:
				g.drawImage(tankImags[1], x, y, null);
				break;
			case L:
				g.drawImage(tankImags[2], x, y, null);
				break;

			case R:
			g.drawImage(tankImags[3], x, y, null);
			break;

		}
		move(); // ����move����
	}

	// ̹���ƶ�
	void move() {

		// ��¼�ƶ�ǰ������
		this.oldX = x;
		this.oldY = y;

		//���ݷ����ƶ���Ӧ����
		switch (direction) { // ѡ���ƶ�����
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

		//��¼����ͼ��÷���
		if (this.direction != Direction.STOP) { //����ͣ���ƶ�
			this.Kdirection = this.direction;
		}

		//��ֹԽ�磬����������ָ����߽�
		if (x < 0)x = 0;
		if (y < 40) y = 40;
		if (x + Tank.width > GameFrame.Fram_width)x = GameFrame.Fram_width - Tank.width;
		if (y + Tank.length > GameFrame.Fram_length)y = GameFrame.Fram_length - Tank.length;

		//�з�̹�ˣ����·��
		if (!good) {
			Direction[] directons = Direction.values();//��ȡ���з����ֵ
			if (step == 0) { //�����ƶ����
				step = r.nextInt(12) + 3; // �������
				int rn = r.nextInt(directons.length);
				direction = directons[rn]; // �����������
			}
			step--;//ÿ���ƶ�һ��

			//5% �ĸ��� �����ӵ�
			if (r.nextInt(40) > 38)this.fire();
		}
	}

	// �ָ��ƶ�ǰ������
	private void changToOldDir() {
		x = oldX;
		y = oldY;
	}

	// ��������
	public void keyPressed(KeyEvent e) { // ���ܼ����¼�
		int key = e.getKeyCode(); // ��ȡ��
		switch (key) {
			case KeyEvent.VK_R: // ������Rʱ�����¿�ʼ��Ϸ
			tc.getGameElements().clearAllElements();
				tc.getGameElements().getHomeTank().setLive(true);
				if (tc.getGameElements().getTanks().size() == 0) { // ����������û��̹��ʱ���ͳ���̹��
					for (int i = 0; i < 20; i++) {
						if (i < 9) // ����̹�˳��ֵ�λ��
							tc.getGameElements().getTanks().add(new Tank(150 + 70 * i, 40, false, Direction.R, tc));
						else if (i < 15)
							tc.getGameElements().getTanks().add(new Tank(700, 140 + 50 * (i - 6), false, Direction.D, tc));
						else
							tc.getGameElements().getTanks().add(new Tank(10, 50 * (i - 12), false, Direction.L, tc));
					}
				}
				Tank homeTank = tc.getGameElements().getHomeTank();
				homeTank= new Tank(300, 560, true, Direction.STOP, tc);// �����Լ����ֵ�λ��
				if (!tc.getGameElements().getHome().isLive()) // ��home��������
					tc.getGameElements().getHome().setLive(true);
				new GameFrame(); // ���´������
				break;
			case KeyEvent.VK_RIGHT: // �������Ҽ�
				bR = true;
				break;

			case KeyEvent.VK_LEFT:// ���������
				bL = true;
				break;

			case KeyEvent.VK_UP: // �������ϼ�
				bU = true;
				break;

			case KeyEvent.VK_DOWN:// �������¼�
			bD = true;
			break;
		}
		decideDirection();// ���ú���ȷ���ƶ�����
	}

	//�����ƶ��÷���
	void decideDirection() {
		if (!bL && !bU && bR && !bD) // �����ƶ�
			direction = Direction.R;

		else if (bL && !bU && !bR && !bD) // ������
			direction = Direction.L;

		else if (!bL && bU && !bR && !bD) // �����ƶ�
			direction = Direction.U;

		else if (!bL && !bU && !bR && bD) // �����ƶ�
			direction = Direction.D;

		else if (!bL && !bU && !bR && !bD)
			direction = Direction.STOP; // û�а������ͱ��ֲ���
	}

	// �����ͷ�
	public void keyReleased(KeyEvent e) { // �����ͷż���
		int key = e.getKeyCode();
		switch (key) {
			case KeyEvent.VK_F: //�����ͷź�ſ���
				fire();break;
			case KeyEvent.VK_RIGHT:
				bR = false;break;

			case KeyEvent.VK_LEFT:
				bL = false;break;

			case KeyEvent.VK_UP:
				bU = false;break;

			case KeyEvent.VK_DOWN:
				bD = false;break;

		}
		decideDirection(); // �ͷż��̺�ȷ���ƶ�����
	}

	// �����ӵ�
	public Bullets fire() { // ���𷽷�
		if (!live)// ���̹������
			return null;
		int x = this.x + Tank.width / 2 - Bullets.width / 2; // ����λ��
		int y = this.y + Tank.length / 2 - Bullets.length / 2;
		Bullets m = new Bullets(x, y + 2, good, Kdirection, this.tc); // û�и�������ʱ����ԭ���ķ��򷢻�
		tc.getGameElements().getBullets().add(m);// ����ӵ�
		return m;
	}

	//
	public Rectangle getRect() {
		return new Rectangle(x, y, width, length);
	}

	public boolean isLive() {
		return live;
	}

	public void setLive(boolean live) {
		this.live = live;
	}

	public boolean isGood() {
		return good;
	}

	
	public boolean collideWithWall(BrickWall w) { // ��ײ����ͨǽʱ
		if (this.live && this.getRect().intersects(w.getRect())) {
			this.changToOldDir(); // ת����ԭ���ķ�����ȥ
			return true;
		}
		return false;
	}

	public boolean collideWithWall(MetalWall w) { // ײ������ǽ
		if (this.live && this.getRect().intersects(w.getRect())) {
			this.changToOldDir();
			return true;
		}
		return false;
	}

	public boolean collideRiver(River r) { // ײ��������ʱ��
		if (this.live && this.getRect().intersects(r.getRect())) {
			this.changToOldDir();
			return true;
		}
		return false;
	}

	public boolean collideHome(Home h) { // ײ���ҵ�ʱ��
		if (this.live && this.getRect().intersects(h.getRect())) {
			this.changToOldDir();
			return true;
		}
		return false;
	}

	public boolean collideWithTanks(java.util.List<Tank> tanks) {// ײ��̹��ʱ
		for (int i = 0; i < tanks.size(); i++) {
			Tank t = tanks.get(i);
			if (this != t) {
				if (this.live && t.isLive() && this.getRect().intersects(t.getRect())) {
					this.changToOldDir();
					t.changToOldDir();
					return true;
				}
			}
		}
		return false;
	}

	public int getLife() {
		return life;
	}

	public void setLife(int life) {
		this.life = life;
	}

	//Ѫ����-�ڲ���
	private class DrawBloodbBar {
		public void draw(Graphics g) {
			Color c = g.getColor();//������ɫ
			g.setColor(Color.RED);//����Ϊ��ɫ
			g.drawRect(375, 585, width, 10);// ����Ѫ����
			int w = width * life / 200; // ����Ѫ��
			g.fillRect(375, 585, w, 10);// ��ʾ���̹�˵�Ѫ����
			g.setColor(c); //�ָ�������ɫ
		}
	}

	//��Ѫ��
	public boolean eat(Blood b) {
		if (this.live && b.isLive() && this.getRect().intersects(b.getRect())) {
			if (this.life <= 100)
				this.life = this.life + 100; // ÿ��һ��������100������
			else
				this.life = 200;
			b.setLive(false);
			return true;
		}
		return false;
	}

	public int getX() {
		return x;
	}

	public int getY() {
		return y;
	}
}