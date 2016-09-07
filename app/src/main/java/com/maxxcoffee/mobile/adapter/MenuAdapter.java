package com.maxxcoffee.mobile.adapter;

import android.animation.ObjectAnimator;
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
import com.maxxcoffee.mobile.database.entity.MenuCategoryEntity;
import com.maxxcoffee.mobile.database.entity.MenuEntity;

import java.util.HashMap;
import java.util.List;

/**
 * Created by Rio Swarawan on 5/20/2016.
 */
public abstract class MenuAdapter extends BaseExpandableListAdapter {

    private Context context;
    private LayoutInflater inflater;
    private List<MenuCategoryEntity> parentData;
    private HashMap<MenuCategoryEntity, List<List<MenuEntity>>> childData;

    public MenuAdapter(Context context, List<MenuCategoryEntity> listDataHeader, HashMap<MenuCategoryEntity, List<List<MenuEntity>>> listChildData) {
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
        final List<MenuEntity> childModel = (List<MenuEntity>) getChild(groupPosition, childPosition);
        final MenuCategoryEntity parentModel = parentData.get(groupPosition);

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
            final MenuEntity model = childModel.get(i);
            if (i % 2 == 0) {
                titleLeft.setText(model.getName());
//                pointLeft.setText(String.valueOf(model.getPoint()));
                Glide.with(context).load(model.getImage()).placeholder(context.getResources().getDrawable(R.drawable.ic_no_image)).centerCrop().crossFade().into(imageLeft);

                layoutLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        onRightMenuClick(model);
                    }
                });
            } else {
                titleRight.setText(model.getName());
//                pointRight.setText(String.valueOf(model.getPoint()));
                Glide.with(context).load(model.getImage()).placeholder(context.getResources().getDrawable(R.drawable.ic_no_image)).centerCrop().crossFade().into(imageRight);

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
        final MenuCategoryEntity parentModel = (MenuCategoryEntity) getGroup(groupPosition);

        if (convertView == null) {
            LayoutInflater infalInflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infalInflater.inflate(R.layout.item_category, null);
        }

        if(isExpanded){
            ImageView containerIndicator = (ImageView) convertView.findViewById(R.id.arrow);
            ObjectAnimator animator =   ObjectAnimator.ofFloat(containerIndicator,"rotation",0,-180f);
            animator.setDuration(200);
            animator.start();
        } else{
            ImageView containerIndicator = (ImageView) convertView.findViewById(R.id.arrow);
            ObjectAnimator animator =   ObjectAnimator.ofFloat(containerIndicator,"rotation",0,0f);
            animator.setDuration(200);
            animator.start();
        }

        TextView category = (TextView) convertView.findViewById(R.id.category);
        category.setText(parentModel.getCategory());

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

    protected abstract void onRightMenuClick(MenuEntity parent);

    protected abstract void onLeftMenuClick(MenuEntity parent);
}
