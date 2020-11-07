package view;


import javax.swing.*;
import java.awt.*;


/**
 * @author BlackSand
 */
public class FrameMain extends JFrame {

    /**
     * swing定义
     */
    private JPanel jp1, jp2, jp3, jp4, jp5;
    private JTextField jtf1;
    private JTable jta, jta2;
    private JScrollPane jsp1, jsp2;
    private JMenuBar jmb;
    private JMenu jm;
    private JMenu cMenu;
    private JMenuItem jmi1, jmi2, jmi3;
    private JLabel jl1, jl2, jl3;
    private JButton jb1;
    //private TableModel tm;

    private int n;
    private boolean isFile;

    public FrameMain() {
        //初始化使用界面
        initFrameMain();


        //设置菜单栏和下拉菜单项目
        jm.add(jmi1);
        jm.add(jmi2);
        cMenu.add(jmi3);
        jmb.add(jm);
        jmb.add(cMenu);

        //设置当前的路径显示
        jtf1.setPreferredSize(new Dimension(400, 25));
        jtf1.setText("D:");
        jp2.add(jl2);
        jp2.add(jtf1);
        jp2.add(jp1);


        //窗口信息设置 面板放置
        this.setTitle("模拟磁盘文件程序");
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.setLayout(new BorderLayout());
        this.setLocation(500, 200);
        this.setSize(1000, 700);
        this.setVisible(true);
        //设置部件的位置
        this.add(jp1, BorderLayout.CENTER);
        this.add(jmb, BorderLayout.NORTH);
    }


    /**
     * 初始化界面项目
     **/
    public void initFrameMain() {
        jp1 = new JPanel();
        jp2 = new JPanel();
        jp3 = new JPanel();
        jp4 = new JPanel();
        jp5 = new JPanel();

        jtf1 = new JTextField();
        jtf1.setEditable(false);
        jl1 = new JLabel("磁盘分析");
        jl3 = new JLabel("已打开文件");
        jl2 = new JLabel("路径");
        jmb = new JMenuBar();
        jm = new JMenu("操作");
        cMenu = new JMenu("查询");

        jmi1 = new JMenuItem("新建文件");
        jmi2 = new JMenuItem("新建文件夹");
        jmi3 = new JMenuItem("查询");
        jb1 = new JButton("显示磁盘使用情况");
    }

}
