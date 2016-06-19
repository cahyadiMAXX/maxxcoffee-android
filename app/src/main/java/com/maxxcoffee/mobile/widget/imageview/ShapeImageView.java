package com.maxxcoffee.mobile.widget.imageview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.ImageView;

/* It was used for make rectangle/ circle view in search item in search activity, inbox activity, and profile activity*/
public class ShapeImageView extends ImageView {
	boolean circle = true;
	boolean gray = false;

	public ShapeImageView(Context context) {
		super(context);
	}

	// Constructor for inflating via XML
	public ShapeImageView(Context context, AttributeSet attrs) {
		super(context, attrs);
	}

	@Override
	public void setMinimumHeight(int minHeight) {
		// TODO Auto-generated method stub
		super.setMinimumHeight(getMeasuredWidth());
	}

	//If circle than display it in circle else diplay it as rectangle
	public void setCircle(boolean circle) {
		this.circle = circle;
		invalidate();
	}
	
	//set Gray is for inbox item, where some item will be grayscale if it was read once
	public void setGray(boolean gray){
	   this.gray = gray;	
	}
	
	public boolean isGray(){
	  return (gray);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		// TODO Auto-generated method stub
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);
		getParent().recomputeViewAttributes(this);
	}

	//the main point in this image view, where image view repaint itself in cirlce/ rectangle view
	@Override
	protected void onDraw(Canvas canvas) {

		// super.onDraw(canvas);
		Drawable drawable = getDrawable();
		Bitmap fullSizeBitmap = ((BitmapDrawable) drawable).getBitmap();
		int scaledWidth = getMeasuredWidth();
		int scaledHeight = getMeasuredHeight();
		int Heights = getMeasuredHeight();
		if (scaledWidth < scaledHeight) {
			scaledHeight = scaledWidth;
		}

		Bitmap mScaledBitmap;
		if (fullSizeBitmap != null) {
			if (scaledWidth == fullSizeBitmap.getWidth()
					&& scaledHeight == fullSizeBitmap.getHeight()) {
				mScaledBitmap = fullSizeBitmap;
			} else {
				if (circle) {
					mScaledBitmap = Bitmap.createScaledBitmap(fullSizeBitmap,
							scaledHeight, scaledHeight, true /* filter */);
				} else {
					if (fullSizeBitmap.getHeight() > fullSizeBitmap.getWidth()) {
						float hgh = (fullSizeBitmap.getHeight() * scaledWidth)
								/ fullSizeBitmap.getWidth();
						mScaledBitmap = Bitmap.createScaledBitmap(
								fullSizeBitmap, scaledWidth, Math.round(hgh),
								true /* filter */);
					} else {
						float wdt = (fullSizeBitmap.getWidth() * scaledHeight)
								/ fullSizeBitmap.getHeight();
						mScaledBitmap = Bitmap.createScaledBitmap(
								fullSizeBitmap, Math.round(wdt), scaledHeight,
								true /* filter */);
					}
				}
			}

			int positionX = scaledWidth < Heights ? 0 : scaledWidth / 2
					- Heights / 2;
			int positionY = Heights < scaledWidth ? 0 : 0;

			if (circle) {
				Bitmap roundBitmap = ShapeImageView.getCircleBitmap(
						getContext(), mScaledBitmap, 5, scaledWidth,
						scaledHeight, false, false, false, false, isGray());
				canvas.drawBitmap(roundBitmap, positionX, positionY, null);
			} else {
				Bitmap rectBitmap = ShapeImageView.getRectBitmap(getContext(),
						mScaledBitmap, 5, scaledWidth, scaledHeight, false,
						false, false, false);
				canvas.drawBitmap(rectBitmap, positionX, positionY, null);
			}
		} else {
			super.onDraw(canvas);
		}

	}

	public static Bitmap getRoundedCornerBitmap(Context context, Bitmap input,
			int pixels, int w, int h, boolean squareTL, boolean squareTR,
			boolean squareBL, boolean squareBR) {

		Bitmap output = Bitmap.createBitmap(w, h, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);
		final float densityMultiplier = context.getResources()
				.getDisplayMetrics().density;

		final int color = 0xff424242;
		final Paint paint = new Paint();
		final Rect rect = new Rect(0, 0, w, h);
		final RectF rectF = new RectF(rect);

		// make sure that our rounded corner is scaled appropriately
		final float roundPx = pixels * densityMultiplier;

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);
		canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

		// draw rectangles over the corners we want to be square
		if (squareTL) {
			canvas.drawRect(0, 0, w / 2, h / 2, paint);
		}
		if (squareTR) {
			canvas.drawRect(w / 2, 0, w, h / 2, paint);
		}
		if (squareBL) {
			canvas.drawRect(0, h / 2, w / 2, h, paint);
		}
		if (squareBR) {
			canvas.drawRect(w / 2, h / 2, w, h, paint);
		}

		paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
		canvas.drawBitmap(input, 0, 0, paint);

		return output;
	}

	public static Bitmap getCircleBitmap(Context context, Bitmap input,
			int pixels, int w, int h, boolean squareTL, boolean squareTR,
			boolean squareBL, boolean squareBR, boolean grays) {
		Bitmap bitmap = input;
		Bitmap circleBitmap = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);

		BitmapShader shader = new BitmapShader(bitmap, TileMode.CLAMP,
				TileMode.CLAMP);
		Paint paint = new Paint();
		paint.setShader(shader);
		
		if(grays)
		{
		    ColorMatrix cm = new ColorMatrix();
		    cm.setSaturation(0);
		    paint.setColorFilter(new ColorMatrixColorFilter(cm));
		}
		
		Paint paintbg = new Paint();
		paintbg.setColor(Color.WHITE);
		Canvas c = new Canvas(circleBitmap);
		paint.setAntiAlias(true);
		paintbg.setAntiAlias(true);
		//c.drawCircle(circleBitmap.getWidth() / 2, circleBitmap.getHeight() / 2,
			//	(circleBitmap.getWidth() / 2), paintbg);
		c.drawCircle(bitmap.getWidth() / 2, bitmap.getHeight() / 2,
				(bitmap.getWidth() / 2), paint);
		return (circleBitmap);
	}

	public static Bitmap getRectBitmap(Context context, Bitmap input,
			int pixels, int w, int h, boolean squareTL, boolean squareTR,
			boolean squareBL, boolean squareBR) {
		Bitmap bitmap = input;
		Bitmap rectBitmap = Bitmap.createBitmap(bitmap.getWidth(),
				bitmap.getHeight(), Config.ARGB_8888);

		BitmapShader shader = new BitmapShader(bitmap, TileMode.CLAMP,
				TileMode.CLAMP);
		Paint paint = new Paint();
		paint.setShader(shader);
		Paint paintbg = new Paint();
		paintbg.setColor(Color.WHITE);
		Canvas c = new Canvas(rectBitmap);
		paint.setAntiAlias(true);
		paintbg.setAntiAlias(true);
		int size = rectBitmap.getWidth();
		if (rectBitmap.getWidth() > rectBitmap.getHeight()) {
			size = rectBitmap.getHeight();
		}

		Rect bg = new Rect(0, 0, size, size);
		Rect img = new Rect(5, 5, size - 5, size - 5);
		c.drawRect(bg, paintbg);
		c.drawRect(img, paint);
		return (rectBitmap);
	}

	
	
	
}