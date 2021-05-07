import org.jetbrains.annotations.NotNull;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.io.IOException;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class MoodleAPI
{

    private final OkHttpClient client;
    private String cookies;
    private String sesskey;


    public MoodleAPI() throws IOException
    {
        client = new OkHttpClient().newBuilder().build();
        loadSeesKey();
    }


    /*
      A session Key and Moodle Session Id is used to prevents bad requests.
      So before sending the first request, we cache both of them.
     */
    private void loadSeesKey() throws IOException
    {
        Request request = getBuilder()
                .get().build();
        Response response = client.newCall(request).execute();
        if (response.isSuccessful()) {
            List<String> cookieList = response.headers().values("Set-Cookie");
            cookies = (cookieList.get(0));
            String html = response.body().string();
            response.close();
            Document parse = Jsoup.parse(html);
            Element select = parse.select("input[name=sesskey]").get(0);
            sesskey = select.attr("value");
        }else {
            System.err.println("Unable to load Session Key ;( ! ");
            System.exit(-1);
        }
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
                .addHeader("Accept-Language", "en-IN,en;q=0.9");
    }


    public int sendRequest(long roll) throws IOException
    {

        MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
        RequestBody body = RequestBody.create("sesskey="+sesskey+"&_qf__login_forgot_password_form=1&username=&email=" + roll + "@kiit.ac.in&submitbuttonemail=Search", mediaType);
        Request request = getBuilder()
                .method("POST", body)
                .addHeader("Cookie",cookies)
                .build();
        Response response = client.newCall(request).execute();
        if (response.code()==404){
            loadSeesKey();
        }
        response.close();
        return response.code();
    }

}
