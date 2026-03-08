import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.StringTokenizer;

public class Main {

    public static void main(String[] args) throws IOException {
        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
        StringTokenizer st = new StringTokenizer(br.readLine());
        int N = Integer.parseInt(st.nextToken());
        int K = Integer.parseInt(st.nextToken());
        int[] type = new int[N + 1];
        int[] target = new int[K + 1];

        for (int i = 1; i <= N; i++)
            type[i] = Integer.parseInt(br.readLine());

        Arrays.sort(type);

        // System.out.println("  coins : " + Arrays.toString(type));
        int prev = 0;

        for (int i = 1; i <= N; i++) {
            int coin = type[i];

            if (prev == coin || coin > K) continue;
            
            prev = coin;
            target[coin] = 1;

            for (int money = 1; money <= K - coin; money++) {

                if (target[money] == 0) continue;
                else {
                    
                    System.out.println((target[money] + 1) + " -,- " + target[money + coin]);
                    if (target[money + coin] == 0) target[money + coin] = target[money] + 1;
                    else target[money + coin] = Math.min(target[money] + 1, target[money + coin]);
                    System.out.println(" money : " + money + " , coin : " + coin + " , cnt : " + target[money+coin]);
                }
            }
            System.out.println("target : " + Arrays.toString(target));
        }

        int answer = target[K];
        if (target[K] == 0) answer = -1;
        
        System.out.println(answer);
    }
}
