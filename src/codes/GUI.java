package codes;

import java.util.*;
import javax.swing.*;
import java.awt.*;
import java.sql.*;

public class GUI {

    private final int SIZE = 90;
    private final int QUANTITY = 5;


    private JFrame frame;
    private JLabel matchLabel;
    private JLabel resultLabel;
    private JButton drawButton;
    private JButton exitButton;
    private JPanel mainPanel;
    private JPanel northPanel;
    private JPanel eastPanel;
    private JPanel southPanel;
    private JPanel westPanel;
    private JPanel centerPanel;

    private ArrayList<JToggleButton> numbers;
    private ArrayList<Integer> selectedNumbers;
    private ArrayList<Integer> drawnNumbers;

    private DatabaseConnector dbc = new DatabaseConnector();

    public GUI(){
    
        initComponents();
        frame.setVisible(true);

    }

    private void initComponents(){
        createFrame();
        createLabels();
        createButtons();
        createPanels();

        generateBoxes();
        renderBoxes();
    }
    private void createFrame(){
        frame = new JFrame();
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }

    private void createLabels(){
        matchLabel = new JLabel("Találatok: ");
        resultLabel = new JLabel("Számok: ");
    }

    private void createButtons(){
        drawButton = new JButton("Sorsolás");
        drawButton.addActionListener(e -> drawNumbers());
        drawButton.setEnabled(false);
        exitButton = new JButton("Kilépés");
        exitButton.addActionListener(e -> closeProgram());
    }

    private void createPanels(){
        mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());

        northPanel = new JPanel();
        northPanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        northPanel.add(matchLabel);


        eastPanel = new JPanel();
        eastPanel.setSize(10, 500);

        southPanel = new JPanel();
        southPanel.add(resultLabel);
        southPanel.add(drawButton);
        southPanel.add(exitButton);


        westPanel = new JPanel();
        westPanel.setSize(10, 500);

        centerPanel = new JPanel();
        centerPanel.setLayout(new GridLayout(10,9));

        mainPanel.add(northPanel, BorderLayout.NORTH);
        mainPanel.add(eastPanel, BorderLayout.EAST);
        mainPanel.add(southPanel, BorderLayout.SOUTH);
        mainPanel.add(westPanel, BorderLayout.WEST);
        mainPanel.add(centerPanel, BorderLayout.CENTER);

        frame.add(mainPanel);

    }

    private void generateBoxes(){
        numbers = new ArrayList<>();
        for(int i=1;i<SIZE+1;i++){
            JToggleButton box = new JToggleButton();
            box.setText(i+"");
            box.addActionListener(event->checkSelectedBoxes());
            numbers.add(box);
        }
    }

    private void renderBoxes(){
        numbers.forEach((num)->{
            centerPanel.add(num);
        }
    );
    }

    private void closeProgram(){
        System.exit(0);
    }
    private void checkSelectedBoxes(){
        int selectedCounter = 0;
        for(int i=0;i<SIZE;i++){
            if(numbers.get(i).isSelected()){
                selectedCounter++;
            } 
        }
        if(selectedCounter == QUANTITY){
            drawButton.setEnabled(true);
        }else{
            drawButton.setEnabled(false);
        }

    }

    private void generateRandomNumbers(){
        drawnNumbers = new ArrayList<>();
        Random rand = new Random();
        for(int i=0;i<QUANTITY;i++){
            int randomNumber = rand.nextInt(90) + 1;
            if(!drawnNumbers.contains(randomNumber)){
                drawnNumbers.add(randomNumber);
                System.out.println(randomNumber);
            }else{
                i--;
            }
        }
    }

    private void drawNumbers(){
        generateRandomNumbers();
        selectedNumbers = new ArrayList<>();
        for(int i=0;i<SIZE;i++){
            if(numbers.get(i).isSelected()){
                selectedNumbers.add(i);
            }
        }
        checkMatches();
        showDrawnNumbers();
        loadNumbersToDatabase();
    }

    private void checkMatches(){
        int matchedCounter = 0;
        for(int i=0;i<selectedNumbers.size();i++){
            for(int j=0;j<drawnNumbers.size();j++){
                if(selectedNumbers.get(i) == drawnNumbers.get(j)){
                    matchedCounter++;
                }
            }
        }
        System.out.println(matchedCounter);
        matchLabel.setText("Találatok: "+matchedCounter);
    }
    private void showDrawnNumbers(){
        String result = "";
        for(int i=0;i<drawnNumbers.size();i++){
            result += drawnNumbers.get(i) + " ";
        }
        resultLabel.setText("Számok: "+result);
    }
    private void loadNumbersToDatabase(){
        Connection conn = dbc.getConnection();
        Statement stmt = null;
        String sqlData = "";
        for(int i=0;i<drawnNumbers.size();i++){
            if(i < (drawnNumbers.size()-1)){
                sqlData += String.valueOf(drawnNumbers.get(i)) + ":";
            }else{
                sqlData += String.valueOf(drawnNumbers.get(i));
            }
        }
        String sql = "INSERT INTO drawns(drawn) VALUES ('"+sqlData+"');";
        try{
            stmt = conn.createStatement();
            stmt.executeQuery(sql);
        }catch(SQLException ex){
            ex.printStackTrace();
        }
    }
}