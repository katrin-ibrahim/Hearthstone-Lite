package controller;

import view.View;

import javax.swing.*;

import model.cards.Card;
import model.cards.minions.Minion;
import model.cards.spells.*;
import model.heroes.*;
import engine.Game;
import engine.GameListener;
import exceptions.*;

import java.util.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.io.IOException;


public class Controller implements ActionListener,GameListener,ItemListener{
           
	
	//drop down
	private Hero [] heroes = {new Hunter(), new Mage(), new Paladin(), new Priest(), new Warlock()};
	private Hero [] heroes2 = {new Hunter(), new Mage(), new Paladin(), new Priest(), new Warlock()};
	private Hero p1;
	private Hero p2; 

	private Game model;
	private View view;
	


	
	//button arrays
	private ArrayList<JButton> currentFieldButtons = new ArrayList<JButton>();
	private ArrayList<JButton> opponentFieldButtons = new ArrayList <JButton>();
	private ArrayList<JButton> handButtons = new ArrayList<JButton>();
	private ArrayList<JButton> handoppButtons = new ArrayList<JButton>();
	
	//buttons
	private JButton selectedButton;
	private JButton heroName;
	private JButton opponentName;
	
	JTextArea spellDesc;
	
	
	


	public Controller()throws FullHandException,CloneNotSupportedException,IOException{
		
		view = new View();
		view.getEndTurn().addActionListener(this);
		view.getUseHeroPower().addActionListener(this);
		view.getStart().addActionListener(this);
		view.getClick().addActionListener(this);
		view.getCombobox().addItemListener(this);
		view.revalidate();
		view.repaint();
	 
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		boolean play = false;
		 JButton b = (JButton)e.getSource();
		 if (b == view.getClick() ){
	           	view.setClick(b);
	           	if(p1== null){
	           		String s = (String)(view.getCombobox().getSelectedItem()) ;
	           		p1 =heroes[view.getCombobox().getSelectedIndex()];
	           		switch(s){
	           		case("Mage"):view.getPlayerSelection().setText("Player 1 selected Mage");
	           		break;
	           		case("Paladin"):view.getPlayerSelection().setText("Player 1 selected Paladin");
	           		break;
	           		case("Priest"):view.getPlayerSelection().setText("Player 1 selected Priest");
	           		break;
	           		case("Warlock"):view.getPlayerSelection().setText("Player 1 selected Warlock");
	           		break;
	           		case("Hunter"):view.getPlayerSelection().setText("Player 1 selected Hunter");
	           		break;
	           		
	           		}
	           	}
	           	else if (p2 == null){
	           		String s = (String)(view.getCombobox().getSelectedItem()) ;
	           		p2= heroes2[view.getCombobox().getSelectedIndex()];
	           		switch(s){
	           		case("Mage"):view.getPlayerSelection().setText("Player 2 selected Mage");
	           		break;
	           		case("Paladin"):view.getPlayerSelection().setText("Player 2 selected Paladin");
	           		break;
	           		case("Priest"):view.getPlayerSelection().setText("Player 2 selected Priest");
	           		break;
	           		case("Warlock"):view.getPlayerSelection().setText("Player 2 selected Warlock");
	           		break;
	           		case("Hunter"):view.getPlayerSelection().setText("Player 2 selected Hunter");
	           		break;
	           		
	           		}
	           	}
		 }
	        if (p1!= null && p2!= null && b.getActionCommand().equals("start playing")){
	        	play = true;
	        	view.getFrame().dispose();
	        	try {
					model = new Game(p1,p2);
					model.setListener(this);
					  if(model.getCurrentHero()==p1)
						JOptionPane.showMessageDialog(null,"Player 1 will start", null, JOptionPane.INFORMATION_MESSAGE);
					  else
					    JOptionPane.showMessageDialog(null,"Player 2 will start", null, JOptionPane.INFORMATION_MESSAGE);
				} catch (FullHandException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
				} catch (CloneNotSupportedException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
				}
	        	SetHero ();	
	        	
	        } 	
	       
		 else if (b.getActionCommand().equals("Play Minion")){
			 int r = handButtons.indexOf(b);
			 Card m = model.getCurrentHero().getHand().get(r);
			 play = true;
			 try {
				model.getCurrentHero().playMinion((Minion)m);
			} catch (NotYourTurnException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
			} catch (NotEnoughManaException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
			} catch (FullFieldException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
			}
		 }
		 else if (b.getActionCommand().equals("End Turn")){
			 play = true;
			 try {
				model.getCurrentHero().endTurn();
			 }
              catch (FullHandException e1) {	  
                JOptionPane.showMessageDialog(null, e1.getMessage()+"\n"+e1.getBurned(), null, JOptionPane.ERROR_MESSAGE);
			} catch (CloneNotSupportedException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
			}
		 }
		 else if( b.getActionCommand().equals("Cast Spell")){
			
			 selectedButton = b ;
			 int r = handButtons.indexOf(selectedButton);
			 Spell s = (Spell)model.getCurrentHero().getHand().get(r);
			 try {
	            	playSpell(s,b);
				} catch (NotYourTurnException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
				} catch (NotEnoughManaException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
				} catch (InvalidTargetException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
				}
		 }
		 else if(b.getActionCommand().equals("Use Hero Power")){
			 play = true;
			 selectedButton = b ;
			 if(!(model.getCurrentHero() instanceof Mage) && !(model.getCurrentHero() instanceof Priest)){
				 try {
					model.getCurrentHero().useHeroPower();
				} catch (NotEnoughManaException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
				} catch (HeroPowerAlreadyUsedException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
				} catch (NotYourTurnException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
				} catch (FullHandException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage()+"\n"+e1.getBurned(), null, JOptionPane.ERROR_MESSAGE);
				} catch (FullFieldException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
				} catch (CloneNotSupportedException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
				}
			 }
		 }
		 else if (selectedButton != null && selectedButton.getActionCommand().equals("Cast Spell")){
			 play = true;
			 int r = handButtons.indexOf(selectedButton);
			 Spell s = (Spell)model.getCurrentHero().getHand().get(r);
			 try {
	            	playSpell(s,b);
				} catch (NotYourTurnException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
				} catch (NotEnoughManaException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
				} catch (InvalidTargetException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
				}
			 selectedButton = null; 
		 }
	        
