package asdfasdf;

import java.util.*;

class UserSolution {

	static int N, mWidth, mHeight;
	
	// mIDs[] - 어항의 아이디와, 초기 입력된 인덱스들을 압축을 통해 저장.
	static int[] mIDs;
	static int[] rankOfId;
	
	// patterns[격자들이 가질 수 있는 패턴의 경우][격자의 위치]
	// 64 - 격자가 가질 수 있는 (0 ~ 3)수들로 만들 수 있는 패턴의 가짓수.
	// 10000 - 어항의 최대 개수 (20) * 어항 최대 너비 (500)
	static int[][] patterns;
	
	// patternCnt[격자들이 가질 수 있는 패턴의 경우] - 해당 패턴이 나타나는 횟수
	static int[] patternCnt = new int[64];
	
	// tanks[어항의 초기 인덱스][어항 너비] - 해당 어항 열의 높이 + 격자 데이터 저장.
	static int[][] tanks;

	private int getPattern(int i, int j) {
		return (tanks[i][j] & 3) << 4 | (tanks[i][j + 1] & 3) << 2 | (tanks[i][j + 2] & 3);
	}
	
	private void setPatterns() {
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < mWidth - 2; j++) {
				int p = getPattern(i, j);
				patterns[p][patternCnt[p]] = i << 12 | j ;
				patternCnt[p]++;
			}
		}
	}
	
	public void init(int N, int mWidth, int mHeight, int mIDs[], int mLengths[][], int mUpShapes[][]) {

		this.N = N;
		this.mWidth = mWidth;
		this.mHeight = mHeight;
		this.mIDs = new int[N];
		rankOfId = new int[N];
		this.tanks = new int[N][500];
		patterns = new int[64][N * 500];
		
		for (int i = 0; i < N; i++) {
			mIDs[i] = mIDs[i] * 100 + i;
		}
		
		Arrays.sort(this.mIDs);
		
		for (int i = 0; i < N; i++) {
			rankOfId[mIDs[i] % 100] = i;
		}
		
		for (int i = 0; i < N; i++) {
			for (int j = 0; j < mWidth; j++) {
				tanks[i][j] = mLengths[i][j] << 2 | mUpShapes[i][j];
			}
		}
		
		setPatterns();
	}

	public int checkStructures(int mLengths[], int mUpShapes[], int mDownShapes[]) {

		int answer = 0;
		int pattern = mDownShapes[0] << 4 | mDownShapes[1] << 2 | mDownShapes[2];
		
		for (int i = 0; i < patternCnt[pattern]; i++) {
			
			int position = patterns[pattern][i];
			int idx = position >> 12;
			int row = position & 0xFFF;
			boolean flag = true;
			
			for (int k = 0; k < 3; k++) {
				int height = tanks[idx][row + k] >> 2;
				if (height + mLengths[k] > mHeight) {
					flag = false;
					break;
				}
			}
			
			if (flag) answer++;
		}
		
		return answer;
	}

	public int addStructures(int mLengths[], int mUpShapes[], int mDownShapes[]) {

		int pattern = mDownShapes[0] << 4 | mDownShapes[1] << 2 | mDownShapes[2];
		int nextPattern = mUpShapes[0] << 4 | mUpShapes[1] << 2 | mUpShapes[2];
		int rank = N;
		int idx = 0, target = 0;
		
		for (int i = 0; i < patternCnt[pattern]; i++) {
			int position = patterns[pattern][i];
			int nidx = position >> 12;
			int row = position & 0xFFF;
			boolean flag = true;
			
			for (int k = 0; k < 3; k++) {
				if((tanks[nidx][row + k] >> 2) + mLengths[k] > mHeight) {
					flag = false;
					break;
				}
			}
			
			if (!flag) continue;
			if (rankOfId[nidx] >= rank) continue;
			
			rank = rankOfId[nidx];
			target = i;
			idx = nidx;
		}

		if (rank == N) return 0;
		
		int id = mIDs[rank];
		int row = patterns[pattern][target] & 0xFFF;
		
		patternCnt[pattern]--;
		patterns[nextPattern][patternCnt[nextPattern]] = patterns[pattern][target];
		patterns[pattern][target] = patterns[pattern][patternCnt[pattern]];
		patternCnt[nextPattern]++;
		
		
		for (int k = 0; k<3; k++) {
			int height = (tanks[idx][row + k] >> 2) + mLengths[k];
			tanks[idx][row + k] = height << 2 | mUpShapes[k];
		}
		
		return id * 1000 + row;
	}

	public Solution.Result pourIn(int mWater) {
		Solution.Result ret = new Solution.Result();

		

		return ret;
	}
}