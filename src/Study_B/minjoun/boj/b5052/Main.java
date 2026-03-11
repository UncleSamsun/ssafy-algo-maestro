package Study_B.minjoun.boj.b5052;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

class TrieNode {
    Map<Character, TrieNode> childNode = new HashMap<>();
    boolean terminal;

    public TrieNode() {}

    public void insert(String word) {
        TrieNode trieNode = this;
        for (int i = 0; i < word.length(); i++) {
            char c = word.charAt(i);

            trieNode.childNode.computeIfAbsent(c, val -> new TrieNode());
            trieNode = trieNode.childNode.get(c);

            if (i == word.length() - 1) {
                trieNode.terminal = true;
                return;
            }
        }
    }

    public boolean contains(String word) {
        TrieNode trieNode = this;
        for(int i=0; i<word.length(); i++) {
            char c= word.charAt(i);
            TrieNode node = trieNode.childNode.get(c);

            // 다음 문자가 없으면 false
            if(node == null) return false;

            trieNode = node;
        }
        // 해당 단어로 종료하는 문자가 있는 경우 false
        return trieNode.terminal;
    }

    public boolean consistence(String word) {
        TrieNode trieNode = this;
        for(int i=0; i<word.length(); i++) {
            char c= word.charAt(i);
            TrieNode node = trieNode.childNode.get(c);

            // 다음 문자가 없으면 false
            if(node == null) return false;

            trieNode = node;
        }
        // 해당 단어로 종료하는 문자가 있는 경우 false
        return trieNode.childNode.isEmpty();
    }
}

public class Main {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringTokenizer st;

    public static void main(String[] args) throws Exception{
        // 테스트케이스 수
        int t = Integer.parseInt(br.readLine().trim());
        next: for (int tc = 0; tc < t; tc++) {
            int n = Integer.parseInt(br.readLine().trim());
            String[] strArr = new String[n];
            TrieNode trieNode = new TrieNode();
            for (int i = 0; i < n; i++) {
                strArr[i] = br.readLine().trim();
                trieNode.insert(strArr[i]);
            }

            for (int i = 0; i < n; i++) {
                if (!trieNode.consistence(strArr[i])) {
                    System.out.println("NO");
                    continue next;
                }
            }
            System.out.println("YES");
        }
    }
}