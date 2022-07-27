package controller;

import com.google.gson.Gson;
import model.*;
import view.Notifications;
import view.Colors;

import javax.management.StringValueExp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class GameController {

    private static GameController gameController;

    private final int height;
    private final int width;
    private ArrayList<User> players;


    private GameController() {
        this.height = 26;
        this.width = 80;
        players = new ArrayList<>();
    }

    public static GameController getInstance() {
        if (gameController == null)
            gameController = new GameController();
        return gameController;
    }

    public ArrayList<User> getPlayers() {
        return players;
    }

    // cheat codes
    public void increaseTurn(int extraTurn, User specificPlayer) {
        specificPlayer.setTurns(specificPlayer.getTurns() + extraTurn);
    }

    public void increaseGold(int extraGold, User specificPlayer) {
        specificPlayer.setGold(specificPlayer.getGold() + extraGold);
    }

    public void increaseHappiness(int extraHappiness, User specificPlayer) {
        specificPlayer.setHappiness(specificPlayer.getHappiness() + extraHappiness);
    }

    public void increaseFood(int extraFood, User specificPlayer) {
        specificPlayer.setFood(specificPlayer.getFood() + extraFood);
    }

    public void increaseFaith(int extraFaith, User specificPlayer) {
        specificPlayer.setFaith(specificPlayer.getFaith() + extraFaith);
    }

    public void increaseScience(int extraScience, User specificPlayer) {
        specificPlayer.setScience(specificPlayer.getScience() + extraScience);
    }

    public void increaseCapitalCitizens(int extraCitizens, User specificPlayer) {
        specificPlayer.getCapital().setCitizensNumber(specificPlayer.getCapital().getCitizensNumber() + extraCitizens);
    }

    public void increaseCapitalDefence(int extraDefence, User specificPlayer) {
        specificPlayer.getCapital().setDefence(specificPlayer.getCapital().getDefence() + extraDefence);
    }

    public void increaseCulture(int extraCulture, User specificPlayer) {
        specificPlayer.setCulture(specificPlayer.getCulture() + extraCulture);
    }

    public void decreaseResearchTurnLeft(int extraResearch, User specificPlayer) {
        specificPlayer.setResearchTurnLeft(specificPlayer.getResearchTurnLeft()- extraResearch);
    }

    // find the tile by given x and y coordinates
    public Tile findTile(int x, int y) {
        if (x < height && y < width)
            return ServerController.getInstance().getMap().getTileBoard()[x][y];
        return null;
    }

    public void moveUnit(Tile origin, Tile destination, Unit unit, boolean isMilitary) {
        if (isMilitary)
            moveMilitary(origin, destination, (MilitaryUnit) unit);
        else
            moveCivilian(origin, destination, (Civilian) unit);
    }

    private void moveMilitary(Tile origin, Tile destination, MilitaryUnit militaryUnit) {
        militaryUnit.setTile(destination);
        origin.setMilitaryUnit(null);
        origin.setMilitaryUnitExists(false);
        destination.setMilitaryUnit(militaryUnit);
        destination.setMilitaryUnitExists(true);
    }

    private void moveCivilian(Tile origin, Tile destination, Civilian civilian) {
        civilian.setTile(destination);
        origin.setCivilianUnit(null);
        origin.setCivilianUnitExists(false);
        destination.setCivilianUnit(civilian);
        destination.setCivilianUnitExists(true);
    }

    public void cityTurnProducts(User user) {
        // for each on user's cities
        if (user.getCities() != null) {
            for (City city : user.getCities()) {
                // if producing production
                if (city.isProductStatus()) {
                    // if product is done
                    if (city.getProductTurnLeft() <= 1) {
                        city.setProductTurnLeft(0);
                        findProduction(city, city.getCurrentProduction());
                        city.setCurrentProduction(null);
                        city.setProductStatus(false);
                    } else
                        city.setProductTurnLeft(city.getProductTurnLeft() - 1);
                }
            }
        }
    }


    public void userTurnResearch(User user) {
        if (user.isResearching()) {
            // if research is done
            if (user.getResearchTurnLeft() <= 1) {
                //notification for research
                //UserPanel.researchDoneNotification(user, user.getCurrentTechnology());
                user.setResearching(false);
                user.setResearchTurnLeft(0);
                user.addTechnology(user.getCurrentTechnology());
                // add given improvements to user's arraylist of improvements
                if (user.getCurrentTechnology().getGivenImprovement() != null) {
                    for (Improvement improvement : user.getCurrentTechnology().getGivenImprovement()) {
                        user.addImprovement(improvement);
                    }
                }
                // add given units to possible products of the city
                if (user.getCurrentTechnology().getGivenUnits() != null) {
                    MeleeMilitaryUnit meleeMilitaryUnit;
                    RangeMilitaryUnit rangeMilitaryUnit;
                    for (Unit givenUnit : user.getCurrentTechnology().getGivenUnits()) {
                        for (City city : user.getCities()) {
                            // range military unit
                            if (givenUnit.getRangeCombatStrength() > 0) {
                                rangeMilitaryUnit = new RangeMilitaryUnit(givenUnit.getName(), null, givenUnit.getHP(), givenUnit.getGoldPrice(), givenUnit.getProductionPrice(), givenUnit.getLevel(), givenUnit.getMP(), givenUnit.getCombatStrength(), givenUnit.getRangeCombatStrength(), city.getOwner(), givenUnit.getAttackPoint(), givenUnit.getMaintainGold());
                                city.addPossibleUnit(rangeMilitaryUnit);
                                city.addProduct(new Product(rangeMilitaryUnit.getName(), rangeMilitaryUnit.getProductionPrice()));
                            }
                            // melee military unit
                            else {
                                meleeMilitaryUnit = new MeleeMilitaryUnit(givenUnit.getName(), null, givenUnit.getHP(), givenUnit.getGoldPrice(), givenUnit.getProductionPrice(), givenUnit.getLevel(), givenUnit.getMP(), givenUnit.getCombatStrength(), givenUnit.getRangeCombatStrength(), city.getOwner(), givenUnit.getAttackPoint(), givenUnit.getMaintainGold());
                                city.addPossibleUnit(meleeMilitaryUnit);
                                city.addProduct(new Product(meleeMilitaryUnit.getName(), meleeMilitaryUnit.getProductionPrice()));
                            }
                        }
                    }
                }
                //add given buildings to possible products of the city
                if (user.getCurrentTechnology().getGivenBuildings() != null) {
                    Building building;
                    for (Building givenBuilding : user.getCurrentTechnology().getGivenBuildings()) {
                        for (City city : user.getCities()) {
                            building = new Building(givenBuilding.getName(), givenBuilding.getMaintainCost(), givenBuilding.getCost(), givenBuilding.getFoodRate(), givenBuilding.getDefence(), givenBuilding.getXP(), givenBuilding.getScienceRate(), givenBuilding.getHappiness(), givenBuilding.getRequirement());
                            city.addPossibleBuildings(building);
                            city.addProduct(new Product(building.getName(), building.getCost()));
                        }
                    }
                }
                user.setCurrentTechnology(null);
            } else
                user.setResearchTurnLeft(user.getResearchTurnLeft() - 1);
        }
    }

    // check all workers
    public void userTurnWorker(User player) {
        if (player.getUnits() != null) {

            for (Unit unit : player.getUnits()) {
                // find worker
                if (unit.getName().equals("worker") && ((Worker) unit).getWorkingStatus()) {
                    Worker worker = (Worker) unit;
                    // if it is done
                    if (worker.getRemainingTurnsToComplete() <= 1) {
                        // delete jungle and jungle and forest and marsh
                        if (worker.getImprovement() != null) {
                            if (worker.getImprovement().getName().equals("Farm") || worker.getImprovement().getName().equals("Mine"))
                                if (worker.getTile().getFeature() != null)
                                    afterBuildingFarmOrMine(worker.getOwner(), worker.getTile());
                            // notification for improvement
                            //UserPanel.improvementDoneNotification(player, worker.getImprovement());
                            worker.setWorkingStatus(false);
                            worker.setRemainingTurnsToComplete(0);
                            worker.getTile().setInProgress(false);
                            worker.getTile().setImprovement(worker.getImprovement());
                            worker.getImprovement().setTile(worker.getTile());
                            if (worker.getImprovement().getGivenResources() != null) {
                                for (Resource resource : worker.getImprovement().getGivenResources()) {
                                    if (worker.getTile().getResource() != null) {
                                        if (resource.getName().equals(worker.getTile().getResource().getName()))
                                            player.setAvailableResources(worker.getTile().getResource());
                                    }
                                }
                            }
                            worker.setImprovement(null);
                        }
                        else {
                            worker.setWorkingStatus(false);
                            worker.setRemainingTurnsToComplete(0);
                            worker.getTile().setInProgress(false);
                            worker.getTile().setRoad(true);
                            //UserPanel.roadNotification(worker.getTile(), worker.getOwner());
                        }
                    } else {
                        worker.setRemainingTurnsToComplete(worker.getRemainingTurnsToComplete() - 1);
                    }
                }
            }
        }
    }

    // find the unit based on production name
    public void findProduction(City city, Product product) {
        Worker worker;
        Settler settler;
        Civilian scout;
        MeleeMilitaryUnit meleeMilitaryUnit;
        RangeMilitaryUnit rangeMilitaryUnit;
        for (int i = 0; i < city.getPossibleUnits().size(); i++) {
            if (city.getPossibleUnits().get(i).getName().equals(product.getName())) {
                Unit unit = city.getPossibleUnits().get(i);
                // worker
                if (unit.getName().equals("worker")) {
                    Tile tile = findTileForCivilian(city.getTile());
                    worker = new Worker(unit.getName(), tile, unit.getHP(), unit.getGoldPrice(), unit.getProductionPrice(), unit.getLevel(), unit.getMP(), unit.getCombatStrength(), unit.getRangeCombatStrength(), false, city.getOwner(), unit.getAttackPoint(), unit.getMaintainGold());
                    tile.setCivilianUnit(worker);
                    tile.setCivilianUnitExists(true);
                    worker.setTile(tile);
                    city.getOwner().addUnit(worker);
                    //UserPanel.productDoneNotification(city.getOwner(), city, worker, this);
                }
                // settler
                else if (unit.getName().equals("settler")) {
                    Tile tile = findTileForCivilian(city.getTile());
                    settler = new Settler("settler", tile, unit.getHP(), unit.getGoldPrice(), unit.getProductionPrice(), unit.getLevel(), unit.getMP(), unit.getCombatStrength(), unit.getRangeCombatStrength(), null, city.getOwner(), unit.getAttackPoint(), unit.getMaintainGold());
                    tile.setCivilianUnit(settler);
                    tile.setCivilianUnitExists(true);
                    settler.setTile(tile);
                    city.getOwner().addUnit(settler);
                    //UserPanel.productDoneNotification(city.getOwner(), city, settler, this);
                }
                else if (unit.getName().equals("scout")) {
                    Tile tile = findTileForCivilian(city.getTile());
                    scout = new Civilian("scout", tile, unit.getHP(), unit.getGoldPrice(), unit.getProductionPrice(), unit.getLevel(), unit.getMP(), unit.getCombatStrength(), unit.getRangeCombatStrength(), city.getOwner(), unit.getAttackPoint(), unit.getMaintainGold());
                    tile.setCivilianUnit(scout);
                    tile.setCivilianUnitExists(true);
                    scout.setTile(tile);
                    city.getOwner().addUnit(scout);
                    //UserPanel.productDoneNotification(city.getOwner(), city, scout, this);
                }
                // ranged
                else if (unit.getRangeCombatStrength() > 0) {
                    Tile tile = findTileForMilitary(city.getTile());
                    rangeMilitaryUnit = new RangeMilitaryUnit(unit.getName(), tile, unit.getHP(), unit.getGoldPrice(), unit.getProductionPrice(), unit.getLevel(), unit.getMP(), unit.getCombatStrength(), unit.getRangeCombatStrength(), city.getOwner(), unit.getAttackPoint(), unit.getMaintainGold());
                    tile.setMilitaryUnit(rangeMilitaryUnit);
                    tile.setMilitaryUnitExists(true);
                    rangeMilitaryUnit.setTile(tile);
                    city.getOwner().addUnit(rangeMilitaryUnit);
                    //UserPanel.productDoneNotification(city.getOwner(), city, rangeMilitaryUnit, this);
                }
                // melee
                else {
                    Tile tile = findTileForMilitary(city.getTile());
                    meleeMilitaryUnit = new MeleeMilitaryUnit(unit.getName(), tile, unit.getHP(), unit.getGoldPrice(), unit.getProductionPrice(), unit.getLevel(), unit.getMP(), unit.getCombatStrength(), unit.getRangeCombatStrength(), city.getOwner(), unit.getAttackPoint(), unit.getMaintainGold());
                    tile.setMilitaryUnit(meleeMilitaryUnit);
                    tile.setMilitaryUnitExists(true);
                    meleeMilitaryUnit.setTile(tile);
                    city.getOwner().addUnit(meleeMilitaryUnit);
                    //UserPanel.productDoneNotification(city.getOwner(), city, meleeMilitaryUnit, this);
                }
                return;
            }
        }

        for (Building building : city.getPossibleBuildings()) {
            if (building.getName().equals(product.getName())) {
                Building building1 = new Building(building.getName(), building.getMaintainCost(), building.getCost(), building.getFoodRate(), building.getDefence(), building.getXP(), building.getScienceRate(), building.getHappiness(), building.getRequirement());
                city.addBuildings(building1);
                //UserPanel.productDoneNotification(city.getOwner(), city, building);
                return;
            }
        }

    }

    public void increaseCitizens(User user){
        for (City city : user.getCities()) {
            if (city.getTurnNumber() >= 10){
                city.setTurnNumber(0);
                city.setCitizensNumber(city.getCitizensNumber() + 1);
                Citizen citizen = new Citizen(null);
                city.addCitizen(citizen);
                //new UserPanel(this).citizensIncreased(user, city);
                if (city.getCitizensNumber() % 5 == 0){
                    user.setUnhappiness(user.getUnhappiness() + 1);
                }
            } else city.setTurnNumber(city.getTurnNumber() + 1);
        }
    }

    public void usersIncomeHandling(User user, Maps map) {
        user.setGoldPerTurn(0);
        user.setFoodPerTurn(0);
        user.setProductPerTurn(0);
        user.setSciencePerTurn(0);
        new ResourceController().userResource(user, map);
        if (user.getImprovements() != null && user.getImprovements().size() > 0) {
            for (Tile tile : user.getTerritory()) {
                if (tile.getImprovement() != null && !tile.isLooted()) {
                    user.setGoldPerTurn(user.getGoldPerTurn() + tile.getImprovement().getGoldRate());
                    user.setFoodPerTurn(user.getFoodPerTurn() + tile.getImprovement().getFoodRate());
                    user.setProductPerTurn(user.getProductPerTurn() + tile.getImprovement().getProductionRate());
                }
            }
        }
        if (user.getCities() != null){
            for (City city : user.getCities()) {
                user.setGoldPerTurn(user.getGoldPerTurn() + city.getGold());
                user.setFood(user.getFoodPerTurn() + city.getFood());
                user.setSciencePerTurn(city.getCitizensNumber() + 3);
            }
        }
        user.setGold(user.getGold() + user.getGoldPerTurn());
        user.setFood(user.getFood() + user.getFoodPerTurn());
        user.setScience(user.getScience() + user.getSciencePerTurn());
        if (user.getUnits() != null){
            for (Unit unit : user.getUnits()) {
                user.setGold(user.getGold() - unit.getMaintenance());
            }
        }
        for (Tile tile : user.getTerritory()) {
            if (tile.isRoad()){
                if(user.getTurns() % 3 == 0) user.setGold(user.getGold() - 1);
            }
        }
        if (user.getGold() < 0){
            user.setScience(user.getScience() + user.getGold());
            user.setGold(0);
        }
    }

    public Tile findTileForMilitary(Tile origin) {
        if (origin.isMilitaryUnitExists()) {
            for (Tile neighbor : origin.getNeighbors()) {
                if (!neighbor.isMilitaryUnitExists() && neighbor.getTerrain().isPassable())
                    return neighbor;
            }
            for (Tile neighbor : origin.getNeighbors()) {
                for (Tile neighborNeighbor : neighbor.getNeighbors()) {
                    if (!neighborNeighbor.isMilitaryUnitExists() && neighbor.getTerrain().isPassable())
                        return neighborNeighbor;
                }
            }
        }
        return origin;
    }
    public Tile findTileForCivilian(Tile origin) {
        if (origin.isCivilianUnitExists()) {
            for (Tile neighbor : origin.getNeighbors()) {
                if (!neighbor.isCivilianUnitExists() && neighbor.getTerrain().isPassable())
                    return neighbor;
            }
            for (Tile neighbor : origin.getNeighbors()) {
                for (Tile neighborNeighbor : neighbor.getNeighbors()) {
                    if (!neighborNeighbor.isCivilianUnitExists() && neighbor.getTerrain().isPassable()){
                        return neighborNeighbor;}
                }
            }
        }
        return origin;
    }
    public void userHappiness(User user){
        for (City city : user.getCities()) {
            if (!city.isUnhappinessEffect())
                user.setUnhappiness(user.getUnhappiness() + 3);
        }
        for (Resource foundResource : user.getFoundResources()) {
            if (user.getAvailableResources().contains(foundResource))
                user.setHappiness(user.getHappiness() + foundResource.getHappiness());
        }
        for (City city : user.getAnnexedCities()) {
            if (!city.isUnhappinessEffect())
                user.setUnhappiness(user.getUnhappiness() + 4);
        }
    }


    public void citiesIncome(User user){
        for (City city : user.getCities()) {
            city.setProduction(0);
            city.setFood(0);
            city.setGold(0);
            for (Citizen citizen : city.getCitizens()) {
                if (citizen.getTile() != null){
                    if (citizen.getTile().getFeature() != null){
                        city.setGold(citizen.getTile().getFeature().getGoldRate());
                        city.setFood(citizen.getTile().getFeature().getFoodRate());
                        city.setProduction(citizen.getTile().getFeature().getProductionRate());
                    }
                    city.setGold(city.getGold() + citizen.getTile().getTerrain().getGoldRate());
                    city.setFood(city.getFood() + citizen.getTile().getTerrain().getFoodRate());
                    city.setProduction(city.getProduction() + citizen.getTile().getTerrain().getProductionRate());
                }
                city.setProduction(city.getProduction() + 1);
                city.setFood(city.getFood() - 1);
            }
        }
    }

    private void afterBuildingFarmOrMine(User user, Tile tile) {
        switch (tile.getFeature().getName()) {
            case "Jungle", "Forest", "Marsh" -> tile.setFeature(null);
        }
    }

    public void makeAllUnOrdered(User user) {
        if (user.getUnits() != null) {
            for (Unit unit : user.getUnits()) {
                if (!unit.isAlert() && !unit.isSleep() && !unit.isFortify())
                    unit.setOrdered(false);
                if (unit.isAlert()) {
                    for (Tile neighbor : unit.getTile().getNeighbors()) {
                        if (neighbor.isMilitaryUnitExists() && !neighbor.getMilitaryUnit().getOwner().equals(user)) {
                            unit.setAlert(false);
                            unit.setOrdered(false);
                        }
                    }
                }
                //check fortify units
                if (unit.isFortify()) {
                    if (unit.getHP() < unit.getTotalHealth())
                        unit.setHP(unit.getHP() + 10);
                    if (unit.getHP() > unit.getTotalHealth()) {
                        unit.setHP(unit.getTotalHealth());
                        unit.setFortify(false);
                    }
                }
            }
        }
    }
    public void foundCity(User user) {
        if (user.getUnits() != null) {
            for (Unit unit : user.getUnits()) {
                for (Tile neighbor : unit.getTile().getNeighbors()) {
                    if (neighbor.getCity() != null &&
                            neighbor.getCity().getTile().equals(neighbor) &&
                            !neighbor.getOwner().equals(user) &&
                            !user.getFoundCities().contains(neighbor.getCity())) {
                        user.addFoundCities(neighbor.getCity());
                        //UserPanel.foundCity(neighbor.getCity(), user);
                    }
                }
            }
        }
    }

    public void moveTravelingUnits(User user) {
        for (Unit unit : user.getUnits()) {
            if (unit.isMoving()) {
                if (unit.getTile().isMilitaryUnitExists() && unit.getTile().isSelectedOne()) {
                    //playGame.moveUnit(unit.getTile(), unit.getDestination(), unit, user, true);
                    unit.setOrdered(true);
                    if (unit.getTile().equals(unit.getDestination())) {
                        unit.setMoving(false);
                        unit.setDestination(null);
                    }
                }
                else if (unit.getTile().isCivilianUnitExists() && unit.getTile().isSelectedTwo()) {
                    //playGame.moveUnit(unit.getTile(), unit.getDestination(), unit, user, false);
                    unit.setOrdered(true);
                    if (unit.getTile().equals(unit.getDestination())) {
                        unit.setMoving(false);
                        unit.setDestination(null);
                    }
                }
            }
        }
    }

    public void assignNeighbor(Maps map) {

        // assign all the neighbors to each tile
        for (int i = 0; i < map.getHeight(); i++)
            for (int j = 0; j < map.getWidth(); j++)
                MapController.getInstance().setNeighbor(map.getTileBoard()[i][j], map);
    }

    public void foundRuin(User user) {
        for (Unit unit : user.getUnits()) {
            if (unit.getTile().getRuin() != null) {
                if (!user.getFoundRuins().contains(unit.getTile().getRuin())) {
                    //UserPanel.foundRuin(unit.getTile().getRuin(), user);
                    Ruin ruin = unit.getTile().getRuin();
                    if (ruin.getGivenTechnologies() != null)
                        addRuinEffectTech(ruin, user);
                    if (ruin.getGold() > 0)
                        addRuinEffectGold(ruin, user);
                    addRuinEffectPopulation(user);
                    if (ruin.isWorker())
                        addRuinEffectWorker(user);
                    if (ruin.isSettler())
                        addRuinEffectSettler(user);
                    if (ruin.getRemovedFogs() != null)
                        addRuinEffectFogOfTile(ruin, user);

                }
            }
        }
    }

    private void addRuinEffectTech(Ruin ruin, User user) {
        for (Technology givenTechnology : ruin.getGivenTechnologies()) {
            if (!user.getTechnologies().contains(givenTechnology)) {
                user.setResearchTurnLeft(1);
                user.setResearching(true);
                user.setCurrentTechnology(givenTechnology);
                userTurnResearch(user);
            }
        }
    }

    private void addRuinEffectGold(Ruin ruin, User user) {
        user.setGold(ruin.getGold());
    }

    private void addRuinEffectPopulation(User user) {
        //todo add one citizen
    }

    private void addRuinEffectWorker(User user) {
        user.getCapital().setProductStatus(true);
        user.getCapital().setCurrentProduction(user.getCapital().getProducts().get(0));
        user.getCapital().setProductTurnLeft(1);
        cityTurnProducts(user);
    }

    private void addRuinEffectSettler(User user) {
        user.getCapital().setProductStatus(true);
        user.getCapital().setCurrentProduction(user.getCapital().getProducts().get(1));
        user.getCapital().setProductTurnLeft(1);
        cityTurnProducts(user);
    }

    private void addRuinEffectFogOfTile(Ruin ruin, User user) {
        for (Tile removedFog : ruin.getRemovedFogs()) {
            if (!user.getVisited().contains(removedFog))
                user.addVisited(removedFog);
        }
    }

    public void addTech(Technology technology, User user) {
        user.setResearchTurnLeft(1);
        user.setResearching(true);
        user.setCurrentTechnology(technology);
        userTurnResearch(user);
    }

    public Response nextTurn(Request request) {

        Response response = new Response();

        Boolean cheat = Boolean.parseBoolean(request.getParameters().get("cheat"));
        String username = request.getParameters().get("username");
        User user = UsersController.getInstance().getUserByUsername(username);
        if (cheat.equals(false)) {
            //next turn raw
            boolean did = true;
            if (user.getUnits() != null) {
                for (Unit unit : user.getUnits()) {
                    if (!unit.isOrdered() && !unit.isAlert() && !unit.isSleep()) {
                        did = false;
                        break;
                    }
                }
            }
            if (did) {
                //next turn
                acceptNextTurn(response, user);
            }
            else {
                //next turn is not possible
                response.setStatusCode("401");
                response.setMessage("you didn't play all your turns");
                HashMap<String, String> parameters = new HashMap<>();
                parameters.put("next turn", String.valueOf(false));
                response.setParameters(parameters);
            }
        }
        else {
            //next turn --force
            acceptNextTurn(response, user);
        }
        return response;
    }

    private void acceptNextTurn(Response response, User user) {
        response.setStatusCode("200");
        response.setMessage("next turn");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("next turn", String.valueOf(true));
        response.setParameters(parameters);
        user.setTurns(1);
    }

    public Response increaseTurnRequest(Request request) {
        Response response = new Response();

        String username = request.getParameters().get("username");
        User user = UsersController.getInstance().getUserByUsername(username);
        int amount = Integer.parseInt(request.getParameters().get("amount"));

        if (amount > 0) {
            increaseTurn(amount, user);
            makeAllUnOrdered(user);
            UnitController.getInstance().repairMovementPoint(user);
            response.setMessage("turn increased successfully!");
        } else
            response.setMessage("invalid number");

        return response;
    }

    public Response increaseGoldRequest(Request request) {
        Response response = new Response();

        String username = request.getParameters().get("username");
        User user = UsersController.getInstance().getUserByUsername(username);
        int amount = Integer.parseInt(request.getParameters().get("amount"));
        if (amount > 0) {
            increaseGold(amount, user);
            response.setMessage("gold increased successfully!");
        } else
            response.setMessage("invalid command");

        return response;
    }

    public Response increaseFoodRequest(Request request) {
        Response response = new Response();

        String username = request.getParameters().get("username");
        User user = UsersController.getInstance().getUserByUsername(username);

        int amount = Integer.parseInt(request.getParameters().get("amount"));
        if (amount > 0) {
            gameController.increaseFood(amount, user);
            response.setMessage("food increased successfully!");
        } else
            response.setMessage("invalid command");

        return response;
    }

    public Response increaseFaithRequest(Request request) {
        Response response = new Response();

        String username = request.getParameters().get("username");
        User user = UsersController.getInstance().getUserByUsername(username);

        int amount = Integer.parseInt(request.getParameters().get("amount"));
        if (amount > 0) {
            increaseFaith(amount, user);
            response.setMessage("faith increased successfully!");
        } else
            response.setMessage("invalid command");

        return response;
    }

    public Response increaseScienceRequest(Request request) {
        Response response = new Response();

        String username = request.getParameters().get("username");
        User user = UsersController.getInstance().getUserByUsername(username);
        int amount = Integer.parseInt(request.getParameters().get("amount"));

        if (amount > 0) {
            increaseScience(amount, user);
            response.setMessage("science increased successfully!");

        } else
            response.setMessage("invalid command");

        return response;
    }

    public Response increaseCapitalCitizensRequest(Request request) {
        Response response = new Response();

        String username = request.getParameters().get("username");
        User user = UsersController.getInstance().getUserByUsername(username);
        int amount = Integer.parseInt(request.getParameters().get("amount"));

        if (amount > 0) {
            if (user.getCapital() != null) {
                increaseCapitalCitizens(amount, user);
                response.setMessage("capitalCitizens increased successfully!");
            }
            else
                response.setMessage("user doesn't have capital right now!");
        } else
            response.setMessage("invalid command");

        return response;
    }

    public Response increaseCapitalDefenceRequest(Request request) {
        Response response = new Response();

        String username = request.getParameters().get("username");
        User user = UsersController.getInstance().getUserByUsername(username);
        int amount = Integer.parseInt(request.getParameters().get("amount"));

        if (amount > 0) {
            if (user.getCapital() != null) {
                increaseCapitalDefence(amount, user);
                response.setMessage("capitalDefence increased successfully!");
            }
            else
                response.setMessage("user doesn't have capital right now!");
        } else
            response.setMessage("invalid command");

        return response;
    }

    public Response increaseCultureRequest(Request request) {
        Response response = new Response();

        String username = request.getParameters().get("username");
        User user = UsersController.getInstance().getUserByUsername(username);
        int amount = Integer.parseInt(request.getParameters().get("amount"));

        if (amount > 0) {
            increaseCulture(amount, user);
            response.setMessage("culture increased successfully!");
        } else
            response.setMessage("invalid command");

        return response;
    }

    public Response increaseHappinessRequest(Request request) {
        Response response = new Response();

        String username = request.getParameters().get("username");
        User user = UsersController.getInstance().getUserByUsername(username);
        int amount = Integer.parseInt(request.getParameters().get("amount"));

        if (amount > 0) {
            increaseHappiness(amount, user);
            response.setMessage("happiness increased successfully!");
        } else
            response.setMessage("invalid command");

        return response;
    }

    public Response decreaseResearchTurnRequest(Request request) {
        Response response = new Response();

        String username = request.getParameters().get("username");
        User user = UsersController.getInstance().getUserByUsername(username);
        int amount = Integer.parseInt(request.getParameters().get("amount"));

        if (amount > 0) {
            if (user.getCurrentTechnology() != null) {
                decreaseResearchTurnLeft(amount, user);
                response.setMessage("researches increased successfully!");
            }
            else
                response.setMessage("user don't research on anything right now!");
        } else
            response.setMessage("invalid number");

        return response;
    }

    public Response selectTileRequest(Request request) {
        Response response = new Response();

        int xOrigin = Integer.parseInt(request.getParameters().get("x"));
        int yOrigin = Integer.parseInt(request.getParameters().get("y"));

        Tile origin = ServerController.getInstance().getMap().getSpecificTile(xOrigin, yOrigin);

        if (xOrigin >= 0 && yOrigin >= 0 && xOrigin <= width && yOrigin <= height) {
            response.setStatusCode("200");
            response.setMessage("valid coordinates!");
            //preparing the conditions
            origin.setSelectedOne(false);
            origin.setSelectedTwo(false);
            if (origin.isMilitaryUnitExists())
                origin.setSelectedOne(true);
            else if (origin.isCivilianUnitExists())
                origin.setSelectedTwo(true);
        }
        else {
            response.setStatusCode("404");
            response.setMessage("invalid coordinates!");
        }
        return response;
    }

    public Response setPlayers(Request request) {
        Response response = new Response();
        ArrayList<User> gamers = new ArrayList<>();
        User user;
        HashMap<String, String> parameters = new HashMap<>();
        for (int i = 1; i <= request.getParameters().size(); i++) {
            if (UsersController.getInstance().sameUsernameExists(request.getParameters().get("username" + i))) {
                user = UsersController.getInstance().getUserByUsername(request.getParameters().get("username" + i));
                gamers.add(user);
            }
            else {
                response.setMessage("some of the usernames are not valid");
                parameters.put("valid", String.valueOf(false));
                return response;
            }
        }
        String gsonPlayers = new Gson().toJson(gamers);
        parameters.put("valid", String.valueOf(true));
        parameters.put("players", gsonPlayers);
        response.setParameters(parameters);
        response.setMessage("game successfully started!");
        return response;
    }



    public Response searchFriend(Request request) {
        Response response = new Response();
        String username = request.getParameters().get("username");
        for (User user : UsersController.getInstance().getUsers()) {
            if (user.getUsername().equals(username)) {
                response.setMessage(username);
                return response;
            }
        }
        response.setMessage("there is no player with this username!");
        return response;
    }

    public void nextPlayer(User user) {
        cityTurnProducts(user);
        userTurnResearch(user);
        userTurnWorker(user);
        increaseCitizens(user);
        citiesIncome(user);
        usersIncomeHandling(user, ServerController.getInstance().getMap());
        userHappiness(user);
        makeAllUnOrdered(user);
        foundCity(user);
        moveTravelingUnits(user);
        foundRuin(user);
    }

    private Response showPlayers() {

        Response response = new Response();
        ArrayList<String> notifications = new ArrayList<>();

        int index = 1;
        String color;
        ColorsController colorsController = new ColorsController();
        for (User player : players) {
            color = colorsController.getColorOfUser(player);
            notifications.add(index + "- username: " + player.getUsername() + " nickname: " + color + player.getNickname() + Colors.RESET);
            index++;
        }
        response.setNotifications(notifications);
        return response;
    }

    public Response showMap(Request request) {
        String username = request.getParameters().get("username");
        User user = UsersController.getInstance().getUserByUsername(username);

        Response response = new Response();
        ArrayList<String> notifications = new ArrayList<>();

        Maps map = ServerController.getInstance().getMap();
        MapController mapController = MapController.getInstance();
        notifications.add("user nickname : " + new ColorsController().getColorOfUser(user) + user.getNickname() + Colors.RESET);
        notifications.add("user Happiness : " + user.getHappiness());
        notifications.add("user UnHappiness : " + user.getUnhappiness());
        notifications.add("user Food : " + user.getFood());
        notifications.add("user Gold : " + user.getGold());
        notifications.add("user Science : " + user.getScience());
        //first top sides of left tiles of game board
        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < map.getWidth(); j++)
            stringBuilder.append("   ")
                    .append(mapController.riverFinder(map.getTileBoard()[0][j], 0))
                    .append("              ");
        stringBuilder.append("\n");
        notifications.add(stringBuilder.toString());

        //print the game board
        for (int i = 0; i < map.getHeight()/2; i++) {
            leftCoordinatesPlace(i, notifications);
            leftOwnerName(i, notifications);
            leftTilesUnit(i, notifications);
            leftResourceAndTerrain(i, notifications);
            rightOwnerName(i, notifications);
            leftBottomSides(i, notifications);
        }

        response.setNotifications(notifications);
        return response;

    }

    private void showInformation(User user) {
        //temporary
        System.out.println("username: " + user.getUsername());
        System.out.println("nickname: " + user.getNickname());
        System.out.println("remained movements: " + user.getTurns());
        System.out.println("gold: " + user.getGold());
        System.out.println("culture: " + user.getCulture());
        System.out.println("faith: " + user.getFaith());
        System.out.println("happiness: " + user.getHappiness());
        System.out.println("food: " + user.getFood());
    }

    private void leftCoordinatesPlace(int i, ArrayList<String> notifications) {
        Maps map = ServerController.getInstance().getMap();
        MapController mapController = MapController.getInstance();
        StringBuilder stringBuilder = new StringBuilder();
        //Coordinates of left tiles and resource and terrain in right tiles
        for (int j = 0; j < map.getWidth(); j++) {
            if (i != 0) {
                if (j != 0)
                    stringBuilder.append(mapController.getColorOfTile(map.getTileBoard()[2 * i - 1][j - 1]))
                            .append("  ")
                            .append(Colors.RESET);
                else stringBuilder.append("  ");
                stringBuilder.append(mapController.riverFinder(map.getTileBoard()[2 * i][j], 5))
                        .append(mapController.getColorOfTile(map.getTileBoard()[2 * i][j]))
                        .append(leftCoordination(i, j))
                        .append(Colors.RESET)
                        .append(mapController.riverFinder(map.getTileBoard()[2 * i][j], 1))
                        .append(mapController.getColorOfTile(map.getTileBoard()[2 * i - 1][j]))
                        .append("   ")
                        .append(mapController.tileResource(map.getTileBoard()[2 * i - 1][j], false))
                        .append("   ")
                        .append(mapController.tileFeature(map.getTileBoard()[2 * i - 1][j], false))
                        .append("  ")
                        .append(Colors.RESET);
            } else stringBuilder.append("  ")
                    .append(mapController.riverFinder(map.getTileBoard()[2 * i][j], 5))
                    .append(mapController.getColorOfTile(map.getTileBoard()[2 * i][j]))
                    .append(leftCoordination(0, j))
                    .append(Colors.RESET)
                    .append(mapController.riverFinder(map.getTileBoard()[2 * i][j], 1))
                    .append("             ");
        }
        notifications.add(stringBuilder.toString());
        if (i != 0) notifications.add(mapController.getColorOfTile(map.getTileBoard()[2 * i - 1][map.getWidth() - 1])
                + "  " + Colors.RESET + mapController.riverFinder(map.getTileBoard()[2 * i - 1][map.getWidth() - 1], 2));
        else notifications.add("");

    }

    private String leftCoordination(int i, int j){
        if (2 * i < 10 && j < 10) return "  [0" + 2 * i + ",0" + j + "]  ";
        else if (2 * i > 9 && j < 10) return "  [" + 2 * i + ",0" + j + "]  ";
        else if (2 * i > 9) return "  [" + 2 * i + "," + j + "]  ";
        else return "  [0" + 2 * i + "," + j + "]  ";
    }

    private void leftOwnerName(int i, ArrayList<String> notifications) {
        Maps map = ServerController.getInstance().getMap();
        MapController mapController = MapController.getInstance();
        String ANSI_COLOR;
        StringBuilder stringBuilder = new StringBuilder();
        //owner name and color of left tiles and right Improvement
        for (int j = 0; j < map.getWidth(); j++) {
            ANSI_COLOR = mapController.getColorOfTileOwner(map.getTileBoard()[2 * i][j]);
            if (i != 0 && j != 0)
                stringBuilder.append(mapController.getColorOfTile(map.getTileBoard()[2 * i - 1][j - 1]));
            stringBuilder.append(" " + Colors.RESET)
                    .append(mapController.riverFinder(map.getTileBoard()[2 * i][j], 5))
                    .append(mapController.getColorOfTile(map.getTileBoard()[2 * i][j]))
                    .append("     ")
                    .append(ANSI_COLOR)
                    .append(mapController.getColorOfTile(map.getTileBoard()[2 * i][j]))
                    .append(mapController.tileOwner(map.getTileBoard()[2 * i][j]))
                    .append(mapController.getColorOfTile(map.getTileBoard()[2 * i][j]))
                    .append("     ")
                    .append(Colors.RESET)
                    .append(mapController.riverFinder(map.getTileBoard()[2 * i][j], 1));
            if (i != 0)
                stringBuilder.append(mapController.getColorOfTileOwner(map.getTileBoard()[2 * i - 1][j]))
                        .append(mapController.getColorOfTile(map.getTileBoard()[2 * i - 1][j]))
                        .append("     ")
                        .append(mapController.tileImprovement(map.getTileBoard()[2 * i - 1][j]))
                        .append("    ")
                        .append(Colors.RESET);
            else stringBuilder.append("            ");
        }
        notifications.add(stringBuilder.toString());
        if (i != 0) notifications.add(mapController.getColorOfTile(map.getTileBoard()[2 * i - 1][map.getWidth() - 1])
                + " " + Colors.RESET + mapController.riverFinder(map.getTileBoard()[2 * i][0], 2));
        else notifications.add("");

    }

    private void leftTilesUnit(int i, ArrayList<String> notifications){
        Maps map = ServerController.getInstance().getMap();
        MapController mapController = MapController.getInstance();
        String ANSI_COLOR;
        StringBuilder stringBuilder = new StringBuilder();
        //units on the left tiles and top sides of right tiles
        for (int j = 0; j < map.getWidth(); j++) {
            ANSI_COLOR = mapController.getColorOfTileOwner(map.getTileBoard()[2 * i][j]);
            stringBuilder.append(mapController.riverFinder(map.getTileBoard()[2 * i][j], 5))
                    .append(mapController.getColorOfTile(map.getTileBoard()[2 * i][j]))
                    .append("    ")
                    .append(ANSI_COLOR)
                    .append(mapController.getColorOfTile(map.getTileBoard()[2 * i][j]))
                    .append(mapController.civilianUnit(map.getTileBoard()[2 * i][j]))
                    .append("   ")
                    .append(ANSI_COLOR)
                    .append(mapController.getColorOfTile(map.getTileBoard()[2 * i][j]))
                    .append(mapController.militaryUnit(map.getTileBoard()[2 * i][j]))
                    .append(mapController.getColorOfTile(map.getTileBoard()[2 * i][j]))
                    .append("    ")
                    .append(Colors.RESET)
                    .append(mapController.riverFinder(map.getTileBoard()[2 * i][j], 1));
            if (i != 0) stringBuilder.append(mapController.riverFinder(map.getTileBoard()[2 * i - 1][j], 3));
            else stringBuilder.append(mapController.riverFinder(map.getTileBoard()[1][j], 0));
        }
        notifications.add(stringBuilder.toString());
        if (i != 0) notifications.add(mapController.riverFinder(map.getTileBoard()[2 * i][map.getWidth() - 1], 2));
        else notifications.add("");
    }

    private void leftResourceAndTerrain(int i, ArrayList<String> notifications) {
        Maps map = ServerController.getInstance().getMap();
        MapController mapController = MapController.getInstance();
        StringBuilder stringBuilder = new StringBuilder();

        //resource and terrain in left tiles and Coordinates of right tiles
        for (int j = 0; j < map.getWidth(); j++) {
            if (i != map.getHeight() / 2 - 1)
                stringBuilder.append(mapController.riverFinder(map.getTileBoard()[2 * i][j], 4))
                        .append(mapController.getColorOfTile(map.getTileBoard()[2 * i][j]))
                        .append("   ")
                        .append(mapController.tileResource(map.getTileBoard()[2 * i][j], false))
                        .append("   ")
                        .append(mapController.tileFeature(map.getTileBoard()[2 * i][j], false))
                        .append("    ")
                        .append(Colors.RESET)
                        .append(mapController.riverFinder(map.getTileBoard()[2 * i][j], 2))
                        .append(mapController.getColorOfTile(map.getTileBoard()[2 * i + 1][j]))
                        .append(rightCoordination(i, j))
                        .append(Colors.RESET);
            else stringBuilder.append(mapController.riverFinder(map.getTileBoard()[2 * i][j], 4))
                    .append(mapController.getColorOfTile(map.getTileBoard()[2 * i][j]))
                    .append("   ")
                    .append(mapController.tileResource(map.getTileBoard()[2 * i][j], false))
                    .append("   ")
                    .append(mapController.tileFeature(map.getTileBoard()[2 * i][0], false))
                    .append("    ")
                    .append(Colors.RESET)
                    .append(mapController.riverFinder(map.getTileBoard()[2 * i][j], 2))
                    .append("           ");
        }
        notifications.add(stringBuilder.toString());
        if (i != map.getHeight() / 2 - 1)
            notifications.add(mapController.riverFinder(map.getTileBoard()[2 * i + 1][map.getWidth() - 1], 1));
        else
            notifications.add("");

    }

    private String rightCoordination(int i, int j){
        if (2 * i + 1 < 10 && j < 10) return "  [0" + (2 * i + 1) + ",0" + j + "]  ";
        else if (2 * i + 1 > 9 && j < 10) return "  [" + (2 * i + 1) + ",0" + j + "]  ";
        else if (2 * i + 1 > 9) return "  [" + (2 * i + 1) + "," + j + "]  ";
        else return "  [0" + (2 * i + 1) + "," + j + "]  ";
    }

    private void rightOwnerName(int i, ArrayList<String> notifications) {
        Maps map = ServerController.getInstance().getMap();
        MapController mapController = MapController.getInstance();
        String ANSI_COLOR;
        String ANSI_COLOR2;
        //owner name and color of right tiles and left Improvement
        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < map.getWidth(); j++) {
            ANSI_COLOR = mapController.getColorOfTileOwner(map.getTileBoard()[2 * i][j]);
            ANSI_COLOR2 = mapController.getColorOfTileOwner(map.getTileBoard()[2 * i + 1][j]);;
            if (i != map.getHeight() / 2 - 1) {
                if (j != 0) System.out.print(mapController.getColorOfTile(map.getTileBoard()[2 * i + 1][j - 1]));
                stringBuilder.append(" " + Colors.RESET)
                        .append(mapController.riverFinder(map.getTileBoard()[2 * i][j], 4))
                        .append(ANSI_COLOR)
                        .append(mapController.getColorOfTile(map.getTileBoard()[2 * i][j]))
                        .append("     ")
                        .append(mapController.tileImprovement(map.getTileBoard()[2 * i][j]))
                        .append("     ")
                        .append(Colors.RESET)
                        .append(mapController.riverFinder(map.getTileBoard()[2 * i][j], 2))
                        .append(ANSI_COLOR2)
                        .append(mapController.getColorOfTile(map.getTileBoard()[2 * i + 1][j]))
                        .append("     ")
                        .append(mapController.getColorOfTile(map.getTileBoard()[2 * i + 1][j]))
                        .append(mapController.tileOwner(map.getTileBoard()[2 * i + 1][j]))
                        .append(mapController.getColorOfTile(map.getTileBoard()[2 * i + 1][j]))
                        .append("    ")
                        .append(Colors.RESET);
            } else stringBuilder.append(" ")
                    .append(mapController.riverFinder(map.getTileBoard()[2 * i][j], 4))
                    .append(ANSI_COLOR)
                    .append(mapController.getColorOfTile(map.getTileBoard()[2 * i][j]))
                    .append("     ")
                    .append(mapController.tileImprovement(map.getTileBoard()[2 * i][j]))
                    .append("     ")
                    .append(Colors.RESET)
                    .append(mapController.riverFinder(map.getTileBoard()[2 * i][j], 2))
                    .append("            ");
        }
        notifications.add(stringBuilder.toString());
        if (i != map.getHeight() / 2 - 1)
            notifications.add(mapController.getColorOfTile(map.getTileBoard()[2 * i + 1][map.getWidth() - 1])
                + " " + Colors.RESET + mapController.riverFinder(map.getTileBoard()[2 * i + 1][map.getWidth() - 1], 1));
        else notifications.add("");

    }

    private void leftBottomSides(int i, ArrayList<String> notifications) {
        Maps map = ServerController.getInstance().getMap();
        MapController mapController = MapController.getInstance();
        String ANSI_COLOR = Colors.WHITE;

        //bottom sides of left tiles and units on the right tiles
        StringBuilder stringBuilder = new StringBuilder();
        for (int j = 0; j < map.getWidth(); j++) {
            if (i != map.getHeight() / 2 - 1) ANSI_COLOR = mapController.getColorOfTileOwner(map.getTileBoard()[2 * i + 1][j]);

            if (i != map.getHeight() / 2 - 1) {
                if (j != 0) System.out.print(mapController.getColorOfTile(map.getTileBoard()[2 * i + 1][j - 1]));
                stringBuilder.append("  " + Colors.RESET)
                        .append(mapController.riverFinder(map.getTileBoard()[2 * i][j], 4))
                        .append(mapController.riverFinder(map.getTileBoard()[2 * i][j], 3))
                        .append(mapController.riverFinder(map.getTileBoard()[2 * i][j], 2))
                        .append(mapController.getColorOfTile(map.getTileBoard()[2 * i + 1][j]))
                        .append("    ")
                        .append(ANSI_COLOR)
                        .append(mapController.getColorOfTile(map.getTileBoard()[2 * i + 1][j]))
                        .append(mapController.civilianUnit(map.getTileBoard()[2 * i + 1][0]))
                        .append(mapController.getColorOfTile(map.getTileBoard()[2 * i + 1][j]))
                        .append("   ")
                        .append(ANSI_COLOR)
                        .append(mapController.getColorOfTile(map.getTileBoard()[2 * i + 1][j]))
                        .append(mapController.militaryUnit(map.getTileBoard()[2 * i + 1][j]))
                        .append("  ")
                        .append(Colors.RESET);
            } else
                stringBuilder.append("  ")
                        .append(mapController.riverFinder(map.getTileBoard()[2 * i][j], 4))
                        .append(mapController.riverFinder(map.getTileBoard()[2 * i][j], 3))
                        .append(mapController.riverFinder(map.getTileBoard()[2 * i][j], 2))
                        .append("             ");
        }
        notifications.add(stringBuilder.toString());
        if (i != map.getHeight() / 2 - 1)
            notifications.add(mapController.getColorOfTile(map.getTileBoard()[2 * i + 1][map.getWidth() - 1])
                    + "  " + Colors.RESET + mapController.riverFinder(map.getTileBoard()[2 * i + 1][map.getWidth() - 1], 1));
        else notifications.add("");

    }

    public Response tileInformation(Request request) {

        Response response = new Response();
        ArrayList<String> notifications = new ArrayList<>();

        int xOrigin = Integer.parseInt(request.getParameters().get("xOrigin"));
        int yOrigin = Integer.parseInt(request.getParameters().get("yOrigin"));
        Tile tile = ServerController.getInstance().getMap().getSpecificTile(xOrigin, yOrigin);

        if (tile.getOwner() != null)
            notifications.add("Owner : " + tile.getOwner().getUsername());
        if (tile.getCity() != null)
            notifications.add("City : " + tile.getCity().getName());
        notifications.add("Coordinate : -x " + tile.getX() + " -y " + tile.getY());
        if (tile.isMilitaryUnitExists() && tile.isSelectedOne())
            notifications.add("military unit selected");
        if (tile.isCivilianUnitExists() && tile.isSelectedTwo())
            notifications.add("civilian unit selected");
        tileDetails(tile, notifications);
        response.setNotifications(notifications);
        return response;
    }

    static void tileDetails(Tile tile, ArrayList<String> notifications) {
        notifications.add("Terrain : " + tile.getTerrain().getName());
        if (tile.getFeature() != null)
            notifications.add("Feature : " + tile.getFeature().getName());
        if (tile.getResource() != null)
            notifications.add("Resource : " + tile.getResource().getName() + " of type : " + tile.getResource().getResourceType());
        if (tile.getImprovement() != null)
            notifications.add("Improvement : " + tile.getImprovement().getName());
        if (tile.isMilitaryUnitExists()) {
            notifications.add("Military unit exists here :");
            Unit unit = tile.getMilitaryUnit();
            unitInformation(unit, notifications);
        }
        if (tile.isCivilianUnitExists()) {
            notifications.add("Civilian unit exists here :");
            Unit unit = tile.getCivilianUnit();
            unitInformation(unit, notifications);
        }
    }

    private static void unitInformation(Unit unit, ArrayList<String> notifications) {
        notifications.add("Name : " + unit.getName());
        notifications.add("HP : " + unit.getHP());
        notifications.add("MP : " + unit.getMP());
        notifications.add("Combat strength : " + unit.getCombatStrength());
        notifications.add("Ranged combat strength : " + unit.getRangeCombatStrength());
        notifications.add("Level : " + unit.getLevel());
    }

    public Response improvementInformation(Request request) {

        Response response = new Response();
        ArrayList<String> notifications = new ArrayList<>();

        String username = request.getParameters().get("username");
        User user = UsersController.getInstance().getUserByUsername(username);
        int xOrigin = Integer.parseInt(request.getParameters().get("xOrigin"));
        int yOrigin = Integer.parseInt(request.getParameters().get("yOrigin"));
        Tile tile = ServerController.getInstance().getMap().getSpecificTile(xOrigin, yOrigin);

        ArrayList<Improvement> improvements = user.getImprovements();

        HashMap<String, String> parameters = new HashMap<>();

        boolean deleted = true;
        if (tile.getImprovement() != null) {
            notifications.add("your current improvement on this tile is :" + tile.getImprovement().getName());
            notifications.add("you have to first remove the current improvement then you can build something else!");
            deleted = false;
        }
        int index = 1;
        // print possible improvements with detail
        for (Improvement improvement : improvements) {
            notifications.add(index + "- " + improvement.getName() + "\nProduction : " + improvement.getProductionRate() + "\nFood : " + improvement.getFoodRate() + "\nGold : " + improvement.getGoldRate());
            index++;
        }

        parameters.put("deleted", String.valueOf(deleted));

        boolean road = false;
        for (Technology technology : user.getTechnologies()) {
            if (technology.getName().equals("The Wheel") && !tile.isRoad()) {
                road = true;
                notifications.add("press -build road- to build road on the tile");
                notifications.add("press -build road cheat- to build road on the tile immediately");
            }
        }

        parameters.put("road", String.valueOf(road));
        int max = improvements.size();
        parameters.put("max", String.valueOf(max));
        response.setParameters(parameters);

        notifications.add("choose an improvement by index to be applied on this tile");
        notifications.add("press \"add (--improvement | -i) (index)\" to build the improvement immediately");
        notifications.add("press <improvement exit> to get out of here");
        response.setNotifications(notifications);
        return response;
    }

    public Response addImprovement(Request request) {
        Response response = new Response();
        int xOrigin = Integer.parseInt(request.getParameters().get("xOrigin"));
        int yOrigin = Integer.parseInt(request.getParameters().get("yOrigin"));
        Tile tile = ServerController.getInstance().getMap().getSpecificTile(xOrigin, yOrigin);
        String username = request.getParameters().get("username");
        User user = UsersController.getInstance().getUserByUsername(username);
        ArrayList<Improvement> improvements = user.getImprovements();
        int index = Integer.parseInt(request.getParameters().get("index"));

        tile.setInProgress(true);
        Worker worker = (Worker) tile.getCivilianUnit();
        if (Boolean.parseBoolean(request.getParameters().get("cheat")))
            worker.setRemainingTurnsToComplete(1);
        else
            worker.setRemainingTurnsToComplete(improvements.get(index - 1).getPrice());
        worker.setWorkingStatus(true);
        worker.setImprovement(improvements.get(index - 1));
        worker.setOrdered(true);
        userTurnWorker(user);
        response.setMessage("improvement is building...");
        return response;
    }

    public Response buildRoad(Request request) {

        Response response = new Response();

        String username = request.getParameters().get("username");
        User user = UsersController.getInstance().getUserByUsername(username);
        int xOrigin = Integer.parseInt(request.getParameters().get("xOrigin"));
        int yOrigin = Integer.parseInt(request.getParameters().get("yOrigin"));
        Tile tile = ServerController.getInstance().getMap().getSpecificTile(xOrigin, yOrigin);
        boolean cheat = Boolean.parseBoolean(request.getParameters().get("cheat"));
        if (!cheat) {
            tile.setInProgress(true);
            Worker worker = (Worker) tile.getCivilianUnit();
            worker.setRemainingTurnsToComplete(5);
            worker.setWorkingStatus(true);
            worker.setOrdered(true);
        }
        else {
            tile.setRoad(true);
            tile.getCivilianUnit().setOrdered(true);
            response.setNotifications(Notifications.roadNotification(user, tile));
        }
        response.setMessage("road built successfully!");
        return response;
    }

    public Response deleteImprovement(Request request) {

        Response response = new Response();

        int xOrigin = Integer.parseInt(request.getParameters().get("xOrigin"));
        int yOrigin = Integer.parseInt(request.getParameters().get("yOrigin"));
        Tile tile = ServerController.getInstance().getMap().getSpecificTile(xOrigin, yOrigin);


        tile.setImprovement(null);
        response.setMessage("improvement deleted successfully!");
        return response;
    }

    public Response selectAnotherTile(Request request) {
        Response response = new Response();

        int xOrigin = Integer.parseInt(request.getParameters().get("xOrigin"));
        int yOrigin = Integer.parseInt(request.getParameters().get("yOrigin"));

        int xDestination = Integer.parseInt(request.getParameters().get("xDestination"));
        int yDestination = Integer.parseInt(request.getParameters().get("yDestination"));

        Tile origin = ServerController.getInstance().getMap().getSpecificTile(xOrigin, yOrigin);
        boolean change = false;
        if (xDestination >= 0 && yDestination >= 0) {
            if (xDestination == xOrigin && yDestination == yOrigin) {
                // if the user press again on the tile , change the unit if two exists
                if (origin.isMilitaryUnitExists() && origin.isSelectedOne() && origin.isCivilianUnitExists()) {
                    origin.setSelectedOne(false);
                    origin.setSelectedTwo(true);
                } else if (origin.isCivilianUnitExists() && origin.isSelectedTwo() && origin.isMilitaryUnitExists()) {
                    origin.setSelectedOne(true);
                    origin.setSelectedTwo(false);
                }
                response.setMessage("still there");
            } else {
                Tile destination = ServerController.getInstance().getMap().getSpecificTile(xDestination, yDestination);
                if (destination != null) {
                    //selectedTile(scanner, destination, user);
                    origin.setSelectedOne(false);
                    origin.setSelectedTwo(false);
                    change = true;
                } else
                    response.setMessage("invalid tile");
            }
        } else
            response.setMessage("invalid coordinates");

        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("change", String.valueOf(change));
        response.setParameters(parameters);
        return response;
    }

    public Response showPossibleImprovements(Request request) {

        Response response = new Response();

        int xOrigin = Integer.parseInt(request.getParameters().get("xOrigin"));
        int yOrigin = Integer.parseInt(request.getParameters().get("yOrigin"));
        Tile origin = ServerController.getInstance().getMap().getSpecificTile(xOrigin, yOrigin);

        String username = request.getParameters().get("username");
        User user = UsersController.getInstance().getUserByUsername(username);
        boolean yes = false;
        if (origin.isCivilianUnitExists() &&
                origin.getCivilianUnit().getOwner().equals(user) &&
                origin.getCivilianUnit().getName().equals("worker")) {
            if (!origin.isLooted())
                yes = true;
            else
                response.setMessage("looted!");
        }
        else
            response.setMessage("there is no worker in this tile");
        HashMap<String, String> parameters = new HashMap<>();
        parameters.put("yes", String.valueOf(yes));
        response.setParameters(parameters);
        return response;
    }

    public Response manPage(Request request) {

        Response response = new Response();
        ArrayList<String> notifications = new ArrayList<>();

        String boldColor = Colors.YELLOW_BOLD;
        String color = Colors.CYAN;
        String noticeColor = Colors.RED;
        notifications.add("Game started , good luck");
        notifications.add("the players are as follows:");
        Response response2 = showPlayers();
        notifications.addAll(response2.getNotifications());
        notifications.add(color + "press \"game exit\" to end the game");
        notifications.add("press \"show board\" to see the map");
        notifications.add("press \"show players\" to see the players");
        notifications.add("press \"show information\" to see the information of current player");
        notifications.add("press \"choose technology\" to choose a technology for research" + Colors.RESET);
        notifications.add(boldColor + "to select a tile :" + Colors.RESET);
        notifications.add(color + "select tile -x <x> -y <y>" + Colors.RESET);
        notifications.add("**********************");
        notifications.add(boldColor + "while you are in tile :" + Colors.RESET);
        notifications.add(color + "press \"tile exit\" to get out of tile" + Colors.RESET);
        notifications.add(boldColor + "to move a unit :" + Colors.RESET);
        notifications.add(color + "move unit to -x <x> -y <y>" + Colors.RESET);
        notifications.add(noticeColor + "notice" + Colors.RESET + " ---> x and y are coordinates of destination , make sure to use valid coordinates");
        notifications.add(boldColor + "to select another tile :" + Colors.RESET);
        notifications.add(color + "select tile -x <x> -y <y>" + Colors.RESET);
        notifications.add(boldColor + "to see possible improvements which can be implemented on this tile :" + Colors.RESET);
        notifications.add(color + "show possible improvements" + Colors.RESET);
        notifications.add("**********************");
        notifications.add(boldColor + "to create city with settler :" + Colors.RESET);
        notifications.add(color + "place city here" + Colors.RESET);
        response.setNotifications(notifications);
        return response;
    }

}
