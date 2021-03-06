package com.ycm.kata.datacollection.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.independentsoft.office.ExtendedBoolean;
import com.independentsoft.office.Unit;
import com.independentsoft.office.UnitType;
import com.independentsoft.office.drawing.Extents;
import com.independentsoft.office.drawing.Offset;
import com.independentsoft.office.drawing.Picture;
import com.independentsoft.office.drawing.PresetGeometry;
import com.independentsoft.office.drawing.ShapeType;
import com.independentsoft.office.drawing.Transform2D;
import com.independentsoft.office.word.DrawingObject;
import com.independentsoft.office.word.HeightRule;
import com.independentsoft.office.word.HorizontalAlignmentType;
import com.independentsoft.office.word.Paragraph;
import com.independentsoft.office.word.Run;
import com.independentsoft.office.word.Shading;
import com.independentsoft.office.word.ShadingPattern;
import com.independentsoft.office.word.StandardBorderStyle;
import com.independentsoft.office.word.VerticalAlignmentType;
import com.independentsoft.office.word.VerticalTextAlignment;
import com.independentsoft.office.word.WordDocument;
import com.independentsoft.office.word.drawing.DrawingObjectSize;
import com.independentsoft.office.word.drawing.Inline;
import com.independentsoft.office.word.tables.Cell;
import com.independentsoft.office.word.tables.HorizontallyMergedCell;
import com.independentsoft.office.word.tables.Row;
import com.independentsoft.office.word.tables.RowHeight;
import com.independentsoft.office.word.tables.Table;
import com.independentsoft.office.word.tables.TableWidthUnit;
import com.independentsoft.office.word.tables.Width;
import com.ycm.kata.datacollection.MyApplication;
import com.ycm.kata.datacollection.R;
import com.ycm.kata.datacollection.model.ProjectEntityDao;
import com.ycm.kata.datacollection.model.entity.ProjectEntity;
import com.ycm.kata.datacollection.utils.ActivityStack;
import com.ycm.kata.datacollection.utils.CommonUtils;
import com.ycm.kata.datacollection.utils.CompressUtil;
import com.ycm.kata.datacollection.utils.ZipUtil;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFPrintSetup;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.RegionUtil;
import org.apache.poi.ss.util.WorkbookUtil;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by changmuyu on 2017/9/19.
 * Description:
 */

public class DataListActivity extends BaseActivity implements GetDataListener, OnItemClickListener, View.OnClickListener, ExportListener {
    private ImageView btnAdd;
    private ImageView btnExport;
    private ImageView btnPrevious;
    private ImageView btnNext;
    private TextView tvCurrentPage;
    private TextView totalSize;
    private ListView listView;
    private MyAdapter myAdapter;
    private static List<ProjectEntity> dataSource;
    private ProjectEntityDao projectEntityDao;
    private ProjectEntity projectEntity;
    private GetData getData;
    private int index = 0;
    private static final int VIEW_COUNT = 5;
    private UpdateViewHandler handler;
    private static final int MY_PERMISSIONS_REQUEST = 1;
    private String imageFile;
    private String decFile;
    private TextView tvEmptyView;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);
        ActivityStack.getInstanse().pushActivity(this);
        preViewHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                String str = (index + 1) + "/" + dataSize;
                tvCurrentPage.setText(str);
            }
        };
        btnAdd = findViewById(R.id.add_new_btn);
        btnAdd.setOnClickListener(this);
        tvCurrentPage = findViewById(R.id.currentPage);
        totalSize = findViewById(R.id.totalSize);
        btnExport = findViewById(R.id.export_btn);
        btnExport.setOnClickListener(this);
        btnPrevious = findViewById(R.id.previous_btn);
        btnPrevious.setOnClickListener(this);
        btnNext = findViewById(R.id.next_btn);
        btnNext.setOnClickListener(this);
        tvEmptyView = findViewById(R.id.search_no_result);
        listView = findViewById(R.id.list);
        dataSource = new ArrayList<>();
        handler = new UpdateViewHandler(this);
        projectEntityDao = MyApplication.getInstances().getDaoSession().getProjectEntityDao();
        myAdapter = new MyAdapter(this, this);
        listView.setAdapter(myAdapter);
        View empty = LayoutInflater.from(this).inflate(R.layout.empy_layout, null);
        listView.setEmptyView(tvEmptyView);
    }

    @Override
    public void loadSuccess(List<ProjectEntity> dataSource) {
        handler.sendEmptyMessage(123);
    }

    @Override
    public int progress(int x) {
        return 0;
    }

    private static int dataSize;

    private static class UpdateViewHandler extends Handler {
        private WeakReference<DataListActivity> weakReference;

        UpdateViewHandler(DataListActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            if (msg.what == 123) {
                weakReference.get().myAdapter.setDataSource(dataSource);
                weakReference.get().myAdapter.notifyDataSetChanged();
                dataSize = dataSource.size();
                if (dataSize % 5 == 0) {
                    dataSize = dataSize / 5;
                } else {
                    dataSize = dataSize / 5 + 1;
                }
                String pageStr = (weakReference.get().index + 1) + "/" + dataSize;
                if (dataSize == 0) {
                    pageStr = 0 + "/" + dataSize;
                }

                weakReference.get().tvCurrentPage.setText(pageStr);
//                weakReference.get().totalSize.setText(dataSize);
            }
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        getDataSource();
    }

    ExportData exportData;

    private void getDataSource() {
        getData = new GetData(projectEntityDao, this);
        getData.start();
    }

    @Override
    public void textOnclickListener(int position) {
        Intent intent = new Intent();
        intent.setClass(this, EditActivity.class);
        intent.putExtra("projectEntity", dataSource.get(position));
        intent.putExtra("tag", "edit");
        startActivity(intent);
    }

    @Override
    public void buttonOnclickListener(final int position) {
        showConfirmCancelDialog("确认删除？", "确定", "取消", "删除之后无法恢复", new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteById(position);
            }

        }, null);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.add_new_btn:
                Intent intent = new Intent();
                intent.setClass(getBaseContext(), MainActivity.class);
                startActivity(intent);
                break;
            case R.id.export_btn:
                if (ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        != PackageManager.PERMISSION_GRANTED) {
                    ActivityCompat.requestPermissions(this,
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE},
                            MY_PERMISSIONS_REQUEST);
                } else {
//                    String ss = Environment.getExternalStorageDirectory().getPath() + File.separator + "data_collection" + File.separator + "file";
//                    /*try {
////                        writeImage(ss, dataSource.get(0));
//
//                    }*/ /*catch (IOException | InvalidFormatException e) {
//                        e.printStackTrace();
//                    }*/
////                    printer();
                    showDialg("正在导出数据");
                    exportData = new ExportData(dataSource, this);
                    dataSuccessHandler = new ExportDataSuccessHandler(this);
                    exportData.start();
                }
                break;
            case R.id.previous_btn:
                preView();
                break;
            case R.id.next_btn:
                nextView();
                break;
        }
    }

    static ProgressDialog pDialog;

    private void showDialg(String message) {
        if (null == pDialog) {
            pDialog = new ProgressDialog(this);
        }
        pDialog.setMessage(message);//设置对话框中的提示信息
        // pDialog.setIndeterminate(indeterminate);//设置对话框里的进度条不显示进度值
//pDialog.setProgress(value);//设置对话框进度条的值
//pDialog.setProgressStyle(style);//设置对话框里进度条的进度值
        pDialog.setCanceledOnTouchOutside(false);
        pDialog.show();
    }

    private static class GetData extends Thread {
        private WeakReference<ProjectEntityDao> weakReference;
        private WeakReference<GetDataListener> listenerWeakReference;

        GetData(ProjectEntityDao projectEntityDao, GetDataListener listener) {
            weakReference = new WeakReference<>(projectEntityDao);
            listenerWeakReference = new WeakReference<>(listener);
        }

        @Override
        public void run() {
            super.run();
            dataSource = weakReference.get().loadAll();
            if (dataSource != null) {
                listenerWeakReference.get().loadSuccess(dataSource);
            }
        }
    }

    private void deleteById(int position) {
        if (dataSource == null || dataSource.isEmpty()) {
            return;
        }

        ProjectEntity pro = dataSource.get(position);
        projectEntityDao.delete(pro);
        String imagePath = pro.getImagePath();
        File image = new File(imagePath);
        if (image.exists()) {
            image.delete();
        }
        getDataSource();
    }

    private Handler preViewHandler;

    // 点击左边的Button，表示向前翻页，索引值要减1.
    public void preView() {
        if (index <= 0) {
            Toast.makeText(getBaseContext(), "已经是第一页啦", Toast.LENGTH_SHORT).show();
            return;
        }
        myAdapter.setIndex(--index);
        // 刷新ListView里面的数值。
        myAdapter.notifyDataSetChanged();
        // 检查Button是否可用。
//        checkButton();
        preViewHandler.sendEmptyMessage(index);
    }

    // 点击右边的Button，表示向后翻页，索引值要加1.
    public void nextView() {
        if (dataSource == null || dataSource.size() == 0) {
            Toast.makeText(getBaseContext(), "没有数据", Toast.LENGTH_SHORT).show();
            return;
        }

        int pageSize;
        if ((dataSource.size() % VIEW_COUNT) == 0) {
            pageSize = dataSource.size() / VIEW_COUNT - 1;
        } else {
            pageSize = (dataSource.size() / VIEW_COUNT);
        }

        if (index >= pageSize) {
            Toast.makeText(getBaseContext(), "已经是最后一页啦", Toast.LENGTH_SHORT).show();
            return;
        }
        myAdapter.setIndex(++index);
        // 刷新ListView里面的数值。
        myAdapter.notifyDataSetChanged();
        preViewHandler.sendEmptyMessage(index);
        // 检查Button是否可用。
//        checkButton();
    }

//    public void checkButton() {
//        // 索引值小于等于0，表示不能向前翻页了，以经到了第一页了。
//        // 将向前翻页的按钮设为不可用。
//        if (index <= 0) {
//            btnNext.setEnabled(false);
//        } else {
//            btnPrevious.setEnabled(true);
//        }
//        // 值的长度减去前几页的长度，剩下的就是这一页的长度，如果这一页的长度比View_Count小，表示这是最后的一页了，后面在没有了。
//        // 将向后翻页的按钮设为不可用。
//        if (dataSource.size() - index * VIEW_COUNT <= VIEW_COUNT) {
//            btnNext.setEnabled(false);
//        }
//        // 否则将2个按钮都设为可用的。
//        else {
//            btnNext.setEnabled(true);
//        }
//
//    }


    /**
     * 为了保证模板的可用，最好在现有的模板上复制后修改
     */
