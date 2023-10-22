package com.example.todolistapp;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;

public class TodoListApp {

//    deklarasi variabel
    static String fileName;
    static ArrayList<String> todoList;
    static boolean isEditing = false;
    static Scanner input;

    public static void main(String[] args) {
        todoList = new ArrayList<>();
        input = new Scanner(System.in);
        String filePath = System.console() == null ? "/src/todolist.txt" : "/todolist.txt";
        fileName = System.getProperty("user.dir") + filePath;

        System.out.println("File : " + fileName);
        while (true){
            showMenu();
        }
    }

    static void clearScreen(){
        try{
            final String os = System.getProperty("os.name");
            if (os.contains("Windows")){
                // clear screen untuk windows
                new ProcessBuilder("cmd", "/c", "cls")
                        .inheritIO().start().waitFor();
            } else {
                Runtime.getRuntime().exec("clear");
                System.out.print("\033[H\033[2J");
                System.out.flush();
            }
        } catch(final Exception e){
            System.out.println("Error karena: " + e.getMessage());
        }
    }

    static void showMenu(){
        System.out.println("====== TODO LIST APP ======");
        System.out.println("[1] Lihat Todo List");
        System.out.println("[2] Tambah Todo List");
        System.out.println("[3] Edit Todo List");
        System.out.println("[4] Hapus Todo List");
        System.out.println("[0] Keluar");
        System.out.println("----------------------------");
        System.out.println("Pilih menu> ");

        String selectedMenu = input.nextLine();

        switch (selectedMenu){
            case "1" :
                showTodoList();
                break;
            case "2" :
                addTodoList();
                break;
            case "3":
                editTodoList();
                break;
            case "4":
                deleteTodoList();
                break;
            case "0":
                System.exit(0);
                break;
            default:
                System.out.println("Pilihan kamu salah !!!");
                backToMenu();
                break;
        }
    }

    static void backToMenu(){
        System.out.println("");
        System.out.println("Tekan [Enter] untuk kembali ...");
        input.nextLine();
        clearScreen();
    }

    static void readTodoList(){
        try{
            File file = new File(fileName);
            Scanner fileReader = new Scanner(file);
            //load isi file ke dalam array todoList
            todoList.clear();
            while(fileReader.hasNextLine()){
                String data = fileReader.nextLine();
                todoList.add(data);
            }
        } catch (FileNotFoundException e){
            System.out.println("Error karena : " +e.getMessage());
        }
    }

    static void showTodoList(){
        clearScreen();
        readTodoList();
        if(todoList.size() > 0){
            System.out.println("TODO LIST:");
            int index = 0;
            for(var data : todoList){
                System.out.println(String.format("[%d] %s", index, data));
                index++;
            }
        } else {
            System.out.println("Tidak ada data!");
        }

        if (!isEditing){
            backToMenu();
        }
    }

    static void addTodoList(){
        clearScreen();

        System.out.println("Apa yang ingin kamu kerjakan?");
        System.out.println("Jawab : ");
        String newTodoList = input.nextLine();

        try{
            FileWriter fileWriter = new FileWriter(fileName, true);
            fileWriter.append(String.format("%s%n", newTodoList));
            fileWriter.close();
            System.out.println("Berhasil ditambahkan!");
        } catch (IOException e){
            System.out.println("Terjadi kesalahan karena: " + e.getMessage());
        }

        backToMenu();
    }

    static void editTodoList(){
        isEditing = true;
        showTodoList();

        try{
            System.out.println("-------------");
            System.out.println("Pilih Indeks> ");
            int index = Integer.parseInt(input.nextLine());

            if(index > todoList.size()){
                throw new IndexOutOfBoundsException("Kamu memasukkan data yang salah");
            } else {
                System.out.println("Data baru: ");
                String newData = input.nextLine();

//                update data
                todoList.set(index, newData);

                System.out.println(todoList.toString());

                try{
                    FileWriter fileWriter = new FileWriter(fileName, false);
//                    write new data
                    for(var data : todoList){
                        fileWriter.append(String.format("%s%n", data));
                    }
                    fileWriter.close();
                    System.out.println("Berhasil diubah");
                } catch (IOException e){
                    System.out.println("Terjadi kesalahan karena: " + e.getMessage());
                }
            }
        } catch (Exception e){
            System.out.println(e.getMessage());
        }

        isEditing = false;
        backToMenu();
    }

    static void deleteTodoList(){
        isEditing = true;
        showTodoList();

        System.out.println("---------");
        System.out.println("Pilih Indeks> ");
        int index = Integer.parseInt(input.nextLine());

        try{
            if(index > todoList.size()){
                throw new IndexOutOfBoundsException("Kamu memsakuukan data yang salah");
            } else{
                System.out.println("Kamu akan menghapus:");
                System.out.printf("[%d] %s%n", index, todoList.get(index) );
                System.out.println("Apa kamu yakin?");
                System.out.println("Jawab y/t : ");
                String jawab = input.nextLine();

                if(jawab.equalsIgnoreCase("y")){
                    todoList.remove(index);
                }

//                tulis ulang file
                try{
                    FileWriter fileWriter = new FileWriter(fileName, false);

                    // write new data
                    for(var data : todoList){
                        fileWriter.append(String.format("%s%n", data));
                    }
                    fileWriter.close();

                    System.out.println("Berhasil dihaspus !");
                } catch (IOException e){
                    System.out.println("Terjadi kesalahan karena : " + e.getMessage());
                }
            }
        } catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
}
