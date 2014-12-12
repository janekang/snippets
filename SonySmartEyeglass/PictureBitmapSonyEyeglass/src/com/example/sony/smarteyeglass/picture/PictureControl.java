/*
Copyright (c) 2011, Sony Mobile Communications Inc.
Copyright (c) 2014, Sony Corporation

 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions are met:

 * Redistributions of source code must retain the above copyright notice, this
 list of conditions and the following disclaimer.

 * Redistributions in binary form must reproduce the above copyright notice,
 this list of conditions and the following disclaimer in the documentation
 and/or other materials provided with the distribution.

 * Neither the name of the Sony Mobile Communications Inc.
 nor the names of its contributors may be used to endorse or promote
 products derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS" AND
 ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED
 WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE
 DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE LIABLE
 FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.example.sony.smarteyeglass.picture;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.SparseArray;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.TextView;

import com.example.sony.smarteyeglass.picture.R;
import com.sony.smarteyeglass.extension.util.SmartEyeglassControlUtils;
import com.sonyericsson.extras.liveware.aef.control.Control;
import com.sonyericsson.extras.liveware.extension.util.ExtensionUtils;
import com.sonyericsson.extras.liveware.extension.util.control.ControlExtension;
import com.sonyericsson.extras.liveware.extension.util.control.ControlTouchEvent;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * This demonstrates two different approaches for displaying and updating a UI: 
 * Using bitmap and using layout. The bitmap approach is useful for accessories
 * without layout support e.g. SmartWatch, or for cases, when you want to
 * display special custom UI elements.
 * This sample shows all standard UI components that can be used on
 * SmartEyeglass, except Gallery.
 */
public final class PictureControl extends ControlExtension {

    /** The quality parameter for encoding PNG data. */
    private static final int PNG_QUALITY = 100;

    /** The size of initial buffer for encoding PNG data. */
    private static final int PNG_DEFAULT_CAPACITY = 256;

    /** The renderer using the bitmap approach. */
    private final Renderer bitmapRenderer = new Renderer() {
        @Override
        public void init() {
            updateBitmap();
        }

        @Override
        public void update() {
            updateBitmap();
        }

        @Override
        public String toString() {
            return "BitmapRenderer";
        }
    };

    /** Instance of the Control Utility class. */
    private final SmartEyeglassControlUtils utils;

    /** The application context. */
    private final Context context;

    /** The state of the application. */
    private final State state;

    /** Contains the chosen UI to render, e.g. layout or bitmap. */
    private Renderer renderer;
    
    private int index;
    private int cardCount;

    /**
     * Creates control extension.
     *
     * @param context The context.
     * @param hostAppPackageName Package name of host application.
     */
    public PictureControl(final Context context,
            final String hostAppPackageName) {
        super(context, hostAppPackageName);
        this.context = context;
        utils = new SmartEyeglassControlUtils(hostAppPackageName, null);
        utils.activate(context);
        
        ArrayList<Integer> imageResourceIds = new ArrayList<Integer>();
        imageResourceIds.add(R.drawable.abyss);
        imageResourceIds.add(R.drawable.black_swan_poster);
        imageResourceIds.add(R.drawable.despair_rose);
        cardCount = imageResourceIds.size();
        
        state = new State(imageResourceIds);
    }

    @Override
    public void onStart() {
        renderer = bitmapRenderer;
        index = 0;
    }

    @Override
    public void onResume() {
        // Send a UI when the extension becomes visible.
        renderer.init();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        Log.d(Constants.LOG_TAG, "onDestroy: PictureControl");
        utils.deactivate();
    };

    @Override
    public void onTouch(final ControlTouchEvent event) {
        super.onTouch(event);

        if (event.getAction() != Control.Intents.TOUCH_ACTION_RELEASE) {
            return;
        }
    }

    @Override
    public void onSwipe(final int direction) {
        if (direction == Control.Intents.SWIPE_DIRECTION_LEFT) {
        	if (index < cardCount-1) {
        		index++;
            	renderer.update();
            }
        }
        else {
        	if (index > 0) {
        		index--;
            	renderer.update();
            }
        }
    }

    /**
     * When using a bitmap as a UI, there is no way to update just a view
     * or a part of the layout, you need to send the whole bitmap. It is
     * necessary to inflate the layout and change the data to be displayed in
     * the view before sending the bitmap.
     */
    private void updateBitmap() {
        RelativeLayout root = new RelativeLayout(context);
        root.setLayoutParams(new LayoutParams(
                R.dimen.smarteyeglass_control_width,
                R.dimen.smarteyeglass_control_height));

        // Sets dimensions and properties of the bitmap to use when rendering
        // the UI.
        final ScreenSize size = new ScreenSize(context);
        final int width = size.getWidth();
        final int height = size.getHeight();
        Bitmap bitmap =
                Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        bitmap.compress(CompressFormat.PNG, PNG_QUALITY,
                new ByteArrayOutputStream(PNG_DEFAULT_CAPACITY));
        bitmap.setDensity(DisplayMetrics.DENSITY_DEFAULT);

        // Inflates an existing layout to use as a base.
        RelativeLayout layout = (RelativeLayout) RelativeLayout.inflate(context,
                R.layout.bitmap, root);
        // Sets dimensions of the layout to use in the UI. We use the same
        // dimensions as the bitmap.
        layout.measure(height, width);
        layout.layout(0, 0, layout.getMeasuredWidth(),
                layout.getMeasuredHeight());

        // Updates the index
        TextView textView = (TextView) layout.findViewById(R.id.btn_update_this);
        textView.setText(getCaption(index));

        // Determines what image to add to the layout.
        ImageView imageView = (ImageView) layout.findViewById(R.id.image);
        imageView.setImageResource(state.getImageId(index));

        // Converts the layout to a bitmap using the canvas.
        Canvas canvas = new Canvas(bitmap);
        layout.draw(canvas);
        utils.showBitmap(bitmap);
    }

    /**
     * Returns the string to display with TextView.
     *
     * @param count The value to display.
     * @return The string to display with TextView.
     */
    private String getCaption(final int count) {
        return context.getString(R.string.text_picture_index) + count;
    }

    /**
     * Returns the URI string corresponding to the specified resource ID.
     *
     * @param id
     *            The resource ID.
     * @return The URI string.
     */
    private String getUriString(final int id) {
        return ExtensionUtils.getUriString(context, id);
    }
}
