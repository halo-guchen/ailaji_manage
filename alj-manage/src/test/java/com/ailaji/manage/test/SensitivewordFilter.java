package com.ailaji.manage.test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

@SuppressWarnings("all")
public class SensitivewordFilter {

    public HashMap sensitiveWordMap;

    public Map initKeyWord() {
        try {
            // 读取敏感词库
            Set<String> keyWordSet = readSensitiveWordFromFile();
            // 将敏感词库加入到HashMap中
            addSensitiveWordToMap(keyWordSet);
            // spring获取application，然后application.setAttribute("sensitiveWordMap",sensitiveWordMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sensitiveWordMap;
    }

    public void addSensitiveWordToMap(Set<String> wordSet) {
        sensitiveWordMap = new HashMap(wordSet.size());
        Map poolMap = null;
        HashMap<String, String> childMap = null;

        String word = null;
        Iterator<String> iterator = wordSet.iterator();
        while (iterator.hasNext()) {
            word = iterator.next();
            poolMap = sensitiveWordMap;
            for (int i = 0; i < word.length(); i++) {
                char wordChar = word.charAt(i);
                Object object = poolMap.get(wordChar);
                if (object != null) {
                    poolMap = (Map) object;
                } else {
                    childMap = new HashMap<String, String>();
                    childMap.put("isEnd", "0");
                    poolMap.put(wordChar, childMap);
                    poolMap = childMap;
                }
                if (i == word.length() - 1) {
                    poolMap.put("isEnd", "1");
                }
            }
        }
    }

    public Set<String> readSensitiveWordFromFile() throws Exception {
        Set<String> set = new HashSet<String>();
        FileInputStream fi = new FileInputStream(new File("D:\\sensitiveWord.txt"));
        InputStreamReader isr = new InputStreamReader(fi);
        BufferedReader br = new BufferedReader(isr);
        try {
            String line = null;
            while ((line = br.readLine()) != null) {
                set.add(line);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            br.close();
            isr.close();
        }
        return set;
    }

    public static void main(String[] args) {
        SensitivewordFilter sensitivewordFilter = new SensitivewordFilter();
        Map initKeyWord = sensitivewordFilter.initKeyWord();
        System.out.println(initKeyWord);
    }

}
