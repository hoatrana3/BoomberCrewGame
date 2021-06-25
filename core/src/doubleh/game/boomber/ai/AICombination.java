package doubleh.game.boomber.ai;

import doubleh.game.boomber.actors.entities.BaseEntity;

import java.util.ArrayList;

public class AICombination extends BaseAI {

    private ArrayList<BaseAI> aiList;

    private ArrayList<BaseAI> runningAIList;

    public AICombination(BaseEntity entity) {
        super(entity);

        aiList = new ArrayList<>();
        runningAIList = new ArrayList<>();
    }

    public AICombination(BaseEntity entity, BaseAI... ais) {
        this(entity);

        for (BaseAI ai : ais) add(ai);
    }

    @Override
    public void update(float delta) {
        for (BaseAI ai : runningAIList) {
            ai.update(delta);
        }
    }

    public void add(BaseAI ai) {
        if (!aiList.contains(ai)) aiList.add(ai);
    }

    public BaseAI get(int index) {
        return aiList.get(index);
    }

    public boolean remove(BaseAI ai) {
        return aiList.remove(ai);
    }

    public void remove(int index) {
        aiList.remove(index);
    }

    public void addToRun(int index) {
        if (!runningAIList.contains(get(index))) runningAIList.add(get(index));
    }

    public void removeFromRun(int index) {
        runningAIList.remove(get(index));
    }

    public void clearRunningAI() {
        runningAIList.clear();
    }

    public ArrayList<BaseAI> getRunningAIList() {
        return runningAIList;
    }
}
