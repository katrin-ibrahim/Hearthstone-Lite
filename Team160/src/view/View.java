package view;
import javax.swing.*;

import model.heroes.Hero;

import java.awt.*;
import java.io.File;
import java.io.IOException;


@SuppressWarnings("serial")
public class View extends JFrame {
	
	//drop down menu
	private String [] options = {"Hunter", "Mage","Paladin","Priest","Warlock"};
	private JComboBox<String> combobox = new JComboBox <String>(options);
	private JFrame frame = new JFrame();
	private Hero h;
	private JButton Click;

	
	//main containers
	private JPanel opponentPanel;
	private JPanel fieldContainer;
	private JPanel currentHeroPanel;
	private JPanel spellPanel ;
	private JLabel playerSelection;
	
	
	//secondary components
	private JPanel currentField;
	private JPanel opponentField;
	
	private JPanel currentHeroHand;
	private JPanel opponentHand;
	
	//hero info container
    private JPanel currentHero;
    private JPanel opponent;
    
	//hero info text areas
	private JTextArea currentHeroInfo;
	private JTextArea opponentInfo;
		
	
	//buttons
	private JButton endTurn;
	private JButton useHeroPower;
	private JButton start;
	
	//colors and fonts
	private Color colorCurrent = new Color (41,61,124);
	private Color colorOpponent = new Color (31,45,92);
	private Color infoColor = new Color(20,204,255);
	private Color cardColor = new Color(232,185,35);

	Font gameFont ;
	
	
	
	
	
