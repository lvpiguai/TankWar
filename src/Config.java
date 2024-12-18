import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;

public class Config {
    // 配置字段：用于存储 JSON 中的数据
    public static BloodConfig bloodConfig;
    public static BombTankConfig bombTankConfig;
    public static GameFrameConfig gameFrameConfig;

    // 加载 JSON 配置文件
    public static void loadConfig(String filePath) {
        System.out.println("正在加载配置文件: " + filePath); // 打印出文件路径，检查是否正确
        Gson gson = new Gson();
        try (FileReader reader = new FileReader(filePath)) {
            AppConfig appConfig = gson.fromJson(reader, AppConfig.class);
            
            // 将配置加载到静态字段
            bloodConfig = appConfig.bloodConfig;
            bombTankConfig = appConfig.bombTankConfig;
            gameFrameConfig = appConfig.gameFrameConfig;
            
            if (appConfig == null) {
                System.err.println("配置文件加载失败，Config 对象为 null");
            }else{
                System.out.println("配置文件加载成功");
                System.out.println("加载的配置文件内容: " + appConfig);
            }

            if(bloodConfig == null) {
                System.err.println("bloodConfig 加载失败");
            } else{
                System.out.println("bloodConfig 加载成功");
            }

            if(bombTankConfig == null) {
                System.err.println("bombTankConfig 加载失败");
            } else{
                System.out.println("bombTankConfig 加载成功");
            }

            if(gameFrameConfig == null) {
                System.err.println("gameFrameConfig 加载失败");
            } else{
                System.out.println("gameFrameConfig 加载成功");
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
    public static class GameFrameConfig {
        int width;
        int length;
    }
    // 定义一个顶层容器类来封装所有配置类
    private static class AppConfig {
        BloodConfig bloodConfig;
        BombTankConfig bombTankConfig;
        GameFrameConfig gameFrameConfig;
    }
}
