import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class main
{

    static ArrayList<Long> rolls;
    static OkHttpClient client = new OkHttpClient().newBuilder()
            .build();

    public static void readRolls() throws FileNotFoundException
    {
        rolls = new ArrayList<>();
        Scanner sc = new Scanner(new File("rolls"));
        while (sc.hasNextLine()) {
            rolls.add(Long.parseLong(sc.nextLine()));
        }
    }

    public static void sendRequest(long roll) throws IOException
    {

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create("sesskey=DDdJsQMkoQ&_qf__login_forgot_password_form=1&username=&email=" + roll + "@kiit.ac.in&submitbuttonemail=Search", mediaType);
        Request request = getBuilder()
                .method("POST", body)
                .build();
        Response response = client.newCall(request).execute();
        System.out.println(" Done " + roll + " code" + response.code());
        response.close();
    }

    @NotNull
    private static Request.Builder getBuilder()
    {
        return new Request.Builder()
                .url("https://kiitmoodle.in/login/forgot_password.php")
                .addHeader("Connection", "keep-alive")
                .addHeader("Cache-Control", "max-age=0")
                .addHeader("sec-ch-ua", "\" Not A;Brand\";v=\"99\", \"Chromium\";v=\"90\", \"Google Chrome\";v=\"90\"")
                .addHeader("sec-ch-ua-mobile", "?0")
                .addHeader("Origin", "https://kiitmoodle.in")
                .addHeader("Upgrade-Insecure-Requests", "1")
                .addHeader("DNT", "1")
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/90.0.4430.93 Safari/537.36")
                .addHeader("Accept", "text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,image/webp,image/apng,*/*;q=0.8,application/signed-exchange;v=b3;q=0.9")
                .addHeader("Sec-Fetch-Site", "same-origin")
                .addHeader("Sec-Fetch-Mode", "navigate")
                .addHeader("Sec-Fetch-User", "?1")
                .addHeader("Sec-Fetch-Dest", "document")
                .addHeader("Referer", "https://kiitmoodle.in/login/forgot_password.php")
                .addHeader("Accept-Language", "en-IN,en;q=0.9")
                .addHeader("Cookie", "MoodleSession=dj2nh61iaa3jmpfbucl6nkmolk;");
    }

    public static void main(String[] args) throws FileNotFoundException
    {
        readRolls();

        rolls.stream().filter(roll -> ("" + roll).startsWith("18") && roll != 1828017).parallel().forEach(it -> {
            try{
                sendRequest(it);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }
}
