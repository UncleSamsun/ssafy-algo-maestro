import java.util.*;

class UserSolution {
    class Load {
        // 다음 도시 넘버
        int city;
        // 도로의 ID
        int id;
        // 도로의 거리
        int dist;

        public Load(int city, int id, int dist) {
            this.city = city;
            this.id = id;
            this.dist = dist;
        }
    }
    class City {
        // 도시 넘버
        int num;
        // 현재까지 쓴 비용
        int cost;
        // 최소 충전 비용인 도시 번호
        int minChargeIdx;

        public City(int num, int cost, int minChargeIdx) {
            this.num = num;
            this.cost = cost;
            this.minChargeIdx = minChargeIdx;
        }
    }
    // 총 도시의 개수
    int totalCityNum;
    // 각 도시 number의 충전 비용
    int[] chargeCost;
    // 도시 그래프
    List<Load>[] cityGraph;
    // 도로 id가 어떤 도시에서 시작됐는지 확인용
    Map<Integer, Integer> whereCityForId;

    // 초기회
    public void init(int N, int mCost[], int K, int mId[], int sCity[], int eCity[], int mDistance[]) {
        totalCityNum = N;
        chargeCost = mCost.clone();
        whereCityForId = new HashMap<>();

        // 간선그래프 초기화
        cityGraph = new ArrayList[N];
        for (int i = 0; i < N; i++) {
            cityGraph[i] = new ArrayList<>();
        }
        // 노드별 간선 추가
        for (int i = 0; i < K; i++) {
            cityGraph[sCity[i]].add(new Load(eCity[i], mId[i], mDistance[i]));
            whereCityForId.put(mId[i], sCity[i]);
        }

    }

    // 도시 추가
    public void add(int mId, int sCity, int eCity, int mDistance) {
        cityGraph[sCity].add(new Load(eCity, mId, mDistance));
        whereCityForId.put(mId, sCity);
    }

    // 도시 삭제
    public void remove(int mId) {
        // 맵에서 해당 도로의 시작 도시 넘버를 가져옴
        int cityNum = whereCityForId.get(mId);
        // 시작 도시넘버의 List를 다 돌아서 도로 id와 같은게 나오면 삭제 후 브레이크
        for (int i = 0; i < cityGraph[cityNum].size(); i++) {
            Load findLoad = cityGraph[cityNum].get(i);
            if (findLoad.id == mId) {
                cityGraph[cityNum].remove(i);
                break;
            }
        }
    }

    // 비용 게산
    public int cost(int sCity, int eCity) {
        // 큐는 해당 도시의 비용을 기준으로 정렬
        PriorityQueue<City> pq = new PriorityQueue<>((c1, c2) -> c1.cost - c2.cost);
        // 시작 도시 추가
        pq.offer(new City(sCity, 0, sCity));
        // 거리 배열 초기화 -> 모든 값을 MAX_VALUE로
        int[][] dist = new int[totalCityNum][totalCityNum];
        for (int i = 0; i < totalCityNum; i++) {
            for (int j = 0; j < totalCityNum; j++)
                dist[i][j] = Integer.MAX_VALUE;
        }
        // 시작 도시 0으로 초기화
        dist[sCity][sCity] = 0;
        // 마지막 도시 도착했을 때 결과값 리턴용
        int result = Integer.MAX_VALUE;
        // 큐 내용물 반복
        while (!pq.isEmpty()) {
            // 도시정보 가져옴
            City nowCity = pq.poll();
            int cityNum = nowCity.num;
            int cost = nowCity.cost;
            int minChargeIdx = nowCity.minChargeIdx;

            // 다익스트라 가지치기
            if (cost != dist[cityNum][minChargeIdx]) continue;
            // 만약 현재 도시가 도착도시라면 최소비용 저장 후 브레이크
            if (cityNum == eCity) {
                result = cost;
                break;
            }

            // 해당 도시에서 연결된 다음 도시 순회
            for (Load list : cityGraph[cityNum]) {
                // 다음 도시 넘버
                int nextCityNum = list.city;
                // 다음 도시로 가기위한 비용
                int nextCost = cost + chargeCost[minChargeIdx] * list.dist;
                // 최소비용을 가진 도시번호 비교
                int nextMinChargeIdx = minChargeIdx;
                // 만약 다음 도시의 비용이 최소비용이라면 최소비용도시넘버를 다음 도시넘버로 변경
                if (chargeCost[minChargeIdx] > chargeCost[nextCityNum]) {
                    nextMinChargeIdx = nextCityNum;
                }
                // 만약 다음 도시 비용이 갱신된다면 큐에 추가
                if (nextCost < dist[nextCityNum][nextMinChargeIdx]) {
                    dist[nextCityNum][nextMinChargeIdx] = nextCost;
                    pq.offer(new City(nextCityNum, nextCost, nextMinChargeIdx));
                }
            }
        }

        // 결과 반환
        return result == Integer.MAX_VALUE ? -1 : result;
    }
}