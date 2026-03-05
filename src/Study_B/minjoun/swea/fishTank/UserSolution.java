package Study_B.minjoun.swea.fishTank;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// 구조체 설치위치 객체
class Structure {
    int id, width;

    public Structure(int id, int width) {
        this.id = id;
        this.width = width;
    }
}
// 어항 객체
class FishTank {
    int id;
    int[] length;
    int[] upShapes;

    public FishTank(int id, int[] length, int[] upShapes) {
        this.id = id;
        this.length = length;
        this.upShapes = upShapes;
    }
}
public class UserSolution {
    // 어항의 수
    int tankNum;
    // 어항의 가로길이
    int width;
    // 어항의 세로길이
    int height;
    // 정렬된 ID순서
    int[] sortedIDs;
    // 기본 ID 순서
    int[] IDs;
    // 현재 어항 상태에서 설치할 수 있는 구조체 정보
    int[][] canStructures;
    // 현재 어항 상태에서 설치할 수 있는 구조체의 개수
    int[] canStructureCounts;
    // 어떤 어항의 어떤 가로좌표에 어떤 구조체가 들어갈 수 있는지
    int[][] locStructures;
    // 어항 객체 선언
    FishTank[] fishTanks;

    public void init(int N, int mWidth, int mHeight, int[] mIDs, int[][] mLengths, int[][] mUpShapes) {
        // 기본 정보 입력
        tankNum = N;
        width = mWidth;
        height = mHeight;

        // 어항 정보 갱신
        fishTanks = new FishTank[N];
        for (int i = 0; i < N; i++) fishTanks[i] = new FishTank(mIDs[i], mLengths[i], mUpShapes[i]);

        sortedIDs = new int[N];
        canStructures = new int[64][10000];
        canStructureCounts = new int[64];
        locStructures = new int[20][500];
        // ID 번호 순으로 어항 idx 정렬
        int[] tmpIDs = new int[N];
        for (int i = 0; i < N; i++) tmpIDs[i] = mIDs[i] * 100 + i;
        Arrays.sort(tmpIDs);
        for (int i = 0; i < N; i++) sortedIDs[i] = tmpIDs[i] % 100;
        // 기본 ID 입력
        IDs = mIDs.clone();

        // 미리 지금 어항상태에서 놓을 수 있는 모든 구조체 정보를 저장함
        for(int i = 0; i<N; i++) addCanStructures(i, 0, width-3, mUpShapes[i]);
    }

    // i 어항의 구조체 정보를 저장하는 메서드
    public void addCanStructures(int i, int sts, int lst, int[] upShapes){
        for (int j = sts; j <= lst; j++) {
            int key = getKey(j, upShapes);
            int value = i * 1000 + j;
            locStructures[i][j] = key * 100000 + (canStructureCounts[key]);
            canStructures[key][canStructureCounts[key]++] = value;
        }
    }

    // i 어항의 구조체 정보를 삭제하는 메서드
    public void removeCanStructures(int i, int sts, int lst){
        // sts 부터 lst 까지 설치 될 수 있는 구조체 정보 삭제
        for (int j = sts; j <= lst; j++) {
            // locStructures에서 해당 좌표에 설치 될 수 있었던 구조체 정보를 가져옴
            int key = locStructures[i][j] / 100000;
            int count = locStructures[i][j] % 100000;
            // 만약 설치 될 수 있었던 구조체 정보가 없었다면 그냥 넘기기
            if (canStructureCounts[key] == 0) continue;
            // 만약 설치될 구조체가 배열의 마지막이었다면 그냥 0으로 하고 count-1
            if (count == canStructureCounts[key] - 1) {
                canStructures[key][canStructureCounts[key] - 1] = 0;
                canStructureCounts[key]--;
                locStructures[i][j] = 0;
            }
            // 위 조건이 아니라면
            // 맨 마지막 구조체 정보를 tmp에 저장
            // 맨 마지막 구조체 정보를 0으로 초기화
            // count 값 -1
            // 삭제해야 됐던 위치에 마지막 구조체 정보 입력
            else {
                int tmp = canStructures[key][canStructureCounts[key] - 1];
                canStructures[key][canStructureCounts[key] - 1] = 0;
                canStructureCounts[key]--;
                canStructures[key][count] = tmp;

                // 구조체가 설치될 위치 값을 새로 저장
                int tmpId = tmp / 1000;
                int tmpWidth = tmp % 1000;
                locStructures[tmpId][tmpWidth] = key * 100000 + count;
            }
        }
    }

