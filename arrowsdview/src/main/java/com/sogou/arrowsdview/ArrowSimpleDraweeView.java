package com.sogou.arrowsdview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.ViewGroup;

import com.facebook.common.references.CloseableReference;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.facebook.drawee.interfaces.DraweeController;
import com.facebook.drawee.view.SimpleDraweeView;
import com.facebook.imagepipeline.bitmaps.PlatformBitmapFactory;
import com.facebook.imagepipeline.common.ResizeOptions;
import com.facebook.imagepipeline.request.BasePostprocessor;
import com.facebook.imagepipeline.request.ImageRequest;
import com.facebook.imagepipeline.request.ImageRequestBuilder;
import com.facebook.imagepipeline.request.Postprocessor;

/**
 * Created by baixuefei on 18/2/11.
 */

public class ArrowSimpleDraweeView extends SimpleDraweeView {

    private static final String TAG = ArrowSimpleDraweeView.class.getSimpleName();
    private float mCornerRadius ;
    private float mArrowTop ;//尖角纵向顶部到矩形登录的距离.
    private float mArrowWidth ;//尖角的横向宽度.
    private float mArrowHeight ;//尖角的纵向高度
    private float mArrowOffset ;

    private int mArrowLocation = 0;
    private static final int LOCATION_LEFT = 0;
    private static final int LOCATION_RIGHT = 1;


    private Context mContext;
    public ArrowSimpleDraweeView(Context context) {
        super(context);
        mContext = context;
        intiView(null);
    }

