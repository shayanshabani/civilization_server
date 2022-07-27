package model;

import java.util.ArrayList;

public class User {
    private final String username;
    private String nickname;
    private String password;
    private String color;
    private int turns;
    private int gold;
    private int product;
    private ArrayList<Tile> territory;
    private int goldPerTurn;
    private int happiness;
    private int unhappiness;
    private int science;
    private int sciencePerTurn;
    private int food;
    private int foodPerTurn;
    private int culture;
    private int culturePerTurn;
    private int faith;
    private int faithPerTurn;
    private int productPerTurn;
    private ArrayList<Resource> availableResources;
    private ArrayList<Resource> foundResources;
    private String civilization;
    private ArrayList<Unit> units;
    private ArrayList<City> cities;
    private ArrayList<City> annexedCities;
    private City capital;
    private ArrayList<Technology> technologies;
    private ArrayList<Improvement> improvements;
    private int turnNumber;

    private ArrayList<Tile> visited;
    private ArrayList<Tile> visible;
    private ArrayList<ArrayList<String>> historyOfNotification;
    private Technology currentTechnology;
    private boolean researching;
    private int researchTurnLeft;
    private ArrayList<String> specialWorking;
    private ArrayList<City> foundCities;
    private ArrayList<Ruin> foundRuins;

