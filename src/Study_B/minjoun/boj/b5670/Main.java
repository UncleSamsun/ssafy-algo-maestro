package Study_B.minjoun.boj.b5670;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;

class TrieNode {
    Map<Character, TrieNode> childNode = new TreeMap<>();
    boolean terminal;

    public TrieNode() {}

    public void insert(String word) {
        TrieNode trieNode = this;
        for (int i = 0; i < word.length(); i++) {
            char ch = word.charAt(i);

            trieNode.childNode.computeIfAbsent(ch, val -> new TrieNode());
            trieNode = trieNode.childNode.get(ch);

            if (i == word.length() - 1) {
                trieNode.terminal = true;
                return;
            }
        }
    }

    public int count(String word, int depth) {
        int cnt = 0;
        int reVal = 0;
        if (depth == 0 || childNode.size() > 1 || (depth != word.length() && terminal))
            cnt++;
        if (depth != word.length()-1) {
            TrieNode trieNode = childNode.get(word.charAt(depth));
            reVal = trieNode.count(word, depth+1);
        }
        return cnt + reVal;
    }
}

public class Main {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringTokenizer st;

    public static void main(String[] args) throws Exception{
        while (true) {
            String in = br.readLine();
            if (in == null || in.isEmpty()) return;

            // 단어의 개수 N
            int N = Integer.parseInt(in);
            TrieNode trieNode = new TrieNode();
            String[] strArr = new String[N];
            for (int i = 0; i < N; i++) {
                strArr[i] = br.readLine().trim();
                trieNode.insert(strArr[i]);
            }

            int cnt = 0;
            for (int i = 0; i < N; i++) {
                cnt += trieNode.count(strArr[i], 0);
            }

            int size = strArr.length;
            double ans = Math.round((cnt / (double)size) * 100) / 100.0;
            System.out.printf("%.2f%n", ans);
        }
    }
}