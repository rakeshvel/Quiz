//libraries
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

/* 
 * Quiz class which accesses an API based on desired characteristics for a quiz to be generated online. The quiz is parsed and formatted such that questions are converted
 * from HTML format and can be easily displayed to users
 */
public class Quiz{
    /*
     * format() takes an input of text from the opentdb API and parses the generated text into seperate questions with associated correct answers.
     * 
     * String toParse - text generated from API cleaned from HTML into console-friendly ASCII
     * List<List<Object>> toReturn - outer list contains a list of questions, which contains entries for different portions of each question
     */
    private static List<List<Object>> format(String toParse){
        //ArrayList to be inputted with question ArrayLists
        List<List<Object>> toReturn = new ArrayList<>();
    
        //splits each question group into independent entries
        String[] dataSplit = toParse.substring(toParse.indexOf("\"results\":"), toParse.lastIndexOf("]")).split("\\},\\{");

        //Each question is handled independently
        for(String data : dataSplit){
            //Finds question portion
            int qSt = data.indexOf("\"question\":\"")+12;
            int qEn = data.indexOf("\",\"", qSt);
            String q = data.substring(qSt, qEn);

            //Finds correct answer
            int ansSt = data.indexOf("\"correct_answer\":\"")+18;
            int ansEn = data.indexOf("\",\"", ansSt);
            String ans = data.substring(ansSt, ansEn);

            //Finds incorrect answers
            int nansSt = data.indexOf("\"incorrect_answers\":[")+22;
            int nansEn = data.indexOf("]", nansSt);
            String nans = data.substring(nansSt, nansEn);

            
            //Since there are three incorrect answers, they must be parsed appropriately
            List<String> answers = new ArrayList<>();
            Collections.addAll(answers, nans.replaceAll("\"", "").split(","));
            answers.add(ans);

            //Collections used to shuffle options randomly
            Collections.shuffle(answers);

            //Creates new ArrayList for each question before being added to returning ArrayList
            List<Object> question = new ArrayList<>();
            question.add(q);
            question.add(answers);
            question.add(ans);
            toReturn.add(question);
        }
        //returns List
        return toReturn;
    }

    /*
     * generate() takes quiz limitations as input and generates an unparsed String, which then gets parsed in format(), and returned to caller
     * 
     * int amount - amount of questions to generate
     * int category - desired category of questions
     * String difficulty - chosen difficulty of questions
     */
    public static List<List<Object>> generate(int amount, int category, String difficulty)throws Exception{
        //creates URL based on inputs and then accesses it
        URL url = new URL("https://opentdb.com/api.php?amount="+amount+"&category="+category+"&difficulty="+difficulty+"&type=multiple");
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        
        //reads from URL and adds each line to a String
        Scanner scanner = new Scanner(conn.getInputStream());
        StringBuilder response = new StringBuilder();
        while(scanner.hasNextLine()){
            response.append(scanner.nextLine());
        }
        scanner.close();

        //cleans questions to desired format, and then returns parsed questions
        return format(response.toString().replace("&quot;", "\"").replace("&#039;", "'").replace("&ldquo;", "\"")
        .replace("&rdquo;", "\"").replace("&rsquo;", "'").replace("&hellip;", "..."));
    }
}
