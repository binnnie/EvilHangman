import java.util.*;

/*
   Brandon Dixon
   CS145
   11/2/2017
   HangmanManager.java
   
   Manages all of the data and word choosing behind the Evil Hangman game.
*/

public class HangmanManager{
   private Set<String> words;
   private int guessesLeft;
   private SortedSet<Character> guessed;
   private String pattern;
   
   public HangmanManager(List<String> dictionary, int length, int max){
      if(length < 1 || max < 0){
         throw new IllegalArgumentException("illegal input.");
      }else{
         this.words = new TreeSet<String>();
         this.guessesLeft = max;
         this.guessed = new TreeSet<Character>();
         for(String i : dictionary){
            if(i.length() == length){
               this.words.add(i);
            }
         }
         if(words.size() > 0){
            this.pattern = "-";
            for(int i = 1 ; i < length ; i++){
               this.pattern += " -";
            }
         }
      }
   }
   
   //Returns the words still being considered.
   public Set<String> words(){
      return this.words;
   }
   
   
   //Returns the guesses left in the game.
   public int guessesLeft(){
      return this.guessesLeft;
   }
   
   
   //Returns the set of characters guessed.
   public SortedSet<Character> guesses(){
      return this.guessed;
   }
   
   
   //Returns the current pattern being displayed to the user unless it is empty in which case it 
   //throws an exception.
   public String pattern(){
      if(this.pattern != null){
         return this.pattern;
      }else{
         throw new IllegalStateException("pattern string is empty.");
      }
   }
   
   //Determines the new pattern based on the guessed character. Returns the number of time the 
   //guessed character occurs in the pattern.
   public int record(char guess){
      if(this.guessesLeft < 1 || this.words.isEmpty()){
         throw new IllegalStateException("game is over.");
      }else if(!this.words.isEmpty() && this.guessed.contains(guess)){
         throw new IllegalArgumentException("character already guessed.");
      }else{
         Map<String, Set<String>> patternMap = buildPatternMap(guess);
         String chosenPattern = choosePattern(patternMap);
         this.pattern = combinePattern(chosenPattern);
         this.words = patternMap.get(chosenPattern);
         int count = 0;
         for(int i = 0 ; i < chosenPattern.length() ; i++){
            if(chosenPattern.charAt(i) == guess){
               count++;
            }
         }
         this.guessed.add(guess);
         if(count == 0){
            this.guessesLeft--;
         }
         return count;
      }
   }
   
   //Returns a new map of possible patterns to the each remaining word; based on the guessed character.
   private Map<String, Set<String>> buildPatternMap(char guess){
      Map<String, Set<String>> patternMap = new TreeMap<String, Set<String>>();
      for(String currentWord : this.words){
         String key = "";
         if(currentWord.charAt(0) == guess){
            key += guess;
         }else{
            key += "-";
         }
         for(int u = 1 ; u < currentWord.length() ; u++){
            if(currentWord.charAt(u) == guess){
               key += " " + guess;
            }else{
               key += " -";
            }
         }
         if(patternMap.get(key) == null){
            patternMap.put(key, new TreeSet<String>());
         }
         patternMap.get(key).add(currentWord);
      }
      return patternMap;
   }
   
   
   //Returns the which pattern has the most possible words mapped to it.
   private String choosePattern(Map<String, Set<String>> patternMap){
      String chosen = null;
      Set<String> patternSet = patternMap.keySet();
      for(String i : patternSet){
         if(chosen == null || patternMap.get(i).size() > patternMap.get(chosen).size()){
            chosen = i;
         }
      }
      return chosen;
   }
   
   //Returns a combination of the new chosen pattern and the already shown pattern since the new 
   //chosen one didn't have the already revealed characters visible.
   private String combinePattern(String chosenPattern){
      String combinedPattern = "";
      for(int i = 0 ; i < this.pattern.length() ; i++){
         if(this.pattern.charAt(i) == '-' && chosenPattern.charAt(i) != '-'){
            combinedPattern += chosenPattern.charAt(i);
         }else{
            combinedPattern += this.pattern.charAt(i);
         }
      }
      return combinedPattern;
   }
}