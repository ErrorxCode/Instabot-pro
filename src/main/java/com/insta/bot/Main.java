package com.insta.bot;


import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Objects;
import java.util.Scanner;

public class Main {
    protected static Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Thread.setDefaultUncaughtExceptionHandler((t, e) -> Main.exit(e instanceof InputMismatchException ? "Please enter a valid option" : "An unknown error occurred"));
        Actions.performLogin();
        type("""
                Note : This bot uses instagram private API. Private API means that it works exactly same as we surf instagram on app So the limits are also same.
                Your account can be temporary banned if you use this bot harshly or so much in a short interval of time and I will not be responsible for that.
                Remember the limits [60 follow/unfollow/comment/etc per hour]. If you go beyond this limit,then instagram consider it spamming and may block you
                                
                """);
        showMenu();
    }

    static void showMenu() {
        clear();
        System.out.println("""
                _________   _          _______   _________   _______    ______     _______   _________
                \\__   __/  ( (    /|  (  ____ \\  \\__   __/  (  ___  )  (  ___ \\   (  ___  )  \\__   __/
                   ) (     |  \\  ( |  | (    \\/     ) (     | (   ) |  | (   ) )  | (   ) |     ) (  \s
                   | |     |   \\ | |  | (_____      | |     | (___) |  | (__/ /   | |   | |     | |  \s
                   | |     | (\\ \\) |  (_____  )     | |     |  ___  |  |  __ (    | |   | |     | |  \s
                   | |     | | \\   |        ) |     | |     | (   ) |  | (  \\ \\   | |   | |     | |  \s
                ___) (___  | )  \\  |  /\\____) |     | |     | )   ( |  | )___) )  | (___) |     | |  \s
                \\_______/  |/    )_)  \\_______)     )_(     |/     \\|  |/ \\___/   (_______)     )_(  \s
                                                                                                     \s
                                                                               Version : 1.0
                                                                               Author : Rahil khan
                """);
        System.out.println("----------------[ WELCOME ]----------------");
        System.out.println("""
                1. Spam group
                2. Spam DM
                3. Dump chat
                4. Unfollow all
                5. Logout
                6. About InstaBot
                """);
        System.out.print("Enter option number : ");

        int option = scanner.nextInt();
        scanner.nextLine();
        clear();
        switch (option) {
            case 1 -> Actions.spamGroup();
            case 2 -> Actions.spamDM();
            case 3 -> Actions.dumpChat();
            case 4 -> Actions.unfollowAll();
            case 5 -> Actions.logout();
            case 6 -> about();
            default -> {
                System.out.println("[!] Invalid option. Exiting...");
                System.exit(-1);
            }
        }
    }


    static void about() {
        System.out.println("""
                ██╗███╗░░██╗░██████╗████████╗░█████╗░██████╗░░█████╗░████████╗  ██╗░░░██╗░░███╗░░
                ██║████╗░██║██╔════╝╚══██╔══╝██╔══██╗██╔══██╗██╔══██╗╚══██╔══╝  ██║░░░██║░████║░░
                ██║██╔██╗██║╚█████╗░░░░██║░░░███████║██████╦╝██║░░██║░░░██║░░░  ╚██╗░██╔╝██╔██║░░
                ██║██║╚████║░╚═══██╗░░░██║░░░██╔══██║██╔══██╗██║░░██║░░░██║░░░  ░╚████╔╝░╚═╝██║░░
                ██║██║░╚███║██████╔╝░░░██║░░░██║░░██║██████╦╝╚█████╔╝░░░██║░░░  ░░╚██╔╝░░███████╗
                ╚═╝╚═╝░░╚══╝╚═════╝░░░░╚═╝░░░╚═╝░░╚═╝╚═════╝░░╚════╝░░░░╚═╝░░░  ░░░╚═╝░░░╚══════╝
                
                Version : 1.0
                Author : Rahil khan
                Library used : EasyInsta
                
                This is command-line instagram bot by which you can automate many things like sending messages, following/unfollowing mass users
                dumping chats and lot more. This bot is made using EasyInsta library and is also available for android. To download this bot on android
                just goto https://xcoder.tk/apps and install it from there. Full documentation available on https://github/ErroxCode/InstaBot
                """);
        exit("");
    }

    static void type(String text) {
        for (char a : text.toCharArray()) {
            System.out.print(a);
            try {
                Thread.sleep(25);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }


    static void clear() {
        try {
            if (Objects.requireNonNull(System.getProperty("os.name")).contains("Windows"))
                new ProcessBuilder("cmd", "/c", "cls").inheritIO().start().waitFor();
            else
                Runtime.getRuntime().exec("clear");
        } catch (IOException | InterruptedException ex) {
            ex.printStackTrace();
        }
    }


    static void exit(String message) {
        try {
            System.out.println(message + " (Press enter to exit)");
            System.in.read();
            System.exit(-1);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}