import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {
    
    static long[] nums, rec;
    static int[] position;

    public static void main(String[] args) throws IOException {
        
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        int N = Integer.parseInt(br.readLine());
        StringTokenizer st = new StringTokenizer(br.readLine());
        StringBuilder sb = new StringBuilder();
        nums = new long[N];
        rec = new long[N];
        position = new int[N];
        
        Arrays.fill(nums, Long.MAX_VALUE);
        int lastIdx = 0, start = 0;
        
        for (int i = 0; i < N; i++) {
            long n = Long.parseLong(st.nextToken());
            int pos = findPosition(0, lastIdx, n);
            
            if (pos == lastIdx) {
                lastIdx++;
                start = i;
            }

            position[i] = pos;
            rec[pos] = n;
            nums[i] = n;
        }
        
        long[] answer = new long[lastIdx];
        lastIdx--;

        // System.out.println(" last, start : " + lastIdx + " " + start);
        while (lastIdx >= 0) {
            if (position[start] == lastIdx) {
                answer[lastIdx] = nums[start];
                // System.out.println(" last : " + lastIdx + " , ans nums" + answer[lastIdx] + " " + nums[start]);
                lastIdx--;
            };

            start--;
        }
        
        // System.out.println("  - rec      : " + Arrays.toString(rec));
        // System.out.println("  - nums     : " + Arrays.toString(nums));
        // System.out.println("  - position : " + Arrays.toString(position));
        for (int i = 0; i<answer.length; i++) {
            sb.append(answer[i] + " ");
        }

        System.out.println(answer.length + "\n" + sb.toString());
    }

    static int findPosition(int left, int right, long val) {
        
        int mid = (left + right) / 2;
        
        if (left >= right) return mid;

        if (rec[mid] < val) return findPosition(mid + 1, right, val);
        else return findPosition(left, mid, val);
    }

}
