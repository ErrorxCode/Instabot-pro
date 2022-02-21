package com.insta.bot;

import com.xcoder.easyinsta.Instagram;
import com.xcoder.easyinsta.exceptions.IGLoginException;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

import me.tongfei.progressbar.ProgressBar;

public class Actions {
    public static int counter = 0;
    private static final Scanner scanner = new Scanner(System.in);
    private static Instagram instagram;
    private static ProgressBar progressBar;
    private static final Instagram.OnMessageActionCallback spamListener = new Instagram.OnMessageActionCallback() {
        @Override
        public void onSuccess(String message) {
            progressBar.step();
            counter++;
            if (counter == progressBar.getMax()) {
                progressBar.close();
                Main.showMenu();
                counter = 0;
                Main.clear();
            }
        }

        @Override
        public void onFailed(Exception exception) {
            System.out.println("Failed to send one message. Error : " + exception.getMessage());
        }

        @Override
        public void onProgress(int percentage) {

        }
    };

    static void spamGroup() {
        System.out.print("Enter spam messages (separated by comma ',') : ");
        String message = scanner.nextLine();
        System.out.print("Enter group admin username : ");
        String admin = scanner.nextLine();
        System.out.print("Enter spam count : ");
        int count = scanner.nextInt();
        System.out.println("Starting spamming bot...");
        progressBar = new ProgressBar("Spamming", count);
        instagram.getDirect(null).spamGroup(count, admin, message.split(","), spamListener);
    }


    static void performLogin() {
        PrintStream err = System.err;
        System.setErr(new PrintStream(new ByteArrayOutputStream()));
        if (!Instagram.isLogin(new File(System.getProperty("user.dir")))) {
            try {
                System.out.print("Enter instagram username : ");
                String username = scanner.nextLine();
                System.out.print("Enter instagram password : ");
                String password = scanner.nextLine();
                instagram = Instagram.loginOrCache(new File(System.getProperty("user.dir")), username, password);
                System.out.println("[!] Logged in successfully");
                Thread.sleep(2000);
                Main.clear();
            } catch (IGLoginException e) {
                Main.exit(e.getMessage());
            } catch (IOException e) {
                Main.exit("Cannot create cache. Try running program as administrator");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } else {
            try {
                instagram = Instagram.loginOrCache(new File(System.getProperty("user.dir")), "", "");
            } catch (IGLoginException | IOException e) {
                e.printStackTrace();
            }
        }
        System.setErr(err);
    }


    static void spamDM() {
        System.out.print("Enter spam messages (separated by comma ',') : ");
        String message = scanner.nextLine();
        System.out.print("Enter username (whom you want to spam) : ");
        String username = scanner.nextLine();
        System.out.print("Enter spam count : ");
        int count = scanner.nextInt();
        System.out.println("Starting spamming bot...");

        progressBar = new ProgressBar("Spamming", count);
        instagram.getDirect(username).spamDM(count, message.split(","), spamListener);
    }


    static void dumpChat() {
        System.out.print("Enter target username : ");
        String username = scanner.nextLine();
        Main.clear();
        System.out.println("""
                ---------- [ DUMPING OPTIONS ]-----------
                1. Dump from particular message
                2. Dump 'n' number of messages
                3. Dump All messages (full chat)
                """);

        System.out.print("Enter option number : ");
        int option = scanner.nextInt();
        int count = 0;
        AtomicReference<Exception> exception = new AtomicReference<>();
        AtomicReference<List<String>> chat = new AtomicReference<>();

        if (option == 1) {
            System.out.print("Enter message from where to dump : ");
            scanner.nextLine();
            String message = scanner.nextLine();
            System.out.println("Dumping. Please wait...");
            instagram.getDirect(username)
                    .getChatMessagesFrom(message, Instagram.FREQUENCY_FIRST)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful())
                            chat.set(task.getValue());
                        else
                            exception.set((Exception) task.getException());
                    });
        } else if (option == 2) {
            System.out.print("Enter no. of messages to dump : ");
            count = scanner.nextInt();
            System.out.println("Dumping. Please wait...");
            instagram.getDirect(username)
                    .getChatMessages(count, null)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful())
                            chat.set(task.getValue());
                        else
                            exception.set((Exception) task.getException());
                    });
        } else
            Main.exit("Invalid option. exiting...");

        try {
            if (exception.get() == null) {
                System.out.println("Dumping complete. Messages saved to 'chat.txt'");
                PrintWriter writer = new PrintWriter("chat.txt");
                chat.get().forEach(writer::println);
                writer.close();
            } else {
                System.out.println("An error occurred while dumping message. Logs saved to 'logs.txt'");
                PrintWriter writer = new PrintWriter("logs.txt");
                writer.println(exception.get().getLocalizedMessage());
                writer.close();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Main.exit("Cannot dump message. 'chat.txt' or 'logs.txt' file missing.");
        }
    }


    static void unfollowAll(){
        System.out.print("Enter the username you want to exclude (separated by comma) : ");
        List<String> usernames = Arrays.asList(scanner.nextLine().split(","));

        try {
            PrintWriter writer = new PrintWriter("logs.txt");
            instagram.getProfile(instagram.username).getFollowings().addOnSuccessListener(list -> {
                progressBar = new ProgressBar("Unfollowing users",list.size() - usernames.size());
                for (String user : list){
                    if (!usernames.contains(user)){
                        instagram.actions().unfollow(user).addOnCompleteListener(task -> {
                            if (task.isSuccessful())
                                progressBar.step();
                            else {
                                System.out.println("Can't unfollow" + user + ". See logs.txt for more info");
                                writer.println(task.getException().getLocalizedMessage());
                            }
                        });
                    }
                }
                progressBar.close();
            }).addOnFailureListener(exception -> {
                writer.println(exception.getLocalizedMessage());
            });
            writer.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    static void logout() {
        System.out.println("Logging out...");
        instagram.clearCache(new File(System.getProperty("user.dir")));
        System.exit(1);
    }
}
