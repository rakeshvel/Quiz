//libraries
import java.util.List;
import java.util.Scanner;

/*
 * Main class runs Quiz and interacts with user to generate desired quiz and process quiz answers and results
 */
public class Main{
    static double best = 0.0;

    /*
     * main() calls run() to start program
     */
    public static void main(String[] args){
        run();
    }

    /*
     * Frequently called helper method to ensure user inputs an integer when requested
     * 
     * Scanner scanner - scanner generated into run()
     * return - int user input
     */
    private static int checkInt(Scanner scanner){
        while(true){
            if(scanner.hasNextInt()){
                return scanner.nextInt();
            }else{
                System.out.print("Invalid input. Please enter a number: ");
                scanner.next();
            }
        }
    }

    /*
     * run() handles all user inputs and prompts user to call itself again
     */
    public static void run(){
        Scanner scanner = new Scanner(System.in);
        //try-catch to handle thrown errors
        try{
            //prompts user for amount of questions
            int amount = 0;
            while(amount<1||amount>5){
                System.out.print("# of Questions to Generate (1-5): ");
                amount = checkInt(scanner);
                scanner.nextLine();
            }

            //prompts user for question category
            int category = 0;
            //hardcoded String of categories from API
            String categories = "Categories of Questions:\n 9. General Knowledge\n10. Books\n11. Film\n12. Music\n13. Musicals & Theatre\n14. Television\n15. Video Games\n" +
            "16. Board Games\n17. Science & Nature\n18. Computers\n19. Mathematics\n20. Mythology\n21. Sports\n22. Geography\n23. History\n24. Politics\n25. Art\n" +
            "26. Celebrities\n27. Animals\n28. Vehicles\n29. Comics\n30. Gadgets\n31. Anime & Manga\n32. Cartoon & Animations";
            System.out.println(categories);
            while(category<9||category>32){
                System.out.print("Chosen Category (9-32) or Select 8 for Category Details: ");
                category = checkInt(scanner);
                if(category==8)
                    System.out.println(categories);
                scanner.nextLine();
            }

            //prompts user for difficulty of quiz
            String difficulty = "";
            while(!(difficulty.equals("easy")||difficulty.equals("medium")||difficulty.equals("hard"))){
                System.out.print("Difficulty (easy, medium, or hard): ");
                difficulty = scanner.next();
                scanner.nextLine();
            }

            System.out.println();

            //calls on Quiz class to generate new quiz based on user input
            List<List<Object>> questions = Quiz.generate(amount, category, difficulty);
            int score = 0;

            //processes each question and handles user inputs to determine if answer was correct
            for(int i = 0; i<questions.size(); i++){
                List<Object> q = questions.get(i);
                String prompt = (String) q.get(0);
                List<String> answers = (List<String>) q.get(1);
                String correct = (String) q.get(2);

                //prints question and each option choice to user
                System.out.println("Question " +(i+1)+ " out of " +amount+ ": " +prompt);
                for(int j = 0; j<answers.size(); j++){
                    System.out.println((j+1)+ ". " +answers.get(j));
                }

                //prompts user for answer choice
                System.out.print("Answer: ");
                int ans = checkInt(scanner);
                scanner.nextLine();

                //checks if user chose correct answer and displays appropriate message
                if(ans<answers.size()+1&&answers.get(ans-1).equals(correct)){
                    System.out.println("Correct.");
                    score++;
                }else{
                    System.out.println("Incorrect. The answer was: " +correct);
                }
                System.out.println();
            }

            //displays current attempt's score as well as best score for session
            double curr = (double)score/amount * 100;
            best = Double.max(best, curr);
            System.out.println("Quiz over! Score: " + curr + "%");
            System.out.println("Best Score: " + best + "%");
            
            //prompts user for repeated attempts
            System.out.print("Would You like to play again? Press 1 for yes, 0 for no: ");
            int check = checkInt(scanner);
            scanner.nextLine();
            if(check==1)
                run();
            else{
                System.out.print("Are you sure? Press 1 to Play Again, 0 to not: ");
                check = checkInt(scanner);
                scanner.nextLine();
                if(check==1)
                    run();
            }
        }catch(Exception e){
            System.err.println("Unexpected Error Occured.");
        }finally{
            scanner.close();
        }
    }
}
