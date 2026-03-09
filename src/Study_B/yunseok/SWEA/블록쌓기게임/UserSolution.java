package swea;

import java.util.Arrays;

class UserSolution {
		
	static final int MOD = 1_000_000;
	
	// 시간초과로 객체 배열 -> 기본형 배열로 교체
	// 3015ms -> 619ms로 개선.
	// 엄청나게 많은 값들이 들어올 때, 차이가 크다.
	// 주이유는 캐시 지역성 (Cache Locality) 
	// -> 메모리에서 CPU로 데이터 가져올 때, 딱 그 값만 가져오는 게 아닌,
	//    주변 데이터도 묶음으로 가져오기 때문( - java Cache Line 검색)
	// 보통 64로 가져오기 때문에, long 배열이면, 8개씩 가져옴 -> Cache 히트 적중률 증가.
	// 하지만 Node 객체 만들어서 사용하면, 캐시 히트하더라도,
	// 메모리 연산 한번 더 거쳐서 가야하기에 더 느릴 수 밖에 없다.
	
//	class Node {
//		long add;
//		long minValue;
//		long maxValue;
//		
//		Node(long add, long minValue, long maxValue) {
//			this.add = add;
//			this.minValue = minValue;
//			this.maxValue = maxValue;
//		}	
//	}
	
//	Node[] segTree;
	
	long[] add = new long[MOD << 2];
	long[] minValue = new long[MOD << 2];
	long[] maxValue = new long[MOD << 2];
	long total;
	int C;
	
	// 격자판의 열의 개수 C가 주어짐.
	// 10 <= C <= 1_000_000
    void init(int C) {
    	
    	total = 0;
    	this.C = C;
    	
    	for (int i = 0; i < C * 4; i++) {
    		add[i] = 0;
    		minValue[i] = 0;
    		maxValue[i] = 0;
    	}
    	
//    	segTree = new Node[C * 4];
    	
//    	for (int i = 1; i < C * 4; i++)
//    		segTree[i] = new Node(0, 0, 0);

    }

    
    // mCol : 떨어지는 블록이 위치한 시작열. ( 0 <= mCol <= C - mLength )
    // mHeight, mLength : 블록들의 높이와 너비.
    // ( 1 <= mHeight <= 3 ,  2 <= mLength <= C )
    // return :
    // ret.top 	 - 남은 블록 중, 가장 높은 블록의 높이 (Integer limit 을 벗어날 수 있음.)
    // ret.count - 남아있던 블록들의 개수를 1_000_000으로 나눈 나머지
    // - 최대 3000회 호출.
    Solution.Result dropBlocks(int mCol, int mHeight, int mLength) {
        Solution.Result ret = new Solution.Result();

        if (mLength != C) {
        	total += mHeight * mLength;
        	        	
        	updateTree(0, C - 1, mCol, mCol + mLength - 1, mHeight, 1);
        }
        
        
        // 트리가 업데이트 됐다면, 루트 노드가, 모든 구간에 대한 정보를 알고 있음.
//      ret.top = (int)(segTree[1].maxValue - segTree[1].minValue);
//      ret.count = (int)((total - segTree[1].minValue * C) % MOD);
        ret.top = (int)(maxValue[1] - minValue[1]);
        ret.count = (int)((total - minValue[1] * C) % MOD);
        
        return ret;
    }

    
    // start, end - 현재 노드가 갖고 있는 구간 정보
    // left, right - 내가 원하는 구간에서 포함하고 있는 양 끝값.
    // value - 업데이트하려는 값.
    void updateTree(int start, int end, int left, int right, int value, int idx) {

    	// 범위를 벗어났으면 아웃.
    	if (start > end || start > right || end < left) return;
    	if (start >= left && end <= right) {
//    		segTree[idx].add += value;
//    		segTree[idx].minValue += value;
//    		segTree[idx].maxValue += value;
    		add[idx] += value;
    		minValue[idx] += value;
    		maxValue[idx] += value;
    		return;
    	}
    	
    	int mid = (start + end) / 2;
    	
    	// 자식 노드들을 먼저 업데이트 하고,
    	updateTree(start, mid, left, right, value, idx << 1);
    	updateTree(mid + 1, end, left, right, value, idx << 1 | 1);
    	
    	// 현재 노드에 대한 업데이트.
//    	segTree[idx].minValue = Math.min(segTree[idx * 2].minValue, segTree[idx * 2 + 1].minValue) + segTree[idx].add;
//    	segTree[idx].maxValue = Math.max(segTree[idx * 2].maxValue, segTree[idx * 2 + 1].maxValue) + segTree[idx].add;
    	minValue[idx] = Math.min(minValue[idx << 1], minValue[idx << 1 | 1]) + add[idx];
    	maxValue[idx] = Math.max(maxValue[idx << 1], maxValue[idx << 1 | 1]) + add[idx];
    }
}
