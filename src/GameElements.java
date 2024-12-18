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
	private Random r = new Random();
	private int mpNum;

    // 构造函数
    public GameElements(GameState gameState) {
		this.gameState = gameState;
    }

    // 初始化游戏元素
	public void initGameElements() {
		mpNum = r.nextInt(2)+1;//随机地图编号
		createBrickWall();//创建普通墙
		createMetalWalls(); // 创建 metalWalls
		createTrees(); // 创建 trees
		createEnemyTanks(); // 创建 enemyTanks
		rivers.add(new River(Config.gameElementsConfig.riverPositions[0][0], Config.gameElementsConfig.riverPositions[0][1])); // 创建河流
        homeTank = new HomeTank(Config.gameElementsConfig.homeTankX, Config.gameElementsConfig.homeTankY, Direction.STOP, this); // 己方坦克
        home = new Home(Config.gameElementsConfig.homeX, Config.gameElementsConfig.homeY); // 实例化home
		bloods.add(new Blood(Config.gameElementsConfig.bloodsPositions[0][0],Config.gameElementsConfig.bloodsPositions[0][1]));
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
	private void clearAllElements() {
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
	//创建普通墙() 
	private void createBrickWall(){
		if(mpNum==1){
			for (int i = 0; i < 10; i++) {//家周围的墙壁
				brickWalls.add(new BrickWall(Config.gameElementsConfig.brickWallPositions[i][0], Config.gameElementsConfig.brickWallPositions[i][1]));
			}
			for (int i = 10; i < 32; i++) {//其他
				brickWalls.add(new BrickWall(Config.gameElementsConfig.brickWallPositions[i][0], Config.gameElementsConfig.brickWallPositions[i][1]));
			}
		}else{
			// 使用第二幅地图的配置
			for (int i = 0; i < 10; i++) { // 家的格局
				brickWalls.add(new BrickWall(Config.gameElementsConfig.secondMapConfig.secondMapBrickWalls[i][0], 
											 Config.gameElementsConfig.secondMapConfig.secondMapBrickWalls[i][1]));
			}
			for (int i = 10; i < 32; i++) { // 其他
				brickWalls.add(new BrickWall(Config.gameElementsConfig.secondMapConfig.secondMapBrickWalls[i][0], 
											 Config.gameElementsConfig.secondMapConfig.secondMapBrickWalls[i][1]));
			}
		}
		
	}
	// 创建金属墙
	private void createMetalWalls() {
		if(mpNum==1){
			for (int i = 0; i < 20; i++) {
				if (i < 10) {
					metalWalls.add(new MetalWall(Config.gameElementsConfig.metalWallPositions[i][0], Config.gameElementsConfig.metalWallPositions[i][1]));
					metalWalls.add(new MetalWall(Config.gameElementsConfig.metalWallPositions[i + 10][0], Config.gameElementsConfig.metalWallPositions[i + 10][1]));
				} else {
					metalWalls.add(new MetalWall(Config.gameElementsConfig.metalWallPositions[i][0], Config.gameElementsConfig.metalWallPositions[i][1]));
				}
			}
		}else{
			 // 使用第二幅地图的配置
			 for (int i = 0; i < 13; i++) {
				metalWalls.add(new MetalWall(Config.gameElementsConfig.secondMapConfig.secondMapMetalWalls[i][0], 
											 Config.gameElementsConfig.secondMapConfig.secondMapMetalWalls[i][1]));
			}
			for (int i = 0; i <= 10; i++) {
				metalWalls.add(new MetalWall(Config.gameElementsConfig.secondMapConfig.secondMapMetalWalls[i][0], 
											 Config.gameElementsConfig.secondMapConfig.secondMapMetalWalls[i][1]));
			}
		}
		
	}
	// 创建树
	private void createTrees() {
		if(mpNum==1){
			for (int i = 0; i < 4; i++) {
				trees.add(new Tree(Config.gameElementsConfig.treePositions[i][0], Config.gameElementsConfig.treePositions[i][1]));
				trees.add(new Tree(Config.gameElementsConfig.treePositions[i + 4][0], Config.gameElementsConfig.treePositions[i + 4][1]));
				trees.add(new Tree(Config.gameElementsConfig.treePositions[i + 8][0], Config.gameElementsConfig.treePositions[i + 8][1]));
				trees.add(new Tree(Config.gameElementsConfig.treePositions[i + 12][0], Config.gameElementsConfig.treePositions[i + 12][1]));
			}
		}else{
			// 使用第二幅地图的配置
			for (int i = 0; i < 4; i++) {
				trees.add(new Tree(Config.gameElementsConfig.secondMapConfig.secondMapTrees[i][0], 
								   Config.gameElementsConfig.secondMapConfig.secondMapTrees[i][1]));
				trees.add(new Tree(Config.gameElementsConfig.secondMapConfig.secondMapTrees[i + 4][0], 
								   Config.gameElementsConfig.secondMapConfig.secondMapTrees[i + 4][1]));
				trees.add(new Tree(Config.gameElementsConfig.secondMapConfig.secondMapTrees[i + 8][0], 
								   Config.gameElementsConfig.secondMapConfig.secondMapTrees[i + 8][1]));
				trees.add(new Tree(Config.gameElementsConfig.secondMapConfig.secondMapTrees[i + 12][0], 
								   Config.gameElementsConfig.secondMapConfig.secondMapTrees[i + 12][1]));
			}
		}
	}
	// 创建坦克
	private void createEnemyTanks() {
		int level = gameState.getGameLevel();
		int count = 0;
		if(level== 1)count = Config.gameElementsConfig.enemyTankCountLevel1;
		else if(level== 2)count = Config.gameElementsConfig.enemyTankCountLevel2;
		else if(level== 3)count = Config.gameElementsConfig.enemyTankCountLevel3;
		else if(level== 4)count = Config.gameElementsConfig.enemyTankCountLevel4;
		for (int i = 0; i < count; i++) {
			if (i < 9) {
				enemyTanks.add(new EnemyTank(Config.gameElementsConfig.enemyTankPositions.level1[i][0], 
											 Config.gameElementsConfig.enemyTankPositions.level1[i][1], 
											 Direction.D, this));
			} else if (i < 15) {
				enemyTanks.add(new EnemyTank(Config.gameElementsConfig.enemyTankPositions.level2[i - 9][0], 
											 Config.gameElementsConfig.enemyTankPositions.level2[i - 9][1], 
											 Direction.D, this));
			} else {
				enemyTanks.add(new EnemyTank(Config.gameElementsConfig.enemyTankPositions.level3[i - 15][0], 
											 Config.gameElementsConfig.enemyTankPositions.level3[i - 15][1], 
											 Direction.D, this));
			}
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
