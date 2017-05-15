package camera;

import android.media.Image;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

/**
 * Created by exjobb on 2017-04-11.
 */

public class ImageSaver implements Runnable {

    private Image image;
    private File file;
    private FileOutputStream fos;

    public ImageSaver(Image image, File file){
        this.image = image;
        this.file = file;
    }

    /**
     *  Takes this class' image, converts it to a byte buffer and saves the buffer to this
     *  class' file
     */
    @Override
    public void run() {
        ByteBuffer buffer = image.getPlanes()[0].getBuffer();
        byte[] bytes = new byte[buffer.remaining()];
        try {
            if(!file.exists())
                file.createNewFile();
            buffer.get(bytes);
            fos = new FileOutputStream(file);
            fos.write(bytes);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally{
            //  Cleanup
            image.close();
            try {
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
