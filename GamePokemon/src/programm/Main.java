package programm;

import view.View;

import java.util.Scanner;


public class Main {
    static Scanner input = new Scanner(System.in);

    public static void main(String[] args) throws Exception {
        View view = new View();

        view.menu(input);
    }
}