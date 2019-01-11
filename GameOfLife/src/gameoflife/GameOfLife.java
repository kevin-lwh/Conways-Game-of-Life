
package gameoflife;

import java.io.*;
import java.util.Scanner;
import java.awt.*; 
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.*; 
import static javax.swing.JFrame.EXIT_ON_CLOSE;

public class GameOfLife extends JFrame {

    //FIELDS
    int numGenerations = 500;
    int currGeneration = 1;
    
    Color aliveColor = Color.YELLOW;
    Color deadColor = Color.BLUE;
    
    String fileName = "Initial cells.txt";

    int width = 1500; //width of the window in pixels
    int height = 1500;
    int borderWidth = 100;

    int numCellsX = 100; //width of the grid (in cells)
    int numCellsY = 100;
    int offsetY = 10;

    boolean alive[][] = new boolean [numCellsX][numCellsY]; //REPLACE null WITH THE CORRECT DECLARATION 
    boolean aliveNext[][] = new boolean [numCellsX][numCellsY]; //REPLACE null WITH THE CORRECT DECLARATION
    
    int cellWidth = (width-2*borderWidth)/numCellsX; //replace with the correct formula that uses width, borderWidth and numCellsX
     
    int labelX = width / 2;
    int labelY = borderWidth;
 
    
    //METHODS
    public void plantFirstGeneration() throws IOException {
        makeEveryoneDead();
        
        Random r = new Random();
        for (int i = 0; i < numCellsX; i++){
            for (int j = 0; j < numCellsY; j++){
                alive[i][j]= r.nextBoolean();
            }
        }
    }

    
    //Sets all cells to dead
    public void makeEveryoneDead() {
        for (int i = 0; i < numCellsX; i++) {
            for (int j = 0; j < numCellsY; j++) {
                alive[i][j] = false;
            }
        }
    }


    
    //Plants a solid rectangle of alive cells.  Would be used in place of plantFromFile()
    public void plantBlock(int startX, int startY, int numColumns, int numRows) {
        
        int endCol = Math.min(startX + numColumns, numCellsX);
        int endRow = Math.min(startY + numRows, numCellsY);

        for (int i = startX; i < endCol; i++) {
            for (int j = startY; j < endRow; j++) {
                alive[i][j] = true;
            }
        }
    }


    
    //Applies the rules of The Game of Life to set the true-false values of the aliveNext[][] array,
    //based on the current values in the alive[][] array
    public void computeNextGeneration() {
        
        for (int i = 0; i < numCellsX; i ++ ){
            for (int j = 0; j < numCellsY; j ++ ){
                
                int livingNeighbors = countLivingNeighbors(i,j);
                
                if (alive[i][j] == true){ //if the current cell is alive rule 1-3
                    if (livingNeighbors  < 2){ //rule 1
                        aliveNext[i][j] = false;
                    }
                    else if (livingNeighbors == 2 || livingNeighbors == 3){ //rule 2
                        aliveNext[i][j] = true;
                    }
                    else if (livingNeighbors > 3){ //rule 3
                        aliveNext[i][j] = false;
                    }
                    else {
                        aliveNext[i][j] = false;
                    }
                }
                
                else{ //if the current cell is dead
                    if (livingNeighbors == 3){ //rule 4
                        aliveNext[i][j] = true;
                    }
                    else{
                        aliveNext[i][j] = false; //if not, then stay dead
                    }
                }  
            }
        }
    }

    
    //Overwrites the current generation's 2-D array with the values from the next generation's 2-D array
    public void plantNextGeneration() {
        
        for (int i = 0; i < numCellsX; i ++){
            for (int j = 0; j < numCellsY; j ++){
                alive[i][j] = aliveNext[i][j];
            }
        }
    }

    
    //Counts the number of living cells adjacent to cell (i, j)
    public int countLivingNeighbors(int i, int j) {
        
        int count = 0;
        int xStart;
        int xEnd;
        int yStart;
        int yEnd;
        
        
        if (i == 0){ //the first row 
          
            if (j == 0){ //top left corner
                xStart = 0;
                xEnd = 2;
                yStart = 0;
                yEnd = 2;
            }
            else if( j == numCellsX-1 ){ //top right corner
                xStart = -1;
                xEnd = 0;
                yStart = 0;
                yEnd = 2;
            }
            else {
                xStart = -1;
                xEnd = 2;
                yStart = 0;
                yEnd = 2; 
            }
        }
        else if( i == numCellsY-1){ //the last row
            if (j == 0){ //bottom left corner
                xStart = 0;
                xEnd = 2;
                yStart = -1;
                yEnd = 1;
            }
            else if (j == numCellsX-1){ //bottom right corner
                xStart = -1;
                xEnd = 1;
                yStart = -1;
                yEnd = 1;
            }
            else {
                xStart = -1;
                xEnd = 1;
                yStart = -1;
                yEnd = 1;
            }
        }
        else if ( j == 0){ //the first column
            xStart = 0;
            xEnd = 2;
            yStart = -1;
            yEnd = 2;
        }
        else if ( j == numCellsX-1){ //the last column
            xStart = -1;
            xEnd = 1;
            yStart = -1;
            yEnd = 2;
        }
        else{
           xStart = -1;
           xEnd = 2;
           yStart = -1;
           yEnd = 2; 
        }
        
        for (int a = yStart; a < yEnd; a++){
            for (int b = xStart; b < xEnd; b++){
                if (a!=0 || b!=0){
                    if (alive[i+a][j+b]){
                        count += 1;
                    }
                }
            }
        }
        return count; 
    }

    
    //Makes the pause between generations
    public static void sleep(int duration) {
        try {
            Thread.sleep(duration);
        } 
        catch (Exception e) {}
    }

    
    //Displays the statistics at the top of the screen
    void drawLabel(Graphics g, int state) {
        g.setColor(Color.black);
        g.fillRect(0, 0, width, borderWidth);
        g.setColor(Color.white);
        g.setFont(new Font("Serif", Font.BOLD, 30));
        g.drawString("Generation: " + state, labelX-100, labelY);
        
    }

    
    //Draws the current generation of living cells on the screen
    public void paint( Graphics g){
        Image img = createImage();
        g.drawImage(img,8,30,this);
    }
    
