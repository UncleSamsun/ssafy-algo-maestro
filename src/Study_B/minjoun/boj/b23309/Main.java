package Study_B.minjoun.boj.b23309;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.StringTokenizer;

public class Main {
    static class CustomLinkedList {
        int head;
        int[] prev;
        int[] next;
        int tail;
        int size;

        public CustomLinkedList() {
            this.head = 0;
            this.tail = 0;
            this.prev = new int[1_000_001];
            this.next = new int[1_000_001];
            this.size = 0;
        }

        public void insert(int[] arr) {
            for (int i = arr.length - 1; i >= 0; i--) {
                if (head == 0) {
                    head = arr[i];
                    tail = arr[i];
                    prev[head] = tail;
                    next[tail] = head;
                }
                else {
                    int tmp = head;
                    head = arr[i];

                    next[head] = tmp;
                    prev[tmp] = head;

                    next[tail] = head;
                    prev[head] = tail;
                }
                size++;
            }
        }

        public int BN(int idx, int val) {
            // 다음 역 번호 저장
            int returnIdx = next[idx];
            // 현재역의 다음 역 정보를 추가된 역으로 변경
            next[idx] = val;
            // 새로 추가된 역의 다음 역 번호를 기존 다음역 번호로 저장
            next[val] = returnIdx;
            // 기존 다음역의 이전 역을 새로 추가된 역으로 저장
            prev[returnIdx] = val;
            // 새로 추가된 역의 이전 역을 현재 역으로 저장
            prev[val] = idx;

            // 기존 다음 역 번호 리턴
            return returnIdx;
        }

        public int BP(int idx, int val) {
            // 현재역의 이전역 번호 저장
            int returnIdx = prev[idx];
            // 현재역의 이전역 정보를 추가된 역으로 변경
            prev[idx] = val;
            // 새로 추가된 역의 이전역 번호를 기존 이전역 번호로 저장
            prev[val] = returnIdx;
            // 기존 이전역의 다음역을 새로 추가된 역으로 저장
            next[returnIdx] = val;
            // 새로 추가된 역의 다음 역을 현재 역으로 저장
            next[val] = idx;

            // 기존 이전 역 번호 리턴
            return returnIdx;
        }

        public int CN(int idx) {
            // 다음 역 번호 저장
            int returnIdx = next[idx];
            // 현재역의 다음 역 정보를 다음역이 저장하고있는 다음역 번호로 저장
            next[idx] = next[returnIdx];
            // 다음다음 역의 이전역 정보를 현재역 번호로 저장
            prev[next[idx]] = idx;

            // 기존 다음 역 번호 리턴
            return returnIdx;
        }

        public int CP(int idx) {
            // 이전 역 번호 저장
            int returnIdx = prev[idx];
            // 현재역의 이전역 정보를 이전역이 저장하고있는 이전역 번호로 저장
            prev[idx] = prev[returnIdx];
            // 이전이전 역의 다음역 정보를 현재역 번호로 저장
            next[prev[idx]] = idx;

            // 기존 이전 역 번호 리턴
            return returnIdx;
        }
    }

    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringTokenizer st;
    public static void main(String[] args) throws Exception{
        st = new StringTokenizer(br.readLine().trim());
        int N = Integer.parseInt(st.nextToken());
        int M = Integer.parseInt(st.nextToken());
        CustomLinkedList linkedList = new CustomLinkedList();
        int[] arr = new int[N];
        st = new StringTokenizer(br.readLine().trim());
        for (int i = 0; i < N; i++) {
            arr[i] = Integer.parseInt(st.nextToken());
        }

        linkedList.insert(arr);

        StringBuilder sb = new StringBuilder();

        for (int i = 0; i < M; i++) {
            st = new StringTokenizer(br.readLine().trim());
            String cmd = st.nextToken();
            int idx = Integer.parseInt(st.nextToken());
            switch (cmd) {
                case "BN" -> {
                    int val = Integer.parseInt(st.nextToken());
                    sb.append(linkedList.BN(idx, val)).append("\n");
                }
                case "BP" -> {
                    int val = Integer.parseInt(st.nextToken());
                    sb.append(linkedList.BP(idx, val)).append("\n");
                }
                case "CP" -> {
                    sb.append(linkedList.CP(idx)).append("\n");
                }
                default -> {
                    sb.append(linkedList.CN(idx)).append("\n");
                }
            }
        }
        System.out.println(sb.toString());
    }
}