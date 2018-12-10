import java.util.*;

public class BankerDispatch {
    public PCB[] process = new PCB[5];
    public int[] wholeResource = {10, 5, 7};
    public int[] available = new int[3];
    public int[] apply = new int[3];

    BankerDispatch() {
        process[0] = new PCB();
        process[0].name = "P0";
        process[0].Max.add(7);
        process[0].Max.add(5);
        process[0].Max.add(3);
        process[0].Allocation.add(0);
        process[0].Allocation.add(1);
        process[0].Allocation.add(0);

        process[1] = new PCB();
        process[1].name = "P1";
        process[1].Max.add(3);
        process[1].Max.add(2);
        process[1].Max.add(2);
        process[1].Allocation.add(2);
        process[1].Allocation.add(0);
        process[1].Allocation.add(0);

        process[2] = new PCB();
        process[2].name = "P2";
        process[2].Max.add(9);
        process[2].Max.add(0);
        process[2].Max.add(2);
        process[2].Allocation.add(3);
        process[2].Allocation.add(0);
        process[2].Allocation.add(2);

        process[3] = new PCB();
        process[3].name = "P3";
        process[3].Max.add(2);
        process[3].Max.add(2);
        process[3].Max.add(2);
        process[3].Allocation.add(2);
        process[3].Allocation.add(1);
        process[3].Allocation.add(1);

        process[4] = new PCB();
        process[4].name = "P4";
        process[4].Max.add(4);
        process[4].Max.add(3);
        process[4].Max.add(3);
        process[4].Allocation.add(0);
        process[4].Allocation.add(0);
        process[4].Allocation.add(2);

        for (int i = 0; i < process.length; i++) {
            for (int j = 0; j < 3; j++) {
                process[i].Need.add(process[i].Max.get(j) - process[i].Allocation.get(j));
            }
        }

        for (int i = 0; i < 3; i++) {
            available[i] = wholeResource[i];
            int temp = 0;
            for (int j = 0; j < 5; j++) {
                temp += process[j].Allocation.get(i);
            }
            available[i] -= temp;
        }

        for (int i = 0; i < apply.length; i++) {
            apply[i] = 0;
        }

    }
}
