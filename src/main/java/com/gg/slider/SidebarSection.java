package com.gg.slider;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.BorderFactory;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.plaf.basic.BasicArrowButton;

/**
 * Panel that contains both the title/header part and the content part.
 * 
 * @author oliver
 *
 */

public class SidebarSection extends JPanel {

	public static final int minComponentHeight = 40;
	public static final int minComponentWidth = 350;
	
	public JPanel titlePanel = new JPanel();
	
	private SideBar sideBarOwner;
	
	public JComponent contentPane; //sidebar section's content
	
	private ArrowPanel arrowPanel;
	
	private int calculatedHeight;

	/**
	 * Construct a new sidebar section with the specified owner and model. 
	 * 
	 * @param owner - SideBar
	 * @param model
	 */
	public SidebarSection(SideBar owner, 
			String text, 
			String supplementaryText, 
			JComponent component, Icon icon) {
		
		this.contentPane = component;
		
		sideBarOwner = owner;
		
		titlePanel.addMouseListener(new MouseAdapter() {
			public void mouseReleased(MouseEvent e) {
				
				if (SidebarSection.this != sideBarOwner.getCurrentSection()) {
					if (sideBarOwner.getCurrentSection() != null)
						sideBarOwner.getCurrentSection().collapse(true);
					
					expand(); //expand this!
				}
				else {
					collapse(true);
				}
			}
		});

		//absolute layout
//		setLayout(null);
		setLayout(new BorderLayout());
		
		add(titlePanel, BorderLayout.NORTH);

		JLabel l1 = new JLabel(text);

		titlePanel.setLayout(new FlowLayout(FlowLayout.LEADING));
		titlePanel.setPreferredSize(new Dimension(this.getPreferredSize().width, SidebarSection.minComponentHeight));
		titlePanel.setBorder(BorderFactory.createLineBorder(Color.GRAY));
		
		arrowPanel = new ArrowPanel(BasicArrowButton.EAST);
		arrowPanel.setPreferredSize(new Dimension(SidebarSection.minComponentWidth, SidebarSection.minComponentHeight-10));
		
		
		if (sideBarOwner.showArrow)
			//add into tab panel the arrow and labels.
			titlePanel.add(arrowPanel);
		
		
		titlePanel.add(new JLabel(icon));
		
		titlePanel.add(l1);

		this.setMinimumSize(new Dimension(minComponentWidth, minComponentHeight));
//		component.setPreferredSize(new Dimension(0,0));
		
		add(component, BorderLayout.CENTER);
		
		revalidate();
	}
	
	/**
	 * Overrides super.
	 */
	
	public void setBounds(int x, int y, int w, int h) {
		super.setBounds(x, y, w, h);
		
		titlePanel.setBounds(0, 0, w, SidebarSection.minComponentHeight);
		
		if (contentPane.isVisible())
			contentPane.setBounds(0, SidebarSection.minComponentHeight, w, contentPane.getPreferredSize().height);
		else
			contentPane.setBounds(0, 0, 0, 0);
	}
	


	public void expand() {
		
		sideBarOwner.setCurrentSection(this);
		
		System.out.println("Expanding ..!");
		
		arrowPanel.changeDirection(BasicArrowButton.SOUTH);
		arrowPanel.updateUI();
		
		
		calculatedHeight = -1;

		if (sideBarOwner.getMode().equals(SideBar.SideBarMode.MAXIMISE_CONTENT)){
			
			calculatedHeight = sideBarOwner.getSize().height;
			
		}else if (sideBarOwner.getMode().equals(SideBar.SideBarMode.MINIMISE_CONTENT)){
			
			//component minimium height
			int minHeight = SidebarSection.minComponentHeight + contentPane.getPreferredSize().height;

			calculatedHeight = minHeight;
		}
		
		
		
		System.out.println("sidebarSection.contentPane.getHeight() " + contentPane.getHeight());
		
		

		
		if (this.sideBarOwner.animate) {
			/**
			 * ANIMATION BIT
			 */
			SidebarAnimation anim = new SidebarAnimation(this, 200);
			
			anim.setStartValue(SidebarSection.minComponentHeight);
			anim.setEndValue(calculatedHeight);
			anim.start();
		}else {
			
			if (sideBarOwner.innerSideBar) {
				calculatedHeight = 1000;
				setMaximumSize(new Dimension(Integer.MAX_VALUE, calculatedHeight));
				setPreferredSize(new Dimension(Integer.MAX_VALUE, calculatedHeight));
				setMinimumSize(new Dimension(Integer.MAX_VALUE, calculatedHeight));
				
				sideBarOwner.setPreferredSize(new Dimension(Integer.MAX_VALUE, calculatedHeight));
				
				
				setSize(new Dimension(Integer.MAX_VALUE, calculatedHeight));
				validate();
				contentPane.setSize(new Dimension(Integer.MAX_VALUE, calculatedHeight));
				contentPane.setVisible(true);
				revalidate();
				updateUI();
				
			}else {
				calculatedHeight = calculatedHeight;
				setMaximumSize(new Dimension(Integer.MAX_VALUE, calculatedHeight));
				setPreferredSize(new Dimension(Integer.MAX_VALUE, calculatedHeight));
				setMinimumSize(new Dimension(Integer.MAX_VALUE, calculatedHeight));
				setSize(new Dimension(Integer.MAX_VALUE, calculatedHeight));
				
				contentPane.setVisible(true);
				revalidate();

			}
			
			
			printDimensions();
		}

		
		
	}
	
	
	
	public void collapse(boolean animate) {
		
		System.out.println("Collapsing ..!");
		
		//remove reference
		if (sideBarOwner.getCurrentSection() == SidebarSection.this)
			sideBarOwner.setCurrentSection(null);

		
		arrowPanel.changeDirection(BasicArrowButton.EAST);
		arrowPanel.updateUI();

		
		/**
		 * ANIMATION BIT
		 */
		SidebarAnimation anim = new SidebarAnimation(this, 200);

		
		
		
		if (animate && this.sideBarOwner.animate) {
			anim.setStartValue(calculatedHeight);
			anim.setEndValue(SidebarSection.minComponentHeight);
			anim.start();
		}else {
			setMaximumSize(new Dimension(Integer.MAX_VALUE, titlePanel.getPreferredSize().height));
			contentPane.setVisible(false);
			revalidate();
			printDimensions();
		}
	}

	public Dimension getMinimumSize(){
		return new Dimension(60,SidebarSection.minComponentHeight);
	}
	
	public Dimension getPreferredSize(){
		return new Dimension(60,SidebarSection.minComponentHeight);
	}
	
	public void printDimensions() {
		System.out.println("sideBar height " + this.sideBarOwner.getSize().height);

		System.out.println("sideBarSection height " + getSize().height);
		System.out.println("sideBarSection titlePanel height " + titlePanel.getSize().height);
		System.out.println("sideBarSection.contentPane height " + contentPane.getSize().height);
		
	}

	
}