package cn.dujc.dg.maker;

import java.io.File;

import cn.dujc.dg.utils.Save2File;
import cn.dujc.dg.utils.Template;

/**
 * Android 资源生成
 * Created by Du JC on 2016/5/24.
 */
public class DPResourceImpl implements Maker {

    private final static String dirTEMPLATE = "values-sw%ddp";

    final File dir;

    private final int[] widths;
    private final int baseWith;
    private final int[] specifyValues;
    private final String parentDir;

    public boolean test = false;

    /**
     * @param widths        要生成的宽度 sw($widths)dp
     * @param baseWith      设计图的宽度尺寸 如750*1334中的750，这个将是所有计算结果的最大上限
     * @param specifyValues 指定要生成的资源值，不指定则采用默认方案，指定则只生成指定的资源值
     */
    public DPResourceImpl(String parentDir, boolean clear, int[] widths, int baseWith, int... specifyValues) {
        this.parentDir = parentDir;
        this.widths = widths;
        this.baseWith = baseWith;
        this.specifyValues = specifyValues;
        dir = Template.getDir(clear, parentDir);
    }

    public void start() {
        for (int width : widths) {
            final double scale = width * 1.0 / baseWith;
            final String valuesDir = String.format(dirTEMPLATE, width);
            generate(scale, valuesDir);
            //AddResMarker.addMarker(valuesDir, width);
            test(width, valuesDir);
        }
        float scale = calcDefaultScale();
        generate(scale, "values");//默认尺寸，dpi＝（√（横向分辨率^2+纵向分辨率^2））/屏幕尺寸）
        // ，屏幕尺寸以4.5寸为标准时，1280*720为320，为160的2倍，以此推出通常情况下可以宽度/360得出倍数，这项不准确。
        // 此项计算只有2个依据，iPhone 750*1334为2倍，1080*1920为3倍，而750/2和1080/3分别为375和360，且Android和iOS相差不多，或可为依据
        test("default", "values");
        //AddResMarker.addMarker("values", "default");
    }

    /**
     * 2017年03月08日 仍然不敢保证计算正确
     * 计算默认值的比例。
     * 2017年3月31日 几乎确定这个计算比例是对的了，我测试了一下drawable不带后缀的图片，等同于mhdpi的图片。
     * 即代表1倍大小。那么，1倍代表的mdpi是320*480的分辨率，为了保证计算的正确性，因为720是2倍，1080是3倍，1440是4倍。
     * 所以我认定1倍的分辨率应该为360*640。所以，默认值的计算，就是计算设计图与360的倍数。
     *
     * @return values目录下计算比例
     */
    private float calcDefaultScale() {
        return 360f / baseWith;
    }//还是直接取，不要再取接近1/8的值了
    /*private float calcDefaultScale(){
        final float b = 8f;//精度，如此值为8，则区间为8分之1，即取最接近0.125倍数的值
        //先计算设计图宽度和360的比值
        float v = 360f / baseWith;
        //比值*8即将设计图放大8倍
        v *= b;
        //比值放大8倍后进行四舍五入
        v += 0.5f;
        int i = (int) v;
        //如果四舍五入后还小于1，那么就直接除回8，否则减回0.5再除回8
        v = ( i != 0 ? i : v - 0.5f ) / b;
        //这个来来回回乘8除8，只是想把得到的倍数差保持在0.125左右
        System.out.println("------------    " + v);
        return v;
    }*/

    /**
     * 生成
     *
     * @param scale         比例
     * @param valuesDirName values-xxx文件夹名称
     */
    private void generate(double scale, String valuesDirName) {
        final File values = new File(dir, valuesDirName);
        if (!values.exists()) values.mkdir();

        {//dimen
            String distanceContent = Template.autoDistances(baseWith, scale, "dp", specifyValues);

            File file = new File(values, Template.xFile);

            Save2File.save(file, distanceContent);
        }
        {//text size
            String textSizeContent = Template.autoTextSizes(baseWith, scale, "sp", specifyValues);

            File file = new File(values, Template.sFile);

            Save2File.save(file, textSizeContent);
        }
    }

    private void test(Object width, String valuesDirName) {
        if (test) {
            final File values = new File(dir, valuesDirName);
            if (!values.exists()) values.mkdirs();
            if (test) {//test text
                Template template = Template.start(baseWith);
                template.append("<string name=\"test_text\">" + "当前选取资源:" + width + "</string>").end();
                File file = new File(values, "du_test_text.xml");
                Save2File.save(file, template.getFileContent());
            }
        }
    }
}
