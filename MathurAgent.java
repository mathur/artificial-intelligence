import java.util.*;

public class MathurAgent implements Agent {
    private Random rand;

    private int numOfStates;
    private int numOfActions;

    private double[][] qValues;
    private int[] actions;

    private final double discount = 0.95;
    private final double learning = 0.1;
    private final double exploration = 0.1;
  
    public MathurAgent() {
        this.rand = new Random();
    }

    public void initialize(int numOfStates, int numOfActions) {
        this.numOfStates = numOfStates;
        this.numOfActions = numOfActions;

        this.qValues = new double[numOfActions][numOfStates];
        this.actions = new int[this.numOfStates];

        for (int i = 0; i < numOfActions; i++) {
            for (int j = 0; j < numOfStates; j++) {
                this.qValues[i][j] = 0.0;
            }
        }
    }

    public int chooseAction(int state) {
        if(this.rand.nextDouble() < exploration) {
            return this.rand.nextInt(numOfActions);
        } else {
            double bestQ = this.qValues[0][state];

            for(int i = 0; i < numOfActions; i++) {
                double currentQ = this.qValues[i][state];
                if(bestQ < currentQ) {
                    bestQ = currentQ;
                }
            }

            ArrayList<Integer> potentialActions = new ArrayList<Integer>();

            for(int i = 0; i < numOfActions; i++) {
                if(bestQ == this.qValues[i][state]) {
                    potentialActions.add(i);
                }
            }

            return potentialActions.get(this.rand.nextInt(potentialActions.size()));
        }
    }

    public void updatePolicy(double reward, int action, int oldState, int newState) {
        double bestQ = this.qValues[0][newState];
        int maxIndex = 0;

        for(int i = 0; i < numOfActions; i++) {
            double currentQ = this.qValues[i][newState];
            if(bestQ < currentQ) {
                bestQ = currentQ;
                maxIndex = i;
            }
        }

        qValues[action][oldState] = qValues[action][oldState] + (learning * (reward + (discount * bestQ) - qValues[action][oldState]));
        actions[newState] = maxIndex;
    }

    public Policy getPolicy() {
        for(int i = 0; i < numOfStates; i++) {
            double bestQ = this.qValues[0][i];

            for(int j = 0; j < numOfActions; j++) {
                double currentQ = this.qValues[j][i];
                if(bestQ < currentQ) {
                    bestQ = currentQ;
                }
            }

            for (int j = 0; j < this.numOfActions; j++) {
                if (this.qValues[j][i] == bestQ) {
                  actions[i] = j;
                  break;
                }
            }
        }

        return new Policy(actions);
    }
}