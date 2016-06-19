package com.maxxcoffee.mobile.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.maxxcoffee.mobile.R;
import com.maxxcoffee.mobile.model.MenuCategoryModel;
import com.maxxcoffee.mobile.model.MenuItemModel;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Rio Swarawan on 5/20/2016.
 */
public abstract class MenuAdapter extends BaseExpandableListAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<MenuCategoryModel> parentData;
    private HashMap<MenuCategoryModel, List<List<MenuItemModel>>> childData;

    public MenuAdapter(Context context, List<MenuCategoryModel> listDataHeader, HashMap<MenuCategoryModel, List<List<MenuItemModel>>> listChildData) {
        this.context = context;
        this.inflater = LayoutInflater.from(context);
        this.parentData = listDataHeader;
        this.childData = listChildData;
    }

    @Override
    public Object getChild(int groupPosition, int childPosititon) {
        return this.childData.get(this.parentData.get(groupPosition)).get(childPosititon);
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public View getChildView(final int groupPosition, final int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        final List<MenuItemModel> childModel = (List<MenuItemModel>) getChild(groupPosition, childPosition);
        final MenuCategoryModel parentModel = parentData.get(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_menu, null);
        }

        TextView titleLeft = (TextView) convertView.findViewById(R.id.title_left);
        TextView pointLeft = (TextView) convertView.findViewById(R.id.point_left);
        ImageView imageLeft = (ImageView) convertView.findViewById(R.id.image_left);
        CardView layoutLeft = (CardView) convertView.findViewById(R.id.card_view_left);

        TextView titleRight = (TextView) convertView.findViewById(R.id.title_right);
        TextView pointRight = (TextView) convertView.findViewById(R.id.point_right);
        ImageView imageRight = (ImageView) convertView.findViewById(R.id.image_right);
        CardView layoutRight = (CardView) convertView.findViewById(R.id.card_view_right);

        layoutRight.setVisibility(childModel.size() == 2 ? View.VISIBLE : View.GONE);

        for (int i = 0; i < childModel.size(); i++) {
            final MenuItemModel model = childModel.get(i);
            if (i % 2 == 0) {
                titleLeft.setText(model.getName());
                pointLeft.setText(String.valueOf(model.getPoint()));
                Glide.with(context).load("").placeholder(model.getImage()).crossFade().into(imageLeft);

                layoutLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onRightMenuClick(model);
                    }
                });
            } else {
                titleRight.setText(model.getName());
                pointRight.setText(String.valueOf(model.getPoint()));
                Glide.with(context).load("").placeholder(model.getImage()).crossFade().into(imageRight);

                layoutRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onLeftMenuClick(model);
                    }
                });
            }
        }
        return convertView;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return this.childData.get(this.parentData.get(groupPosition)).size();
    }

    @Override
    public Object getGroup(int groupPosition) {
        return this.parentData.get(groupPosition);
    }

    @Override
    public int getGroupCount() {
        return this.parentData.size();
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        final MenuCategoryModel parentModel = (MenuCategoryModel) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_category, null);
        }

        TextView category = (TextView) convertView.findViewById(R.id.category);
        category.setText(parentModel.getName());

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

    protected abstract void onRightMenuClick(MenuItemModel parent);

    protected abstract void onLeftMenuClick(MenuItemModel parent);
}
