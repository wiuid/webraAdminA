package top.webra;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * 主启动类
 * @author webra
 */
@SpringBootApplication
@MapperScan("top.webra.mapper")
@EnableTransactionManagement
public class ApplicationRun {
    public static void main(String[] args){
        SpringApplication.run(ApplicationRun.class,args);
        System.out.println(" __          __    _                               _             _                  \n" +
                " \\ \\        / /   | |                    /\\       | |           (_)           /\\    \n" +
                "  \\ \\  /\\  / /___ | |__   _ __  __ _    /  \\    __| | _ __ ___   _  _ __     /  \\   \n" +
                "   \\ \\/  \\/ // _ \\| '_ \\ | '__|/ _` |  / /\\ \\  / _` || '_ ` _ \\ | || '_ \\   / /\\ \\  \n" +
                "    \\  /\\  /|  __/| |_) || |  | (_| | / ____ \\| (_| || | | | | || || | | | / ____ \\ \n" +
                "     \\/  \\/  \\___||_.__/ |_|   \\__,_|/_/    \\_\\\\__,_||_| |_| |_||_||_| |_|/_/    \\_\\\n" +
                "   _____  _                _            _____                                       \n" +
                "  / ____|| |              | |          / ____|                                      \n" +
                " | (___  | |_  __ _  _ __ | |_  ______| (___   _   _   ___  ___  ___  ___  ___      \n" +
                "  \\___ \\ | __|/ _` || '__|| __||______|\\___ \\ | | | | / __|/ __|/ _ \\/ __|/ __|     \n" +
                "  ____) || |_| (_| || |   | |_         ____) || |_| || (__| (__|  __/\\__ \\\\__ \\     \n" +
                " |_____/  \\__|\\__,_||_|    \\__|       |_____/  \\__,_| \\___|\\___|\\___||___/|___/     \n" +
                "                                                                                    \n" +
                "                                                                                    ");
    }
}