    public ArrowSimpleDraweeView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        intiView(attrs);
    }

    public ArrowSimpleDraweeView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
        intiView(attrs);
    }

    public void intiView(AttributeSet attrs){


        mCornerRadius = DensityUtils.dip2px(mContext,10);
        mArrowTop =DensityUtils.dip2px(mContext,40);//尖角纵向顶部到矩形登录的距离.
        mArrowWidth = DensityUtils.dip2px(mContext,10);//尖角的横向宽度.
        mArrowHeight = DensityUtils.dip2px(mContext,20);//尖角的纵向高度
        mArrowOffset = -DensityUtils.dip2px(mContext,10);

        if(attrs != null){
            TypedArray a = getContext().obtainStyledAttributes(attrs, R.styleable.ArrowSimpleDraweeView);
            mCornerRadius = a.getDimension(R.styleable.ArrowSimpleDraweeView_aCornerRadius,mCornerRadius);
            mArrowTop = a.getDimension(R.styleable.ArrowSimpleDraweeView_arrowTop,mArrowTop);
            float tmp = a.getDimension(R.styleable.ArrowSimpleDraweeView_arrowWidth,mArrowWidth);
            mArrowWidth = tmp;
            //LogUtils.d(TAG,"mArrowWidth:"+tmp+",DensityUtils.dip2px(10):"+DensityUtils.dip2px(10));
            mArrowHeight = a.getDimension(R.styleable.ArrowSimpleDraweeView_arrowHeihgt,mArrowHeight);
            mArrowOffset = a.getDimension(R.styleable.ArrowSimpleDraweeView_arrowOffsety,mArrowOffset);
            mArrowLocation = a.getInt(R.styleable.ArrowSimpleDraweeView_arrowLocation,mArrowLocation);
            a.recycle();
        }
    }

    public void showImage(final Uri uri, final int imageSuitableWidth, final int imageSuitableHeight){

//        Postprocessor redMeshPostPorcessor = new BasePostprocessor() {
//            @Override
//            public void process(Bitmap destBitmap, Bitmap sourceBitmap) {
//
//                Canvas canvas = new Canvas(destBitmap);
//                int color = 0xff424242;// int color = 0xff424242;
//                Paint paint = new Paint();
//                paint.setColor(color);
//                // 防止锯齿
//                paint.setAntiAlias(true);
//
//                Rect rect = new Rect(0,0,sourceBitmap.getWidth(),sourceBitmap.getHeight());
//
//                RectF rectF = new RectF(rect);
//
//                Path path = new Path();
//                if(mArrowLocation == LOCATION_LEFT){
//                    leftPath(rectF,path);
//                }else {
//                    rightPath(rectF,path);
//                }
//                canvas.drawARGB(0,0,0,0);
//                canvas.drawPath(path,paint);
//                paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
//                canvas.drawBitmap(sourceBitmap,rect,rect,paint);
//
//                LogUtils.d(TAG,"test size - redMeshPostPorcessor.width:"+sourceBitmap.getWidth()+",height:"+sourceBitmap.getHeight()+",destBitmap.width:"+destBitmap.getWidth()+",destBitMap.height:"+destBitmap.getHeight()+",imageSuitableWidth:"+imageSuitableWidth+",imageSuitableHeight:"+imageSuitableHeight+",mArrowWidth:"+mArrowWidth+",location:"+mArrowLocation+",uri:"+uri);
//            }
//        };

        Postprocessor redMeshPostPorcessor = new BasePostprocessor() {
            @Override
            public CloseableReference<Bitmap> process(
                    Bitmap sourceBitmap,
                    PlatformBitmapFactory bitmapFactory) {

                //创建一个安全的 新的bitmap
                CloseableReference<Bitmap> bitmapRef = bitmapFactory.createBitmap(
                        imageSuitableWidth,
                        imageSuitableHeight);
                try {
                    Bitmap destBitmap = bitmapRef.get();

                    Canvas canvas = new Canvas(destBitmap);
                    int color = 0xff424242;// int color = 0xff424242;
                    Paint paint = new Paint();
                    paint.setColor(color);
                    // 防止锯齿
                    paint.setAntiAlias(true);

                    Rect rect = new Rect(0,0,imageSuitableWidth,imageSuitableHeight);

                    RectF rectF = new RectF(rect);

                    Path path = new Path();
                    if(mArrowLocation == LOCATION_LEFT){
                        leftPath(rectF,path);
                    }else {
                        rightPath(rectF,path);
                    }
                    canvas.drawARGB(0,0,0,0);
                    canvas.drawPath(path,paint);
                    paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
                    float scale = 1.0f;

                    float scaleWidth = (float) (imageSuitableWidth*1.0/sourceBitmap.getWidth());
                    float scaleHeight = (float) (imageSuitableHeight*1.0/sourceBitmap.getHeight());
                    scale = Math.max(scaleWidth,scaleHeight);
//                    canvas.drawBitmap(sourceBitmap,rect,rect,paint);
                    Matrix matrix = new Matrix();
                    matrix.postScale(scale,scale);
                    //LogUtils.d(TAG,"scaleWidth:"+scaleWidth+",scaleHeight:"+scaleHeight+",scale:"+scale);
                    //LogUtils.d(TAG,"test size - redMeshPostPorcessor.width:"+sourceBitmap.getWidth()+",height:"+sourceBitmap.getHeight()+",destBitmap.width:"+destBitmap.getWidth()+",destBitMap.height:"+destBitmap.getHeight()+",imageSuitableWidth:"+imageSuitableWidth+",imageSuitableHeight:"+imageSuitableHeight+",mArrowWidth:"+mArrowWidth+",location:"+mArrowLocation+",uri:"+uri);
                    canvas.drawBitmap(sourceBitmap,matrix,paint);
                    return CloseableReference.cloneOrNull(bitmapRef);
                } finally {
                    CloseableReference.closeSafely(bitmapRef);
                }
            }
        };


        //LogUtils.d(TAG,"test size - imageSuitableWidth:"+imageSuitableWidth+",imageSuitableHeight:"+imageSuitableHeight);
        ImageRequest request = ImageRequestBuilder.newBuilderWithSource(uri)
                .setResizeOptions(new ResizeOptions(imageSuitableWidth, imageSuitableHeight))
                .setPostprocessor(redMeshPostPorcessor)
                .build();

        ViewGroup.LayoutParams params = getLayoutParams();
        params.width = imageSuitableWidth;
        params.height = imageSuitableHeight;
        DraweeController controller = Fresco.newDraweeControllerBuilder()
                .setOldController(getController())
                .setImageRequest(request)
                .build();
        setController(controller);
        setLayoutParams(params);

    }


    /**
     * 绘制尖角在左侧的path
     *
     *         A
     *         * * * * * * * * * * * * *
     *       *                 *         *
     *     *                   *           *
     *     *  F                *     B      *
     *     *                   *            *
     *     * mArrowTop         * * * * * * **
     *   *                                  *
     *  *                                   *
     *   *                                  *
     *     *                                *
     *     *                                * C
     *     *                                *
     *     *                                *
     *     *  E                             *
     *     *                                *
     *     *                                *
     *     *                                *
     *     *                                *
     *     *                               *
     *      *                             *
     *        * * * * * * * * * * * * * *
     *                     D
     *
     * @param rect
     * @param path  如上图所示 从A点开始 顺时针绘制path路径.
     */
    public void leftPath(RectF rect, Path path) {
        path.moveTo(mCornerRadius + mArrowWidth, rect.top);//移动到A点
        path.lineTo(rect.width(), rect.top);//顶部横线
        path.arcTo(new RectF(rect.right - mCornerRadius * 2, rect.top, rect.right,
                mCornerRadius * 2 + rect.top), 270, 90);//绘制 右上角的90度的圆弧. (B对应的区域)
        path.lineTo(rect.right, rect.top);//绘制 右侧直线
        path.arcTo(new RectF(rect.right - mCornerRadius * 2, rect.bottom - mCornerRadius * 2,
                rect.right, rect.bottom), 0, 90);//右下角圆弧
        path.lineTo(rect.left + mArrowWidth, rect.bottom);//底部横线 (D对应的横线)
        path.arcTo(new RectF(rect.left + mArrowWidth, rect.bottom - mCornerRadius * 2,
                mCornerRadius * 2 + rect.left + mArrowWidth, rect.bottom), 90, 90);//左下角圆弧
        path.lineTo(rect.left + mArrowWidth, mArrowTop + mArrowHeight);//左侧偏下部竖线(E所示竖线)
        path.lineTo(rect.left, mArrowTop - mArrowOffset);//左侧凸起尖角 下半部分斜线
        path.lineTo(rect.left + mArrowWidth, mArrowTop); //左侧凸起尖角 上半部分斜线
        path.lineTo(rect.left + mArrowWidth, rect.top);//左侧片上部竖线(F所示竖线)
        path.arcTo(new RectF(rect.left + mArrowWidth, rect.top, mCornerRadius * 2
                + rect.left + mArrowWidth, mCornerRadius * 2 + rect.top), 180, 90);//左上角圆弧

        path.close();
    }

    /**
     *  绘制 尖角在右侧 的path
     * @param rect
     * @param path
     */
    public void rightPath(RectF rect, Path path) {
        path.moveTo(mCornerRadius, rect.top);
        path.lineTo(rect.width(), rect.top);
        path.arcTo(new RectF(rect.right - mCornerRadius * 2 - mArrowWidth, rect.top,
                rect.right - mArrowWidth, mCornerRadius * 2 + rect.top), 270, 90);
        path.lineTo(rect.right - mArrowWidth, mArrowTop);
        path.lineTo(rect.right, mArrowTop - mArrowOffset);
        path.lineTo(rect.right - mArrowWidth, mArrowTop + mArrowHeight);
        path.lineTo(rect.right - mArrowWidth, rect.height() - mCornerRadius);
        path.arcTo(new RectF(rect.right - mCornerRadius * 2 - mArrowWidth, rect.bottom
                - mCornerRadius * 2, rect.right - mArrowWidth, rect.bottom), 0, 90);
        path.lineTo(rect.left, rect.bottom);
        path.arcTo(new RectF(rect.left, rect.bottom - mCornerRadius * 2, mCornerRadius * 2
                + rect.left, rect.bottom), 90, 90);
        path.lineTo(rect.left, rect.top);
        path.arcTo(new RectF(rect.left, rect.top, mCornerRadius * 2 + rect.left,
                mCornerRadius * 2 + rect.top), 180, 90);
        path.close();
    }


    public float getmCornerRadius() {
        return mCornerRadius;
    }

    public void setmCornerRadius(float mCornerRadius) {
        this.mCornerRadius = mCornerRadius;
    }

    public float getmArrowTop() {
        return mArrowTop;
    }

    public void setmArrowTop(float mArrowTop) {
        this.mArrowTop = mArrowTop;
    }

    public float getmArrowWidth() {
        return mArrowWidth;
    }

    public void setmArrowWidth(float mArrowWidth) {
        this.mArrowWidth = mArrowWidth;
    }

    public float getmArrowHeight() {
        return mArrowHeight;
    }

    public void setmArrowHeight(float mArrowHeight) {
        this.mArrowHeight = mArrowHeight;
    }

    public float getmArrowOffset() {
        return mArrowOffset;
    }

    public void setmArrowOffset(float mArrowOffset) {
        this.mArrowOffset = mArrowOffset;
    }

    public int getmArrowLocation() {
        return mArrowLocation;
    }

    public void setmArrowLocation(int mArrowLocation) {
        this.mArrowLocation = mArrowLocation;
    }
}
