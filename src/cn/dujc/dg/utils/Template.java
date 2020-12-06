package cn.dujc.dg.utils;

import java.io.File;
import java.text.DecimalFormat;

/**
 * Created by Du JC on 2016/6/12.
 */
public class Template {

    public final static String XTemplate = "<dimen name=\"dp%d\">%s</dimen>\n";
    public final static String STemplate = "<dimen name=\"sp%d\">%s</dimen>\n";
    public final static String xFile = "dp_size.xml", sFile = "sp_size.xml";
    public final static String xmlHead = "<?xml version=\"1.0\" encoding=\"utf-8\"?>\n";
    private final static String dirStr = "res";
    private static final DecimalFormat format = new DecimalFormat("######0.00");
    private final StringBuilder mFileContent;

    private Template(int baseWidth) {
        mFileContent = new StringBuilder(Template.xmlHead);

        if (baseWidth > 0) {
            mFileContent.append("<!-- current base width is ");
            mFileContent.append(baseWidth);
            mFileContent.append(" -->\n");
        }

        mFileContent.append("<resources>\n");
    }

    public static File getDir(boolean clear, String parent) {
        return getDir(clear, new File(parent));
    }

    public static File getDir(boolean clear, File parent) {
        File file = new File(parent, dirStr);
        if (clear) DeleteFile.delete(file);
        file.mkdirs();
        return file;
    }

    /**
     * 保留2位小数后四舍五入的字符串
     *
     * @param v
     * @return
     */
    public static String keep2NumAfterPointer(double v) {
        int values = (int) (v * 100 + 0.5);
        double number = values / 100.0;
        number = Math.max(number, 0.5);//计算出来的值，最小也不能小于1
        return format.format(number);
    }

    /**
     * 自动生成间距大小值。间距大小为2起始到设计图宽度的四分之一之间的偶数，四分之一到二分之一递增4，二分之一到宽度递增10
     *
     * @param baseWith 设计图参考宽度
     * @param scale    比例
     * @param xUnit    间距单位
     * @return xml内容
     */
    public static String autoDistances(int baseWith, double scale, String xUnit, int... specifyValues) {
        Template template = Template.start(baseWith);

        if (specifyValues == null || specifyValues.length == 0) {
            final int _temp_40 = baseWith / 40;
            final int _temp_20 = baseWith / 20;
            final int quarter = _temp_40 * 10;// 四分之一
            final int twentieth = _temp_20 % 2 == 0 ? _temp_20 : _temp_20 + 1;//二十分之一
            final int halt = _temp_20 * 10;//二分之一
            for (int size = 1; size <= 600; size++) {
                template.append(String.format(Template.XTemplate, size, keep2NumAfterPointer(scale * size) + xUnit));
            }
//            boolean twice = false;
//            for (int size = twentieth; size < quarter; size++) {
//                if ((twice = !twice) || size % 5 == 0)
//                    template.append(String.format(Template.XTemplate, size, keep2NumAfterPointer(scale * size) + xUnit));
//            }
//            for (int size = quarter; size < halt; size += 5) {
//                template.append(String.format(Template.XTemplate, size, keep2NumAfterPointer(scale * size) + xUnit));
//            }
//            for (int size = halt; size < baseWith; size += 10) {
//                template.append(String.format(Template.XTemplate, size, keep2NumAfterPointer(scale * size) + xUnit));
//            }
//            template.append(String.format(Template.XTemplate, baseWith, keep2NumAfterPointer(scale * baseWith) + xUnit));
        } else {
            for (int size : specifyValues) {
                template.append(String.format(Template.XTemplate, size, keep2NumAfterPointer(scale * size) + xUnit));
            }
        }

        template.end();

        return template.getFileContent();
    }

    /**
     * 自动生成字体大小值。字体大小范围为设计图宽度的百分之一到十分之一
     *
     * @param baseWith 设计图参考宽度
     * @param scale    比例
     * @param sUnit    字体单位
     * @return xml内容
     */
    public static String autoTextSizes(int baseWith, double scale, String sUnit, int... specifyValues) {
        Template template = Template.start(baseWith);
        final int max_text_size = baseWith / 10;
        final int _temp_ = baseWith / 100;
        final int min_text_size = _temp_ % 2 == 0 ? _temp_ : _temp_ - 1;

        if (specifyValues == null || specifyValues.length == 0) {
            for (int size = 1; size <= max_text_size; size++) {
                template.append(String.format(Template.STemplate, size, keep2NumAfterPointer(scale * size) + sUnit));
            }
        } else {
            for (int size : specifyValues) {
                template.append(String.format(Template.STemplate, size, keep2NumAfterPointer(scale * size) + sUnit));
            }
        }

        template.end();

        return template.getFileContent();
    }

    /**
     * 开始一个模板
     *
     * @param baseWidth 设计图参考宽度。大于0时则会注释当前设计图大小
     * @return 新建的模板
     */
    public static Template start(int baseWidth) {
        return new Template(baseWidth);
    }

    /**
     * 添加xml内容
     *
     * @param append 要添加的内容
     * @return 现有内容
     */
    public Template append(String append) {
        if (mFileContent == null) {
            throw new NullPointerException("并未开始");
        }
        if (mFileContent.toString().endsWith("</resources>")) {
            throw new IllegalStateException("已经结束");
        }
        mFileContent.append(append);
        return this;
    }

    /**
     * 结束添加xml内容
     */
    public void end() {
        if (mFileContent == null) {
            throw new NullPointerException("并未开始");
        }
        if (mFileContent.toString().endsWith("</resources>")) {
            throw new IllegalStateException("已经结束");
        }
        mFileContent.append("</resources>");
    }

    /**
     * 获取xml内容
     *
     * @return 内容
     */
    public String getFileContent() {
        if (mFileContent == null) {
            throw new NullPointerException("并未开始");
        }
        final String content = mFileContent.toString();
        if (!content.endsWith("</resources>")) {
            throw new IllegalStateException("并未结束");
        }
        return content;
    }
}
