/*
 * @author Brian Knotten (Github: "BK874"). Primary author of this file, used originally in the "BitsPlease" repository.
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.ArrayList;
import java.util.ListIterator;
import java.io.Serializable;

public class BoardPanel implements Serializable {
    private static final long serialVersionUID = 1L;
    // Data for the BoardPanel
	private ArrayList<BoardSpace> spaces = new ArrayList<BoardSpace>();
    private static final Font BUTTON_FONT = new Font("Courier", Font.PLAIN, 48);
    private static final int BOARD_COLUMNS = 8;
    private static final int BOARD_ROWS = 8;
    private static final int BOARD_HORIZONTAL_GAP = 20;
    private static final int BOARD_VERTICAL_GAP = 10;
    private static final GridLayout BOARD_LAYOUT = new GridLayout(BOARD_ROWS, BOARD_COLUMNS, BOARD_VERTICAL_GAP, BOARD_HORIZONTAL_GAP);
    private static final int NUM_SPACES = BOARD_COLUMNS * BOARD_ROWS;
    private static final int MID_SPACE = (NUM_SPACES / 2); // The middle space. When even # spaces, the one closer to the final space

    private static final int SPECIAL_MOD = (NUM_SPACES / 5); // The mod for the special spaces. They will be separated by this number of spaces
    private static final int FIRST_SPECIAL = SPECIAL_MOD;
    private static final int SECOND_SPECIAL = SPECIAL_MOD * 2;
    private static final int THIRD_SPECIAL = SPECIAL_MOD * 3;
    private static final int FOURTH_SPECIAL = SPECIAL_MOD * 4;
    private static final int FIFTH_SPECIAL = SPECIAL_MOD * 5;

    private static final String START_SPACE_TEXT = "Start ->";
    private static final String GRANDMAS_HOUSE_TEXT = "-> Grandma's House";
    private static final String FIRST_SPECIAL_SPACE_TEXT = "Candy Cane Forest";
    private static final String SECOND_SPECIAL_SPACE_TEXT = "Minty Mountains";
    private static final String THIRD_SPECIAL_SPACE_TEXT = "Bubble Gum Trapeze";
    private static final String FOURTH_SPECIAL_SPACE_TEXT = "Marshmallow Marsh";
    private static final String FIFTH_SPECIAL_SPACE_TEXT = "Licorice Lagoon";

    private transient JPanel mainPanel;

    public BoardPanel(Player[] players){
		this.initializeMainPanel();

		// ----------------- //
		// Create the Spaces //
		// ----------------- //
		// Create the "Start" space
		BoardSpace newStart = new BoardSpace(Color.WHITE, new JLabel(START_SPACE_TEXT), players);
		newStart.setAsStartSpace(true);
		spaces.add(newStart);

		// Create all of the colored spaces
		for(int i = 1; i < NUM_SPACES-1; i++){
		    Color backgroundColor = Color.WHITE;
			switch(i % 5){
			    case 0: backgroundColor = Color.ORANGE; break;
			    case 1: backgroundColor = Color.RED; break;
			    case 2: backgroundColor = Color.YELLOW; break;
			    case 3: backgroundColor = Color.BLUE; break;
			    case 4: backgroundColor = Color.GREEN; break;
		    }

		    // Check if this space is a special square.
		    JLabel newLabel = new JLabel("");
		    switch(i){
			    case FIRST_SPECIAL: newLabel = new JLabel(FIRST_SPECIAL_SPACE_TEXT);
				backgroundColor = Color.MAGENTA;
				break;
			    case SECOND_SPECIAL: newLabel = new JLabel(SECOND_SPECIAL_SPACE_TEXT);
				backgroundColor = Color.CYAN;
				break;
			    case THIRD_SPECIAL: newLabel = new JLabel(THIRD_SPECIAL_SPACE_TEXT);
				backgroundColor = Color.PINK;
				break;
			    case FOURTH_SPECIAL: newLabel = new JLabel(FOURTH_SPECIAL_SPACE_TEXT);
				backgroundColor = Color.GRAY;
				break;
			    case FIFTH_SPECIAL: newLabel = new JLabel(FIFTH_SPECIAL_SPACE_TEXT);
				backgroundColor = Color.BLACK;
				break;
			    default: break;
		    }

		    // Add a color-appropriate text color
		    Color textColor = Color.BLACK;
		    if(backgroundColor.equals(Color.RED)){
		    	textColor = Color.BLACK;
		    } else if(backgroundColor.equals(Color.RED)){
		    	textColor = Color.WHITE;
		    } else if(backgroundColor.equals(Color.YELLOW)){
		    	textColor = Color.BLACK;
		    } else if(backgroundColor.equals(Color.BLUE)){
		    	textColor = Color.WHITE;
		    } else if(backgroundColor.equals(Color.GREEN)){
		    	textColor = Color.BLACK;
		    } else if(backgroundColor.equals(Color.ORANGE)){
		    	textColor = Color.BLACK;
		    } else if(backgroundColor.equals(Color.MAGENTA)){
				textColor = Color.BLACK;
		    } else if(backgroundColor.equals(Color.CYAN)){
				textColor = Color.BLACK;
		    } else if(backgroundColor.equals(Color.PINK)){
				textColor = Color.BLACK;
		    } else if(backgroundColor.equals(Color.GRAY)){
				textColor = Color.WHITE;
		    } else if(backgroundColor.equals(Color.BLACK)){
				textColor = Color.WHITE;
		    }
		    newLabel.setForeground(textColor);
		    BoardSpace newSpace = new BoardSpace(backgroundColor, newLabel);
		    spaces.add(newSpace);
		}

		// Create the end space ("Grandma's House")
		BoardSpace newGrandma = new BoardSpace(Color.WHITE, new JLabel(GRANDMAS_HOUSE_TEXT));
		newGrandma.setAsGrandmasHouse(true);

		spaces.add(newGrandma);

		// --------------------------- //
		// Add the Spaces to the board //
		// --------------------------- //
		this.refreshBoardPanel();
    }

    public JPanel getPanel(){
    	if(mainPanel == null){
    		this.initializeMainPanel();
    	}

    	return mainPanel;
    }

    private void initializeMainPanel(){
    	mainPanel = new JPanel();
    	mainPanel.setLayout(BOARD_LAYOUT);
    }

    public void refreshBoardPanel(){
        if(mainPanel == null){
            initializeMainPanel();
        }
        
		mainPanel.removeAll();
		mainPanel.revalidate();
		mainPanel.repaint();

		for(BoardSpace space : spaces){
			mainPanel.add(space.getPanel());
			space.updateText();
		}
		mainPanel.revalidate();
		mainPanel.repaint();
    }

    public void add(BoardSpace space){
    	mainPanel.add(space.getPanel());
    }

    // Retrieve a specific board space
    public BoardSpace getSpace(int index){
		return spaces.get(index);
    }

    // Retrieve the index of a specific space
    public int getSpaceIndex(BoardSpace space){
	return spaces.indexOf(space);
    }

    public ListIterator<BoardSpace> getListIterator(int index){
	ListIterator<BoardSpace> iter = spaces.listIterator(index);
	return iter;
    }

    // Retrieve the number of spaces
    public int getNumSpaces(){
		return spaces.size();
    }



    public BoardSpace sendPlayerToMiddleSpace(Player player){
    	BoardSpace previousSpace = player.getPosition();
    	previousSpace.removePlayer(player);

    	BoardSpace middleSpace = this.getSpace(BoardPanel.MID_SPACE);
    	middleSpace.addPlayer(player);

    	player.setPosition(middleSpace);

    	return middleSpace;
    }

    public BoardSpace sendPlayerToNextSpace(Player player, Card card, Boolean reverse){
		
    	BoardSpace currentPlayerSpace = player.getPosition();

		// If the Card passed-in is a "Skip" card,
		//	do nothing.
		if (card.getValue() == Card.SKIP) {
			return player.getPosition();
		}
		if (!reverse) {
			// If the Player is already at "Grandma's House",
			//	do nothing.
			if (currentPlayerSpace.isGrandmasHouse()) {
				return currentPlayerSpace;
			}
		} else {
			// If the Player is already at "start",
			//	do nothing.
			if (currentPlayerSpace.isStartSpace()) {
				return currentPlayerSpace;
			}
		}

		// Handle "special" cards
		//	Behavior is the same regardless of "reverse" value
		if (card.getValue() > 2) {
			int cardValue = card.getValue();
			BoardSpace specialSpace = null;
			if (cardValue == Card.GO_TO_FIRST_SPECIAL) {
				specialSpace = getSpace(FIRST_SPECIAL);
			} else if (cardValue == Card.GO_TO_SECOND_SPECIAL) {
				specialSpace = getSpace(SECOND_SPECIAL);
			} else if (cardValue == Card.GO_TO_THIRD_SPECIAL) {
				specialSpace = getSpace(THIRD_SPECIAL);
			} else if (cardValue == Card.GO_TO_FOURTH_SPECIAL) {
				specialSpace = getSpace(FOURTH_SPECIAL);
			} else if (cardValue == Card.GO_TO_FIFTH_SPECIAL) {
				specialSpace = getSpace(FIFTH_SPECIAL);
			}
			player.setPosition(specialSpace);
			currentPlayerSpace.removePlayer(player);
			specialSpace.addPlayer(player);

			return specialSpace;
		}

		// Find the next space that the Player can go to,
		//	based on the Color and value of the Card passed-in
		//	(aka the one that they drew from the deck)
		BoardSpace oldPlayerSpace = player.getPosition();
		BoardSpace newSpace = null;
		BoardSpace grandmasHouseSpace = this.getSpace(spaces.size() - 1);
		Color cardColor = card.getColor();
		boolean alreadyFoundFirstSpaceForCardColor = false; // Used for dealing with "DOUBLE" color cards

		//If "reverse" is true, look backwards for the color
		if (reverse) {
			for (int i = spaces.indexOf(player.getPosition()) - 1; i >= 0; i--) { // We check the spaces that are before the Player's current space
				BoardSpace space = this.getSpace(i);
				Color spaceColor = space.getSpaceColor();

				// If the next space we're looking at is the start space,
				//	the Player MUST take that space.
				if (space.isStartSpace()) {
					newSpace = this.getSpace(i);
					break;
				}

				if (spaceColor.equals(cardColor)) {
					if (card.getValue() == Card.SINGLE) {
						newSpace = this.getSpace(i);
						break;
					} else if (card.getValue() == Card.DOUBLE && alreadyFoundFirstSpaceForCardColor) {
						newSpace = this.getSpace(i);
						break;
					} else {
						alreadyFoundFirstSpaceForCardColor = true;
					}
				}
			}
		} else {
			for (int i = spaces.indexOf(player.getPosition()) + 1; i < NUM_SPACES - 2; i++) { // We check the spaces that are after the Player's current space, but before Grandma's House
				BoardSpace space = this.getSpace(i);
				Color spaceColor = space.getSpaceColor();

				// If the next space we're looking at is "Grandma's House",
				//	the Player MUST take that space.
				if (space.isGrandmasHouse()) {
					newSpace = this.getSpace(i);
					break;
				}

				if (spaceColor.equals(cardColor)) {
					if (card.getValue() == Card.SINGLE) {
						newSpace = this.getSpace(i);
						break;
					} else if (card.getValue() == Card.DOUBLE && alreadyFoundFirstSpaceForCardColor) {
						newSpace = this.getSpace(i);
						break;
					} else {
						alreadyFoundFirstSpaceForCardColor = true;
					}
				}
			}
		}

		// If we've gotten this far without finding a new BoardSpace,
		//	it means the next available space for the Player MUST be the end ("Grandma's House").
		// NOTE: This can probably be removed since we are checking for grandma's house on every loop. 
		//		Throw an exception if newSpace is null
		if (newSpace == null) {
			newSpace = grandmasHouseSpace;
		}

		// Set the Player's position to the new space's index
		player.setPosition(newSpace);

		// Remove the Player from their old BoardSpace
		// BoardSpace previousSpace = oldPlayerIndex);
		oldPlayerSpace.removePlayer(player);

		// Add the Player to their new BoardSpace
		// BoardSpace newSpace = player.getPosition();
		newSpace.addPlayer(player);

		return newSpace;
    }
}
