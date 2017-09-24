package com.ycm.kata.datacollection.view;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.ycm.kata.datacollection.MyApplication;
import com.ycm.kata.datacollection.R;
import com.ycm.kata.datacollection.model.ProjectEntityDao;
import com.ycm.kata.datacollection.model.entity.ProjectEntity;
import com.ycm.kata.datacollection.utils.CommonUtils;


import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFClientAnchor;
import org.apache.poi.hssf.usermodel.HSSFPatriarch;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.util.CellAddress;
import org.apache.poi.ss.util.CellRangeAddress;

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

public class DataListActivity extends Activity implements GetDataListener, OnItemClickListener, View.OnClickListener, ExportListener {
    private Button btnAdd;
    private Button btnExport;
    private Button btnPrevious;
    private Button btnNext;
    private ListView listView;
    private MyAdapter myAdapter;
    private static List<ProjectEntity> dataSource;
    private ProjectEntityDao projectEntityDao;
    private ProjectEntity projectEntity;
    private GetData getData;
    private int index = 0;
    private static final int VIEW_COUNT = 6;
    private UpdateViewHandler handler;
    private static final int MY_PERMISSIONS_REQUEST = 1;
    private String imageFile;
    private String decFile;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data_list);
        btnAdd = findViewById(R.id.add_new_btn);
        btnAdd.setOnClickListener(this);
        btnExport = findViewById(R.id.export_btn);
        btnExport.setOnClickListener(this);
        btnPrevious = findViewById(R.id.previous_btn);
        btnPrevious.setOnClickListener(this);
        btnNext = findViewById(R.id.next_btn);
        btnNext.setOnClickListener(this);
        listView = findViewById(R.id.list);
        dataSource = new ArrayList<>();
        handler = new UpdateViewHandler(this);
        projectEntityDao = MyApplication.getInstances().getDaoSession().getProjectEntityDao();
        myAdapter = new MyAdapter(this, this);
        listView.setAdapter(myAdapter);
        View empty = LayoutInflater.from(this).inflate(R.layout.empy_layout, null);
        listView.setEmptyView(empty);
    }

    @Override
    public void loadSuccess(List<ProjectEntity> dataSource) {
        handler.sendEmptyMessage(123);
    }

    @Override
    public int progress(int x) {
        return 0;
    }

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
    public void buttonOnclickListener(int position) {
        deleteById(position);
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
            if (dataSource != null && dataSource.size() != 0) {
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
        getDataSource();
    }

    // 点击左边的Button，表示向前翻页，索引值要减1.
    public void preView() {
        if (index <= 0) {
            Toast.makeText(getBaseContext(), "已经是第一页啦", Toast.LENGTH_SHORT).show();
            return;
        }
        Log.d("tagggg1", index + " ");
        myAdapter.setIndex(--index);
        // 刷新ListView里面的数值。
        myAdapter.notifyDataSetChanged();
        // 检查Button是否可用。
//        checkButton();
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
        Log.d("tagggg", index + " ");
        myAdapter.setIndex(++index);
        // 刷新ListView里面的数值。
        myAdapter.notifyDataSetChanged();
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

    private void writeExcel(List<ProjectEntity> dataSource, ExportListener exportListener) {
        List<ProjectEntity> currentPs = new ArrayList<>();
        currentPs.addAll(dataSource/*filterDataSource(dataSource)*/);
        FileOutputStream fileOut = null;
        String rootPath = CommonUtils.getDataFilePath(System.currentTimeMillis());
        if (rootPath == null) {
            return;
        }
        HSSFWorkbook wb = new HSSFWorkbook();
        HSSFSheet sheet1 = wb.createSheet("项目数据" + CommonUtils.formatDate(System.currentTimeMillis()));
        String p = CommonUtils.getDataFilePath(System.currentTimeMillis());
        if (p == null) {
            return;
        }
        try {
            fileOut = new FileOutputStream(p);
            int lastRow = 0;
            for (int i = 0; i < currentPs.size(); i++) {
                if (lastRow == 0) {
                    lastRow += 1;
                } else {
                    lastRow += 5;
                }

                //创建第一行
                HSSFRow rowName = sheet1.createRow(lastRow);
                //第1列
                HSSFCell cellNameTitle = rowName.createCell(0);
                CellAddress cellNameTitleAddress = cellNameTitle.getAddress();
                CellRangeAddress newCellTitleNameAd = new CellRangeAddress(cellNameTitleAddress.getRow(), cellNameTitleAddress.getRow() + 1 + 1, cellNameTitleAddress.getColumn(), cellNameTitleAddress.getColumn() + 1);
                sheet1.addMergedRegion(newCellTitleNameAd);
                cellNameTitle.setCellValue("项目名称");

                //第2列
                HSSFCell cellName = rowName.createCell(newCellTitleNameAd.getLastColumn() + 1);
                CellAddress cellNameAddress = cellName.getAddress();
                CellRangeAddress newCellNameAd = new CellRangeAddress(cellNameAddress.getRow(), cellNameAddress.getRow() + 1 + 1, cellNameAddress.getColumn(), cellNameAddress.getColumn() + 2 + 1);
                sheet1.addMergedRegion(newCellNameAd);
                cellName.setCellValue(currentPs.get(i).getProjectName());

                //第3列
                HSSFCell cellCheckDateTitle = rowName.createCell(newCellNameAd.getLastColumn() + 1);
                CellAddress checkDateTitleAddress = cellCheckDateTitle.getAddress();
                CellRangeAddress newCellTitleCheckDateAd = new CellRangeAddress(checkDateTitleAddress.getRow(), checkDateTitleAddress.getRow() + 1 + 1, checkDateTitleAddress.getColumn(), checkDateTitleAddress.getColumn() + 1);
                sheet1.addMergedRegion(newCellTitleCheckDateAd);
                cellCheckDateTitle.setCellValue("检测日期");

                //第4列
                HSSFCell checkDate = rowName.createCell(newCellTitleCheckDateAd.getLastColumn() + 1);
                CellAddress checkDateAddress = checkDate.getAddress();
                CellRangeAddress newCellCheckDateAd = new CellRangeAddress(checkDateAddress.getRow(), checkDateAddress.getRow() + 1 + 1, checkDateAddress.getColumn(), checkDateAddress.getColumn() + 2);
                sheet1.addMergedRegion(newCellCheckDateAd);
                checkDate.setCellValue(CommonUtils.formatDate(currentPs.get(i).getCheckDate()));

                //创建第二行
                HSSFRow rowUnitEngineer = sheet1.createRow(newCellCheckDateAd.getLastRow() + 1);

                //第1列
                HSSFCell cellUnitEngineerTitle = rowUnitEngineer.createCell(0);
                CellAddress unitEngineerTitleAddress = cellUnitEngineerTitle.getAddress();
                CellRangeAddress UnitEngineerTitleCheckDateAd = new CellRangeAddress(unitEngineerTitleAddress.getRow(), unitEngineerTitleAddress.getRow() + 2 + 1, unitEngineerTitleAddress.getColumn(), unitEngineerTitleAddress.getColumn() + 1);
                sheet1.addMergedRegion(UnitEngineerTitleCheckDateAd);
                cellUnitEngineerTitle.setCellValue("单位工程");

                //第2列
                HSSFCell cellUnitEngineer = rowUnitEngineer.createCell(UnitEngineerTitleCheckDateAd.getLastColumn() + 1);
                CellAddress cellUnitEngineerAddress = cellUnitEngineer.getAddress();
                CellRangeAddress newCellUnitEngineerAd = new CellRangeAddress(cellUnitEngineerAddress.getRow(), cellUnitEngineerAddress.getRow() + 2 + 1, cellUnitEngineerAddress.getColumn(), cellUnitEngineerAddress.getColumn() + 1);
                sheet1.addMergedRegion(newCellUnitEngineerAd);
                cellUnitEngineer.setCellValue(currentPs.get(i).getUnitEngineering());

                //第3列
                HSSFCell cellBlockPileTitle = rowUnitEngineer.createCell(newCellUnitEngineerAd.getLastColumn() + 1);
                CellAddress blockPileTitleAddress = cellBlockPileTitle.getAddress();
                CellRangeAddress newBlockPileTitleAddress = new CellRangeAddress(blockPileTitleAddress.getRow(), blockPileTitleAddress.getRow() + 2 + 1, blockPileTitleAddress.getColumn(), blockPileTitleAddress.getColumn() + 1);
                sheet1.addMergedRegion(newBlockPileTitleAddress);
                cellBlockPileTitle.setCellValue("标段及桩号");

                //第4列
                HSSFCell cellBlockPile = rowUnitEngineer.createCell(newBlockPileTitleAddress.getLastColumn() + 1);
                CellAddress cellBlockPileAddress = cellBlockPile.getAddress();
                CellRangeAddress newCellBlockPileAd = new CellRangeAddress(cellBlockPileAddress.getRow(), cellBlockPileAddress.getRow() + 2 + 1, cellBlockPileAddress.getColumn(), cellBlockPileAddress.getColumn() + 1);
                sheet1.addMergedRegion(newCellBlockPileAd);
                cellBlockPile.setCellValue(currentPs.get(i).getBlock() + " " + dataSource.get(i).getPilNo());

                //第5列
                HSSFCell cellDefectsTitle = rowUnitEngineer.createCell(newCellBlockPileAd.getLastColumn() + 1);
                CellAddress defectsAddress = cellDefectsTitle.getAddress();
                CellRangeAddress newDefectsTitleAddress = new CellRangeAddress(defectsAddress.getRow(), defectsAddress.getRow() + 2 + 1, defectsAddress.getColumn(), defectsAddress.getColumn() + 2);
                sheet1.addMergedRegion(newDefectsTitleAddress);
                cellDefectsTitle.setCellValue("缺陷描述");

                //创建第三行
                HSSFRow rowImage = sheet1.createRow(UnitEngineerTitleCheckDateAd.getLastRow() + 1);

                //第1列
                HSSFCell cellImage = rowImage.createCell(0);
                CellAddress imageAddress = cellImage.getAddress();
                CellRangeAddress imageAd = new CellRangeAddress(imageAddress.getRow(), imageAddress.getRow() + 10 + 10 + 11, imageAddress.getColumn(), imageAddress.getColumn() + 4 + 3);
                sheet1.addMergedRegion(imageAd);
                //插入图片
                insertImage(sheet1, currentPs.get(i).getImagePath(), imageAddress, imageAd, wb);

                //第2列
                HSSFCell cellDefects = rowImage.createCell(imageAd.getLastColumn() + 1);
                CellAddress cellDefectsAddress = cellDefects.getAddress();
                CellRangeAddress newCellDefectsAd = new CellRangeAddress(cellDefectsAddress.getRow(), cellDefectsAddress.getRow() + 10 + 10 + 11, cellDefectsAddress.getColumn(), cellDefectsAddress.getColumn() + 2);
                sheet1.addMergedRegion(newCellDefectsAd);
                cellDefects.setCellValue(currentPs.get(i).getDefects());

                //创建第四行
                HSSFRow rowRemark = sheet1.createRow(imageAd.getLastRow() + 1);

                //第1列
                HSSFCell cellRemarkTitle = rowRemark.createCell(0);
                CellAddress remarkTitleAddress = cellRemarkTitle.getAddress();
                CellRangeAddress remarkTitleAd = new CellRangeAddress(remarkTitleAddress.getRow(), remarkTitleAddress.getRow() + 1 + 1, remarkTitleAddress.getColumn(), remarkTitleAddress.getColumn() + 1);
                sheet1.addMergedRegion(remarkTitleAd);
                cellRemarkTitle.setCellValue("备注");

                //第2列
                HSSFCell cellRemark = rowRemark.createCell(remarkTitleAd.getLastColumn() + 1);
                CellAddress cellRemarkAddress = cellRemark.getAddress();
                CellRangeAddress newCellRemarkAd = new CellRangeAddress(cellRemarkAddress.getRow(), cellRemarkAddress.getRow() + 1 + 1, cellRemarkAddress.getColumn(), cellRemarkAddress.getColumn() + 6 + 2);
                sheet1.addMergedRegion(newCellRemarkAd);
                cellRemark.setCellValue(currentPs.get(i).getRemark());
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
            BitmapFactory.Options options = new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(imagePath, options); // 此时返回的bitmap为null
            double imageWidth = options.outWidth;
            double imageHeight = options.outHeight;

            double zoomIndex;
            if (imageHeight > 255) {
                zoomIndex = imageHeight / 255;
                imageHeight /= zoomIndex;
                imageWidth /= zoomIndex;
            }
            int columnWidth = sheet.getDefaultColumnWidth();// 8
            int rowHeight = sheet.getDefaultRowHeight();//255
            int columNum = imageRCellAd.getLastColumn() - imageCellAd.getColumn();
            int rowNum /*= imageRCellAd.getLastRow() - imageCellAd.getRow()*/;

            rowNum = 31 /*(columnWidth * columNum) / rowHeight*/;

            if (imageHeight > 255) {
                imageHeight = 255;
            } else if (imageHeight <= 0) {
                return;
            }
            HSSFClientAnchor anchor =
                    new HSSFClientAnchor(0, 0, (int) imageWidth, (int) imageHeight, (short) imageCellAd.getColumn(), imageCellAd.getRow(), (short) (imageRCellAd.getLastColumn()), imageRCellAd.getLastRow());

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