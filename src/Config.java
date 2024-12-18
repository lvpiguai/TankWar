import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;
import java.awt.Color;

public class Config {

    // 配置字段：用于存储 JSON 中的数据
    public static BloodConfig bloodConfig;
    public static BombTankConfig bombTankConfig;
    public static BrickWallConfig brickWallConfig;
    public static BulletConfig bulletConfig;
    public static TankConfig tankConfig;
    public static EnemyTankConfig enemyTankConfig;
    public static GameStateConfig gameStateConfig;
    public static HomeConfig homeConfig;
    public static HomeTankConfig homeTankConfig;
    public static MetalWallConfig metalWallConfig;
    public static RiverConfig riverConfig;
    public static TreeConfig treeConfig;
    public static WindowManagerConfig windowManagerConfig;
    public static GameElementsConfig gameElementsConfig;
    public static GameFrameConfig gameFrameConfig;

    // 加载 JSON 配置文件
    public static void loadConfig(String filePath) {
        System.out.println("正在加载配置文件: " + filePath); // 打印出文件路径，检查是否正确
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filePath)) {
            Config config = gson.fromJson(reader, Config.class);
            // 配置加载到静态字段
            bloodConfig = config.bloodConfig;
            bombTankConfig = config.bombTankConfig;
            brickWallConfig = config.brickWallConfig;
            bulletConfig = config.bulletConfig;
            tankConfig = config.tankConfig;
            enemyTankConfig = config.enemyTankConfig;
            gameStateConfig = config.gameStateConfig;
            homeConfig = config.homeConfig;
            homeTankConfig = config.homeTankConfig;
            metalWallConfig = config.metalWallConfig;
            riverConfig = config.riverConfig;
            treeConfig = config.treeConfig;
            windowManagerConfig = config.windowManagerConfig;
            gameElementsConfig = config.gameElementsConfig;
            gameFrameConfig = config.gameFrameConfig;
            if (config == null) {
                System.err.println("配置文件加载失败，Config 对象为 null");
            }else{
                System.out.println("配置文件加载成功");
                System.out.println("加载的配置文件内容: " + config);
            }
            
            if (config.gameStateConfig == null) {
                System.err.println("gameStateConfig 加载失败！");
            } else {
                System.out.println("gameStateConfig 加载成功！");
            }
            
            if (config.gameFrameConfig == null) {
                System.err.println("gameFrameConfig 加载失败！");
            } else {
                System.out.println("gameFrameConfig 加载成功！");
            }

            if (config.gameElementsConfig == null) {
                System.err.println("gameElementsConfig 加载失败！");
            } else {
                System.out.println("gameElementsConfig 加载成功！");
            }

            if(config.windowManagerConfig == null) {
                System.err.println("windowManagerConfig 加载失败！");
            } else{
                System.out.println("windowManagerConfig 加载成功"); 
            }

            if(config.bloodConfig == null) {
                System.err.println("bloodConfig 加载失败");
            } else{
                System.out.println("bloodConfig 加载成功");
            }

        }catch (IOException e) {
            System.err.println("配置文件加载失败: " + e.getMessage());
            e.printStackTrace();
        }
    }
    // 各种配置类
    public static class BloodConfig {
        int count;
        int[][] position;
        int width;
        int length;
        int bloodVolume;
        String imagesPath;
    }
    public static class BombTankConfig {
        String[] explosionImages;
        int bloodVolume;
    }
    public static class BrickWallConfig {
        String imagePath;
        int width;
        int length;
        int bloodVolume;
    }
    public static class BulletConfig {
        int speedX;
        int speedY;
        int width;
        int length;
        String[] imagePaths;
    }
    public static class TankConfig {
        int speedX;
        int speedY;
        String[] imagePaths;
        String initialDirection; // 初始方向
    }
    public static class EnemyTankConfig {
        int speedX;
        int speedY;
        String[] imagePaths;
        int bloodVolume;
        int width;
        int length;
        int minSteps;
        int maxSteps;
    }
    public static class GameStateConfig {
        String fontName;
        int fontStyle;
        int fontSize;
        int initGameLevel;
        int timeTextX;
        int timeTextY;
        int enemyTankCountTextX;
        int enemyTankCountTextY;
        int homeTankHealthTextX;
        int homeTankHealthTextY;
        String timeLabel;
        String enemyTankCountLabel;
        String homeTankHealthLabel;
        String winMessage;
        String loseMessage;
        int winMessageX;
        int winMessageY;
        int loseMessageX;
        int loseMessageY;
    }
    public static class HomeConfig {
        String imagePath; // 大本营图像路径
        int bloodVolume;  // 血量
    }
    public static class HomeTankConfig {
        int bloodVolume;    // 坦克血量
        int width;          // 坦克宽度
        int length;         // 坦克长度
        String[] imagePaths; // 坦克图像路径数组
        int healthBarX;     // 血条的 x 坐标
        int healthBarY;     // 血条的 y 坐标
        int healthBarWidth; // 血条的宽度
        int healthBarHeight; // 血条的高度
    }
    public static class MetalWallConfig {
        int width;        // 墙壁宽度
        int length;       // 墙壁长度
        String imagePath; // 图像路径
    }
    public static class RiverConfig {
        int width;         // 河流宽度
        int length;        // 河流长度
        String imagePath;  // 图像路径
    }
    public static class TreeConfig {
        int width;         // 树的宽度
        int length;        // 树的长度
        String imagePath;  // 图像路径
    }
    public static class WindowManagerConfig {
        String windowTitle;           // 窗口标题
        Color backgroundColor;       // 背景颜色
        int windowWidth;             // 窗口宽度
        int windowHeight;            // 窗口高度
        String fontName;             // 字体名称
        int fontSize;                // 字体大小
        int fontStyle;               // 字体样式（如：Font.BOLD）
        String[] menuNames;          // 菜单名称
        String[][] menuItemNames;    // 菜单项名称
        String startNewGameConfirmation;
        String exitConfirmation;
        String gameInstructionsMessage;
        String gameInstructionsTitle;
        Level level1;
        Level level2;
        Level level3;
        Level level4;
        public static class Level {
            int value;
            int speedX;
            int speedY;
            int bulletSpeedX;
            int bulletSpeedY;
        }
    }
    public static class GameElementsConfig {
        int homeTankX;
        int homeTankY;
        int homeX;
        int homeY;
        int[][] brickWallPositions;
        int[][] metalWallPositions;
        int[][] treePositions;
        int[][] riverPositions;
        int[][] bloodsPositions;
        int enemyTankCountLevel1;
        int enemyTankCountLevel2;
        int enemyTankCountLevel3;
        int enemyTankCountLevel4;
        EnemyTankPositions enemyTankPositions;
        public static class EnemyTankPositions {
            int[][] level1;//坦克出生点
            int[][] level2;
            int[][] level3;
            int[][] level4;
        }
        SecondMapConfig secondMapConfig;
        public static class SecondMapConfig {
            // 普通墙
            public int[][] secondMapBrickWalls;
            // 金属墙
            public int[][] secondMapMetalWalls;
            // 树
            public int[][] secondMapTrees;
        }
    }
    public static class GameFrameConfig {
        int width;
        int length;
    }
}