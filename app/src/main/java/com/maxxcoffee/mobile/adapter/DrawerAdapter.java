package com.maxxcoffee.mobile.adapter;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.model.ChildDrawerModel;
import com.maxxcoffee.mobile.model.ParentDrawerModel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Rio Swarawan on 5/20/2016.
 */
public class DrawerAdapter extends BaseExpandableListAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<ParentDrawerModel> parentDat;
    private HashMap<ParentDrawerModel, List<ChildDrawerModel>> childData;

    public DrawerAdapter(Context context, List<ParentDrawerModel> listDataHeader, HashMap<ParentDrawerModel, List<ChildDrawerModel>> listChildData) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.parentDat = listDataHeader;
        this.childData = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.childData.get(this.parentDat.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final ChildDrawerModel childModel = (ChildDrawerModel) getChild(groupPosition, childPosition);
        final ParentDrawerModel parentModel = parentDat.get(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_drawer_child, null);
        }

        TextView item = (TextView) convertView.findViewById(R.id.item);
        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.layout);

        item.setText(childModel.getName());
//        layout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                onChildSelected(parentModel, childPosition);
//            }
//        });
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        List<ChildDrawerModel> childs = this.childData.get(this.parentDat.get(groupPosition));
        if (childs != null) {
            return childs.size();
        } else {
            return 0;
        }
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.parentDat.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.parentDat.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final ParentDrawerModel parentModel = (ParentDrawerModel) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_drawer_parent, null);
        }

        TextView item = (TextView) convertView.findViewById(R.id.item);
        ImageView icon = (ImageView) convertView.findViewById(R.id.icon);
        ImageView arrow = (ImageView) convertView.findViewById(R.id.arrow);
        RelativeLayout layout = (RelativeLayout) convertView.findViewById(R.id.layout);

        item.setText(parentModel.getName());
        arrow.setVisibility(parentModel.isExpandable() ? View.VISIBLE : View.GONE);

        if (parentModel.getIcon() != null) {
            icon.setImageDrawable(context.getResources().getDrawable(parentModel.getIcon()));
        }

//        if (!parentModel.isExpandable()) {
//            layout.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    onParentSelected(parentModel);
//                }
//            });
//        }
        return convertView;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
//
//    protected abstract void onParentSelected(ParentDrawerModel parent);
//
//    protected abstract void onChildSelected(ParentDrawerModel parent, Integer position);
}
