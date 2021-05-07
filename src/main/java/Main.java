import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class Main
{

    private ArrayList<Long> rolls;
    private MoodleAPI moodleAPI;

    public Main() throws IOException
    {
        readRolls();
        moodleAPI = new MoodleAPI();
    }

    public void readRolls() throws FileNotFoundException
    {
        rolls = new ArrayList<>();
        Scanner sc = new Scanner(new File("rolls"));
        while (sc.hasNextLine()) {
            rolls.add(Long.parseLong(sc.nextLine()));
        }
    }

    public int sendRequest(long roll) throws IOException
    {
        return moodleAPI.sendRequest(roll);
    }

    public static void main(String[] args) throws IOException
    {

        Main main = new Main();

        main.rolls.stream().filter(roll -> ("" + roll).startsWith("18") && roll != 1828017).forEach(it -> {
            try{
                main.sendRequest(it);
            } catch (IOException e) {
                e.printStackTrace();
            }
        });


    }
}
