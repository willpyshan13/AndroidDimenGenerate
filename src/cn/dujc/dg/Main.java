package cn.dujc.dg;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.filechooser.FileSystemView;

import cn.dujc.dg.maker.DPResourceImpl;
import cn.dujc.dg.utils.VerticalFlowLayout;
import org.jb2011.lnf.beautyeye.BeautyEyeLNFHelper;

/**
 * Created by Du JC on 2016/9/23.
 */
public class Main {

    public static void main(String[] args) {
        try {
            BeautyEyeLNFHelper.frameBorderStyle = BeautyEyeLNFHelper.FrameBorderStyle.generalNoTranslucencyShadow;
            BeautyEyeLNFHelper.launchBeautyEyeLNF();
            UIManager.put("RootPane.setupButtonVisible", false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        UI ui = new UI("资源生成器");
        ui.setVisible(true);
    }

    private static void generate(String dir, boolean clear, int width, int min, int max, int interval) {
        final int size = (int) ((max - min) * 1f / interval + 1.5f);//四舍五入并向上再取一份
        final int[] widths = new int[size];
        for (int index = 0; index < size; index++) {
            widths[index] = min + index * interval;
        }
        DPResourceImpl resource = new DPResourceImpl(dir, clear, widths, width);
        resource.start();
    }

    private static class UI extends JFrame {

        private static final String DEFAULT_WIDTH = "1080", DEFAULT_MIN = "160", DEFAULT_MAX = "1280", DEFAULT_INTERVAL = "40";

        private JTextField mWidthTextField, mMinTextField, mMaxTextField, mIntervalTextField, mPathTextField;
        private JCheckBox mDeleteOldCheckBox;
        private JButton mChooserButton, mGenerateButton;
        private JFileChooser mFileChooser = new JFileChooser();

        private UI(String title) throws HeadlessException {
            super(title);
            getContentPane().setLayout(new VerticalFlowLayout(true, false));
            setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
            //setSize(300, 300);
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setLocation((screenSize.width - 300) / 2, (screenSize.height - 300) / 2);
            setResizable(false);
            mFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

            JLabel widthLabel, minLabel, maxLabel, intervalLabel, pathLabel;
            widthLabel = new JLabel("设计图宽度(?px)");
            minLabel = new JLabel("最小生成宽度(sw-?dp)");
            maxLabel = new JLabel("最大生成宽度(sw-?dp)");
            intervalLabel = new JLabel("生成宽度间隔(?dp)");
            pathLabel = new JLabel("资源保存路径");

            mWidthTextField = new JTextField(DEFAULT_WIDTH);
            mMinTextField = new JTextField(DEFAULT_MIN);
            mMaxTextField = new JTextField(DEFAULT_MAX);
            mIntervalTextField = new JTextField(DEFAULT_INTERVAL);

            mDeleteOldCheckBox = new JCheckBox("删除旧文件");
            mPathTextField = new JTextField(FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath());

            mChooserButton = new JButton("选择");
            mGenerateButton = new JButton("生成");

            add(widthLabel);
            add(mWidthTextField);

            add(minLabel);
            add(mMinTextField);

            add(maxLabel);
            add(mMaxTextField);

            add(intervalLabel);
            add(mIntervalTextField);

            add(pathLabel);
            JPanel pathPanel = new JPanel(new BorderLayout());
            pathPanel.add(mDeleteOldCheckBox, BorderLayout.WEST);
            pathPanel.add(mPathTextField, BorderLayout.CENTER);
            pathPanel.add(mChooserButton, BorderLayout.EAST);
            add(pathPanel);

            add(mGenerateButton);

            mChooserButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    mFileChooser.showOpenDialog(null);
                    mPathTextField.setText(mFileChooser.getSelectedFile().getAbsolutePath());
                }
            });

            mGenerateButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    String a0 = mWidthTextField.getText(), a1 = mMinTextField.getText(), a2 = mMaxTextField.getText(), a3 = mIntervalTextField.getText();
                    generate(mPathTextField.getText(), mDeleteOldCheckBox.isSelected()
                            , getNumberDefault(a0, DEFAULT_WIDTH)
                            , getNumberDefault(a1, DEFAULT_MIN)
                            , getNumberDefault(a2, DEFAULT_MAX)
                            , getNumberDefault(a3, DEFAULT_INTERVAL));
                }
            });

            pack();
        }

        private static int getNumberDefault(String str, String defValue) {
            if (str == null || "".equals(str)) str = defValue;
            int result = 0;
            try {
                result = Integer.valueOf(str);
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
            if (result == 0) {
                try {
                    result = Integer.valueOf(defValue);
                } catch (NumberFormatException e) {
                    e.printStackTrace();
                }
            }
            return result;
        }
    }
}