		 else if (b.getActionCommand().equals("Attack"))
			  selectedButton = b;
		
		 else if(selectedButton != null && selectedButton.getActionCommand().equals("Attack")){
			 play = true;
		     int r = currentFieldButtons.indexOf(selectedButton);
		     Minion m = (Minion)model.getCurrentHero().getField().get(r);

		     try {
				attack(m,b);
		    } catch (CannotAttackException e1) {
		    	JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
			} catch (NotYourTurnException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
			} catch (TauntBypassException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
			} catch (InvalidTargetException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
			} catch (NotSummonedException e1) {
				JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
			}
		     selectedButton = null;
		 }  
		
		
		else if(selectedButton != null && selectedButton.getActionCommand().equals("Use Hero Power")){
			 play = true;
			if (currentFieldButtons.contains(b)){
				int i = currentFieldButtons.indexOf(b);
				Minion m = model.getCurrentHero().getField().get(i);
				Hero h = model.getCurrentHero();
				Priest pr = null;
				Mage ma =null;
				if(h instanceof Priest){
					 pr = (Priest)h;
					 try {
						pr.useHeroPower(m);
					} catch (NotEnoughManaException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					} catch (HeroPowerAlreadyUsedException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					} catch (NotYourTurnException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					} catch (FullHandException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage()+"\n"+e1.getBurned(), null, JOptionPane.ERROR_MESSAGE);
					} catch (FullFieldException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					} catch (CloneNotSupportedException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					}
					 selectedButton = null;
			    }
				if(h instanceof Mage){
					ma =(Mage)h;
					try {
						ma.useHeroPower(m);
					} catch (NotEnoughManaException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					} catch (HeroPowerAlreadyUsedException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					} catch (NotYourTurnException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					} catch (FullHandException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage()+"\n"+e1.getBurned(), null, JOptionPane.ERROR_MESSAGE);
					} catch (FullFieldException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					} catch (CloneNotSupportedException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					}
					 selectedButton = null;
				}
				 
		    }
		
			else if (opponentFieldButtons.contains(b)){
				int i = opponentFieldButtons.indexOf(b);
				Minion m = model.getOpponent().getField().get(i);
				Hero h = model.getCurrentHero();
				Priest pr = null;
				Mage ma =null;
				if(h instanceof Priest){
					 pr = (Priest)h;
					 try {
						pr.useHeroPower(m);
					} catch (NotEnoughManaException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					} catch (HeroPowerAlreadyUsedException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					} catch (NotYourTurnException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					} catch (FullHandException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage()+"\n"+e1.getBurned(), null, JOptionPane.ERROR_MESSAGE);
					} catch (FullFieldException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					} catch (CloneNotSupportedException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					}
			    }
				if(h instanceof Mage){
					ma =(Mage)h;
					try {
						ma.useHeroPower(m);
					} catch (NotEnoughManaException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					} catch (HeroPowerAlreadyUsedException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					} catch (NotYourTurnException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					} catch (FullHandException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage()+"\n"+e1.getBurned(), null, JOptionPane.ERROR_MESSAGE);
					} catch (FullFieldException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					} catch (CloneNotSupportedException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					}
				}		
		    }
			else if(b == heroName){
				Hero target = model.getCurrentHero();
				Hero h = model.getCurrentHero();
				Priest pr = null;
				Mage ma =null;
				if(h instanceof Priest){
					 pr = (Priest)h;
					 try {
						pr.useHeroPower(target);
					} catch (NotEnoughManaException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					} catch (HeroPowerAlreadyUsedException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					} catch (NotYourTurnException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					} catch (FullHandException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage()+"\n"+e1.getBurned(), null, JOptionPane.ERROR_MESSAGE);
					} catch (FullFieldException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					} catch (CloneNotSupportedException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					}
			    }
				if(h instanceof Mage){
					ma =(Mage)h;
					try {
						ma.useHeroPower(target);
					} catch (NotEnoughManaException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					} catch (HeroPowerAlreadyUsedException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					} catch (NotYourTurnException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					} catch (FullHandException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage()+"\n"+e1.getBurned(), null, JOptionPane.ERROR_MESSAGE);
					} catch (FullFieldException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					} catch (CloneNotSupportedException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					}
				}		
		    }
			else{
				Hero target = model.getOpponent();
				Hero h = model.getCurrentHero();
				Priest pr = null;
				Mage ma =null;
				if(h instanceof Priest){
					 pr = (Priest)h;
					 try {
						pr.useHeroPower(target);
					} catch (NotEnoughManaException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					} catch (HeroPowerAlreadyUsedException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					} catch (NotYourTurnException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					} catch (FullHandException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage()+"\n"+e1.getBurned(), null, JOptionPane.ERROR_MESSAGE);
					} catch (FullFieldException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					} catch (CloneNotSupportedException e1) {
						JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
					}
			    }
				
			
			if(h instanceof Mage){
				ma =(Mage)h;
				try {
					ma.useHeroPower(target);
				} catch (NotEnoughManaException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
				} catch (HeroPowerAlreadyUsedException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
				} catch (NotYourTurnException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
				} catch (FullHandException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage()+"\n"+e1.getBurned(), null, JOptionPane.ERROR_MESSAGE);
				} catch (FullFieldException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
				} catch (CloneNotSupportedException e1) {
					JOptionPane.showMessageDialog(null, e1.getMessage(), null, JOptionPane.ERROR_MESSAGE);
				}
			}		
		}
	}
		
			 if (p1 != null && p2 != null && play )
				 SetHero();
			
			 
   }
	
	
	
    public void SetHero (){
		
		Font small = view.getGameFont().deriveFont(16f);

		
		view.getOpponent().removeAll();
		view.getCurrentHero().removeAll();
		
		
	//*OPPONENT HERO*	
		//setting opponent hero image
		JButton imageOpponent = new JButton ();
		imageOpponent.setBounds(0,0,150,160);
		
		//setting opponent hero name
		opponentName = new JButton ();
		opponentName.addActionListener(this);
		opponentName.setBounds(0,160,150,45);
		
		
		//setting opponent hero image
		if(model.getOpponent() instanceof Paladin){
			opponentName.setFont(small);
			opponentName.setText("Uther Lightbringer");
			imageOpponent.setIcon(new ImageIcon("images/uther.png"));
			 
		}  
		if(model.getOpponent() instanceof Priest){
			opponentName.setFont(small);
			opponentName.setText("Anduin Wrynn");
			imageOpponent.setIcon(new ImageIcon("images/anduin.png"));
			
		}	
		if(model.getOpponent() instanceof Mage){
			opponentName.setFont(small);
			opponentName.setText("Jaina Proudmoore");
			imageOpponent.setIcon(new ImageIcon("images/jaina.png"));
			
		}
		if(model.getOpponent() instanceof Warlock){
			opponentName.setFont(view.getGameFont());
			opponentName.setText("Gul'dan");
			imageOpponent.setIcon(new ImageIcon("images/gul'dan.png"));
			
			
		}	
		if(model.getOpponent() instanceof Hunter){
			opponentName.setFont(view.getGameFont());
			opponentName.setText("Rexxar");
			imageOpponent.setIcon(new ImageIcon("images/rexxar.png"));
			
		}
	
		
		
		//setting opponent info
		JTextArea opponentInfo = new JTextArea (model.getOpponent().toString());
		opponentInfo.setBounds(155,0,160,205);
		opponentInfo.setFont(view.getGameFont());
		opponentInfo.setBackground(view.getInfoColor());
		
		//adding info,name and image to opponent panel
		view.getOpponent().add(opponentInfo);
		view.getOpponent().add(imageOpponent);
		view.getOpponent().add(opponentName);
		

		
		
	//*CURRENT HERO*
		//setting current hero image bounds
		JButton imageCurrent = new JButton ();
		imageCurrent.setBounds(0,0,150,160);
		
		
		//setting current hero name bounds
		heroName = new JButton ();
		heroName.addActionListener(this);
		heroName.setBounds(0,160,150,45);
		
		
        //setting current hero image and name + font
		if(model.getCurrentHero() instanceof Paladin){
			 heroName.setFont(small);
			 heroName.setText("Uther Lightbringer");
			 imageCurrent.setIcon(new ImageIcon("images/uther.png"));
			
		}  
		if(model.getCurrentHero() instanceof Priest){
			heroName.setFont(small);
			heroName.setText("Anduin Wrynn");
			imageCurrent.setIcon(new ImageIcon("images/anduin.png"));
			 
			
		}	
		if(model.getCurrentHero() instanceof Mage){
			heroName.setFont(small);
			heroName.setText("Jaina Proudmoore");
			imageCurrent.setIcon(new ImageIcon("images/jaina.png"));
			
		}
		if(model.getCurrentHero() instanceof Warlock){
			heroName.setFont(view.getGameFont());
			heroName.setText("Gul'dan");
			imageCurrent.setIcon(new ImageIcon("images/gul'dan.png"));
			
			
		}	
		if(model.getCurrentHero() instanceof Hunter){
			heroName.setFont(view.getGameFont());
			heroName.setText("Rexxar");
			imageCurrent.setIcon(new ImageIcon("images/rexxar.png"));
			 
		}
			
		//setting current hero info
		JTextArea currentInfo = new JTextArea (model.getCurrentHero().toString());
		currentInfo.setBounds(155,0,145,205);
		currentInfo.setFont(view.getGameFont());
		currentInfo.setBackground(view.getInfoColor());
	
		//adding to current hero panel
		view.getCurrentHero().add(heroName);	
		view.getCurrentHero().add(imageCurrent);
		view.getCurrentHero().add(currentInfo);
		
		SetHand();
		SetField();
		
		view.revalidate();
		view.repaint();
	}

	public void SetField() {
		
		view.getCurrentField().removeAll();
		view.getOpponentField().removeAll();

		currentFieldButtons = new ArrayList<JButton>();
		opponentFieldButtons = new ArrayList <JButton>();
		
		ArrayList<Minion> fieldCurrent = model.getCurrentHero().getField();
		ArrayList<Minion> fieldOpponent = model.getOpponent().getField();
		
		Font medium = view.getGameFont().deriveFont(20f);
		Font small = view.getGameFont().deriveFont(14f);
	
		
		
        
		
   //*Current Hero*
		for(int i = 0 ; i < fieldCurrent.size() ;i++){
			JPanel p = new JPanel();
			p.setBackground(view.getCardColor());
			int index = i*150;
			p.setBounds(index,10,140,220);
			p.setLayout(null);
			
	        
	        String s = fieldCurrent.get(i).cardOnField();
	        if(model.getCurrentHero().getField().get(i).isAttacked())
	        	s+= "\nATTACKED";
	        if(model.getCurrentHero().getField().get(i).isSleeping())
	        	s+="\nSleeping";
	        
	        
	        	
	        JTextArea cardInfo = new JTextArea (s);
	        cardInfo.setBackground(view.getCardColor());
	        cardInfo.setFont(medium);
	        cardInfo.setBounds(0,0,140,190);
	        
	        JButton action = new JButton();
	        action.setFont(medium);
	        action.setText("Attack");
	        action.addActionListener(this);
	        action.setBounds(0,190,140,30);
	        
	     
	        p.add(action);
        	p.add(cardInfo);    

            currentFieldButtons.add(action);
			view.getCurrentField().add(p);
			
			p.revalidate();
			p.repaint();
			
			view.revalidate();
			view.repaint();
			
		}
 //*OPPONENT*
		for(int i = 0 ; i < fieldOpponent.size() ;i++){
			JPanel p = new JPanel();
			int index = i*150;
			p.setBounds(index,5,140,220);
			p.setLayout(null);
			
	  
	        JTextArea cardInfo = new JTextArea (fieldOpponent.get(i).cardOnField());
	        cardInfo.setBackground(view.getCardColor());
	        cardInfo.setFont(medium);
	        cardInfo.setBounds(0,0,140,190);
	        
	        JButton action = new JButton();
	        action.setFont(small);
	        action.setText("Attack this minion");
	        action.addActionListener(this);
	        action.setBounds(0,190,140,30);
	        
	        
	        p.add(action);
        	p.add(cardInfo);    
            
            opponentFieldButtons.add(action);
            
			view.getOpponentField().add(p);
			
			p.revalidate();
			p.repaint();
			view.revalidate();
			view.repaint();
			
		}
	}
		
	public void SetHand() {
		view.getSpellPanel().removeAll();
		
		view.getCurrentHeroHand().removeAll();
		view.getOpponentHand().removeAll();
		
		Font small = view.getGameFont().deriveFont(16f);
		Font medium = view.getGameFont().deriveFont(20f);
		
		ArrayList<Card> handCurrent = model.getCurrentHero().getHand();
		handButtons = new ArrayList <JButton>();
		
		//*SPELL DESCRIPTION*
		
	    spellDesc = new JTextArea(model.getCurrentHero().spelltoString());
		spellDesc.setBackground(view.getInfoColor());
		spellDesc.setFont(small);
		spellDesc.setBounds(0,0,500,450);
		spellDesc.validate();
		view.getSpellPanel().add(spellDesc);
		
		
		for(int i = 0 ; i < handCurrent.size() ;i++){
			
		    
			JPanel p = new JPanel();
			p.setBackground(view.getCardColor());
			
			
			int index = i*150;
			p.setBounds(index,5,140,220);
			p.setLayout(null);
			
			JButton imageB = new JButton ();
	        JButton action = new JButton();
	        action.setFont(medium);
	        
	        if(handCurrent.get(i) instanceof Minion )
	        	action.setText("Play Minion");
	        else
	        	action.setText("Cast Spell");
	        action.addActionListener(this);
	        
	   
	        JTextArea cardInfo = new JTextArea (handCurrent.get(i).toString());
	        cardInfo.setBackground(view.getCardColor());
	        
	        
	        
	        
	        if(handCurrent.get(i) instanceof Minion ){
	        	
	        	cardInfo.setBounds(0,0,140,190);
	        	cardInfo.setFont(medium);
	        	action.setBounds(0,190, 140, 30);
	        	cardInfo.setEditable(false);
	        	
		        }
	        
	        else{
	        	imageB.setBounds(0,0,140,140);
	            
        	    imageB.setIcon(new ImageIcon("images/spell.png"));
        	    imageB.setBackground(Color.BLACK);
        	    cardInfo.setBounds(0, 140,150,50);
        	    cardInfo.setFont(small);
        	    action.setBounds(0,190, 140, 30);
        	    
	        	
        	    }
	        
	        
	        p.add(action);
        	p.add(cardInfo);    
            p.add(imageB);

    
            
            handButtons.add(action);
			view.getCurrentHeroHand().add(p);
			p.revalidate();
			p.repaint();
			view.revalidate();
			view.repaint();
		}
		
		
		
			
		ArrayList<Card> handOpponent = model.getOpponent().getHand();
		 for(int k = 0 ; k < handOpponent.size() ;k++){
		    JButton j = new JButton();
		    j.setBounds(k*150,5,140,200);
		    j.setIcon(new ImageIcon("images/images.jpg"));
            handoppButtons.add(j);
		    view.getOpponentHand().add(j);
		    view.revalidate();
			view.repaint();
	
	  }
		
	
	}

    public void playSpell(Spell s,JButton b) throws NotYourTurnException, NotEnoughManaException, InvalidTargetException{
		
		if(b.getActionCommand().equals("Cast Spell")){
		    if (s instanceof FieldSpell){
		    	model.getCurrentHero().castSpell((FieldSpell)s);
		    	SetHero();
		    	selectedButton = null; 
		    }
			   
		    else if(s instanceof AOESpell){
			   model.getCurrentHero().castSpell((AOESpell)s,model.getOpponent().getField());
		       SetHero();
		       selectedButton = null; 
		    }
		    
		}
		    
		
		else{
			Hero h = null;
	        if(currentFieldButtons.contains(b)){
			   int i = currentFieldButtons.indexOf(b);
			   Minion m = model.getCurrentHero().getField().get(i);
			
			if(s instanceof MinionTargetSpell)
				model.getCurrentHero().castSpell((MinionTargetSpell)s,m);
			if(s instanceof LeechingSpell)
				model.getCurrentHero().castSpell((LeechingSpell)s,m);
		}
		else if (opponentFieldButtons.contains(b)){
			int i = opponentFieldButtons.indexOf(b);
			Minion m = model.getOpponent().getField().get(i);
			
			if (s instanceof MinionTargetSpell) 
				model.getCurrentHero().castSpell((MinionTargetSpell)s,m);
			if (s instanceof LeechingSpell) 
				model.getCurrentHero().castSpell((LeechingSpell)s, m);
			
		}
		else if (b == heroName){
			h = model.getCurrentHero();
			model.getCurrentHero().castSpell((HeroTargetSpell)s,h);
		     }
		else {
			h = model.getOpponent();
			model.getCurrentHero().castSpell((HeroTargetSpell)s,h);	
			}      
	    }
		
	}
   
    public void attack (Minion m ,JButton b) throws CannotAttackException, NotYourTurnException, TauntBypassException, InvalidTargetException, NotSummonedException{
    	int i = 0;
    	if(currentFieldButtons.contains(b)){
    	    i  = currentFieldButtons.indexOf(b);
			Minion minionTarget = model.getCurrentHero().getField().get(i);
    		model.getCurrentHero().attackWithMinion(m ,minionTarget); 
    	}
         else if (opponentFieldButtons.contains(b)){
		    i = opponentFieldButtons.indexOf(b);
		    Minion minionTarget = model.getOpponent().getField().get(i);
		    model.getCurrentHero().attackWithMinion(m ,minionTarget);   
         }
         else if(b==heroName){
        	 Hero h = model.getCurrentHero();
        	 model.getCurrentHero().attackWithMinion(m, h); 
         }
         else {
        	 Hero h = model.getOpponent();
        	 model.getCurrentHero().attackWithMinion(m,h);

         }
    }

	public void itemStateChanged(ItemEvent event) {
		if (event.getSource()==view.getCombobox() ){
			if( p1 == null)
				view.setH(heroes[view.getCombobox().getSelectedIndex()]) ;
	        
				
			else if (p2 == null)
				view.setH(heroes2[view.getCombobox().getSelectedIndex()]);
			 
		}                                           	
	
	}
	

	@Override
	public void onGameOver() {
		if(model.getCurrentHero().getCurrentHP()==0){
			if(model.getOpponent()==p1)
				JOptionPane.showMessageDialog(null, "player 1 wins ", null, JOptionPane.INFORMATION_MESSAGE);
			else
				JOptionPane.showMessageDialog(null, "player 2 wins ", null, JOptionPane.INFORMATION_MESSAGE);	
		}
		else if(model.getOpponent().getCurrentHP()==0){
			if(model.getCurrentHero()==p1)
				JOptionPane.showMessageDialog(null, "player 1 wins ", null, JOptionPane.INFORMATION_MESSAGE);
			else
				JOptionPane.showMessageDialog(null, "player 2 wins ", null, JOptionPane.INFORMATION_MESSAGE);	
		}
		System.exit(0);
		
	}

	public static void main(String[]args) throws FullHandException, CloneNotSupportedException, IOException{
		new Controller();
	}



		
}