    // 구조체 정보를 불러오기 위한 키 값을 만드는 메서드
    public int getKey(int i, int[] shapes){
        return ((shapes[i] & 3) << 4) | ((shapes[i+1] & 3) << 2) | (shapes[i+2] & 3);
    }

    // 받아온 구조물이 현재 어항 상태에 얼마나 설치될 수 있는지 개수를 반환하는 메서드
    public int checkStructures(int[] mLengths, int[] mUpShapes, int[] mDownShapes) {
        List<Structure> structures = canStructureList(mLengths, mDownShapes);
        return structures.isEmpty() ? 0 : structures.size();
    }

    // 받아온 구조물을 설치하는 메서드
    public int addStructures(int[] mLengths, int[] mUpShapes, int[] mDownShapes) {
        // 설치 가능한 구조물 위치를 리스트로 받아옴
        List<Structure> structures = canStructureList(mLengths, mDownShapes);
        // 만약 설치 가능 위치가 없다면 0반환
        if (structures.isEmpty()) return 0;

        // 어항 id가 최저인지, 어항의 가장 앞부분인지 저장용
        int minId = Integer.MAX_VALUE;
        int minWidth = Integer.MAX_VALUE;
        // 리스트를 순회
        for (Structure structure : structures) {
            int getId = structure.id;
            int getWidth = structure.width;
            // 1. 처음 값이면 바로 저장 / 2. 어항 id가 더 작다면 저장 / 3. 어항 id가 같다면 더 왼쪽인 위치를 저장
            if (minId == Integer.MAX_VALUE || IDs[getId] < IDs[minId] || (IDs[getId] == IDs[minId] && getWidth < minWidth)) {
                minId = getId;
                minWidth = getWidth;
            }
        }
        // 구조물을 어항에 설치
        for (int i = 0; i < 3; i++) {
            fishTanks[minId].length[minWidth + i] += mLengths[i];
            fishTanks[minId].upShapes[minWidth + i] = mUpShapes[i];
        }
        // 구조물이 채워진 위치에 따라 변경되야될 설치 가능 위치를 삭제
        removeCanStructures(minId, Math.max(0, minWidth - 2), Math.min(width - 3, minWidth + 2));
        // 구조물이 채워진 후 설치 가능한 위치 추가
        addCanStructures(minId, Math.max(0, minWidth - 2), Math.min(width - 3, minWidth + 2), fishTanks[minId].upShapes);

        // 반환값 리턴
        return IDs[minId] * 1000 + minWidth + 1;
    }