    public User(String username, String nickname, String password) {
        this.username = username;
        this.nickname = nickname;
        this.password = password;
        gold = 0;
        turns = 1;
        visited = new ArrayList<>();
        territory = new ArrayList<>();
        foundResources = new ArrayList<>();
        availableResources = new ArrayList<>();
        technologies = new ArrayList<>();
        improvements = new ArrayList<>();
        units = new ArrayList<>();
        cities = new ArrayList<>();
        annexedCities = new ArrayList<>();
        currentTechnology = null;
        researching = false;
        researchTurnLeft = 0;
        happiness = 15;
        unhappiness = 0;
        historyOfNotification = new ArrayList<>();
        food = 0;
        product = 0;
        territory = new ArrayList<>();
        historyOfNotification = new ArrayList<>();
        improvements = new ArrayList<>();
        technologies = new ArrayList<>();
        availableResources = new ArrayList<>();
        specialWorking = new ArrayList<>();
        foundCities = new ArrayList<>();
        foundRuins = new ArrayList<>();
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getColor() {
        return color;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public int getTurns() {
        return turns;
    }

    public void setTurns(int turns) {
        this.turns = turns;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getNickname() {
        return nickname;
    }

    public String getPassword() {
        return password;
    }

    public ArrayList<Tile> getTerritory() {
        return this.territory;
    }

    public void setTerritory(ArrayList<Tile> territory) {
        this.territory = territory;
    }

    public int getGoldPerTurn() {
        return goldPerTurn;
    }

    public void setGoldPerTurn(int goldPerTurn) {
        this.goldPerTurn = goldPerTurn;
    }

    public int getHappiness() {
        return happiness;
    }

    public void setHappiness(int happiness) {
        this.happiness = happiness;
    }

    public void setScience(int science) {
        this.science = science;
    }

    public int getScience() {
        return science;
    }

    public int getSciencePerTurn() {
        return sciencePerTurn;
    }

    public void setSciencePerTurn(int sciencePerTurn) {
        this.sciencePerTurn = sciencePerTurn;
    }

    public int getFood() {
        return food;
    }

    public void setFood(int food) {
        this.food = food;
    }

    public int getFoodPerTurn() {
        return foodPerTurn;
    }

    public void setFoodPerTurn(int foodPerTurn) {
        this.foodPerTurn = foodPerTurn;
    }

    public int getCulture() {
        return culture;
    }

    public void setCulture(int culture) {
        this.culture = culture;
    }

    public int getCulturePerTurn() {
        return culturePerTurn;
    }

    public void setCulturePerTurn(int culturePerTurn) {
        this.culturePerTurn = culturePerTurn;
    }

    public int getFaith() {
        return faith;
    }

    public void setFaith(int faith) {
        this.faith = faith;
    }

    public int getFaithPerTurn() {
        return faithPerTurn;
    }

    public void setFaithPerTurn(int faithPerTurn) {
        this.faithPerTurn = faithPerTurn;
    }

    public ArrayList<Resource> getAvailableResources() {
        return availableResources;
    }

    public void setAvailableResources(Resource availableResource) {
        this.availableResources.add(availableResource);
    }

    public String getCivilization() {
        return civilization;
    }

    public void setCivilization(String civilization) {
        this.civilization = civilization;
    }

    public ArrayList<Unit> getUnits() {
        return units;
    }

    public void setUnits(ArrayList<Unit> units) {
        this.units = units;
    }

    public void addAnnexedCity(City city){
        this.annexedCities.add(city);
    }

    public void addFoundResourced(Resource foundResource){
        this.foundResources.add(foundResource);
    }

    public void addAvailableResource(Resource resource){
        this.availableResources.add(resource);
    }

    public void addUnit(Unit unit) {
        this.units.add(unit);
    }

    public ArrayList<City> getCities() {
        return cities;
    }

    public void setCities(ArrayList<City> cities) {
        this.cities = cities;
    }

    public City getCapital() {
        return capital;
    }

    public void setCapital(City capital) {
        this.capital = capital;
    }

    public ArrayList<Technology> getTechnologies() {
        return technologies;
    }

    public void setTechnologies(ArrayList<Technology> technologies) {
        this.technologies = technologies;
    }

    public int getTurnNumber() {
        return turnNumber;
    }

    public void setTurnNumber(int turnNumber) {
        this.turnNumber = turnNumber;
    }

    public ArrayList<Tile> getVisited() {
        return visited;
    }

    public void setVisited(ArrayList<Tile> visited) {
        this.visited = visited;
    }

    public ArrayList<Tile> getVisible() {
        return visible;
    }

    public void setVisible(ArrayList<Tile> visible) {
        this.visible = visible;
    }

    public void addVisited(Tile visit) {
        visited.add(visit);
    }

    public void addVisible(Tile visible) {
        this.visible.add(visible);
    }

    public void addTerritory(Tile tile) {
        territory.add(tile);
    }

    public void addTechnology(Technology technology) {
        technologies.add(technology);
    }

    public void addCity(City city) {
        cities.add(city);
    }

    public void setCurrentTechnology(Technology currentTechnology) {
        this.currentTechnology = currentTechnology;
    }

    public Technology getCurrentTechnology() {
        return currentTechnology;
    }

    public boolean isResearching() {
        return researching;
    }

    public void setResearching(boolean researching) {
        this.researching = researching;
    }

    public int getResearchTurnLeft() {
        return researchTurnLeft;
    }

    public void setResearchTurnLeft(int researchTurnLeft) {
        this.researchTurnLeft = researchTurnLeft;
    }

    public ArrayList<Resource> getFoundResources() {
        return foundResources;
    }

    public void setFoundResources(ArrayList<Resource> foundResources) {
        this.foundResources = foundResources;
    }

    public void removeTerritory(Tile tile) {
        int index = 0;
        for (Tile tile1 : territory) {
            if (tile1.equals(tile)) {
                territory.remove(index);
                return;
            }
            index++;
        }
    }

    public void removeUnit(PhysicalObject unit) {
        int index = 0;
        for (Unit unit1 : units) {
            if (unit1.equals(unit)) {
                units.remove(index);
                return;
            }
            index++;
        }
    }

    public ArrayList<Improvement> getImprovements() {
        return improvements;
    }

    public void setImprovements(ArrayList<Improvement> improvements) {
        this.improvements = improvements;
    }

    public void addImprovement(Improvement improvement) {
        this.improvements.add(improvement);
    }

    public ArrayList<ArrayList<String>> getHistoryOfNotification() {
        return historyOfNotification;
    }

    public void setHistoryOfNotification(ArrayList<ArrayList<String>> historyOfNotification) {
        this.historyOfNotification = historyOfNotification;
    }
    public void addHistoryOfNotification(ArrayList<String> notification) {
        historyOfNotification.add(notification);
    }

    public int getProduct() {
        return product;
    }

    public int getProductPerTurn() {
        return productPerTurn;
    }

    public void setProduct(int product) {
        this.product = product;
    }

    public void setProductPerTurn(int productPerTurn) {
        this.productPerTurn = productPerTurn;
    }

    public int getUnhappiness() {
        return unhappiness;
    }

    public void setUnhappiness(int unhappiness) {
        this.unhappiness = unhappiness;
    }

    public ArrayList<City> getAnnexedCities() {
        return annexedCities;
    }

    public void setAnnexedCities(ArrayList<City> annexedCities) {
        this.annexedCities = annexedCities;
    }

    public void removeCity(City city) {
        cities.remove(city);
    }

    public ArrayList<String> getSpecialWorking() {
        return specialWorking;
    }

    public void setSpecialWorking(ArrayList<String> specialWorking) {
        this.specialWorking = specialWorking;
    }

    public void addSpecialWorking(String specialWorking) {
        this.specialWorking.add(specialWorking);
    }

    public ArrayList<City> getFoundCities() {
        return foundCities;
    }
    public void addFoundCities(City city) {
        foundCities.add(city);
    }

    public void setAvailableResources(ArrayList<Resource> availableResources) {
        this.availableResources = availableResources;
    }

    public void setFoundCities(ArrayList<City> foundCities) {
        this.foundCities = foundCities;
    }

    public ArrayList<Ruin> getFoundRuins() {
        return foundRuins;
    }

    public void setFoundRuins(ArrayList<Ruin> foundRuins) {
        this.foundRuins = foundRuins;
    }

    public void addFoundRuin(Ruin ruin) {
        foundRuins.add(ruin);
    }
}