//    private void printer() {
//        try {
//            saveFile("template.docx", this, R.raw.template);//文件目录res/raw
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
//        //现场检查记录
//        String aafileurl = Environment.getExternalStorageDirectory() + "/data_collection/file/template.docx";
//        final String bbfileurl = Environment.getExternalStorageDirectory() + "/data_collection/file/template_printer.docx";
//        //获取模板文件
//        File demoFile = new File(aafileurl);
//        //创建生成的文件
//        File newFile = new File(bbfileurl);
//        if (newFile.exists()) {
//            newFile.delete();
//        }
//        for (int i = 0; i < dataSource.size(); i++) {
//            Map<String, String> map = new HashMap<>();
//            map.put("$project_name$", dataSource.get(i).getProjectName());
//            map.put("$check_date$", CommonUtils.formatDate(dataSource.get(i).getCheckDate()));
//            map.put("$unit_engineering$", dataSource.get(i).getUnitEngineering());
//            map.put("$block_pile$", dataSource.get(i).getBlock() + " " + dataSource.get(i).getPilNo());
//            map.put("$defects$", dataSource.get(i).getDefects());
//            map.put("$remark$", dataSource.get(i).getRemark());
//            map.put("$image$", dataSource.get(i).getImagePath());
////            writeDoc(demoFile, newFile, map);
//        }
//
//    }

    /**
     * demoFile 模板文件
     * newFile 生成文件
     * map 要填充的数据
     */
//    public boolean writeDoc(File demoFile, File newFile, Map<String, String> map) {
//        try {
//            String imagePath = "";
//            FileInputStream in = new FileInputStream(demoFile);
//            HWPFDocument hdt = new HWPFDocument(in);
//            // 读取word文本内容
//            Range range = hdt.getRange();
//            // 替换文本内容
//            for (Map.Entry<String, String> entry : map.entrySet()) {
//                range.replaceText(entry.getKey(), entry.getValue());
//            }
//
//            ByteArrayOutputStream ostream = new ByteArrayOutputStream();
//            FileOutputStream out = new FileOutputStream(newFile, true);
//            hdt.write(ostream);
//            // 输出字节流
//            out.write(ostream.toByteArray());
//            out.close();
//            ostream.close();
//            Toast.makeText(this, "保存成功", Toast.LENGTH_SHORT).show();
//            return true;
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return false;
//    }

//    private int getImageType(String imgFile) {
//        byte format;
//        if (imgFile.endsWith(".emf")) {
//            format = 2;
//        } else if (imgFile.endsWith(".wmf")) {
//            format = 3;
//        } else if (imgFile.endsWith(".pict")) {
//            format = 4;
//        } else if (!imgFile.endsWith(".jpeg") && !imgFile.endsWith(".jpg")) {
//            if (imgFile.endsWith(".png")) {
//                format = 6;
//            } else if (imgFile.endsWith(".dib")) {
//                format = 7;
//            } else if (imgFile.endsWith(".gif")) {
//                format = 8;
//            } else if (imgFile.endsWith(".tiff")) {
//                format = 9;
//            } else if (imgFile.endsWith(".eps")) {
//                format = 10;
//            } else if (imgFile.endsWith(".bmp")) {
//                format = 11;
//            } else {
//                format = 12;
//            }
//        } else {
//            format = 5;
//        }
//        return format;
//    }

//    private void writeImage(String decFile, ProjectEntity pje) throws IOException, InvalidFormatException {
//        File rf = new File(decFile);
//        if (!rf.exists()) {
//            rf.mkdir();
//        }
//
//        String name = System.currentTimeMillis() + ".doc";
//        File f = new File(decFile, name);
//        if (!f.exists()) {
//            f.createNewFile();
//        }
//        FileInputStream fileInputStream1 = new FileInputStream(f);
////        HWPFDocument document = new HWPFDocument(fileInputStream1);
//
//        XWPFDocument doc = new XWPFDocument();
//        XWPFParagraph p = doc.createParagraph();
//        XWPFRun r = p.createRun();
//        XWPFTable xwpfTable = r.getDocument().createTable(4, 4);
//        xwpfTable.getRow(1).getCell(0).setText("项目名称");
//        xwpfTable.getRow(1).getCell(1).setText(pje.getProjectName());
//        xwpfTable.getRow(1).getCell(2).setText("检测日期");
//        xwpfTable.getRow(1).getCell(3).setText(formatDate(pje.getCheckDate()));
//
//
//        xwpfTable.getRow(2).getCell(0).setText("单位工程");
//        xwpfTable.getRow(2).getCell(1).setText(pje.getUnitEngineering());
//        xwpfTable.getRow(2).getCell(2).setText("标段及桩号");
//        xwpfTable.getRow(2).getCell(3).setText(pje.getBlock() + " " + pje.getPilNo());
//
//        File imageFilePath = new File(pje.getImagePath());
//        FileInputStream fileInputStream = new FileInputStream(imageFilePath);
//        xwpfTable.getRow(3).getCell(0).addParagraph().createRun().addPicture(fileInputStream, getImageType(pje.getImagePath()), pje.getImagePath(), Units.toEMU(200.0D), Units.toEMU(200.0D));
////        xwpfTable.getRow(3).getCell(1).;
//        xwpfTable.getRow(3).getCell(2).setText("缺陷描述");
//        xwpfTable.getRow(3).getCell(3).setText(pje.getDefects());
//
//        xwpfTable.getRow(4).getCell(0).setText("备注");
//        xwpfTable.getRow(4).getCell(1).setText(pje.getRemark());
//        r.addBreak();
//        FileOutputStream out = new FileOutputStream(decFile + File.separator + name);
//        doc.write(out);
//        out.close();
////        doc.close();
////        r.setText(imgFile);
////        r.addBreak();
////        FileOutputStream out = null;
////        try {
////            r.addPicture(new FileInputStream(imgFile), getImageType(imgFile), imgFile, Units.toEMU(200.0D), Units.toEMU(200.0D));
////            out = new FileOutputStream(decFile);
////        } catch (InvalidFormatException | IOException e) {
////            e.printStackTrace();
////        } finally {
////            r.addBreak(BreakType.PAGE);
////            try {
////                if (out != null) {
////                    out.close();
////                }
////                doc.write(out);
////                doc.close();
////            } catch (IOException e) {
////                e.printStackTrace();
////            }
////
////        }
//
//
//    }

//    private String formatDate(long time) {
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
//        Date d1 = new Date(time);
//        return format.format(d1);
//    }

    /**
     * 将文件复制到SD卡，并返回该文件对应的数据库对象
     *
     * @return
     * @throws IOException
     */
