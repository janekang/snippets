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
package com.example.sony.smarteyeglass.helloworld;

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

import com.example.sony.smarteyeglass.helloworld.R;
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
public final class HelloWorldControl extends ControlExtension {

    /** The quality parameter for encoding PNG data. */
    private static final int PNG_QUALITY = 100;

    /** The size of initial buffer for encoding PNG data. */
    private static final int PNG_DEFAULT_CAPACITY = 256;

    /** The renderer using the layout approach. */
    private final Renderer layoutRenderer = new Renderer() {
        @Override
        public void init() {
            updateLayout();
        }

        @Override
        public void update() {
            int count = state.getCount();

            // The sendText method is used to update the text of a single view
            // instead of updating the entire layout.
            sendText(R.id.btn_update_this, getCaption(count));

            // The sendImage method is used to update an image of a single
            // ImageView.
            sendImage(R.id.image, state.getImageId());
        }

        @Override
        public String toString() {
            return "LAYOUT";
        }
    };

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
            return "BITMAP";
        }
    };

    /** The map from the swipe direction to the renderer. */
    private final SparseArray<Renderer> rendererMap =
            new SparseArray<Renderer>();

    /** Instance of the Control Utility class. */
    private final SmartEyeglassControlUtils utils;

    /** The application context. */
    private final Context context;

    /** The state of the application. */
    private final State state;

    /** Contains the chosen UI to render, e.g. layout or bitmap. */
    private Renderer renderer;

    /**
     * Creates control extension.
     *
     * @param context The context.
     * @param hostAppPackageName Package name of host application.
     */
    public HelloWorldControl(final Context context,
            final String hostAppPackageName) {
        super(context, hostAppPackageName);
        this.context = context;
        utils = new SmartEyeglassControlUtils(hostAppPackageName, null);
        utils.activate(context);
        state = new State();
        rendererMap.put(Control.Intents.SWIPE_DIRECTION_LEFT, bitmapRenderer);
        rendererMap.put(Control.Intents.SWIPE_DIRECTION_RIGHT, layoutRenderer);
    }

    @Override
    public void onStart() {
        state.reset();
        renderer = layoutRenderer;
    }

    @Override
    public void onResume() {
        // Send a UI when the extension becomes visible.
        renderer.init();
        super.onResume();
    }

    @Override
    public void onDestroy() {
        Log.d(Constants.LOG_TAG, "onDestroy: HelloWorldControl");
        utils.deactivate();
    };

    @Override
    public void onTouch(final ControlTouchEvent event) {
        super.onTouch(event);

        Log.d(Constants.LOG_TAG,
                "onTouch: HelloWorldControl " + renderer
                + " - " + event.getX()
                + ", " + event.getY());

        if (event.getAction() != Control.Intents.TOUCH_ACTION_RELEASE) {
            return;
        }

        // The touch method can be used with both layouts and bitmaps. It is
        // necessary together with bitmaps because there is no reference to a
        // clicked view. When using bitmaps it is necessary to check if the
        // touch area is inside the view area.
        state.update();
        renderer.update();
    }

    @Override
    public void onSwipe(final int direction) {
        Renderer next = rendererMap.get(direction);
        if (next == null) {
            return;
        }
        renderer = next;
        state.reset();
        renderer.init();
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

        // Updates the counter value.
        int count = state.getCount();
        TextView textView = (TextView) layout.findViewById(R.id.btn_update_this);
        textView.setText(getCaption(count));

        // Determines what icon to add to the layout.
        ImageView imageView = (ImageView) layout.findViewById(R.id.image);
        imageView.setImageResource(state.getImageId());

        // Converts the layout to a bitmap using the canvas.
        Canvas canvas = new Canvas(bitmap);
        layout.draw(canvas);
        utils.showBitmap(bitmap);
    }

    /**
     * This is an example of how to update the entire layout and some of the
     * views. For each view, a bundle is used. This bundle must have the layout
     * reference, i.e. the view ID and the content to be used. This method
     * updates an ImageView and a TextView.
     * 
     * Note that when you are using the layout approach for displaying TextView
     * elements, they will be rendered using optimized SST font.
     *
     * @see Control.Intents#EXTRA_DATA_XML_LAYOUT
     * @see Registration.LayoutSupport
     */
    private void updateLayout() {
        List<Bundle> list = new ArrayList<Bundle>();

        // Prepare a bundle to update the button text.
        Bundle textBundle = new Bundle();
        textBundle.putInt(Control.Intents.EXTRA_LAYOUT_REFERENCE,
                R.id.btn_update_this);
        textBundle.putString(Control.Intents.EXTRA_TEXT, getCaption(state.getCount()));
        list.add(textBundle);

        // Prepare a bundle to update the ImageView image.
        Bundle imageBundle = new Bundle();
        imageBundle.putInt(Control.Intents.EXTRA_LAYOUT_REFERENCE, R.id.image);
        imageBundle.putString(Control.Intents.EXTRA_DATA_URI,
                getUriString(R.drawable.icon_extension48));
        list.add(imageBundle);

        showLayout(R.layout.layout, list.toArray(new Bundle[list.size()]));
    }

    /**
     * Returns the string to display with TextView.
     *
     * @param count The value to display.
     * @return The string to display with TextView.
     */
    private String getCaption(final int count) {
        return context.getString(R.string.text_tap_count) + count;
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
