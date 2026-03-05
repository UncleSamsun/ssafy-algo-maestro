import java.util.*;

class UserSolution {

	// 걍 무한대 상수
	static final int INF = Integer.MAX_VALUE;
	
	// N - 어항의 개수 
	// mWidth, mHeight - 어항의 최대 너비, 최대 높이
	static int N, mWidth, mHeight;
	
	// mIDs - 어항의 ID + 어항의 인덱스 값을 저장하는 자료구조
	// - ex) id: 50, idx: 1 -> 50 * 100 + 1 = 5001
	// 		  이런식으로 자료 압축하여 저장하고 관리.
	// rankOfId - 어항의 인덱스 값으로, 우선순위 추적 가능하게 관리하는 자료 구조.
	static int[] mIDs;
	static int[] rankOfId;
	
	
	// patterns - 결합판 3개로 만들 수 있는 무늬들에 대해서 어항의 인덱스와, 해당 어항에서의 열을 자료 압축하여 관리하는 자료구조.
	// patternCnt - 결합판 3개로 만들 수 있는 해당 무늬가 모든 어항에 대해서 총 몇개 존재하고 있는지 저장하고 관리하는 자료구조.
	static int[][] patterns;
	static int[] patternCnt = new int[64];
	
	// tanks - 어항들
	static int[][] tanks;

	// i 인덱스를 가지는 어항에서 j번째 열부터 시작하는 패턴 반환.
	private int getPattern(int i, int j) {
		return (tanks[i][j] & 3) << 4 | (tanks[i][j + 1] & 3) << 2 | (tanks[i][j + 2] & 3);
	}
	
