/*
 * Decompiled with CFR 0_92.
 * 
 * Could not load the following classes:
 *  android.graphics.Bitmap
 *  android.graphics.drawable.Drawable
 *  android.widget.ImageView
 *  java.lang.Object
 *  java.lang.String
 *  java.util.ArrayList
 *  java.util.Iterator
 */
package com.sonyericsson.android.camera.util;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.widget.ImageView;
import java.util.ArrayList;
import java.util.Iterator;

public final class BitmapImageManager {
    private static final String TAG = "BitmapImageManager";
    private ArrayList<BitmapItem> mThumbnails = new ArrayList();

    public BitmapImageManager() {
        this.mThumbnails.clear();
    }

    private void dump() {
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private BitmapItem getItem(Bitmap bitmap) {
        if (bitmap == null || bitmap.isRecycled()) return null;
        for (BitmapItem bitmapItem : this.mThumbnails) {
            if (!bitmapItem.equal(bitmap)) continue;
            return bitmapItem;
        }
        return null;
    }

    /*
     * Enabled force condition propagation
     * Lifted jumps to return sites
     */
    private BitmapItem getItem(ImageView imageView) {
        if (imageView == null) return null;
        for (BitmapItem bitmapItem : this.mThumbnails) {
            if (!bitmapItem.contains(imageView)) continue;
            return bitmapItem;
        }
        return null;
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void add(Bitmap bitmap) {
        ArrayList<BitmapItem> arrayList;
        if (bitmap == null) {
            return;
        }
        ArrayList<BitmapItem> arrayList2 = arrayList = this.mThumbnails;
        synchronized (arrayList2) {
            BitmapItem bitmapItem = super.getItem(bitmap);
            if (bitmapItem != null) {
                bitmapItem.add();
            } else {
                BitmapItem bitmapItem2 = new BitmapItem((BitmapImageManager)this, bitmap);
                this.mThumbnails.add((Object)bitmapItem2);
            }
            super.dump();
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void add(Bitmap bitmap, ImageView imageView) {
        ArrayList<BitmapItem> arrayList;
        if (bitmap == null) {
            return;
        }
        ArrayList<BitmapItem> arrayList2 = arrayList = this.mThumbnails;
        synchronized (arrayList2) {
            BitmapItem bitmapItem;
            BitmapItem bitmapItem2 = super.getItem(imageView);
            if (bitmapItem2 != null) {
                bitmapItem2.remove(imageView);
            }
            if ((bitmapItem = super.getItem(bitmap)) != null) {
                bitmapItem.add(imageView);
            } else {
                BitmapItem bitmapItem3 = new BitmapItem((BitmapImageManager)this, bitmap, imageView);
                this.mThumbnails.add((Object)bitmapItem3);
            }
            super.dump();
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void clear() {
        ArrayList<BitmapItem> arrayList;
        ArrayList<BitmapItem> arrayList2 = arrayList = this.mThumbnails;
        synchronized (arrayList2) {
            Iterator iterator = this.mThumbnails.iterator();
            while (iterator.hasNext()) {
                ((BitmapItem)iterator.next()).init();
                iterator.remove();
            }
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void remove(Bitmap bitmap) {
        ArrayList<BitmapItem> arrayList;
        if (bitmap == null) {
            return;
        }
        ArrayList<BitmapItem> arrayList2 = arrayList = this.mThumbnails;
        synchronized (arrayList2) {
            BitmapItem bitmapItem = super.getItem(bitmap);
            if (bitmapItem != null) {
                bitmapItem.subtract();
            }
            super.dump();
            return;
        }
    }

    /*
     * Enabled aggressive block sorting
     * Enabled unnecessary exception pruning
     */
    public void remove(ImageView imageView) {
        ArrayList<BitmapItem> arrayList;
        ArrayList<BitmapItem> arrayList2 = arrayList = this.mThumbnails;
        synchronized (arrayList2) {
            BitmapItem bitmapItem = super.getItem(imageView);
            if (bitmapItem != null) {
                bitmapItem.remove(imageView);
                imageView.setImageDrawable(null);
            }
            super.dump();
            return;
        }
    }

    private class BitmapItem {
        private static final String TAG = "BitmapItem";
        private Bitmap mImage;
        private int mRefCount;
        private ArrayList<ImageView> mViews;
        final /* synthetic */ BitmapImageManager this$0;

        public BitmapItem(BitmapImageManager bitmapImageManager, Bitmap bitmap) {
            this.this$0 = bitmapImageManager;
            this.mViews = new ArrayList();
            this.mImage = bitmap;
            this.mRefCount = 1;
            this.mViews.clear();
        }

        public BitmapItem(BitmapImageManager bitmapImageManager, Bitmap bitmap, ImageView imageView) {
            super(bitmapImageManager, bitmap);
            this.mViews.add((Object)imageView);
        }

        private void clear() {
            BitmapItem bitmapItem = this;
            synchronized (bitmapItem) {
                this.init();
                this.this$0.mThumbnails.remove((Object)this);
                return;
            }
        }

        public void add() {
            BitmapItem bitmapItem = this;
            synchronized (bitmapItem) {
                if (this.mRefCount < Integer.MAX_VALUE) {
                    this.mRefCount = 1 + this.mRefCount;
                }
                return;
            }
        }

        public void add(ImageView imageView) {
            void var4_2 = this;
            synchronized (var4_2) {
                this.add();
                this.mViews.add((Object)imageView);
                return;
            }
        }

        public boolean contains(ImageView imageView) {
            void var4_2 = this;
            synchronized (var4_2) {
                boolean bl = this.mViews.contains((Object)imageView);
                return bl;
            }
        }

        public void dump() {
            BitmapItem bitmapItem = this;
            synchronized (bitmapItem) {
                boolean bl;
                Iterator iterator = this.mViews.iterator();
                while (!(bl = iterator.hasNext())) {
                }
                return;
            }
        }

        public boolean equal(Bitmap bitmap) {
            if (this.mImage == bitmap) {
                return true;
            }
            return false;
        }

        public void init() {
            BitmapItem bitmapItem = this;
            synchronized (bitmapItem) {
                if (!this.mImage.isRecycled()) {
                    this.mImage.recycle();
                }
                this.mImage = null;
                this.mRefCount = 0;
                return;
            }
        }

        public void remove(ImageView imageView) {
            void var4_2 = this;
            synchronized (var4_2) {
                if (this.mViews.contains((Object)imageView)) {
                    this.mViews.remove((Object)imageView);
                    this.subtract();
                }
                return;
            }
        }

        /*
         * Enabled aggressive block sorting
         * Enabled unnecessary exception pruning
         */
        public void subtract() {
            BitmapItem bitmapItem = this;
            synchronized (bitmapItem) {
                if (this.mRefCount > 1) {
                    this.mRefCount = -1 + this.mRefCount;
                } else if (this.mRefCount != 0) {
                    this.clear();
                }
                return;
            }
        }
    }

}