//    public void saveFile(String fileName, Context context, int rawid) throws IOException {
//
//        // 首先判断该目录下的文件夹是否存在
//        File dir = new File(Environment.getExternalStorageDirectory() + "/data_collection/file/");
//        if (!dir.exists()) {
//            // 文件夹不存在 ， 则创建文件夹
//            dir.mkdirs();
//        }
//
//        // 判断目标文件是否存在
//        File file1 = new File(dir, fileName);
//
//        if (!file1.exists()) {
//            file1.createNewFile(); // 创建文件
//
//        }
//        InputStream input = context.getResources().openRawResource(rawid); // 获取资源文件raw
//        try {
//
//            FileOutputStream out = new FileOutputStream(file1); // 文件输出流、用于将文件写到SD卡中
//            byte[] buffer = new byte[1024];
//            int len = 0;
//            while ((len = (input.read(buffer))) != -1) { // 读取文件，-- 进到内存
//
//                out.write(buffer, 0, len); // 写入数据 ，-- 从内存出
//            }
//            input.close();
//            out.close(); // 关闭流
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//    }
    public static void dismiss() {
        if (pDialog != null) {
            pDialog.dismiss();
        }
    }

    ExportDataSuccessHandler dataSuccessHandler;

    private static class ExportDataSuccessHandler extends Handler {
        private WeakReference<DataListActivity> weakReference;

        ExportDataSuccessHandler(DataListActivity activity) {
            weakReference = new WeakReference<>(activity);
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            dismiss();
            Toast.makeText(weakReference.get(), "成功", Toast.LENGTH_SHORT).show();
        }
    }

    private class ExportData extends Thread {
        private WeakReference<ExportListener> weakReference;
        private List<ProjectEntity> dataSource;

        ExportData(List<ProjectEntity> dataSource, ExportListener exportListener) {
            this.dataSource = dataSource;
            weakReference = new WeakReference<>(exportListener);

        }

        @Override
        public void run() {
            super.run();

            writeExcel(dataSource, weakReference.get());
            dataSuccessHandler.sendEmptyMessage(123);
        }
    }

    private List<ProjectEntity> filterDataSource(List<ProjectEntity> projectEntityList) {
        String currentDate = CommonUtils.formatDate(System.currentTimeMillis());
        List<ProjectEntity> currentP = new ArrayList<>();
        for (ProjectEntity p : projectEntityList) {
            if (TextUtils.equals(CommonUtils.formatDate(p.getCheckDate()), currentDate)) {
                currentP.add(p);
            }
        }

        return currentP;

    }

    private void writeExcel4(List<ProjectEntity> dataSource, ExportListener exportListener) {

        try {
            WordDocument doc = new WordDocument();
            Run run11 = new Run("项目名称");

            run11.setBold(ExtendedBoolean.TRUE);
            Paragraph paragraph11 = new Paragraph();
            paragraph11.add(run11);
            paragraph11.setHorizontalTextAlignment(HorizontalAlignmentType.CENTER);
            Cell cell11 = new Cell();
            cell11.setWidth(new Width(TableWidthUnit.POINT, 1170));
            cell11.setVerticalAlignment(VerticalAlignmentType.CENTER);
            cell11.add(paragraph11);

            Run run12 = new Run("Item #");
            Paragraph paragraph12 = new Paragraph();
            paragraph12.add(run12);
            paragraph12.setHorizontalTextAlignment(HorizontalAlignmentType.CENTER);
            Cell cell12 = new Cell();
            cell12.setWidth(new Width(TableWidthUnit.POINT, 2730));
            cell12.setVerticalAlignment(VerticalAlignmentType.CENTER);
            cell12.add(paragraph12);


            Run run13 = new Run("检测日期");
            run13.setBold(ExtendedBoolean.TRUE);
            Paragraph paragraph13 = new Paragraph();
            paragraph13.add(run13);
            paragraph13.setHorizontalTextAlignment(HorizontalAlignmentType.CENTER);
            Cell cell13 = new Cell();
            cell13.setWidth(new Width(TableWidthUnit.POINT, 1560));
            cell13.setVerticalAlignment(VerticalAlignmentType.CENTER);
            cell13.add(paragraph13);


            Run run14 = new Run("");
            Paragraph paragraph14 = new Paragraph();
            paragraph14.add(run14);
            paragraph14.setHorizontalTextAlignment(HorizontalAlignmentType.CENTER);
            Cell cell14 = new Cell();
            cell14.setWidth(new Width(TableWidthUnit.POINT, 2340));
            cell14.setVerticalAlignment(VerticalAlignmentType.CENTER);
            cell14.add(paragraph14);

            Row row1 = new Row();
            row1.setHeight(new RowHeight(HeightRule.EXACT, 2300));
            row1.add(cell11);
            row1.add(cell12);
            row1.add(cell13);
            row1.add(cell14);


            Run run21 = new Run("单位工程");
            run21.setBold(ExtendedBoolean.TRUE);
            Paragraph paragraph21 = new Paragraph();
            paragraph21.add(run21);
            Cell cell21 = new Cell();
            cell21.setWidth(new Width(TableWidthUnit.POINT, 1170));
            cell21.add(paragraph21);


            Run run22 = new Run("Item #");
            Paragraph paragraph22 = new Paragraph();
            paragraph22.add(run22);

            Cell cell22 = new Cell();
            cell22.setWidth(new Width(TableWidthUnit.POINT, 1170));
            cell22.add(paragraph22);


            Run run23 = new Run("标段及桩号");
            run23.setBold(ExtendedBoolean.TRUE);
            Paragraph paragraph23 = new Paragraph();
            paragraph23.add(run23);

            Cell cell23 = new Cell();
            cell23.setWidth(new Width(TableWidthUnit.POINT, 1560));
            cell23.add(paragraph23);


            Run run24 = new Run("Unit Price");
            Paragraph paragraph24 = new Paragraph();
            paragraph14.add(run24);

            Cell cell24 = new Cell();
            cell24.setWidth(new Width(TableWidthUnit.POINT, 1560));
            cell24.add(paragraph24);


            Run run25 = new Run("缺陷描述");
            run25.setBold(ExtendedBoolean.TRUE);
            Paragraph paragraph25 = new Paragraph();
            paragraph25.add(run25);
            paragraph25.setHorizontalTextAlignment(HorizontalAlignmentType.CENTER);
            paragraph25.setVerticalTextAlignment(VerticalTextAlignment.CENTER);
            Cell cell25 = new Cell();
            cell25.setWidth(new Width(TableWidthUnit.POINT, 2340));
            cell25.add(paragraph25);

            Row row2 = new Row();
            row2.setHeight(new RowHeight(HeightRule.EXACT, 2300));
            row2.add(cell21);
            row2.add(cell22);
            row2.add(cell23);
            row2.add(cell24);
            row2.add(cell25);

            String path = Environment.getExternalStorageDirectory().getPath() + File.separator + Environment.DIRECTORY_DCIM + File.separator + "Camera" + File.separator + "IMG_20171215_173507.jpg";
            Picture picture = new Picture(path);
            Unit pictureWidth = new Unit(720, UnitType.POINT);
            Unit pictureHeight = new Unit(480, UnitType.POINT);

            Offset offset = new Offset(10, 10);
            Extents extents = new Extents(pictureWidth, pictureHeight);

            picture.getShapeProperties().setPresetGeometry(new PresetGeometry(ShapeType.RECTANGLE));
            picture.getShapeProperties().setTransform2D(new Transform2D(offset, extents));
            picture.setID("1");
            picture.setName("IMG_20171215_173507.jpg");

            Inline inline = new Inline(picture);
            inline.setSize(new DrawingObjectSize(pictureWidth, pictureHeight));
            inline.setID("1");
            inline.setName("Picture 1");
            inline.setDescription("IMG_20171215_173507.jpg");

            DrawingObject drawingObject = new DrawingObject(inline);

            Run run31 = new Run();
//            run31.add(drawingObject);
            Paragraph paragraph31 = new Paragraph();
            paragraph31.setVerticalTextAlignment(VerticalTextAlignment.CENTER);
            paragraph31.add(run31);
            Cell cell31 = new Cell();
            cell31.setWidth(new Width(TableWidthUnit.POINT, 5460));
            cell31.add(paragraph31);

            Run run32 = new Run("Item #");
            Paragraph paragraph32 = new Paragraph();
            paragraph12.add(run32);

            Cell cell32 = new Cell();
            cell32.setWidth(new Width(TableWidthUnit.POINT, 2340));
            cell32.add(paragraph32);

            Row row3 = new Row();
            row3.setHeight(new RowHeight(HeightRule.EXACT, 6300));
            row3.add(cell31);
            row3.add(cell32);

            Run run41 = new Run("项目名称");
            run41.setBold(ExtendedBoolean.TRUE);
            Paragraph paragraph41 = new Paragraph();
            paragraph41.add(run41);
            Cell cell41 = new Cell();
            cell41.setWidth(new Width(TableWidthUnit.POINT, 1170));
            cell41.add(paragraph41);


            Run run42 = new Run("Item #");
            Paragraph paragraph42 = new Paragraph();
            paragraph42.add(run42);

            Cell cell42 = new Cell();
            cell42.setWidth(new Width(TableWidthUnit.POINT, 6630));
            cell42.add(paragraph42);

            Row row4 = new Row();
            row4.setHeight(new RowHeight(HeightRule.EXACT, 1300));
            row4.add(cell41);
            row4.add(cell42);
            row4.setAlignment(HorizontalAlignmentType.CENTER);
            Table table1 = new Table(StandardBorderStyle.SINGLE_LINE);

            table1.add(row1);
            table1.add(row2);
            table1.add(row3);
            table1.add(row4);

            Run run2 = new Run();
            Paragraph paragraph2 = new Paragraph();
            paragraph2.add(run2);
            paragraph2.setPageBreakBefore(ExtendedBoolean.TRUE);

            Run run3 = new Run();
            run3.addText("我应该在第2页");
            Paragraph paragraph3 = new Paragraph();
            paragraph3.add(run3);

            HorizontallyMergedCell horizontallyMergedCell = new HorizontallyMergedCell();

            cell11.setHorizontallyMergedCell(horizontallyMergedCell);
            doc.getBody().add(table1);
            doc.getBody().add(paragraph2);
            doc.getBody().add(paragraph3);


            String path1 = Environment.getExternalStorageDirectory().getPath();
            doc.save(path1 + File.separator + "123.docx", true);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    private void writeExcel2(List<ProjectEntity> dataSource, ExportListener exportListener) {
        List<ProjectEntity> currentPs = new ArrayList<>();
        currentPs.addAll(dataSource/*filterDataSource(dataSource)*/);
        FileOutputStream fileOut = null;
        FileInputStream fileInput = null;
        String p = CommonUtils.getDataFilePath(System.currentTimeMillis());
        if (p == null) {
            return;
        }
        int lastRow = 0;
        for (int i = 0; i < currentPs.size(); i++) {
            if (lastRow == 0) {
//                    lastRow += 1;
            } else {
                lastRow += 6;
            }
            HSSFWorkbook wb = null;
            try {
                fileInput = new FileInputStream(p);
                fileOut = new FileOutputStream(p);
                wb = new HSSFWorkbook(fileInput);

                HSSFSheet sheet1 = wb.createSheet("项目数据" + CommonUtils.formatDate(System.currentTimeMillis()));
                HSSFCellStyle hssfCellStyle = wb.createCellStyle();
                HSSFFont titleFont = wb.createFont();
                titleFont.setFontName("宋体");
                titleFont.setBold(true);
                titleFont.setFontHeightInPoints((short) 18);
                HSSFFont contentFont = wb.createFont();
                contentFont.setFontName("宋体");
                contentFont.setBold(false);
                contentFont.setFontHeightInPoints((short) 16);
                HSSFPrintSetup printSetup = sheet1.getPrintSetup();
                printSetup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
                sheet1.setMargin(HSSFSheet.RightMargin, -0.5);

                //创建第一行，为标题行，可用可不用
                HSSFRow titleRow = sheet1.createRow(lastRow);
                //第1列
                HSSFCell cellTitle = titleRow.createCell(0);
                CellAddress cellTitleAddress = cellTitle.getAddress();
                CellRangeAddress newCellTitleAd = new CellRangeAddress(cellTitleAddress.getRow(), cellTitleAddress.getRow() + 1 + 1, cellTitleAddress.getColumn(), cellTitleAddress.getColumn() + 9);
                sheet1.addMergedRegion(newCellTitleAd);
//                cellTitle.setCellValue("项目名称");

                hssfCellStyle.setFont(titleFont);
                cellTitle.setCellStyle(hssfCellStyle);
                HSSFCellStyle style = cellTitle.getCellStyle();
                style.setAlignment(HorizontalAlignment.CENTER_SELECTION);//居中
                style.setVerticalAlignment(VerticalAlignment.CENTER);

                RegionUtil.setBorderBottom(2, newCellTitleAd, sheet1);

                //创建第二行
                HSSFRow rowName = sheet1.createRow(newCellTitleAd.getLastRow() + 1);
                //第1列
                HSSFCell cellNameTitle = rowName.createCell(0);
                CellAddress cellNameTitleAddress = cellNameTitle.getAddress();
                CellRangeAddress newCellTitleNameAd = new CellRangeAddress(cellNameTitleAddress.getRow(),
                        cellNameTitleAddress.getRow() + 1 + 1 + 1, cellNameTitleAddress.getColumn(), cellNameTitleAddress.getColumn() + 1);
                sheet1.addMergedRegion(newCellTitleNameAd);
                cellNameTitle.setCellValue("项目名称");
                hssfCellStyle.setFont(titleFont);
                hssfCellStyle.setAlignment(HorizontalAlignment.CENTER);//居中
                cellNameTitle.setCellStyle(hssfCellStyle);
                RegionUtil.setBorderBottom(2, newCellTitleNameAd, sheet1);
                RegionUtil.setBorderLeft(2, newCellTitleNameAd, sheet1);
                RegionUtil.setBorderRight(2, newCellTitleNameAd, sheet1);
                RegionUtil.setBorderTop(2, newCellTitleNameAd, sheet1);

                //第2列
                HSSFCell cellName = rowName.createCell(newCellTitleNameAd.getLastColumn() + 1);
                CellAddress cellNameAddress = cellName.getAddress();
                CellRangeAddress newCellNameAd = new CellRangeAddress(cellNameAddress.getRow(), cellNameAddress.getRow() + 1 + 1 + 1,
                        cellNameAddress.getColumn(), cellNameAddress.getColumn() + 2);
                sheet1.addMergedRegion(newCellNameAd);
                cellName.setCellValue(currentPs.get(i).getProjectName());
                hssfCellStyle.setFont(contentFont);
                cellName.setCellStyle(hssfCellStyle);
                RegionUtil.setBorderBottom(2, newCellNameAd, sheet1);
                RegionUtil.setBorderLeft(2, newCellNameAd, sheet1);
                RegionUtil.setBorderRight(2, newCellNameAd, sheet1);
                RegionUtil.setBorderTop(2, newCellNameAd, sheet1);


                //第3列
                HSSFCell cellCheckDateTitle = rowName.createCell(newCellNameAd.getLastColumn() + 1);
                CellAddress checkDateTitleAddress = cellCheckDateTitle.getAddress();
                CellRangeAddress newCellTitleCheckDateAd = new CellRangeAddress(checkDateTitleAddress.getRow(),
                        checkDateTitleAddress.getRow() + 1 + 1 + 1, checkDateTitleAddress.getColumn(), checkDateTitleAddress.getColumn() + 1);
                sheet1.addMergedRegion(newCellTitleCheckDateAd);
                cellCheckDateTitle.setCellValue("检测日期");
                hssfCellStyle.setFont(titleFont);
                cellCheckDateTitle.setCellStyle(hssfCellStyle);
                RegionUtil.setBorderBottom(2, newCellTitleCheckDateAd, sheet1);
                RegionUtil.setBorderLeft(2, newCellTitleCheckDateAd, sheet1);
                RegionUtil.setBorderRight(2, newCellTitleCheckDateAd, sheet1);
                RegionUtil.setBorderTop(2, newCellTitleCheckDateAd, sheet1);

                //第4列
                HSSFCell checkDate = rowName.createCell(newCellTitleCheckDateAd.getLastColumn() + 1);
                CellAddress checkDateAddress = checkDate.getAddress();
                CellRangeAddress newCellCheckDateAd = new CellRangeAddress(checkDateAddress.getRow(),
                        checkDateAddress.getRow() + 1 + 1 + 1, checkDateAddress.getColumn(), checkDateAddress.getColumn() + 2);
                sheet1.addMergedRegion(newCellCheckDateAd);
                checkDate.setCellValue(CommonUtils.formatDate(currentPs.get(i).getCheckDate()));
                hssfCellStyle.setFont(contentFont);
                RegionUtil.setBorderBottom(2, newCellCheckDateAd, sheet1);
                RegionUtil.setBorderLeft(2, newCellCheckDateAd, sheet1);
                RegionUtil.setBorderRight(2, newCellCheckDateAd, sheet1);
                RegionUtil.setBorderTop(2, newCellCheckDateAd, sheet1);
                checkDate.setCellStyle(hssfCellStyle);

                //创建第三行
                HSSFRow rowUnitEngineer = sheet1.createRow(newCellCheckDateAd.getLastRow() + 1);
                //第1列
                HSSFCell cellUnitEngineerTitle = rowUnitEngineer.createCell(0);
                CellAddress unitEngineerTitleAddress = cellUnitEngineerTitle.getAddress();
                CellRangeAddress unitEngineerTitleCheckDateAd = new CellRangeAddress(unitEngineerTitleAddress.getRow(),
                        unitEngineerTitleAddress.getRow() + 2 + 1, unitEngineerTitleAddress.getColumn(), unitEngineerTitleAddress.getColumn() + 1);
                sheet1.addMergedRegion(unitEngineerTitleCheckDateAd);
                cellUnitEngineerTitle.setCellValue("单位工程");
                hssfCellStyle.setFont(titleFont);
                cellUnitEngineerTitle.setCellStyle(hssfCellStyle);
                RegionUtil.setBorderBottom(2, unitEngineerTitleCheckDateAd, sheet1);
                RegionUtil.setBorderLeft(2, unitEngineerTitleCheckDateAd, sheet1);
                RegionUtil.setBorderRight(2, unitEngineerTitleCheckDateAd, sheet1);
                RegionUtil.setBorderTop(2, unitEngineerTitleCheckDateAd, sheet1);

                //第2列
                HSSFCell cellUnitEngineer = rowUnitEngineer.createCell(unitEngineerTitleCheckDateAd.getLastColumn() + 1);
                CellAddress cellUnitEngineerAddress = cellUnitEngineer.getAddress();
                CellRangeAddress newCellUnitEngineerAd = new CellRangeAddress(cellUnitEngineerAddress.getRow(),
                        cellUnitEngineerAddress.getRow() + 2 + 1, cellUnitEngineerAddress.getColumn(), cellUnitEngineerAddress.getColumn() + 1);
                sheet1.addMergedRegion(newCellUnitEngineerAd);
                cellUnitEngineer.setCellValue(currentPs.get(i).getUnitEngineering());
                hssfCellStyle.setFont(contentFont);
                cellUnitEngineer.setCellStyle(hssfCellStyle);
                RegionUtil.setBorderBottom(2, newCellUnitEngineerAd, sheet1);
                RegionUtil.setBorderLeft(2, newCellUnitEngineerAd, sheet1);
                RegionUtil.setBorderRight(2, newCellUnitEngineerAd, sheet1);
                RegionUtil.setBorderTop(2, newCellUnitEngineerAd, sheet1);

                //第3列
                HSSFCell cellBlockPileTitle = rowUnitEngineer.createCell(newCellUnitEngineerAd.getLastColumn() + 1);
                CellAddress blockPileTitleAddress = cellBlockPileTitle.getAddress();
                CellRangeAddress newBlockPileTitleAddress = new CellRangeAddress(blockPileTitleAddress.getRow(),
                        blockPileTitleAddress.getRow() + 2 + 1, blockPileTitleAddress.getColumn(), blockPileTitleAddress.getColumn() + 1);
                sheet1.addMergedRegion(newBlockPileTitleAddress);
                cellBlockPileTitle.setCellValue("标段及桩号");
                hssfCellStyle.setFont(titleFont);
                cellBlockPileTitle.setCellStyle(hssfCellStyle);
                RegionUtil.setBorderBottom(2, newBlockPileTitleAddress, sheet1);
                RegionUtil.setBorderLeft(2, newBlockPileTitleAddress, sheet1);
                RegionUtil.setBorderRight(2, newBlockPileTitleAddress, sheet1);
                RegionUtil.setBorderTop(2, newBlockPileTitleAddress, sheet1);

                //第4列
                HSSFCell cellBlockPile = rowUnitEngineer.createCell(newBlockPileTitleAddress.getLastColumn() + 1);
                CellAddress cellBlockPileAddress = cellBlockPile.getAddress();
                CellRangeAddress newCellBlockPileAd = new CellRangeAddress(cellBlockPileAddress.getRow(),
                        cellBlockPileAddress.getRow() + 2 + 1, cellBlockPileAddress.getColumn(), cellBlockPileAddress.getColumn() + 1);
                sheet1.addMergedRegion(newCellBlockPileAd);
                cellBlockPile.setCellValue(currentPs.get(i).getBlock() + " " + dataSource.get(i).getPilNo());
                hssfCellStyle.setFont(contentFont);
                cellBlockPile.setCellStyle(hssfCellStyle);
                RegionUtil.setBorderBottom(2, newCellBlockPileAd, sheet1);
                RegionUtil.setBorderLeft(2, newCellBlockPileAd, sheet1);
                RegionUtil.setBorderRight(2, newCellBlockPileAd, sheet1);
                RegionUtil.setBorderTop(2, newCellBlockPileAd, sheet1);

                //第5列
                HSSFCell cellDefectsTitle = rowUnitEngineer.createCell(newCellBlockPileAd.getLastColumn() + 1);
                CellAddress defectsAddress = cellDefectsTitle.getAddress();
                CellRangeAddress newDefectsTitleAddress = new CellRangeAddress(defectsAddress.getRow(),
                        defectsAddress.getRow() + 2 + 1, defectsAddress.getColumn(), defectsAddress.getColumn() + 1);
                sheet1.addMergedRegion(newDefectsTitleAddress);
                cellDefectsTitle.setCellValue("缺陷描述");
                hssfCellStyle.setFont(titleFont);
                cellDefectsTitle.setCellStyle(hssfCellStyle);
                RegionUtil.setBorderBottom(2, newDefectsTitleAddress, sheet1);
                RegionUtil.setBorderLeft(2, newDefectsTitleAddress, sheet1);
                RegionUtil.setBorderRight(2, newDefectsTitleAddress, sheet1);
                RegionUtil.setBorderTop(2, newDefectsTitleAddress, sheet1);

                //创建第4行
                HSSFRow rowImage = sheet1.createRow(unitEngineerTitleCheckDateAd.getLastRow() + 1);
                //第1列
                HSSFCell cellImage = rowImage.createCell(0);
                CellAddress imageAddress = cellImage.getAddress();
                CellRangeAddress imageAd = new CellRangeAddress(imageAddress.getRow(),
                        imageAddress.getRow() + 35, imageAddress.getColumn(), imageAddress.getColumn() + 7);
                sheet1.addMergedRegion(imageAd);
                RegionUtil.setBorderBottom(2, imageAd, sheet1);
                RegionUtil.setBorderLeft(2, imageAd, sheet1);
                RegionUtil.setBorderRight(2, imageAd, sheet1);
                RegionUtil.setBorderTop(2, imageAd, sheet1);

//                //插入图片
                insertImage(sheet1, currentPs.get(i).getImagePath(), imageAddress, imageAd, wb);

                //第2列
                HSSFCell cellDefects = rowImage.createCell(imageAd.getLastColumn() + 1);
                CellAddress cellDefectsAddress = cellDefects.getAddress();
                CellRangeAddress newCellDefectsAd = new CellRangeAddress(cellDefectsAddress.getRow(),
                        /*imageAddress.getRow() + y*/cellDefectsAddress.getRow() + 35, cellDefectsAddress.getColumn(), cellDefectsAddress.getColumn() + 1);
                sheet1.addMergedRegion(newCellDefectsAd);
                cellDefects.setCellValue(currentPs.get(i).getDefects());
                hssfCellStyle.setFont(contentFont);
                cellDefects.setCellStyle(hssfCellStyle);
                RegionUtil.setBorderBottom(2, newCellDefectsAd, sheet1);
                RegionUtil.setBorderLeft(2, newCellDefectsAd, sheet1);
                RegionUtil.setBorderRight(2, newCellDefectsAd, sheet1);
                RegionUtil.setBorderTop(2, newCellDefectsAd, sheet1);

                //创建第5行
                HSSFRow rowRemark = sheet1.createRow(imageAd.getLastRow() + 1);
                //第1列
                HSSFCell cellRemarkTitle = rowRemark.createCell(0);
                CellAddress remarkTitleAddress = cellRemarkTitle.getAddress();
                CellRangeAddress remarkTitleAd = new CellRangeAddress(remarkTitleAddress.getRow(),
                        remarkTitleAddress.getRow() + 1 + 1 + 1, remarkTitleAddress.getColumn(), remarkTitleAddress.getColumn() + 1);
                sheet1.addMergedRegion(remarkTitleAd);
                cellRemarkTitle.setCellValue("备注");
                hssfCellStyle.setFont(titleFont);
                cellRemarkTitle.setCellStyle(hssfCellStyle);
                RegionUtil.setBorderBottom(2, remarkTitleAd, sheet1);
                RegionUtil.setBorderLeft(2, remarkTitleAd, sheet1);
                RegionUtil.setBorderRight(2, remarkTitleAd, sheet1);
                RegionUtil.setBorderTop(2, remarkTitleAd, sheet1);

                //第2列
                HSSFCell cellRemark = rowRemark.createCell(remarkTitleAd.getLastColumn() + 1);
                CellAddress cellRemarkAddress = cellRemark.getAddress();
                CellRangeAddress newCellRemarkAd = new CellRangeAddress(cellRemarkAddress.getRow(),
                        cellRemarkAddress.getRow() + 1 + 1 + 1, cellRemarkAddress.getColumn(), cellRemarkAddress.getColumn() + 6 + 1);
                sheet1.addMergedRegion(newCellRemarkAd);
                cellRemark.setCellValue(currentPs.get(i).getRemark());
                hssfCellStyle.setFont(contentFont);
                RegionUtil.setBorderBottom(2, newCellRemarkAd, sheet1);
                RegionUtil.setBorderLeft(2, newCellRemarkAd, sheet1);
                RegionUtil.setBorderRight(2, newCellRemarkAd, sheet1);
                RegionUtil.setBorderTop(2, newCellRemarkAd, sheet1);
                cellRemark.setCellStyle(hssfCellStyle);

                lastRow = remarkTitleAd.getLastRow();
                exportListener.progress(i);
                // 写入excel文件
                wb.write(fileOut);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileOut != null) {
                    try {
                        fileOut.flush();
                        fileOut.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    if (fileInput != null) {
                        fileInput.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    if (wb != null) {
                        wb.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }


    private void writeExcel(List<ProjectEntity> dataSource, ExportListener exportListener) {
        List<ProjectEntity> currentPs = new ArrayList<>();
        currentPs.addAll(dataSource);
        int all = currentPs.size();
        int defaultCount = 5;
        int pages = getPages(all, defaultCount);
        int currentPage = 1;
        long currentTime = System.currentTimeMillis();
        String levelOneFileRootPath = CommonUtils.getZipPath(currentTime);
        String levelTwoFileRootPath = CommonUtils.getLevelTwoDataRootPath(currentTime);
        while (currentPage <= pages) {
            String filePath = CommonUtils.getDataFilePath(currentTime, currentPage);
            if (filePath == null) {
                return;
            }


//            String p = CommonUtils.getDataFilePath(System.currentTimeMillis(), pages);
//            if (p == null) {
//                return;
//            }
            FileOutputStream fileOut = null;
            HSSFWorkbook wb = new HSSFWorkbook();

            HSSFSheet sheet1 = wb.createSheet("项目数据" + CommonUtils.formatDate(System.currentTimeMillis()));
            HSSFCellStyle hssfCellStyle = wb.createCellStyle();
            HSSFFont titleFont = wb.createFont();
            titleFont.setFontName("宋体");
            titleFont.setBold(true);
            titleFont.setFontHeightInPoints((short) 18);
            HSSFFont contentFont = wb.createFont();
            contentFont.setFontName("宋体");
            contentFont.setBold(false);
            contentFont.setFontHeightInPoints((short) 14);
            HSSFPrintSetup printSetup = sheet1.getPrintSetup();
            printSetup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
            sheet1.setMargin(HSSFSheet.RightMargin, -0.5);


            try {
                fileOut = new FileOutputStream(filePath);
                int lastRow = 0;

                int start;
                int end;

                start = (currentPage - 1) * defaultCount;
                if (currentPage == pages) {
                    end = all;
                } else {
                    end = currentPage * defaultCount;
                }
                for (int i = start; i < end; i++) {
                    if (lastRow != 0) {
                        lastRow += 6;
                    }

                    //创建第一行，为标题行，可用可不用
                    HSSFRow titleRow = sheet1.createRow(lastRow);
                    //第1列
                    HSSFCell cellTitle = titleRow.createCell(0);
                    CellAddress cellTitleAddress = cellTitle.getAddress();
                    CellRangeAddress newCellTitleAd = new CellRangeAddress(cellTitleAddress.getRow(), cellTitleAddress.getRow() + 1 + 1, cellTitleAddress.getColumn(), cellTitleAddress.getColumn() + 9);
                    sheet1.addMergedRegion(newCellTitleAd);

                    hssfCellStyle.setFont(titleFont);
                    cellTitle.setCellStyle(hssfCellStyle);
                    HSSFCellStyle style = cellTitle.getCellStyle();
                    style.setAlignment(HorizontalAlignment.CENTER_SELECTION);//居中
                    style.setVerticalAlignment(VerticalAlignment.CENTER);

                    RegionUtil.setBorderBottom(2, newCellTitleAd, sheet1);

                    //创建第二行
                    HSSFRow rowName = sheet1.createRow(newCellTitleAd.getLastRow() + 1);
                    //第1列
                    HSSFCell cellNameTitle = rowName.createCell(0);
                    CellAddress cellNameTitleAddress = cellNameTitle.getAddress();
                    CellRangeAddress newCellTitleNameAd = new CellRangeAddress(cellNameTitleAddress.getRow(),
                            cellNameTitleAddress.getRow() + 1 + 1 + 1, cellNameTitleAddress.getColumn(), cellNameTitleAddress.getColumn() + 1);
                    sheet1.addMergedRegion(newCellTitleNameAd);
                    cellNameTitle.setCellValue("项目名称");
                    hssfCellStyle.setFont(titleFont);
                    hssfCellStyle.setAlignment(HorizontalAlignment.CENTER);//居中
                    cellNameTitle.setCellStyle(hssfCellStyle);
                    RegionUtil.setBorderBottom(2, newCellTitleNameAd, sheet1);
                    RegionUtil.setBorderLeft(2, newCellTitleNameAd, sheet1);
                    RegionUtil.setBorderRight(2, newCellTitleNameAd, sheet1);
                    RegionUtil.setBorderTop(2, newCellTitleNameAd, sheet1);

                    //第2列
                    HSSFCell cellName = rowName.createCell(newCellTitleNameAd.getLastColumn() + 1);
                    CellAddress cellNameAddress = cellName.getAddress();
                    CellRangeAddress newCellNameAd = new CellRangeAddress(cellNameAddress.getRow(), cellNameAddress.getRow() + 1 + 1 + 1,
                            cellNameAddress.getColumn(), cellNameAddress.getColumn() + 2);
                    sheet1.addMergedRegion(newCellNameAd);
                    cellName.setCellValue(currentPs.get(i).getProjectName());
                    hssfCellStyle.setFont(contentFont);
                    cellName.setCellStyle(hssfCellStyle);
                    RegionUtil.setBorderBottom(2, newCellNameAd, sheet1);
                    RegionUtil.setBorderLeft(2, newCellNameAd, sheet1);
                    RegionUtil.setBorderRight(2, newCellNameAd, sheet1);
                    RegionUtil.setBorderTop(2, newCellNameAd, sheet1);


                    //第3列
                    HSSFCell cellCheckDateTitle = rowName.createCell(newCellNameAd.getLastColumn() + 1);
                    CellAddress checkDateTitleAddress = cellCheckDateTitle.getAddress();
                    CellRangeAddress newCellTitleCheckDateAd = new CellRangeAddress(checkDateTitleAddress.getRow(),
                            checkDateTitleAddress.getRow() + 1 + 1 + 1, checkDateTitleAddress.getColumn(), checkDateTitleAddress.getColumn() + 1);
                    sheet1.addMergedRegion(newCellTitleCheckDateAd);
                    cellCheckDateTitle.setCellValue("检测日期");
                    hssfCellStyle.setFont(titleFont);
                    cellCheckDateTitle.setCellStyle(hssfCellStyle);
                    RegionUtil.setBorderBottom(2, newCellTitleCheckDateAd, sheet1);
                    RegionUtil.setBorderLeft(2, newCellTitleCheckDateAd, sheet1);
                    RegionUtil.setBorderRight(2, newCellTitleCheckDateAd, sheet1);
                    RegionUtil.setBorderTop(2, newCellTitleCheckDateAd, sheet1);

                    //第4列
                    HSSFCell checkDate = rowName.createCell(newCellTitleCheckDateAd.getLastColumn() + 1);
                    CellAddress checkDateAddress = checkDate.getAddress();
                    CellRangeAddress newCellCheckDateAd = new CellRangeAddress(checkDateAddress.getRow(),
                            checkDateAddress.getRow() + 1 + 1 + 1, checkDateAddress.getColumn(), checkDateAddress.getColumn() + 2);
                    sheet1.addMergedRegion(newCellCheckDateAd);
                    checkDate.setCellValue(CommonUtils.formatDate(currentPs.get(i).getCheckDate()));
                    hssfCellStyle.setFont(contentFont);
                    RegionUtil.setBorderBottom(2, newCellCheckDateAd, sheet1);
                    RegionUtil.setBorderLeft(2, newCellCheckDateAd, sheet1);
                    RegionUtil.setBorderRight(2, newCellCheckDateAd, sheet1);
                    RegionUtil.setBorderTop(2, newCellCheckDateAd, sheet1);
                    checkDate.setCellStyle(hssfCellStyle);

                    //创建第三行
                    HSSFRow rowUnitEngineer = sheet1.createRow(newCellCheckDateAd.getLastRow() + 1);
                    //第1列
                    HSSFCell cellUnitEngineerTitle = rowUnitEngineer.createCell(0);
                    CellAddress unitEngineerTitleAddress = cellUnitEngineerTitle.getAddress();
                    CellRangeAddress unitEngineerTitleCheckDateAd = new CellRangeAddress(unitEngineerTitleAddress.getRow(),
                            unitEngineerTitleAddress.getRow() + 2 + 1, unitEngineerTitleAddress.getColumn(), unitEngineerTitleAddress.getColumn() + 1);
                    sheet1.addMergedRegion(unitEngineerTitleCheckDateAd);
                    cellUnitEngineerTitle.setCellValue("单位工程");
                    hssfCellStyle.setFont(titleFont);
                    cellUnitEngineerTitle.setCellStyle(hssfCellStyle);
                    RegionUtil.setBorderBottom(2, unitEngineerTitleCheckDateAd, sheet1);
                    RegionUtil.setBorderLeft(2, unitEngineerTitleCheckDateAd, sheet1);
                    RegionUtil.setBorderRight(2, unitEngineerTitleCheckDateAd, sheet1);
                    RegionUtil.setBorderTop(2, unitEngineerTitleCheckDateAd, sheet1);

                    //第2列
                    HSSFCell cellUnitEngineer = rowUnitEngineer.createCell(unitEngineerTitleCheckDateAd.getLastColumn() + 1);
                    CellAddress cellUnitEngineerAddress = cellUnitEngineer.getAddress();
                    CellRangeAddress newCellUnitEngineerAd = new CellRangeAddress(cellUnitEngineerAddress.getRow(),
                            cellUnitEngineerAddress.getRow() + 2 + 1, cellUnitEngineerAddress.getColumn(), cellUnitEngineerAddress.getColumn() + 1);
                    sheet1.addMergedRegion(newCellUnitEngineerAd);
                    cellUnitEngineer.setCellValue(currentPs.get(i).getUnitEngineering());
                    hssfCellStyle.setFont(contentFont);
                    cellUnitEngineer.setCellStyle(hssfCellStyle);
                    RegionUtil.setBorderBottom(2, newCellUnitEngineerAd, sheet1);
                    RegionUtil.setBorderLeft(2, newCellUnitEngineerAd, sheet1);
                    RegionUtil.setBorderRight(2, newCellUnitEngineerAd, sheet1);
                    RegionUtil.setBorderTop(2, newCellUnitEngineerAd, sheet1);

                    //第3列
                    HSSFCell cellBlockPileTitle = rowUnitEngineer.createCell(newCellUnitEngineerAd.getLastColumn() + 1);
                    CellAddress blockPileTitleAddress = cellBlockPileTitle.getAddress();
                    CellRangeAddress newBlockPileTitleAddress = new CellRangeAddress(blockPileTitleAddress.getRow(),
                            blockPileTitleAddress.getRow() + 2 + 1, blockPileTitleAddress.getColumn(), blockPileTitleAddress.getColumn() + 1);
                    sheet1.addMergedRegion(newBlockPileTitleAddress);
                    cellBlockPileTitle.setCellValue("标段及桩号");
                    hssfCellStyle.setFont(titleFont);
                    cellBlockPileTitle.setCellStyle(hssfCellStyle);
                    RegionUtil.setBorderBottom(2, newBlockPileTitleAddress, sheet1);
                    RegionUtil.setBorderLeft(2, newBlockPileTitleAddress, sheet1);
                    RegionUtil.setBorderRight(2, newBlockPileTitleAddress, sheet1);
                    RegionUtil.setBorderTop(2, newBlockPileTitleAddress, sheet1);

                    //第4列
                    HSSFCell cellBlockPile = rowUnitEngineer.createCell(newBlockPileTitleAddress.getLastColumn() + 1);
                    CellAddress cellBlockPileAddress = cellBlockPile.getAddress();
                    CellRangeAddress newCellBlockPileAd = new CellRangeAddress(cellBlockPileAddress.getRow(),
                            cellBlockPileAddress.getRow() + 2 + 1, cellBlockPileAddress.getColumn(), cellBlockPileAddress.getColumn() + 1);
                    sheet1.addMergedRegion(newCellBlockPileAd);
                    cellBlockPile.setCellValue(currentPs.get(i).getBlock() + " " + dataSource.get(i).getPilNo());
                    hssfCellStyle.setFont(contentFont);
                    cellBlockPile.setCellStyle(hssfCellStyle);
                    RegionUtil.setBorderBottom(2, newCellBlockPileAd, sheet1);
                    RegionUtil.setBorderLeft(2, newCellBlockPileAd, sheet1);
                    RegionUtil.setBorderRight(2, newCellBlockPileAd, sheet1);
                    RegionUtil.setBorderTop(2, newCellBlockPileAd, sheet1);

                    //第5列
                    HSSFCell cellDefectsTitle = rowUnitEngineer.createCell(newCellBlockPileAd.getLastColumn() + 1);
                    CellAddress defectsAddress = cellDefectsTitle.getAddress();
                    CellRangeAddress newDefectsTitleAddress = new CellRangeAddress(defectsAddress.getRow(),
                            defectsAddress.getRow() + 2 + 1, defectsAddress.getColumn(), defectsAddress.getColumn() + 1);
                    sheet1.addMergedRegion(newDefectsTitleAddress);
                    cellDefectsTitle.setCellValue("缺陷描述");
                    hssfCellStyle.setFont(titleFont);
                    cellDefectsTitle.setCellStyle(hssfCellStyle);
                    RegionUtil.setBorderBottom(2, newDefectsTitleAddress, sheet1);
                    RegionUtil.setBorderLeft(2, newDefectsTitleAddress, sheet1);
                    RegionUtil.setBorderRight(2, newDefectsTitleAddress, sheet1);
                    RegionUtil.setBorderTop(2, newDefectsTitleAddress, sheet1);

                    //创建第4行
                    HSSFRow rowImage = sheet1.createRow(unitEngineerTitleCheckDateAd.getLastRow() + 1);
                    //第1列
                    HSSFCell cellImage = rowImage.createCell(0);
                    CellAddress imageAddress = cellImage.getAddress();
                    CellRangeAddress imageAd = new CellRangeAddress(imageAddress.getRow(),
                            imageAddress.getRow() + 35, imageAddress.getColumn(), imageAddress.getColumn() + 7);
                    sheet1.addMergedRegion(imageAd);
                    RegionUtil.setBorderBottom(2, imageAd, sheet1);
                    RegionUtil.setBorderLeft(2, imageAd, sheet1);
                    RegionUtil.setBorderRight(2, imageAd, sheet1);
                    RegionUtil.setBorderTop(2, imageAd, sheet1);


//                //插入图片
                    insertImage(sheet1, currentPs.get(i).getImagePath(), imageAddress, imageAd, wb);

                    //第2列
                    HSSFCell cellDefects = rowImage.createCell(imageAd.getLastColumn() + 1);
                    CellAddress cellDefectsAddress = cellDefects.getAddress();
                    CellRangeAddress newCellDefectsAd = new CellRangeAddress(cellDefectsAddress.getRow(),
                        /*imageAddress.getRow() + y*/cellDefectsAddress.getRow() + 35, cellDefectsAddress.getColumn(), cellDefectsAddress.getColumn() + 1);
                    sheet1.addMergedRegion(newCellDefectsAd);
                    cellDefects.setCellValue(currentPs.get(i).getDefects());
                    hssfCellStyle.setFont(contentFont);
                    cellDefects.setCellStyle(hssfCellStyle);
                    RegionUtil.setBorderBottom(2, newCellDefectsAd, sheet1);
                    RegionUtil.setBorderLeft(2, newCellDefectsAd, sheet1);
                    RegionUtil.setBorderRight(2, newCellDefectsAd, sheet1);
                    RegionUtil.setBorderTop(2, newCellDefectsAd, sheet1);

                    //创建第5行
                    HSSFRow rowRemark = sheet1.createRow(imageAd.getLastRow() + 1);
                    //第1列
                    HSSFCell cellRemarkTitle = rowRemark.createCell(0);
                    CellAddress remarkTitleAddress = cellRemarkTitle.getAddress();
                    CellRangeAddress remarkTitleAd = new CellRangeAddress(remarkTitleAddress.getRow(),
                            remarkTitleAddress.getRow() + 1 + 1 + 1, remarkTitleAddress.getColumn(), remarkTitleAddress.getColumn() + 1);
                    sheet1.addMergedRegion(remarkTitleAd);
                    cellRemarkTitle.setCellValue("备注");
                    hssfCellStyle.setFont(titleFont);
                    cellRemarkTitle.setCellStyle(hssfCellStyle);
                    RegionUtil.setBorderBottom(2, remarkTitleAd, sheet1);
                    RegionUtil.setBorderLeft(2, remarkTitleAd, sheet1);
                    RegionUtil.setBorderRight(2, remarkTitleAd, sheet1);
                    RegionUtil.setBorderTop(2, remarkTitleAd, sheet1);

                    //第2列
                    HSSFCell cellRemark = rowRemark.createCell(remarkTitleAd.getLastColumn() + 1);
                    CellAddress cellRemarkAddress = cellRemark.getAddress();
                    CellRangeAddress newCellRemarkAd = new CellRangeAddress(cellRemarkAddress.getRow(),
                            cellRemarkAddress.getRow() + 1 + 1 + 1, cellRemarkAddress.getColumn(), cellRemarkAddress.getColumn() + 6 + 1);
                    sheet1.addMergedRegion(newCellRemarkAd);
                    cellRemark.setCellValue(currentPs.get(i).getRemark());
                    hssfCellStyle.setFont(contentFont);
                    RegionUtil.setBorderBottom(2, newCellRemarkAd, sheet1);
                    RegionUtil.setBorderLeft(2, newCellRemarkAd, sheet1);
                    RegionUtil.setBorderRight(2, newCellRemarkAd, sheet1);
                    RegionUtil.setBorderTop(2, newCellRemarkAd, sheet1);
                    cellRemark.setCellStyle(hssfCellStyle);

                    lastRow = remarkTitleAd.getLastRow();
                    exportListener.progress(i);
                }

                // 写入excel文件
                wb.write(fileOut);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fileOut != null) {
                    try {
                        fileOut.flush();
                        fileOut.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }

                try {
                    wb.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            currentPage++;
        }

        try {
            ZipUtil.zipFolder(levelTwoFileRootPath, levelOneFileRootPath);
//            CompressUtil.zip(levelTwoFileRootPath, levelOneFileRootPath, null);
        } catch (Exception e) {
            Log.e("zip", e.getMessage());
        }
        shareDataFile(Uri.fromFile(new File(levelOneFileRootPath)));
    }

    private int getPages(int all, int defaultCount) {
        if (all < defaultCount) {
            return 1;
        }

        if (all % defaultCount == 0) {
            return all / defaultCount;
        } else {
            return all / defaultCount + 1;
        }
    }

    private void writeExcel3(List<ProjectEntity> dataSource, ExportListener exportListener) {
        List<ProjectEntity> currentPs = new ArrayList<>();
        currentPs.addAll(dataSource/*filterDataSource(dataSource)*/);
        FileOutputStream fileOut = null;
//        String rootPath = CommonUtils.getDataFilePath(System.currentTimeMillis());
//        if (rootPath == null) {
//            return;
//        }
        HSSFWorkbook wb = new HSSFWorkbook();

        HSSFSheet sheet1 = wb.createSheet("项目数据" + CommonUtils.formatDate(System.currentTimeMillis()));
        HSSFCellStyle hssfCellStyle = wb.createCellStyle();
        HSSFFont titleFont = wb.createFont();
        titleFont.setFontName("宋体");
        titleFont.setBold(true);
        titleFont.setFontHeightInPoints((short) 18);
        HSSFFont contentFont = wb.createFont();
        contentFont.setFontName("宋体");
        contentFont.setBold(false);
        contentFont.setFontHeightInPoints((short) 16);
        HSSFPrintSetup printSetup = sheet1.getPrintSetup();
        printSetup.setPaperSize(HSSFPrintSetup.A4_PAPERSIZE);
        sheet1.setMargin(HSSFSheet.RightMargin, -0.5);

        String p = CommonUtils.getDataFilePath(System.currentTimeMillis());
        if (p == null) {
            return;
        }
        try {
            fileOut = new FileOutputStream(p);
            int lastRow = 0;
            for (int i = 0; i < currentPs.size(); i++) {
                if (lastRow == 0) {
//                    lastRow += 1;
                } else {
                    lastRow += 6;
                }

                //创建第一行，为标题行，可用可不用
                HSSFRow titleRow = sheet1.createRow(lastRow);
                //第1列
                HSSFCell cellTitle = titleRow.createCell(0);
                CellAddress cellTitleAddress = cellTitle.getAddress();
                CellRangeAddress newCellTitleAd = new CellRangeAddress(cellTitleAddress.getRow(), cellTitleAddress.getRow() + 1 + 1, cellTitleAddress.getColumn(), cellTitleAddress.getColumn() + 9);
                sheet1.addMergedRegion(newCellTitleAd);
//                cellTitle.setCellValue("项目名称");

                hssfCellStyle.setFont(titleFont);
                cellTitle.setCellStyle(hssfCellStyle);
                HSSFCellStyle style = cellTitle.getCellStyle();
                style.setAlignment(HorizontalAlignment.CENTER_SELECTION);//居中
                style.setVerticalAlignment(VerticalAlignment.CENTER);

                RegionUtil.setBorderBottom(2, newCellTitleAd, sheet1);
//                RegionUtil.setBorderLeft(2,newCellTitleAd, sheet1);
//                RegionUtil.setBorderRight(2,newCellTitleAd, sheet1);
//                RegionUtil.setBorderTop(2,newCellTitleAd, sheet1);


                //创建第二行
                HSSFRow rowName = sheet1.createRow(newCellTitleAd.getLastRow() + 1);
                //第1列
                HSSFCell cellNameTitle = rowName.createCell(0);
                CellAddress cellNameTitleAddress = cellNameTitle.getAddress();
                CellRangeAddress newCellTitleNameAd = new CellRangeAddress(cellNameTitleAddress.getRow(),
                        cellNameTitleAddress.getRow() + 1 + 1 + 1, cellNameTitleAddress.getColumn(), cellNameTitleAddress.getColumn() + 1);
                sheet1.addMergedRegion(newCellTitleNameAd);
                cellNameTitle.setCellValue("项目名称");
                hssfCellStyle.setFont(titleFont);
                hssfCellStyle.setAlignment(HorizontalAlignment.CENTER);//居中
                cellNameTitle.setCellStyle(hssfCellStyle);
                RegionUtil.setBorderBottom(2, newCellTitleNameAd, sheet1);
                RegionUtil.setBorderLeft(2, newCellTitleNameAd, sheet1);
                RegionUtil.setBorderRight(2, newCellTitleNameAd, sheet1);
                RegionUtil.setBorderTop(2, newCellTitleNameAd, sheet1);

                //第2列
                HSSFCell cellName = rowName.createCell(newCellTitleNameAd.getLastColumn() + 1);
                CellAddress cellNameAddress = cellName.getAddress();
                CellRangeAddress newCellNameAd = new CellRangeAddress(cellNameAddress.getRow(), cellNameAddress.getRow() + 1 + 1 + 1,
                        cellNameAddress.getColumn(), cellNameAddress.getColumn() + 2);
                sheet1.addMergedRegion(newCellNameAd);
                cellName.setCellValue(currentPs.get(i).getProjectName());
                hssfCellStyle.setFont(contentFont);
                cellName.setCellStyle(hssfCellStyle);
                RegionUtil.setBorderBottom(2, newCellNameAd, sheet1);
                RegionUtil.setBorderLeft(2, newCellNameAd, sheet1);
                RegionUtil.setBorderRight(2, newCellNameAd, sheet1);
                RegionUtil.setBorderTop(2, newCellNameAd, sheet1);


                //第3列
                HSSFCell cellCheckDateTitle = rowName.createCell(newCellNameAd.getLastColumn() + 1);
                CellAddress checkDateTitleAddress = cellCheckDateTitle.getAddress();
                CellRangeAddress newCellTitleCheckDateAd = new CellRangeAddress(checkDateTitleAddress.getRow(),
                        checkDateTitleAddress.getRow() + 1 + 1 + 1, checkDateTitleAddress.getColumn(), checkDateTitleAddress.getColumn() + 1);
                sheet1.addMergedRegion(newCellTitleCheckDateAd);
                cellCheckDateTitle.setCellValue("检测日期");
                hssfCellStyle.setFont(titleFont);
                cellCheckDateTitle.setCellStyle(hssfCellStyle);
                RegionUtil.setBorderBottom(2, newCellTitleCheckDateAd, sheet1);
                RegionUtil.setBorderLeft(2, newCellTitleCheckDateAd, sheet1);
                RegionUtil.setBorderRight(2, newCellTitleCheckDateAd, sheet1);
                RegionUtil.setBorderTop(2, newCellTitleCheckDateAd, sheet1);

                //第4列
                HSSFCell checkDate = rowName.createCell(newCellTitleCheckDateAd.getLastColumn() + 1);
                CellAddress checkDateAddress = checkDate.getAddress();
                CellRangeAddress newCellCheckDateAd = new CellRangeAddress(checkDateAddress.getRow(),
                        checkDateAddress.getRow() + 1 + 1 + 1, checkDateAddress.getColumn(), checkDateAddress.getColumn() + 2);
                sheet1.addMergedRegion(newCellCheckDateAd);
                checkDate.setCellValue(CommonUtils.formatDate(currentPs.get(i).getCheckDate()));
                hssfCellStyle.setFont(contentFont);
                RegionUtil.setBorderBottom(2, newCellCheckDateAd, sheet1);
                RegionUtil.setBorderLeft(2, newCellCheckDateAd, sheet1);
                RegionUtil.setBorderRight(2, newCellCheckDateAd, sheet1);
                RegionUtil.setBorderTop(2, newCellCheckDateAd, sheet1);
                checkDate.setCellStyle(hssfCellStyle);

                //创建第三行
                HSSFRow rowUnitEngineer = sheet1.createRow(newCellCheckDateAd.getLastRow() + 1);
                //第1列
                HSSFCell cellUnitEngineerTitle = rowUnitEngineer.createCell(0);
                CellAddress unitEngineerTitleAddress = cellUnitEngineerTitle.getAddress();
                CellRangeAddress unitEngineerTitleCheckDateAd = new CellRangeAddress(unitEngineerTitleAddress.getRow(),
                        unitEngineerTitleAddress.getRow() + 2 + 1, unitEngineerTitleAddress.getColumn(), unitEngineerTitleAddress.getColumn() + 1);
                sheet1.addMergedRegion(unitEngineerTitleCheckDateAd);
                cellUnitEngineerTitle.setCellValue("单位工程");
                hssfCellStyle.setFont(titleFont);
                cellUnitEngineerTitle.setCellStyle(hssfCellStyle);
                RegionUtil.setBorderBottom(2, unitEngineerTitleCheckDateAd, sheet1);
                RegionUtil.setBorderLeft(2, unitEngineerTitleCheckDateAd, sheet1);
                RegionUtil.setBorderRight(2, unitEngineerTitleCheckDateAd, sheet1);
                RegionUtil.setBorderTop(2, unitEngineerTitleCheckDateAd, sheet1);

                //第2列
                HSSFCell cellUnitEngineer = rowUnitEngineer.createCell(unitEngineerTitleCheckDateAd.getLastColumn() + 1);
                CellAddress cellUnitEngineerAddress = cellUnitEngineer.getAddress();
                CellRangeAddress newCellUnitEngineerAd = new CellRangeAddress(cellUnitEngineerAddress.getRow(),
                        cellUnitEngineerAddress.getRow() + 2 + 1, cellUnitEngineerAddress.getColumn(), cellUnitEngineerAddress.getColumn() + 1);
                sheet1.addMergedRegion(newCellUnitEngineerAd);
                cellUnitEngineer.setCellValue(currentPs.get(i).getUnitEngineering());
                hssfCellStyle.setFont(contentFont);
                cellUnitEngineer.setCellStyle(hssfCellStyle);
                RegionUtil.setBorderBottom(2, newCellUnitEngineerAd, sheet1);
                RegionUtil.setBorderLeft(2, newCellUnitEngineerAd, sheet1);
                RegionUtil.setBorderRight(2, newCellUnitEngineerAd, sheet1);
                RegionUtil.setBorderTop(2, newCellUnitEngineerAd, sheet1);

                //第3列
                HSSFCell cellBlockPileTitle = rowUnitEngineer.createCell(newCellUnitEngineerAd.getLastColumn() + 1);
                CellAddress blockPileTitleAddress = cellBlockPileTitle.getAddress();
                CellRangeAddress newBlockPileTitleAddress = new CellRangeAddress(blockPileTitleAddress.getRow(),
                        blockPileTitleAddress.getRow() + 2 + 1, blockPileTitleAddress.getColumn(), blockPileTitleAddress.getColumn() + 1);
                sheet1.addMergedRegion(newBlockPileTitleAddress);
                cellBlockPileTitle.setCellValue("标段及桩号");
                hssfCellStyle.setFont(titleFont);
                cellBlockPileTitle.setCellStyle(hssfCellStyle);
                RegionUtil.setBorderBottom(2, newBlockPileTitleAddress, sheet1);
                RegionUtil.setBorderLeft(2, newBlockPileTitleAddress, sheet1);
                RegionUtil.setBorderRight(2, newBlockPileTitleAddress, sheet1);
                RegionUtil.setBorderTop(2, newBlockPileTitleAddress, sheet1);

                //第4列
                HSSFCell cellBlockPile = rowUnitEngineer.createCell(newBlockPileTitleAddress.getLastColumn() + 1);
                CellAddress cellBlockPileAddress = cellBlockPile.getAddress();
                CellRangeAddress newCellBlockPileAd = new CellRangeAddress(cellBlockPileAddress.getRow(),
                        cellBlockPileAddress.getRow() + 2 + 1, cellBlockPileAddress.getColumn(), cellBlockPileAddress.getColumn() + 1);
                sheet1.addMergedRegion(newCellBlockPileAd);
                cellBlockPile.setCellValue(currentPs.get(i).getBlock() + " " + dataSource.get(i).getPilNo());
                hssfCellStyle.setFont(contentFont);
                cellBlockPile.setCellStyle(hssfCellStyle);
                RegionUtil.setBorderBottom(2, newCellBlockPileAd, sheet1);
                RegionUtil.setBorderLeft(2, newCellBlockPileAd, sheet1);
                RegionUtil.setBorderRight(2, newCellBlockPileAd, sheet1);
                RegionUtil.setBorderTop(2, newCellBlockPileAd, sheet1);

                //第5列
                HSSFCell cellDefectsTitle = rowUnitEngineer.createCell(newCellBlockPileAd.getLastColumn() + 1);
                CellAddress defectsAddress = cellDefectsTitle.getAddress();
                CellRangeAddress newDefectsTitleAddress = new CellRangeAddress(defectsAddress.getRow(),
                        defectsAddress.getRow() + 2 + 1, defectsAddress.getColumn(), defectsAddress.getColumn() + 1);
                sheet1.addMergedRegion(newDefectsTitleAddress);
                cellDefectsTitle.setCellValue("缺陷描述");
                hssfCellStyle.setFont(titleFont);
                cellDefectsTitle.setCellStyle(hssfCellStyle);
                RegionUtil.setBorderBottom(2, newDefectsTitleAddress, sheet1);
                RegionUtil.setBorderLeft(2, newDefectsTitleAddress, sheet1);
                RegionUtil.setBorderRight(2, newDefectsTitleAddress, sheet1);
                RegionUtil.setBorderTop(2, newDefectsTitleAddress, sheet1);

                //创建第4行
                HSSFRow rowImage = sheet1.createRow(unitEngineerTitleCheckDateAd.getLastRow() + 1);
                //第1列
                HSSFCell cellImage = rowImage.createCell(0);
                CellAddress imageAddress = cellImage.getAddress();
                CellRangeAddress imageAd = new CellRangeAddress(imageAddress.getRow(),
                        imageAddress.getRow() + 35, imageAddress.getColumn(), imageAddress.getColumn() + 7);
                sheet1.addMergedRegion(imageAd);
                RegionUtil.setBorderBottom(2, imageAd, sheet1);
                RegionUtil.setBorderLeft(2, imageAd, sheet1);
                RegionUtil.setBorderRight(2, imageAd, sheet1);
                RegionUtil.setBorderTop(2, imageAd, sheet1);


                /*CellRangeAddress imageAd = null;
                byte[] buffer;
                int y = 0;
                int x = 0;
                FileInputStream fis = null;
                ByteArrayOutputStream bos = null;
                try {
                    File file = new File(currentPs.get(i).getImagePath());
                    fis = new FileInputStream(file);
                    bos = new ByteArrayOutputStream(1000);
                    byte[] b = new byte[1000];
                    int n;
                    while ((n = fis.read(b)) != -1) {
                        bos.write(b, 0, n);
                    }

                    buffer = bos.toByteArray();
                    //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
                    HSSFPatriarch patriarch = sheet1.createDrawingPatriarch();
                    //anchor主要用于设置图片的属性
//                hssfCell.getRow().getRowNum();
//            BitmapFactory.Options options = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeFile(currentPs.get(i).getImagePath()); // 此时返回的bitmap为null
                    double imageWidth = bitmap.getWidth();
                    double imageHeight = bitmap.getHeight();

//            double zoomIndex;
//            if (imageHeight > 255) {
//                zoomIndex = imageHeight / 255;
//                imageHeight /= zoomIndex;
//                imageWidth /= zoomIndex;
//            }
                    int columnWidth = sheet1.getDefaultColumnWidth();// 8
                    int rowHeight = sheet1.getDefaultRowHeight();//255
//                    int columNum = imageRCellAd.getLastColumn() - imageCellAd.getColumn();
                    int rowNum *//*= imageRCellAd.getLastRow() - imageCellAd.getRow()*//*;

                    rowNum = 31 *//*(columnWidth * columNum) / rowHeight*//*;

//            if (imageHeight > 255) {
//                imageHeight = 255;
//            } else if (imageHeight <= 0) {
//                return;
//            }

                    imageAd = new CellRangeAddress(imageAddress.getRow(), imageAddress.getRow() + y*//*imageAddress.getRow() + 10 + 10 + 11*//*, imageAddress.getColumn(), imageAddress.getColumn() + 4 + 3);
                    sheet1.addMergedRegion(imageAd);
                    HSSFClientAnchor anchor = new HSSFClientAnchor(0, 0, (int) 255, (int) 255,
                            (short) imageAddress.getColumn(), imageAddress.getRow(), (short) (imageRCellAd.getLastColumn()), imageAddress.getRow() + y*//*imageRCellAd.getLastRow()*//*);
                    anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);
                    //插入图片
                    patriarch.createPicture(anchor, wb.addPicture(buffer, HSSFWorkbook.PICTURE_TYPE_JPEG));
                } catch (IOException e) {
                    e.printStackTrace();
                } finally {
                    try {
                        if (fis != null) {
                            fis.close();
                        }

                        if (bos != null) {
                            bos.close();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }*/


//                //插入图片
                insertImage(sheet1, currentPs.get(i).getImagePath(), imageAddress, imageAd, wb);

                //第2列
                HSSFCell cellDefects = rowImage.createCell(imageAd.getLastColumn() + 1);
                CellAddress cellDefectsAddress = cellDefects.getAddress();
                CellRangeAddress newCellDefectsAd = new CellRangeAddress(cellDefectsAddress.getRow(),
                        /*imageAddress.getRow() + y*/cellDefectsAddress.getRow() + 35, cellDefectsAddress.getColumn(), cellDefectsAddress.getColumn() + 1);
                sheet1.addMergedRegion(newCellDefectsAd);
                cellDefects.setCellValue(currentPs.get(i).getDefects());
                hssfCellStyle.setFont(contentFont);
                cellDefects.setCellStyle(hssfCellStyle);
                RegionUtil.setBorderBottom(2, newCellDefectsAd, sheet1);
                RegionUtil.setBorderLeft(2, newCellDefectsAd, sheet1);
                RegionUtil.setBorderRight(2, newCellDefectsAd, sheet1);
                RegionUtil.setBorderTop(2, newCellDefectsAd, sheet1);

                //创建第5行
                HSSFRow rowRemark = sheet1.createRow(imageAd.getLastRow() + 1);
                //第1列
                HSSFCell cellRemarkTitle = rowRemark.createCell(0);
                CellAddress remarkTitleAddress = cellRemarkTitle.getAddress();
                CellRangeAddress remarkTitleAd = new CellRangeAddress(remarkTitleAddress.getRow(),
                        remarkTitleAddress.getRow() + 1 + 1 + 1, remarkTitleAddress.getColumn(), remarkTitleAddress.getColumn() + 1);
                sheet1.addMergedRegion(remarkTitleAd);
                cellRemarkTitle.setCellValue("备注");
                hssfCellStyle.setFont(titleFont);
                cellRemarkTitle.setCellStyle(hssfCellStyle);
                RegionUtil.setBorderBottom(2, remarkTitleAd, sheet1);
                RegionUtil.setBorderLeft(2, remarkTitleAd, sheet1);
                RegionUtil.setBorderRight(2, remarkTitleAd, sheet1);
                RegionUtil.setBorderTop(2, remarkTitleAd, sheet1);

                //第2列
                HSSFCell cellRemark = rowRemark.createCell(remarkTitleAd.getLastColumn() + 1);
                CellAddress cellRemarkAddress = cellRemark.getAddress();
                CellRangeAddress newCellRemarkAd = new CellRangeAddress(cellRemarkAddress.getRow(),
                        cellRemarkAddress.getRow() + 1 + 1 + 1, cellRemarkAddress.getColumn(), cellRemarkAddress.getColumn() + 6 + 1);
                sheet1.addMergedRegion(newCellRemarkAd);
                cellRemark.setCellValue(currentPs.get(i).getRemark());
                hssfCellStyle.setFont(contentFont);
                RegionUtil.setBorderBottom(2, newCellRemarkAd, sheet1);
                RegionUtil.setBorderLeft(2, newCellRemarkAd, sheet1);
                RegionUtil.setBorderRight(2, newCellRemarkAd, sheet1);
                RegionUtil.setBorderTop(2, newCellRemarkAd, sheet1);
                cellRemark.setCellStyle(hssfCellStyle);

                lastRow = remarkTitleAd.getLastRow();
                exportListener.progress(i);
            }

            // 写入excel文件
            wb.write(fileOut);
            shareDataFile(Uri.fromFile(new File(p)));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fileOut != null) {
                try {
                    fileOut.flush();
                    fileOut.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            try {
                wb.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void shareDataFile(Uri uri) {
        Intent intent = new Intent(Intent.ACTION_SEND);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        intent.setType("application/vnd.ms-excel");
        intent.putExtra(Intent.EXTRA_STREAM, uri);
        intent.putExtra(Intent.EXTRA_SUBJECT, "数据文件");
        intent.putExtra(Intent.EXTRA_TEXT, "数据文件");
        startActivity(Intent.createChooser(intent, "来自数据采集"));
    }

    public void insertImage(HSSFSheet sheet,
                            String imagePath,
                            CellAddress imageCellAd,
                            CellRangeAddress imageRCellAd,
                            HSSFWorkbook wb) {
        byte[] buffer;
        FileInputStream fis = null;
        ByteArrayOutputStream bos = null;
        try {
            File file = new File(imagePath);
            fis = new FileInputStream(file);
            bos = new ByteArrayOutputStream(1000);
            byte[] b = new byte[1000];
            int n;
            while ((n = fis.read(b)) != -1) {
                bos.write(b, 0, n);
            }

            buffer = bos.toByteArray();
            //画图的顶级管理器，一个sheet只能获取一个（一定要注意这点）
            HSSFPatriarch patriarch = sheet.createDrawingPatriarch();
            //anchor主要用于设置图片的属性
//                hssfCell.getRow().getRowNum();
//            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath); // 此时返回的bitmap为null
            double imageWidth = bitmap.getWidth();
            double imageHeight = bitmap.getHeight();

//            double zoomIndex;
//            if (imageHeight > 255) {
//                zoomIndex = imageHeight / 255;
//                imageHeight /= zoomIndex;
//                imageWidth /= zoomIndex;
//            }
            int columnWidth = sheet.getDefaultColumnWidth();// 8
            int rowHeight = sheet.getDefaultRowHeight();//255
            int columNum = imageRCellAd.getLastColumn() - imageCellAd.getColumn();
            int rowNum /*= imageRCellAd.getLastRow() - imageCellAd.getRow()*/;

            rowNum = 31 /*(columnWidth * columNum) / rowHeight*/;

//            if (imageHeight > 255) {
//                imageHeight = 255;
//            } else if (imageHeight <= 0) {
//                return;
//            }
            int x = 0;
            int y = 0;
            if (imageWidth < imageHeight) {
                x = (int) (imageWidth / 81.81);
                y = (int) (imageHeight / 22);
            } else {
                x = (int) (imageWidth / 80);
                y = (int) (imageHeight / 21.95);
            }

            if (x > 7) {
                double proportion = x / 7.00;
                x = 7;
                y = (int) (y / proportion);
            }
            HSSFClientAnchor anchor = new HSSFClientAnchor(112, 112, (int) 112, (int) 112,
                    (short) imageCellAd.getColumn(), imageCellAd.getRow(), (short) (imageCellAd.getColumn() + x/*imageRCellAd.getLastColumn()*/), imageCellAd.getRow() + y/*imageRCellAd.getLastRow()*/);
            anchor.setAnchorType(ClientAnchor.AnchorType.DONT_MOVE_AND_RESIZE);
            //插入图片
            patriarch.createPicture(anchor, wb.addPicture(buffer, HSSFWorkbook.PICTURE_TYPE_JPEG));
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (fis != null) {
                    fis.close();
                }

                if (bos != null) {
                    bos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}