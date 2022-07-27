package model;

public class Worker extends Civilian{

    private Improvement improvement;
    private boolean buildingRoad;
    private boolean workingStatus;
    private int remainingTurnsToComplete;

    public Worker(String name, Tile tile, int HP, int goldPrice, int productionPrice, int level, int MP, int combatStrength, int rangeCombatStrength, boolean working, User user, int attackPoint, int maintainGold) {
        super(name, tile, HP, goldPrice, productionPrice, level, MP, combatStrength, rangeCombatStrength, user, attackPoint, maintainGold);
        workingStatus = working;
    }

    public Improvement getImprovement() {
        return improvement;
    }

    public void setImprovement(Improvement improvement) {
        this.improvement = improvement;
    }

    public boolean getWorkingStatus() {
        return workingStatus;
    }

    public void setWorkingStatus(boolean workingStatus) {
        this.workingStatus = workingStatus;
    }

    public int getRemainingTurnsToComplete() {
        return remainingTurnsToComplete;
    }

    public void setRemainingTurnsToComplete(int remainingTurnsToComplete) {
        this.remainingTurnsToComplete = remainingTurnsToComplete;
    }

    public boolean isBuildingRoad() {
        return buildingRoad;
    }

    public void setBuildingRoad(boolean buildingRoad) {
        this.buildingRoad = buildingRoad;
    }

}
