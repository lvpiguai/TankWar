import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class GameElements {
    private GameState gameState;
    private HomeTank homeTank = null;// 己方坦克
    private Home home = null;// 实例化home
    private List<EnemyTank> enemyTanks = new ArrayList<>();
    private List<Bullet> bullets = new ArrayList<>();
    private List<BrickWall> brickWalls = new ArrayList<>();
    private List<MetalWall> metalWalls = new ArrayList<>();
    private List<River> rivers = new ArrayList<>();
    private List<Tree> trees = new ArrayList<>();
	private List<BombTank>bombTanks = new ArrayList<>();
	private List<Blood> bloods = new ArrayList<>();

    // 构造函数
    public GameElements(GameState gameState) {
		this.gameState = gameState;
    }

    // 初始化游戏元素
	public void initGameElements() {
		createBrickWall();//创建普通墙
		createMetalWalls(); // 创建 metalWalls
		createTrees(); // 创建 trees
		createEnemyTanks(); // 创建 enemyTanks
		rivers.add(new River(85, 100)); // 创建河流
		homeTank = new HomeTank(280, 520, Direction.STOP,this);// 己方坦克
        home = new Home(373, 545);// 实例化home
		bloods.add(new Blood(155,196));
	}
    // 绘制所有元素
    public void drawElements(Graphics g) {
		home.draw(g); // 画出home
		homeTank.draw(g); // 画出自己家的坦克
        for(Blood b:bloods){//血包
			b.draw(g);
		}
		for(BombTank bt:bombTanks){//爆炸
			bt.draw(g);
		}
		for(Tree t:trees){//树
			t.draw(g);
		}
		for(EnemyTank t:enemyTanks){//敌方坦克
			t.draw(g);
		}
		for(Bullet b:bullets){//子弹
			b.draw(g);
		}
		for(BrickWall w:brickWalls){//砖墙
			w.draw(g);
		}
		for(MetalWall w:metalWalls){//金属墙
			w.draw(g);
		}
		for(River r:rivers){//河流
			r.draw(g);
		}
    }
	//重置所有元素
	public void reset(){
		clearAllElements();// 清空
		initGameElements();//重新初始化
	}
    // 检查元素间的碰撞
    private void checkCollisions() {
		bulletsCollisions();//子弹碰撞
		enemyTanksCollisions();//敌方坦克碰撞
		homeTankCollisions();//己方坦克碰撞
    }	
	//子弹碰撞检测
	private void bulletsCollisions() {
		for(Bullet b:bullets){ //先检测子弹的碰撞
			if(b.isGood()){ //己方子弹
				for(EnemyTank et:enemyTanks){ //敌方坦克
					if(b.collide(et)){
						et.beHited();//坦克被击中
						b.onHit();//子弹命中
					}
				}	
			}
			else{ //敌方子弹
				if(b.collide(homeTank)){
					homeTank.beHited();//坦克被击中
					bombTanks.add(new BombTank(homeTank.getX(),homeTank.getY())); //创建bombTank
					b.onHit();//子弹命中
				}else if(b.collide(home)){//家被击中
					home.beHited();//家被击中
					b.onHit();//子弹命中
				}
			}
			for(BrickWall w:brickWalls){ //砖墙
				if(b.collide(w)){ 
					w.beHited();
					b.onHit();
				}
			}
			for(MetalWall w:metalWalls){ //金属墙	
				if(b.collide(w)){
					b.onHit();
				}
			}
		}
	}
	//敌方坦克碰撞检测
	private void enemyTanksCollisions() {
		for(EnemyTank t:enemyTanks){ //敌方坦克
			for(BrickWall w:brickWalls){ //砖墙
				if(t.collide(w)){
					t.restorePosition();
				}
			}
			for(MetalWall w:metalWalls){ //金属墙
				if(t.collide(w)){
					t.restorePosition();
				}
			}
			for(EnemyTank et:enemyTanks){ //检测除了自己的敌方坦克
				if(et!=t && t.collide(et)){ //
					t.restorePosition();
				}
			}
			for(River r:rivers){ //河流
				if(t.collide(r)){
					t.restorePosition();
				}
			}
			if(t.collide(homeTank)){//检测己方坦克
				t.restorePosition();
			}
			if(t.collide(home)){//检测家
				t.restorePosition();
			}
		}
	}
	//己方坦克碰撞检测
	private void homeTankCollisions() {
		HomeTank t = homeTank;
		for(BrickWall w:brickWalls){ //砖墙
			if(t.collide(w)){
				t.restorePosition();
			}
		}
		for(MetalWall w:metalWalls){ //金属墙
			if(t.collide(w)){
				t.restorePosition();
			}
		}
		for(River r:rivers){ //河流
			if(t.collide(r)){
				t.restorePosition();
			}
		}
		for(EnemyTank et:enemyTanks){ //敌方坦克
			if(t.collide(et)){
				t.restorePosition();
			}
		}
		if(t.collide(home)){//检测家
			t.restorePosition();
		}
		for(Blood b:bloods){ //血包检测
			if(t.collide(b)){
				b.setBloodVolume(0);//血量为0
				t.heal();//治愈
			}
		}
	}
	//清空所有元素
	public void clearAllElements() {
		enemyTanks.clear();
		bullets.clear();
		brickWalls.clear();
		brickWalls.clear();
		metalWalls.clear();
		rivers.clear();
		trees.clear();
		bloods.clear();
	}
	//更新游戏逻辑
	public void update(){
		moveAndFire(); //移动和发射
		checkCollisions();//碰撞检测
		checkBounds(); //越界检查
		checkLife();//检查生命
	}
	//越界检查
	private void checkBounds() {
		for(Bullet b:bullets){
			if(b.isOutOfBounds()){
				b.setBloodVolume(0); //生命为0
			}
		}
		for(EnemyTank t:enemyTanks){
			if(t.isOutOfBounds()){
				t.restorePosition();
			}
		}
		if(homeTank.isOutOfBounds()){
			homeTank.restorePosition();
		}
	}
	//检查生命
	private void checkLife() {
		//游戏失败
		if(homeTank.isLive()==false || home.isLive()==false){
			gameState.setLost();
		}
		//游戏胜利
		if(enemyTanks.size()==0){
			gameState.setWon();
		}
		//消除生命为 0 的元素
		for(int i = 0;i<enemyTanks.size();i++){ //遍历敌方坦克
			EnemyTank t = enemyTanks.get(i);
			if(t.isLive()==false){
				enemyTanks.remove(t);
				--i;
				bombTanks.add(new BombTank(t.getX(),t.getY())); //创建bombTank
			}
		}
		for(int i = 0;i<bullets.size();i++){ //遍历子弹
			Bullet b = bullets.get(i);
			if(b.isLive()==false){
				bullets.remove(b);
				--i;
			}
		}
		for(int i = 0;i<bombTanks.size();i++){ //遍历bombTank
			BombTank bt = bombTanks.get(i);
			if(bt.isLive()==false){
				bombTanks.remove(bt);
				--i;
			}	
		}
		for(int i = 0;i<brickWalls.size();i++){ //遍历砖墙
			BrickWall w = brickWalls.get(i);
			if(w.isLive()==false){
				brickWalls.remove(w);
				--i;	
			}	
		}
		for(int i = 0;i<bloods.size();i++){ //遍历血包
			Blood b = bloods.get(i);
			if(b.isLive()==false){
				bloods.remove(b);
				--i;	
			}	
		}
		if(bloods.size()==0){ //血包为0
			bloods.add(new Blood(155,196));
		}
	}
	//移动逻辑
	private void moveAndFire() {
		//移动和发射子弹坦克
		Random r = new Random();
		homeTank.move(); // 移动己方坦克
		for(EnemyTank t:enemyTanks){ 
			t.move();// 移动敌方坦克
			if(r.nextInt(100)>=90){ // 十分之一概率发射子弹
				t.fire();
			}
		}
		for(Bullet b:bullets){ // 移动子弹
			b.move();
		}
		for(Blood b:bloods){//血包
			b.move();	
		}
	}
	private void createBrickWall(){//创建普通墙() 
		for (int i = 0; i < 10; i++) { // 家的格局
			if (i < 4)
				brickWalls.add(new BrickWall(350, 580 - 21 * i));
			else if (i < 7)
				brickWalls.add(new BrickWall(372 + 22 * (i - 4), 517));
			else
				brickWalls.add(new BrickWall(416, 538 + (i - 7) * 21));
		}
		for (int i = 0; i < 32; i++) { // 砖墙
			if (i < 16) {
				brickWalls.add(new BrickWall(220 + 20 * i, 300)); // 砖墙布局
				brickWalls.add(new BrickWall(500 + 20 * i, 180));
				brickWalls.add(new BrickWall(200, 400 + 20 * i));
				brickWalls.add(new BrickWall(500, 400 + 20 * i));
			} else if (i < 32) {
				brickWalls.add(new BrickWall(220 + 20 * (i - 16), 320));
				brickWalls.add(new BrickWall(500 + 20 * (i - 16), 220));
				brickWalls.add(new BrickWall(220, 400 + 20 * (i - 16)));
				brickWalls.add(new BrickWall(520, 400 + 20 * (i - 16)));
			}
		}
	}
	// 创建金属墙
	private void createMetalWalls() {
		for (int i = 0; i < 20; i++) { // 金属墙布局
			if (i < 10) {
				metalWalls.add(new MetalWall(140 + 30 * i, 150));
				metalWalls.add(new MetalWall(600, 400 + 20 * (i)));
			} else if (i < 20)
				metalWalls.add(new MetalWall(140 + 30 * (i - 10), 180));
			else
				metalWalls.add(new MetalWall(500 + 30 * (i - 10), 160));
		}
	}

	// 创建树
	private void createTrees() {
		for (int i = 0; i < 4; i++) { // 树的布局
			if (i < 4) {
				trees.add(new Tree(0 + 30 * i, 360));
				trees.add(new Tree(220 + 30 * i, 360));
				trees.add(new Tree(440 + 30 * i, 360));
				trees.add(new Tree(660 + 30 * i, 360));
			}
		}
	}

	// 创建坦克
	private void createEnemyTanks() {
		int level = gameState.getGameLevel();
		int count;
		if(level== 1 || level == 2)count = 12;
		else count = 20;
		for (int i = 0; i < count; i++) { // 根据游戏级别初始化敌方坦克数目
			if (i < 9) // 设置坦克出现的位置
				enemyTanks.add(new EnemyTank(150 + 70 * i, 40, Direction.D, this));
			else if (i < 15)
				enemyTanks.add(new EnemyTank(700, 140 + 50 * (i - 6), Direction.D, this));
			else
				enemyTanks.add(new EnemyTank(10, 50 * (i - 12), Direction.D, this));
		}
	}

    //获取己方坦克
    public HomeTank getHomeTank() {
        return homeTank;
    }
	//获取敌方坦克
	public List<EnemyTank> getEnemyTanks() {
		return enemyTanks;
	}
	public List<Bullet> getBullets() {
		return bullets;
	}
}
