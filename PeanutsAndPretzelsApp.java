/*
 * @author Daniel Peek
 * @Alice Knowles
 * @Patrick Skinner
 * @Kahurangi Cassidy
 * 
 */
import java.io.*;
import java.util.*;  

/*
 * PeanutsAndPretzels is a class that outputs a possible move for a Peanuts and Pretzels scenario.
 */
public class PeanutsAndPretzels{
  
  
  public static void main(String [] args){
    PeanutsAndPretzels p = new PeanutsAndPretzels();
    int [] snackStock = new int[2];         
    ArrayList<String> inputs = new ArrayList<String>();
    inputs = p.readInputs();
    
    while (!inputs.isEmpty()){ 
      
      ArrayList<String> inputsLeftovers = new ArrayList<String>();
      boolean finalRound = false;
      int multipleRounds = 0; 
      
      for (int i=0; i<inputs.size(); i++){ 
        
        if (inputs.get(i).equals("")){ 
          if (multipleRounds == 0){
            multipleRounds = i;
          }
        }
        
        if (multipleRounds !=0){
          if (i!=multipleRounds){
            inputsLeftovers.add(inputs.get(i));
            inputs.remove(i);
            i--;
          }
        }
        
        else if (multipleRounds==0 && i==inputs.size()-1){
          finalRound = true;
        }
      }
      
      if (finalRound== false){
        for(; multipleRounds<inputs.size(); multipleRounds++){
          inputs.remove(multipleRounds);
        }
      }
      snackStock = p.getSnackStock(inputs);     
      inputs.remove(0);                    
      
      p.findMove(snackStock, inputs, p.makeTable(snackStock, inputs));
      inputs = inputsLeftovers;
    }
  }
  
  
  
  /*
   * Method which creates the a 2D array assigning values (0 or 1) based
   * upon the given moves.
   * @parram snackStock, snackStock is the initial value of peanuts and pretzels.
   * @parram inputs, is the total input for each scenario including peanut and pretzel values.
   * @return table, table is the 2D array where all values have been assigned based
   * upon the given inputs.
   */
  public int [][] makeTable (int[] snackStock, ArrayList<String> inputs){
    int [][] table = new int[snackStock[0]*2][snackStock[1]*2];
    ArrayList<int[]> moves = getMoves(inputs, snackStock);
    for(int i=0; i<=snackStock[0]; i++){
      for(int j=0; j<=snackStock[1]; j++){
        for(int[] move: moves){
          if(j - move[1] > -1 && i - move[0] > -1 && table[i - move[0]][j - move[1]] == 0){
            table[i][j] = 1;
            break;
          } else if(j - move[1] > -1 && i - move[0] > -1 && table[i - move[0]][j - move[1]] != 0) table[i][j] = 0;
        }
        table[0][0] = 0;
      }
    }
    return table;
  }
  
  
  
  
  /*
   * The findMove method tests if there is any moves that can be made from the starting position
   * that would ensure the starting person to win. If there are moves that can be made, it takes 
   * the first move in that list, otherwise it just prints "0 0".
   * @parram snackStock, snackStock is the initial value of peanuts and pretzels.
   * @parram inputs, is the total input for each scenario including peanut and pretzel values.
   * @parram table, this is the 2D array where all values have been assigned based 
   * upon the given inputs.
   */
  public void findMove(int[] snackStock, ArrayList<String> inputs, int [][] table){
    ArrayList<int[]> list = new ArrayList<int[]>();
    ArrayList<int[]> moves = getMoves(inputs, snackStock);
    
    for (int[] move: moves){
      if (table[snackStock[0]-move[0]][snackStock[1]-move[1]]==0) list.add(move);
    } 
    
    if (list.size() != 0) {
      int p1 = list.get(0)[0];
      int p2 = list.get(0)[1];
      System.out.println(p1+" "+p2);
    } else  System.out.println("0 0");
  }
  
  
  
  
  /*
   * The doubleSign method deals with scenarios in the input which involve two of the symbols '<' or '>'.
   * @parram specialMoves, specialMoves is the current list of moves that involve a symbol
   * @parram snackStock, snackStock is the initial value of peanuts and pretzels.
   * @parram s, an array of all string inputs.
   * @parram sign1, the input value from the list of possible moves that involves a symbol.
   * @parram sign2, the input value from the list of possible moves that involves a symbol.
   * @return specialMoves, a list of all the special moves which involve two symbols.
   */
  private ArrayList<int []> doubleSign (ArrayList<int[]> specialMoves, int[] snackStock, String [] s, char sign1, char sign2){
    int xlimit=0, ylimit=0, xstart=0, ystart=0;
    
    if (sign1 == '>') {
      xlimit = snackStock[0];
      xstart = Integer.parseInt(s[0].substring(1));
    } else  if (snackStock[0] < Integer.parseInt(s[0].substring(1))-1){
      xlimit = snackStock[0];
      xstart =0;
    } else {
      xlimit = Integer.parseInt(s[0].substring(1))-1;
      xstart =0;
    }
    
    if (sign2 == '>') {
      ylimit = snackStock[1];
      ystart = Integer.parseInt(s[1].substring(1))-1;
    } else {
      ylimit = Integer.parseInt(s[1].substring(1))-1;
      ystart = 0;
    }
    
    for (int x=0; xstart+x<= xlimit; x++){
      for (int y=0; ystart+y<= ylimit; y++){
        int [] temp = new int[2];
        temp[0] = xstart+x;
        temp[1] = ystart+y;
        specialMoves.add(temp);
      }
    }
    
    return specialMoves;
  }
  
  
  
