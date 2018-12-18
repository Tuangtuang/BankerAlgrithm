import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class JFrameDemo extends JFrame {
    public static JTextField[][] vector = new JTextField[3][5];//文本输入框
    public static JLabel availableValue = new JLabel();
    public static JButton confirm = new JButton("确认");
    public static JTextField PcbInput = new JTextField();
    public static JTextField ResourceInput = new JTextField();
    public static BankerDispatch dispatch = new BankerDispatch();

    public static void main(String[] args) {
        JFrame frame = new JFrame("银行家算法");
        frame.setSize(1050, 600);//设置窗口大小
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);//设置为可以关闭
        //创建面板
        JPanel panel = new JPanel();
        frame.add(panel);
//        BankerDispatch dispatch=new BankerDispatch();
//        dispatch.Initiate();
        placeComponents(panel);
        frame.setVisible(true);
        actionHandle();

    }

    public static void placeComponents(JPanel panel) {
        panel.setLayout(null);//设置布局
        JLabel P0 = new JLabel("P0:");
        P0.setBounds(10, 40, 80, 50);
        panel.add(P0);
        JLabel P1 = new JLabel("P1:");
        P1.setBounds(10, 100, 80, 50);
        panel.add(P1);
        JLabel P2 = new JLabel("P2:");
        P2.setBounds(10, 160, 80, 50);
        panel.add(P2);
        JLabel P3 = new JLabel("P3:");
        P3.setBounds(10, 220, 80, 50);
        panel.add(P3);
        JLabel P4 = new JLabel("P4:");
        P4.setBounds(10, 280, 80, 50);
        panel.add(P4);
        JLabel Max = new JLabel("Max");
        Max.setBounds(110, 10, 80, 50);
        panel.add(Max);
        JLabel Allocation = new JLabel("Allocation");
        Allocation.setBounds(210, 10, 80, 50);
        panel.add(Allocation);
        JLabel Need = new JLabel("Need");
        Need.setBounds(310, 10, 80, 50);
        panel.add(Need);
        JLabel Available = new JLabel("Available");
        Available.setBounds(410, 10, 80, 50);
        panel.add(Available);
        JLabel Right = new JLabel("Created by 唐雨琪 on 2018/12/8. All rights reserved.");
        Right.setBounds(650, 500, 400, 100);
        panel.add(Right);

//        JTextField [][] vector=new JTextField[3][5];//文本输入框
        for (int i = 0; i < 3; i++) {
            for (int j = 0; j < 5; j++) {
                vector[i][j] = new JTextField();
                vector[i][j].setBounds(100 + i * 100, 40 + j * 60, 80, 40);
                panel.add(vector[i][j]);
            }
        }

        JLabel PcbId = new JLabel("请输入进程号：");
        PcbId.setBounds(700, 0, 200, 70);
        panel.add(PcbId);
//        JTextField PcbInput=new JTextField();
        PcbInput.setBounds(700, 50, 200, 40);
        panel.add(PcbInput);
        JLabel Resource = new JLabel("请求资源数：");
        Resource.setBounds(700, 100, 200, 40);
        panel.add(Resource);
//        JTextField ResourceInput=new JTextField();
        ResourceInput.setBounds(700, 140, 200, 40);
        panel.add(ResourceInput);

//        JButton confirm=new JButton("确认");
        confirm.setBounds(700, 250, 200, 40);
        panel.add(confirm);


        availableValue.setText(dispatch.available[0] + ", " + dispatch.available[1] + ", " + dispatch.available[2]);
        availableValue.setBounds(410, 30, 80, 50);
        panel.add(availableValue);

        JLabel whole = new JLabel();
        whole.setText("系统总资源: " + dispatch.wholeResource[0] + ", " + dispatch.wholeResource[1] + ", " + dispatch.wholeResource[2]);
        whole.setBounds(10, 320, 200, 50);
        panel.add(whole);

        for (int i = 0; i < 5; i++) {
            vector[0][i].setText(dispatch.process[i].Max.get(0) + ", " + dispatch.process[i].Max.get(1) + ", " + dispatch.process[i].Max.get(2));
        }

        for (int i = 0; i < 5; i++) {
            vector[1][i].setText(dispatch.process[i].Allocation.get(0) + ", " + dispatch.process[i].Allocation.get(1) + ", " + dispatch.process[i].Allocation.get(2));
        }

        for (int i = 0; i < 5; i++) {
            vector[2][i].setText(dispatch.process[i].Need.get(0) + ", " + dispatch.process[i].Need.get(1) + ", " + dispatch.process[i].Need.get(2));
        }
    }

    public static void actionHandle() {
        confirm.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (e.getSource() == confirm) {
                    String Id = PcbInput.getText();
                    String valueString = ResourceInput.getText();
//                    System.out.println(valueString);
                    int processId = IsExist(Id);
                    if (processId == -1) {
                        JOptionPane.showMessageDialog(null, "进程不存在！");
                        return;
                    }

                    if (JudgeApply(valueString) == false) {
                        JOptionPane.showMessageDialog(null, "输入格式不合法！");
                        return;
                    }
                    if (IsLegal(processId) == false) {
                        JOptionPane.showMessageDialog(null, "合法性检查不通过");
                        return;
                    }
                    if (FacialSatisfy() == false) {
                        JOptionPane.showMessageDialog(null, "表面满足性检查不通过");
                        return;
                    }
                    ArrayList<Integer> SafeReference = SecurityReference(processId);
//                    SafeReference.clear();
                    if (SafeReference.size() < 5) {
                        JOptionPane.showMessageDialog(null, "没有安全序列，不合法");
                    } else {
                        ModifyData();//修改数据
                        String Safe = new String();
                        for (int i = 0; i < SafeReference.size(); i++) {
                            Safe += SafeReference.get(i) + " ";
                        }
                        JOptionPane.showMessageDialog(null, "分配成功，安全序列为" + Safe);
                    }

                }
            }
        });
    }

    //判断进程是否存在
    public static int IsExist(String Id) {
        for (int i = 0; i < dispatch.process.length; i++) {
//            System.out.println(dispatch.process[i].name+" ");
            if (Id.equals(dispatch.process[i].name) == true) {
                return i;
            }
        }
        return -1;
    }

    //判断输入是否合法
    public static boolean JudgeApply(String value) {
        String[] SArray = value.split(",");
//        for(int i=0;i<SArray.length;i++){
//            System.out.println(SArray[i]);
//        }

        if (SArray.length < 3) {
            return false;
        } else {
            for (int i = 0; i < 3; i++) {
                dispatch.apply[i] = Integer.parseInt(SArray[i]);
            }
        }
        return true;
    }

    //合法性检查
    public static boolean IsLegal(int Id) {
        for (int i = 0; i < dispatch.process[Id].Need.size(); i++) {
            if (dispatch.apply[i] > dispatch.process[Id].Need.get(i)) {
                return false;
            }
        }
        return true;
    }

    //表面满足检查
    public static boolean FacialSatisfy() {
        for (int p = 0; p < dispatch.available.length; p++) {
            if (dispatch.apply[p] > dispatch.available[p]) {
                return false;
            }
        }
        return true;
    }

    //尝试查找安全性序列
    public static ArrayList<Integer> SecurityReference(int Id) {
        //修改所有进程为未访问
        for (int p = 0; p < 5; p++) {
            dispatch.process[p].state = false;
        }
        ArrayList<Integer> Reference = new ArrayList<Integer>();
        Reference.clear();
        //先记原来的available，以防找不到安全序列
        int[] originalAvailable = new int[3];
        for (int i = 0; i < 3; i++) {
            originalAvailable[i] = dispatch.available[i];
        }
        ModifyAvailable();
        int[] originalNeed = new int[3];
        int[] originalAllocation = new int[3];
        for (int i = 0; i < 3; i++) {
            originalNeed[i] = dispatch.process[Id].Need.get(i);
            originalAllocation[i] = dispatch.process[Id].Allocation.get(i);
        }
        ModifyNeed(Id);
        int i = 0;
        int count = 0;
        while (AllVisit() == false && count < 10) {
            if (CurrSatisfy(i) == true && dispatch.process[i].state == false) {
                dispatch.process[i].state = true;
                for (int j = 0; j < dispatch.available.length; j++) {
                    dispatch.available[j] += dispatch.process[i].Allocation.get(j);
                }
                Reference.add(i);
            }
            i = (i + 1) % 5;
            count++;
        }
        //没有找到合法序列，将available改回来
        if (Reference.size() < 5) {
            for (int k = 0; k < 3; k++) {
                dispatch.available[k] = originalAvailable[k];
                dispatch.process[Id].Need.set(k, originalNeed[k]);
                dispatch.process[Id].Need.set(k, originalNeed[k]);
            }
            availableValue.setText(dispatch.available[0] + ", " + dispatch.available[1] + ", " + dispatch.available[2]);
        } else {
            for (int k = 0; k < 3; k++) {
                dispatch.available[k] = originalAvailable[k] - dispatch.apply[k];
            }
        }
        return Reference;
    }

    //查看是否所有的进程都被访问过
    public static boolean AllVisit() {
        for (int i = 0; i < dispatch.process.length; i++) {
            if (dispatch.process[i].state == false) {
                return false;
            }
        }
        return true;
    }

    //尝试修改available
    public static void ModifyAvailable() {
        for (int i = 0; i < dispatch.available.length; i++) {
            dispatch.available[i] -= dispatch.apply[i];
        }
        availableValue.setText(dispatch.available[0] + ", " + dispatch.available[1] + ", " + dispatch.available[2]);
    }

    //尝试修改need&allocation
    public static void ModifyNeed(int Id) {
        for (int i = 0; i < dispatch.process[Id].Need.size(); i++) {
            dispatch.process[Id].Need.set(i, dispatch.process[Id].Need.get(i) - dispatch.apply[i]);
            dispatch.process[Id].Allocation.set(i, dispatch.process[Id].Allocation.get(i) + dispatch.apply[i]);
        }
//        dispatch.process[Id].state=true;
    }

    //查看available是否满足当前进程
    public static boolean CurrSatisfy(int Id) {
        System.out.println(Id);
        for (int i = 0; i < dispatch.available.length; i++) {
            if (dispatch.available[i] < dispatch.process[Id].Need.get(i)) {
                return false;
            }
        }
        return true;
    }

    public static void ModifyData() {
        for (int i = 0; i < 5; i++) {
            vector[0][i].setText(dispatch.process[i].Max.get(0) + ", " + dispatch.process[i].Max.get(1) + ", " + dispatch.process[i].Max.get(2));
        }

        for (int i = 0; i < 5; i++) {
            vector[1][i].setText(dispatch.process[i].Allocation.get(0) + ", " + dispatch.process[i].Allocation.get(1) + ", " + dispatch.process[i].Allocation.get(2));
        }

        for (int i = 0; i < 5; i++) {
            vector[2][i].setText(dispatch.process[i].Need.get(0) + ", " + dispatch.process[i].Need.get(1) + ", " + dispatch.process[i].Need.get(2));
        }
        //availableValue.setText(dispatch.available[0]+", "+dispatch.available[1]+", "+dispatch.available[2]);
    }

}