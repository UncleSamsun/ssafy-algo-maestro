package swea;

import java.util.*;

class UserSolution {
    static final int MAX_GROUPS_CNT = 18001;
    int last, N;
    
    int[] mIds = new int[MAX_GROUPS_CNT];
    int[] selfNum = new int[MAX_GROUPS_CNT];
    int[] totalNum = new int[MAX_GROUPS_CNT];
    int[] parentIdx = new int[MAX_GROUPS_CNT];
    int[][] children = new int[MAX_GROUPS_CNT][3];
    int[] childCnt = new int[MAX_GROUPS_CNT];
    
    Map<Integer, Integer> idToIdx = new HashMap<>();

    public void init(int N, int mId[], int mNum[]) {
        this.N = N;
        this.last = N + 1;
        idToIdx.clear();
        
        for (int i = 1; i <= N; i++) {
            mIds[i] = mId[i - 1];
            selfNum[i] = mNum[i - 1];
            totalNum[i] = mNum[i - 1];
            parentIdx[i] = 0;
            childCnt[i] = 0;
            idToIdx.put(mId[i - 1], i);
        }
    }

    public int add(int mId, int mNum, int mParent) {
    	
        int pIdx = idToIdx.get(mParent);

        if (childCnt[pIdx] >= 3) return -1;

        int cur = last++;
        
        mIds[cur] = mId;
        selfNum[cur] = mNum;
        totalNum[cur] = mNum;
        parentIdx[cur] = pIdx;
        childCnt[cur] = 0;
        idToIdx.put(mId, cur);
        
        children[pIdx][childCnt[pIdx]++] = cur;
        
        int temp = pIdx;
        
        while (temp != 0) {
            totalNum[temp] += mNum;
            temp = parentIdx[temp];
        }
        
        return totalNum[pIdx];
    }

    public int remove(int mId) {
        
    	if (!idToIdx.containsKey(mId)) return -1;
        
    	int target = idToIdx.get(mId);
        int pIdx = parentIdx[target];
        int removedWeight = totalNum[target];

        if (pIdx != 0) {
            
        	for (int i = 0; i < childCnt[pIdx]; i++) {
                if (children[pIdx][i] == target) {
                    children[pIdx][i] = children[pIdx][childCnt[pIdx] - 1];
                    childCnt[pIdx]--;
                    break;
                }
            }
        
            int temp = pIdx;
            
            while (temp != 0) {
                totalNum[temp] -= removedWeight;
                temp = parentIdx[temp];
            }
        }

        int result = totalNum[target];
        dfsRemove(target);
        
        return result;
    }

    private void dfsRemove(int cur) {
        idToIdx.remove(mIds[cur]);
        
        for (int i = 0; i < childCnt[cur]; i++)
            dfsRemove(children[cur][i]);

        
        childCnt[cur] = 0; // 초기화
    }

    public int distribute(int K) {
        int low = 0, high = 0;
        int ans = 0;
        int[] roots = new int[N];
        
        for (int i = 0; i < N; i++) {
            roots[i] = totalNum[i + 1];
            if (roots[i] > high) high = roots[i];
        }

        while (low <= high) {
        	
            int mid = low + (high - low) / 2;
            
            if (check(mid, K, roots)) {
                ans = mid;
                low = mid + 1;
            } else {
                high = mid - 1;
            }
        }
        
        return ans;
    }

    private boolean check(int limit, int K, int[] roots) {
        
    	long sum = 0;
        
    	for (int val : roots)
            sum += Math.min(val, limit);
        
    	return sum <= K;
    }
}