  /*
   * The oneSign method deals with scenarios in the input which involve one of the symbols '<' or '>'.
   * @parram specialMoves, specialMoves is the current list of moves that involve a symbol
   * @parram snackStock, snackStock is the initial value of peanuts and pretzels.
   * @parram s, an array of all string inputs.
   * @parram sign, the input value from the list of possible moves that involves a symbol.
   * @return specialMoves, a list of all the special moves which involve two symbols.
   */
  public ArrayList<int[]> oneSign (ArrayList<int[]> specialMoves, int j, String[] s, int[] snackStock, char sign){
    int limit=0, start=0, index1=0, index2=0;
    
    if (sign == '>'){
      limit = snackStock[j];
      start = Integer.parseInt(s[j].substring(1));
    } else if (snackStock[j] <Integer.parseInt(s[j].substring(1))-1){
      limit = snackStock[j];
      start = 0;
    } else{
      limit = Integer.parseInt(s[j].substring(1))-1;
      start = 0;
    }
    
    if (j==0){
      index1=0;
      index2=1;
    } else{
      index1=1;
      index2=0;
    }
    
    for (int i=0; start+i <= limit; i++){
      int [] temp = new int[2];
      temp[index1] = start+i;
      temp[index2] = Integer.parseInt(s[index2]);
      specialMoves.add(temp);
    }
    
    return specialMoves;
  }
  
  
  
  
  /*
   * This method gives a complete list of all the possible moves, including the lists
   * returned from oneSign and twoSign that contains all special moves.
   * @parram snackStock, snackStock is the initial value of peanuts and pretzels.
   * @parram inputs, is the total input for each scenario including peanut and pretzel values.
   * @return moves, the complete list of all moves.
   */
  public ArrayList<int []> getMoves(ArrayList<String> inputs, int[] snackStock){
    ArrayList<int []> moves = new ArrayList<int []>();
    
    for (int i=0; i<inputs.size(); i++){                             
      String [] s = inputs.get(i).split("\\s+");
      if (s[0].charAt(0) == '>' || s[0].charAt(0) == '<'){                   
        if (s[1].charAt(0) == '>' || s[1].charAt(0) == '<'){                            
          moves = doubleSign(moves, snackStock, s, s[0].charAt(0), s[1].charAt(0));
        } else {
          moves = oneSign(moves, 0, s, snackStock,  s[0].charAt(0));
        }
      } else if ((s[1].charAt(0) == '>' || s[1].charAt(0) == '<')){
        moves = oneSign(moves, 1, s, snackStock,  s[1].charAt(0));
      } else {
        if ((Integer.parseInt(s[0]) <= snackStock[0]) && (Integer.parseInt(s[1]) <= snackStock[1])){
          int [] temp = new int[2];
          temp[0] = Integer.parseInt(s[0]);
          temp[1] = Integer.parseInt(s[1]);
          moves.add(temp);
        }
      }
    }
    
    int [] singleMove1 = new int[2]; singleMove1[0] = 0; singleMove1[1] =1;
    int [] singleMove2 = new int[2]; singleMove2[0] = 1; singleMove2[1] =0;
    moves.add(singleMove1); moves.add(singleMove2);
    
    return moves;
  }
  
  
  
  /* This method takes the given input, then takes the first line of that input
   * and splits it into an array. The first value represents the initial peanuts
   * count, while the second value represents the initial pretzels count.
   * @parram inputs, a list of the inputed strings.
   * @return snackStock, an array containing the values of peanuts and pretzels.
   */
  public int[] getSnackStock(ArrayList<String> inputs){
    int [] snackStock = new int[2];
    String [] snackStockString = inputs.get(0).split("\\s+");
    for(int i=0; i<snackStock.length; i++) snackStock[i] = Integer.parseInt(snackStockString[i]);
    return snackStock;
  }
  
  
  
  /*
   * This method reads the input file and splits the input based upon lines, and puts 
   * each line into an arraylist.
   * @return inputs, the list of input value.
   */
  public ArrayList<String> readInputs(){
    ArrayList<String> inputs = new ArrayList<String>();
    String input;
    
    try{
      
      BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
      while((input = br.readLine()) != null) inputs.add(input);
      
    } catch (IOException io) {
      io.printStackTrace();
    }
    
    return inputs;
  }
  
}