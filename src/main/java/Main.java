

import net.dv8tion.jda.api.JDA;
import net.dv8tion.jda.api.JDABuilder;
import net.dv8tion.jda.api.OnlineStatus;
import net.dv8tion.jda.api.entities.MessageEmbed;

import javax.security.auth.login.LoginException;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Optional;

public class Main extends Thread{
    public static Main INSTANCE;

    public JDA jda;
    public JDABuilder builder;

    public static void main(String[] args) {
        try {
            new Main();
        } catch (LoginException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }
    public Main() throws LoginException {
        INSTANCE = this;

        String TOKEN = getEnVar("BOT_TOKEN");

        builder = JDABuilder.createLight(TOKEN);
        builder.setStatus(OnlineStatus.OFFLINE);
        jda = builder.build();
        System.out.println(MessageEmbed.DESCRIPTION_MAX_LENGTH);
        try {
            jda.awaitReady();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println("Online");
        startThread();
    }
    public void startThread(){
        new Thread(() -> {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            String line;
            try {
                while ((line = reader.readLine()) != null) {

                    if (line.equalsIgnoreCase("exit")) {
                        jda.shutdownNow();
                        this.stop();
                    }
                    jda.retrieveUserById(line).queue(usr -> System.out.println(usr.getAsTag()));
                }
            } catch (IOException | IllegalArgumentException e) {
                e.printStackTrace();
                startThread();
            }
        }).start();
    }

    public static String getEnVar(String envar) {
        String content = null;
        try {
            content = Optional.ofNullable(System.getenv(envar)).orElseThrow(
                    () -> new Exception("Environment Variable " + envar + " is not set in the environment"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return content;
    }
}
