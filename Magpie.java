    /**
     * Search for one word in phrase. The search is not case
     * sensitive. This method will check that the given goal
     * is not a substring of a longer string (so, for
     * example, "I know" does not contain "no").
     *
     * @param statement
     *            the string to search
     * @param goal
     *            the string to search for
     * @param startPos
     *            the character of the string to begin the
     *            search at
     * @return the index of the first occurrence of goal in
     *         statement or -1 if it's not found
     */
public class Magpie
{
    /**
     * Get a default greeting 	
     * @return a greeting
     */
    public String getGreeting()
    {
        return "Hello, let's talk.";
    }

    /**
     * Gives a response to a user statement
     * 
     * @param statement
     *            the user statement
     * @return a response based on the rules given
     */
    public String getResponse(String statement)
    {
        String response = "";
        if (findKeyword(statement, "yes",0) >= 0)
        {
            response = "Yessir, and how you mean so?";
        }
        else if (findKeyword(statement, "mother", 0) >= 0
                 || findKeyword(statement, "father", 0) >= 0
                 || findKeyword(statement, "sister", 0) >= 0
                 || findKeyword(statement, "brother", 0) >= 0)
        {
            response = "Your family seems cool, tell me more!";
        }
        else if (findKeyword(statement, "cat", 0) >=0
                || findKeyword(statement, "dog", 0) >=0)
        {
            response = "Tell me more about your pets.";
        }
        else if (findKeyword(statement, "Mr. Finkelstein", 0) >=0)
        {
            response = "He sounds like a good teacher. How is his teaching style?";
        }
        else if (statement.trim().equals("")) 
            {
            response = "Say something, please.";
            }
        else if (findKeyword(statement, "I want to", 0) >=0)
            {
                response = transformIWantToStatement(statement);
            }
           
        else if (findKeyword(statement, "me", 0) >=0
                || findKeyword(statement, "you", 0) >=0)
            {
                response = transformYouMeStatement(statement);
            }
            
        else
        {
            response = getRandomResponse();
        }
        return response;
    }

    /**
     * Pick a default response to use if nothing else fits.
     * @return a non-committal string
     */
    private String getRandomResponse()
    {
        final int NUMBER_OF_RESPONSES = 6;
        double r = Math.random();
        int whichResponse = (int)(r * NUMBER_OF_RESPONSES);
        String response = "";

        if (whichResponse == 0)
        {
            response = "Interesting, tell me more.";
        }
        else if (whichResponse == 1)
        {
            response = "Hmmm.";
        }
        else if (whichResponse == 2)
        {
            response = "Do you really think so?";
        }
        else if (whichResponse == 3)
        {
            response = "You don't say.";
        }
        else if (whichResponse == 4)
            {
                response = "Fire, tell me more!";
            }
        else if (whichResponse == 5)
            {
                response = "Awesome, explain it more sounds interesting";
            }
        return response;
    }

 /**
     * Search for one word in phrase. The search is not case
     * sensitive. This method will check that the given goal
     * is not a substring of a longer string (so, for
     * example, "I know" does not contain "no").
     *
     * @param statement
     *            the string to search
     * @param goal
     *            the string to search for
     * @param startPos
     *            the character of the string to begin the
     *            search at
     * @return the index of the first occurrence of goal in
     *         statement or -1 if it's not found
     */
 private static int findKeyword(String statement, String goal, int startPos)
 {
     String phrase = statement.trim().toLowerCase();
     goal = goal.toLowerCase();

     // The only change to incorporate the startPos is in
     // the line below
     int psn = phrase.indexOf(goal, startPos);

     // Refinement--make sure the goal isn't part of a word
     while (psn >= 0)
     {
         // Find the string of length 1 before and after
         // the word
         String before = " ", after = " ";
         if (psn > 0)
         {
             before = phrase.substring(psn - 1, psn);
         }
         if (psn + goal.length() < phrase.length())
         {
             after = phrase.substring(
                 psn + goal.length(),
                 psn + goal.length() + 1);
         }

         // If before and after aren't letters, we've found the word
         if (((before.compareTo("a") < 0) || (before.compareTo("z") > 0)) // before is not a letter
             && ((after.compareTo("a") < 0) || (after.compareTo("z") > 0))) // after is not a letter
         {
             return psn;
         }

         // The last position didn't work, so let's find
         // the next, if there is one.
         psn = phrase.indexOf(goal, psn + 1);
     }

     return -1;
    }
   /**
     * Take a statement with "I want to <something>." and transform it into 
     * "What would it mean to <something>?"
     * @param statement the user statement, assumed to contain "I want to"
     * @return the transformed statement
     */
   private String transformIWantToStatement(String statement)
   {
       // Remove the final period, if there is one
       statement = statement.trim();
       String lastChar = statement.substring(statement.length() - 1);
       if (lastChar.equals("."))
       {
           statement = statement.substring(0, statement.length() - 1);
       }

       // Transform the statement into a question
       int kw = statement.indexOf("I want to");
       if (kw != -1) {
        // Extract the <something> part
        String something = statement.substring(kw + "I want to ".length());
        // Construct the new statement
        return "What would it mean to " + something + "?";
    } else {
        // Fallback if "I want to " isn't found (though assumed present)
        return "I'm not sure what you mean by that.";
    }
}

    /**
     * Take a statement with "you <something> me" and transform it into 
     * "What makes you think that I <something> you?"
     * @param statement the user statement, assumed to contain "you" followed by "me"
     * @return the transformed statement
     */
    private String transformYouMeStatement(String statement)
    {
        String you = "you";
        String me = "me";
        int psnOfYou = findKeyword (statement, you, 0);
        int psnOfMe = findKeyword (statement, me, psnOfYou + you.length());

        String restOfStatement = statement.substring(psnOfYou + you.length(), psnOfMe).trim();
        return "What makes you think that I " + restOfStatement + " you?";
    }
}