	public View () {
		//fonts
		try{
			gameFont = Font.createFont(Font.TRUETYPE_FONT, new File("fonts\\LifeCraft_Font.ttf")).deriveFont(25f);
			GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
			ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, new File("LifeCraft_Font.ttf")));
		}
		catch(IOException |FontFormatException e){
			
		}	    
		
		//view configuration
		this.setSize(1800,1000);
		this.setTitle("Hearthstone");
		this.setVisible(true);
		this.setResizable(false);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setLayout(null);
		
		
		//main containers
		opponentPanel = new JPanel ();
		opponentPanel.setBounds(0,0,1800,210);
		opponentPanel.setBackground(colorOpponent);
		opponentPanel.setLayout(null);
		
		spellPanel = new JPanel();
		spellPanel.setLayout(null);
		spellPanel.setBounds(1500,210,300,450);
		spellPanel.setBackground(infoColor);
		add(spellPanel);
		
		
		fieldContainer = new JPanel();
		fieldContainer.setBounds(0,210,1500,500);
		fieldContainer.setBackground(colorOpponent);
		fieldContainer.setLayout(null);
		
		currentHeroPanel = new JPanel ();
		currentHeroPanel.setBounds(0,710,1800,290);
		currentHeroPanel.setBackground(colorCurrent);
		currentHeroPanel.setLayout(null);
		
		
		
		
		
    	//hero Panels
		opponent = new JPanel ();
		opponent.setLayout(null);
		opponent.setBounds(5,5,290,270);
		opponent.setBackground(colorOpponent);
		opponentPanel.add(opponent);
		
		
		currentHero = new JPanel ();
		currentHero.setLayout(null);
		currentHero.setBounds(5,5,290,270);
		currentHero.setBackground(colorCurrent);
		currentHeroPanel.add(currentHero);

		
		// hero hands
		opponentHand = new JPanel ();
		opponentHand.setBounds(305,0,1600,210);
		opponentHand.setLayout(null);
		opponentHand.setBackground(colorOpponent);
		opponentPanel.add(opponentHand);
		
		currentHeroHand = new JPanel ();
		currentHeroHand.setBounds(305,0,1700,285);
		currentHeroHand.setLayout(null);
		currentHeroHand.setBackground(colorCurrent);
		currentHeroPanel.add(currentHeroHand);
	
		//adding fields to field container
		opponentField = new JPanel();
		opponentField.setBounds(5,10,1500,240);
		opponentField.setLayout(null);
		opponentField.setBackground(colorOpponent);
		fieldContainer.add(opponentField);
		
		currentField = new JPanel ();
		currentField.setBounds(5,250,1500,250);
		currentField.setLayout(null);
		currentField.setBackground(colorCurrent);
		fieldContainer.add(currentField);
		
		
		//adding buttons
		endTurn = new JButton();
		endTurn.setBounds(1650,660,150,50);
		endTurn.setText("End Turn");
		endTurn.setFont(gameFont);
		this.add(endTurn);
		
	    Font medium = gameFont.deriveFont(20f);
		useHeroPower = new JButton ();
		useHeroPower.setBounds(1500,660,150,50);
		
		useHeroPower.setFont(medium);
		useHeroPower.setText("Use Hero Power");
		this.add(useHeroPower);
		
	
		
		this.add(opponentPanel);
		this.add(fieldContainer);
		this.add(currentHeroPanel);
		
		
		
		//drop down configuration		
	    frame.setPreferredSize(new Dimension (300,300));
	    //frame.setLayout(new FlowLayout());
	    frame.setLayout(null);
	    frame.setSize(700, 300);
	    JPanel a = new JPanel();
	    a.setBackground(cardColor);;
	    a.setSize(700,300);
	    a.setLayout(new FlowLayout());
	    frame.setResizable(false);
	    frame.add(a);
	 
	    JLabel wel = new JLabel ("welcome to hearthstone please select who you would like to play as\n");
	    wel.setFont(gameFont);
	    a.add(wel);
	    
	    playerSelection = new JLabel();
	    playerSelection.setFont(getGameFont());
	    a.add(playerSelection);
	    
	    start = new JButton("start playing");
	    a.add(start);
	    
	    Click = new JButton("Choose");
		a.add(Click);
		
	    combobox.setSelectedItem(null);
	    a.add(combobox);
	
	    frame.setTitle("Choose Hero");
		frame.setVisible(true);
		frame.setAlwaysOnTop(true);
		
		
	    frame.setDefaultCloseOperation (JFrame.EXIT_ON_CLOSE);
	    
	   // frame.setBackground(colorCurrent);
		frame.revalidate();
		frame.repaint();
	    
	    this.revalidate();
		this.repaint();
	}




	public JComboBox<String> getCombobox() {
		return combobox;
	}




	public JButton getStart() {
		return start;
	}




	public void setCombobox(JComboBox<String> combobox) {
		this.combobox = combobox;
	}




	public Hero getH() {
		return h;
	}




	public void setH(Hero h) {
		this.h = h;
	}




	public JButton getClick() {
		return Click;
	}




	public void setClick(JButton click) {
		Click = click;
	}




	public String[] getOptions() {
		return options;
	}




	public JFrame getFrame() {
		return frame;
	}




	public JPanel getOpponentPanel() {
		return opponentPanel;
	}




	public JPanel getFieldContainer() {
		return fieldContainer;
	}




	public JPanel getCurrentHeroPanel() {
		return currentHeroPanel;
	}




	public JPanel getCurrentField() {
		return currentField;
	}




	public JPanel getOpponentField() {
		return opponentField;
	}




	public JPanel getCurrentHeroHand() {
		return currentHeroHand;
	}




	public JPanel getOpponentHand() {
		return opponentHand;
	}




	public JPanel getCurrentHero() {
		return currentHero;
	}




	public JPanel getOpponent() {
		return opponent;
	}




	public JTextArea getCurrentHeroInfo() {
		return currentHeroInfo;
	}




	public JTextArea getOpponentInfo() {
		return opponentInfo;
	}




	public JButton getEndTurn() {
		return endTurn;
	}




	public JButton getUseHeroPower() {
		return useHeroPower;
	}




	public Color getColorCurrent() {
		return colorCurrent;
	}




	public Color getColorOpponent() {
		return colorOpponent;
	}




	public Font getGameFont() {
		return gameFont;
	}

	

	public JPanel getSpellPanel() {
		return spellPanel;
	}




	public static void main (String [] args) throws  CloneNotSupportedException, IOException{
		 new View();
		

	}




	public JLabel getPlayerSelection() {
		return playerSelection;
	}




	public Color getInfoColor() {
		return infoColor;
	}




	public Color getCardColor() {
		return cardColor;
	}
	
	
	

}
