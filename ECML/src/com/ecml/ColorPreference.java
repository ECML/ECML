
package com.ecml;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.preference.Preference;
import android.view.View;

/**
 * @class ColorPreference
 *  The ColorPreference is used in a PreferenceScreen to let
 *  the user choose a color for an option.
 * 
 *  This Preference displays text, plus an additional color box and a Cancel, an Off and an OK Button
 */

public class ColorPreference extends Preference {

	private View colorview; 		/* The view displaying the selected color */
	private int color; 				/* The selected color */
	private Context context;		/* The context */
	private AlertDialog dialog;		/* The Dialog to be used (allows the use of negative, neutral and positive buttons) */
	private ColorView colorView;	/* The colorView that allows us to get the selected Color */


	public ColorPreference(Context ctx) {
		super(ctx);
		context = ctx;
		setWidgetLayoutResource(R.layout.color_preference);
	}

	public void setColor(int value) {
		color = value;
		if (colorview != null) {
			colorview.setBackgroundColor(color);
		}
	}

	public int getColor() {
		return color;
	}


	@Override
	protected void onBindView(View view) {
		super.onBindView(view);

		colorview = (View) view.findViewById(R.id.color_preference_widget);
		if (color != 0) {
			colorview.setBackgroundColor(color);
		}
	}

	/* When clicked, display the color picker dialog */
	protected void onClick() {

		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle("Pick a Color");

		builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface builder, int whichButton) {
			}
		});

		builder.setNeutralButton("Off", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface builder, int which) {
				color = Color.WHITE;
				colorview.setBackgroundColor(color);
			}
		});

		builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface builder, int whichButton) {
				color = colorView.getSelectedColor();
				colorview.setBackgroundColor(color);
			}
		});

		colorView = new ColorView(getContext(), color);
		builder.setView(colorView);

		dialog = builder.create();
		dialog.show();
	}
}