    public List<Structure> canStructureList(int[] mLengths, int[] mDownShapes) {
        int key = getKey(0, mDownShapes);
        List<Structure> result = new ArrayList<>();
        // 설치 가능한 어항 위치가 없다면 빈 리스트 리턴
        if (canStructureCounts[key] == 0) return result;

        for (int i = 0; i < canStructureCounts[key]; i++) {
            int id = canStructures[key][i] / 1000;
            int width = canStructures[key][i] % 1000;
            // 최대 높이보다 작은지
            if (height - fishTanks[id].length[width] >= mLengths[0] &&
                height - fishTanks[id].length[width+1] >= mLengths[1] &&
                height - fishTanks[id].length[width+2] >= mLengths[2]) {

                // 어항 구조물의 높이 차이를 구해서 새로운 구조물이 들어올 수 있는지 확인
                int div1 = fishTanks[id].length[width] - fishTanks[id].length[width+1];
                int div2 = fishTanks[id].length[width+1] - fishTanks[id].length[width+2];
                boolean b1 = div1 == 0 || (div1 > 0 ? mLengths[1] > div1 : mLengths[0] > Math.abs(div1));
                boolean b2 = div2 == 0 || (div2 > 0 ? mLengths[2] > div2 : mLengths[1] > Math.abs(div2));
                // 모든 구조물이 들어올 수 있다면 리스트에 추가
                if (b1 && b2) {
                    result.add(new Structure(id, width));
                }
            }
        }
        // 가능 리스트 반환
        return result;
    }

    public Solution.Result pourIn(int mWater) {
        Solution.Result ret = new Solution.Result();
        // 초기값 설정
        ret.height = -1;

        // 어항을 순회 (sortedIDs의 순서가 우선순위라면 그대로 사용)
        for (int i = 0; i < tankNum; i++) {
            // 어항ID가 최저인 인덱스번호를 가져옴
            int id = sortedIDs[i];
            // 어항ID 저장
            int tankID = fishTanks[id].id;

            // 해당 어항에 물을 부었을 때 꽉 차있는 최고 높이 및 물 사용량 계산
            int[] get = getMaxHeight(id, mWater);
            // 만약 물을 사용 안했으면 넘기기!
            if (get[1] == 0) continue;

            // 우선순위에 따른 결과 갱신
            // 1. 물의 높이가 더 높은지 / 2. 물 사용량이 더 많은지 / 어항ID가 더 작은지
            if (get[0] > ret.height) {
                update(ret, tankID, get[0], get[1]);
            } else if (get[0] == ret.height) {
                if (get[1] > ret.used) {
                    update(ret, tankID, get[0], get[1]);
                } else if (get[1] == ret.used && tankID < ret.ID) {
                    update(ret, tankID, get[0], get[1]);
                }
            }
        }
        return ret;
    }

    // 어항에 물을 부었을 때 최대 높이 및 물 사용량을 리턴하는 메서드
    private int[] getMaxHeight(int id, int mWater) {
        // 어항의 높이를 저장할 배열
        int[] tankHeight = new int[height+1];
        // 어항이 덜 차있는 최저 높이
        int minLength = Integer.MAX_VALUE;
        // 가로길이만큼 순회하면서 어항 높이 정보 및 최저 높이를 저장
        for (int j = 0; j < width; j++) {
            tankHeight[fishTanks[id].length[j]]++;
            minLength = Math.min(minLength, fishTanks[id].length[j]);
        }

        // 물 사용량 저장용
        int used = 0;
        // 어항의 최고높이에 도달하거나, 남은 물의 양이 어항을 채우기 부족할 때까지 반복
        while (true) {
            // 만약 남은 물의 양이 어항을 채울 수 없다면 종료
            if (mWater - tankHeight[minLength] < 0) break;

            // 어항 한줄을 채우고 남은 물의 양
            mWater -= tankHeight[minLength];
            // 어항을 채우면서 쓴 물의 양
            used += tankHeight[minLength];
            // 아래층이 빈 만큼 위에서도 비어있기 때문에 추가해야됨!
            tankHeight[minLength+1] += tankHeight[minLength];
            // 어항의 높이 증가
            minLength++;
            // 만약 높이가 어항의 크기를 넘는다면 종료
            if (minLength+1 > height) break;
        }
        // 어항이 꽉차있는 높이 및 물 사용량 리턴
        return new int[]{minLength, used};
    }

    // 어항에 물채웠을 때의 정보를 업데이트 하는 메서드
    private void update(Solution.Result res, int id, int h, int u) {
        res.ID = id;
        res.height = h;
        res.used = u;
    }
}