    //Draws the current generation of living cells on the screen
    public Image createImage(){
        
        BufferedImage bufferedImage = new BufferedImage(width,height, BufferedImage.TYPE_INT_RGB);
        Graphics g = bufferedImage.getGraphics();
        int x, y, i, j;
        
        x = borderWidth;
        y = borderWidth + offsetY;

        drawLabel(g, currGeneration);

        for (i = 0; i < numCellsX; i++) {
            //Fill this in
            
            for (j = 0; j < numCellsY; j++) {
                
                if (alive[i][j] == true){
                    g.setColor(Color.green);
                }
                else{
                    g.setColor(Color.black);

                }
                
                g.fillRect(x, y, cellWidth, cellWidth);
                g.setColor( Color.white );
                g.drawRect(x, y, cellWidth, cellWidth);
                x += cellWidth;
                
            }
            
            x = borderWidth;
            y += cellWidth;
        }
        return bufferedImage;
    }


    //Sets up the JFrame screen
    public void initializeWindow() {
        setTitle("Game of Life Simulator");
        setSize(height, width);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setBackground(Color.black);
        setVisible(true); //calls paint() for the first time
    }
    
    
    //Main algorithm
    public static void main(String args[]) throws IOException {

        GameOfLife currGame = new GameOfLife();

        currGame.initializeWindow();
        currGame.plantFirstGeneration(); //Sets the initial generation of living cells, either by reading from a file or creating them algorithmically
        

        for (int i = 1; i <= currGame.numGenerations-1; i++) {
            currGame.repaint();
            currGame.computeNextGeneration(); //fills onNext using rules
            currGame.plantNextGeneration();//overwrite on with onNext
            currGame.currGeneration+=1;
            sleep(100);
            
        }
        
    } 
    
}
