import java.util.*;
 
class UserSolution {
 
    private Object object;
 
    //노드에 들어갈 정보 : 도시의 id, cost, 거리, 길 id, 전체 합 
 
    static class Node implements Comparable<Node> {
        int cityId, totalSum, cost;
        int Rdist, roadId;
 
        Node(int cityId, int cost, int Rdist, int roadId, int totalSum) {
            this.cityId = cityId;
 
            this.cost = cost;
            this.roadId = roadId;
            this.Rdist = Rdist;
             
            this.totalSum = totalSum; 
        }
 
        @Override
        public int compareTo(Node o) {
            return Integer.compare(this.totalSum, o.totalSum);
        }
 
    }
 
    static int cityNum;
    static int roadNum;
    static int[] mCostArr;
    static List<Node>[] graph;
 
    public void init(int N, int mCost[], int K, int mId[], int sCity[], int eCity[], int mDistance[]) {
 
        // 인접 리스트 -> 노드 리스트를 담는 배열을 선언한다.
        cityNum = N;
        mCostArr = new int[N];
        for (int i = 0; i < N; i++) {
            mCostArr[i] = mCost[i];
        }
 
        graph = new ArrayList[N];
        for (int i = 0; i < N; i++) {
            graph[i] = new ArrayList<>();
        }
 
        for (int i = 0; i < K; i++) {
            int sCityIdx = sCity[i];
            int eCityIdx = eCity[i];
            int eCityCost = mCostArr[eCityIdx];
            int mDistanceVal = mDistance[i];
            int mIdVal = mId[i];
 
            graph[sCityIdx].add(new Node(eCityIdx, eCityCost, mDistanceVal, mIdVal, 0));
 
        }
 
        return;
    }
 
    public void add(int mId, int sCity, int eCity, int mDistance) {
        // s와 e를 잇는 길을 인접 리스트에 추가한다.
        graph[sCity].add(new Node(eCity, mCostArr[eCity], mDistance, mId, 0));
 
    }
 
    public void remove(int mId) {
        // 인접 리스트에서 삭제한다
        for (int i = 0; i < cityNum; i++) {
            for (int j = 0; j < graph[i].size(); j++) {
                if (graph[i].get(j).roadId == mId) {
                    graph[i].remove(j);
                }
            }
        }
        return;
    }
 
    public int cost(int sCity, int eCity) {
        // 상태확장 다익스트라
        int[][]dist = new int[cityNum][2001]; //충전 비용 
         
        for(int i=0 ; i< cityNum ; i++) {
            Arrays.fill(dist[i], Integer.MAX_VALUE);
        }
         
 
        dist[sCity][mCostArr[sCity]] = 0; 
        PriorityQueue<Node> pq = new PriorityQueue<>();
        pq.add(new Node(sCity, mCostArr[sCity], 0, 0, 0));
 
        while (!pq.isEmpty()) {
 
            Node curr = pq.poll();
             
            if(curr.totalSum > dist[curr.cityId][curr.cost] ) continue; 
             
            if(curr.cityId == eCity){
                return curr.totalSum; 
            }
            else {
                for(Node next : graph[curr.cityId]) {
                    int nextSum = curr.totalSum + (curr.cost * next.Rdist); 
                    int nextCost = Math.min(curr.cost, next.cost); 
                     
                    if(nextSum < dist[next.cityId][nextCost]) {
                        dist[next.cityId][nextCost] = nextSum; 
                        pq.add(new Node(next.cityId, nextCost, next.Rdist, next.roadId, nextSum)); 
                    }
                }
            }
        }
 
        return -1; 
    }
}