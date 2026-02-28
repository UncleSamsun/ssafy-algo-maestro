import java.util.*;
import java.io.*;

public class Solution {
    static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
    static StringTokenizer st;
    static Deque<Integer>[] qArr = new ArrayDeque[5];
    static ArrayList<Integer>[] arr = new ArrayList[5];

    public static void main(String[] args) throws IOException {

        int T = Integer.parseInt(br.readLine());
        for (int t = 1; t <= T; t++) {
            for (int i = 1; i < 5; i++) {
                qArr[i] = new ArrayDeque<>();
                arr[i] = new ArrayList<>();
            }
            int K = Integer.parseInt(br.readLine());
            for (int i = 1; i < 5; i++) {
                st = new StringTokenizer(br.readLine());
                for (int j = 0; j < 8; j++) {
                    int tp = Integer.parseInt(st.nextToken());
                    qArr[i].offer(tp);
                    arr[i].add(tp);
                }
            }
            for (int i = 0; i < K; i++) {
                st = new StringTokenizer(br.readLine());
                int target = Integer.parseInt(st.nextToken());
                int direction = Integer.parseInt(st.nextToken());
                int[] dirs = getRotateArr(target, direction);

                for (int j = 1; j <= 4; j++) {
                    if (dirs[j] == 1) {
                        qArr[j].offerFirst(qArr[j].pollLast());
                        arr[j] = new ArrayList<>(qArr[j]);
                    } else if (dirs[j] == -1) {
                        qArr[j].offer(qArr[j].poll());
                        arr[j] = new ArrayList<>(qArr[j]);
                    }
                }
            }
            int result = 0;
            for (int i = 1; i <= 4; i++) {
                result += arr[i].get(0) == 0 ? 0 : (int) Math.pow(2, i - 1);
            }
            System.out.printf("#%d %d\n", t, result);
        }
    }

    static int[] getRotateArr(int target, int direction) {
        int[] rotateArr = {-1, -1, -1, -1, -1};
        rotateArr[target] = direction;
        int tmp = target;
        while (tmp + 1 < 5) {
            if (rotateArr[tmp] == 0) {
                rotateArr[tmp + 1] = 0;
            } else if (arr[tmp].get(2) == arr[tmp + 1].get(6))
                rotateArr[tmp + 1] = 0;
            else {
                if (rotateArr[tmp] == 1)
                    rotateArr[tmp + 1] = -1;
                else if (rotateArr[tmp] == -1)
                    rotateArr[tmp + 1] = 1;
            }
            tmp++;
        }
        tmp = target;
        while (tmp - 1 > 0) {
            if (rotateArr[tmp] == 0)
                rotateArr[tmp - 1] = 0;
            else if (arr[tmp].get(6) == arr[tmp - 1].get(2))
                rotateArr[tmp - 1] = 0;
            else {
                if (rotateArr[tmp] == 1)
                    rotateArr[tmp - 1] = -1;
                else if (rotateArr[tmp] == -1)
                    rotateArr[tmp - 1] = 1;
            }
            tmp--;
        }
        return rotateArr;
    }
}