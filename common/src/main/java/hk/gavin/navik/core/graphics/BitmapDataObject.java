package hk.gavin.navik.core.graphics;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

public class BitmapDataObject implements Serializable {

    private Bitmap mBitmap;

    public BitmapDataObject(Bitmap bitmap) {
        this.mBitmap = bitmap;
    }

    public Bitmap getBitmap() {
        return mBitmap;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        mBitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

        byte[] byteArray = stream.toByteArray();
        out.writeInt(byteArray.length);
        out.write(byteArray);
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        int bufferLength = in.readInt();
        byte[] byteArray = new byte[bufferLength];

        int pos = 0;
        do {
            int read = in.read(byteArray, pos, bufferLength - pos);

            if (read != -1) {
                pos += read;
            }
            else {
                break;
            }

        } while (pos < bufferLength);

        mBitmap = BitmapFactory.decodeByteArray(byteArray, 0, bufferLength);
    }
}
