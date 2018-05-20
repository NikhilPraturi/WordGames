package com.kanyakubjbrahminsamiti.akhil.wordgames;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

/**
 * Created by root on 26/1/18.
 */

public class InterfcThree {
    public static String package_name = "com.example.wordgames";
    public static String server_ip ="";
    public static Integer server_port=0;
    public static String server_user_name="";
    public static String client_user_name="";
    public static String SERVICE_TYPE="_wordgames._tcp";
    public static Integer my_score=0;
    public static Integer opponent_score=0;
    public static List<String> words =Arrays.asList("all","one","wonder","wall","west","pot","water","bottle","wow","weather",
            "goal","hill","kitchen","bell","bore");
    public static HashSet<String> words_set = new HashSet<>(Arrays.asList("all","one","wonder","wall","west","pot","water","bottle","wow",
            "weather","goal","hill","kitchen","bell","bore"));
}
