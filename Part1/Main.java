public class Main {
    public static void main(String[] args) {
        Race race = new Race(20, 4);

        Horse horse1 = new Horse('\u265E', "Harry", 0.8);
        Horse horse2 = new Horse('\u2658', "Max", 0.6);
        Horse horse3 = new Horse('\u265E', "Sam", 0.7);
        Horse horse4 = new Horse('\u2658', "Karen", 0.7);


        race.addHorse(horse1, 1);
        race.addHorse(horse2, 2);
        race.addHorse(horse3, 3);
        race.addHorse(horse4, 4);



        race.startRace();
    }
}
