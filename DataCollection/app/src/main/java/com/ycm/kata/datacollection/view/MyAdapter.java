package com.ycm.kata.datacollection.view;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.ycm.kata.datacollection.R;
import com.ycm.kata.datacollection.model.entity.ProjectEntity;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

/**
 * Created by changmuyu on 2017/9/19.
 * Description:
 */

public class MyAdapter extends BaseAdapter {
    private Context context;
    private List<ProjectEntity> dataSource;
    private static final int VIEW_COUNT = 6;
    private int index = 0;
    private OnItemClickListener onItemClickListener;
    MyAdapter(Context context, OnItemClickListener onItemClickListener) {
        this.context = context;
        this.onItemClickListener = onItemClickListener;
    }
    public List<ProjectEntity> getDataSource() {
        return dataSource;
    }

    public void setDataSource(List<ProjectEntity> dataSource) {
        this.dataSource = dataSource;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    @Override
    public int getCount() {
        if (dataSource == null) {
            return 0;
        }
        // ori表示到目前为止的前几页的总共的个数。
        int ori = VIEW_COUNT * index;

        // 值的总个数-前几页的个数就是这一页要显示的个数，如果比默认的值小，说明这是最后一页，只需显示这么多就可以了
        if (dataSource.size() - ori < VIEW_COUNT) {
            return dataSource.size() - ori;
        }
        // 如果比默认的值还要大，说明一页显示不完，还要用换一页显示，这一页用默认的值显示满就可以了。
        else {
            return VIEW_COUNT;
        }
//        return dataSource == null ? 0 : dataSource.size();
    }

    @Override
    public Object getItem(int i) {

        return dataSource == null ? null : dataSource.get(i + index * VIEW_COUNT);
    }

    @Override
    public long getItemId(int i) {
        return dataSource == null ? 0 : i + index * VIEW_COUNT;
    }

    @Override
    public View getView(final int i, View contentView, ViewGroup viewGroup) {
        HolderView holderView;

        if (contentView == null) {
            contentView = LayoutInflater.from(context).inflate(R.layout.data_item, null);
            holderView = new HolderView(contentView);
        } else {
            holderView = (HolderView) contentView.getTag();

        }
        ProjectEntity projectEntity = dataSource.get(i + index * VIEW_COUNT);
        holderView.getTextView().setText(formatDate(projectEntity.getCheckDate())
                + " " + projectEntity.getId() + " " + projectEntity.getProjectName());
        holderView.getTextView().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.textOnclickListener(i + index * VIEW_COUNT);
            }
        });
        holderView.getButton().setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClickListener.buttonOnclickListener(i);
            }
        });
        contentView.setTag(holderView);
        return contentView;
    }

    private String formatDate(long time) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd", Locale.CHINA);
        Date d1 = new Date(time);
        return format.format(d1);
    }

    private class HolderView {
        private TextView textView;
        private Button button;

        HolderView(View view) {
            textView = view.findViewById(R.id.text);
            button = view.findViewById(R.id.delete);
        }

        public TextView getTextView() {
            return textView;
        }

        public void setTextView(TextView textView) {
            this.textView = textView;
        }

        public Button getButton() {
            return button;
        }

        public void setButton(Button button) {
            this.button = button;
        }
    }
}