	// patterns 배열 초기화 함수.
	private void setPatterns() {
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < mWidth - 2; j++) {
				int p = getPattern(i, j);
				patterns[p][patternCnt[p]] = i << 12 | j ;
				patternCnt[p]++;
			}
		}
	}
	
	// 초기화. - 1회 호출.
	public void init(int N, int mWidth, int mHeight, int mIDs[], int mLengths[][], int mUpShapes[][]) {

		this.N = N;
		this.mWidth = mWidth;
		this.mHeight = mHeight;
		this.mIDs = new int[N];
		rankOfId = new int[N];
		// 어항의 최대 너비는 500이기 때문에, 500으로 할당.
		this.tanks = new int[N][500];
		patterns = new int[64][N * 500];
		
		// patternCnt를 0으로 재 초기화.
		// -> 그냥 new로 초기화 해도 됨.
		Arrays.fill(patternCnt, 0);
		
		// 자료 압축하여 저장.
		for (int i = 0; i < N; i++)
			this.mIDs[i] = mIDs[i] * 100 + i;

		// id 순서대로 mIDs 배열 정렬.
		// - 배열의 순서가 바뀌더라도, 어항의 인덱스 값을 mIDs[i] % 100으로 추적할 수 있기 때문에,
		//	 아이디 우선순위대로 정렬해도 괜찮다.
		Arrays.sort(this.mIDs);
		
		// 어항의 인덱스로, 바로 우선순위를 추적할 수 있는 rankOfId 배열 초기화.
		// - 우선순위대로 정렬된 mIDs 배열의 인덱스 값에, 우선순위를 저장.
		// ex) mIDs = [5001, 1002, 2000, 3003] 이라면,
		//		rankOfId = [2, 0, 1, 3] 으로 저장된다.
		// 따라서, 0번 인덱스 어항의 id 우선순위는 두번째이다.
		for (int i = 0; i < N; i++)
			rankOfId[this.mIDs[i] % 100] = i;
		
		// i 인덱스 어항의 j 번째 열에는, 그 열에 위치한 구조물의 높이와, 결합판의 종류를 자료압축하여 저장.
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < mWidth; j++) {
				tanks[i][j] = mLengths[i][j] << 2 | mUpShapes[i][j];
			}
		}
		
		// 결합판들로 만들 수 있는 패턴 초기화 저장.
		setPatterns();
	}

	// 주어진 구조물이 설치될 수 있는 위치가 몇개 존재하는지 확인하는 함수.
	// - 최대 10_000번 호출.
	public int checkStructures(int mLengths[], int mUpShapes[], int mDownShapes[]) {

		// pattern - 주어진 구조물의 아래에 적혀있는 결합판 무늬.
		int answer = 0;
		int pattern = mDownShapes[0] << 4 | mDownShapes[1] << 2 | mDownShapes[2];
		
		// 해당 패턴이 이 어항에 존재하는 만큼 반복.
		for (int i = 0; i < patternCnt[pattern]; i++) {
			
			// position - 해당 패턴이 어느 어항, 어느 열에 위치하는지 압축된 자료
			// row - 12번 비트 이후로 인덱스 값을 저장하고 있어, 12번째 비트까지 AND 연산을 해도 되나, 그냥 11번 비트까지만 확인했음.
			int position = patterns[pattern][i];
			int idx = position >> 12;
			int row = position & 0x7FF;
			boolean flag = true;
			int prevHeight = 0;

			// 해당 위치에서 주어진 구조물을 설치할 수 있는지 확인.
			for (int k = 0; k < 3; k++) {
				int height = tanks[idx][row + k] >> 2;

				// 주어진 구조물들이 붙어있을 수 있는지,
				// 주어진 구조물들을 설치했을 때, 높이 제한을 넘기진 않는지 확인.
				if (
					(k > 0 && !isConnected(height, mLengths[k], prevHeight, mLengths[k-1])) ||
					height + mLengths[k] > mHeight
				) {
					flag = false;
					break;
				}

				prevHeight = height;
			}
			
			// flag - 설치할 수 있다면  answer 증가.
			if (flag) answer++;
		}
		
		return answer;
	}

	boolean isConnected(int height, int curDiff, int prevHeight, int prevDiff) {

		int prevBottom = prevHeight + 1;
		int prevTop = prevHeight + prevDiff;
		int bottom = height + 1;
		int top = height + curDiff;

		if (prevTop < bottom || prevBottom > top) return false;
		return true;
	}

	public int addStructures(int mLengths[], int mUpShapes[], int mDownShapes[]) {

		int pattern = mDownShapes[0] << 4 | mDownShapes[1] << 2 | mDownShapes[2];
		int rank = N + 1;
		int idx = 0, target = 0, targetRow = INF;
		
		for (int i = 0; i < patternCnt[pattern]; i++) {
			
			int position = patterns[pattern][i];
			int nidx = position >> 12;
			int row = position & 0x7FF;
			int prevHeight = 0;
			boolean flag = true;
			
			for (int k = 0; k < 3; k++) {

				int height = tanks[nidx][row + k] >> 2;

				if((k > 0 && !isConnected(height, mLengths[k], prevHeight, mLengths[k - 1])) ||
					height + mLengths[k] > mHeight
				) {
					flag = false;
					break;
				}

				prevHeight = height;
			}
			
			if (!flag) continue;
			if (rankOfId[nidx] > rank) continue;
			
			if (rankOfId[nidx] != rank || targetRow > row) {
				rank = rankOfId[nidx];
				target = i;
				targetRow = row;
				idx = nidx;
			}
		}

		if (rank == N + 1) return 0;

		int id = mIDs[rank] / 100;
		int row = patterns[pattern][target] & 0x7FF;
		int cidx = mIDs[rank] % 100;
		
		resetPattern(cidx, row, mUpShapes);

		for (int k = 0; k<3; k++) {
			int height = (tanks[idx][row + k] >> 2) + mLengths[k];
			tanks[idx][row + k] = height << 2 | mUpShapes[k];
		}
		
		return id * 1000 + row + 1;
	}

	void removePattern(int pattern, int idx, int row) {
		
		for (int i = 0; i < patternCnt[pattern]; i++) {
			
			if (patterns[pattern][i] == (idx << 12 | row)) {
				
				patterns[pattern][i] = patterns[pattern][patternCnt[pattern] - 1];
				patterns[pattern][patternCnt[pattern] - 1] = 0;
				
				break;
			}
		}
	
		patternCnt[pattern]--;
	}
	
	int makePattern(int idx, int start ) { 
		
		return  (tanks[idx][start] & 3 << 4) 		|
				(tanks[idx][start + 1] & 3 << 2)	|
				(tanks[idx][start + 2] & 3);	
	}
	
	void resetPattern(int idx, int row, int[] mUpShapes) {

		for (int i = -2; i <= 2; i++) {
	
			int start = row + i;
			
			if (start < 0 || start > mWidth - 3) continue;
	
			int oldPattern = makePattern(idx, start);
			
			removePattern(oldPattern, idx, start);
		}
		
		for (int i = 0; i < 3; i++) {
			int height = tanks[idx][row + i] >> 2;
			tanks[idx][row + i] = height << 2 | mUpShapes[i];
		}
			
		for (int i = -2; i <= 2; i++) {
			
			int start = row + i;
			
			if (start < 0 || start > mWidth - 3) continue;

			int newPattern = makePattern(idx, start);	
			int position = (idx << 12) | start;
		
			patterns[newPattern][patternCnt[newPattern]] = position;
			patternCnt[newPattern]++;
		}
	}

	public Solution.Result pourIn(int mWater) {
		Solution.Result ret = new Solution.Result();
		ret.ID = ret.height = ret.used = 0;
		
		for (int idAndIdx : mIDs) {
			
			// heightCnt - 물 채울때 사용하는 자료구조. 어항에 존재하는 구조물들의 높이를 저장.
			// ex) 어항의 구조물들의 높이가, [0, 0, 1, 3, 2] 라면,
			//		heightCnt는 [2,1,1,1,0,0] 뭐 이런식으로 저장되어있다.
			// 		값을 누적하여 더해가는 방식으로 사용.
			int[] heightCnt = new int[mHeight + 1];
			
			int id = idAndIdx / 100;
			int idx = idAndIdx % 100;
			int used = 0;
			int[] tank = tanks[idx];
			int minHeight = INF;
			
			for (int i = 0; i < mWidth; i++) {
				int h = tank[i] >> 2;
				minHeight = Math.min(h, minHeight);
				heightCnt[h]++;
			}
			
			
			while (minHeight < mHeight) {
				
				if (used + heightCnt[minHeight] > mWater) break;
				
				heightCnt[minHeight + 1] += heightCnt[minHeight];
				used += heightCnt[minHeight];
				minHeight++;

			}

			if (used == 0 || used > mWater) continue;
			
			if (minHeight > ret.height) {
				ret.used = used;
				ret.ID = id;
				ret.height = minHeight;
			} else if (minHeight == ret.height && used > ret.used) {
				ret.ID = id;
				ret.used = used;
			}
		}
		
		return ret;
	}
}
