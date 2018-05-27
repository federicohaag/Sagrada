package it.polimi.se2018.model;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class AdjacentValuePlacementRuleDecoratorTest {

    private static Cell[][] pattern;

    private PlacementRule rule;
    private PlacementRule decoratedRule;

    private WindowPattern windowPattern;

    private Dice threeDice;
    private Dice fourDice;

    @BeforeClass
    public static void setUpClass() throws Exception {
        pattern = new Cell[3][3];
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                pattern[i][j] = new Cell();
            }
        }
        pattern[1][0] = new Cell(3, DiceColors.NOCOLOR);
    }

    @Before
    public void setUp() throws Exception {
        PlacementRule emptyRule = new EmptyPlacementRule();
        rule = new AdjacentValuePlacementRuleDecorator(emptyRule);
        decoratedRule = new AdjacentValuePlacementRuleDecorator(new ValuePlacementRuleDecorator(emptyRule));

        windowPattern = new WindowPattern("", "",0, pattern);
        threeDice = new Dice(DiceColors.BLUE, 3);
        fourDice = new Dice(DiceColors.BLUE, 4);
        windowPattern.putDiceOnCell(threeDice, 1,1);
    }

    @Test
    public void testCheckIfMoveIsAllowedWhenMoveIsOnAlreadyPlacedDice() {
        assertFalse(rule.checkIfMoveIsAllowed(windowPattern, threeDice, 1,1));
    }

    @Test
    public void testCheckIfMoveIsAllowed() {
        assertTrue(decoratedRule.checkIfMoveIsAllowed(windowPattern, fourDice, 1, 2));
    }

    @Test
    public void testCheckIfMoveIsAllowedIfDecoratedNotAllowed() {
        assertFalse(decoratedRule.checkIfMoveIsAllowed(windowPattern, fourDice, 1, 0));
    }
    @Test
    public void testCheckAdjacentValueConstraintsBelow() {
        assertFalse(rule.checkIfMoveIsAllowed(windowPattern, threeDice, 0,1));
        assertTrue(rule.checkIfMoveIsAllowed(windowPattern, fourDice, 0,1));
    }

    @Test
    public void testCheckAdjacentValueConstraintsAbove() {
        assertFalse(rule.checkIfMoveIsAllowed(windowPattern, threeDice, 2,1));
        assertTrue(rule.checkIfMoveIsAllowed(windowPattern, fourDice, 2,1));
    }

    @Test
    public void testCheckAdjacentValueConstraintsLeft() {
        assertFalse(rule.checkIfMoveIsAllowed(windowPattern, threeDice, 1,2));
        assertTrue(rule.checkIfMoveIsAllowed(windowPattern, fourDice, 1,2));
    }

    @Test
    public void testCheckAdjacentValueConstraintsRight() {
        assertFalse(rule.checkIfMoveIsAllowed(windowPattern, threeDice, 1,0));
        assertTrue(rule.checkIfMoveIsAllowed(windowPattern, fourDice, 1,0));
    }
}