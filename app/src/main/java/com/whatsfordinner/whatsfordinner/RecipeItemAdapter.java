package com.whatsfordinner.whatsfordinner;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by marck on 21.03.15.
 */
public class RecipeItemAdapter extends ArrayAdapter<Recipe> {
    private ArrayList<Recipe> recipes;
    private Context mContext;
    private DisplayImageOptions options;

    public RecipeItemAdapter(Context context, int textViewResourceId, List<Recipe> recipes, DisplayImageOptions displayImageOptions) {
        super(context, textViewResourceId, recipes);

        this.mContext = context;
        this.recipes = new ArrayList<Recipe>(recipes);
        this.options = displayImageOptions;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) { // v should not be null
            LayoutInflater vi = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.listitem, parent, false);
        }

        // fill the particular tweet into the textViews of this list item
        Recipe recipe = recipes.get(position);
        if (recipe != null) {
            TextView tvTitle = (TextView) v.findViewById(R.id.title);
            TextView tvShorttext = (TextView) v.findViewById(R.id.shorttext);
            ImageView tvRecipeImage = (ImageView) v.findViewById(R.id.recipeImage);


            if (tvTitle != null) {
                tvTitle.setText(recipe.getTitle());
            }

            if (tvShorttext != null) {
                tvShorttext.setText(recipe.getLink());
            }

            if (tvRecipeImage != null) {
                ImageLoader.getInstance().displayImage(recipe.getImageSrc(), tvRecipeImage, options);
            }

        }
        return v;
    }
}
