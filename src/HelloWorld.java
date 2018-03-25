import java.util.*;

public class HelloWorld {
    private final static int friendsAmount = 100;

    private final static int minFofAmount = 100;
    private final static int maxFofAmount = 100;

    private final static int test = 1000;

    private final static int[] exampleFriends = {9766, 4323, 1256, 4578};
    private final static int[][] exampleFofs = {{1256, 4323, 1897, 3244, 5678}, {4578, 1897}, {5678, 6547, 9766, 9543}, {3624}};

    private static int[] randomFriends(int amount) {
        List<Integer> friendsNumList = new ArrayList<>(10000);
        for (int i = 0; i < 10000; i++)
            friendsNumList.add(i);
        Collections.shuffle(friendsNumList);

        int[] friendArr = new int[amount];
        for (int i = 0; i < amount; i++)
            friendArr[i] = friendsNumList.get(i);
        return friendArr;
    }

    private static int[][] randomFofs() {
        int[][] fofs = new int[friendsAmount][];
        for (int i = 0; i < friendsAmount; i++)
            fofs[i] = randomFriends(new Random().nextInt(maxFofAmount - minFofAmount + 1) + minFofAmount);
        return fofs;
    }

    public static void main(String[] args) {
        test(new SlowHSet(), new FastBitSet(), new FasterInt(), new StrangeBool());
    }

    private static void test(Algorithm... algorithms) {
        int[] friendArr = /*exampleFriends;*/ randomFriends(friendsAmount);
        int[][] friendsOfFriendsArr = /*exampleFofs;*/ randomFofs();

        int counter = 1;
        for (Algorithm algorithm : algorithms) {
            System.out.print(counter++ + "). " + algorithm.name() + "\t");
            System.out.print("Answer: " + algorithm.calculate(friendArr, friendsOfFriendsArr) + "\t");

            long timeBefore = System.nanoTime();
            for (int x = 0; x < test; x++)
                algorithm.calculate(friendArr, friendsOfFriendsArr);
            System.out.printf("Time: %.3f\n", (System.nanoTime() - timeBefore) / 1000.);
        }
    }

    //O(n) memory, O(n) complexity
    private static class SlowHSet implements Algorithm {
        @Override
        public String name() {
            return "SlowHSet";
        }

        @Override
        public int calculate(int[] friendArr, int[][] friendsOfFriendsArr) {
            Set<Integer> fofs = new java.util.HashSet<>();

            int fs = friendArr.length, ffs;
            for (int i = fs; i > 0; i--) {
                fofs.add(friendArr[i - 1]);

                ffs = friendsOfFriendsArr[i - 1].length;
                for (; ffs > 0; ffs--)
                    fofs.add(friendsOfFriendsArr[i - 1][ffs - 1]);
            }

            return fofs.size() - fs;
        }
    }

    //O(1) memory, O(n) complexity
    private static class FastBitSet implements Algorithm {
        @Override
        public String name() {
            return "FastBitSet";
        }

        @Override
        public int calculate(int[] friendArr, int[][] friendsOfFriendsArr) {
            BitSet fofs = new BitSet(10_000);

            int n = friendArr.length, ofofs = -n, f;
            for (; n > 0; n--) {
                f = friendArr[n - 1];
                if (!fofs.get(f)) {
                    fofs.set(f);
                    ofofs++;
                }
                int fs = friendsOfFriendsArr[n - 1].length;
                for (; fs > 0; fs--) {
                    f = friendsOfFriendsArr[n - 1][fs - 1];
                    if (!fofs.get(f)) {
                        fofs.set(f);
                        ofofs++;
                    }
                }
            }

            return ofofs;//fofs.cardinality() - n;
        }
    }

    //O(1) memory, O(n) complexity
    private static class FasterInt implements Algorithm {
        @Override
        public String name() {
            return "FasterInt";
        }

        @Override
        public int calculate(int[] friendArr, int[][] friendsOfFriendsArr) {
            int[] fofs = new int[313];

            int fs = friendArr.length, ofos = -fs, f, ffs, mask, index;
            for (; fs > 0; fs--) {
                f = friendArr[fs - 1];
                mask = 1 << f % 32;
                index = f / 32;

                if ((fofs[index] & mask) == 0) {
                    ofos++;
                    fofs[index] |= mask;
                }

                ffs = friendsOfFriendsArr[fs - 1].length;
                for (; ffs > 0; ffs--) {
                    f = friendsOfFriendsArr[fs - 1][ffs - 1];
                    mask = 1 << f % 32;
                    index = f / 32;
                    if ((fofs[index] & mask) == 0) {
                        ofos++;
                        fofs[index] |= mask;
                    }
                }
            }

            return ofos;
        }
    }

    //O(1) memory, O(n) complexity
    private static class StrangeBool implements Algorithm {
        @Override
        public String name() {
            return "StrangeBool";
        }

        @Override
        public int calculate(int[] friendArr, int[][] friendsOfFriendsArr) {
            boolean[] fofs = new boolean[10000];

            int fs = friendArr.length, ffs, f, ofofs = -fs;
            for (; fs > 0; fs--) {
                f = friendArr[fs - 1];
                if (!fofs[f]) {
                    ofofs++;
                    fofs[f] = true;
                }

                ffs = friendsOfFriendsArr[fs - 1].length;
                for (; ffs > 0; ffs--) {
                    f = friendsOfFriendsArr[fs - 1][ffs - 1];
                    if (!fofs[f]) {
                        ofofs++;
                        fofs[f] = true;
                    }
                }
            }

            return ofofs;
        }
    }

    interface Algorithm {
        String name();
        int calculate(int[] friendArr, int[][] friendsOfFriendsArr);
    }
}
