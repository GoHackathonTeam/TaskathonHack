import java.util.Scanner;

public class Solution {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] fofs = new int[313];

        int friendsAmout = scanner.nextInt(), onlyFofs = -friendsAmout,
                friend, friendsOfFriendAmount, mask, index;
        for (; friendsAmout > 0; friendsAmout--) {
            friend = scanner.nextInt();
            mask = 1 << friend % 32;
            index = friend / 32;

            if ((fofs[index] & mask) == 0) {
                onlyFofs++;
                fofs[index] |= mask;
            }

            friendsOfFriendAmount = scanner.nextInt();
            for (; friendsOfFriendAmount > 0; friendsOfFriendAmount--) {
                friend = scanner.nextInt();
                mask = 1 << friend % 32;
                index = friend / 32;
                if ((fofs[index] & mask) == 0) {
                    onlyFofs++;
                    fofs[index] |= mask;
                }
            }
        }

        System.out.println(onlyFofs);
    }
}
