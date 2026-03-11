package Study_B.minjoun.boj.b14725;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

class TrieNode {
    Map<String, TrieNode> childNode = new TreeMap<>();
    boolean terminal;

    public TrieNode() {}

    public void insert(String[] word) {
        TrieNode trieNode = this;
        for (int i = 0; i < word.length; i++) {
            String str = word[i];

            trieNode.childNode.computeIfAbsent(str, val -> new TrieNode());
            trieNode = trieNode.childNode.get(str);

            if (i == word.length - 1) {
                trieNode.terminal = true;
                return;
            }
        }
    }

    public void print(int depth) {
        for (Map.Entry<String, TrieNode> entry : childNode.entrySet()) {
            for (int i = 0; i < depth; i++) System.out.print("--");
            System.out.println(entry.getKey());
            TrieNode trieNode = entry.getValue();
            if (trieNode != null) trieNode.print(depth+1);
        }
    }
}

public class Main {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringTokenizer st;

    public static void main(String[] args) throws Exception{
        // 테스트케이스 수
        int n = Integer.parseInt(br.readLine().trim());

        TrieNode trieNode = new TrieNode();
        for (int i = 0; i < n; i++) {
            st = new StringTokenizer(br.readLine().trim());
            int length = Integer.parseInt(st.nextToken());
            String[] strs = new String[length];
            for (int j = 0; j < length; j++) {
                strs[j] = st.nextToken();
            }
            trieNode.insert(strs);
        }
        trieNode.print(0);
    }
}