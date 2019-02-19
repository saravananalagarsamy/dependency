package com.salesforce.dependency;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Scanner;

/**
 * The entry point for the Test program
 */
public class SalesforceMain {

    Stack depStack;

    public SalesforceMain() {
        depStack = new Stack();
    }

    public static void main(String[] args) {
        SalesforceMain m = new SalesforceMain();
        //read input from stdin
        Scanner scan = new Scanner(System.in);

        while (true) {
            String line = scan.nextLine();

            //no action for empty input
            if (line == null || line.length() == 0) {
                continue;
            }

            //the END command to stop the program
            if ("END".equals(line)) {
                System.out.println("END");
                break;
            }

            //Please provide your implementation here
            m.depStack.processInputCommand(line);
        }

    }

    class Stack {

        HashMap<String, String> depStack;
        ArrayList<String> softList;
        ArrayList<String> installedSoft;

        public Stack() {
            depStack = new HashMap<>();
            softList = new ArrayList<>();
            installedSoft = new ArrayList<>();
        }

        public void processInputCommand(String command) {
            switch (command.split(" ")[0]) {
                case "DEPEND":

                    this.dependSoft(command);
                    break;
                case "INSTALL":
                    System.out.println(command);
                    this.installSoft(command);
                    break;
                case "REMOVE":

                    this.removeSoft(command);
                    break;
                case "LIST":
                    
                    this.listSoft();
                    break;
                default:
                    System.out.println("Invalid command");
            }
        }

        public boolean addToSoft(String soft) {
            if (!softList.contains(soft)) {
                softList.add(soft);
                return true;
            }
            return false;
        }

        public ArrayList<String> getSoftListByIndex(String str) {
            ArrayList<String> data = new ArrayList<>();
            for (String s : str.split(",")) {
                data.add(softList.get(Integer.valueOf(s)));
            }
            return data;
        }

        public String getIndexList(String[] data) {
            String res = "";
            for (int i = 1; i < data.length; i++) {
                if (res != "") {
                    res = res + "," + softList.indexOf(data[i]);
                    continue;
                }
                res = String.valueOf(softList.indexOf(data[i]));
            }
            return res;
        }

        public String getKeyForExistingVlaue(HashMap<String, String> map, String val) {
            for (Entry<String, String> entry : map.entrySet()) {
                if (entry.getValue().contains(val)) {
                    return entry.getKey().toString();
                }
            }
            return null;

        }

        public String[] getListIngoreCommand(String data) {
            return data.substring(data.indexOf(" ") + 1).split(" ");
        }

        public boolean installSoft(String ins) {
            if (ins.contains("INSTALL")) {
                String[] data = getListIngoreCommand(ins);
                for (String s : data) {
                    if (!installedSoft.contains(s)) {
                        if (!depStack.containsKey(s)) {
                            installedSoft.add(s);
                            System.out.println("Installing " + s);
                        } else {
                            ArrayList<String> temp = getSoftListByIndex(depStack.get(s));
                            for (String softdep : temp) {
                                if (!installedSoft.contains(softdep)) {
                                    this.installSoft("INSTALL " + softdep);
                                }
                            }
                            installedSoft.add(s);
                            System.out.println("Installing " + s);
                        }

                    }
                    System.out.println(s+" is already installed");
                }
            }
            return true;
        }

        public boolean removeSoft(String dep) {
            if (dep.contains("REMOVE")) {
                String[] data = getListIngoreCommand(dep);
                for (String soft : data) {
                        if(depStack.containsValue(String.valueOf(softList.indexOf(soft)))){
                            String dependsOn = this.getKeyForExistingVlaue(depStack, String.valueOf(softList.indexOf(soft)));
                            ArrayList<String> deplist = getSoftListByIndex(depStack.get(dependsOn));
                            for (String depsoft : deplist) {
                                if (installedSoft.contains(depsoft)) {
                                    System.out.println(dep);
                                    System.out.println(soft + " is Still needed");
                                    return false;
                                }
                            }
                        }
                        else {
                            installedSoft.remove(soft);
                            System.out.println("Removing " + soft);
                        }
                    }
            }
            return true;
        }

        public boolean isValidDep(HashMap<String, String> stackStr, String[] arrData) {
            for (int i = 1; i < arrData.length; i++) {
                if (stackStr.containsKey(arrData[i])) {
                    String deplist = stackStr.get(arrData[1]);
                    String temp = softList.get(Integer.valueOf(deplist));
                    if (temp.contains(arrData[0])) {
                        System.out.println("DEPEND " + Arrays.toString(arrData));
                        System.out.println(arrData[i] + " depends on " + temp + ", ignoring command");
                        return false;
                    }
                }
            }
            return true;
        }

        public boolean dependSoft(String dep) {
            if (dep.contains("DEPEND")) {
                String[] data = getListIngoreCommand(dep);
                //Add to softList
                for (String soft : data) {
                    if (!softList.contains(soft)) {
                        addToSoft(soft);
                    }
                }
                if (isValidDep(depStack, data)) {
                    depStack.put(data[0], getIndexList(data));
                    System.out.println(dep);
                    return true;
                }

            }
            return false;
        }

        public void listSoft() {
            for (String data : installedSoft) {
                System.out.println(data);
            }
        }
    }

}
