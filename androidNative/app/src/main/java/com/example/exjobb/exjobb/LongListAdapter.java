package com.example.exjobb.exjobb;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewPropertyAnimator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by exjobb on 2017-03-22.
 */

public class LongListAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<Pair<String,String>> data;
    private LayoutInflater inflater = null;


    public LongListAdapter(Context context, ArrayList<Pair<String,String>> data){
        this.context = context;
        this.data = data;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View vi = convertView;
        if(vi == null)
            vi = inflater.inflate(R.layout.long_list_list_item, null);
        TextView firstName = (TextView) vi.findViewById(R.id.longListListItemFirstName);
        TextView lastName = (TextView) vi.findViewById(R.id.longListListItemLastName);
        Button editInformation = (Button) vi.findViewById(R.id.longListListItemEdit);
        Button deleteBtn = (Button) vi.findViewById(R.id.longListListItemDelete);

        firstName.setText(data.get(position).first);
        lastName.setText(data.get(position).second);

        //  Define animation
        //  Slide off full width
        final ObjectAnimator deleteAnimation = ObjectAnimator.ofFloat(vi, "translationX", vi.getWidth());
        deleteAnimation.setDuration(300);
        //  Fade out
        final ObjectAnimator fadeAnimation = ObjectAnimator.ofFloat(vi, "alpha", 0);
        fadeAnimation.setDuration(250);

        editInformation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Edit information
                data.set(position, new Pair<String, String>("new FirstName", "new LastName"));
                notifyDataSetChanged();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  Animate

                deleteAnimation.addListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {
                        fadeAnimation.start();
                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        //  Delete row
                        data.remove(position);
                        notifyDataSetChanged();
                        //  Remove all listeners from the animations and reverse them to reset the view
                        deleteAnimation.removeListener(this);
                        deleteAnimation.setDuration(0);
                        fadeAnimation.removeAllListeners();
                        fadeAnimation.setDuration(0);
                        deleteAnimation.reverse();
                        fadeAnimation.reverse();
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
                //  Start slide off animation

                deleteAnimation.start();

            }
        });


        return vi;
    }
}
